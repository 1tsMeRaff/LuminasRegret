package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import object.OBJ_Key;

public class UI {
    
    GamePanel gp;
    Graphics2D g2;
    Font kingThings, kingThingsL, fusionPixel;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    public String currentDialogue = " ";
    
    
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
    }
    
    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }
    
    public void draw(Graphics2D g2) {
    	
    	this.g2 = g2;
    	
    	g2.setFont(fusionPixel);
    	g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    	g2.setColor(Color.white);
    	
    	//Play State
    	if(gp.gameState == gp.playState) {
    		
    	}
    	//Pause State
    	if(gp.gameState == gp.pauseState) {
    		drawPauseScreen();
    	}
    	//Dialogue State
    	if(gp.gameState == gp.dialogueState) {
    		drawDialogueScreen();
    	}
    }
    public void drawPauseScreen() {
    	
    	String text = "PAUSED";
    	int x = getXforCenteredText(text) ;
    	
    	int y = gp.screenHeight/2;
    	
    	g2.drawString(text, x, y);
    }
    
    public void drawDialogueScreen() {
    	
    	//Window
    	int x = gp.tileSize * 2;
    	int y = gp.tileSize / 2;
    	int width = gp.screenWidth - (gp.tileSize * 4);
    	int height = gp.tileSize * 4;
    	drawSubWindow(x, y, width, height);
    	
    	g2.setFont(g2.getFont().deriveFont(Font.PLAIN,28F));
    	x += gp.tileSize;
    	y += gp.tileSize;
    	
    	for(String line : currentDialogue.split("/n")) {
    		g2.drawString(line, x, y);
    		y += 40;
    	}
    	
    }
    public void drawSubWindow(int x, int y, int widht, int height) {
    	
    	Color c = new Color(0, 0, 0, 200);
    	g2.setColor(c);
    	g2.fillRoundRect(x, y, widht, height, 35, 35);
    	
    	c = new Color(255, 255, 255);
    	g2.setColor(c);
    	g2.setStroke(new BasicStroke(5));
    	g2.drawRoundRect(x+5, y+5, widht-10, height-10, 25, 25);
    	
    }
    
    public int getXforCenteredText(String text) {
    	int lenght = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
    	int x = gp.screenWidth/2 - lenght/2;
    	return x;
    	
    }
    
}