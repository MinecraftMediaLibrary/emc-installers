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

package io.github.pulsebeat02.emcinstallers.implementation.vlc.search.strategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public abstract class BaseNativeDiscoveryStrategy implements NativeDiscoveryStrategy {

  public static final String PLUGIN_ENV_NAME = "VLC_PLUGIN_PATH";

  private final Pattern[] patternsToMatch;
  private final String[] pluginPathFormats;

  public BaseNativeDiscoveryStrategy(final String[] filenamePatterns,
      final String[] pluginPathFormats) {
    this.patternsToMatch = new Pattern[filenamePatterns.length];
    this.pluginPathFormats = pluginPathFormats;
    this.compilePatterns(filenamePatterns);
  }

  private void compilePatterns(final String[] filenamePatterns) {
    for (int i = 0; i < filenamePatterns.length; i++) {
      this.patternsToMatch[i] = Pattern.compile(filenamePatterns[i]);
    }
  }

  @Override
  public final Optional<String> discover() {
    return this.discoveryDirectories().stream().parallel().filter(this::find).findAny();
  }

  protected abstract List<String> discoveryDirectories();

  private boolean find(final String directoryName) {

    final Path dir = Paths.get(directoryName);
    if (Files.notExists(dir)) {
      return false;
    }

    final Set<String> matches = new HashSet<>(this.patternsToMatch.length);
    try (final Stream<Path> stream = Files.walk(dir, 1).parallel()) {
      return stream.anyMatch(this.getMatchingPatternFile(matches));
    } catch (final IOException e) {
      e.printStackTrace();
    }

    return false;
  }

  private Predicate<Path> getMatchingPatternFile(final Set<String> matches) {
    return file -> {
      for (final Pattern pattern : this.patternsToMatch) {
        if (this.validatePattern(matches, file, pattern)) {
          return true;
        }
      }
      return false;
    };
  }

  private boolean validatePattern(final Set<String> matches, final Path file,
      final Pattern pattern) {
    final Matcher matcher = pattern.matcher(file.getFileName().toString());
    if (matcher.matches()) {
      matches.add(pattern.pattern());
      return matches.size() == this.patternsToMatch.length;
    }
    return false;
  }

  @Override
  public boolean onFound(final String path) {
    return true;
  }

  @Override
  public final boolean onSetPluginPath(final String path) {
    for (final String pathFormat : this.pluginPathFormats) {
      final String pluginPath = String.format(pathFormat, path);
      if (Files.exists(Paths.get(pluginPath))) {
        return this.setPluginPath(pluginPath);
      }
    }
    return false;
  }

  protected abstract boolean setPluginPath(String pluginPath);

}
