package io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.osx;

import com.google.common.collect.ImmutableSet;
import com.sun.jna.NativeLibrary;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.BaseDiscoveryAlgorithm;
import java.nio.file.Path;
import uk.co.caprica.vlcj.binding.RuntimeUtil;

public final class OSXDiscoveryAlgorithm extends BaseDiscoveryAlgorithm {

  public OSXDiscoveryAlgorithm() {
    super("dylib", ImmutableSet.of());
  }

  @Override
  public void onLibVlcFound(final Path path) {
    final String name = RuntimeUtil.getLibVlcCoreLibraryName();
    NativeLibrary.addSearchPath(name, path.toString());
    NativeLibrary.getInstance(name);
  }
}
