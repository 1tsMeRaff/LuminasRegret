package tile;

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
	public Tile[] tile;
	public int mapTileNum[][];
	
	public TileManager(GamePanel gp) {
		
		this.gp = gp;
		mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
		
		tile = new Tile[70];
		
		getTileImage();	
		loadMap("/maps/maps1.txt");
	}
	
	public void getTileImage() {
			
			//Pembatas
			setup(0, "air", true); //dipakai
//			setup(1, "batu1", false);
			setup(2, "sungai11", true); //dipakai
			setup(3, "sungai01", true); //dipakai
			setup(4, "jembatan1", false); //dipakai
//			setup(5, "pohon1", true);
			setup(6, "pohon3", true); //dipakai
			setup(7, "jalan01", false); //dipakai
			setup(8, "jalan02", false);
			setup(9, "jalan03", false);
			setup(10, "jalan04", false);
			setup(11, "jalan4", false); //dipakai
			setup(12, "jalan11", false); //dipakai
			setup(13, "jalan12", false); //dipakai
			setup(14, "jalan13", false); //dipakai
			setup(15, "jalan14", false); //dipakai
			setup(16, "jalan15", false); //dipakai
//			setup(17, "jalan7", true);
//			setup(18, "jalan8", true);
//			setup(19, "jalan2", true);
//			setup(20, "jalan12", true);
//			setup(21, "jalan13", true);
//			setup(22, "jalan14", true);
			setup(23, "jalan2", false); //dipakai
			setup(24, "jalan3", false); //dipakai
//			setup(25, "jembatan2", true);
			setup(26, "jalan5", false); //dipakai
			setup(27, "jalan6", false); //dipakai
			setup(28, "jalan7", false);	//dipakai		
			setup(29, "jalan8", false); //dipakai
//			setup(30, "pohon4", false);
			setup(31, "jembatan2", false); //dipakai
			setup(32, "jembatan3", false); //dipakai
//			setup(33, "rumah", false);
//			setup(34, "sungai04", false);
//			setup(35, "semak1", true);
//			setup(36, "semak2", true);
//			setup(37, "sungai07", true);
//			setup(38, "sungai02", true);
//			setup(39, "sungai03", true);
			setup(40, "rumput", false); //dipakai
//			setup(41, "sungai05", true);
//			setup(42, "sungai06", true);
			setup(43, "sungai01", false); //dipakai
			setup(44, "sungai02", false); //dipakai
			setup(45, "sungai03", false); //dipakai
			setup(46, "sungai04", false); //dipakai
			setup(47, "sungai05", false); //dipakai
			setup(48, "sungai06", false); //dipakai
			setup(49, "sungai07", false); //dipakai
			setup(50, "sungai08", false); //dipakai
//			setup(51, "sungai6", true);
			setup(52, "sungai5", true); //dipakai
			setup(53, "sungai10", false); //dipakai
			setup(54, "sungai11", false); //dipakai
			setup(55, "sungai12", false); //dipakai
			setup(56, "sungai2", true); //dipakai
			setup(57, "sungai8", true);  //dipakai
			setup(58, "sungai4", true); //dipakai
//			setup(59, "hitam3", true);
			setup(60, "sungai6", true); //dipakai
			setup(61, "sungai7", true); //dipakai
			setup(62, "sungai3", true); //dipakai
			setup(63, "pohon2", true); //dipakai
			setup(64, "trunk", false); //dipakai

	}
	
	public void setup(int index, String imageName, boolean collision) {
		
		UtilityTool uTool = new UtilityTool();
	    
	    try {
	        tile[index] = new Tile();
	        tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
	        tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
	        tile[index].collision = collision;
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void loadMap(String filePath) {
		try {
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			
			while(col < gp.maxWorldCol && row < gp.maxWorldRow ) {
				
				String line = br.readLine();
				
				while(col < gp.maxWorldCol) {
					
					String numbers[] = line.split(" ");
					
					int num = Integer.parseInt(numbers[col]);
					
					mapTileNum[col][row] = num;
					col++;
				}
				if(col == gp.maxWorldCol) {
					col = 0;
					row++;
				}
			}	 
			br.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		
		int worldCol = 0;
		int worldRow = 0;

		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow ) {
			
			int tileNum = mapTileNum[worldCol][worldRow];
			
			int worldX = worldCol * gp.tileSize;
			int worldY = worldRow * gp.tileSize;
			int screenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;
			
			if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
			   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
			   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
			   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
				
				g2.drawImage(tile[tileNum].image, screenX, screenY, null);
			}
			
			worldCol++;

			if(worldCol == gp.maxWorldCol) {
				worldCol = 0;
				worldRow++;

			}
		}
	}
}