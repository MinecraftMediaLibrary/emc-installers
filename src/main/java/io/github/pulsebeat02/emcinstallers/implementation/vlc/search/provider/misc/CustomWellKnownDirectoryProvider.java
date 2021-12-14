package io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.misc;

import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.DiscoveryDirectoryProvider;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.DiscoveryProviderPriority;

public class CustomWellKnownDirectoryProvider implements DiscoveryDirectoryProvider {

  private final String[] directories;

  public CustomWellKnownDirectoryProvider(final String... directories) {
    this.directories = directories;
  }

  @Override
  public int priority() {
    return DiscoveryProviderPriority.WELL_KNOWN_DIRECTORY;
  }

  @Override
  public String[] directories() {
    return this.directories;
  }

  @Override
  public boolean supported() {
    return true;
  }
}
