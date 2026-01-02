package entity;

import java.util.Random;

import ai.Node;
import main.GamePanel;

public class NPC_Guide extends Entity {
	
	public NPC_Guide(GamePanel gp) {
		super(gp);
		
		direction = "down";
		speed = 1;
		
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
	    
			actionLockCounter++;
			if(actionLockCounter == 120) {
				Random random = new Random();
				int i = random.nextInt(100)+1;
				
				if(i <= 25) {
					direction = "up";
				}
				if(i > 25 && i <= 50) {
					direction = "down";
				}
				if(i > 50 && i <= 75) {
					direction = "left";
				}
				if(i > 75 && i <= 100) {
					direction = "right";
				}
				
				actionLockCounter = 0;
			}
		}
	
	public void speak() {
		
		super.speak();
		onPath = true;
	}
}
