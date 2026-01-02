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
            BufferedImage img = getCurrentImage();
            if (img != null) {
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;
                
                // Pastikan dalam layar
                if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
                   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                    
                    g2.drawImage(img, screenX, screenY, null);
                    
                    // DEBUG: Gambar bounding box merah
                    // g2.setColor(java.awt.Color.RED);
                    // g2.drawRect(screenX, screenY, img.getWidth(), img.getHeight());
                }
            }
        }
    }
    
    private java.awt.image.BufferedImage getCurrentImage() {
        if (image != null) return image; // Gunakan image single jika ada
        
        // Atau gunakan image berdasarkan direction
        switch(direction) {
            case "up": return (spriteNum == 1) ? up1 : up2;
            case "down": return (spriteNum == 1) ? down1 : down2;
            case "left": return (spriteNum == 1) ? left1 : left2;
            case "right": return (spriteNum == 1) ? right1 : right2;
            default: return down1;
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