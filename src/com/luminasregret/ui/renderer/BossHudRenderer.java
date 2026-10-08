package com.luminasregret.ui.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import com.luminasregret.engine.core.GamePanel;
import com.luminasregret.game.entity.Entity;
import com.luminasregret.ui.UI;

/**
 * Modul renderer khusus untuk visualisasi bar HP monster biasa dan Boss Goblin King (termasuk fase murka).
 */
public class BossHudRenderer {

    private final GamePanel gp;
    private final UI ui;

    public BossHudRenderer(GamePanel gp, UI ui) {
        this.gp = gp;
        this.ui = ui;
    }

    public void drawMonsterLife(Graphics2D g2) {
        for(int i = 0; i < gp.monster[gp.currentMap].length; i++) {
            Entity monster = gp.monster[gp.currentMap][i];
            if(monster != null && monster.inCamera()) {
                if(monster.hpBarOn && !monster.boss) {
                    double oneScale = (double)gp.tileSize / monster.maxLife;
                    double hpBarValue = oneScale * Math.max(0, monster.life);

                    g2.setColor(UI.COLOR_HP_BG);
                    g2.fillRect(monster.getScreenX() - 1, monster.getScreenY() - 16, gp.tileSize + 2, 12);

                    g2.setColor(UI.COLOR_HP_RED);
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

    public void drawBossLife(Graphics2D g2) {
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

                g2.setColor(UI.COLOR_HP_BG);
                g2.fillRect(x - 1, y - 1, gp.tileSize * 8 + 2, 16);

                if(monster.rage) {
                    g2.setColor(UI.COLOR_HP_BOSS_RAGE);
                } else {
                    g2.setColor(UI.COLOR_HP_BOSS_RED);
                }
                g2.fillRect(x, y, (int)hpBarValue, 14);

                g2.setFont(ui.fontPixel20Bold);
                String title = monster.name;
                if(monster.rage) {
                    title += " [FASE 2: MURKA]";
                    g2.setColor(UI.COLOR_BOSS_RAGE_TEXT);
                } else {
                    g2.setColor(Color.white);
                }
                g2.drawString(title, x + 4, y - 8);

                String hpRatio = displayLife + " / " + monster.maxLife;
                int hpTextX = x + gp.tileSize * 8 - g2.getFontMetrics().stringWidth(hpRatio) - 4;
                g2.setColor(Color.white);
                g2.drawString(hpRatio, hpTextX, y - 8);
                break;
            }
        }
    }
}
