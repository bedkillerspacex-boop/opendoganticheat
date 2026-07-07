package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Configuration$Builder {
   public static final int DEFAULT_BLOCK_SIZE = 8192;
   public static final long DEFAULT_MAX_SIZE = 4294967296L;
   public static final long DEFAULT_MAX_CACHE_SIZE = -1L;
   private final PathType pathType;
   private ImmutableSet nameDisplayNormalization = ImmutableSet.of();
   private ImmutableSet nameCanonicalNormalization = ImmutableSet.of();
   private boolean pathEqualityUsesCanonicalForm = false;
   private int blockSize = 8192;
   private long maxSize = 4294967296L;
   private long maxCacheSize = -1L;
   private ImmutableSet attributeViews = ImmutableSet.of();
   private Set attributeProviders = null;
   private Map defaultAttributeValues;
   private FileTimeSource fileTimeSource = SystemFileTimeSource.INSTANCE;
   private WatchServiceConfiguration watchServiceConfig = WatchServiceConfiguration.DEFAULT;
   private ImmutableSet roots = ImmutableSet.of();
   private String workingDirectory;
   private ImmutableSet supportedFeatures = ImmutableSet.of();
   private String displayName;
   private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("[^:]+:[^:]+");

   private Configuration$Builder(PathType var1) {
      this.pathType = (PathType)Preconditions.checkNotNull(var1);
   }

   private Configuration$Builder(Configuration var1) {
      this.pathType = var1.pathType;
      this.nameDisplayNormalization = var1.nameDisplayNormalization;
      this.nameCanonicalNormalization = var1.nameCanonicalNormalization;
      this.pathEqualityUsesCanonicalForm = var1.pathEqualityUsesCanonicalForm;
      this.blockSize = var1.blockSize;
      this.maxSize = var1.maxSize;
      this.maxCacheSize = var1.maxCacheSize;
      this.attributeViews = var1.attributeViews;
      this.attributeProviders = var1.attributeProviders.isEmpty() ? null : new HashSet(var1.attributeProviders);
      this.defaultAttributeValues = var1.defaultAttributeValues.isEmpty() ? null : new HashMap(var1.defaultAttributeValues);
      this.fileTimeSource = var1.fileTimeSource;
      this.watchServiceConfig = var1.watchServiceConfig;
      this.roots = var1.roots;
      this.workingDirectory = var1.workingDirectory;
      this.supportedFeatures = var1.supportedFeatures;
   }

   @CanIgnoreReturnValue
   public Configuration$Builder setNameDisplayNormalization(PathNormalization var1, PathNormalization... var2) {
      this.nameDisplayNormalization = this.checkNormalizations(Lists.asList(var1, var2));
      return this;
   }

   @CanIgnoreReturnValue
   public Configuration$Builder setNameCanonicalNormalization(PathNormalization var1, PathNormalization... var2) {
      this.nameCanonicalNormalization = this.checkNormalizations(Lists.asList(var1, var2));
      return this;
   }

   private ImmutableSet checkNormalizations(List var1) {
      PathNormalization var2 = null;
      PathNormalization var3 = null;
      PathNormalization var4 = null;

      for (PathNormalization var6 : var1) {
         Preconditions.checkNotNull(var6);
         checkNormalizationNotSet(var6, var2);
         switch (var6) {
            case NONE:
               var2 = var6;
               break;
            case NFC:
            case NFD:
               checkNormalizationNotSet(var6, var3);
               var3 = var6;
               break;
            case CASE_FOLD_UNICODE:
            case CASE_FOLD_ASCII:
               checkNormalizationNotSet(var6, var4);
               var4 = var6;
               break;
            default:
               throw new AssertionError();
         }
      }

      return var2 != null ? ImmutableSet.of() : Sets.immutableEnumSet(var1);
   }

   private static void checkNormalizationNotSet(PathNormalization var0, @Nullable PathNormalization var1) {
      if (var1 != null) {
         throw new IllegalArgumentException("can't set normalization " + var0 + ": normalization " + var1 + " already set");
      }
   }

   @CanIgnoreReturnValue
   public Configuration$Builder setPathEqualityUsesCanonicalForm(boolean var1) {
      this.pathEqualityUsesCanonicalForm = var1;
      return this;
   }

   @CanIgnoreReturnValue
   public Configuration$Builder setBlockSize(int var1) {
      Preconditions.checkArgument(var1 > 0, "blockSize (%s) must be positive", var1);
      this.blockSize = var1;
      return this;
   }

   @CanIgnoreReturnValue
   public Configuration$Builder setMaxSize(long var1) {
      Preconditions.checkArgument(var1 > 0L, "maxSize (%s) must be positive", var1);
      this.maxSize = var1;
      return this;
   }

   @CanIgnoreReturnValue
   public Configuration$Builder setMaxCacheSize(long var1) {
      Preconditions.checkArgument(var1 >= 0L, "maxCacheSize (%s) may not be negative", var1);
      this.maxCacheSize = var1;
      return this;
   }

   @CanIgnoreReturnValue
   public Configuration$Builder setAttributeViews(String var1, String... var2) {
      this.attributeViews = ImmutableSet.copyOf(Lists.asList(var1, var2));
      return this;
   }

   @CanIgnoreReturnValue
   public Configuration$Builder addAttributeProvider(AttributeProvider var1) {
      Preconditions.checkNotNull(var1);
      if (this.attributeProviders == null) {
         this.attributeProviders = new HashSet();
      }

      this.attributeProviders.add(var1);
      return this;
   }

   @CanIgnoreReturnValue
   public Configuration$Builder setDefaultAttributeValue(String var1, Object var2) {
      Preconditions.checkArgument(ATTRIBUTE_PATTERN.matcher(var1).matches(), "attribute (%s) must be of the form \"view:attribute\"", var1);
      Preconditions.checkNotNull(var2);
      if (this.defaultAttributeValues == null) {
         this.defaultAttributeValues = new HashMap();
      }

      this.defaultAttributeValues.put(var1, var2);
      return this;
   }

   @CanIgnoreReturnValue
   public Configuration$Builder setFileTimeSource(FileTimeSource var1) {
      this.fileTimeSource = (FileTimeSource)Preconditions.checkNotNull(var1);
      return this;
   }

   @CanIgnoreReturnValue
   public Configuration$Builder setRoots(String var1, String... var2) {
      List var3 = Lists.asList(var1, var2);

      for (String var5 : var3) {
         PathType$ParseResult var6 = this.pathType.parsePath(var5);
         Preconditions.checkArgument(var6.isRoot(), "invalid root: %s", var5);
      }

      this.roots = ImmutableSet.copyOf(var3);
      return this;
   }

   @CanIgnoreReturnValue
   public Configuration$Builder setWorkingDirectory(String var1) {
      PathType$ParseResult var2 = this.pathType.parsePath(var1);
      Preconditions.checkArgument(var2.isAbsolute(), "working directory must be an absolute path: %s", var1);
      this.workingDirectory = (String)Preconditions.checkNotNull(var1);
      return this;
   }

   @CanIgnoreReturnValue
   public Configuration$Builder setSupportedFeatures(Feature... var1) {
      this.supportedFeatures = Sets.immutableEnumSet(Arrays.asList(var1));
      return this;
   }

   @CanIgnoreReturnValue
   public Configuration$Builder setWatchServiceConfiguration(WatchServiceConfiguration var1) {
      this.watchServiceConfig = (WatchServiceConfiguration)Preconditions.checkNotNull(var1);
      return this;
   }

   @CanIgnoreReturnValue
   private Configuration$Builder setDisplayName(String var1) {
      this.displayName = (String)Preconditions.checkNotNull(var1);
      return this;
   }

   public Configuration build() {
      return new Configuration(this, null);
   }
}
