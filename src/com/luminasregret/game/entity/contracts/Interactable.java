package com.luminasregret.game.entity.contracts;

import com.luminasregret.game.entity.Player;

/**
 * Kontrak standar untuk elemen game atau NPC yang dapat merespons aksi interaksi pemain (Tombol E / Enter / Klik).
 */
public interface Interactable {
    void interact(Player player);
    String getInteractionPrompt();
}
