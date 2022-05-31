package io.github.pulsebeat02.emcinstallers.implementation.vlc.search;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_new;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_release;

import com.sun.jna.NativeLibrary;
import com.sun.jna.StringArray;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.strategy.BaseNativeDiscoveryStrategy;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.strategy.NativeDiscoveryStrategy;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.strategy.linux.LinuxNativeDiscoveryStrategy;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.strategy.osx.OsxNativeDiscoveryStrategy;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.search.strategy.win.WindowsNativeDiscoveryStrategy;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.support.version.LibVlcVersion;

/**
 * Enhanced native discovery class for VLC. This class should not be touched by the user, and only
 * be used for internal library purposes.
 */
public class EnhancedNativeDiscovery {

  private static final List<NativeDiscoveryStrategy> DEFAULT_STRATEGIES;
  private static boolean FOUND;

  static {
    DEFAULT_STRATEGIES =
        Arrays.asList(
            new LinuxNativeDiscoveryStrategy(),
            new OsxNativeDiscoveryStrategy(),
            new WindowsNativeDiscoveryStrategy());
  }

  private final List<NativeDiscoveryStrategy> discoveryStrategies;
  private NativeDiscoveryStrategy successfulStrategy;
  private String discoveredPath;

  public EnhancedNativeDiscovery(final NativeDiscoveryStrategy... discoveryStrategies) {
    this.discoveryStrategies =
        discoveryStrategies.length > 0 ? Arrays.asList(discoveryStrategies) : DEFAULT_STRATEGIES;
  }

  /**
   * Attempts to discover VLC on the host environment.
   *
   * @return whether discovery was successful or not
   */
  public final boolean discover() {
    if (FOUND) {
      return true;
    } else {
      for (final NativeDiscoveryStrategy discoveryStrategy : this.discoveryStrategies) {
        if (discoveryStrategy.supported()) {
          final Optional<String> optional = discoveryStrategy.discover();
          if (optional.isPresent()) {
            final String path = optional.get();
            return this.attemptPath(discoveryStrategy, path);
          }
        }
      }
      return false;
    }
  }

  private boolean attemptPath(final NativeDiscoveryStrategy discoveryStrategy, final String path) {
    this.addLibvlc(discoveryStrategy, path);
    this.tryPluginPath(path, discoveryStrategy);
    return this.attemptLibraryLoad(discoveryStrategy, path);
  }

  private boolean attemptLibraryLoad(
      final NativeDiscoveryStrategy discoveryStrategy, final String path) {
    if (this.tryLoadingLibrary()) {
      this.successfulStrategy = discoveryStrategy;
      this.discoveredPath = path;
      FOUND = true;
      return true;
    } else {
      return false;
    }
  }

  private void addLibvlc(final NativeDiscoveryStrategy discoveryStrategy, final String path) {
    if (discoveryStrategy.onFound(path)) {
      NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), path);
    }
  }

  private void tryPluginPath(final String path, final NativeDiscoveryStrategy discoveryStrategy) {
    final String env = System.getenv(BaseNativeDiscoveryStrategy.PLUGIN_ENV_NAME);
    if (env == null || env.length() == 0) {
      discoveryStrategy.onSetPluginPath(path);
    }
  }

  private boolean tryLoadingLibrary() {
    try {
      final libvlc_instance_t instance = libvlc_new(0, new StringArray(new String[0]));
      if (this.attemptNativeLibraryRelease(instance)) {
        return true;
      }
    } catch (final UnsatisfiedLinkError e) {
      System.err.println(e.getMessage());
    }
    return false;
  }

  private boolean attemptNativeLibraryRelease(final libvlc_instance_t instance) {
    if (instance != null) {
      libvlc_release(instance);
      final LibVlcVersion version = new LibVlcVersion();
      return version.isSupported();
    }
    return false;
  }

  public NativeDiscoveryStrategy getDiscoveryStrategy() {
    return this.successfulStrategy;
  }

  public String getDiscoveredPath() {
    return this.discoveredPath;
  }
}
