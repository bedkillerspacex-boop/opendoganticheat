package org.ywzj.doganticheat.jimfs;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

class JimfsAsynchronousFileChannel$2 implements Callable {
   JimfsAsynchronousFileChannel$2(JimfsAsynchronousFileChannel var1, ByteBuffer var2, long var3) {
      this.this$0 = var1;
      this.val$dst = var2;
      this.val$position = var3;
   }

   public Integer call() {
      return JimfsAsynchronousFileChannel.access$100(this.this$0).read(this.val$dst, this.val$position);
   }
}
