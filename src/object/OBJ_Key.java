package object;

//Import Entity sebagai parent class item
import entity.Entity;
import main.GamePanel;

//Import GamePanel untuk akses game state, UI, dan object
public class OBJ_Key extends Entity {
	
	// Referensi ke GamePanel
	GamePanel gp;
	
	public OBJ_Key(GamePanel gp) {
		
		// Memanggil constructor dari Entity
		super(gp);
		
		// Menyimpan referensi GamePanel
		this.gp = gp;
		
		// Menentukan tipe item sebagai item consumable
		type = type_consumable;
		name = "kunci";
		type = type_consumable;
		down1 = setup("/objects/key", gp.tileSize, gp.tileSize);
		
		// Deskripsi item (ditampilkan di inventory)
		description = "[" + name + "]\n Its a kunci.";
		
	}
	//Method use() dipanggil saat player menggunakan kunci
	public void use(Entity entity) {
		
		// Mengubah game state ke dialog
		gp.gameState = gp.dialogueState;
		
		// Mencari object bernama "Door" di sekitar player
		int objIndex = getDetected(entity, gp.obj, "Door");
		
		// Jika pintu ditemukan
		if(objIndex != 999) {
			gp.ui.currentDialogue = "Menggunakan " + name + " dan membuka pintu";
			gp.playSE(1);
			gp.obj[gp.currentMap][objIndex] = null;
			gp.obj[gp.currentMap][objIndex + 1] = null;
		}
		else {
			// Jika tidak ada pintu di sekitar player
			gp.ui.currentDialogue = "apa yang kamu lakukan";
		}
	}
}
