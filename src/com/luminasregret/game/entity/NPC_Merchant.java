package com.luminasregret.game.entity;

import java.awt.Rectangle;

import com.luminasregret.engine.core.GamePanel;
import com.luminasregret.game.object.*;

public class NPC_Merchant extends Entity{
	
    public NPC_Merchant(GamePanel gp) {
        super(gp);
        
        name = "Boran - Pedagang Kelana";
        direction = "down";
        speed = 0;

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        solidAreaDefaultX = 8;
        solidAreaDefaultY = 16;
        
        getImage();
        setDialogue();
        setItems();
    }
    
    public final void getImage() {
        up1 = setup("/npc/merchant_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/merchant_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/merchant_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/merchant_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/merchant_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/merchant_right_2", gp.tileSize, gp.tileSize);
    }
    
    public final void setDialogue() {
        if(gp.currentMap == 1) {
            dialogues[0] = "Heh, tak kusangka kau berhasil menembus sampai sejauh ini, kawan!\nPersiapkan senjatamu sebelum memasuki gerbang utara ke sarang sang raja goblin.";
        } else {
            dialogues[0] = "Salam pengelana. Namaku Boran.\nSelama koinmu gemerincing, persediaan di kantongku adalah milikmu.";
        }
        dialogues[1] = "Waspadalah dengan monster di kegelapan. Datang lagi nanti!";
        dialogues[2] = "Koinmu terlalu tipis! Tebas dulu monster liar di sekitar kalau mau belanja.";
        dialogues[3] = "Kantong tasmu sudah sesak! Jual atau gunakan dulu barangmu.";
        dialogues[4] = "Aku tidak menerima barang yang sedang kau kenakan!";
    }
    
    public final void setItems() {
        inventory.add(new OBJ_Bread(gp));
        inventory.add(new OBJ_Axe(gp));
        inventory.add(new OBJ_Shield_Iron(gp));
        inventory.add(new OBJ_Key(gp));
    }

    @Override
    public void setAction() {
        // Boran pedagang selalu diam di lapak toko desanya
        speed = 0;
    }
    
    @Override
    public void speak() {
        // Hadapi pemain
        switch(gp.player.direction) {
            case "up": direction = "down"; break;
            case "down": direction = "up"; break;
            case "left": direction = "right"; break;
            case "right": direction = "left"; break;
        }

        setDialogue();
        gp.ui.currentSpeakerName = name;
        gp.ui.currentDialogue = dialogues[0];
        gp.gameState = gp.tradeState;
        gp.ui.npc = this;
    }
}
