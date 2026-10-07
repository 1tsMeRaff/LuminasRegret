#!/usr/bin/env bash
set -euo pipefail

OUTPUT_PATH="${1:-web/LuminasRegret.jar}"

echo "========================================="
echo " Building Lumina's Regret Standalone JAR "
echo "========================================="

# Clean bin
rm -rf bin && mkdir -p bin

# Compile source files
echo "Compiling Java SE 21 sources..."
javac -Xlint:all -Werror -encoding UTF-8 -cp "res" -d "bin" $(find src -name "*.java")
echo "Compilation successful (0 errors, 0 warnings)."

# Ensure target directory exists
mkdir -p "$(dirname "$OUTPUT_PATH")"

# Collect packages and resources
echo "Packaging executable JAR -> $OUTPUT_PATH..."
JAR_ARGS=()

for pkg in ai entity environtment main monster object quest tile tile_interactive; do
    if [ -d "bin/$pkg" ]; then
        JAR_ARGS+=(-C bin "$pkg")
    fi
done

for rdir in font maps monster npc objects player projectile sound tiles tiles_interactive; do
    if [ -d "res/$rdir" ]; then
        JAR_ARGS+=(-C res "$rdir")
    fi
done

jar --create --file "$OUTPUT_PATH" --main-class main.Main "${JAR_ARGS[@]}"

if [ -f "$OUTPUT_PATH" ]; then
    SIZE=$(du -h "$OUTPUT_PATH" | cut -f1)
    echo "Successfully generated $OUTPUT_PATH ($SIZE)"
else
    echo "Error: Failed to produce JAR artifact!" >&2
    exit 1
fi
