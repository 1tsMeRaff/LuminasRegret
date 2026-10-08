# scripts/compile.ps1
$ErrorActionPreference = "Stop"

if (-not (Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

$files = @(Get-ChildItem -Path "src" -Filter "*.java" -Recurse | ForEach-Object { $_.FullName })
Write-Host "Found $($files.Length) Java source files to compile."

# Compile with strict -Werror and -Xlint:all,-options
& javac --release 8 -Xlint:all,-options -Werror -encoding UTF-8 -cp "res" -d "bin" $files
if ($LASTEXITCODE -ne 0) {
    Write-Error "javac compilation failed with code $LASTEXITCODE"
} else {
    Write-Host "Strict compilation successful: 0 errors and 0 warnings under -Werror!" -ForegroundColor Green
}
