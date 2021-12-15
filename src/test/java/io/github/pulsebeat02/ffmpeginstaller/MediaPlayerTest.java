package io.github.pulsebeat02.ffmpeginstaller;

import io.github.pulsebeat02.emcinstallers.implementation.vlc.VLCInstallationKit;
import java.io.IOException;
import java.nio.file.Paths;
import javax.swing.JFrame;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

public final class MediaPlayerTest {

  public static void main(String[] args) throws IOException {
    VLCInstallationKit.create(Paths.get("")).start();

    final VLCPlayer player = new VLCPlayer();
    player.play("/Users/bli24/Downloads/chaminade.mp4");
  }

  private static class VLCPlayer {

    private final JFrame frame;
    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public VLCPlayer() {
      mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
      frame = new JFrame();
      frame.setContentPane(mediaPlayerComponent);
      frame.setLocation(0, 0);
      frame.setSize(300, 400);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
    }

    public void play(final String mrl) {
      mediaPlayerComponent.mediaPlayer().media().play(mrl);
    }

    public JFrame getFrame() {
      return frame;
    }

    public EmbeddedMediaPlayerComponent getMediaPlayerComponent() {
      return mediaPlayerComponent;
    }
  }
}
