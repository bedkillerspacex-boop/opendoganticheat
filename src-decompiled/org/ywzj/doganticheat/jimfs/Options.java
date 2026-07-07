package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.nio.file.CopyOption;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

final class Options {
   public static final ImmutableSet NOFOLLOW_LINKS = ImmutableSet.of(LinkOption.NOFOLLOW_LINKS);
   public static final ImmutableSet FOLLOW_LINKS = ImmutableSet.of();
   private static final ImmutableSet DEFAULT_READ = ImmutableSet.of(StandardOpenOption.READ);
   private static final ImmutableSet DEFAULT_READ_NOFOLLOW_LINKS = ImmutableSet.of(StandardOpenOption.READ, LinkOption.NOFOLLOW_LINKS);
   private static final ImmutableSet DEFAULT_WRITE = ImmutableSet.of(StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

   private Options() {
   }

   public static ImmutableSet getLinkOptions(LinkOption... var0) {
      return var0.length == 0 ? FOLLOW_LINKS : NOFOLLOW_LINKS;
   }

   public static ImmutableSet getOptionsForChannel(Set var0) {
      if (var0.isEmpty()) {
         return DEFAULT_READ;
      }

      boolean var1 = var0.contains(StandardOpenOption.APPEND);
      boolean var2 = var1 || var0.contains(StandardOpenOption.WRITE);
      boolean var3 = !var2 || var0.contains(StandardOpenOption.READ);
      if (var3) {
         if (var1) {
            throw new UnsupportedOperationException("'READ' + 'APPEND' not allowed");
         }

         if (!var2) {
            return var0.contains(LinkOption.NOFOLLOW_LINKS) ? DEFAULT_READ_NOFOLLOW_LINKS : DEFAULT_READ;
         }
      }

      return addWrite(var0);
   }

   public static ImmutableSet getOptionsForInputStream(OpenOption... var0) {
      boolean var1 = false;

      for (OpenOption var5 : var0) {
         if (Preconditions.checkNotNull(var5) != StandardOpenOption.READ) {
            if (var5 != LinkOption.NOFOLLOW_LINKS) {
               throw new UnsupportedOperationException("'" + var5 + "' not allowed");
            }

            var1 = true;
         }
      }

      return var1 ? NOFOLLOW_LINKS : FOLLOW_LINKS;
   }

   public static ImmutableSet getOptionsForOutputStream(OpenOption... var0) {
      if (var0.length == 0) {
         return DEFAULT_WRITE;
      } else {
         ImmutableSet var1 = addWrite(Arrays.asList(var0));
         if (var1.contains(StandardOpenOption.READ)) {
            throw new UnsupportedOperationException("'READ' not allowed");
         } else {
            return var1;
         }
      }
   }

   private static ImmutableSet addWrite(Collection var0) {
      return var0.contains(StandardOpenOption.WRITE) ? ImmutableSet.copyOf(var0) : ImmutableSet.builder().add(StandardOpenOption.WRITE).addAll(var0).build();
   }

   public static ImmutableSet getMoveOptions(CopyOption... var0) {
      return ImmutableSet.copyOf(Lists.asList(LinkOption.NOFOLLOW_LINKS, var0));
   }

   public static ImmutableSet getCopyOptions(CopyOption... var0) {
      ImmutableSet var1 = ImmutableSet.copyOf(var0);
      if (var1.contains(StandardCopyOption.ATOMIC_MOVE)) {
         throw new UnsupportedOperationException("'ATOMIC_MOVE' not allowed");
      } else {
         return var1;
      }
   }
}
