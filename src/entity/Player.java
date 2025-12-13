package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
    
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    
    public Player(GamePanel gp, KeyHandler keyH) {
        
        super(gp);
        
        this.keyH = keyH;
        
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);
        
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;
        
        setDefaultValues();
        getPlayerImage();
    }
    
    public void setDefaultValues() {
        
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
        
        // Player Status
        maxLife = 6;
        life = maxLife;
    }
    
    public void getPlayerImage() {
        
        up1 = setup("/player/top1");
        up2 = setup("/player/top2");
        down1 = setup("/player/bot1"); 
        down2 = setup("/player/bot2");
        left1 = setup("/player/left1");
        left2 = setup("/player/left2");
        right1 = setup("/player/right1");
        right2 = setup("/player/right2");
    }
    
    public void update() {
        
        // Reset collision status
        collisionOn = false;
        
        // Simpan posisi sebelumnya untuk collision checking
        int tempWorldX = worldX;
        int tempWorldY = worldY;
        
        // Cek input untuk menentukan arah dan pergerakan
        if (keyH.upPressed == true) {
            direction = "up";
            worldY -= speed;
        }
        if (keyH.downPressed == true) {
            direction = "down";
            worldY += speed;
        }
        if (keyH.leftPressed == true) {
            direction = "left";
            worldX -= speed;
        }
        if (keyH.rightPressed == true) {
            direction = "right";
            worldX += speed;
        }
        
        // Check Tile Collision
        gp.cChecker.checkTile(this);
        
        // Check Object Collision
        int objIndex = gp.cChecker.checkObject(this, true);
        pickUpObject(objIndex);
        
        // Check NPC Collision
        int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
        interactNPC(npcIndex);
        
        // Check Event
        gp.eHandler.checkEvent();
        
        // Reset enterPressed
        gp.keyH.enterPressed = false;
        
        // Jika ada collision, kembalikan ke posisi sebelumnya
        if (collisionOn == true) {
            worldX = tempWorldX;
            worldY = tempWorldY;
        }
        
        // Sprite Animation Logic
        boolean isMoving = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;
        
        if (isMoving) {
            spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        } else {
            // Reset to default sprite when not moving
            spriteNum = 1;
            spriteCounter = 0;
        }
    }
    
    public void pickUpObject(int i) {
        if (i != 999) {
            // Handle object pickup logic here
        }
    }
    
    public void interactNPC(int i) {
        if (i != 999) {
            if (gp.keyH.enterPressed == true) {
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }
    }

    public void draw(Graphics2D g2) {
        
        BufferedImage image = null;
        
        switch(direction) {
            case "up":
                image = (spriteNum == 1) ? up1 : up2;
                break;
            case "down":
                image = (spriteNum == 1) ? down1 : down2;
                break;
            case "left":
                image = (spriteNum == 1) ? left1 : left2;
                break;
            case "right":
                image = (spriteNum == 1) ? right1 : right2;
                break;
        }
        
        // Gambar player di tengah screen
        g2.drawImage(image, screenX, screenY, null);
    }
}