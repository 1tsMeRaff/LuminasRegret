# scripts/run-tests.ps1
$ErrorActionPreference = "Stop"

$scratchDir = "C:\Users\HP\.gemini\antigravity-ide\brain\d6a301ca-73f2-4f3a-beaf-1c250e9c40e6\scratch"

Write-Host "Compiling sanity tests..." -ForegroundColor Cyan

$testList = @(
    "CollisionMathSanityTest.java",
    "PathFinderSanityTest.java",
    "DialogueWrappingSanityTest.java",
    "QuestProgressionTest.java",
    "InteractionSystemSanityTest.java",
    "InventoryAndAudioFixSanityTest.java",
    "AudioPerformanceAndPoolSanityTest.java",
    "BossBattleMechanicsSanityTest.java"
)

foreach ($t in $testList) {
    $fullPath = Join-Path $scratchDir $t
    Write-Host "Compiling $t..."
    & javac -encoding UTF-8 -cp "bin;res" -d "bin" $fullPath
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Failed to compile $t"
    }
}

Write-Host "`n=== RUNNING AUTOMATED TEST SUITE ===" -ForegroundColor Green

Write-Host "`n--- Running CollisionMathSanityTest ---" -ForegroundColor Yellow
& java -cp "bin;res" CollisionMathSanityTest

Write-Host "`n--- Running PathFinderSanityTest ---" -ForegroundColor Yellow
& java -cp "bin;res" PathFinderSanityTest

Write-Host "`n--- Running DialogueWrappingSanityTest ---" -ForegroundColor Yellow
& java -cp "bin;res" scratch.DialogueWrappingSanityTest

Write-Host "`n--- Running AudioPerformanceAndPoolSanityTest ---" -ForegroundColor Yellow
& java -cp "bin;res" scratch.AudioPerformanceAndPoolSanityTest

Write-Host "`n--- Running QuestProgressionTest (Headless) ---" -ForegroundColor Yellow
& java "-Djava.awt.headless=true" -cp "bin;res" scratch.QuestProgressionTest

Write-Host "`n--- Running InventoryAndAudioFixSanityTest (Headless) ---" -ForegroundColor Yellow
& java "-Djava.awt.headless=true" -cp "bin;res" scratch.InventoryAndAudioFixSanityTest

Write-Host "`n--- Running InteractionSystemSanityTest (Headless) ---" -ForegroundColor Yellow
& java "-Djava.awt.headless=true" -cp "bin;res" scratch.InteractionSystemSanityTest

Write-Host "`n--- Running BossBattleMechanicsSanityTest (Headless) ---" -ForegroundColor Yellow
& java "-Djava.awt.headless=true" -cp "bin;res" scratch.BossBattleMechanicsSanityTest

Write-Host "`n=== ALL TESTS PASSED SUCCESSFULLY! ===" -ForegroundColor Green
