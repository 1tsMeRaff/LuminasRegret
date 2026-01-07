package monster;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_PlayerMana;

public class MON_GoblinKing extends Entity {
	
	public static final String monName = "Goblin King"; 

	public MON_GoblinKing(GamePanel gp) {
		super(gp);
		// TODO Auto-generated constructor stub
		this.gp = gp;
		
		type = type_monster;
		boss = true;
		name = monName;
		defaultSpeed = 1;
		speed = defaultSpeed;
		maxLife = 50;
		life = maxLife;
		attack = 0;
		defense = 2;
		exp = 50;
		knockBackPower = 5;
		sleep = true;
		
		int area = gp.tileSize * 5;
		solidArea.x = 48;
		solidArea.y = 48;
		solidArea.width = area - 48 * 2;
		solidArea.height = area - 48;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		attackArea.width = gp.tileSize * 3;
		attackArea.height = gp.tileSize * 3;
		
		motion1_duration = 40;
		motion2_duration = 85;
		
		getImage();
		getAttackImage();
	}
	
	public void getImage() {
		
		int size = 5;
		
		up1 = setup("/monster/goblin_up1", gp.tileSize * size, gp.tileSize * size);
		up2 = setup("/monster/goblin_up2", gp.tileSize * size, gp.tileSize * size);
		down1 = setup("/monster/goblin_down1", gp.tileSize * size, gp.tileSize * size);
		down2 = setup("/monster/goblin_down2", gp.tileSize * size, gp.tileSize * size);
		left1 = setup("/monster/goblin_left1", gp.tileSize * size, gp.tileSize * size);
		left2 = setup("/monster/goblin_left2", gp.tileSize * size, gp.tileSize * size);
		right1 = setup("/monster/goblin_right1", gp.tileSize * size, gp.tileSize * size);
		right2 = setup("/monster/goblin_right2", gp.tileSize * size, gp.tileSize * size);
	}
	
	public void getAttackImage() {
		
		int size = 5;
	
		attackUp1 = setup("/monster/goblin_att_up1", gp.tileSize * size, gp.tileSize * size);
    	attackUp2 = setup("/monster/goblin_att_up2", gp.tileSize * size, gp.tileSize * size);
    	attackUp3 = setup("/monster/goblin_att_up3", gp.tileSize * size, gp.tileSize * size);
    	
    	attackDown1 = setup("/monster/goblin_att_down1", gp.tileSize * size, gp.tileSize * size);
    	attackDown2 = setup("/monster/goblin_att_down2", gp.tileSize * size, gp.tileSize * size);
    	attackDown3 = setup("/monster/goblin_att_down3", gp.tileSize * size, gp.tileSize * size);
    	
    	attackLeft1 = setup("/monster/goblin_att_left1", gp.tileSize * size, gp.tileSize * size);
    	attackLeft2 = setup("/monster/goblin_att_left2", gp.tileSize * size, gp.tileSize * size);
    	attackLeft3 = setup("/monster/goblin_att_left3", gp.tileSize * size, gp.tileSize * size);
    	
    	attackRight1 = setup("/monster/goblin_att_right1", gp.tileSize * size, gp.tileSize * size);
    	attackRight2 = setup("/monster/goblin_att_right2", gp.tileSize * size, gp.tileSize * size);
    	attackRight3 = setup("/monster/goblin_att_right3", gp.tileSize * size, gp.tileSize * size);
    }
	
	@Override
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		
		// Hitung posisi pada layar relatif terhadap player
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		if(worldX + gp.tileSize * 5 > gp.player.worldX - gp.player.screenX &&
		   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
		   worldY + gp.tileSize * 5 > gp.player.worldY - gp.player.screenY &&
		   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
			
			switch(direction) {
			case "up":
				if(attacking == false) {
					if(spriteNum == 1) {image = up1;}
					if(spriteNum == 2) {image = up2;}
				}
				if(attacking == true) {
					if(spriteNum == 1) {image = attackUp1;}
					if(spriteNum == 2) {image = attackUp2;} 
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
			
			if(attacking) {
				g2.drawImage(image, screenX, screenY, null);
			}
			else {
				g2.drawImage(image, screenX, screenY, null);
			}
			
//             g2.setColor(java.awt.Color.RED);
//             g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
		}
	}

	public void setDialogue() {
		
		dialogues[0] = "Tidak ada yang bisa mencuri hartaku!";
		dialogues[1] = "Kamu akan mati disini!";
		dialogues[2] = "SELAMAT DATANG DIKEMATIANMU!";
	}
	public void setAction() {
		
	    
		if(!rage && life < maxLife / 3) {
			rage = true;
			defaultSpeed++;
			speed = defaultSpeed;
			
		}
		
		if(getTileDistance(gp.player) < 10) {
			
			moveTowardPlayer(60);
		}
		else {
			randomMovement();
		}
		
		if(!attacking) {
			checkAttackOnNot(60, gp.tileSize * 10, gp.tileSize * 5);
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

