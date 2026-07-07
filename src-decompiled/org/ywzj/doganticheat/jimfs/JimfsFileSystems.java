package org.ywzj.doganticheat.jimfs;

import com.google.common.collect.UnmodifiableIterator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashMap;

final class JimfsFileSystems {
   private static final Runnable DO_NOTHING = new JimfsFileSystems$1();

   private JimfsFileSystems() {
   }

   private static Runnable removeFileSystemRunnable(URI var0) {
      if (Jimfs.systemProvider == null) {
         return DO_NOTHING;
      }

      try {
         Method var1 = Jimfs.systemProvider.getClass().getDeclaredMethod("removeFileSystemRunnable", URI.class);
         return (Runnable)var1.invoke(null, var0);
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException var2) {
         throw new RuntimeException("Unable to get Runnable for removing the FileSystem from the cache when it is closed", var2);
      }
   }

   public static JimfsFileSystem newFileSystem(JimfsFileSystemProvider var0, URI var1, Configuration var2) {
      PathService var3 = new PathService(var2);
      FileSystemState var4 = new FileSystemState(var2.fileTimeSource, removeFileSystemRunnable(var1));
      JimfsFileStore var5 = createFileStore(var2, var3, var4);
      FileSystemView var6 = createDefaultView(var2, var5, var3);
      WatchServiceConfiguration var7 = var2.watchServiceConfig;
      JimfsFileSystem var8 = new JimfsFileSystem(var0, var1, var5, var3, var6, var7);
      var3.setFileSystem(var8);
      return var8;
   }

   private static JimfsFileStore createFileStore(Configuration var0, PathService var1, FileSystemState var2) {
      AttributeService var3 = new AttributeService(var0);
      HeapDisk var4 = new HeapDisk(var0);
      FileFactory var5 = new FileFactory(var4, var0.fileTimeSource);
      HashMap var6 = new HashMap();
      UnmodifiableIterator var7 = var0.roots.iterator();

      while (var7.hasNext()) {
         String var8 = (String)var7.next();
         JimfsPath var9 = var1.parsePath(var8);
         if (!var9.isAbsolute() && var9.getNameCount() == 0) {
            throw new IllegalArgumentException("Invalid root path: " + var8);
         }

         Name var10 = var9.root();
         Directory var11 = var5.createRootDirectory(var10);
         var3.setInitialAttributes(var11);
         var6.put(var10, var11);
      }

      return new JimfsFileStore(new FileTree(var6), var5, var4, var3, var0.supportedFeatures, var2);
   }

   private static FileSystemView createDefaultView(Configuration var0, JimfsFileStore var1, PathService var2) {
      JimfsPath var3 = var2.parsePath(var0.workingDirectory);
      Directory var4 = var1.getRoot(var3.root());
      if (var4 == null) {
         throw new IllegalArgumentException("Invalid working dir path: " + var3);
      }

      UnmodifiableIterator var5 = var3.names().iterator();

      while (var5.hasNext()) {
         Name var6 = (Name)var5.next();
         Directory var7 = (Directory)var1.directoryCreator().get();
         var1.setInitialAttributes(var7);
         var4.link(var6, var7);
         var4 = var7;
      }

      return new FileSystemView(var1, var4, var3);
   }
}
