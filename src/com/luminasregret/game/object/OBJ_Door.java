package com.luminasregret.game.object;

//Import Entity sebagai parent class semua object
import com.luminasregret.game.entity.Entity;

//Import GamePanel untuk akses UI dan game state
import com.luminasregret.engine.core.GamePanel;

public class OBJ_Door extends Entity {
	
	public OBJ_Door(GamePanel gp) {
		super(gp);
		
		// Menentukan tipe object sebagai obstacle (penghalang)
		type = type_obstacle;
		name = "Door";
		down1 = setup("/objects/door_bawah",gp.tileSize,gp.tileSize);
		collision = true;
		
		// Mengatur area tabrakan pintu
		solidArea.x = 0;
		solidArea.y = 16;
		solidArea.width = 48;
		solidArea.height = 32;
		
		// Menyimpan posisi awal solidArea
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
	}
	public void interact() {
		gp.ui.npc = null;
		if (gp.player.removeItem("Key") || gp.player.removeItem("Kunci")) {
			gp.playSE(1);
			gp.gameState = gp.dialogueState;
			gp.ui.currentSpeakerName = "Pintu";
			gp.ui.currentDialogue = "Kamu membuka pintu dengan kunci!";
			for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
				if (gp.obj[gp.currentMap][i] == this) {
					gp.obj[gp.currentMap][i] = null;
					break;
				}
			}
		} else {
			gp.gameState = gp.dialogueState;
			gp.ui.currentSpeakerName = "Pintu";
			gp.ui.currentDialogue = "Pintu ini terkunci rapat.\nKamu membutuhkan Kunci untuk membukanya.";
		}
	}
}
