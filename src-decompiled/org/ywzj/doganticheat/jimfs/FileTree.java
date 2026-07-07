package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.Nullable;

final class FileTree {
   private static final int MAX_SYMBOLIC_LINK_DEPTH = 40;
   private static final ImmutableList EMPTY_PATH_NAMES = ImmutableList.of(Name.SELF);
   private final ImmutableSortedMap roots;

   FileTree(Map var1) {
      this.roots = ImmutableSortedMap.copyOf(var1, Name.canonicalOrdering());
   }

   public ImmutableSortedSet getRootDirectoryNames() {
      return this.roots.keySet();
   }

   public @Nullable DirectoryEntry getRoot(Name var1) {
      Directory var2 = (Directory)this.roots.get(var1);
      return var2 == null ? null : var2.entryInParent();
   }

   public DirectoryEntry lookUp(File var1, JimfsPath var2, Set var3) {
      Preconditions.checkNotNull(var2);
      Preconditions.checkNotNull(var3);
      DirectoryEntry var4 = this.lookUp(var1, var2, var3, 0);
      if (var4 == null) {
         throw new NoSuchFileException(var2.toString());
      } else {
         return var4;
      }
   }

   private @Nullable DirectoryEntry lookUp(File var1, JimfsPath var2, Set var3, int var4) {
      ImmutableList var5 = var2.names();
      if (var2.isAbsolute()) {
         DirectoryEntry var6 = this.getRoot(var2.root());
         if (var6 == null) {
            return null;
         }

         if (var5.isEmpty()) {
            return var6;
         }

         var1 = var6.file();
      } else if (isEmpty(var5)) {
         var5 = EMPTY_PATH_NAMES;
      }

      return this.lookUp(var1, var5, var3, var4);
   }

   private @Nullable DirectoryEntry lookUp(File var1, Iterable var2, Set var3, int var4) {
      Iterator var5 = var2.iterator();

      Name var6;
      for (var6 = (Name)var5.next(); var5.hasNext(); var6 = (Name)var5.next()) {
         Directory var7 = this.toDirectory(var1);
         if (var7 == null) {
            return null;
         }

         DirectoryEntry var8 = var7.get(var6);
         if (var8 == null) {
            return null;
         }

         File var9 = var8.file();
         if (var9.isSymbolicLink()) {
            DirectoryEntry var10 = this.followSymbolicLink(var1, (SymbolicLink)var9, var4);
            if (var10 == null) {
               return null;
            }

            var1 = var10.fileOrNull();
         } else {
            var1 = var9;
         }
      }

      return this.lookUpLast(var1, var6, var3, var4);
   }

   private @Nullable DirectoryEntry lookUpLast(@Nullable File var1, Name var2, Set var3, int var4) {
      Directory var5 = this.toDirectory(var1);
      if (var5 == null) {
         return null;
      }

      DirectoryEntry var6 = var5.get(var2);
      if (var6 == null) {
         return new DirectoryEntry(var5, var2, null);
      }

      File var7 = var6.file();
      return !var3.contains(LinkOption.NOFOLLOW_LINKS) && var7.isSymbolicLink()
         ? this.followSymbolicLink(var1, (SymbolicLink)var7, var4)
         : this.getRealEntry(var6);
   }

   private @Nullable DirectoryEntry followSymbolicLink(File var1, SymbolicLink var2, int var3) {
      if (var3 >= 40) {
         throw new IOException("too many levels of symbolic links");
      } else {
         return this.lookUp(var1, var2.target(), Options.FOLLOW_LINKS, var3 + 1);
      }
   }

   private @Nullable DirectoryEntry getRealEntry(DirectoryEntry var1) {
      Name var2 = var1.name();
      if (!var2.equals(Name.SELF) && !var2.equals(Name.PARENT)) {
         return var1;
      }

      Directory var3 = this.toDirectory(var1.file());
      assert var3 != null;
      return var3.entryInParent();
   }

   private @Nullable Directory toDirectory(@Nullable File var1) {
      return var1 != null && var1.isDirectory() ? (Directory)var1 : null;
   }

   private static boolean isEmpty(ImmutableList var0) {
      return var0.isEmpty() || var0.size() == 1 && ((Name)var0.get(0)).toString().isEmpty();
   }
}
