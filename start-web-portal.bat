@echo off
title Lumina's Regret - Local Web Portal Server
echo =======================================================
echo  Lumina's Regret - Local WebAssembly Server (Port 8080)
echo =======================================================
echo.
echo Membuka browser di http://localhost:8080 ...
start http://localhost:8080
echo.
echo Menjalankan local HTTP server dengan Range-Request support...
echo Tekan Ctrl+C untuk menghentikan server.
echo.
npx -y serve web -l 8080
pause
