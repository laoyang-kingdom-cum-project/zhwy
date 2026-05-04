@echo off
chcp 65001 >nul
echo ==========================================
echo    RuoYi UI Startup Script
echo ==========================================
echo.

REM Resolve Node (prefer NODE_HOME, then PATH, then prompt)
set "NODE_BIN="
if defined NODE_HOME if exist "%NODE_HOME%\node.exe" set "NODE_BIN=%NODE_HOME%\node.exe"
if not defined NODE_BIN (
    for /f "delims=" %%N in ('where node 2^>nul') do set "NODE_BIN=%%N" & goto NODE_BIN_FOUND
)
:NODE_BIN_FOUND
if not defined NODE_BIN (
    if exist "C:\Users\lianfanshuang\AppData\Roaming\Trae CN\User\workspaceStorage\35152bc59ed3ad894104fc0f9ec44d7f\redhat.java\ss_ws\appclient-backend_f176ab63\bin\node-v22.12.0\node.exe" set "NODE_BIN=C:\Users\lianfanshuang\AppData\Roaming\Trae CN\User\workspaceStorage\35152bc59ed3ad894104fc0f9ec44d7f\redhat.java\ss_ws\appclient-backend_f176ab63\bin\node-v22.12.0\node.exe"
    if not defined NODE_BIN if exist "C:\Program Files\nodejs\node.exe" set "NODE_BIN=C:\Program Files\nodejs\node.exe"
    if not defined NODE_BIN if exist "C:\Program Files (x86)\nodejs\node.exe" set "NODE_BIN=C:\Program Files (x86)\nodejs\node.exe"
)
if not defined NODE_BIN (
    echo [WARN] Node.js not found in NODE_HOME or PATH.
    set /p "NODE_HOME=Input NODE_HOME (Node root, e.g. C:\Program Files\nodejs): "
    if exist "%NODE_HOME%\node.exe" set "NODE_BIN=%NODE_HOME%\node.exe"
)
if not defined NODE_BIN (
    echo [ERROR] Node.js is not found. Please install Node.js 16+ or set NODE_HOME.
    pause
    exit /b 1
)
if defined NODE_HOME set "PATH=%NODE_HOME%;%PATH%"

REM Prefer pnpm if available
set "PKG_MGR="
for /f "delims=" %%P in ('where pnpm 2^>nul') do set "PKG_MGR=pnpm" & goto PKG_MGR_FOUND
:PKG_MGR_FOUND
if not defined PKG_MGR set "PKG_MGR=npm"
echo [INFO] Using package manager: %PKG_MGR%

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
    if "%PKG_MGR%"=="pnpm" (
        call pnpm list --depth 0 >nul 2>&1
    ) else (
        call npm list --depth=0 >nul 2>&1
    )
    if errorlevel 1 (
        echo [INFO] Dependencies check failed, need to reinstall
        set NEED_INSTALL=1
    )
)

if %NEED_INSTALL%==1 (
    echo [INFO] Installing dependencies...
    echo.
    if "%PKG_MGR%"=="pnpm" (
        call pnpm install
    ) else (
        call npm install
    )
    if errorlevel 1 (
        echo [ERROR] Package install failed!
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

if "%PKG_MGR%"=="pnpm" (
    call pnpm run dev
) else (
    call npm run dev
)

if errorlevel 1 (
    echo.
    echo [ERROR] Failed to start!
    pause
    exit /b 1
)

pause
