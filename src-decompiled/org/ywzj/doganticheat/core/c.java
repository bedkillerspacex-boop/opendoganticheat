package org.ywzj.doganticheat.core;

import cpw.mods.modlauncher.TransformingClassLoader;
import java.util.HashSet;
import java.util.List;

public class c {
   private static Class a;

   public static byte[] a() {
      try {
         return (byte[])a.getDeclaredMethod("op0").invoke(null);
      } catch (Exception var1) {
         return new byte[]{-1};
      }
   }

   public static byte[] a(int var0, float var1, float var2, float var3, float var4) {
      try {
         return (byte[])a.getDeclaredMethod("op1", int.class, float.class, float.class, float.class, float.class).invoke(null, var0, var1, var2, var3, var4);
      } catch (Exception var6) {
         return new byte[]{-1};
      }
   }

   public static byte[] a(byte[] var0) {
      try {
         return (byte[])a.getDeclaredMethod("core1", byte[].class).invoke(null, var0);
      } catch (Exception var2) {
         return new byte[]{-1};
      }
   }

   public static void b(byte[] var0) {
      try {
         a.getDeclaredMethod("op2", byte[].class).invoke(null, var0);
      } catch (Exception var2) {
      }
   }

   public static void a(byte var0) {
      try {
         a.getDeclaredMethod("op3", byte.class).invoke(null, var0);
      } catch (Exception var2) {
      }
   }

   public static byte[] c(byte[] var0) {
      try {
         return (byte[])a.getDeclaredMethod("op4", byte[].class).invoke(null, var0);
      } catch (Exception var2) {
         return new byte[]{-1};
      }
   }

   public static byte[] d(byte[] var0) {
      try {
         return (byte[])a.getDeclaredMethod("op5", byte[].class).invoke(null, var0);
      } catch (Exception var2) {
         return new byte[]{-1};
      }
   }

   public static byte[] a(List var0) {
      try {
         return (byte[])a.getDeclaredMethod("op6", List.class).invoke(null, var0);
      } catch (Exception var2) {
         return new byte[]{-1};
      }
   }

   public static byte[] b() {
      try {
         return (byte[])a.getDeclaredMethod("op7").invoke(null);
      } catch (Exception var1) {
         return new byte[]{-1};
      }
   }

   public static byte[] e(byte[] var0) {
      try {
         return (byte[])a.getDeclaredMethod("op8", byte[].class).invoke(null, var0);
      } catch (Exception var2) {
         return new byte[]{-1};
      }
   }

   public static byte[] a(int var0, byte[] var1) {
      try {
         return (byte[])a.getDeclaredMethod("op9", int.class, byte[].class).invoke(null, var0, var1);
      } catch (Exception var3) {
         return new byte[]{-1};
      }
   }

   public static HashSet c() {
      try {
         return (HashSet)a.getDeclaredMethod("op10").invoke(null);
      } catch (Exception var1) {
         return null;
      }
   }

   public static Object d() {
      try {
         return a.getDeclaredMethod("test").invoke(null);
      } catch (Exception var1) {
         return null;
      }
   }

   static {
      try {
         a = TransformingClassLoader.getSystemClassLoader().loadClass("org.ywzj.doganticheat.core.NativeApi");
      } catch (Exception var1) {
         var1.printStackTrace();
      }
   }
}
