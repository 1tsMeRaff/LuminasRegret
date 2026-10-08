package com.luminasregret.game.entity.contracts;

import java.awt.Graphics2D;

/**
 * Kontrak standar untuk elemen visual yang digambar ke buffer layar dengan dukungan frustum culling.
 */
public interface Renderable {
    void draw(Graphics2D g2);
    boolean inCamera();
    int getWorldY();
}
