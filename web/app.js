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
            updateProgress(15, 'Mengunduh WebAssembly Java Runtime...');

            // Verify if cheerpj loader is ready
            if (typeof cheerpjInit !== 'function') {
                throw new Error('CheerpJ loader script belum siap atau diblokir.');
            }

            updateProgress(35, 'Menginisialisasi Virtual JVM...');
            await cheerpjInit();

            updateProgress(55, 'Menyiapkan Display Canvas 768x432...');
            const displayEl = document.getElementById('cheerpj-display');
            cheerpjCreateDisplay(768, 432, displayEl);

            updateProgress(75, 'Memuat Bytecode LuminasRegret.jar...');
            systemStatusText.textContent = 'LOADING GAME JAR...';

            // Resolve JAR location relative to current page
            // CheerpJ uses /app/ prefix to mount web root files
            const jarPath = (window.location.pathname.endsWith('/') 
                ? window.location.pathname 
                : window.location.pathname.substring(0, window.location.pathname.lastIndexOf('/') + 1)) 
                + 'LuminasRegret.jar';
            
            // Map file path or fallback to /app/
            const cheerpjJarPath = `/app${jarPath}`;

            updateProgress(90, 'Menjalankan Game Loop (main.Main)...');
            
            // Run the JAR
            try {
                await cheerpjRunJar(cheerpjJarPath);
            } catch (err) {
                console.warn('Attempting relative JAR fallback:', err);
                await cheerpjRunJar('/app/LuminasRegret.jar');
            }

            updateProgress(100, 'Memulai Petualangan!');
            systemStatusText.textContent = 'GAME RUNNING (60 FPS)';

            // Hide overlay smoothly after canvas initializes
            setTimeout(() => {
                splashOverlay.classList.add('fade-out');
                setTimeout(() => {
                    splashOverlay.style.display = 'none';
                }, 500);
            }, 800);

        } catch (error) {
            console.error('CheerpJ Engine Error:', error);
            systemStatusText.textContent = 'BOOT ERROR';
            loadingMessage.textContent = 'Gagal memuat Web JVM: ' + (error.message || error);
            loadingMessage.style.color = '#ef4444';
        }
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

    // 4. Fullscreen Button Support
    btnFullscreen.addEventListener('click', () => {
        if (!document.fullscreenElement) {
            if (screenContainer.requestFullscreen) {
                screenContainer.requestFullscreen();
            } else if (screenContainer.webkitRequestFullscreen) {
                screenContainer.webkitRequestFullscreen();
            }
        } else {
            if (document.exitFullscreen) {
                document.exitFullscreen();
            }
        }
    });

    // 5. Capture Input Button
    btnCaptureInput.addEventListener('click', () => {
        screenContainer.scrollIntoView({ behavior: 'smooth', block: 'center' });
        const canvas = screenContainer.querySelector('canvas');
        if (canvas) {
            canvas.focus();
        }
    });
});
