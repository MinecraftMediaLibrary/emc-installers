package io.github.pulsebeat02.emcinstallers.implementation.vlc.installation.platform.osx;

import io.github.pulsebeat02.emcinstallers.implementation.vlc.installation.VLCInstaller;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.installation.ManualInstallationStrategy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public final class OSXInstallationStrategy extends ManualInstallationStrategy {

  public OSXInstallationStrategy(
      final VLCInstaller installer) {
    super(installer);
  }

  @Override
  public Optional<Path> getInstalledPath() {
    final Path path = this.getInstaller().getPath().getParent().resolve("VLC.app");
    return Files.exists(path) ? Optional.of(path) : Optional.empty();
  }

  @Override
  public Path execute() throws IOException {
    final String vlc = "VLC.app";
    final Path dmg = this.getInstaller().getPath();
    final Path disk = Paths.get("/Volumes/VLC media player");
    final Path app = dmg.getParent().resolve(vlc);
    this.mountDisk(dmg);
    this.copyApplication(disk.resolve(vlc), app);
    this.changePermissions(app);
    this.unmountDisk(disk);
    this.deleteFile(dmg);
    return app;
  }

  private void unmountDisk(final Path disk) throws IOException {
    this.runNativeProcess("diskutil", "unmount", disk.toString());
  }

  private void changePermissions(final Path app) throws IOException {
    this.runNativeProcess("chmod", "-R", "755", app.toString());
  }

  private void mountDisk(final Path dmg) throws IOException {
    this.runNativeProcess("/usr/bin/hdiutil", "attach", dmg.toString());
  }

  private void copyApplication(final Path src, final Path app) throws IOException {
    this.runNativeProcess("rsync", "-a", String.format("%s/", src.toString()), app.toString());
  }

  private void runNativeProcess(final String... arguments) throws IOException {
    try {
      new ProcessBuilder(arguments).start().waitFor();
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }
}
