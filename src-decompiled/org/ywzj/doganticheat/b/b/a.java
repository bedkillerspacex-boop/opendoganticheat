package org.ywzj.doganticheat.b.b;

import io.netty.buffer.ByteBuf;
import java.util.List;

public class a {
   private final List a;

   public a(List var1) {
      this.a = var1;
   }

   public static a a(ByteBuf var0) {
      return null;
   }

   public void b(ByteBuf var1) {
      var1.writeShort(this.a.size());

      for (short var2 = 0; var2 < this.a.size(); var2++) {
         var1.writeShort(var2);
      }
   }
}
