package org.ywzj.doganticheat.b.b;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import net.minecraft.network.FriendlyByteBuf;

public class n {
   private final boolean a;
   private final byte[] b;
   private final HashMap c;

   public n(boolean var1, byte[] var2, HashMap var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   public static n a(FriendlyByteBuf var0) {
      boolean var1 = var0.readBoolean();
      short var2 = var0.readShort();
      byte[] var3 = new byte[var2];
      var0.readBytes(var3);
      HashMap var4 = null;
      if (var0.readableBytes() > 0) {
         byte[] var5 = new byte[var0.readShort()];
         var0.readBytes(var5);
         String var6 = new String(var5);
         Gson var7 = new Gson();
         var4 = (HashMap)var7.fromJson(var6, TypeToken.getParameterized(HashMap.class, new Type[]{String.class, String.class}).getType());
      }

      return new n(var1, var3, var4);
   }

   public void b(FriendlyByteBuf var1) {
   }

   public boolean a() {
      return this.a;
   }

   public byte[] b() {
      return this.b;
   }

   public HashMap c() {
      return this.c;
   }
}
