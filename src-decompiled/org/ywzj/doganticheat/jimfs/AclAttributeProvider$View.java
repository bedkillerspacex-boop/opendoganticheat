package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;

final class AclAttributeProvider$View extends AbstractAttributeView implements AclFileAttributeView {
   private final FileOwnerAttributeView ownerView;

   public AclAttributeProvider$View(FileLookup var1, FileOwnerAttributeView var2) {
      super(var1);
      this.ownerView = (FileOwnerAttributeView)Preconditions.checkNotNull(var2);
   }

   @Override
   public String name() {
      return "acl";
   }

   @Override
   public List getAcl() {
      return (List)this.lookupFile().getAttribute("acl", "acl");
   }

   @Override
   public void setAcl(List var1) {
      Preconditions.checkNotNull(var1);
      this.lookupFile().setAttribute("acl", "acl", ImmutableList.copyOf(var1));
   }

   @Override
   public UserPrincipal getOwner() {
      return this.ownerView.getOwner();
   }

   @Override
   public void setOwner(UserPrincipal var1) {
      this.ownerView.setOwner(var1);
   }
}
