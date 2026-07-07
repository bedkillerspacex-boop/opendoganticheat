package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class PathType {
   private final boolean allowsMultipleRoots;
   private final String separator;
   private final String otherSeparators;
   private final Joiner joiner;
   private final Splitter splitter;
   private static final char[] regexReservedChars = "^$.?+*\\[]{}()".toCharArray();

   public static PathType unix() {
      return UnixPathType.INSTANCE;
   }

   public static PathType windows() {
      return WindowsPathType.INSTANCE;
   }

   protected PathType(boolean var1, char var2, char... var3) {
      this.separator = String.valueOf(var2);
      this.allowsMultipleRoots = var1;
      this.otherSeparators = String.valueOf(var3);
      this.joiner = Joiner.on(var2);
      this.splitter = createSplitter(var2, var3);
   }

   private static boolean isRegexReserved(char var0) {
      return Arrays.binarySearch(regexReservedChars, var0) >= 0;
   }

   private static Splitter createSplitter(char var0, char... var1) {
      if (var1.length == 0) {
         return Splitter.on(var0).omitEmptyStrings();
      }

      StringBuilder var2 = new StringBuilder();
      var2.append("[");
      appendToRegex(var0, var2);

      for (char var6 : var1) {
         appendToRegex(var6, var2);
      }

      var2.append("]");
      return Splitter.onPattern(var2.toString()).omitEmptyStrings();
   }

   private static void appendToRegex(char var0, StringBuilder var1) {
      if (isRegexReserved(var0)) {
         var1.append("\\");
      }

      var1.append(var0);
   }

   public final boolean allowsMultipleRoots() {
      return this.allowsMultipleRoots;
   }

   public final String getSeparator() {
      return this.separator;
   }

   public final String getOtherSeparators() {
      return this.otherSeparators;
   }

   public final Joiner joiner() {
      return this.joiner;
   }

   public final Splitter splitter() {
      return this.splitter;
   }

   protected final PathType$ParseResult emptyPath() {
      return new PathType$ParseResult(null, ImmutableList.of(""));
   }

   public abstract PathType$ParseResult parsePath(String var1);

   @Override
   public String toString() {
      return this.getClass().getSimpleName();
   }

   public abstract String toString(@Nullable String var1, Iterable var2);

   protected abstract String toUriPath(String var1, Iterable var2, boolean var3);

   protected abstract PathType$ParseResult parseUriPath(String var1);

   public final URI toUri(URI var1, String var2, Iterable var3, boolean var4) {
      String var5 = this.toUriPath(var2, var3, var4);

      try {
         return new URI(var1.getScheme(), var1.getUserInfo(), var1.getHost(), var1.getPort(), var5, null, null);
      } catch (URISyntaxException var7) {
         throw new AssertionError(var7);
      }
   }

   public final PathType$ParseResult fromUri(URI var1) {
      return this.parseUriPath(var1.getPath());
   }

   static {
      Arrays.sort(regexReservedChars);
   }
}
