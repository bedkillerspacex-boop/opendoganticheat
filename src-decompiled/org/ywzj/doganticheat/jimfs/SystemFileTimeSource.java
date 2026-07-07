package org.ywzj.doganticheat.jimfs;

import java.nio.file.attribute.FileTime;

enum SystemFileTimeSource implements FileTimeSource {
   INSTANCE;

   @Override
   public FileTime now() {
      return FileTime.fromMillis(System.currentTimeMillis());
   }

   @Override
   public String toString() {
      return "SystemFileTimeSource";
   }
}
