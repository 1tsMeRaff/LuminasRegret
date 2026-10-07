package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound implements AutoCloseable {
    
    Clip clip;
    URL soundURL[] = new URL[30];
    FloatControl fc;
    int volumeScale = 3;
    float volume;
    
    public Sound() {
        soundURL[0] = loadSoundURL("/sound/overworld.wav");
        soundURL[1] = loadSoundURL("/sound/Coin.wav");
        soundURL[2] = loadSoundURL("/sound/dungeon.wav");
        soundURL[3] = loadSoundURL("/sound/boss.wav");
        soundURL[4] = loadSoundURL("/sound/title.wav");
        soundURL[5] = loadSoundURL("/sound/bonk.wav");
        soundURL[6] = loadSoundURL("/sound/receivedamage.wav");
        soundURL[7] = loadSoundURL("/sound/swingweapon.wav");
        soundURL[8] = loadSoundURL("/sound/levelup.wav");
        soundURL[9] = loadSoundURL("/sound/swipe.wav");
        soundURL[10] = loadSoundURL("/sound/windows.wav");
        soundURL[11] = loadSoundURL("/sound/stairs.wav");
        soundURL[12] = loadSoundURL("/sound/swipe.wav");
        soundURL[13] = loadSoundURL("/sound/swipe.wav");
        soundURL[14] = loadSoundURL("/sound/swipe.wav");
    }

    private URL loadSoundURL(String path) {
        URL url = getClass().getResource(path);
        if (url == null) {
            java.io.File file = new java.io.File("res" + path);
            if (file.exists()) {
                try {
                    url = file.toURI().toURL();
                } catch (java.net.MalformedURLException ignored) {
                }
            }
        }
        return url;
    }
    
    public void closeCurrentClip() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.close();
            clip = null;
            fc = null;
        }
    }

    public void setFile(int i) {
        if (i < 0 || i >= soundURL.length || soundURL[i] == null) {
            return;
        }
        closeCurrentClip();
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i])) {
            clip = AudioSystem.getClip();
            clip.open(ais);
            
            // Inisialisasi fc segera setelah clip dibuka
            fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            
            // Panggil checkVolume agar volume langsung diterapkan pada file yang baru dibuka
            checkVolume();
            
        } catch(Exception e) {
            System.err.println("Error loading sound ID " + i + ": " + e.getMessage());
        }
    }
    
    public void play() {
        if (clip != null) {
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
    
    public void checkVolume() {
        if (fc != null) {
            switch(volumeScale) {
                case 0: volume = -80f; break; // Mute
                case 1: volume = -20f; break;
                case 2: volume = -12f; break;
                case 3: volume = -5f; break;
                case 4: volume = 1f; break;
                case 5: volume = 6f; break; // Max
                default: volume = -5f; break;
            }
            fc.setValue(volume);
        }
    }

    @Override
    public void close() {
        closeCurrentClip();
    }
}