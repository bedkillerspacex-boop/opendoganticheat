package org.ywzj.doganticheat;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

class Premain$1 implements ClassFileTransformer {
   @Override
   public byte[] transform(ClassLoader var1, String var2, Class var3, ProtectionDomain var4, byte[] var5) {
      if ("cpw/mods/niofs/union/UnionFileSystem".equals(var2)) {
         try {
            ClassReader var6 = new ClassReader(var5);
            ClassWriter var7 = new ClassWriter(3);
            Premain$1$1 var8 = new Premain$1$1(this, 589824, var7);
            var6.accept(var8, 0);
            return var7.toByteArray();
         } catch (Throwable var10) {
            var10.printStackTrace();
         }
      } else if ("org/spongepowered/asm/launch/platform/MainAttributes".equals(var2)) {
         try {
            ClassReader var11 = new ClassReader(var5);
            ClassWriter var12 = new ClassWriter(3);
            Premain$1$2 var13 = new Premain$1$2(this, 589824, var12);
            var11.accept(var13, 0);
            return var12.toByteArray();
         } catch (Throwable var9) {
            var9.printStackTrace();
         }
      }

      return null;
   }
}
