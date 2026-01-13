package entity;

import java.awt.Rectangle;
import java.util.Random;

import main.GamePanel;

public class NPC_Guide extends Entity {
    
    public NPC_Guide(GamePanel gp) {
        super(gp);
        
        direction = "down";
        speed = 1;
        
        solidArea = new Rectangle(12, 20, 24, 28);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        getImage();
        setDialogue();
    }
    
    public void getImage() {
    	
    	int size = 18;
		
		up1 = setup("/npc/up1", gp.tileSize - size, gp.tileSize - size);
	    up2 = setup("/npc/up2", gp.tileSize - size, gp.tileSize - size);
	    down1 = setup("/npc/down1", gp.tileSize - size, gp.tileSize - size); 
	    down2 = setup("/npc/down2", gp.tileSize - size, gp.tileSize - size);
	    left1 = setup("/npc/left1", gp.tileSize - size, gp.tileSize - size);
	    left2 = setup("/npc/left2", gp.tileSize - size, gp.tileSize - size);
		right1 = setup("/npc/right1", gp.tileSize - size, gp.tileSize - size);
		right2 = setup("/npc/right2", gp.tileSize - size, gp.tileSize - size);
	}
	public void setDialogue() {
		
		
		dialogues[0] = "Halo petualang /n aku tahu kamu juga mencari harta itu";
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
            
            if (myPath.isEmpty()) {
                
                boolean found = gp.pFinder.search(currentCol, currentRow, goalCol, goalRow);
                
                if (found && !gp.pFinder.pathList.isEmpty()) {
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
//        if (!isAtTileCenter()) {
//            alignToTileCenter();
//        }

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