package com.luminasregret.ui.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import com.luminasregret.engine.core.GamePanel;
import com.luminasregret.ui.UI;

/**
 * Modul renderer khusus untuk visualisasi layar menu: Title Screen, Pause Overlay, Options Screen,
 * Game Over Screen, Victory Screen, dan efek Fade Transition.
 */
public class MenuRenderer {

    private final GamePanel gp;
    private final UI ui;

    public MenuRenderer(GamePanel gp, UI ui) {
        this.gp = gp;
        this.ui = ui;
    }

    public void drawTitleScreen(Graphics2D g2) {
        g2.setColor(UI.COLOR_TITLE_BG);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Title Name
        g2.setFont(ui.fontPixel80Bold);
        String text = "Lumina's Regret";
        int x = ui.getXforCenteredText(text);
        int y = (int)(gp.tileSize * 2.5);
        
        // Shadow
        g2.setColor(Color.BLACK);
        g2.drawString(text, x + 5, y + 5);
        
        // Main Color
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
        
        // Main Character Image
        x = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
        y += gp.tileSize; 
        g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);
        
        // Menu
        g2.setFont(ui.fontPixel40Bold);
        
        text = "NEW GAME";
        x = ui.getXforCenteredText(text);
        y += gp.tileSize * 3;
        g2.drawString(text, x, y);
        if(ui.commandNum == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }
        
        text = "LOAD GAME";
        x = ui.getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(ui.commandNum == 1) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "QUIT";
        x = ui.getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(ui.commandNum == 2) {
            g2.drawString(">", x - gp.tileSize, y);
        }
    }

    public void drawPauseScreen(Graphics2D g2) {
        String text = "PAUSED";
        g2.setFont(ui.fontPixel50Bold);
        int x = ui.getXforCenteredText(text);
        int y = gp.screenHeight / 2;
        g2.drawString(text, x, y);
    }

    public void drawOptionsScreen(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.setFont(ui.fontPixel32Bold);

        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 8;
        int frameX = (gp.screenWidth - frameWidth) / 2;
        int frameY = (gp.screenHeight - frameHeight) / 2;
        
        DialogueRenderer.drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);

        switch(ui.subState) {
            case 0: options_top(g2, frameX, frameY); break;
            case 1: options_fullScreenNotification(g2, frameX, frameY); break;
            case 2: options_control(g2, frameX, frameY); break;
            case 3: options_endGameConfirmation(g2, frameX, frameY); break;
        }

        gp.keyH.enterPressed = false;
    }

    private void options_top(Graphics2D g2, int frameX, int frameY) {
        String text = "Options";
        int textX = ui.getXforCenteredText(text);
        int textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        int itemDy = gp.tileSize - 10;
        textX = frameX + gp.tileSize - 20;
        textY += gp.tileSize;

        g2.drawString("Full Screen", textX, textY);
        if (ui.commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) {
                gp.toggleFullScreen();
                ui.subState = 1;
            }
        }
        textY += itemDy;

        g2.drawString("Music", textX, textY);
        if (ui.commandNum == 1) {
            g2.drawString(">", textX - 25, textY);
        }
        textY += itemDy;

        g2.drawString("SE", textX, textY);
        if (ui.commandNum == 2) {
            g2.drawString(">", textX - 25, textY);
        }
        textY += itemDy;

        g2.drawString("Control", textX, textY);
        if (ui.commandNum == 3) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) {
                ui.subState = 2;
                ui.commandNum = 0;
            }
        }
        textY += itemDy;

        g2.drawString("End Game", textX, textY);
        if (ui.commandNum == 4) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) {
                ui.subState = 3;
                ui.commandNum = 0;
            }
        }
        textY += itemDy;

        g2.drawString("Back", textX, textY);
        if (ui.commandNum == 5) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) {
                gp.gameState = gp.playState;
                ui.commandNum = 0;
            }
        }

        // Checkbox & Sliders
        int controlX = frameX + (int)(gp.tileSize * 4.9);
        int baseY = frameY + gp.tileSize * 2 + 10;

        g2.setStroke(new BasicStroke(3));
        g2.drawRect(controlX, baseY - 20, 24, 24);
        if (gp.fullScreenOn) {
            g2.fillRect(controlX, baseY - 20, 24, 24);
        }

        baseY += itemDy;
        g2.drawRect(controlX, baseY - 20, 120, 24);
        int volumeWidth = 24 * gp.music.volumeScale;
        g2.fillRect(controlX, baseY - 20, volumeWidth, 24);

        baseY += itemDy;
        g2.drawRect(controlX, baseY - 20, 120, 24);
        volumeWidth = 24 * gp.se.volumeScale;
        g2.fillRect(controlX, baseY - 20, volumeWidth, 24);
        
        gp.config.saveConfig();
    }

    private void options_control(Graphics2D g2, int frameX, int frameY) {
        String text = "Control";
        int textX = ui.getXforCenteredText(text) - 8;
        int textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        int itemDy = gp.tileSize - 12;
        textX = frameX + gp.tileSize - 20;
        textY += gp.tileSize;

        g2.drawString("Move", textX, textY); textY += itemDy;
        g2.drawString("Action", textX, textY); textY += itemDy;
        g2.drawString("Magic", textX, textY); textY += itemDy;
        g2.drawString("Inventory", textX, textY); textY += itemDy;
        g2.drawString("Pause", textX, textY); textY += itemDy;
        g2.drawString("Options", textX, textY);

        textX = (frameX + gp.tileSize * 5) - 18;
        textY = frameY + gp.tileSize * 2;
        g2.drawString("WASD", textX, textY); textY += itemDy;
        g2.drawString("E / ENTER", textX, textY); textY += itemDy;
        g2.drawString("F", textX, textY); textY += itemDy;
        g2.drawString("C", textX, textY); textY += itemDy;
        g2.drawString("P", textX, textY); textY += itemDy;
        g2.drawString("ESC", textX, textY);

        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize * 7 + 20;
        g2.drawString("Back", textX, textY);
        if (ui.commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) {
                ui.subState = 0;
                ui.commandNum = 3;
            }
        }
    }

    private void options_fullScreenNotification(Graphics2D g2, int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        ui.currentDialogue = "The change will take \neffect after restarting \nthe game.";
        for (String line : ui.currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        textY = frameY + gp.tileSize * 7;
        g2.drawString("Back", textX, textY);
        if (ui.commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) {
                ui.subState = 0;
            }
        }
    }

    private void options_endGameConfirmation(Graphics2D g2, int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        ui.currentDialogue = "Apakah kamu yakin?";
        for (String line : ui.currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        String text = "Yes";
        textX = ui.getXforCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if (ui.commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) {
                ui.subState = 0;
                gp.gameState = gp.titleState;
            }
        }

        text = "No";
        textX = ui.getXforCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if (ui.commandNum == 1) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) {
                ui.subState = 0;
                ui.commandNum = 4;
            }
        }
    }

    public void drawGameOverScreen(Graphics2D g2) {
        g2.setColor(UI.COLOR_HALF_BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(ui.fontPixel110Bold);
        String text = "Game Over";

        g2.setColor(Color.BLACK);
        int x = ui.getXforCenteredText(text);
        int y = gp.tileSize * 2;
        g2.drawString(text, x, y);

        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);

        g2.setFont(ui.fontPixel50Bold);
        text = "Retry";
        x = ui.getXforCenteredText(text);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);
        if(ui.commandNum == 0) {
            g2.drawString(">", x - 40, y);
        }

        text = "Quit";
        x = ui.getXforCenteredText(text);
        y += 55;
        g2.drawString(text, x, y);
        if(ui.commandNum == 1) {
            g2.drawString(">", x - 40, y);
        }
    }

    public void drawGameClearScreen(Graphics2D g2) {
        g2.setColor(UI.COLOR_CLEAR_OVERLAY);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int textY = gp.tileSize * 2;

        g2.setFont(ui.fontPixel54Bold);
        String title = "LUMINA'S REGRET";
        g2.setColor(UI.COLOR_CLEAR_TITLE_SHADOW);
        g2.drawString(title, ui.getXforCenteredText(title) + 3, textY + 3);
        g2.setColor(UI.COLOR_BANNER_GOLD);
        g2.drawString(title, ui.getXforCenteredText(title), textY);

        textY += 45;
        g2.setFont(ui.fontPixel22Bold);
        g2.setColor(Color.WHITE);
        String sub = "- KUTUKAN TELAH TERANGKAT -";
        g2.drawString(sub, ui.getXforCenteredText(sub), textY);

        textY += 40;
        g2.setFont(ui.fontPixel15Plain);
        g2.setColor(UI.COLOR_CLEAR_SUBTEXT);
        String line1 = "Dengan kembalinya Relik Suci, kedamaian menyelimuti tanah Lumina.";
        String line2 = "Goblin King telah ditaklukkan dan kegelapan sirna untuk selamanya.";
        g2.drawString(line1, ui.getXforCenteredText(line1), textY);
        textY += 25;
        g2.drawString(line2, ui.getXforCenteredText(line2), textY);

        int boxW = gp.tileSize * 7;
        int boxH = (int)(gp.tileSize * 2.2);
        int boxX = gp.screenWidth / 2 - boxW / 2;
        int boxY = textY + 20;
        DialogueRenderer.drawSubWindow(g2, boxX, boxY, boxW, boxH);

        g2.setFont(ui.fontPixel15Bold);
        g2.setColor(UI.COLOR_BANNER_GOLD);
        g2.drawString("Statistik Petualang:", boxX + 24, boxY + 30);

        g2.setFont(ui.fontPixel14Plain);
        g2.setColor(Color.WHITE);
        g2.drawString("Level Akhir: " + gp.player.level + "   |   Koin: " + gp.player.coin, boxX + 24, boxY + 56);
        g2.drawString("HP Maksimal: " + gp.player.maxLife + "   |   Mana: " + gp.player.maxMana, boxX + 24, boxY + 80);

        int optY = boxY + boxH + 40;
        g2.setFont(ui.fontPixel24Bold);

        String opt1 = "Main Lagi (Restart)";
        int opt1X = ui.getXforCenteredText(opt1);
        g2.drawString(opt1, opt1X, optY);
        if(ui.commandNum == 0) {
            g2.drawString(">", opt1X - 30, optY);
        }

        optY += 40;
        String opt2 = "Menu Utama (Title Screen)";
        int opt2X = ui.getXforCenteredText(opt2);
        g2.drawString(opt2, opt2X, optY);
        if(ui.commandNum == 1) {
            g2.drawString(">", opt2X - 30, optY);
        }
    }

    public void drawTransition(Graphics2D g2) {
        ui.counter++;
        g2.setColor(new Color(0, 0, 0, ui.counter * 5));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        if(ui.counter == 50) {
            ui.counter = 0;
            gp.gameState = gp.playState;
            gp.currentMap = gp.eHandler.tempMap;
            gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
            gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;
            gp.eHandler.previousEventX = gp.player.worldX;
            gp.eHandler.previousEventY = gp.player.worldY;
            gp.playAreaMusic();
        }
    }
}
