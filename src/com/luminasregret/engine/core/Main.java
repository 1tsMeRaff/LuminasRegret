package com.luminasregret.engine.core;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

	public static JFrame window;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			window = new JFrame();
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setResizable(false);
			window.setTitle("Lumina's Regret");

			GamePanel gamePanel = new GamePanel();
			window.add(gamePanel);

			gamePanel.config.loadConfig();
			if (gamePanel.fullScreenOn) {
				window.setUndecorated(true);
			}

			window.pack();
			window.setLocationRelativeTo(null);
			window.setVisible(true);

			gamePanel.setupGame();
			gamePanel.startGameThread();
		});
	}
}