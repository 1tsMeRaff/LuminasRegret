# Architectural Inspection & Audit Report: Lumina's Regret
**Auditor**: Principal Java Architect, Staff Core Java Engineer, JVM Performance Specialist & Effective Java Reviewer  
**Target Platform**: Java SE 21 (Core JDK)  
**Scope**: Full Codebase Zero-Tolerance Technical & Architectural Inspection  

---

## Table of Contents
1. [Executive Architecture Summary](#1-executive-architecture-summary)
2. [Package & Class Blueprint](#2-package--class-blueprint)
3. [Java Best Practice & Idiomatic Design Standards](#3-java-best-practice--idiomatic-design-standards)
4. [Concurrency & Memory Model Strategy](#4-concurrency--memory-model-strategy)
5. [Deep Technical Analysis (Task per Task)](#5-deep-technical-analysis-task-per-task)
   - [Task 1.1: Core Architecture, Modularity, & Package Structure Audit](#task-11-core-architecture-modularity--package-structure-audit)
   - [Task 1.2: Object-Oriented Design, SOLID, & Effective Java Compliance](#task-12-object-oriented-design-solid--effective-java-compliance)
   - [Task 1.3: Concurrency, Thread Safety, & Race Condition Audit](#task-13-concurrency-thread-safety--race-condition-audit)
   - [Task 1.4: Memory Management, JVM Allocation, & Resource Lifecycle](#task-14-memory-management-jvm-allocation--resource-lifecycle)
   - [Task 1.5: Data Structures, Collections, & Exception Strategy](#task-15-data-structures-collections--exception-strategy)
6. [Contract Definition & Method Signatures](#6-contract-definition--method-signatures)
7. [Risk & Regression Assessment](#7-risk--regression-assessment)
8. [Recommended Implementation Sequence](#8-recommended-implementation-sequence)

---

## 1. Executive Architecture Summary

Audit teknis komprehensif dan agresif (*zero-tolerance architectural inspection*) terhadap codebase **Lumina's Regret** (berjalan pada lingkungan **Java SE 21**) menunjukkan bahwa arsitektur saat ini berada dalam kondisi **kritis (High Architectural Debt)**. Kode dibangun dengan paradigma prosedural terpusat (*monolithic procedural sprawl*) yang dibungkus dalam kelas-kelas berorientasi objek semu.

Dua anti-pattern dominan yang mendegradasi fondasi sistem adalah **God Object** ([Entity.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/entity/Entity.java) dan [GamePanel.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/GamePanel.java)) serta **Inheritance Abuse** (subclassing tanpa spesialisasi behavioral murni). Selain itu, terdapat pelanggaran fundamental terhadap *Java Memory Model (JMM)*, kebocoran resource native OS (*Audio line & stream leaks*), serta *GC pressure* masif yang dihasilkan oleh puluhan ribu alokasi objek sementara (*short-lived heap allocations*) di dalam game loop per detik.

```
                              ┌──────────────────────────────────┐
                              │            GamePanel             │
                              │ (God Class / Central Coupling)   │
                              └─┬──────────────┬───────────────┬─┘
                                │              │               │
        ┌───────────────────────┼──────────────┼───────────────┼──────────────────────┐
        ▼                       ▼              ▼               ▼                      ▼
┌───────────────┐     ┌──────────────────┐ ┌────────┐ ┌───────────────────┐ ┌──────────────────┐
│  KeyHandler   │     │      Entity      │ │ TileM. │ │ CollisionChecker  │ │    Sound/UI      │
│ (EDT Unsafe)  │     │ (965 LOC God Obj)│ │ (Leaks)│ │ (GC Thrashing)    │ │(Resource Leaks)  │
└───────┬───────┘     └────────┬─────────┘ └────────┘ └───────────────────┘ └──────────────────┘
        │                      │
        │ Race Conditions      │ Extreme Coupling (IS-A Weapon, Chest, Particle?)
        ▼                      ▼
┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│ CRITICAL HAZARDS: Unsafe Concurrency (EDT vs GameThread), Eden GC Spikes, Unclosed Streams  │
└─────────────────────────────────────────────────────────────────────────────────────────────┘
```

### Klasifikasi Temuan Audit (Severity Matrix)

| Tingkat Keparahan | Lokasi / Komponen | Kategori Pelanggaran | Ringkasan Dampak Teknis |
| :--- | :--- | :--- | :--- |
| **CRITICAL** | [KeyHandler.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/KeyHandler.java#L9-L10) <br> [GamePanel.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/GamePanel.java#L165-L180) | JMM / Thread Safety Violation | **Data race & visibility hazard**: Variabel input diakses dan dimutasi lintas thread (AWT-EDT vs `gameThread`) tanpa `volatile`, locks, atau atomic types. Berpotensi register caching oleh JIT compiler dan dropped inputs. |
| **CRITICAL** | [CollisionChecker.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/CollisionChecker.java#L133-L250) | Memory / GC Eden Churn | **GC pressure ekstrem**: Alokasi instansiasi `new Rectangle(...)` hingga 4x per pasang entitas di setiap tick game loop (~50.000–120.000 objek/detik). Memicu *stop-the-world* GC pauses yang merusak frame pacing. |
| **CRITICAL** | [Sound.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/Sound.java#L35-L51) | Unmanaged OS Resource Leak | **Audio handle exhaustion**: `AudioInputStream` dan instance `Clip` sebelumnya tidak pernah ditutup (`close()`). Pemanggilan `playSE()` berulang kali menumpuk unclosed native lines pada sound daemon OS. |
| **CRITICAL** | [Entity.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/entity/Entity.java#L758-L760) | Indexing Fatal Bug | `gp.projectile[i].length` di mana `i = random.nextInt(rate)`. Jika `rate > maxMap` (10), program melempar `ArrayIndexOutOfBoundsException` tak tertangani yang mematikan game loop seketika. |
| **MAJOR** | [Entity.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/entity/Entity.java#L18-L125) | God Object & Inheritance Abuse | Pelanggaran SRP (*Single Responsibility Principle*). Senjata, chest, proyektil, partikel, NPC, dan monster semuanya mewarisi 90+ atribut mutable publik yang tidak relevan. |
| **MAJOR** | [PathFinder.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/ai/PathFinder.java#L33-L88) | Algorithmic Inefficiency $O(N \cdot M)$ | Nested 4-level loop (50×50 grid × 520 entitas) mengeksekusi 1,3 juta iterasi per kalkulasi pathfinding. Ditambah *shared mutable state* tunggal pada `GamePanel.pFinder` memicu konflik state antar monster. |
| **MAJOR** | [TileManager.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/tile/TileManager.java#L174-L175) <br> [UI.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/UI.java#L46-L56) | I/O Stream Leak | Pemanggilan `ImageIO.read(getClass().getResourceAsStream(...))` dan font loading tanpa `try-with-resources`. Stream input tetap terbuka di memori JVM. |
| **MAJOR** | [EventHandler.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/EventHandler.java#L18-L43) | Excessive Heap Eager Allocation | Menginstansiasi 25.000 objek `EventRect` (10 maps × 50 col × 50 row) di memori heap secara statis saat inisialisasi, padahal hanya <10 titik koordinat yang memiliki event aktif. |
| **MINOR** | [UI.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/UI.java#L258-L261) | Concurrent Modification / Logic Bug | Menghapus elemen `ArrayList` saat iterasi maju tanpa dekremen indeks ($i$), menyebabkan elemen berikutnya terlewatkan (*skipped element execution*). |
| **MINOR** | [Config.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/Config.java#L19-L38) | Platform Encoding Vulnerability | Penggunaan `FileReader`/`FileWriter` tanpa spesifikasi `StandardCharsets.UTF_8`. File stream rawan korupsi pada konfigurasi lintas OS. |

---

## 2. Package & Class Blueprint

Arsitektur target memisahkan *Game Engine Core*, *Input*, *Audio*, *Physics/Spatial*, *World*, *Entity Component/Data Carrier*, dan *Render Subsystem* menjadi bounded context independen.

```
src/
├── core/
│   ├── engine/
│   │   ├── GameEngine.java (Class - Master Loop, Thread Controller)
│   │   ├── GameState.java (Sealed Interface / Enum)
│   │   └── WindowManager.java (Class - Swing Lifecycle, Canvas/BufferStrategy)
│   ├── input/
│   │   ├── InputManager.java (Class - Atomic Input Collector)
│   │   ├── KeyState.java (Record - Thread-safe Snapshot)
│   │   └── Command.java (Enum - Abstraksi Intent, bukan KeyCode)
│   └── audio/
│       ├── AudioManager.java (Class - AutoCloseable Pool)
│       └── SoundClip.java (Record / Class - Managed Clip Handle)
├── spatial/
│   ├── BoundingBox.java (Record - Immutable AABB)
│   ├── SpatialPartition.java (Interface - Grid / QuadTree)
│   └── CollisionEngine.java (Class - Pure Primitive Math, Zero-GC)
├── world/
│   ├── WorldMap.java (Record / Class - Tile Metadata & Grid)
│   ├── TileType.java (Enum - Properties, Collision, Texture ID)
│   ├── EventTrigger.java (Record - Sparse Coordinate Event)
│   └── WorldEventManager.java (Class - Map-based Event Dispatcher)
├── entity/
│   ├── Actor.java (Interface - Contract untuk Dynamic Objects)
│   ├── Transform.java (Record / Class - Position, Direction)
│   ├── Stats.java (Record - Health, Mana, Defense, Attack)
│   ├── Item.java (Sealed Interface - Weapon, Armor, Consumable, KeyItem)
│   ├── Inventory.java (Class - Defensive-copy Encapsulated Container)
│   └── Projectile.java (Class - Independent Lifecycled Object)
├── ai/
│   ├── PathFinder.java (Interface - Isolated / Stateless Path Planner)
│   ├── PathNode.java (Class implements Comparable<PathNode>)
│   └── AStarSearch.java (Class - PriorityQueue Min-Heap Implementation)
└── render/
    ├── Camera.java (Record / Class - Viewport Calculator)
    ├── SpriteSheet.java (Class - Texture Subimage Caching)
    └── RenderPipeline.java (Class - Layered Rendering Context)
```

---

## 3. Java Best Practice & Idiomatic Design Standards

Penegakan kaidah **Effective Java (3rd Edition)** dan **Clean Code** pada arsitektur baru:

1. **Item 1: Pertimbangkan Static Factory Methods daripada Constructor**
   - Inisialisasi entitas dan item tidak lagi menggunakan konstruktor bertumpuk dengan 40+ argumen/mutasi langsung. Digantikan oleh static factory: `Item.createWeapon(...)`, `Actor.spawnMonster(...)`.
2. **Item 15 & 16: Minimalkan Aksesibilitas Anggota dan Gunakan Aksesor, Bukan Public Fields**
   - Hapus seluruh `public int worldX, worldY`, `public int speed`, `public Rectangle solidArea` pada [Entity.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/entity/Entity.java). Bungkus seluruh data dalam status privat atau *immutable records*.
3. **Item 17: Minimalkan Mutabilitas (Immutability by Default)**
   - Transformasi koordinat, bounding box, dan event trigger ke dalam Java `record`. Data yang tidak berubah selama frame tick berstatus *immutable*.
4. **Item 18: Utamakan Komposisi daripada Pewarisan (Composition over Inheritance)**
   - Putus hubungan di mana `OBJ_Chest`, `OBJ_Axe`, dan `Particle` mewarisi `Entity`. `OBJ_Chest` adalah `WorldObject` yang memiliki `InventoryComponent`, bukan `Entity` yang memiliki `hpBarCounter` dan `myPath`.
5. **Item 23: Hindari Tagged Classes, Gunakan Class Hierarchy / Sealed Types**
   - Ganti konstanta int manual (`type_player = 0`, `type_monster = 2`, `type_sword = 3`) dengan Java 21 `sealed interface` dan `enum` ekspresif yang diverifikasi melalui exhaustive pattern matching.
6. **Item 50: Buat Defensive Copies jika Diperlukan**
   - Getter untuk inventory, path node list, atau audio list wajib mengembalikan `Collections.unmodifiableList(...)` atau salinan baru guna mencegah mutasi eksternal tak terkontrol.
7. **Item 9: Utamakan `try-with-resources` daripada `try-finally`**
   - Seluruh stream file resource di [Sound.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/Sound.java), [TileManager.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/tile/TileManager.java), [Config.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/Config.java), dan [UI.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/UI.java) wajib ditutup menggunakan idiom `try (InputStream is = ...)` untuk mencegah *file descriptor leak*.

---

## 4. Concurrency & Memory Model Strategy

### 4.1 Concurrency Model: Thread Confinement & Atomic Boundary

Sistem game desktop murni berbasis Swing memiliki dua thread aktif:
* **AWT-Event Dispatch Thread (EDT)**: Menangkap OS keystrokes (`keyPressed`, `keyReleased`) dan window lifecycle.
* **Game Loop Thread (`gameThread`)**: Menjalankan kalkulasi logika fisika 60 Hz dan rendering.

```
       [AWT-EDT Thread]                         [Game Loop Thread]
   (OS Keyboard / Mouse Events)                (60 FPS Fixed Timestep)
               │                                          │
               ▼                                          ▼
   ┌───────────────────────┐                  ┌────────────────────────┐
   │ Writes to bitmask     │                  │ Reads snapshot via     │
   │ AtomicInteger keyMask │                  │ AtomicInteger.get()    │
   └───────────┬───────────┘                  └───────────┬────────────┘
               │                                          │
               └─────────────── JMM Barrier ──────────────┘
                       (Happens-Before Guaranteed)
```

* **Penetapan Memory Barrier**:
  Hapus seluruh boolean publik non-volatile di [KeyHandler.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/KeyHandler.java). Gunakan `java.util.concurrent.atomic.AtomicInteger` sebagai bitmask ringkas:
  - EDT memanggil: `inputMask.updateAndGet(mask -> mask | KEY_UP_BIT);`
  - GameThread membaca snapshot: `int currentInput = inputMask.get();`
  - Hal ini menjamin relasi *happens-before* sesuai spesifikasi JMM (JSR-133) tanpa overhead synchronized lock.
* **Swing Rendering Confinement**:
  Hilangkan pemanggilan `Graphics g = getGraphics(); g.dispose();` dari background thread di [GamePanel.java:390](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/GamePanel.java#L390). Gunakan `java.awt.Canvas` dengan `java.awt.image.BufferStrategy` aktif (Active Rendering) atau delegasikan repaint ke EDT via `SwingUtilities.invokeLater()` jika tetap memakai `JPanel`.

### 4.2 Zero-Allocation GC Strategy di Dalam Hot Path Game Loop

* **Primitive Collision Math (Eliminasi `new Rectangle`)**:
  Komparasi collision tidak boleh membuat instance `Rectangle` baru. Kalkulasi dilakukan menggunakan aljabar Axis-Aligned Bounding Box (AABB) dengan parameter tipe data primitif:
  $$\text{Overlap}_X = (X_1 < X_2 + W_2) \land (X_1 + W_1 > X_2)$$
  $$\text{Overlap}_Y = (Y_1 < Y_2 + H_2) \land (Y_1 + H_1 > Y_2)$$
  Operasi ini dieksekusi murni pada CPU register / stack frame tanpa alokasi heap 1 byte pun.
* **Eliminasi `new Random()`**:
  Ganti instansiasi acak di [Entity.java:742](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/entity/Entity.java#L742) dan [Entity.java:826](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/entity/Entity.java#L826) dengan `java.util.concurrent.ThreadLocalRandom.current().nextInt(...)`.
* **Sparse Event Coordinates**:
  Hapus array kubik statis `EventRect[10][50][50]` di [EventHandler.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/EventHandler.java). Ganti dengan `Map<Long, EventTrigger>` di mana `key = ((long)map << 32) | ((long)col << 16) | row`. Mengurangi 25.000 objek menjadi hanya sejumlah event yang benar-benar ada di peta.

---

## 5. Deep Technical Analysis (Task per Task)

### Task 1.1: Core Architecture, Modularity, & Package Structure Audit

1. **Requirement & Problem Statement**:
   Membongkar kopling sirkular dan sentralisasi berlebih pada [GamePanel.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/GamePanel.java). Saat ini, hampir semua kelas ([Player](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/entity/Player.java#L20), [CollisionChecker](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/CollisionChecker.java#L9), [AssetSetter](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/AssetSetter.java#L24), [UI](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/UI.java#L21), [TileManager](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/tile/TileManager.java#L18)) menyimpan referensi langsung ke `GamePanel` publik (*spaghetti coupling*).
2. **Core Java Architecture & Package Structure**:
   Pecah tanggung jawab `GamePanel` ke dalam:
   - `core.engine.GameLoop`: Hanya mengontrol delta time, tick rate (60 Hz), dan thread lifecycle.
   - `world.WorldService`: Mengelola array peta dan navigasi antar map.
   - `entity.EntityRegistry`: Mengelola koleksi entitas, spawning, dan culling.
3. **Object-Oriented Design & Design Patterns**:
   - Terapkan **Mediator Pattern** atau **Event Bus ringan berbasis Interface**: Antar subsistem berkomunikasi melalui pertukaran event (*loose coupling*), bukan dengan membaca field publik `gp.player.worldX`.
   - Terapkan **Facade Pattern** untuk membungkus Game Engine.
4. **Java Best Practices & Idiomatic Standards (Effective Java)**:
   - **Item 15 (Minimize the accessibility of classes and members)**: Kelas utilitas dan internal tracker dijadikan package-private.
   - Hilangkan akses direct traversal `gp.obj[gp.currentMap][i]`.
5. **Concurrency & Thread-Safety Model**:
   Pemisahan state engine dari representasi Swing GUI memastikan logic loop dapat berjalan di thread independen tanpa interferensi GUI paint locks.
6. **Memory Management & JVM Impact**:
   Mengurangi ukuran retain objek `GamePanel` yang menjadi akar penahan (*GC Root Reference Holder*) bagi seluruh objek dalam game.
7. **Collections & Data Structures Strategy**:
   Ganti array 2D kaku `Entity[maxMap][20]` dengan `List<Actor>` dinamis per map (`ArrayList` dengan initial capacity terukur).
8. **Refactor Impact & Compatibility**:
   Membutuhkan restrukturisasi signature konstruktor hampir di seluruh codebase.
9. **Implementation Order**:
   Definisikan interface `WorldContext` dan `ServiceLocator`, lalu ganti referensi `GamePanel` di kelas pembantu dengan interface konteks terbatas.

---

### Task 1.2: Object-Oriented Design, SOLID, & Effective Java Compliance

1. **Requirement & Problem Statement**:
   [Entity.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/entity/Entity.java) menampung 965 baris kode dengan pelanggaran *Single Responsibility Principle* (SRP) dan *Liskov Substitution Principle* (LSP). Item dalam tas ([OBJ_Key](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/object/OBJ_Key.java), [OBJ_Bread](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/object/OBJ_Bread.java)) diperlakukan sebagai turunan `Entity`, sehingga roti dan kunci memiliki atribut HP, kecepatan jalan, AI Pathfinding, dan bahkan inventory sendiri di memori.
2. **Core Java Architecture & Package Structure**:
   Pisahkan entitas dinamis (`Actor`), objek statis interaktif (`StaticProp`), dan item inventaris (`Item`). `Item` diisolasi ke dalam package `entity.item`.
3. **Object-Oriented Design & Design Patterns**:
   - **Strategy Pattern**: Perilaku gerak (`MovementStrategy`: random, chase, passive) disuntikkan ke dalam `Actor`, bukan di-hardcode di dalam `Entity.update()` dengan switch-case direction.
   - **Component-based Separation**: Pisahkan komponen `CombatStats` dari entitas.
4. **Java Best Practices & Idiomatic Standards (Effective Java)**:
   - **Item 18 (Favor composition over inheritance)**: `Player` memiliki `Inventory`, `Monster` memiliki `CombatStats`.
   - **Item 23 (Prefer class hierarchies to tagged classes)**: Hapus integer tag `type_player`, `type_monster`, `type_consumable`. Gunakan Java 21 `sealed interface Item permits Weapon, Shield, Consumable`.
5. **Concurrency & Thread-Safety Model**:
   Objek `Item` dijadikan *immutable record*: aman dibagikan antar komponen tanpa perlindungan konkurensi tambahan.
6. **Memory Management & JVM Impact**:
   Pengurangan drastis ukuran footprint objek item: dari ~400 byte per instance `Entity` (overhead field-field yang tidak terpakai) menjadi ~32 byte untuk satu Java `record`.
7. **Collections & Data Structures Strategy**:
   Penggunaan `EnumMap<EquipmentSlot, Item>` untuk perlengkapan karakter yang aktif.
8. **Refactor Impact & Compatibility**:
   Mengharuskan modifikasi cara [UI.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/UI.java#L111) dan [Player.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/entity/Player.java#L114) memanipulasi tas dan perlengkapan.
9. **Implementation Order**:
   Ekstraksi interface dan record `Item` -> Refaktorisasi `Player.inventory` agar menerima `Item` -> Pecah `Entity` menjadi `Actor` murni.

---

### Task 1.3: Concurrency, Thread Safety, & Race Condition Audit

1. **Requirement & Problem Statement**:
   Terjadi pelanggaran *Java Memory Model* (JMM) fatal antara AWT-EDT dan `gameThread` pada pembacaan/penulisan field di [KeyHandler.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/KeyHandler.java#L9-L10). Selain itu, pemanggilan `getGraphics()` pada [GamePanel.java:390](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/GamePanel.java#L390) melanggar arsitektur threading Swing.
2. **Core Java Architecture & Package Structure**:
   Bangun `core.input.InputManager` yang bertindak sebagai jembatan isolasi thread (*thread confinement bridge*).
3. **Object-Oriented Design & Design Patterns**:
   - **Producer-Consumer / Snapshot Pattern**: EDT memproduksi event perubahan input bitmask, Game Loop mengonsumsi snapshot nilai bitmask di awal setiap frame.
4. **Java Best Practices & Idiomatic Standards (Effective Java)**:
   - **Item 78 (Synchronize access to shared mutable data)**: Mengeliminasi data race dengan menggunakan operasi atomic atau variabel `volatile`.
5. **Concurrency & Thread-Safety Model**:
   Gunakan bitwise flag dalam `AtomicInteger`:
   ```java
   public final class InputSnapshot {
       public static final int UP = 1 << 0;
       public static final int DOWN = 1 << 1;
       public static final int LEFT = 1 << 2;
       public static final int RIGHT = 1 << 3;
       public static final int ACTION = 1 << 4;
   }
   ```
   Game loop membaca integer primitif dalam satu siklus memori atomic: `int state = inputManager.pollSnapshot();`.
6. **Memory Management & JVM Impact**:
   Zero heap allocation per frame untuk polling input. Menghilangkan alokasi objek event yang tidak perlu.
7. **Collections & Data Structures Strategy**:
   Penggunaan primitive bitfield integer alih-alih multiple boolean variables.
8. **Refactor Impact & Compatibility**:
   [Player.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/entity/Player.java#L252) dan [KeyHandler.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/KeyHandler.java) harus dirombak agar tidak saling membaca variabel boolean mentah.
9. **Implementation Order**:
   Buat kelas `InputManager` thread-safe -> Pasang `InputManager` pada listener window -> Sambungkan `Player.update()` ke snapshot `InputManager`.

---

### Task 1.4: Memory Management, JVM Allocation, & Resource Lifecycle

1. **Requirement & Problem Statement**:
   - Bencana alokasi short-lived object di [CollisionChecker.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/CollisionChecker.java#L133-L250) membebani Young Generation (Eden space).
   - Kebocoran native audio resource di [Sound.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/Sound.java#L37-L43) karena `AudioInputStream` dan instance `Clip` sebelumnya tidak ditutup saat berganti efek suara.
   - Stream leak pada `ImageIO.read` di [TileManager.java:174](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/tile/TileManager.java#L174) dan [UI.java:46](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/UI.java#L46).
2. **Core Java Architecture & Package Structure**:
   Buat package `core.audio` dengan interface `AutoCloseableSound`. Buat kelas `spatial.AABBCollision` murni static mathematical functions.
3. **Object-Oriented Design & Design Patterns**:
   - **Object Pool Pattern**: Pool untuk audio `Clip` yang reusable.
   - **Flyweight Pattern**: Caching gambar tile dan font sehingga tidak dimuat berulang kali ke heap.
4. **Java Best Practices & Idiomatic Standards (Effective Java)**:
   - **Item 9 (Prefer try-with-resources to try-finally)**:
     ```java
     try (InputStream is = getClass().getResourceAsStream(path)) {
         if (is == null) throw new ResourceNotFoundException(path);
         return ImageIO.read(is);
     }
     ```
   - **Item 6 (Avoid creating unnecessary objects)**: Stop instansiasi `new Rectangle()` di setiap pengecekan fisik.
5. **Concurrency & Thread-Safety Model**:
   Audio clips yang diputar di background thread internal Java Sound API dikelola lifecycle penutupannya dengan aman (`LineListener`).
6. **Memory Management & JVM Impact**:
   Penurunan alokasi memori Eden dari megabytes/detik menjadi mendekati nol selama runtime game loop berjalan. Mengeliminasi *micro-stuttering* akibat GC pause.
7. **Collections & Data Structures Strategy**:
   Penggunaan pooling data array primitif jika buffer sementara dibutuhkan.
8. **Refactor Impact & Compatibility**:
   `CollisionChecker` lama dihapus total, digantikan oleh stateless math utility.
9. **Implementation Order**:
   Bungkus `Sound` dengan `AutoCloseable` dan perbaiki stream leak -> Tulis ulang `CollisionChecker` menjadi aljabar primitif -> Amankan pemuatan file konfigurasi dan font dengan `try-with-resources`.

---

### Task 1.5: Data Structures, Collections, & Exception Strategy

1. **Requirement & Problem Statement**:
   - [PathFinder.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/ai/PathFinder.java#L202-L211) melakukan linear search $O(N)$ pada `ArrayList<Node> openList` untuk mencari node dengan F-cost terendah, dan `openList.remove(currentNode)` memakan $O(N)$ array copy.
   - `PathFinder.buildPath()` melakukan `pathList.add(0, current)` yang memicu $O(N^2)$ array shifts.
   - Nested loop di `PathFinder.setNodes()` memakan 1,3 juta iterasi per kalkulasi pathfinding.
   - Tangkapan exception di [Config.java:68](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/Config.java#L68) dan [Sound.java:47](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/main/Sound.java#L47) menggunakan generic `catch (Exception e)` dengan sekadar print console tanpa recovery terarah.
2. **Core Java Architecture & Package Structure**:
   Modernisasi package `ai` menjadi struktur A* idiomatik Java Collections. Buat hierarki exception spesifik: `GameResourceException`, `ConfigurationException` (Unchecked).
3. **Object-Oriented Design & Design Patterns**:
   - **Comparable Specification**: [Node.java](file:///c:/Users/HP/Rafi/MyProject/LuminasRegret/src/ai/Node.java) mengimplementasikan `Comparable<PathNode>` untuk integrasi natif dengan struktur data heap.
4. **Java Best Practices & Idiomatic Standards (Effective Java)**:
   - **Item 14 (Consider implementing Comparable)**: `PathNode` mengurutkan prioritas berdasarkan F-Cost dan H-Cost secara konsisten dengan `equals()`.
   - **Item 72 (Favor the use of standard exceptions)**: Gunakan `IllegalArgumentException`, `IllegalStateException`.
   - **Item 77 (Don't ignore exceptions)**: Hilangkan silent catch.
5. **Concurrency & Thread-Safety Model**:
   Objek `PathFinder` diinstansiasi per aktor atau dijadikan pure worker thread-safe, bukan satu instance bersama yang saling menimpa array tujuan.
6. **Memory Management & JVM Impact**:
   Pergantian ke `PriorityQueue` memangkas traversal CPU cycles secara eksponensial ($O(N) \to O(\log N)$). Pembalikan path menggunakan `ArrayDeque` ($O(1)$ prepend) mengeliminasi pemborosan alokasi array shift di heap.
7. **Collections & Data Structures Strategy**:
   - `PriorityQueue<PathNode>` untuk `openList`.
   - `ArrayDeque<PathNode>` untuk rekonstruksi rute akhir.
   - Penandaan solid tiles menggunakan array bitwise boolean `boolean[] solidGrid` 1D ($col + row \times width$) untuk optimasi cache line CPU L1/L2.
8. **Refactor Impact & Compatibility**:
   Monster AI harus memanggil API `PathFinder` baru yang mengembalikan `Optional<Deque<PathNode>>`.
9. **Implementation Order**:
   Implementasi `Comparable` dan kontrak kesetaraan pada `PathNode` -> Tulis ulang algoritma A* menggunakan `PriorityQueue` -> Optimasi pre-computated solid grid map.

---

## 6. Contract Definition & Method Signatures

Blueprint kontrak API baru (Pure Java 21) tanpa implementasi konkret:

```java
package spatial;

/**
 * Immutable Axis-Aligned Bounding Box (AABB) dengan aljabar zero-allocation.
 */
public record BoundingBox(int x, int y, int width, int height) {
    public BoundingBox {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Dimensi bounding box tidak boleh negatif");
        }
    }

    public static boolean intersects(int x1, int y1, int w1, int h1, 
                                     int x2, int y2, int w2, int h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
    }
}
```

```java
package core.input;

/**
 * Kontrak snapshot input thread-safe (JMM-compliant).
 */
public interface InputService {
    int getActiveMask();
    boolean isActionPressed(int actionBit);
    void resetOneShotTriggers();
}
```

```java
package core.audio;

/**
 * Pengelolaan audio berbasis idiom AutoCloseable untuk mencegah resource leak.
 */
public interface AudioService extends AutoCloseable {
    void playSoundEffect(int soundId);
    void playMusic(int musicId);
    void stopMusic();
    void setMasterVolume(float decibels);
    @Override
    void close();
}
```

```java
package entity.item;

/**
 * Representasi item berbasis Java 21 Sealed Interface.
 */
public sealed interface Item permits Weapon, Shield, Consumable, QuestItem {
    int id();
    String name();
    String description();
    int value();
}

public record Weapon(int id, String name, String description, int value, 
                     int attackPower, int knockBackPower) implements Item {}

public record Shield(int id, String name, String description, int value, 
                     int defensePower) implements Item {}

public record Consumable(int id, String name, String description, int value, 
                         int healAmount, int manaRestore) implements Item {}

public record QuestItem(int id, String name, String description, int value) implements Item {}
```

```java
package ai;

import java.util.Deque;
import java.util.Optional;

/**
 * Kontrak pencarian rute independen (isolated search context).
 */
public interface PathfindingService {
    Optional<Deque<Coordinate>> findPath(Coordinate start, Coordinate goal, boolean[] collisionGrid, int gridWidth, int gridHeight);
}

public record Coordinate(int col, int row) {}
```

---

## 7. Risk & Regression Assessment

| Komponen yang Diubah | Risiko Regresi Potensial | Strategi Mitigasi / Verifikasi |
| :--- | :--- | :--- |
| **Physics / Collision** | Perbedaan deteksi tabrakan 1-pixel akibat peralihan dari `Rectangle.intersects()` ke aljabar AABB primitif. | Buat unit test matrix untuk memverifikasi kesetaraan output geometri sebelum kode lama dihapus. |
| **Input System** | Drop input atau delayed response jika siklus sinkronisasi bitmask antara EDT dan Game Loop tidak selaras. | Jaga agar Game Loop mengeksekusi polling input tepat di awal frame tick sebelum fisika dievaluasi. |
| **Audio Subsystem** | Terjadi crash (*LineUnavailableException*) jika pool audio clip melampaui batas channel sound card OS. | Batasi ukuran pool maksimum (misal: 16 concurrent lines) dengan eviction policy untuk sound yang selesai diputar. |
| **Inventory System** | Bug saat transfer item antar chest dan player karena migrasi dari `Entity` ke `Item` record. | Isolasi fungsionalitas container ke dalam kelas `Inventory` mandiri yang divalidasi dengan unit test. |
| **Pathfinding Engine** | NPC mogok berjalan atau *oscillation bug* (bolak-balik arah) akibat penanganan heuristic A* baru. | Pertahankan threshold jarak tile center lama (2-4 pixel tolerance) pada logika `followPath()`. |

---

## 8. Recommended Implementation Sequence

Rencana refaktorisasi 4 fase bertahap (*risk-managed modernization roadmap*):

```
┌────────────────────────────────────────────────────────────────────────┐
│ PHASE 1: STABILIZATION & LEAK ISOLATION (ZERO ARCHITECTURAL REGRESSION)│
│ • Perbaiki stream leak & clip leaks pada Sound, TileManager, dan UI    │
│ • Amankan JMM Concurrency pada KeyHandler (AtomicInteger bitmask)      │
│ • Patch fatal bug index array out-of-bounds pada projectile Entity     │
└──────────────────────────────────┬─────────────────────────────────────┘
                                   │
                                   ▼
┌────────────────────────────────────────────────────────────────────────┐
│ PHASE 2: HOT PATH ZERO-GC REFACTORING                                  │
│ • Hapus alokasi new Rectangle di CollisionChecker (ganti AABB murni)   │
│ • Hapus alokasi new Random di loop tick (ganti ThreadLocalRandom)      │
│ • Modernisasi A* PathFinder menggunakan PriorityQueue & ArrayDeque     │
│ • Hapus 25.000 eager array EventRect (ganti sparse Map)                │
└──────────────────────────────────┬─────────────────────────────────────┘
                                   │
                                   ▼
┌────────────────────────────────────────────────────────────────────────┐
│ PHASE 3: DOMAIN & OBJECT MODEL DECOUPLING                              │
│ • Ekstraksi Item menjadi Sealed Interface & Record (Lepas dari Entity) │
│ • Pecah God Class Entity menjadi Actor dan WorldObject                 │
│ • Terapkan enkapsulasi privat dan defensive copying pada Inventory     │
└──────────────────────────────────┬─────────────────────────────────────┘
                                   │
                                   ▼
┌────────────────────────────────────────────────────────────────────────┐
│ PHASE 4: ENGINE & SUBSYSTEM DECOUPLING                                 │
│ • Pecah GamePanel menjadi GameLoop, WorldService, dan EntityRegistry   │
│ • Terapkan Active Rendering (BufferStrategy) independen dari EDT       │
│ • Finalisasi transisi ke standar Java SE 21 murni                      │
└────────────────────────────────────────────────────────────────────────┘
```
