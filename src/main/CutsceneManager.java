package main;

import java.awt.Graphics2D;

import entity.PlayerDummy;
import object.OBJ_Door1;

public class CutsceneManager {
	
	GamePanel gp;
	Graphics2D g2;
	public int sceneNum;
	public int scenePhase;

	// Scene Number
	public final int NA = 0;
	public final int goblinKing = 1;

	public CutsceneManager(GamePanel gp) {
		this.gp = gp;
	}
	public void draw(Graphics2D gp) {
		this.g2 = g2;
		
		switch(sceneNum) {
		case goblinKing: scene_goblinKing(); break;
		}
	}
	public void scene_goblinKing() {
		
		if(scenePhase == 0) {
			
			gp.bossBattleOn = true;
			
			// Shut the iron door
			for(int i = 0; i < gp.obj[1].length; i++) {

			    if(gp.obj[gp.currentMap][i] == null) {
			        gp.obj[gp.currentMap][i] = new OBJ_Door1(gp);
			        gp.obj[gp.currentMap][i].worldX = gp.tileSize * 25;
			        gp.obj[gp.currentMap][i].worldY = gp.tileSize * 28;
			        gp.obj[gp.currentMap][i].temp = true;
			        gp.playSE(21);
			        break;
			    }
			}
			// Search a vacant slot for the dummy
			for(int i = 0; i < gp.npc[1].length; i++) {

			    if(gp.npc[gp.currentMap][i] == null) {
			        gp.npc[gp.currentMap][i] = new PlayerDummy(gp);
			        gp.npc[gp.currentMap][i].worldX = gp.player.worldX;
			        gp.npc[gp.currentMap][i].worldY = gp.player.worldY;
			        gp.npc[gp.currentMap][i].direction = gp.player.direction;
			        break;
			    }
			}
			gp.player.drawing = false;
			
			scenePhase++;
		}
		if(scenePhase == 1) {
			
			gp.player.worldY -= 2;
		}
	}
}
