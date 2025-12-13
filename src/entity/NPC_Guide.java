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
		
		up1 = setup("/npc/up1");
	    up2 = setup("/npc/up2");
	    down1 = setup("/npc/down1"); 
	    down2 = setup("/npc/down2");
	    left1 = setup("/npc/left1");
	    left2 = setup("/npc/left2");
		right1 = setup("/npc/right1");
		right2 = setup("/npc/right2");
	}
	public void setDialogue() {
		
		
		dialogues[0] = "Hello World";
		dialogues[1] = "Hello World 2";
		dialogues[2] = "Hello World 3";
		dialogues[3] = "Hello World 4";
		dialogues[4] = "Hello World 5";
	}
	public void setAction() {
		
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
	public void speak() {
		
		super.speak();
	}
}
