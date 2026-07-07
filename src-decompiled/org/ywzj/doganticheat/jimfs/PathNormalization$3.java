package org.ywzj.doganticheat.jimfs;

import java.text.Normalizer;
import java.text.Normalizer.Form;

enum PathNormalization$3 {
   PathNormalization$3(int var3) {
   }

   @Override
   public String apply(String var1) {
      return Normalizer.normalize(var1, Form.NFD);
   }
}
