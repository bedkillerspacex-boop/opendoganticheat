package org.ywzj.doganticheat.b.a;

import java.util.function.Supplier;
import net.minecraftforge.network.NetworkEvent.Context;
import org.ywzj.doganticheat.b.b.l;

public class e {
   public static void a(l var0, Supplier var1) {
      Context var2 = (Context)var1.get();
      var2.setPacketHandled(true);
      var2.enqueueWork(() -> a(var0));
   }

   private static void a(l var0) {
      try {
         org.ywzj.doganticheat.core.c.b(var0.b().getEncoded());
         org.ywzj.doganticheat.core.c.a(var0.a());
         byte[] var1 = org.ywzj.doganticheat.core.c.a();
         org.ywzj.doganticheat.b.a.a.sendToServer(new org.ywzj.doganticheat.b.b.d(var1));
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }
}
