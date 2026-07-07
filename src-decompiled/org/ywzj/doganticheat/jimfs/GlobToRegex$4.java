package org.ywzj.doganticheat.jimfs;

class GlobToRegex$4 extends GlobToRegex$State {
   GlobToRegex$4() {
      super(null);
   }

   @Override
   void process(GlobToRegex var1, char var2) {
      if (var2 == ']') {
         throw GlobToRegex.access$1100(var1, "Empty []");
      }

      if (var2 == '!') {
         GlobToRegex.access$1500(var1, '^');
      } else if (var2 == '-') {
         GlobToRegex.access$1500(var1, var2);
      } else {
         GlobToRegex.access$1600(var1, var2);
      }

      GlobToRegex.access$1000(var1);
      GlobToRegex.access$400(var1, GlobToRegex.access$1700());
   }

   @Override
   void finish(GlobToRegex var1) {
      throw GlobToRegex.access$1100(var1, "Unclosed [");
   }

   @Override
   public String toString() {
      return "BRACKET_FIRST_CHAR";
   }
}
