package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_Slash extends Projectile {
	
	GamePanel gp;

	    public OBJ_Slash(GamePanel gp) {
	        super(gp);
	        this.gp = gp;
	        
	        name = "Slash";
	        speed = 5;
	        maxLife = 80;
	        life = maxLife;
	        attack = 2;
	        useCost = 1;
	        alive = false;
	        getImage();
	    }

	    public void getImage() {
	        up1 = setup("/projectile/rangeatt000", gp.tileSize / 4, gp.tileSize / 4);
	        up2 = setup("/projectile/rangeatt000", gp.tileSize / 4, gp.tileSize / 4);
	        down1 = setup("/projectile/rangeatt000", gp.tileSize / 4, gp.tileSize / 4);
	        down2 = setup("/projectile/rangeatt000", gp.tileSize / 4, gp.tileSize / 4);
	        left1 = setup("/projectile/rangeatt000", gp.tileSize / 4, gp.tileSize / 4);
	        left2 = setup("/projectile/rangeatt000", gp.tileSize / 4, gp.tileSize / 4);
	        right1 = setup("/projectile/rangeatt000", gp.tileSize / 4, gp.tileSize / 4);
	        right2 = setup("/projectile/rangeatt000", gp.tileSize / 4, gp.tileSize / 4);
	    }
	    
	    public boolean haveResource(Entity user) {
	    	
	    	boolean haveResource = false;
	    	if(user.mana >= useCost) {
	    		haveResource = true;
	    	}
	    	return haveResource;
	    }
	    
	    public void substractResource(Entity user) {
	    	user.mana -= useCost;
	    }
	}

