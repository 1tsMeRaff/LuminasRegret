package com.luminasregret.ui.renderer;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import com.luminasregret.engine.core.GamePanel;
import com.luminasregret.ui.UI;

/**
 * Modul renderer khusus untuk visualisasi jendela dialog interaktif, nameplate pembicara,
 * tombol navigasi, dan dynamic text wrapping berstandar anti-overflow.
 */
public class DialogueRenderer {

    private final GamePanel gp;
    private final UI ui;

    public DialogueRenderer(GamePanel gp, UI ui) {
        this.gp = gp;
        this.ui = ui;
    }

    public void drawDialogueScreen(Graphics2D g2) {
        int x;
        int y;
        int width;
        int height;

        // Mode dagang atau dialog normal
        if (gp.gameState == gp.tradeState) {
            x = gp.tileSize;
            width = (int)(gp.tileSize * 10.5);
            height = (int)(gp.tileSize * 3.4);
            y = gp.screenHeight - height - (int)(gp.tileSize * 0.4);
        } else {
            x = gp.tileSize;
            width = gp.screenWidth - (gp.tileSize * 2);
            height = (int)(gp.tileSize * 3.4);
            y = gp.screenHeight - height - (int)(gp.tileSize * 0.4);
        }

        drawSubWindow(g2, x, y, width, height);

        // Speaker Name Tag Badge
        if (ui.currentSpeakerName != null && !ui.currentSpeakerName.trim().isEmpty()) {
            g2.setFont(ui.fontPixel15Bold);
            int badgeW = g2.getFontMetrics().stringWidth(ui.currentSpeakerName) + 32;
            int badgeH = 28;
            int badgeX = x + 20;
            int badgeY = y - 14;

            g2.setColor(UI.COLOR_BADGE_BG);
            g2.fillRoundRect(badgeX, badgeY, badgeW, badgeH, 12, 12);

            g2.setColor(UI.COLOR_BANNER_GOLD);
            g2.setStroke(UI.STROKE_2);
            g2.drawRoundRect(badgeX, badgeY, badgeW, badgeH, 12, 12);

            g2.drawString(ui.currentSpeakerName, badgeX + 16, badgeY + 19);
        }

        // Teks dialog utama
        g2.setFont(ui.fontPixel19Plain);
        g2.setColor(Color.WHITE);

        FontMetrics fm = g2.getFontMetrics();
        int textPaddingX = 28;
        int textPaddingY = 36;
        int maxTextWidth = width - (textPaddingX * 2);
        int textX = x + textPaddingX;
        int textY = y + textPaddingY;
        int lineHeight = 28;

        if (ui.currentDialogue != null && !ui.currentDialogue.isEmpty()) {
            ArrayList<String> wrappedLines = wrapDialogueText(ui.currentDialogue, maxTextWidth, fm);
            for (String line : wrappedLines) {
                g2.drawString(line, textX, textY);
                textY += lineHeight;
            }
        }

        // Indikator hint tombol lanjut
        g2.setFont(ui.fontPixel13Bold);
        String promptHint = "[E / ENTER / Klik] Lanjut ▶";
        int hintW = g2.getFontMetrics().stringWidth(promptHint);
        int hintX = x + width - hintW - 24;
        int hintY = y + height - 16;
        g2.setColor(UI.COLOR_BANNER_GOLD);
        g2.drawString(promptHint, hintX, hintY);
    }

    public static ArrayList<String> wrapDialogueText(String rawText, int maxWidth, FontMetrics fm) {
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

    public static void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(UI.COLOR_SUBWINDOW_BG);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        g2.setColor(Color.WHITE);
        g2.setStroke(UI.STROKE_5);
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }
}
