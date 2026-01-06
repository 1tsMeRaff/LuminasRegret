package entity;

import java.awt.Rectangle;
import java.util.Random;

import main.GamePanel;

public class NPC_Guide extends Entity {
    
    public NPC_Guide(GamePanel gp) {
        super(gp);
        
        direction = "down";
        speed = 1;
        
        // Solid Area yang lebih kecil untuk presisi
        solidArea = new Rectangle(12, 20, 24, 28); // Lebih ke tengah
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        getImage();
        setDialogue();
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
            
            // Cek jika sudah sampai tujuan
            if (currentCol == goalCol && currentRow == goalRow) {
                if (isAtTileCenter()) {
                    onPath = false;
                    direction = "down";
                    myPath.clear(); // Bersihkan path lokal
                } else {
                    moveToTileCenter(currentCol, currentRow);
                }
                return;
            }
            
            // LOGIKA PENTING: Cek myPath (milik sendiri), bukan gp.pFinder
            if (myPath.isEmpty()) {
                
                System.out.println("🔍 NPC Searching path...");
                
                // Pinjam kalkulator global (gp.pFinder) sebentar
                boolean found = gp.pFinder.search(currentCol, currentRow, goalCol, goalRow);
                
                if (found && !gp.pFinder.pathList.isEmpty()) {
                    System.out.println("✅ Path found! Copying to local memory...");
                    
                    // COPY hasil pathfinder global ke path lokal NPC
                    myPath.clear();
                    myPath.addAll(gp.pFinder.pathList); 
                    
                    // Langsung jalan frame ini
                    followImprovedPath();
                } else {
                    System.out.println("❌ Path not found via Pathfinder!");
                    onPath = false;
                    myPath.clear();
                }
            } else {
                // Jika myPath masih ada isinya, ikuti saja (jangan search ulang)
                // Ini mencegah path NPC ditimpa oleh Monster
                followImprovedPath();
            }
        } else {
            randomMovement();
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
        myPath.clear();
    }
}