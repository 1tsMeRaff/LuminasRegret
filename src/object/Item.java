package object;

import java.awt.image.BufferedImage;
import entity.Entity;

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
