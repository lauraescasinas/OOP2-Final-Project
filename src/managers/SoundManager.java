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

    public void playDunSFX() {
        try {
            URL dunURL = getClass().getResource("/Music/Dun.wav");
            if (dunURL != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(dunURL);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.addLineListener(event -> {
                    if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
                clip.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void playWalkSFX() {
        try {
            URL walkURL = getClass().getResource("/Music/Walk.wav");
            if (walkURL != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(walkURL);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.addLineListener(event -> {
                    if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
                clip.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void playGlassEyeSFX() {
        try {
            URL glassEyeURL = getClass().getResource("/Music/GlassEye.wav");
            if (glassEyeURL != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(glassEyeURL);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.addLineListener(event -> {
                    if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
                clip.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void playRoseSFX() {
        try {
            URL roseURL = getClass().getResource("/Music/Rose.wav");
            System.out.println("Rose URL: " + roseURL); // ADD THIS
            if (roseURL != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(roseURL);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.addLineListener(event -> {
                    if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
                clip.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void playStatueRotationSFX() {
        try {
            URL statueURL = getClass().getResource("/Music/StatueRotation.wav");
            if (statueURL != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(statueURL);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.addLineListener(event -> {
                    if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
                clip.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void playErrorSFX() {
        try {
            URL errorURL = getClass().getResource("/Music/WrongPassword.wav");
            if (errorURL != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(errorURL);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.addLineListener(event -> {
                    if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
                clip.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void playCorrectPasswordSFX() {
        try {
            URL correctURL = getClass().getResource("/Music/CorrectPassword.wav");
            if (correctURL != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(correctURL);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.addLineListener(event -> {
                    if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
                clip.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}