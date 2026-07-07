package org.ywzj.doganticheat.jimfs;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.util.List;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

final class AclAttributeProvider extends AttributeProvider {
   private static final ImmutableSet ATTRIBUTES = ImmutableSet.of("acl");
   private static final ImmutableSet INHERITED_VIEWS = ImmutableSet.of("owner");
   private static final ImmutableList DEFAULT_ACL = ImmutableList.of();

   @Override
   public String name() {
      return "acl";
   }

   @Override
   public ImmutableSet inherits() {
      return INHERITED_VIEWS;
   }

   @Override
   public ImmutableSet fixedAttributes() {
      return ATTRIBUTES;
   }

   @Override
   public ImmutableMap defaultValues(Map var1) {
      Object var2 = var1.get("acl:acl");
      ImmutableList var3 = DEFAULT_ACL;
      if (var2 != null) {
         var3 = toAcl((List)checkType("acl", "acl", var2, List.class));
      }

      return ImmutableMap.of("acl:acl", var3);
   }

   @Override
   public @Nullable Object get(File var1, String var2) {
      return var2.equals("acl") ? var1.getAttribute("acl", "acl") : null;
   }

   @Override
   public void set(File var1, String var2, String var3, Object var4, boolean var5) {
      if (var3.equals("acl")) {
         checkNotCreate(var2, var3, var5);
         var1.setAttribute("acl", "acl", toAcl((List)checkType(var2, var3, var4, List.class)));
      }
   }

   private static ImmutableList toAcl(List var0) {
      ImmutableList var1 = ImmutableList.copyOf(var0);
      UnmodifiableIterator var2 = var1.iterator();

      while (var2.hasNext()) {
         Object var3 = var2.next();
         if (!(var3 instanceof AclEntry)) {
            throw new IllegalArgumentException("invalid element for attribute 'acl:acl': should be List<AclEntry>, found element of type " + var3.getClass());
         }
      }

      return var1;
   }

   @Override
   public Class viewType() {
      return AclFileAttributeView.class;
   }

   public AclFileAttributeView view(FileLookup var1, ImmutableMap var2) {
      return new AclAttributeProvider$View(var1, (FileOwnerAttributeView)var2.get("owner"));
   }
}
