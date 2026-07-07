package org.ywzj.doganticheat.jimfs;

import java.nio.file.attribute.DosFileAttributes;

class DosAttributeProvider$Attributes extends BasicAttributeProvider$Attributes implements DosFileAttributes {
   private final boolean readOnly;
   private final boolean hidden;
   private final boolean archive;
   private final boolean system;

   protected DosAttributeProvider$Attributes(File var1) {
      super(var1);
      this.readOnly = (Boolean)var1.getAttribute("dos", "readonly");
      this.hidden = (Boolean)var1.getAttribute("dos", "hidden");
      this.archive = (Boolean)var1.getAttribute("dos", "archive");
      this.system = (Boolean)var1.getAttribute("dos", "system");
   }

   @Override
   public boolean isReadOnly() {
      return this.readOnly;
   }

   @Override
   public boolean isHidden() {
      return this.hidden;
   }

   @Override
   public boolean isArchive() {
      return this.archive;
   }

   @Override
   public boolean isSystem() {
      return this.system;
   }
}
