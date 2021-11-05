package io.github.pulsebeat02.ffmpeginstaller;

import io.github.pulsebeat02.emcinstallers.implementation.ffmpeg.FFmpegInstaller;
import io.github.pulsebeat02.emcinstallers.implementation.rtsp.RTSPInstaller;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.VLCInstaller;
import java.io.IOException;
import java.nio.file.Path;

public final class InstallerTest {

  public static void main(final String[] args) throws IOException {
    final FFmpegInstaller ffmpegInstaller = FFmpegInstaller.create();
    final RTSPInstaller rtspInstaller = RTSPInstaller.create();
    final VLCInstaller vlcInstaller = VLCInstaller.create();
    final Path ffmpeg = ffmpegInstaller.download(true);
    final Path rtsp = rtspInstaller.download(true);
    final Path vlc = vlcInstaller.download(true);
    System.out.println(ffmpeg);
    System.out.println(rtsp);
    System.out.println(vlc);
  }
}
