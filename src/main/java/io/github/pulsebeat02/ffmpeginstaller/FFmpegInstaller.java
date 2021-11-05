package io.github.pulsebeat02.ffmpeginstaller;

import static io.github.pulsebeat02.ffmpeginstaller.OS.FREEBSD;
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
        .put(FREEBSD, false,
            "https://github.com/eugeneware/ffmpeg-static/releases/download/b4.4/freebsd-x64")
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

  /**
   * Constructs a new FFmpegInstaller with the specified directory for the executable.
   *
   * @param executable directory
   * @return new FFmpegInstaller
   */
  public static FFmpegInstaller create(final Path executable) {
    return new FFmpegInstaller(executable);
  }

  /**
   * Constructs a new FFmpegInstaller with the default directory for the executable.
   * <p>
   * For Windows, it is C:/Program Files/static-ffmpeg Otherwise, it is [user home
   * directory]/static-ffmpeg
   *
   * @return
   */
  public static FFmpegInstaller create() {
    return new FFmpegInstaller();
  }

  /**
   * Downloads the binary into the specified directory with the file name "ffmpeg". If the file
   * already exists there, it will return the path of that file assuming that FFmpeg has already
   * been installed. Otherwise, it downloads a new file. If chmod is set to true, it will change the
   * file permissions to 777 you can use {@ProcessBuilder} or {@Process} to use the binary. It
   * returns the path of the downloaded executable
   *
   * @param chmod whether chmod 777 should be applied (if not windows)
   * @return the path of the download executable
   * @throws IOException if an issue occurred during file creation, downloading, or renaming.
   */
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
}
