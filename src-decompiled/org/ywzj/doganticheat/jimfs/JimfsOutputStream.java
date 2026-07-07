package org.ywzj.doganticheat.jimfs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.OutputStream;

final class JimfsOutputStream extends OutputStream {
   @GuardedBy("this")
   @VisibleForTesting
   RegularFile file;
   @GuardedBy("this")
   private long pos;
   private final boolean append;
   private final FileSystemState fileSystemState;

   JimfsOutputStream(RegularFile var1, boolean var2, FileSystemState var3) {
      this.file = (RegularFile)Preconditions.checkNotNull(var1);
      this.append = var2;
      this.fileSystemState = var3;
      var3.register(this);
   }

   @Override
   public synchronized void write(int var1) {
      this.checkNotClosed();
      this.file.writeLock().lock();

      try {
         if (this.append) {
            this.pos = this.file.sizeWithoutLocking();
         }

         this.file.write(this.pos++, (byte)var1);
         this.file.setLastModifiedTime(this.fileSystemState.now());
      } finally {
         this.file.writeLock().unlock();
      }
   }

   @Override
   public void write(byte[] var1) {
      this.writeInternal(var1, 0, var1.length);
   }

   @Override
   public void write(byte[] var1, int var2, int var3) {
      Preconditions.checkPositionIndexes(var2, var2 + var3, var1.length);
      this.writeInternal(var1, var2, var3);
   }

   private synchronized void writeInternal(byte[] var1, int var2, int var3) {
      this.checkNotClosed();
      this.file.writeLock().lock();

      try {
         if (this.append) {
            this.pos = this.file.sizeWithoutLocking();
         }

         this.pos = this.pos + this.file.write(this.pos, var1, var2, var3);
         this.file.setLastModifiedTime(this.fileSystemState.now());
      } finally {
         this.file.writeLock().unlock();
      }
   }

   @GuardedBy("this")
   private void checkNotClosed() {
      if (this.file == null) {
         throw new IOException("stream is closed");
      }
   }

   @Override
   public synchronized void close() {
      if (this.isOpen()) {
         this.fileSystemState.unregister(this);
         this.file.closed();
         this.file = null;
      }
   }

   @GuardedBy("this")
   private boolean isOpen() {
      return this.file != null;
   }
}
