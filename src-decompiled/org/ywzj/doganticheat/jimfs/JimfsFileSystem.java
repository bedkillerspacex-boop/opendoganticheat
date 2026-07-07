package org.ywzj.doganticheat.jimfs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableSortedSet.Builder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.checkerframework.checker.nullness.qual.Nullable;

final class JimfsFileSystem extends FileSystem {
   private final JimfsFileSystemProvider provider;
   private final URI uri;
   private final JimfsFileStore fileStore;
   private final PathService pathService;
   private final UserPrincipalLookupService userLookupService = new UserLookupService(true);
   private final FileSystemView defaultView;
   private final WatchServiceConfiguration watchServiceConfig;
   private @Nullable ExecutorService defaultThreadPool;

   JimfsFileSystem(JimfsFileSystemProvider var1, URI var2, JimfsFileStore var3, PathService var4, FileSystemView var5, WatchServiceConfiguration var6) {
      this.provider = (JimfsFileSystemProvider)Preconditions.checkNotNull(var1);
      this.uri = (URI)Preconditions.checkNotNull(var2);
      this.fileStore = (JimfsFileStore)Preconditions.checkNotNull(var3);
      this.pathService = (PathService)Preconditions.checkNotNull(var4);
      this.defaultView = (FileSystemView)Preconditions.checkNotNull(var5);
      this.watchServiceConfig = (WatchServiceConfiguration)Preconditions.checkNotNull(var6);
   }

   public JimfsFileSystemProvider provider() {
      return this.provider;
   }

   public URI getUri() {
      return this.uri;
   }

   public FileSystemView getDefaultView() {
      return this.defaultView;
   }

   @Override
   public String getSeparator() {
      return this.pathService.getSeparator();
   }

   public ImmutableSortedSet getRootDirectories() {
      Builder var1 = ImmutableSortedSet.orderedBy(this.pathService);
      UnmodifiableIterator var2 = this.fileStore.getRootDirectoryNames().iterator();

      while (var2.hasNext()) {
         Name var3 = (Name)var2.next();
         var1.add(this.pathService.createRoot(var3));
      }

      return var1.build();
   }

   public JimfsPath getWorkingDirectory() {
      return this.defaultView.getWorkingDirectoryPath();
   }

   @VisibleForTesting
   PathService getPathService() {
      return this.pathService;
   }

   public JimfsFileStore getFileStore() {
      return this.fileStore;
   }

   public ImmutableSet getFileStores() {
      this.fileStore.state().checkOpen();
      return ImmutableSet.of(this.fileStore);
   }

   public ImmutableSet supportedFileAttributeViews() {
      return this.fileStore.supportedFileAttributeViews();
   }

   public JimfsPath getPath(String var1, String... var2) {
      this.fileStore.state().checkOpen();
      return this.pathService.parsePath(var1, var2);
   }

   public URI toUri(JimfsPath var1) {
      this.fileStore.state().checkOpen();
      return this.pathService.toUri(this.uri, var1.toAbsolutePath());
   }

   public JimfsPath toPath(URI var1) {
      this.fileStore.state().checkOpen();
      return this.pathService.fromUri(var1);
   }

   @Override
   public PathMatcher getPathMatcher(String var1) {
      this.fileStore.state().checkOpen();
      return this.pathService.createPathMatcher(var1);
   }

   @Override
   public UserPrincipalLookupService getUserPrincipalLookupService() {
      this.fileStore.state().checkOpen();
      return this.userLookupService;
   }

   @Override
   public WatchService newWatchService() {
      return this.watchServiceConfig.newWatchService(this.defaultView, this.pathService);
   }

   public synchronized ExecutorService getDefaultThreadPool() {
      if (this.defaultThreadPool == null) {
         this.defaultThreadPool = Executors.newCachedThreadPool(
            new ThreadFactoryBuilder().setDaemon(true).setNameFormat("JimfsFileSystem-" + this.uri.getHost() + "-defaultThreadPool-%s").build()
         );
         this.fileStore.state().register(new JimfsFileSystem$1(this));
      }

      return this.defaultThreadPool;
   }

   @Override
   public boolean isReadOnly() {
      return false;
   }

   @Override
   public boolean isOpen() {
      return this.fileStore.state().isOpen();
   }

   @Override
   public void close() {
      this.fileStore.state().close();
   }
}
