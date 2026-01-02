package entity;

import main.GamePanel;

public class Projectile extends Entity {
    
    Entity user;

    public Projectile(GamePanel gp) {
        super(gp);
    }
    
    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;
        
        // PERHITUNGAN POSISI YANG TEPAT UNTUK SEMUA PROJECTILE
        // Asumsi: projectile memiliki ukuran tileSize penuh (48x48)
        int projectileWidth = (image != null) ? image.getWidth() : gp.tileSize;
        int projectileHeight = (image != null) ? image.getHeight() : gp.tileSize;
        
        // Posisi tengah player
        int playerCenterX = worldX + (gp.tileSize / 2);
        int playerCenterY = worldY + (gp.tileSize / 2);
        
        // Hitung posisi projectile agar tepat di tengah depan player
        switch(direction) {
            case "up":
                // Muncul di atas player, rata tengah horizontal
                this.worldX = playerCenterX - (projectileWidth / 2);
                this.worldY = worldY - projectileHeight; // Di atas player
                break;
            case "down":
                // Muncul di bawah player, rata tengah horizontal
                this.worldX = playerCenterX - (projectileWidth / 2);
                this.worldY = worldY + gp.tileSize; // Di bawah player
                break;
            case "left":
                // Muncul di kiri player, rata tengah vertikal
                this.worldX = worldX - projectileWidth; // Di kiri player
                this.worldY = playerCenterY - (projectileHeight / 2);
                break;
            case "right":
                // Muncul di kanan player, rata tengah vertikal
                this.worldX = worldX + gp.tileSize; // Di kanan player
                this.worldY = playerCenterY - (projectileHeight / 2);
                break;
        }
        
//        // DEBUG
//        System.out.println("DEBUG Projectile.set():");
//        System.out.println("  Type: " + this.name);
//        System.out.println("  Player pos: " + worldX + ", " + worldY);
//        System.out.println("  Direction: " + direction);
//        System.out.println("  Projectile pos: " + this.worldX + ", " + this.worldY);
//        System.out.println("  Projectile size: " + projectileWidth + "x" + projectileHeight);
//        System.out.println("  TileSize: " + gp.tileSize);
    }

    public void update() {
        
        if(user == gp.player) {
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            if(monsterIndex != 999) {
                gp.player.damageMonster(monsterIndex, attack, knockBackPower);
                generateParticle(user.projectile, gp.monster[gp.currentMap][monsterIndex]);
                alive = false;
            }
        }
        if(user != gp.player) {
            boolean contactPlayer = gp.cChecker.checkPlayer(this);
            if(gp.player.invincible == false && contactPlayer == true) {
                damagePlayer(attack);
                generateParticle(user.projectile, gp.player);
                alive = false;
            }
        }
        
        // Update posisi berdasarkan direction
        switch(direction) {
            case "up": worldY -= speed; break;
            case "down": worldY += speed; break;
            case "left": worldX -= speed; break;
            case "right": worldX += speed; break;
        }
        
        // Kurangi life (range/maksimum jarak)
        life--;
        if(life <= 0) {
            alive = false;
        }

        // Sprite animation
        spriteCounter++;
        if(spriteCounter > 12) {
            if(spriteNum == 1) {
                spriteNum = 2;
            }
            else if(spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
    
    public boolean haveResource(Entity user) {
        boolean haveResource = false;
        return haveResource;
    }
    
    public void substractResource(Entity user) {}
}