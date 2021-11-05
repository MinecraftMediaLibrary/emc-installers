package io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.osx;

import com.google.common.collect.ImmutableSet;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.BaseDirectoryProvider;
import java.util.Set;

public final class OSXDirectoryProvider extends BaseDirectoryProvider {

  private static final Set<String> DIRS;

  static {
    DIRS = ImmutableSet.of("/Applications/VLC.app/Contents/Frameworks",
        "/Applications/VLC.app/Contents/MacOS/lib");
  }

  public OSXDirectoryProvider() {
    super(DIRS);
  }
}
