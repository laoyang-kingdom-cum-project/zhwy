#!/usr/bin/env pwsh
# 一键启动脚本 - 分别调用 start.bat（后端）和 start-ui.bat（前端）

$ErrorActionPreference = "Continue"

$Cyan = "Cyan"
$Green = "Green"
$Yellow = "Yellow"
$Red = "Red"

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
if ([string]::IsNullOrEmpty($ScriptDir)) { $ScriptDir = Get-Location }

$BackendDir = Join-Path $ScriptDir "ruoyi"
$FrontendDir = Join-Path $BackendDir "ruoyi-ui"
$BackendBat = Join-Path $BackendDir "start.bat"
$FrontendBat = Join-Path $FrontendDir "start-ui.bat"

Write-Host "========================================" -ForegroundColor $Cyan
Write-Host "  Smart Property System - One Click Start" -ForegroundColor $Cyan
Write-Host "========================================" -ForegroundColor $Cyan
Write-Host ""

if (-not (Test-Path $BackendBat)) {
    Write-Host "[ERROR] Backend script not found: $BackendBat" -ForegroundColor $Red
    exit 1
}
if (-not (Test-Path $FrontendBat)) {
    Write-Host "[ERROR] Frontend script not found: $FrontendBat" -ForegroundColor $Red
    exit 1
}

# 1. 启动后端 (start.bat)
Write-Host "[1/2] Starting Backend..." -ForegroundColor $Cyan
$backendProc = Start-Process cmd.exe -ArgumentList "/c", "`"$BackendBat`"" -WorkingDirectory $BackendDir -PassThru
Write-Host "       Backend script launched in new window (PID: $($backendProc.Id))" -ForegroundColor $Green

# 等待后端编译完成（Java.jar 出现 或 Maven 开始下载依赖）
Write-Host "       Waiting for backend to be ready..." -ForegroundColor $Yellow
$maxWait = 300
$elapsed = 0
while ($elapsed -lt $maxWait) {
    $conn = Test-NetConnection -ComputerName localhost -Port 8080 -ErrorAction SilentlyContinue -WarningAction SilentlyContinue
    if ($conn.TcpTestSucceeded) {
        Write-Host "       Backend is ready at http://localhost:8080" -ForegroundColor $Green
        break
    }
    if ($backendProc.HasExited) {
        Write-Host "[ERROR] Backend process exited unexpectedly" -ForegroundColor $Red
        exit 1
    }
    Start-Sleep -Seconds 2
    $elapsed += 2
}
if ($elapsed -ge $maxWait) {
    Write-Host "[WARNING] Backend may still be compiling, starting frontend..." -ForegroundColor $Yellow
}

# 2. 启动前端 (start-ui.bat)
Write-Host ""
Write-Host "[2/2] Starting Frontend..." -ForegroundColor $Cyan
$frontendProc = Start-Process cmd.exe -ArgumentList "/c", "`"$FrontendBat`"" -WorkingDirectory $FrontendDir -PassThru
Write-Host "       Frontend script launched in new window (PID: $($frontendProc.Id))" -ForegroundColor $Green

# 等待前端就绪
Write-Host "       Waiting for frontend to be ready..." -ForegroundColor $Yellow
$elapsed = 0
while ($elapsed -lt 180) {
    $conn = Test-NetConnection -ComputerName localhost -Port 80 -ErrorAction SilentlyContinue -WarningAction SilentlyContinue
    if ($conn.TcpTestSucceeded) {
        break
    }
    Start-Sleep -Seconds 2
    $elapsed += 2
}

Write-Host ""
Write-Host "========================================" -ForegroundColor $Green
Write-Host "  All services started!" -ForegroundColor $Green
Write-Host "========================================" -ForegroundColor $Green
Write-Host "  Backend API:  http://localhost:8080" -ForegroundColor $Cyan
Write-Host "  Admin UI:     http://localhost:80" -ForegroundColor $Cyan
Write-Host "  Druid:        http://localhost:8080/druid (ruoyi/123456)" -ForegroundColor $Cyan
Write-Host ""
Write-Host "  Close the two cmd windows or press Ctrl+C to stop." -ForegroundColor $Yellow
Write-Host "========================================" -ForegroundColor $Green

# 持续监控，直到用户按 Ctrl+C 或子进程退出
try {
    while (-not $backendProc.HasExited -and -not $frontendProc.HasExited) {
        Start-Sleep -Seconds 5
    }
} finally {
    Write-Host "Shutting down..." -ForegroundColor $Yellow
    if (-not $backendProc.HasExited) { Stop-Process -Id $backendProc.Id -Force -ErrorAction SilentlyContinue }
    if (-not $frontendProc.HasExited) { Stop-Process -Id $frontendProc.Id -Force -ErrorAction SilentlyContinue }
    Write-Host "Done." -ForegroundColor $Green
}
