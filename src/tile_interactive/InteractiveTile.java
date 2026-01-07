package tile_interactive;

//Import untuk menggambar objek ke layar
import java.awt.Graphics2D;

//Import Entity sebagai parent class
import entity.Entity;

//Import GamePanel untuk akses player, tileSize, dll
import main.GamePanel;

//InteractiveTile adalah kelas induk untuk semua tile 
//yang bisa berinteraksi dengan player pohon.
public class InteractiveTile extends Entity {
	
	// Referensi ke GamePanel
	GamePanel gp;
	
	// Menandakan apakah tile bisa dihancurkan (misalnya pohon)
	public boolean destructible = false;

    public InteractiveTile(GamePanel gp, int col, int row) {
    	// Memanggil constructor dari Entity
        super(gp);
        
        // Simpan referensi GamePanel
        this.gp = gp;
    }
    
    //Mengecek apakah item yang digunakan player
    //adalah item yang benar untuk tile ini contoh kapak untuk pohon
    public boolean isCorrectItem(Entity entity) {
    	
    	// Default: tidak ada item yang cocok
    	boolean isCorrectItem = false;
    	
    	// Method ini akan dioverride oleh subclass
    	return isCorrectItem;
    }
    
    //Update logika InteractiveTile setiap frame
    public void update () {
    	
    	// Jika tile sedang dalam kondisi kebal (invincible)
    	if(invincible == true) {
    		
    		// Hitung waktu kebal
            invincibleCounter++;
            
            // Setelah 20 frame, kebal dinonaktifkan
            if(invincibleCounter > 20) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    	
    }
    
    //Menggambar InteractiveTile ke layar
    public void draw(Graphics2D g2) {
    	
    	// Hitung posisi tile relatif terhadap layar
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		// Cek apakah tile masih berada di dalam area layar
		if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
		   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
		   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
		   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
			
			// Gambar sprite tile
			g2.drawImage(down1, screenX, screenY, null);
	
		}
    }
}
