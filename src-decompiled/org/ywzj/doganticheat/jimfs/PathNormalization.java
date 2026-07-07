package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Function;
import java.util.regex.Pattern;

public enum PathNormalization implements Function {
   NONE(0),
   NFC(128),
   NFD(128),
   CASE_FOLD_UNICODE(66),
   CASE_FOLD_ASCII(2);

   private final int patternFlags;

   PathNormalization(int var3) {
      this.patternFlags = var3;
   }

   public abstract String apply(String var1);

   public int patternFlags() {
      return this.patternFlags;
   }

   public static String normalize(String var0, Iterable var1) {
      String var2 = var0;

      for (PathNormalization var4 : var1) {
         var2 = var4.apply(var2);
      }

      return var2;
   }

   public static Pattern compilePattern(String var0, Iterable var1) {
      int var2 = 0;

      for (PathNormalization var4 : var1) {
         var2 |= var4.patternFlags();
      }

      return Pattern.compile(var0, var2);
   }
}
