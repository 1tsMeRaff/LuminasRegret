package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import ai.Node;
import main.GamePanel;
import main.UtilityTool;

public class Entity {
	
	protected GamePanel gp;
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public BufferedImage attackUp1, attackUp2, attackUp3, attackDown1,
						 attackDown2, attackDown3, attackLeft1, attackLeft2, 
						 attackLeft3, attackRight1, attackRight2, attackRight3;
	public BufferedImage image, image2, image3;
	public Rectangle solidArea = new Rectangle(8, 28, 16, 20);
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collisionOn = false;
	String dialogues[] = new String[20];
	public Entity attacker;
	
	// State
	public int worldX, worldY;
	public String direction = "down";
	public int spriteNum = 1;
	int dialogueIndex = 0;
	public boolean collision = false;
	public boolean invincible = false;
	public boolean attacking = false;
	public boolean alive = true;
	public boolean dying = false;
	boolean hpBarOn = false;
	public boolean onPath = false;
	public boolean knockBack = false;
	public String knockBackDirection;
	
	
	//Counter
	public int spriteCounter = 0;
	public int actionLockCounter = 0;
	public int invincibleCounter = 0;
	public int rangeAvailableCounter = 0;
	int dyingCounter = 0;
	int hpBarCounter = 0;
	int knockBackCounter = 0;
	
	// Character Status
	public String name;
	public int defaultSpeed;
	public int speed;
	public int maxLife;
	public int life;
	public int maxMana;
	public int mana;
	public int level;
	public int strength;
	public int dexterity;
	public int attack;
	public int defense;
	public int exp;
	public int nextLevelExp;
	public int coin;
	public int motion1_duration;
	public int motion2_duration;
	public Entity currentWeapon;
	public Entity currentShield;
	public Projectile projectile;
	public int projectileIndex = -1;
	
	public int aggroRange;
	
	// Item Attributes
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
	public int value;
	public int attackValue;
	public int defenseValue;
	public String description = "";
	public int useCost;
	public int price;
	public int knockBackPower = 0; 
	
	// Tipe Equipment
	public int type; // 0 = player, 1 = npc, 2 = monster
	public final int type_player = 0;
	public final int type_npc = 1;
	public final int type_monster = 2;
	public final int type_sword = 3;
	public final int type_axe = 4;
	public final int type_shield = 5;
	public final int type_consumable = 6;
	public final int type_pickupOnly = 7;
	public final int type_obstacle = 8;
	public Entity currentLight;
	
	// Path Finder
	public ArrayList<Node> myPath = new ArrayList<>();
	public int goalCol;
	public int goalRow;
	public boolean hasGoal;
	
    // Constructor
    public Entity(GamePanel gp) {
        this.gp = gp;
    }
    
    public int getLeftX() {
    	return worldX + solidArea.x;
    }
    public int getRightX() {
    	return worldX + solidArea.x + solidArea.width;
    }
    public int getTopY() {
    	return worldY + solidArea.y;
    }
    public int getBottomY() {
    	return worldY + solidArea.y + solidArea.height;
    }
    public int getCol() {
    	return (worldX + solidArea.x)/gp.tileSize;
    }
    public int getRow() {
    	return (worldY + solidArea.y)/gp.tileSize;
    }
    
    // Check distance between entities
    public int getDistanceTo(Entity other) {
        int centerX1 = worldX + solidArea.x + solidArea.width/2;
        int centerY1 = worldY + solidArea.y + solidArea.height/2;
        int centerX2 = other.worldX + other.solidArea.x + other.solidArea.width/2;
        int centerY2 = other.worldY + other.solidArea.y + other.solidArea.height/2;
        
        return (int)Math.sqrt(
            Math.pow(centerX1 - centerX2, 2) + 
            Math.pow(centerY1 - centerY2, 2)
        );
    }

	public void setAction() {}
	public void damageReaction() {}
	public void speak() {
		
		if(dialogues[dialogueIndex] == null) {
			dialogueIndex = 0;
		}
		gp.ui.currentDialogue = dialogues[dialogueIndex];
		dialogueIndex++;
		
		switch(gp.player.direction) {
		case "up":
			direction = "down";
			break;
		case "down":
			direction = "up";
			break;
		case "left":
			direction = "right";
			break;
		case "right":
			direction = "left";
			break;
		}
		
	}
	public void interact() {
		
	}
	
	public void use(Entity entity) {
		
	}
	
	public void checkDrop() {
		
	}
	
	public void dropItems(Entity droppedItem) {
		
		for(int i = 0; i < gp.obj[1].length; i++) {
			if(gp.obj[gp.currentMap][i] == null) {
				gp.obj[gp.currentMap][i] = droppedItem;
				gp.obj[gp.currentMap][i].worldX = worldX; //the dead monsters worldX
				gp.obj[gp.currentMap][i].worldY = worldY;
				break;
			}
		}
	}
	
	public Color getParticleColor() {
    	Color color = null;
    	return color;
    }
    
    public int getParticleSize() {
    	int size = 0; //6 pixels
    	return size;
    }
    
    public int getParticleSpeed() {
    	int speed = 0;
    	return speed;
    }
    
    public int getParticleMaxLife() {
    	int maxLife = 0;
    	return maxLife;
    }
    
    public void generateParticle(Entity generator, Entity target) {
    	
    	Color color = generator.getParticleColor();
    	int size = generator.getParticleSize();
    	int speed = generator.getParticleSpeed();
    	int maxLife = generator.getParticleMaxLife();
    	
    	Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2, -1);
    	Particle p2 = new Particle(gp, target, color, size, speed, maxLife, 2, -1);
    	Particle p3 = new Particle(gp, target, color, size, speed, maxLife, -2, 1);
    	Particle p4 = new Particle(gp, target, color, size, speed, maxLife, 2, 1);
    	gp.particleList.add(p1);
    	gp.particleList.add(p2);
    	gp.particleList.add(p3);
    	gp.particleList.add(p4);
    }
	
    public void checkCollision() {
    	
    	collisionOn = false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkEntity(this, gp.npc);
		gp.cChecker.checkEntity(this, gp.monster);
		boolean contactPlayer = gp.cChecker.checkPlayer(this);
		
		
		if(this.type == type_monster && contactPlayer == true) {
			
			damagePlayer(attack);
		}

    }
	public void update() {
		
		collisionOn = false;
		
		if(knockBack) {
			
			checkCollision();
			
			if(collisionOn == true) {
				knockBackCounter = 0;
				knockBack = false;
				speed = defaultSpeed;
			}
			else if(collisionOn == false) {
				switch(knockBackDirection) {
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
			}
			
			knockBackCounter++;
			if(knockBackCounter == 10) {
				knockBackCounter = 0;
				knockBack = false;
				speed = defaultSpeed;
			}
		}
		else if(attacking) {
			attacking();
		}
		else {
			
			setAction();
			checkCollision();
					
			// IF COLLISION IS FALSE, PLAYER CAN MOVE
			if (collisionOn == false) {
			    switch (direction) {
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
			}
			
			// Sprite animation logic
			spriteCounter++;
			if (spriteCounter > 12) {
			    if (spriteNum == 1) {
			        spriteNum = 2;
			    } else if (spriteNum == 2) {
			        spriteNum = 1;
			    }
			    spriteCounter = 0;
			}
		}
		
		// Invincible Counter
        if(invincible == true) {
        	invincibleCounter++;
        	if(invincibleCounter > 40) {
        		invincible = false;
        		invincibleCounter = 0;
        	}
        }
        if(rangeAvailableCounter < 30) {
        	rangeAvailableCounter++;
        }
	}
	
	public void attacking() {
        spriteCounter++;

        if(spriteCounter <= motion1_duration) {
            spriteNum = 1;
        }
        if(spriteCounter > motion1_duration && spriteCounter <= motion2_duration) {
            spriteNum = 2;
            
            // Simpan posisi & solidArea asli
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;
            
            switch(direction) {
                case "up":    worldY -= attackArea.height; break;
                case "down":  worldY += attackArea.height; break;
                case "left":  worldX -= attackArea.width;  break;
                case "right": worldX += attackArea.width;  break;
            }
            
            // Set solidArea menjadi ukuran senjata
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            
            if(type == type_monster) {
            	if(gp.cChecker.checkPlayer(this)) {
            		damagePlayer(attack);
            	}
            }
            else {
            	// Check Collision dengan senjata
                int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
                gp.player.damageMonster(monsterIndex, this, attack, currentWeapon.knockBackPower);
                
                int iTileIndex = gp.cChecker.checkInteractiveTile(this);
                gp.player.damageInteractiveTile(iTileIndex);
                
                int projectileIndex = gp.cChecker.checkEntity(this, gp.projectile);
                gp.player.damageProjectile(projectileIndex);
            }
            
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        
        if(spriteCounter > motion2_duration) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }
	
	public void damagePlayer(int attack) {
		if(gp.player.invincible == false) {
			gp.playSE(6);
			
			int damage = attack - gp.player.defense;
			if(damage < 0) {
				damage = 0;
			}
			gp.player.life -= damage;
			gp.player.invincible = true;
		}
	}
	
    public void knockBack(Entity target, Entity attacker, int knockBackPower) {
    	
    	this.attacker = attacker;
    	target.knockBackDirection = attacker.direction;
    	target.speed += 10;
    	target.knockBack = true;
    }
    
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
		   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
		   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
		   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
			
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
			
			//Monster Hp Bar
			if(type == 2 && hpBarOn == true) {

			    double oneScale = (double)gp.tileSize/maxLife;
			    double hpBarValue = oneScale*life;

			    g2.setColor(new Color(35,35,35));
			    g2.fillRect(screenX-1, screenY-16, gp.tileSize+2, 12);

			    g2.setColor(new Color(255,0,30));
			    g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);

			    hpBarCounter++;

			    if(hpBarCounter > 600) {
			        hpBarCounter = 0;
			        hpBarOn = false;
			    }
			}
			
			if(invincible == true) {
				hpBarOn = true;
				hpBarCounter = 0;
				changeAlpha(g2, 0.4F);
	        }
			if(dying == true) {
				dyingAnimation(g2);
			}
			
			g2.drawImage(image, tempScreenX, tempScreenY, null);
			
			changeAlpha(g2, 1F);
		}
	}
	public void dyingAnimation(Graphics2D g2) {
		
		dyingCounter++;
		
		int i = 5;
		
		if(dyingCounter <= i) {changeAlpha(g2, 0f);}
		if(dyingCounter <= i && dyingCounter <= i*2) {changeAlpha(g2, 1f);}
		if(dyingCounter <= i*2 && dyingCounter <= i*3) {changeAlpha(g2, 0f);}
		if(dyingCounter <= i*3 && dyingCounter <= i*4) {changeAlpha(g2, 1f);}
		if(dyingCounter <= i*4 && dyingCounter <= i*5) {changeAlpha(g2, 0f);}
		if(dyingCounter <= i*5 && dyingCounter <= i*6) {changeAlpha(g2, 1f);}
		if(dyingCounter <= i*6 && dyingCounter <= i*7) {changeAlpha(g2, 0f);}
		if(dyingCounter <= i*7 && dyingCounter <= i*8) {changeAlpha(g2, 0f);}
		if(dyingCounter > i*8) {
			alive = false;
		}
	}
	
	public void changeAlpha(Graphics2D g2, float alphaValue) {
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
	}
	public BufferedImage setup(String imagePath, int widht, int height) {
		
	    UtilityTool uTool = new UtilityTool();
	    BufferedImage image = null;
	    
	    try {
	        image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
	        image = uTool.scaleImage(image, widht, height);
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return image;
	}
	
	public void startPathFinding(int goalCol, int goalRow) {
	    
	    this.onPath = true;
	    this.goalCol = goalCol;
	    this.goalRow = goalRow;
	    
	    // Clear path lama
	    if (gp.pFinder != null) {
	        gp.pFinder.pathList.clear();
	    }
	}

	public void stopPathFinding() {
	    System.out.println("🛑 stopPathFinding() called");
	    this.onPath = false;
	    this.goalCol = -1;
	    this.goalRow = -1;
	    
	    if (gp.pFinder != null) {
	        gp.pFinder.pathList.clear();
	    }
	}
	
    public void followPath() {
        System.out.println("--- followPath() called ---");
        
        if (gp.pFinder.pathList.isEmpty()) {
            System.out.println("PathList is empty, waiting...");
            direction = "down";
            return;
        }
        
        Node nextNode = gp.pFinder.pathList.get(0);
        System.out.println("Next node: " + nextNode.col + ", " + nextNode.row);
        
        int targetX = nextNode.col * gp.tileSize + gp.tileSize / 2;
        int targetY = nextNode.row * gp.tileSize + gp.tileSize / 2;
        
        int centerX = worldX + solidArea.x + solidArea.width / 2;
        int centerY = worldY + solidArea.y + solidArea.height / 2;
        
        int dx = targetX - centerX;
        int dy = targetY - centerY;
        
        System.out.println("Target: " + targetX + ", " + targetY);
        System.out.println("Center: " + centerX + ", " + centerY);
        System.out.println("Diff: " + dx + ", " + dy);
        
        int threshold = gp.tileSize / 4;
        
        if(Math.abs(dx) < threshold && Math.abs(dy) < threshold) {
            System.out.println("Reached node, removing from path");
            gp.pFinder.pathList.remove(0);
            
            if(gp.pFinder.pathList.isEmpty()) {
                System.out.println("Path finished!");
                return;
            }
            
            // Update to next node
            nextNode = gp.pFinder.pathList.get(0);
            System.out.println("Moving to next node: " + nextNode.col + ", " + nextNode.row);
        }
        
        // Tentukan arah
        if(Math.abs(dx) > Math.abs(dy)) {
            direction = (dx > 0) ? "right" : "left";
        } else {
            direction = (dy > 0) ? "down" : "up";
        }
        
        System.out.println("Selected direction: " + direction);
    }
    
    // PATH FOLLOWING
    public void followImprovedPath() {
    	
    	if (myPath.isEmpty()) { 
            // Jika path habis, stop
    		onPath = false;
            return; 
            }

         Node nextNode = myPath.get(0); // Ambil dari myPath
         int nextCol = nextNode.col;
         int nextRow = nextNode.row;

        // 3. Hitung posisi target
        int targetCenterX = getTileCenterX(nextCol);
        int targetCenterY = getTileCenterY(nextRow);
        int npcCenterX = worldX + solidArea.x + solidArea.width/2;
        int npcCenterY = worldY + solidArea.y + solidArea.height/2;

        int dx = targetCenterX - npcCenterX;
        int dy = targetCenterY - npcCenterY;

        // 4. Cek apakah sudah sampai di target (Node saat ini)
        int threshold = gp.tileSize / 8;
        if (Math.abs(dx) < threshold && Math.abs(dy) < threshold) {
            myPath.remove(0); // Hapus dari myPath
            // alignToTile(nextCol, nextRow);
            return;
        }

        // 5. Tentukan arah
        determineDirection(dx, dy);

        // 6. Cek Collision
        collisionOn = false; // Reset dulu
        checkCollision();
        
        switch (direction) {
            case "up": worldY -= speed; break;
            case "down": worldY += speed; break;
            case "left": worldX -= speed; break;
            case "right": worldX += speed; break;
        }
    }
    
    public void alignToTile(int tileX, int tileY) {
        int targetX = getTileCenterX(tileX) - solidArea.width/2 - solidArea.x;
        int targetY = getTileCenterY(tileY) - solidArea.height/2 - solidArea.y;
        
        worldX = targetX;
        worldY = targetY;
    }
    
    public void determineDirection(int dx, int dy) {
    	
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                direction = "right";
                System.out.println("→ Moving RIGHT to reach center");
            } else {
                direction = "left";
                System.out.println("← Moving LEFT to reach center");
            }
        } else {
            if (dy > 0) {
                direction = "down";
                System.out.println("↓ Moving DOWN to reach center");
            } else {
                direction = "up";
                System.out.println("↑ Moving UP to reach center");
            }
        }
    }
    
    public void checkAttackOnNot(int rate, int straight, int horizontal) {
        boolean targetInRange = false;
        int xDis = getXdistance(gp.player);
        int yDis = getYdistance(gp.player);

        switch (direction) {
            case "up":
                if (gp.player.worldY < worldY && yDis < straight && xDis < horizontal) {
                	targetInRange = true;
                }
                break;
            case "down":
                if (gp.player.worldY > worldY && yDis < straight && xDis < horizontal) {
                    targetInRange = true;
                }
                break;
            case "left":
                if (gp.player.worldX < worldX && xDis < straight && yDis < horizontal) {
                    targetInRange = true;
                }
                break;
            case "right":
                if (gp.player.worldX > worldX && xDis < straight && yDis < horizontal) {
                    targetInRange = true;
                }
                break;
        }
        
        if(targetInRange) {
        	int i = new Random().nextInt(rate);
        	if(i == 0) {
        		attacking = true;
        		spriteNum = 1;
        		spriteCounter = 0;
        		rangeAvailableCounter = 0;
        	}
        }
    }
    
    public void checkShootOrNot(int rate, int shotInterval) {
        int i = new Random().nextInt(rate);
        if (i == 0 && projectile.alive == false && rangeAvailableCounter == shotInterval) {
            projectile.set(worldX, worldY, direction, true, this);

            // CHECK VACANCY
            for (int ii = 0; ii < gp.projectile[i].length; ii++) {
                if (gp.projectile[gp.currentMap][ii] == null) {
                    gp.projectile[gp.currentMap][ii] = projectile;
                    break;
                }
            }
            rangeAvailableCounter = 0;
        }
    }
    
    public void checkStartChasingOrNot(Entity target, int distance, int rate) {
    	
    	if(getTileDistance(target) < distance) {
    		int i = new Random().nextInt(rate);
    		if(i == 0) {
    			onPath = true;
    		}
    	}
    }
    
    public void checkStopChasingOrNot(Entity target, int distance, int rate) {
    	
    	if(getTileDistance(target) > distance) {
    		int i = new Random().nextInt(rate);
    		if(i == 0) {
    			onPath = false;
    		}
    	}
    }
    
    public int getXdistance(Entity target) {
    	int xDistance = Math.abs(worldX - target.worldX);
    	return xDistance;
    }
    
    public int getYdistance(Entity target) {
    	int yDistance = Math.abs(worldY - target.worldY);
    	return yDistance;
    }
    
    public int getTileDistance(Entity target) {
    	int tileDistance = (getXdistance(target) + getYdistance(target)) / gp.tileSize;
    	return tileDistance;
    }
    
    public int getGoalCol(Entity target) {
    	int goalCol = (target.worldX + target.solidArea.x) / gp.tileSize;
    	return goalCol;
    }
    
    public int getGoalRow(Entity target) {
    	int goalRow = (target.worldY + target.solidArea.y) / gp.tileSize;
    	return goalRow;
    }
    
    public void randomMovement() {
        actionLockCounter++;
        if (actionLockCounter >= 120) {
            Random random = new Random();
            int i = random.nextInt(100);
            
            if (i < 25) direction = "up";
            else if (i < 50) direction = "down";
            else if (i < 75) direction = "left";
            else direction = "right";
            
            actionLockCounter = 0;
        }
    }
    
    public void moveToTileCenter(int tileX, int tileY) {
        // Bergerak ke tengah tile saat sudah di tile yang benar
        int targetCenterX = getTileCenterX(tileX);
        int targetCenterY = getTileCenterY(tileY);
        
        int npcCenterX = worldX + solidArea.x + solidArea.width/2;
        int npcCenterY = worldY + solidArea.y + solidArea.height/2;
        
        int dx = targetCenterX - npcCenterX;
        int dy = targetCenterY - npcCenterY;
        
        if (Math.abs(dx) > Math.abs(dy)) {
            direction = (dx > 0) ? "right" : "left";
        } else {
            direction = (dy > 0) ? "down" : "up";
        }
        
        System.out.println("Centering to tile (" + tileX + ", " + tileY + ")");
    }
    
    // PERHITUNGAN POSISI TILE
    public int getCurrentTileX() {
        // Hitung tile berdasarkan worldX (bukan dengan solidArea offset)
        return (worldX + solidArea.x + solidArea.width/2) / gp.tileSize;
    }
    
    public int getCurrentTileY() {
        return (worldY + solidArea.y + solidArea.height/2) / gp.tileSize;
    }
    
    public int getTileCenterX(int tileX) {
        // Hitung posisi pixel tengah tile X
        return tileX * gp.tileSize + gp.tileSize/2;
    }
    
    public int getTileCenterY(int tileY) {
        // Hitung posisi pixel tengah tile Y
        return tileY * gp.tileSize + gp.tileSize/2;
    }
    
    public boolean isAtTileCenter() {
        // Cek apakah NPC sudah di tengah tile
        int centerX = worldX + solidArea.x + solidArea.width/2;
        int centerY = worldY + solidArea.y + solidArea.height/2;
        
        int tileX = centerX / gp.tileSize;
        int tileY = centerY / gp.tileSize;
        
        int tileCenterX = getTileCenterX(tileX);
        int tileCenterY = getTileCenterY(tileY);
        
        int dx = Math.abs(tileCenterX - centerX);
        int dy = Math.abs(tileCenterY - centerY);
        
        return dx < 2 && dy < 2; // Toleransi 2 pixel
    }
    
    public void alignToTileCenter() {
        // Paksa NPC ke tengah tile
        int currentCol = getCurrentTileX();
        int currentRow = getCurrentTileY();
        
        int targetX = getTileCenterX(currentCol) - solidArea.width/2 - solidArea.x;
        int targetY = getTileCenterY(currentRow) - solidArea.height/2 - solidArea.y;
        
        worldX = targetX;
        worldY = targetY;
        
        System.out.println("Aligned NPC to tile center: (" + currentCol + ", " + currentRow + ")");
    }
    
    public int getDetected(Entity user, Entity target[][], String targetName) {
    	
    	int index = 999;
    	
    	//cek objek sekitar
    	int nextWorldX = user.getLeftX();
    	int nextWorldY = user.getTopY();
    	
    	switch(user.direction) {
    	case "up": nextWorldY = user.getTopY()-1; break;
    	case "down": nextWorldY = user.getBottomY()+1; break;
    	case "left": nextWorldX = user.getLeftX()-1; break;
    	case "right": nextWorldX = user.getRightX()+1; break;
    	}
    	int col = nextWorldX/gp.tileSize;
    	int row = nextWorldY/gp.tileSize;
    	
    	for(int i = 0; i < target[1].length; i++) {
    		if(target[gp.currentMap][i] !=null) {
    			if(target[gp.currentMap][i].getCol() == col &&
    					target[gp.currentMap][i].getRow() == row &&
    					target[gp.currentMap][i].name.equals(targetName)) {
    				
    				index = i;
    				break;
    			}
    		}
    	}
    	return index;
    }
}


