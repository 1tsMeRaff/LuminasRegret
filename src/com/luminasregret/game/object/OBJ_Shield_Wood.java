package com.luminasregret.game.object;

import com.luminasregret.game.entity.Entity;
import com.luminasregret.engine.core.GamePanel;

public class OBJ_Shield_Wood extends Entity {

	public OBJ_Shield_Wood(GamePanel gp) {
		super(gp);
		
		type = type_shield;
		name = "Wood Shield";
	    down1 = setup("/objects/shield", gp.tileSize, gp.tileSize);
	    defenseValue = 1;
	    description = "[" + name + "]\n Just a regular shield.";
	}

	
}
