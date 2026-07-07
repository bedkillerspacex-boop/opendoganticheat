package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.StandardOpenOption;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import org.checkerframework.checker.nullness.qual.Nullable;

final class JimfsFileSystemProvider extends FileSystemProvider {
   private static final JimfsFileSystemProvider INSTANCE = new JimfsFileSystemProvider();
   private static final FileAttribute[] NO_ATTRS;

   static JimfsFileSystemProvider instance() {
      return INSTANCE;
   }

   @Override
   public String getScheme() {
      return "jimfs";
   }

   @Override
   public FileSystem newFileSystem(URI var1, Map var2) {
      throw new UnsupportedOperationException("This method should not be called directly;use an overload of Jimfs.newFileSystem() to create a FileSystem.");
   }

   @Override
   public FileSystem newFileSystem(Path var1, Map var2) {
      JimfsPath var3 = checkPath(var1);
      Preconditions.checkNotNull(var2);
      URI var4 = var3.toUri();
      URI var5 = URI.create("jar:" + var4);

      try {
         return FileSystems.newFileSystem(var5, var2);
      } catch (Exception var7) {
         throw new UnsupportedOperationException(var7);
      }
   }

   @Override
   public FileSystem getFileSystem(URI var1) {
      throw new UnsupportedOperationException("This method should not be called directly; use FileSystems.getFileSystem(URI) instead.");
   }

   private static JimfsFileSystem getFileSystem(Path var0) {
      return (JimfsFileSystem)checkPath(var0).getFileSystem();
   }

   @Override
   public Path getPath(URI var1) {
      throw new UnsupportedOperationException("This method should not be called directly; use Paths.get(URI) instead.");
   }

   private static JimfsPath checkPath(Path var0) {
      if (var0 instanceof JimfsPath) {
         return (JimfsPath)var0;
      } else {
         throw new ProviderMismatchException("path " + var0 + " is not associated with a Jimfs file system");
      }
   }

   private static FileSystemView getDefaultView(JimfsPath var0) {
      return getFileSystem(var0).getDefaultView();
   }

   @Override
   public FileChannel newFileChannel(Path var1, Set var2, FileAttribute... var3) {
      JimfsPath var4 = checkPath(var1);
      if (!var4.getJimfsFileSystem().getFileStore().supportsFeature(Feature.FILE_CHANNEL)) {
         throw new UnsupportedOperationException();
      } else {
         return this.newJimfsFileChannel(var4, var2, var3);
      }
   }

   private JimfsFileChannel newJimfsFileChannel(JimfsPath var1, Set var2, FileAttribute... var3) {
      ImmutableSet var4 = Options.getOptionsForChannel(var2);
      FileSystemView var5 = getDefaultView(var1);
      RegularFile var6 = var5.getOrCreateRegularFile(var1, var4, var3);
      return new JimfsFileChannel(var6, var4, var5.state());
   }

   @Override
   public SeekableByteChannel newByteChannel(Path var1, Set var2, FileAttribute... var3) {
      JimfsPath var4 = checkPath(var1);
      JimfsFileChannel var5 = this.newJimfsFileChannel(var4, var2, var3);
      return var4.getJimfsFileSystem().getFileStore().supportsFeature(Feature.FILE_CHANNEL) ? var5 : new DowngradedSeekableByteChannel(var5);
   }

   @Override
   public AsynchronousFileChannel newAsynchronousFileChannel(Path var1, Set var2, @Nullable ExecutorService var3, FileAttribute... var4) {
      JimfsFileChannel var5 = (JimfsFileChannel)this.newFileChannel(var1, var2, var4);
      if (var3 == null) {
         JimfsFileSystem var6 = (JimfsFileSystem)var1.getFileSystem();
         var3 = var6.getDefaultThreadPool();
      }

      return var5.asAsynchronousFileChannel(var3);
   }

   @Override
   public InputStream newInputStream(Path var1, OpenOption... var2) {
      JimfsPath var3 = checkPath(var1);
      ImmutableSet var4 = Options.getOptionsForInputStream(var2);
      FileSystemView var5 = getDefaultView(var3);
      RegularFile var6 = var5.getOrCreateRegularFile(var3, var4, NO_ATTRS);
      return new JimfsInputStream(var6, var5.state());
   }

   @Override
   public OutputStream newOutputStream(Path var1, OpenOption... var2) {
      JimfsPath var3 = checkPath(var1);
      ImmutableSet var4 = Options.getOptionsForOutputStream(var2);
      FileSystemView var5 = getDefaultView(var3);
      RegularFile var6 = var5.getOrCreateRegularFile(var3, var4, NO_ATTRS);
      return new JimfsOutputStream(var6, var4.contains(StandardOpenOption.APPEND), var5.state());
   }

   @Override
   public DirectoryStream newDirectoryStream(Path var1, Filter var2) {
      JimfsPath var3 = checkPath(var1);
      return getDefaultView(var3).newDirectoryStream(var3, var2, Options.FOLLOW_LINKS, var3);
   }

   @Override
   public void createDirectory(Path var1, FileAttribute... var2) {
      JimfsPath var3 = checkPath(var1);
      FileSystemView var4 = getDefaultView(var3);
      var4.createDirectory(var3, var2);
   }

   @Override
   public void createLink(Path var1, Path var2) {
      JimfsPath var3 = checkPath(var1);
      JimfsPath var4 = checkPath(var2);
      Preconditions.checkArgument(var3.getFileSystem().equals(var4.getFileSystem()), "link and existing paths must belong to the same file system instance");
      FileSystemView var5 = getDefaultView(var3);
      var5.link(var3, getDefaultView(var4), var4);
   }

   @Override
   public void createSymbolicLink(Path var1, Path var2, FileAttribute... var3) {
      JimfsPath var4 = checkPath(var1);
      JimfsPath var5 = checkPath(var2);
      Preconditions.checkArgument(var4.getFileSystem().equals(var5.getFileSystem()), "link and target paths must belong to the same file system instance");
      FileSystemView var6 = getDefaultView(var4);
      var6.createSymbolicLink(var4, var5, var3);
   }

   @Override
   public Path readSymbolicLink(Path var1) {
      JimfsPath var2 = checkPath(var1);
      return getDefaultView(var2).readSymbolicLink(var2);
   }

   @Override
   public void delete(Path var1) {
      JimfsPath var2 = checkPath(var1);
      FileSystemView var3 = getDefaultView(var2);
      var3.deleteFile(var2, FileSystemView$DeleteMode.ANY);
   }

   @Override
   public void copy(Path var1, Path var2, CopyOption... var3) {
      this.copy(var1, var2, Options.getCopyOptions(var3), false);
   }

   private void copy(Path var1, Path var2, ImmutableSet var3, boolean var4) {
      JimfsPath var5 = checkPath(var1);
      JimfsPath var6 = checkPath(var2);
      FileSystemView var7 = getDefaultView(var5);
      FileSystemView var8 = getDefaultView(var6);
      var7.copy(var5, var8, var6, var3, var4);
   }

   @Override
   public void move(Path var1, Path var2, CopyOption... var3) {
      this.copy(var1, var2, Options.getMoveOptions(var3), true);
   }

   @Override
   public boolean isSameFile(Path var1, Path var2) {
      if (var1.equals(var2)) {
         return true;
      } else if (var1 instanceof JimfsPath && var2 instanceof JimfsPath) {
         JimfsPath var3 = (JimfsPath)var1;
         JimfsPath var4 = (JimfsPath)var2;
         FileSystemView var5 = getDefaultView(var3);
         FileSystemView var6 = getDefaultView(var4);
         return var5.isSameFile(var3, var6, var4);
      } else {
         return false;
      }
   }

   @Override
   public boolean isHidden(Path var1) {
      JimfsPath var2 = checkPath(var1);
      FileSystemView var3 = getDefaultView(var2);
      return this.getFileStore(var1).supportsFileAttributeView("dos")
         ? ((DosFileAttributes)var3.readAttributes(var2, DosFileAttributes.class, Options.NOFOLLOW_LINKS)).isHidden()
         : var1.getNameCount() > 0 && var1.getFileName().toString().startsWith(".");
   }

   @Override
   public FileStore getFileStore(Path var1) {
      return getFileSystem(var1).getFileStore();
   }

   @Override
   public void checkAccess(Path var1, AccessMode... var2) {
      JimfsPath var3 = checkPath(var1);
      getDefaultView(var3).checkAccess(var3);
   }

   @Override
   public @Nullable FileAttributeView getFileAttributeView(Path var1, Class var2, LinkOption... var3) {
      JimfsPath var4 = checkPath(var1);
      return getDefaultView(var4).getFileAttributeView(var4, var2, Options.getLinkOptions(var3));
   }

   @Override
   public BasicFileAttributes readAttributes(Path var1, Class var2, LinkOption... var3) {
      JimfsPath var4 = checkPath(var1);
      return getDefaultView(var4).readAttributes(var4, var2, Options.getLinkOptions(var3));
   }

   @Override
   public Map readAttributes(Path var1, String var2, LinkOption... var3) {
      JimfsPath var4 = checkPath(var1);
      return getDefaultView(var4).readAttributes(var4, var2, Options.getLinkOptions(var3));
   }

   @Override
   public void setAttribute(Path var1, String var2, Object var3, LinkOption... var4) {
      JimfsPath var5 = checkPath(var1);
      getDefaultView(var5).setAttribute(var5, var2, var3, Options.getLinkOptions(var4));
   }

   static {
      try {
         Handler.register();
      } catch (Throwable var1) {
      }

      NO_ATTRS = new FileAttribute[0];
   }
}
