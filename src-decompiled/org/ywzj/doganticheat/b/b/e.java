package org.ywzj.doganticheat.b.b;

import java.nio.charset.StandardCharsets;
import net.minecraft.network.FriendlyByteBuf;

public class e {
   private byte[] a;

   public e(String var1) {
      this.a = var1.getBytes(StandardCharsets.UTF_8);
   }

   public static e a(FriendlyByteBuf var0) {
      return null;
   }

   public void b(FriendlyByteBuf var1) {
      this.a = org.ywzj.doganticheat.core.c.c(this.a);
      var1.writeShort(this.a.length);
      var1.writeBytes(this.a);
   }
}
