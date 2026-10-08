package com.luminasregret.game.object;

import com.luminasregret.game.entity.Entity;
import com.luminasregret.engine.core.GamePanel;

public class OBJ_Lantern extends WorldObject {

	public OBJ_Lantern(GamePanel gp) {
		super(gp);
		
		type = type_light;
		name = "Lentera";
		down1 = setup("/objects/lantern", gp.tileSize, gp.tileSize);
		description = "[Lentera]\nIlluminates your \nsurroundings."; // Teks yang akan muncul di menu inventory
		price = 200; // Harga barang di in-game
		lightRadius = 250; // Jarak pandang
		
	}
	
}
