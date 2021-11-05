package io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.win;

import com.google.common.collect.ImmutableSet;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.BaseDiscoveryAlgorithm;
import java.nio.file.Path;

public final class WinDiscoveryAlgorithm extends BaseDiscoveryAlgorithm {

  public WinDiscoveryAlgorithm() {
    super("dll", ImmutableSet.of());
  }

  @Override
  public void onLibVlcFound(final Path path) {
  }
}
