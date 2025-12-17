package main;

import entity.NPC_Guide;
import monster.MON_GreenSlime;
import object.OBJ_Portal;


public class AssetSetter {
	
	GamePanel gp;
	
	public AssetSetter(GamePanel gp) {
	    this.gp = gp;
	}

	public void setObject() {
		
		gp.obj[0] = new OBJ_Portal(gp);
		gp.obj[0].worldX = gp.tileSize*21;
		gp.obj[0].worldY = gp.tileSize*22;
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
