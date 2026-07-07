package org.ywzj.doganticheat.jimfs;

final class Configuration$WindowsHolder {
   private static final Configuration WINDOWS = Configuration$Builder.access$100(Configuration.builder(PathType.windows()), "Windows")
      .setRoots("C:\\")
      .setWorkingDirectory("C:\\work")
      .setNameCanonicalNormalization(PathNormalization.CASE_FOLD_ASCII)
      .setPathEqualityUsesCanonicalForm(true)
      .setAttributeViews("basic")
      .setSupportedFeatures(Feature.LINKS, Feature.SYMBOLIC_LINKS, Feature.FILE_CHANNEL)
      .build();

   private Configuration$WindowsHolder() {
   }
}
