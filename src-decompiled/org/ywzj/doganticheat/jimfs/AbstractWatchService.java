package org.ywzj.doganticheat.jimfs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.Watchable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.checkerframework.checker.nullness.qual.Nullable;

abstract class AbstractWatchService implements WatchService {
   private final BlockingQueue queue = new LinkedBlockingQueue();
   private final WatchKey poison = new AbstractWatchService$Key(this, null, ImmutableSet.of());
   private final AtomicBoolean open = new AtomicBoolean(true);

   public AbstractWatchService$Key register(Watchable var1, Iterable var2) {
      this.checkOpen();
      return new AbstractWatchService$Key(this, var1, var2);
   }

   @VisibleForTesting
   public boolean isOpen() {
      return this.open.get();
   }

   final void enqueue(AbstractWatchService$Key var1) {
      if (this.isOpen()) {
         this.queue.add(var1);
      }
   }

   public void cancelled(AbstractWatchService$Key var1) {
   }

   @VisibleForTesting
   ImmutableList queuedKeys() {
      return ImmutableList.copyOf(this.queue);
   }

   @Override
   public @Nullable WatchKey poll() {
      this.checkOpen();
      return this.check((WatchKey)this.queue.poll());
   }

   @Override
   public @Nullable WatchKey poll(long var1, TimeUnit var3) {
      this.checkOpen();
      return this.check((WatchKey)this.queue.poll(var1, var3));
   }

   @Override
   public WatchKey take() {
      this.checkOpen();
      return this.check((WatchKey)this.queue.take());
   }

   private @Nullable WatchKey check(@Nullable WatchKey var1) {
      if (var1 == this.poison) {
         this.queue.offer(this.poison);
         throw new ClosedWatchServiceException();
      } else {
         return var1;
      }
   }

   protected final void checkOpen() {
      if (!this.open.get()) {
         throw new ClosedWatchServiceException();
      }
   }

   @Override
   public void close() {
      if (this.open.compareAndSet(true, false)) {
         this.queue.clear();
         this.queue.offer(this.poison);
      }
   }
}
