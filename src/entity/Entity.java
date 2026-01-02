package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

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
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collisionOn = false;
	String dialogues[] = new String[20];
	
	// State
	public int worldX, worldY;
	public String direction = "down";
	public int spriteNum = 1;
	int dialogueIndex = 0;
	public boolean collision = false;
	public boolean invincible = false;
	boolean attacking = false;
	public boolean alive = true;
	public boolean dying = false;
	boolean hpBarOn = false;
	public boolean onPath = false;
	public boolean knockBack = false;
	
	
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
	public Entity currentWeapon;
	public Entity currentShield;
	public Projectile projectile;
	
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
	public Entity currentLight;
	
	public Entity(GamePanel gp) {
		this.gp = gp;
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
		
		if(knockBack == true) {
			
			checkCollision();
			
			if(collisionOn == true) {
				knockBackCounter = 0;
				knockBack = false;
				speed = defaultSpeed;
			}
			else if(collisionOn == false) {
				switch(gp.player.direction) {
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
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
		   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
		   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
		   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
			
			switch(direction) {
			case "up":
				if (spriteNum == 1) {image = up1;}
				if (spriteNum == 2) {image = up2;}
				break;
			case "down":
				if (spriteNum == 1) {image = down1;}
				if (spriteNum == 2) {image = down2;}
				break;
			case "left":
				if (spriteNum == 1) {image = left1;}
				if (spriteNum == 2) {image = left2;}
				break;
			case "right":
				if (spriteNum == 1) {image = right1;}
				if (spriteNum == 2) {image = right2;}
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
			
			g2.drawImage(image, screenX, screenY, null);
			
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
	
	public void searchPath(int goalCol, int goalRow) {
	    
	    // Cek posisi saat ini
	    int currentCol = (worldX + solidArea.x) / gp.tileSize;
	    int currentRow = (worldY + solidArea.y) / gp.tileSize;
	    
	    // Jika path kosong, hitung path baru
	    if(gp.pFinder.pathList.isEmpty()) {
	        gp.pFinder.setNodes(currentCol, currentRow, goalCol, goalRow, this);
	        
	        if(!gp.pFinder.search()) {
	            onPath = false;
	            return;
	        }
	        
	        // Hapus node pertama jika NPC sudah di posisinya
	        if(!gp.pFinder.pathList.isEmpty()) {
	            Node firstNode = gp.pFinder.pathList.get(0);
	            if(firstNode.col == currentCol && firstNode.row == currentRow) {
	                gp.pFinder.pathList.remove(0);
	            }
	        }
	    }
	    
	    // Jika path masih ada
	    if(!gp.pFinder.pathList.isEmpty()) {
	        Node nextNode = gp.pFinder.pathList.get(0);
	        
	        // Simple direction berdasarkan grid
	        if(nextNode.row < currentRow) {
	            direction = "up";
	        } 
	        else if(nextNode.row > currentRow) {
	            direction = "down";
	        }
	        else if(nextNode.col < currentCol) {
	            direction = "left";
	        }
	        else if(nextNode.col > currentCol) {
	            direction = "right";
	        }
	        
	        // Cek jika sudah mendekati node
	        int nextCenterX = nextNode.col * gp.tileSize + gp.tileSize/2;
	        int nextCenterY = nextNode.row * gp.tileSize + gp.tileSize/2;
	        int currentCenterX = worldX + solidArea.x + solidArea.width/2;
	        int currentCenterY = worldY + solidArea.y + solidArea.height/2;
	        
	        // Jika sudah dekat, hapus node
	        if(Math.abs(currentCenterX - nextCenterX) <= speed * 2 && 
	           Math.abs(currentCenterY - nextCenterY) <= speed * 2) {
	            gp.pFinder.pathList.remove(0);
	            
	            // Cek jika sudah mencapai tujuan
	            if(nextNode.col == goalCol && nextNode.row == goalRow) {
	                onPath = false;
	                gp.pFinder.pathList.clear();
	            }
	        }
	    } else {
	        onPath = false;
	    }
	}
//    public int getDetected(Entity user, Entity target[][], String targetName)
//    {
//        int index = 999;
//
//        //Check the surrounding object
//        int nextWorldX = user.getLeftX();
//        int nextWorldY = user.getTopY();
//
//        switch (user.direction)
//        {
//            case "up" : nextWorldY = user.getTopY() - gp.player.speed; break;
//            case "down": nextWorldY = user.getBottomY() + gp.player.speed; break;
//            case "left": nextWorldX = user.getLeftX() - gp.player.speed; break;
//            case "right": nextWorldX = user.getRightX() + gp.player.speed; break;
//        }
//        int col = nextWorldX/gp.tileSize;
//        int row = nextWorldY/gp.tileSize;
//
//        for(int i = 0; i < target[1].length; i++)
//        {
//            if(target[gp.currentMap][i] != null)
//            {
//                if (target[gp.currentMap][i].getCol() == col                                //checking if player 1 tile away from target (key etc.) (must be same direction)
//                        && target[gp.currentMap][i].getRow() == row
//                            && target[gp.currentMap][i].name.equals(targetName))
//                {
//                    index = i;
//                    break;
//                }
//            }
//
//        }
//        return  index;
//    }
}
