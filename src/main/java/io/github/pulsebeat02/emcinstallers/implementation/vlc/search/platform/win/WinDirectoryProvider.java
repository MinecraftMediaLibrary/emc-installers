/**
 * MIT License
 *
 * Copyright (c) 2021 Brandon Li
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.win;

import static com.sun.jna.platform.win32.Advapi32Util.registryGetStringValue;
import static com.sun.jna.platform.win32.WinReg.HKEY_LOCAL_MACHINE;
import static io.github.pulsebeat02.emcinstallers.OS.WINDOWS;
import static io.github.pulsebeat02.emcinstallers.OS.getOperatingSystem;

import com.google.common.collect.ImmutableSet;
import com.sun.jna.platform.win32.Win32Exception;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.BaseDirectoryProvider;
import java.util.Set;
import uk.co.caprica.vlcj.support.version.LibVlcVersion;
import uk.co.caprica.vlcj.support.version.Version;

public final class WinDirectoryProvider extends BaseDirectoryProvider {

  private static final Set<String> DIRS;

  static {
    DIRS = getPath();
  }

  private static Set<String> getPath() {
    try {
      final String key = "SOFTWARE\\VideoLAN\\VLC";
      Set<String> pre = null;
      try {
        final String directory =
            registryGetStringValue(HKEY_LOCAL_MACHINE, key, "InstallDir");
        final String ver =
            registryGetStringValue(HKEY_LOCAL_MACHINE, key, "Version");
        if (directory.isEmpty() || ver.isEmpty()) {
          pre = ImmutableSet.of();
        } else if (new Version(ver).atLeast(LibVlcVersion.requiredVersion)) {
          pre = ImmutableSet.of(directory);
        }
      } catch (final Win32Exception ignored) {
        pre = ImmutableSet.of();
      }
      return pre;
    } catch (final UnsatisfiedLinkError ignored) { // ignore because not Windows
    }
    return null;
  }

  public WinDirectoryProvider() {
    super(DIRS);
  }
}
