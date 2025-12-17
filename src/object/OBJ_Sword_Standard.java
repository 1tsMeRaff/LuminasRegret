package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Standard extends Entity {

	public OBJ_Sword_Standard(GamePanel gp) {
		super(gp);
		name = "Normal Sword";
	    down1 = setup("/objects/sword", gp.tileSize, gp.tileSize);
	    attackValue = 1;
	}
}
