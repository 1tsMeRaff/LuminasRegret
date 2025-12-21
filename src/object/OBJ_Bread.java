package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Bread extends Entity {

	GamePanel gp;
	int value = 3;

	public OBJ_Bread(GamePanel gp) {
	    super(gp);
	    
	    this.gp = gp;
	    
	    type = type_consumable;
	    name = "Red Potion";
	    down1 = setup("/objects/bread", gp.tileSize, gp.tileSize);
	    description = "[" + name + "]\n Hanya roti biasa.";
	}

	public void use(Entity entity) {
		
	    gp.gameState = gp.dialogueState;
	    gp.ui.currentDialogue = "Kamu memakan " + name + "\n"
	    						+ "hp bertambah " + value + ".";
	    entity.life += value;
	    if(gp.player.life > gp.player.maxLife) {
	        gp.player.life = gp.player.maxLife;
	    }
	    gp.playSE(2);
	}
}
