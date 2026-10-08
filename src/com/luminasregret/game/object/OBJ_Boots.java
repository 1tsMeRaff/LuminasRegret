package com.luminasregret.game.object;

import com.luminasregret.game.entity.Entity;
import com.luminasregret.engine.core.GamePanel;

public class OBJ_Boots extends Entity {
	
	public OBJ_Boots(GamePanel gp) {
		super(gp);
		
		name = "Boots";
		down1 = setup("/objects/boots", gp.tileSize, gp.tileSize);
		
	}
}
