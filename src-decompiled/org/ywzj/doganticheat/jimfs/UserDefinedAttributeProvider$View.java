package org.ywzj.doganticheat.jimfs;

import java.nio.ByteBuffer;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;

class UserDefinedAttributeProvider$View extends AbstractAttributeView implements UserDefinedFileAttributeView {
   public UserDefinedAttributeProvider$View(FileLookup var1) {
      super(var1);
   }

   @Override
   public String name() {
      return "user";
   }

   @Override
   public List list() {
      return UserDefinedAttributeProvider.access$000(this.lookupFile()).asList();
   }

   private byte[] getStoredBytes(String var1) {
      byte[] var2 = (byte[])this.lookupFile().getAttribute(this.name(), var1);
      if (var2 == null) {
         throw new IllegalArgumentException("attribute '" + this.name() + ":" + var1 + "' is not set");
      } else {
         return var2;
      }
   }

   @Override
   public int size(String var1) {
      return this.getStoredBytes(var1).length;
   }

   @Override
   public int read(String var1, ByteBuffer var2) {
      byte[] var3 = this.getStoredBytes(var1);
      var2.put(var3);
      return var3.length;
   }

   @Override
   public int write(String var1, ByteBuffer var2) {
      byte[] var3 = new byte[var2.remaining()];
      var2.get(var3);
      this.lookupFile().setAttribute(this.name(), var1, var3);
      return var3.length;
   }

   @Override
   public void delete(String var1) {
      this.lookupFile().deleteAttribute(this.name(), var1);
   }
}
