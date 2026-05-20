package main;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SoundManager {

    private Clip bgmClip;

    private AudioInputStream convertToSupportedFormat(AudioInputStream inputStr) {
        AudioFormat baseFormat = inputStr.getFormat();

        // Check if encoding or bit depth is problematic (e.g., 32-bit or non-PCM)
        if (baseFormat.getSampleSizeInBits() > 16 ||
                baseFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {

            AudioFormat targetFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16, // Downsample 32-bit to a widely supported 16-bit depth
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );

            if (AudioSystem.isConversionSupported(targetFormat, baseFormat)) {
                return AudioSystem.getAudioInputStream(targetFormat, inputStr);
            }
        }
        return inputStr;
    }

    public void playBGM(String resourcePath) {
        stopBGM();
        try {
            InputStream raw = getClass().getResourceAsStream(resourcePath);
            if (raw == null) {
                System.err.println("SoundManager: BGM resource not found – " + resourcePath);
                return;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(raw));

            ais = convertToSupportedFormat(ais);

            bgmClip = AudioSystem.getClip();
            bgmClip.open(ais);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            System.err.println("SoundManager: could not play BGM – " + resourcePath);
            e.printStackTrace();
        }
    }

    public void playBGMOnce(String resourcePath) {
        stopBGM();
        try {
            InputStream raw = getClass().getResourceAsStream(resourcePath);
            if (raw == null) {
                System.err.println("SoundManager: BGM resource not found – " + resourcePath);
                return;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(raw));

            ais = convertToSupportedFormat(ais);

            bgmClip = AudioSystem.getClip();
            bgmClip.open(ais);
            bgmClip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            System.err.println("SoundManager: could not play BGM – " + resourcePath);
            e.printStackTrace();
        }
    }

    public void stopBGM() {
        if (bgmClip != null) {
            if (bgmClip.isRunning()) bgmClip.stop();
            bgmClip.close();
            bgmClip = null;
        }
    }

    public void playSFX(String resourcePath) {
        try {
            InputStream raw = getClass().getResourceAsStream(resourcePath);
            if (raw == null) {
                System.err.println("SoundManager: SFX resource not found – " + resourcePath);
                return;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(raw));

            ais = convertToSupportedFormat(ais);

            Clip sfxClip = AudioSystem.getClip();
            sfxClip.open(ais);
            sfxClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    sfxClip.close();
                }
            });
            sfxClip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            System.err.println("SoundManager: could not play SFX – " + resourcePath);
            e.printStackTrace();
        }
    }
}