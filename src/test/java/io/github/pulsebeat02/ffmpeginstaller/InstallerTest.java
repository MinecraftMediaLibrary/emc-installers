package io.github.pulsebeat02.ffmpeginstaller;

import io.github.pulsebeat02.emcinstallers.implementation.ffmpeg.FFmpegInstaller;
import io.github.pulsebeat02.emcinstallers.implementation.rtsp.RTSPInstaller;
import io.github.pulsebeat02.emcinstallers.implementation.vlc.VLCInstallationKit;
import java.io.IOException;

public final class InstallerTest {

  public static void main(final String[] args) throws IOException {
    final FFmpegInstaller ffmpeg = FFmpegInstaller.create();
    final RTSPInstaller rtsp = RTSPInstaller.create();
    final VLCInstallationKit vlc = VLCInstallationKit.create();
    System.out.println(ffmpeg.download(true));
    System.out.println(rtsp.download(true));
    System.out.println(vlc.start().get());
  }
}
