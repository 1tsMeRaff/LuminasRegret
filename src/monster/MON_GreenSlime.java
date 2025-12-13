package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class MON_GreenSlime extends Entity {

	public MON_GreenSlime(GamePanel gp) {
		super(gp);
		
		type = 2;
		name = "Green Slime";
		speed = 1;
		maxLife = 4;
		life = maxLife;
		
		solidArea.x = 3;
		solidArea.y = 18;
		solidArea.width = 42;
		solidArea.height = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage();
	}
	public void getImage() {
		
		up1 = setup("/monster/slimeup1");
		up2 = setup("/monster/slimeup2");
		down1 = setup("/monster/slimeidle");
		down2 = setup("/monster/slimemove");
		left1 = setup("/monster/slimeleft1");
		left2 = setup("/monster/slimeleft2");
		right1 = setup("/monster/slimeright1");
		right2 = setup("/monster/slimeright2");
	}
	public void setAction() {
		
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
