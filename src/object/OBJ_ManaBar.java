package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_ManaBar extends Entity {

    public OBJ_ManaBar(GamePanel gp) {
        super(gp);
        
        type = type_pickupOnly;
        name = "Mana Bar";
		value = 1;
        down1 = setup("/objects/mana_full", gp.tileSize, gp.tileSize);
        image = down1;
        down2 = setup("/objects/mana_blank", gp.tileSize, gp.tileSize);
        image2 = down2;
    }
    
    public void use(Entity entity) {
		
		gp.playSE(1);
		gp.ui.addMessage("Mana +" + value);
		entity.mana += value;
	}
}