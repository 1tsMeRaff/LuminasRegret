package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	GamePanel gp;
	public boolean upPressed, downPressed, leftPressed, rightPressed, actionPressed, enterPressed, rangeKeyPressed;
	
	//Debug
	boolean showDebugText= false;
	
	
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}
	
	
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		
		// Title State
		if(gp.gameState == gp.titleState) {
			titleState(code);
		}
	
		// Play State
		else if(gp.gameState == gp.playState) {
			playState(code);
		}
		//Pause State
		else if(gp.gameState == gp.pauseState) {
			pauseState(code);
		}
		
		//Dialogue State
		else if(gp.gameState == gp.dialogueState) {
			dialogueState(code);
		}
		
		// Character State
		else if(gp.gameState == gp.characterState) {
			characterState(code);
		}
		
		// Option State
		else if(gp.gameState == gp.optionsState) {
			optionsState(code);
		}
	}

	
	public void titleState(int code) {
		
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			gp.ui.commandNum--;
			if(gp.ui.commandNum < 0) {
				gp.ui.commandNum = 2;
			}
		}
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			gp.ui.commandNum++;
			if(gp.ui.commandNum > 2) {
				gp.ui.commandNum = 0;
			}
		}
		if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
			if(gp.ui.commandNum == 0) {
				gp.gameState = gp.playState;
				gp.playMusic(0);
				
			}
			if(gp.ui.commandNum == 1) {
				
				
			}
			if(gp.ui.commandNum == 2) {
				System.exit(code);
				
			}
		}
	}
	
	public void playState(int code) {
		
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			upPressed = true;
		}
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			downPressed = true;
		}
		if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			leftPressed = true;
		}
		if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			rightPressed = true;
		}
		if(code == KeyEvent.VK_E) {
		    actionPressed = true;
		}
		if(code == KeyEvent.VK_P) {
			gp.gameState = gp.pauseState;
		}
		if(code == KeyEvent.VK_C) {
			gp.gameState = gp.characterState;
		}
		if(code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}
		if(code == KeyEvent.VK_F) {
			rangeKeyPressed = true;
		}
		if(code == KeyEvent.VK_ESCAPE) {
			gp.gameState = gp.optionsState;
			gp.ui.commandNum = 0; // RESET ke pilihan pertama
	        gp.ui.subState = 0;   // RESET ke main options
		}
		
		//Debug
		if(code == KeyEvent.VK_T) {
			if (showDebugText == false) {
		        showDebugText = true;
		    } else {
		        showDebugText = false;
		    }
		}
		if(code == KeyEvent.VK_R) {
			gp.tileM.loadMap("/maps/world01.txt");
		}
	}
	
	public void pauseState(int code) {
		
		if(code == KeyEvent.VK_P || code == KeyEvent.VK_ESCAPE) {
			gp.gameState = gp.playState;
		}
	}
	
	public void dialogueState(int code) {
		
		if(code == KeyEvent.VK_E) {
			gp.gameState = gp.playState;
			actionPressed = false;
		}
	}

	public void characterState(int code) {
	
		if(code == KeyEvent.VK_C) {
			gp.gameState = gp.playState;
		}
		
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			if(gp.ui.slotRow != 0) {
				gp.ui.slotRow--;
				gp.playSE(9);
			}
		}
		
		if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			if(gp.ui.slotCol != 0) {
				gp.ui.slotCol--;
				gp.playSE(9);
			}
		}
		
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			if(gp.ui.slotRow != 3) {
				gp.ui.slotRow++;
				gp.playSE(9);
			}
		}
		
		if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			if(gp.ui.slotCol != 4) {
				gp.ui.slotCol++;
				gp.playSE(9);
			}
		}
		
		if(code == KeyEvent.VK_ENTER) {
			gp.player.selectItem();
		}
	}
	
	public void optionsState(int code) {
		
	    if (code == KeyEvent.VK_ESCAPE) {
	        gp.gameState = gp.playState;
	    }
	    if (code == KeyEvent.VK_ENTER) {
	        enterPressed = true;
	    }

	 // Tentukan maxCommandNum berdasarkan subState
	    int maxCommandNum = 0;
	    switch(gp.ui.subState) {
	        case 0: // Main options
	            maxCommandNum = 5; 
	            break;
	        case 1: // Full screen notification (hanya "Back")
	            maxCommandNum = 0;
	            break;
	        case 2: // Control screen (hanya "Back")
	            maxCommandNum = 0;
	            break;
	        case 3: // End game confirmation ("Yes" dan "No")
	            maxCommandNum = 1;
	            break;
	    }

	 // Navigasi dengan W/S (hanya untuk subState yang memiliki >1 pilihan)
	    if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
	        if (maxCommandNum > 0) { // Hanya jika ada lebih dari 1 pilihan
	            gp.ui.commandNum--;
	            gp.playSE(9);
	            if (gp.ui.commandNum < 0) {
	                gp.ui.commandNum = maxCommandNum;
	            }
	        }
	    }
	    
	    if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
	        if (maxCommandNum > 0) { // Hanya jika ada lebih dari 1 pilihan
	            gp.ui.commandNum++;
	            gp.playSE(9);
	            if (gp.ui.commandNum > maxCommandNum) {
	                gp.ui.commandNum = 0;
	            }
	        }
	    }
	    
	    if (code == KeyEvent.VK_ENTER) {
	        gp.playSE(9);
	    }
	    
	    if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
	        if (gp.ui.subState == 0) {
	            // Jika kursor di baris Music (index 1)
	            if (gp.ui.commandNum == 1 && gp.music.volumeScale > 0) {
	                gp.music.volumeScale--;
	                gp.music.checkVolume();
	                gp.playSE(9);
	            }
	            // Jika kursor di baris Sound Effect (index 2)
	            if (gp.ui.commandNum == 2 && gp.se.volumeScale > 0) {
	                gp.se.volumeScale--;
	                gp.playSE(9);
	            }
	        }
	    }

	    // MENAMBAH VOLUME (PANAH KANAN atau TOMBOL D)
	    if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
	        if (gp.ui.subState == 0) {
	            if (gp.ui.commandNum == 1 && gp.music.volumeScale < 5) {
	                gp.music.volumeScale++;
	                gp.music.checkVolume();
	                gp.playSE(9);
	            }
	            if (gp.ui.commandNum == 2 && gp.se.volumeScale < 5) {
	                gp.se.volumeScale++;
	                gp.playSE(9);
	            }
	        }
	    }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			upPressed = false;
		}
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			downPressed = false;
		}
		if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			leftPressed = false;
		}
		if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			rightPressed = false;
		}
		if(code == KeyEvent.VK_E) {
		    actionPressed = false;
		}
		if(code == KeyEvent.VK_ENTER) {
			enterPressed = false;
		}
		if(code == KeyEvent.VK_F) {
			rangeKeyPressed = false;
		}
	}
}