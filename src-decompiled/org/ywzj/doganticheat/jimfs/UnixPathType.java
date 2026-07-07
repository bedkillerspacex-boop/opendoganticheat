package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import java.nio.file.InvalidPathException;
import org.checkerframework.checker.nullness.qual.Nullable;

final class UnixPathType extends PathType {
   static final PathType INSTANCE = new UnixPathType();

   private UnixPathType() {
      super(false, '/');
   }

   @Override
   public PathType$ParseResult parsePath(String var1) {
      if (var1.isEmpty()) {
         return this.emptyPath();
      }

      checkValid(var1);
      String var2 = var1.startsWith("/") ? "/" : null;
      return new PathType$ParseResult(var2, this.splitter().split(var1));
   }

   private static void checkValid(String var0) {
      int var1 = var0.indexOf(0);
      if (var1 != -1) {
         throw new InvalidPathException(var0, "nul character not allowed", var1);
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
      StringBuilder var4 = new StringBuilder();

      for (String var6 : var2) {
         var4.append('/').append(var6);
      }

      if (var3 || var4.length() == 0) {
         var4.append('/');
      }

      return var4.toString();
   }

   @Override
   public PathType$ParseResult parseUriPath(String var1) {
      Preconditions.checkArgument(var1.startsWith("/"), "uriPath (%s) must start with /", var1);
      return this.parsePath(var1);
   }
}
