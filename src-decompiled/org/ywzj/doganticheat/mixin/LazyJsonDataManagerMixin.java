package org.ywzj.doganticheat.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.tacz.guns.GunMod;
import com.tacz.guns.resource.manager.LazyJsonDataManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.ywzj.doganticheat.core.c;

@Mixin(value = LazyJsonDataManager.class, remap = false)
public abstract class LazyJsonDataManagerMixin {
   @Shadow
   @Final
   private Gson gson;

   @Shadow
   @Nullable
   protected abstract Reader openReader(ResourceManager var1, ResourceLocation var2);

   @Overwrite
   @Nullable
   public final JsonElement readResourceElement(ResourceManager var1, ResourceLocation var2) {
      try (Reader var3 = this.openReader(var1, var2)) {
         if (var3 == null) {
            return null;
         }

         JsonElement var16 = (JsonElement)GsonHelper.m_263475_(this.gson, var3, JsonElement.class, true);
         if (var16 instanceof JsonPrimitive) {
            throw new JsonParseException("err");
         }

         return var16;
      } catch (JsonParseException var14) {
         Resource var4 = (Resource)var1.m_213713_(var2).orElse(null);
         if (var4 == null) {
            return null;
         }

         try (InputStream var5 = var4.m_215507_()) {
            byte[] var6 = c.d(var5.readAllBytes());
            String var7 = new String(var6, StandardCharsets.UTF_8);
            return (JsonElement)GsonHelper.m_263467_(this.gson, var7, JsonElement.class, true);
         } catch (IOException | JsonParseException | IllegalArgumentException var13) {
            GunMod.LOGGER.error("Couldn't parse data file {} from {}", var2, var13);
         }
      } catch (IOException | IllegalArgumentException var15) {
         GunMod.LOGGER.error("Couldn't parse data file {} ", var2, var15);
      }

      return null;
   }
}
