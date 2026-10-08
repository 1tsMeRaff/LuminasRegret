package com.luminasregret.game.object;

import com.luminasregret.game.entity.Entity;
import com.luminasregret.engine.core.GamePanel;

public class OBJ_Key extends WorldObject {
	
	GamePanel gp;
	
	public OBJ_Key(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		type = type_consumable;
		name = "kunci";
		type = type_consumable;
		down1 = setup("/objects/key", gp.tileSize, gp.tileSize);
		description = "[" + name + "]\n Its a kunci.";
		
	}
	public void use(Entity entity) {
		
		gp.gameState = gp.dialogueState;
		
		int objIndex = getDetected(entity, gp.obj, "Door");
		
		if(objIndex != 999) {
			gp.ui.currentDialogue = "Menggunakan " + name + " dan membuka pintu";
			gp.playSE(1);
			gp.obj[gp.currentMap][objIndex] = null;
			gp.obj[gp.currentMap][objIndex + 1] = null;
		}
		else {
			gp.ui.currentDialogue = "apa yang kamu lakukan?";
			gp.player.inventory.add(new OBJ_Key(gp));
		}
	}
}
