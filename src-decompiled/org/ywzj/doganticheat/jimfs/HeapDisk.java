package org.ywzj.doganticheat.jimfs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;
import java.io.IOException;
import java.math.RoundingMode;

final class HeapDisk {
   private final int blockSize;
   private final int maxBlockCount;
   private final int maxCachedBlockCount;
   @VisibleForTesting
   final RegularFile blockCache;
   private int allocatedBlockCount;

   public HeapDisk(Configuration var1) {
      this.blockSize = var1.blockSize;
      this.maxBlockCount = toBlockCount(var1.maxSize, this.blockSize);
      this.maxCachedBlockCount = var1.maxCacheSize == -1L ? this.maxBlockCount : toBlockCount(var1.maxCacheSize, this.blockSize);
      this.blockCache = this.createBlockCache(this.maxCachedBlockCount);
   }

   public HeapDisk(int var1, int var2, int var3) {
      Preconditions.checkArgument(var1 > 0, "blockSize (%s) must be positive", var1);
      Preconditions.checkArgument(var2 > 0, "maxBlockCount (%s) must be positive", var2);
      Preconditions.checkArgument(var3 >= 0, "maxCachedBlockCount (%s) must be non-negative", var3);
      this.blockSize = var1;
      this.maxBlockCount = var2;
      this.maxCachedBlockCount = var3;
      this.blockCache = this.createBlockCache(var3);
   }

   private static int toBlockCount(long var0, int var2) {
      return (int)LongMath.divide(var0, var2, RoundingMode.FLOOR);
   }

   private RegularFile createBlockCache(int var1) {
      return new RegularFile(-1, SystemFileTimeSource.INSTANCE.now(), this, new byte[Math.min(var1, 8192)][], 0, 0L);
   }

   public int blockSize() {
      return this.blockSize;
   }

   public synchronized long getTotalSpace() {
      return (long)this.maxBlockCount * this.blockSize;
   }

   public synchronized long getUnallocatedSpace() {
      return (long)(this.maxBlockCount - this.allocatedBlockCount) * this.blockSize;
   }

   public synchronized void allocate(RegularFile var1, int var2) {
      int var3 = this.allocatedBlockCount + var2;
      if (var3 > this.maxBlockCount) {
         throw new IOException("out of disk space");
      }

      int var4 = Math.max(var2 - this.blockCache.blockCount(), 0);

      for (int var5 = 0; var5 < var4; var5++) {
         var1.addBlock(new byte[this.blockSize]);
      }

      if (var4 != var2) {
         this.blockCache.transferBlocksTo(var1, var2 - var4);
      }

      this.allocatedBlockCount = var3;
   }

   public void free(RegularFile var1) {
      this.free(var1, var1.blockCount());
   }

   public synchronized void free(RegularFile var1, int var2) {
      int var3 = this.maxCachedBlockCount - this.blockCache.blockCount();
      if (var3 > 0) {
         var1.copyBlocksTo(this.blockCache, Math.min(var2, var3));
      }

      var1.truncateBlocks(var1.blockCount() - var2);
      this.allocatedBlockCount -= var2;
   }
}
