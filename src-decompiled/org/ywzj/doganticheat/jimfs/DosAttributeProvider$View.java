package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileTime;

final class DosAttributeProvider$View extends AbstractAttributeView implements DosFileAttributeView {
   private final BasicFileAttributeView basicView;

   public DosAttributeProvider$View(FileLookup var1, BasicFileAttributeView var2) {
      super(var1);
      this.basicView = (BasicFileAttributeView)Preconditions.checkNotNull(var2);
   }

   @Override
   public String name() {
      return "dos";
   }

   @Override
   public DosFileAttributes readAttributes() {
      return new DosAttributeProvider$Attributes(this.lookupFile());
   }

   @Override
   public void setTimes(FileTime var1, FileTime var2, FileTime var3) {
      this.basicView.setTimes(var1, var2, var3);
   }

   @Override
   public void setReadOnly(boolean var1) {
      this.lookupFile().setAttribute("dos", "readonly", var1);
   }

   @Override
   public void setHidden(boolean var1) {
      this.lookupFile().setAttribute("dos", "hidden", var1);
   }

   @Override
   public void setSystem(boolean var1) {
      this.lookupFile().setAttribute("dos", "system", var1);
   }

   @Override
   public void setArchive(boolean var1) {
      this.lookupFile().setAttribute("dos", "archive", var1);
   }
}
