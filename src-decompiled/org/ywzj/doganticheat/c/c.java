package org.ywzj.doganticheat.c;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class c {
   public static List a(File var0) {
      ArrayList var1 = new ArrayList();
      if (var0.isDirectory()) {
         File[] var2 = var0.listFiles();
         if (var2 != null) {
            for (File var6 : var2) {
               var1.addAll(a(var6));
            }
         }
      } else {
         var1.add(var0);
      }

      return var1;
   }

   public static String a(List var0) {
      ArrayList var1 = new ArrayList();

      for (File var3 : var0) {
         try {
            FileInputStream var4 = new FileInputStream(var3);
            var1.add(a.a(var4));
         } catch (FileNotFoundException var5) {
            var5.printStackTrace();
         }
      }

      Collections.sort(var1);
      StringBuilder var6 = new StringBuilder();

      for (String var8 : var1) {
         var6.append(var8);
      }

      return var6.toString();
   }
}
