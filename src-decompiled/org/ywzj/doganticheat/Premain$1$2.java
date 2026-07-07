package org.ywzj.doganticheat;

import java.io.File;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

class Premain$1$2 extends ClassVisitor {
   Premain$1$2(Premain$1 var1, int var2, ClassVisitor var3) {
      super(var2, var3);
      this.this$0 = var1;
   }

   public MethodVisitor visitMethod(int var1, String var2, String var3, String var4, String[] var5) {
      if (var2.equals("getJarAttributes") && var3.equals("(Ljava/io/File;)Ljava/util/jar/Attributes;")) {
         MethodVisitor var6 = super.visitMethod(var1, var2, var3, var4, var5);
         GeneratorAdapter var7 = new GeneratorAdapter(var6, var1, var2, var3);
         var7.visitCode();
         Label var8 = var7.newLabel();
         Label var9 = var7.newLabel();
         Label var10 = var7.newLabel();
         Label var11 = var7.newLabel();
         var7.visitTryCatchBlock(var8, var9, var10, "java/lang/Exception");
         var7.mark(var8);
         var7.invokeStatic(Type.getType("Lcpw/mods/modlauncher/TransformingClassLoader;"), new Method("getSystemClassLoader", "()Ljava/lang/ClassLoader;"));
         var7.push("org.ywzj.doganticheat.core.EncryptedFileSystem");
         var7.invokeVirtual(Type.getType(ClassLoader.class), new Method("loadClass", "(Ljava/lang/String;)Ljava/lang/Class;"));
         var7.dup();
         var7.push("getJarAttributes");
         var7.push(1);
         var7.newArray(Type.getType(Class.class));
         var7.dup();
         var7.push(0);
         var7.visitLdcInsn(Type.getType(File.class));
         var7.arrayStore(Type.getType(Class.class));
         var7.invokeVirtual(Type.getType(Class.class), new Method("getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;"));
         var7.push((Type)null);
         var7.push(1);
         var7.newArray(Type.getType(Object.class));
         var7.dup();
         var7.push(0);
         var7.loadArg(0);
         var7.arrayStore(Type.getType(Object.class));
         var7.invokeVirtual(Type.getType(java.lang.reflect.Method.class), new Method("invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;"));
         var7.checkCast(Type.getType("Ljava/util/jar/Attributes;"));
         var7.returnValue();
         var7.mark(var9);
         var7.goTo(var11);
         var7.mark(var10);
         var7.storeLocal(1, Type.getType(Exception.class));
         var7.loadLocal(1);
         var7.invokeVirtual(Type.getType(Exception.class), new Method("printStackTrace", "()V"));
         var7.mark(var11);
         var7.visitInsn(1);
         var7.returnValue();
         var7.endMethod();
         return null;
      } else {
         return super.visitMethod(var1, var2, var3, var4, var5);
      }
   }
}
