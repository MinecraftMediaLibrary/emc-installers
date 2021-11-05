package io.github.pulsebeat02.ffmpeginstaller;

import java.io.IOException;
import java.nio.file.Path;

public final class FFmpegInstallerTest {

  public static void main(final String[] args) throws IOException {
    final FFmpegInstaller installer = FFmpegInstaller.create();
    final Path path = installer.download(true);
    System.out.println(path);
  }

}
