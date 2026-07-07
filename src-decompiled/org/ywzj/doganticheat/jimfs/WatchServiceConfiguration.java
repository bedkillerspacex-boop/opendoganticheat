package org.ywzj.doganticheat.jimfs;

import java.util.concurrent.TimeUnit;

public abstract class WatchServiceConfiguration {
   static final WatchServiceConfiguration DEFAULT = polling(5L, TimeUnit.SECONDS);

   public static WatchServiceConfiguration polling(long var0, TimeUnit var2) {
      return new WatchServiceConfiguration$PollingConfig(var0, var2, null);
   }

   WatchServiceConfiguration() {
   }

   abstract AbstractWatchService newWatchService(FileSystemView var1, PathService var2);
}
