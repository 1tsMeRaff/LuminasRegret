package com.luminasregret.game.object;

import com.luminasregret.game.entity.Entity;
import com.luminasregret.engine.core.GamePanel;

public class OBJ_ManaBar extends Entity {

    public OBJ_ManaBar(GamePanel gp) {
        super(gp);
        
        type = type_pickupOnly;
        name = "Mana Bar";
		value = 1;
        down1 = setup("/objects/mana_full", gp.tileSize, gp.tileSize);
        image = setup("/objects/mana_full", 24, 24);
        down2 = setup("/objects/mana_blank", gp.tileSize, gp.tileSize);
        image2 = setup("/objects/mana_blank", 24, 24);
    }
    
    public void use(Entity entity) {
		
	}
}