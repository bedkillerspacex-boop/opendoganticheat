package org.ywzj.doganticheat.b.a;

import java.util.function.Supplier;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class b {
   private static final Logger a = LogManager.getLogger();

   public static int a(Object var0, Supplier var1) {
      Context var2 = (Context)var1.get();
      LogicalSide var3 = var2.getDirection().getReceptionSide();
      var2.setPacketHandled(true);
      var2.enqueueWork(() -> a());
      return 0;
   }

   private static void a() {
      a.warn("Wrong Side");
   }
}
