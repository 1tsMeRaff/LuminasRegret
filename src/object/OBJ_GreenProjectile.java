package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_GreenProjectile extends Projectile {
	
	GamePanel gp;

    public OBJ_GreenProjectile(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
        name = "GreenProjectile";
        speed = 8;
        maxLife = 80;
        life = maxLife;
        attack = 2;
        useCost = 1;
        alive = false;
        getImage();
    }

    public void getImage() {
        up1 = setup("/projectile/balls005", gp.tileSize, gp.tileSize);
        up2 = setup("/projectile/balls005", gp.tileSize, gp.tileSize);
        down1 = setup("/projectile/balls005", gp.tileSize, gp.tileSize);
        down2 = setup("/projectile/balls005", gp.tileSize, gp.tileSize);
        left1 = setup("/projectile/balls005", gp.tileSize, gp.tileSize);
        left2 = setup("/projectile/balls005", gp.tileSize, gp.tileSize);
        right1 = setup("/projectile/balls005", gp.tileSize, gp.tileSize);
        right2 = setup("/projectile/balls005", gp.tileSize, gp.tileSize);
    }
}
