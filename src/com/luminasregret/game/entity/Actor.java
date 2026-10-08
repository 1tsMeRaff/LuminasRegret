package com.luminasregret.game.entity;

import com.luminasregret.engine.core.GamePanel;
import com.luminasregret.game.entity.contracts.Damageable;

/**
 * Kelas dasar representasi aktor hidup (Player, Monster, NPC) dalam dunia Lumina's Regret.
 * Menegakkan Single Responsibility Principle (SRP) dengan merangkum status tempur,
 * respon kerusakan, dan animasi karakter aktif.
 */
public class Actor extends Entity implements Damageable {

    public Actor(GamePanel gp) {
        super(gp);
    }

    @Override
    public void takeDamage(int damage, Entity attacker) {
        this.attacker = attacker;
        this.life = Math.max(0, this.life - damage);
        damageReaction();
    }

    @Override
    public int getLife() {
        return life;
    }

    @Override
    public int getMaxLife() {
        return maxLife;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }
}
