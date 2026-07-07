package org.ywzj.doganticheat.mixin;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.tacz.guns.GunMod;
import com.tacz.guns.util.ResourceScanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.ywzj.doganticheat.core.c;

@Mixin(value = ResourceScanner.class, remap = false)
public class ResourceScannerMixin {
   @Overwrite
   public static Map scanDirectory(ResourceManager var0, FileToIdConverter var1, Gson var2) {
      HashMap var3 = Maps.newHashMap();

      for (Entry var5 : var1.m_247457_(var0).entrySet()) {
         ResourceLocation var6 = (ResourceLocation)var5.getKey();
         ResourceLocation var7 = var1.m_245273_(var6);

         try (BufferedReader var8 = ((Resource)var5.getValue()).m_215508_()) {
            JsonElement var18 = (JsonElement)GsonHelper.m_263475_(var2, var8, JsonElement.class, true);
            if (var18 instanceof JsonPrimitive) {
               throw new JsonParseException("err");
            }

            JsonElement var19 = var3.put(var7, var18);
            if (var19 != null) {
               throw new IllegalStateException("Duplicate data file ignored with ID " + var7);
            }
         } catch (JsonParseException var16) {
            try {
               byte[] var9 = c.d(((Resource)var5.getValue()).m_215507_().readAllBytes());
               String var10 = new String(var9, StandardCharsets.UTF_8);
               JsonElement var11 = (JsonElement)GsonHelper.m_263467_(var2, var10, JsonElement.class, true);
               JsonElement var12 = var3.put(var7, var11);
               if (var12 != null) {
                  throw new IllegalStateException("Duplicate data file ignored with ID " + var7);
               }
            } catch (IOException | JsonParseException | IllegalArgumentException var13) {
               GunMod.LOGGER.error("Couldn't parse data file {} from {}", var7, var6, var13);
            }
         } catch (IOException | IllegalArgumentException var17) {
            GunMod.LOGGER.error("Couldn't parse data file {} from {}", var7, var6, var17);
         }
      }

      return var3;
   }
}
