package org.ywzj.doganticheat.a;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PathPackResources;
import net.minecraftforge.fml.loading.FMLPaths;

public class a {
   private static final List a = new ArrayList();

   public static void a(List var0) {
      a.clear();

      for (PackResources var2 : var0) {
         if (var2 instanceof FilePackResources var3) {
            try {
               Field var5 = FilePackResources.class.getDeclaredField("f_243750_");
               var5.setAccessible(true);
               File var6 = (File)var5.get(var3);

               try {
                  FileInputStream var7 = new FileInputStream(var6);
                  a.add(org.ywzj.doganticheat.c.a.a(var7) + "&dac&" + var6.getName());
               } catch (Exception var10) {
                  var10.printStackTrace();
                  a.add("0000000000000000000000000000000000000000&dac&" + var6.getName());
               }
            } catch (Exception var11) {
               var11.printStackTrace();
            }
         } else if (var2 instanceof PathPackResources var4) {
            try {
               Field var17 = PathPackResources.class.getDeclaredField("f_243919_");
               var17.setAccessible(true);
               Path var19 = (Path)var17.get(var4);
               List var21 = org.ywzj.doganticheat.c.c.a(var19.toFile());
               var21.removeIf(var0x -> "pack.mcmeta".equals(var0x.getName().toLowerCase(Locale.ROOT)));
               a.add(
                  org.ywzj.doganticheat.c.a.a(new ByteArrayInputStream(org.ywzj.doganticheat.c.c.a(var21).getBytes(StandardCharsets.UTF_8)))
                     + "&dac&"
                     + var19.toFile().getName()
               );
            } catch (Exception var9) {
               var9.printStackTrace();
               a.add("0000000000000000000000000000000000000000&dac&" + var4);
            }
         }
      }

      try {
         File var13 = FMLPaths.GAMEDIR.get().resolve("tacz").toFile();
         if (var13.exists() && var13.isDirectory()) {
            File[] var14 = var13.listFiles();
            if (var14 == null) {
               return;
            }

            for (File var20 : var14) {
               if (var20.isDirectory()) {
                  List var22 = org.ywzj.doganticheat.c.c.a(var20);
                  a.add(
                     org.ywzj.doganticheat.c.a.a(new ByteArrayInputStream(org.ywzj.doganticheat.c.c.a(var22).getBytes(StandardCharsets.UTF_8)))
                        + "&dac&"
                        + var20.getName()
                  );
               } else if (var20.getName().toLowerCase(Locale.ROOT).endsWith(".zip")) {
                  try {
                     FileInputStream var23 = new FileInputStream(var20);
                     a.add(org.ywzj.doganticheat.c.a.a(var23) + "&dac&" + var20.getName());
                  } catch (FileNotFoundException var8) {
                     var8.printStackTrace();
                  }
               }
            }
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      }
   }

   public static List a() {
      return a;
   }
}
