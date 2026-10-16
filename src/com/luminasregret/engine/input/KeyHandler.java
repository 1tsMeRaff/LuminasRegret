package com.luminasregret.engine.input;

import com.luminasregret.engine.core.GamePanel;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Unified Keyboard Input Subsystem for Lumina's Regret.
 * Supports dual-hand ergonomics:
 * - Layout A: WASD Movement + J (Attack) + K (Magic) + L (Guard) + Space/Shift/Q (Dash) + E (Interact) + I/C/Tab (Inventory)
 * - Layout B: Arrow Keys Movement + Z (Attack) + X (Magic) + C (Guard) + Space/Shift (Dash) + Enter (Interact)
 * Features SOCD anti-jamming, anti-sticking FocusListener, and consistent UI confirmations.
 */
public class KeyHandler implements KeyListener, FocusListener {

	GamePanel gp;
	public volatile boolean upPressed, downPressed, leftPressed, rightPressed;
	public volatile boolean actionPressed, enterPressed, rangeKeyPressed, dashKeyPressed;
	public volatile boolean attackKeyPressed, guardKeyPressed;
	
	// Debug
	public volatile boolean showDebugText = false;
	
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// No-op for raw action keys
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		// Title State
		if(gp.gameState == gp.titleState) {
			titleState(code);
		}
		// Play State
		else if(gp.gameState == gp.playState) {
			playState(code);
		}
		// Pause State
		else if(gp.gameState == gp.pauseState) {
			pauseState(code);
		}
		// Dialogue State
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
		// Game Over State
		else if(gp.gameState == gp.gameOverState) {
			gameOverState(code);
		}
		// Trade State
		else if(gp.gameState == gp.tradeState) {
			tradeState(code);
		}
		// Game Clear State
		else if(gp.gameState == gp.gameClearState) {
			gameClearState(code);
		}
	}  

	public void titleState(int code) {
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			gp.ui.commandNum--;
			if(gp.ui.commandNum < 0) {
				gp.ui.commandNum = 2;
			}
			gp.playSE(9);
		}
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			gp.ui.commandNum++;
			if(gp.ui.commandNum > 2) {
				gp.ui.commandNum = 0;
			}
			gp.playSE(9);
		}
		if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE || code == KeyEvent.VK_E || 
		   code == KeyEvent.VK_J || code == KeyEvent.VK_Z) {
			if(gp.ui.commandNum == 0) {
				gp.gameState = gp.playState;
				gp.playMusic(0);
			}
			if(gp.ui.commandNum == 1) {
				// Slot for Load Game
			}
			if(gp.ui.commandNum == 2) {
				System.exit(0);
			}
		}
	}
	
	public void playState(int code) {
		// Movement Keys (WASD + Arrow Keys)
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

		// Dedicated Melee Attack: J or Z
		if(code == KeyEvent.VK_J || code == KeyEvent.VK_Z) {
			attackKeyPressed = true;
		}

		// Dedicated Magic Projectile Slash: K, X, or F
		if(code == KeyEvent.VK_K || code == KeyEvent.VK_X || code == KeyEvent.VK_F) {
			rangeKeyPressed = true;
		}

		// Shield Guard Stance: Hold L or C
		if(code == KeyEvent.VK_L || code == KeyEvent.VK_C) {
			guardKeyPressed = true;
		}

		// Dash / Evade: SPACE, SHIFT, or Q
		if(code == KeyEvent.VK_SPACE || code == KeyEvent.VK_SHIFT || code == KeyEvent.VK_Q) {
			dashKeyPressed = true;
		}

		// Interaction / Talk / Inspect: E or ENTER
		if(code == KeyEvent.VK_E) {
		    actionPressed = true;
		}
		if(code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}

		// Inventory & Stats: TAB, B (Bag), or I
		if(code == KeyEvent.VK_TAB || code == KeyEvent.VK_B || code == KeyEvent.VK_I) {
			gp.gameState = gp.characterState;
			resetMovement();
		}

		// Pause: P
		if(code == KeyEvent.VK_P) {
			gp.gameState = gp.pauseState;
			resetMovement();
		}

		// Options Menu: ESCAPE
		if(code == KeyEvent.VK_ESCAPE) {
			gp.gameState = gp.optionsState;
			gp.ui.commandNum = 0;
	        gp.ui.subState = 0;
			resetMovement();
		}
		
		// Debug Toggles
		if(code == KeyEvent.VK_T) {
			showDebugText = !showDebugText;
		}
		if(code == KeyEvent.VK_R) {
			switch(gp.currentMap) {
			case 0: gp.tileM.loadMap("/maps/maps1.txt", 0); break;
			case 1: gp.tileM.loadMap("/maps/maps2.txt", 1); break;
			}
		}
	}
	
	public void pauseState(int code) {
		if(code == KeyEvent.VK_P || code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_ENTER || 
		   code == KeyEvent.VK_SPACE || code == KeyEvent.VK_E || code == KeyEvent.VK_J || code == KeyEvent.VK_Z) {
			gp.gameState = gp.playState;
		}
	}
	
	public void dialogueState(int code) {
		if(code == KeyEvent.VK_E || code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE || 
		   code == KeyEvent.VK_J || code == KeyEvent.VK_Z) {
			actionPressed = false;
			enterPressed = false;
			attackKeyPressed = false;
			gp.advanceDialogue();
		}
	}

	public void characterState(int code) {
		if(code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_TAB || code == KeyEvent.VK_B || code == KeyEvent.VK_I) {
			gp.gameState = gp.playState;
			resetMovement();
		}
		
		if(code == KeyEvent.VK_E || code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE || 
		   code == KeyEvent.VK_J || code == KeyEvent.VK_Z) {
			gp.player.selectItem();
		}
		playerInventory(code);
	}
	
	public void optionsState(int code) {
	    if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_Q || code == KeyEvent.VK_X) {
	        if (gp.ui.subState != 0) {
	            gp.ui.subState = 0;
	            gp.ui.commandNum = 0;
	        } else {
	            gp.gameState = gp.playState;
				resetMovement();
	        }
	    }
	    if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_E || code == KeyEvent.VK_SPACE || 
		    code == KeyEvent.VK_J || code == KeyEvent.VK_Z) {
	        enterPressed = true;
			gp.playSE(9);
	    }

	    int maxCommandNum = 0;
	    switch(gp.ui.subState) {
	        case 0: maxCommandNum = 5; break; // Main options
	        case 1: maxCommandNum = 0; break;
	        case 2: maxCommandNum = 0; break;
	        case 3: maxCommandNum = 1; break;
	    }

	    if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
	        if (maxCommandNum > 0) {
	            gp.ui.commandNum--;
	            gp.playSE(9);
	            if (gp.ui.commandNum < 0) {
	                gp.ui.commandNum = maxCommandNum;
	            }
	        }
	    }
	    
	    if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
	        if (maxCommandNum > 0) {
	            gp.ui.commandNum++;
	            gp.playSE(9);
	            if (gp.ui.commandNum > maxCommandNum) {
	                gp.ui.commandNum = 0;
	            }
	        }
	    }
	    
	    if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
	        if (gp.ui.subState == 0) {
	            if (gp.ui.commandNum == 1 && gp.music.volumeScale > 0) {
	                gp.music.volumeScale--;
	                gp.music.checkVolume();
	                gp.playSE(9);
	            }
	            if (gp.ui.commandNum == 2 && gp.se.volumeScale > 0) {
	                gp.se.volumeScale--;
	                gp.playSE(9);
	            }
	        }
	    }

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

	public void gameOverState(int code) {
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			gp.ui.commandNum--;
			if(gp.ui.commandNum < 0) {
				gp.ui.commandNum = 1;
			}
			gp.playSE(9);
		}
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			gp.ui.commandNum++;
			if(gp.ui.commandNum > 1) {
				gp.ui.commandNum = 0;
			}
			gp.playSE(9);
		}
		if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_E || code == KeyEvent.VK_SPACE || 
		   code == KeyEvent.VK_J || code == KeyEvent.VK_Z) {
			if(gp.ui.commandNum == 0) {
				gp.gameState = gp.playState;
				gp.resetGame(false);
				gp.playAreaMusic();
			}
			else if(gp.ui.commandNum == 1) {
				gp.gameState = gp.titleState;
				gp.resetGame(true);
				gp.playMusic(4);
			}
		}
	}

	public void gameClearState(int code) {
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			gp.ui.commandNum--;
			if(gp.ui.commandNum < 0) {
				gp.ui.commandNum = 1;
			}
			gp.playSE(9);
		}
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			gp.ui.commandNum++;
			if(gp.ui.commandNum > 1) {
				gp.ui.commandNum = 0;
			}
			gp.playSE(9);
		}
		if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_E || code == KeyEvent.VK_SPACE || 
		   code == KeyEvent.VK_J || code == KeyEvent.VK_Z) {
			if(gp.ui.commandNum == 0) {
				gp.resetGame(true);
				gp.gameState = gp.playState;
				gp.playAreaMusic();
			}
			else if(gp.ui.commandNum == 1) {
				gp.resetGame(true);
				gp.gameState = gp.titleState;
				gp.playMusic(4);
			}
		}
	}

	public void tradeState(int code) {
	    if(code == KeyEvent.VK_E || code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE || 
		   code == KeyEvent.VK_J || code == KeyEvent.VK_Z) {
	        actionPressed = true;
	    }
	    
	    if(gp.ui.subState == 0) {
	        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
	            gp.ui.commandNum--;
	            gp.playSE(9);
	            if(gp.ui.commandNum < 0) gp.ui.commandNum = 2;
	        }
	        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
	            gp.ui.commandNum++;
	            gp.playSE(9);
	            if(gp.ui.commandNum > 2) gp.ui.commandNum = 0;
	        }
	        if(code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_Q || code == KeyEvent.VK_X) {
	            gp.ui.commandNum = 0;
	            gp.gameState = gp.playState;
	            gp.ui.npc = null;
	            gp.ui.currentDialogue = "";
	            gp.ui.currentSpeakerName = "";
				resetMovement();
	        }
	    }
	    
	    if(gp.ui.subState == 1) {
	        npcInventory(code);
	        if(code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_Q || code == KeyEvent.VK_X) {
	            gp.ui.subState = 0;
	        }
	    }
	    
	    if(gp.ui.subState == 2) {
	        playerInventory(code);
	        if(code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_Q || code == KeyEvent.VK_X) {
	            gp.ui.subState = 0;
	        }
	    }
	}
	
	public void playerInventory(int code) {
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			if(gp.ui.playerSlotRow != 0) {
				gp.ui.playerSlotRow--;
				gp.playSE(9);
			}
		}
		if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			if(gp.ui.playerSlotCol != 0) {
				gp.ui.playerSlotCol--;
				gp.playSE(9);
			}
		}
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			if(gp.ui.playerSlotRow != 3) {
				gp.ui.playerSlotRow++;
				gp.playSE(9);
			}
		}
		if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			if(gp.ui.playerSlotCol != 4) {
				gp.ui.playerSlotCol++;
				gp.playSE(9);
			}
		}
	}
	
	public void npcInventory(int code) {
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			if(gp.ui.npcSlotRow != 0) {
				gp.ui.npcSlotRow--;
				gp.playSE(9);
			}
		}
		if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			if(gp.ui.npcSlotCol != 0) {
				gp.ui.npcSlotCol--;
				gp.playSE(9);
			}
		}
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			if(gp.ui.npcSlotRow != 3) {
				gp.ui.npcSlotRow++;
				gp.playSE(9);
			}
		}
		if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			if(gp.ui.npcSlotCol != 4) {
				gp.ui.npcSlotCol++;
				gp.playSE(9);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
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
		if(code == KeyEvent.VK_J || code == KeyEvent.VK_Z) {
			attackKeyPressed = false;
		}
		if(code == KeyEvent.VK_K || code == KeyEvent.VK_X || code == KeyEvent.VK_F) {
			rangeKeyPressed = false;
		}
		if(code == KeyEvent.VK_L || code == KeyEvent.VK_C) {
			guardKeyPressed = false;
		}
		if(code == KeyEvent.VK_SPACE || code == KeyEvent.VK_SHIFT || code == KeyEvent.VK_Q) {
			dashKeyPressed = false;
		}
		if(code == KeyEvent.VK_E) {
		    actionPressed = false;
		}
		if(code == KeyEvent.VK_ENTER) {
			enterPressed = false;
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		// Ready for input
	}

	@Override
	public void focusLost(FocusEvent e) {
		resetAllKeys();
	}

	public void resetMovement() {
		upPressed = false;
		downPressed = false;
		leftPressed = false;
		rightPressed = false;
	}

	public void resetAllKeys() {
		resetMovement();
		actionPressed = false;
		enterPressed = false;
		rangeKeyPressed = false;
		dashKeyPressed = false;
		attackKeyPressed = false;
		guardKeyPressed = false;
	}
}