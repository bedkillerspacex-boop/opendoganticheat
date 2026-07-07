package org.ywzj.doganticheat;

import com.sun.jna.Native;
import java.util.ArrayList;

public class WindowUtils {
   public static boolean a() {
      return System.getProperty("os.name").toLowerCase().contains("windows");
   }

   public static String[] b() {
      WindowUtils$User32 var0 = WindowUtils$User32.INSTANCE;
      ArrayList var1 = new ArrayList();
      var0.EnumWindows((var2, var3) -> {
         byte[] var4 = new byte[512];
         var0.GetWindowTextA(var2, var4, 512);
         String var5 = Native.toString(var4);
         var1.add(var5);
         return true;
      }, null);
      return var1.toArray(new String[0]);
   }
}
