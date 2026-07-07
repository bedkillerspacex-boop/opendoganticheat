package org.ywzj.doganticheat.jimfs;

import java.io.Closeable;

class JimfsFileSystem$1 implements Closeable {
   JimfsFileSystem$1(JimfsFileSystem var1) {
      this.this$0 = var1;
   }

   @Override
   public void close() {
      JimfsFileSystem.access$000(this.this$0).shutdown();
   }
}
