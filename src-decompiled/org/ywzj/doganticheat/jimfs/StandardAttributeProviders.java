package org.ywzj.doganticheat.jimfs;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import org.checkerframework.checker.nullness.qual.Nullable;

final class StandardAttributeProviders {
   private static final ImmutableMap PROVIDERS = new Builder()
      .put("basic", new BasicAttributeProvider())
      .put("owner", new OwnerAttributeProvider())
      .put("posix", new PosixAttributeProvider())
      .put("dos", new DosAttributeProvider())
      .put("acl", new AclAttributeProvider())
      .put("user", new UserDefinedAttributeProvider())
      .build();

   private StandardAttributeProviders() {
   }

   public static @Nullable AttributeProvider get(String var0) {
      AttributeProvider var1 = (AttributeProvider)PROVIDERS.get(var0);
      return var1 == null && var0.equals("unix") ? new UnixAttributeProvider() : var1;
   }
}
