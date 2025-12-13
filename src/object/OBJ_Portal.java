package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Portal extends Entity {
	
	public OBJ_Portal(GamePanel gp) {
		super(gp);
		
		name = "Door";
		down1 = setup("/objects/portal");
		collision = true;
		
		solidArea.x = 0;
		solidArea.y = 16;
		solidArea.width = 48;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
	}
}