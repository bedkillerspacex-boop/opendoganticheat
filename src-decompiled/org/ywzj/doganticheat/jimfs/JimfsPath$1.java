package org.ywzj.doganticheat.jimfs;

import java.nio.file.Path;
import java.util.AbstractList;

class JimfsPath$1 extends AbstractList {
   JimfsPath$1(JimfsPath var1) {
      this.this$0 = var1;
   }

   public Path get(int var1) {
      return this.this$0.getName(var1);
   }

   @Override
   public int size() {
      return this.this$0.getNameCount();
   }
}
