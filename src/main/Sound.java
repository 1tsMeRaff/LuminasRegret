package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
    
    Clip clip;
    URL soundURL[] = new URL[30];
    FloatControl fc;
    int volumeScale = 3;
    float volume;
    
    public Sound() {
        soundURL[0] = getClass().getResource("/sound/Theme.wav");
        soundURL[1] = getClass().getResource("/sound/Coin.wav");
        soundURL[2] = getClass().getResource("/sound/Theme.wav");
        soundURL[3] = getClass().getResource("/sound/Theme.wav");
        soundURL[4] = getClass().getResource("/sound/Theme.wav");
        soundURL[5] = getClass().getResource("/sound/bonk.wav");
        soundURL[6] = getClass().getResource("/sound/receivedamage.wav");
        soundURL[7] = getClass().getResource("/sound/swingweapon.wav");
        soundURL[8] = getClass().getResource("/sound/levelup.wav");
        soundURL[9] = getClass().getResource("/sound/swipe.wav");
        soundURL[10] = getClass().getResource("/sound/windows.wav");
    }
    
    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            
            // Inisialisasi fc segera setelah clip dibuka
            fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            
            // Panggil checkVolume agar volume langsung diterapkan pada file yang baru dibuka
            checkVolume();
            
        } catch(Exception e) {
            // Penting: print error jika file tidak ditemukan atau clip gagal dibuka
            System.out.println("Error loading sound: " + e.getMessage());
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
        // Tambahkan pengaman IF agar tidak NullPointerException
        if (fc != null) {
            switch(volumeScale) {
                case 0: volume = -80f; break; // Mute
                case 1: volume = -20f; break;
                case 2: volume = -12f; break;
                case 3: volume = -5f; break;
                case 4: volume = 1f; break;
                case 5: volume = 6f; break; // Max
            }
            fc.setValue(volume);
        }
    }
}