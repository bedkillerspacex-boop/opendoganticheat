package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.UnmodifiableIterator;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;

final class JimfsPath implements Path {
   private final @Nullable Name root;
   private final ImmutableList names;
   private final PathService pathService;

   public JimfsPath(PathService var1, @Nullable Name var2, Iterable var3) {
      this.pathService = (PathService)Preconditions.checkNotNull(var1);
      this.root = var2;
      this.names = ImmutableList.copyOf(var3);
   }

   public @Nullable Name root() {
      return this.root;
   }

   public ImmutableList names() {
      return this.names;
   }

   public @Nullable Name name() {
      return !this.names.isEmpty() ? (Name)Iterables.getLast(this.names) : this.root;
   }

   public boolean isEmptyPath() {
      return this.root == null && this.names.size() == 1 && ((Name)this.names.get(0)).toString().isEmpty();
   }

   @Override
   public FileSystem getFileSystem() {
      return this.pathService.getFileSystem();
   }

   public JimfsFileSystem getJimfsFileSystem() {
      return (JimfsFileSystem)this.pathService.getFileSystem();
   }

   @Override
   public boolean isAbsolute() {
      return this.root != null;
   }

   public @Nullable JimfsPath getRoot() {
      return this.root == null ? null : this.pathService.createRoot(this.root);
   }

   public @Nullable JimfsPath getFileName() {
      return this.names.isEmpty() ? null : this.getName(this.names.size() - 1);
   }

   public @Nullable JimfsPath getParent() {
      return !this.names.isEmpty() && (this.names.size() != 1 || this.root != null)
         ? this.pathService.createPath(this.root, this.names.subList(0, this.names.size() - 1))
         : null;
   }

   @Override
   public int getNameCount() {
      return this.names.size();
   }

   public JimfsPath getName(int var1) {
      Preconditions.checkArgument(var1 >= 0 && var1 < this.names.size(), "index (%s) must be >= 0 and < name count (%s)", var1, this.names.size());
      return this.pathService.createFileName((Name)this.names.get(var1));
   }

   public JimfsPath subpath(int var1, int var2) {
      Preconditions.checkArgument(
         var1 >= 0 && var2 <= this.names.size() && var2 > var1,
         "beginIndex (%s) must be >= 0; endIndex (%s) must be <= name count (%s) and > beginIndex",
         var1,
         var2,
         this.names.size()
      );
      return this.pathService.createRelativePath(this.names.subList(var1, var2));
   }

   private static boolean startsWith(List var0, List var1) {
      return var0.size() >= var1.size() && var0.subList(0, var1.size()).equals(var1);
   }

   @Override
   public boolean startsWith(Path var1) {
      JimfsPath var2 = this.checkPath(var1);
      return var2 != null && this.getFileSystem().equals(var2.getFileSystem()) && Objects.equals(this.root, var2.root) && startsWith(this.names, var2.names);
   }

   @Override
   public boolean startsWith(String var1) {
      return this.startsWith(this.pathService.parsePath(var1));
   }

   @Override
   public boolean endsWith(Path var1) {
      JimfsPath var2 = this.checkPath(var1);
      if (var2 == null) {
         return false;
      } else {
         return var2.isAbsolute() ? this.compareTo(var2) == 0 : startsWith(this.names.reverse(), var2.names.reverse());
      }
   }

   @Override
   public boolean endsWith(String var1) {
      return this.endsWith(this.pathService.parsePath(var1));
   }

   public JimfsPath normalize() {
      if (this.isNormal()) {
         return this;
      }

      ArrayDeque var1 = new ArrayDeque();
      UnmodifiableIterator var2 = this.names.iterator();

      while (var2.hasNext()) {
         Name var3 = (Name)var2.next();
         if (var3.equals(Name.PARENT)) {
            Name var4 = (Name)var1.peekLast();
            if (var4 != null && !var4.equals(Name.PARENT)) {
               var1.removeLast();
            } else if (!this.isAbsolute()) {
               var1.add(var3);
            }
         } else if (!var3.equals(Name.SELF)) {
            var1.add(var3);
         }
      }

      return Iterables.elementsEqual(var1, this.names) ? this : this.pathService.createPath(this.root, var1);
   }

   private boolean isNormal() {
      if (this.getNameCount() != 0 && (this.getNameCount() != 1 || this.isAbsolute())) {
         boolean var1 = this.isAbsolute();
         boolean var2 = true;
         UnmodifiableIterator var3 = this.names.iterator();

         while (var3.hasNext()) {
            Name var4 = (Name)var3.next();
            if (var4.equals(Name.PARENT)) {
               if (var1) {
                  var2 = false;
                  break;
               }
            } else {
               if (var4.equals(Name.SELF)) {
                  var2 = false;
                  break;
               }

               var1 = true;
            }
         }

         return var2;
      } else {
         return true;
      }
   }

   JimfsPath resolve(Name var1) {
      return this.resolve(this.pathService.createFileName(var1));
   }

   public JimfsPath resolve(Path var1) {
      JimfsPath var2 = this.checkPath(var1);
      if (var2 == null) {
         throw new ProviderMismatchException(var1.toString());
      } else if (this.isEmptyPath() || var2.isAbsolute()) {
         return var2;
      } else {
         return var2.isEmptyPath() ? this : this.pathService.createPath(this.root, ImmutableList.builder().addAll(this.names).addAll(var2.names).build());
      }
   }

   public JimfsPath resolve(String var1) {
      return this.resolve(this.pathService.parsePath(var1));
   }

   public JimfsPath resolveSibling(Path var1) {
      JimfsPath var2 = this.checkPath(var1);
      if (var2 == null) {
         throw new ProviderMismatchException(var1.toString());
      }

      if (var2.isAbsolute()) {
         return var2;
      }

      JimfsPath var3 = this.getParent();
      return var3 == null ? var2 : var3.resolve(var1);
   }

   public JimfsPath resolveSibling(String var1) {
      return this.resolveSibling(this.pathService.parsePath(var1));
   }

   public JimfsPath relativize(Path var1) {
      JimfsPath var2 = this.checkPath(var1);
      if (var2 == null) {
         throw new ProviderMismatchException(var1.toString());
      }

      Preconditions.checkArgument(Objects.equals(this.root, var2.root), "Paths have different roots: %s, %s", this, var1);
      if (this.equals(var1)) {
         return this.pathService.emptyPath();
      }

      if (this.isEmptyPath()) {
         return var2;
      }

      ImmutableList var3 = var2.names;
      int var4 = 0;

      for (int var5 = 0; var5 < Math.min(this.getNameCount(), var3.size()) && ((Name)this.names.get(var5)).equals(var3.get(var5)); var5++) {
         var4++;
      }

      int var8 = Math.max(0, this.getNameCount() - var4);
      ImmutableList var6 = var3.size() <= var4 ? ImmutableList.of() : var3.subList(var4, var3.size());
      ArrayList var7 = new ArrayList(var8 + var6.size());
      var7.addAll(Collections.nCopies(var8, Name.PARENT));
      var7.addAll(var6);
      return this.pathService.createRelativePath(var7);
   }

   public JimfsPath toAbsolutePath() {
      return this.isAbsolute() ? this : this.getJimfsFileSystem().getWorkingDirectory().resolve(this);
   }

   public JimfsPath toRealPath(LinkOption... var1) {
      return this.getJimfsFileSystem().getDefaultView().toRealPath(this, this.pathService, Options.getLinkOptions(var1));
   }

   @Override
   public WatchKey register(WatchService var1, Kind[] var2, Modifier... var3) {
      Preconditions.checkNotNull(var3);
      return this.register(var1, var2);
   }

   @Override
   public WatchKey register(WatchService var1, Kind... var2) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      if (!(var1 instanceof AbstractWatchService)) {
         throw new IllegalArgumentException("watcher (" + var1 + ") is not associated with this file system");
      }

      AbstractWatchService var3 = (AbstractWatchService)var1;
      return var3.register(this, Arrays.asList(var2));
   }

   @Override
   public URI toUri() {
      return this.getJimfsFileSystem().toUri(this);
   }

   @Override
   public java.io.File toFile() {
      throw new UnsupportedOperationException();
   }

   @Override
   public Iterator iterator() {
      return this.asList().iterator();
   }

   private List asList() {
      return new JimfsPath$1(this);
   }

   @Override
   public int compareTo(Path var1) {
      JimfsPath var2 = (JimfsPath)var1;
      return ComparisonChain.start()
         .compare(this.getJimfsFileSystem().getUri(), ((JimfsPath)var1).getJimfsFileSystem().getUri())
         .compare(this, var2, this.pathService)
         .result();
   }

   @Override
   public boolean equals(@Nullable Object var1) {
      return var1 instanceof JimfsPath && this.compareTo((JimfsPath)var1) == 0;
   }

   @Override
   public int hashCode() {
      return this.pathService.hash(this);
   }

   @Override
   public String toString() {
      return this.pathService.toString(this);
   }

   private @Nullable JimfsPath checkPath(Path var1) {
      return Preconditions.checkNotNull(var1) instanceof JimfsPath && var1.getFileSystem().equals(this.getFileSystem()) ? (JimfsPath)var1 : null;
   }
}
