package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableSet.Builder;
import java.nio.ByteBuffer;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import org.checkerframework.checker.nullness.qual.Nullable;

final class UserDefinedAttributeProvider extends AttributeProvider {
   @Override
   public String name() {
      return "user";
   }

   @Override
   public ImmutableSet fixedAttributes() {
      return ImmutableSet.of();
   }

   @Override
   public boolean supports(String var1) {
      return true;
   }

   @Override
   public ImmutableSet attributes(File var1) {
      return userDefinedAttributes(var1);
   }

   private static ImmutableSet userDefinedAttributes(File var0) {
      Builder var1 = ImmutableSet.builder();
      UnmodifiableIterator var2 = var0.getAttributeNames("user").iterator();

      while (var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.add(var3);
      }

      return var1.build();
   }

   @Override
   public @Nullable Object get(File var1, String var2) {
      Object var3 = var1.getAttribute("user", var2);
      if (var3 instanceof byte[]) {
         byte[] var4 = (byte[])var3;
         return var4.clone();
      } else {
         return null;
      }
   }

   @Override
   public void set(File var1, String var2, String var3, Object var4, boolean var5) {
      Preconditions.checkNotNull(var4);
      checkNotCreate(var2, var3, var5);
      byte[] var6;
      if (var4 instanceof byte[]) {
         var6 = (byte[])((byte[])var4).clone();
      } else {
         if (!(var4 instanceof ByteBuffer)) {
            throw invalidType(var2, var3, var4, byte[].class, ByteBuffer.class);
         }

         ByteBuffer var7 = (ByteBuffer)var4;
         var6 = new byte[var7.remaining()];
         var7.get(var6);
      }

      var1.setAttribute("user", var3, var6);
   }

   @Override
   public Class viewType() {
      return UserDefinedFileAttributeView.class;
   }

   public UserDefinedFileAttributeView view(FileLookup var1, ImmutableMap var2) {
      return new UserDefinedAttributeProvider$View(var1);
   }
}
