package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.ClosedDirectoryStreamException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.SecureDirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.util.Iterator;
import java.util.Set;

final class JimfsSecureDirectoryStream implements SecureDirectoryStream {
   private final FileSystemView view;
   private final Filter filter;
   private final FileSystemState fileSystemState;
   private boolean open = true;
   private Iterator iterator = new JimfsSecureDirectoryStream$DirectoryIterator(this, null);
   public static final Filter ALWAYS_TRUE_FILTER = new JimfsSecureDirectoryStream$1();

   public JimfsSecureDirectoryStream(FileSystemView var1, Filter var2, FileSystemState var3) {
      this.view = (FileSystemView)Preconditions.checkNotNull(var1);
      this.filter = (Filter)Preconditions.checkNotNull(var2);
      this.fileSystemState = var3;
      var3.register(this);
   }

   private JimfsPath path() {
      return this.view.getWorkingDirectoryPath();
   }

   @Override
   public synchronized Iterator iterator() {
      this.checkOpen();
      Iterator var1 = this.iterator;
      Preconditions.checkState(var1 != null, "iterator() has already been called once");
      this.iterator = null;
      return var1;
   }

   @Override
   public synchronized void close() {
      this.open = false;
      this.fileSystemState.unregister(this);
   }

   protected synchronized void checkOpen() {
      if (!this.open) {
         throw new ClosedDirectoryStreamException();
      }
   }

   public SecureDirectoryStream newDirectoryStream(Path var1, LinkOption... var2) {
      this.checkOpen();
      JimfsPath var3 = checkPath(var1);
      return (SecureDirectoryStream)this.view.newDirectoryStream(var3, ALWAYS_TRUE_FILTER, Options.getLinkOptions(var2), this.path().resolve(var3));
   }

   public SeekableByteChannel newByteChannel(Path var1, Set var2, FileAttribute... var3) {
      this.checkOpen();
      JimfsPath var4 = checkPath(var1);
      ImmutableSet var5 = Options.getOptionsForChannel(var2);
      return new JimfsFileChannel(this.view.getOrCreateRegularFile(var4, var5), var5, this.fileSystemState);
   }

   public void deleteFile(Path var1) {
      this.checkOpen();
      JimfsPath var2 = checkPath(var1);
      this.view.deleteFile(var2, FileSystemView$DeleteMode.NON_DIRECTORY_ONLY);
   }

   public void deleteDirectory(Path var1) {
      this.checkOpen();
      JimfsPath var2 = checkPath(var1);
      this.view.deleteFile(var2, FileSystemView$DeleteMode.DIRECTORY_ONLY);
   }

   public void move(Path var1, SecureDirectoryStream var2, Path var3) {
      this.checkOpen();
      JimfsPath var4 = checkPath(var1);
      JimfsPath var5 = checkPath(var3);
      if (!(var2 instanceof JimfsSecureDirectoryStream)) {
         throw new ProviderMismatchException("targetDir isn't a secure directory stream associated with this file system");
      }

      JimfsSecureDirectoryStream var6 = (JimfsSecureDirectoryStream)var2;
      this.view.copy(var4, var6.view, var5, ImmutableSet.of(), true);
   }

   @Override
   public FileAttributeView getFileAttributeView(Class var1) {
      return this.getFileAttributeView(this.path().getFileSystem().getPath("."), var1);
   }

   public FileAttributeView getFileAttributeView(Path var1, Class var2, LinkOption... var3) {
      this.checkOpen();
      JimfsPath var4 = checkPath(var1);
      ImmutableSet var5 = Options.getLinkOptions(var3);
      return this.view.getFileAttributeView(new JimfsSecureDirectoryStream$2(this, var4, var5), var2);
   }

   private static JimfsPath checkPath(Path var0) {
      if (var0 instanceof JimfsPath) {
         return (JimfsPath)var0;
      } else {
         throw new ProviderMismatchException("path " + var0 + " is not associated with a Jimfs file system");
      }
   }
}
