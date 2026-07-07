package org.ywzj.doganticheat.jimfs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.Watchable;
import java.nio.file.WatchEvent.Kind;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.checkerframework.checker.nullness.qual.Nullable;

final class AbstractWatchService$Key implements WatchKey {
   @VisibleForTesting
   static final int MAX_QUEUE_SIZE = 256;
   private final AbstractWatchService watcher;
   private final Watchable watchable;
   private final ImmutableSet subscribedTypes;
   private final AtomicReference state = new AtomicReference<>(AbstractWatchService$Key$State.READY);
   private final AtomicBoolean valid = new AtomicBoolean(true);
   private final AtomicInteger overflow = new AtomicInteger();
   private final BlockingQueue events = new ArrayBlockingQueue(256);

   private static WatchEvent overflowEvent(int var0) {
      return new AbstractWatchService$Event(StandardWatchEventKinds.OVERFLOW, var0, null);
   }

   public AbstractWatchService$Key(AbstractWatchService var1, @Nullable Watchable var2, Iterable var3) {
      this.watcher = (AbstractWatchService)Preconditions.checkNotNull(var1);
      this.watchable = var2;
      this.subscribedTypes = ImmutableSet.copyOf(var3);
   }

   @VisibleForTesting
   AbstractWatchService$Key$State state() {
      return (AbstractWatchService$Key$State)this.state.get();
   }

   public boolean subscribesTo(Kind var1) {
      return this.subscribedTypes.contains(var1);
   }

   public void post(WatchEvent var1) {
      if (!this.events.offer(var1)) {
         this.overflow.incrementAndGet();
      }
   }

   public void signal() {
      if (this.state.getAndSet(AbstractWatchService$Key$State.SIGNALLED) == AbstractWatchService$Key$State.READY) {
         this.watcher.enqueue(this);
      }
   }

   @Override
   public boolean isValid() {
      return this.watcher.isOpen() && this.valid.get();
   }

   @Override
   public List pollEvents() {
      ArrayList var1 = new ArrayList(this.events.size());
      this.events.drainTo(var1);
      int var2 = this.overflow.getAndSet(0);
      if (var2 != 0) {
         var1.add(overflowEvent(var2));
      }

      return Collections.unmodifiableList(var1);
   }

   @CanIgnoreReturnValue
   @Override
   public boolean reset() {
      if (this.isValid() && this.state.compareAndSet(AbstractWatchService$Key$State.SIGNALLED, AbstractWatchService$Key$State.READY) && !this.events.isEmpty()) {
         this.signal();
      }

      return this.isValid();
   }

   @Override
   public void cancel() {
      this.valid.set(false);
      this.watcher.cancelled(this);
   }

   @Override
   public Watchable watchable() {
      return this.watchable;
   }
}
