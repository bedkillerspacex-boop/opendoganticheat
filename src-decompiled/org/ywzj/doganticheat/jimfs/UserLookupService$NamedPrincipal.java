package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import java.nio.file.attribute.UserPrincipal;

abstract class UserLookupService$NamedPrincipal implements UserPrincipal {
   protected final String name;

   private UserLookupService$NamedPrincipal(String var1) {
      this.name = (String)Preconditions.checkNotNull(var1);
   }

   @Override
   public final String getName() {
      return this.name;
   }

   @Override
   public final int hashCode() {
      return this.name.hashCode();
   }

   @Override
   public final String toString() {
      return this.name;
   }
}
