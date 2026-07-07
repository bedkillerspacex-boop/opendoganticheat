package org.ywzj.doganticheat.mixin;

import com.tacz.guns.client.event.RenderHeadShotAABB;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = RenderHeadShotAABB.class, remap = false)
public class RenderHeadShotAABBMixin {
   @Overwrite
   public static void onRenderEntity(Post var0) {
   }
}
