package io.github.pulsebeat02.emcinstallers.implementation.vlc.installation;

import static io.github.pulsebeat02.emcinstallers.OS.MAC;
import static io.github.pulsebeat02.emcinstallers.OS.WINDOWS;
import static io.github.pulsebeat02.emcinstallers.OS.getOperatingSystem;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import io.github.pulsebeat02.emcinstallers.OS;
import io.github.pulsebeat02.emcinstallers.implementation.BaseInstaller;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.installation.platform.osx.OSXInstallationStrategy;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.installation.platform.win.WinInstallationStrategy;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public final class VLCInstaller extends BaseInstaller {

  public static final String VERSION;
  private static final Table<OS, Boolean, String> BITS_64;
  private static final Table<OS, Boolean, String> BITS_32;

  static {
    VERSION = "3.0.16";
    BITS_64 = ImmutableTable.<OS, Boolean, String>builder()
        .put(MAC, true,
            String.format("https://mirror.csclub.uwaterloo.ca/vlc/vlc/%s/macosx/vlc-%s-arm64.dmg",
                VERSION, VERSION))
        .put(MAC, false,
            String.format(
                "https://mirror.csclub.uwaterloo.ca/vlc/vlc/%s/macosx/vlc-%s-universal.dmg",
                VERSION, VERSION))
        .put(WINDOWS, false,
            String.format("https://mirror.csclub.uwaterloo.ca/vlc/vlc/%s/win64/vlc-%s-win64.zip",
                VERSION, VERSION))
        .build();
    BITS_32 = ImmutableTable.<OS, Boolean, String>builder()
        .put(WINDOWS, false,
            String.format("https://mirror.csclub.uwaterloo.ca/vlc/vlc/%s/win32/vlc-%s-win32.zip",
                VERSION, VERSION))
        .build();
  }

  VLCInstaller(final Path folder) {
    super(folder, "vlc", BITS_32, BITS_64);
  }

  VLCInstaller() {
    super("vlc", BITS_32, BITS_64);
  }

  /**
   * Constructs a new VLCInstaller with the specified directory for the executable.
   *
   * @param executable directory
   * @return new VLCInstaller
   */
  public static VLCInstaller create(final Path executable) {
    return new VLCInstaller(executable);
  }

  /**
   * Constructs a new VLCInstaller with the default directory for the executable.
   * <p>
   * For Windows, it is C:/Program Files/static-emc Otherwise, it is [user home
   * directory]/static-emc
   *
   * @return new VLCInstaller
   */
  public static VLCInstaller create() {
    return new VLCInstaller();
  }

  @Override
  public Path download(final boolean chmod) throws IOException {

    final InstallationStrategy strategy = this.getStrategy();
    final Optional<Path> optional = strategy.getInstalledPath();
    if (optional.isPresent()) {
      return optional.get();
    }

    super.download(chmod);

    return strategy.execute();
  }

  private InstallationStrategy getStrategy() {
    switch (getOperatingSystem()) {
      case MAC:
        return new OSXInstallationStrategy(this);
      case WINDOWS:
        return new WinInstallationStrategy(this);
      default:
        throw new AssertionError("Operating System not Supported!");
    }
  }

  @Override
  public boolean isSupported() {
    final OS os = getOperatingSystem();
    return os == WINDOWS || os == MAC;
  }
}