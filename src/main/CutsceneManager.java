package main;

import java.awt.Graphics2D;

import entity.PlayerDummy;
import object.OBJ_Door1;

public class CutsceneManager {
    
    GamePanel gp;
    public int sceneNum;
    public int scenePhase;
    private int moveCounter = 0;
    private final int MOVE_DISTANCE = 100; // Jarak maksimum player bergerak
    private int dialogueCounter = 0;
    private boolean dialogueActive = false;
    private String[] currentDialogue;
    private int dialogueIndex = 0;

    // Scene Number
    public final int NA = 0;
    public final int goblinKing = 1;

    public CutsceneManager(GamePanel gp) {
        this.gp = gp;
    }
    
    public void draw(Graphics2D g2) {
        // Hanya menggambar elemen visual cutscene jika ada
    }
    
    public void update() {
        if(sceneNum == goblinKing) {
            scene_goblinKing();
        }
        
        // Update dialog jika aktif
        if(dialogueActive) {
            dialogueCounter++;
        }
    }
    
    public void startScene(int sceneNum) {
        this.sceneNum = sceneNum;
        this.scenePhase = 0;
        this.moveCounter = 0;
        this.dialogueActive = false;
        gp.gameState = gp.cutsceneState;
    }
    
    private void scene_goblinKing() {
        
        if(scenePhase == 0) {
            // Fase 0: Setup scene dan mulai dialog pertama
            if(!dialogueActive) {
                gp.bossBattleOn = true;
                
                // Shut the iron door
                for(int i = 0; i < gp.obj[gp.currentMap].length; i++) {
                    if(gp.obj[gp.currentMap][i] == null) {
                        gp.obj[gp.currentMap][i] = new OBJ_Door1(gp);
                        gp.obj[gp.currentMap][i].worldX = gp.tileSize * 25;
                        gp.obj[gp.currentMap][i].worldY = gp.tileSize * 28;
                        gp.obj[gp.currentMap][i].temp = true;
                        gp.playSE(21);
                        break;
                    }
                }
                
                // Create player dummy
                for(int i = 0; i < gp.npc[gp.currentMap].length; i++) {
                    if(gp.npc[gp.currentMap][i] == null) {
                        gp.npc[gp.currentMap][i] = new PlayerDummy(gp);
                        gp.npc[gp.currentMap][i].worldX = gp.player.worldX;
                        gp.npc[gp.currentMap][i].worldY = gp.player.worldY;
                        gp.npc[gp.currentMap][i].direction = gp.player.direction;
                        break;
                    }
                }
                
                gp.player.drawing = false;
                
                // Mulai dialog pertama
                startDialogue(new String[]{
                    "Pintu besi terkunci dengan keras!",
                    "Tidak ada jalan keluar...",
                    "Goblin King: HAHAHA! Sudah terjebak, manusia kecil!"
                });
            }
        }
        else if(scenePhase == 1) {
            // Fase 1: Player bergerak ke atas
            if(moveCounter < MOVE_DISTANCE) {
                gp.player.worldY -= 2;
                moveCounter += 2;
                
                // Check if player hits top of map
                if(gp.player.worldY < 0) {
                    gp.player.worldY = 0;
                }
            } else {
                // Setelah selesai bergerak, mulai dialog kedua
                if(!dialogueActive) {
                    startDialogue(new String[]{
                        "Goblin King: Mau lari kemana?",
                        "Aku akan menghancurkanmu!"
                    });
                }
            }
        }
        else if(scenePhase == 2) {
            // Fase 2: Akhir cutscene
            if(!dialogueActive) {
                gp.player.drawing = true;
                
                // Remove dummy
                for(int i = 0; i < gp.npc[gp.currentMap].length; i++) {
                    if(gp.npc[gp.currentMap][i] instanceof PlayerDummy) {
                        gp.npc[gp.currentMap][i] = null;
                        break;
                    }
                }
                
                // Reset boss HP bar untuk memastikan muncul
                for(int i = 0; i < gp.monster[gp.currentMap].length; i++) {
                    if(gp.monster[gp.currentMap][i] != null && 
                       gp.monster[gp.currentMap][i].name.equals("Goblin King")) {
                        gp.monster[gp.currentMap][i].hpBarOn = true;
                        gp.monster[gp.currentMap][i].hpBarCounter = 0;
                    }
                }
                
                // End cutscene
                endScene();
            }
        }
    }
    
    private void startDialogue(String[] dialogue) {
        currentDialogue = dialogue;
        dialogueIndex = 0;
        dialogueActive = true;
        dialogueCounter = 0;
        gp.ui.currentDialogue = currentDialogue[dialogueIndex];
        gp.gameState = gp.dialogueState;
    }
    
    private void nextDialogue() {
        dialogueIndex++;
        if(dialogueIndex < currentDialogue.length) {
            gp.ui.currentDialogue = currentDialogue[dialogueIndex];
            dialogueCounter = 0;
        } else {
            // Dialog selesai, lanjut ke fase berikutnya
            dialogueActive = false;
            gp.gameState = gp.cutsceneState;
            scenePhase++;
        }
    }
    
    public void handleDialogueInput() {
        if(dialogueActive && gp.keyH.actionPressed) {
            gp.keyH.actionPressed = false;
            nextDialogue();
        }
    }
    
    private void endScene() {
        sceneNum = NA;
        scenePhase = 0;
        moveCounter = 0;
        dialogueActive = false;
        gp.gameState = gp.playState;
    }
    
    public boolean isDialogueActive() {
        return dialogueActive;
    }
}