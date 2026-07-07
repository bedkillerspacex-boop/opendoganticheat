package org.ywzj.doganticheat.b.a;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent.Context;
import org.ywzj.doganticheat.b.b.i;

public class a {
   public static void a(i var0, Supplier var1) {
      Context var2 = (Context)var1.get();
      var2.setPacketHandled(true);
      var2.enqueueWork(() -> a(var0));
   }

   private static void a(i var0) {
      new Thread(() -> {
         HashSet var1 = org.ywzj.doganticheat.core.c.c();
         ArrayList var2 = new ArrayList();

         for (short var3 = 0; var3 < var0.a().size(); var3++) {
            if (var1.contains(var0.a().get(var3))) {
               var2.add(var3);
            }
         }

         Minecraft.m_91087_().m_18707_(() -> org.ywzj.doganticheat.b.a.a.sendToServer(new org.ywzj.doganticheat.b.b.a(var2)));
      }).start();
   }
}
