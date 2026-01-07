package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Bread extends Entity {

	GamePanel gp;

	public OBJ_Bread(GamePanel gp) {
	    super(gp);
	    
	    this.gp = gp;
	    
	    type = type_consumable;
	    name = "Roti";
	    value = 2;
	    down1 = setup("/objects/bread", gp.tileSize, gp.tileSize);
	    description = "[" + name + "]\n Hanya roti biasa.";
	}

	public void use(Entity entity) {
		
	    gp.gameState = gp.dialogueState;
	    gp.ui.currentDialogue = "Kamu memakan " + name + "\n"
	    						+ "hp bertambah " + value + ".";
	    if(entity.life >= entity.maxLife) {
			entity.life = entity.maxLife;
		}
		else if(entity.life + 1 == entity.maxLife) {
			entity.life += 1;
		}
		else {
			entity.life += value;
		}
	    gp.playSE(8);
	}
}