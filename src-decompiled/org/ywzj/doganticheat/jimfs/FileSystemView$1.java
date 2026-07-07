package org.ywzj.doganticheat.jimfs;

import java.util.Set;

class FileSystemView$1 implements FileLookup {
   FileSystemView$1(FileSystemView var1, JimfsPath var2, Set var3) {
      this.this$0 = var1;
      this.val$path = var2;
      this.val$options = var3;
   }

   @Override
   public File lookup() {
      return this.this$0.lookUpWithLock(this.val$path, this.val$options).requireExists(this.val$path).file();
   }
}
