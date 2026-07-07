package org.ywzj.doganticheat.jimfs;

import java.nio.channels.FileLock;
import java.util.concurrent.Callable;

class JimfsAsynchronousFileChannel$1 implements Callable {
   JimfsAsynchronousFileChannel$1(JimfsAsynchronousFileChannel var1, long var2, long var4, boolean var6) {
      this.this$0 = var1;
      this.val$position = var2;
      this.val$size = var4;
      this.val$shared = var6;
   }

   public FileLock call() {
      return this.this$0.tryLock(this.val$position, this.val$size, this.val$shared);
   }
}
