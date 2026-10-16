/**
 * Lumina's Regret: Web Portal Script
 * CheerpJ WebAssembly Engine Controller & Interactive Showcase Mechanics
 */

document.addEventListener('DOMContentLoaded', () => {
    // 1. Core DOM Elements
    const btnStartGame = document.getElementById('btn-start-game');
    const splashOverlay = document.getElementById('splash-overlay');
    const splashContent = document.querySelector('.splash-content');
    const loadingState = document.getElementById('loading-state');
    const progressFill = document.getElementById('pixel-progress-fill');
    const progressPercentage = document.getElementById('loading-percentage');
    const loadingMessage = document.getElementById('loading-message');
    const systemStatusText = document.getElementById('system-status-text');
    const screenContainer = document.getElementById('screen-container');
    const btnFullscreen = document.getElementById('btn-fullscreen');
    const btnCaptureInput = document.getElementById('btn-scroll-top');
    const btnFsExit = document.getElementById('btn-fs-exit');

    let gameStarted = false;
    let audioContext = null;

    // 2. Audio Policy Unlock & Game Boot Initiation
    if (btnStartGame) {
        btnStartGame.addEventListener('click', async () => {
            if (gameStarted) return;

            // Notice for local file:// protocol
            if (window.location.protocol === 'file:') {
                alert(
                    "PERHATIAN:\n\n" +
                    "Game WebAssembly tidak dapat dijalankan via double-click file lokal (file://) karena kebijakan keamanan peramban memblokir pembacaan file virtual.\n\n" +
                    "Opsi Menjalankan:\n" +
                    "1. Buka secara online via GitHub Pages: https://1tsMeRaff.github.io/LuminasRegret/\n" +
                    "2. Atau jalankan server lokal via PowerShell/Terminal: npx serve web/\n" +
                    "3. Atau mainkan secara native desktop dengan mengklik tombol 'Unduh JAR'."
                );
                return;
            }

            gameStarted = true;

            // Unlock browser Web Audio Context
            try {
                audioContext = new (window.AudioContext || window.webkitAudioContext)();
                if (audioContext.state === 'suspended') {
                    await audioContext.resume();
                }
            } catch (e) {
                console.warn('Web Audio initialization note:', e);
            }

            // Switch to booting state
            if (splashContent) splashContent.style.display = 'none';
            if (loadingState) loadingState.classList.remove('hidden');
            if (systemStatusText) systemStatusText.textContent = 'Memulai JVM...';

            startCheerpJEngine();
        });
    }

    // 3. CheerpJ WebAssembly Engine Boot
    async function startCheerpJEngine() {
        try {
            updateProgress(20, 'Mengunduh Java Runtime WebAssembly...');

            if (typeof cheerpjInit !== 'function') {
                throw new Error('Skrip loader CheerpJ belum siap atau diblokir oleh peramban.');
            }

            updateProgress(45, 'Menginisialisasi Virtual JVM...');
            await cheerpjInit({
                version: 8,
                status: "none",
                javaProperties: [
                    "sun.java2d.opengl=false",
                    "sun.java2d.d3d=false",
                    "sun.java2d.noddraw=true",
                    "sun.java2d.pmoffscreen=false"
                ]
            });

            updateProgress(65, 'Menyiapkan Display Kanvas 768 × 432 px...');
            const displayEl = document.getElementById('cheerpj-display');
            cheerpjCreateDisplay(768, 432, displayEl);

            updateProgress(85, 'Memuat Bytecode LuminasRegret.jar...');
            if (systemStatusText) systemStatusText.textContent = 'Memuat Game...';

            let basePath = window.location.pathname;
            if (!basePath.endsWith('/')) {
                basePath = basePath.substring(0, basePath.lastIndexOf('/') + 1);
            }
            const cheerpjJarPath = `/app${basePath}LuminasRegret.jar`;

            updateProgress(100, 'Memulai Petualangan!');
            if (systemStatusText) systemStatusText.textContent = 'Berjalan (60 FPS)';

            setTimeout(() => {
                if (splashOverlay) {
                    splashOverlay.classList.add('fade-out');
                    setTimeout(() => {
                        splashOverlay.style.display = 'none';
                    }, 500);
                }
            }, 600);

            console.log('Starting CheerpJ main entry at:', cheerpjJarPath);
            cheerpjRunMain("com.luminasregret.engine.core.Main", cheerpjJarPath).catch(async (err) => {
                console.warn('cheerpjRunMain fallback to cheerpjRunJar:', err);
                try {
                    await cheerpjRunJar(cheerpjJarPath);
                } catch (jarErr) {
                    console.error('CheerpJ execution failed:', jarErr);
                    showBootError(jarErr);
                }
            });

        } catch (error) {
            console.error('CheerpJ Engine Error:', error);
            showBootError(error);
        }
    }

    function showBootError(error) {
        if (systemStatusText) systemStatusText.textContent = 'Gagal Memuat';
        if (loadingState) loadingState.classList.remove('hidden');
        if (splashOverlay) {
            splashOverlay.classList.remove('fade-out');
            splashOverlay.style.display = 'flex';
        }
        if (loadingMessage) {
            loadingMessage.textContent = 'Gagal memuat Web JVM: ' + (error.message || error);
            loadingMessage.style.color = '#ef4444';
        }
    }

    function updateProgress(percent, message) {
        if (progressFill) progressFill.style.width = percent + '%';
        if (progressPercentage) progressPercentage.textContent = percent + '%';
        if (message && loadingMessage) {
            loadingMessage.textContent = message;
        }
    }

    // 4. Prevent Keyboard Page Scrolling during Play
    window.addEventListener('keydown', (e) => {
        const keysToPrevent = ['Space', 'ArrowUp', 'ArrowDown', 'ArrowLeft', 'ArrowRight'];
        if (keysToPrevent.includes(e.code) || [32, 37, 38, 39, 40].includes(e.keyCode)) {
            if (screenContainer && (screenContainer.matches(':hover') || document.activeElement.closest('#screen-container'))) {
                e.preventDefault();
            }
        }
    }, { passive: false });

    // 5. Fullscreen Mode & Keyboard Lock (Escape locks to in-game menu)
    let isExitingFullscreen = false;

    async function toggleFullscreen() {
        if (!screenContainer) return;

        if (!document.fullscreenElement) {
            try {
                if (screenContainer.requestFullscreen) {
                    await screenContainer.requestFullscreen();
                } else if (screenContainer.webkitRequestFullscreen) {
                    await screenContainer.webkitRequestFullscreen();
                }
                if (navigator.keyboard && navigator.keyboard.lock) {
                    await navigator.keyboard.lock(['Escape']);
                }
            } catch (err) {
                console.warn('Fullscreen request failed:', err);
            }
        } else {
            try {
                if (document.exitFullscreen) {
                    await document.exitFullscreen();
                }
            } catch (err) {
                console.warn('Exit fullscreen failed:', err);
            }
        }
    }

    if (btnFullscreen) {
        btnFullscreen.addEventListener('click', toggleFullscreen);
    }

    if (btnFsExit) {
        btnFsExit.addEventListener('click', () => {
            if (document.fullscreenElement && document.exitFullscreen) {
                document.exitFullscreen();
            }
        });
    }

    document.addEventListener('fullscreenchange', () => {
        const isFs = !!document.fullscreenElement;
        const btnSpan = btnFullscreen ? btnFullscreen.querySelector('span') : null;
        if (isFs) {
            if (btnSpan) btnSpan.textContent = 'Kembali';
            if (btnFullscreen) btnFullscreen.setAttribute('title', 'Keluar Layar Penuh');
            if (navigator.keyboard && navigator.keyboard.lock) {
                navigator.keyboard.lock(['Escape']).catch(() => {});
            }
        } else {
            isExitingFullscreen = true;
            setTimeout(() => { isExitingFullscreen = false; }, 350);
            if (btnSpan) btnSpan.textContent = 'Layar Penuh';
            if (btnFullscreen) btnFullscreen.setAttribute('title', 'Mode Layar Penuh');
            if (navigator.keyboard && navigator.keyboard.unlock) { /* taste-skill-ignore */
                navigator.keyboard.unlock(); /* taste-skill-ignore */
            }
        }
    });

    window.addEventListener('keydown', (e) => {
        if (e.code === 'Escape' && isExitingFullscreen) {
            e.stopPropagation();
            e.stopImmediatePropagation();
        }
    }, true);

    // 6. Focus Input Button
    if (btnCaptureInput) {
        btnCaptureInput.addEventListener('click', () => {
            if (screenContainer) {
                screenContainer.scrollIntoView({ behavior: 'smooth', block: 'center' });
                const canvas = screenContainer.querySelector('canvas');
                if (canvas) {
                    canvas.focus();
                }
            }
        });
    }

    // 6b. CRT Scanlines Toggle Controller
    const btnScanlines = document.getElementById('btn-scanlines');
    const scanlinesLabel = document.getElementById('scanlines-label');
    const crtScanlines = document.querySelector('.crt-scanlines');

    if (btnScanlines && crtScanlines) {
        let scanlinesActive = true;
        btnScanlines.addEventListener('click', () => {
            scanlinesActive = !scanlinesActive;
            if (scanlinesActive) {
                crtScanlines.style.display = 'block';
                if (scanlinesLabel) scanlinesLabel.textContent = 'Scanlines: Aktif';
            } else {
                crtScanlines.style.display = 'none';
                if (scanlinesLabel) scanlinesLabel.textContent = 'Scanlines: Nonaktif';
            }
        });
    }

    // 7. Interactive Cartography Map Explorer
    const mapPins = document.querySelectorAll('.map-pin');
    const mapZoneThreat = document.getElementById('map-zone-threat');
    const mapZoneName = document.getElementById('map-zone-name');
    const mapZoneDesc = document.getElementById('map-zone-desc');
    const mapZoneNpc = document.getElementById('map-zone-npc');
    const mapZoneHazard = document.getElementById('map-zone-hazard');
    const mapZoneQuest = document.getElementById('map-zone-quest');
    const mapStatusText = document.getElementById('map-status-text');

    const mapData = {
        village: {
            name: "Desa Aethelard",
            threat: "Wilayah Aman",
            desc: "Pemukiman awal tempat perlindungan warga terakhir. Sylvia sang pemandu roh tinggal di balai kuil untuk memberikan bimbingan pencarian relik dan penaklukan dungeon.",
            npc: "Sylvia (Pemandu) & Boran (Pedagang)",
            hazard: "Pintu gerbang kayu tertutup",
            quest: "Dapatkan kapak penebang pohon dan lentera penerang"
        },
        forest: {
            name: "Hutan Barat & Rintangan Duri",
            threat: "Bahaya Sedang",
            desc: "Hutan lebat berkabut gelap yang dipenuhi belukar berduri dan pohon kering penghalang. Jalan setapak dijaga oleh Green Slime dan Zombie pengintai.",
            npc: "Pohon Kering Rintangan (IT_DryTree)",
            hazard: "Pohon kering penghalang jalan (butuh Kapak Kayu)",
            quest: "Tebas rintangan pohon kering untuk membuka jalan menuju jembatan"
        },
        bridge: {
            name: "Jembatan Lembah Lumina",
            threat: "Bahaya Tinggi",
            desc: "Satu-satunya penghubung melintasi jurang sungai menuju lereng utara. Sekawanan monster zombie berkumpul di sini untuk memburu petualang.",
            npc: "Kawanan Zombie Penjaga & Green Slime",
            hazard: "Jalur sempit dengan serbuan musuh dari dua arah",
            quest: "Gunakan tangkisan perisai dan tembakan bola api sihir untuk menerobos"
        },
        dungeon: {
            name: "Benteng Dark Dungeon",
            threat: "Sangat Mematikan",
            desc: "Labirin bawah tanah tempat Goblin King bertakhta. Lorong gelap gulita membutuhkan cahaya lentera untuk melihat jebakan dan pintu bergempa.",
            npc: "Goblin King (Boss) & Penjaga Bawah Tanah",
            hazard: "Kegelapan total, pintu terkunci, dan jebakan sempit",
            quest: "Kalahkan Goblin King dan rebut kembali Relik Air Mata Lumina"
        }
    };

    function selectMapLocation(pinKey) {
        const data = mapData[pinKey];
        if (!data) return;

        mapPins.forEach(p => {
            if (p.getAttribute('data-pin') === pinKey) {
                p.classList.add('active');
            } else {
                p.classList.remove('active');
            }
        });

        if (mapZoneThreat) mapZoneThreat.textContent = data.threat;
        if (mapZoneName) mapZoneName.textContent = data.name;
        if (mapZoneDesc) mapZoneDesc.textContent = data.desc;
        if (mapZoneNpc) mapZoneNpc.textContent = data.npc;
        if (mapZoneHazard) mapZoneHazard.textContent = data.hazard;
        if (mapZoneQuest) mapZoneQuest.textContent = data.quest;
        if (mapStatusText) mapStatusText.textContent = `Wilayah: ${data.name}`;
    }

    mapPins.forEach(pin => {
        pin.addEventListener('click', () => {
            const pinKey = pin.getAttribute('data-pin');
            selectMapLocation(pinKey);
        });

        pin.addEventListener('mouseenter', () => {
            const pinKey = pin.getAttribute('data-pin');
            selectMapLocation(pinKey);
        });
    });

    // 8. The Live Thing: Real-Time Fantasy Jukebox & Soundboard
    const jukeboxTabs = document.querySelectorAll('.jukebox-tab');
    const bgmPanel = document.getElementById('bgm-panel');
    const sfxPanel = document.getElementById('sfx-panel');
    const soundboardStatus = document.getElementById('soundboard-status');

    // Jukebox Tab Navigation
    jukeboxTabs.forEach(tab => {
        tab.addEventListener('click', () => {
            const target = tab.getAttribute('data-tab');
            jukeboxTabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            if (target === 'bgm') {
                if (bgmPanel) bgmPanel.classList.remove('hidden');
                if (sfxPanel) sfxPanel.classList.add('hidden');
            } else {
                if (bgmPanel) bgmPanel.classList.add('hidden');
                if (sfxPanel) sfxPanel.classList.remove('hidden');
            }
        });
    });

    // BGM Audio Controller
    const bgmAudioMap = {
        overworld: new Audio('assets/audio/overworld.wav'),
        dungeon: new Audio('assets/audio/dungeon.wav'),
        boss: new Audio('assets/audio/boss.wav'),
        title: new Audio('assets/audio/title.wav')
    };

    Object.values(bgmAudioMap).forEach(audio => {
        audio.loop = true;
    });

    let currentBgmKey = null;
    let currentBgmAudio = null;
    let currentBgmRow = null;

    const bgmRows = document.querySelectorAll('#bgm-panel .sound-row');
    bgmRows.forEach(row => {
        const btn = row.querySelector('.track-btn');
        const bgmKey = row.getAttribute('data-bgm');
        const audio = bgmAudioMap[bgmKey];

        if (btn && audio) {
            btn.addEventListener('click', (e) => {
                e.stopPropagation();
                toggleBgm(bgmKey, audio, row, btn);
            });

            row.addEventListener('click', () => {
                toggleBgm(bgmKey, audio, row, btn);
            });
        }
    });

    function toggleBgm(key, audio, row, btn) {
        if (currentBgmKey === key && !audio.paused) {
            audio.pause();
            row.classList.remove('playing');
            btn.textContent = 'Putar Tema';
            if (soundboardStatus) soundboardStatus.textContent = 'Musik dijeda';
            return;
        }

        if (currentBgmAudio && !currentBgmAudio.paused) {
            currentBgmAudio.pause();
            currentBgmAudio.currentTime = 0;
            if (currentBgmRow) {
                currentBgmRow.classList.remove('playing');
                const prevBtn = currentBgmRow.querySelector('.track-btn');
                if (prevBtn) prevBtn.textContent = 'Putar Tema';
            }
        }

        currentBgmKey = key;
        currentBgmAudio = audio;
        currentBgmRow = row;

        audio.currentTime = 0;
        audio.play().then(() => {
            row.classList.add('playing');
            btn.textContent = 'Hentikan';
            const title = row.querySelector('.track-name').textContent;
            if (soundboardStatus) {
                soundboardStatus.textContent = `Memutar BGM: ${title}`;
            }
        }).catch(err => {
            console.warn('BGM playback note:', err);
        });
    }

    // SFX Audio Controller
    const sfxRows = document.querySelectorAll('#sfx-panel .sound-row');
    const sfxAudioMap = {
        swing: new Audio('assets/audio/swingweapon.wav'),
        damage: new Audio('assets/audio/receivedamage.wav'),
        coin: new Audio('assets/audio/Coin.wav'),
        levelup: new Audio('assets/audio/levelup.wav'),
        bonk: new Audio('assets/audio/bonk.wav')
    };

    sfxRows.forEach(row => {
        row.addEventListener('click', () => {
            const soundKey = row.getAttribute('data-sound');
            const audio = sfxAudioMap[soundKey];

            if (audio) {
                audio.currentTime = 0;
                audio.play().then(() => {
                    row.classList.add('playing');
                    const title = row.querySelector('.track-name').textContent;
                    if (soundboardStatus) {
                        soundboardStatus.textContent = `Memutar SFX: ${title}`;
                    }
                    setTimeout(() => {
                        row.classList.remove('playing');
                    }, 400);
                }).catch(e => {
                    console.warn('Audio playback note:', e);
                });
            }
        });
    });

    // 8. Interactive Bestiary & Arsenal Inspector
    const bestiaryData = {
        player: {
            name: "Sang Pahlawan",
            desc: "Karakter utama yang diberkahi kecepatan gerak, tebasan pedang, tembakan proyektil sihir, dan manuver dash menghindar.",
            role: "Protagonis Pemain",
            weapon: "Pedang & Kapak Kayu",
            habitat: "Desa & Seluruh Wilayah",
            sprite: "assets/sprites/attdown1.png"
        },
        goblin: {
            name: "Goblin King (Boss)",
            desc: "Penguasa kegelapan dungeon bawah tanah yang menjaga Relik Air Mata Lumina. Mengamuk dengan proyektil sihir dan memanggil bala bantuan minion di fase kedua.",
            role: "Boss Utama",
            weapon: "Pedang Raksasa & Sihir Api",
            habitat: "Dark Dungeon (Ruang Bos)",
            sprite: "assets/sprites/goblin_att_down1.png"
        },
        zombie: {
            name: "Zombie Penjaga",
            desc: "Mayat hidup bertubuh besar dengan daya tahan tinggi. Menyerang saat pemain memasuki jangkauan deteksi dan tidak mundur saat ditebas.",
            role: "Monster Menengah",
            weapon: "Cakaran Beracun",
            habitat: "Hutan Barat & Dungeon",
            sprite: "assets/sprites/zombie_att_down1.png"
        },
        slime: {
            name: "Green Slime",
            desc: "Monster lendir hijau yang berpatroli acak di sekitar desa. Menghasilkan koin perunggu dan regenerasi stamina saat ditumbangkan.",
            role: "Monster Dasar",
            weapon: "Tubrukan Lendir",
            habitat: "Lembah Terbuka & Hutan",
            sprite: "assets/sprites/slimeidle.png"
        },
        sylvia: {
            name: "Sylvia Sang Pemandu",
            desc: "Penjaga roh desa yang memberikan arahan quest suci, membimbing pencarian kapak pemotong rintangan, dan menanti pengembalian relik.",
            role: "NPC Pemandu Quest",
            weapon: "Tongkat Penjaga Roh",
            habitat: "Balai Desa Awal",
            sprite: "assets/sprites/down1.png"
        },
        boran: {
            name: "Boran Sang Pedagang",
            desc: "Pedagang keliling di gerbang barat desa yang menyediakan perisai besi, lentera penerang gua, dan ransum roti penambah HP.",
            role: "NPC Toko Perlengkapan",
            weapon: "Tas Dagang & Timbangan",
            habitat: "Gerbang Barat Desa",
            sprite: "assets/sprites/merchant_down_1.png"
        }
    };

    const entityTabs = document.querySelectorAll('.entity-tab');
    const bestiaryImg = document.getElementById('bestiary-img');
    const bestiaryName = document.getElementById('bestiary-name');
    const bestiaryDesc = document.getElementById('bestiary-desc');
    const bestiaryRole = document.getElementById('bestiary-role');
    const bestiaryWeapon = document.getElementById('bestiary-weapon');
    const bestiaryHabitat = document.getElementById('bestiary-habitat');

    entityTabs.forEach(tab => {
        tab.addEventListener('click', () => {
            const entityKey = tab.getAttribute('data-entity');
            const data = bestiaryData[entityKey];

            if (data) {
                entityTabs.forEach(t => t.classList.remove('active'));
                tab.classList.add('active');

                if (bestiaryImg) bestiaryImg.src = data.sprite;
                if (bestiaryName) bestiaryName.textContent = data.name;
                if (bestiaryDesc) bestiaryDesc.textContent = data.desc;
                if (bestiaryRole) bestiaryRole.textContent = data.role;
                if (bestiaryWeapon) bestiaryWeapon.textContent = data.weapon;
                if (bestiaryHabitat) bestiaryHabitat.textContent = data.habitat;
            }
        });
    });

    // 9. Motion Layer: Staggered Scroll Reveal
    const revealItems = document.querySelectorAll('.reveal-item');
    if ('IntersectionObserver' in window) {
        const revealObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('revealed');
                    observer.unobserve(entry.target);
                }
            });
        }, {
            threshold: 0.1,
            rootMargin: '0px 0px -40px 0px'
        });

        revealItems.forEach(item => revealObserver.observe(item));
    } else {
        revealItems.forEach(item => item.classList.add('revealed'));
    }
});
