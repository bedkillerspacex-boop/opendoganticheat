package org.ywzj.doganticheat.c;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.ywzj.doganticheat.b.b.e;

public class a {
   public static String a(InputStream var0) {
      try {
         MessageDigest var1 = MessageDigest.getInstance("SHA1");
         byte[] var2 = new byte[4096];

         for (int var3 = var0.read(var2, 0, 4096); var3 > -1; var3 = var0.read(var2, 0, 4096)) {
            var1.update(var2, 0, var3);
         }

         byte[] var4 = var1.digest();
         return String.format("%0" + (var4.length << 1) + "x", new BigInteger(1, var4)).toUpperCase();
      } catch (Exception var5) {
         return "err";
      }
   }

   public static void a(String var0) {
      Minecraft.m_91087_().m_18707_(() -> org.ywzj.doganticheat.b.a.a.sendToServer(new e(var0)));
   }

   public static String a(int var0) {
      String var1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
      StringBuilder var2 = new StringBuilder();
      Random var3 = new Random();

      for (int var4 = 0; var4 < var0; var4++) {
         var2.append(var1.charAt(var3.nextInt(var1.length())));
      }

      return var2.toString();
   }
}
