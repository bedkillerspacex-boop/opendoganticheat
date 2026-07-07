package org.ywzj.doganticheat.jimfs;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import org.checkerframework.checker.nullness.qual.Nullable;

final class BasicAttributeProvider extends AttributeProvider {
   private static final ImmutableSet ATTRIBUTES = ImmutableSet.of(
      "size", "fileKey", "isDirectory", "isRegularFile", "isSymbolicLink", "isOther", new String[]{"creationTime", "lastAccessTime", "lastModifiedTime"}
   );

   @Override
   public String name() {
      return "basic";
   }

   @Override
   public ImmutableSet fixedAttributes() {
      return ATTRIBUTES;
   }

   @Override
   public @Nullable Object get(File var1, String var2) {
      switch (var2) {
         case "size":
            return var1.size();
         case "fileKey":
            return var1.id();
         case "isDirectory":
            return var1.isDirectory();
         case "isRegularFile":
            return var1.isRegularFile();
         case "isSymbolicLink":
            return var1.isSymbolicLink();
         case "isOther":
            return !var1.isDirectory() && !var1.isRegularFile() && !var1.isSymbolicLink();
         case "creationTime":
            return var1.getCreationTime();
         case "lastAccessTime":
            return var1.getLastAccessTime();
         case "lastModifiedTime":
            return var1.getLastModifiedTime();
         default:
            return null;
      }
   }

   @Override
   public void set(File var1, String var2, String var3, Object var4, boolean var5) {
      switch (var3) {
         case "creationTime":
            checkNotCreate(var2, var3, var5);
            var1.setCreationTime((FileTime)checkType(var2, var3, var4, FileTime.class));
            break;
         case "lastAccessTime":
            checkNotCreate(var2, var3, var5);
            var1.setLastAccessTime((FileTime)checkType(var2, var3, var4, FileTime.class));
            break;
         case "lastModifiedTime":
            checkNotCreate(var2, var3, var5);
            var1.setLastModifiedTime((FileTime)checkType(var2, var3, var4, FileTime.class));
            break;
         case "size":
         case "fileKey":
         case "isDirectory":
         case "isRegularFile":
         case "isSymbolicLink":
         case "isOther":
            throw unsettable(var2, var3, var5);
      }
   }

   @Override
   public Class viewType() {
      return BasicFileAttributeView.class;
   }

   public BasicFileAttributeView view(FileLookup var1, ImmutableMap var2) {
      return new BasicAttributeProvider$View(var1);
   }

   @Override
   public Class attributesType() {
      return BasicFileAttributes.class;
   }

   @Override
   public BasicFileAttributes readAttributes(File var1) {
      return new BasicAttributeProvider$Attributes(var1);
   }
}
