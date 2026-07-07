package org.ywzj.doganticheat.jimfs;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

class BasicAttributeProvider$Attributes implements BasicFileAttributes {
   private final FileTime lastModifiedTime;
   private final FileTime lastAccessTime;
   private final FileTime creationTime;
   private final boolean regularFile;
   private final boolean directory;
   private final boolean symbolicLink;
   private final long size;
   private final Object fileKey;

   protected BasicAttributeProvider$Attributes(File var1) {
      this.lastModifiedTime = var1.getLastModifiedTime();
      this.lastAccessTime = var1.getLastAccessTime();
      this.creationTime = var1.getCreationTime();
      this.regularFile = var1.isRegularFile();
      this.directory = var1.isDirectory();
      this.symbolicLink = var1.isSymbolicLink();
      this.size = var1.size();
      this.fileKey = var1.id();
   }

   @Override
   public FileTime lastModifiedTime() {
      return this.lastModifiedTime;
   }

   @Override
   public FileTime lastAccessTime() {
      return this.lastAccessTime;
   }

   @Override
   public FileTime creationTime() {
      return this.creationTime;
   }

   @Override
   public boolean isRegularFile() {
      return this.regularFile;
   }

   @Override
   public boolean isDirectory() {
      return this.directory;
   }

   @Override
   public boolean isSymbolicLink() {
      return this.symbolicLink;
   }

   @Override
   public boolean isOther() {
      return false;
   }

   @Override
   public long size() {
      return this.size;
   }

   @Override
   public Object fileKey() {
      return this.fileKey;
   }
}
