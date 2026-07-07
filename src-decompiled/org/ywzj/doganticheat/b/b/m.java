package org.ywzj.doganticheat.b.b;

import io.netty.buffer.ByteBuf;

public class m {
   private final byte[] a;
   private final byte[] b;

   public m(byte[] var1, byte[] var2) {
      this.a = var1;
      this.b = var2;
   }

   public static m a(ByteBuf var0) {
      byte[] var1 = new byte[var0.readShort()];
      var0.readBytes(var1);
      byte[] var2 = new byte[var0.readShort()];
      var0.readBytes(var2);
      return new m(var1, var2);
   }

   public void b(ByteBuf var1) {
   }

   public byte[] a() {
      return this.a;
   }

   public byte[] b() {
      return this.b;
   }
}
