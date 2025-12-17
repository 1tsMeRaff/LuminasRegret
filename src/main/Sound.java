package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	
	Clip clip;
	URL soundURL[] = new URL[30];
	
	public Sound() {
		
		soundURL[0] = getClass().getResource("/sound/Theme.wav"); //lagu tema
		soundURL[1] = getClass().getResource("/sound/Coin.wav"); //sfx
		soundURL[2] = getClass().getResource("/sound/Theme.wav"); //sfx
		soundURL[3] = getClass().getResource("/sound/Theme.wav"); //sfx
		soundURL[4] = getClass().getResource("/sound/Theme.wav"); //sfx
		soundURL[5] = getClass().getResource("/sound/bonk.wav"); //sfx
		soundURL[6] = getClass().getResource("/sound/receivedamage.wav"); //sfx
		soundURL[7] = getClass().getResource("/sound/swingweapon.wav"); //sfx
		soundURL[8] = getClass().getResource("/sound/levelup.wav"); //sfx
	}
	
	public void setFile(int i) {
		
		try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch(Exception e) {
            e.printStackTrace();
        }
		
	}
	public void play() {
		
		clip.start();
	}
	public void loop() {
		
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	public void stop() {
		
		clip.stop();
	}
}
