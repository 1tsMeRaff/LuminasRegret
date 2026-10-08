package com.luminasregret.game.entity.contracts;

import java.awt.Rectangle;

/**
 * Kontrak standar untuk elemen game yang memiliki batas fisik dan area tabrakan (AABB).
 */
public interface Collidable {
    Rectangle getSolidArea();
    int getWorldX();
    int getWorldY();
    boolean isSolid();
}
