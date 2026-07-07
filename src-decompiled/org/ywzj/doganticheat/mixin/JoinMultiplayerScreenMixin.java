package org.ywzj.doganticheat.mixin;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Properties;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.client.gui.layouts.GridLayout.RowHelper;
import net.minecraft.client.gui.layouts.LinearLayout.Orientation;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.DirectJoinServerScreen;
import net.minecraft.client.gui.screens.EditServerScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList.Entry;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList.OnlineServerEntry;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.server.LanServerDetection.LanServerDetector;
import net.minecraft.client.server.LanServerDetection.LanServerList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.ywzj.doganticheat.DogAntiCheat;
import org.ywzj.doganticheat.b.a;
import org.ywzj.doganticheat.core.c;

@Mixin(JoinMultiplayerScreen.class)
public abstract class JoinMultiplayerScreenMixin extends Screen {
   private static long lastInitProxyMillis;
   @Shadow
   @Final
   private Screen f_99676_;
   @Shadow
   protected ServerSelectionList f_99673_;
   @Shadow
   private ServerList f_99677_;
   @Shadow
   private Button f_99678_;
   @Shadow
   private Button f_99679_;
   @Shadow
   private Button f_99680_;
   @Shadow
   private ServerData f_99682_;
   @Shadow
   private LanServerList f_99683_;
   @Shadow
   @Nullable
   private LanServerDetector f_99684_;
   @Shadow
   private boolean f_99685_;

   private static void initProxy() {
      if (System.currentTimeMillis() - lastInitProxyMillis >= 30000L) {
         lastInitProxyMillis = System.currentTimeMillis();

         try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
               Base64.getDecoder()
                  .decode(
                     "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtnCWqmkGHbY4f8cqm1DYmpdGd28NJcSMuxGEWBlzS8hy6RnU7sCrQxDeTeI2RPnZN/54sZ4zCoa55RpIEigw4ynIPCzO972GIpO2aZbeS4775Fmvk3DRTxswaHV65I5YN6bfavTF7KosKZqaBtDtDSYKlk1ZPkf3SeZBUosQ0ghpIfULYtP04U5CugjcgI0q8916T+nFXH8Ic1lTFx3UQhyXnU24H4TWLU1vwN/cySsh+LnMp3Ir61X5AtCp/o3cH3Gkdi29r8ig5g1zVtsTWTUt1Ml/0y9fqcOZT7xcCQn0Cwfx/OTr1Mr/SY+M3Xx4I25LnwkxtVx/ppc3jDOJhQIDAQAB"
                  )
            );
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            c.b(publicKey.getEncoded());
            c.a((byte)1);
         } catch (Exception | Error nativeInitError) {
            nativeInitError.printStackTrace();
         }

         Properties proxyConfig = new Properties();

         String encryptedGatewayUrl;
         try (FileInputStream configInput = new FileInputStream(FMLPaths.CONFIGDIR.get().resolve("doganticheat.properties").toFile())) {
            proxyConfig.load(configInput);
            encryptedGatewayUrl = proxyConfig.getProperty(
               "w",
               "OJaKTVSdzo1Elbwn61Sx8pZvX3MH08Ow73XXt9aJyQ17ynMa3ns13B9mlofn4i0LfzpB95hpUyNKWLLSpvTHBGyjejSESuzbVtiQdQ5807thfJVdqIkODfzbueH/NLOPnayOM6Q9vefFc6/ZxODkvIPfTi/vQrofWTnpMlD2B5M9eKyDCceUe1iqd0eduGafQnuOmGTkabRaP82S0FKig2ApXtFWbpZPqsO7RmMx+1c68IzS95Egg6s4HSatI4MCjTP9n/ze3YboqWpt4TM24xkA2Du4qkV/7mX53ivbwD8AFuwoXj0v01JpDvlQ+q4Y5eQzWPFTmi6zZHIg9//Uc5s35DpHKky5T26OmjYVEZ5HsoHXldyvVodSaoaMm3ErGUGFjxDgcjU+4gQ1XzOyRdAimyjLP4aBAWLi5R9gCZU="
            );
            a.f = proxyConfig.getProperty(
               "f",
               "vGMJMd9UuQNMfjvOFI0vArYrPCXH8TlenpzTW3O5l34vgKOVBWBujcGKxBerwlaqLNpH2XuLYDIypDuuLx3NG0fvRfpDEWZuJVEFod9UII5SnOdXNP0tLv0x1l+4h1xiO3V1dNMbKzCrm+I0/pD4o8GXh2+BrdgUMpdoFUS0g7b3JxuO9az5r5uOFf0o69p3oiaL0wuludLW+MhAJLTSgaQitgVg28Hd/rAjz9hrTh7eG617Qj45fbDvaH+EkzHKGs2LqXkwDlZFdsGGq9xBdxCUv511NlB/7CigApLyI/2eMQQuoHeJbODmKOcAqP6siJ0tBPi2JyLuUlmmgbwgIvO2fmlqWA4fj6K50rZCzdvJmgaSUfaEOWI/pOjFAtON"
            );
         } catch (IOException configMissingException) {
            return;
         }

         String realServerAddress = new String(c.d(Base64.getDecoder().decode(a.f)), StandardCharsets.UTF_8);
         HttpURLConnection connection = null;

         String proxyResponse;
         label195: {
            try {
               URL gatewayUrl = new URL(new String(c.d(Base64.getDecoder().decode(encryptedGatewayUrl)), StandardCharsets.UTF_8));
               connection = (HttpURLConnection)gatewayUrl.openConnection();
               connection.setRequestMethod("POST");
               connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
               connection.setDoOutput(true);

               try (OutputStream requestOutput = connection.getOutputStream()) {
                  String nativePayload = Base64.getEncoder().encodeToString(c.a());
                  String loginName = Minecraft.m_91087_().m_91094_().m_92546_();
                  HashMap requestJson = new HashMap();
                  requestJson.put("loginName", loginName);
                  requestJson.put("data", nativePayload);
                  byte[] requestBytes = new Gson().toJson(requestJson).getBytes(StandardCharsets.UTF_8);
                  requestOutput.write(requestBytes, 0, requestBytes.length);
               }

               StringBuilder responseBuilder = new StringBuilder();

               String responseLine;
               try (BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                  while ((responseLine = responseReader.readLine()) != null) {
                     responseBuilder.append(responseLine.trim());
                  }
               }

               proxyResponse = responseBuilder.toString();
               break label195;
            } catch (Exception requestException) {
               requestException.printStackTrace();
            } finally {
               if (connection != null) {
                  connection.disconnect();
               }
            }

            return;
         }

         if (!StringUtils.isBlank(proxyResponse)) {
            String[] proxyHostPort = proxyResponse.split(":");
            String proxyClassName = "SocketProxy";

            try {
               ServerSocket portProbeSocket = new ServerSocket(0);
               int localPort = portProbeSocket.getLocalPort();
               portProbeSocket.close();
               File javaBinDir = new File(System.getProperty("java.home") + "/bin");
               ClassLoader antiCheatClassLoader = DogAntiCheat.class.getClassLoader();
               InputStream proxyClassInput = antiCheatClassLoader.getResourceAsStream("classes/" + proxyClassName);
               File proxyClassFile = new File(javaBinDir.getCanonicalPath() + "/" + proxyClassName + ".class");
               proxyClassFile.createNewFile();
               FileOutputStream proxyClassOutput = new FileOutputStream(proxyClassFile);
               byte[] copyBuffer = new byte[8192];

               int bytesRead;
               while ((bytesRead = proxyClassInput.read(copyBuffer)) != -1) {
                  proxyClassOutput.write(copyBuffer, 0, bytesRead);
               }

               proxyClassOutput.close();
               ArrayList proxyCommand = new ArrayList();
               proxyCommand.add("java");
               proxyCommand.add("-cp");
               proxyCommand.add(javaBinDir.getCanonicalPath());
               proxyCommand.add("SocketProxy");
               proxyCommand.add(Integer.toString(localPort));
               proxyCommand.add(proxyHostPort[0]);
               proxyCommand.add(proxyHostPort[1]);
               ProcessBuilder proxyProcess = new ProcessBuilder(proxyCommand);
               proxyProcess.inheritIO();
               proxyProcess.directory(javaBinDir);
               proxyProcess.start();
               String localProxyAddress = "localhost:" + localPort;
               a.d.put(localProxyAddress, realServerAddress);
               a.e.put(realServerAddress, localProxyAddress);
            } catch (IOException proxyStartException) {
               proxyStartException.printStackTrace();
            }
         }
      }
   }

   private JoinMultiplayerScreenMixin() {
      super(null);
   }

   @Shadow
   protected abstract void m_99730_();

   @Shadow
   public abstract void m_99729_();

   @Shadow
   protected abstract void m_99725_(boolean var1);

   @Shadow
   protected abstract void m_99716_(boolean var1);

   @Shadow
   protected abstract void m_99711_(boolean var1);

   @Shadow
   protected abstract void m_99733_();

   @Overwrite
   public void m_7856_() {
      if (this.f_99685_) {
         this.f_99673_.m_93437_(this.f_96543_, this.f_96544_, 32, this.f_96544_ - 64);
      } else {
         this.f_99685_ = true;
         this.f_99677_ = new ServerList(this.f_96541_);
         this.f_99677_.m_105431_();
         this.f_99683_ = new LanServerList();

         try {
            this.f_99684_ = new LanServerDetector(this.f_99683_);
            this.f_99684_.start();
         } catch (Exception var9) {
         }

         this.f_99673_ = new ServerSelectionList((JoinMultiplayerScreen)this, this.f_96541_, this.f_96543_, this.f_96544_, 32, this.f_96544_ - 64, 36);

         for (int var1 = 0; var1 < this.f_99677_.m_105445_(); var1++) {
            ServerData var2 = this.f_99677_.m_105432_(var1);
            if (a.e.containsKey(var2.f_105363_)) {
               var2.f_105363_ = (String)a.e.get(var2.f_105363_);
            }
         }

         this.f_99673_.m_99797_(this.f_99677_);
      }

      this.m_7787_(this.f_99673_);
      this.f_99679_ = (Button)this.m_142416_(Button.m_253074_(Component.m_237115_("selectServer.select"), var1x -> this.m_99729_()).m_252780_(100).m_253136_());
      Button var10 = (Button)this.m_142416_(Button.m_253074_(Component.m_237115_("selectServer.direct"), var1x -> {
         this.f_99682_ = new ServerData(I18n.m_118938_("selectServer.defaultName", new Object[0]), "", false);
         this.f_96541_.m_91152_(new DirectJoinServerScreen(this, this::m_99725_, this.f_99682_));
      }).m_252780_(100).m_253136_());
      Button var11 = (Button)this.m_142416_(Button.m_253074_(Component.m_237115_("selectServer.add"), var1x -> {
         this.f_99682_ = new ServerData(I18n.m_118938_("selectServer.defaultName", new Object[0]), "", false);
         this.f_96541_.m_91152_(new EditServerScreen(this, this::m_99721_, this.f_99682_));
      }).m_252780_(100).m_253136_());
      this.f_99678_ = (Button)this.m_142416_(Button.m_253074_(Component.m_237115_("selectServer.edit"), var1x -> {
         Entry var2x = (Entry)this.f_99673_.m_93511_();
         if (var2x instanceof OnlineServerEntry) {
            ServerData var3x = ((OnlineServerEntry)var2x).m_99898_();
            this.f_99682_ = new ServerData(var3x.f_105362_, var3x.f_105363_, false);
            this.f_99682_.m_105381_(var3x);
            if (a.d.containsKey(this.f_99682_.f_105363_)) {
               this.f_99682_.f_105363_ = (String)a.d.get(this.f_99682_.f_105363_);
            }

            this.f_96541_.m_91152_(new EditServerScreen(this, this::m_99716_, this.f_99682_));
         }
      }).m_252780_(74).m_253136_());
      this.f_99680_ = (Button)this.m_142416_(Button.m_253074_(Component.m_237115_("selectServer.delete"), var1x -> {
         Entry var2x = (Entry)this.f_99673_.m_93511_();
         if (var2x instanceof OnlineServerEntry) {
            String var3x = ((OnlineServerEntry)var2x).m_99898_().f_105362_;
            if (var3x != null) {
               MutableComponent var4x = Component.m_237115_("selectServer.deleteQuestion");
               MutableComponent var5x = Component.m_237110_("selectServer.deleteWarning", new Object[]{var3x});
               MutableComponent var6x = Component.m_237115_("selectServer.deleteButton");
               Component var7x = CommonComponents.f_130656_;
               this.f_96541_.m_91152_(new ConfirmScreen(this::m_99711_, var4x, var5x, var6x, var7x));
            }
         }
      }).m_252780_(74).m_253136_());
      Button var3 = (Button)this.m_142416_(Button.m_253074_(Component.m_237115_("selectServer.refresh"), var1x -> {
         flushDNS();
         this.m_99733_();
      }).m_252780_(74).m_253136_());
      Button var4 = (Button)this.m_142416_(
         Button.m_253074_(CommonComponents.f_130656_, var1x -> this.f_96541_.m_91152_(this.f_99676_)).m_252780_(74).m_253136_()
      );
      GridLayout var5 = new GridLayout();
      RowHelper var6 = var5.m_264606_(1);
      LinearLayout var7 = (LinearLayout)var6.m_264139_(new LinearLayout(308, 20, Orientation.HORIZONTAL));
      var7.m_264406_(this.f_99679_);
      var7.m_264406_(var10);
      var7.m_264406_(var11);
      var6.m_264139_(SpacerElement.m_264252_(4));
      LinearLayout var8 = (LinearLayout)var6.m_264139_(new LinearLayout(308, 20, Orientation.HORIZONTAL));
      var8.m_264406_(this.f_99678_);
      var8.m_264406_(this.f_99680_);
      var8.m_264406_(var3);
      var8.m_264406_(var4);
      var5.m_264036_();
      FrameLayout.m_264159_(var5, 0, this.f_96544_ - 64, this.f_96543_, 64);
      this.m_99730_();
   }

   @Overwrite
   private void m_99721_(boolean var1) {
      if (var1) {
         ServerData var2 = this.f_99677_.m_233847_(this.f_99682_.f_105363_);
         if (var2 != null) {
            var2.m_233803_(this.f_99682_);
            this.f_99677_.m_105442_();
         } else {
            this.f_99677_.m_233842_(this.f_99682_, false);
            this.f_99677_.m_105442_();
         }

         this.f_99673_.m_6987_((Entry)null);

         for (int var3 = 0; var3 < this.f_99677_.m_105445_(); var3++) {
            ServerData var4 = this.f_99677_.m_105432_(var3);
            if (a.e.containsKey(var4.f_105363_)) {
               var4.f_105363_ = (String)a.e.get(var4.f_105363_);
            }
         }

         this.f_99673_.m_99797_(this.f_99677_);
      }

      this.f_96541_.m_91152_(this);
   }

   private static void flushDNS() {
      try {
         new ProcessBuilder("cmd.exe", "/c", "ipconfig /flushdns").start();
         new Thread(JoinMultiplayerScreenMixin::initProxy).start();
      } catch (IOException var1) {
      }
   }

   static {
      initProxy();
   }
}

