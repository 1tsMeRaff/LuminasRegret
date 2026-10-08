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

import com.luminasregret.engine.core.GamePanel;
import com.luminasregret.game.entity.Entity;
import com.luminasregret.game.object.OBJ_Coin_Bronze;
import com.luminasregret.game.object.OBJ_Heart;
import com.luminasregret.game.object.OBJ_ManaBar;
import com.luminasregret.ui.renderer.BossHudRenderer;
import com.luminasregret.ui.renderer.DialogueRenderer;
import com.luminasregret.ui.renderer.HudRenderer;
import com.luminasregret.ui.renderer.InventoryRenderer;
import com.luminasregret.ui.renderer.MenuRenderer;

/**
 * Facade Pattern untuk subsistem UI Lumina's Regret.
 * Mengoordinasikan delegasi render visual ke 5 sub-renderer independen:
 * HudRenderer, BossHudRenderer, DialogueRenderer, MenuRenderer, dan InventoryRenderer.
 */
@SuppressWarnings("this-escape")
public final class UI {
    
    public GamePanel gp;
    public Graphics2D g2;
    public Font kingThings, kingThingsL, fusionPixel;
    public BufferedImage heart_full, heart_half, heart_blank, playerMana_Full, playerMana_Blank, coin;
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
    public int counter = 0;
    public Entity npc;
    
    // Sub-renderer Modular (SRP & GoF Facade Pattern)
    public final HudRenderer hudRenderer;
    public final BossHudRenderer bossHudRenderer;
    public final DialogueRenderer dialogueRenderer;
    public final MenuRenderer menuRenderer;
    public final InventoryRenderer inventoryRenderer;
    
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

        // Inisialisasi Sub-renderer
        hudRenderer = new HudRenderer(gp, this);
        bossHudRenderer = new BossHudRenderer(gp, this);
        dialogueRenderer = new DialogueRenderer(gp, this);
        menuRenderer = new MenuRenderer(gp, this);
        inventoryRenderer = new InventoryRenderer(gp, this);
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

    public boolean containsMessage(String text) {
        if (text == null) return false;
        for (String msg : message) {
            if (msg != null && msg.contains(text)) {
                return true;
            }
        }
        return false;
    }
    
    public void draw(Graphics2D g2) {
        this.g2 = g2;
        
        if (fusionPixel != null) {
            g2.setFont(fusionPixel);
        }
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.white);
        
        // Title State
        if(gp.gameState == gp.titleState) {
            menuRenderer.drawTitleScreen(g2);
            hudRenderer.drawMessage(g2);
        }
        // Play State
        else if(gp.gameState == gp.playState) {
            hudRenderer.drawPlayerLife(g2);
            bossHudRenderer.drawMonsterLife(g2);
            bossHudRenderer.drawBossLife(g2);
            hudRenderer.drawMessage(g2);
            hudRenderer.drawQuestWidget(g2);
            hudRenderer.drawQuestBanner(g2);
            hudRenderer.drawInteractionPrompt(g2);
        }
        // Pause State
        else if(gp.gameState == gp.pauseState) {
            hudRenderer.drawPlayerLife(g2);
            menuRenderer.drawPauseScreen(g2);
        }
        // Dialogue State
        else if(gp.gameState == gp.dialogueState) {
            hudRenderer.drawPlayerLife(g2);
            dialogueRenderer.drawDialogueScreen(g2);
            hudRenderer.drawQuestBanner(g2);
        }
        // Character State
        else if(gp.gameState == gp.characterState) {
            inventoryRenderer.drawCharacterScreen(g2);
            inventoryRenderer.drawInventory(g2, gp.player, true);
        }
        // Option State
        else if(gp.gameState == gp.optionsState) {
            menuRenderer.drawOptionsScreen(g2);
        }
        // Game Over State
        else if(gp.gameState == gp.gameOverState) {
            menuRenderer.drawGameOverScreen(g2);
        }
        // Transition State
        else if(gp.gameState == gp.transitionState) {
            menuRenderer.drawTransition(g2);
        }
        // Trade State
        else if(gp.gameState == gp.tradeState) {
            inventoryRenderer.drawTradeScreen(g2);
        }
        // Game Clear State
        else if(gp.gameState == gp.gameClearState) {
            menuRenderer.drawGameClearScreen(g2);
        }
    }
    
    // Metode delegasi untuk kompatibilitas penuh
    public void drawPlayerLife() { hudRenderer.drawPlayerLife(g2); }
    public void drawMonsterLife() { bossHudRenderer.drawMonsterLife(g2); }
    public void drawBossLife() { bossHudRenderer.drawBossLife(g2); }
    public void drawMessage() { hudRenderer.drawMessage(g2); }
    public void drawQuestWidget() { hudRenderer.drawQuestWidget(g2); }
    public void drawQuestBanner() { hudRenderer.drawQuestBanner(g2); }
    public void drawInteractionPrompt() { hudRenderer.drawInteractionPrompt(g2); }
    public void drawDialogueScreen() { dialogueRenderer.drawDialogueScreen(g2); }
    public void drawTitleScreen() { menuRenderer.drawTitleScreen(g2); }
    public void drawPauseScreen() { menuRenderer.drawPauseScreen(g2); }
    public void drawOptionsScreen() { menuRenderer.drawOptionsScreen(g2); }
    public void drawGameOverScreen() { menuRenderer.drawGameOverScreen(g2); }
    public void drawGameClearScreen() { menuRenderer.drawGameClearScreen(g2); }
    public void drawTransition() { menuRenderer.drawTransition(g2); }
    public void drawCharacterScreen() { inventoryRenderer.drawCharacterScreen(g2); }
    public void drawInventory(Entity entity, boolean cursor) { inventoryRenderer.drawInventory(g2, entity, cursor); }
    public void drawTradeScreen() { inventoryRenderer.drawTradeScreen(g2); }
    public void trade_select() { inventoryRenderer.trade_select(g2); }
    public void trade_buy() { inventoryRenderer.trade_buy(g2); }
    public void trade_sell() { inventoryRenderer.trade_sell(g2); }
    public int getItemIndexOnSlot(int slotCol, int slotRow) { return inventoryRenderer.getItemIndexOnSlot(slotCol, slotRow); }

    public static ArrayList<String> wrapDialogueText(String rawText, int maxWidth, FontMetrics fm) {
        return DialogueRenderer.wrapDialogueText(rawText, maxWidth, fm);
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        DialogueRenderer.drawSubWindow(g2, x, y, width, height);
    }
    
    public int getXforCenteredText(String text) {
        int length = g2.getFontMetrics().stringWidth(text);
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }
    
    public int getXforAlignToRightText(String text, int tailX) {
        int length = g2.getFontMetrics().stringWidth(text);
        int x = tailX - length;
        return x;
    }
}