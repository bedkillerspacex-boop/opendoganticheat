package org.ywzj.doganticheat.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipException;
import org.ywzj.doganticheat.jimfs.Configuration;
import org.ywzj.doganticheat.jimfs.Jimfs;

public class EncryptedFileSystem {
   private static final MethodHandle ZIPFS_CH;
   private static final MethodHandle FCI_UNINTERUPTIBLE;
   private static final Constructor EFSM_CONSTRUCTOR;
   public static final HashMap MANIFESTS = new HashMap();

   public static FileSystem newFileSystem(Path var0) {
      try {
         return FileSystems.newFileSystem(var0);
      } catch (ZipException var11) {
         FileSystem var2 = Jimfs.newFileSystem(Configuration.windows());
         Path var3 = var2.getPath("C:\\mods\\" + var0.getFileName().toString());
         Files.createDirectories(var3.getParent());
         Files.createFile(var3);
         byte[] var4 = c.d(Files.readAllBytes(var0));
         Files.write(var3, var4, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
         FileSystem var5 = FileSystems.newFileSystem(URI.create("jar:" + var3.toUri()), Collections.singletonMap("create", "true"));

         try (JarInputStream var6 = new JarInputStream(new ByteArrayInputStream(var4))) {
            MANIFESTS.put(var0.toString(), var6.getManifest());
         }

         return var5;
      }
   }

   public static Optional openFileSystem(Path var0) {
      try {
         FileSystem var1 = newFileSystem(var0);
         SeekableByteChannel var2 = (SeekableByteChannel)ZIPFS_CH.invoke((FileSystem)var1);

         try {
            if (var2 instanceof FileChannel) {
               FCI_UNINTERUPTIBLE.invoke((SeekableByteChannel)var2);
            }
         } catch (Exception var4) {
         }

         return Optional.of(EFSM_CONSTRUCTOR.newInstance(var0, var1, var2));
      } catch (Throwable var5) {
         var5.printStackTrace();
         return Optional.empty();
      }
   }

   public static Attributes getJarAttributes(File var0) {
      try (JarFile var1 = new JarFile(var0)) {
         Manifest var7 = var1.getManifest();
         if (var7 != null) {
            return var7.getMainAttributes();
         }
      } catch (IOException var6) {
         Manifest var2 = (Manifest)MANIFESTS.get(var0.getPath());
         if (var2 != null) {
            return var2.getMainAttributes();
         }
      }

      return null;
   }

   static {
      try {
         Field var0 = Lookup.class.getDeclaredField("IMPL_LOOKUP");
         var0.setAccessible(true);
         Lookup var1 = (Lookup)var0.get(null);
         Class var2 = Class.forName("jdk.nio.zipfs.ZipPath");
         var2 = Class.forName("jdk.nio.zipfs.ZipFileSystem");
         ZIPFS_CH = var1.findGetter(var2, "ch", SeekableByteChannel.class);
         var2 = Class.forName("sun.nio.ch.FileChannelImpl");
         FCI_UNINTERUPTIBLE = var1.findSpecial(var2, "setUninterruptible", MethodType.methodType(void.class), var2);
         if (b.a()) {
            b.a("addOpensToAllUnnamed", "cpw.mods.securejarhandler", "cpw.mods.niofs.union", null);
         }

         Class var3 = Class.forName("cpw.mods.niofs.union.UnionFileSystem$EmbeddedFileSystemMetadata");
         EFSM_CONSTRUCTOR = var3.getDeclaredConstructor(Path.class, FileSystem.class, SeekableByteChannel.class);
         EFSM_CONSTRUCTOR.setAccessible(true);
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }
   }
}
