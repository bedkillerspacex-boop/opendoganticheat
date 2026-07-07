package org.ywzj.doganticheat.jimfs;

class GlobToRegex$3 extends GlobToRegex$State {
   GlobToRegex$3() {
      super(null);
   }

   @Override
   void process(GlobToRegex var1, char var2) {
      if (var2 == '*') {
         GlobToRegex.access$1200(var1);
         GlobToRegex.access$1000(var1);
      } else {
         GlobToRegex.access$1300(var1);
         GlobToRegex.access$1000(var1);
         GlobToRegex.access$1400(var1).process(var1, var2);
      }
   }

   @Override
   void finish(GlobToRegex var1) {
      GlobToRegex.access$1300(var1);
   }

   @Override
   public String toString() {
      return "STAR";
   }
}
