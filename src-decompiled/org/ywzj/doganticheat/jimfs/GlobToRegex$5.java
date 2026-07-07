package org.ywzj.doganticheat.jimfs;

class GlobToRegex$5 extends GlobToRegex$State {
   GlobToRegex$5() {
      super(null);
   }

   @Override
   void process(GlobToRegex var1, char var2) {
      if (var2 == ']') {
         GlobToRegex.access$1800(var1);
         GlobToRegex.access$1000(var1);
      } else {
         GlobToRegex.access$1600(var1, var2);
      }
   }

   @Override
   void finish(GlobToRegex var1) {
      throw GlobToRegex.access$1100(var1, "Unclosed [");
   }

   @Override
   public String toString() {
      return "BRACKET";
   }
}
