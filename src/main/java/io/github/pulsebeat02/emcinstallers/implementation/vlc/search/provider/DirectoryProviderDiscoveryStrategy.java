/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2019 Caprica Software Limited.
 */

package io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider;

import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.linux.LinuxWellKnownDirectoryProvider;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.misc.ConfigurationFileDiscoveryDirectoryProvider;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.misc.CustomWellKnownDirectoryProvider;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.misc.JnaLibraryPathDirectoryProvider;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.osx.OsxWellKnownDirectoryProvider;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.path.SystemPathDirectoryProvider;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.path.UserDirDirectoryProvider;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.win.WindowsInstallDirectoryProvider;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.strategy.BaseNativeDiscoveryStrategy;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class DirectoryProviderDiscoveryStrategy extends BaseNativeDiscoveryStrategy {

  private static final List<DiscoveryDirectoryProvider> DIRECTORY_PROVIDERS;

  static {
    DIRECTORY_PROVIDERS =
        new ArrayList<>(
            Arrays.asList(
                new ConfigurationFileDiscoveryDirectoryProvider(),
                new JnaLibraryPathDirectoryProvider(),
                new LinuxWellKnownDirectoryProvider(),
                new OsxWellKnownDirectoryProvider(),
                new SystemPathDirectoryProvider(),
                new UserDirDirectoryProvider(),
                new WindowsInstallDirectoryProvider()));
    DIRECTORY_PROVIDERS.sort((o1, o2) -> o2.priority() - o1.priority());
  }

  private final List<DiscoveryDirectoryProvider> directoryProviders;

  public DirectoryProviderDiscoveryStrategy(
      final String[] filenamePatterns, final String[] pluginPathFormats) {
    super(filenamePatterns, pluginPathFormats);
    this.directoryProviders = DIRECTORY_PROVIDERS;
  }

  @Override
  public final List<String> discoveryDirectories() {
    final List<String> directories = new ArrayList<>();
    for (final DiscoveryDirectoryProvider provider : this.getSupportedProviders()) {
      directories.addAll(Arrays.asList(provider.directories()));
    }
    return directories;
  }

  private List<DiscoveryDirectoryProvider> getSupportedProviders() {
    final List<DiscoveryDirectoryProvider> result = new ArrayList<>();
    for (final DiscoveryDirectoryProvider list : this.directoryProviders) {
      if (list.supported()) {
        result.add(list);
      }
    }
    return this.sort(result);
  }

  private List<DiscoveryDirectoryProvider> sort(final List<DiscoveryDirectoryProvider> providers) {
    return providers;
  }

  public static void addSearchDirectory(final Path path) {
    DIRECTORY_PROVIDERS.add(new CustomWellKnownDirectoryProvider(path.toString()));
    DIRECTORY_PROVIDERS.sort((o1, o2) -> o2.priority() - o1.priority());
  }
}
