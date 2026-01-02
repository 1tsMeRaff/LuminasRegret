package main;

import entity.NPC_Guide;
import entity.NPC_Merchant;
import monster.MON_GreenSlime;
import object.OBJ_Axe;
import object.OBJ_Bread;
import object.OBJ_Coin_Bronze;
import object.OBJ_Key;
import object.OBJ_PlayerMana;
import object.OBJ_Portal;
import object.OBJ_Shield_Iron;
import tile_interactive.IT_DryTree;


public class AssetSetter {
	
	GamePanel gp;
	
	public AssetSetter(GamePanel gp) {
	    this.gp = gp;
	}

	public void setObject() {
		
		int mapNum = 0;
		int i = 0;	
//		gp.obj[i] = new OBJ_Portal(gp);
//		gp.obj[i].worldX = gp.tileSize*21;
//		gp.obj[i].worldY = gp.tileSize*22;
//		i++;
		
		gp.obj[mapNum][i] = new OBJ_Coin_Bronze(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize*22;
		gp.obj[mapNum][i].worldY = gp.tileSize*22;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_PlayerMana(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize*30;
		gp.obj[mapNum][i].worldY = gp.tileSize*20;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Key(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize*10;
		gp.obj[mapNum][i].worldY = gp.tileSize*36;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Axe(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize*12;
		gp.obj[mapNum][i].worldY = gp.tileSize*17;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Shield_Iron(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize*10;
		gp.obj[mapNum][i].worldY = gp.tileSize*12;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Bread(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize*12;
		gp.obj[mapNum][i].worldY = gp.tileSize*26;
		i++;
		
	}
	public void setNPC() {
		
		int mapNum = 0;
		int i = 0;
		
		// Map 0
		gp.npc[mapNum][i] = new NPC_Guide(gp);
		gp.npc[mapNum][i].worldX = gp.tileSize*24;
		gp.npc[mapNum][i].worldY = gp.tileSize*24;
		i++;
		
		gp.npc[mapNum][i] = new NPC_Merchant(gp);
		gp.npc[mapNum][i].worldX = gp.tileSize*12;
		gp.npc[mapNum][i].worldY = gp.tileSize*19;
		i++;
		
		// Map 1
		mapNum = 1;
		i = 0;
		gp.npc[mapNum][i] = new NPC_Merchant(gp);
		gp.npc[mapNum][i].worldX = gp.tileSize*23;
		gp.npc[mapNum][i].worldY = gp.tileSize*18;
		i++;
	}
	public void setMonster() {
		
		int mapNum = 0;
		int i = 1;
		gp.monster[mapNum][i] = new MON_GreenSlime(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 11;
		gp.monster[mapNum][i].worldY = gp.tileSize * 27;
		i++;
		
		gp.monster[mapNum][i] = new MON_GreenSlime(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 26;
		gp.monster[mapNum][i].worldY = gp.tileSize * 24;
		i++;
		
		gp.monster[mapNum][i] = new MON_GreenSlime(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 22;
		gp.monster[mapNum][i].worldY = gp.tileSize * 25;
		i++;
		
		gp.monster[mapNum][i] = new MON_GreenSlime(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 26;
		gp.monster[mapNum][i].worldY = gp.tileSize * 30;
		i++;
		
		gp.monster[mapNum][i] = new MON_GreenSlime(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 31;
		gp.monster[mapNum][i].worldY = gp.tileSize * 24;
		i++;
		
//		mapNum = 1;
//		gp.monster[mapNum][i] = new MON_GreenSlime(gp);
//		gp.monster[mapNum][i].worldX = gp.tileSize * 31;
//		gp.monster[mapNum][i].worldY = gp.tileSize * 24;
//		i++;
	}
	
	public void setInteractiveTile () {
		
		int mapNum = 0;
		int i = 0;
		
		int maxTiles = gp.iTile[mapNum].length;
		
		//kiri atas
		gp.iTile[mapNum][i] = new IT_DryTree(gp,12,11);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,13,11);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,12,12);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,13,12);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,12,13);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,13,13);i++;
		
		//bawah kolam
		gp.iTile[mapNum][i] = new IT_DryTree(gp,18,18);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,18,19);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,17,19);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,17,20);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,17,21);i++;
		
		//ujung kanan
		gp.iTile[mapNum][i] = new IT_DryTree(gp,34,11);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,34,12);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,35,12);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,35,13);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,34,13);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,33,13);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,32,13);i++;
		
		gp.iTile[mapNum][i] = new IT_DryTree(gp,37,14);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,38,15);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,37,15);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,36,15);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,35,15);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,34,15);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,36,16);i++;
		
		//ujung kana bagian kiri
		gp.iTile[mapNum][i] = new IT_DryTree(gp,27,15);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,27,16);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,27,17);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,28,17);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,30,17);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,28,18);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,30,18);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,32,18);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,30,19);i++;
		
		gp.iTile[mapNum][i] = new IT_DryTree(gp,33,20);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,34,20);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,35,21);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,34,21);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,33,21);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,32,21);i++;
		
		//tengah bagian kiri
		gp.iTile[mapNum][i] = new IT_DryTree(gp,13,26);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,13,27);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,12,27);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,12,28);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,11,28);i++;
		
		gp.iTile[mapNum][i] = new IT_DryTree(gp,12,35);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,13,35);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,12,36);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,14,36);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,12,37);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,13,37);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,12,38);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,13,38);i++;
		
		//kiri bawah
		gp.iTile[mapNum][i] = new IT_DryTree(gp,18,28);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,19,28);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,17,29);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,18,29);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,19,29);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,20,29);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,17,30);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,18,30);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,19,30);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,20,30);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,17,31);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,18,31);i++;
		
		//tengah bawah
		gp.iTile[mapNum][i] = new IT_DryTree(gp,19,35);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,21,35);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,22,35);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,27,35);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,29,35);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,18,36);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,20,36);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,23,36);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,24,36);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,25,36);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,28,36);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,29,36);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,19,37);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,21,37);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,23,37);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,24,37);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,27,37);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,30,37);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,18,38);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,22,38);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,26,38);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,29,38);i++;
		gp.iTile[mapNum][i] = new IT_DryTree(gp,31,38);i++;
		
		

	}

}
