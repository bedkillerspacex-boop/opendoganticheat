package org.ywzj.doganticheat.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.HttpTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.ywzj.doganticheat.a.c;

@Mixin(HttpTexture.class)
public class HttpTextureMixin {
   @Inject(method = "upload", at = @At("HEAD"))
   private void upload(NativeImage var1, CallbackInfo var2) {
      c.b.put((HttpTexture)this, copyNativeImage(var1));
   }

   private static NativeImage copyNativeImage(NativeImage var0) {
      int var1 = var0.m_84982_();
      int var2 = var0.m_85084_();
      NativeImage var3 = new NativeImage(var1, var2, true);

      for (int var4 = 0; var4 < var2; var4++) {
         for (int var5 = 0; var5 < var1; var5++) {
            int var6 = var0.m_84985_(var5, var4);
            var3.m_84988_(var5, var4, var6);
         }
      }

      return var3;
   }
}
