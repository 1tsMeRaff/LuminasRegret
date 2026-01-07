package object;

//Import Entity sebagai parent class semua object
import entity.Entity;

//Import GamePanel untuk akses UI dan game state
import main.GamePanel;

public class OBJ_Door extends Entity {
	
	// Referensi ke GamePanel
	GamePanel gp;
	
	public OBJ_Door(GamePanel gp) {
		
		// Memanggil constructor dari Entity
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
		
		gp.gameState = gp.dialogueState;
		gp.ui.currentDialogue = "butuh kunci.";
	}

}
