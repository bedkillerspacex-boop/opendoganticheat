package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import java.nio.file.attribute.FileAttribute;

final class AttributeService$SimpleFileAttribute implements FileAttribute {
   private final String name;
   private final Object value;

   AttributeService$SimpleFileAttribute(String var1, Object var2) {
      this.name = (String)Preconditions.checkNotNull(var1);
      this.value = Preconditions.checkNotNull(var2);
   }

   @Override
   public String name() {
      return this.name;
   }

   @Override
   public Object value() {
      return this.value;
   }
}
