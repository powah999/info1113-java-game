package WizardTD;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
    private String soundFile;
    private boolean loop;
    private volatile boolean pause;
    private long lastPlaybackPosition; // Store the last playback position
    private boolean playing;
    private Thread audioThread;

    public SoundPlayer(String soundFile, boolean loop) {
        this.soundFile = soundFile;
        this.loop = loop;
        this.pause = false;
        this.playing = false;
        this.audioThread = null;
        this.lastPlaybackPosition = 0;
    }

    public void play() {
        if (playing) {
            return;
        }

        audioThread = new Thread(() -> {
            try {
                File audioFile = new File(soundFile);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        if (loop && !pause) {
                            clip.setMicrosecondPosition(0); // Restart the sound
                            clip.start();
                        }
                        playing = false;
                    }
                });

                clip.start();
                playing = true;

                while (playing) {
                    if (pause) {
                        clip.stop();
                        lastPlaybackPosition = clip.getMicrosecondPosition(); // Store the current position
                    } else {
                        if (clip.isRunning()) {
                            clip.start();
                        } else {
                            clip.setMicrosecondPosition(lastPlaybackPosition); // Resume from the stored position
                            clip.start();
                        }
                    }
                    // Add a small delay to avoid busy-waiting
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                clip.close();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        });

        audioThread.start();
    }

}
