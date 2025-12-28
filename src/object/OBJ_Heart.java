package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Heart extends Entity {
	
	GamePanel gp;

	public OBJ_Heart(GamePanel gp) {
		super(gp);
		
		type = type_pickupOnly;
		name = "Heart";
		value = 2;
		down1 = setup("/objects/heart_full", gp.tileSize, gp.tileSize);
		image = setup("/objects/heart_full", gp.tileSize, gp.tileSize);
		image2 = setup("/objects/heart_half", gp.tileSize, gp.tileSize);
		image3 = setup("/objects/heart_blank", gp.tileSize, gp.tileSize);
<<<<<<< HEAD
	} 
=======
	}
	public void use(Entity entity) {
		
		gp.playSE(1);
		gp.ui.addMessage("Life +" + value);
		entity.life += value;
	}
		
>>>>>>> b74082ab7769620652817009b083bf45c3f0f96b
}