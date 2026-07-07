package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Handler extends URLStreamHandler {
   private static final String JAVA_PROTOCOL_HANDLER_PACKAGES = "java.protocol.handler.pkgs";

   static void register() {
      register(Handler.class);
   }

   static void register(Class var0) {
      Preconditions.checkArgument("Handler".equals(var0.getSimpleName()));
      String var1 = var0.getPackage().getName();
      int var2 = var1.lastIndexOf(46);
      Preconditions.checkArgument(var2 > 0, "package for Handler (%s) must have a parent package", var1);
      String var3 = var1.substring(0, var2);
      String var4 = System.getProperty("java.protocol.handler.pkgs");
      if (var4 == null) {
         var4 = var3;
      } else {
         var4 = var4 + "|" + var3;
      }

      System.setProperty("java.protocol.handler.pkgs", var4);
   }

   @Override
   protected URLConnection openConnection(URL var1) {
      return new PathURLConnection(var1);
   }

   @Override
   protected @Nullable InetAddress getHostAddress(URL var1) {
      return null;
   }
}
