package org.ywzj.doganticheat.jimfs;

class GlobToRegex$2 extends GlobToRegex$State {
   GlobToRegex$2() {
      super(null);
   }

   @Override
   void process(GlobToRegex var1, char var2) {
      GlobToRegex.access$900(var1, var2);
      GlobToRegex.access$1000(var1);
   }

   @Override
   void finish(GlobToRegex var1) {
      throw GlobToRegex.access$1100(var1, "Hanging escape (\\) at end of pattern");
   }

   @Override
   public String toString() {
      return "ESCAPE";
   }
}
