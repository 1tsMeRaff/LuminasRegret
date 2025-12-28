package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_PlayerMana extends Entity {

	GamePanel gp;
	
	public OBJ_PlayerMana(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		
		type = type_pickupOnly;
		value = 1;
		name = "PlayerMana";
		down1 = setup("/objects/heart_full", gp.tileSize, gp.tileSize);
		image = setup("/objects/heart_full", gp.tileSize, gp.tileSize);
		image2 = setup("/objects/heart_half", gp.tileSize, gp.tileSize);
		image3 = setup("/objects/heart_blank", gp.tileSize, gp.tileSize);
	}
	public void use(Entity entity) {
		
		gp.playSE(1);
		gp.ui.addMessage("Mana +" + value);
		entity.mana += value;
	}

}
