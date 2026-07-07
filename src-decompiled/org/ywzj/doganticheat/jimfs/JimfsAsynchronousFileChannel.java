package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileLock;
import java.util.concurrent.ExecutorService;
import org.checkerframework.checker.nullness.qual.Nullable;

final class JimfsAsynchronousFileChannel extends AsynchronousFileChannel {
   private final JimfsFileChannel channel;
   private final ListeningExecutorService executor;

   public JimfsAsynchronousFileChannel(JimfsFileChannel var1, ExecutorService var2) {
      this.channel = (JimfsFileChannel)Preconditions.checkNotNull(var1);
      this.executor = MoreExecutors.listeningDecorator(var2);
   }

   @Override
   public long size() {
      return this.channel.size();
   }

   private void addCallback(ListenableFuture var1, CompletionHandler var2, @Nullable Object var3) {
      var1.addListener(new JimfsAsynchronousFileChannel$CompletionHandlerCallback(var1, var2, var3, null), this.executor);
   }

   @CanIgnoreReturnValue
   @Override
   public AsynchronousFileChannel truncate(long var1) {
      this.channel.truncate(var1);
      return this;
   }

   @Override
   public void force(boolean var1) {
      this.channel.force(var1);
   }

   @Override
   public void lock(long var1, long var3, boolean var5, @Nullable Object var6, CompletionHandler var7) {
      Preconditions.checkNotNull(var7);
      this.addCallback(this.lock(var1, var3, var5), var7, var6);
   }

   public ListenableFuture lock(long var1, long var3, boolean var5) {
      Util.checkNotNegative(var1, "position");
      Util.checkNotNegative(var3, "size");
      if (!this.isOpen()) {
         return closedChannelFuture();
      }

      if (var5) {
         this.channel.checkReadable();
      } else {
         this.channel.checkWritable();
      }

      return this.executor.submit(new JimfsAsynchronousFileChannel$1(this, var1, var3, var5));
   }

   @Override
   public FileLock tryLock(long var1, long var3, boolean var5) {
      Util.checkNotNegative(var1, "position");
      Util.checkNotNegative(var3, "size");
      this.channel.checkOpen();
      if (var5) {
         this.channel.checkReadable();
      } else {
         this.channel.checkWritable();
      }

      return new JimfsFileChannel$FakeFileLock(this, var1, var3, var5);
   }

   @Override
   public void read(ByteBuffer var1, long var2, @Nullable Object var4, CompletionHandler var5) {
      this.addCallback(this.read(var1, var2), var5, var4);
   }

   public ListenableFuture read(ByteBuffer var1, long var2) {
      Preconditions.checkArgument(!var1.isReadOnly(), "dst may not be read-only");
      Util.checkNotNegative(var2, "position");
      if (!this.isOpen()) {
         return closedChannelFuture();
      }

      this.channel.checkReadable();
      return this.executor.submit(new JimfsAsynchronousFileChannel$2(this, var1, var2));
   }

   @Override
   public void write(ByteBuffer var1, long var2, @Nullable Object var4, CompletionHandler var5) {
      this.addCallback(this.write(var1, var2), var5, var4);
   }

   public ListenableFuture write(ByteBuffer var1, long var2) {
      Util.checkNotNegative(var2, "position");
      if (!this.isOpen()) {
         return closedChannelFuture();
      }

      this.channel.checkWritable();
      return this.executor.submit(new JimfsAsynchronousFileChannel$3(this, var1, var2));
   }

   @Override
   public boolean isOpen() {
      return this.channel.isOpen();
   }

   @Override
   public void close() {
      this.channel.close();
   }

   private static ListenableFuture closedChannelFuture() {
      SettableFuture var0 = SettableFuture.create();
      var0.setException(new ClosedChannelException());
      return var0;
   }
}
