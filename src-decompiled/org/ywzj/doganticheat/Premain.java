package org.ywzj.doganticheat;

import java.lang.instrument.Instrumentation;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.ywzj.doganticheat.core.NativeApi;

public class Premain {
   public static void premain(String var0, Instrumentation var1) {
      if (DogAntiCheat.loadDlls()) {
         new Thread(NativeApi::core0).start();
         DogAntiCheat.preInit = true;
      }

      var1.addTransformer(new Premain$1());

      try {
         X509EncodedKeySpec var2 = new X509EncodedKeySpec(
            Base64.getDecoder()
               .decode(
                  "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtnCWqmkGHbY4f8cqm1DYmpdGd28NJcSMuxGEWBlzS8hy6RnU7sCrQxDeTeI2RPnZN/54sZ4zCoa55RpIEigw4ynIPCzO972GIpO2aZbeS4775Fmvk3DRTxswaHV65I5YN6bfavTF7KosKZqaBtDtDSYKlk1ZPkf3SeZBUosQ0ghpIfULYtP04U5CugjcgI0q8916T+nFXH8Ic1lTFx3UQhyXnU24H4TWLU1vwN/cySsh+LnMp3Ir61X5AtCp/o3cH3Gkdi29r8ig5g1zVtsTWTUt1Ml/0y9fqcOZT7xcCQn0Cwfx/OTr1Mr/SY+M3Xx4I25LnwkxtVx/ppc3jDOJhQIDAQAB"
               )
         );
         KeyFactory var3 = KeyFactory.getInstance("RSA");
         PublicKey var4 = var3.generatePublic(var2);
         NativeApi.op2(var4.getEncoded());
         NativeApi.op3((byte)1);
      } catch (Exception | Error var5) {
         var5.printStackTrace();
      }
   }
}
