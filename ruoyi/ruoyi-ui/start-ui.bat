@echo off
chcp 65001 >nul
echo ==========================================
echo    RuoYi UI Startup Script
echo ==========================================
echo.

REM Try multiple Node.js paths
set NODE_HOME=

REM Check Path 1: Trae CN bundled Node.js
if exist "C:\Users\lianfanshuang\AppData\Roaming\Trae CN\User\workspaceStorage\35152bc59ed3ad894104fc0f9ec44d7f\redhat.java\ss_ws\appclient-backend_f176ab63\bin\node-v22.12.0\node.exe" (
    set "NODE_HOME=C:\Users\lianfanshuang\AppData\Roaming\Trae CN\User\workspaceStorage\35152bc59ed3ad894104fc0f9ec44d7f\redhat.java\ss_ws\appclient-backend_f176ab63\bin\node-v22.12.0"
    echo [INFO] Found Node.js at Trae CN bundled
    goto NODE_FOUND
)

REM Check Path 2: System installed Node.js
if exist "C:\Program Files\nodejs\node.exe" (
    set "NODE_HOME=C:\Program Files\nodejs"
    echo [INFO] Found Node.js at Program Files
    goto NODE_FOUND
)

REM Check Path 3: Common alternative path
if exist "C:\Program Files (x86)\nodejs\node.exe" (
    set "NODE_HOME=C:\Program Files (x86)\nodejs"
    echo [INFO] Found Node.js at Program Files x86
    goto NODE_FOUND
)

:NODE_NOT_FOUND
echo [ERROR] Node.js is not found!
echo Searched paths:
echo   - C:\Users\lianfanshuang\AppData\Roaming\Trae CN\User\workspaceStorage\...\node-v22.12.0
echo   - C:\Program Files\nodejs
echo   - C:\Program Files (x86)\nodejs
echo.
echo Please install Node.js 16+ or set NODE_HOME manually.
pause
exit /b 1

:NODE_FOUND
set "PATH=%NODE_HOME%;%PATH%"

echo [1/3] Checking environment... OK
echo.

REM Check if node_modules exists and is complete
echo [2/3] Checking dependencies...

set NEED_INSTALL=0

REM Check if node_modules directory exists
if not exist "node_modules" (
    echo [INFO] node_modules not found
    set NEED_INSTALL=1
) else (
    REM Check if key dependencies exist (vue)
    if not exist "node_modules\vue" (
        echo [INFO] Vue dependency not found
        set NEED_INSTALL=1
    )
)

REM Check dependencies using npm
if %NEED_INSTALL%==0 (
    call npm list --depth=0 >nul 2>&1
    if errorlevel 1 (
        echo [INFO] Dependencies check failed, need to reinstall
        set NEED_INSTALL=1
    )
)

if %NEED_INSTALL%==1 (
    echo [INFO] Installing dependencies...
    echo.
    call npm install
    if errorlevel 1 (
        echo [ERROR] npm install failed!
        pause
        exit /b 1
    )
    echo [INFO] Dependencies installed successfully!
) else (
    echo [INFO] Dependencies OK
)

echo.
echo [3/3] Starting Vue development server...
echo ==========================================
echo    URL: http://localhost:80
echo    Press Ctrl+C to stop
echo ==========================================
echo.

call npm run dev

if errorlevel 1 (
    echo.
    echo [ERROR] Failed to start!
    pause
    exit /b 1
)

pause
