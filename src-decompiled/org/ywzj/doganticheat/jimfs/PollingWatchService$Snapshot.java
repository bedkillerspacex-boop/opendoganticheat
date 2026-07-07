package org.ywzj.doganticheat.jimfs;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.attribute.FileTime;
import java.util.Map;
import java.util.Map.Entry;

final class PollingWatchService$Snapshot {
   private final ImmutableMap modifiedTimes;

   PollingWatchService$Snapshot(PollingWatchService var1, Map var2) {
      this.this$0 = var1;
      this.modifiedTimes = ImmutableMap.copyOf(var2);
   }

   boolean postChanges(PollingWatchService$Snapshot var1, AbstractWatchService$Key var2) {
      boolean var3 = false;
      if (var2.subscribesTo(StandardWatchEventKinds.ENTRY_CREATE)) {
         for (Name var6 : Sets.difference(var1.modifiedTimes.keySet(), this.modifiedTimes.keySet())) {
            var2.post(new AbstractWatchService$Event(StandardWatchEventKinds.ENTRY_CREATE, 1, PollingWatchService.access$200(this.this$0).createFileName(var6)));
            var3 = true;
         }
      }

      if (var2.subscribesTo(StandardWatchEventKinds.ENTRY_DELETE)) {
         for (Name var13 : Sets.difference(this.modifiedTimes.keySet(), var1.modifiedTimes.keySet())) {
            var2.post(
               new AbstractWatchService$Event(StandardWatchEventKinds.ENTRY_DELETE, 1, PollingWatchService.access$200(this.this$0).createFileName(var13))
            );
            var3 = true;
         }
      }

      if (var2.subscribesTo(StandardWatchEventKinds.ENTRY_MODIFY)) {
         UnmodifiableIterator var10 = this.modifiedTimes.entrySet().iterator();

         while (var10.hasNext()) {
            Entry var12 = (Entry)var10.next();
            Name var14 = (Name)var12.getKey();
            FileTime var7 = (FileTime)var12.getValue();
            FileTime var8 = (FileTime)var1.modifiedTimes.get(var14);
            if (var8 != null && !var7.equals(var8)) {
               var2.post(
                  new AbstractWatchService$Event(StandardWatchEventKinds.ENTRY_MODIFY, 1, PollingWatchService.access$200(this.this$0).createFileName(var14))
               );
               var3 = true;
            }
         }
      }

      return var3;
   }
}
