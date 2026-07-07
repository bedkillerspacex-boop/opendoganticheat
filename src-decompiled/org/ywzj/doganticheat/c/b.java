package org.ywzj.doganticheat.c;

import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;

public class b {
   public static byte[] a(byte[] var0) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();

      try {
         GZIPOutputStream var2 = new GZIPOutputStream(var1);
         var2.write(var0);
         var2.flush();
         var2.close();
      } catch (Exception var3) {
      }

      return var1.toByteArray();
   }
}
