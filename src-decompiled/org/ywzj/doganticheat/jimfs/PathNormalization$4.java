package org.ywzj.doganticheat.jimfs;

import com.ibm.icu.lang.UCharacter;

enum PathNormalization$4 {
   PathNormalization$4(int var3) {
   }

   @Override
   public String apply(String var1) {
      try {
         return UCharacter.foldCase(var1, true);
      } catch (NoClassDefFoundError var4) {
         NoClassDefFoundError var3 = new NoClassDefFoundError(
            "PathNormalization.CASE_FOLD_UNICODE requires ICU4J. Did you forget to include it on your classpath?"
         );
         var3.initCause(var4);
         throw var3;
      }
   }
}
