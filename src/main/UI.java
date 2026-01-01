package main;

import entity.Entity;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaBar;

public class UI {
    
    GamePanel gp;
    Graphics2D g2;
    Font kingThings, kingThingsL, fusionPixel;
    BufferedImage heart_full, heart_half, heart_blank, playerMana_Full, playerMana_Blank, coin;
    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = " ";
    public int commandNum = 0;
    public int titleScreenState = 0;
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;
    int subState = 0;
    int counter = 0;
    public Entity npc;
    
    
    public UI(GamePanel gp) {
        this.gp = gp;
        
        
        try {
            InputStream is = getClass().getResourceAsStream("/font/Kingthings_Petrock.ttf");
            kingThings = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/Kingthings_Petrock_light.ttf");
            kingThingsL = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/fusion-pixel.ttf");
            fusionPixel = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
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
    
    public void addMessage(String text) {
        
        message.add(text);
        messageCounter.add(0);
    }
    
    public void draw(Graphics2D g2) {
        
        this.g2 = g2;
        
        g2.setFont(fusionPixel);
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
            drawMessage();
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
    }
    
    public void drawPlayerLife() {
        
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        int startX = x; 
        
        // DRAW MAX LIFE
        while(i < gp.player.maxLife / 2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize + 10;
        }
        
        int endX = x - 10; 
        int totalHudWidth = endX - startX;
        
        // DRAW CURRENT LIFE
        x = gp.tileSize / 2;
        y = gp.tileSize / 2;
        i = 0;
        
        while(i < gp.player.life) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if(i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize + 10;
        }
        
        if(gp.player.life <= 0) {
        	gp.player.life = 0;
        }

        // DRAW MANA
        int manaWidth = (gp.player.maxMana > 0) ? totalHudWidth / gp.player.maxMana : 0; 
        
        // Posisi Y Mana Bar (Sedikit di bawah hati)
        y = (int) (gp.tileSize * 1.5); 
        
        x = startX;
        i = 0;
        while (i < gp.player.maxMana) {
            g2.drawImage(playerMana_Blank, x, y, manaWidth, gp.tileSize, null);
            i++;
            x += manaWidth - 1; 
        }

        x = startX;
        i = 0;
        while (i < gp.player.mana) {
            g2.drawImage(playerMana_Full, x, y, manaWidth, gp.tileSize, null);
            i++;
            x += manaWidth - 1;
        }
    }
    
    public void drawMessage(){
        
        int messageX = gp.tileSize;
        int messageY = gp.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        for(int i = 0; i < message.size(); i++) {
            if(message.get(i) != null) {
                
                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX+2, messageY+2);
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1; // messageCounter++
                messageCounter.set(i, counter); // set the counter to the array
                messageY += 40; // Spacing antar pesan diperkecil sedikit

                if(messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }
    
    public void drawTitleScreen() {
        
        g2.setColor(new Color(0,102,102));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Title Name
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80F));
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
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
        
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
        
        Font originalFont = g2.getFont();
        Font largeFont = originalFont.deriveFont(Font.PLAIN, 48f);
        g2.setFont(largeFont);
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2;
        
        g2.drawString(text, x, y);
    }
    
    public void drawDialogueScreen() {
        
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 5;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 3;
        
        drawSubWindow(x, y, width, height);
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,28F));
        x += gp.tileSize;
        y += gp.tileSize;
        
        for(String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 35;
        }
        
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
        g2.setFont(g2.getFont().deriveFont(28F));

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
                    entity.inventory.get(i) == entity.currentShield || entity.inventory.get(i) == entity.currentLight) {
                g2.setColor(new Color(240,190,90));
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
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(cursorX,cursorY,cursorWidth,cursorHeight,10,10);

            //DESCRIPTION FRAME
            int dFrameX = frameX;
            int dFrameY = frameY + frameHeight;
            int dFrameWidth = frameWidth;
            int dFrameHeight = gp.tileSize * 3;
            
            // Sesuaikan posisi description frame agar tidak keluar dari layar bawah
            if (dFrameY + dFrameHeight > gp.screenHeight) {
                dFrameY = frameY - dFrameHeight;  // Taruh di atas jika tidak muat di bawah
            }

            //DRAW DESCRIPTION TEXT
            int textX = dFrameX + 20;
            int textY = dFrameY + gp.tileSize;
            g2.setFont(g2.getFont().deriveFont(28F));

            int itemIndex = getItemIndexOnSlot(slotCol, slotRow);
            if(itemIndex < entity.inventory.size()) {
                drawSubWindow(dFrameX,dFrameY,dFrameWidth,dFrameHeight);
                for(String line : entity.inventory.get(itemIndex).description.split("\n")) {
                    g2.drawString(line,textX,textY);
                    textY += 32;
                }
            }
        }
    }
    
    public void drawGameOverScreen() {
        g2.setColor(new Color(0,0,0,150)); //Half-black
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,110f));
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
        g2.setFont(g2.getFont().deriveFont(50f));
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
        g2.setFont(g2.getFont().deriveFont(32F));

        // 1. HITUNG UKURAN FRAME
        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 8;

        // 2. HITUNG POSISI FRAME AGAR TEPAT DI TENGAH LAYAR
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
                gp.fullScreenOn = !gp.fullScreenOn;
                subState = 1;
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
    	}
    }
    
    public void drawTradeScreen() {
    	
        switch(subState) {
            case 0: trade_select(); break;
            case 1: trade_buy(); break;
            case 2: trade_sell(); break;
        }
        gp.keyH.enterPressed = false;
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
            if(gp.keyH.enterPressed) subState = 1;
        }
        y += gp.tileSize;
        g2.drawString("Jual", x, y);
        if(commandNum == 1) {
            g2.drawString(">", x-24, y);
            if(gp.keyH.enterPressed) subState = 2;
        }
        y += gp.tileSize;
        g2.drawString("Keluar", x, y);
        if(commandNum == 2) {
            g2.drawString(">", x-24, y);
            if(gp.keyH.enterPressed) {
                commandNum = 0;
                gp.gameState = gp.playState;
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
            g2.drawImage(coin, priceX - 16, priceY - 19, 32, 32, null); // Asumsi ada coinImage
            g2.drawString("" + price, priceX + 24, priceY + 8);

            // Proses Pembelian saat Enter ditekan
            if(gp.keyH.actionPressed) {
                if(price > gp.player.coin) {
                    subState = 0; // Kembali ke menu utama
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
                }
            }
        }
    }

    public void trade_sell() {
        drawInventory(gp.player, true);

        int hintY = gp.tileSize * 7;
        drawSubWindow(gp.tileSize * 2, hintY, gp.tileSize * 8, gp.tileSize * 2);
        g2.drawString("[ESC] Kembali", gp.tileSize * 3, hintY + gp.tileSize);

        int coinX = gp.tileSize * 11;
        drawSubWindow(coinX, hintY, gp.tileSize * 4, gp.tileSize * 2);
        g2.drawString("Koin: " + gp.player.coin, coinX + 20, hintY + gp.tileSize);

        int itemIndex = getItemIndexOnSlot(playerSlotCol, playerSlotRow);

        if(itemIndex < gp.player.inventory.size()) {
            // Tampilkan Harga Jual (Setengah Harga)
            int price = gp.player.inventory.get(itemIndex).price / 2;
            int priceX = gp.tileSize * 11;
            int priceY = (int)(gp.tileSize * 5.5);
            drawSubWindow(priceX, priceY, gp.tileSize * 3, gp.tileSize);
            g2.drawString("" + price, priceX + 50, priceY + 32);

            if(gp.keyH.enterPressed) {
                // Cek apakah item sedang dipakai (Equipped)
                if(gp.player.inventory.get(itemIndex) == gp.player.currentWeapon || 
                   gp.player.inventory.get(itemIndex) == gp.player.currentShield) {
                    subState = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "Lepaskan item sebelum menjualnya!";
                } else {
                    gp.player.coin += price;
                    gp.player.inventory.remove(itemIndex);
                    gp.playSE(12);
                }
            }
        }
    }
    
public int getItemIndexOnSlot(int slotCol, int slotRow) {
	
        int itemIndex = slotCol + (slotRow * 5);
        return itemIndex;
        
    }
    
    public void drawSubWindow(int x, int y, int width, int height) {
        
        Color c = new Color(0, 0, 0, 200);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        
        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
        
    }
    
    public int getXforCenteredText(String text) {
        
        int lenght = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - lenght/2;
        return x;
    }
    
    public int getXforAlignToRightText(String text, int tailX) {
        
        int lenght = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - lenght;
        return x;
    }
}