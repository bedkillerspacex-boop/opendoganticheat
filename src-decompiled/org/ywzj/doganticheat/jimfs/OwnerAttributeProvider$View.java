package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;

final class OwnerAttributeProvider$View extends AbstractAttributeView implements FileOwnerAttributeView {
   public OwnerAttributeProvider$View(FileLookup var1) {
      super(var1);
   }

   @Override
   public String name() {
      return "owner";
   }

   @Override
   public UserPrincipal getOwner() {
      return (UserPrincipal)this.lookupFile().getAttribute("owner", "owner");
   }

   @Override
   public void setOwner(UserPrincipal var1) {
      this.lookupFile().setAttribute("owner", "owner", Preconditions.checkNotNull(var1));
   }
}
