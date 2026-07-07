package org.ywzj.doganticheat.jimfs;

import java.text.Normalizer;
import java.text.Normalizer.Form;

enum PathNormalization$2 {
   PathNormalization$2(int var3) {
   }

   @Override
   public String apply(String var1) {
      return Normalizer.normalize(var1, Form.NFC);
   }
}
