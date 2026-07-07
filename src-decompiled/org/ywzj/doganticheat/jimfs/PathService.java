package org.ywzj.doganticheat.jimfs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.UnmodifiableIterator;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

final class PathService implements Comparator {
   private static final Ordering DISPLAY_ROOT_ORDERING = Name.displayOrdering().nullsLast();
   private static final Ordering DISPLAY_NAMES_ORDERING = Name.displayOrdering().lexicographical();
   private static final Ordering CANONICAL_ROOT_ORDERING = Name.canonicalOrdering().nullsLast();
   private static final Ordering CANONICAL_NAMES_ORDERING = Name.canonicalOrdering().lexicographical();
   private final PathType type;
   private final ImmutableSet displayNormalizations;
   private final ImmutableSet canonicalNormalizations;
   private final boolean equalityUsesCanonicalForm;
   private final Ordering rootOrdering;
   private final Ordering namesOrdering;
   private volatile FileSystem fileSystem;
   private volatile JimfsPath emptyPath;
   private static final Predicate NOT_EMPTY = new PathService$1();

   PathService(Configuration var1) {
      this(var1.pathType, var1.nameDisplayNormalization, var1.nameCanonicalNormalization, var1.pathEqualityUsesCanonicalForm);
   }

   PathService(PathType var1, Iterable var2, Iterable var3, boolean var4) {
      this.type = (PathType)Preconditions.checkNotNull(var1);
      this.displayNormalizations = ImmutableSet.copyOf(var2);
      this.canonicalNormalizations = ImmutableSet.copyOf(var3);
      this.equalityUsesCanonicalForm = var4;
      this.rootOrdering = var4 ? CANONICAL_ROOT_ORDERING : DISPLAY_ROOT_ORDERING;
      this.namesOrdering = var4 ? CANONICAL_NAMES_ORDERING : DISPLAY_NAMES_ORDERING;
   }

   public void setFileSystem(FileSystem var1) {
      Preconditions.checkState(this.fileSystem == null, "may not set fileSystem twice");
      this.fileSystem = (FileSystem)Preconditions.checkNotNull(var1);
   }

   public FileSystem getFileSystem() {
      return this.fileSystem;
   }

   public String getSeparator() {
      return this.type.getSeparator();
   }

   public JimfsPath emptyPath() {
      JimfsPath var1 = this.emptyPath;
      if (var1 == null) {
         var1 = this.createPathInternal(null, ImmutableList.of(Name.EMPTY));
         this.emptyPath = var1;
         return var1;
      } else {
         return var1;
      }
   }

   public Name name(String var1) {
      switch (var1) {
         case "":
            return Name.EMPTY;
         case ".":
            return Name.SELF;
         case "..":
            return Name.PARENT;
         default:
            String var4 = PathNormalization.normalize(var1, this.displayNormalizations);
            String var5 = PathNormalization.normalize(var1, this.canonicalNormalizations);
            return Name.create(var4, var5);
      }
   }

   @VisibleForTesting
   List names(Iterable var1) {
      ArrayList var2 = new ArrayList();

      for (String var4 : var1) {
         var2.add(this.name(var4));
      }

      return var2;
   }

   public JimfsPath createRoot(Name var1) {
      return this.createPath((Name)Preconditions.checkNotNull(var1), ImmutableList.of());
   }

   public JimfsPath createFileName(Name var1) {
      return this.createPath(null, ImmutableList.of(var1));
   }

   public JimfsPath createRelativePath(Iterable var1) {
      return this.createPath(null, ImmutableList.copyOf(var1));
   }

   public JimfsPath createPath(@Nullable Name var1, Iterable var2) {
      ImmutableList var3 = ImmutableList.copyOf(Iterables.filter(var2, NOT_EMPTY));
      return var1 == null && var3.isEmpty() ? this.emptyPath() : this.createPathInternal(var1, var3);
   }

   protected final JimfsPath createPathInternal(@Nullable Name var1, Iterable var2) {
      return new JimfsPath(this, var1, var2);
   }

   public JimfsPath parsePath(String var1, String... var2) {
      String var3 = this.type.joiner().join(Iterables.filter(Lists.asList(var1, var2), NOT_EMPTY));
      return this.toPath(this.type.parsePath(var3));
   }

   private JimfsPath toPath(PathType$ParseResult var1) {
      Name var2 = var1.root() == null ? null : this.name(var1.root());
      List var3 = this.names(var1.names());
      return this.createPath(var2, var3);
   }

   public String toString(JimfsPath var1) {
      Name var2 = var1.root();
      String var3 = var2 == null ? null : var2.toString();
      Iterable var4 = Iterables.transform(var1.names(), Functions.toStringFunction());
      return this.type.toString(var3, var4);
   }

   public int hash(JimfsPath var1) {
      int var2 = 31;
      var2 = 31 * var2 + this.getFileSystem().hashCode();
      Name var3 = var1.root();
      ImmutableList var4 = var1.names();
      if (this.equalityUsesCanonicalForm) {
         var2 = 31 * var2 + (var3 == null ? 0 : var3.hashCode());
         UnmodifiableIterator var5 = var4.iterator();

         while (var5.hasNext()) {
            Name var6 = (Name)var5.next();
            var2 = 31 * var2 + var6.hashCode();
         }
      } else {
         var2 = 31 * var2 + (var3 == null ? 0 : var3.toString().hashCode());
         UnmodifiableIterator var9 = var4.iterator();

         while (var9.hasNext()) {
            Name var10 = (Name)var9.next();
            var2 = 31 * var2 + var10.toString().hashCode();
         }
      }

      return var2;
   }

   public int compare(JimfsPath var1, JimfsPath var2) {
      return ComparisonChain.start().compare(var1.root(), var2.root(), this.rootOrdering).compare(var1.names(), var2.names(), this.namesOrdering).result();
   }

   public URI toUri(URI var1, JimfsPath var2) {
      Preconditions.checkArgument(var2.isAbsolute(), "path (%s) must be absolute", var2);
      String var3 = String.valueOf(var2.root());
      Iterable var4 = Iterables.transform(var2.names(), Functions.toStringFunction());
      return this.type.toUri(var1, var3, var4, Files.isDirectory(var2, LinkOption.NOFOLLOW_LINKS));
   }

   public JimfsPath fromUri(URI var1) {
      return this.toPath(this.type.fromUri(var1));
   }

   public PathMatcher createPathMatcher(String var1) {
      return PathMatchers.getPathMatcher(
         var1,
         this.type.getSeparator() + this.type.getOtherSeparators(),
         this.equalityUsesCanonicalForm ? this.canonicalNormalizations : this.displayNormalizations
      );
   }
}
