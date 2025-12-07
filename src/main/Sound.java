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
	}
	
	public void setFile(int i) {
		
		try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch(Exception e) {
            // Error handling
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
