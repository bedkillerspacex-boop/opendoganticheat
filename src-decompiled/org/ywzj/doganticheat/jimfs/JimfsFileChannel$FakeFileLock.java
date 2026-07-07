package org.ywzj.doganticheat.jimfs;

import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.concurrent.atomic.AtomicBoolean;

final class JimfsFileChannel$FakeFileLock extends FileLock {
   private final AtomicBoolean valid = new AtomicBoolean(true);

   public JimfsFileChannel$FakeFileLock(FileChannel var1, long var2, long var4, boolean var6) {
      super(var1, var2, var4, var6);
   }

   public JimfsFileChannel$FakeFileLock(AsynchronousFileChannel var1, long var2, long var4, boolean var6) {
      super(var1, var2, var4, var6);
   }

   @Override
   public boolean isValid() {
      return this.valid.get();
   }

   @Override
   public void release() {
      this.valid.set(false);
   }
}
