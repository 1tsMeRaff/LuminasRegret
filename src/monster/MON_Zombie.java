package monster;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_PlayerMana;

public class MON_Zombie extends Entity {

	public MON_Zombie(GamePanel gp) {
		super(gp);
		// TODO Auto-generated constructor stub
		this.gp = gp;
		
		type = type_monster;
		name = "Green Slime";
		defaultSpeed = 1;
		speed = defaultSpeed;
		maxLife = 4;
		life = maxLife;
		attack = 2;
		defense = 2;
		exp = 10;
		
		solidArea.x = 4;
		solidArea.y = 4;
		solidArea.width = 40;
		solidArea.height = 44;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		attackArea.width = 48;
		attackArea.height = 48;
		
		motion1_duration = 40;
		motion2_duration = 85;
		
		getImage();
		getAttackImage();
	}
	
	public void getImage() {
		
		int size = 64;
		
		up1 = setup("/monster/zombie_up1", size, size);
		up2 = setup("/monster/zombie_up1", size, size);
		down1 = setup("/monster/zombie_down1", size, size);
		down2 = setup("/monster/zombie_down2", size, size);
		left1 = setup("/monster/zombie_left1", size, size);
		left2 = setup("/monster/zombie_left2", size, size);
		right1 = setup("/monster/zombie_right1", size, size);
		right2 = setup("/monster/zombie_right2", size, size);
	}
	
	public void getAttackImage() {
		
		int size = 192;
	
		attackUp1 = setup("/monster/zombie_att_up1", size, size);
    	attackUp2 = setup("/monster/zombie_att_up2", size, size);
    	attackUp3 = setup("/monster/zombie_att_up3", size, size);
    	
    	attackDown1 = setup("/monster/zombie_att_down1", size, size);
    	attackDown2 = setup("/monster/zombie_att_down2", size, size);
    	attackDown3 = setup("/monster/zombie_att_down3", size, size);
    	
    	attackLeft1 = setup("/monster/zombie_att_left1", size, size);
    	attackLeft2 = setup("/monster/zombie_att_left2", size, size);
    	attackLeft3 = setup("/monster/zombie_att_left3", size, size);
    	
    	attackRight1 = setup("/monster/zombie_att_right1", size, size);
    	attackRight2 = setup("/monster/zombie_att_right2", size, size);
    	attackRight3 = setup("/monster/zombie_att_right3", size, size);
    }
	
	    @Override
		public void draw(Graphics2D g2) {
			
			BufferedImage image = null;
			
			// Hitung posisi pada layar relatif terhadap player
			int screenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;
			
			// Optimization: Hanya gambar jika masuk area kamera
			if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
			   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
			   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
			   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
				
				// Logika pemilihan gambar (Copy logika switch dari Entity parent jika perlu, 
	            // atau gunakan logic di bawah ini)
				switch(direction) {
				case "up":
					if(attacking == false) {
						if(spriteNum == 1) {image = up1;}
						if(spriteNum == 2) {image = up2;}
					}
					if(attacking == true) {
						if(spriteNum == 1) {image = attackUp1;}
						if(spriteNum == 2) {image = attackUp2;} 
	                    // Asumsi ada 3 frame attack sesuai setup image Anda
	                    if(spriteNum == 3) {image = attackUp3;} 
					}
					break;
				case "down":
					if(attacking == false) {
						if(spriteNum == 1) {image = down1;}
						if(spriteNum == 2) {image = down2;}
					}
					if(attacking == true) {
						if(spriteNum == 1) {image = attackDown1;}
						if(spriteNum == 2) {image = attackDown2;}
	                    if(spriteNum == 3) {image = attackDown3;}
					}
					break;
				case "left":
					if(attacking == false) {
						if(spriteNum == 1) {image = left1;}
						if(spriteNum == 2) {image = left2;}
					}
					if(attacking == true) {
						if(spriteNum == 1) {image = attackLeft1;}
						if(spriteNum == 2) {image = attackLeft2;}
	                    if(spriteNum == 3) {image = attackLeft3;}
					}
					break;
				case "right":
					if(attacking == false) {
						if(spriteNum == 1) {image = right1;}
						if(spriteNum == 2) {image = right2;}
					}
					if(attacking == true) {
						if(spriteNum == 1) {image = attackRight1;}
						if(spriteNum == 2) {image = attackRight2;}
	                    if(spriteNum == 3) {image = attackRight3;}
					}
					break;
				}
				
				// --- FIX ADJUSTMENT ---
				if(attacking) {
	                // Geser gambar ke kiri dan atas sebesar 64 pixel agar tetap di tengah
	                // Karena ukuran attack (192) - ukuran normal (64) = 128. 128 / 2 = 64.
					g2.drawImage(image, screenX - 64, screenY - 64, null);
				}
				else {
	                // Gambar normal
					g2.drawImage(image, screenX, screenY, null);
				}
			}
		}

	public void setAction() {
		
	    
		if (onPath) {
			checkStopChasingOrNot(gp.player, 5, 100);
			
			// Cari jalan setiap kali player bergerak
	        searchPath(getGoalCol(gp.player), getGoalRow(gp.player));
		}
		else {
			checkStartChasingOrNot(gp.player, 5, 100);
			randomMovement();
		}
		
		if(!attacking) {
			checkAttackOnNot(30, gp.tileSize * 4, gp.tileSize);
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

