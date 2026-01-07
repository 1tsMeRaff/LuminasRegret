package tile_interactive;

//Import untuk warna partikel saat pohon ditebang
import java.awt.Color;

//Import Entity untuk akses weapon player
import entity.Entity;

//Import GamePanel untuk akses tileSize dan sistem game
import main.GamePanel;

//pohon yang bisa di tebang oleh kapak
public class IT_DryTree extends InteractiveTile{

	public IT_DryTree(GamePanel gp, int col ,int row) {
		
		// Memanggil constructor InteractiveTile
		super(gp,col,row);
		
		// Simpan referensi GamePanel
		this.gp = gp;
		
		this.life = 3; // Misal: butuh 3 kali pukul untuk tumbang
	    this.invincible = false;
		
	    // Mengatur posisi pohon di dunia berdasarkan tile
		this.worldX = gp.tileSize * col;
		this.worldY = gp.tileSize * row;
		
		down1 = setup("/tiles_interactive/drytree",gp.tileSize,gp.tileSize);
		destructible = true;
		
		// Mengatur area tabrakan (collision area)
		solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = gp.tileSize; 
        solidArea.height = gp.tileSize;
        
        // Menyimpan posisi awal solidArea
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
		
		collisionOn = true;
		
	}
    public boolean isCorrectItem(Entity entity) {
    	boolean isCorrectItem = false;
    	
    	// Pohon hanya bisa ditebang jika player memakai kapak
    	if(entity.currentWeapon.type == type_axe) {
    		isCorrectItem = true;
    	}
    	
    	return isCorrectItem;
    }
    
    //Mengembalikan warna partikel saat pohon ditebang
    public Color getParticleColor() {
    	
    	// Warna coklat kayu
    	Color color = new Color(65,50,30);
    	return color;
    }
    
    //Mengatur ukuran partikel
    public int getParticleSize() {
    	int size = 6; //6 pixels
    	return size;
    }
    
    //Mengatur kecepatan partikel
    public int getParticleSpeed() {
    	int speed = 1;
    	return speed;
    }
    
    public int getParticleMaxLife() {
    	int maxLife = 20;
    	return maxLife;
    }
}
