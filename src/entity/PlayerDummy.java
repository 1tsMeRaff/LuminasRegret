package entity;

import main.GamePanel;

public class PlayerDummy extends Entity {
	
	public static final String npcName = "Dummy"; 
	
	public PlayerDummy(GamePanel gp) {
		super(gp);
		
		name = npcName;
		getPlayerImage();
	}
	  public void getPlayerImage() {
	        
	        up1 = setup("/player/top1", gp.tileSize, gp.tileSize);
	        up2 = setup("/player/top2", gp.tileSize, gp.tileSize);
	        down1 = setup("/player/bot1", gp.tileSize, gp.tileSize); 
	        down2 = setup("/player/bot2", gp.tileSize, gp.tileSize);
	        left1 = setup("/player/left1", gp.tileSize, gp.tileSize);
	        left2 = setup("/player/left2", gp.tileSize, gp.tileSize);
	        right1 = setup("/player/right1", gp.tileSize, gp.tileSize);
	        right2 = setup("/player/right2", gp.tileSize, gp.tileSize);
	    }

}
