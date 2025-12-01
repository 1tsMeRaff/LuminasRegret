package entity;

//import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

public class Player extends Entity {
	
	GamePanel gp;
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
	public int hasKey = 0;
	int standCounter = 0;
	boolean moving = false;
	int pixelCounter = 0;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		
		this.gp = gp;
		this.keyH = keyH;
		
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		solidArea = new Rectangle();
		solidArea.x = 1;
		solidArea.y = 1;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 46;
		solidArea.height = 46;
		
		setDefaultValues();
		getPlayerImage();
	}
	
	public void setDefaultValues() {
		
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 21;
		speed = 4;
		direction = "down";
	}
	
	public void getPlayerImage() {
		
		up1 = setup("upjalan1");
	    up2 = setup("upjalan2");
	    down1 = setup("bawahdiam"); 
	    down2 = setup("bawahdiam2");
	    left1 = setup("kirijalan");
	    left2 = setup("kirijalan");
		right1 = setup("kananjalan1");
		right2 = setup("kananjalan2");
	}
	
	public BufferedImage setup(String imageName) {
		
	    UtilityTool uTool = new UtilityTool();
	    BufferedImage image = null;
	    
	    try {
	        image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
	        image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return image;
	}
	
	public void update() {
	    
	    // Kita hanya menerima input jika karakter sedang TIDAK bergerak
	    if (moving == false) {
	        
	        if (keyH.upPressed == true || keyH.downPressed == true || 
	            keyH.leftPressed == true || keyH.rightPressed == true) {
	            
	            if (keyH.upPressed == true) {
	                direction = "up";
	            } else if (keyH.downPressed == true) {
	                direction = "down";
	            } else if (keyH.leftPressed == true) {
	                direction = "left";
	            } else if (keyH.rightPressed == true) {
	                direction = "right";
	            }
	            
	            if (moving == true) {
	                
	                // Gerakkan karakter berdasarkan arah
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
	                
	                // Tambahkan counter pixel yang sudah ditempuh
	                pixelCounter += speed;
	                
	                // Jika sudah bergerak sejauh 48 pixel (1 tile) ATAU LEBIH
	                if (pixelCounter >= gp.tileSize) { // Gunakan >= (lebih besar dari atau sama dengan)
	                    
	                    // 1. Hitung sisa pergerakan (kelebihan piksel)
	                    int remainder = pixelCounter - gp.tileSize;
	                    
	                    // 2. Terapkan sisa pergerakan (untuk memastikan karakter berhenti tepat di grid berikutnya)
	                    switch(direction) {
	                        case "up":
	                            worldY += remainder; // Mundurkan Y sebeser sisanya
	                            break;
	                        case "down":
	                            worldY -= remainder; // Mundurkan Y sebeser sisanya
	                            break;
	                        case "left":
	                            worldX += remainder; // Mundurkan X sebeser sisanya
	                            break;
	                        case "right":
	                            worldX -= remainder; // Mundurkan X sebeser sisanya
	                            break;
	                    }

	                    moving = false;
	                    pixelCounter = 0;
	                }
	            }
	            
	          //Check Tile Collision
				collisionOn = false;
				gp.cChecker.checkTile(this);
				
				//Check Object Collision
				int objIndex = gp.cChecker.checkObject(this, true);
				pickUpObject(objIndex);
	            
	            // Jika tidak menabrak, mulai pergerakan
	            if (collisionOn == false) {
	                moving = true;
	            }
	        }
	    }
	    
	    // Logika Pergerakan Grid
	    if (moving == true) {
	        
	        // Gerakkan karakter berdasarkan arah
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
	        
	        // Tambahkan counter pixel yang sudah ditempuh
	        pixelCounter += speed;
	        
	        // Jika sudah bergerak sejauh 48 pixel (1 tile), berhenti
	        if (pixelCounter >= 48) {
	            moving = false;
	            pixelCounter = 0;
	        }
	    }
	    
	    // Logika Sprite Animation (Hanya berjalan jika tombol ditekan / sedang bergerak)
	    if (keyH.upPressed == true || keyH.downPressed == true || 
	        keyH.leftPressed == true || keyH.rightPressed == true || moving == true) {
	            
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
	}
	public void pickUpObject(int i) {
		
		if(i != 999) {
			
			String objectName = gp.obj[i].name;
			
			switch(objectName) {
		    case "Key":
		    	gp.playSE(1);
		        hasKey++;
		        gp.obj[i] = null;
		        gp.ui.showMessage("You got a key!");
		        break;
		    case "Door":
		    	if (hasKey > 0) {
                    gp.playSE(3);
                    gp.obj[i] = null;
                    hasKey--;
                    gp.ui.showMessage("You opened the door!");
                } else {
                    gp.ui.showMessage("You need a key!");
                }
		        break;
		    case "Boots":
		    	gp.playSE(1);
		    	speed += 1;
		    	gp.obj[i] = null;
		    	gp.ui.showMessage("Speed up!");
		    	break;
		    case "Chest":
                gp.ui.gameFinished = true;
                gp.stopMusic();
                gp.playSE(4); 
                break;
			}
		}
		
	}

	public void draw(Graphics2D g2) {
		
//		g2.setColor(Color.white);
//		g2.fillRect(x, y, gp.tileSize, gp.tileSize);
		
		BufferedImage image = null;
		
		switch(direction) {
		case "up":
			if (spriteNum == 1) {
				image = up1;
			}
			if (spriteNum == 2) {
				image = up2;
			}
			break;
		case "down":
			if (spriteNum == 1) {
				image = down1;
			}
			if (spriteNum == 2) {
				image = down2;
			}
			break;
		case "left":
			if (spriteNum == 1) {
				image = left1;
			}
			if (spriteNum == 2) {
				image = left2;
			}
			break;
		case "right":
			if (spriteNum == 1) {
				image = right1;
			}
			if (spriteNum == 2) {
				image = right2;
			}
			break;
		}
		g2.drawImage(image, screenX, screenY, null);
			
	}
}