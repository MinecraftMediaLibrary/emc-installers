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

package io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.win;

import static com.sun.jna.platform.win32.WinReg.HKEY_LOCAL_MACHINE;

import com.sun.jna.platform.win32.Advapi32Util;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.DiscoveryDirectoryProvider;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.DiscoveryProviderPriority;
import uk.co.caprica.vlcj.binding.support.runtime.RuntimeUtil;

public class WindowsInstallDirectoryProvider implements DiscoveryDirectoryProvider {

  private static final String VLC_REGISTRY_KEY;
  private static final String VLC_INSTALL_DIR_KEY;

  static {
    VLC_REGISTRY_KEY = "SOFTWARE\\VideoLAN\\VLC";
    VLC_INSTALL_DIR_KEY = "InstallDir";
  }

  @Override
  public int priority() {
    return DiscoveryProviderPriority.INSTALL_DIR;
  }

  @Override
  public String[] directories() {
    final String installDir = this.getVlcInstallDir();
    return installDir != null ? new String[]{installDir} : new String[0];
  }

  @Override
  public boolean supported() {
    return RuntimeUtil.isWindows();
  }

  private String getVlcInstallDir() {
    try {
      return Advapi32Util.registryGetStringValue(HKEY_LOCAL_MACHINE, VLC_REGISTRY_KEY,
          VLC_INSTALL_DIR_KEY);
    } catch (final Exception e) {
      return null;
    }
  }
}
