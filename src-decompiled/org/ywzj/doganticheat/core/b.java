package org.ywzj.doganticheat.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import sun.misc.Unsafe;

public class b {
   private static boolean a = false;

   public static boolean a() {
      return a;
   }

   public static void a(String var0, String var1, String var2, Module var3) {
      if (!a) {
         throw new RuntimeException();
      }

      Class var4 = Class.forName("jdk.internal.module.Modules");
      Class var5 = Class.forName("java.lang.Module");
      Optional var6 = (Optional)var4.getMethod("findLoadedModule", String.class).invoke(null, var1);
      Object var7 = var6.orElseThrow(IllegalArgumentException::new);
      if (var3 != null) {
         var4.getMethod(var0, var5, String.class, var5).invoke(null, var7, var2, var3);
      } else {
         var4.getMethod(var0, var5, String.class).invoke(null, var7, var2);
      }
   }

   public static void a(String var0) {
      Class var1 = Class.forName("java.lang.Module");
      Class var2 = Class.forName("java.lang.ModuleLayer");
      Class var3 = Class.forName("java.lang.module.ModuleFinder");
      Class var4 = Class.forName("java.lang.module.ModuleReference");
      Class var5 = Class.forName("java.lang.module.ModuleDescriptor");
      Class var6 = Class.forName("java.lang.module.Configuration");
      Class var7 = Class.forName("java.lang.module.ResolvedModule");
      Class var8 = Class.forName("jdk.internal.loader.BuiltinClassLoader");
      Object var9 = var3.getMethod("of", Path[].class).invoke(null, new Path[]{Paths.get(var0)});
      Set var10 = (Set)var3.getMethod("findAll").invoke(var9);
      Method var11 = var6.getDeclaredMethod("resolveAndBind", var3, List.class, var3, Collection.class);
      var11.setAccessible(true);
      Object var12 = var11.invoke(
         null,
         var9,
         Collections.singletonList(var2.getMethod("configuration").invoke(var2.getMethod("boot").invoke(null))),
         var9,
         var10.stream().peek(var2x -> {
            try {
               var8.getMethod("loadModule", var4).invoke(ClassLoader.getSystemClassLoader(), var2x);
            } catch (Exception var4x) {
               throw new RuntimeException(var4x);
            }
         }).map(var1x -> {
            try {
               return var4.getMethod("descriptor").invoke(var1x);
            } catch (Exception var3x) {
               throw new RuntimeException(var3x);
            }
         }).map(var1x -> {
            try {
               return var5.getMethod("name").invoke(var1x);
            } catch (Exception var3x) {
               throw new RuntimeException(var3x);
            }
         }).collect(Collectors.toList())
      );
      Field var13 = var6.getDeclaredField("graph");
      var13.setAccessible(true);
      Field var14 = var7.getDeclaredField("cf");
      var14.setAccessible(true);
      Map var15 = (Map)var13.get(var12);
      var15.forEach((var2x, var3x) -> {
         try {
            var14.set(var2x, var2.getMethod("configuration").invoke(var2.getMethod("boot").invoke(null)));

            for (Object var5x : var3x) {
               var14.set(var5x, var2.getMethod("configuration").invoke(var2.getMethod("boot").invoke(null)));
            }
         } catch (Exception var6x) {
            throw new RuntimeException(var6x);
         }
      });
      var15.putAll((Map)var13.get(var2.getMethod("configuration").invoke(var2.getMethod("boot").invoke(null))));
      var13.set(var2.getMethod("configuration").invoke(var2.getMethod("boot").invoke(null)), new HashMap(var15));
      Set var16 = (Set)var6.getMethod("modules").invoke(var2.getMethod("configuration").invoke(var2.getMethod("boot").invoke(null)));
      Field var17 = var6.getDeclaredField("modules");
      var17.setAccessible(true);
      HashSet var18 = new HashSet((Set)var6.getMethod("modules").invoke(var12));
      var17.set(var2.getMethod("configuration").invoke(var2.getMethod("boot").invoke(null)), new HashSet(var18));
      Field var19 = var6.getDeclaredField("nameToModule");
      var19.setAccessible(true);
      HashMap var20 = new HashMap((Map)var19.get(var2.getMethod("configuration").invoke(var2.getMethod("boot").invoke(null))));
      var20.putAll((Map)var19.get(var12));
      var19.set(var2.getMethod("configuration").invoke(var2.getMethod("boot").invoke(null)), new HashMap(var20));
      Field var21 = var2.getDeclaredField("nameToModule");
      var21.setAccessible(true);
      Map var22 = (Map)var21.get(var2.getMethod("boot").invoke(null));
      Method var23 = var1.getDeclaredMethod("defineModules", var6, Function.class, var2);
      var23.setAccessible(true);
      var22.putAll(
         (Map)var23.invoke(
            null,
            var2.getMethod("configuration").invoke(var2.getMethod("boot").invoke(null)),
            (Function<String, ClassLoader>)var0x -> ClassLoader.getSystemClassLoader(),
            var2.getMethod("boot").invoke(null)
         )
      );
      var18.addAll(var16);
      var17.set(var2.getMethod("configuration").invoke(var2.getMethod("boot").invoke(null)), new HashSet(var18));
      Field var24 = var2.getDeclaredField("modules");
      var24.setAccessible(true);
      var24.set(var2.getMethod("boot").invoke(null), null);
      Field var25 = var2.getDeclaredField("servicesCatalog");
      var25.setAccessible(true);
      var25.set(var2.getMethod("boot").invoke(null), null);
      Method var26 = var1.getDeclaredMethod("implAddReads", var1);
      var26.setAccessible(true);
      Method var27 = var2.getMethod("findModule", String.class);
      Object var28 = var2.getMethod("boot").invoke(null);
      Method var29 = var7.getMethod("name");

      for (Object var31 : (Set)var6.getMethod("modules").invoke(var12)) {
         Optional var32 = (Optional)var27.invoke(var28, var29.invoke(var31));
         var32.ifPresent(var5x -> {
            try {
               for (Object var7x : var16) {
                  Optional var8x = (Optional)var27.invoke(var28, var29.invoke(var7x));
                  var8x.ifPresent(var2xx -> {
                     try {
                        var26.invoke(var5x, var2xx);
                     } catch (Exception var4xx) {
                        throw new RuntimeException(var4xx);
                     }
                  });
               }
            } catch (Exception var9x) {
               throw new RuntimeException(var9x);
            }
         });
      }
   }

   static {
      try {
         Class var0 = Class.forName("sun.misc.Unsafe");
         Field var1 = var0.getDeclaredField("theUnsafe");
         var1.setAccessible(true);
         Unsafe var2 = (Unsafe)var1.get(null);
         Class var3 = Class.forName("jdk.internal.module.Modules");
         Object var4 = Class.class.getMethod("getModule").invoke(var3);
         Field var5 = Class.class.getDeclaredField("module");
         var2.getAndSetObject(b.class, var2.objectFieldOffset(var5), var4);
         a = true;
      } catch (Throwable var6) {
         var6.printStackTrace();
      }
   }
}
