package io.github.pulsebeat02.emcinstallers.implementation.vlc.search;

import java.nio.file.Path;
import java.util.Set;

public interface DiscoveryAlgorithm {

  String getBinaryExtension();

  Set<String> getSearchPatterns();

  void onLibVlcFound(final Path path);
}
