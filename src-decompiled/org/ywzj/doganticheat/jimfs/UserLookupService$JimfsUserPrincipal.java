package org.ywzj.doganticheat.jimfs;

final class UserLookupService$JimfsUserPrincipal extends UserLookupService$NamedPrincipal {
   private UserLookupService$JimfsUserPrincipal(String var1) {
      super(var1, null);
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof UserLookupService$JimfsUserPrincipal && this.getName().equals(((UserLookupService$JimfsUserPrincipal)var1).getName());
   }
}
