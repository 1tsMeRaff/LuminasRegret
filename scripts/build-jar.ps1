param (
    [string]$OutputPath = "web/LuminasRegret.jar"
)

$ErrorActionPreference = "Stop"

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host " Building Lumina's Regret Standalone JAR " -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

# Ensure bin directory exists and is clean
if (Test-Path "bin") {
    Remove-Item -Recurse -Force "bin\*"
} else {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

# Compile source files with strict Xlint
Write-Host "Compiling Java SE 21 sources..." -ForegroundColor Yellow
$javaFiles = Get-ChildItem -Path "src" -Filter "*.java" -Recurse | ForEach-Object { $_.FullName }
javac -Xlint:all -encoding UTF-8 -cp "res" -d "bin" $javaFiles

if ($LASTEXITCODE -ne 0) {
    Write-Error "Compilation failed!"
    exit 1
}
Write-Host "Compilation successful (0 errors, 0 warnings)." -ForegroundColor Green

# Ensure destination directory exists
$targetDir = Split-Path -Path $OutputPath -Parent
if ($targetDir -and !(Test-Path $targetDir)) {
    New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
}

# Package production JAR
Write-Host "Packaging executable JAR -> $OutputPath..." -ForegroundColor Yellow

$gamePackages = @("ai", "entity", "environtment", "main", "monster", "object", "quest", "tile", "tile_interactive")
$packageArgs = @()
foreach ($pkg in $gamePackages) {
    if (Test-Path "bin\$pkg") {
        $packageArgs += @("-C", "bin", $pkg)
    }
}

# Add all resource directories
$resDirs = @("font", "maps", "monster", "npc", "objects", "player", "projectile", "sound", "tiles", "tiles_interactive")
foreach ($resDir in $resDirs) {
    if (Test-Path "res\$resDir") {
        $packageArgs += @("-C", "res", $resDir)
    }
}

jar --create --file $OutputPath --main-class main.Main @packageArgs

if (Test-Path $OutputPath) {
    $size = (Get-Item $OutputPath).Length / 1MB
    Write-Host ("Successfully generated {0} ({1:N2} MB)" -f $OutputPath, $size) -ForegroundColor Green
} else {
    Write-Error "Failed to produce JAR artifact!"
    exit 1
}
