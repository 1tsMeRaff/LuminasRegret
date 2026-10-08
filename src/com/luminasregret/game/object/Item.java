package com.luminasregret.game.object;

import com.luminasregret.engine.core.GamePanel;
import com.luminasregret.game.entity.Entity;
import com.luminasregret.game.entity.Player;

import java.awt.image.BufferedImage;
import com.luminasregret.game.entity.Entity;

/**
 * Contract defining an Item in the game world and inventory.
 * Adheres to Effective Java: Item 20 (Prefer interfaces to abstract classes).
 */
public interface Item {
    String getName();
    String getDescription();
    BufferedImage getItemImage();
    int getItemType();
    int getPrice();
    int getValue();
    int getAttackValue();
    int getDefenseValue();
    int getUseCost();
    int getKnockBackPower();
    int getLightRadius();
    void use(Entity user);
}
