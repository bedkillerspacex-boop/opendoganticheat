package org.ywzj.doganticheat.jimfs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.ProviderNotFoundException;
import java.nio.file.spi.FileSystemProvider;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Jimfs {
   public static final String URI_SCHEME = "jimfs";
   private static final Logger LOGGER = Logger.getLogger(Jimfs.class.getName());
   static final @Nullable FileSystemProvider systemProvider = getSystemJimfsProvider();

   private Jimfs() {
   }

   public static FileSystem newFileSystem() {
      return newFileSystem(newRandomFileSystemName());
   }

   public static FileSystem newFileSystem(String var0) {
      return newFileSystem(var0, Configuration.forCurrentPlatform());
   }

   public static FileSystem newFileSystem(Configuration var0) {
      return newFileSystem(newRandomFileSystemName(), var0);
   }

   public static FileSystem newFileSystem(String var0, Configuration var1) {
      try {
         URI var2 = new URI("jimfs", var0, null, null);
         return newFileSystem(var2, var1);
      } catch (URISyntaxException var3) {
         throw new IllegalArgumentException(var3);
      }
   }

   @VisibleForTesting
   static FileSystem newFileSystem(URI var0, Configuration var1) {
      Preconditions.checkArgument("jimfs".equals(var0.getScheme()), "uri (%s) must have scheme %s", var0, "jimfs");

      try {
         JimfsFileSystem var2 = JimfsFileSystems.newFileSystem(JimfsFileSystemProvider.instance(), var0, var1);

         try {
            ImmutableMap var3 = ImmutableMap.of("fileSystem", var2);
            FileSystems.newFileSystem(var0, var3, SystemJimfsFileSystemProvider.class.getClassLoader());
         } catch (ProviderNotFoundException | ServiceConfigurationError var4) {
         }

         return var2;
      } catch (IOException var5) {
         throw new AssertionError(var5);
      }
   }

   private static @Nullable FileSystemProvider getSystemJimfsProvider() {
      try {
         for (FileSystemProvider var1 : FileSystemProvider.installedProviders()) {
            if (var1.getScheme().equals("jimfs")) {
               return var1;
            }
         }

         for (FileSystemProvider var2 : ServiceLoader.load(FileSystemProvider.class, SystemJimfsFileSystemProvider.class.getClassLoader())) {
            if (var2.getScheme().equals("jimfs")) {
               return var2;
            }
         }
      } catch (ProviderNotFoundException | ServiceConfigurationError var3) {
         LOGGER.log(
            Level.INFO,
            "An exception occurred when attempting to find the system-loaded FileSystemProvider for Jimfs. This likely means that your environment does not support loading services via ServiceLoader or is not configured correctly. This does not prevent using Jimfs, but it will mean that methods that look up via URI such as Paths.get(URI) cannot work.",
            var3
         );
      }

      return null;
   }

   private static String newRandomFileSystemName() {
      return UUID.randomUUID().toString();
   }
}
