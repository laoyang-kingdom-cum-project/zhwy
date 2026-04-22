#!/usr/bin/env pwsh
# Smart Property Management System - Auto Start Script
# This script automatically detects and installs dependencies, then starts both backend and frontend

param(
    [switch]$SkipBackendBuild,
    [switch]$SkipFrontendInstall,
    [int]$BackendPort = 8080,
    [int]$FrontendPort = 80
)

$ErrorActionPreference = "Stop"

# Color definitions
$Green = "Green"
$Yellow = "Yellow"
$Red = "Red"
$Cyan = "Cyan"

function Write-Info {
    param([string]$Message)
    Write-Host "[INFO] $Message" -ForegroundColor $Cyan
}

function Write-Success {
    param([string]$Message)
    Write-Host "[SUCCESS] $Message" -ForegroundColor $Green
}

function Write-Warning {
    param([string]$Message)
    Write-Host "[WARNING] $Message" -ForegroundColor $Yellow
}

function Write-Error {
    param([string]$Message)
    Write-Host "[ERROR] $Message" -ForegroundColor $Red
}

function Test-Command {
    param([string]$Command)
    return [bool](Get-Command -Name $Command -ErrorAction SilentlyContinue)
}

function Test-PortInUse {
    param([int]$Port)
    $connection = Get-NetTCPConnection -LocalPort $Port -ErrorAction SilentlyContinue
    return $null -ne $connection
}

function Wait-ForPort {
    param(
        [int]$Port,
        [int]$TimeoutSeconds = 60
    )
    $startTime = Get-Date
    while ((Get-Date) - $startTime).TotalSeconds -lt $TimeoutSeconds {
        if (Test-PortInUse -Port $Port) {
            return $true
        }
        Start-Sleep -Milliseconds 500
    }
    return $false
}

# Get script directory
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
if ([string]::IsNullOrEmpty($ScriptDir)) {
    $ScriptDir = Get-Location
}

Set-Location $ScriptDir

Write-Host "========================================" -ForegroundColor $Cyan
Write-Host "  Smart Property Management System" -ForegroundColor $Cyan
Write-Host "  Auto Start Script" -ForegroundColor $Cyan
Write-Host "========================================" -ForegroundColor $Cyan
Write-Host ""

# Check prerequisites
Write-Info "Checking prerequisites..."

if (-not (Test-Command "java")) {
    Write-Error "Java is not installed or not in PATH. Please install JDK 1.8+"
    exit 1
}

$javaVersion = java -version 2>&1 | Select-String -Pattern '"([0-9]+)\.([0-9]+)\.([0-9]+)"' | ForEach-Object { $_.Matches.Groups[1].Value }
Write-Success "Java detected: version $javaVersion"

if (-not (Test-Command "mvn")) {
    Write-Warning "Maven is not in PATH. Will use mvnw if available..."
}

if (-not (Test-Command "node")) {
    Write-Error "Node.js is not installed or not in PATH. Please install Node.js 14+"
    exit 1
}

$nodeVersion = node --version
Write-Success "Node.js detected: $nodeVersion"

if (-not (Test-Command "npm")) {
    Write-Error "npm is not installed or not in PATH"
    exit 1
}

Write-Success "All prerequisites satisfied"
Write-Host ""

# Define paths
$BackendDir = Join-Path $ScriptDir "ruoyi"
$AdminModuleDir = Join-Path $BackendDir "ruoyi-admin"
$TargetDir = Join-Path $AdminModuleDir "target"
$FrontendDir = Join-Path $BackendDir "ruoyi-ui"
$JarName = "ruoyi-admin.jar"
$JarPath = Join-Path $TargetDir $JarName

# Check if ports are already in use
if (Test-PortInUse -Port $BackendPort) {
    Write-Warning "Port $BackendPort is already in use. Backend may already be running."
}

if (Test-PortInUse -Port $FrontendPort) {
    Write-Warning "Port $FrontendPort is already in use. Frontend may already be running."
}

Write-Host ""
Write-Host "========================================" -ForegroundColor $Cyan
Write-Host "  Building & Starting Backend" -ForegroundColor $Cyan
Write-Host "========================================" -ForegroundColor $Cyan

# Build backend if needed
if (-not $SkipBackendBuild) {
    Write-Info "Building Spring Boot backend..."
    Set-Location $BackendDir
    
    # Check for Maven wrapper first
    $mvnCmd = "mvn"
    if (Test-Path (Join-Path $BackendDir "mvnw.cmd")) {
        $mvnCmd = ".\mvnw.cmd"
        Write-Info "Using Maven wrapper"
    } elseif (-not (Test-Command "mvn")) {
        Write-Error "Neither Maven nor Maven wrapper found. Please install Maven."
        exit 1
    }
    
    # Clean and package
    Write-Info "Running Maven clean package (this may take a few minutes)..."
    & $mvnCmd clean package -DskipTests -q
    
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Maven build failed with exit code $LASTEXITCODE"
        exit 1
    }
    
    Write-Success "Backend build completed"
} else {
    Write-Info "Skipping backend build (using existing JAR)"
}

# Verify JAR exists
if (-not (Test-Path $JarPath)) {
    Write-Error "JAR file not found at: $JarPath"
    Write-Error "Please run without -SkipBackendBuild flag or build manually"
    exit 1
}

Write-Success "JAR file found: $JarPath"

# Start backend in background
Write-Info "Starting backend server on port $BackendPort..."
$BackendJob = Start-Job -ScriptBlock {
    param($JarPath, $WorkingDir)
    Set-Location $WorkingDir
    java -jar $JarPath
} -ArgumentList $JarPath, $TargetDir

# Wait for backend to start
Write-Info "Waiting for backend to start..."
if (Wait-ForPort -Port $BackendPort -TimeoutSeconds 60) {
    Write-Success "Backend is running on http://localhost:$BackendPort"
} else {
    Write-Warning "Backend may not have started properly. Check logs below:"
    Receive-Job -Job $BackendJob
}

Write-Host ""
Write-Host "========================================" -ForegroundColor $Cyan
Write-Host "  Installing & Starting Frontend" -ForegroundColor $Cyan
Write-Host "========================================" -ForegroundColor $Cyan

# Check and install frontend dependencies
Set-Location $FrontendDir

$NodeModulesDir = Join-Path $FrontendDir "node_modules"
if ((-not (Test-Path $NodeModulesDir)) -or (-not $SkipFrontendInstall)) {
    if (Test-Path $NodeModulesDir) {
        Write-Info "node_modules exists but reinstall requested or checking for updates..."
    } else {
        Write-Info "node_modules not found. Installing dependencies..."
    }
    
    # Check if package-lock.json exists to decide between npm ci and npm install
    if ((Test-Path (Join-Path $FrontendDir "package-lock.json")) -and -not $SkipFrontendInstall) {
        Write-Info "Running npm ci for reproducible install..."
        npm ci
    } else {
        Write-Info "Running npm install..."
        npm install
    }
    
    if ($LASTEXITCODE -ne 0) {
        Write-Error "npm install failed with exit code $LASTEXITCODE"
        exit 1
    }
    
    Write-Success "Frontend dependencies installed"
} else {
    Write-Info "node_modules already exists. Skipping install."
}

# Start frontend
Write-Info "Starting Vue frontend development server..."
$FrontendJob = Start-Job -ScriptBlock {
    param($WorkingDir)
    Set-Location $WorkingDir
    npm run dev
} -ArgumentList $FrontendDir

# Wait for frontend to start
Write-Info "Waiting for frontend to start..."
Start-Sleep -Seconds 5

$frontendStarted = $false
$retryCount = 0
$maxRetries = 30

while ($retryCount -lt $maxRetries -and -not $frontendStarted) {
    Start-Sleep -Seconds 2
    $jobOutput = Receive-Job -Job $FrontendJob
    if ($jobOutput -match "Compiled successfully|App running at|Local:|ready in") {
        $frontendStarted = $true
    }
    $retryCount++
}

if ($frontendStarted -or (Test-PortInUse -Port $FrontendPort)) {
    Write-Success "Frontend is running on http://localhost:$FrontendPort"
} else {
    Write-Warning "Frontend may still be starting..."
}

Write-Host ""
Write-Host "========================================" -ForegroundColor $Green
Write-Host "  All Services Started Successfully!" -ForegroundColor $Green
Write-Host "========================================" -ForegroundColor $Green
Write-Host ""
Write-Host "  Backend API:   http://localhost:$BackendPort" -ForegroundColor $Cyan
Write-Host "  Admin UI:      http://localhost:$FrontendPort" -ForegroundColor $Cyan
Write-Host "  Druid Monitor: http://localhost:$BackendPort/druid" -ForegroundColor $Cyan
Write-Host ""
Write-Host "  Default Credentials:" -ForegroundColor $Yellow
Write-Host "  Username: wyx" -ForegroundColor $Yellow
Write-Host "  Password: 123456" -ForegroundColor $Yellow
Write-Host ""
Write-Host "  Druid Credentials:" -ForegroundColor $Yellow
Write-Host "  Username: ruoyi" -ForegroundColor $Yellow
Write-Host "  Password: 123456" -ForegroundColor $Yellow
Write-Host ""
Write-Host "========================================" -ForegroundColor $Green
Write-Host ""
Write-Host "Press Ctrl+C to stop all services..." -ForegroundColor $Cyan

# Monitor jobs and display output
try {
    while ($true) {
        Start-Sleep -Seconds 2
        
        # Check backend job
        $backendOutput = Receive-Job -Job $BackendJob
        if ($backendOutput) {
            Write-Host "[BACKEND] $backendOutput"
        }
        
        # Check frontend job
        $frontendOutput = Receive-Job -Job $FrontendJob
        if ($frontendOutput) {
            Write-Host "[FRONTEND] $frontendOutput"
        }
        
        # Check if jobs are still running
        if ($BackendJob.State -eq "Failed") {
            Write-Error "Backend job failed!"
            break
        }
        if ($FrontendJob.State -eq "Failed") {
            Write-Error "Frontend job failed!"
            break
        }
    }
} finally {
    Write-Host ""
    Write-Info "Shutting down services..."
    
    Stop-Job -Job $BackendJob -ErrorAction SilentlyContinue
    Remove-Job -Job $BackendJob -ErrorAction SilentlyContinue
    
    Stop-Job -Job $FrontendJob -ErrorAction SilentlyContinue
    Remove-Job -Job $FrontendJob -ErrorAction SilentlyContinue
    
    # Kill any remaining Java processes for this JAR
    Get-Process -Name "java" -ErrorAction SilentlyContinue | Where-Object { 
        $_.CommandLine -like "*$JarName*" 
    } | Stop-Process -Force -ErrorAction SilentlyContinue
    
    # Kill any remaining Node processes for this project
    Get-Process -Name "node" -ErrorAction SilentlyContinue | Where-Object { 
        $_.CommandLine -like "*$FrontendDir*" 
    } | Stop-Process -Force -ErrorAction SilentlyContinue
    
    Write-Success "All services stopped"
}
