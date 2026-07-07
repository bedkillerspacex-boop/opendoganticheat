package org.ywzj.doganticheat.jimfs;

final class Configuration$UnixHolder {
   private static final Configuration UNIX = Configuration$Builder.access$100(Configuration.builder(PathType.unix()), "Unix")
      .setRoots("/")
      .setWorkingDirectory("/work")
      .setAttributeViews("basic")
      .setSupportedFeatures(Feature.LINKS, Feature.SYMBOLIC_LINKS, Feature.SECURE_DIRECTORY_STREAM, Feature.FILE_CHANNEL)
      .build();

   private Configuration$UnixHolder() {
   }
}
