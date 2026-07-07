package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;

final class Util {
   private static final int C1 = -862048943;
   private static final int C2 = 461845907;
   private static final int ARRAY_LEN = 8192;
   private static final byte[] ZERO_ARRAY = new byte[8192];
   private static final byte[][] NULL_ARRAY = new byte[8192][];

   private Util() {
   }

   public static int nextPowerOf2(int var0) {
      if (var0 == 0) {
         return 1;
      }

      int var1 = Integer.highestOneBit(var0);
      return var1 == var0 ? var0 : var1 << 1;
   }

   static void checkNotNegative(long var0, String var2) {
      Preconditions.checkArgument(var0 >= 0L, "%s must not be negative: %s", var2, var0);
   }

   static void checkNoneNull(Iterable var0) {
      if (!(var0 instanceof ImmutableCollection)) {
         for (Object var2 : var0) {
            Preconditions.checkNotNull(var2);
         }
      }
   }

   static int smearHash(int var0) {
      return 461845907 * Integer.rotateLeft(var0 * -862048943, 15);
   }

   static void zero(byte[] var0, int var1, int var2) {
      int var3;
      for (var3 = var2; var3 > 8192; var3 -= 8192) {
         System.arraycopy(ZERO_ARRAY, 0, var0, var1, 8192);
         var1 += 8192;
      }

      System.arraycopy(ZERO_ARRAY, 0, var0, var1, var3);
   }

   static void clear(byte[][] var0, int var1, int var2) {
      int var3;
      for (var3 = var2; var3 > 8192; var3 -= 8192) {
         System.arraycopy(NULL_ARRAY, 0, var0, var1, 8192);
         var1 += 8192;
      }

      System.arraycopy(NULL_ARRAY, 0, var0, var1, var3);
   }
}
