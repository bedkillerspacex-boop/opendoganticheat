package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Ascii;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.ImmutableListMultimap.Builder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.checkerframework.checker.nullness.qual.Nullable;

final class PathURLConnection extends URLConnection {
   private static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";
   private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
   private InputStream stream;
   private ImmutableListMultimap headers = ImmutableListMultimap.of();

   PathURLConnection(URL var1) {
      super((URL)Preconditions.checkNotNull(var1));
   }

   @Override
   public void connect() {
      if (this.stream == null) {
         Path var1 = Paths.get(toUri(this.url));
         long var2;
         if (Files.isDirectory(var1)) {
            StringBuilder var4 = new StringBuilder();
            DirectoryStream var5 = Files.newDirectoryStream(var1);

            try {
               for (Path var7 : var5) {
                  var4.append(var7.getFileName()).append('\n');
               }
            } catch (Throwable var9) {
               if (var5 != null) {
                  try {
                     var5.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }
               }

               throw var9;
            }

            if (var5 != null) {
               var5.close();
            }

            byte[] var11 = var4.toString().getBytes(StandardCharsets.UTF_8);
            this.stream = new ByteArrayInputStream(var11);
            var2 = var11.length;
         } else {
            this.stream = Files.newInputStream(var1);
            var2 = Files.size(var1);
         }

         FileTime var10 = Files.getLastModifiedTime(var1);
         String var12 = (String)MoreObjects.firstNonNull(Files.probeContentType(var1), "application/octet-stream");
         Builder var13 = ImmutableListMultimap.builder();
         var13.put("content-length", "" + var2);
         var13.put("content-type", var12);
         if (var10 != null) {
            SimpleDateFormat var14 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
            var14.setTimeZone(TimeZone.getTimeZone("GMT"));
            var13.put("last-modified", var14.format(new Date(var10.toMillis())));
         }

         this.headers = var13.build();
      }
   }

   private static URI toUri(URL var0) {
      try {
         return var0.toURI();
      } catch (URISyntaxException var2) {
         throw new IOException("URL " + var0 + " cannot be converted to a URI", var2);
      }
   }

   @Override
   public InputStream getInputStream() {
      this.connect();
      return this.stream;
   }

   @Override
   public Map getHeaderFields() {
      try {
         this.connect();
      } catch (IOException var2) {
         return ImmutableMap.of();
      }

      return this.headers.asMap();
   }

   @Override
   public @Nullable String getHeaderField(String var1) {
      try {
         this.connect();
      } catch (IOException var3) {
         return null;
      }

      return (String)Iterables.getFirst(this.headers.get(Ascii.toLowerCase(var1)), null);
   }
}
