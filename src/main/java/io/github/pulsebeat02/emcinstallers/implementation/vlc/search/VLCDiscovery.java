package io.github.pulsebeat02.emcinstallers.implementation.vlc.search;

import static io.github.pulsebeat02.emcinstallers.OS.LINUX;
import static io.github.pulsebeat02.emcinstallers.OS.MAC;
import static io.github.pulsebeat02.emcinstallers.OS.WINDOWS;
import static io.github.pulsebeat02.emcinstallers.OS.getOperatingSystem;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_new;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_release;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.sun.jna.NativeLibrary;
import com.sun.jna.StringArray;
import io.github.pulsebeat02.emcinstallers.OS;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.linux.LinuxDirectoryProvider;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.linux.LinuxDiscoveryAlgorithm;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.osx.OSXDirectoryProvider;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.osx.OSXDiscoveryAlgorithm;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.win.WinDirectoryProvider;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.platform.win.WinDiscoveryAlgorithm;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Stream;
import uk.co.caprica.vlcj.binding.LibC;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.support.version.LibVlcVersion;

public final class VLCDiscovery {

  private static final NativeDiscovery NATIVE_DISCOVERY;
  private static final Map<OS, Entry<DirectoryProvider, DiscoveryAlgorithm>> IMPLEMENTATION;

  static {
    NATIVE_DISCOVERY = new NativeDiscovery();
    IMPLEMENTATION = ImmutableMap.of(
        LINUX,
        new SimpleImmutableEntry<>(new LinuxDirectoryProvider(), new LinuxDiscoveryAlgorithm()),
        MAC, new SimpleImmutableEntry<>(new OSXDirectoryProvider(), new OSXDiscoveryAlgorithm()),
        WINDOWS, new SimpleImmutableEntry<>(new WinDirectoryProvider(), new WinDiscoveryAlgorithm())
    );
  }

  private final DiscoveryAlgorithm algorithm;
  private final Set<String> provider;
  private final String keyword;

  VLCDiscovery() {
    final Entry<DirectoryProvider, DiscoveryAlgorithm> entry = IMPLEMENTATION.get(
        getOperatingSystem());
    this.algorithm = entry.getValue();
    this.provider = Sets.newHashSet(entry.getKey().getSearchDirectories());
    this.keyword = this.getBinaryName();
  }

  VLCDiscovery(final DirectoryProvider provider, final DiscoveryAlgorithm algorithm) {
    this.algorithm = algorithm;
    this.provider = Sets.newHashSet(provider.getSearchDirectories());
    this.keyword = this.getBinaryName();
  }

  /**
   * Constructs a new VLCDiscovery with default configuration.
   *
   * @return a VLCDiscovery
   */
  public static VLCDiscovery create() {
    return new VLCDiscovery();
  }

  /**
   * Constructs a new VLCDiscovery with the specified configuration.
   *
   * @param provider  the directory provider
   * @param algorithm the algorithm
   * @return a VLCDiscovery
   */
  public static VLCDiscovery create(final DirectoryProvider provider,
      final DiscoveryAlgorithm algorithm) {
    return new VLCDiscovery(provider, algorithm);
  }

  private String getBinaryName() {
    return String.format("libvlc.%s", this.algorithm.getBinaryExtension());
  }

  /**
   * Attempts to discover a VLC path located on the computer or from the specified paths added. For
   * adding custom paths, see VLCDiscovery#addSearchPath.
   *
   * @return an optional containing the path. Empty if no path could be found
   * @see VLCDiscovery#addSearchPath(Path)
   */
  public Optional<Path> discover() {
    final Optional<Path> optional = this.basicDiscovery();
    if (optional.isPresent()) {
      return optional;
    }
    for (final String str : this.provider) {
      final Path path = Paths.get(str);
      try {
        if (CompletableFuture.supplyAsync(() -> this.findPluginsFolder(path)).get()
            && CompletableFuture.supplyAsync(() -> this.findLibVlc(path)).get()) {
          return this.loadNativeLibrary() ? Optional.of(path) : Optional.empty();
        }
      } catch (final InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }
    return Optional.empty();
  }

  private boolean findPluginsFolder(final Path directory) {
    try (final Stream<Path> stream = Files.walk(directory).parallel()) {
      final Optional<Path> plugins = stream.filter(this.getPluginPredicate()).findFirst();
      if (plugins.isPresent()) {
        final Path path = plugins.get();
        for (final String pattern : this.algorithm.getSearchPatterns()) {
          final Path extended = path.resolve(pattern);
          if (Files.exists(extended)) {
            this.setVLCPluginPath(extended);
            return true;
          }
        }
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  private boolean findLibVlc(final Path directory) {
    try (final Stream<Path> stream = Files.walk(directory).parallel()) {
      final Optional<Path> libvlc = stream.filter(this.getKeywordPredicate()).findFirst();
      if (libvlc.isPresent()) {
        final Path path = libvlc.get().getParent();
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), path.toString());
        this.algorithm.onLibVlcFound(path);
        return true;
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  private Predicate<? super Path> getKeywordPredicate() {
    return path -> Files.isRegularFile(path) && this.getPathName(path).equals(this.keyword);
  }

  private Predicate<? super Path> getPluginPredicate() {
    return path -> Files.isDirectory(path) && this.getPathName(path).equals("plugins");
  }

  private String getPathName(final Path path) {
    return path.getFileName().toString();
  }

  private void setVLCPluginPath(final Path path) {
    final String pluginPath = "VLC_PLUGIN_PATH";
    final String env = System.getenv(pluginPath);
    if (env == null || env.length() == 0) {
      if (getOperatingSystem() == WINDOWS) {
        LibC.INSTANCE._putenv(String.format("%s=%s", pluginPath, path));
      } else {
        LibC.INSTANCE.setenv(pluginPath, path.toString(), 1);
      }
    }
  }

  private boolean loadNativeLibrary() {
    try {
      final libvlc_instance_t instance =
          libvlc_new(0, new StringArray(new String[]{"--reset-plugins-cache"}));
      if (instance != null) {
        libvlc_release(instance);
        final LibVlcVersion version = new LibVlcVersion();
        if (version.isSupported()) {
          return true;
        }
      }
    } catch (final UnsatisfiedLinkError e) {
      e.printStackTrace();
    }
    return false;
  }

  private Optional<Path> basicDiscovery() {
    if (NATIVE_DISCOVERY.discover()) {
      return Optional.of(Paths.get(NATIVE_DISCOVERY.discoveredPath()));
    }
    return Optional.empty();
  }

  /**
   * Adds the search path to the find list.
   *
   * @param path the path to add
   */
  public void addSearchPath(final String path) {
    this.provider.add(path);
  }

  /**
   * Adds the search path to the find list.
   *
   * @param path the path to add
   */
  public void addSearchPath(final Path path) {
    this.addSearchPath(path.toString());
  }
}
