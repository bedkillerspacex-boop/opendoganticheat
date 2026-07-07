package org.ywzj.doganticheat.jimfs;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

final class DosAttributeProvider extends AttributeProvider {
   private static final ImmutableSet ATTRIBUTES = ImmutableSet.of("readonly", "hidden", "archive", "system");
   private static final ImmutableSet INHERITED_VIEWS = ImmutableSet.of("basic", "owner");

   @Override
   public String name() {
      return "dos";
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
      return ImmutableMap.of(
         "dos:readonly",
         getDefaultValue("dos:readonly", var1),
         "dos:hidden",
         getDefaultValue("dos:hidden", var1),
         "dos:archive",
         getDefaultValue("dos:archive", var1),
         "dos:system",
         getDefaultValue("dos:system", var1)
      );
   }

   private static Boolean getDefaultValue(String var0, Map var1) {
      Object var2 = var1.get(var0);
      return var2 != null ? (Boolean)checkType("dos", var0, var2, Boolean.class) : false;
   }

   @Override
   public @Nullable Object get(File var1, String var2) {
      return ATTRIBUTES.contains(var2) ? var1.getAttribute("dos", var2) : null;
   }

   @Override
   public void set(File var1, String var2, String var3, Object var4, boolean var5) {
      if (this.supports(var3)) {
         checkNotCreate(var2, var3, var5);
         var1.setAttribute("dos", var3, checkType(var2, var3, var4, Boolean.class));
      }
   }

   @Override
   public Class viewType() {
      return DosFileAttributeView.class;
   }

   public DosFileAttributeView view(FileLookup var1, ImmutableMap var2) {
      return new DosAttributeProvider$View(var1, (BasicFileAttributeView)var2.get("basic"));
   }

   @Override
   public Class attributesType() {
      return DosFileAttributes.class;
   }

   public DosFileAttributes readAttributes(File var1) {
      return new DosAttributeProvider$Attributes(var1);
   }
}
