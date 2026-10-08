package tile;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

@SuppressWarnings("this-escape")
public class TileManager {
    
    GamePanel gp;
    
    public Tile[][] tile;
    
    public int mapTileNum[][][];
    
    public TileManager(GamePanel gp) {
        
        this.gp = gp;
        
        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
        
        tile = new Tile[gp.maxMap][300];
        
        getTileImage();    
        loadMap("/maps/maps1.txt", 0); 
        loadMap("/maps/maps2.txt", 1); 
    }
    
    public void getTileImage() {
        
        setupWorld0();
        
        setupWorld1();
    }
    
    //Setup tile khusus untuk MAP 0
    private void setupWorld0() {
        setup(0, 0, "air", true); 
        setup(0, 2, "sungai11", true); 
        setup(0, 3, "sungai01", true); 
        setup(0, 4, "jembatan1", false); 
        setup(0, 5, "rumah", true); 
        setup(0, 6, "pohon3", true); 
        setup(0, 7, "jalan01", false); 
        setup(0, 8, "pohon5", true); 
        setup(0, 9, "batu4", false); 
        setup(0, 10, "jalan04", false);
        setup(0, 11, "jalan4", false);
        setup(0, 12, "jalan11", false); 
        setup(0, 13, "jalan12", false); 
        setup(0, 14, "jalan13", false); 
        setup(0, 15, "jalan14", false); 
        setup(0, 16, "jalan15", false);
        setup(0, 17, "jalan4", true);
        setup(0, 18, "batu2", true);
        setup(0, 19, "batu3", true);
        setup(0, 20, "semak1", false);
        setup(0, 21, "semak2", false);
        setup(0, 22, "jalan14", true);
        setup(0, 23, "jalan2", false);
        setup(0, 24, "jalan3", false);
        setup(0, 25, "jembatan2", true);
        setup(0, 26, "jalan5", false); 
        setup(0, 27, "jalan6", false); 
        setup(0, 28, "jalan7", false);         
        setup(0, 29, "jalan8", false); 
        setup(0, 30, "pohon4", false);
        setup(0, 31, "jembatan2", false); 
        setup(0, 32, "jembatan3", false); 
        setup(0, 33, "rumah", false);
        setup(0, 34, "sungai04", false);
        setup(0, 35, "semak1", true);
        setup(0, 36, "semak2", true);
        setup(0, 37, "sungai07", true);
        setup(0, 38, "sungai02", true);
        setup(0, 39, "sungai03", true);
        setup(0, 40, "rumput", false); 
        setup(0, 41, "sungai05", true);
        setup(0, 42, "sungai06", true);
        setup(0, 43, "sungai01", false); 
        setup(0, 44, "sungai02", false); 
        setup(0, 45, "sungai03", false); 
        setup(0, 46, "sungai04", false); 
        setup(0, 47, "sungai05", false); 
        setup(0, 48, "sungai06", false); 
        setup(0, 49, "sungai07", false); 
        setup(0, 50, "sungai08", false); 
        setup(0, 51, "sungai6", true);
        setup(0, 52, "sungai5", true); 
        setup(0, 53, "sungai10", false); 
        setup(0, 54, "sungai11", false); 
        setup(0, 55, "sungai12", false); 
        setup(0, 56, "sungai2", true); 
        setup(0, 57, "sungai8", true);  
        setup(0, 58, "sungai4", true); 
        setup(0, 59, "tanah", false); 
        setup(0, 60, "sungai6", true); 
        setup(0, 61, "sungai7", true); 
        setup(0, 62, "sungai3", true); 
        setup(0, 63, "pohon2", true); 
        setup(0, 64, "trunk", false); 
        setup(0, 65, "portal", false);
    }
    
    //Setup tile khusus untuk MAP 1
    private void setupWorld1() {
        setup(1, 0, "hitam", true);
        setup(1, 1, "tile001", false);
        setup(1, 2, "tile002", true);
        setup(1, 3, "tile003", true);
        setup(1, 4, "tile004", true);
        setup(1, 5, "tile005", true);
        setup(1, 21, "tile026", true);
        setup(1, 22, "tile027", true);
        setup(1, 23, "tile028", true);
        setup(1, 24, "tile029", true);
        setup(1, 26, "tile032", true);
        setup(1, 27, "portal2", false);
        setup(1, 36, "tile048", true);
        setup(1, 37, "tile049", true);
        setup(1, 38, "tile050", true);
        setup(1, 39, "tile051", true);
        setup(1, 40, "tile052", true);
        setup(1, 41, "tile053", false);
        setup(1, 53, "tile072", true);
        setup(1, 54, "tile073", true);
        setup(1, 55, "tile074", true);
        setup(1, 56, "tile075", true);
        setup(1, 57, "tile076", true);
        setup(1, 58, "tile077", true);
        setup(1, 60, "tile080", true);
        setup(1, 65, "tile096", true);
        setup(1, 66, "tile097", true);
        setup(1, 67, "tile098", true);
        setup(1, 68, "tile099", true);
        setup(1, 69, "tile100", true);
        setup(1, 70, "tile101", true);
        setup(1, 72, "tile104", true);
        setup(1, 76, "tile111", false);
        setup(1, 89, "tile144", true);
        setup(1, 90, "tile145", false);
        setup(1, 91, "tile146", true);
        setup(1, 92, "tile148", false);
        setup(1, 103, "tile171", true);
        setup(1, 104, "tile172", false);
        setup(1, 105, "tile173", true);
        setup(1, 117, "tile220", false);
        setup(1, 118, "tile221", false);
        setup(1, 119, "tile222", false);
        setup(1, 121, "tile224", false);
        setup(1, 129, "tile244", false);
        setup(1, 130, "tile246", false);
        setup(1, 140, "tile268", false);
        setup(1, 141, "tile269", false);
        setup(1, 142, "tile270", false);
        setup(1, 143, "tile271(2)", false);
        setup(1, 144, "tile271", false);
        setup(1, 145, "tile272(2)", false);
        setup(1, 146, "tile272", false);
        
        BufferedImage airImage = null;
        try (InputStream is = getClass().getResourceAsStream("/tiles/air.png")) {
            if (is != null) {
                BufferedImage rawAir = ImageIO.read(is);
                if (rawAir != null) {
                    airImage = new UtilityTool().scaleImage(rawAir, gp.tileSize, gp.tileSize);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 300; i++) {
            if (tile[1][i] == null) {
                tile[1][i] = new Tile();
                tile[1][i].image = airImage;
                tile[1][i].collision = false;
            }
        }
    }
    
    public void setup(int mapIndex, int tileIndex, String imageName, boolean collision) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage rawImage = null;
        try (InputStream is = getClass().getResourceAsStream("/tiles/" + imageName + ".png")) {
            if (is != null) {
                rawImage = ImageIO.read(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (rawImage == null) {
            java.io.File file = new java.io.File("res/tiles/" + imageName + ".png");
            if (file.exists()) {
                try {
                    rawImage = ImageIO.read(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (rawImage != null) {
            tile[mapIndex][tileIndex] = new Tile();
            tile[mapIndex][tileIndex].image = uTool.scaleImage(rawImage, gp.tileSize, gp.tileSize);
            tile[mapIndex][tileIndex].collision = collision;
        } else {
            System.err.println("Tile resource not found: /tiles/" + imageName + ".png");
        }
    }
    
    public void loadMap(String filePath, int map) {
        InputStream is = getClass().getResourceAsStream(filePath);
        if (is == null) {
            java.io.File file = new java.io.File("res" + filePath);
            if (file.exists()) {
                try {
                    is = new java.io.FileInputStream(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (is == null) {
            System.err.println("Map resource not found: " + filePath);
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8))) {
            int col = 0;
            int row = 0;
            
            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                    if (line == null) break;
                    
                    String numbers[] = line.split(" ");
                    while (col < gp.maxWorldCol && col < numbers.length) {
                        int num = Integer.parseInt(numbers[col]);
                        mapTileNum[map][col][row] = num;
                        col++;
                    }
                    if (col >= gp.maxWorldCol) {
                        col = 0;
                        row++;
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g2) {
        // Direct visible screen bounds culling (reduces loop iterations from 2500 to ~198 per frame)
        int startCol = Math.max(0, (gp.player.worldX - gp.player.screenX) / gp.tileSize - 1);
        int endCol = Math.min(gp.maxWorldCol - 1, (gp.player.worldX - gp.player.screenX + gp.screenWidth) / gp.tileSize + 1);
        int startRow = Math.max(0, (gp.player.worldY - gp.player.screenY) / gp.tileSize - 1);
        int endRow = Math.min(gp.maxWorldRow - 1, (gp.player.worldY - gp.player.screenY + gp.screenHeight) / gp.tileSize + 1);

        for (int worldRow = startRow; worldRow <= endRow; worldRow++) {
            for (int worldCol = startCol; worldCol <= endCol; worldCol++) {
                int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];
                if (tile[gp.currentMap][tileNum] != null && tile[gp.currentMap][tileNum].image != null) {
                    int worldX = worldCol * gp.tileSize;
                    int worldY = worldRow * gp.tileSize;
                    int screenX = worldX - gp.player.worldX + gp.player.screenX;
                    int screenY = worldY - gp.player.worldY + gp.player.screenY;
                    g2.drawImage(tile[gp.currentMap][tileNum].image, screenX, screenY, null);
                }
            }
        }
    }
    
    public boolean getTileCollision(int map, int col, int row) {
        int tileNum = mapTileNum[map][col][row];
        if (tile[map][tileNum] != null) {
            return tile[map][tileNum].collision;
        }
        return false;
    }
}