package org.ywzj.doganticheat.jimfs;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;

final class AbstractWatchService$Event implements WatchEvent {
   private final Kind kind;
   private final int count;
   private final @Nullable Object context;

   public AbstractWatchService$Event(Kind var1, int var2, @Nullable Object var3) {
      this.kind = (Kind)Preconditions.checkNotNull(var1);
      Preconditions.checkArgument(var2 >= 0, "count (%s) must be non-negative", var2);
      this.count = var2;
      this.context = var3;
   }

   @Override
   public Kind kind() {
      return this.kind;
   }

   @Override
   public int count() {
      return this.count;
   }

   @Override
   public @Nullable Object context() {
      return this.context;
   }

   @Override
   public boolean equals(Object var1) {
      if (!(var1 instanceof AbstractWatchService$Event)) {
         return false;
      }

      AbstractWatchService$Event var2 = (AbstractWatchService$Event)var1;
      return this.kind().equals(var2.kind()) && this.count() == var2.count() && Objects.equals(this.context(), var2.context());
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.kind(), this.count(), this.context());
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this).add("kind", this.kind()).add("count", this.count()).add("context", this.context()).toString();
   }
}
