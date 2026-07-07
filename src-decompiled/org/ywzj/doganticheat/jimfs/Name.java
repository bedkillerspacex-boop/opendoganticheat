package org.ywzj.doganticheat.jimfs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;

final class Name {
   static final Name EMPTY = new Name("", "");
   public static final Name SELF = new Name(".", ".");
   public static final Name PARENT = new Name("..", "..");
   private final String display;
   private final String canonical;
   private static final Ordering DISPLAY_ORDERING = Ordering.natural().onResultOf(new Name$1());
   private static final Ordering CANONICAL_ORDERING = Ordering.natural().onResultOf(new Name$2());

   @VisibleForTesting
   static Name simple(String var0) {
      switch (var0) {
         case ".":
            return SELF;
         case "..":
            return PARENT;
         default:
            return new Name(var0, var0);
      }
   }

   public static Name create(String var0, String var1) {
      return new Name(var0, var1);
   }

   private Name(String var1, String var2) {
      this.display = (String)Preconditions.checkNotNull(var1);
      this.canonical = (String)Preconditions.checkNotNull(var2);
   }

   @Override
   public boolean equals(@Nullable Object var1) {
      if (var1 instanceof Name) {
         Name var2 = (Name)var1;
         return this.canonical.equals(var2.canonical);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Util.smearHash(this.canonical.hashCode());
   }

   @Override
   public String toString() {
      return this.display;
   }

   public static Ordering displayOrdering() {
      return DISPLAY_ORDERING;
   }

   public static Ordering canonicalOrdering() {
      return CANONICAL_ORDERING;
   }
}
