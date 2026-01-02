package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
    
    GamePanel gp;
    public Tile[][] tile;
    public int mapTileNum[][][];
    boolean drawPath = true;
    
    public TileManager(GamePanel gp) {
        
        this.gp = gp;
        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
        
        tile = new Tile[gp.maxMap][300];  // Setiap map memiliki array tile sendiri
        
        getTileImage();    
        loadMap("/maps/maps1.txt", 0);
        loadMap("/maps/maps2.txt", 1);
    }
    
    public void getTileImage() {
        
        // Setup tile untuk world 0 (map pertama)
        setupWorld0();
        
        // Setup tile untuk world 1 (map kedua)
        setupWorld1();
    }
    
    private void setupWorld0() {
        setup(0, 0, "air", true); // dipakai
        setup(0, 2, "sungai11", true); // dipakai
        setup(0, 3, "sungai01", true); // dipakai
        setup(0, 4, "jembatan1", false); // dipakai
        setup(0, 5, "rumah", true); // dipakai
        setup(0, 6, "pohon3", true); // dipakai
        setup(0, 7, "jalan01", false); // dipakai
        setup(0, 8, "pohon5", true); // dipakai
        setup(0, 9, "batu4", false); // dipakai
        setup(0, 10, "jalan04", false);
        setup(0, 11, "jalan4", false); // dipakai
        setup(0, 12, "jalan11", false); // dipakai
        setup(0, 13, "jalan12", false); // dipakai
        setup(0, 14, "jalan13", false); // dipakai
        setup(0, 15, "jalan14", false); // dipakai
        setup(0, 16, "jalan15", false); // dipakai
        setup(0, 17, "jalan4", true); // dipakai
        setup(0, 18, "batu2", true);
        setup(0, 19, "batu3", true);
        setup(0, 20, "semak1", false); // dipakai
        setup(0, 21, "semak2", false);
        setup(0, 22, "jalan14", true);
        setup(0, 23, "jalan2", false); // dipakai
        setup(0, 24, "jalan3", false); // dipakai
        setup(0, 25, "jembatan2", true);
        setup(0, 26, "jalan5", false); // dipakai
        setup(0, 27, "jalan6", false); // dipakai
        setup(0, 28, "jalan7", false); // dipakai        
        setup(0, 29, "jalan8", false); // dipakai
        setup(0, 30, "pohon4", false);
        setup(0, 31, "jembatan2", false); // dipakai
        setup(0, 32, "jembatan3", false); // dipakai
        setup(0, 33, "rumah", false);
        setup(0, 34, "sungai04", false);
        setup(0, 35, "semak1", true);
        setup(0, 36, "semak2", true);
        setup(0, 37, "sungai07", true);
        setup(0, 38, "sungai02", true);
        setup(0, 39, "sungai03", true);
        setup(0, 40, "rumput", false); // dipakai
        setup(0, 41, "sungai05", true);
        setup(0, 42, "sungai06", true);
        setup(0, 43, "sungai01", false); // dipakai
        setup(0, 44, "sungai02", false); // dipakai
        setup(0, 45, "sungai03", false); // dipakai
        setup(0, 46, "sungai04", false); // dipakai
        setup(0, 47, "sungai05", false); // dipakai
        setup(0, 48, "sungai06", false); // dipakai
        setup(0, 49, "sungai07", false); // dipakai
        setup(0, 50, "sungai08", false); // dipakai
        setup(0, 51, "sungai6", true);
        setup(0, 52, "sungai5", true); // dipakai
        setup(0, 53, "sungai10", false); // dipakai
        setup(0, 54, "sungai11", false); // dipakai
        setup(0, 55, "sungai12", false); // dipakai
        setup(0, 56, "sungai2", true); // dipakai
        setup(0, 57, "sungai8", true);  // dipakai
        setup(0, 58, "sungai4", true); // dipakai
        setup(0, 59, "tanah", false); // dipakai
        setup(0, 60, "sungai6", true); // dipakai
        setup(0, 61, "sungai7", true); // dipakai
        setup(0, 62, "sungai3", true); // dipakai
        setup(0, 63, "pohon2", true); // dipakai
        setup(0, 64, "trunk", false); // dipakai
        setup(0, 65, "portal", false); // dipakai
    }
    
    private void setupWorld1() {
        // Untuk world 1, gunakan index yang sama tapi di array terpisah
        setup(1, 0, "hitam", false);
        setup(1, 1, "tile001", false);
        setup(1, 2, "tile002", false);
        setup(1, 3, "tile003", true);
        setup(1, 4, "tile004", false);
        setup(1, 5, "tile005", false);
        setup(1, 21, "tile026", false);
        setup(1, 22, "tile027", false);
        setup(1, 23, "tile028", false);
        setup(1, 24, "tile029", false);
        setup(1, 26, "tile032", false);
        setup(1, 27, "portal", false);
        setup(1, 36, "tile048", false);
        setup(1, 37, "tile049", false);
        setup(1, 38, "tile050", true);
        setup(1, 39, "tile051", false);
        setup(1, 40, "tile052", false);
        setup(1, 41, "tile053", false);
        setup(1, 53, "tile072", false);
        setup(1, 54, "tile073", false);
        setup(1, 55, "tile074", false);
        setup(1, 56, "tile075", false);
        setup(1, 57, "tile076", false);
        setup(1, 58, "tile077", false);
        setup(1, 60, "tile080", false);
        setup(1, 65, "tile096", false);
        setup(1, 66, "tile097", false);
        setup(1, 67, "tile098", false);
        setup(1, 68, "tile099", false);
        setup(1, 69, "tile100", false);
        setup(1, 70, "tile101", false);
        setup(1, 72, "tile104", false);
        setup(1, 76, "tile111", true);
        setup(1, 89, "tile144", false);
        setup(1, 90, "tile145", false);
        setup(1, 91, "tile146", false);
        setup(1, 92, "tile148", false);
        setup(1, 103, "tile171", false);
        setup(1, 104, "tile172", false);
        setup(1, 105, "tile173", false);
        setup(1, 117, "tile220", false);
        setup(1, 118, "tile221", false);
        setup(1, 119, "tile222", false);
        setup(1, 121, "tile224", true);
        setup(1, 129, "tile244", false);
        setup(1, 130, "tile246", false);
        setup(1, 140, "tile268", false);
        setup(1, 141, "tile269", false);
        setup(1, 142, "tile270", false);
        setup(1, 143, "tile271(2)", false);
        setup(1, 144, "tile271", false);
        setup(1, 145, "tile272(2)", false);
        setup(1, 146, "tile272", false);
        
        // Setup tile yang kosong di world 1 dengan placeholder
        for (int i = 0; i < 300; i++) {
            if (tile[1][i] == null) {
                // Jika tile belum disetup, buat tile kosong dengan collision false
                try {
                    tile[1][i] = new Tile();
                    tile[1][i].image = ImageIO.read(getClass().getResourceAsStream("/tiles/air.png"));
                    tile[1][i].image = new UtilityTool().scaleImage(tile[1][i].image, gp.tileSize, gp.tileSize);
                    tile[1][i].collision = false;
                } catch (IOException e) {
                    // Jika tidak ada file "air.png", buat tile dengan gambar null
                    tile[1][i] = new Tile();
                    tile[1][i].collision = false;
                }
            }
        }
    }
    
    public void setup(int mapIndex, int tileIndex, String imageName, boolean collision) {
        
        UtilityTool uTool = new UtilityTool();
        
        try {
            tile[mapIndex][tileIndex] = new Tile();
            tile[mapIndex][tileIndex].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
            tile[mapIndex][tileIndex].image = uTool.scaleImage(tile[mapIndex][tileIndex].image, gp.tileSize, gp.tileSize);
            tile[mapIndex][tileIndex].collision = collision;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadMap(String filePath, int map) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            int col = 0;
            int row = 0;
            
            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                
                String line = br.readLine();
                
                while (col < gp.maxWorldCol) {
                    
                    String numbers[] = line.split(" ");
                    
                    int num = Integer.parseInt(numbers[col]);
                    
                    mapTileNum[map][col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g2) {
        
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            
            int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];
            
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                
                // Gunakan currentMap untuk mengakses tile yang benar
                if (tile[gp.currentMap][tileNum] != null && tile[gp.currentMap][tileNum].image != null) {
                    g2.drawImage(tile[gp.currentMap][tileNum].image, screenX, screenY, null);
                }
            }
            
            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
        
        if(drawPath == true) {
        	
        	g2.setColor(new Color(255,0,0,70));
            for(int i = 0; i < gp.pFinder.pathList.size(); i++) {
                int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize;
                int worldY = gp.pFinder.pathList.get(i).row * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;
                g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
            }
        }
    }
    
    // Helper method untuk mendapatkan tile collision
    public boolean getTileCollision(int map, int col, int row) {
        int tileNum = mapTileNum[map][col][row];
        if (tile[map][tileNum] != null) {
            return tile[map][tileNum].collision;
        }
        return false;
    }
}