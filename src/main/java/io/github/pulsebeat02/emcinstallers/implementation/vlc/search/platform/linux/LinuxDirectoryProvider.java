package io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.linux;

import com.google.common.collect.ImmutableSet;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.BaseDirectoryProvider;
import java.util.Set;

public final class LinuxDirectoryProvider extends BaseDirectoryProvider {

  private static final Set<String> DIRS;

  static {
    DIRS = ImmutableSet.of(
        "/usr/lib/x86_64-linux-gnu",
        "/usr/lib64",
        "/usr/local/lib64",
        "/usr/lib/i386-linux-gnu",
        "/usr/lib",
        "/usr/local/lib");
  }

  public LinuxDirectoryProvider() {
    super(DIRS);
  }
}
