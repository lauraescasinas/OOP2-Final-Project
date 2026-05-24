package managers;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class SoundManager {
    public Clip menuMusic;
    public Clip clickSFX;

    public void loadAudio() {
        try {
            URL musicURL = getClass().getResource("/Music/MainMenu.wav");
            if (musicURL != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(musicURL);
                menuMusic = AudioSystem.getClip();
                menuMusic.open(ais);
            }

            URL sfxURL = getClass().getResource("/Music/Button.wav");
            if (sfxURL != null) {
                AudioInputStream ais2 = AudioSystem.getAudioInputStream(sfxURL);
                clickSFX = AudioSystem.getClip();
                clickSFX.open(ais2);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void playMenuMusic() {
        if (menuMusic != null) {
            menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
            menuMusic.start();
        }
    }

    public void stopMenuMusic() {
        if (menuMusic != null) {
            menuMusic.stop();
            menuMusic.close();
        }
    }

    public void playClickSFX() {
        if (clickSFX != null) {
            clickSFX.setFramePosition(0);
            clickSFX.start();
        }
    }
}