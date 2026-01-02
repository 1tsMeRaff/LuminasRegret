package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_GreenProjectile extends Projectile {
    
    GamePanel gp;

    public OBJ_GreenProjectile(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
        name = "GreenProjectile";
        speed = 3;
        maxLife = 80; // Maksimum jarak tempuh
        life = maxLife;
        attack = 2;
        useCost = 1;
        alive = false;
        
        // Setup ukuran
        int projectileSize = gp.tileSize / 2; // 24x24 jika tileSize=48
        
        // Solid area sesuai dengan ukuran gambar
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = projectileSize;
        solidArea.height = projectileSize;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        getImage();
    }

    public void getImage() {
        //1 gambar untuk semua arah
        image = setup("/projectile/balls005", gp.tileSize / 2, gp.tileSize / 2);
    }
    
    // Optional: Override draw method untuk projectile lebih kecil
    @Override
    public void draw(java.awt.Graphics2D g2) {
        if (image != null && alive) {
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            // Gambar langsung di posisi yang sudah dihitung
            g2.drawImage(image, screenX, screenY, null);
        }
    }
}