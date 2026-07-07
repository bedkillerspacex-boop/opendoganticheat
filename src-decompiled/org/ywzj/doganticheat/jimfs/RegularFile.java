package org.ywzj.doganticheat.jimfs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedBytes;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

final class RegularFile extends File {
   private final ReadWriteLock lock = new ReentrantReadWriteLock();
   private final HeapDisk disk;
   private byte[][] blocks;
   private int blockCount;
   private long size;
   private int openCount = 0;
   private boolean deleted = false;

   public static RegularFile create(int var0, FileTime var1, HeapDisk var2) {
      return new RegularFile(var0, var1, var2, new byte[32][], 0, 0L);
   }

   RegularFile(int var1, FileTime var2, HeapDisk var3, byte[][] var4, int var5, long var6) {
      super(var1, var2);
      this.disk = (HeapDisk)Preconditions.checkNotNull(var3);
      this.blocks = (byte[][])Preconditions.checkNotNull(var4);
      this.blockCount = var5;
      Preconditions.checkArgument(var6 >= 0L);
      this.size = var6;
   }

   public Lock readLock() {
      return this.lock.readLock();
   }

   public Lock writeLock() {
      return this.lock.writeLock();
   }

   private void expandIfNecessary(int var1) {
      if (var1 > this.blocks.length) {
         this.blocks = Arrays.copyOf(this.blocks, Util.nextPowerOf2(var1));
      }
   }

   int blockCount() {
      return this.blockCount;
   }

   void copyBlocksTo(RegularFile var1, int var2) {
      int var3 = this.blockCount - var2;
      int var4 = var1.blockCount + var2;
      var1.expandIfNecessary(var4);
      System.arraycopy(this.blocks, var3, var1.blocks, var1.blockCount, var2);
      var1.blockCount = var4;
   }

   void transferBlocksTo(RegularFile var1, int var2) {
      this.copyBlocksTo(var1, var2);
      this.truncateBlocks(this.blockCount - var2);
   }

   void truncateBlocks(int var1) {
      Util.clear(this.blocks, var1, this.blockCount - var1);
      this.blockCount = var1;
   }

   void addBlock(byte[] var1) {
      this.expandIfNecessary(this.blockCount + 1);
      this.blocks[this.blockCount++] = var1;
   }

   @VisibleForTesting
   byte[] getBlock(int var1) {
      return this.blocks[var1];
   }

   public long sizeWithoutLocking() {
      return this.size;
   }

   @Override
   public long size() {
      this.readLock().lock();

      try {
         return this.size;
      } finally {
         this.readLock().unlock();
      }
   }

   RegularFile copyWithoutContent(int var1, FileTime var2) {
      byte[][] var3 = new byte[Math.max(this.blockCount * 2, 32)][];
      return new RegularFile(var1, var2, this.disk, var3, 0, this.size);
   }

   @Override
   void copyContentTo(File var1) {
      RegularFile var2 = (RegularFile)var1;
      this.disk.allocate(var2, this.blockCount);

      for (int var3 = 0; var3 < this.blockCount; var3++) {
         byte[] var4 = this.blocks[var3];
         byte[] var5 = var2.blocks[var3];
         System.arraycopy(var4, 0, var5, 0, var4.length);
      }
   }

   @Override
   ReadWriteLock contentLock() {
      return this.lock;
   }

   @Override
   public synchronized void opened() {
      this.openCount++;
   }

   @Override
   public synchronized void closed() {
      if (--this.openCount == 0 && this.deleted) {
         this.deleteContents();
      }
   }

   @Override
   public synchronized void deleted() {
      if (this.links() == 0) {
         this.deleted = true;
         if (this.openCount == 0) {
            this.deleteContents();
         }
      }
   }

   private void deleteContents() {
      this.disk.free(this);
      this.size = 0L;
   }

   @CanIgnoreReturnValue
   public boolean truncate(long var1) {
      if (var1 >= this.size) {
         return false;
      }

      long var3 = var1 - 1L;
      this.size = var1;
      int var5 = this.blockIndex(var3) + 1;
      int var6 = this.blockCount - var5;
      if (var6 > 0) {
         this.disk.free(this, var6);
      }

      return true;
   }

   private void prepareForWrite(long var1, long var3) {
      long var5 = var1 + var3;
      int var7 = this.blockCount - 1;
      int var8 = this.blockIndex(var5 - 1L);
      if (var8 > var7) {
         int var9 = var8 - var7;
         this.disk.allocate(this, var9);
      }

      if (var1 > this.size) {
         long var14 = var1 - this.size;
         int var11 = this.blockIndex(this.size);
         byte[] var12 = this.blocks[var11];
         int var13 = this.offsetInBlock(this.size);
         var14 -= zero(var12, var13, this.length(var13, var14));

         while (var14 > 0L) {
            var12 = this.blocks[++var11];
            var14 -= zero(var12, 0, this.length(var14));
         }

         this.size = var1;
      }
   }

   @CanIgnoreReturnValue
   public int write(long var1, byte var3) {
      this.prepareForWrite(var1, 1L);
      byte[] var4 = this.blocks[this.blockIndex(var1)];
      int var5 = this.offsetInBlock(var1);
      var4[var5] = var3;
      if (var1 >= this.size) {
         this.size = var1 + 1L;
      }

      return 1;
   }

   @CanIgnoreReturnValue
   public int write(long var1, byte[] var3, int var4, int var5) {
      this.prepareForWrite(var1, var5);
      if (var5 == 0) {
         return 0;
      }

      int var6 = var5;
      int var7 = this.blockIndex(var1);
      byte[] var8 = this.blocks[var7];
      int var9 = this.offsetInBlock(var1);
      int var10 = put(var8, var9, var3, var4, this.length(var9, var6));
      var6 -= var10;
      var4 += var10;

      while (var6 > 0) {
         var8 = this.blocks[++var7];
         var10 = put(var8, 0, var3, var4, this.length(var6));
         var6 -= var10;
         var4 += var10;
      }

      long var11 = var1 + var5;
      if (var11 > this.size) {
         this.size = var11;
      }

      return var5;
   }

   @CanIgnoreReturnValue
   public int write(long var1, ByteBuffer var3) {
      int var4 = var3.remaining();
      this.prepareForWrite(var1, var4);
      if (var4 == 0) {
         return 0;
      }

      int var5 = this.blockIndex(var1);
      byte[] var6 = this.blocks[var5];
      int var7 = this.offsetInBlock(var1);
      put(var6, var7, var3);

      while (var3.hasRemaining()) {
         var6 = this.blocks[++var5];
         put(var6, 0, var3);
      }

      long var8 = var1 + var4;
      if (var8 > this.size) {
         this.size = var8;
      }

      return var4;
   }

   @CanIgnoreReturnValue
   public long write(long var1, Iterable var3) {
      long var4 = var1;

      for (ByteBuffer var7 : var3) {
         var1 += this.write(var1, var7);
      }

      return var1 - var4;
   }

   public long transferFrom(ReadableByteChannel var1, long var2, long var4) {
      if (var4 != 0L && var2 <= this.size) {
         long var6 = var4;
         long var8 = var2;
         int var10 = this.blockIndex(var2);

         label33:
         for (int var11 = this.offsetInBlock(var2); var6 > 0L; var11 = 0) {
            byte[] var12 = this.blockForWrite(var10);
            ByteBuffer var13 = ByteBuffer.wrap(var12, var11, this.length(var11, var6));

            while (var13.hasRemaining()) {
               int var14 = var1.read(var13);
               if (var14 < 1) {
                  if (var8 >= this.size && var13.position() == 0) {
                     this.disk.free(this, 1);
                  }
                  break label33;
               }

               var8 += var14;
               var6 -= var14;
            }

            var10++;
         }

         if (var8 > this.size) {
            this.size = var8;
         }

         return var8 - var2;
      } else {
         return 0L;
      }
   }

   public int read(long var1) {
      if (var1 >= this.size) {
         return -1;
      }

      byte[] var3 = this.blocks[this.blockIndex(var1)];
      int var4 = this.offsetInBlock(var1);
      return UnsignedBytes.toInt(var3[var4]);
   }

   public int read(long var1, byte[] var3, int var4, int var5) {
      int var6 = (int)this.bytesToRead(var1, var5);
      if (var6 > 0) {
         int var7 = var6;
         int var8 = this.blockIndex(var1);
         byte[] var9 = this.blocks[var8];
         int var10 = this.offsetInBlock(var1);
         int var11 = get(var9, var10, var3, var4, this.length(var10, var7));
         var7 -= var11;
         var4 += var11;

         while (var7 > 0) {
            int var12 = ++var8;
            var9 = this.blocks[var12];
            var11 = get(var9, 0, var3, var4, this.length(var7));
            var7 -= var11;
            var4 += var11;
         }
      }

      return var6;
   }

   public int read(long var1, ByteBuffer var3) {
      int var4 = (int)this.bytesToRead(var1, var3.remaining());
      if (var4 > 0) {
         int var5 = var4;
         int var6 = this.blockIndex(var1);
         byte[] var7 = this.blocks[var6];
         int var8 = this.offsetInBlock(var1);
         var5 -= get(var7, var8, var3, this.length(var8, var5));

         while (var5 > 0) {
            int var9 = ++var6;
            var7 = this.blocks[var9];
            var5 -= get(var7, 0, var3, this.length(var5));
         }
      }

      return var4;
   }

   public long read(long var1, Iterable var3) {
      if (var1 >= this.size()) {
         return -1L;
      }

      long var4 = var1;

      for (ByteBuffer var7 : var3) {
         int var8 = this.read(var1, var7);
         if (var8 == -1) {
            break;
         }

         var1 += var8;
      }

      return var1 - var4;
   }

   public long transferTo(long var1, long var3, WritableByteChannel var5) {
      long var6 = this.bytesToRead(var1, var3);
      if (var6 > 0L) {
         long var8 = var6;
         int var10 = this.blockIndex(var1);
         byte[] var11 = this.blocks[var10];
         int var12 = this.offsetInBlock(var1);
         ByteBuffer var13 = ByteBuffer.wrap(var11, var12, this.length(var12, var8));

         while (var13.hasRemaining()) {
            var8 -= var5.write(var13);
         }

         Java8Compatibility.clear(var13);

         while (var8 > 0L) {
            int var14 = ++var10;
            var11 = this.blocks[var14];
            var13 = ByteBuffer.wrap(var11, 0, this.length(var8));

            while (var13.hasRemaining()) {
               var8 -= var5.write(var13);
            }

            Java8Compatibility.clear(var13);
         }
      }

      return Math.max(var6, 0L);
   }

   private byte[] blockForWrite(int var1) {
      if (var1 >= this.blockCount) {
         int var2 = var1 - this.blockCount + 1;
         this.disk.allocate(this, var2);
      }

      return this.blocks[var1];
   }

   private int blockIndex(long var1) {
      return (int)(var1 / this.disk.blockSize());
   }

   private int offsetInBlock(long var1) {
      return (int)(var1 % this.disk.blockSize());
   }

   private int length(long var1) {
      return (int)Math.min(this.disk.blockSize(), var1);
   }

   private int length(int var1, long var2) {
      return (int)Math.min(this.disk.blockSize() - var1, var2);
   }

   private long bytesToRead(long var1, long var3) {
      long var5 = this.size - var1;
      return var5 <= 0L ? -1L : Math.min(var5, var3);
   }

   private static int zero(byte[] var0, int var1, int var2) {
      Util.zero(var0, var1, var2);
      return var2;
   }

   private static int put(byte[] var0, int var1, byte[] var2, int var3, int var4) {
      System.arraycopy(var2, var3, var0, var1, var4);
      return var4;
   }

   private static void put(byte[] var0, int var1, ByteBuffer var2) {
      int var3 = Math.min(var0.length - var1, var2.remaining());
      var2.get(var0, var1, var3);
   }

   private static int get(byte[] var0, int var1, byte[] var2, int var3, int var4) {
      System.arraycopy(var0, var1, var2, var3, var4);
      return var4;
   }

   private static int get(byte[] var0, int var1, ByteBuffer var2, int var3) {
      var2.put(var0, var1, var3);
      return var3;
   }
}
