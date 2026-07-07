package org.ywzj.doganticheat.jimfs;

import java.nio.file.attribute.GroupPrincipal;

final class UserLookupService$JimfsGroupPrincipal extends UserLookupService$NamedPrincipal implements GroupPrincipal {
   private UserLookupService$JimfsGroupPrincipal(String var1) {
      super(var1, null);
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof UserLookupService$JimfsGroupPrincipal && ((UserLookupService$JimfsGroupPrincipal)var1).name.equals(this.name);
   }
}
