package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;

final class WatchServiceConfiguration$PollingConfig extends WatchServiceConfiguration {
   private final long interval;
   private final TimeUnit timeUnit;

   private WatchServiceConfiguration$PollingConfig(long var1, TimeUnit var3) {
      Preconditions.checkArgument(var1 > 0L, "interval (%s) must be positive", var1);
      this.interval = var1;
      this.timeUnit = (TimeUnit)Preconditions.checkNotNull(var3);
   }

   @Override
   AbstractWatchService newWatchService(FileSystemView var1, PathService var2) {
      return new PollingWatchService(var1, var2, var1.state(), this.interval, this.timeUnit);
   }

   @Override
   public String toString() {
      return "WatchServiceConfiguration.polling(" + this.interval + ", " + this.timeUnit + ")";
   }
}
