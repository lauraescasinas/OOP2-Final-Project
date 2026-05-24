package managers;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class SoundManager {
    public Clip menuMusic;
    public Clip clickSFX;
    public Clip introMusic;
    public Clip roomMusic;
    public Clip outdoorMusic;
    public Clip museumMusic;
    public Clip workshopMusic;
    public Clip gardenMusic;

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
            menuMusic = null; // must null this out so loadAudio() can re-open it later
        }
    }

    public void playIntroMusic() {
        try {
            URL introURL = getClass().getResource("/Music/Intro.wav");
            if (introURL != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(introURL);
                introMusic = AudioSystem.getClip();
                introMusic.open(ais);
                introMusic.start(); // plays once, no loop
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stopIntroMusic() {
        if (introMusic != null) {
            introMusic.stop();
            introMusic.close();
            introMusic = null;
        }
    }

    public void playRoomMusic() {
        try {
            URL roomURL = getClass().getResource("/Music/Room.wav");
            if (roomURL != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(roomURL);
                roomMusic = AudioSystem.getClip();
                roomMusic.open(ais);
                roomMusic.loop(Clip.LOOP_CONTINUOUSLY);
                roomMusic.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stopRoomMusic() {
        if (roomMusic != null) {
            roomMusic.stop();
            roomMusic.close();
            roomMusic = null;
        }
    }

    public void playMuseumMusic() {
        try {
            URL url = getClass().getResource("/Music/Museum.wav");
            if (url != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                museumMusic = AudioSystem.getClip();
                museumMusic.open(ais);
                museumMusic.loop(Clip.LOOP_CONTINUOUSLY);
                museumMusic.start();
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public void stopMuseumMusic() {
        if (museumMusic != null) {
            museumMusic.stop();
            museumMusic.close();
            museumMusic = null;
        }
    }

    public void playWorkshopMusic() {
        try {
            URL url = getClass().getResource("/Music/Workshop.wav");
            if (url != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                workshopMusic = AudioSystem.getClip();
                workshopMusic.open(ais);
                workshopMusic.loop(Clip.LOOP_CONTINUOUSLY);
                workshopMusic.start();
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public void stopWorkshopMusic() {
        if (workshopMusic != null) {
            workshopMusic.stop();
            workshopMusic.close();
            workshopMusic = null;
        }
    }

    public void playGardenMusic() {
        try {
            URL url = getClass().getResource("/Music/Garden.wav");
            if (url != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                gardenMusic = AudioSystem.getClip();
                gardenMusic.open(ais);
                gardenMusic.loop(Clip.LOOP_CONTINUOUSLY);
                gardenMusic.start();
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public void stopGardenMusic() {
        if (gardenMusic != null) {
            gardenMusic.stop();
            gardenMusic.close();
            gardenMusic = null;
        }
    }

    public void playOutdoorMusic() {
        try {
            URL outdoorURL = getClass().getResource("/Music/Outdoor.wav");
            if (outdoorURL != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(outdoorURL);
                outdoorMusic = AudioSystem.getClip();
                outdoorMusic.open(ais);
                outdoorMusic.loop(Clip.LOOP_CONTINUOUSLY);
                outdoorMusic.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stopOutdoorMusic() {
        if (outdoorMusic != null) {
            outdoorMusic.stop();
            outdoorMusic.close();
            outdoorMusic = null;
        }
    }

    public void playClickSFX() {
        if (clickSFX != null) {
            clickSFX.setFramePosition(0);
            clickSFX.start();
        }
    }

    public void playDemonAppearanceSFX() {
        try {
            URL demonURL = getClass().getResource("/Music/DemonAppearance.wav");
            if (demonURL != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(demonURL);
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
            System.out.println("Rose URL: " + roseURL);
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