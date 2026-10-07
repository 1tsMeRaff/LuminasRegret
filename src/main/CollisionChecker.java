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
        
        int futureX = entity.worldX + entity.solidArea.x;
        int futureY = entity.worldY + entity.solidArea.y;
        int futureW = entity.solidArea.width;
        int futureH = entity.solidArea.height;
        
        switch (direction) {
            case "up": futureY -= entity.speed; break;
            case "down": futureY += entity.speed; break;
            case "left": futureX -= entity.speed; break;
            case "right": futureX += entity.speed; break;
        }
        
        for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
            Entity obj = gp.obj[gp.currentMap][i];
            if (obj != null) {
                int objX = obj.worldX + obj.solidArea.x;
                int objY = obj.worldY + obj.solidArea.y;
                int objW = obj.solidArea.width;
                int objH = obj.solidArea.height;
                
                if (CollisionMath.intersects(futureX, futureY, futureW, futureH, objX, objY, objW, objH)) {
                    if (obj.collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                    }
                }
            }
        }
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
        
        int futureX = entity.worldX + entity.solidArea.x;
        int futureY = entity.worldY + entity.solidArea.y;
        int futureW = entity.solidArea.width;
        int futureH = entity.solidArea.height;
        
        switch (direction) {
            case "up": futureY -= entity.speed; break;
            case "down": futureY += entity.speed; break;
            case "left": futureX -= entity.speed; break;
            case "right": futureX += entity.speed; break;
        }
        
        for (int i = 0; i < target[gp.currentMap].length; i++) {
            Entity tgt = target[gp.currentMap][i];
            if (tgt != null && tgt != entity) {
                int tgtX = tgt.worldX + tgt.solidArea.x;
                int tgtY = tgt.worldY + tgt.solidArea.y;
                int tgtW = tgt.solidArea.width;
                int tgtH = tgt.solidArea.height;
                
                if (CollisionMath.intersects(futureX, futureY, futureW, futureH, tgtX, tgtY, tgtW, tgtH)) {
                    entity.collisionOn = true;
                    index = i;
                }
            }
        }
        return index;
    }
    
    // Check collision with player (untuk NPC/Monster)
    public boolean checkPlayer(Entity entity) {
        if (gp.player == null) return false;
        
        int futureX = entity.worldX + entity.solidArea.x;
        int futureY = entity.worldY + entity.solidArea.y;
        int futureW = entity.solidArea.width;
        int futureH = entity.solidArea.height;
        
        switch (entity.direction) {
            case "up": futureY -= entity.speed; break;
            case "down": futureY += entity.speed; break;
            case "left": futureX -= entity.speed; break;
            case "right": futureX += entity.speed; break;
        }
        
        int playerX = gp.player.worldX + gp.player.solidArea.x;
        int playerY = gp.player.worldY + gp.player.solidArea.y;
        int playerW = gp.player.solidArea.width;
        int playerH = gp.player.solidArea.height;
        
        if (CollisionMath.intersects(futureX, futureY, futureW, futureH, playerX, playerY, playerW, playerH)) {
            entity.collisionOn = true;
            return true;
        }
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
        
        int entitySolidX = entity.worldX + entity.solidArea.x;
        int entitySolidY = entity.worldY + entity.solidArea.y;
        
        int atkX = 0;
        int atkY = 0;
        int atkW = entity.attackArea.width;
        int atkH = entity.attackArea.height;
        
        switch (entity.direction) {
            case "up":
                atkX = entitySolidX + (entity.solidArea.width / 2) - (atkW / 2);
                atkY = entitySolidY - atkH;
                break;
            case "down":
                atkX = entitySolidX + (entity.solidArea.width / 2) - (atkW / 2);
                atkY = entitySolidY + entity.solidArea.height;
                break;
            case "left":
                atkX = entitySolidX - atkW;
                atkY = entitySolidY + (entity.solidArea.height / 2) - (atkH / 2);
                break;
            case "right":
                atkX = entitySolidX + entity.solidArea.width;
                atkY = entitySolidY + (entity.solidArea.height / 2) - (atkH / 2);
                break;
        }
        
        for (int i = 0; i < gp.iTile[gp.currentMap].length; i++) {
            InteractiveTile tile = gp.iTile[gp.currentMap][i];
            if (tile != null && tile.destructible) {
                int tileX = tile.worldX + tile.solidArea.x;
                int tileY = tile.worldY + tile.solidArea.y;
                int tileW = tile.solidArea.width;
                int tileH = tile.solidArea.height;
                
                if (CollisionMath.intersects(atkX, atkY, atkW, atkH, tileX, tileY, tileW, tileH)) {
                    index = i;
                    break;
                }
            }
        }
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