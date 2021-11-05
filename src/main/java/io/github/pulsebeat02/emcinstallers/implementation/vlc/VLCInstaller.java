package io.github.pulsebeat02.emcinstallers.implementation.vlc;

import static io.github.pulsebeat02.emcinstallers.OS.MAC;
import static io.github.pulsebeat02.emcinstallers.OS.WINDOWS;
import static io.github.pulsebeat02.emcinstallers.OS.getOperatingSystem;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.common.io.ByteStreams;
import io.github.pulsebeat02.emcinstallers.OS;
import io.github.pulsebeat02.emcinstallers.implementation.BaseInstaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class VLCInstaller extends BaseInstaller {

  private static final String VERSION;
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

  public VLCInstaller(final Path folder) {
    super(folder, "vlc", BITS_32, BITS_64);
  }

  public VLCInstaller() {
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
    final Path path = this.getPath();
    final Path executable = getOperatingSystem() == MAC ? path.resolve("VLC.app")
        : path.resolve("VLC");
    if (Files.exists(executable)) {
      return executable;
    }
    this.setUrl("https://mirror.csclub.uwaterloo.ca/vlc/vlc/3.0.16/win64/vlc-3.0.16-win64.zip");
    super.download(chmod);
    return this.handleVLC();
  }

  private Path handleVLC() throws IOException {
    switch (getOperatingSystem()) {
      case MAC:
        return this.handleMac();
      case WINDOWS:
        return this.handleWindows();
      default:
        throw new AssertionError("Operating System not Supported!");
    }
  }

  private Path handleWindows() throws IOException {
    final Path zip = this.getPath();
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
    Files.move(temp.resolve(String.format("vlc-%s", VERSION)), dest);
  }

  private Path handleMac() throws IOException {
    final String vlc = "VLC.app";
    final Path dmg = this.getPath();
    final Path disk = Paths.get("/Volumes/VLC media player");
    final Path app = dmg.getParent().resolve(vlc);
    this.mountDisk(dmg);
    this.copyApplication(disk.resolve(vlc), app);
    this.changePermissions(app);
    this.unmountDisk(disk);
    this.deleteFile(dmg);
    return app;
  }

  private void renameFile(final Path file, final String name) throws IOException {
    Files.move(file, file.resolveSibling(name));
  }

  private void deleteFile(final Path dmg) throws IOException {
    Files.deleteIfExists(dmg);
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

  @Override
  public boolean isSupported() {
    final OS os = getOperatingSystem();
    return os == WINDOWS || os == MAC;
  }
}
