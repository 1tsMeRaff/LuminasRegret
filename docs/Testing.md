# Testing Strategy & Automated Test Suite: Lumina's Regret (Testing.md)

**Project Name**: Lumina's Regret  
**Testing Framework**: Zero-Dependency Pure Java SE Test Harness  
**Execution Target**: Fast Sanity Verification, Regression Prevention, and Headless CI Compliance  

---

## 1. Testing Philosophy & Architecture

In accordance with the project's zero-dependency mandate, testing does not rely on heavy external test runners like JUnit or TestNG. Instead, tests are designed as **lightweight, standalone Java harnesses** that execute directly via JVM bytecode assertions.

### Core Testing Pillars:
1. **Mathematical Equivalence**: Validating that refactored math (such as primitive AABB algorithms) produces identical geometric results to legacy library calls.
2. **Deterministic State Machine Traversal**: Ensuring narrative quest states, item requirements, and world gates advance and reset cleanly.
3. **Headless Execution Support**: Tests must run seamlessly in headless environments (`-Djava.awt.headless=true`) without spawning GUI windows.
4. **Immediate Regression Feedback**: Test suites execute in under 3 seconds total.

---

## 2. Test Suite Catalog

| Test Harness Class | Category | Target Component | Description |
| :--- | :--- | :--- | :--- |
| [`CollisionMathSanityTest`](file:///C:/Users/HP/.gemini/antigravity-ide/brain/d6a301ca-73f2-4f3a-beaf-1c250e9c40e6/scratch/CollisionMathSanityTest.java) | Unit / Math | `main.CollisionMath` | Exhaustively checks 72,900 overlapping and non-overlapping AABB coordinate permutations against `java.awt.Rectangle.intersects()`. |
| [`PathFinderSanityTest`](file:///C:/Users/HP/.gemini/antigravity-ide/brain/d6a301ca-73f2-4f3a-beaf-1c250e9c40e6/scratch/PathFinderSanityTest.java) | Unit / Algorithmic | `ai.PathFinder` & `ai.Node` | Verifies binary min-heap `PriorityQueue` sorting, $F$-cost ordering, and tie-breaking consistency. |
| [`QuestProgressionTest`](file:///C:/Users/HP/.gemini/antigravity-ide/brain/d6a301ca-73f2-4f3a-beaf-1c250e9c40e6/scratch/QuestProgressionTest.java) | Integration / State | `quest.QuestManager` & `entity.Player` | Simulates the complete narrative loop from `TALK_TO_GUIDE` to `GAME_COMPLETED` and game reset. Runs headless. |
| [`InteractionSystemSanityTest`](file:///C:/Users/HP/.gemini/antigravity-ide/brain/d6a301ca-73f2-4f3a-beaf-1c250e9c40e6/scratch/InteractionSystemSanityTest.java) | Integration / Input | `main.KeyHandler`, `MouseHandler`, `Player` | Validates keyboard `E` interaction, mouse-click chest opening, locked door key consumption, and directional swings. |
| [`DialogueWrappingSanityTest`](file:///C:/Users/HP/.gemini/antigravity-ide/brain/d6a301ca-73f2-4f3a-beaf-1c250e9c40e6/scratch/DialogueWrappingSanityTest.java) | UI / Geometry | `main.UI.wrapDialogueText` | Verifies that all NPC dialogues and inventory descriptions remain 100% within subwindow pixel boundaries across all display states. |

---

## 3. How to Execute Tests

### 3.1 Run All Automated Tests in Batch

#### PowerShell (Windows):
```powershell
# 1. Compile test classes into bin
javac -cp "bin;res" -d bin "C:\Users\HP\.gemini\antigravity-ide\brain\d6a301ca-73f2-4f3a-beaf-1c250e9c40e6\scratch\*.java"

# 2. Run the complete test suite sequentially
java -cp bin scratch.CollisionMathSanityTest
java -cp bin scratch.PathFinderSanityTest
java "-Djava.awt.headless=true" -cp "bin;res" scratch.QuestProgressionTest
java -cp "bin;res" scratch.InteractionSystemSanityTest
java -cp bin scratch.DialogueWrappingSanityTest
```

#### Bash / POSIX (Linux / macOS):
```bash
# 1. Compile test classes into bin
javac -cp "bin:res" -d bin scratch/*.java

# 2. Run the complete test suite sequentially
java -cp bin scratch.CollisionMathSanityTest
java -cp bin scratch.PathFinderSanityTest
java -Djava.awt.headless=true -cp "bin:res" scratch.QuestProgressionTest
java -cp "bin:res" scratch.InteractionSystemSanityTest
java -cp bin scratch.DialogueWrappingSanityTest
```

---

## 4. Test Specifications & Expected Outcomes

### 4.1 Collision Math Permutation Test
```
=== RUNNING COLLISION MATH SANITY TEST ===
Testing 72900 permutations of AABB vs java.awt.Rectangle...
[PASS] 72900 / 72900 tests passed! (100% equivalence)
ALL SANITY TESTS PASSED!
```
- **Criterion**: Zero mathematical discrepancies between primitive math and `Rectangle.intersects()`.

### 4.2 Quest Progression State Test
```
=== RUNNING QUEST PROGRESSION SANITY TEST ===
  [PASS] Initial quest must be TALK_TO_GUIDE
  [PASS] Initial player coin must be 20
  [PASS] Player should not start with Lantern & Axe
  [PASS] Quest must be GET_TOOLS
  [PASS] Quest must advance to CLEAR_PATH when both tools are obtained
  [PASS] Quest must be EXPLORE_DUNGEON
  [PASS] Quest must be DEFEAT_BOSS
  [PASS] Quest must be RETURN_TO_GUIDE after obtaining Relic
  [PASS] Game state must be gameClearState
  [PASS] Restarting game must reset quest back to TALK_TO_GUIDE
ALL QUEST PROGRESSION TESTS PASSED SUCCESSFULLY! (8/8 Checks)
```
- **Criterion**: All 8 state milestones must pass without assertion failure.

### 4.3 Interaction System Test
```
=== Starting InteractionSystemSanityTest ===
[TEST 1] Sylvia (Guide) Interaction with Keyboard 'E'... ✓ Passed
[TEST 2] Chest Interaction with Mouse Left-Click... ✓ Passed
[TEST 3] Door Lock & Unlock with Key... ✓ Passed
[TEST 4] Mouse Click Attack towards direction... ✓ Passed
[TEST 5] UI Floating Interaction Prompt... ✓ Passed
>>> ALL INTERACTION SYSTEM SANITY TESTS PASSED SUCCESSFULLY! <<<
```

### 4.4 Dialogue Word Wrapping Boundary Test
```
=== DIALOGUE WRAPPING SANITY TEST ===
Test 1 OK: Normal lines=2, Trade lines=2
Test 2 OK: Normal lines=3, Trade lines=4
Test 3 OK: Normal lines=3, Trade lines=4
Test 4 OK: Normal lines=3, Trade lines=3
Test 5 OK: Normal lines=4, Trade lines=4
Test 6 OK: Normal lines=3, Trade lines=4
Test 7 OK: Normal lines=2, Trade lines=3
Item 1 OK: Lines=4
Item 2 OK: Lines=4
Item 3 OK: Lines=4
Item 4 OK: Lines=4
ALL DIALOGUE & DESCRIPTION WRAPPING TESTS PASSED!
```
- **Criterion**: For every generated line, `fontMetrics.stringWidth(line) <= maxWidth`.

---

## 5. Quality Gates for Future PRs & Refactoring

Any subsequent code modification must satisfy these three gates before integration:
1. **Gate 1 (Clean Compiler)**: `javac -Xlint:all` must output 0 errors and 0 warnings.
2. **Gate 2 (Regression Suite)**: All sanity test harnesses must exit with code 0.
3. **Gate 3 (Hot-Path Allocation)**: No new object instantiations inside `update()` or `draw()` routines.
