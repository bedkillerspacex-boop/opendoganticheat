package org.ywzj.doganticheat.jimfs;

class GlobToRegex$6 extends GlobToRegex$State {
   GlobToRegex$6() {
      super(null);
   }

   @Override
   void process(GlobToRegex var1, char var2) {
      switch (var2) {
         case '*':
            GlobToRegex.access$400(var1, GlobToRegex.access$700());
            break;
         case ',':
            GlobToRegex.access$2000(var1);
            break;
         case '?':
            GlobToRegex.access$100(var1);
            break;
         case '[':
            GlobToRegex.access$200(var1);
            GlobToRegex.access$400(var1, GlobToRegex.access$300());
            break;
         case '\\':
            GlobToRegex.access$400(var1, GlobToRegex.access$800());
            break;
         case '{':
            throw GlobToRegex.access$1100(var1, "{ not allowed in subpattern group");
         case '}':
            GlobToRegex.access$1900(var1);
            GlobToRegex.access$1000(var1);
            break;
         default:
            GlobToRegex.access$900(var1, var2);
      }
   }

   @Override
   void finish(GlobToRegex var1) {
      throw GlobToRegex.access$1100(var1, "Unclosed {");
   }

   @Override
   public String toString() {
      return "CURLY_BRACE";
   }
}
