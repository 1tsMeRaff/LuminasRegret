package com.luminasregret.ui.renderer;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import com.luminasregret.engine.core.GamePanel;
import com.luminasregret.game.entity.Entity;
import com.luminasregret.game.quest.QuestType;
import com.luminasregret.ui.UI;

/**
 * Modul renderer khusus untuk visualisasi layar karakter, inventori 5x4 grid, deskripsi perlengkapan,
 * dan antarmuka transaksi pedagang (Trade Screen: Beli / Jual / Keluar).
 */
public class InventoryRenderer {

    private final GamePanel gp;
    private final UI ui;

    public InventoryRenderer(GamePanel gp, UI ui) {
        this.gp = gp;
        this.ui = ui;
    }

    public void drawCharacterScreen(Graphics2D g2) {
        final int frameX = gp.tileSize;
        final int frameY = (int)(gp.tileSize * 0.4);
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 8;

        DialogueRenderer.drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);
        
        g2.setColor(Color.white);
        g2.setFont(ui.fontPixel28Bold);

        int textX = frameX + 20;
        int textY = frameY + 38;
        final int lineHeight = 28;

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
        
        int tailX = (frameX + frameWidth) - 20;
        textY = frameY + 38;
        String value;

        value = String.valueOf(gp.player.level);
        textX = ui.getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = ui.getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
        textX = ui.getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.strength);
        textX = ui.getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = ui.getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = ui.getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = ui.getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.exp);
        textX = ui.getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = ui.getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = ui.getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        if (gp.player.currentWeapon != null && gp.player.currentWeapon.down1 != null) {
            g2.drawImage(gp.player.currentWeapon.down1, tailX - 34, textY - 32, null);
        }
        textY += 42;
        if (gp.player.currentShield != null && gp.player.currentShield.down1 != null) {
            g2.drawImage(gp.player.currentShield.down1, tailX - 34, textY - 38, null);
        }
    }

    public void drawInventory(Graphics2D g2, Entity entity, boolean cursor) {
        int frameX;
        int frameY;
        int frameWidth;
        int frameHeight;
        int slotCol;
        int slotRow;
        
        if(entity == gp.player) {
            frameX = gp.tileSize * 9 + 12;
            frameY = gp.tileSize - 24;
            frameWidth = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = ui.playerSlotCol;
            slotRow = ui.playerSlotRow;
        } else {
            frameX = gp.tileSize - 12;
            frameY = gp.tileSize - 24;
            frameWidth = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = ui.npcSlotCol;
            slotRow = ui.npcSlotRow;
        }

        DialogueRenderer.drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);

        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tileSize + 3;

        for(int i = 0; i < entity.inventory.size(); i++) {
            if(entity.inventory.get(i) == entity.currentWeapon ||
               entity.inventory.get(i) == entity.currentShield || 
               entity.inventory.get(i) == entity.currentLight) {
            	
                g2.setColor(UI.COLOR_EQUIP_CURSOR);
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            }

            if (entity.inventory.get(i).down1 != null) {
                g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);
            }

            slotX += slotSize;
            if(i == 4 || i == 9 || i == 14) {
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        if(cursor) {
            int cursorX = slotXstart + (slotSize * slotCol);
            int cursorY = slotYstart + (slotSize * slotRow);
            int cursorWidth = gp.tileSize;
            int cursorHeight = gp.tileSize;

            g2.setColor(Color.white);
            g2.setStroke(UI.STROKE_3);
            g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

            int dFrameX = frameX;
            int dFrameY = frameY + frameHeight;
            int dFrameWidth = frameWidth;
            int dFrameHeight = gp.tileSize * 3;
            
            if (dFrameY + dFrameHeight > gp.screenHeight) {
                dFrameY = frameY - dFrameHeight;
            }

            int itemIndex = getItemIndexOnSlot(slotCol, slotRow);
            if(itemIndex < entity.inventory.size()) {
                DialogueRenderer.drawSubWindow(g2, dFrameX, dFrameY, dFrameWidth, dFrameHeight);

                g2.setFont(ui.fontPixel16Plain);
                g2.setColor(Color.WHITE);

                FontMetrics fm = g2.getFontMetrics();
                int textX = dFrameX + 18;
                int textY = dFrameY + 30;
                int maxTextWidth = dFrameWidth - 36;
                int lineHeight = 22;

                String desc = entity.inventory.get(itemIndex).description;
                if (desc != null && !desc.isEmpty()) {
                    ArrayList<String> descLines = DialogueRenderer.wrapDialogueText(desc, maxTextWidth, fm);
                    for (String line : descLines) {
                        g2.drawString(line, textX, textY);
                        textY += lineHeight;
                    }
                }
            }
        }
    }

    public void drawTradeScreen(Graphics2D g2) {
        switch(ui.subState) {
            case 0: trade_select(g2); break;
            case 1: trade_buy(g2); break;
            case 2: trade_sell(g2); break;
        }
        gp.keyH.enterPressed = false;
        gp.keyH.actionPressed = false;
    }

    public void trade_select(Graphics2D g2) {
        ui.drawDialogueScreen();

        int x = gp.tileSize * 12;
        int y = gp.tileSize * 3;
        int width = gp.tileSize * 3;
        int height = (int)(gp.tileSize * 3.5);
        DialogueRenderer.drawSubWindow(g2, x, y, width, height);

        x += gp.tileSize;
        y += gp.tileSize;
        g2.drawString("Beli", x, y);
        if(ui.commandNum == 0) {
            g2.drawString(">", x - 24, y);
            if(gp.keyH.actionPressed || gp.keyH.enterPressed) { 
            	ui.subState = 1;
            	gp.keyH.actionPressed = false;
            	gp.keyH.enterPressed = false;
            	gp.playSE(9);
            }
        }
        y += gp.tileSize;
        g2.drawString("Jual", x, y);
        if(ui.commandNum == 1) {
            g2.drawString(">", x - 24, y);
            if(gp.keyH.actionPressed || gp.keyH.enterPressed) { 
            	ui.subState = 2;
            	gp.keyH.actionPressed = false;
            	gp.keyH.enterPressed = false;
            	gp.playSE(9);
            }
        }
        y += gp.tileSize;
        g2.drawString("Keluar", x, y);
        if(ui.commandNum == 2) {
            g2.drawString(">", x - 24, y);
            if(gp.keyH.actionPressed || gp.keyH.enterPressed) { 
                ui.commandNum = 0;
                gp.gameState = gp.playState;
                gp.keyH.actionPressed = false;
                gp.keyH.enterPressed = false;
                ui.npc = null;
                ui.currentDialogue = "";
                ui.currentSpeakerName = "";
                gp.playSE(9);
            }
        }
    }

    public void trade_buy(Graphics2D g2) {
        drawInventory(g2, gp.player, false);
        drawInventory(g2, ui.npc, true);

        int hintY = gp.tileSize * 8;
        DialogueRenderer.drawSubWindow(g2, gp.tileSize * 9 + 14, hintY - gp.tileSize * 3 + 22, gp.tileSize * 5 + 32, gp.tileSize + 32);
        g2.drawString("[ESC] Kembali", gp.tileSize * 10, gp.tileSize * 6 + 22);

        int coinX = gp.tileSize * 10;
        DialogueRenderer.drawSubWindow(g2, coinX + gp.tileSize + 24, hintY - gp.tileSize + 6, gp.tileSize * 4 - 24, gp.tileSize * 2 - 16);
        g2.drawString("Koin: " + gp.player.coin, coinX + gp.tileSize * 2, hintY);

        int itemIndex = getItemIndexOnSlot(ui.npcSlotCol, ui.npcSlotRow);

        if(ui.npc != null && itemIndex < ui.npc.inventory.size()) {
            int price = ui.npc.inventory.get(itemIndex).price;
            int priceX = gp.tileSize * 4;
            int priceY = (int)(gp.tileSize * 5.5);
            DialogueRenderer.drawSubWindow(g2, priceX - 24, priceY - 24, gp.tileSize * 3, gp.tileSize);
            if (ui.coin != null) {
                g2.drawImage(ui.coin, priceX - 16, priceY - 19, 32, 32, null);
            }
            g2.drawString("" + price, priceX + 24, priceY + 8);

            if(gp.keyH.actionPressed || gp.keyH.enterPressed) {
                gp.keyH.actionPressed = false;
                gp.keyH.enterPressed = false;
                if(price > gp.player.coin) {
                    ui.subState = 0;
                    gp.gameState = gp.dialogueState;
                    ui.currentDialogue = "Koinmu tidak cukup untuk membeli itu!";
                    gp.playSE(10);
                } else if(gp.player.inventory.size() == gp.player.maxInventorySize) {
                    ui.subState = 0;
                    gp.gameState = gp.dialogueState;
                    ui.currentDialogue = "Tasmu sudah penuh!";
                } else {
                    gp.player.coin -= price;
                    gp.player.inventory.add(ui.npc.inventory.get(itemIndex));
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

    public void trade_sell(Graphics2D g2) {
        drawInventory(g2, gp.player, true);

        int hintX = gp.tileSize * 2;
        int hintY = gp.tileSize * 7;
        int hintWidth = gp.tileSize * 6;
        int hintHeight = gp.tileSize * 2;
        DialogueRenderer.drawSubWindow(g2, hintX, hintY, hintWidth, hintHeight);
        g2.drawString("[ESC] Kembali", hintX + 24, hintY + 60);

        int coinX = gp.tileSize * 9;
        int coinY = gp.tileSize * 7;
        int coinWidth = gp.tileSize * 4;
        int coinHeight = gp.tileSize * 2;
        DialogueRenderer.drawSubWindow(g2, coinX, coinY, coinWidth, coinHeight);
        g2.drawString("Koin: " + gp.player.coin, coinX + 20, coinY + 60);

        int itemIndex = getItemIndexOnSlot(ui.playerSlotCol, ui.playerSlotRow);

        if(itemIndex < gp.player.inventory.size()) {
            int price = gp.player.inventory.get(itemIndex).price / 2;
            int priceX = gp.tileSize * 2;
            int priceY = (int)(gp.tileSize * 5.5);
            int priceWidth = gp.tileSize * 3;
            int priceHeight = gp.tileSize;
            
            DialogueRenderer.drawSubWindow(g2, priceX, priceY, priceWidth, priceHeight);
            g2.drawString("Harga: " + price, priceX + 20, priceY + 32);

            if(gp.keyH.actionPressed || gp.keyH.enterPressed) {
                gp.keyH.actionPressed = false;
                gp.keyH.enterPressed = false;
                
                if(gp.player.inventory.get(itemIndex) == gp.player.currentWeapon || 
                   gp.player.inventory.get(itemIndex) == gp.player.currentShield ||
                   gp.player.inventory.get(itemIndex) == gp.player.currentLight) {
                	
                    ui.subState = 0;
                    gp.gameState = gp.dialogueState;
                    ui.currentDialogue = "Lepaskan item sebelum menjualnya!";
                    gp.playSE(10); 
                } else {
                    gp.player.coin += price;
                    gp.player.inventory.remove(itemIndex);
                    gp.playSE(12);
                    
                    if(ui.playerSlotCol > 0 && itemIndex % 5 == 0) {
                        ui.playerSlotCol--;
                    }
                }
            }
        }
    }

    public int getItemIndexOnSlot(int slotCol, int slotRow) {
        return slotCol + (slotRow * 5);
    }
}
