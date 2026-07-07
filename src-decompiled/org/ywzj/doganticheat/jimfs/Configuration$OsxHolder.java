package org.ywzj.doganticheat.jimfs;

final class Configuration$OsxHolder {
   private static final Configuration OS_X = Configuration$Builder.access$100(Configuration.unix().toBuilder(), "OSX")
      .setNameDisplayNormalization(PathNormalization.NFC)
      .setNameCanonicalNormalization(PathNormalization.NFD, PathNormalization.CASE_FOLD_ASCII)
      .setSupportedFeatures(Feature.LINKS, Feature.SYMBOLIC_LINKS, Feature.FILE_CHANNEL)
      .build();

   private Configuration$OsxHolder() {
   }
}
