package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

final class FileFactory$SymbolicLinkSupplier implements Supplier {
   private final JimfsPath target;

   protected FileFactory$SymbolicLinkSupplier(FileFactory var1, JimfsPath var2) {
      this.this$0 = var1;
      this.target = (JimfsPath)Preconditions.checkNotNull(var2);
   }

   public SymbolicLink get() {
      return this.this$0.createSymbolicLink(this.target);
   }
}
