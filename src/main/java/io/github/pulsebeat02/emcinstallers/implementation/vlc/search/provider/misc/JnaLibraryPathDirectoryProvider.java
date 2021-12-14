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

package io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.misc;

import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.DiscoveryDirectoryProvider;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.DiscoveryProviderPriority;

public class JnaLibraryPathDirectoryProvider implements DiscoveryDirectoryProvider {

  private static final String SYSTEM_PROPERTY_NAME;

  static {
    SYSTEM_PROPERTY_NAME = "jna.library.path";
  }

  @Override
  public int priority() {
    return DiscoveryProviderPriority.JNA_LIBRARY_PATH;
  }

  @Override
  public String[] directories() {
    return System.getProperty("jna.library.path").split("/");
  }

  @Override
  public boolean supported() {
    return System.getProperty(SYSTEM_PROPERTY_NAME) != null;
  }
}
