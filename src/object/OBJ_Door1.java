package object;

//Import Entity sebagai parent class semua object di game
import entity.Entity;

//Import GamePanel untuk akses UI dan game state
import main.GamePanel;

public class OBJ_Door1 extends Entity {
	
	public OBJ_Door1(GamePanel gp) {
		super(gp);
		
		type = type_obstacle;
		name = "Door1";
		down1 = setup("/objects/door_atas",gp.tileSize,gp.tileSize);
		collision = true;
		
		// Mengatur area tabrakan (collision area)
		solidArea.x = 0;
		solidArea.y = 16;
		solidArea.width = 48;
		solidArea.height = 32;
		
		// Menyimpan posisi default solidArea
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
