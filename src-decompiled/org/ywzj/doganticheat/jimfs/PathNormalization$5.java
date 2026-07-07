package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Ascii;

enum PathNormalization$5 {
   PathNormalization$5(int var3) {
   }

   @Override
   public String apply(String var1) {
      return Ascii.toLowerCase(var1);
   }
}
