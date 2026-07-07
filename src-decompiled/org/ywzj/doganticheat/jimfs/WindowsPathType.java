package org.ywzj.doganticheat.jimfs;

import java.nio.file.InvalidPathException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.checkerframework.checker.nullness.qual.Nullable;

final class WindowsPathType extends PathType {
   static final WindowsPathType INSTANCE = new WindowsPathType();
   private static final Pattern WORKING_DIR_WITH_DRIVE = Pattern.compile("^[a-zA-Z]:([^\\\\].*)?$");
   private static final Pattern TRAILING_SPACES = Pattern.compile("[ ]+(\\\\|$)");
   private static final Pattern UNC_ROOT = Pattern.compile("^(\\\\\\\\)([^\\\\]+)?(\\\\[^\\\\]+)?");
   private static final Pattern DRIVE_LETTER_ROOT = Pattern.compile("^[a-zA-Z]:\\\\");

   private WindowsPathType() {
      super(true, '\\', '/');
   }

   @Override
   public PathType$ParseResult parsePath(String var1) {
      String var2 = var1;
      var1 = var1.replace('/', '\\');
      if (WORKING_DIR_WITH_DRIVE.matcher(var1).matches()) {
         throw new InvalidPathException(
            var2, "Jimfs does not currently support the Windows syntax for a relative path on a specific drive (e.g. \"C:foo\\bar\")"
         );
      }

      String var3;
      if (var1.startsWith("\\\\")) {
         var3 = this.parseUncRoot(var1, var2);
      } else {
         if (var1.startsWith("\\")) {
            throw new InvalidPathException(
               var2, "Jimfs does not currently support the Windows syntax for an absolute path on the current drive (e.g. \"\\foo\\bar\")"
            );
         }

         var3 = this.parseDriveRoot(var1);
      }

      int var4 = var3 != null && var3.length() <= 3 ? var3.length() : 0;

      for (int var5 = var4; var5 < var1.length(); var5++) {
         char var6 = var1.charAt(var5);
         if (isReserved(var6)) {
            throw new InvalidPathException(var2, "Illegal char <" + var6 + ">", var5);
         }
      }

      Matcher var8 = TRAILING_SPACES.matcher(var1);
      if (var8.find()) {
         throw new InvalidPathException(var2, "Trailing char < >", var8.start());
      }

      if (var3 != null) {
         var1 = var1.substring(var3.length());
         if (!var3.endsWith("\\")) {
            var3 = var3 + "\\";
         }
      }

      return new PathType$ParseResult(var3, this.splitter().split(var1));
   }

   private String parseUncRoot(String var1, String var2) {
      Matcher var3 = UNC_ROOT.matcher(var1);
      if (var3.find()) {
         String var4 = var3.group(2);
         if (var4 == null) {
            throw new InvalidPathException(var2, "UNC path is missing hostname");
         } else {
            String var5 = var3.group(3);
            if (var5 == null) {
               throw new InvalidPathException(var2, "UNC path is missing sharename");
            } else {
               return var1.substring(var3.start(), var3.end());
            }
         }
      } else {
         throw new InvalidPathException(var2, "Invalid UNC path");
      }
   }

   private @Nullable String parseDriveRoot(String var1) {
      Matcher var2 = DRIVE_LETTER_ROOT.matcher(var1);
      return var2.find() ? var1.substring(var2.start(), var2.end()) : null;
   }

   private static boolean isReserved(char var0) {
      switch (var0) {
         case '"':
         case '*':
         case ':':
         case '<':
         case '>':
         case '?':
         case '|':
            return true;
         default:
            return var0 <= 31;
      }
   }

   @Override
   public String toString(@Nullable String var1, Iterable var2) {
      StringBuilder var3 = new StringBuilder();
      if (var1 != null) {
         var3.append(var1);
      }

      this.joiner().appendTo(var3, var2);
      return var3.toString();
   }

   @Override
   public String toUriPath(String var1, Iterable var2, boolean var3) {
      if (var1.startsWith("\\\\")) {
         var1 = var1.replace('\\', '/');
      } else {
         var1 = "/" + var1.replace('\\', '/');
      }

      StringBuilder var4 = new StringBuilder();
      var4.append(var1);
      Iterator var5 = var2.iterator();
      if (var5.hasNext()) {
         var4.append((String)var5.next());

         while (var5.hasNext()) {
            var4.append('/').append((String)var5.next());
         }
      }

      if (var3 && var4.charAt(var4.length() - 1) != '/') {
         var4.append('/');
      }

      return var4.toString();
   }

   @Override
   public PathType$ParseResult parseUriPath(String var1) {
      var1 = var1.replace('/', '\\');
      if (var1.charAt(0) == '\\' && var1.charAt(1) != '\\') {
         var1 = var1.substring(1);
      }

      return this.parsePath(var1);
   }
}
