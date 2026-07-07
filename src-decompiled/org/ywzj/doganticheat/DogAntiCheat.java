package org.ywzj.doganticheat;

import cpw.mods.cl.ModuleClassLoader;
import cpw.mods.modlauncher.TransformingClassLoader;
import java.lang.reflect.Field;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.ywzj.doganticheat.core.a;
import org.ywzj.doganticheat.core.b;
import org.ywzj.doganticheat.core.c;

@Mod("doganticheat")
public class DogAntiCheat {
   public static int version = 23;
   public static boolean preInit = false;
   public static boolean init = false;
   public static ModuleClassLoader moduleClassLoader;
   public static String tips = "请使用客户端自带的HMCL启动器来启动游戏！\n任何因自行修改客户端导致的包括但不限于：提示“反作弊未启动”、提示请勿自行安装mod、提示请勿自行安装材质包、提示因注入而封号，我们概不做解释与支持\n";

   public DogAntiCheat() {
      IEventBus var1 = FMLJavaModLoadingContext.get().getModEventBus();
      if (isPreInit()) {
         init = true;

         try {
            Class var8 = TransformingClassLoader.getSystemClassLoader().loadClass("org.ywzj.doganticheat.DogAntiCheat");
            Field var9 = var8.getDeclaredField("init");
            var9.setBoolean(null, true);
            Field var10 = var8.getDeclaredField("moduleClassLoader");
            var10.set(null, this.getClass().getClassLoader());
         } catch (Exception var7) {
            var7.printStackTrace();
         }

         c.b();
         this.registerCommonEvents(var1);
      } else {
         LogManager.getLogger().error("反作弊未能成功启动");
         boolean var2 = false;

         for (String var6 : WindowUtils.b()) {
            if (var6.contains("Plain Craft Launcher")) {
               var2 = true;
               break;
            }
         }

         if (var2) {
            tips = tips + "请勿使用PCL启动器！";
         }

         throw new RuntimeException(tips);
      }
   }

   public static boolean loadDlls() {
      try {
         if (b.a()) {
            b.a("addOpensToAllUnnamed", "java.base", "java.net", null);
            b.a("addOpensToAllUnnamed", "java.base", "jdk.internal.loader", null);
            b.a("addOpensToAllUnnamed", "java.base", "java.lang.invoke", null);
            a.a();
            return true;
         }
      } catch (Exception var1) {
         var1.printStackTrace();
      }

      return false;
   }

   private static boolean isPreInit() {
      try {
         Class var0 = TransformingClassLoader.getSystemClassLoader().loadClass("org.ywzj.doganticheat.DogAntiCheat");
         Field var1 = var0.getDeclaredField("preInit");
         return (Boolean)var1.get(null);
      } catch (Exception var2) {
         var2.printStackTrace();
         return false;
      }
   }

   public void registerCommonEvents(IEventBus var1) {
      var1.register(org.ywzj.doganticheat.b.a.class);
   }
}
