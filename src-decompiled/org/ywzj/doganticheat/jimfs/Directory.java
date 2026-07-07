package org.ywzj.doganticheat.jimfs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.ImmutableSortedSet.Builder;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.file.attribute.FileTime;
import java.util.Iterator;
import org.checkerframework.checker.nullness.qual.Nullable;

final class Directory extends File implements Iterable {
   private DirectoryEntry entryInParent;
   private static final int INITIAL_CAPACITY = 16;
   private static final int INITIAL_RESIZE_THRESHOLD = 12;
   private DirectoryEntry[] table = new DirectoryEntry[16];
   private int resizeThreshold = 12;
   private int entryCount;

   public static Directory create(int var0, FileTime var1) {
      return new Directory(var0, var1);
   }

   public static Directory createRoot(int var0, FileTime var1, Name var2) {
      return new Directory(var0, var1, var2);
   }

   private Directory(int var1, FileTime var2) {
      super(var1, var2);
      this.put(new DirectoryEntry(this, Name.SELF, this));
   }

   private Directory(int var1, FileTime var2, Name var3) {
      this(var1, var2);
      this.linked(new DirectoryEntry(this, var3, this));
   }

   Directory copyWithoutContent(int var1, FileTime var2) {
      return create(var1, var2);
   }

   public DirectoryEntry entryInParent() {
      return this.entryInParent;
   }

   public Directory parent() {
      return this.entryInParent.directory();
   }

   @Override
   void linked(DirectoryEntry var1) {
      Directory var2 = var1.directory();
      this.entryInParent = var1;
      this.forcePut(new DirectoryEntry(this, Name.PARENT, var2));
   }

   @Override
   void unlinked() {
      this.parent().decrementLinkCount();
   }

   @VisibleForTesting
   int entryCount() {
      return this.entryCount;
   }

   public boolean isEmpty() {
      return this.entryCount() == 2;
   }

   public @Nullable DirectoryEntry get(Name var1) {
      int var2 = bucketIndex(var1, this.table.length);

      for (DirectoryEntry var3 = this.table[var2]; var3 != null; var3 = var3.next) {
         if (var1.equals(var3.name())) {
            return var3;
         }
      }

      return null;
   }

   public void link(Name var1, File var2) {
      DirectoryEntry var3 = new DirectoryEntry(this, checkNotReserved(var1, "link"), var2);
      this.put(var3);
      var2.linked(var3);
   }

   public void unlink(Name var1) {
      DirectoryEntry var2 = this.remove(checkNotReserved(var1, "unlink"));
      var2.file().unlinked();
   }

   public ImmutableSortedSet snapshot() {
      Builder var1 = new Builder(Name.displayOrdering());

      for (DirectoryEntry var3 : this) {
         if (!isReserved(var3.name())) {
            var1.add(var3.name());
         }
      }

      return var1.build();
   }

   private static Name checkNotReserved(Name var0, String var1) {
      if (isReserved(var0)) {
         throw new IllegalArgumentException("cannot " + var1 + ": " + var0);
      } else {
         return var0;
      }
   }

   private static boolean isReserved(Name var0) {
      return var0 == Name.SELF || var0 == Name.PARENT;
   }

   private static int bucketIndex(Name var0, int var1) {
      return var0.hashCode() & var1 - 1;
   }

   @VisibleForTesting
   void put(DirectoryEntry var1) {
      this.put(var1, false);
   }

   private void put(DirectoryEntry var1, boolean var2) {
      int var3 = bucketIndex(var1.name(), this.table.length);
      DirectoryEntry var4 = null;

      for (DirectoryEntry var5 = this.table[var3]; var5 != null; var5 = var5.next) {
         if (var5.name().equals(var1.name())) {
            if (var2) {
               if (var4 != null) {
                  var4.next = var1;
               } else {
                  this.table[var3] = var1;
               }

               var1.next = var5.next;
               var5.next = null;
               var1.file().incrementLinkCount();
               return;
            }

            throw new IllegalArgumentException("entry '" + var1.name() + "' already exists");
         }

         var4 = var5;
      }

      this.entryCount++;
      if (this.expandIfNeeded()) {
         var3 = bucketIndex(var1.name(), this.table.length);
         addToBucket(var3, this.table, var1);
      } else if (var4 != null) {
         var4.next = var1;
      } else {
         this.table[var3] = var1;
      }

      var1.file().incrementLinkCount();
   }

   private void forcePut(DirectoryEntry var1) {
      this.put(var1, true);
   }

   private boolean expandIfNeeded() {
      if (this.entryCount <= this.resizeThreshold) {
         return false;
      }

      DirectoryEntry[] var1 = new DirectoryEntry[this.table.length << 1];

      for (DirectoryEntry var5 : this.table) {
         while (var5 != null) {
            int var6 = bucketIndex(var5.name(), var1.length);
            addToBucket(var6, var1, var5);
            DirectoryEntry var7 = var5.next;
            var5.next = null;
            var5 = var7;
         }
      }

      this.table = var1;
      this.resizeThreshold <<= 1;
      return true;
   }

   private static void addToBucket(int var0, DirectoryEntry[] var1, DirectoryEntry var2) {
      DirectoryEntry var3 = null;

      for (DirectoryEntry var4 = var1[var0]; var4 != null; var4 = var4.next) {
         var3 = var4;
      }

      if (var3 != null) {
         var3.next = var2;
      } else {
         var1[var0] = var2;
      }
   }

   @CanIgnoreReturnValue
   @VisibleForTesting
   DirectoryEntry remove(Name var1) {
      int var2 = bucketIndex(var1, this.table.length);
      DirectoryEntry var3 = null;

      for (DirectoryEntry var4 = this.table[var2]; var4 != null; var4 = var4.next) {
         if (var1.equals(var4.name())) {
            if (var3 != null) {
               var3.next = var4.next;
            } else {
               this.table[var2] = var4.next;
            }

            var4.next = null;
            this.entryCount--;
            var4.file().decrementLinkCount();
            return var4;
         }

         var3 = var4;
      }

      throw new IllegalArgumentException("no entry matching '" + var1 + "' in this directory");
   }

   @Override
   public Iterator iterator() {
      return new Directory$1(this);
   }
}
