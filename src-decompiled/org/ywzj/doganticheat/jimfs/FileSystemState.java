package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.ClosedFileSystemException;
import java.nio.file.attribute.FileTime;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

final class FileSystemState implements Closeable {
   private final Set resources = Sets.newConcurrentHashSet();
   private final FileTimeSource fileTimeSource;
   private final Runnable onClose;
   private final AtomicBoolean open = new AtomicBoolean(true);
   private final AtomicInteger registering = new AtomicInteger();

   FileSystemState(FileTimeSource var1, Runnable var2) {
      this.fileTimeSource = (FileTimeSource)Preconditions.checkNotNull(var1);
      this.onClose = (Runnable)Preconditions.checkNotNull(var2);
   }

   public boolean isOpen() {
      return this.open.get();
   }

   public void checkOpen() {
      if (!this.open.get()) {
         throw new ClosedFileSystemException();
      }
   }

   @CanIgnoreReturnValue
   public Closeable register(Closeable var1) {
      this.checkOpen();
      this.registering.incrementAndGet();

      try {
         this.checkOpen();
         this.resources.add(var1);
         return var1;
      } finally {
         this.registering.decrementAndGet();
      }
   }

   public void unregister(Closeable var1) {
      this.resources.remove(var1);
   }

   public FileTime now() {
      return this.fileTimeSource.now();
   }

   @Override
   public void close() {
      if (this.open.compareAndSet(true, false)) {
         this.onClose.run();
         Throwable var1 = null;

         do {
            for (Closeable var3 : this.resources) {
               try {
                  var3.close();
               } catch (Throwable var8) {
                  if (var1 == null) {
                     var1 = var8;
                  } else {
                     var1.addSuppressed(var8);
                  }
               } finally {
                  this.resources.remove(var3);
               }
            }
         } while (this.registering.get() > 0 || !this.resources.isEmpty());

         Throwables.propagateIfPossible(var1, IOException.class);
      }
   }
}
