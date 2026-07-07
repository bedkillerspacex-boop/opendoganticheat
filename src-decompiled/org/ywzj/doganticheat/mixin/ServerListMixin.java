package org.ywzj.doganticheat.mixin;

import java.util.List;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.ywzj.doganticheat.b.a;

@Mixin(ServerList.class)
public class ServerListMixin {
   @Shadow
   @Final
   private List f_105427_;

   @Inject(method = "save", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILHARD)
   public void beforeSave(CallbackInfo var1) {
      for (ServerData var3 : this.f_105427_) {
         if (a.d.containsKey(var3.f_105363_)) {
            var3.f_105363_ = (String)a.d.get(var3.f_105363_);
         }
      }
   }

   @Inject(method = "save", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
   public void afterSave(CallbackInfo var1) {
      for (ServerData var3 : this.f_105427_) {
         if (a.e.containsKey(var3.f_105363_)) {
            var3.f_105363_ = (String)a.e.get(var3.f_105363_);
         }
      }
   }
}
