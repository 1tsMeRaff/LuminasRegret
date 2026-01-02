package entity;

import main.GamePanel;
import object.*;

import java.awt.*;

public class NPC_Merchant extends Entity{
	
    public NPC_Merchant(GamePanel gp) {
        super(gp);
        
        direction = "down";
        speed = 1;

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        solidAreaDefaultX = 8;
        solidAreaDefaultY = 16;
        
        getImage();
        setDialogue();
        setItems();
    }
    
    public void getImage() {
        up1 = setup("/npc/merchant_down_1",gp.tileSize,gp.tileSize);
        up2 = setup("/npc/merchant_down_2",gp.tileSize,gp.tileSize);
        down1 = setup("/npc/merchant_down_1",gp.tileSize,gp.tileSize);
        down2 = setup("/npc/merchant_down_2",gp.tileSize,gp.tileSize);
        left1 = setup("/npc/merchant_down_1",gp.tileSize,gp.tileSize);
        left2 = setup("/npc/merchant_down_2",gp.tileSize,gp.tileSize);
        right1 = setup("/npc/merchant_down_1",gp.tileSize,gp.tileSize);
        right2 = setup("/npc/merchant_down_2",gp.tileSize,gp.tileSize);
    }
    
    public void setDialogue() {
        dialogues[0] = "He he ha, so you found me.\nI have some good stuff. \nDo you want to trade?";
        dialogues[1] = "Come again, hehe!";
        dialogues[2] = "You need more coin to buy that!";
        dialogues[3] = "You can not carry any more!";
        dialogues[4] = "You can not sell an equipped item!";
    }
    
    public void setItems() {
        inventory.add(new OBJ_Bread(gp));
        inventory.add(new OBJ_Axe(gp));
        inventory.add(new OBJ_Shield_Iron(gp));
        inventory.add(new OBJ_Key(gp));
    }
    
    public void speak() {
    	
    	super.speak();
//        facePlayer();
        gp.gameState = gp.tradeState;
        gp.ui.npc = this;
    }
}
