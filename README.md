# Lumina's Regret

<div align="center">

![Java](https://img.shields.io/badge/Java-SE_21_LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Build](https://img.shields.io/badge/Build-0_Warnings_0_Errors-brightgreen?style=for-the-badge)
![Dependencies](https://img.shields.io/badge/Dependencies-Zero_External-blue?style=for-the-badge)
![Frame Rate](https://img.shields.io/badge/Framerate-60.0_FPS_Fixed-orange?style=for-the-badge)
![Style](https://img.shields.io/badge/Aesthetics-16--bit_Retro_Pixel_Art-purple?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

*Sebuah game 2D Action-RPG bernuansa retro 16-bit klasik yang dibangun secara murni (*pure*) menggunakan Java SE 21 tanpa framework atau library pihak ketiga.*

</div>

---

## 📖 Ringkasan & Cerita

**Lumina's Regret** terinspirasi dari mahakarya Action-Adventure 16-bit legendaris seperti *The Legend of Zelda: A Link to the Past* dan *Secret of Mana*.

Kabut kutukan kelam telah menyelimuti lembah suci sejak kejatuhan roh Lumina. Di kedalaman labirin bawah tanah (*Dark Dungeon*), Goblin King telah merampas **Relik Air Mata Lumina**. Sebagai musafir yang terpilih, Anda harus mengumpulkan perlengkapan, menembus rintangan alam liar, menjelajahi dungeon gelap, dan mengalahkan Goblin King dalam pertarungan multi-fase epik untuk mematahkan kutukan abadi tersebut.

---

## 🎮 Fitur Utama

- **100% Pure Java SE 21**: Dibangun sepenuhnya menggunakan pustaka standar JDK (`java.desktop` / `java.base`). Zero external dependencies (tanpa Maven, Gradle, LWJGL, atau library eksternal).
- **Deterministic 60.0 FPS Game Loop**: Game loop fixed-timestep presisi tinggi dengan AABB collision math yang bebas alokasi GC di hot-path update & render.
- **Sistem Pergerakan Mulus (*Per-Axis Wall Sliding*)**: Mekanika tabrakan AABB independen per-sumbu mencegah karakter tersangkut saat menyerempet dinding atau bergerak diagonal.
- **Sistem Input Terpadu (*Keyboard & Mouse*)**:
  - Dukungan keyboard universal: Tombol **`E`**, **`ENTER`**, dan **`SPACE`** dapat digunakan bergantian untuk interaksi, bicara, konfirmasi belanja, equip barang, dan navigasi menu.
  - Dukungan mouse penuh: Klik kiri untuk ayunan senjata terarah atau klik objek interaktif; hover dan klik pada inventori, toko merchant Boran, dan pengaturan sistem; klik kanan untuk manuver *dash escape*.
- **State-Machine Quest Engine**: Alur cerita terstruktur dari memandu quest Sylvia, mencari Kapak & Lentera, menebas rintangan pohon, menjelajahi dungeon, hingga mengembalikan Relik Lumina.
- **Pertarungan Multi-Fase Bos**: Goblin King dengan AI agresif, serangan proyektil terkutuk, dan pemanggilan minion Slime & Zombie saat memasuki mode *Enraged* (Fase 2).
- **Arsitektur Musik Dinamis (*SNES Orchestral Fantasy*)**:
  - Musik terkurasi berlisensi **CC0 Public Domain** dalam format uncompressed 16-bit PCM Stereo WAV.
  - BGM berganti otomatis secara dinamis sesuai area (*Title Theme*, *Overworld Folk Adventure*, *Dark Dungeon Ambience*, dan *Epic Boss Battle*).
- **HUD Kompak & Grafis Piksel Proporsional**: Indikator HP Heart dan Mana Bar 24×24 piksel dengan pesan notifikasi toast bergaya retro.

---

## 🕹️ Skema Kontrol

| Kontrol | Tombol Keyboard | Mouse |
| :--- | :--- | :--- |
| **Bergerak** | `W`, `A`, `S`, `D` atau Tombol Panah | — |
| **Interaksi / Konfirmasi / Beli / Jual** | `E` / `ENTER` / `SPACE` | Klik Kiri pada NPC / Peti / Pintu / Menu UI |
| **Serangan Fisik (Senjata)** | `J` | Klik Kiri ke arah kursor |
| **Sihir Proyektil (Magic Slash)** | `K` (membutuhkan 1 Mana) | — |
| **Menangkis (Guard)** | `L` (memerlukan Perisai) | — |
| **Manuver Menghindar (Dash Escape)**| — | Klik Kanan ke arah kursor |
| **Buka Tas / Karakter (Inventory)** | `C` | — |
| **Buka Menu Opsi (Settings)** | `ESCAPE` | — |
| **Jeda (Pause)** | `P` | — |

---

## 🛠️ Persyaratan Sistem

- **Java Development Kit (JDK)**: Versi 21 LTS atau lebih baru.
- **Sistem Operasi**: Windows, macOS, atau Linux.
- **Resolusi Game**: $768 \times 432$ px (16×9 grid, tile 16×16 px diskalakan 3× menjadi 48×48 px). Mendukung Fullscreen dinamis.

---

## 🚀 Panduan Menjalankan Game

### 1. Kloning Repositori
```bash
git clone https://github.com/1tsMeRaff/LuminasRegret.git
cd LuminasRegret
```

### 2. Kompilasi (Clean Build)
Proyek ini mengedepankan standar nol peringatan (*Zero Warnings*) di bawah `javac -Xlint:all`:

**Di Windows (PowerShell):**
```powershell
if (Test-Path bin) { Remove-Item -Recurse -Force bin\* } else { New-Item -ItemType Directory -Path bin }
javac -Xlint:all -encoding UTF-8 -cp "res" -d bin (Get-ChildItem -Path src -Filter *.java -Recurse | ForEach-Object { $_.FullName })
```

**Di Linux / macOS (Bash):**
```bash
rm -rf bin && mkdir bin
javac -Xlint:all -encoding UTF-8 -cp "res" -d bin $(find src -name "*.java")
```

### 3. Jalankan Permainan
```bash
java -cp "bin:res" com.luminasregret.engine.core.Main        # Linux / macOS
java -cp "bin;res" com.luminasregret.engine.core.Main        # Windows (PowerShell / CMD)
```

### 4. Menjalankan Automated Sanity Tests (Headless)
Game dilengkapi dengan rangkaian automated unit & sanity test headless:
```powershell
powershell -ExecutionPolicy Bypass -File scripts/run-tests.ps1
```
Atau panduan lengkap pengujian dapat dilihat di [Testing.md](docs/Testing.md).

---

## 📁 Struktur Direktori

```
LuminasRegret/
├── .github/workflows/  # CI/CD Automated Build & Test pipeline
├── bin/                # Output binary file .class terkompilasi
├── docs/               # Dokumentasi teknis & arsitektur proyek
│   ├── Architecture.md # Cetak biru arsitektur sistem & loop 60 FPS
│   ├── Patterns.md     # Penerapan desain pola & Effective Java
│   ├── Roadmap.md      # Roadmap pengembangan & milestone rilis
│   ├── Run.md          # Panduan eksekusi desktop, tuning JVM & DPI
│   ├── Testing.md      # Katalog automated sanity & headless testing
│   └── report.md       # Laporan inspeksi & audit arsitektur menyeluruh
├── res/                # Aset game (sprites, tiles, maps, sounds, fonts)
│   ├── font/           # Font retro pixel-art TTF
│   ├── maps/           # Map plaintext 50x50 tile (worldV3.txt, dungeon01.txt)
│   ├── monster/        # Sprite Slime, Zombie, Goblin King
│   ├── npc/            # Sprite 4 arah Guide (Sylvia) & Merchant (Boran)
│   ├── objects/        # Sprite item, senjata, relik, kunci, peti, HUD
│   ├── player/         # Animasi gerak, ayunan tebas senjata, perisai
│   └── sound/          # File audio uncompressed WAV (BGM & SFX)
├── scripts/            # Helper automation scripts (compile, build-jar, run-tests)
├── src/com/luminasregret/ # Source code modular Java SE (Reverse-Domain Standard)
│   ├── engine/         # Sub-sistem inti (core, audio, gfx, input, physics, ai)
│   ├── game/           # Domain gameplay (entity, monster, object, quest, tile, world)
│   └── ui/             # Dynamic HUD, menu interaktif & typography presentation
├── web/                # WebAssembly web portal player (CheerpJ)
├── AGENTS.md           # Master operational guide untuk AI Coding Agent
└── README.md           # Halaman utama repositori
```

---

## 📜 Lisensi & Kredit Aset

- **Source Code**: Dirilis di bawah lisensi [MIT License](LICENSE).
- **Aset Audio (Musik & SFX)**: Berlisensi **Creative Commons CC0 (Public Domain)**:
  - *The Old Tower Inn* & *King's Feast* oleh **RandomMind** (OpenGameArt).
  - *Epic Boss Battle* oleh **Juhani Junkala / SubspaceAudio** (OpenGameArt).
  - *Spooky Dungeon* oleh **Memoraphile / You're Perfect Studio** (OpenGameArt).
- **Aset Visual & Sprite**: Berlisensi CC0 / OpenGameArt dengan penyesuaian khusus proyek.

---

<div align="center">
Dikembangkan dengan ❤️ untuk pelestarian estetika retro 16-bit RPG murni.
</div>
