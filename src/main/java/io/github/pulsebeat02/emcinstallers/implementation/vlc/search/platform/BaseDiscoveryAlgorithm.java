package io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform;

import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.DiscoveryAlgorithm;
import java.util.Set;

public abstract class BaseDiscoveryAlgorithm implements DiscoveryAlgorithm {

  private final String extension;
  private final Set<String> patterns;

  public BaseDiscoveryAlgorithm(final String extension, final Set<String> patterns) {
    this.extension = extension;
    this.patterns = patterns;
  }

  @Override
  public String getBinaryExtension() {
    return this.extension;
  }

  @Override
  public Set<String> getSearchPatterns() {
    return this.patterns;
  }
}
