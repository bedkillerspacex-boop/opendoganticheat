package org.ywzj.doganticheat.mixin;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.ywzj.doganticheat.a.a;

@Mixin(ReloadableResourceManager.class)
public class ReloadableResourceManagerMixin {
   @Inject(method = "createReload", at = @At("HEAD"))
   public void createFullReload(Executor var1, Executor var2, CompletableFuture var3, List var4, CallbackInfoReturnable var5) {
      a.a(var4);
   }
}
