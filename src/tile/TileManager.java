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
	public int mapTileNum[][][];
	
	public TileManager(GamePanel gp) {
		
		this.gp = gp;
		mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
		
		tile = new Tile[300];
		
		getTileImage();	
		loadMap("/maps/maps1.txt",0);
		loadMap("/maps/maps2.txt",1);
	}
	
	public void getTileImage() {
			
			//Pembatas
			setup(0, "air", true); //dipakai
//			setup(1, "batu1", false);
			setup(2, "sungai11", true); //dipakai
			setup(3, "sungai01", true); //dipakai
			setup(4, "jembatan1", false); //dipakai
			setup(5, "rumah", true); //dipakai
			setup(6, "pohon3", true); //dipakai
			setup(7, "jalan01", false); //dipakai
			setup(8, "pohon5", true); //dipakai
			setup(9, "batu4", false); //dipakai
			setup(10, "jalan04", false);
			setup(11, "jalan4", false); //dipakai
			setup(12, "jalan11", false); //dipakai
			setup(13, "jalan12", false); //dipakai
			setup(14, "jalan13", false); //dipakai
			setup(15, "jalan14", false); //dipakai
			setup(16, "jalan15", false); //dipakai
			setup(17, "jalan4", true); //dipakai
			setup(18, "batu2", true);
			setup(19, "batu3", true);
			setup(20, "semak1", false); //dipakai
			setup(21, "semak2", false);
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
			setup(59, "tanah", false); //dipakai
			setup(60, "sungai6", true); //dipakai
			setup(61, "sungai7", true); //dipakai
			setup(62, "sungai3", true); //dipakai
			setup(63, "pohon2", true); //dipakai
			setup(64, "trunk", false); //dipakai
			setup(65, "portal", false);
			
			//map ke 2
			setup(0, "hitam", false);
			setup(1, "tile001", false);
			setup(2, "tile002", false);
			setup(3, "tile003", true);
			setup(4, "tile004", false);
			setup(5, "tile005", false);
//			setup(6, "portal", false);
//			setup(7, "portal", false);
//			setup(8, "portal", false);
//			setup(9, "portal", false);

//			setup(10, "portal", false);
//			setup(11, "portal", false);
//			setup(12, "portal", false);
//			setup(13, "portal", false);
//			setup(14, "portal", false);
//			setup(15, "portal", false);
//			setup(16, "portal", false);
//			setup(17, "portal", false);
//			setup(18, "portal", false);
//			setup(19, "portal", false);

//			setup(20, "portal", false);
			setup(21, "tile026", false);
			setup(22, "tile027", false);
			setup(23, "tile028", false);
			setup(24, "tile029", false);
//			setup(25, "portal", false);
			setup(26, "tile032", false);
			setup(27, "portal", false);
//			setup(28, "portal", false);
//			setup(29, "portal", false);  

//			setup(30, "portal", false);
//			setup(31, "portal", false);
//			setup(32, "portal", false);
//			setup(33, "portal", false);
//			setup(34, "portal", false);
//			setup(35, "portal", false);
			setup(36, "tile048", false);
			setup(37, "tile049", false);
			setup(38, "tile050", true);
			setup(39, "tile051", false);

			setup(40, "tile052", false);
			setup(41, "tile053", false);
//			setup(42, "portal", false);
//			setup(43, "portal", false);
//			setup(44, "portal", false);
//			setup(45, "portal", false);
//			setup(46, "portal", false);
//			setup(47, "portal", false);
//			setup(48, "portal", false);
//			setup(49, "portal", false);

//			setup(50, "portal", false);
//			setup(51, "portal", false);
//			setup(52, "portal", false);
			setup(53, "tile072", false);
			setup(54, "tile073", false);
			setup(55, "tile074", false);
			setup(56, "tile075", false);
			setup(57, "tile076", false);
			setup(58, "tile077", false);
//			setup(59, "portal", false);

			setup(60, "tile080", false);
//			setup(61, "portal", false);
//			setup(62, "portal", false);
//			setup(63, "portal", false);
//			setup(64, "portal", false);
			setup(65, "tile096", false);
			setup(66, "tile097", false);
			setup(67, "tile098", false);
			setup(68, "tile099", false);
			setup(69, "tile100", false);

			setup(70, "tile101", false);
//			setup(71, "portal", false);
			setup(72, "tile104", false);
//			setup(73, "portal", false);
//			setup(74, "portal", false);
//			setup(75, "portal", false);
			setup(76, "tile111", true);
//			setup(77, "portal", false);
//			setup(78, "portal", false);
//			setup(79, "portal", false);

//			setup(80, "portal", false);
//			setup(81, "portal", false);
//			setup(82, "portal", false);
//			setup(83, "portal", false);
//			setup(84, "portal", false);
//			setup(85, "portal", false);
//			setup(86, "portal", false);
//			setup(87, "portal", false);
//			setup(88, "portal", false);
			setup(89, "tile144", false);

			setup(90, "tile145", false);
			setup(91, "tile146", false);
			setup(92, "tile148", false);
//			setup(93, "portal", false);
//			setup(94, "portal", false);
//			setup(95, "portal", false);
//			setup(96, "portal", false);
//			setup(97, "portal", false);
//			setup(98, "portal", false);
//			setup(99, "portal", false);

//			setup(100, "portal", false);
//			setup(101, "portal", false);
//			setup(102, "portal", false);
			setup(103, "tile171", false);
			setup(104, "tile172", false);
			setup(105, "tile173", false);
//			setup(106, "portal", false);
//			setup(107, "portal", false);
//			setup(108, "portal", false);
//			setup(109, "portal", false);

//			setup(110, "portal", false);
//			setup(111, "portal", false);
//			setup(112, "portal", false);
//			setup(113, "portal", false);
//			setup(114, "portal", false);
//			setup(115, "portal", false);
//			setup(116, "portal", false);
			setup(117, "tile220", false);
			setup(118, "tile221", false);
			setup(119, "tile222", false);

//			setup(120, "portal", false);
			setup(121, "tile224", true);
//			setup(122, "portal", false);
//			setup(123, "portal", false);
//			setup(124, "portal", false);
//			setup(125, "portal", false);
//			setup(126, "portal", false);
//			setup(127, "portal", false);
//			setup(128, "portal", false);
			setup(129, "tile244", false);

			setup(130, "tile246", false);
//			setup(131, "portal", false);
//			setup(132, "portal", false);
//			setup(133, "portal", false);
//			setup(134, "portal", false);
//			setup(135, "portal", false);
//			setup(136, "portal", false);
//			setup(137, "portal", false);
//			setup(138, "portal", false);
//			setup(139, "portal", false);

			setup(140, "tile268", false);
			setup(141, "tile269", false);
			setup(142, "tile270", false);
			setup(143, "tile271(2)", false);
			setup(144, "tile271", false);
			setup(145, "tile272(2)", false);
			setup(146, "tile272", false);
//			setup(147, "portal", false);
//			setup(148, "portal", false);
//			setup(149, "portal", false);

//			setup(150, "portal", false);
//			setup(151, "portal", false);
//			setup(152, "portal", false);
//			setup(153, "portal", false);
//			setup(154, "portal", false);
//			setup(155, "portal", false);
//			setup(156, "portal", false);
//			setup(157, "portal", false);
//			setup(158, "portal", false);
//			setup(159, "portal", false);

//			setup(160, "portal", false);
//			setup(161, "portal", false);
//			setup(162, "portal", false);
//			setup(163, "portal", false);
//			setup(164, "portal", false);
//			setup(165, "portal", false);
//			setup(166, "portal", false);
//			setup(167, "portal", false);
//			setup(168, "portal", false);
//			setup(169, "portal", false);

//			setup(170, "portal", false);
//			setup(171, "portal", false);
//			setup(172, "portal", false);
//			setup(173, "portal", false);
//			setup(174, "portal", false);
//			setup(175, "portal", false);
//			setup(176, "portal", false);
//			setup(177, "portal", false);
//			setup(178, "portal", false);
//			setup(179, "portal", false);

//			setup(180, "portal", false);
//			setup(181, "portal", false);
//			setup(182, "portal", false);
//			setup(183, "portal", false);
//			setup(184, "portal", false);
//			setup(185, "portal", false);
//			setup(186, "portal", false);
//			setup(187, "portal", false);
//			setup(188, "portal", false);
//			setup(189, "portal", false);

//			setup(190, "portal", false);
//			setup(191, "portal", false);
//			setup(192, "portal", false);
//			setup(193, "portal", false);
//			setup(194, "portal", false);
//			setup(195, "portal", false);
//			setup(196, "portal", false);
//			setup(197, "portal", false);
//			setup(198, "portal", false);
//			setup(199, "portal", false);

//			setup(200, "portal", false);
//			setup(201, "portal", false);
//			setup(202, "portal", false);
//			setup(203, "portal", false);
//			setup(204, "portal", false);
//			setup(205, "portal", false);
//			setup(206, "portal", false);
//			setup(207, "portal", false);
//			setup(208, "portal", false);
//			setup(209, "portal", false);
//
//			setup(210, "portal", false);
//			setup(211, "portal", false);
//			setup(212, "portal", false);
//			setup(213, "portal", false);
//			setup(214, "portal", false);
//			setup(215, "portal", false);
//			setup(216, "portal", false);
//			setup(217, "portal", false);
//			setup(218, "portal", false);
//			setup(219, "portal", false);
//
//			setup(220, "portal", false);
//			setup(221, "portal", false);
//			setup(222, "portal", false);
//			setup(223, "portal", false);
//			setup(224, "portal", false);
//			setup(225, "portal", false);
//			setup(226, "portal", false);
//			setup(227, "portal", false);
//			setup(228, "portal", false);
//			setup(229, "portal", false);

//			setup(230, "portal", false);
//			setup(231, "portal", false);
//			setup(232, "portal", false);
//			setup(233, "portal", false);
//			setup(234, "portal", false);
//			setup(235, "portal", false);
//			setup(236, "portal", false);
//			setup(237, "portal", false);
//			setup(238, "portal", false);
//			setup(239, "portal", false);
//
//			setup(240, "portal", false);
//			setup(241, "portal", false);
//			setup(242, "portal", false);
//			setup(243, "portal", false);
//			setup(244, "portal", false);
//			setup(245, "portal", false);
//			setup(246, "portal", false);
//			setup(247, "portal", false);
//			setup(248, "portal", false);
//			setup(249, "portal", false);
//
//			setup(250, "portal", false);
//			setup(251, "portal", false);
//			setup(252, "portal", false);
//			setup(253, "portal", false);
//			setup(254, "portal", false);
//			setup(255, "portal", false);
//			setup(256, "portal", false);
//			setup(257, "portal", false);
//			setup(258, "portal", false);
//			setup(259, "portal", false);
//
//			setup(260, "portal", false);
//			setup(261, "portal", false);
//			setup(262, "portal", false);
//			setup(263, "portal", false);
//			setup(264, "portal", false);
//			setup(265, "portal", false);
//			setup(266, "portal", false);
//			setup(267, "portal", false);
//			setup(268, "portal", false);
//			setup(269, "portal", false);
//
//			setup(270, "portal", false);
//			setup(271, "portal", false);
//			setup(272, "portal", false);
//			setup(273, "portal", false);
//			setup(274, "portal", false);
//			setup(275, "portal", false);
//			setup(276, "portal", false);


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
	
	public void loadMap(String filePath, int map) {
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
					
					mapTileNum[map][col][row] = num;
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
			
			int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];
			
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