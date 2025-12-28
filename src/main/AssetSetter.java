package main;

import entity.NPC_Guide;
import monster.MON_GreenSlime;
import object.OBJ_Axe;
import object.OBJ_Bread;
import object.OBJ_Coin_Bronze;
import object.OBJ_Key;
import object.OBJ_Portal;
import object.OBJ_Shield_Iron;


public class AssetSetter {
	
	GamePanel gp;
	
	public AssetSetter(GamePanel gp) {
	    this.gp = gp;
	}

	public void setObject() {
		
		int i = 0;	
//		gp.obj[i] = new OBJ_Portal(gp);
//		gp.obj[i].worldX = gp.tileSize*21;
//		gp.obj[i].worldY = gp.tileSize*22;
//		i++;
		
		gp.obj[i] = new OBJ_Coin_Bronze(gp);
		gp.obj[i].worldX = gp.tileSize*21;
		gp.obj[i].worldY = gp.tileSize*19;
		i++;
		
		gp.obj[i] = new OBJ_Key(gp);
		gp.obj[i].worldX = gp.tileSize*26;
		gp.obj[i].worldY = gp.tileSize*21;
		i++;
		
		gp.obj[i] = new OBJ_Axe(gp);
		gp.obj[i].worldX = gp.tileSize*26;
		gp.obj[i].worldY = gp.tileSize*22;
		i++;
		
		gp.obj[i] = new OBJ_Shield_Iron(gp);
		gp.obj[i].worldX = gp.tileSize*25;
		gp.obj[i].worldY = gp.tileSize*22;
		i++;
		
		gp.obj[i] = new OBJ_Bread(gp);
		gp.obj[i].worldX = gp.tileSize*24;
		gp.obj[i].worldY = gp.tileSize*22;
		i++;
		
	}
	public void setNPC() {
		
		gp.npc[0] = new NPC_Guide(gp);
		gp.npc[0].worldX = gp.tileSize*28;
		gp.npc[0].worldY = gp.tileSize*28;
	}
	public void setMonster() {
		
		int i = 1;
		gp.monster[i] = new MON_GreenSlime(gp);
		gp.monster[i].worldX = gp.tileSize * 27;
		gp.monster[i].worldY = gp.tileSize * 28;
		i++;
		
		gp.monster[i] = new MON_GreenSlime(gp);
		gp.monster[i].worldX = gp.tileSize * 26;
		gp.monster[i].worldY = gp.tileSize * 24;
		i++;
		
		gp.monster[i] = new MON_GreenSlime(gp);
		gp.monster[i].worldX = gp.tileSize * 22;
		gp.monster[i].worldY = gp.tileSize * 25;
		i++;
		
		gp.monster[i] = new MON_GreenSlime(gp);
		gp.monster[i].worldX = gp.tileSize * 26;
		gp.monster[i].worldY = gp.tileSize * 30;
		i++;
		
		gp.monster[i] = new MON_GreenSlime(gp);
		gp.monster[i].worldX = gp.tileSize * 31;
		gp.monster[i].worldY = gp.tileSize * 24;
		i++;
	}
}
