package org.ywzj.doganticheat.jimfs;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public final class Configuration {
   final PathType pathType;
   final ImmutableSet nameDisplayNormalization;
   final ImmutableSet nameCanonicalNormalization;
   final boolean pathEqualityUsesCanonicalForm;
   final int blockSize;
   final long maxSize;
   final long maxCacheSize;
   final ImmutableSet attributeViews;
   final ImmutableSet attributeProviders;
   final ImmutableMap defaultAttributeValues;
   final FileTimeSource fileTimeSource;
   final WatchServiceConfiguration watchServiceConfig;
   final ImmutableSet roots;
   final String workingDirectory;
   final ImmutableSet supportedFeatures;
   private final String displayName;

   public static Configuration unix() {
      return Configuration$UnixHolder.access$000();
   }

   public static Configuration osX() {
      return Configuration$OsxHolder.access$200();
   }

   public static Configuration windows() {
      return Configuration$WindowsHolder.access$300();
   }

   public static Configuration forCurrentPlatform() {
      String var0 = System.getProperty("os.name");
      if (var0.contains("Windows")) {
         return windows();
      } else {
         return var0.contains("OS X") ? osX() : unix();
      }
   }

   public static Configuration$Builder builder(PathType var0) {
      return new Configuration$Builder(var0, null);
   }

   private Configuration(Configuration$Builder var1) {
      this.pathType = Configuration$Builder.access$500(var1);
      this.nameDisplayNormalization = Configuration$Builder.access$600(var1);
      this.nameCanonicalNormalization = Configuration$Builder.access$700(var1);
      this.pathEqualityUsesCanonicalForm = Configuration$Builder.access$800(var1);
      this.blockSize = Configuration$Builder.access$900(var1);
      this.maxSize = Configuration$Builder.access$1000(var1);
      this.maxCacheSize = Configuration$Builder.access$1100(var1);
      this.attributeViews = Configuration$Builder.access$1200(var1);
      this.attributeProviders = Configuration$Builder.access$1300(var1) == null
         ? ImmutableSet.of()
         : ImmutableSet.copyOf(Configuration$Builder.access$1300(var1));
      this.defaultAttributeValues = Configuration$Builder.access$1400(var1) == null
         ? ImmutableMap.of()
         : ImmutableMap.copyOf(Configuration$Builder.access$1400(var1));
      this.fileTimeSource = Configuration$Builder.access$1500(var1);
      this.watchServiceConfig = Configuration$Builder.access$1600(var1);
      this.roots = Configuration$Builder.access$1700(var1);
      this.workingDirectory = Configuration$Builder.access$1800(var1);
      this.supportedFeatures = Configuration$Builder.access$1900(var1);
      this.displayName = Configuration$Builder.access$2000(var1);
   }

   @Override
   public String toString() {
      if (this.displayName != null) {
         return MoreObjects.toStringHelper(this).addValue(this.displayName).toString();
      }

      ToStringHelper var1 = MoreObjects.toStringHelper(this)
         .add("pathType", this.pathType)
         .add("roots", this.roots)
         .add("supportedFeatures", this.supportedFeatures)
         .add("workingDirectory", this.workingDirectory);
      if (!this.nameDisplayNormalization.isEmpty()) {
         var1.add("nameDisplayNormalization", this.nameDisplayNormalization);
      }

      if (!this.nameCanonicalNormalization.isEmpty()) {
         var1.add("nameCanonicalNormalization", this.nameCanonicalNormalization);
      }

      var1.add("pathEqualityUsesCanonicalForm", this.pathEqualityUsesCanonicalForm).add("blockSize", this.blockSize).add("maxSize", this.maxSize);
      if (this.maxCacheSize != -1L) {
         var1.add("maxCacheSize", this.maxCacheSize);
      }

      if (!this.attributeViews.isEmpty()) {
         var1.add("attributeViews", this.attributeViews);
      }

      if (!this.attributeProviders.isEmpty()) {
         var1.add("attributeProviders", this.attributeProviders);
      }

      if (!this.defaultAttributeValues.isEmpty()) {
         var1.add("defaultAttributeValues", this.defaultAttributeValues);
      }

      var1.add("fileTimeSource", this.fileTimeSource);
      if (this.watchServiceConfig != WatchServiceConfiguration.DEFAULT) {
         var1.add("watchServiceConfig", this.watchServiceConfig);
      }

      return var1.toString();
   }

   public Configuration$Builder toBuilder() {
      return new Configuration$Builder(this, null);
   }
}
