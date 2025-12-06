package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_Guide extends Entity {
	
	public NPC_Guide(GamePanel gp) {
		super(gp);
		
		direction = "down";
		speed = 1;
		
		getImage();
	}
	public void getImage() {
		
		up1 = setup("/npc/idle-removebg-preview");
	    up2 = setup("/npc/idle-removebg-preview");
	    down1 = setup("/npc/idle-removebg-preview"); 
	    down2 = setup("/npc/idle-removebg-preview");
	    left1 = setup("/npc/idle-removebg-preview");
	    left2 = setup("/npc/idle-removebg-preview");
		right1 = setup("/npc/idle-removebg-preview");
		right2 = setup("/npc/idle-removebg-preview");
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
	
}
