package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import java.nio.file.FileStore;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileStoreAttributeView;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.checkerframework.checker.nullness.qual.Nullable;

final class JimfsFileStore extends FileStore {
   private final FileTree tree;
   private final HeapDisk disk;
   private final AttributeService attributes;
   private final FileFactory factory;
   private final ImmutableSet supportedFeatures;
   private final FileSystemState state;
   private final Lock readLock;
   private final Lock writeLock;

   public JimfsFileStore(FileTree var1, FileFactory var2, HeapDisk var3, AttributeService var4, ImmutableSet var5, FileSystemState var6) {
      this.tree = (FileTree)Preconditions.checkNotNull(var1);
      this.factory = (FileFactory)Preconditions.checkNotNull(var2);
      this.disk = (HeapDisk)Preconditions.checkNotNull(var3);
      this.attributes = (AttributeService)Preconditions.checkNotNull(var4);
      this.supportedFeatures = (ImmutableSet)Preconditions.checkNotNull(var5);
      this.state = (FileSystemState)Preconditions.checkNotNull(var6);
      ReentrantReadWriteLock var7 = new ReentrantReadWriteLock();
      this.readLock = var7.readLock();
      this.writeLock = var7.writeLock();
   }

   FileSystemState state() {
      return this.state;
   }

   Lock readLock() {
      return this.readLock;
   }

   Lock writeLock() {
      return this.writeLock;
   }

   ImmutableSortedSet getRootDirectoryNames() {
      this.state.checkOpen();
      return this.tree.getRootDirectoryNames();
   }

   @Nullable Directory getRoot(Name var1) {
      DirectoryEntry var2 = this.tree.getRoot(var1);
      return var2 == null ? null : (Directory)var2.file();
   }

   boolean supportsFeature(Feature var1) {
      return this.supportedFeatures.contains(var1);
   }

   DirectoryEntry lookUp(File var1, JimfsPath var2, Set var3) {
      this.state.checkOpen();
      return this.tree.lookUp(var1, var2, var3);
   }

   Supplier regularFileCreator() {
      this.state.checkOpen();
      return this.factory.regularFileCreator();
   }

   Supplier directoryCreator() {
      this.state.checkOpen();
      return this.factory.directoryCreator();
   }

   Supplier symbolicLinkCreator(JimfsPath var1) {
      this.state.checkOpen();
      return this.factory.symbolicLinkCreator(var1);
   }

   File copyWithoutContent(File var1, AttributeCopyOption var2) {
      File var3 = this.factory.copyWithoutContent(var1);
      this.setInitialAttributes(var3);
      this.attributes.copyAttributes(var1, var3, var2);
      return var3;
   }

   void setInitialAttributes(File var1, FileAttribute... var2) {
      this.state.checkOpen();
      this.attributes.setInitialAttributes(var1, var2);
   }

   @Nullable FileAttributeView getFileAttributeView(FileLookup var1, Class var2) {
      this.state.checkOpen();
      return this.attributes.getFileAttributeView(var1, var2);
   }

   ImmutableMap readAttributes(File var1, String var2) {
      this.state.checkOpen();
      return this.attributes.readAttributes(var1, var2);
   }

   BasicFileAttributes readAttributes(File var1, Class var2) {
      this.state.checkOpen();
      return this.attributes.readAttributes(var1, var2);
   }

   void setAttribute(File var1, String var2, Object var3) {
      this.state.checkOpen();
      this.attributes.setAttribute(var1, var2, var3, false);
   }

   ImmutableSet supportedFileAttributeViews() {
      this.state.checkOpen();
      return this.attributes.supportedFileAttributeViews();
   }

   @Override
   public String name() {
      return "jimfs";
   }

   @Override
   public String type() {
      return "jimfs";
   }

   @Override
   public boolean isReadOnly() {
      return false;
   }

   @Override
   public long getTotalSpace() {
      this.state.checkOpen();
      return this.disk.getTotalSpace();
   }

   @Override
   public long getUsableSpace() {
      this.state.checkOpen();
      return this.getUnallocatedSpace();
   }

   @Override
   public long getUnallocatedSpace() {
      this.state.checkOpen();
      return this.disk.getUnallocatedSpace();
   }

   @Override
   public boolean supportsFileAttributeView(Class var1) {
      this.state.checkOpen();
      return this.attributes.supportsFileAttributeView(var1);
   }

   @Override
   public boolean supportsFileAttributeView(String var1) {
      this.state.checkOpen();
      return this.attributes.supportedFileAttributeViews().contains(var1);
   }

   @Override
   public FileStoreAttributeView getFileStoreAttributeView(Class var1) {
      this.state.checkOpen();
      return null;
   }

   @Override
   public Object getAttribute(String var1) {
      throw new UnsupportedOperationException();
   }
}
