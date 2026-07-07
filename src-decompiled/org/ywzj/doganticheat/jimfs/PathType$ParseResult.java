package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class PathType$ParseResult {
   private final @Nullable String root;
   private final Iterable names;

   public PathType$ParseResult(@Nullable String var1, Iterable var2) {
      this.root = var1;
      this.names = (Iterable)Preconditions.checkNotNull(var2);
   }

   public boolean isAbsolute() {
      return this.root != null;
   }

   public boolean isRoot() {
      return this.root != null && Iterables.isEmpty(this.names);
   }

   public @Nullable String root() {
      return this.root;
   }

   public Iterable names() {
      return this.names;
   }
}
