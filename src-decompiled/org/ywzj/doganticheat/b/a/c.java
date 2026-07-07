package org.ywzj.doganticheat.b.a;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import net.minecraftforge.network.NetworkEvent.Context;
import org.ywzj.doganticheat.b.b.j;

public class c {
   public static void a(j var0, Supplier var1) {
      Context var2 = (Context)var1.get();
      var2.setPacketHandled(true);
      var2.enqueueWork(() -> a(var0));
   }

   private static void a(j var0) {
      short var1 = var0.a();
      byte[] var2 = var0.b();
      new Thread(
            () -> {
               try {
                  boolean var2x = true;
                  byte[] var3 = new byte[]{-1};
                  if (var1 == 1) {
                     var3 = org.ywzj.doganticheat.core.c.a(1, var2);
                     var2x = false;
                  } else if (var1 == 2) {
                     var3 = org.ywzj.doganticheat.core.c.a(1, var2);
                     var2x = false;
                  } else if (var1 == 3) {
                     var3 = org.ywzj.doganticheat.core.c.a(2, var2);
                     var2x = false;
                  } else if (var1 == 4) {
                     var3 = org.ywzj.doganticheat.core.c.a(3, var2);
                     var2x = false;
                  } else if (var1 == 5) {
                     var3 = org.ywzj.doganticheat.core.c.a(0, 0.0F, 0.0F, 1.0F, 1.0F);
                     var2x = false;
                  } else if (var1 == 6) {
                     var3 = org.ywzj.doganticheat.core.c.a(1, 0.0F, 0.0F, 1.0F, 1.0F);
                     var2x = false;
                  } else if (var1 == 7 || var1 == 8) {
                     try (
                        ByteArrayOutputStream var15 = new ByteArrayOutputStream();
                        ObjectOutputStream var6 = new ObjectOutputStream(var15);
                     ) {
                        var6.writeObject(org.ywzj.doganticheat.core.c.c());
                        var6.flush();
                        var3 = org.ywzj.doganticheat.c.b.a(var15.toByteArray());
                     } catch (IOException var13) {
                        var13.printStackTrace();
                     }
                  } else if (var1 == 9) {
                     var3 = org.ywzj.doganticheat.core.c.e(var2);
                     var2x = false;
                  } else if (var1 == 10) {
                     List var5 = Arrays.stream(new String(var2, StandardCharsets.UTF_8).split("/")).map(Float::valueOf).toList();
                     var3 = org.ywzj.doganticheat.core.c.a(
                        ((Float)var5.get(0)).intValue(), (Float)var5.get(1), (Float)var5.get(2), (Float)var5.get(3), (Float)var5.get(4)
                     );
                     var2x = false;
                  }

                  if (var2x) {
                     var3 = org.ywzj.doganticheat.core.c.c(var3);
                  }

                  ByteArrayInputStream var4 = new ByteArrayInputStream(var3);
                  int var16 = var4.available();
                  byte[] var17 = new byte[8192];

                  int var7;
                  while ((var7 = var4.read(var17)) >= 0) {
                     if (var17.length == var7) {
                        org.ywzj.doganticheat.b.a.a.sendToServer(new org.ywzj.doganticheat.b.b.b(var4.available() == 0, var17, var16));
                     } else {
                        org.ywzj.doganticheat.b.a.a.sendToServer(new org.ywzj.doganticheat.b.b.b(var4.available() == 0, Arrays.copyOf(var17, var7), var16));
                     }
                  }
               } catch (Exception var14) {
                  org.ywzj.doganticheat.c.a.a(Arrays.toString(var14.getStackTrace()));
                  org.ywzj.doganticheat.b.a.a.sendToServer(new org.ywzj.doganticheat.b.b.b(true, "err".getBytes(StandardCharsets.UTF_8), -1));
               }
            }
         )
         .start();
   }
}
