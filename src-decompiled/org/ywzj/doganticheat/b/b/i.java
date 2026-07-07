package org.ywzj.doganticheat.b.b;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;

public class i {
   private final List a;

   public i(List var1) {
      this.a = var1;
   }

   public static i a(FriendlyByteBuf var0) {
      short var1 = var0.readShort();
      ArrayList var2 = new ArrayList();

      for (int var3 = 0; var3 < var1; var3++) {
         var2.add(var0.m_130277_());
      }

      return new i(var2);
   }

   public void b(FriendlyByteBuf var1) {
   }

   public List a() {
      return this.a;
   }
}
