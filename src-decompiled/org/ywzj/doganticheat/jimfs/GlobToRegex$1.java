package org.ywzj.doganticheat.jimfs;

class GlobToRegex$1 extends GlobToRegex$State {
   GlobToRegex$1() {
      super(null);
   }

   @Override
   void process(GlobToRegex var1, char var2) {
      switch (var2) {
         case '*':
            GlobToRegex.access$400(var1, GlobToRegex.access$700());
            return;
         case '?':
            GlobToRegex.access$100(var1);
            return;
         case '[':
            GlobToRegex.access$200(var1);
            GlobToRegex.access$400(var1, GlobToRegex.access$300());
            return;
         case '\\':
            GlobToRegex.access$400(var1, GlobToRegex.access$800());
            return;
         case '{':
            GlobToRegex.access$500(var1);
            GlobToRegex.access$400(var1, GlobToRegex.access$600());
            return;
         default:
            GlobToRegex.access$900(var1, var2);
      }
   }

   @Override
   public String toString() {
      return "NORMAL";
   }
}
