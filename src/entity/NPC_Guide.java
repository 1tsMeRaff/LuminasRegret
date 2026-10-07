package entity;

import java.awt.Rectangle;
import java.util.Random;

import main.GamePanel;

public class NPC_Guide extends Entity {
    
    public NPC_Guide(GamePanel gp) {
        super(gp);
        
        direction = "down";
        speed = 1;
        
        solidArea = new Rectangle(12, 20, 24, 28);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        name = "Sylvia - Penjaga Roh Lumina";
        getImage();
        setDialogue();
    }
    
    public final void getImage() {
		up1 = setup("/npc/up1", gp.tileSize, gp.tileSize);
	    up2 = setup("/npc/up2", gp.tileSize, gp.tileSize);
	    down1 = setup("/npc/down1", gp.tileSize, gp.tileSize); 
	    down2 = setup("/npc/down2", gp.tileSize, gp.tileSize);
	    left1 = setup("/npc/left1", gp.tileSize, gp.tileSize);
	    left2 = setup("/npc/left2", gp.tileSize, gp.tileSize);
		right1 = setup("/npc/right1", gp.tileSize, gp.tileSize);
		right2 = setup("/npc/right2", gp.tileSize, gp.tileSize);
	}
	public final void setDialogue() {
		dialogues[0] = "Salam, wahai musafir takdir...\nAku Sylvia, penjaga sisa-sisa cahaya roh Lumina.";
		dialogues[1] = "Kabut kutukan telah menyelimuti lembah ini sejak kejatuhan Lumina.\nDi kedalaman Dungeon bawah tanah, Goblin King merenggut Relik Suci kita.";
		dialogues[2] = "Jika kau ingin mematahkan kutukan ini, persiapkan dirimu.\nBawalah sebuah Kapak untuk menebas duri dan Lentera dari Boran sang pedagang di barat!";
	}
    
	@Override
    public void setAction() {
        if (onPath) {
            int currentCol = getCurrentTileX();
            int currentRow = getCurrentTileY();
            
            // Cek jika sudah sampai tujuan
            if (currentCol == goalCol && currentRow == goalRow) {
                if (isAtTileCenter()) {
                    onPath = false;
                    direction = "down";
                    myPath.clear(); // Bersihkan path lokal
                } else {
                    moveToTileCenter(currentCol, currentRow);
                }
                return;
            }
            
            if (myPath.isEmpty()) {
                boolean found = gp.pFinder.search(currentCol, currentRow, goalCol, goalRow);
                if (found && !gp.pFinder.pathList.isEmpty()) {
                    myPath.clear();
                    myPath.addAll(gp.pFinder.pathList); 
                    followImprovedPath();
                } else {
                    onPath = false;
                    myPath.clear();
                }
            } else {
                followImprovedPath();
            }
        } else {
            randomMovement();
        }
    }
    
    @Override
    public void speak() {
        // Hadapi player
        switch(gp.player.direction) {
            case "up": direction = "down"; break;
            case "down": direction = "up"; break;
            case "left": direction = "right"; break;
            case "right": direction = "left"; break;
        }

        gp.ui.currentSpeakerName = name;
        quest.QuestType currentQuest = gp.qManager.getCurrentQuest();

        if (currentQuest == quest.QuestType.TALK_TO_GUIDE) {
            if (dialogueIndex < 3) {
                gp.gameState = gp.dialogueState;
                gp.ui.currentDialogue = dialogues[dialogueIndex];
                dialogueIndex++;

                if (dialogueIndex == 3) {
                    gp.qManager.completeCurrentAndAdvance(quest.QuestType.GET_TOOLS);
                }
            } else {
                // Selesai seluruh baris dialog
                gp.gameState = gp.playState;
                gp.ui.npc = null;
                gp.ui.currentDialogue = "";
                gp.ui.currentSpeakerName = "";
                dialogueIndex = 0;
            }
        }
        else if (currentQuest == quest.QuestType.GET_TOOLS) {
            if (dialogueIndex == 0) {
                gp.gameState = gp.dialogueState;
                if (gp.player.hasItem("Kapak") && gp.player.hasItem("Lentera")) {
                    gp.ui.currentDialogue = "Luar biasa! Kapak dan Lentera kini berada di genggamanmu.\nSekarang tebaslah semak rintangan menuju portal kuno di tenggara!";
                    gp.qManager.completeCurrentAndAdvance(quest.QuestType.CLEAR_PATH);
                } else {
                    gp.ui.currentDialogue = "Apakah kau sudah menemui Boran di barat, musafir?\nTanpa Kapak dan Lentera, kegelapan Dungeon akan menelan jiwamu.";
                }
                dialogueIndex = 1;
            } else {
                gp.gameState = gp.playState;
                gp.ui.npc = null;
                gp.ui.currentDialogue = "";
                gp.ui.currentSpeakerName = "";
                dialogueIndex = 0;
            }
        }
        else if (currentQuest == quest.QuestType.CLEAR_PATH) {
            if (dialogueIndex == 0) {
                gp.gameState = gp.dialogueState;
                gp.ui.currentDialogue = "Gunakan bilah Kapakmu untuk membuka jalur di semak duri tenggara.\nWaspadalah terhadap mayat hidup terkutuk yang menjaga gerbang portal!";
                if (!onPath) {
                    goalCol = 29;
                    goalRow = 26;
                    onPath = true;
                    myPath.clear();
                }
                dialogueIndex = 1;
            } else {
                gp.gameState = gp.playState;
                gp.ui.npc = null;
                gp.ui.currentDialogue = "";
                gp.ui.currentSpeakerName = "";
                dialogueIndex = 0;
            }
        }
        else if (currentQuest == quest.QuestType.EXPLORE_DUNGEON || currentQuest == quest.QuestType.DEFEAT_BOSS) {
            if (dialogueIndex == 0) {
                gp.gameState = gp.dialogueState;
                gp.ui.currentDialogue = "Roh-roh suci menyertai langkahmu di kegelapan...\nKumpulkan kunci gerbang di tiap sayap dungeon dan tundukkan sang Goblin King!";
                dialogueIndex = 1;
            } else {
                gp.gameState = gp.playState;
                gp.ui.npc = null;
                gp.ui.currentDialogue = "";
                gp.ui.currentSpeakerName = "";
                dialogueIndex = 0;
            }
        }
        else if (currentQuest == quest.QuestType.RETURN_TO_GUIDE) {
            gp.ui.currentDialogue = "Kau telah kembali membawa Relik Air Mata Lumina!\nHangatnya cahaya suci akhirnya mematahkan kutukan kelam ini selamanya.\nTerima kasih, pahlawan sejati!";
            gp.qManager.completeCurrentAndAdvance(quest.QuestType.GAME_COMPLETED);
            gp.gameState = gp.gameClearState;
            gp.playSE(2); // Fanfare sound
            gp.ui.npc = null;
            dialogueIndex = 0;
        }
        else {
            if (dialogueIndex == 0) {
                gp.gameState = gp.dialogueState;
                gp.ui.currentDialogue = "Cahaya suci Lumina kini abadi di bumi ini berkat ketabahan hatimu.";
                dialogueIndex = 1;
            } else {
                gp.gameState = gp.playState;
                gp.ui.npc = null;
                gp.ui.currentDialogue = "";
                gp.ui.currentSpeakerName = "";
                dialogueIndex = 0;
            }
        }
    }
}