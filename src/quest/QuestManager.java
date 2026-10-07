package quest;

import main.GamePanel;

public class QuestManager {

    private final GamePanel gp;
    private QuestType currentQuest = QuestType.TALK_TO_GUIDE;

    // Banner notification state
    private String bannerHeader = "";
    private String bannerText = "";
    private int bannerCounter = 0;
    private final int BANNER_MAX_TIME = 180; // 3 detik pada 60 FPS
    private boolean bannerActive = false;

    public QuestManager(GamePanel gp) {
        this.gp = gp;
    }

    public QuestType getCurrentQuest() {
        return currentQuest;
    }

    public void setQuest(QuestType quest) {
        this.currentQuest = quest;
    }

    public void advanceQuest(QuestType nextQuest) {
        if (currentQuest != nextQuest) {
            this.currentQuest = nextQuest;
            showBanner("MISI DIPERBARUI", nextQuest.getTitle());
            gp.playSE(1); // Suara notifikasi / power up
            gp.ui.addMessage("Misi: " + nextQuest.getTitle());
        }
    }

    public void completeCurrentAndAdvance(QuestType nextQuest) {
        if (currentQuest != nextQuest) {
            this.currentQuest = nextQuest;
            showBanner("MISI SELESAI!", "Misi Baru: " + nextQuest.getTitle());
            gp.playSE(2); // Suara reward/heal/fanfare
            gp.ui.addMessage("Misi Baru: " + nextQuest.getTitle());
        }
    }

    public void showBanner(String header, String text) {
        this.bannerHeader = header;
        this.bannerText = text;
        this.bannerCounter = BANNER_MAX_TIME;
        this.bannerActive = true;
    }

    public void update() {
        if (bannerActive) {
            bannerCounter--;
            if (bannerCounter <= 0) {
                bannerActive = false;
            }
        }
    }

    public boolean isBannerActive() {
        return bannerActive;
    }

    public String getBannerHeader() {
        return bannerHeader;
    }

    public String getBannerText() {
        return bannerText;
    }

    public float getBannerAlpha() {
        if (!bannerActive) return 0f;
        // Fade in pada awal 20 frame, fade out pada akhir 20 frame
        if (bannerCounter > BANNER_MAX_TIME - 20) {
            return (float) (BANNER_MAX_TIME - bannerCounter) / 20f;
        } else if (bannerCounter < 20) {
            return (float) bannerCounter / 20f;
        }
        return 1f;
    }

    public boolean isReached(QuestType quest) {
        return currentQuest.ordinal() >= quest.ordinal();
    }
}
