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
        
        int entityCenterWorldX = entity.worldX + entity.solidArea.x + (entity.solidArea.width / 2);
        int entityCenterWorldY = entity.worldY + entity.solidArea.y + (entity.solidArea.height / 2);

        int entityCenterCol = entityCenterWorldX / gp.tileSize;
        int entityCenterRow = entityCenterWorldY / gp.tileSize;

        int tileNum;
        
        switch(entity.direction) {
        case "up":
            entityCenterRow = (entityCenterWorldY - entity.speed) / gp.tileSize;
            tileNum = gp.tileM.mapTileNum[gp.currentMap][entityCenterCol][entityCenterRow];
            if(gp.tileM.tile[gp.currentMap][tileNum] != null && 
               gp.tileM.tile[gp.currentMap][tileNum].collision == true) {
                entity.collisionOn = true;
            }
            break;
        case "down":
            entityCenterRow = (entityCenterWorldY + entity.speed) / gp.tileSize;
            tileNum = gp.tileM.mapTileNum[gp.currentMap][entityCenterCol][entityCenterRow];
            if(gp.tileM.tile[gp.currentMap][tileNum] != null && 
               gp.tileM.tile[gp.currentMap][tileNum].collision == true) {
                entity.collisionOn = true;
            }
            break;
        case "left":
            entityCenterCol = (entityCenterWorldX - entity.speed) / gp.tileSize;
            tileNum = gp.tileM.mapTileNum[gp.currentMap][entityCenterCol][entityCenterRow];
            if(gp.tileM.tile[gp.currentMap][tileNum] != null && 
               gp.tileM.tile[gp.currentMap][tileNum].collision == true) {
                entity.collisionOn = true;
            }
            break;
        case "right":
            entityCenterCol = (entityCenterWorldX + entity.speed) / gp.tileSize;
            tileNum = gp.tileM.mapTileNum[gp.currentMap][entityCenterCol][entityCenterRow];
            if(gp.tileM.tile[gp.currentMap][tileNum] != null && 
               gp.tileM.tile[gp.currentMap][tileNum].collision == true) {
                entity.collisionOn = true;
            }
            break;
        }
    }
    
    public int checkObject(Entity entity, boolean player) {
        int index = 999;
        
        for(int i = 0; i < gp.obj[1].length; i++) {
            if(gp.obj[gp.currentMap][i] != null) {
                
                // Simpan posisi asli
                int originalEntityX = entity.solidArea.x;
                int originalEntityY = entity.solidArea.y;
                int originalObjX = gp.obj[gp.currentMap][i].solidArea.x;
                int originalObjY = gp.obj[gp.currentMap][i].solidArea.y;
                
                // Set posisi untuk collision check
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].worldX + gp.obj[gp.currentMap][i].solidArea.x;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].worldY + gp.obj[gp.currentMap][i].solidArea.y;
                
                // Adjust based on movement direction
                switch(entity.direction) {
                case "up":
                    entity.solidArea.y -= entity.speed;                    
                    break;
                case "down":
                    entity.solidArea.y += entity.speed;
                    break;
                case "left":
                    entity.solidArea.x -= entity.speed;
                    break;
                case "right":
                    entity.solidArea.x += entity.speed;
                    break;
                }
                
                // Untuk semua objek, gunakan intersect biasa untuk pickup
                if(entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                    if(gp.obj[gp.currentMap][i].collision == true) {
                        entity.collisionOn = true;
                    }
                    if(player == true) {
                        index = i;
                    }
                }
                
                // Reset posisi
                entity.solidArea.x = originalEntityX;
                entity.solidArea.y = originalEntityY;
                gp.obj[gp.currentMap][i].solidArea.x = originalObjX;
                gp.obj[gp.currentMap][i].solidArea.y = originalObjY;
            }
        }
        return index;
    }
    
    // NPC Or Monster
    public int checkEntity(Entity entity, Entity[][] target) {
        
        int index = 999;
        
        // Hitung titik tengah entity
        int entityCenterX = entity.worldX + entity.solidArea.x + (entity.solidArea.width / 2);
        int entityCenterY = entity.worldY + entity.solidArea.y + (entity.solidArea.height / 2);
        
        for(int i = 0; i < target[1].length; i++) {
            if(target[gp.currentMap][i] != null && target[gp.currentMap][i] != entity) {
                // Hitung titik tengah target
                int targetCenterX = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x + 
                                   (target[gp.currentMap][i].solidArea.width / 2);
                int targetCenterY = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y + 
                                   (target[gp.currentMap][i].solidArea.height / 2);
                
                // Hitung jarak antara titik tengah
                int distanceX = Math.abs(entityCenterX - targetCenterX);
                int distanceY = Math.abs(entityCenterY - targetCenterY);
                
                // Tentukan threshold untuk collision
                int thresholdX = (entity.solidArea.width + target[gp.currentMap][i].solidArea.width) / 4;
                int thresholdY = (entity.solidArea.height + target[gp.currentMap][i].solidArea.height) / 4;
                
                // Cek jika titik tengah cukup dekat berdasarkan arah gerakan
                boolean isColliding = false;
                
                switch(entity.direction) {
                case "up":
                    isColliding = distanceX < thresholdX && 
                                 entityCenterY - entity.speed <= targetCenterY + thresholdY &&
                                 entityCenterY - entity.speed >= targetCenterY - thresholdY;
                    break;
                case "down":
                    isColliding = distanceX < thresholdX && 
                                 entityCenterY + entity.speed <= targetCenterY + thresholdY &&
                                 entityCenterY + entity.speed >= targetCenterY - thresholdY;
                    break;
                case "left":
                    isColliding = distanceY < thresholdY && 
                                 entityCenterX - entity.speed <= targetCenterX + thresholdX &&
                                 entityCenterX - entity.speed >= targetCenterX - thresholdX;
                    break;
                case "right":
                    isColliding = distanceY < thresholdY && 
                                 entityCenterX + entity.speed <= targetCenterX + thresholdX &&
                                 entityCenterX + entity.speed >= targetCenterX - thresholdX;
                    break;
                }
                
                if(isColliding) {
                    entity.collisionOn = true;
                    index = i;
                }
            }
        }
        return index;
    }
    
    public boolean checkPlayer(Entity entity) {
        
        // Hitung titik tengah entity
        int entityCenterX = entity.worldX + entity.solidArea.x + (entity.solidArea.width / 2);
        int entityCenterY = entity.worldY + entity.solidArea.y + (entity.solidArea.height / 2);
        
        // Hitung titik tengah player
        int playerCenterX = gp.player.worldX + gp.player.solidArea.x + (gp.player.solidArea.width / 2);
        int playerCenterY = gp.player.worldY + gp.player.solidArea.y + (gp.player.solidArea.height / 2);
        
        // Hitung jarak antara titik tengah
        int distanceX = Math.abs(entityCenterX - playerCenterX);
        int distanceY = Math.abs(entityCenterY - playerCenterY);
        
        // Tentukan threshold untuk collision
        int thresholdX = (entity.solidArea.width + gp.player.solidArea.width) / 4;
        int thresholdY = (entity.solidArea.height + gp.player.solidArea.height) / 4;
        
        // Cek jika titik tengah cukup dekat berdasarkan arah gerakan
        boolean isColliding = false;
        
        switch(entity.direction) {
        case "up":
            isColliding = distanceX < thresholdX && 
                         entityCenterY - entity.speed <= playerCenterY + thresholdY &&
                         entityCenterY - entity.speed >= playerCenterY - thresholdY;
            break;
        case "down":
            isColliding = distanceX < thresholdX && 
                         entityCenterY + entity.speed <= playerCenterY + thresholdY &&
                         entityCenterY + entity.speed >= playerCenterY - thresholdY;
            break;
        case "left":
            isColliding = distanceY < thresholdY && 
                         entityCenterX - entity.speed <= playerCenterX + thresholdX &&
                         entityCenterX - entity.speed >= playerCenterX - thresholdX;
            break;
        case "right":
            isColliding = distanceY < thresholdY && 
                         entityCenterX + entity.speed <= playerCenterX + thresholdX &&
                         entityCenterX + entity.speed >= playerCenterX - thresholdX;
            break;
        }
        
        if(isColliding) {
            entity.collisionOn = true;
            return true;
        }
        
        return false;
    }
    
    // Method untuk mengecek tile di koordinat tertentu
    public boolean isTileCollision(int col, int row) {
        if (col < 0 || col >= gp.maxWorldCol || row < 0 || row >= gp.maxWorldRow) {
            return true; // Di luar map = collision
        }
        
        int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
        if (gp.tileM.tile[gp.currentMap][tileNum] != null) {
            return gp.tileM.tile[gp.currentMap][tileNum].collision;
        }
        return false;
    }
    
    // Method untuk mengecek apakah titik tertentu berada di tile yang collision
    public boolean isPointInCollisionTile(int worldX, int worldY) {
        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;
        
        return isTileCollision(col, row);
    }
    
    // Method untuk mendapatkan tile di posisi tertentu
    public int getTileAtPosition(int worldX, int worldY) {
        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;
        
        if (col >= 0 && col < gp.maxWorldCol && row >= 0 && row < gp.maxWorldRow) {
            return gp.tileM.mapTileNum[gp.currentMap][col][row];
        }
        return 0;
    }
    
    public int checkInteractiveTile(Entity entity) {
        int index = 999;
        
        if (gp.iTile == null || 
            gp.currentMap < 0 || 
            gp.currentMap >= gp.iTile.length || 
            gp.iTile[gp.currentMap] == null) {
            return index;
        }
        
        InteractiveTile[] tiles = gp.iTile[gp.currentMap];
        
        // Hitung titik tengah entity
        int entityCenterX = entity.worldX + entity.solidArea.x + (entity.solidArea.width / 2);
        int entityCenterY = entity.worldY + entity.solidArea.y + (entity.solidArea.height / 2);
        
        // Hitung titik attack berdasarkan arah
        int attackX = entityCenterX;
        int attackY = entityCenterY;
        
        switch(entity.direction) {
            case "up":
                attackY -= entity.attackArea.height;
                break;
            case "down":
                attackY += entity.attackArea.height;
                break;
            case "left":
                attackX -= entity.attackArea.width;
                break;
            case "right":
                attackX += entity.attackArea.width;
                break;
        }
        
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] != null && tiles[i].destructible) {
                // Hitung titik tengah interactive tile
                int tileCenterX = tiles[i].worldX + tiles[i].solidArea.x + (tiles[i].solidArea.width / 2);
                int tileCenterY = tiles[i].worldY + tiles[i].solidArea.y + (tiles[i].solidArea.height / 2);
                
                // Hitung jarak antara titik attack dan titik tengah tile
                int distanceX = Math.abs(attackX - tileCenterX);
                int distanceY = Math.abs(attackY - tileCenterY);
                
                // Tentukan threshold (gunakan ukuran attack area sebagai referensi)
                int threshold = Math.min(entity.attackArea.width, entity.attackArea.height) / 2;
                
                // Cek jika titik attack cukup dekat dengan titik tengah tile
                if (distanceX < threshold && distanceY < threshold) {
                    index = i;
                    break; // Stop after first collision
                }
            }
        }
        
        return index;
    }
    
    // Cek apakah entity berada tepat di tengah tile secara horizontal
    public boolean isAtHorizontalTileCenter(Entity entity) {
        int entityCenterX = entity.worldX + entity.solidArea.x + (entity.solidArea.width / 2);
        int tileLeft = (entityCenterX / gp.tileSize) * gp.tileSize;
        int tileCenterX = tileLeft + (gp.tileSize / 2);
        
        return Math.abs(entityCenterX - tileCenterX) <= 2; // Toleransi 2 pixel
    }
    
    // Cek apakah entity berada tepat di tengah tile secara vertikal
    public boolean isAtVerticalTileCenter(Entity entity) {
        int entityCenterY = entity.worldY + entity.solidArea.y + (entity.solidArea.height / 2);
        int tileTop = (entityCenterY / gp.tileSize) * gp.tileSize;
        int tileCenterY = tileTop + (gp.tileSize / 2);
        
        return Math.abs(entityCenterY - tileCenterY) <= 2;
    }
}