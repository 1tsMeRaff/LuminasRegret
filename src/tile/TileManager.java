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
		
		tile = new Tile[50];
		
		getTileImage();	
		loadMap("/maps/world01.txt");
	}
	
	public void getTileImage() {
			
			//Pembatas
			setup(0, "jalan1", false);
			setup(1, "jalan1", false);
			setup(2, "jalan1", false);
			setup(3, "jalan1", false);
			setup(4, "jalan1", false);
			setup(5, "jalan1", false);
			setup(6, "jalan1", false);
			setup(7, "jalan1", false);
			setup(8, "jalan1", false);
			setup(9, "jalan1", false);
			//Pembatas
			
			
			setup(10, "jalan1", false);
			setup(11, "jalan2", false);
			setup(12, "air1", true);
			setup(13, "air2", true);
			setup(14, "rumput1", false);
			setup(15, "rumput2", false);
			setup(16, "kolam11", true);
			setup(17, "kolam12", true);
			setup(18, "kolam13", true);
			setup(19, "kolam21", true);
			setup(20, "kolam22", true);
			setup(21, "kolam23", true);
			setup(22, "kolam31", true);
			setup(23, "kolam32", true);
			setup(24, "kolam33", true);
			setup(25, "sea11", true);
			setup(26, "sea12", true);
			setup(27, "sea21", true);
			setup(28, "sea22", true);
				
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