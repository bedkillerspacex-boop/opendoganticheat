package org.ywzj.doganticheat;

import org.objectweb.asm.MethodVisitor;

class Premain$1$1$1 extends MethodVisitor {
   Premain$1$1$1(Premain$1$1 var1, int var2, MethodVisitor var3) {
      super(var2, var3);
      this.this$1 = var1;
   }

   public void visitCode() {
      super.visitCode();
      this.mv.visitVarInsn(25, 0);
      this.mv.visitMethodInsn(184, "org/ywzj/doganticheat/core/EncryptedFileSystem", "openFileSystem", "(Ljava/nio/file/Path;)Ljava/util/Optional;", false);
      this.mv.visitInsn(176);
      this.mv.visitMaxs(1, 1);
      this.mv.visitEnd();
   }
}
