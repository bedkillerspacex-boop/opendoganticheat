package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Supplier;

final class FileFactory$RegularFileSupplier implements Supplier {
   private FileFactory$RegularFileSupplier(FileFactory var1) {
      this.this$0 = var1;
   }

   public RegularFile get() {
      return this.this$0.createRegularFile();
   }
}
