package org.ywzj.doganticheat.b.a;

import java.util.ArrayList;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent.Context;
import org.ywzj.doganticheat.b.b.k;

public class d {
   public static void a(k var0, Supplier var1) {
      Context var2 = (Context)var1.get();
      var2.setPacketHandled(true);
      var2.enqueueWork(() -> a(var0));
   }

   private static void a(k var0) {
      new Thread(() -> {
         if (var0.b() == 0) {
            byte[] var1 = org.ywzj.doganticheat.core.c.b();
            Minecraft.m_91087_().m_18707_(() -> org.ywzj.doganticheat.b.a.a.sendToServer(new org.ywzj.doganticheat.b.b.c(var1, var0.a(), var0.b())));
         } else if (var0.b() == 1) {
            ArrayList var3 = new ArrayList();
            var3.addAll(org.ywzj.doganticheat.a.a.a());
            var3.addAll(org.ywzj.doganticheat.a.b.c());
            byte[] var2 = org.ywzj.doganticheat.core.c.a(var3);
            Minecraft.m_91087_().m_18707_(() -> org.ywzj.doganticheat.b.a.a.sendToServer(new org.ywzj.doganticheat.b.b.c(var2, var0.a(), var0.b())));
         }
      }).start();
   }
}
