package com.luminasregret.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.luminasregret.engine.core.GamePanel;
import com.luminasregret.game.entity.Entity;
import com.luminasregret.game.entity.Player;
import com.luminasregret.game.entity.NPC_Guide;
import com.luminasregret.game.entity.NPC_Merchant;
import com.luminasregret.game.object.*;
import com.luminasregret.game.quest.QuestType;
import com.luminasregret.game.tile.interactive.InteractiveTile;

public class UI {
    
    GamePanel gp;
    Graphics2D g2;
    Font kingThings, kingThingsL, fusionPixel;
    BufferedImage heart_full, heart_half, heart_blank, playerMana_Full, playerMana_Blank, coin;
    public boolean messageOn = false;
    public ArrayList<String> message = new ArrayList<>();
    public ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = " ";
    public String currentSpeakerName = "";
    public int commandNum = 0;
    public int titleScreenState = 0;
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;
    public int subState = 0;
    int counter = 0;
    public Entity npc;
    
    
    // Pre-allocated static colors to eliminate GC pressure in the hot render path
    public static final Color COLOR_SHADOW_BLACK = new Color(0, 0, 0, 220);
    public static final Color COLOR_HALF_BLACK = new Color(0, 0, 0, 150);
    public static final Color COLOR_BG_DARK_PILL = new Color(15, 15, 20, 200);
    public static final Color COLOR_GOLD_BORDER = new Color(230, 200, 110, 160);
    public static final Color COLOR_HP_BG = new Color(35, 35, 35);
    public static final Color COLOR_HP_RED = new Color(255, 0, 30);
    public static final Color COLOR_HP_BOSS_RED = new Color(220, 20, 30);
    public static final Color COLOR_HP_BOSS_RAGE = new Color(255, 60, 0);
    public static final Color COLOR_BOSS_RAGE_TEXT = new Color(255, 120, 120);
    public static final Color COLOR_PROMPT_BG = new Color(15, 20, 35, 220);
    public static final Color COLOR_PROMPT_BORDER = new Color(255, 215, 0, 230);
    public static final Color COLOR_QUEST_BG = new Color(0, 0, 0, 185);
    public static final Color COLOR_QUEST_BORDER = new Color(255, 215, 0, 210);
    public static final Color COLOR_QUEST_HEADER = new Color(255, 215, 0);
    public static final Color COLOR_QUEST_DESC = new Color(210, 210, 210);
    public static final Color COLOR_BANNER_BG = new Color(15, 15, 25, 235);
    public static final Color COLOR_BANNER_GOLD = new Color(255, 215, 0);
    public static final Color COLOR_SUBWINDOW_BG = new Color(0, 0, 0, 200);
    public static final Color COLOR_CLEAR_OVERLAY = new Color(0, 0, 0, 225);
    public static final Color COLOR_CLEAR_TITLE_SHADOW = new Color(130, 95, 0);
    public static final Color COLOR_CLEAR_SUBTEXT = new Color(215, 215, 215);
    public static final Color COLOR_TITLE_BG = new Color(0, 102, 102);
    public static final Color COLOR_EQUIP_CURSOR = new Color(240, 190, 90);
    public static final Color COLOR_BADGE_BG = new Color(15, 18, 30, 245);

    // Pre-allocated static strokes
    public static final BasicStroke STROKE_1_5 = new BasicStroke(1.5f);
    public static final BasicStroke STROKE_2 = new BasicStroke(2.0f);
    public static final BasicStroke STROKE_3 = new BasicStroke(3.0f);
    public static final BasicStroke STROKE_5 = new BasicStroke(5.0f);

    // Pre-cached fonts to avoid calling deriveFont() every frame
    public Font fontPixel11Plain;
    public Font fontPixel13Bold;
    public Font fontPixel14Plain;
    public Font fontPixel15Bold;
    public Font fontPixel15Plain;
    public Font fontPixel16Bold;
    public Font fontPixel16Plain;
    public Font fontPixel18Plain;
    public Font fontPixel18Bold;
    public Font fontPixel19Plain;
    public Font fontPixel20Bold;
    public Font fontPixel22Bold;
    public Font fontPixel24Bold;
    public Font fontPixel28Bold;
    public Font fontPixel32Bold;
    public Font fontPixel40Bold;
    public Font fontPixel50Bold;
    public Font fontPixel54Bold;
    public Font fontPixel80Bold;
    public Font fontPixel110Bold;
    
    public UI(GamePanel gp) {
        this.gp = gp;
        
        kingThings = loadFont("/font/Kingthings_Petrock.ttf", 28f);
        kingThingsL = loadFont("/font/Kingthings_Petrock_light.ttf", 20f);
        fusionPixel = loadFont("/font/fusion-pixel.ttf", 18f);

        // Pre-derive all fonts once to eliminate GC overhead
        Font baseFont = (fusionPixel != null) ? fusionPixel : new Font("SansSerif", Font.PLAIN, 18);
        fontPixel11Plain = baseFont.deriveFont(Font.PLAIN, 11f);
        fontPixel13Bold = baseFont.deriveFont(Font.BOLD, 13f);
        fontPixel14Plain = baseFont.deriveFont(Font.PLAIN, 14f);
        fontPixel15Bold = baseFont.deriveFont(Font.BOLD, 15f);
        fontPixel15Plain = baseFont.deriveFont(Font.PLAIN, 15f);
        fontPixel16Bold = baseFont.deriveFont(Font.BOLD, 16f);
        fontPixel16Plain = baseFont.deriveFont(Font.PLAIN, 16f);
        fontPixel18Plain = baseFont.deriveFont(Font.PLAIN, 18f);
        fontPixel18Bold = baseFont.deriveFont(Font.BOLD, 18f);
        fontPixel19Plain = baseFont.deriveFont(Font.PLAIN, 19f);
        fontPixel20Bold = baseFont.deriveFont(Font.BOLD, 20f);
        fontPixel22Bold = baseFont.deriveFont(Font.BOLD, 22f);
        fontPixel24Bold = baseFont.deriveFont(Font.BOLD, 24f);
        fontPixel28Bold = baseFont.deriveFont(Font.BOLD, 28f);
        fontPixel32Bold = baseFont.deriveFont(Font.BOLD, 32f);
        fontPixel40Bold = baseFont.deriveFont(Font.BOLD, 40f);
        fontPixel50Bold = baseFont.deriveFont(Font.BOLD, 50f);
        fontPixel54Bold = baseFont.deriveFont(Font.BOLD, 54f);
        fontPixel80Bold = baseFont.deriveFont(Font.BOLD, 80f);
        fontPixel110Bold = baseFont.deriveFont(Font.BOLD, 110f);
        
        // Create HUD Object
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
        
        Entity mana = new OBJ_ManaBar(gp);
        playerMana_Full = mana.image;
        playerMana_Blank = mana.image2;
        
        Entity bronzeCoin = new OBJ_Coin_Bronze(gp);
        coin = bronzeCoin.down1;
    }

    private Font loadFont(String path, float defaultSize) {
        Font font = null;
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            File file = new File("res" + path);
            if (file.exists()) {
                try {
                    is = new FileInputStream(file);
                } catch (IOException ignored) {
                }
            }
        }
        if (is != null) {
            try (InputStream stream = is) {
                font = Font.createFont(Font.TRUETYPE_FONT, stream);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
        }
        if (font == null) {
            font = new Font("SansSerif", Font.PLAIN, (int)defaultSize);
        }
        return font;
    }
    
    public void addMessage(String text) {
        
        message.add(text);
        messageCounter.add(0);
    }
    
    public void draw(Graphics2D g2) {
        
        this.g2 = g2;
        
        if (fusionPixel != null) {
            g2.setFont(fusionPixel);
        }
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.white);
        
        //Title State
        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
            drawMessage();
        }
        //Play State
        if(gp.gameState == gp.playState) {
            drawPlayerLife();
            drawMonsterLife();
            drawBossLife();
            drawMessage();
            drawQuestWidget();
            drawQuestBanner();
            drawInteractionPrompt();
        }
        //Pause State
        if(gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }
        //Dialogue State
        if(gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
            drawQuestBanner();
        }
        //CharacterState
        if(gp.gameState == gp.characterState) {
            drawCharacterScreen();
            drawInventory(gp.player, true);
        }
        //Option State
        if(gp.gameState == gp.optionsState) {
            drawOptionsScreen();
        }
        //Game Over State
        if(gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }
        //Transition State
        if(gp.gameState == gp.transitionState) {
            drawTransition();
        }
        //Trade State
        if(gp.gameState == gp.tradeState) {
            drawTradeScreen();
        }
        //Game Clear State
        if(gp.gameState == gp.gameClearState) {
            drawGameClearScreen();
        }
    }
    
    public void drawPlayerLife() {
        int startX = 20;
        int startY = 20;
        int heartGap = 26; // 24px icon + 2px gap

        // DRAW MAX LIFE (BLANK HEARTS)
        int x = startX;
        int y = startY;
        int i = 0;
        while(i < gp.player.maxLife / 2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += heartGap;
        }

        // DRAW CURRENT LIFE (HALF / FULL HEARTS)
        x = startX;
        y = startY;
        i = 0;
        while(i < gp.player.life) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if(i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += heartGap;
        }

        if(gp.player.life < 0) {
            gp.player.life = 0;
        }

        // DRAW MANA (COMPACT 24x24 CRYSTALS)
        int manaGap = 26; // 24px icon + 2px gap
        int manaY = startY + 28; // Placed right below heart row

        // DRAW MAX MANA (BLANK CRYSTALS)
        x = startX;
        i = 0;
        while(i < gp.player.maxMana) {
            g2.drawImage(playerMana_Blank, x, manaY, null);
            i++;
            x += manaGap;
        }

        // DRAW CURRENT MANA (FULL CRYSTALS)
        x = startX;
        i = 0;
        while(i < gp.player.mana) {
            g2.drawImage(playerMana_Full, x, manaY, null);
            i++;
            x += manaGap;
        }

        if(gp.player.mana < 0) {
            gp.player.mana = 0;
        }
    }
    
    public void drawMonsterLife() {
    	
    	for(int i = 0; i < gp.monster[gp.currentMap].length; i++) {
    		
    		Entity monster = gp.monster[gp.currentMap][i];
    		
    		if(monster != null && monster.inCamera()) {
    			
    			// Monster Hp Bar (Regular monster overhead indicator)
    			if(monster.hpBarOn && !monster.boss) {

    			    double oneScale = (double)gp.tileSize/monster.maxLife;
    			    double hpBarValue = oneScale * Math.max(0, monster.life);

    			    g2.setColor(COLOR_HP_BG);
    			    g2.fillRect(monster.getScreenX()-1, monster.getScreenY()-16, gp.tileSize+2, 12);

    			    g2.setColor(COLOR_HP_RED);
    			    g2.fillRect(monster.getScreenX(), monster.getScreenY() - 15, (int)hpBarValue, 10);

    			    monster.hpBarCounter++;

    			    if(monster.hpBarCounter > 600) {
    			    	monster.hpBarCounter = 0;
    			    	monster.hpBarOn = false;
    			    }
    			}
    		}
    	}
    }

    public void drawBossLife() {
        if (!gp.bossBattleOn) return;

        for(int i = 0; i < gp.monster[gp.currentMap].length; i++) {
            Entity monster = gp.monster[gp.currentMap][i];
            if(monster != null && monster.boss && monster.alive) {
                int displayLife = Math.max(0, monster.life);
                double oneScale = (double)gp.tileSize * 8 / monster.maxLife;
                double hpBarValue = oneScale * displayLife;
                if (hpBarValue < 0) {
                    hpBarValue = 0;
                }

                int x = gp.screenWidth / 2 - gp.tileSize * 4;
                int y = gp.screenHeight - (gp.tileSize * 2);

                g2.setColor(COLOR_HP_BG);
                g2.fillRect(x - 1, y - 1, gp.tileSize * 8 + 2, 16);

                if(monster.rage) {
                    g2.setColor(COLOR_HP_BOSS_RAGE); // Oranye merah membara saat fase 2 murka
                } else {
                    g2.setColor(COLOR_HP_BOSS_RED);
                }
                g2.fillRect(x, y, (int)hpBarValue, 14);

                g2.setFont(fontPixel20Bold);
                String title = monster.name;
                if(monster.rage) {
                    title += " [FASE 2: MURKA]";
                    g2.setColor(COLOR_BOSS_RAGE_TEXT);
                } else {
                    g2.setColor(Color.white);
                }
                g2.drawString(title, x + 4, y - 8);

                // Tampilkan rasio angka HP di sisi kanan (clamped ke 0)
                String hpRatio = displayLife + " / " + monster.maxLife;
                int hpTextX = x + gp.tileSize * 8 - g2.getFontMetrics().stringWidth(hpRatio) - 4;
                g2.setColor(Color.white);
                g2.drawString(hpRatio, hpTextX, y - 8);
                break;
            }
        }
    }
    
    public void drawMessage(){
        
        int messageX = 20;
        int messageY = (int)(gp.tileSize * 3.5);
        g2.setFont(fontPixel18Plain);
        FontMetrics fm = g2.getFontMetrics();

        for(int i = 0; i < message.size(); i++) {
            if(message.get(i) != null) {
                String text = message.get(i);
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getHeight();

                // Sleek semi-transparent dark pill background
                g2.setColor(COLOR_BG_DARK_PILL);
                g2.fillRoundRect(messageX, messageY - fm.getAscent() - 3, textWidth + 16, textHeight + 6, 8, 8);

                // Elegant border accent
                g2.setColor(COLOR_GOLD_BORDER);
                g2.drawRoundRect(messageX, messageY - fm.getAscent() - 3, textWidth + 16, textHeight + 6, 8, 8);

                // Text shadow & text
                g2.setColor(COLOR_SHADOW_BLACK);
                g2.drawString(text, messageX + 9, messageY + 1);
                g2.setColor(Color.white);
                g2.drawString(text, messageX + 8, messageY);

                int counter = messageCounter.get(i) + 1; // messageCounter++
                messageCounter.set(i, counter); // set the counter to the array
                messageY += textHeight + 8; // Spacing antar pesan rapi dan proporsional

                if(messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                    i--;
                }
            }
        }
    }

    public boolean containsMessage(String text) {
        if (text == null) return false;
        for (String msg : message) {
            if (msg != null && msg.contains(text)) {
                return true;
            }
        }
        return false;
    }
    
    public void drawTitleScreen() {
        
        g2.setColor(COLOR_TITLE_BG);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Title Name
        g2.setFont(fontPixel80Bold);
        String text = "Lumina's Regret";
        int x = getXforCenteredText(text);
        int y = (int)(gp.tileSize * 2.5); // Posisi judul disesuaikan untuk layar pendek
        
        // Shadow
        g2.setColor(Color.BLACK);
        g2.drawString(text, x+5, y+5);
        
        // Main Color
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
        
        // Main Character Image
        x = gp.screenWidth/2 - (gp.tileSize * 2)/2;
        y += gp.tileSize; 
        g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);
        
        // Menu
        g2.setFont(fontPixel40Bold);
        
        text = "NEW GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize * 3; // Mengurangi jarak agar muat
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawString(">", x-gp.tileSize, y);
        }
        
        text = "LOAD GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawString(">", x-gp.tileSize, y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 2) {
            g2.drawString(">", x-gp.tileSize, y);
        }
        
    }
    
    public void drawPauseScreen() {
        String text = "PAUSED";
        g2.setFont(fontPixel50Bold);
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2;
        g2.drawString(text, x, y);
    }
    
    public void drawDialogueScreen() {
        int x;
        int y;
        int width;
        int height;

        // Jika dalam mode dagang, sesuaikan lebar agar tidak bertabrakan dengan menu di kanan
        if (gp.gameState == gp.tradeState) {
            x = gp.tileSize;
            width = (int)(gp.tileSize * 10.5);
            height = (int)(gp.tileSize * 3.4);
            y = gp.screenHeight - height - (int)(gp.tileSize * 0.4);
        } else {
            // Mode dialog biasa & cutscene: lebar proporsional 672px
            x = gp.tileSize;
            width = gp.screenWidth - (gp.tileSize * 2);
            height = (int)(gp.tileSize * 3.4);
            y = gp.screenHeight - height - (int)(gp.tileSize * 0.4);
        }
        
        drawSubWindow(x, y, width, height);

        // Speaker Name Tag Badge
        if (currentSpeakerName != null && !currentSpeakerName.trim().isEmpty()) {
            g2.setFont(fontPixel15Bold);
            int badgeW = g2.getFontMetrics().stringWidth(currentSpeakerName) + 32;
            int badgeH = 28;
            int badgeX = x + 20;
            int badgeY = y - 14;

            g2.setColor(COLOR_BADGE_BG);
            g2.fillRoundRect(badgeX, badgeY, badgeW, badgeH, 12, 12);

            g2.setColor(COLOR_BANNER_GOLD);
            g2.setStroke(STROKE_2);
            g2.drawRoundRect(badgeX, badgeY, badgeW, badgeH, 12, 12);

            g2.drawString(currentSpeakerName, badgeX + 16, badgeY + 19);
        }
        
        // Font dialog: 19F agar rapi, proporsional, dan tidak meluap
        g2.setFont(fontPixel19Plain);
        g2.setColor(Color.WHITE);

        FontMetrics fm = g2.getFontMetrics();
        int textPaddingX = 28;
        int textPaddingY = 36;
        int maxTextWidth = width - (textPaddingX * 2);
        int textX = x + textPaddingX;
        int textY = y + textPaddingY;
        int lineHeight = 28;

        if (currentDialogue != null && !currentDialogue.isEmpty()) {
            ArrayList<String> wrappedLines = wrapDialogueText(currentDialogue, maxTextWidth, fm);
            for (String line : wrappedLines) {
                g2.drawString(line, textX, textY);
                textY += lineHeight;
            }
        }

        // Hint tombol lanjut di pojok kanan bawah
        g2.setFont(fontPixel13Bold);
        String promptHint = "[E / ENTER / Klik] Lanjut ▶";
        int hintW = g2.getFontMetrics().stringWidth(promptHint);
        int hintX = x + width - hintW - 24;
        int hintY = y + height - 16;
        g2.setColor(COLOR_BANNER_GOLD);
        g2.drawString(promptHint, hintX, hintY);
    }

    public ArrayList<String> wrapDialogueText(String rawText, int maxWidth, FontMetrics fm) {
        ArrayList<String> lines = new ArrayList<>();
        if (rawText == null || rawText.trim().isEmpty()) {
            return lines;
        }

        String[] paragraphs = rawText.split("\n", -1);
        for (String para : paragraphs) {
            if (para.trim().isEmpty()) {
                lines.add("");
                continue;
            }
            String[] words = para.split("\\s+");
            StringBuilder currentLine = new StringBuilder();

            for (String word : words) {
                if (word.isEmpty()) continue;

                if (currentLine.length() == 0) {
                    if (fm.stringWidth(word) <= maxWidth) {
                        currentLine.append(word);
                    } else {
                        // Kata lebih panjang dari lebar garis - potong per karakter
                        for (int i = 0; i < word.length(); i++) {
                            char c = word.charAt(i);
                            if (fm.stringWidth(currentLine.toString() + c) <= maxWidth) {
                                currentLine.append(c);
                            } else {
                                lines.add(currentLine.toString());
                                currentLine = new StringBuilder();
                                currentLine.append(c);
                            }
                        }
                    }
                } else {
                    String testLine = currentLine + " " + word;
                    if (fm.stringWidth(testLine) <= maxWidth) {
                        currentLine.append(" ").append(word);
                    } else {
                        lines.add(currentLine.toString());
                        currentLine = new StringBuilder();
                        if (fm.stringWidth(word) <= maxWidth) {
                            currentLine.append(word);
                        } else {
                            for (int i = 0; i < word.length(); i++) {
                                char c = word.charAt(i);
                                if (fm.stringWidth(currentLine.toString() + c) <= maxWidth) {
                                    currentLine.append(c);
                                } else {
                                    lines.add(currentLine.toString());
                                    currentLine = new StringBuilder();
                                    currentLine.append(c);
                                }
                            }
                        }
                    }
                }
            }
            if (currentLine.length() > 0) {
                lines.add(currentLine.toString());
            }
        }
        return lines;
    }

    public void drawInteractionPrompt() {
        Entity target = gp.player.nearbyInteractable;
        if (target == null) return;

        int screenX = target.worldX - gp.player.worldX + gp.player.screenX;
        int screenY = target.worldY - gp.player.worldY + gp.player.screenY;

        // Tentukan teks prompt berdasarkan tipe entity
        String actionText;
        if (target instanceof NPC_Guide || target instanceof NPC_Merchant) {
            actionText = "[E / Klik] Bicara";
        } else if (target instanceof OBJ_Chest) {
            actionText = "[E / Klik] Buka Peti";
        } else if (target instanceof OBJ_Door || target instanceof OBJ_Door1) {
            actionText = "[E / Klik] Buka Pintu";
        } else if (target instanceof InteractiveTile) {
            actionText = "[E / Klik] Tebas";
        } else if (target.type == target.type_pickupOnly || target.type == target.type_sword || target.type == target.type_axe || target.type == target.type_shield || target.type == target.type_consumable) {
            actionText = "[E / Klik] Ambil";
        } else {
            actionText = "[E / Klik] Interaksi";
        }

        g2.setFont(fontPixel13Bold);
        int textWidth = g2.getFontMetrics().stringWidth(actionText);
        int boxW = textWidth + 16;
        int boxH = 22;

        // Floating bounce animation
        double bobOffset = Math.sin(System.currentTimeMillis() * 0.006) * 3;
        int boxX = screenX + (gp.tileSize / 2) - (boxW / 2);
        int boxY = (int) (screenY - 14 + bobOffset);

        // Draw shadow & background
        g2.setColor(COLOR_PROMPT_BG);
        g2.fillRoundRect(boxX, boxY, boxW, boxH, 10, 10);

        // Border glow
        g2.setColor(COLOR_PROMPT_BORDER);
        g2.setStroke(STROKE_1_5);
        g2.drawRoundRect(boxX, boxY, boxW, boxH, 10, 10);

        // Text
        g2.setColor(Color.WHITE);
        g2.drawString(actionText, boxX + 8, boxY + 15);
    }
    
    public void drawCharacterScreen() {
        
        // FRAME
        final int frameX = gp.tileSize;
        final int frameY = (int)(gp.tileSize * 0.4);
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 8;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        // Text
        g2.setColor(Color.white);
        g2.setFont(fontPixel28Bold);

        int textX = frameX + 20;
        int textY = frameY + 38;
        final int lineHeight = 28;

        // NAMES
        g2.drawString("Level", textX, textY); textY += lineHeight;
        g2.drawString("Life", textX, textY); textY += lineHeight;
        g2.drawString("Mana", textX, textY); textY += lineHeight;
        g2.drawString("Strength", textX, textY); textY += lineHeight;
        g2.drawString("Dexterity", textX, textY); textY += lineHeight;
        g2.drawString("Attack", textX, textY); textY += lineHeight;
        g2.drawString("Defense", textX, textY); textY += lineHeight;
        g2.drawString("Exp", textX, textY); textY += lineHeight;
        g2.drawString("Next Level", textX, textY); textY += lineHeight;
        g2.drawString("Coin", textX, textY); textY += lineHeight + 5;
        g2.drawString("Weapon", textX, textY); textY += lineHeight + 10;
        g2.drawString("Shield", textX, textY);
        
        // VALUES
        int tailX = (frameX + frameWidth) - 20;
        // Reset textY
        textY = frameY + 38;
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.strength);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.exp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        g2.drawImage(gp.player.currentWeapon.down1, tailX - 34, textY - 32, null);
        textY += 42;
        g2.drawImage(gp.player.currentShield.down1, tailX - 34, textY - 38, null);
        
    }
    
    public void drawInventory(Entity entity, boolean cursor) {
    	
        int frameX = 0;
        int frameY = 0;
        int frameWidth = 0;
        int frameHeight = 0;
        int slotCol = 0;
        int slotRow = 0;
        
        if(entity == gp.player) {
            frameX = gp.tileSize * 9 + 12;
            frameY = gp.tileSize - 24;
            frameWidth = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        }
        else {
            frameX = gp.tileSize - 12;
            frameY = gp.tileSize - 24;
            frameWidth = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;
        }


        //DRAW FRAME
        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        //SLOT
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tileSize + 3;


        //DRAW PLAYER'S ITEMS
        for(int i = 0; i < entity.inventory.size(); i++) {

            //EQUIP CURSOR
            if(entity.inventory.get(i) == entity.currentWeapon ||
                    entity.inventory.get(i) == entity.currentShield || 
                    entity.inventory.get(i) == entity.currentLight) {
            	
                g2.setColor(COLOR_EQUIP_CURSOR);
                g2.fillRoundRect(slotX,slotY, gp.tileSize, gp.tileSize,10,10 );
            }

            g2.drawImage(entity.inventory.get(i).down1, slotX,slotY,null);  //draw item

            slotX += slotSize;

            if(i == 4 || i == 9 || i == 14) {
                //reset slotX
                slotX = slotXstart;
                //next row
                slotY += slotSize;
            }
        }

        //CURSOR
        if(cursor == true) {
            int cursorX = slotXstart + (slotSize * slotCol);
            int cursorY = slotYstart + (slotSize * slotRow);
            int cursorWidth = gp.tileSize;
            int cursorHeight = gp.tileSize;

            //DRAW CURSOR
            g2.setColor(Color.white);
            g2.setStroke(STROKE_3);
            g2.drawRoundRect(cursorX,cursorY,cursorWidth,cursorHeight,10,10);

            //DESCRIPTION FRAME
            int dFrameX = frameX;
            int dFrameY = frameY + frameHeight;
            int dFrameWidth = frameWidth;
            int dFrameHeight = gp.tileSize * 3;
            
            // Sesuaikan posisi description frame agar tidak keluar dari layar bawah
            if (dFrameY + dFrameHeight > gp.screenHeight) {
                dFrameY = frameY - dFrameHeight;
            }

            int itemIndex = getItemIndexOnSlot(slotCol, slotRow);
            if(itemIndex < entity.inventory.size()) {
                drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);

                g2.setFont(fontPixel16Plain);
                g2.setColor(Color.WHITE);

                FontMetrics fm = g2.getFontMetrics();
                int textX = dFrameX + 18;
                int textY = dFrameY + 30;
                int maxTextWidth = dFrameWidth - 36;
                int lineHeight = 22;

                String desc = entity.inventory.get(itemIndex).description;
                if (desc != null && !desc.isEmpty()) {
                    ArrayList<String> descLines = wrapDialogueText(desc, maxTextWidth, fm);
                    for (String line : descLines) {
                        g2.drawString(line, textX, textY);
                        textY += lineHeight;
                    }
                }
            }
        }
    }
    
    public void drawGameOverScreen() {
        g2.setColor(COLOR_HALF_BLACK); //Half-black
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(fontPixel110Bold);
        text = "Game Over";

        //Shadow
        g2.setColor(Color.BLACK);
        x = getXforCenteredText(text);
        y = gp.tileSize * 2;
        g2.drawString(text,x,y);
        //Text
        g2.setColor(Color.white);
        g2.drawString(text,x-4,y-4);

        //RETRY
        g2.setFont(fontPixel50Bold);
        text = "Retry";
        x = getXforCenteredText(text);
        y += gp.tileSize * 4;
        g2.drawString(text,x,y);
        if(commandNum == 0) {
            g2.drawString(">", x-40, y);
        }

        //BACK TO THE TITLE SCREEN
        text = "Quit";
        x = getXforCenteredText(text);
        y += 55;
        g2.drawString(text,x,y);
        if(commandNum == 1) {
            g2.drawString(">", x-40, y);
        }

    }
    
    public void drawOptionsScreen() {
        
        g2.setColor(Color.white);
        g2.setFont(fontPixel32Bold);

        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 8;

        int frameX = (gp.screenWidth - frameWidth) / 2;
        int frameY = (gp.screenHeight - frameHeight) / 2;
        
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch(subState) {
            case 0: options_top(frameX, frameY); break;
            case 1: options_fullScreenNotification(frameX, frameY); break;
            case 2: options_control(frameX, frameY); break;
            case 3: options_endGameConfirmation(frameX, frameY); break;
        }

        gp.keyH.enterPressed = false;
    }

    public void options_top(int frameX, int frameY) {
        int textX;
        int textY;

        // Title (Paling Atas Tengah)
        String text = "Options";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        int itemDy = gp.tileSize - 10;

        // Label Menu (Kiri)
        textX = frameX + gp.tileSize - 12;
        textY += gp.tileSize + 10;
        
        g2.drawString("Full Screen", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX-20, textY);
            if (gp.keyH.enterPressed) {
                gp.toggleFullScreen();
            }
        }

        textY += itemDy;
        g2.drawString("Music", textX, textY);
        if (commandNum == 1) g2.drawString(">", textX-25, textY);

        textY += itemDy;
        g2.drawString("SE", textX, textY);
        if (commandNum == 2) g2.drawString(">", textX-25, textY);

        textY += itemDy;
        g2.drawString("Control", textX, textY);
        if (commandNum == 3) {
            g2.drawString(">", textX-25, textY);
            if (gp.keyH.enterPressed) {
                subState = 2;
                commandNum = 0;
            }
        }

        textY += itemDy;
        g2.drawString("End Game", textX, textY);
        if (commandNum == 4) {
            g2.drawString(">", textX-25, textY);
            if (gp.keyH.enterPressed) {
                subState = 3;
                commandNum = 0;
            }
        }

        textY += itemDy * 2;
        g2.drawString("Back", textX, textY);
        if (commandNum == 5) {
            g2.drawString(">", textX-25, textY);
            if (gp.keyH.enterPressed) {
                gp.gameState = gp.playState;
                commandNum = 0;
            }
        }

        // --- BAGIAN KANAN (Checkbox & Sliders) ---
        int controlX = frameX + (int)(gp.tileSize * 4.9);
        int baseY = frameY + gp.tileSize * 2 + 10;

        // Checkbox Full Screen
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(controlX, baseY - 20, 24, 24);
        if (gp.fullScreenOn) {
            g2.fillRect(controlX, baseY - 20, 24, 24);
        }

        // Music Slider
        baseY += itemDy;
        g2.drawRect(controlX, baseY - 20, 120, 24);
        int volumeWidth = 24 * gp.music.volumeScale;
        g2.fillRect(controlX, baseY - 20, volumeWidth, 24);

        // SE Slider
        baseY += itemDy;
        g2.drawRect(controlX, baseY - 20, 120, 24);
        volumeWidth = 24 * gp.se.volumeScale;
        g2.fillRect(controlX, baseY - 20, volumeWidth, 24);
        
        gp.config.saveConfig();
    }

    public void options_control(int frameX, int frameY) {
        int textX;
        int textY;

        String text = "Control";
        textX = getXforCenteredText(text) - 8;
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        int itemDy = gp.tileSize - 12;
        textX = frameX + gp.tileSize - 20;
        textY += gp.tileSize;

        // Kolom Kiri
        g2.drawString("Move", textX, textY); textY += itemDy;
        g2.drawString("Action", textX, textY); textY += itemDy;
        g2.drawString("Magic", textX, textY); textY += itemDy;
        g2.drawString("Inventory", textX, textY); textY += itemDy;
        g2.drawString("Pause", textX, textY); textY += itemDy;
        g2.drawString("Options", textX, textY);

        // Kolom Kanan
        textX = (frameX + gp.tileSize * 5) - 18;
        textY = frameY + gp.tileSize * 2;
        g2.drawString("WASD", textX, textY); textY += itemDy;
        g2.drawString("E / ENTER", textX, textY); textY += itemDy;
        g2.drawString("F", textX, textY); textY += itemDy;
        g2.drawString("C", textX, textY); textY += itemDy;
        g2.drawString("P", textX, textY); textY += itemDy;
        g2.drawString("ESC", textX, textY);

        // Back
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize * 7 + 20;
        g2.drawString("Back", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX-25, textY);
            if (gp.keyH.enterPressed) {
                subState = 0;
                commandNum = 3;
            }
        }
    }

    public void options_fullScreenNotification(int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        currentDialogue = "The change will take \neffect after restarting \nthe game.";
        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        // Back
        textY = frameY + gp.tileSize * 7;
        g2.drawString("Back", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX-25, textY);
            if (gp.keyH.enterPressed) {
                subState = 0;
            }
        }
    }

    public void options_endGameConfirmation(int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        currentDialogue = "Apakah kamu yakin?";
        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        String text = "Yes";
        textX = getXforCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX-25, textY);
            if (gp.keyH.enterPressed) {
                subState = 0;
                gp.gameState = gp.titleState;
            }
        }

        text = "No";
        textX = getXforCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX-25, textY);
            if (gp.keyH.enterPressed) {
                subState = 0;
                commandNum = 4;
            }
        }
    }
    
    public void drawTransition() {
    	
    	counter++;
    	g2.setColor(new Color(0, 0, 0, counter * 5));
    	g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

    	if(counter == 50) {
    	    counter = 0;
    	    gp.gameState = gp.playState;
    	    gp.currentMap = gp.eHandler.tempMap;
    	    gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
    	    gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;
    	    gp.eHandler.previousEventX = gp.player.worldX;
    	    gp.eHandler.previousEventY = gp.player.worldY;
    	    gp.playAreaMusic();
    	}
    }
    
    public void drawTradeScreen() {
    	
        switch(subState) {
            case 0: trade_select(); break;
            case 1: trade_buy(); break;
            case 2: trade_sell(); break;
        }
        gp.keyH.enterPressed = false;
        gp.keyH.actionPressed = false;
    }

    public void trade_select() {
    	
        drawDialogueScreen();

        // Draw Window
        int x = gp.tileSize * 12;
        int y = gp.tileSize * 3;
        int width = gp.tileSize * 3;
        int height = (int)(gp.tileSize * 3.5);
        drawSubWindow(x, y, width, height);

        x += gp.tileSize;
        y += gp.tileSize;
        g2.drawString("Beli", x, y);
        if(commandNum == 0) {
            g2.drawString(">", x-24, y);
            if(gp.keyH.actionPressed || gp.keyH.enterPressed) { 
            	subState = 1;
            	gp.keyH.actionPressed = false;
            	gp.keyH.enterPressed = false;
            	gp.playSE(9);
            }
        }
        y += gp.tileSize;
        g2.drawString("Jual", x, y);
        if(commandNum == 1) {
            g2.drawString(">", x-24, y);
            if(gp.keyH.actionPressed || gp.keyH.enterPressed) {
            	subState = 2;
            	gp.keyH.actionPressed = false;
            	gp.keyH.enterPressed = false;
            	gp.playSE(9);
            }
        }
        y += gp.tileSize;
        g2.drawString("Keluar", x, y);
        if(commandNum == 2) {
            g2.drawString(">", x-24, y);
            if(gp.keyH.actionPressed || gp.keyH.enterPressed) {
                commandNum = 0;
                gp.gameState = gp.playState;
                gp.keyH.actionPressed = false;
                gp.keyH.enterPressed = false;
                npc = null;
                currentDialogue = "";
                currentSpeakerName = "";
                gp.playSE(9);
            }
        }
    }

    public void trade_buy() {
        // Gambar Inventori (Pemain di kanan, NPC di kiri)
        drawInventory(gp.player, false);
        drawInventory(npc, true);

        int hintY = gp.tileSize * 8;
        drawSubWindow(gp.tileSize * 9 + 14, hintY - gp.tileSize * 3 + 22, gp.tileSize * 5 + 32 , gp.tileSize + 32);
        g2.drawString("[ESC] Kembali", gp.tileSize * 10, gp.tileSize * 6 + 22);

        // Window Koin Pemain
        int coinX = gp.tileSize * 10;
        drawSubWindow(coinX + gp.tileSize + 24, hintY - gp.tileSize + 6, gp.tileSize * 4 - 24, gp.tileSize * 2 - 16);
        g2.drawString("Koin: " + gp.player.coin, coinX + gp.tileSize * 2, hintY);

        // Ambil index item yang sedang ditunjuk kursor NPC
        int itemIndex = getItemIndexOnSlot(npcSlotCol, npcSlotRow);

        if(itemIndex < npc.inventory.size()) {
            // Tampilkan Window Harga
            int price = npc.inventory.get(itemIndex).price;
            int priceX = gp.tileSize * 4;
            int priceY = (int)(gp.tileSize * 5.5);
            drawSubWindow(priceX - 24, priceY - 24, gp.tileSize * 3, gp.tileSize);
            g2.drawImage(coin, priceX - 16, priceY - 19, 32, 32, null);
            g2.drawString("" + price, priceX + 24, priceY + 8);

            // Proses Pembelian saat Enter / E / Space ditekan
            if(gp.keyH.actionPressed == true || gp.keyH.enterPressed == true) {
                gp.keyH.actionPressed = false;
                gp.keyH.enterPressed = false;
                if(price > gp.player.coin) {
                    subState = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "Koinmu tidak cukup untuk membeli itu!";
                    gp.playSE(10);
                }
                else if(gp.player.inventory.size() == gp.player.maxInventorySize) {
                    subState = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "Tasmu sudah penuh!";
                }
                else {
                    gp.player.coin -= price;
                    gp.player.inventory.add(npc.inventory.get(itemIndex));
                    gp.playSE(12);

                    if(gp.qManager.getCurrentQuest() == QuestType.GET_TOOLS) {
                        if(gp.player.hasItem("Kapak") && gp.player.hasItem("Lentera")) {
                            gp.qManager.completeCurrentAndAdvance(QuestType.CLEAR_PATH);
                        }
                    }
                }
            }
        }
    }

    public void trade_sell() {
        // Gambar inventory player dengan cursor
        drawInventory(gp.player, true);

        // Window untuk hint di bagian bawah kiri
        int hintX = gp.tileSize * 2;
        int hintY = gp.tileSize * 7; // 336
        int hintWidth = gp.tileSize * 6; // 288
        int hintHeight = gp.tileSize * 2; // 96
        drawSubWindow(hintX, hintY, hintWidth, hintHeight);
        g2.drawString("[ESC] Kembali", hintX + 24, hintY + 60);

        // Window untuk koin di bagian bawah kanan
        int coinX = gp.tileSize * 9;
        int coinY = gp.tileSize * 7;
        int coinWidth = gp.tileSize * 4;
        int coinHeight = gp.tileSize * 2;
        drawSubWindow(coinX, coinY, coinWidth, coinHeight);
        g2.drawString("Koin: " + gp.player.coin, coinX + 20, coinY + 60);

        int itemIndex = getItemIndexOnSlot(playerSlotCol, playerSlotRow);

        if(itemIndex < gp.player.inventory.size()) {
            // Tampilkan Harga Jual (Setengah Harga) - PERBAIKI POSISI
            int price = gp.player.inventory.get(itemIndex).price / 2;
            int priceX = gp.tileSize * 2;
            int priceY = (int)(gp.tileSize * 5.5);
            int priceWidth = gp.tileSize * 3;
            int priceHeight = gp.tileSize;
            
            drawSubWindow(priceX, priceY, priceWidth, priceHeight);
            g2.drawString("Harga: " + price, priceX + 20, priceY + 32);

            if(gp.keyH.actionPressed || gp.keyH.enterPressed) {
                // Reset flag input
                gp.keyH.actionPressed = false;
                gp.keyH.enterPressed = false;
                
                // Cek apakah item sedang dipakai (Equipped)
                if(gp.player.inventory.get(itemIndex) == gp.player.currentWeapon || 
                   gp.player.inventory.get(itemIndex) == gp.player.currentShield ||
                   gp.player.inventory.get(itemIndex) == gp.player.currentLight) {
                	
                    subState = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "Lepaskan item sebelum menjualnya!";
                    gp.playSE(10); 
                    
                } else {
                    gp.player.coin += price;
                    gp.player.inventory.remove(itemIndex);
                    gp.playSE(12);
                    
                    if(playerSlotCol > 0 && itemIndex % 5 == 0) {
                        playerSlotCol--;
                    }
                }
            }
        }
    }
    
    public int getItemIndexOnSlot(int slotCol, int slotRow) {
	
        int itemIndex = slotCol + (slotRow * 5);
        return itemIndex;
        
    }
    
    public void drawSubWindow(int x, int y, int width, int height) {
        g2.setColor(COLOR_SUBWINDOW_BG);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        
        g2.setColor(Color.WHITE);
        g2.setStroke(STROKE_5);
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }
    
    public int getXforCenteredText(String text) {
        int length = g2.getFontMetrics().stringWidth(text);
        int x = gp.screenWidth/2 - length/2;
        return x;
    }
    
    public int getXforAlignToRightText(String text, int tailX) {
        int length = g2.getFontMetrics().stringWidth(text);
        int x = tailX - length;
        return x;
    }

    public void drawQuestWidget() {
        if(gp.qManager.getCurrentQuest() == null) return;

        int width = 290;
        int height = 75;
        int x = gp.screenWidth - width - 16;
        int y = 14;

        // Background box transparan elegan
        g2.setColor(COLOR_QUEST_BG);
        g2.fillRoundRect(x, y, width, height, 18, 18);

        // Border aksen emas
        g2.setColor(COLOR_QUEST_BORDER);
        g2.setStroke(STROKE_2);
        g2.drawRoundRect(x, y, width, height, 18, 18);

        // Header
        g2.setFont(fontPixel13Bold);
        g2.setColor(COLOR_QUEST_HEADER);
        g2.drawString("MISI AKTIF", x + 14, y + 20);

        // Judul Quest
        g2.setFont(fontPixel16Bold);
        g2.setColor(Color.WHITE);
        g2.drawString(gp.qManager.getCurrentQuest().getTitle(), x + 14, y + 42);

        // Deskripsi ringkas
        g2.setFont(fontPixel11Plain);
        g2.setColor(COLOR_QUEST_DESC);
        String desc = gp.qManager.getCurrentQuest().getDescription();
        if(desc.length() > 42) {
            desc = desc.substring(0, 39) + "...";
        }
        g2.drawString(desc, x + 14, y + 62);
    }

    public void drawQuestBanner() {
        if(!gp.qManager.isBannerActive()) return;

        float alpha = gp.qManager.getBannerAlpha();
        if(alpha < 0f) alpha = 0f;
        if(alpha > 1f) alpha = 1f;

        java.awt.Composite originalComposite = g2.getComposite();
        g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha));

        int bWidth = 420;
        int bHeight = 65;
        int bX = gp.screenWidth / 2 - bWidth / 2;
        int bY = gp.tileSize * 2;

        // Banner box
        g2.setColor(COLOR_BANNER_BG);
        g2.fillRoundRect(bX, bY, bWidth, bHeight, 20, 20);

        g2.setColor(COLOR_BANNER_GOLD);
        g2.setStroke(STROKE_3);
        g2.drawRoundRect(bX, bY, bWidth, bHeight, 20, 20);

        // Header
        g2.setFont(fontPixel18Bold);
        g2.setColor(COLOR_BANNER_GOLD);
        int headerX = getXforCenteredText(gp.qManager.getBannerHeader());
        g2.drawString(gp.qManager.getBannerHeader(), headerX, bY + 28);

        // Subtext
        g2.setFont(fontPixel14Plain);
        g2.setColor(Color.WHITE);
        int textX = getXforCenteredText(gp.qManager.getBannerText());
        g2.drawString(gp.qManager.getBannerText(), textX, bY + 50);

        g2.setComposite(originalComposite);
    }

    public void drawGameClearScreen() {
        g2.setColor(COLOR_CLEAR_OVERLAY);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int textY = gp.tileSize * 2;

        // Title
        g2.setFont(fontPixel54Bold);
        String title = "LUMINA'S REGRET";
        g2.setColor(COLOR_CLEAR_TITLE_SHADOW);
        g2.drawString(title, getXforCenteredText(title) + 3, textY + 3);
        g2.setColor(COLOR_BANNER_GOLD);
        g2.drawString(title, getXforCenteredText(title), textY);

        // Subtitle
        textY += 45;
        g2.setFont(fontPixel22Bold);
        g2.setColor(Color.WHITE);
        String sub = "- KUTUKAN TELAH TERANGKAT -";
        g2.drawString(sub, getXforCenteredText(sub), textY);

        // Narrative
        textY += 40;
        g2.setFont(fontPixel15Plain);
        g2.setColor(COLOR_CLEAR_SUBTEXT);
        String line1 = "Dengan kembalinya Relik Suci, kedamaian menyelimuti tanah Lumina.";
        String line2 = "Goblin King telah ditaklukkan dan kegelapan sirna untuk selamanya.";
        g2.drawString(line1, getXforCenteredText(line1), textY);
        textY += 25;
        g2.drawString(line2, getXforCenteredText(line2), textY);

        // Stats Subwindow
        int boxW = gp.tileSize * 7;
        int boxH = (int)(gp.tileSize * 2.2);
        int boxX = gp.screenWidth / 2 - boxW / 2;
        int boxY = textY + 20;
        drawSubWindow(boxX, boxY, boxW, boxH);

        g2.setFont(fontPixel15Bold);
        g2.setColor(COLOR_BANNER_GOLD);
        g2.drawString("Statistik Petualang:", boxX + 24, boxY + 30);

        g2.setFont(fontPixel14Plain);
        g2.setColor(Color.WHITE);
        g2.drawString("Level Akhir: " + gp.player.level + "   |   Koin: " + gp.player.coin, boxX + 24, boxY + 56);
        g2.drawString("HP Maksimal: " + gp.player.maxLife + "   |   Mana: " + gp.player.maxMana, boxX + 24, boxY + 80);

        // Options
        int optY = boxY + boxH + 40;
        g2.setFont(fontPixel24Bold);

        String opt1 = "Main Lagi (Restart)";
        int opt1X = getXforCenteredText(opt1);
        g2.drawString(opt1, opt1X, optY);
        if(commandNum == 0) {
            g2.drawString(">", opt1X - 30, optY);
        }

        optY += 40;
        String opt2 = "Menu Utama (Title Screen)";
        int opt2X = getXforCenteredText(opt2);
        g2.drawString(opt2, opt2X, optY);
        if(commandNum == 1) {
            g2.drawString(">", opt2X - 30, optY);
        }
    }
}