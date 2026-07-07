package org.ywzj.doganticheat.jimfs;

import java.util.Arrays;

final class InternalCharMatcher {
   private final char[] chars;

   public static InternalCharMatcher anyOf(String var0) {
      return new InternalCharMatcher(var0);
   }

   private InternalCharMatcher(String var1) {
      this.chars = var1.toCharArray();
      Arrays.sort(this.chars);
   }

   public boolean matches(char var1) {
      return Arrays.binarySearch(this.chars, var1) >= 0;
   }
}
