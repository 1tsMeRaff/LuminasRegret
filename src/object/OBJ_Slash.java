package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class OBJ_Slash extends Projectile {
    
    GamePanel gp;

    public OBJ_Slash(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
        name = "Slash";
        speed = 3;
        maxLife = 80;
        life = maxLife;
        attack = 2;
        knockBackPower = 0;
        useCost = 1;
        alive = false;
        
        // Solid area untuk projectile
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        getImage();
    }

    public void getImage() {
        // Gunakan 1 gambar untuk semua arah (ukuran tileSize penuh)
        image = setup("/projectile/rangeatt000", gp.tileSize, gp.tileSize);
        
        // Atau tetap setup semua direction jika ingin animasi
        up1 = setup("/projectile/rangeatt000", gp.tileSize, gp.tileSize);
        up2 = setup("/projectile/rangeatt000", gp.tileSize, gp.tileSize);
        down1 = setup("/projectile/rangeatt000", gp.tileSize, gp.tileSize);
        down2 = setup("/projectile/rangeatt000", gp.tileSize, gp.tileSize);
        left1 = setup("/projectile/rangeatt000", gp.tileSize, gp.tileSize);
        left2 = setup("/projectile/rangeatt000", gp.tileSize, gp.tileSize);
        right1 = setup("/projectile/rangeatt000", gp.tileSize, gp.tileSize);
        right2 = setup("/projectile/rangeatt000", gp.tileSize, gp.tileSize);
    }
    
    // Override draw untuk projectile
    @Override
    public void draw(Graphics2D g2) {
        if (alive) {
            BufferedImage img = image;
            if (img != null) {
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;
                
                // Cek apakah dalam layar
                if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
                   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                    
                    g2.drawImage(img, screenX, screenY, null);
                }
            }
        }
        else {
            // Hapus dari array jika ada indeks yang valid
            if (projectileIndex >= 0 && projectileIndex < gp.projectile[gp.currentMap].length) {
                gp.projectile[gp.currentMap][projectileIndex] = null;
                projectileIndex = -1;  // Reset
            }
        }
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