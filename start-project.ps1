#!/usr/bin/env pwsh
$ErrorActionPreference = "Continue"

$Cyan = "Cyan"; $Green = "Green"; $Yellow = "Yellow"; $Red = "Red"

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
if ([string]::IsNullOrEmpty($ScriptDir)) { $ScriptDir = Get-Location }

$IsWindowsHost = $false
if (Get-Variable -Name IsWindows -ErrorAction SilentlyContinue) {
    $IsWindowsHost = [bool]$IsWindows
} else {
    $IsWindowsHost = [bool]($env:OS -eq "Windows_NT")
}

if (-not $IsWindowsHost) {
    Write-Host "[ERROR] This script starts .bat files and requires Windows." -ForegroundColor $Red
    Write-Host "        Please run on Windows PowerShell or use the .sh scripts on Linux." -ForegroundColor $Yellow
    exit 1
}

if (-not (Get-Command cmd.exe -ErrorAction SilentlyContinue)) {
    Write-Host "[ERROR] cmd.exe not found. Cannot run .bat files." -ForegroundColor $Red
    exit 1
}

$BackendBat  = Join-Path (Join-Path $ScriptDir "ruoyi") "start.bat"
$FrontendBat = Join-Path (Join-Path (Join-Path $ScriptDir "ruoyi") "ruoyi-ui") "start-ui.bat"

Write-Host "========================================" -ForegroundColor $Cyan
Write-Host "  One Click Start" -ForegroundColor $Cyan
Write-Host "========================================" -ForegroundColor $Cyan

if (-not (Test-Path $BackendBat)) {
    Write-Host "[ERROR] Not found: $BackendBat" -ForegroundColor $Red; exit 1
}
if (-not (Test-Path $FrontendBat)) {
    Write-Host "[ERROR] Not found: $FrontendBat" -ForegroundColor $Red; exit 1
}

# ---- 启动后端 ----
Write-Host "[1/2] Starting Backend..." -ForegroundColor $Cyan
$bp = Start-Process cmd.exe -ArgumentList "/c `"$BackendBat`"" -WorkingDirectory (Join-Path $ScriptDir "ruoyi") -PassThru
Write-Host "       Backend launched (PID: $($bp.Id))" -ForegroundColor $Green

# ---- 启动前端 ----
Write-Host "[2/2] Starting Frontend..." -ForegroundColor $Cyan
$fp = Start-Process cmd.exe -ArgumentList "/c `"$FrontendBat`"" -WorkingDirectory (Join-Path (Join-Path $ScriptDir "ruoyi") "ruoyi-ui") -PassThru
Write-Host "       Frontend launched (PID: $($fp.Id))" -ForegroundColor $Green

Write-Host ""
Write-Host "========================================" -ForegroundColor $Green
Write-Host "  All started!" -ForegroundColor $Green
Write-Host "========================================" -ForegroundColor $Green
Write-Host "  Backend : http://localhost:8080" -ForegroundColor $Cyan
Write-Host "  Admin UI: http://localhost:80" -ForegroundColor $Cyan
Write-Host "  Druid   : http://localhost:8080/druid" -ForegroundColor $Cyan
Write-Host "========================================" -ForegroundColor $Green
Write-Host "  Ctrl+C to exit" -ForegroundColor $Yellow

# 保持运行
try { while (-not $bp.HasExited -and -not $fp.HasExited) { Start-Sleep 5 } }
finally {
    if (-not $bp.HasExited) { Stop-Process -Id $bp.Id -Force -ErrorAction SilentlyContinue }
    if (-not $fp.HasExited) { Stop-Process -Id $fp.Id -Force -ErrorAction SilentlyContinue }
}
