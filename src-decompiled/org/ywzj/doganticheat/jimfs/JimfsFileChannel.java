package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.FileLockInterruptionException;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

final class JimfsFileChannel extends FileChannel {
   @GuardedBy("blockingThreads")
   private final Set blockingThreads = new HashSet();
   private final RegularFile file;
   private final FileSystemState fileSystemState;
   private final boolean read;
   private final boolean write;
   private final boolean append;
   @GuardedBy("this")
   private long position;

   public JimfsFileChannel(RegularFile var1, Set var2, FileSystemState var3) {
      this.file = var1;
      this.fileSystemState = var3;
      this.read = var2.contains(StandardOpenOption.READ);
      this.write = var2.contains(StandardOpenOption.WRITE);
      this.append = var2.contains(StandardOpenOption.APPEND);
      var3.register(this);
   }

   public AsynchronousFileChannel asAsynchronousFileChannel(ExecutorService var1) {
      return new JimfsAsynchronousFileChannel(this, var1);
   }

   void checkReadable() {
      if (!this.read) {
         throw new NonReadableChannelException();
      }
   }

   void checkWritable() {
      if (!this.write) {
         throw new NonWritableChannelException();
      }
   }

   void checkOpen() {
      if (!this.isOpen()) {
         throw new ClosedChannelException();
      }
   }

   private boolean beginBlocking() {
      this.begin();
      synchronized (this.blockingThreads) {
         if (this.isOpen()) {
            this.blockingThreads.add(Thread.currentThread());
            return true;
         } else {
            return false;
         }
      }
   }

   private void endBlocking(boolean var1) {
      synchronized (this.blockingThreads) {
         this.blockingThreads.remove(Thread.currentThread());
      }

      this.end(var1);
   }

   @Override
   public int read(ByteBuffer var1) {
      Preconditions.checkNotNull(var1);
      this.checkOpen();
      this.checkReadable();
      int var2 = 0;
      synchronized (this) {
         boolean var4 = false;

         byte var5;
         try {
            if (this.beginBlocking()) {
               this.file.readLock().lockInterruptibly();

               try {
                  var2 = this.file.read(this.position, var1);
                  if (var2 != -1) {
                     this.position += var2;
                  }

                  this.file.setLastAccessTime(this.fileSystemState.now());
                  var4 = true;
                  return var2;
               } finally {
                  this.file.readLock().unlock();
               }
            }

            var5 = 0;
         } catch (InterruptedException var18) {
            Thread.currentThread().interrupt();
            return var2;
         } finally {
            this.endBlocking(var4);
         }

         return var5;
      }
   }

   @Override
   public long read(ByteBuffer[] var1, int var2, int var3) {
      Preconditions.checkPositionIndexes(var2, var2 + var3, var1.length);
      List var4 = Arrays.asList(var1).subList(var2, var2 + var3);
      Util.checkNoneNull(var4);
      this.checkOpen();
      this.checkReadable();
      long var5 = 0L;
      synchronized (this) {
         boolean var8 = false;

         long var9;
         try {
            if (this.beginBlocking()) {
               this.file.readLock().lockInterruptibly();

               try {
                  var5 = this.file.read(this.position, var4);
                  if (var5 != -1L) {
                     this.position += var5;
                  }

                  this.file.setLastAccessTime(this.fileSystemState.now());
                  var8 = true;
                  return var5;
               } finally {
                  this.file.readLock().unlock();
               }
            }

            var9 = 0L;
         } catch (InterruptedException var23) {
            Thread.currentThread().interrupt();
            return var5;
         } finally {
            this.endBlocking(var8);
         }

         return var9;
      }
   }

   @Override
   public int read(ByteBuffer var1, long var2) {
      Preconditions.checkNotNull(var1);
      Util.checkNotNegative(var2, "position");
      this.checkOpen();
      this.checkReadable();
      int var4 = 0;
      boolean var5 = false;

      try {
         if (!this.beginBlocking()) {
            return 0;
         }

         this.file.readLock().lockInterruptibly();

         try {
            var4 = this.file.read(var2, var1);
            this.file.setLastAccessTime(this.fileSystemState.now());
            var5 = true;
         } finally {
            this.file.readLock().unlock();
         }
      } catch (InterruptedException var16) {
         Thread.currentThread().interrupt();
      } finally {
         this.endBlocking(var5);
      }

      return var4;
   }

   @Override
   public int write(ByteBuffer var1) {
      Preconditions.checkNotNull(var1);
      this.checkOpen();
      this.checkWritable();
      int var2 = 0;
      synchronized (this) {
         boolean var4 = false;

         byte var5;
         try {
            if (this.beginBlocking()) {
               this.file.writeLock().lockInterruptibly();

               try {
                  if (this.append) {
                     this.position = this.file.size();
                  }

                  var2 = this.file.write(this.position, var1);
                  this.position += var2;
                  this.file.setLastModifiedTime(this.fileSystemState.now());
                  var4 = true;
                  return var2;
               } finally {
                  this.file.writeLock().unlock();
               }
            }

            var5 = 0;
         } catch (InterruptedException var18) {
            Thread.currentThread().interrupt();
            return var2;
         } finally {
            this.endBlocking(var4);
         }

         return var5;
      }
   }

   @Override
   public long write(ByteBuffer[] var1, int var2, int var3) {
      Preconditions.checkPositionIndexes(var2, var2 + var3, var1.length);
      List var4 = Arrays.asList(var1).subList(var2, var2 + var3);
      Util.checkNoneNull(var4);
      this.checkOpen();
      this.checkWritable();
      long var5 = 0L;
      synchronized (this) {
         boolean var8 = false;

         long var9;
         try {
            if (this.beginBlocking()) {
               this.file.writeLock().lockInterruptibly();

               try {
                  if (this.append) {
                     this.position = this.file.size();
                  }

                  var5 = this.file.write(this.position, var4);
                  this.position += var5;
                  this.file.setLastModifiedTime(this.fileSystemState.now());
                  var8 = true;
                  return var5;
               } finally {
                  this.file.writeLock().unlock();
               }
            }

            var9 = 0L;
         } catch (InterruptedException var23) {
            Thread.currentThread().interrupt();
            return var5;
         } finally {
            this.endBlocking(var8);
         }

         return var9;
      }
   }

   @Override
   public int write(ByteBuffer var1, long var2) {
      Preconditions.checkNotNull(var1);
      Util.checkNotNegative(var2, "position");
      this.checkOpen();
      this.checkWritable();
      int var4 = 0;
      if (this.append) {
         synchronized (this) {
            boolean var6 = false;

            byte var7;
            try {
               if (this.beginBlocking()) {
                  this.file.writeLock().lockInterruptibly();

                  try {
                     var2 = this.file.sizeWithoutLocking();
                     var4 = this.file.write(var2, var1);
                     this.position = var2 + var4;
                     this.file.setLastModifiedTime(this.fileSystemState.now());
                     var6 = true;
                     return var4;
                  } finally {
                     this.file.writeLock().unlock();
                  }
               }

               var7 = 0;
            } catch (InterruptedException var45) {
               Thread.currentThread().interrupt();
               return var4;
            } finally {
               this.endBlocking(var6);
            }

            return var7;
         }
      } else {
         boolean var5 = false;

         try {
            if (!this.beginBlocking()) {
               return 0;
            }

            this.file.writeLock().lockInterruptibly();

            try {
               var4 = this.file.write(var2, var1);
               this.file.setLastModifiedTime(this.fileSystemState.now());
               var5 = true;
            } finally {
               this.file.writeLock().unlock();
            }
         } catch (InterruptedException var42) {
            Thread.currentThread().interrupt();
         } finally {
            this.endBlocking(var5);
         }
      }

      return var4;
   }

   @Override
   public long position() {
      this.checkOpen();
      synchronized (this) {
         boolean var4 = false;

         long var5;
         try {
            this.begin();
            if (this.isOpen()) {
               long var1 = this.position;
               var4 = true;
               return var1;
            }

            var5 = 0L;
         } finally {
            this.end(var4);
         }

         return var5;
      }
   }

   @CanIgnoreReturnValue
   @Override
   public FileChannel position(long var1) {
      Util.checkNotNegative(var1, "newPosition");
      this.checkOpen();
      synchronized (this) {
         boolean var4 = false;

         JimfsFileChannel var5;
         try {
            this.begin();
            if (this.isOpen()) {
               this.position = var1;
               var4 = true;
               return this;
            }

            var5 = this;
         } finally {
            this.end(var4);
         }

         return var5;
      }
   }

   @Override
   public long size() {
      this.checkOpen();
      long var1 = 0L;
      boolean var3 = false;

      try {
         if (!this.beginBlocking()) {
            return 0L;
         }

         this.file.readLock().lockInterruptibly();

         try {
            var1 = this.file.sizeWithoutLocking();
            var3 = true;
         } finally {
            this.file.readLock().unlock();
         }
      } catch (InterruptedException var15) {
         Thread.currentThread().interrupt();
      } finally {
         this.endBlocking(var3);
      }

      return var1;
   }

   @CanIgnoreReturnValue
   @Override
   public FileChannel truncate(long var1) {
      Util.checkNotNegative(var1, "size");
      this.checkOpen();
      this.checkWritable();
      synchronized (this) {
         boolean var4 = false;

         JimfsFileChannel var5;
         try {
            if (this.beginBlocking()) {
               this.file.writeLock().lockInterruptibly();

               try {
                  this.file.truncate(var1);
                  if (this.position > var1) {
                     this.position = var1;
                  }

                  this.file.setLastModifiedTime(this.fileSystemState.now());
                  var4 = true;
                  return this;
               } finally {
                  this.file.writeLock().unlock();
               }
            }

            var5 = this;
         } catch (InterruptedException var18) {
            Thread.currentThread().interrupt();
            return this;
         } finally {
            this.endBlocking(var4);
         }

         return var5;
      }
   }

   @Override
   public void force(boolean var1) {
      this.checkOpen();
      boolean var2 = false;

      try {
         this.begin();
         var2 = true;
      } finally {
         this.end(var2);
      }
   }

   @Override
   public long transferTo(long var1, long var3, WritableByteChannel var5) {
      Preconditions.checkNotNull(var5);
      Util.checkNotNegative(var1, "position");
      Util.checkNotNegative(var3, "count");
      this.checkOpen();
      this.checkReadable();
      long var6 = 0L;
      boolean var8 = false;

      try {
         if (!this.beginBlocking()) {
            return 0L;
         }

         this.file.readLock().lockInterruptibly();

         try {
            var6 = this.file.transferTo(var1, var3, var5);
            this.file.setLastAccessTime(this.fileSystemState.now());
            var8 = true;
         } finally {
            this.file.readLock().unlock();
         }
      } catch (InterruptedException var20) {
         Thread.currentThread().interrupt();
      } finally {
         this.endBlocking(var8);
      }

      return var6;
   }

   @Override
   public long transferFrom(ReadableByteChannel var1, long var2, long var4) {
      Preconditions.checkNotNull(var1);
      Util.checkNotNegative(var2, "position");
      Util.checkNotNegative(var4, "count");
      this.checkOpen();
      this.checkWritable();
      long var6 = 0L;
      if (this.append) {
         synchronized (this) {
            boolean var9 = false;

            long var10;
            try {
               if (this.beginBlocking()) {
                  this.file.writeLock().lockInterruptibly();

                  try {
                     var2 = this.file.sizeWithoutLocking();
                     var6 = this.file.transferFrom(var1, var2, var4);
                     this.position = var2 + var6;
                     this.file.setLastModifiedTime(this.fileSystemState.now());
                     var9 = true;
                     return var6;
                  } finally {
                     this.file.writeLock().unlock();
                  }
               }

               var10 = 0L;
            } catch (InterruptedException var49) {
               Thread.currentThread().interrupt();
               return var6;
            } finally {
               this.endBlocking(var9);
            }

            return var10;
         }
      } else {
         boolean var8 = false;

         try {
            if (!this.beginBlocking()) {
               return 0L;
            }

            this.file.writeLock().lockInterruptibly();

            try {
               var6 = this.file.transferFrom(var1, var2, var4);
               this.file.setLastModifiedTime(this.fileSystemState.now());
               var8 = true;
            } finally {
               this.file.writeLock().unlock();
            }
         } catch (InterruptedException var46) {
            Thread.currentThread().interrupt();
         } finally {
            this.endBlocking(var8);
         }
      }

      return var6;
   }

   @Override
   public MappedByteBuffer map(MapMode var1, long var2, long var4) {
      throw new UnsupportedOperationException();
   }

   @Override
   public FileLock lock(long var1, long var3, boolean var5) {
      this.checkLockArguments(var1, var3, var5);
      boolean var6 = false;

      try {
         this.begin();
         var6 = true;
         return new JimfsFileChannel$FakeFileLock(this, var1, var3, var5);
      } finally {
         try {
            this.end(var6);
         } catch (ClosedByInterruptException var14) {
            throw new FileLockInterruptionException();
         }
      }
   }

   @Override
   public FileLock tryLock(long var1, long var3, boolean var5) {
      this.checkLockArguments(var1, var3, var5);
      return new JimfsFileChannel$FakeFileLock(this, var1, var3, var5);
   }

   private void checkLockArguments(long var1, long var3, boolean var5) {
      Util.checkNotNegative(var1, "position");
      Util.checkNotNegative(var3, "size");
      this.checkOpen();
      if (var5) {
         this.checkReadable();
      } else {
         this.checkWritable();
      }
   }

   @Override
   protected void implCloseChannel() {
      try {
         synchronized (this.blockingThreads) {
            for (Thread var3 : this.blockingThreads) {
               var3.interrupt();
            }
         }
      } finally {
         this.fileSystemState.unregister(this);
         this.file.closed();
      }
   }
}
