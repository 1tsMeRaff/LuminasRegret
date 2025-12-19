package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	GamePanel gp;
	public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;
	
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
		
		// CharacterState
		else if(gp.gameState == gp.characterState) {
			characterState(code);
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
				//gp.playMusic(0);
				
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
		if(code == KeyEvent.VK_P || code == KeyEvent.VK_ESCAPE) {
			gp.gameState = gp.pauseState;
		}
		if(code == KeyEvent.VK_C) {
			gp.gameState = gp.characterState;
		}
		if(code == KeyEvent.VK_E) {
			enterPressed = true;
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
		
		if(code == KeyEvent.VK_ENTER) {
			gp.gameState = gp.playState;
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
		if(code == KeyEvent.VK_ENTER) {
			enterPressed = false;
		}
	}
}