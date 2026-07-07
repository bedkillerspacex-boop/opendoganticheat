package org.ywzj.doganticheat.b.b;

import net.minecraft.network.FriendlyByteBuf;

public class o {
   private final byte a;
   private final float b;
   private final int c;

   public o(byte var1, float var2, int var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   public static o a(FriendlyByteBuf var0) {
      return new o(var0.readByte(), var0.readFloat(), var0.readInt());
   }

   public void b(FriendlyByteBuf var1) {
   }

   public byte a() {
      return this.a;
   }

   public float b() {
      return this.b;
   }

   public int c() {
      return this.c;
   }
}
