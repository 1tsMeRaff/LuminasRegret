package entity;

import java.awt.Rectangle;
import java.util.Random;

import ai.Node;
import main.GamePanel;

public class NPC_Guide extends Entity {
    
    public NPC_Guide(GamePanel gp) {
        super(gp);
        
        direction = "down";
        speed = 1; // ⬅️ Tambah speed untuk testing
        
        // Solid Area yang lebih kecil untuk presisi
        solidArea = new Rectangle(12, 20, 24, 28); // Lebih ke tengah
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        getImage();
        setDialogue();
        
        // Inisialisasi variabel pathfinding
        goalCol = -1;
        goalRow = -1;
        onPath = false;
    }
    
    // --------------------------------------------------
    // PERBAIKI PERHITUNGAN POSISI TILE
    // --------------------------------------------------
    public int getCurrentTileX() {
        // Hitung tile berdasarkan worldX (bukan dengan solidArea offset)
        return (worldX + solidArea.x + solidArea.width/2) / gp.tileSize;
    }
    
public void getImage() {
		
		up1 = setup("/npc/up1", gp.tileSize, gp.tileSize);
	    up2 = setup("/npc/up2", gp.tileSize, gp.tileSize);
	    down1 = setup("/npc/down1", gp.tileSize, gp.tileSize); 
	    down2 = setup("/npc/down2", gp.tileSize, gp.tileSize);
	    left1 = setup("/npc/left1", gp.tileSize, gp.tileSize);
	    left2 = setup("/npc/left2", gp.tileSize, gp.tileSize);
		right1 = setup("/npc/right1", gp.tileSize, gp.tileSize);
		right2 = setup("/npc/right2", gp.tileSize, gp.tileSize);
	}
	public void setDialogue() {
		
		
		dialogues[0] = "Hello World";
		dialogues[1] = "Hello World 2";
		dialogues[2] = "Hello World 3";
		dialogues[3] = "Hello World 4";
		dialogues[4] = "Hello World 5";
	}
    
    public int getCurrentTileY() {
        return (worldY + solidArea.y + solidArea.height/2) / gp.tileSize;
    }
    
    public int getTileCenterX(int tileX) {
        // Hitung posisi pixel tengah tile X
        return tileX * gp.tileSize + gp.tileSize/2;
    }
    
    public int getTileCenterY(int tileY) {
        // Hitung posisi pixel tengah tile Y
        return tileY * gp.tileSize + gp.tileSize/2;
    }
    
    public boolean isAtTileCenter() {
        // Cek apakah NPC sudah di tengah tile
        int centerX = worldX + solidArea.x + solidArea.width/2;
        int centerY = worldY + solidArea.y + solidArea.height/2;
        
        int tileX = centerX / gp.tileSize;
        int tileY = centerY / gp.tileSize;
        
        int tileCenterX = getTileCenterX(tileX);
        int tileCenterY = getTileCenterY(tileY);
        
        int dx = Math.abs(tileCenterX - centerX);
        int dy = Math.abs(tileCenterY - centerY);
        
        return dx < 2 && dy < 2; // Toleransi 2 pixel
    }
    
    public void alignToTileCenter() {
        // Paksa NPC ke tengah tile
        int currentCol = getCurrentTileX();
        int currentRow = getCurrentTileY();
        
        int targetX = getTileCenterX(currentCol) - solidArea.width/2 - solidArea.x;
        int targetY = getTileCenterY(currentRow) - solidArea.height/2 - solidArea.y;
        
        worldX = targetX;
        worldY = targetY;
        
        System.out.println("Aligned NPC to tile center: (" + currentCol + ", " + currentRow + ")");
    }
    
    // --------------------------------------------------
    // PERBAIKI setAction()
    // --------------------------------------------------
    @Override
    public void setAction() {
        if (onPath) {
            System.out.println("\n🎯 PATH MODE");
            
            int currentCol = getCurrentTileX();
            int currentRow = getCurrentTileY();
            
            System.out.println("Current tile: (" + currentCol + ", " + currentRow + ")");
            System.out.println("Goal tile: (" + goalCol + ", " + goalRow + ")");
            
            // Cek jika sudah sampai
            if (currentCol == goalCol && currentRow == goalRow) {
                // Pastikan di tengah tile sebelum berhenti
                if (isAtTileCenter()) {
                    System.out.println("✅ REACHED DESTINATION!");
                    onPath = false;
                    direction = "down";
                    gp.pFinder.pathList.clear();
                } else {
                    // Bergerak ke tengah tile
                    moveToTileCenter(currentCol, currentRow);
                }
                return;
            }
            
            // Cari path jika kosong
            if (gp.pFinder.pathList.isEmpty()) {
                System.out.println("🔍 Searching path...");
                boolean found = gp.pFinder.search(currentCol, currentRow, goalCol, goalRow);
                
                if (found) {
                    System.out.println("✅ Path found! Nodes: " + gp.pFinder.pathList.size());
                    if (!gp.pFinder.pathList.isEmpty()) {
                        followImprovedPath();
                    }
                } else {
                    System.out.println("❌ Path not found!");
                    onPath = false;
                }
            } else {
                followImprovedPath();
            }
        } else {
            // Random movement
            randomMovement();
        }
    }
    
    // --------------------------------------------------
    // PATH FOLLOWING YANG LEBIH BAIK
    // --------------------------------------------------
    private void followImprovedPath() {
        if (gp.pFinder.pathList.isEmpty()) {
            System.out.println("Path list empty");
            return;
        }
        
        Node nextNode = gp.pFinder.pathList.get(0);
        int nextCol = nextNode.col;
        int nextRow = nextNode.row;
        
        // Hitung posisi tengah tile target
        int targetCenterX = getTileCenterX(nextCol);
        int targetCenterY = getTileCenterY(nextRow);
        
        // Hitung posisi tengah NPC
        int npcCenterX = worldX + solidArea.x + solidArea.width/2;
        int npcCenterY = worldY + solidArea.y + solidArea.height/2;
        
        // Hitung jarak ke target
        int dx = targetCenterX - npcCenterX;
        int dy = targetCenterY - npcCenterY;
        
        System.out.println("NPC Center: (" + npcCenterX + ", " + npcCenterY + ")");
        System.out.println("Target Center: (" + targetCenterX + ", " + targetCenterY + ")");
        System.out.println("Distance: (" + dx + ", " + dy + ")");
        
        // Threshold: jika sangat dekat, anggap sudah mencapai node
        int threshold = gp.tileSize / 8; // 1/8 tile (6 pixel jika tileSize=48)
        
        if (Math.abs(dx) < threshold && Math.abs(dy) < threshold) {
            System.out.println("✓ Reached node (" + nextCol + ", " + nextRow + ")");
            gp.pFinder.pathList.remove(0);
            
            // Posisikan NPC tepat di tengah tile
            alignToTile(nextCol, nextRow);
            
            if (gp.pFinder.pathList.isEmpty()) {
                System.out.println("✓ Finished all path nodes");
            }
            return;
        }
        
        // Tentukan arah dengan smoothing
        determineDirection(dx, dy);
    }
    
    private void alignToTile(int tileX, int tileY) {
        // Posisikan NPC tepat di tengah tile
        int targetX = getTileCenterX(tileX) - solidArea.width/2 - solidArea.x;
        int targetY = getTileCenterY(tileY) - solidArea.height/2 - solidArea.y;
        
        worldX = targetX;
        worldY = targetY;
        
        System.out.println("Aligned to tile (" + tileX + ", " + tileY + ")");
    }
    
    private void determineDirection(int dx, int dy) {
        // Prioritaskan axis dengan jarak terbesar
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                direction = "right";
                System.out.println("→ Moving RIGHT to reach center");
            } else {
                direction = "left";
                System.out.println("← Moving LEFT to reach center");
            }
        } else {
            if (dy > 0) {
                direction = "down";
                System.out.println("↓ Moving DOWN to reach center");
            } else {
                direction = "up";
                System.out.println("↑ Moving UP to reach center");
            }
        }
    }
    
    private void moveToTileCenter(int tileX, int tileY) {
        // Bergerak ke tengah tile saat sudah di tile yang benar
        int targetCenterX = getTileCenterX(tileX);
        int targetCenterY = getTileCenterY(tileY);
        
        int npcCenterX = worldX + solidArea.x + solidArea.width/2;
        int npcCenterY = worldY + solidArea.y + solidArea.height/2;
        
        int dx = targetCenterX - npcCenterX;
        int dy = targetCenterY - npcCenterY;
        
        if (Math.abs(dx) > Math.abs(dy)) {
            direction = (dx > 0) ? "right" : "left";
        } else {
            direction = (dy > 0) ? "down" : "up";
        }
        
        System.out.println("Centering to tile (" + tileX + ", " + tileY + ")");
    }
    
    // --------------------------------------------------
    // PERBAIKI speak() METHOD
    // --------------------------------------------------
    @Override
    public void speak() {
        super.speak();
        
        System.out.println("\n🚀 ACTIVATING PATHFINDING");
        
        // Pastikan NPC di tengah tile sebelum mulai
        if (!isAtTileCenter()) {
            System.out.println("Aligning NPC to tile center first...");
            alignToTileCenter();
        }
        
        int currentCol = getCurrentTileX();
        int currentRow = getCurrentTileY();
        
        // Pilih goal yang tidak solid dan berbeda dari posisi saat ini
        int[][] testGoals = {
            {currentCol + 4, currentRow},     // 4 kanan
            {currentCol, currentRow + 4},     // 4 bawah
            {currentCol + 3, currentRow + 3}, // diagonal
            {currentCol - 3, currentRow},     // 3 kiri
            {currentCol, currentRow - 3}      // 3 atas
        };
        
        for (int[] goal : testGoals) {
            int col = goal[0];
            int row = goal[1];
            
            if (col >= 0 && col < gp.maxWorldCol && 
                row >= 0 && row < gp.maxWorldRow) {
                
                int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
                boolean collision = gp.tileM.tile[gp.currentMap][tileNum].collision;
                
                if (!collision && (col != currentCol || row != currentRow)) {
                    goalCol = col;
                    goalRow = row;
                    System.out.println("✅ Goal set to: (" + goalCol + ", " + goalRow + ")");
                    break;
                }
            }
        }
        
        if (goalCol == -1) {
            // Fallback: cari tile bebas terdekat
            for (int radius = 1; radius <= 5; radius++) {
                for (int c = currentCol - radius; c <= currentCol + radius; c++) {
                    for (int r = currentRow - radius; r <= currentRow + radius; r++) {
                        if (c >= 0 && c < gp.maxWorldCol && 
                            r >= 0 && r < gp.maxWorldRow &&
                            (c != currentCol || r != currentRow)) {
                            
                            int tileNum = gp.tileM.mapTileNum[gp.currentMap][c][r];
                            boolean collision = gp.tileM.tile[gp.currentMap][tileNum].collision;
                            
                            if (!collision) {
                                goalCol = c;
                                goalRow = r;
                                System.out.println("✅ Fallback goal: (" + goalCol + ", " + goalRow + ")");
                                radius = 6; // Exit loops
                                c = currentCol + 6;
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        onPath = true;
        gp.pFinder.pathList.clear();
        
        System.out.println("Start tile: (" + currentCol + ", " + currentRow + ")");
        System.out.println("Goal tile: (" + goalCol + ", " + goalRow + ")");
        System.out.println("NPC world pos: (" + worldX + ", " + worldY + ")");
    }
    
    // --------------------------------------------------
    // RANDOM MOVEMENT
    // --------------------------------------------------
    private void randomMovement() {
        actionLockCounter++;
        if (actionLockCounter >= 120) {
            Random random = new Random();
            int i = random.nextInt(100);
            
            if (i < 25) direction = "up";
            else if (i < 50) direction = "down";
            else if (i < 75) direction = "left";
            else direction = "right";
            
            actionLockCounter = 0;
        }
    }
    
    // --------------------------------------------------
    // DEBUG METHODS
    // --------------------------------------------------
    public void printPositionInfo() {
        int tileX = getCurrentTileX();
        int tileY = getCurrentTileY();
        int centerX = worldX + solidArea.x + solidArea.width/2;
        int centerY = worldY + solidArea.y + solidArea.height/2;
        int tileCenterX = getTileCenterX(tileX);
        int tileCenterY = getTileCenterY(tileY);
        
        System.out.println("\n📊 POSITION DEBUG:");
        System.out.println("World: (" + worldX + ", " + worldY + ")");
        System.out.println("Tile: (" + tileX + ", " + tileY + ")");
        System.out.println("NPC Center: (" + centerX + ", " + centerY + ")");
        System.out.println("Tile Center: (" + tileCenterX + ", " + tileCenterY + ")");
        System.out.println("Offset: (" + (centerX - tileCenterX) + ", " + (centerY - tileCenterY) + ")");
        System.out.println("At center: " + isAtTileCenter());
    }
    
    public void testSimplePath(int steps) {
        System.out.println("\n🧪 SIMPLE PATH TEST - " + steps + " steps right");
        
        alignToTileCenter();
        
        int currentCol = getCurrentTileX();
        int currentRow = getCurrentTileY();
        
        goalCol = 30;
        goalRow = 30;
        
        onPath = true;
        gp.pFinder.pathList.clear();
        
        System.out.println("Testing from (" + currentCol + ", " + currentRow + 
                         ") to (" + goalCol + ", " + goalRow + ")");
    }
}