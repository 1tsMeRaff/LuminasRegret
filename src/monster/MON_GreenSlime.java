package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class MON_GreenSlime extends Entity {
	
	GamePanel gp;

	public MON_GreenSlime(GamePanel gp) {
		super(gp);
		
		this.gp = gp;
		
		type = 2;
		name = "Green Slime";
		speed = 1;
		maxLife = 4;
		life = maxLife;
		attack = 5;
		defense = 0;
		exp = 2;
		
		solidArea.x = 3;
		solidArea.y = 18;
		solidArea.width = 42;
		solidArea.height = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage();
	}
	public void getImage() {
		
		up1 = setup("/monster/slimeup1", gp.tileSize, gp.tileSize);
		up2 = setup("/monster/slimeup2", gp.tileSize, gp.tileSize);
		down1 = setup("/monster/slimeidle", gp.tileSize, gp.tileSize);
		down2 = setup("/monster/slimemove", gp.tileSize, gp.tileSize);
		left1 = setup("/monster/slimeleft1", gp.tileSize, gp.tileSize);
		left2 = setup("/monster/slimeleft2", gp.tileSize, gp.tileSize);
		right1 = setup("/monster/slimeright1", gp.tileSize, gp.tileSize);
		right2 = setup("/monster/slimeright2", gp.tileSize, gp.tileSize);
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
	public void damageReaction() {
		
		actionLockCounter = 0;
		direction = gp.player.direction;
	}
}
