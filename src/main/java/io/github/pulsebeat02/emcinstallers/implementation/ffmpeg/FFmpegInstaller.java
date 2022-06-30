/**
 * MIT License
 *
 * <p>Copyright (c) 2021 Brandon Li
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.pulsebeat02.emcinstallers.implementation.ffmpeg;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import io.github.pulsebeat02.emcinstallers.OS;
import io.github.pulsebeat02.emcinstallers.implementation.BaseInstaller;
import java.nio.file.Path;

/** FFmpeg installer class. */
public final class FFmpegInstaller extends BaseInstaller {

  public static final String VERSION;
  private static final Table<OS, Boolean, String> BITS_64;
  private static final Table<OS, Boolean, String> BITS_32;

  static {
    VERSION = "b5.0.1";
    BITS_64 =
        ImmutableTable.<OS, Boolean, String>builder()
            .put(
                OS.LINUX,
                true,
                String.format(
                    "https://github.com/eugeneware/ffmpeg-static/releases/download/%s/linux-arm64",
                    VERSION))
            .put(
                OS.LINUX,
                false,
                String.format(
                    "https://github.com/eugeneware/ffmpeg-static/releases/download/%s/linux-x64",
                    VERSION))
            .put(
                OS.MAC,
                true,
                String.format(
                    "https://github.com/eugeneware/ffmpeg-static/releases/download/%s/darwin-arm64",
                    VERSION))
            .put(
                OS.MAC,
                false,
                String.format(
                    "https://github.com/eugeneware/ffmpeg-static/releases/download/%s/darwin-x64",
                    VERSION))
            .put(
                OS.WINDOWS,
                false,
                String.format(
                    "https://github.com/eugeneware/ffmpeg-static/releases/download/%s/win32-x64",
                    VERSION))
            .put(
                OS.FREEBSD,
                false,
                String.format(
                    "https://github.com/eugeneware/ffmpeg-static/releases/download/%s/freebsd-x64",
                    VERSION))
            .build();
    BITS_32 =
        ImmutableTable.<OS, Boolean, String>builder()
            .put(
                OS.LINUX,
                true,
                String.format(
                    "https://github.com/eugeneware/ffmpeg-static/releases/download/%s/linux-arm",
                    VERSION))
            .put(
                OS.LINUX,
                false,
                String.format(
                    "https://github.com/eugeneware/ffmpeg-static/releases/download/%s/linux-ia32",
                    VERSION))
            .put(
                OS.WINDOWS,
                false,
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
   *
   * <p>For Windows, it is C:/Program Files/static-emc Otherwise, it is [user home
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
