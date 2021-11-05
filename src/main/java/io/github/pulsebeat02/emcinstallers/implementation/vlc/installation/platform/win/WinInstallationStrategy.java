package io.github.pulsebeat02.emcinstallers.implementation.vlc.installation.platform.win;

import com.google.common.io.ByteStreams;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.installation.VLCInstaller;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.installation.ManualInstallationStrategy;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class WinInstallationStrategy extends ManualInstallationStrategy {

  public WinInstallationStrategy(
      final VLCInstaller installer) {
    super(installer);
  }

  @Override
  public Optional<Path> getInstalledPath() {
    final Path path = this.getInstaller().getPath().getParent().resolve("VLC");
    return Files.exists(path) ? Optional.of(path) : Optional.empty();
  }

  @Override
  public Path execute() throws IOException {
    final Path zip = this.getInstaller().getPath();
    final Path parent = zip.getParent();
    final Path temp = parent.resolve("temp-vlc");
    final Path path = parent.resolve("VLC");
    this.extractArchive(zip, temp);
    this.deleteFile(zip);
    this.moveFiles(temp, path);
    this.deleteFile(temp);
    return path;
  }

  private void extractArchive(final Path zip, final Path temp) throws IOException {
    try (final ZipFile zipFile = new ZipFile(zip.toFile())) {
      final Enumeration<? extends ZipEntry> entries = zipFile.entries();
      while (entries.hasMoreElements()) {
        final ZipEntry entry = entries.nextElement();
        final Path entryDestination = temp.resolve(entry.getName());
        if (entry.isDirectory()) {
          this.createDirectories(entryDestination);
        } else {
          this.createDirectories(entryDestination.getParent());
          try (final InputStream in = zipFile.getInputStream(entry);
              final OutputStream out = new FileOutputStream(entryDestination.toFile())) {
            ByteStreams.copy(in, out);
          }
        }
      }
    }
  }

  private void createDirectories(final Path path) throws IOException {
    if (Files.notExists(path)) {
      Files.createDirectories(path);
    }
  }

  private void moveFiles(final Path temp, final Path dest) throws IOException {
    Files.move(temp.resolve(String.format("vlc-%s", VLCInstaller.VERSION)), dest);
  }
}
