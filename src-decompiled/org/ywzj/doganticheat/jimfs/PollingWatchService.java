package org.ywzj.doganticheat.jimfs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.file.Path;
import java.nio.file.Watchable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

final class PollingWatchService extends AbstractWatchService {
   private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder()
      .setNameFormat("org.ywzj.doganticheat.jimfs.PollingWatchService-thread-%d")
      .setDaemon(true)
      .build();
   private final ScheduledExecutorService pollingService = Executors.newSingleThreadScheduledExecutor(THREAD_FACTORY);
   private final ConcurrentMap snapshots = new ConcurrentHashMap();
   private final FileSystemView view;
   private final PathService pathService;
   private final FileSystemState fileSystemState;
   @VisibleForTesting
   final long interval;
   @VisibleForTesting
   final TimeUnit timeUnit;
   private ScheduledFuture pollingFuture;
   private final Runnable pollingTask = new PollingWatchService$1(this);

   PollingWatchService(FileSystemView var1, PathService var2, FileSystemState var3, long var4, TimeUnit var6) {
      this.view = (FileSystemView)Preconditions.checkNotNull(var1);
      this.pathService = (PathService)Preconditions.checkNotNull(var2);
      this.fileSystemState = (FileSystemState)Preconditions.checkNotNull(var3);
      Preconditions.checkArgument(var4 >= 0L, "interval (%s) may not be negative", var4);
      this.interval = var4;
      this.timeUnit = (TimeUnit)Preconditions.checkNotNull(var6);
      var3.register(this);
   }

   @CanIgnoreReturnValue
   @Override
   public AbstractWatchService$Key register(Watchable var1, Iterable var2) {
      JimfsPath var3 = this.checkWatchable(var1);
      AbstractWatchService$Key var4 = super.register(var3, var2);
      PollingWatchService$Snapshot var5 = this.takeSnapshot(var3);
      synchronized (this) {
         this.snapshots.put(var4, var5);
         if (this.pollingFuture == null) {
            this.startPolling();
         }

         return var4;
      }
   }

   private JimfsPath checkWatchable(Watchable var1) {
      if (var1 instanceof JimfsPath && this.isSameFileSystem((Path)var1)) {
         return (JimfsPath)var1;
      } else {
         throw new IllegalArgumentException("watchable (" + var1 + ") must be a Path associated with the same file system as this watch service");
      }
   }

   private boolean isSameFileSystem(Path var1) {
      return ((JimfsFileSystem)var1.getFileSystem()).getDefaultView() == this.view;
   }

   @VisibleForTesting
   synchronized boolean isPolling() {
      return this.pollingFuture != null;
   }

   @Override
   public synchronized void cancelled(AbstractWatchService$Key var1) {
      this.snapshots.remove(var1);
      if (this.snapshots.isEmpty()) {
         this.stopPolling();
      }
   }

   @Override
   public void close() {
      super.close();
      synchronized (this) {
         for (AbstractWatchService$Key var3 : this.snapshots.keySet()) {
            var3.cancel();
         }

         this.pollingService.shutdown();
         this.fileSystemState.unregister(this);
      }
   }

   private void startPolling() {
      this.pollingFuture = this.pollingService.scheduleAtFixedRate(this.pollingTask, this.interval, this.interval, this.timeUnit);
   }

   private void stopPolling() {
      this.pollingFuture.cancel(false);
      this.pollingFuture = null;
   }

   private PollingWatchService$Snapshot takeSnapshot(JimfsPath var1) {
      return new PollingWatchService$Snapshot(this, this.view.snapshotModifiedTimes(var1));
   }
}
