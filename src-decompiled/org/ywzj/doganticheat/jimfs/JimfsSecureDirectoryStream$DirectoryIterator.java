package org.ywzj.doganticheat.jimfs;

import com.google.common.collect.AbstractIterator;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.Path;
import java.util.Iterator;
import org.checkerframework.checker.nullness.qual.Nullable;

final class JimfsSecureDirectoryStream$DirectoryIterator extends AbstractIterator {
   private @Nullable Iterator fileNames;

   private JimfsSecureDirectoryStream$DirectoryIterator(JimfsSecureDirectoryStream var1) {
      this.this$0 = var1;
   }

   protected synchronized Path computeNext() {
      this.this$0.checkOpen();

      try {
         if (this.fileNames == null) {
            this.fileNames = JimfsSecureDirectoryStream.access$100(this.this$0).snapshotWorkingDirectoryEntries().iterator();
         }

         while (this.fileNames.hasNext()) {
            Name var1 = (Name)this.fileNames.next();
            JimfsPath var2 = JimfsSecureDirectoryStream.access$100(this.this$0).getWorkingDirectoryPath().resolve(var1);
            if (JimfsSecureDirectoryStream.access$200(this.this$0).accept(var2)) {
               return var2;
            }
         }

         return (Path)this.endOfData();
      } catch (IOException var3) {
         throw new DirectoryIteratorException(var3);
      }
   }
}
