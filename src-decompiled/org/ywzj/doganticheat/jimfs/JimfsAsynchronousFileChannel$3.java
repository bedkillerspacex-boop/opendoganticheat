package org.ywzj.doganticheat.jimfs;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

class JimfsAsynchronousFileChannel$3 implements Callable {
   JimfsAsynchronousFileChannel$3(JimfsAsynchronousFileChannel var1, ByteBuffer var2, long var3) {
      this.this$0 = var1;
      this.val$src = var2;
      this.val$position = var3;
   }

   public Integer call() {
      return JimfsAsynchronousFileChannel.access$100(this.this$0).write(this.val$src, this.val$position);
   }
}
