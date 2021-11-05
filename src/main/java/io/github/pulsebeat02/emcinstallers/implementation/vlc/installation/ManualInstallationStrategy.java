package io.github.pulsebeat02.emcinstallers.implementation.vlc.installation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ManualInstallationStrategy implements InstallationStrategy {

  private final VLCInstaller installer;

  public ManualInstallationStrategy(final VLCInstaller installer) {
    this.installer = installer;
  }

  public void deleteFile(final Path dmg) throws IOException {
    Files.deleteIfExists(dmg);
  }

  public VLCInstaller getInstaller() {
    return this.installer;
  }
}
