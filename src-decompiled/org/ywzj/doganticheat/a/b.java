package org.ywzj.doganticheat.a;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;

public class b {
   private static final File a = new File(Minecraft.m_91087_().f_91069_, "shaderpacks");
   private static final List b = new ArrayList();

   public static void a() {
      b.clear();
      List var0 = b();
      ArrayList var1 = new ArrayList();

      for (File var3 : var0) {
         if (var3.isDirectory()) {
            List var4 = org.ywzj.doganticheat.c.c.a(var3);
            var1.add(
               org.ywzj.doganticheat.c.a.a(new ByteArrayInputStream(org.ywzj.doganticheat.c.c.a(var4).getBytes(StandardCharsets.UTF_8)))
                  + "&dac&"
                  + var3.getName()
            );
         } else {
            try {
               FileInputStream var6 = new FileInputStream(var3);
               var1.add(org.ywzj.doganticheat.c.a.a(var6) + "&dac&" + var3.getName());
            } catch (FileNotFoundException var5) {
               var5.printStackTrace();
            }
         }
      }

      b.addAll(var1);
   }

   public static List b() {
      ArrayList var0 = new ArrayList();
      ArrayList var1 = new ArrayList();

      try {
         if (!a.exists()) {
            a.mkdir();
         }

         File[] var2 = a.listFiles();

         for (File var6 : var2) {
            String var7 = var6.getName();
            if (var6.isDirectory()) {
               if (!var7.equals("debug")) {
                  File var8 = new File(var6, "shaders");
                  if (var8.exists() && var8.isDirectory()) {
                     var0.add(var6);
                  }
               }
            } else if (var6.isFile() && var7.toLowerCase().endsWith(".zip")) {
               var1.add(var6);
            }
         }
      } catch (Exception var9) {
      }

      ArrayList var10 = new ArrayList();
      var10.addAll(var0);
      var10.addAll(var1);
      return var10;
   }

   public static List c() {
      return b;
   }
}
