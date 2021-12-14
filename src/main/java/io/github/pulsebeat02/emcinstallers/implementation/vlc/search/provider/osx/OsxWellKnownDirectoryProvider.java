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

package io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.osx;

import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.provider.WellKnownDirectoryProvider;
import uk.co.caprica.vlcj.binding.RuntimeUtil;

public class OsxWellKnownDirectoryProvider extends WellKnownDirectoryProvider {

  private static final String[] DIRECTORIES;

  static {
    final String contents = "/Applications/VLC.app/Contents";
    DIRECTORIES = new String[]{
        String.format("%s/Frameworks", contents),
        String.format("%s/MacOS/lib", contents)
    };
  }

  @Override
  public String[] directories() {
    return DIRECTORIES;
  }

  @Override
  public boolean supported() {
    return RuntimeUtil.isMac();
  }

}
