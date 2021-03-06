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

package io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.path;

import io.github.pulsebeat02.emcinstallers.OS;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.DiscoveryDirectoryProvider;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.DiscoveryProviderPriority;

public class SystemPathDirectoryProvider implements DiscoveryDirectoryProvider {

  @Override
  public int priority() {
    return DiscoveryProviderPriority.SYSTEM_PATH;
  }

  @Override
  public String[] directories() {

    final String path = System.getenv("PATH");
    if (path == null) {
      return new String[0];
    }

    return OS.getOperatingSystem() == OS.WINDOWS ? path.split(";") : path.split(":");
  }

  @Override
  public boolean supported() {
    return true;
  }
}
