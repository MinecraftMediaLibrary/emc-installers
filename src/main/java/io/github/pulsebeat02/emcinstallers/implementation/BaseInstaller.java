package io.github.pulsebeat02.emcinstallers.implementation;

import static io.github.pulsebeat02.emcinstallers.OS.WINDOWS;
import static io.github.pulsebeat02.emcinstallers.OS.getExecutablePath;
import static io.github.pulsebeat02.emcinstallers.OS.getOperatingSystem;
import static io.github.pulsebeat02.emcinstallers.OS.isArm;
import static io.github.pulsebeat02.emcinstallers.OS.isBits64;

import com.google.common.collect.Table;
import io.github.pulsebeat02.emcinstallers.OS;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class BaseInstaller implements Installer {

  private final Table<OS, Boolean, String> bits32;
  private final Table<OS, Boolean, String> bits64;
  private final String url;
  private Path path;

  public BaseInstaller(
      final Path folder,
      final String name,
      final Table<OS, Boolean, String> bits32,
      final Table<OS, Boolean, String> bits64) {
    this.path = folder.resolve(name);
    this.bits32 = bits32;
    this.bits64 = bits64;
    this.url = this.getDownloadUrl();
    if (this.url == null) {
      throw new AssertionError("");
    }
    this.createFiles();
  }

  public BaseInstaller(final String name, final Table<OS, Boolean, String> bits32,
      final Table<OS, Boolean, String> bits64) {
    this(getExecutablePath(), name, bits32, bits64);
  }

  private String getDownloadUrl() {
    final OS os = getOperatingSystem();
    final boolean arm = isArm();
    return isBits64() ? this.bits64.get(os, arm) : this.bits32.get(os, arm);
  }

  private String getFilename() {
    return this.url.substring(this.url.lastIndexOf('/') + 1);
  }

  private void createFiles() {
    if (Files.notExists(this.path)) {
      try {
        Files.createDirectories(this.path.getParent());
      } catch (final IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public Path download(final boolean chmod) throws IOException {
    if (Files.exists(this.path)) {
      return this.path;
    } else {
      Files.createFile(this.path);
    }
    try (final ReadableByteChannel readableByteChannel =
        Channels.newChannel(new URL(this.url).openStream());
        final FileChannel channel = new FileOutputStream(this.path.toFile()).getChannel()) {
      channel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
    }
    if (chmod) {
      this.changePermissions();
    }
    this.renameFile();
    return this.path;
  }

  private void renameFile() throws IOException {
    if (getOperatingSystem() == WINDOWS) {
      final Path newPath = this.path.resolveSibling(String.format("%s.exe", this.getFilename()));
      Files.move(this.path, newPath);
      this.path = newPath;
    }
  }

  private void changePermissions() throws IOException {
    if (getOperatingSystem() != WINDOWS) {
      final ProcessBuilder builder = new ProcessBuilder("chmod", "777", this.path.toString());
      try {
        builder.start().waitFor();
      } catch (final InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public String getUrl() {
    return this.url;
  }

  public Table<OS, Boolean, String> getBits32() {
    return this.bits32;
  }

  public Table<OS, Boolean, String> getBits64() {
    return this.bits64;
  }

  public Path getPath() {
    return this.path;
  }
}
