package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Coin_Bronze extends Entity {
	
	GamePanel gp;
	
	public OBJ_Coin_Bronze(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		// Objek yang langsung aktif saat disentuh (auto-pickup)
		type = type_pickupOnly;
		name = "Coin Bronze";
		value = 1;
		down1 = setup("/objects/coin_bronze", gp.tileSize, gp.tileSize);
	}

		public void use(Entity entity) { // yang terjadi saat Player menyentuh koin
			
			gp.playSE(1); // Sound Effect
			gp.ui.addMessage("Coin +" +value); // Muncul teks pesan di layar
			gp.player.coin += value; // Menambah jumlah koin di dompet player
	}
}
