package org.ywzj.doganticheat.jimfs;

import java.net.URI;

class SystemJimfsFileSystemProvider$1 implements Runnable {
   SystemJimfsFileSystemProvider$1(URI var1) {
      this.val$uri = var1;
   }

   @Override
   public void run() {
      SystemJimfsFileSystemProvider.access$000().remove(this.val$uri);
   }
}
