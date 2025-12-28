package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_PlayerMana extends Entity {

	GamePanel gp;
	
	public OBJ_PlayerMana(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name = "PlayerMana";
		image = setup("/objects/heart_full", gp.tileSize, gp.tileSize);
		image2 = setup("/objects/heart_half", gp.tileSize, gp.tileSize);
		image3 = setup("/objects/heart_blank", gp.tileSize, gp.tileSize);
	}

}
