package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import java.nio.file.DirectoryStream;
import java.nio.file.SecureDirectoryStream;
import java.util.Iterator;

final class DowngradedDirectoryStream implements DirectoryStream {
   private final SecureDirectoryStream secureDirectoryStream;

   DowngradedDirectoryStream(SecureDirectoryStream var1) {
      this.secureDirectoryStream = (SecureDirectoryStream)Preconditions.checkNotNull(var1);
   }

   @Override
   public Iterator iterator() {
      return this.secureDirectoryStream.iterator();
   }

   @Override
   public void close() {
      this.secureDirectoryStream.close();
   }
}
