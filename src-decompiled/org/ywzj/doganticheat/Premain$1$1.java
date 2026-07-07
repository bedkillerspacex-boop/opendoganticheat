package org.ywzj.doganticheat;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

class Premain$1$1 extends ClassVisitor {
   Premain$1$1(Premain$1 var1, int var2, ClassVisitor var3) {
      super(var2, var3);
      this.this$0 = var1;
   }

   public MethodVisitor visitMethod(int var1, String var2, String var3, String var4, String[] var5) {
      MethodVisitor var6 = super.visitMethod(var1, var2, var3, var4, var5);
      return var2.equals("openFileSystem") && var3.equals("(Ljava/nio/file/Path;)Ljava/util/Optional;") && (var1 & 8) != 0
         ? new Premain$1$1$1(this, 589824, var6)
         : var6;
   }
}
