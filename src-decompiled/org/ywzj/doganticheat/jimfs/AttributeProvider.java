package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.io.Serializable;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttributeView;
import java.util.Arrays;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AttributeProvider {
   public abstract String name();

   public ImmutableSet inherits() {
      return ImmutableSet.of();
   }

   public abstract Class viewType();

   public abstract FileAttributeView view(FileLookup var1, ImmutableMap var2);

   public ImmutableMap defaultValues(Map var1) {
      return ImmutableMap.of();
   }

   public abstract ImmutableSet fixedAttributes();

   public boolean supports(String var1) {
      return this.fixedAttributes().contains(var1);
   }

   public ImmutableSet attributes(File var1) {
      return this.fixedAttributes();
   }

   public abstract @Nullable Object get(File var1, String var2);

   public abstract void set(File var1, String var2, String var3, Object var4, boolean var5);

   public @Nullable Class attributesType() {
      return null;
   }

   public BasicFileAttributes readAttributes(File var1) {
      throw new UnsupportedOperationException();
   }

   protected static RuntimeException unsettable(String var0, String var1, boolean var2) {
      checkNotCreate(var0, var1, var2);
      throw new IllegalArgumentException("cannot set attribute '" + var0 + ":" + var1 + "'");
   }

   protected static void checkNotCreate(String var0, String var1, boolean var2) {
      if (var2) {
         throw new UnsupportedOperationException("cannot set attribute '" + var0 + ":" + var1 + "' during file creation");
      }
   }

   protected static Object checkType(String var0, String var1, Object var2, Class var3) {
      Preconditions.checkNotNull(var2);
      if (var3.isInstance(var2)) {
         return var3.cast(var2);
      } else {
         throw invalidType(var0, var1, var2, var3);
      }
   }

   protected static IllegalArgumentException invalidType(String var0, String var1, Object var2, Class... var3) {
      Serializable var4 = var3.length == 1 ? var3[0] : "one of " + Arrays.toString(var3);
      throw new IllegalArgumentException("invalid type " + var2.getClass() + " for attribute '" + var0 + ":" + var1 + "': expected " + var4);
   }
}
