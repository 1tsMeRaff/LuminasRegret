package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_GreenProjectile;
import object.OBJ_Heart;
import object.OBJ_PlayerMana;

public class MON_GreenSlime extends Entity {
	
	GamePanel gp;

	public MON_GreenSlime(GamePanel gp) {
		super(gp);
		
		this.gp = gp;
		
		type = type_monster;
		name = "Green Slime";
		defaultSpeed = 1;
		speed = defaultSpeed;
		maxLife = 4;
		life = maxLife;
		attack = 2;
		defense = 0;
		exp = 2;
		projectile = new OBJ_GreenProjectile(gp);
		
		solidArea.x = 12;
		solidArea.y = 12;
		solidArea.width = 24;
		solidArea.height = 24;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage();
	}
	public void getImage() {
		
		int size = 64;
		
		up1 = setup("/monster/slimeup1", size, size);
		up2 = setup("/monster/slimeup2", size, size);
		down1 = setup("/monster/slimeidle", size, size);
		down2 = setup("/monster/slimemove", size, size);
		left1 = setup("/monster/slimeleft1", size, size);
		left2 = setup("/monster/slimeleft2", size, size);
		right1 = setup("/monster/slimeright1", size, size);
		right2 = setup("/monster/slimeright2", size, size);
	}
	public void setAction() {
	    
	    if (onPath) {
	        // 1. UPDATE GOAL SECARA REALTIME
	        int currentCol = (worldX + solidArea.x) / gp.tileSize;
	        int currentRow = (worldY + solidArea.y) / gp.tileSize;
	        int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
	        int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;

	        // 2. LOGIKA MENEMBAK (Hanya saat probabilitas tepat)
	        int i = new Random().nextInt(100) + 1;
	        if (i > 99 && !projectile.alive && rangeAvailableCounter == 30) {
	            projectile.set(worldX, worldY, direction, true, this);
	            
	            // Check Vacancy - PINDAHKAN KE DALAM IF MENEMBAK
	            for (int ii = 0; ii < gp.projectile[gp.currentMap].length; ii++) {
	                if (gp.projectile[gp.currentMap][ii] == null) {
	                    gp.projectile[gp.currentMap][ii] = projectile;
	                    break;
	                }
	            }
	            rangeAvailableCounter = 0;
	        }

	        // 3. RE-PATHFINDING (Cari jalan setiap kali player bergerak)
	        searchPath(goalCol, goalRow);

	    } else {
	        // Jarak deteksi (Opsional: Aggro otomatis jika player mendekat)
//	        checkStartChasing(gp.player, 5, 100); 
	        
	        // Random movement jika tidak sedang mengejar
	        randomMovement();
	    }
	}
	
	public void searchPath(int goalCol, int goalRow) {
		
	    int currentCol = (worldX + solidArea.x) / gp.tileSize;
	    int currentRow = (worldY + solidArea.y) / gp.tileSize;

	    boolean found = gp.pFinder.search(currentCol, currentRow, goalCol, goalRow);
	    
	    if (found) {
	    	if (gp.pFinder.pathList.size() > 0) {

		        // Ambil arah dari node pertama di path
		        int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
		        int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

		        // Tentukan arah berdasarkan posisi node berikutnya
		        if (nextY < worldY) direction = "up";
		        else if (nextY > worldY) direction = "down";
		        else if (nextX < worldX) direction = "left";
		        else if (nextX > worldX) direction = "right";
	    	}
	    	else {
	    		onPath = false;
	    	}
	    }
	}
	
	public void damageReaction() {
		
		actionLockCounter = 0;
//		direction = gp.player.direction;
		onPath = true;
	}
	public void checkDrop() {
		
		//Cast a Die
		int i = new Random().nextInt(100)+1;
		
		//Set the Monster Drop
		if(i < 50) {
			dropItems(new OBJ_Coin_Bronze (gp));
		}
		if(i >= 50 && i < 75) {
			dropItems(new OBJ_Heart(gp));
		}
		if(i >= 75 && i < 100) {
			dropItems(new OBJ_PlayerMana(gp));
		}
	}
}
