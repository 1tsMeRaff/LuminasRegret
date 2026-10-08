package com.luminasregret.game.object;

import com.luminasregret.engine.core.GamePanel;
import com.luminasregret.game.entity.Entity;
import com.luminasregret.game.entity.Player;
import com.luminasregret.game.entity.contracts.Interactable;

/**
 * Kelas dasar representasi objek pasif, prop, dan item dunia (Key, Chest, Door, Relic, Potion).
 * Mengimplementasikan kontrak Interactable tanpa keterikatan pada logika pertempuran dan AI monster.
 */
public class WorldObject extends Entity implements Interactable {

    public WorldObject(GamePanel gp) {
        super(gp);
    }

    @Override
    public void interact(Player player) {
        interact();
    }

    @Override
    public String getInteractionPrompt() {
        return name != null ? name : "Objek";
    }
}
