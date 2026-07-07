package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.checkerframework.checker.nullness.qual.Nullable;

final class UnixAttributeProvider extends AttributeProvider {
   private static final ImmutableSet ATTRIBUTES = ImmutableSet.of("uid", "ino", "dev", "nlink", "rdev", "ctime", new String[]{"mode", "gid"});
   private static final ImmutableSet INHERITED_VIEWS = ImmutableSet.of("basic", "owner", "posix");
   private final AtomicInteger uidGenerator = new AtomicInteger();
   private final ConcurrentMap idCache = new ConcurrentHashMap();

   @Override
   public String name() {
      return "unix";
   }

   @Override
   public ImmutableSet inherits() {
      return INHERITED_VIEWS;
   }

   @Override
   public ImmutableSet fixedAttributes() {
      return ATTRIBUTES;
   }

   @Override
   public Class viewType() {
      return UnixFileAttributeView.class;
   }

   public UnixFileAttributeView view(FileLookup var1, ImmutableMap var2) {
      throw new UnsupportedOperationException();
   }

   private Integer getUniqueId(Object var1) {
      Integer var2 = (Integer)this.idCache.get(var1);
      if (var2 == null) {
         var2 = this.uidGenerator.incrementAndGet();
         Integer var3 = this.idCache.putIfAbsent(var1, var2);
         if (var3 != null) {
            return var3;
         }
      }

      return var2;
   }

   @Override
   public @Nullable Object get(File var1, String var2) {
      switch (var2) {
         case "uid":
            UserPrincipal var5 = (UserPrincipal)var1.getAttribute("owner", "owner");
            return this.getUniqueId(var5);
         case "gid":
            GroupPrincipal var6 = (GroupPrincipal)var1.getAttribute("posix", "group");
            return this.getUniqueId(var6);
         case "mode":
            Set var7 = (Set)var1.getAttribute("posix", "permissions");
            return toMode(var7);
         case "ctime":
            return var1.getCreationTime();
         case "rdev":
            return 0L;
         case "dev":
            return 1L;
         case "ino":
            return var1.id();
         case "nlink":
            return var1.links();
         default:
            return null;
      }
   }

   @Override
   public void set(File var1, String var2, String var3, Object var4, boolean var5) {
      throw unsettable(var2, var3, var5);
   }

   private static int toMode(Set var0) {
      short var1 = 0;

      for (PosixFilePermission var3 : var0) {
         Preconditions.checkNotNull(var3);
         switch (var3) {
            case OWNER_READ:
               var1 |= 256;
               break;
            case OWNER_WRITE:
               var1 |= 128;
               break;
            case OWNER_EXECUTE:
               var1 |= 64;
               break;
            case GROUP_READ:
               var1 |= 32;
               break;
            case GROUP_WRITE:
               var1 |= 16;
               break;
            case GROUP_EXECUTE:
               var1 |= 8;
               break;
            case OTHERS_READ:
               var1 |= 4;
               break;
            case OTHERS_WRITE:
               var1 |= 2;
               break;
            case OTHERS_EXECUTE:
               var1 |= 1;
               break;
            default:
               throw new AssertionError();
         }
      }

      return var1;
   }
}
