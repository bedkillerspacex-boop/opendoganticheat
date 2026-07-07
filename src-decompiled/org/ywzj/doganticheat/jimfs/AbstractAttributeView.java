package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import java.nio.file.attribute.FileAttributeView;

abstract class AbstractAttributeView implements FileAttributeView {
   private final FileLookup lookup;

   protected AbstractAttributeView(FileLookup var1) {
      this.lookup = (FileLookup)Preconditions.checkNotNull(var1);
   }

   protected final File lookupFile() {
      return this.lookup.lookup();
   }
}
