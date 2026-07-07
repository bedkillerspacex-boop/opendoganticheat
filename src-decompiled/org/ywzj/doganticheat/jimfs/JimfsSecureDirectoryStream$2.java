package org.ywzj.doganticheat.jimfs;

import com.google.common.collect.ImmutableSet;

class JimfsSecureDirectoryStream$2 implements FileLookup {
   JimfsSecureDirectoryStream$2(JimfsSecureDirectoryStream var1, JimfsPath var2, ImmutableSet var3) {
      this.this$0 = var1;
      this.val$checkedPath = var2;
      this.val$optionsSet = var3;
   }

   @Override
   public File lookup() {
      this.this$0.checkOpen();
      return JimfsSecureDirectoryStream.access$100(this.this$0)
         .lookUpWithLock(this.val$checkedPath, this.val$optionsSet)
         .requireExists(this.val$checkedPath)
         .file();
   }
}
