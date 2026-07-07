package org.ywzj.doganticheat.b.b;

import io.netty.buffer.ByteBuf;
import java.util.Arrays;

public class f {
   private final byte[] a;
   private final byte[] b;

   public f(byte[] var1, byte[] var2) {
      this.a = var1;
      this.b = var2;
   }

   public static f a(ByteBuf var0) {
      return null;
   }

   public void b(ByteBuf var1) {
      var1.writeShort(this.a.length);
      var1.writeBytes(this.a);
      var1.writeInt(Arrays.hashCode(org.ywzj.doganticheat.core.c.d(this.b)) + Arrays.hashCode(this.a));
   }
}
