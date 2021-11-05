package io.github.pulsebeat02.emcinstallers.implementation.vlc.installation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public interface InstallationStrategy {

  Optional<Path> getInstalledPath();

  Path execute() throws IOException;
}
