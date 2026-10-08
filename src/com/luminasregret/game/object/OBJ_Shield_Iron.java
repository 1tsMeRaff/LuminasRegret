package com.luminasregret.game.object;

import com.luminasregret.game.entity.Entity;
import com.luminasregret.engine.core.GamePanel;

public class OBJ_Shield_Iron extends Entity{

	public OBJ_Shield_Iron(GamePanel gp) {
		super(gp);
		
		type = type_shield;
		name = "Iron Shield";
	    down1 = setup("/objects/iron_shield", gp.tileSize, gp.tileSize);
	    defenseValue = 2;
	    description = "[" + name + "]\n Perisai yang terbuat dari besi.";
	}

	
}
