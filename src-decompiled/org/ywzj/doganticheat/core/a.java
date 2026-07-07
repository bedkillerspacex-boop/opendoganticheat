package org.ywzj.doganticheat.core;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import org.ywzj.doganticheat.DogAntiCheat;

public class a {
   private static final String a = System.getProperty("java.io.tmpdir") + "/dac";

   public static void a() {
      b();
      String var0 = System.getProperty("java.home");
      File var1 = new File(var0 + "/bin");
      if (var1.exists() && var1.isDirectory()) {
         a("ucrtbased", var1, false);
         a("vcruntime140d", var1, false);
         a("vcruntime140_1d", var1, false);
         a("msvcp140d", var1, false);
         a("libcrypto-3-x64", var1, false);
         a("libssl-3-x64", var1, false);
         a("zlib", var1, false);
      }

      File var2 = new File(a);
      if (!var2.exists()) {
         var2.mkdirs();
      }

      File var3 = b("dac-1.20", var2, true);
      System.load(var3.getAbsolutePath());
   }

   private static File a(String var0, File var1, boolean var2) {
      try {
         String var3 = var0 + ".dll";
         String var4 = var0 + (var2 ? "_" + System.currentTimeMillis() : "") + ".dll";
         ClassLoader var5 = DogAntiCheat.class.getClassLoader();
         InputStream var6 = var5.getResourceAsStream("dlls/" + var3);
         File var7 = new File(var1.getAbsolutePath() + "/" + var4);
         var7.createNewFile();
         FileOutputStream var8 = new FileOutputStream(var7);
         byte[] var9 = new byte[8192];

         int var10;
         while ((var10 = var6.read(var9)) != -1) {
            var8.write(var9, 0, var10);
         }

         var8.close();
         return var7;
      } catch (Exception var11) {
         return null;
      }
   }

   private static File b(String var0, File var1, boolean var2) {
      try {
         String var3 = var0 + ".dll";
         String var4 = var0 + (var2 ? "_" + System.currentTimeMillis() : "") + ".zip";
         ClassLoader var5 = DogAntiCheat.class.getClassLoader();
         InputStream var6 = var5.getResourceAsStream("dlls/" + var3);
         if (var6 == null) {
            return null;
         }

         ByteArrayOutputStream var7 = new ByteArrayOutputStream();
         byte[] var8 = new byte[8192];

         int var9;
         while ((var9 = var6.read(var8)) != -1) {
            var7.write(var8, 0, var9);
         }

         File var10 = new File(var1, var4);
         var10.createNewFile();
         FileOutputStream var11 = new FileOutputStream(var10);
         byte[] var12 = var7.toByteArray();
         var11.write(var12);
         Random var13 = new Random();
         ZipOutputStream var14 = new ZipOutputStream(var11);

         for (int var15 = 0; var15 < 10; var15++) {
            ZipEntry var16 = new ZipEntry(org.ywzj.doganticheat.c.a.a(8) + ".bmp");
            var14.putNextEntry(var16);
            InputStream var17 = var5.getResourceAsStream("dac.png");
            BufferedImage var18 = ImageIO.read(var17);
            int var19 = var13.nextInt(10);
            BufferedImage var20 = new BufferedImage(1024 + var19, 1024 + var19, 1);
            Graphics2D var21 = var20.createGraphics();
            var21.drawImage(var18, 0, 0, 1024 + var19, 1024 + var19, null);
            var21.dispose();

            for (int var22 = 0; var22 < 1024; var22++) {
               for (int var23 = 0; var23 < 1024; var23++) {
                  int var24 = var20.getRGB(var23, var22);
                  int var25 = var24 >> 16 & 0xFF;
                  int var26 = var24 >> 8 & 0xFF;
                  int var27 = var24 & 0xFF;
                  float var28 = 0.8F + var13.nextFloat() * 0.4F;
                  var25 = Math.min(255, Math.max(0, (int)(var25 * var28)));
                  var26 = Math.min(255, Math.max(0, (int)(var26 * var28)));
                  var27 = Math.min(255, Math.max(0, (int)(var27 * var28)));
                  int var29 = var25 << 16 | var26 << 8 | var27;
                  var20.setRGB(var23, var22, var29);
               }
            }

            ByteArrayOutputStream var31 = new ByteArrayOutputStream();
            ImageIO.write(var20, "bmp", var31);
            var31.flush();
            byte[] var32 = var31.toByteArray();
            var14.write(var32);
            var14.closeEntry();
         }

         var14.close();
         return var10;
      } catch (Exception var30) {
         var30.printStackTrace();
         return null;
      }
   }

   private static void b() {
      File var0 = new File(a);
      if (var0.exists() && var0.isDirectory()) {
         File[] var1 = var0.listFiles();
         if (var1 != null) {
            for (File var5 : var1) {
               if (var5.isFile() && (var5.getName().endsWith(".dll") || var5.getName().endsWith(".zip")) && !var5.getName().equals("vcruntime140.dll")) {
                  try {
                     var5.delete();
                  } catch (Exception var7) {
                  }
               }
            }
         }
      }
   }
}
