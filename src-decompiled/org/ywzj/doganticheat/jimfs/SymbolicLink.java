package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import java.nio.file.attribute.FileTime;

final class SymbolicLink extends File {
   private final JimfsPath target;

   public static SymbolicLink create(int var0, FileTime var1, JimfsPath var2) {
      return new SymbolicLink(var0, var1, var2);
   }

   private SymbolicLink(int var1, FileTime var2, JimfsPath var3) {
      super(var1, var2);
      this.target = (JimfsPath)Preconditions.checkNotNull(var3);
   }

   JimfsPath target() {
      return this.target;
   }

   @Override
   File copyWithoutContent(int var1, FileTime var2) {
      return create(var1, var2, this.target);
   }
}
