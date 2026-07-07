package org.ywzj.doganticheat.b.a;

import java.util.function.Supplier;
import net.minecraftforge.network.NetworkEvent.Context;
import org.ywzj.doganticheat.b.b.o;

public class h {
   public static void a(o var0, Supplier var1) {
      Context var2 = (Context)var1.get();
      var2.setPacketHandled(true);
      var2.enqueueWork(() -> a(var0));
   }

   private static void a(o var0) {
      new Thread(() -> {
         boolean var1 = true;
         if (var0.a() == 1) {
            var1 = org.ywzj.doganticheat.a.c.a(var0.b());
         } else if (var0.a() == 2) {
            var1 = org.ywzj.doganticheat.a.c.b(var0.b());
         } else if (var0.a() == 3) {
            var1 = org.ywzj.doganticheat.a.c.c(var0.b());
         } else if (var0.a() == 4) {
            var1 = org.ywzj.doganticheat.a.c.a(var0.c());
         }

         if (var1) {
            org.ywzj.doganticheat.b.a.a.sendToServer(new org.ywzj.doganticheat.b.b.h("ok#" + var0.a()));
         } else {
            org.ywzj.doganticheat.b.a.a.sendToServer(new org.ywzj.doganticheat.b.b.h("fail#" + var0.a()));
         }
      }).start();
   }
}
