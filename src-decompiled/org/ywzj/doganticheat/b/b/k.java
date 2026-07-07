package org.ywzj.doganticheat.b.b;

import net.minecraft.network.FriendlyByteBuf;

public class k {
   private final byte[] a;
   private final short b;

   public k(byte[] var1, short var2) {
      this.a = var1;
      this.b = var2;
   }

   public static k a(FriendlyByteBuf var0) {
      byte[] var1 = new byte[var0.readShort()];
      var0.readBytes(var1);
      short var2 = var0.readShort();
      return new k(var1, var2);
   }

   public void b(FriendlyByteBuf var1) {
   }

   public byte[] a() {
      return this.a;
   }

   public short b() {
      return this.b;
   }
}
