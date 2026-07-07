package org.ywzj.doganticheat.jimfs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.InputStream;

final class JimfsInputStream extends InputStream {
   @GuardedBy("this")
   @VisibleForTesting
   RegularFile file;
   @GuardedBy("this")
   private long pos;
   @GuardedBy("this")
   private boolean finished;
   private final FileSystemState fileSystemState;

   public JimfsInputStream(RegularFile var1, FileSystemState var2) {
      this.file = (RegularFile)Preconditions.checkNotNull(var1);
      this.fileSystemState = var2;
      var2.register(this);
   }

   @Override
   public synchronized int read() {
      this.checkNotClosed();
      if (this.finished) {
         return -1;
      }

      this.file.readLock().lock();

      try {
         int var1 = this.file.read(this.pos++);
         if (var1 == -1) {
            this.finished = true;
         } else {
            this.file.setLastAccessTime(this.fileSystemState.now());
         }

         return var1;
      } finally {
         this.file.readLock().unlock();
      }
   }

   @Override
   public int read(byte[] var1) {
      return this.readInternal(var1, 0, var1.length);
   }

   @Override
   public int read(byte[] var1, int var2, int var3) {
      Preconditions.checkPositionIndexes(var2, var2 + var3, var1.length);
      return this.readInternal(var1, var2, var3);
   }

   private synchronized int readInternal(byte[] var1, int var2, int var3) {
      this.checkNotClosed();
      if (this.finished) {
         return -1;
      }

      this.file.readLock().lock();

      try {
         int var4 = this.file.read(this.pos, var1, var2, var3);
         if (var4 == -1) {
            this.finished = true;
         } else {
            this.pos += var4;
         }

         this.file.setLastAccessTime(this.fileSystemState.now());
         return var4;
      } finally {
         this.file.readLock().unlock();
      }
   }

   @Override
   public long skip(long var1) {
      if (var1 <= 0L) {
         return 0L;
      }

      synchronized (this) {
         this.checkNotClosed();
         if (this.finished) {
            return 0L;
         }

         int var4 = (int)Math.min(Math.max(this.file.size() - this.pos, 0L), var1);
         this.pos += var4;
         return var4;
      }
   }

   @Override
   public synchronized int available() {
      this.checkNotClosed();
      if (this.finished) {
         return 0;
      }

      long var1 = Math.max(this.file.size() - this.pos, 0L);
      return Ints.saturatedCast(var1);
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
