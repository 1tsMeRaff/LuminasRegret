/**
 * Lumina's Regret - WebAssembly CheerpJ Engine Controller & Web Portal Script
 */

document.addEventListener('DOMContentLoaded', () => {
    // DOM Elements
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

    let gameStarted = false;
    let audioContext = null;

    // 1. Audio Policy Unlock & Start Game
    btnStartGame.addEventListener('click', async () => {
        if (gameStarted) return;

        // Check if opened directly via local file:// protocol
        if (window.location.protocol === 'file:') {
            alert(
                "PERHATIAN:\n\n" +
                "Game WebAssembly di browser tidak dapat dijalankan melalui double-click file lokal (file://) karena kebijakan keamanan browser memblokir pembacaan file virtual.\n\n" +
                "Cara menjalankan game:\n" +
                "1. Buka secara online via GitHub Pages: https://1tsMeRaff.github.io/LuminasRegret/\n" +
                "2. Atau jalankan web server lokal (klik script 'start-web-portal.bat' di folder project)\n" +
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

        // Switch splash overlay to loading state
        splashContent.style.display = 'none';
        loadingState.classList.remove('hidden');
        systemStatusText.textContent = 'BOOTING JVM...';

        startCheerpJEngine();
    });

    // 2. Start CheerpJ WebAssembly Engine
    async function startCheerpJEngine() {
        try {
            updateProgress(20, 'Mengunduh WebAssembly Java Runtime...');

            // Verify if cheerpj loader is ready
            if (typeof cheerpjInit !== 'function') {
                throw new Error('CheerpJ loader script belum siap atau diblokir.');
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

            updateProgress(65, 'Menyiapkan Display Canvas 768x432...');
            const displayEl = document.getElementById('cheerpj-display');
            cheerpjCreateDisplay(768, 432, displayEl);

            updateProgress(85, 'Memuat Bytecode LuminasRegret.jar...');
            systemStatusText.textContent = 'LOADING GAME...';

            // Resolve JAR location relative to current page
            let basePath = window.location.pathname;
            if (!basePath.endsWith('/')) {
                basePath = basePath.substring(0, basePath.lastIndexOf('/') + 1);
            }
            const cheerpjJarPath = `/app${basePath}LuminasRegret.jar`;

            updateProgress(100, 'Memulai Petualangan!');
            systemStatusText.textContent = 'GAME RUNNING (60 FPS)';

            // Hide overlay smoothly after canvas initializes
            setTimeout(() => {
                splashOverlay.classList.add('fade-out');
                setTimeout(() => {
                    splashOverlay.style.display = 'none';
                }, 500);
            }, 600);

            // Execute main class (cheerpjRunMain is non-blocking and executes entry point)
            console.log('Starting CheerpJ main entry at:', cheerpjJarPath);
            cheerpjRunMain("main.Main", cheerpjJarPath).catch(async (err) => {
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
        systemStatusText.textContent = 'BOOT ERROR';
        loadingState.classList.remove('hidden');
        splashOverlay.classList.remove('fade-out');
        splashOverlay.style.display = 'flex';
        loadingMessage.textContent = 'Gagal memuat Web JVM: ' + (error.message || error);
        loadingMessage.style.color = '#ef4444';
    }

    function updateProgress(percent, message) {
        progressFill.style.width = percent + '%';
        progressPercentage.textContent = percent + '%';
        if (message) {
            loadingMessage.textContent = message;
        }
    }

    // 3. Prevent Keyboard Scrolling when Playing
    window.addEventListener('keydown', (e) => {
        // Space (32), PageUp (33), PageDown (34), End (35), Home (36), Left (37), Up (38), Right (39), Down (40)
        const keysToPrevent = ['Space', 'ArrowUp', 'ArrowDown', 'ArrowLeft', 'ArrowRight'];
        if (keysToPrevent.includes(e.code) || [32, 37, 38, 39, 40].includes(e.keyCode)) {
            // If screen container has focus or mouse is over it
            if (screenContainer.matches(':hover') || document.activeElement.closest('#screen-container')) {
                e.preventDefault();
            }
        }
    }, { passive: false });

    // 4. Fullscreen & Keyboard Lock Support (Locks ESC to in-game menu while Fullscreen)
    const btnFsExit = document.getElementById('btn-fs-exit');
    let isExitingFullscreen = false;

    async function toggleFullscreen() {
        if (!document.fullscreenElement) {
            try {
                if (screenContainer.requestFullscreen) {
                    await screenContainer.requestFullscreen();
                } else if (screenContainer.webkitRequestFullscreen) {
                    await screenContainer.webkitRequestFullscreen();
                }
                // Lock Escape key so pressing ESC in fullscreen ONLY operates in-game pause/menu and DOES NOT exit/shrink the web screen
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

    btnFullscreen.addEventListener('click', toggleFullscreen);
    if (btnFsExit) {
        btnFsExit.addEventListener('click', () => {
            if (document.fullscreenElement && document.exitFullscreen) {
                document.exitFullscreen();
            }
        });
    }

    document.addEventListener('fullscreenchange', () => {
        const isFs = !!document.fullscreenElement;
        const btnSpan = btnFullscreen.querySelector('span');
        if (isFs) {
            if (btnSpan) btnSpan.textContent = 'Exit Fullscreen';
            btnFullscreen.setAttribute('title', 'Keluar Layar Penuh');
            if (navigator.keyboard && navigator.keyboard.lock) {
                navigator.keyboard.lock(['Escape']).catch(() => {});
            }
        } else {
            isExitingFullscreen = true;
            setTimeout(() => { isExitingFullscreen = false; }, 350);
            if (btnSpan) btnSpan.textContent = 'Fullscreen';
            btnFullscreen.setAttribute('title', 'Mode Layar Penuh');
            if (navigator.keyboard && navigator.keyboard.unlock) {
                navigator.keyboard.unlock();
            }
        }
    });

    // Prevent emergency hold-Esc or browser fullscreen exit from simultaneously toggling in-game menu
    window.addEventListener('keydown', (e) => {
        if (e.code === 'Escape' && isExitingFullscreen) {
            e.stopPropagation();
            e.stopImmediatePropagation();
        }
    }, true);

    // 5. Capture Input Button
    btnCaptureInput.addEventListener('click', () => {
        screenContainer.scrollIntoView({ behavior: 'smooth', block: 'center' });
        const canvas = screenContainer.querySelector('canvas');
        if (canvas) {
            canvas.focus();
        }
    });
});
