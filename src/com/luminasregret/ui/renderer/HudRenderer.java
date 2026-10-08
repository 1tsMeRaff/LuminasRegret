package com.luminasregret.ui.renderer;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import com.luminasregret.engine.core.GamePanel;
import com.luminasregret.game.entity.Entity;
import com.luminasregret.game.entity.NPC_Guide;
import com.luminasregret.game.entity.NPC_Merchant;
import com.luminasregret.game.entity.contracts.Interactable;
import com.luminasregret.game.object.OBJ_Chest;
import com.luminasregret.game.object.OBJ_Door;
import com.luminasregret.game.object.OBJ_Door1;
import com.luminasregret.game.tile.interactive.InteractiveTile;
import com.luminasregret.ui.UI;

/**
 * Modul renderer khusus untuk visualisasi HUD pemain (bar hati, kristal mana, antrean pesan floating,
 * widget quest aktif, banner misi, dan prompt interaksi dinamis).
 */
public class HudRenderer {

    private final GamePanel gp;
    private final UI ui;

    public HudRenderer(GamePanel gp, UI ui) {
        this.gp = gp;
        this.ui = ui;
    }

    public void drawPlayerLife(Graphics2D g2) {
        int startX = 20;
        int startY = 16;
        int heartGap = 26; // 24px icon + 2px gap

        // DRAW MAX LIFE (BLANK HEARTS)
        int x = startX;
        int y = startY;
        int i = 0;
        while(i < gp.player.maxLife / 2) {
            g2.drawImage(ui.heart_blank, x, y, null);
            i++;
            x += heartGap;
        }

        // DRAW CURRENT LIFE (HALF / FULL HEARTS)
        x = startX;
        y = startY;
        i = 0;
        while(i < gp.player.life) {
            g2.drawImage(ui.heart_half, x, y, null);
            i++;
            if(i < gp.player.life) {
                g2.drawImage(ui.heart_full, x, y, null);
            }
            i++;
            x += heartGap;
        }

        if(gp.player.life < 0) {
            gp.player.life = 0;
        }

        // DRAW MANA (COMPACT 24x24 CRYSTALS)
        int manaGap = 26;
        int manaY = startY + 28;

        // DRAW MAX MANA (BLANK CRYSTALS)
        x = startX;
        i = 0;
        while(i < gp.player.maxMana) {
            g2.drawImage(ui.playerMana_Blank, x, manaY, null);
            i++;
            x += manaGap;
        }

        // DRAW CURRENT MANA (FULL CRYSTALS)
        x = startX;
        i = 0;
        while(i < gp.player.mana) {
            g2.drawImage(ui.playerMana_Full, x, manaY, null);
            i++;
            x += manaGap;
        }

        if(gp.player.mana < 0) {
            gp.player.mana = 0;
        }
    }

    public void drawMessage(Graphics2D g2) {
        int messageX = 20;
        int messageY = (int)(gp.tileSize * 3.5);
        g2.setFont(ui.fontPixel18Plain);
        FontMetrics fm = g2.getFontMetrics();

        for(int i = 0; i < ui.message.size(); i++) {
            if(ui.message.get(i) != null) {
                String text = ui.message.get(i);
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getHeight();

                g2.setColor(UI.COLOR_BG_DARK_PILL);
                g2.fillRoundRect(messageX, messageY - fm.getAscent() - 3, textWidth + 16, textHeight + 6, 8, 8);

                g2.setColor(UI.COLOR_GOLD_BORDER);
                g2.drawRoundRect(messageX, messageY - fm.getAscent() - 3, textWidth + 16, textHeight + 6, 8, 8);

                g2.setColor(UI.COLOR_SHADOW_BLACK);
                g2.drawString(text, messageX + 9, messageY + 1);
                g2.setColor(Color.white);
                g2.drawString(text, messageX + 8, messageY);

                int counter = ui.messageCounter.get(i) + 1;
                ui.messageCounter.set(i, counter);
                messageY += textHeight + 8;

                if(ui.messageCounter.get(i) > 180) {
                    ui.message.remove(i);
                    ui.messageCounter.remove(i);
                    i--;
                }
            }
        }
    }

    public void drawQuestWidget(Graphics2D g2) {
        if(gp.qManager.getCurrentQuest() == null) return;

        int width = 290;
        int height = 75;
        int x = gp.screenWidth - width - 16;
        int y = 14;

        g2.setColor(UI.COLOR_QUEST_BG);
        g2.fillRoundRect(x, y, width, height, 18, 18);

        g2.setColor(UI.COLOR_QUEST_BORDER);
        g2.setStroke(UI.STROKE_2);
        g2.drawRoundRect(x, y, width, height, 18, 18);

        g2.setFont(ui.fontPixel13Bold);
        g2.setColor(UI.COLOR_QUEST_HEADER);
        g2.drawString("MISI AKTIF", x + 14, y + 20);

        g2.setFont(ui.fontPixel16Bold);
        g2.setColor(Color.WHITE);
        g2.drawString(gp.qManager.getCurrentQuest().getTitle(), x + 14, y + 42);

        g2.setFont(ui.fontPixel11Plain);
        g2.setColor(UI.COLOR_QUEST_DESC);
        String desc = gp.qManager.getCurrentQuest().getDescription();
        if(desc.length() > 42) {
            desc = desc.substring(0, 39) + "...";
        }
        g2.drawString(desc, x + 14, y + 62);
    }

    public void drawQuestBanner(Graphics2D g2) {
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

        g2.setColor(UI.COLOR_BANNER_BG);
        g2.fillRoundRect(bX, bY, bWidth, bHeight, 20, 20);

        g2.setColor(UI.COLOR_BANNER_GOLD);
        g2.setStroke(UI.STROKE_3);
        g2.drawRoundRect(bX, bY, bWidth, bHeight, 20, 20);

        g2.setFont(ui.fontPixel18Bold);
        g2.setColor(UI.COLOR_BANNER_GOLD);
        int headerX = ui.getXforCenteredText(gp.qManager.getBannerHeader());
        g2.drawString(gp.qManager.getBannerHeader(), headerX, bY + 28);

        g2.setFont(ui.fontPixel14Plain);
        g2.setColor(Color.WHITE);
        int textX = ui.getXforCenteredText(gp.qManager.getBannerText());
        g2.drawString(gp.qManager.getBannerText(), textX, bY + 50);

        g2.setComposite(originalComposite);
    }

    public void drawInteractionPrompt(Graphics2D g2) {
        Entity target = gp.player.nearbyInteractable;
        if (target == null) return;

        int screenX = target.worldX - gp.player.worldX + gp.player.screenX;
        int screenY = target.worldY - gp.player.worldY + gp.player.screenY;

        String actionText;
        if (target instanceof NPC_Guide || target instanceof NPC_Merchant) {
            actionText = "[E / Klik] Bicara";
        } else if (target instanceof OBJ_Chest) {
            actionText = "[E / Klik] Buka Peti";
        } else if (target instanceof OBJ_Door || target instanceof OBJ_Door1) {
            actionText = "[E / Klik] Buka Pintu";
        } else if (target instanceof InteractiveTile) {
            actionText = "[E / Klik] Tebas";
        } else if (target instanceof Interactable) {
            actionText = "[E / Klik] " + ((Interactable)target).getInteractionPrompt();
        } else if (target.type == Entity.type_pickupOnly || target.type == Entity.type_sword || 
                   target.type == Entity.type_axe || target.type == Entity.type_shield || 
                   target.type == Entity.type_consumable) {
            actionText = "[E / Klik] Ambil";
        } else {
            actionText = "[E / Klik] Interaksi";
        }

        g2.setFont(ui.fontPixel13Bold);
        int textWidth = g2.getFontMetrics().stringWidth(actionText);
        int boxW = textWidth + 16;
        int boxH = 22;

        double bobOffset = Math.sin(System.currentTimeMillis() * 0.006) * 3;
        int boxX = screenX + (gp.tileSize / 2) - (boxW / 2);
        int boxY = (int) (screenY - 14 + bobOffset);

        g2.setColor(UI.COLOR_PROMPT_BG);
        g2.fillRoundRect(boxX, boxY, boxW, boxH, 10, 10);

        g2.setColor(UI.COLOR_PROMPT_BORDER);
        g2.setStroke(UI.STROKE_1_5);
        g2.drawRoundRect(boxX, boxY, boxW, boxH, 10, 10);

        g2.setColor(Color.WHITE);
        g2.drawString(actionText, boxX + 8, boxY + 15);
    }
}
