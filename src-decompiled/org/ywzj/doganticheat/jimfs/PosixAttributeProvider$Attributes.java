package org.ywzj.doganticheat.jimfs;

import com.google.common.collect.ImmutableSet;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.UserPrincipal;

class PosixAttributeProvider$Attributes extends BasicAttributeProvider$Attributes implements PosixFileAttributes {
   private final UserPrincipal owner;
   private final GroupPrincipal group;
   private final ImmutableSet permissions;

   protected PosixAttributeProvider$Attributes(File var1) {
      super(var1);
      this.owner = (UserPrincipal)var1.getAttribute("owner", "owner");
      this.group = (GroupPrincipal)var1.getAttribute("posix", "group");
      this.permissions = (ImmutableSet)var1.getAttribute("posix", "permissions");
   }

   @Override
   public UserPrincipal owner() {
      return this.owner;
   }

   @Override
   public GroupPrincipal group() {
      return this.group;
   }

   public ImmutableSet permissions() {
      return this.permissions;
   }
}
