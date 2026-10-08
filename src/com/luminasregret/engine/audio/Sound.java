package com.luminasregret.engine.audio;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound implements AutoCloseable {
    
    // Dedicated BGM streams and SFX asset mappings
    Clip clip;
    final URL musicURL[] = new URL[10];
    final URL seURL[] = new URL[30];
    URL soundURL[] = seURL;
    FloatControl fc;
    public int volumeScale = 3;
    public float volume;

    // Preloaded Audio Pool for SFX to eliminate runtime I/O and stream decoding freezes
    private final Clip[][] sePool = new Clip[30][];
    private final FloatControl[][] fcPool = new FloatControl[30][];
    private final int[] voiceIndex = new int[30];
    private final ExecutorService soundExecutor;
    private int currentFileIndex = -1;
    
    public Sound() {
        // 1. Curated BGM Tracks (Only played via gp.playMusic / playAreaMusic)
        musicURL[0] = loadSoundURL("/sound/overworld.wav"); // Overworld / Village
        musicURL[1] = loadSoundURL("/sound/dungeon.wav");   // Dungeon exploration
        musicURL[2] = loadSoundURL("/sound/dungeon.wav");   // Dungeon (ID 2 compatibility)
        musicURL[3] = loadSoundURL("/sound/boss.wav");      // Goblin King Boss fight
        musicURL[4] = loadSoundURL("/sound/title.wav");     // Main Title Screen & Menu

        // 2. Curated Sound Effects (Only played via gp.playSE)
        seURL[0] = loadSoundURL("/sound/swipe.wav");
        seURL[1] = loadSoundURL("/sound/Coin.wav");           // Coin pickup / prompt chime
        seURL[2] = loadSoundURL("/sound/levelup.wav");        // Quest complete / Fanfare / Heal reward!
        seURL[3] = loadSoundURL("/sound/enter.wav");          // Door enter / Confirm
        seURL[4] = loadSoundURL("/sound/openinven.wav");      // Open bag / Inventory
        seURL[5] = loadSoundURL("/sound/bonk.wav");           // Attack hit on monster
        seURL[6] = loadSoundURL("/sound/receivedamage.wav");   // Player received damage
        seURL[7] = loadSoundURL("/sound/swingweapon.wav");    // Weapon swing / slash
        seURL[8] = loadSoundURL("/sound/levelup.wav");        // Level up / Consume bread / Magic
        seURL[9] = loadSoundURL("/sound/swipe.wav");          // Menu cursor / dialogue
        seURL[10] = loadSoundURL("/sound/windows.wav");       // Subwindow open / prompt
        seURL[11] = loadSoundURL("/sound/stairs.wav");        // Dry tree chop / stairs
        seURL[12] = loadSoundURL("/sound/swipe.wav");         // Trade item buy/sell
        seURL[13] = loadSoundURL("/sound/stairs.wav");        // Map transition teleport

        // Backward compatibility
        this.soundURL = seURL;

        // Asynchronous daemon dispatcher to guarantee zero frame stalls on the game thread
        this.soundExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "Sound-Dispatcher");
                t.setDaemon(true);
                return t;
            }
        });
    }

    private URL loadSoundURL(String path) {
        URL url = getClass().getResource(path);
        if (url == null) {
            File file = new File("res" + path);
            if (file.exists()) {
                try {
                    url = file.toURI().toURL();
                } catch (MalformedURLException ignored) {
                }
            }
        }
        return url;
    }

    /**
     * Preloads and decodes SFX into memory so playback is instant (<0.01ms) during gameplay.
     */
    public void initSoundPool() {
        int[] sfxIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        for (int id : sfxIds) {
            loadSEClip(id);
        }
        checkVolume();
    }

    private void loadSEClip(int id) {
        if (id < 0 || id >= seURL.length || seURL[id] == null) {
            return;
        }
        // Dual-voice polyphony for high-frequency combat and event sounds
        int voiceCount = (id == 1 || id == 2 || id == 5 || id == 6 || id == 7 || id == 9) ? 2 : 1;
        sePool[id] = new Clip[voiceCount];
        fcPool[id] = new FloatControl[voiceCount];

        for (int v = 0; v < voiceCount; v++) {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(seURL[id]);
                Clip c = AudioSystem.getClip();
                c.open(ais);
                sePool[id][v] = c;
                if (c.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    fcPool[id][v] = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
                }
            } catch (Exception ignored) {
                // Graceful fallback if audio line is unavailable or in headless mode
            }
        }
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
        if (i < 0 || i >= musicURL.length || musicURL[i] == null) {
            return;
        }
        currentFileIndex = i;
        closeCurrentClip();
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(musicURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            }
            checkVolume();
        } catch(Exception e) {
            // AudioSystem line unavailable or file load issue
        }
    }
    
    public void play() {
        if (clip != null) {
            clip.start();
        } else if (currentFileIndex >= 0) {
            playSE(currentFileIndex);
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

    /**
     * Non-blocking fire-and-forget SFX playback with dual-voice polyphony.
     */
    public void playSE(final int i) {
        if (i < 0 || i >= seURL.length || seURL[i] == null) {
            return;
        }
        soundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                playSEDirect(i);
            }
        });
    }

    public void playSEDirect(int i) {
        if (i < 0 || i >= seURL.length || seURL[i] == null) {
            return;
        }
        Clip[] voices = sePool[i];
        if (voices == null || voices.length == 0) {
            loadSEClip(i);
            voices = sePool[i];
        }
        if (voices != null && voices.length > 0) {
            int idx = voiceIndex[i];
            voiceIndex[i] = (idx + 1) % voices.length;
            Clip c = voices[idx];
            if (c != null) {
                try {
                    if (c.isRunning()) {
                        c.stop();
                    }
                    c.setFramePosition(0);
                    c.start();
                } catch (Exception ignored) {}
            }
        }
    }
    
    public void checkVolume() {
        switch(volumeScale) {
            case 0: volume = -80f; break; // Mute
            case 1: volume = -20f; break;
            case 2: volume = -12f; break;
            case 3: volume = -5f; break;
            case 4: volume = 1f; break;
            case 5: volume = 6f; break; // Max
            default: volume = -5f; break;
        }
        if (fc != null) {
            try {
                fc.setValue(volume);
            } catch (Exception ignored) {}
        }
        for (int i = 0; i < fcPool.length; i++) {
            if (fcPool[i] != null) {
                for (int v = 0; v < fcPool[i].length; v++) {
                    if (fcPool[i][v] != null) {
                        try {
                            fcPool[i][v].setValue(volume);
                        } catch (Exception ignored) {}
                    }
                }
            }
        }
    }

    @Override
    public void close() {
        closeCurrentClip();
        for (int i = 0; i < sePool.length; i++) {
            if (sePool[i] != null) {
                for (int v = 0; v < sePool[i].length; v++) {
                    if (sePool[i][v] != null) {
                        try {
                            if (sePool[i][v].isRunning()) {
                                sePool[i][v].stop();
                            }
                            sePool[i][v].close();
                        } catch (Exception ignored) {}
                        sePool[i][v] = null;
                    }
                }
            }
        }
        if (!soundExecutor.isShutdown()) {
            soundExecutor.shutdown();
        }
    }
}