package org.ywzj.doganticheat.jimfs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Table;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Table.Cell;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.locks.ReadWriteLock;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class File {
   private final int id;
   private int links;
   private FileTime creationTime;
   private FileTime lastAccessTime;
   private FileTime lastModifiedTime;
   private @Nullable Table attributes;

   File(int var1, FileTime var2) {
      this.id = var1;
      this.creationTime = var2;
      this.lastAccessTime = var2;
      this.lastModifiedTime = var2;
   }

   public int id() {
      return this.id;
   }

   public long size() {
      return 0L;
   }

   public final boolean isDirectory() {
      return this instanceof Directory;
   }

   public final boolean isRegularFile() {
      return this instanceof RegularFile;
   }

   public final boolean isSymbolicLink() {
      return this instanceof SymbolicLink;
   }

   abstract File copyWithoutContent(int var1, FileTime var2);

   void copyContentTo(File var1) {
   }

   @Nullable ReadWriteLock contentLock() {
      return null;
   }

   void opened() {
   }

   void closed() {
   }

   void deleted() {
   }

   final boolean isRootDirectory() {
      return this.isDirectory() && this.equals(((Directory)this).parent());
   }

   public final synchronized int links() {
      return this.links;
   }

   void linked(DirectoryEntry var1) {
      Preconditions.checkNotNull(var1);
   }

   void unlinked() {
   }

   final synchronized void incrementLinkCount() {
      this.links++;
   }

   final synchronized void decrementLinkCount() {
      this.links--;
   }

   public final synchronized FileTime getCreationTime() {
      return this.creationTime;
   }

   public final synchronized FileTime getLastAccessTime() {
      return this.lastAccessTime;
   }

   public final synchronized FileTime getLastModifiedTime() {
      return this.lastModifiedTime;
   }

   final synchronized void setCreationTime(FileTime var1) {
      this.creationTime = var1;
   }

   final synchronized void setLastAccessTime(FileTime var1) {
      this.lastAccessTime = var1;
   }

   final synchronized void setLastModifiedTime(FileTime var1) {
      this.lastModifiedTime = var1;
   }

   public final synchronized ImmutableSet getAttributeNames(String var1) {
      return this.attributes == null ? ImmutableSet.of() : ImmutableSet.copyOf(this.attributes.row(var1).keySet());
   }

   @VisibleForTesting
   final synchronized ImmutableSet getAttributeKeys() {
      if (this.attributes == null) {
         return ImmutableSet.of();
      }

      Builder var1 = ImmutableSet.builder();

      for (Cell var3 : this.attributes.cellSet()) {
         var1.add((String)var3.getRowKey() + ':' + (String)var3.getColumnKey());
      }

      return var1.build();
   }

   public final synchronized @Nullable Object getAttribute(String var1, String var2) {
      return this.attributes == null ? null : this.attributes.get(var1, var2);
   }

   public final synchronized void setAttribute(String var1, String var2, Object var3) {
      if (this.attributes == null) {
         this.attributes = HashBasedTable.create();
      }

      this.attributes.put(var1, var2, var3);
   }

   public final synchronized void deleteAttribute(String var1, String var2) {
      if (this.attributes != null) {
         this.attributes.remove(var1, var2);
      }
   }

   final synchronized void copyBasicAttributes(File var1) {
      var1.setFileTimes(this.creationTime, this.lastModifiedTime, this.lastAccessTime);
   }

   private synchronized void setFileTimes(FileTime var1, FileTime var2, FileTime var3) {
      this.creationTime = var1;
      this.lastModifiedTime = var2;
      this.lastAccessTime = var3;
   }

   final synchronized void copyAttributes(File var1) {
      this.copyBasicAttributes(var1);
      var1.putAll(this.attributes);
   }

   private synchronized void putAll(@Nullable Table var1) {
      if (var1 != null && this.attributes != var1) {
         if (this.attributes == null) {
            this.attributes = HashBasedTable.create();
         }

         this.attributes.putAll(var1);
      }
   }

   @Override
   public final String toString() {
      return MoreObjects.toStringHelper(this).add("id", this.id()).toString();
   }
}
