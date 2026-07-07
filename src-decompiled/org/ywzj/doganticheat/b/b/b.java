package org.ywzj.doganticheat.b.b;

import net.minecraft.network.FriendlyByteBuf;

public class b {
   private final boolean a;
   private final byte[] b;
   private final int c;

   public b(boolean var1, byte[] var2, int var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      if (var2.length > 32763) {
         throw new RuntimeException("packet size > 32K");
      }
   }

   public static b a(FriendlyByteBuf var0) {
      return null;
   }

   public void b(FriendlyByteBuf var1) {
      var1.writeBoolean(this.a);
      var1.writeShort(this.b.length);
      var1.writeBytes(this.b);
      var1.writeInt(this.c);
   }
}
