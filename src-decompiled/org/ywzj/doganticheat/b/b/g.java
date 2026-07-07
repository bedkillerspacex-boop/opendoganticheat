package org.ywzj.doganticheat.b.b;

import java.nio.charset.StandardCharsets;
import net.minecraft.network.FriendlyByteBuf;

public class g {
   private final boolean a;
   private final String b;

   public g(boolean var1, String var2) {
      this.a = var1;
      this.b = var2;
   }

   public static g a(FriendlyByteBuf var0) {
      return null;
   }

   public void b(FriendlyByteBuf var1) {
      var1.writeBoolean(this.a);
      byte[] var2 = this.b.getBytes(StandardCharsets.UTF_8);
      var1.writeShort(var2.length);
      var1.writeBytes(var2);
   }
}
