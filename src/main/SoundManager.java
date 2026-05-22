package main;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SoundManager {

    private Clip bgmClip;
    private Clip ambientClip;
    private final List<Clip> activeSfxClips = new ArrayList<>();

    private AudioInputStream convertToSupportedFormat(AudioInputStream inputStr) {
        AudioFormat baseFormat = inputStr.getFormat();
        if (baseFormat.getSampleSizeInBits() > 16 || baseFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            AudioFormat targetFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
                    baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
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
            if (raw == null) return;
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(raw));
            ais = convertToSupportedFormat(ais);
            bgmClip = AudioSystem.getClip();
            bgmClip.open(ais);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void playBGMOnce(String resourcePath) {
        stopBGM();
        try {
            InputStream raw = getClass().getResourceAsStream(resourcePath);
            if (raw == null) return;
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(raw));
            ais = convertToSupportedFormat(ais);
            bgmClip = AudioSystem.getClip();
            bgmClip.open(ais);
            bgmClip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void stopBGM() {
        if (bgmClip != null) {
            if (bgmClip.isRunning()) bgmClip.stop();
            bgmClip.close();
            bgmClip = null;
        }
    }

    // ── Street ambient channel (loops independently of BGM) ──────────────────
    public void playAmbient(String resourcePath) {
        stopAmbient();
        try {
            InputStream raw = getClass().getResourceAsStream(resourcePath);
            if (raw == null) return;
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(raw));
            ais = convertToSupportedFormat(ais);
            ambientClip = AudioSystem.getClip();
            ambientClip.open(ais);
            ambientClip.loop(Clip.LOOP_CONTINUOUSLY);
            ambientClip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void stopAmbient() {
        if (ambientClip != null) {
            if (ambientClip.isRunning()) ambientClip.stop();
            ambientClip.close();
            ambientClip = null;
        }
    }

    // ── One-shot SFX with tracking so resetAllAudio() can stop them ──────────
    public void playSFX(String resourcePath) {
        try {
            InputStream raw = getClass().getResourceAsStream(resourcePath);
            if (raw == null) return;
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(raw));
            ais = convertToSupportedFormat(ais);
            Clip sfxClip = AudioSystem.getClip();
            sfxClip.open(ais);
            synchronized (activeSfxClips) {
                activeSfxClips.add(sfxClip);
            }
            sfxClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    sfxClip.close();
                    synchronized (activeSfxClips) {
                        activeSfxClips.remove(sfxClip);
                    }
                }
            });
            sfxClip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ── Stops BGM, ambient, and every in-flight SFX clip ─────────────────────
    public void resetAllAudio() {
        stopBGM();
        stopAmbient();
        synchronized (activeSfxClips) {
            for (Clip clip : new ArrayList<>(activeSfxClips)) {
                if (clip.isRunning()) clip.stop();
                clip.close();
            }
            activeSfxClips.clear();
        }
    }
}
