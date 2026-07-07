package org.ywzj.doganticheat.jimfs;

import java.nio.file.attribute.FileTime;

public interface FileTimeSource {
   FileTime now();
}
