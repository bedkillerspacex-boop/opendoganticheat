package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import org.checkerframework.checker.nullness.qual.Nullable;

final class FileSystemView {
   private final JimfsFileStore store;
   private final Directory workingDirectory;
   private final JimfsPath workingDirectoryPath;

   public FileSystemView(JimfsFileStore var1, Directory var2, JimfsPath var3) {
      this.store = (JimfsFileStore)Preconditions.checkNotNull(var1);
      this.workingDirectory = (Directory)Preconditions.checkNotNull(var2);
      this.workingDirectoryPath = (JimfsPath)Preconditions.checkNotNull(var3);
   }

   private boolean isSameFileSystem(FileSystemView var1) {
      return this.store == var1.store;
   }

   public FileSystemState state() {
      return this.store.state();
   }

   private FileTime now() {
      return this.state().now();
   }

   public JimfsPath getWorkingDirectoryPath() {
      return this.workingDirectoryPath;
   }

   DirectoryEntry lookUpWithLock(JimfsPath var1, Set var2) {
      this.store.readLock().lock();

      try {
         return this.lookUp(var1, var2);
      } finally {
         this.store.readLock().unlock();
      }
   }

   private DirectoryEntry lookUp(JimfsPath var1, Set var2) {
      return this.store.lookUp(this.workingDirectory, var1, var2);
   }

   public DirectoryStream newDirectoryStream(JimfsPath var1, Filter var2, Set var3, JimfsPath var4) {
      Directory var5 = (Directory)this.lookUpWithLock(var1, var3).requireDirectory(var1).file();
      FileSystemView var6 = new FileSystemView(this.store, var5, var4);
      JimfsSecureDirectoryStream var7 = new JimfsSecureDirectoryStream(var6, var2, this.state());
      return this.store.supportsFeature(Feature.SECURE_DIRECTORY_STREAM) ? var7 : new DowngradedDirectoryStream(var7);
   }

   public ImmutableSortedSet snapshotWorkingDirectoryEntries() {
      this.store.readLock().lock();

      try {
         ImmutableSortedSet var1 = this.workingDirectory.snapshot();
         this.workingDirectory.setLastAccessTime(this.now());
         return var1;
      } finally {
         this.store.readLock().unlock();
      }
   }

   public ImmutableMap snapshotModifiedTimes(JimfsPath var1) {
      Builder var2 = ImmutableMap.builder();
      this.store.readLock().lock();

      try {
         for (DirectoryEntry var5 : (Directory)this.lookUp(var1, Options.FOLLOW_LINKS).requireDirectory(var1).file()) {
            if (!var5.name().equals(Name.SELF) && !var5.name().equals(Name.PARENT)) {
               var2.put(var5.name(), var5.file().getLastModifiedTime());
            }
         }

         return var2.build();
      } finally {
         this.store.readLock().unlock();
      }
   }

   public boolean isSameFile(JimfsPath var1, FileSystemView var2, JimfsPath var3) {
      if (!this.isSameFileSystem(var2)) {
         return false;
      }

      this.store.readLock().lock();

      try {
         File var4 = this.lookUp(var1, Options.FOLLOW_LINKS).fileOrNull();
         File var5 = var2.lookUp(var3, Options.FOLLOW_LINKS).fileOrNull();
         return var4 != null && Objects.equals(var4, var5);
      } finally {
         this.store.readLock().unlock();
      }
   }

   public JimfsPath toRealPath(JimfsPath var1, PathService var2, Set var3) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var3);
      this.store.readLock().lock();

      try {
         DirectoryEntry var4 = this.lookUp(var1, var3).requireExists(var1);
         ArrayList var5 = new ArrayList();
         var5.add(var4.name());

         while (!var4.file().isRootDirectory()) {
            var4 = var4.directory().entryInParent();
            var5.add(var4.name());
         }

         List var6 = Lists.reverse(var5);
         Name var7 = (Name)var6.remove(0);
         return var2.createPath(var7, var6);
      } finally {
         this.store.readLock().unlock();
      }
   }

   @CanIgnoreReturnValue
   public Directory createDirectory(JimfsPath var1, FileAttribute... var2) {
      return (Directory)this.createFile(var1, this.store.directoryCreator(), true, var2);
   }

   @CanIgnoreReturnValue
   public SymbolicLink createSymbolicLink(JimfsPath var1, JimfsPath var2, FileAttribute... var3) {
      if (!this.store.supportsFeature(Feature.SYMBOLIC_LINKS)) {
         throw new UnsupportedOperationException();
      } else {
         return (SymbolicLink)this.createFile(var1, this.store.symbolicLinkCreator(var2), true, var3);
      }
   }

   private File createFile(JimfsPath var1, Supplier var2, boolean var3, FileAttribute... var4) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      this.store.writeLock().lock();

      try {
         DirectoryEntry var5 = this.lookUp(var1, Options.NOFOLLOW_LINKS);
         if (var5.exists()) {
            if (var3) {
               throw new FileAlreadyExistsException(var1.toString());
            } else {
               return var5.file();
            }
         } else {
            Directory var6 = var5.directory();
            File var7 = (File)var2.get();
            this.store.setInitialAttributes(var7, var4);
            var6.link(var1.name(), var7);
            var6.setLastModifiedTime(this.now());
            return var7;
         }
      } finally {
         this.store.writeLock().unlock();
      }
   }

   public RegularFile getOrCreateRegularFile(JimfsPath var1, Set var2, FileAttribute... var3) {
      Preconditions.checkNotNull(var1);
      if (!var2.contains(StandardOpenOption.CREATE_NEW)) {
         RegularFile var4 = this.lookUpRegularFile(var1, var2);
         if (var4 != null) {
            return var4;
         }
      }

      if (!var2.contains(StandardOpenOption.CREATE) && !var2.contains(StandardOpenOption.CREATE_NEW)) {
         throw new NoSuchFileException(var1.toString());
      } else {
         return this.getOrCreateRegularFileWithWriteLock(var1, var2, var3);
      }
   }

   private @Nullable RegularFile lookUpRegularFile(JimfsPath var1, Set var2) {
      this.store.readLock().lock();

      try {
         DirectoryEntry var3 = this.lookUp(var1, var2);
         if (var3.exists()) {
            File var4 = var3.file();
            if (!var4.isRegularFile()) {
               throw new FileSystemException(var1.toString(), null, "not a regular file");
            } else {
               return open((RegularFile)var4, var2);
            }
         } else {
            return null;
         }
      } finally {
         this.store.readLock().unlock();
      }
   }

   private RegularFile getOrCreateRegularFileWithWriteLock(JimfsPath var1, Set var2, FileAttribute[] var3) {
      this.store.writeLock().lock();

      try {
         File var4 = this.createFile(var1, this.store.regularFileCreator(), var2.contains(StandardOpenOption.CREATE_NEW), var3);
         if (!var4.isRegularFile()) {
            throw new FileSystemException(var1.toString(), null, "not a regular file");
         } else {
            return open((RegularFile)var4, var2);
         }
      } finally {
         this.store.writeLock().unlock();
      }
   }

   private static RegularFile open(RegularFile var0, Set var1) {
      if (var1.contains(StandardOpenOption.TRUNCATE_EXISTING) && var1.contains(StandardOpenOption.WRITE)) {
         var0.writeLock().lock();

         try {
            var0.truncate(0L);
         } finally {
            var0.writeLock().unlock();
         }
      }

      var0.opened();
      return var0;
   }

   public JimfsPath readSymbolicLink(JimfsPath var1) {
      if (!this.store.supportsFeature(Feature.SYMBOLIC_LINKS)) {
         throw new UnsupportedOperationException();
      }

      SymbolicLink var2 = (SymbolicLink)this.lookUpWithLock(var1, Options.NOFOLLOW_LINKS).requireSymbolicLink(var1).file();
      return var2.target();
   }

   public void checkAccess(JimfsPath var1) {
      this.lookUpWithLock(var1, Options.FOLLOW_LINKS).requireExists(var1);
   }

   public void link(JimfsPath var1, FileSystemView var2, JimfsPath var3) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      Preconditions.checkNotNull(var3);
      if (!this.store.supportsFeature(Feature.LINKS)) {
         throw new UnsupportedOperationException();
      }

      if (!this.isSameFileSystem(var2)) {
         throw new FileSystemException(var1.toString(), var3.toString(), "can't link: source and target are in different file system instances");
      }

      Name var4 = var1.name();
      this.store.writeLock().lock();

      try {
         File var5 = var2.lookUp(var3, Options.FOLLOW_LINKS).requireExists(var3).file();
         if (!var5.isRegularFile()) {
            throw new FileSystemException(var1.toString(), var3.toString(), "can't link: not a regular file");
         }

         Directory var6 = this.lookUp(var1, Options.NOFOLLOW_LINKS).requireDoesNotExist(var1).directory();
         var6.link(var4, var5);
         var6.setLastModifiedTime(this.now());
      } finally {
         this.store.writeLock().unlock();
      }
   }

   public void deleteFile(JimfsPath var1, FileSystemView$DeleteMode var2) {
      this.store.writeLock().lock();

      try {
         DirectoryEntry var3 = this.lookUp(var1, Options.NOFOLLOW_LINKS).requireExists(var1);
         this.delete(var3, var2, var1);
      } finally {
         this.store.writeLock().unlock();
      }
   }

   private void delete(DirectoryEntry var1, FileSystemView$DeleteMode var2, JimfsPath var3) {
      Directory var4 = var1.directory();
      File var5 = var1.file();
      this.checkDeletable(var5, var2, var3);
      var4.unlink(var1.name());
      var4.setLastModifiedTime(this.now());
      var5.deleted();
   }

   private void checkDeletable(File var1, FileSystemView$DeleteMode var2, Path var3) {
      if (var1.isRootDirectory()) {
         throw new FileSystemException(var3.toString(), null, "can't delete root directory");
      }

      if (var1.isDirectory()) {
         if (var2 == FileSystemView$DeleteMode.NON_DIRECTORY_ONLY) {
            throw new FileSystemException(var3.toString(), null, "can't delete: is a directory");
         }

         this.checkEmpty((Directory)var1, var3);
      } else if (var2 == FileSystemView$DeleteMode.DIRECTORY_ONLY) {
         throw new FileSystemException(var3.toString(), null, "can't delete: is not a directory");
      }

      if (var1 == this.workingDirectory && !var3.isAbsolute()) {
         throw new FileSystemException(var3.toString(), null, "invalid argument");
      }
   }

   private void checkEmpty(Directory var1, Path var2) {
      if (!var1.isEmpty()) {
         throw new DirectoryNotEmptyException(var2.toString());
      }
   }

   public void copy(JimfsPath var1, FileSystemView var2, JimfsPath var3, Set var4, boolean var5) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      Preconditions.checkNotNull(var3);
      Preconditions.checkNotNull(var4);
      boolean var6 = this.isSameFileSystem(var2);
      File var8 = null;
      lockBoth(this.store.writeLock(), var2.store.writeLock());

      File var7;
      try {
         DirectoryEntry var9 = this.lookUp(var1, var4).requireExists(var1);
         DirectoryEntry var10 = var2.lookUp(var3, Options.NOFOLLOW_LINKS);
         Directory var11 = var9.directory();
         var7 = var9.file();
         Directory var12 = var10.directory();
         if (var5 && var7.isDirectory()) {
            if (var6) {
               this.checkMovable(var7, var1);
               this.checkNotAncestor(var7, var12, var2);
            } else {
               this.checkDeletable(var7, FileSystemView$DeleteMode.ANY, var1);
            }
         }

         if (var10.exists()) {
            if (var10.file().equals(var7)) {
               return;
            }

            if (!var4.contains(StandardCopyOption.REPLACE_EXISTING)) {
               throw new FileAlreadyExistsException(var3.toString());
            }

            var2.delete(var10, FileSystemView$DeleteMode.ANY, var3);
         }

         if (var5 && var6) {
            var11.unlink(var1.name());
            var11.setLastModifiedTime(this.now());
            var12.link(var3.name(), var7);
            var12.setLastModifiedTime(this.now());
         } else {
            AttributeCopyOption var13 = AttributeCopyOption.NONE;
            if (var5) {
               var13 = AttributeCopyOption.BASIC;
            } else if (var4.contains(StandardCopyOption.COPY_ATTRIBUTES)) {
               var13 = var6 ? AttributeCopyOption.ALL : AttributeCopyOption.BASIC;
            }

            var8 = var2.store.copyWithoutContent(var7, var13);
            var12.link(var3.name(), var8);
            var12.setLastModifiedTime(this.now());
            this.lockSourceAndCopy(var7, var8);
            if (var5) {
               this.delete(var9, FileSystemView$DeleteMode.ANY, var1);
            }
         }
      } finally {
         var2.store.writeLock().unlock();
         this.store.writeLock().unlock();
      }

      if (var8 != null) {
         try {
            var7.copyContentTo(var8);
         } finally {
            this.unlockSourceAndCopy(var7, var8);
         }
      }
   }

   private void checkMovable(File var1, JimfsPath var2) {
      if (var1.isRootDirectory()) {
         throw new FileSystemException(var2.toString(), null, "can't move root directory");
      }
   }

   private static void lockBoth(Lock var0, Lock var1) {
      while (true) {
         var0.lock();
         if (var1.tryLock()) {
            return;
         }

         var0.unlock();
         var1.lock();
         if (var0.tryLock()) {
            return;
         }

         var1.unlock();
      }
   }

   private void checkNotAncestor(File var1, Directory var2, FileSystemView var3) {
      if (this.isSameFileSystem(var3)) {
         for (Directory var4 = var2; !var4.equals(var1); var4 = var4.parent()) {
            if (var4.isRootDirectory()) {
               return;
            }
         }

         throw new IOException("invalid argument: can't move directory into a subdirectory of itself");
      }
   }

   private void lockSourceAndCopy(File var1, File var2) {
      var1.opened();
      ReadWriteLock var3 = var1.contentLock();
      if (var3 != null) {
         var3.readLock().lock();
      }

      ReadWriteLock var4 = var2.contentLock();
      if (var4 != null) {
         var4.writeLock().lock();
      }
   }

   private void unlockSourceAndCopy(File var1, File var2) {
      ReadWriteLock var3 = var1.contentLock();
      if (var3 != null) {
         var3.readLock().unlock();
      }

      ReadWriteLock var4 = var2.contentLock();
      if (var4 != null) {
         var4.writeLock().unlock();
      }

      var1.closed();
   }

   public @Nullable FileAttributeView getFileAttributeView(FileLookup var1, Class var2) {
      return this.store.getFileAttributeView(var1, var2);
   }

   public @Nullable FileAttributeView getFileAttributeView(JimfsPath var1, Class var2, Set var3) {
      return this.store.getFileAttributeView(new FileSystemView$1(this, var1, var3), var2);
   }

   public BasicFileAttributes readAttributes(JimfsPath var1, Class var2, Set var3) {
      File var4 = this.lookUpWithLock(var1, var3).requireExists(var1).file();
      return this.store.readAttributes(var4, var2);
   }

   public ImmutableMap readAttributes(JimfsPath var1, String var2, Set var3) {
      File var4 = this.lookUpWithLock(var1, var3).requireExists(var1).file();
      return this.store.readAttributes(var4, var2);
   }

   public void setAttribute(JimfsPath var1, String var2, Object var3, Set var4) {
      File var5 = this.lookUpWithLock(var1, var4).requireExists(var1).file();
      this.store.setAttribute(var5, var2, var3);
   }
}
