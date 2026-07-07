package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import org.checkerframework.checker.nullness.qual.Nullable;

final class JimfsAsynchronousFileChannel$CompletionHandlerCallback implements Runnable {
   private final ListenableFuture future;
   private final CompletionHandler completionHandler;
   private final @Nullable Object attachment;

   private JimfsAsynchronousFileChannel$CompletionHandlerCallback(ListenableFuture var1, CompletionHandler var2, @Nullable Object var3) {
      this.future = (ListenableFuture)Preconditions.checkNotNull(var1);
      this.completionHandler = (CompletionHandler)Preconditions.checkNotNull(var2);
      this.attachment = var3;
   }

   @Override
   public void run() {
      Object var1;
      try {
         var1 = Futures.getDone(this.future);
      } catch (ExecutionException var3) {
         this.onFailure(var3.getCause());
         return;
      } catch (RuntimeException | Error var4) {
         this.onFailure(var4);
         return;
      }

      this.onSuccess(var1);
   }

   private void onSuccess(Object var1) {
      this.completionHandler.completed(var1, this.attachment);
   }

   private void onFailure(Throwable var1) {
      this.completionHandler.failed(var1, this.attachment);
   }
}
