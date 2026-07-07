package org.ywzj.doganticheat.mixin;

import com.mojang.datafixers.util.Pair;
import java.util.function.Function;
import java.util.regex.Pattern;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.ywzj.doganticheat.a.c;

@Mixin(BlockModel.class)
public class BlockModelMixin {
   private static final String regex = ".*/stone$";

   @Inject(
      method = "bake(Lnet/minecraft/client/resources/model/ModelBaker;Lnet/minecraft/client/renderer/block/model/BlockModel;Ljava/util/function/Function;Lnet/minecraft/client/resources/model/ModelState;Lnet/minecraft/resources/ResourceLocation;Z)Lnet/minecraft/client/resources/model/BakedModel;",
      at = @At("HEAD")
   )
   public void bake(ModelBaker var1, BlockModel var2, Function var3, ModelState var4, ResourceLocation var5, boolean var6, CallbackInfoReturnable var7) {
      BlockModel var8 = (BlockModel)this;
      if (!var8.f_111416_.contains("infested") && !var8.f_111416_.contains("item")) {
         if (Pattern.matches(".*/stone$", var8.f_111416_)) {
            c.a.put("stone", new Pair(var8, var3));
         }
      }
   }
}
