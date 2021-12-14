package io.github.pulsebeat02.ffmpeginstaller;

import io.github.pulsebeat02.emcinstallers.implementation.vlc.VLCInstallationKit;
import java.io.IOException;

public final class InstallerTest {

  public static void main(final String[] args) throws IOException {
    final VLCInstallationKit kit = VLCInstallationKit.create();
    System.out.println(kit.start().get());
  }
}
