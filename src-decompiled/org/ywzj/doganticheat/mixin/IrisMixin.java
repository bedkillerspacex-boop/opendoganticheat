package org.ywzj.doganticheat.mixin;

import net.irisshaders.iris.Iris;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.ywzj.doganticheat.a.b;
import org.ywzj.doganticheat.b.a;
import org.ywzj.doganticheat.b.b.e;

@Mixin(value = Iris.class, remap = false)
public class IrisMixin {
   @Inject(method = "loadShaderpack", at = @At("HEAD"))
   private static void loadShaderpack(CallbackInfo var0) {
      b.a();

      try {
         a.a.sendToServer(new e("i_i"));
      } catch (Exception var2) {
      }
   }
}
