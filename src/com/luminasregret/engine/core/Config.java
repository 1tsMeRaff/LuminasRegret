package com.luminasregret.engine.core;

import com.luminasregret.engine.core.GamePanel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Config {
	
	GamePanel gp;
	private static final String CONFIG_FILE = "config.txt";

	public Config(GamePanel gp) {
	    this.gp = gp;
	}

	public void saveConfig() {
	    try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
	            new FileOutputStream(CONFIG_FILE), StandardCharsets.UTF_8))) {
	        
	        // Full screen
	        bw.write(gp.fullScreenOn ? "On" : "Off");
	        bw.newLine();
	        
	        // Music volume
	        bw.write(String.valueOf(gp.music.volumeScale));
	        bw.newLine();
	        
	        // SE volume
	        bw.write(String.valueOf(gp.se.volumeScale));
	        bw.newLine();
	        
	    } catch (IOException e) {
	        System.err.println("Gagal menyimpan config: " + e.getMessage());
	    }
	}

	public void loadConfig() {
		File file = new File(CONFIG_FILE);
		if (!file.exists()) {
			return;
		}
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
		        new FileInputStream(file), StandardCharsets.UTF_8))) {
		    
		    String s = br.readLine();
		    if (s != null) {
		        gp.fullScreenOn = "On".equalsIgnoreCase(s.trim());
		    }
		    
		    s = br.readLine();
		    if (s != null) {
		        try {
		            gp.music.volumeScale = Integer.parseInt(s.trim());
		        } catch (NumberFormatException ignored) {}
		    }
		    
		    s = br.readLine();
		    if (s != null) {
		        try {
		            gp.se.volumeScale = Integer.parseInt(s.trim());
		        } catch (NumberFormatException ignored) {}
		    }
		    
		} catch (Exception e) {
		    System.err.println("Gagal memuat config: " + e.getMessage());
		}
	}
}
