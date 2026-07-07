package org.ywzj.doganticheat.jimfs;

import com.google.common.collect.AbstractIterator;
import org.checkerframework.checker.nullness.qual.Nullable;

class Directory$1 extends AbstractIterator {
   int index;
   @Nullable DirectoryEntry entry;

   Directory$1(Directory var1) {
      this.this$0 = var1;
   }

   protected DirectoryEntry computeNext() {
      if (this.entry != null) {
         this.entry = this.entry.next;
      }

      while (this.entry == null && this.index < Directory.access$000(this.this$0).length) {
         this.entry = Directory.access$000(this.this$0)[this.index++];
      }

      return this.entry != null ? this.entry : (DirectoryEntry)this.endOfData();
   }
}
