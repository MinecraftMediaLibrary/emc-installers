package io.github.pulsebeat02.emcinstallers;

import com.google.common.collect.ImmutableSet;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;

public enum OS {

  LINUX,
  FREEBSD,
  MAC,
  WINDOWS;

  private static final String OS_ARCH;
  private static final OS CURRENT;
  private static final boolean BITS_64;
  private static final boolean ARM;
  private static final Path EXECUTABLES;

  static {
    OS_ARCH = System.getProperty("os.arch").toLowerCase(Locale.ROOT);
    CURRENT = getOperatingSystem0();
    BITS_64 = is64Bits0();
    ARM = isArm0();
    EXECUTABLES = getPath0();
  }

  private static OS getOperatingSystem0() {
    final Set<String> prefix = ImmutableSet.of("nix", "nux", "aix");
    final String os = System.getProperty("os.name").toLowerCase();
    if (os.contains("win")) {
      return WINDOWS;
    } else if (prefix.stream().anyMatch(os::contains)) {
      return LINUX;
    } else if (os.contains("mac")) {
      return MAC;
    } else if (isFreeBsd(os)) {
      return FREEBSD;
    } else {
      throw new AssertionError("Unsupported Operating System!");
    }
  }

  private static boolean isFreeBsd(final String os) {
    return os.contains("freebsd");
  }

  private static boolean is64Bits0() {
    if (CURRENT == WINDOWS) {
      final String arch = System.getenv("PROCESSOR_ARCHITECTURE");
      final String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");
      return arch != null && arch.endsWith("64")
          || wow64Arch != null && wow64Arch.endsWith("64");
    } else {
      return OS_ARCH.contains("64");
    }
  }

  private static Path getPath0() {
    switch (CURRENT) {
      case LINUX:
      case MAC:
        return Paths.get(System.getProperty("user.home"), "static-emc");
      case WINDOWS:
        return Paths.get("C:/Program Files/static-emc/");
      default:
        throw new AssertionError("Unsupported Operating System!");
    }
  }

  private static boolean isArm0() {
    return OS_ARCH.contains("arm");
  }

  public static OS getOperatingSystem() {
    return CURRENT;
  }

  public static boolean isBits64() {
    return BITS_64;
  }

  public static boolean isArm() {
    return ARM;
  }

  public static Path getExecutablePath() {
    return EXECUTABLES;
  }
}
