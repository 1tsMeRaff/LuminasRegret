package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Standard extends Entity {

	public OBJ_Sword_Standard(GamePanel gp) {
		super(gp);
		
		type = type_sword;
		name = "Normal Sword";
	    down1 = setup("/objects/sword", gp.tileSize, gp.tileSize);
	    attackValue = 1;
	    attackArea.width = 40;
	    attackArea.height = 40;
	    description = "[" + name + "]\n Just a regular sword.";
	    
	}
}
