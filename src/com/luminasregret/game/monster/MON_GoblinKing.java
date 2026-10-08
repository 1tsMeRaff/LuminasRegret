package com.luminasregret.game.monster;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.luminasregret.game.entity.Entity;
import com.luminasregret.engine.core.GamePanel;
import com.luminasregret.game.object.*;
import com.luminasregret.game.object.OBJ_Coin_Bronze;
import com.luminasregret.game.object.OBJ_GreenProjectile;
import com.luminasregret.game.object.OBJ_Heart;
import com.luminasregret.game.object.OBJ_PlayerMana;

public class MON_GoblinKing extends Entity {
	
	public static final String monName = "Goblin King"; 
	public int shootCounter = 0;
	public int summonCounter = 0;
	public boolean phase2Triggered = false;
	private int summonToggle = 0; 

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
		attack = 2;
		defense = 2;
		exp = 50;
		knockBackPower = 5;
//		sleep = true;
		
		int area = gp.tileSize * 5;
		solidArea.x = 48;
		solidArea.y = 48;
		solidArea.width = area - 48 * 2;
		solidArea.height = area - 48;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		attackArea.width = gp.tileSize * 2;
		attackArea.height = gp.tileSize * 4;
		
		motion1_duration = 25;
		motion2_duration = 50;
		
		getImage();
		getAttackImage();
	}
	
	public final void getImage() {
		
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
	
	public final void getAttackImage() {
		
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
			
			if(rage) {
				java.awt.Composite origComp = g2.getComposite();
				g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.35f));
				g2.setColor(new java.awt.Color(255, 30, 30));
				int pulse = (spriteCounter % 16);
				int auraPad = 12 + pulse;
				g2.fillOval(screenX - auraPad, screenY - auraPad, gp.tileSize * 5 + auraPad * 2, gp.tileSize * 5 + auraPad * 2);
				g2.setComposite(origComp);
			}

			if(attacking) {
				drawAttackArea(g2);
				g2.drawImage(image, screenX, screenY, null);
			}
			else {
				g2.drawImage(image, screenX, screenY, null);
			}
		}
	}

	public void setDialogue() {
		
		dialogues[0] = "Tidak ada yang bisa mencuri hartaku!";
		dialogues[1] = "Kamu akan mati disini!";
		dialogues[2] = "SELAMAT DATANG DIKEMATIANMU!";
	}

	@Override
	public void setAction() {
		// Deteksi Transisi Fase 2 (Enrage Mode) saat HP <= 50%
		if (!phase2Triggered && life <= maxLife / 2) {
			phase2Triggered = true;
			rage = true;
			defaultSpeed = 2;
			speed = defaultSpeed;
			attack = 3;

			gp.playSE(6);
			gp.ui.addMessage("GOBLIN KING MURKA! FASE 2 DIMULAI!");
			gp.qManager.showBanner("BOS MURKA!", "Goblin King memanggil minion dan melepaskan sihir kutukan!");

			// Panggil 2 minion pengawal pertama (1 Slime & 1 Zombie)
			summonMinions(2);
		}

		if (getTileDistance(gp.player) < 12) {
			moveTowardPlayer(rage ? 30 : 60);
		} else {
			randomMovement();
		}

		if (!attacking) {
			checkAttackOnNot(rage ? 40 : 60, gp.tileSize * 10, gp.tileSize * 5);
		}

		// MEKANIK FASE 2
		if (rage) {
			// 1. Serangan Proyektil Energi Kutukan
			shootCounter++;
			if (shootCounter >= 140 && !attacking) {
				shootCounter = 0;
				if (getTileDistance(gp.player) <= 12) {
					shootCursedProjectile();
				}
			}

			// 2. Pemanggilan Minion Berkala
			summonCounter++;
			if (summonCounter >= 600) {
				summonCounter = 0;
				int activeMinions = countActiveMinions();
				if (activeMinions < 2) {
					summonMinions(1);
					gp.ui.addMessage("Goblin King memanggil bala bantuan!");
					gp.playSE(2);
				}
			}
		}
	}

	public void shootCursedProjectile() {
		int diffX = gp.player.worldX - worldX;
		int diffY = gp.player.worldY - worldY;
		String shootDir = direction;
		if (Math.abs(diffX) > Math.abs(diffY)) {
			shootDir = (diffX > 0) ? "right" : "left";
		} else {
			shootDir = (diffY > 0) ? "down" : "up";
		}

		OBJ_GreenProjectile p = new OBJ_GreenProjectile(gp);
		int spawnX = worldX + (gp.tileSize * 5 / 2);
		int spawnY = worldY + (gp.tileSize * 5 / 2);
		p.set(spawnX, spawnY, shootDir, true, this);

		for (int i = 0; i < gp.projectile[gp.currentMap].length; i++) {
			if (gp.projectile[gp.currentMap][i] == null) {
				gp.projectile[gp.currentMap][i] = p;
				p.projectileIndex = i;
				break;
			}
		}
		gp.playSE(8);
	}

	public void summonMinions(int count) {
		if (gp.monster == null || gp.currentMap >= gp.monster.length || gp.monster[gp.currentMap] == null) {
			return;
		}

		for (int c = 0; c < count; c++) {
			for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
				if (gp.monster[gp.currentMap][i] == null) {
					Entity minion;
					if (summonToggle % 2 == 0) {
						minion = new MON_GreenSlime(gp);
						minion.worldX = worldX - gp.tileSize * 2;
						minion.worldY = worldY + gp.tileSize * 2;
					} else {
						minion = new MON_Zombie(gp);
						minion.worldX = worldX + gp.tileSize * 6;
						minion.worldY = worldY + gp.tileSize * 2;
					}
					minion.temp = true;
					gp.monster[gp.currentMap][i] = minion;
					summonToggle++;
					break;
				}
			}
		}
	}

	public int countActiveMinions() {
		if (gp.monster == null || gp.currentMap >= gp.monster.length || gp.monster[gp.currentMap] == null) {
			return 0;
		}
		int count = 0;
		for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
			Entity m = gp.monster[gp.currentMap][i];
			if (m != null && m != this && m.alive) {
				count++;
			}
		}
		return count;
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
		// Akhiri pertarungan boss & buka pintu arena yang terkunci
		gp.bossBattleOn = false;
		gp.removeTempEntity();
		gp.playAreaMusic();
		
		// Drop wajib Relik Lumina
		dropItems(new OBJ_Relic(gp));
		
		// Bonus drops
		dropItems(new OBJ_Coin_Bronze(gp));
		dropItems(new OBJ_Heart(gp));
		
		gp.ui.addMessage("Goblin King dikalahkan! Ambil Relik Lumina!");
		gp.qManager.showBanner("BOS DIKALAHKAN!", "Ambil Relik Lumina yang terjatuh!");
		gp.playSE(2);
	}
	
	public void drawAttackArea(Graphics2D g2) {
	    
	    // Posisi layar berdasarkan world coordinates
	    int screenX = worldX - gp.player.worldX + gp.player.screenX;
	    int screenY = worldY - gp.player.worldY + gp.player.screenY;

	    // Tentukan titik mulai gambar attackArea
	    int areaX = screenX;
	    int areaY = screenY;
	    
	    // Ukuran tubuh monster (5 tile)
	    int monsterSize = gp.tileSize * 5;

	    // Hitung posisi kotak serangan di depan tubuh monster
	    switch(direction) {
	        case "up": 
	            areaY -= attackArea.height; 
	            // Opsional: geser ke tengah tubuh
	            areaX += (monsterSize / 2) - (attackArea.width / 2);
	            break;
	        case "down": 
	            areaY += monsterSize; 
	            areaX += (monsterSize / 2) - (attackArea.width / 2);
	            break;
	        case "left": 
	            areaX -= attackArea.width; 
	            areaY += (monsterSize / 2) - (attackArea.height / 2);
	            break;
	        case "right": 
	            areaX += monsterSize; 
	            areaY += (monsterSize / 2) - (attackArea.height / 2);
	            break;
	    }

	    // Gambar visualisasi area (Merah transparan)
	    g2.setColor(new java.awt.Color(255, 0, 0, 100)); 
	    g2.fillRect(areaX, areaY, attackArea.width, attackArea.height);
	    
	    // Gambar outline
	    g2.setColor(java.awt.Color.RED);
	    g2.drawRect(areaX, areaY, attackArea.width, attackArea.height);
	}
}

