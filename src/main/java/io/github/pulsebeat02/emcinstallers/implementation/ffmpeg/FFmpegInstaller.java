package io.github.pulsebeat02.emcinstallers.implementation.ffmpeg;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import io.github.pulsebeat02.emcinstallers.OS;
import io.github.pulsebeat02.emcinstallers.implementation.BaseInstaller;
import java.nio.file.Path;

public final class FFmpegInstaller extends BaseInstaller {

  public static final String VERSION;
  private static final Table<OS, Boolean, String> BITS_64;
  private static final Table<OS, Boolean, String> BITS_32;

  static {
    VERSION = "b4.4";
    BITS_64 = ImmutableTable.<OS, Boolean, String>builder()
        .put(OS.LINUX, true,
            String.format(
                "https://github.com/eugeneware/ffmpeg-static/releases/download/%s/linux-arm64",
                VERSION))
        .put(OS.LINUX, false,
            String.format(
                "https://github.com/eugeneware/ffmpeg-static/releases/download/%s/linux-x64",
                VERSION))
        .put(OS.MAC, true,
            String.format(
                "https://github.com/eugeneware/ffmpeg-static/releases/download/%s/darwin-arm64",
                VERSION))
        .put(OS.MAC, false,
            String.format(
                "https://github.com/eugeneware/ffmpeg-static/releases/download/%s/darwin-x64",
                VERSION))
        .put(OS.WINDOWS, false,
            String.format(
                "https://github.com/eugeneware/ffmpeg-static/releases/download/%s/win32-x64",
                VERSION))
        .put(OS.FREEBSD, false,
            String.format(
                "https://github.com/eugeneware/ffmpeg-static/releases/download/%s/freebsd-x64",
                VERSION))
        .build();
    BITS_32 = ImmutableTable.<OS, Boolean, String>builder()
        .put(OS.LINUX, true,
            String.format(
                "https://github.com/eugeneware/ffmpeg-static/releases/download/%s/linux-arm",
                VERSION))
        .put(OS.LINUX, false,
            String.format(
                "https://github.com/eugeneware/ffmpeg-static/releases/download/%s/linux-ia32",
                VERSION))
        .put(OS.WINDOWS, false,
            String.format(
                "https://github.com/eugeneware/ffmpeg-static/releases/download/%s/win32-ia32",
                VERSION))
        .build();
  }

  FFmpegInstaller(final Path folder) {
    super(folder, "ffmpeg", BITS_32, BITS_64);
  }

  FFmpegInstaller() {
    super("ffmpeg", BITS_32, BITS_64);
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
   * For Windows, it is C:/Program Files/static-emc Otherwise, it is [user home
   * directory]/static-emc
   *
   * @return new FFmpegInstaller
   */
  public static FFmpegInstaller create() {
    return new FFmpegInstaller();
  }

  @Override
  public boolean isSupported() {
    return true;
  }
}
