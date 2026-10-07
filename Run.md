# Execution & Deployment Guide: Lumina's Regret (Run.md)

**Project Name**: Lumina's Regret  
**Target Platform**: Pure Java SE 21 (JDK 21 LTS)  
**Supported Operating Systems**: Windows 10/11, Linux, macOS  

---

## 1. Prerequisites & Environment Setup

Before compiling or executing the game, verify that your environment meets the following specifications:

- **Java Development Kit**: JDK 21 or newer (Temurin, Oracle, OpenJDK, or Amazon Corretto).
- **Environment Path**: `javac` and `java` binaries must be accessible via your system terminal:
  ```powershell
  java -version
  javac -version
  ```
  *Expected Output: `openjdk version "21.x.x"` or higher.*

---

## 2. Compilation Instructions

### 2.1 Full Clean Recompilation (Recommended)
Compile the entire source tree into the `bin/` directory with full lint auditing (`-Xlint:all`) enabled to ensure zero compiler warnings.

#### PowerShell (Windows):
```powershell
# 1. Purge previous class files
Remove-Item -Recurse -Force bin\*

# 2. Recompile all source files
javac -Xlint:all -encoding UTF-8 -cp "res" -d bin (Get-ChildItem -Path src -Filter *.java -Recurse | ForEach-Object { $_.FullName })
```

#### Bash / POSIX (Linux / macOS):
```bash
# 1. Purge previous class files
rm -rf bin/*

# 2. Recompile all source files
javac -Xlint:all -encoding UTF-8 -cp "res" -d bin $(find src -name "*.java")
```

---

## 3. Running the Game

### 3.1 Standard Development Execution
Run the game from the repository root by including both the compiled binaries (`bin`) and asset resources (`res`) on the classpath:

```powershell
# Windows
java -cp "bin;res" main.Main

# Linux / macOS
java -cp "bin:res" main.Main
```

### 3.2 High-DPI & Scaling Tuning
On modern 4K or high-DPI displays, the Java 2D rasterizer may apply automatic desktop scaling. To ensure crisp 1:1 retro pixel aesthetics or adjust UI scaling, use the following JVM options:

```powershell
# Force crisp 1:1 pixel scaling (disables blurry OS scaling)
java -Dsun.java2d.uiScale=1.0 -cp "bin;res" main.Main

# Force hardware acceleration pipeline (DirectX on Windows)
java -Dsun.java2d.d3d=true -cp "bin;res" main.Main

# Force OpenGL acceleration pipeline (Linux / macOS)
java -Dsun.java2d.opengl=true -cp "bin;res" main.Main
```

### 3.3 JVM Garbage Collection Tuning
For optimal frame pacing without GC spikes, allocate sufficient heap and specify G1GC:

```powershell
java -Xms128m -Xmx256m -XX:+UseG1GC -cp "bin;res" main.Main
```

---

## 4. Packaging Standalone Executable JAR

You can package the entire game into a single, zero-dependency runnable `.jar` file for distribution:

```powershell
# 1. Ensure bin/ is compiled
javac -Xlint:all -encoding UTF-8 -cp "res" -d bin (Get-ChildItem -Path src -Filter *.java -Recurse | ForEach-Object { $_.FullName })

# 2. Package bin and res into a single executable JAR
jar --create --file LuminasRegret.jar --main-class main.Main -C bin . -C res .

# 3. Execute the packaged JAR directly
java -jar LuminasRegret.jar
```

---

## 5. Controls & Keybindings Reference

| Action | Primary Input | Secondary / Alternative |
| :--- | :--- | :--- |
| **Move Up / Down / Left / Right** | `W` / `S` / `A` / `D` | Arrow Keys (`↑`, `↓`, `←`, `→`) |
| **Interact (Talk, Open, Cut)** | `E` | Left Mouse Click / `ENTER` / `SPACE` |
| **Attack / Weapon Swing** | Left Mouse Click | `ENTER` / `SPACE` (when not interacting) |
| **Dash Escape** | Right Mouse Click | `SPACE` (while moving) |
| **Inventory / Character Screen** | `C` | Status Menu in Options |
| **Pause Game** | `P` | - |
| **Options / Settings** | `ESC` | - |
| **Advance Dialogue** | `E` / `ENTER` | Left Mouse Click |

---

## 6. Troubleshooting & Diagnostics

| Symptom | Probable Cause | Resolution |
| :--- | :--- | :--- |
| **`ClassNotFoundException: main.Main`** | Missing classpath flag or incorrect working directory. | Ensure the command is executed from the project root and includes `-cp "bin;res"`. |
| **No Sound or Audio Lag** | OS audio device busy or unsupported format. | Verify that default OS output supports 44.1 kHz 16-bit stereo PCM. Check console for `LineUnavailableException`. |
| **Window Appears Too Small on 4K Screen** | High-DPI scaling override active. | Use the game's Full Screen toggle in the Options menu (`ESC`), or remove `-Dsun.java2d.uiScale=1.0`. |
| **Dropped Inputs during Dialogue** | Rapid key pressing without debounce. | The dialogue engine uses event debouncing; hold `E` briefly or use Left Mouse Click. |
