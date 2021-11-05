package io.github.pulsebeat02.emcinstallers.implementation.vlc;

import static io.github.pulsebeat02.emcinstallers.OS.*;

import io.github.pulsebeat02.emcinstallers.implementation.vlc.installation.VLCInstaller;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.VLCDiscovery;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

public final class VLCInstallationKit {

  private final Path[] others;

  VLCInstallationKit(final Path... others) {
    this.others = others;
  }

  VLCInstallationKit() {
    this(getExecutablePath());
  }

  /**
   * Attempts to search for the VLC binary in common installation paths. If a library has been found
   * and loaded successfully, it will be available to be used by VLCJ. Otherwise, if a library could
   * not be found, it will download the respective binary for the user operating system and load
   * that libvlc that way.
   *
   * @throws IOException if an issue occurred during installation
   */
  public void start() throws IOException {
    this.installBinary(true, this.others);
  }

  private void installBinary(final boolean chmod, final Path... others) throws IOException {
    this.searchAndLoadBinary(VLCInstaller.create().download(chmod), others);
  }

  private void searchAndLoadBinary(final Path binary, final Path... others) {
    final VLCDiscovery discovery = VLCDiscovery.create();
    discovery.addSearchPath(binary);
    Arrays.stream(others).forEach(discovery::addSearchPath);
    discovery.discover();
  }

  /**
   * Constructs a new VLCInstallationKit with default parameters for downloading/loading libvlc into
   * the runtime.
   *
   * @return a new VLCInstallationKit
   */
  public static VLCInstallationKit create() {
    return new VLCInstallationKit();
  }

  /**
   * Constructs a new VLCInstallationKit with the specified path(s) for downloading/loading libvlc
   * into the runtime.
   *
   * @param others the desired paths to search
   * @return a new VLCInstallationKit
   */
  public static VLCInstallationKit create(final Path... others) {
    return new VLCInstallationKit(others);
  }
}
