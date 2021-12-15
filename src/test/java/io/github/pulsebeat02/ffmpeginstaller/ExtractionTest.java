package io.github.pulsebeat02.ffmpeginstaller;

import com.google.common.io.ByteStreams;
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

public final class ExtractionTest {

  public static void main(String[] args) throws IOException {
    final String downloads = "/Users/bli24/Downloads/";
    extractArchive(Paths.get(downloads, "file.zip"), Paths.get(downloads, "extracted"));
  }

  private static void extractArchive(final Path zip, final Path temp) throws IOException {
    try (final ZipFile zipFile = new ZipFile(zip.toFile())) {
      final Enumeration<? extends ZipEntry> entries = zipFile.entries();
      while (entries.hasMoreElements()) {
        final ZipEntry entry = entries.nextElement();
        final Path entryDestination = temp.resolve(entry.getName());
        if (entry.isDirectory()) {
          createDirectories(entryDestination);
        } else {
          createDirectories(entryDestination.getParent());
          try (final InputStream in = zipFile.getInputStream(entry);
              final OutputStream out = new FileOutputStream(entryDestination.toFile())) {
            ByteStreams.copy(in, out);
          }
        }
      }
    }
  }

  private static void createDirectories(final Path path) throws IOException {
    if (Files.notExists(path)) {
      Files.createDirectories(path);
    }
  }
}
