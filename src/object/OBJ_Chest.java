package object;

import entity.Entity;

//Import GamePanel untuk akses player, UI, dan sound
import main.GamePanel;

public class OBJ_Chest extends Entity {
	
	// Referensi ke GamePanel
	GamePanel gp;
	
	// Item yang tersimpan di dalam peti
	Entity loot;
	
	// Status apakah peti sudah dibuka atau belum
	boolean opened = false;
		
	public OBJ_Chest(GamePanel gp, Entity loot) {
		
		// Memanggil constructor dari Entity
		super(gp);
		
		// Menyimpan referensi GamePanel
		this.gp = gp;
		
		// Menyimpan item di dalam peti
		this.loot = loot;
		
		type =  type_obstacle;
		name = "Chest";
		image = setup("/objects/chest", gp.tileSize, gp.tileSize);
		image2 = setup("/objects/chest_open", gp.tileSize, gp.tileSize);
		
		// Sprite awal adalah peti tertutup
		down1 = image;
		collision = true;
		
		// Mengatur area tabrakan (collision area)
		solidArea.x = 4;
		solidArea.y = 16;
		solidArea.width = 40;
		solidArea.height = 32;
		
		// Menyimpan posisi default solidArea
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
	}
	public void interact() {
		
		// Mengubah game state ke dialog
		gp.gameState = gp.dialogueState;
		
		// Jika peti belum pernah dibuka
		if(opened == false) {
			gp.playSE(1);
			
			StringBuilder sb = new StringBuilder();
			
			// Pesan awal saat membuka peti
			sb.append("kamu membuka peti dan menemukan " + loot.name + "!");
			
			// Cek apakah inventory player sudah penuh
			if(gp.player.inventory.size() == gp.player.maxInventorySize) {
				
				// Jika inventory penuh
				sb.append("kamu tidak bisa membawa lebih banyak");
			}
			else {
				
				// Jika inventory masih tersedia
				sb.append("\nkamu mendapatkan " + loot.name + "!");
				gp.player.inventory.add(loot);
				
				// Ganti sprite peti menjadi terbuka
				down1 = image2;
				
				// Tandai peti sudah dibuka
				opened = true;
			}
			gp.ui.currentDialogue = sb.toString();
		}
		else {
			
			// Jika peti sudah dibuka sebelumnya
			gp.ui.currentDialogue = "sudah membukanya";
		}
	}
}