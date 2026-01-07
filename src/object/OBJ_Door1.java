package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Door1 extends Entity {
	
	GamePanel gp;
	
	public OBJ_Door1(GamePanel gp) {
		super(gp);
		
		type = type_obstacle;
		name = "Door1";
		down1 = setup("/objects/door_atas",gp.tileSize,gp.tileSize);
		collision = true;
		
		solidArea.x = 0;
		solidArea.y = 16;
		solidArea.width = 48;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
	}
	public void interact() {
		
		gp.gameState = gp.dialogueState;
		gp.ui.currentDialogue = "butuh kunci.";
	}

}
