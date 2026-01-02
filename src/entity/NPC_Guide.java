package entity;

import java.util.Random;

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
	public void setAction() {
		
		if(onPath == true) {
			
			int goalCol = 28;
			int goalRow = 29;
			
			searchPath(goalCol, goalRow);
		}
		else {
			
			actionLockCounter ++;
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
	}
	public void speak() {
		
		super.speak();
		onPath = true;
	}
}
