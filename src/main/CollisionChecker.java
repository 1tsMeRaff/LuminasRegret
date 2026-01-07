package main;

import entity.Entity;
import tile_interactive.InteractiveTile;
import java.awt.Rectangle;

public class CollisionChecker {
    
    GamePanel gp;
    
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }
    
    public void checkTile(Entity entity) {
        // Hitung batas solid area di world coordinates
        int entityLeft = entity.worldX + entity.solidArea.x;
        int entityRight = entityLeft + entity.solidArea.width;
        int entityTop = entity.worldY + entity.solidArea.y;
        int entityBottom = entityTop + entity.solidArea.height;
        
        // Convert ke tile coordinates
        int leftCol = entityLeft / gp.tileSize;
        int rightCol = entityRight / gp.tileSize;
        int topRow = entityTop / gp.tileSize;
        int bottomRow = entityBottom / gp.tileSize;
        
        int tileNum1, tileNum2;
        
        String direction = entity.direction;
        if(entity.knockBack) {
        	direction = entity.knockBackDirection;
        }
        
        
        switch (direction) {
            case "up":
                // Cek tile di atas entity
                int nextTopRow = (entityTop - entity.speed) / gp.tileSize;
                
                if (nextTopRow < 0) {
                    entity.collisionOn = true;
                    return;
                }
                
                // Cek dua titik di kiri dan kanan atas entity
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][leftCol][nextTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][rightCol][nextTopRow];
                
                if (isCollisionTile(tileNum1) || isCollisionTile(tileNum2)) {
                    entity.collisionOn = true;
                }
                break;
                
            case "down":
                // Cek tile di bawah entity
                int nextBottomRow = (entityBottom + entity.speed) / gp.tileSize;
                
                if (nextBottomRow >= gp.maxWorldRow) {
                    entity.collisionOn = true;
                    return;
                }
                
                // Cek dua titik di kiri dan kanan bawah entity
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][leftCol][nextBottomRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][rightCol][nextBottomRow];
                
                if (isCollisionTile(tileNum1) || isCollisionTile(tileNum2)) {
                    entity.collisionOn = true;
                }
                break;
                
            case "left":
                // Cek tile di kiri entity
                int nextLeftCol = (entityLeft - entity.speed) / gp.tileSize;
                
                if (nextLeftCol < 0) {
                    entity.collisionOn = true;
                    return;
                }
                
                // Cek dua titik di atas dan bawah kiri entity
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][nextLeftCol][topRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][nextLeftCol][bottomRow];
                
                if (isCollisionTile(tileNum1) || isCollisionTile(tileNum2)) {
                    entity.collisionOn = true;
                }
                break;
                
            case "right":
                // Cek tile di kanan entity
                int nextRightCol = (entityRight + entity.speed) / gp.tileSize;
                
                if (nextRightCol >= gp.maxWorldCol) {
                    entity.collisionOn = true;
                    return;
                }
                
                // Cek dua titik di atas dan bawah kanan entity
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][nextRightCol][topRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][nextRightCol][bottomRow];
                
                if (isCollisionTile(tileNum1) || isCollisionTile(tileNum2)) {
                    entity.collisionOn = true;
                }
                break;
        }
    }
    
    private boolean isCollisionTile(int tileNum) {
        if (tileNum < 0 || tileNum >= gp.tileM.tile[gp.currentMap].length) {
            return true;
        }
        return gp.tileM.tile[gp.currentMap][tileNum] != null && 
               gp.tileM.tile[gp.currentMap][tileNum].collision;
    }
    
    // Check collision with objects
    public int checkObject(Entity entity, boolean player) {
        int index = 999;
        
        String direction = entity.direction;
        if(entity.knockBack) {
        	direction = entity.knockBackDirection;
        }
        
        if (gp.obj == null || gp.currentMap < 0 || gp.currentMap >= gp.obj.length) {
            return index;
        }
        
        // Simpan posisi asli solid area
        Rectangle originalEntitySolidArea = new Rectangle(entity.solidArea);
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        
        // Posisi solid area setelah bergerak
        Rectangle futureSolidArea = new Rectangle(entity.solidArea);
        
        switch (direction) {
            case "up": futureSolidArea.y -= entity.speed; break;
            case "down": futureSolidArea.y += entity.speed; break;
            case "left": futureSolidArea.x -= entity.speed; break;
            case "right": futureSolidArea.x += entity.speed; break;
        }
        
        for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
            if (gp.obj[gp.currentMap][i] != null) {
                // Simpan posisi asli object
                Rectangle originalObjSolidArea = new Rectangle(gp.obj[gp.currentMap][i].solidArea);
                
                // Hitung posisi object di world
                Rectangle objWorldSolidArea = new Rectangle(
                    gp.obj[gp.currentMap][i].worldX + gp.obj[gp.currentMap][i].solidArea.x,
                    gp.obj[gp.currentMap][i].worldY + gp.obj[gp.currentMap][i].solidArea.y,
                    gp.obj[gp.currentMap][i].solidArea.width,
                    gp.obj[gp.currentMap][i].solidArea.height
                );
                
                // Cek collision antara future solid area dan object
                if (futureSolidArea.intersects(objWorldSolidArea)) {
                    if (gp.obj[gp.currentMap][i].collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                    }
                }
                
                // Reset posisi object
                gp.obj[gp.currentMap][i].solidArea.setBounds(originalObjSolidArea);
            }
        }
        
        // Reset posisi entity
        entity.solidArea.setBounds(originalEntitySolidArea);
        return index;
    }
    
    // Check collision with other entities (NPC/Monster)
    public int checkEntity(Entity entity, Entity[][] target) {
        int index = 999;
        String direction = entity.direction;
        if(entity.knockBack) {
        	direction = entity.knockBackDirection;
        }
        
        if (target == null || gp.currentMap < 0 || gp.currentMap >= target.length) {
            return index;
        }
        
        // Simpan posisi asli solid area entity
        Rectangle originalEntitySolidArea = new Rectangle(entity.solidArea);
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        
        // Posisi solid area setelah bergerak
        Rectangle futureSolidArea = new Rectangle(entity.solidArea);
        
        switch (direction) {
            case "up": futureSolidArea.y -= entity.speed; break;
            case "down": futureSolidArea.y += entity.speed; break;
            case "left": futureSolidArea.x -= entity.speed; break;
            case "right": futureSolidArea.x += entity.speed; break;
        }
        
        for (int i = 0; i < target[gp.currentMap].length; i++) {
            if (target[gp.currentMap][i] != null && target[gp.currentMap][i] != entity) {
                // Simpan posisi asli target
                Rectangle originalTargetSolidArea = new Rectangle(target[gp.currentMap][i].solidArea);
                
                // Hitung posisi target di world
                Rectangle targetWorldSolidArea = new Rectangle(
                    target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x,
                    target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y,
                    target[gp.currentMap][i].solidArea.width,
                    target[gp.currentMap][i].solidArea.height
                );
                
                // Cek collision antara future solid area dan target
                if (futureSolidArea.intersects(targetWorldSolidArea)) {
                    entity.collisionOn = true;
                    index = i;
                }
                
                // Reset posisi target
                target[gp.currentMap][i].solidArea.setBounds(originalTargetSolidArea);
            }
        }
        
        // Reset posisi entity
        entity.solidArea.setBounds(originalEntitySolidArea);
        return index;
    }
    
    // Check collision with player (untuk NPC/Monster)
    public boolean checkPlayer(Entity entity) {
        if (gp.player == null) return false;
        
        // Simpan posisi asli solid area entity
        Rectangle originalEntitySolidArea = new Rectangle(entity.solidArea);
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        
        // Posisi solid area setelah bergerak
        Rectangle futureSolidArea = new Rectangle(entity.solidArea);
        
        switch (entity.direction) {
            case "up": futureSolidArea.y -= entity.speed; break;
            case "down": futureSolidArea.y += entity.speed; break;
            case "left": futureSolidArea.x -= entity.speed; break;
            case "right": futureSolidArea.x += entity.speed; break;
        }
        
        // Hitung posisi player di world
        Rectangle playerWorldSolidArea = new Rectangle(
            gp.player.worldX + gp.player.solidArea.x,
            gp.player.worldY + gp.player.solidArea.y,
            gp.player.solidArea.width,
            gp.player.solidArea.height
        );
        
        // Cek collision antara future solid area dan player
        if (futureSolidArea.intersects(playerWorldSolidArea)) {
            entity.collisionOn = true;
            // Reset posisi entity
            entity.solidArea.setBounds(originalEntitySolidArea);
            return true;
        }
        
        // Reset posisi entity
        entity.solidArea.setBounds(originalEntitySolidArea);
        return false;
    }
    
    // Check collision with interactive tiles
    public int checkInteractiveTile(Entity entity) {
        int index = 999;
        
        if (gp.iTile == null || 
            gp.currentMap < 0 || 
            gp.currentMap >= gp.iTile.length || 
            gp.iTile[gp.currentMap] == null) {
            return index;
        }
        
        // Simpan posisi asli solid area entity
        Rectangle originalEntitySolidArea = new Rectangle(entity.solidArea);
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        
        // Gunakan attack area untuk interaksi
        Rectangle attackArea = new Rectangle(entity.attackArea);
        
        // Atur posisi attack area berdasarkan arah dan ukuran attack area
        switch (entity.direction) {
            case "up":
                attackArea.x = entity.solidArea.x + (entity.solidArea.width / 2) - (attackArea.width / 2);
                attackArea.y = entity.solidArea.y - attackArea.height;
                break;
            case "down":
                attackArea.x = entity.solidArea.x + (entity.solidArea.width / 2) - (attackArea.width / 2);
                attackArea.y = entity.solidArea.y + entity.solidArea.height;
                break;
            case "left":
                attackArea.x = entity.solidArea.x - attackArea.width;
                attackArea.y = entity.solidArea.y + (entity.solidArea.height / 2) - (attackArea.height / 2);
                break;
            case "right":
                attackArea.x = entity.solidArea.x + entity.solidArea.width;
                attackArea.y = entity.solidArea.y + (entity.solidArea.height / 2) - (attackArea.height / 2);
                break;
        }
        
        for (int i = 0; i < gp.iTile[gp.currentMap].length; i++) {
            InteractiveTile tile = gp.iTile[gp.currentMap][i];
            if (tile != null && tile.destructible) {
                // Hitung posisi tile di world
                Rectangle tileWorldSolidArea = new Rectangle(
                    tile.worldX + tile.solidArea.x,
                    tile.worldY + tile.solidArea.y,
                    tile.solidArea.width,
                    tile.solidArea.height
                );
                
                // Cek collision dengan attack area
                if (attackArea.intersects(tileWorldSolidArea)) {
                    index = i;
                    break;
                }
            }
        }
        
        // Reset posisi entity
        entity.solidArea.setBounds(originalEntitySolidArea);
        return index;
    }
    
    // Utility methods
    public boolean isTileCollision(int col, int row) {
        if (col < 0 || col >= gp.maxWorldCol || row < 0 || row >= gp.maxWorldRow) {
            return true;
        }
        
        int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
        return isCollisionTile(tileNum);
    }
    
    public boolean isPointInCollisionTile(int worldX, int worldY) {
        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;
        return isTileCollision(col, row);
    }
    
    public int getTileAtPosition(int worldX, int worldY) {
        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;
        
        if (col >= 0 && col < gp.maxWorldCol && row >= 0 && row < gp.maxWorldRow) {
            return gp.tileM.mapTileNum[gp.currentMap][col][row];
        }
        return -1;
    }
    
    public boolean isAtHorizontalTileCenter(Entity entity) {
        int entityCenterX = entity.worldX + entity.solidArea.x + (entity.solidArea.width / 2);
        int tileCenterX = ((entityCenterX / gp.tileSize) * gp.tileSize) + (gp.tileSize / 2);
        return Math.abs(entityCenterX - tileCenterX) <= 2;
    }
    
    public boolean isAtVerticalTileCenter(Entity entity) {
        int entityCenterY = entity.worldY + entity.solidArea.y + (entity.solidArea.height / 2);
        int tileCenterY = ((entityCenterY / gp.tileSize) * gp.tileSize) + (gp.tileSize / 2);
        return Math.abs(entityCenterY - tileCenterY) <= 2;
    }
    
    // Metode untuk mendapatkan collision area di world coordinates
    public Rectangle getWorldCollisionArea(Entity entity) {
        return new Rectangle(
            entity.worldX + entity.solidArea.x,
            entity.worldY + entity.solidArea.y,
            entity.solidArea.width,
            entity.solidArea.height
        );
    }
}