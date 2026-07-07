package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Supplier;

final class FileFactory$DirectorySupplier implements Supplier {
   private FileFactory$DirectorySupplier(FileFactory var1) {
      this.this$0 = var1;
   }

   public Directory get() {
      return this.this$0.createDirectory();
   }
}
