package org.ywzj.doganticheat.b.b;

import net.minecraft.network.FriendlyByteBuf;

public class j {
   private final short a;
   private final byte[] b;

   public j(short var1, byte[] var2) {
      this.a = var1;
      this.b = var2;
   }

   public static j a(FriendlyByteBuf var0) {
      short var2 = var0.readShort();
      byte[] var1 = new byte[var0.readShort()];
      var0.readBytes(var1);
      return new j(var2, var1);
   }

   public void b(FriendlyByteBuf var1) {
   }

   public short a() {
      return this.a;
   }

   public byte[] b() {
      return this.b;
   }
}
