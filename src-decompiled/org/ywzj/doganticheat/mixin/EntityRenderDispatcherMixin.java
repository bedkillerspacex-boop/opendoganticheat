package org.ywzj.doganticheat.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
   @Overwrite
   private static void m_114441_(PoseStack var0, VertexConsumer var1, Entity var2, float var3) {
   }
}
