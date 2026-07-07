package org.ywzj.doganticheat.b.b;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import net.minecraft.network.FriendlyByteBuf;

public class l {
   private final byte a;
   private final PublicKey b;

   public l(byte var1, PublicKey var2) {
      this.a = var1;
      this.b = var2;
   }

   public static l a(FriendlyByteBuf var0) {
      byte var1 = var0.readByte();

      try {
         byte[] var2 = new byte[var0.readInt()];
         var0.readBytes(var2);
         X509EncodedKeySpec var3 = new X509EncodedKeySpec(var2);
         KeyFactory var4 = KeyFactory.getInstance("RSA");
         PublicKey var5 = var4.generatePublic(var3);
         return new l(var1, var5);
      } catch (Exception var6) {
         throw new RuntimeException(var6);
      }
   }

   public void b(FriendlyByteBuf var1) {
   }

   public byte a() {
      return this.a;
   }

   public PublicKey b() {
      return this.b;
   }
}
