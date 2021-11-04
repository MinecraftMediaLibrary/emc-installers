package io.github.pulsebeat02.ffmpeginstaller;

import static io.github.pulsebeat02.ffmpeginstaller.OS.LINUX;
import static io.github.pulsebeat02.ffmpeginstaller.OS.MAC;
import static io.github.pulsebeat02.ffmpeginstaller.OS.WINDOWS;
import static io.github.pulsebeat02.ffmpeginstaller.OS.getExecutablePath;
import static io.github.pulsebeat02.ffmpeginstaller.OS.getOperatingSystem;
import static io.github.pulsebeat02.ffmpeginstaller.OS.isArm;
import static io.github.pulsebeat02.ffmpeginstaller.OS.isBits64;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FFmpegInstaller {

  private static final Table<OS, Boolean, String> BITS_64;
  private static final Table<OS, Boolean, String> BITS_32;

  static {
    BITS_64 = ImmutableTable.<OS, Boolean, String>builder()
        .put(LINUX, true,
            "https://github.com/eugeneware/ffmpeg-static/releases/download/b4.4/linux-arm6")
        .put(LINUX, false,
            "https://github.com/eugeneware/ffmpeg-static/releases/download/b4.4/linux-x64")
        .put(MAC, true,
            "https://github.com/eugeneware/ffmpeg-static/releases/download/b4.4/darwin-arm64")
        .put(MAC, false,
            "https://github.com/eugeneware/ffmpeg-static/releases/download/b4.4/darwin-x64")
        .put(WINDOWS, false,
            "https://github.com/eugeneware/ffmpeg-static/releases/download/b4.4/win32-x64")
        .build();
    BITS_32 = ImmutableTable.<OS, Boolean, String>builder()
        .put(LINUX, true,
            "https://github.com/eugeneware/ffmpeg-static/releases/download/b4.4/linux-arm")
        .put(LINUX, false,
            "https://github.com/eugeneware/ffmpeg-static/releases/download/b4.4/linux-ia32")
        .put(WINDOWS, false,
            "https://github.com/eugeneware/ffmpeg-static/releases/download/b4.4/win32-ia32")
        .build();
  }

  private final String url;
  private Path path;

  FFmpegInstaller(final Path folder) {
    this.url = this.getDownloadUrl();
    this.path = folder.resolve("ffmpeg");
    this.createFiles();
  }

  FFmpegInstaller() {
    this(getExecutablePath());
  }

  private String getDownloadUrl() {
    final OS os = getOperatingSystem();
    final boolean arm = isArm();
    return isBits64() ? BITS_64.get(os, arm) : BITS_32.get(os, arm);
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

  public static FFmpegInstaller create(final Path executable) {
    return new FFmpegInstaller(executable);
  }

  public static FFmpegInstaller create() {
    return new FFmpegInstaller();
  }

  public Path download() throws IOException {
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
    this.changePermissions();
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
}
