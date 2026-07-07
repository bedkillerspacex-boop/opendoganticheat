package org.ywzj.doganticheat.jimfs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.regex.Pattern;

@VisibleForTesting
final class PathMatchers$RegexPathMatcher implements PathMatcher {
   private final Pattern pattern;

   private PathMatchers$RegexPathMatcher(Pattern var1) {
      this.pattern = (Pattern)Preconditions.checkNotNull(var1);
   }

   @Override
   public boolean matches(Path var1) {
      return this.pattern.matcher(var1.toString()).matches();
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this).addValue(this.pattern).toString();
   }
}
