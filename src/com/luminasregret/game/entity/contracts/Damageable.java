package com.luminasregret.game.entity.contracts;

import com.luminasregret.game.entity.Entity;

/**
 * Kontrak standar untuk karakter atau entitas yang dapat menerima kerusakan dalam sistem pertarungan.
 */
public interface Damageable {
    void takeDamage(int damage, Entity attacker);
    int getLife();
    int getMaxLife();
    boolean isAlive();
}
