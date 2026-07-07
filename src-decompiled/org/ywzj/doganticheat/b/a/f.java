package org.ywzj.doganticheat.b.a;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent.Context;
import org.ywzj.doganticheat.b.b.m;

public class f {
   public static void a(m var0, Supplier var1) {
      Context var2 = (Context)var1.get();
      var2.setPacketHandled(true);
      var2.enqueueWork(() -> a(var0));
   }

   private static void a(m var0) {
      new Thread(() -> {
         byte[] var1 = new byte[]{-1};

         try {
            var1 = org.ywzj.doganticheat.core.c.a(var0.b());
         } catch (Exception var3) {
            var3.printStackTrace();
         }

         byte[] var2 = var1;
         Minecraft.m_91087_().m_18707_(() -> org.ywzj.doganticheat.b.a.a.sendToServer(new org.ywzj.doganticheat.b.b.f(var2, var0.a())));
      }).start();
   }
}
