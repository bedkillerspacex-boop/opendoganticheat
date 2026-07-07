package org.ywzj.doganticheat.mixin;

import net.minecraft.client.CameraType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CameraType.class)
public class CameraTypeMixin {
   @Overwrite
   public CameraType m_90614_() {
      return CameraType.FIRST_PERSON;
   }
}
