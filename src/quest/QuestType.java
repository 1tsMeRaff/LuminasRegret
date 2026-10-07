package quest;

public enum QuestType {
    TALK_TO_GUIDE(
        "Temui Guide di Desa",
        "Bicaralah dengan Guide di dekat desa untuk petunjuk awal."
    ),
    GET_TOOLS(
        "Dapatkan Kapak & Lentera",
        "Beli Kapak dari Merchant (atau cari di peti) dan ambil Lentera."
    ),
    CLEAR_PATH(
        "Tebang Rintangan & Masuk Portal",
        "Tebang deretan pohon rintangan dan masuki portal menuju Dungeon."
    ),
    EXPLORE_DUNGEON(
        "Jelajahi Dungeon & Cari Kunci",
        "Kumpulkan kunci dari sayap dungeon untuk membuka gerbang Boss."
    ),
    DEFEAT_BOSS(
        "Kalahkan Goblin King!",
        "Masuki Boss Chamber dan kalahkan Goblin King di ruang terdalam."
    ),
    RETURN_TO_GUIDE(
        "Bawa Relik Kembali ke Guide",
        "Bawa Lumina's Relic kembali ke Guide di desa untuk mematahkan kutukan."
    ),
    GAME_COMPLETED(
        "Kutukan Telah Terangkat",
        "Kamu telah berhasil menuntaskan kisah Lumina's Regret!"
    );

    private final String title;
    private final String description;

    QuestType(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
