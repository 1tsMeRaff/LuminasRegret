package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Axe;
import object.OBJ_Key;
import object.OBJ_Shield_Wood;
import object.OBJ_Slash;
import object.OBJ_Sword_Standard;

public class Player extends Entity {
    
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCanceled = false;
    
    public Player(GamePanel gp, KeyHandler keyH) {
        
        super(gp);
        
        this.keyH = keyH;
        
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);
        
        // Solid Area
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 28;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 16;
        solidArea.height = 20;
        
        // Attack Area
//        attackArea.width = 36;
//        attackArea.height = 36;
        
        
        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }
    
    public void setDefaultValues() {
        
        worldX = gp.tileSize * 25;
        worldY = gp.tileSize * 25;
        defaultSpeed = 4;
        speed = defaultSpeed;
        direction = "down";
        
     // PLAYER STATUS
        level = 1;
        maxLife = 6;
        life = maxLife;
        maxMana = 4;
        mana = maxMana;
        strength = 1;
        dexterity = 1;
        exp = 0;
        nextLevelExp = 5;
        coin = 500;
        currentWeapon = new OBJ_Sword_Standard(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        projectile = new OBJ_Slash(gp);
        attack = getAttack(); // The total attack value is decided by strength and weapon
        defense = getDefense(); // The total defense value is decided by dexterity and shield
    }
    public void setDefaultPositions() {
    	
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 23;
        direction = "down";
    }
    public void restoreLifeAndMana() {
    	
    	life = maxLife;
    	mana = maxMana;
    	invincible = false;
    }

    public void setItems() {
    	
    	inventory.clear();
    	inventory.add(currentWeapon);
    	inventory.add(currentShield);
    	inventory.add(new OBJ_Axe(gp));
    	inventory.add(new OBJ_Key(gp));
    	inventory.add(new OBJ_Key(gp));
    }
    public int getAttack() {
    	attackArea = currentWeapon.attackArea;
    	return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense() {
        return defense = dexterity * currentShield.defenseValue;
    }
    
    public void getPlayerImage() {
        
        up1 = setup("/player/top1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/top2", gp.tileSize, gp.tileSize);
        down1 = setup("/player/bot1", gp.tileSize, gp.tileSize); 
        down2 = setup("/player/bot2", gp.tileSize, gp.tileSize);
        left1 = setup("/player/left1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/left2", gp.tileSize, gp.tileSize);
        right1 = setup("/player/right1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/right2", gp.tileSize, gp.tileSize);
    }
    public void getPlayerAttackImage() {
    	
    	if(currentWeapon.type == type_sword) {
    		attackUp1 = setup("/player/attup1", gp.tileSize, gp.tileSize);
        	attackUp2 = setup("/player/attup2", gp.tileSize, gp.tileSize);
        	attackUp3 = setup("/player/attup3", gp.tileSize, gp.tileSize);
        	
        	attackDown1 = setup("/player/attdown1", gp.tileSize, gp.tileSize);
        	attackDown2 = setup("/player/attdown2", gp.tileSize, gp.tileSize);
        	attackDown3 = setup("/player/attdown3", gp.tileSize, gp.tileSize);
        	
        	attackLeft1 = setup("/player/attleft1", gp.tileSize, gp.tileSize);
        	attackLeft2 = setup("/player/attleft2", gp.tileSize, gp.tileSize);
        	attackLeft3 = setup("/player/attleft3", gp.tileSize, gp.tileSize);
        	
        	attackRight1 = setup("/player/attright1", gp.tileSize, gp.tileSize);
        	attackRight2 = setup("/player/attright2", gp.tileSize, gp.tileSize);
        	attackRight3 = setup("/player/attright3", gp.tileSize, gp.tileSize);
    	}
    	
    	if(currentWeapon.type == type_axe	) {
    		attackUp1 = setup("/player/attup1", gp.tileSize, gp.tileSize);
        	attackUp2 = setup("/player/attup2", gp.tileSize, gp.tileSize);
        	attackUp3 = setup("/player/attup3", gp.tileSize, gp.tileSize);
        	
        	attackDown1 = setup("/player/attdown1", gp.tileSize, gp.tileSize);
        	attackDown2 = setup("/player/attdown2", gp.tileSize, gp.tileSize);
        	attackDown3 = setup("/player/attdown3", gp.tileSize, gp.tileSize);
        	
        	attackLeft1 = setup("/player/attleft1", gp.tileSize, gp.tileSize);
        	attackLeft2 = setup("/player/attleft2", gp.tileSize, gp.tileSize);
        	attackLeft3 = setup("/player/attleft3", gp.tileSize, gp.tileSize);
        	
        	attackRight1 = setup("/player/attright1", gp.tileSize, gp.tileSize);
        	attackRight2 = setup("/player/attright2", gp.tileSize, gp.tileSize);
        	attackRight3 = setup("/player/attright3", gp.tileSize, gp.tileSize);
    	}
    }
    
    public void update() {
        
        // Simpan posisi sebelumnya untuk collision checking
        int tempWorldX = worldX;
        int tempWorldY = worldY;
        
        // Gunakan float/double untuk perhitungan akurat
        float moveX = 0;
        float moveY = 0;
        
        if(attacking == true) {
        	attacking();
        }
        
        else if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true ||
        		keyH.rightPressed == true || keyH.actionPressed == true) {
        	
        	// Cek input dan hitung vektor
            if (keyH.upPressed == true) {
                direction = "up";
                moveY -= speed;
            }
            if (keyH.downPressed == true) {
                direction = "down";
                moveY += speed;
            }
            if (keyH.leftPressed == true) {
                direction = "left";
                moveX -= speed;
            }
            if (keyH.rightPressed == true) {
                direction = "right";
                moveX += speed;
            }
            
            // Reset collision status
            collisionOn = false;
            // Check Tile Collision
            gp.cChecker.checkTile(this);
            
            // Check Object Collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);
            
            // Check NPC Collision
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);
            
            // Check Monster Collision
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);
            
            // CHECK INTERACTIVE TILE COLLISION
            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            
            // Check Event
            gp.eHandler.checkEvent();
            
            // Normalize diagonal movement (jika bergerak diagonal)
            if (moveX != 0 && moveY != 0) {
                float length = (float)Math.sqrt(moveX * moveX + moveY * moveY);
                float normalizedSpeed = speed; // Target speed
                
                // Skala vektor untuk mendapatkan kecepatan yang tepat
                moveX = (moveX / length) * normalizedSpeed;
                moveY = (moveY / length) * normalizedSpeed;
            }
            
            // Terapkan pergerakan (dengan rounding untuk posisi integer)
            worldX += Math.round(moveX);
            worldY += Math.round(moveY);
            
            // Jika ada collision, kembalikan ke posisi sebelumnya
            if (collisionOn == true && keyH.actionPressed == false) {
            	
            	switch(direction) {
            	case "up":
            		worldY -= speed;
            		break;
            	case "down":
            		worldY += speed;
            		break;
            	case "left":
            		worldX -= speed;
            		break;
            	case "right":
            		worldX += speed;
            		break;
            	}
                worldX = tempWorldX;
                worldY = tempWorldY;
            }
            
            if(keyH.actionPressed == true && attackCanceled == false) {
            	gp.playSE(7);
            	attacking = true;
            	spriteCounter = 0;
            }
            
            attackCanceled = false;
            
            // Sprite Animation Logic
                spriteCounter++;
                if (spriteCounter > 12) {
                	if(spriteNum == 1) {
                		spriteNum = 2;
                	}
                	else if(spriteNum == 2) {
                		spriteNum = 1;
                	}
                    spriteCounter = 0;
                }
        }
        
        if(gp.keyH.rangeKeyPressed == true && projectile.alive == false 
        	    && rangeAvailableCounter == 30 && projectile.haveResource(this) == true) {
        	    
        	    // PERBAIKAN: Pastikan ini memanggil set() yang sudah diperbaiki
        	    projectile.set(worldX, worldY, direction, true, this);
        	    
        	    projectile.substractResource(this);
        	    
        	    // Get Vacancy
        	    for(int i = 0; i < gp.projectile[gp.currentMap].length; i++) {
        	    	if(gp.projectile[gp.currentMap][i] == null) {
        	    		gp.projectile[gp.currentMap][i] = projectile;
        	    		break;
        	    	}
        	    }
        	    
        	    rangeAvailableCounter = 0;
        	}
        
        
        // Invincible Counter
        if(invincible == true) {
        	invincibleCounter++;
        	if(invincibleCounter > 60) {
        		invincible = false;
        		invincibleCounter = 0;
        	}
        }
	        if(rangeAvailableCounter < 30) {
	        	rangeAvailableCounter++;
	        }
	        if (life > maxLife) {
			    life = maxLife;
			}
	        if (mana > maxMana) {
			    mana = maxMana;
			}
	        if (life <= 0) {
	        	gp.gameState = gp.gameOverState;
	        	gp.stopMusic();
	        	gp.playSE(10);
	        }
    }
    public void attacking() {
        spriteCounter++;

        if(spriteCounter <= 5) {
            spriteNum = 1;
        }
        if(spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;
            
            // Current Player Position
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;
            
            // Adjust Player Position For attackArea
            switch(direction) {
                case "up": worldY -= attackArea.height; break;
                case "down": worldY += attackArea.height; break;
                case "left": worldX -= attackArea.width; break;
                case "right": worldX += attackArea.width; break;
            }
            
            // AttackArea becomes SolidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            
            // Check Monster collision
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex, attack, currentWeapon.knockBackPower);
            
            // Check Interactive Tile collision (PERBAIKAN DI SINI)
            int iTileIndex = gp.cChecker.checkInteractiveTile(this);
            if (iTileIndex != 999) {
                damageInteractiveTile(iTileIndex);
            }
            
            int projectileIndex = gp.cChecker.checkEntity(this, gp.projectile);
            damageProjectile(projectileIndex);
            
            // Restore values
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        
        if(spriteCounter > 20 && spriteCounter <= 35) {
            spriteNum = 3;
        }
        
        if(spriteCounter > 35) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }
    
    public void pickUpObject(int i) {
    	
        if (i != 999) {
        	
        	//Pickup Only Items
        	if(gp.obj[gp.currentMap][i].type == type_pickupOnly) { //FIXED
        		
        		gp.obj[gp.currentMap][i].use(this); //FIXED
        		gp.obj[gp.currentMap][i] = null; //FIXED
        	}
        	
        	//Inventory Items
        	else {
        		String text;
            	
            	if(inventory.size() != maxInventorySize) {
            		
            		inventory.add(gp.obj[gp.currentMap][i]); //FIXED
            		gp.playSE(1);
            		text = "Mendapat" + gp.obj[gp.currentMap][i].name + "!"; //FIXED
            	}
            	else {
            		text = "Inventory penuh!!!";
            	}
            	gp.ui.addMessage(text);
            	gp.obj[gp.currentMap][i] = null; //FIXED DONT'S FORGET THIS
        	}
        	
        }
    }
    
    
    public void interactNPC(int i) {
    	
    	if(gp.keyH.actionPressed == true) {
    		if (i != 999) {
    			attackCanceled = true;
    			gp.gameState = gp.dialogueState;
                gp.npc[gp.currentMap][i].speak(); //FIXED
        		gp.keyH.actionPressed = false;
            }
    	}
    }
    
    public void contactMonster(int i) {
    	
    	if(i != 999) {
    		
    		if(invincible == false && gp.monster[gp.currentMap][i].dying == false) { //FIXED
    			gp.playSE(6);
    			
    			int damage = gp.monster[gp.currentMap][i].attack - defense; //FIXED
    			if(damage < 0) {
    				damage = 0;
    			}
    			life -= damage;
    			invincible = true;
    		}
    	}
    }
    
    public void damageMonster(int i, int attack, int knockBackPower) {
    	
    	if(i != 999) {
    		
    		if(gp.monster[gp.currentMap][i].invincible == false) { //FIXED
    			
    			gp.playSE(5);
    			if(knockBackPower > 0) {
    	   			knockBack(gp.monster[gp.currentMap][i], knockBackPower);
    			}
    			
    			int damage = attack - gp.monster[gp.currentMap][i].defense; //FIXED
    			if(damage < 0) {
    				damage = 0;
    			}
    			gp.monster[gp.currentMap][i].life -= damage; //FIXED
    			gp.ui.addMessage(damage + "damage!");
    			gp.monster[gp.currentMap][i].invincible = true; //FIXED
    			gp.monster[gp.currentMap][i].damageReaction(); //FIXED
    			
    			if(gp.monster[gp.currentMap][i].life <= 0) { //FIXED
    				gp.monster[gp.currentMap][i].dying = true; //FIXED
    				gp.ui.addMessage("Killed the " + gp.monster[gp.currentMap][i].name + "!"); //FIXED
    				gp.ui.addMessage("Exp + " + gp.monster[gp.currentMap][i].exp); //FIXED
    				exp += gp.monster[gp.currentMap][i].exp; //FIXED
    				checkLevelUp();
    			}
    		}
    	}
    }
    public void knockBack(Entity entity, int knockBackPower) {
    	
    	entity.direction = direction;
    	entity.speed += 10;
    	entity.knockBack = true;
    }
    public void damageInteractiveTile(int i) { //FIXED
        if (i != 999 && gp.iTile[gp.currentMap][i].destructible == true 
        	&& gp.iTile[gp.currentMap][i].isCorrectItem(this) == true) {
            
            Entity destroyedTile = gp.iTile[gp.currentMap][i];
            
            gp.iTile[gp.currentMap][i] = null;
            
            generateParticle(destroyedTile, destroyedTile);
        }
    }
    
    public void damageProjectile(int i) {
    	
    	if(i != 999) {
    		Entity Projectile = gp.projectile[gp.currentMap][i];
    		projectile.alive = false;
    		generateParticle(projectile, projectile);
    	}
    }

    public void checkLevelUp() {
    	
        if(exp >= nextLevelExp) {
        	
            level++; 
            nextLevelExp = nextLevelExp*2; 
            maxLife += 2; 
            life += 2;
            strength++; 
            dexterity++; 
            attack = getAttack(); 
            defense = getDefense();
            
            gp.playSE(8);
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You are level " + level + " now!\n	" 
            + "You feel stronger!";
        }
    }
    
    public void selectItem() {
    	int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);
    	
    	if(itemIndex < inventory.size()) {
    		
    		Entity selectedItem = inventory.get(itemIndex);
    		
    		if(selectedItem.type == type_sword || selectedItem.type == type_axe) {
    			
    			currentWeapon = selectedItem;
    			attack = getAttack();
    			getPlayerAttackImage();
    		}
    		if(selectedItem.type == type_shield) {
    			
    			currentShield = selectedItem;
    			defense = getDefense();
    		}
    		if(selectedItem.type == type_consumable) {
    			
    			selectedItem.use(this);
    			inventory.remove(itemIndex);
    		}
    	}
    }
    public void draw(Graphics2D g2) {
        
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;
        
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
        
        if(invincible == true) {
        	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        
        // Gambar player di tengah screen
        g2.drawImage(image, screenX, screenY, null);
        
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        
    }
}