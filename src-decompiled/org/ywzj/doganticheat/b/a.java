package org.ywzj.doganticheat.b;

import java.util.HashMap;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.ywzj.doganticheat.b.a.c;
import org.ywzj.doganticheat.b.a.e;
import org.ywzj.doganticheat.b.a.f;
import org.ywzj.doganticheat.b.a.g;
import org.ywzj.doganticheat.b.a.h;
import org.ywzj.doganticheat.b.b.d;
import org.ywzj.doganticheat.b.b.i;
import org.ywzj.doganticheat.b.b.j;
import org.ywzj.doganticheat.b.b.k;
import org.ywzj.doganticheat.b.b.l;
import org.ywzj.doganticheat.b.b.m;
import org.ywzj.doganticheat.b.b.n;
import org.ywzj.doganticheat.b.b.o;

public class a {
   public static SimpleChannel a;
   public static final String b = "1";
   public static final ResourceLocation c = new ResourceLocation("doganticheat", "data");
   public static final HashMap d = new HashMap();
   public static final HashMap e = new HashMap();
   public static String f;
   public static final String g = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtnCWqmkGHbY4f8cqm1DYmpdGd28NJcSMuxGEWBlzS8hy6RnU7sCrQxDeTeI2RPnZN/54sZ4zCoa55RpIEigw4ynIPCzO972GIpO2aZbeS4775Fmvk3DRTxswaHV65I5YN6bfavTF7KosKZqaBtDtDSYKlk1ZPkf3SeZBUosQ0ghpIfULYtP04U5CugjcgI0q8916T+nFXH8Ic1lTFx3UQhyXnU24H4TWLU1vwN/cySsh+LnMp3Ir61X5AtCp/o3cH3Gkdi29r8ig5g1zVtsTWTUt1Ml/0y9fqcOZT7xcCQn0Cwfx/OTr1Mr/SY+M3Xx4I25LnwkxtVx/ppc3jDOJhQIDAQAB";

   @SubscribeEvent
   public static void a(FMLCommonSetupEvent var0) {
      a = NetworkRegistry.newSimpleChannel(c, () -> "1", var0x -> true, var0x -> true);
      a.registerMessage(org.ywzj.doganticheat.b.b.a.b(), l.class, l::b, l::a, e::a, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      a.registerMessage(org.ywzj.doganticheat.b.b.b.b(), d.class, d::b, d::a, org.ywzj.doganticheat.b.a.b::a, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      a.registerMessage(
         org.ywzj.doganticheat.b.b.c.b(),
         org.ywzj.doganticheat.b.b.e.class,
         org.ywzj.doganticheat.b.b.e::b,
         org.ywzj.doganticheat.b.b.e::a,
         org.ywzj.doganticheat.b.a.b::a,
         Optional.of(NetworkDirection.PLAY_TO_SERVER)
      );
      a.registerMessage(org.ywzj.doganticheat.b.b.d.b(), j.class, j::b, j::a, c::a, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      a.registerMessage(
         org.ywzj.doganticheat.b.b.e.b(),
         org.ywzj.doganticheat.b.b.b.class,
         org.ywzj.doganticheat.b.b.b::b,
         org.ywzj.doganticheat.b.b.b::a,
         org.ywzj.doganticheat.b.a.b::a,
         Optional.of(NetworkDirection.PLAY_TO_SERVER)
      );
      a.registerMessage(org.ywzj.doganticheat.b.b.f.b(), k.class, k::b, k::a, org.ywzj.doganticheat.b.a.d::a, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      a.registerMessage(
         org.ywzj.doganticheat.b.b.g.b(),
         org.ywzj.doganticheat.b.b.c.class,
         org.ywzj.doganticheat.b.b.c::b,
         org.ywzj.doganticheat.b.b.c::a,
         org.ywzj.doganticheat.b.a.b::a,
         Optional.of(NetworkDirection.PLAY_TO_SERVER)
      );
      a.registerMessage(org.ywzj.doganticheat.b.b.h.b(), o.class, o::b, o::a, h::a, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      a.registerMessage(
         org.ywzj.doganticheat.b.b.i.b(),
         org.ywzj.doganticheat.b.b.h.class,
         org.ywzj.doganticheat.b.b.h::b,
         org.ywzj.doganticheat.b.b.h::a,
         org.ywzj.doganticheat.b.a.b::a,
         Optional.of(NetworkDirection.PLAY_TO_SERVER)
      );
      a.registerMessage(org.ywzj.doganticheat.b.b.j.b(), m.class, m::b, m::a, f::a, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      a.registerMessage(
         org.ywzj.doganticheat.b.b.k.b(),
         org.ywzj.doganticheat.b.b.f.class,
         org.ywzj.doganticheat.b.b.f::b,
         org.ywzj.doganticheat.b.b.f::a,
         org.ywzj.doganticheat.b.a.b::a,
         Optional.of(NetworkDirection.PLAY_TO_SERVER)
      );
      a.registerMessage(org.ywzj.doganticheat.b.b.l.b(), i.class, i::b, i::a, org.ywzj.doganticheat.b.a.a::a, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      a.registerMessage(
         org.ywzj.doganticheat.b.b.m.b(),
         org.ywzj.doganticheat.b.b.a.class,
         org.ywzj.doganticheat.b.b.a::b,
         org.ywzj.doganticheat.b.b.a::a,
         org.ywzj.doganticheat.b.a.b::a,
         Optional.of(NetworkDirection.PLAY_TO_SERVER)
      );
      a.registerMessage(org.ywzj.doganticheat.b.b.n.b(), n.class, n::b, n::a, g::a, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      a.registerMessage(
         org.ywzj.doganticheat.b.b.o.b(),
         org.ywzj.doganticheat.b.b.g.class,
         org.ywzj.doganticheat.b.b.g::b,
         org.ywzj.doganticheat.b.b.g::a,
         org.ywzj.doganticheat.b.a.b::a,
         Optional.of(NetworkDirection.PLAY_TO_SERVER)
      );
   }
}
