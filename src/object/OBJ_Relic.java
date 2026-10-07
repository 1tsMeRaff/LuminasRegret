package object;

import entity.Entity;
import main.GamePanel;
import quest.QuestType;

public class OBJ_Relic extends Entity {

    private final GamePanel gp;

    public OBJ_Relic(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Relik Lumina";
        type = type_consumable;
        down1 = setup("/objects/mana_collect", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nKristal air mata suci Lumina.\nBawa kembali ke Guide di desa.";
    }

    @Override
    public void use(Entity entity) {
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "Relik ini memancarkan cahaya hangat.\nBicaralah dengan Guide di desa untuk mematahkan kutukan.";
    }
}
