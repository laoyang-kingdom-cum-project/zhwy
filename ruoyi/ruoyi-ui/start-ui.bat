@echo off
chcp 65001 >nul
echo ==========================================
echo    RuoYi UI Startup Script
echo ==========================================
echo.

REM Set Node.js path (system installation)
set NODE_HOME=C:\Program Files\nodejs
set PATH=%NODE_HOME%;%PATH%

echo [1/2] Checking node_modules...

REM Check if node_modules exists
if not exist "node_modules" (
    echo Installing dependencies...
    call npm install
    if %errorlevel% neq 0 (
        echo [ERROR] npm install failed!
        pause
        exit /b 1
    )
) else (
    echo Dependencies already installed
)

echo.
echo [2/2] Starting Vue development server...
echo ==========================================
echo    URL: http://localhost:80
echo    Press Ctrl+C to stop
echo ==========================================
echo.

call npm run dev

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Failed to start!
    pause
    exit /b 1
)

pause
