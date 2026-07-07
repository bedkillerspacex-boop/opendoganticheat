package org.ywzj.doganticheat.jimfs;

import java.io.IOException;
import java.util.Map.Entry;

class PollingWatchService$1 implements Runnable {
   PollingWatchService$1(PollingWatchService var1) {
      this.this$0 = var1;
   }

   @Override
   public void run() {
      synchronized (this.this$0) {
         for (Entry var3 : PollingWatchService.access$000(this.this$0).entrySet()) {
            AbstractWatchService$Key var4 = (AbstractWatchService$Key)var3.getKey();
            PollingWatchService$Snapshot var5 = (PollingWatchService$Snapshot)var3.getValue();
            JimfsPath var6 = (JimfsPath)var4.watchable();

            try {
               PollingWatchService$Snapshot var7 = PollingWatchService.access$100(this.this$0, var6);
               boolean var8 = var5.postChanges(var7, var4);
               var3.setValue(var7);
               if (var8) {
                  var4.signal();
               }
            } catch (IOException var10) {
               var4.cancel();
            }
         }
      }
   }
}
