package org.ywzj.doganticheat.jimfs;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.NotLinkException;
import java.nio.file.Path;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;

final class DirectoryEntry {
   private final Directory directory;
   private final Name name;
   private final @Nullable File file;
   @Nullable DirectoryEntry next;

   DirectoryEntry(Directory var1, Name var2, @Nullable File var3) {
      this.directory = (Directory)Preconditions.checkNotNull(var1);
      this.name = (Name)Preconditions.checkNotNull(var2);
      this.file = var3;
   }

   public boolean exists() {
      return this.file != null;
   }

   @CanIgnoreReturnValue
   public DirectoryEntry requireExists(Path var1) {
      if (!this.exists()) {
         throw new NoSuchFileException(var1.toString());
      } else {
         return this;
      }
   }

   @CanIgnoreReturnValue
   public DirectoryEntry requireDoesNotExist(Path var1) {
      if (this.exists()) {
         throw new FileAlreadyExistsException(var1.toString());
      } else {
         return this;
      }
   }

   @CanIgnoreReturnValue
   public DirectoryEntry requireDirectory(Path var1) {
      this.requireExists(var1);
      if (!this.file().isDirectory()) {
         throw new NotDirectoryException(var1.toString());
      } else {
         return this;
      }
   }

   @CanIgnoreReturnValue
   public DirectoryEntry requireSymbolicLink(Path var1) {
      this.requireExists(var1);
      if (!this.file().isSymbolicLink()) {
         throw new NotLinkException(var1.toString());
      } else {
         return this;
      }
   }

   public Directory directory() {
      return this.directory;
   }

   public Name name() {
      return this.name;
   }

   public File file() {
      Preconditions.checkState(this.exists());
      return this.file;
   }

   public @Nullable File fileOrNull() {
      return this.file;
   }

   @Override
   public boolean equals(Object var1) {
      if (!(var1 instanceof DirectoryEntry)) {
         return false;
      }

      DirectoryEntry var2 = (DirectoryEntry)var1;
      return this.directory.equals(var2.directory) && this.name.equals(var2.name) && Objects.equals(this.file, var2.file);
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.directory, this.name, this.file);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this).add("directory", this.directory).add("name", this.name).add("file", this.file).toString();
   }
}
