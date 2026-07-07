package org.ywzj.doganticheat.jimfs;

import com.google.auto.service.AutoService;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.MapMaker;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

@AutoService(FileSystemProvider.class)
public final class SystemJimfsFileSystemProvider extends FileSystemProvider {
   static final String FILE_SYSTEM_KEY = "fileSystem";
   private static final ConcurrentMap fileSystems = new MapMaker().weakValues().makeMap();

   @Override
   public String getScheme() {
      return "jimfs";
   }

   @Override
   public FileSystem newFileSystem(URI var1, Map var2) {
      Preconditions.checkArgument(var1.getScheme().equalsIgnoreCase("jimfs"), "uri (%s) scheme must be '%s'", var1, "jimfs");
      Preconditions.checkArgument(isValidFileSystemUri(var1), "uri (%s) may not have a path, query or fragment", var1);
      Preconditions.checkArgument(
         var2.get("fileSystem") instanceof FileSystem, "env map (%s) must contain key '%s' mapped to an instance of %s", var2, "fileSystem", FileSystem.class
      );
      FileSystem var3 = (FileSystem)var2.get("fileSystem");
      if (fileSystems.putIfAbsent(var1, var3) != null) {
         throw new FileSystemAlreadyExistsException(var1.toString());
      } else {
         return var3;
      }
   }

   @Override
   public FileSystem getFileSystem(URI var1) {
      FileSystem var2 = (FileSystem)fileSystems.get(var1);
      if (var2 == null) {
         throw new FileSystemNotFoundException(var1.toString());
      } else {
         return var2;
      }
   }

   @Override
   public Path getPath(URI var1) {
      Preconditions.checkArgument("jimfs".equalsIgnoreCase(var1.getScheme()), "uri scheme does not match this provider: %s", var1);
      String var2 = var1.getPath();
      Preconditions.checkArgument(!Strings.isNullOrEmpty(var2), "uri must have a path: %s", var1);
      return toPath(this.getFileSystem(toFileSystemUri(var1)), var1);
   }

   private static boolean isValidFileSystemUri(URI var0) {
      return Strings.isNullOrEmpty(var0.getPath()) && Strings.isNullOrEmpty(var0.getQuery()) && Strings.isNullOrEmpty(var0.getFragment());
   }

   private static URI toFileSystemUri(URI var0) {
      try {
         return new URI(var0.getScheme(), var0.getUserInfo(), var0.getHost(), var0.getPort(), null, null, null);
      } catch (URISyntaxException var2) {
         throw new AssertionError(var2);
      }
   }

   private static Path toPath(FileSystem var0, URI var1) {
      try {
         Method var2 = var0.getClass().getDeclaredMethod("toPath", URI.class);
         return (Path)var2.invoke(var0, var1);
      } catch (NoSuchMethodException var3) {
         throw new IllegalArgumentException("invalid file system: " + var0, var3);
      } catch (InvocationTargetException | IllegalAccessException var4) {
         throw new RuntimeException(var4);
      }
   }

   @Override
   public FileSystem newFileSystem(Path var1, Map var2) {
      FileSystemProvider var3 = var1.getFileSystem().provider();
      return var3.newFileSystem(var1, var2);
   }

   public static Runnable removeFileSystemRunnable(URI var0) {
      return new SystemJimfsFileSystemProvider$1(var0);
   }

   @Override
   public SeekableByteChannel newByteChannel(Path var1, Set var2, FileAttribute... var3) {
      throw new UnsupportedOperationException();
   }

   @Override
   public DirectoryStream newDirectoryStream(Path var1, Filter var2) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void createDirectory(Path var1, FileAttribute... var2) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void delete(Path var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void copy(Path var1, Path var2, CopyOption... var3) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void move(Path var1, Path var2, CopyOption... var3) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean isSameFile(Path var1, Path var2) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean isHidden(Path var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public FileStore getFileStore(Path var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void checkAccess(Path var1, AccessMode... var2) {
      throw new UnsupportedOperationException();
   }

   @Override
   public FileAttributeView getFileAttributeView(Path var1, Class var2, LinkOption... var3) {
      throw new UnsupportedOperationException();
   }

   @Override
   public BasicFileAttributes readAttributes(Path var1, Class var2, LinkOption... var3) {
      throw new UnsupportedOperationException();
   }

   @Override
   public Map readAttributes(Path var1, String var2, LinkOption... var3) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setAttribute(Path var1, String var2, Object var3, LinkOption... var4) {
      throw new UnsupportedOperationException();
   }
}
