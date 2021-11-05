package io.github.pulsebeat02.ffmpeginstaller;

import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.VLCDiscovery;

public final class InstallerTest {

  public static void main(final String[] args) {
    final VLCDiscovery discovery = VLCDiscovery.create();
    discovery.discover();
  }

}
