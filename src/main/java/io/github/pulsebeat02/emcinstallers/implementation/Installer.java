package io.github.pulsebeat02.emcinstallers.implementation;

import java.io.IOException;
import java.nio.file.Path;

public interface Installer {

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
  Path download(final boolean chmod) throws IOException;

  /**
   * Returns whether the operating system is *most likely* supported.
   *
   * @return whether the current operating system is supported or not
   */
  boolean isSupported();
}
