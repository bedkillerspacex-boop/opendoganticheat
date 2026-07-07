package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.PatternSyntaxException;

final class GlobToRegex {
   private static final InternalCharMatcher REGEX_RESERVED = InternalCharMatcher.anyOf("^$.?+*\\[]{}()");
   private final String glob;
   private final String separators;
   private final InternalCharMatcher separatorMatcher;
   private final StringBuilder builder = new StringBuilder();
   private final Deque states = new ArrayDeque();
   private int index;
   private static final GlobToRegex$State NORMAL = new GlobToRegex$1();
   private static final GlobToRegex$State ESCAPE = new GlobToRegex$2();
   private static final GlobToRegex$State STAR = new GlobToRegex$3();
   private static final GlobToRegex$State BRACKET_FIRST_CHAR = new GlobToRegex$4();
   private static final GlobToRegex$State BRACKET = new GlobToRegex$5();
   private static final GlobToRegex$State CURLY_BRACE = new GlobToRegex$6();

   public static String toRegex(String var0, String var1) {
      return new GlobToRegex(var0, var1).convert();
   }

   private GlobToRegex(String var1, String var2) {
      this.glob = (String)Preconditions.checkNotNull(var1);
      this.separators = var2;
      this.separatorMatcher = InternalCharMatcher.anyOf(var2);
   }

   private String convert() {
      this.pushState(NORMAL);

      for (this.index = 0; this.index < this.glob.length(); this.index++) {
         this.currentState().process(this, this.glob.charAt(this.index));
      }

      this.currentState().finish(this);
      return this.builder.toString();
   }

   private void pushState(GlobToRegex$State var1) {
      this.states.push(var1);
   }

   private void popState() {
      this.states.pop();
   }

   private GlobToRegex$State currentState() {
      return (GlobToRegex$State)this.states.peek();
   }

   private PatternSyntaxException syntaxError(String var1) {
      throw new PatternSyntaxException(var1, this.glob, this.index);
   }

   private void appendExact(char var1) {
      this.builder.append(var1);
   }

   private void append(char var1) {
      if (this.separatorMatcher.matches(var1)) {
         this.appendSeparator();
      } else {
         this.appendNormal(var1);
      }
   }

   private void appendNormal(char var1) {
      if (REGEX_RESERVED.matches(var1)) {
         this.builder.append('\\');
      }

      this.builder.append(var1);
   }

   private void appendSeparator() {
      if (this.separators.length() == 1) {
         this.appendNormal(this.separators.charAt(0));
      } else {
         this.builder.append('[');

         for (int var1 = 0; var1 < this.separators.length(); var1++) {
            this.appendInBracket(this.separators.charAt(var1));
         }

         this.builder.append("]");
      }
   }

   private void appendNonSeparator() {
      this.builder.append("[^");

      for (int var1 = 0; var1 < this.separators.length(); var1++) {
         this.appendInBracket(this.separators.charAt(var1));
      }

      this.builder.append(']');
   }

   private void appendQuestionMark() {
      this.appendNonSeparator();
   }

   private void appendStar() {
      this.appendNonSeparator();
      this.builder.append('*');
   }

   private void appendStarStar() {
      this.builder.append(".*");
   }

   private void appendBracketStart() {
      this.builder.append('[');
      this.appendNonSeparator();
      this.builder.append("&&[");
   }

   private void appendBracketEnd() {
      this.builder.append("]]");
   }

   private void appendInBracket(char var1) {
      if (var1 == '\\') {
         this.builder.append('\\');
      }

      this.builder.append(var1);
   }

   private void appendCurlyBraceStart() {
      this.builder.append('(');
   }

   private void appendSubpatternSeparator() {
      this.builder.append('|');
   }

   private void appendCurlyBraceEnd() {
      this.builder.append(')');
   }
}
