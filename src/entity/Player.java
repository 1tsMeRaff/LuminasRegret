package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Axe;
import object.OBJ_Key;
import object.OBJ_Lantern;
import object.OBJ_Shield_Wood;
import object.OBJ_Slash;
import object.OBJ_Sword_Standard;
import tile_interactive.InteractiveTile;

@SuppressWarnings("this-escape")
public class Player extends Entity {
    
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCanceled = false;
    public boolean lightUpdated = false;
    
    // Dash
    public boolean dashing = false;
    public int dashCounter = 0;
    public int dashCoolDown = 0;
    final int dashDuration = 8;     // 0.25 detik
    final int dashCoolDownMax = 40;  // Cooldown dash
    
    public Player(GamePanel gp, KeyHandler keyH) {
        
        super(gp);
        
        this.keyH = keyH;
        
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);
        
        // Solid Area
        solidArea = new Rectangle();
        solidArea.x = 15;
        solidArea.y = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 18;
        solidArea.height = 20;
        
        
        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }
    
    public void setDefaultValues() {
        
        worldX = gp.tileSize * 20;
        worldY = gp.tileSize * 21;
        defaultSpeed = 3;
        speed = defaultSpeed;
        direction = "down";
        
     // PLAYER STATUS
        level = 1;
        maxLife = 6;
        life = maxLife;
        maxMana = 4;
        mana = maxMana;
        strength = 2;
        dexterity = 1;
        exp = 0;
        nextLevelExp = 5;
        coin = 20;
        currentWeapon = new OBJ_Sword_Standard(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        currentLight = null;
        projectile = new OBJ_Slash(gp);
        attack = getAttack(); // The total attack value is decided by strength and weapon
        defense = getDefense(); // The total defense value is decided by dexterity and shield
    }
    public void setDefaultPositions() {
    	
    	if(gp.currentMap == 0) {
    		gp.currentMap = 0;
            worldX = gp.tileSize * 23;
            worldY = gp.tileSize * 23;
            direction = "down";
    	}
    	else {
    		gp.currentMap = 1;
            worldX = gp.tileSize * 13;
            worldY = gp.tileSize * 39;
            direction = "down";
    	}
    	
    }
    
    public void restoreStatus() {
    	
    	life = maxLife;
    	mana = maxMana;
    	invincible = false;
    	attacking = false;
    	knockBack = false;
    	speed = defaultSpeed;
//    	lightUpdate = true;
    }

    public void setItems() {
    	
    	inventory.clear();
    	inventory.add(currentWeapon);
    	inventory.add(currentShield);
    }
    public int getAttack() {
    	attackArea = currentWeapon.attackArea;
    	motion1_duration = currentWeapon.motion1_duration;
    	motion2_duration = currentWeapon.motion2_duration;
    	return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense() {
        return defense = dexterity * currentShield.defenseValue;
    }
    
    public void getPlayerImage() {
        
        up1 = setup("/player/top1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/top2", gp.tileSize, gp.tileSize);
        down1 = setup("/player/bot1", gp.tileSize, gp.tileSize); 
        down2 = setup("/player/bot2", gp.tileSize, gp.tileSize);
        left1 = setup("/player/left1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/left2", gp.tileSize, gp.tileSize);
        right1 = setup("/player/right1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/right2", gp.tileSize, gp.tileSize);
    }
    public void getPlayerAttackImage() {
    	
    	if(currentWeapon.type == type_sword) {
    		attackUp1 = setup("/player/attup1", gp.tileSize, gp.tileSize);
        	attackUp2 = setup("/player/attup2", gp.tileSize, gp.tileSize);
        	attackUp3 = setup("/player/attup3", gp.tileSize, gp.tileSize);
        	
        	attackDown1 = setup("/player/attdown1", gp.tileSize, gp.tileSize);
        	attackDown2 = setup("/player/attdown2", gp.tileSize, gp.tileSize);
        	attackDown3 = setup("/player/attdown3", gp.tileSize, gp.tileSize);
        	
        	attackLeft1 = setup("/player/attleft1", gp.tileSize, gp.tileSize);
        	attackLeft2 = setup("/player/attleft2", gp.tileSize, gp.tileSize);
        	attackLeft3 = setup("/player/attleft3", gp.tileSize, gp.tileSize);
        	
        	attackRight1 = setup("/player/attright1", gp.tileSize, gp.tileSize);
        	attackRight2 = setup("/player/attright2", gp.tileSize, gp.tileSize);
        	attackRight3 = setup("/player/attright3", gp.tileSize, gp.tileSize);
    	}
    	
    	if(currentWeapon.type == type_axe	) {
    		attackUp1 = setup("/player/attup1", gp.tileSize, gp.tileSize);
        	attackUp2 = setup("/player/attup2", gp.tileSize, gp.tileSize);
        	attackUp3 = setup("/player/attup3", gp.tileSize, gp.tileSize);
        	
        	attackDown1 = setup("/player/attdown1", gp.tileSize, gp.tileSize);
        	attackDown2 = setup("/player/attdown2", gp.tileSize, gp.tileSize);
        	attackDown3 = setup("/player/attdown3", gp.tileSize, gp.tileSize);
        	
        	attackLeft1 = setup("/player/attleft1", gp.tileSize, gp.tileSize);
        	attackLeft2 = setup("/player/attleft2", gp.tileSize, gp.tileSize);
        	attackLeft3 = setup("/player/attleft3", gp.tileSize, gp.tileSize);
        	
        	attackRight1 = setup("/player/attright1", gp.tileSize, gp.tileSize);
        	attackRight2 = setup("/player/attright2", gp.tileSize, gp.tileSize);
        	attackRight3 = setup("/player/attright3", gp.tileSize, gp.tileSize);
    	}
    }
    
    public void update() {
        
        // 1. LOGIKA DASHING (PRIORITAS TERTINGGI)
        if(dashing == true) {
            invincible = true; 
            
            // --- SETTING KECEPATAN DASH ---
            // Dash speed sekarang kita buat relatif terhadap default speed
            int dashSpeed = defaultSpeed + 6; 
            
            // Simpan speed asli
            speed = dashSpeed; 
            
            // Dash Movement (Tanpa Collision Monster)
            collisionOn = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkObject(this, true);
            gp.cChecker.checkEntity(this, gp.iTile);
            
            if(collisionOn == false) {
                 switch(direction) {
                    case "up":    worldY -= speed; break;
                    case "down":  worldY += speed; break;
                    case "left":  worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }
            
            // Kembalikan speed asli
            speed = defaultSpeed; 

            // Counter durasi dash
            dashCounter++;
            if(dashCounter > dashDuration) {
                dashing = false;
                dashCounter = 0;
                dashCoolDown = dashCoolDownMax;
                invincible = false;
            }
            return; // PENTING: Stop update disini saat dash
        }

        // Cooldown Dash berkurang setiap frame
        if(dashCoolDown > 0) {
            dashCoolDown--;
        }
        
        if(gp.keyH.rangeKeyPressed == true && rangeAvailableCounter >= 30) {
            if(projectile.haveResource(this)) {
                object.OBJ_Slash newSlash = new object.OBJ_Slash(gp);
                newSlash.set(worldX, worldY, direction, true, this);
                newSlash.substractResource(this);

                // Cari slot kosong di array projectile map aktif
                for(int i = 0; i < gp.projectile[gp.currentMap].length; i++) {
                    if(gp.projectile[gp.currentMap][i] == null) {
                        gp.projectile[gp.currentMap][i] = newSlash;
                        newSlash.projectileIndex = i;
                        break;
                    }
                }
                gp.playSE(8); // SFX Swipe / Magic wave
                rangeAvailableCounter = 0;
            } else {
                gp.ui.addMessage("Mana tidak cukup!");
                rangeAvailableCounter = 15; // Cooldown singkat untuk mencegah spam notifikasi
            }
        }

        // 2. LOGIKA ATTACK
        if(attacking == true) {
            attacking();
            return; // KUNCI GERAKAN: Player berhenti sejenak saat mengayunkan pedang/kapak
        }
        
        // 3. LOGIKA INTERAKSI & AKSI (KEYBOARD: E / ENTER / SPACE)
        if(keyH.actionPressed == true || keyH.enterPressed == true) {
            keyH.actionPressed = false;
            keyH.enterPressed = false;

            boolean interacted = interactNearest();
            if(!interacted && attackCanceled == false) {
                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }
            attackCanceled = false;
        }

        // 4. MOVEMENT NORMAL & INPUT
        if(keyH.upPressed == true || keyH.downPressed == true || 
           keyH.leftPressed == true || keyH.rightPressed == true || keyH.dashKeyPressed == true) {
            
            // --- AKTIVASI DASH (Q) ---
            if(keyH.dashKeyPressed == true && dashCoolDown == 0 && attackCanceled == false) {
                 dashing = true;
                 gp.playSE(7); // Sound dash
                 return;
            }

            // Tentukan pergerakan per-sumbu (X dan Y)
            boolean moveX = false;
            boolean moveY = false;
            String dirX = "";
            String dirY = "";

            if (keyH.upPressed) { moveY = true; dirY = "up"; }
            else if (keyH.downPressed) { moveY = true; dirY = "down"; }

            if (keyH.leftPressed) { moveX = true; dirX = "left"; }
            else if (keyH.rightPressed) { moveX = true; dirX = "right"; }
            
            // Cek apakah player menekan tombol vertikal DAN horizontal bersamaan
            boolean isMovingDiagonal = (keyH.upPressed || keyH.downPressed) && 
                                       (keyH.leftPressed || keyH.rightPressed);
            
            if(isMovingDiagonal) {
                speed = (int)Math.round(defaultSpeed * 0.707); 
            } else {
                speed = defaultSpeed;
            }

            // --- CEK PER-AXIS COLLISION (WALL SLIDING) ---
            boolean canMoveX = false;
            boolean canMoveY = false;
            String origDirection = direction;

            // 1. Cek Sumbu X Independen
            if (moveX) {
                direction = dirX;
                collisionOn = false;
                gp.cChecker.checkTile(this);
                int objIndex = gp.cChecker.checkObject(this, true);
                pickUpObject(objIndex);
                int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
                interactNPC(npcIndex);
                int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
                contactMonster(monsterIndex);
                gp.cChecker.checkEntity(this, gp.iTile);
                gp.eHandler.checkEvent();

                if (!collisionOn) {
                    canMoveX = true;
                }
            }

            // 2. Cek Sumbu Y Independen
            if (moveY) {
                direction = dirY;
                collisionOn = false;
                gp.cChecker.checkTile(this);
                int objIndex = gp.cChecker.checkObject(this, true);
                pickUpObject(objIndex);
                int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
                interactNPC(npcIndex);
                int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
                contactMonster(monsterIndex);
                gp.cChecker.checkEntity(this, gp.iTile);
                gp.eHandler.checkEvent();

                if (!collisionOn) {
                    canMoveY = true;
                }
            }

            // --- GERAKKAN PLAYER DENGAN SMOOTH SLIDING ---
            if (canMoveX) {
                if (dirX.equals("left")) worldX -= speed;
                else if (dirX.equals("right")) worldX += speed;
                direction = dirX;
            }
            if (canMoveY) {
                if (dirY.equals("up")) worldY -= speed;
                else if (dirY.equals("down")) worldY += speed;
                if (!canMoveX || moveY) {
                    direction = dirY;
                }
            }
            if (!canMoveX && !canMoveY) {
                direction = origDirection;
            }
            
            if(life <= 0) {
            	gp.stopMusic();
        		gp.playSE(10);
            	gp.gameState = gp.gameOverState;
            }
            
            // Kembalikan speed ke normal untuk perhitungan frame berikutnya
            speed = defaultSpeed;
            
            // Sprite Animation
            spriteCounter++;
            if (spriteCounter > 12) {
                if(spriteNum == 1) spriteNum = 2;
                else if(spriteNum == 2) spriteNum = 1;
                spriteCounter = 0;
            }
        }
        
        if(invincible == true) {
            invincibleCounter++;
            if(invincibleCounter > 40) {
                 if(dashing == false) { 
                     invincible = false;
                     invincibleCounter = 0;
                  }
            }
        }
        if(rangeAvailableCounter < 30) {
            rangeAvailableCounter++;
        }
    }
    
    public void pickUpObject(int i) {
        if (i != 999) {
            // Pickup Only Items (koin, mana potion, dsb)
            if(gp.obj[gp.currentMap][i].type == type_pickupOnly) {
                gp.obj[gp.currentMap][i].use(this);
                gp.obj[gp.currentMap][i] = null;
            }
            // Non-obstacle item di tanah bisa langsung diambil saat diinjak
            else if(gp.obj[gp.currentMap][i].type != type_obstacle) {
                pickUpSpecificItem(gp.obj[gp.currentMap][i]);
            }
        }
    }

    public void pickUpSpecificItem(Entity item) {
        if (item == null) return;
        if (inventory.size() < maxInventorySize) {
            inventory.add(item);
            gp.playSE(1);
            String text = "Mendapat " + item.name + "!";
            if(item instanceof object.OBJ_Relic) {
                gp.qManager.completeCurrentAndAdvance(quest.QuestType.RETURN_TO_GUIDE);
            } else if(gp.qManager.getCurrentQuest() == quest.QuestType.GET_TOOLS) {
                if(hasItem("Kapak") && hasItem("Lentera")) {
                    gp.qManager.completeCurrentAndAdvance(quest.QuestType.CLEAR_PATH);
                }
            }
            gp.ui.addMessage(text);
            removeObj(item);
        } else {
            gp.ui.addMessage("Inventory penuh!!!");
        }
    }

    private void removeObj(Entity target) {
        if (gp.obj == null || gp.currentMap >= gp.obj.length || gp.obj[gp.currentMap] == null) return;
        for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
            if (gp.obj[gp.currentMap][i] == target) {
                gp.obj[gp.currentMap][i] = null;
                break;
            }
        }
    }

    private boolean isEntityInArray(Entity target, Entity[] arr) {
        if (arr == null) return false;
        for (Entity e : arr) {
            if (e == target) return true;
        }
        return false;
    }
    
    public boolean hasItem(String itemName) {
        if(currentWeapon != null && currentWeapon.name.equalsIgnoreCase(itemName)) return true;
        if(currentShield != null && currentShield.name.equalsIgnoreCase(itemName)) return true;
        if(currentLight != null && currentLight.name.equalsIgnoreCase(itemName)) return true;
        for(Entity item : inventory) {
            if(item != null && item.name.equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    public boolean removeItem(String itemName) {
        if (itemName == null) return false;
        for (int i = 0; i < inventory.size(); i++) {
            Entity item = inventory.get(i);
            if (item != null && item.name != null && item.name.equalsIgnoreCase(itemName)) {
                inventory.remove(i);
                return true;
            }
        }
        return false;
    }

    public void faceTowards(int targetCenterX, int targetCenterY) {
        int dx = targetCenterX - getCenterX();
        int dy = targetCenterY - getCenterY();
        if (Math.abs(dx) > Math.abs(dy)) {
            direction = (dx >= 0) ? "right" : "left";
        } else {
            direction = (dy >= 0) ? "down" : "up";
        }
    }

    private String getOppositeDirection(String dir) {
        switch (dir) {
            case "up": return "down";
            case "down": return "up";
            case "left": return "right";
            case "right": return "left";
            default: return "down";
        }
    }

    private boolean isFacing(int dx, int dy) {
        switch (direction) {
            case "up":    return dy <= 24 && Math.abs(dx) <= Math.abs(dy) + 32;
            case "down":  return dy >= -24 && Math.abs(dx) <= Math.abs(dy) + 32;
            case "left":  return dx <= 24 && Math.abs(dy) <= Math.abs(dx) + 32;
            case "right": return dx >= -24 && Math.abs(dy) <= Math.abs(dx) + 32;
            default:      return true;
        }
    }

    public Entity getNearbyInteractable() {
        int playerCenterX = getCenterX();
        int playerCenterY = getCenterY();
        int maxDist = (int)(gp.tileSize * 2.2); // ~105 px

        Entity bestTarget = null;
        double minDistance = Double.MAX_VALUE;

        // 1. Cek NPCs
        if (gp.npc != null && gp.currentMap >= 0 && gp.currentMap < gp.npc.length && gp.npc[gp.currentMap] != null) {
            for (int i = 0; i < gp.npc[gp.currentMap].length; i++) {
                Entity n = gp.npc[gp.currentMap][i];
                if (n != null && n.alive) {
                    int dx = n.getCenterX() - playerCenterX;
                    int dy = n.getCenterY() - playerCenterY;
                    double dist = Math.hypot(dx, dy);

                    if (dist <= maxDist) {
                        boolean facing = isFacing(dx, dy);
                        if (dist <= gp.tileSize * 1.5 || facing) {
                            double score = facing ? (dist * 0.7) : dist;
                            if (score < minDistance) {
                                minDistance = score;
                                bestTarget = n;
                            }
                        }
                    }
                }
            }
        }

        // 2. Cek Objects (Chest, Door, Item di tanah)
        if (gp.obj != null && gp.currentMap >= 0 && gp.currentMap < gp.obj.length && gp.obj[gp.currentMap] != null) {
            for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
                Entity o = gp.obj[gp.currentMap][i];
                if (o != null) {
                    int dx = o.getCenterX() - playerCenterX;
                    int dy = o.getCenterY() - playerCenterY;
                    double dist = Math.hypot(dx, dy);

                    if (dist <= maxDist) {
                        boolean facing = isFacing(dx, dy);
                        if (dist <= gp.tileSize * 1.5 || facing) {
                            double score = facing ? (dist * 0.7) : dist;
                            if (score < minDistance) {
                                minDistance = score;
                                bestTarget = o;
                            }
                        }
                    }
                }
            }
        }

        // 3. Cek Interactive Tiles (Pohon kering)
        if (bestTarget == null && gp.iTile != null && gp.currentMap >= 0 && gp.currentMap < gp.iTile.length && gp.iTile[gp.currentMap] != null) {
            for (int i = 0; i < gp.iTile[gp.currentMap].length; i++) {
                InteractiveTile it = gp.iTile[gp.currentMap][i];
                if (it != null && it.destructible) {
                    int dx = it.getCenterX() - playerCenterX;
                    int dy = it.getCenterY() - playerCenterY;
                    double dist = Math.hypot(dx, dy);

                    if (dist <= maxDist && (dist <= gp.tileSize * 1.5 || isFacing(dx, dy))) {
                        if (dist < minDistance) {
                            minDistance = dist;
                            bestTarget = it;
                        }
                    }
                }
            }
        }

        return bestTarget;
    }

    public boolean interactWith(Entity target) {
        if (target == null) return false;

        attackCanceled = true;
        faceTowards(target.getCenterX(), target.getCenterY());

        // A. Jika NPC
        if (target instanceof NPC_Guide || target instanceof NPC_Merchant || (gp.npc != null && isEntityInArray(target, gp.npc[gp.currentMap]))) {
            target.direction = getOppositeDirection(this.direction);
            gp.ui.npc = target;
            target.speak();
            return true;
        }

        // B. Jika Interactive Tile (Pohon kering)
        if (target instanceof InteractiveTile) {
            InteractiveTile it = (InteractiveTile) target;
            if (it.isCorrectItem(this)) {
                attacking = true;
                spriteCounter = 0;
                attackCanceled = false;
                gp.playSE(1); // Suara ayunan tebasan kapak
                for (int i = 0; i < gp.iTile[gp.currentMap].length; i++) {
                    if (gp.iTile[gp.currentMap][i] == it) {
                        gp.iTile[gp.currentMap][i] = null;
                        break;
                    }
                }
                generateParticle(it, it);
                return true;
            } else {
                gp.gameState = gp.dialogueState;
                gp.ui.npc = null;
                gp.ui.currentSpeakerName = "Rintangan";
                gp.ui.currentDialogue = "Semak duri ini terlalu lebat.\nKamu memerlukan Kapak untuk menebasnya!";
                return true;
            }
        }

        // C. Jika Object
        if (target.type == type_pickupOnly) {
            target.use(this);
            removeObj(target);
            return true;
        } else if (target.type == type_obstacle) {
            gp.ui.npc = null;
            target.interact();
            return true;
        } else {
            pickUpSpecificItem(target);
            return true;
        }
    }

    public boolean interactNearest() {
        Entity target = getNearbyInteractable();
        if (target != null) {
            return interactWith(target);
        }
        return false;
    }

    public boolean interactAtLocation(int worldTargetX, int worldTargetY) {
        int playerCenterX = getCenterX();
        int playerCenterY = getCenterY();
        int maxClickDist = (int)(gp.tileSize * 1.5); // ~72 px (radius 1.5 tile)

        // 1. Cek apakah klik mengenai NPC
        if (gp.npc != null && gp.currentMap >= 0 && gp.currentMap < gp.npc.length && gp.npc[gp.currentMap] != null) {
            for (int i = 0; i < gp.npc[gp.currentMap].length; i++) {
                Entity n = gp.npc[gp.currentMap][i];
                if (n != null && n.alive) {
                    if (worldTargetX >= n.worldX && worldTargetX <= n.worldX + gp.tileSize &&
                        worldTargetY >= n.worldY && worldTargetY <= n.worldY + gp.tileSize) {
                        double dist = Math.hypot(n.getCenterX() - playerCenterX, n.getCenterY() - playerCenterY);
                        if (dist <= maxClickDist) {
                            return interactWith(n);
                        } else {
                            faceTowards(n.getCenterX(), n.getCenterY());
                            gp.ui.addMessage("Mendekatlah untuk berbicara!");
                            return true;
                        }
                    }
                }
            }
        }

        // 2. Cek apakah klik mengenai Object
        if (gp.obj != null && gp.currentMap >= 0 && gp.currentMap < gp.obj.length && gp.obj[gp.currentMap] != null) {
            for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
                Entity o = gp.obj[gp.currentMap][i];
                if (o != null) {
                    if (worldTargetX >= o.worldX && worldTargetX <= o.worldX + gp.tileSize &&
                        worldTargetY >= o.worldY && worldTargetY <= o.worldY + gp.tileSize) {
                        double dist = Math.hypot(o.getCenterX() - playerCenterX, o.getCenterY() - playerCenterY);
                        if (dist <= maxClickDist) {
                            return interactWith(o);
                        } else {
                            faceTowards(o.getCenterX(), o.getCenterY());
                            gp.ui.addMessage("Mendekatlah untuk berinteraksi!");
                            return true;
                        }
                    }
                }
            }
        }

        // 3. Cek apakah klik mengenai Interactive Tile
        if (gp.iTile != null && gp.currentMap >= 0 && gp.currentMap < gp.iTile.length && gp.iTile[gp.currentMap] != null) {
            for (int i = 0; i < gp.iTile[gp.currentMap].length; i++) {
                InteractiveTile it = gp.iTile[gp.currentMap][i];
                if (it != null && it.destructible) {
                    if (worldTargetX >= it.worldX && worldTargetX <= it.worldX + gp.tileSize &&
                        worldTargetY >= it.worldY && worldTargetY <= it.worldY + gp.tileSize) {
                        double dist = Math.hypot(it.getCenterX() - playerCenterX, it.getCenterY() - playerCenterY);
                        if (dist <= maxClickDist) {
                            return interactWith(it);
                        } else {
                            faceTowards(it.getCenterX(), it.getCenterY());
                            gp.ui.addMessage("Mendekatlah untuk menebas!");
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public void attackTowards(int worldTargetX, int worldTargetY) {
        faceTowards(worldTargetX, worldTargetY);
        if (!attacking) {
            attacking = true;
            spriteCounter = 0;
            gp.playSE(7);
        }
    }
    
    public void interactNPC(int i) {
        if (i != 999) {
            if (gp.keyH.actionPressed == true || gp.keyH.enterPressed == true) {
                gp.keyH.actionPressed = false;
                gp.keyH.enterPressed = false;
                interactWith(gp.npc[gp.currentMap][i]);
            }
        }
    }
    
    public void contactMonster(int i) {
    	
    	if(i != 999) {
    		
    		if(invincible == false && gp.monster[gp.currentMap][i].dying == false) { //FIXED
    			gp.playSE(6);
    			
    			int damage = gp.monster[gp.currentMap][i].attack - defense; //FIXED
    			if(damage < 0) {
    				damage = 0;
    			}
    			life -= damage;
    			invincible = true;
    		}
    	}
    }
    
    public void damageMonster(int i, Entity attacker, int attack, int knockBackPower) {
    	
    	if(i != 999) {
    		
    		if(gp.monster[gp.currentMap][i].invincible == false && gp.monster[gp.currentMap][i].dying == false) { //FIXED
    			
    			gp.playSE(5);
    			if(knockBackPower > 0) {
    	   			knockBack(gp.monster[gp.currentMap][i], attacker, knockBackPower);
    			}
    			
    			int damage = attack - gp.monster[gp.currentMap][i].defense; //FIXED
    			if(damage < 0) {
    				damage = 0;
    			}
    			gp.monster[gp.currentMap][i].life -= damage; //FIXED
    			gp.ui.addMessage(damage + " damage!");
    			gp.monster[gp.currentMap][i].invincible = true; //FIXED
    			gp.monster[gp.currentMap][i].damageReaction(); //FIXED
    			
    			if(gp.monster[gp.currentMap][i].life <= 0) { //FIXED
    				gp.monster[gp.currentMap][i].dying = true; //FIXED
    				gp.ui.addMessage("Killed the " + gp.monster[gp.currentMap][i].name + "!"); //FIXED
    				gp.ui.addMessage("Exp + " + gp.monster[gp.currentMap][i].exp); //FIXED
    				exp += gp.monster[gp.currentMap][i].exp; //FIXED
    				checkLevelUp();
    			}
    		}
    	}
    }
    
    public void knockBack(Entity entity, int knockBackPower) {
    	
    	entity.direction = direction;
    	entity.speed += 10;
    	entity.knockBack = true;
    }
    
    public void damageInteractiveTile(int i) { //FIXED
        if (i != 999 && gp.iTile[gp.currentMap][i].destructible == true 
        	&& gp.iTile[gp.currentMap][i].isCorrectItem(this) == true) {
            
            Entity destroyedTile = gp.iTile[gp.currentMap][i];
            
            gp.iTile[gp.currentMap][i] = null;
            
            generateParticle(destroyedTile, destroyedTile);
        }
    }
    
    public void damageProjectile(int i) {
    	
    	if(i != 999) {
    		Entity Projectile = gp.projectile[gp.currentMap][i];
    		projectile.alive = false;
    		generateParticle(projectile, projectile);
    	}
    }

    public void checkLevelUp() {
    	
        if(exp >= nextLevelExp) {
        	
            level++; 
            nextLevelExp = nextLevelExp*2; 
            maxLife += 2; 
            life += 2;
            strength++; 
            dexterity++; 
            attack = getAttack(); 
            defense = getDefense();
            
            gp.playSE(8);
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You are level " + level + " now!\n	" 
            + "You feel stronger!";
        }
    }
    
    public void selectItem() {
    	int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);
    	
    	if(itemIndex < inventory.size()) {
    		
    		Entity selectedItem = inventory.get(itemIndex);
    		
    		if(selectedItem.type == type_sword || selectedItem.type == type_axe) {
    			
    			currentWeapon = selectedItem;
    			attack = getAttack();
    			getPlayerAttackImage();
    		}
    		if(selectedItem.type == type_shield) {
    			
    			currentShield = selectedItem;
    			defense = getDefense();
    		}
    		if(selectedItem.type == type_light) {
    			
    			if(currentLight == selectedItem) {
    				currentLight = null;
    			}
    			else {
    				currentLight = selectedItem;
    			}
    			lightUpdated = true;
    		}
    		if(selectedItem.type == type_consumable) {
    			
    			selectedItem.use(this);
    			inventory.remove(itemIndex);
    		}
    	}
    }
    public void draw(Graphics2D g2) {
        
    	BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;
        
        switch(direction) {
            case "up":
            	if(attacking == false) {
            		if(spriteNum == 1) {image = up1;}
            		if(spriteNum == 2) {image = up2;}
            	}
            	if(attacking == true) {
            		if(spriteNum == 1) {image = attackUp1;}
            		if(spriteNum == 2) {image = attackUp2;}
            		if(spriteNum == 3) {image = attackUp3;}
            	}
                break;
            case "down":
            	if(attacking == false) {
            		if(spriteNum == 1) {image = down1;}
            		if(spriteNum == 2) {image = down2;}
            	}
            	if(attacking == true) {
            		if(spriteNum == 1) {image = attackDown1;}
            		if(spriteNum == 2) {image = attackDown2;}
            		if(spriteNum == 3) {image = attackDown3;}
            	}
                break;
            case "left":
            	if(attacking == false) {
            		if(spriteNum == 1) {image = left1;}
            		if(spriteNum == 2) {image = left2;}
            	}
            	if(attacking == true) {
            		if(spriteNum == 1) {image = attackLeft1;}
            		if(spriteNum == 2) {image = attackLeft2;}
            		if(spriteNum == 3) {image = attackLeft3;}
            	}
                break;
            case "right":
            	if(attacking == false) {
            		if(spriteNum == 1) {image = right1;}
            		if(spriteNum == 2) {image = right2;}
            	}
            	if(attacking == true) {
            		if(spriteNum == 1) {image = attackRight1;}
            		if(spriteNum == 2) {image = attackRight2;}
            		if(spriteNum == 3) {image = attackRight3;}
            	}
                break;
        }
        
        if(dashing == true) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); 
            // Player jadi setengah transparan saat dash
        }
        else if(invincible == true) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        g2.drawImage(image, screenX, screenY, null);

        // Reset composite
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}