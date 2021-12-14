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
 * Copyright 2009-2020 Caprica Software Limited.
 */

package io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.misc;

import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.DiscoveryDirectoryProvider;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.DiscoveryProviderPriority;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigurationFileDiscoveryDirectoryProvider implements DiscoveryDirectoryProvider {

  private static final String CONFIG_DIR;
  private static final String CONFIG_FILE_NAME;
  private static final String PROPERTY_NAME;

  static {
    CONFIG_DIR = String.format("%s/.config/vlcj", System.getProperty("user.home"));
    CONFIG_FILE_NAME = "vlcj.config";
    PROPERTY_NAME = "nativeDirectory";
  }

  @Override
  public int priority() {
    return DiscoveryProviderPriority.CONFIG_FILE;
  }

  @Override
  public String[] directories() {
    final Path file = Paths.get(CONFIG_DIR, CONFIG_FILE_NAME);
    final Properties properties = new Properties();
    try (final Reader reader = Files.newBufferedReader(file)) {
      properties.load(reader);
      final String directory = properties.getProperty(PROPERTY_NAME);
      if (directory != null) {
        return new String[]{directory};
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return new String[0];
  }

  @Override
  public boolean supported() {
    final Path file = Paths.get(CONFIG_DIR, CONFIG_FILE_NAME);
    return Files.exists(file) && Files.isRegularFile(file) && Files.isReadable(file);
  }
}
