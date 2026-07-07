package org.ywzj.doganticheat.b.a;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkEvent.Context;
import org.apache.commons.io.FileUtils;
import org.ywzj.doganticheat.b.b.n;

public class g {
    private static final ExecutorService updateExecutor = Executors.newSingleThreadExecutor();
    private static final ConcurrentHashMap pendingChunks = new ConcurrentHashMap();
    private static ByteArrayOutputStream updateBuffer;
    private static int nextChunkIndex;

    public static void a(n updatePacket, Supplier contextSupplier) {
        Context context = (Context)contextSupplier.get();
        context.setPacketHandled(true);
        context.enqueueWork(() -> g.a(updatePacket));
    }

    private static void a(n updatePacket) {
        updateExecutor.execute(() -> {
            if (updateBuffer == null) {
                updateBuffer = new ByteArrayOutputStream();
                pendingChunks.clear();
                nextChunkIndex = 0;
                new Thread(() -> {
                    int secondsUntilTimeout = 60;
                    int lastObservedIndex = 0;
                    while (secondsUntilTimeout > 0 && updateBuffer != null) {
                        if (lastObservedIndex == nextChunkIndex) {
                            --secondsUntilTimeout;
                        } else {
                            lastObservedIndex = nextChunkIndex;
                        }
                        try {
                            Thread.sleep(1000L);
                        } catch (Exception ignored) {
                        }
                    }
                    if (secondsUntilTimeout <= 0) {
                        updateBuffer = null;
                        org.ywzj.doganticheat.c.a.a("客户端更新因丢包而失败");
                    }
                }).start();
            }

            int chunkIndex = Integer.parseInt((String)updatePacket.c().get("index"));
            if (chunkIndex == nextChunkIndex) {
                g.b(updatePacket);
                ++nextChunkIndex;
                while (pendingChunks.containsKey(nextChunkIndex)) {
                    g.b((n)pendingChunks.remove(nextChunkIndex));
                    ++nextChunkIndex;
                }
            } else {
                pendingChunks.put(chunkIndex, updatePacket);
            }
        });
    }

    private static void b(n updatePacket) {
        updateBuffer.writeBytes(updatePacket.b());
        if (!updatePacket.a()) {
            return;
        }

        try {
            byte[] updateBytes = updateBuffer.toByteArray();
            File targetModFile = FMLPaths.MODSDIR.get().resolve((String)updatePacket.c().get("fileName")).toFile();
            if (!org.ywzj.doganticheat.c.a.a(new ByteArrayInputStream(updateBytes)).equals(updatePacket.c().get("hash"))) {
                Minecraft.m_91087_().m_18707_(() -> org.ywzj.doganticheat.b.a.a.sendToServer(new org.ywzj.doganticheat.b.b.g(false, "客户端更新校验失败")));
                return;
            }

            FileUtils.writeByteArrayToFile(targetModFile, updateBytes);
            if (!org.ywzj.doganticheat.c.a.a(new FileInputStream(targetModFile)).equals(updatePacket.c().get("hash"))) {
                Minecraft.m_91087_().m_18707_(() -> org.ywzj.doganticheat.b.a.a.sendToServer(new org.ywzj.doganticheat.b.b.g(false, "客户端更新保存失败")));
            } else {
                Minecraft.m_91087_().m_18707_(() -> org.ywzj.doganticheat.b.a.a.sendToServer(new org.ywzj.doganticheat.b.b.g(true, "客户端更新保存成功")));
            }
        } catch (Exception writeException) {
            writeException.printStackTrace();
            Minecraft.m_91087_().m_18707_(() -> org.ywzj.doganticheat.b.a.a.sendToServer(new org.ywzj.doganticheat.b.b.g(false, "客户端更新失败: " + Arrays.toString(writeException.getStackTrace()))));
        } finally {
            updateBuffer = null;
        }
    }

    static {
        nextChunkIndex = 0;
    }
}
