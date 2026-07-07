package org.ywzj.doganticheat.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Minecraft.class)
public class MinecraftMixin {
   @Overwrite
   public boolean m_91314_(Entity var1) {
      return var1.m_142038_();
   }
}
