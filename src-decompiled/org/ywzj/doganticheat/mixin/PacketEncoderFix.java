package org.ywzj.doganticheat.mixin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketEncoder;
import net.minecraft.network.SkipPacketException;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.util.profiling.jfr.JvmProfiler;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PacketEncoder.class)
public class PacketEncoderFix {
   @Shadow
   @Final
   private static Logger f_130538_;
   @Shadow
   @Final
   private PacketFlow f_130540_;

   @Overwrite
   protected void encode(ChannelHandlerContext var1, Packet var2, ByteBuf var3) {
      ConnectionProtocol var4 = (ConnectionProtocol)var1.channel().attr(Connection.f_129461_).get();
      if (var4 == null) {
         throw new RuntimeException("ConnectionProtocol unknown: " + var2);
      }

      int var5 = var4.m_264521_(this.f_130540_, var2);
      if (f_130538_.isDebugEnabled()) {
         f_130538_.debug(
            Connection.f_202555_, "OUT: [{}:{}] {}", new Object[]{var1.channel().attr(Connection.f_129461_).get(), var5, var2.getClass().getName()}
         );
      }

      if (var5 != -1) {
         FriendlyByteBuf var6 = new FriendlyByteBuf(var3);
         var6.m_130130_(var5);

         try {
            int var7 = var6.writerIndex();
            var2.m_5779_(var6);
            int var8 = var6.writerIndex() - var7;
            if (var8 > 8388608) {
               throw new IllegalArgumentException("Packet too big (is " + var8 + ", should be less than 8388608): " + var2);
            }

            int var9 = ((ConnectionProtocol)var1.channel().attr(Connection.f_129461_).get()).m_129582_();
            JvmProfiler.f_185340_.m_183508_(var9, var5, var1.channel().remoteAddress(), var8);
         } catch (Throwable var10) {
            f_130538_.error("Error receiving packet {}", var5, var10);
            if (var2.m_6588_()) {
               throw new SkipPacketException(var10);
            } else {
               throw var10;
            }
         }
      }
   }
}
