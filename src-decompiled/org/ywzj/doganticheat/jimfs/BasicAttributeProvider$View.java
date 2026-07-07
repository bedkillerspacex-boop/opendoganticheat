package org.ywzj.doganticheat.jimfs;

import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import org.checkerframework.checker.nullness.qual.Nullable;

final class BasicAttributeProvider$View extends AbstractAttributeView implements BasicFileAttributeView {
   protected BasicAttributeProvider$View(FileLookup var1) {
      super(var1);
   }

   @Override
   public String name() {
      return "basic";
   }

   @Override
   public BasicFileAttributes readAttributes() {
      return new BasicAttributeProvider$Attributes(this.lookupFile());
   }

   @Override
   public void setTimes(@Nullable FileTime var1, @Nullable FileTime var2, @Nullable FileTime var3) {
      File var4 = this.lookupFile();
      if (var1 != null) {
         var4.setLastModifiedTime(var1);
      }

      if (var2 != null) {
         var4.setLastAccessTime(var2);
      }

      if (var3 != null) {
         var4.setCreationTime(var3);
      }
   }
}
