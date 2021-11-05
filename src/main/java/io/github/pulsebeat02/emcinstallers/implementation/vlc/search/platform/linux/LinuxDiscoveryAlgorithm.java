package io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.linux;

import com.google.common.collect.ImmutableSet;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.BaseDiscoveryAlgorithm;
import java.nio.file.Path;

public final class LinuxDiscoveryAlgorithm extends BaseDiscoveryAlgorithm {

  public LinuxDiscoveryAlgorithm() {
    super("so", ImmutableSet.of("/vlc/plugins", "/plugins"));
  }

  @Override
  public void onLibVlcFound(final Path path) {
  }
}
