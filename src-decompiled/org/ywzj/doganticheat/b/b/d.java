package org.ywzj.doganticheat.b.b;

import net.minecraft.network.FriendlyByteBuf;

public class d {
   private final byte[] a;

   public d(byte[] var1) {
      this.a = var1;
   }

   public static d a(FriendlyByteBuf var0) {
      return null;
   }

   public void b(FriendlyByteBuf var1) {
      var1.writeShort(this.a.length);
      var1.writeBytes(this.a);
   }
}
