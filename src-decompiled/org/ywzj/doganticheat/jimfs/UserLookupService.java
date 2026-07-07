package org.ywzj.doganticheat.jimfs;

import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.attribute.UserPrincipalNotFoundException;

final class UserLookupService extends UserPrincipalLookupService {
   private final boolean supportsGroups;

   public UserLookupService(boolean var1) {
      this.supportsGroups = var1;
   }

   @Override
   public UserPrincipal lookupPrincipalByName(String var1) {
      return createUserPrincipal(var1);
   }

   @Override
   public GroupPrincipal lookupPrincipalByGroupName(String var1) {
      if (!this.supportsGroups) {
         throw new UserPrincipalNotFoundException(var1);
      } else {
         return createGroupPrincipal(var1);
      }
   }

   static UserPrincipal createUserPrincipal(String var0) {
      return new UserLookupService$JimfsUserPrincipal(var0, null);
   }

   static GroupPrincipal createGroupPrincipal(String var0) {
      return new UserLookupService$JimfsGroupPrincipal(var0, null);
   }
}
