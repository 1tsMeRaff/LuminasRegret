package main;

import entity.Entity;

public class EventHandler {
	
	GamePanel gp;
	private final java.util.Map<Long, EventRect> eventMap = new java.util.HashMap<>();
	
	int previousEventX, previousEventY;
	boolean canTouchEvent = true;
	int tempMap, tempCol, tempRow;
	
	public EventHandler(GamePanel gp) {
		this.gp = gp;
	}

	private long toKey(int map, int col, int row) {
		return (((long) map) << 32) | (((long) col) << 16) | (long) row;
	}

	public EventRect getEventRect(int map, int col, int row) {
		long key = toKey(map, col, row);
		return eventMap.computeIfAbsent(key, k -> {
			EventRect er = new EventRect();
			er.x = 8;
			er.y = 8;
			er.width = 32;
			er.height = 32;
			er.eventRectDefaultX = er.x;
			er.eventRectDefaultY = er.y;
			return er;
		});
	}

	public boolean hit(int map, int col, int row, String reqDirection) {
		boolean hit = false;
		
		if(map == gp.currentMap) {
			int playerX = gp.player.worldX + gp.player.solidArea.x;
			int playerY = gp.player.worldY + gp.player.solidArea.y;
			int playerW = gp.player.solidArea.width;
			int playerH = gp.player.solidArea.height;
			
			EventRect ev = getEventRect(map, col, row);
			int evX = col * gp.tileSize + ev.eventRectDefaultX;
			int evY = row * gp.tileSize + ev.eventRectDefaultY;
			int evW = ev.width;
			int evH = ev.height;
			
			if(CollisionMath.intersects(playerX, playerY, playerW, playerH, evX, evY, evW, evH) && !ev.eventDone) {
				if(gp.player.direction.equals(reqDirection) || reqDirection.contentEquals("any")) {
					hit = true;
					previousEventX = gp.player.worldX;
					previousEventY = gp.player.worldY;
				}
			}
		}
		
		return hit;
	}
	public void damagePit(int gameState) {
		
		gp.gameState = gameState;
		gp.ui.npc = null;
		gp.ui.currentDialogue = "Damage";
		gp.player.life -= 1;
//		eventRect[map][col][row].eventDone = true;
		canTouchEvent = false;
	}
	public void healingPool(int gameState) {
		
		if(gp.keyH.enterPressed == true) {
			gp.gameState = gameState;
			gp.ui.npc = null;
			gp.player.attackCanceled = true;
			gp.playSE(2);
			gp.ui.currentDialogue = "Healing";
			gp.player.life = gp.player.maxLife;
			gp.player.mana = gp.player.maxMana;
			gp.aSetter.setMonster();
		}
	}
	
	public void teleport(int map, int col, int row) {
		
		gp.gameState = gp.transitionState;
		tempMap = map;
		tempCol = col;
		tempRow = row;
				
		gp.currentMap = map;
		gp.player.worldX = gp.tileSize * col;
		gp.player.worldY = gp.tileSize * row;
		previousEventX = gp.player.worldX;
		previousEventY = gp.player.worldY;
		canTouchEvent = false;
		gp.playSE(13);
	}

	public void speak(Entity entity) {
        if(gp.keyH.actionPressed == true) {
            gp.gameState = gp.dialogueState;
            gp.player.attackCanceled = true;
            entity.speak();
        }
    }
	// Di dalam method checkEvent():
	public void checkEvent() {
	    // Check if the player character is more than 1 tile away from the last event
	    int xDistance = Math.abs(gp.player.worldX - previousEventX);
	    int yDistance = Math.abs(gp.player.worldY - previousEventY);
	    int distance = Math.max(xDistance, yDistance);
	    if(distance > gp.tileSize) {
	        canTouchEvent = true;
	    }

	    if(canTouchEvent == true) {
	        if(hit(0,23,12,"up") == true) {
	            healingPool(gp.dialogueState);
	        }
	        else if((hit(0,36,35,"any") || hit(0,36,37,"any")) == true && gp.keyH.actionPressed == true) {
	            if(!gp.player.hasItem("Lentera")) {
	                gp.gameState = gp.dialogueState;
	                gp.ui.npc = null;
	                gp.ui.currentDialogue = "Dungeon terlalu gelap tanpa Lentera!\nDapatkan Lentera terlebih dahulu di desa.";
	                return;
	            }
	            teleport(1,11,39);
	            if(gp.qManager.getCurrentQuest() == quest.QuestType.CLEAR_PATH) {
	                gp.qManager.completeCurrentAndAdvance(quest.QuestType.EXPLORE_DUNGEON);
	            }
	        }
	        else if((hit(1,11,39,"any") || hit(1,12,40,"any")) == true && gp.keyH.actionPressed == true) {
	            teleport(0,36,35);
	        }
	        else if((hit(1,25,27,"any") || hit(1,25,26,"any")) == true) {
	            goblinKing();
	        }
	    }
	}

	public void goblinKing() {
	    if(gp.bossBattleOn == false && gp.csManager.sceneNum == gp.csManager.NA) {
	        gp.gameState = gp.cutsceneState;
	        gp.csManager.startScene(gp.csManager.goblinKing);
	        gp.bossBattleOn = true;
	        if(gp.qManager.getCurrentQuest() == quest.QuestType.EXPLORE_DUNGEON) {
	            gp.qManager.completeCurrentAndAdvance(quest.QuestType.DEFEAT_BOSS);
	        }
	    }
	}
}
