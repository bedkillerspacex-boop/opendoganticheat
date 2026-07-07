package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.UserPrincipal;
import java.util.Set;

class PosixAttributeProvider$View extends AbstractAttributeView implements PosixFileAttributeView {
   private final BasicFileAttributeView basicView;
   private final FileOwnerAttributeView ownerView;

   protected PosixAttributeProvider$View(FileLookup var1, BasicFileAttributeView var2, FileOwnerAttributeView var3) {
      super(var1);
      this.basicView = (BasicFileAttributeView)Preconditions.checkNotNull(var2);
      this.ownerView = (FileOwnerAttributeView)Preconditions.checkNotNull(var3);
   }

   @Override
   public String name() {
      return "posix";
   }

   @Override
   public PosixFileAttributes readAttributes() {
      return new PosixAttributeProvider$Attributes(this.lookupFile());
   }

   @Override
   public void setTimes(FileTime var1, FileTime var2, FileTime var3) {
      this.basicView.setTimes(var1, var2, var3);
   }

   @Override
   public void setPermissions(Set var1) {
      this.lookupFile().setAttribute("posix", "permissions", ImmutableSet.copyOf(var1));
   }

   @Override
   public void setGroup(GroupPrincipal var1) {
      this.lookupFile().setAttribute("posix", "group", Preconditions.checkNotNull(var1));
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
