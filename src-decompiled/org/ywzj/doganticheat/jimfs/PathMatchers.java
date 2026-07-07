package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.nio.file.PathMatcher;

final class PathMatchers {
   private PathMatchers() {
   }

   public static PathMatcher getPathMatcher(String var0, String var1, ImmutableSet var2) {
      int var3 = var0.indexOf(58);
      Preconditions.checkArgument(var3 > 0, "Must be of the form 'syntax:pattern': %s", var0);
      String var4 = Ascii.toLowerCase(var0.substring(0, var3));
      String var5 = var0.substring(var3 + 1);
      switch (var4) {
         case "glob":
            var5 = GlobToRegex.toRegex(var5, var1);
         case "regex":
            return fromRegex(var5, var2);
         default:
            throw new UnsupportedOperationException("Invalid syntax: " + var0);
      }
   }

   private static PathMatcher fromRegex(String var0, Iterable var1) {
      return new PathMatchers$RegexPathMatcher(PathNormalization.compilePattern(var0, var1), null);
   }
}
