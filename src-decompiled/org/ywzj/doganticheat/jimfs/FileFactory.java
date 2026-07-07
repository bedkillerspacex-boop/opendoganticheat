package org.ywzj.doganticheat.jimfs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.concurrent.atomic.AtomicInteger;

final class FileFactory {
   private final AtomicInteger idGenerator = new AtomicInteger();
   private final HeapDisk disk;
   private final FileTimeSource fileTimeSource;
   private final Supplier directorySupplier = new FileFactory$DirectorySupplier(this, null);
   private final Supplier regularFileSupplier = new FileFactory$RegularFileSupplier(this, null);

   public FileFactory(HeapDisk var1, FileTimeSource var2) {
      this.disk = (HeapDisk)Preconditions.checkNotNull(var1);
      this.fileTimeSource = (FileTimeSource)Preconditions.checkNotNull(var2);
   }

   private int nextFileId() {
      return this.idGenerator.getAndIncrement();
   }

   public Directory createDirectory() {
      return Directory.create(this.nextFileId(), this.fileTimeSource.now());
   }

   public Directory createRootDirectory(Name var1) {
      return Directory.createRoot(this.nextFileId(), this.fileTimeSource.now(), var1);
   }

   @VisibleForTesting
   RegularFile createRegularFile() {
      return RegularFile.create(this.nextFileId(), this.fileTimeSource.now(), this.disk);
   }

   @VisibleForTesting
   SymbolicLink createSymbolicLink(JimfsPath var1) {
      return SymbolicLink.create(this.nextFileId(), this.fileTimeSource.now(), var1);
   }

   public File copyWithoutContent(File var1) {
      return var1.copyWithoutContent(this.nextFileId(), this.fileTimeSource.now());
   }

   public Supplier directoryCreator() {
      return this.directorySupplier;
   }

   public Supplier regularFileCreator() {
      return this.regularFileSupplier;
   }

   public Supplier symbolicLinkCreator(JimfsPath var1) {
      return new FileFactory$SymbolicLinkSupplier(this, var1);
   }
}
