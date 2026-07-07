package org.ywzj.doganticheat.jimfs;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

final class OwnerAttributeProvider extends AttributeProvider {
   private static final ImmutableSet ATTRIBUTES = ImmutableSet.of("owner");
   private static final UserPrincipal DEFAULT_OWNER = UserLookupService.createUserPrincipal("user");

   @Override
   public String name() {
      return "owner";
   }

   @Override
   public ImmutableSet fixedAttributes() {
      return ATTRIBUTES;
   }

   @Override
   public ImmutableMap defaultValues(Map var1) {
      Object var2 = var1.get("owner:owner");
      UserPrincipal var3 = DEFAULT_OWNER;
      if (var2 != null) {
         if (!(var2 instanceof String)) {
            throw invalidType("owner", "owner", var2, String.class, UserPrincipal.class);
         }

         var3 = UserLookupService.createUserPrincipal((String)var2);
      }

      return ImmutableMap.of("owner:owner", var3);
   }

   @Override
   public @Nullable Object get(File var1, String var2) {
      return var2.equals("owner") ? var1.getAttribute("owner", "owner") : null;
   }

   @Override
   public void set(File var1, String var2, String var3, Object var4, boolean var5) {
      if (var3.equals("owner")) {
         checkNotCreate(var2, var3, var5);
         UserPrincipal var6 = (UserPrincipal)checkType(var2, var3, var4, UserPrincipal.class);
         if (!(var6 instanceof UserLookupService$JimfsUserPrincipal)) {
            var6 = UserLookupService.createUserPrincipal(var6.getName());
         }

         var1.setAttribute("owner", "owner", var6);
      }
   }

   @Override
   public Class viewType() {
      return FileOwnerAttributeView.class;
   }

   public FileOwnerAttributeView view(FileLookup var1, ImmutableMap var2) {
      return new OwnerAttributeProvider$View(var1);
   }
}
