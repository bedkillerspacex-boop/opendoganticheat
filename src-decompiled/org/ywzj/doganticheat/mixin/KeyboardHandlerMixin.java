package org.ywzj.doganticheat.mixin;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
   @Inject(method = "keyPress", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
   public void keyPress(long var1, int var3, int var4, int var5, int var6, CallbackInfo var7) {
      Minecraft.m_91087_().f_91066_.f_92062_ = false;
   }

   @Inject(method = "handleDebugKeys", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
   public void handleDebugKeys(int var1, CallbackInfoReturnable var2) {
      if (var1 == 65 || var1 == 66 || var1 == 84) {
         var2.setReturnValue(false);
      }
   }
}
