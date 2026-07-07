package org.ywzj.doganticheat.jimfs;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.Nullable;

final class PosixAttributeProvider extends AttributeProvider {
   private static final ImmutableSet ATTRIBUTES = ImmutableSet.of("group", "permissions");
   private static final ImmutableSet INHERITED_VIEWS = ImmutableSet.of("basic", "owner");
   private static final GroupPrincipal DEFAULT_GROUP = UserLookupService.createGroupPrincipal("group");
   private static final ImmutableSet DEFAULT_PERMISSIONS = Sets.immutableEnumSet(PosixFilePermissions.fromString("rw-r--r--"));

   @Override
   public String name() {
      return "posix";
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
      Object var2 = var1.get("posix:group");
      GroupPrincipal var3 = DEFAULT_GROUP;
      if (var2 != null) {
         if (!(var2 instanceof String)) {
            throw new IllegalArgumentException(
               "invalid type " + var2.getClass().getName() + " for attribute 'posix:group': should be one of " + String.class + " or " + GroupPrincipal.class
            );
         }

         var3 = UserLookupService.createGroupPrincipal((String)var2);
      }

      Object var4 = var1.get("posix:permissions");
      ImmutableSet var5 = DEFAULT_PERMISSIONS;
      if (var4 != null) {
         if (var4 instanceof String) {
            var5 = Sets.immutableEnumSet(PosixFilePermissions.fromString((String)var4));
         } else {
            if (!(var4 instanceof Set)) {
               throw new IllegalArgumentException(
                  "invalid type " + var4.getClass().getName() + " for attribute 'posix:permissions': should be one of " + String.class + " or " + Set.class
               );
            }

            var5 = toPermissions((Set)var4);
         }
      }

      return ImmutableMap.of("posix:group", var3, "posix:permissions", var5);
   }

   @Override
   public @Nullable Object get(File var1, String var2) {
      switch (var2) {
         case "group":
            return var1.getAttribute("posix", "group");
         case "permissions":
            return var1.getAttribute("posix", "permissions");
         default:
            return null;
      }
   }

   @Override
   public void set(File var1, String var2, String var3, Object var4, boolean var5) {
      switch (var3) {
         case "group":
            checkNotCreate(var2, var3, var5);
            GroupPrincipal var8 = (GroupPrincipal)checkType(var2, var3, var4, GroupPrincipal.class);
            if (!(var8 instanceof UserLookupService$JimfsGroupPrincipal)) {
               var8 = UserLookupService.createGroupPrincipal(var8.getName());
            }

            var1.setAttribute("posix", "group", var8);
            break;
         case "permissions":
            var1.setAttribute("posix", "permissions", toPermissions((Set)checkType(var2, var3, var4, Set.class)));
      }
   }

   private static ImmutableSet toPermissions(Set var0) {
      ImmutableSet var1 = ImmutableSet.copyOf(var0);
      UnmodifiableIterator var2 = var1.iterator();

      while (var2.hasNext()) {
         Object var3 = var2.next();
         if (!(var3 instanceof PosixFilePermission)) {
            throw new IllegalArgumentException(
               "invalid element for attribute 'posix:permissions': should be Set<PosixFilePermission>, found element of type " + var3.getClass()
            );
         }
      }

      return Sets.immutableEnumSet(var1);
   }

   @Override
   public Class viewType() {
      return PosixFileAttributeView.class;
   }

   public PosixFileAttributeView view(FileLookup var1, ImmutableMap var2) {
      return new PosixAttributeProvider$View(var1, (BasicFileAttributeView)var2.get("basic"), (FileOwnerAttributeView)var2.get("owner"));
   }

   @Override
   public Class attributesType() {
      return PosixFileAttributes.class;
   }

   public PosixFileAttributes readAttributes(File var1) {
      return new PosixAttributeProvider$Attributes(var1);
   }
}
