package org.ywzj.doganticheat.a;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.datafixers.util.Pair;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

public class c {
   public static HashMap a = new HashMap();
   public static HashMap b = new HashMap();

   public static boolean a(float var0) {
      Pair var1 = (Pair)a.get("stone");
      BlockModel var2 = (BlockModel)var1.getFirst();
      TextureAtlasSprite var3 = (TextureAtlasSprite)((Function)var1.getSecond()).apply(var2.m_111480_("particle"));
      NativeImage var4 = a(var3);
      if (var4 == null) {
         return false;
      }

      int var5 = var4.m_84982_();
      int var6 = var4.m_85084_();
      double var7 = var4.m_84982_() / 16.0;
      HashMap var9 = new HashMap();

      for (Direction var13 : new Direction[]{Direction.UP, Direction.EAST, Direction.SOUTH}) {
         boolean[][] var14 = new boolean[var5][var6];

         for (int var15 = 0; var15 < var5; var15++) {
            for (int var16 = 0; var16 < var6; var16++) {
               var14[var15][var16] = false;
            }
         }

         var9.put(var13, var14);
      }

      for (BlockElement var18 : var2.m_111436_()) {
         for (Direction var20 : var18.f_111310_.keySet()) {
            BlockElementFace var21 = (BlockElementFace)var18.f_111310_.get(var20);
            NativeImage var22 = a((TextureAtlasSprite)((Function)var1.getSecond()).apply(var2.m_111480_(var21.f_111356_)));
            a(var20, var9, var18, var22, var7);
         }
      }

      return var9.values().stream().allMatch(var1x -> {
         int var2x = 0;
         int var3x = var1x.length * var1x[0].length;

         for (boolean[] var7x : var1x) {
            for (boolean var11 : var7x) {
               if (var11) {
                  var2x++;
               }
            }
         }

         return (double)var2x / var3x > var0;
      });
   }

   public static boolean b(float var0) {
      Minecraft var1 = Minecraft.m_91087_();
      if (var1.f_91074_ == null) {
         return false;
      }

      LocalPlayer var2 = var1.f_91074_;
      ResourceLocation var3 = var2.m_108560_();

      NativeImage var4;
      try {
         InputStream var5 = ((Resource)Minecraft.m_91087_().m_91098_().m_213713_(var3).get()).m_215507_();
         var4 = NativeImage.m_85058_(var5);
      } catch (Exception var11) {
         var4 = (NativeImage)b.get(Minecraft.m_91087_().m_91097_().m_118506_(var3));
      }

      try {
         double var13 = var4.m_84982_() / 64.0;
         int var7 = (int)(96.0 * var13 * var13);
         int var8 = 0;

         for (int var9 = (int)(20.0 * var13); var9 < (int)(28.0 * var13); var9++) {
            for (int var10 = (int)(20.0 * var13); var10 < (int)(32.0 * var13); var10++) {
               if (!a(var9, var10, var4)) {
                  var8++;
               }
            }
         }

         return (double)var8 / var7 > var0;
      } catch (Exception var12) {
         var12.printStackTrace();
         return false;
      }
   }

   public static boolean c(float var0) {
      HashMap var1 = new HashMap();
      Minecraft var2 = Minecraft.m_91087_();
      if (var2.f_91074_ == null) {
         return false;
      }

      LocalPlayer var3 = var2.f_91074_;
      ResourceLocation var4 = var3.m_108560_();

      NativeImage var5;
      try {
         InputStream var6 = ((Resource)Minecraft.m_91087_().m_91098_().m_213713_(var4).get()).m_215507_();
         var5 = NativeImage.m_85058_(var6);
      } catch (Exception var12) {
         var5 = (NativeImage)b.get(Minecraft.m_91087_().m_91097_().m_118506_(var4));
      }

      try {
         double var14 = var5.m_84982_() / 64.0;
         int var8 = (int)(96.0 * var14 * var14);

         for (int var9 = (int)(20.0 * var14); var9 < (int)(28.0 * var14); var9++) {
            for (int var10 = (int)(20.0 * var14); var10 < (int)(32.0 * var14); var10++) {
               Integer var11 = var5.m_84985_(var9, var10);
               var1.put(var11, var1.getOrDefault(var11, 0L) + 1L);
            }
         }

         return (double)Collections.<Long>max(var1.values()).longValue() / var8 < var0;
      } catch (Exception var13) {
         var13.printStackTrace();
         return false;
      }
   }

   public static boolean a(int var0) {
      long var1 = Util.m_137550_();

      while (Util.m_137550_() - var1 < var0) {
         try {
            Thread.sleep(10L);
         } catch (Exception var4) {
         }
      }

      return true;
   }

   private static void a(Direction var0, HashMap var1, BlockElement var2, NativeImage var3, double var4) {
      boolean[][] var6 = (boolean[][])var1.get(var0);
      if (var6 != null) {
         for (int var7 = (int)var2.f_111308_.x(); var7 < (int)var2.f_111309_.x(); var7++) {
            for (int var8 = (int)var2.f_111308_.z(); var8 < (int)var2.f_111309_.z(); var8++) {
               for (int var9 = (int)(var4 * var7); var9 < (int)(var4 * (var7 + 1)); var9++) {
                  for (int var10 = (int)(var4 * var8); var10 < (int)(var4 * (var8 + 1)); var10++) {
                     if (!a(var9, var10, var3)) {
                        var6[var9][var10] = true;
                     }
                  }
               }
            }
         }
      }
   }

   private static NativeImage a(TextureAtlasSprite var0) {
      try {
         Class var1 = var0.getClass();
         Field var2 = var1.getDeclaredField("f_244165_");
         var2.setAccessible(true);
         SpriteContents var3 = (SpriteContents)var2.get(var0);
         return var3.getOriginalImage();
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }
   }

   private static boolean a(int var0, int var1, NativeImage var2) {
      return (var2.m_84985_(var0, var1) >> 24 & 0xFF) < 255;
   }
}
