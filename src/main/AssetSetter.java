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
		
		gp.monster[0] = new MON_GreenSlime(gp);
		gp.monster[0].worldX = gp.tileSize * 27;
		gp.monster[0].worldY = gp.tileSize * 28;
		
		gp.monster[1] = new MON_GreenSlime(gp);
		gp.monster[1].worldX = gp.tileSize * 26;
		gp.monster[1].worldY = gp.tileSize * 24;
	}
}
