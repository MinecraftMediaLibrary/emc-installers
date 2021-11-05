package io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform;

import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.DirectoryProvider;
import java.util.Set;

public abstract class BaseDirectoryProvider implements DirectoryProvider {

  private final Set<String> directories;

  public BaseDirectoryProvider(final Set<String> directories) {
    this.directories = directories;
  }

  @Override
  public Set<String> getSearchDirectories() {
    return this.directories;
  }
}
