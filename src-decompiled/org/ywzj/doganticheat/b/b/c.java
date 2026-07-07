package org.ywzj.doganticheat.b.b;

import java.util.Arrays;
import net.minecraft.network.FriendlyByteBuf;

public class c {
   private final byte[] a;
   private final byte[] b;
   private final short c;

   public c(byte[] var1, byte[] var2, short var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   public static c a(FriendlyByteBuf var0) {
      return null;
   }

   public void b(FriendlyByteBuf var1) {
      var1.writeShort(this.a.length);
      var1.writeBytes(this.a);
      var1.writeShort(this.c);
      var1.writeInt(Arrays.hashCode(org.ywzj.doganticheat.core.c.d(this.b)) + Arrays.hashCode(this.a));
   }
}
