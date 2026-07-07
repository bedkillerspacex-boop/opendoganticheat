package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;

final class DowngradedSeekableByteChannel implements SeekableByteChannel {
   private final FileChannel channel;

   DowngradedSeekableByteChannel(FileChannel var1) {
      this.channel = (FileChannel)Preconditions.checkNotNull(var1);
   }

   @Override
   public int read(ByteBuffer var1) {
      return this.channel.read(var1);
   }

   @Override
   public int write(ByteBuffer var1) {
      return this.channel.write(var1);
   }

   @Override
   public long position() {
      return this.channel.position();
   }

   @CanIgnoreReturnValue
   @Override
   public SeekableByteChannel position(long var1) {
      this.channel.position(var1);
      return this;
   }

   @Override
   public long size() {
      return this.channel.size();
   }

   @CanIgnoreReturnValue
   @Override
   public SeekableByteChannel truncate(long var1) {
      this.channel.truncate(var1);
      return this;
   }

   @Override
   public boolean isOpen() {
      return this.channel.isOpen();
   }

   @Override
   public void close() {
      this.channel.close();
   }
}
