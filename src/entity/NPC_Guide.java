package entity;

import java.awt.Rectangle;
import java.util.Random;

import main.GamePanel;

public class NPC_Guide extends Entity {
    
    public NPC_Guide(GamePanel gp) {
        super(gp);
        
        direction = "down";
        speed = 1; // ⬅️ Tambah speed untuk testing
        
        // Solid Area yang lebih kecil untuk presisi
        solidArea = new Rectangle(12, 20, 24, 28); // Lebih ke tengah
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        getImage();
        setDialogue();
        
        // Inisialisasi variabel pathfinding
        goalCol = -1;
        goalRow = -1;
        onPath = false;
    }
    
    public void getImage() {
		
		up1 = setup("/npc/up1", gp.tileSize, gp.tileSize);
	    up2 = setup("/npc/up2", gp.tileSize, gp.tileSize);
	    down1 = setup("/npc/down1", gp.tileSize, gp.tileSize); 
	    down2 = setup("/npc/down2", gp.tileSize, gp.tileSize);
	    left1 = setup("/npc/left1", gp.tileSize, gp.tileSize);
	    left2 = setup("/npc/left2", gp.tileSize, gp.tileSize);
		right1 = setup("/npc/right1", gp.tileSize, gp.tileSize);
		right2 = setup("/npc/right2", gp.tileSize, gp.tileSize);
	}
	public void setDialogue() {
		
		
		dialogues[0] = "Hello World";
		dialogues[1] = "Hello World 2";
		dialogues[2] = "Hello World 3";
		dialogues[3] = "Hello World 4";
		dialogues[4] = "Hello World 5";
	}
    
    @Override
    public void setAction() {
    	
        if (onPath) {
            int currentCol = getCurrentTileX();
            int currentRow = getCurrentTileY();
            
            // Cek jika sudah sampai
            if (currentCol == goalCol && currentRow == goalRow) {
                // Pastikan di tengah tile sebelum berhenti
                if (isAtTileCenter()) {
                    onPath = false;
                    direction = "down";
                    gp.pFinder.pathList.clear();
                } else {
                    // Bergerak ke tengah tile
                    moveToTileCenter(currentCol, currentRow);
                }
                return;
            }
            
            // Cari path jika kosong
            if (gp.pFinder.pathList.isEmpty()) {
                System.out.println("🔍 Searching path...");
                boolean found = gp.pFinder.search(currentCol, currentRow, goalCol, goalRow);
                
                if (found) {
                    System.out.println("✅ Path found! Nodes: " + gp.pFinder.pathList.size());
                    if (!gp.pFinder.pathList.isEmpty()) {
                        followImprovedPath();
                    }
                } else {
                    System.out.println("❌ Path not found!");
                    onPath = false;
                }
            } else {
                followImprovedPath();
            }
        } else {
            // Random movement
//            randomMovement();
        }
    }
    
    @Override
    public void speak() {
    	
    	super.speak();

        // Pastikan NPC berada tepat di tengah tile
        if (!isAtTileCenter()) {
            System.out.println("Aligning NPC to tile center first...");
            alignToTileCenter();
        }

        goalCol = 29;
        goalRow = 30;

        if (goalCol < 0 || goalCol >= gp.maxWorldCol ||
            goalRow < 0 || goalRow >= gp.maxWorldRow) {
        	
            onPath = false;
            return;
        }
        
        onPath = true;
        gp.pFinder.pathList.clear();
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
}