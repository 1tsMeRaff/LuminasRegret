package object;

//Import Entity sebagai parent class semua object di game
import entity.Entity;

//Import GamePanel untuk akses UI dan game state
import main.GamePanel;

public class OBJ_Door1 extends Entity {
	
	// Referensi ke GamePanel
	GamePanel gp;
	
	public OBJ_Door1(GamePanel gp) {
		
		// Memanggil constructor dari Entity
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
		
		gp.gameState = gp.dialogueState;
		gp.ui.currentDialogue = "butuh kunci.";
	}

}
