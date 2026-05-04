@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo ==========================================
echo   One Click Start (CMD)
echo ==========================================
echo.

set "SCRIPT_DIR=%~dp0"
set "BACKEND_BAT=%SCRIPT_DIR%ruoyi\start.bat"
set "FRONTEND_BAT=%SCRIPT_DIR%ruoyi\ruoyi-ui\start-ui.bat"

if not exist "%BACKEND_BAT%" (
  echo [ERROR] Not found: %BACKEND_BAT%
  exit /b 1
)
if not exist "%FRONTEND_BAT%" (
  echo [ERROR] Not found: %FRONTEND_BAT%
  exit /b 1
)

rem ---- Start backend ----
echo [1/2] Starting Backend...
start "RuoYi Backend" cmd /c ""%BACKEND_BAT%""

echo        Waiting for port 8080...
call :WaitPort localhost 8080 300
if errorlevel 1 (
  echo [ERROR] Backend did not become ready in time.
  exit /b 1
)

echo        Backend ready!

rem ---- Start frontend ----
echo [2/2] Starting Frontend...
start "RuoYi UI" cmd /c ""%FRONTEND_BAT%""

echo        Waiting for port 80...
call :WaitPort localhost 80 120
if errorlevel 1 (
  echo [WARN] Frontend port 80 not ready yet. It may still be starting or using another port.
)

echo.
echo ==========================================
echo   All started!
echo ==========================================
echo   Backend : http://localhost:8080
echo   Admin UI: http://localhost:80
echo   Druid   : http://localhost:8080/druid
echo ==========================================
echo   Press Ctrl+C to stop the CMD windows
echo.

goto :eof

:WaitPort
set "HOST=%~1"
set "PORT=%~2"
set "TIMEOUT=%~3"
set /a ELAPSED=0
:WaitPortLoop
powershell -NoProfile -Command "try { $c = New-Object Net.Sockets.TcpClient; $t = $c.ConnectAsync('%HOST%', %PORT%); if ($t.Wait(500) -and $c.Connected) { exit 0 } else { exit 1 } } catch { exit 1 }"
if %ERRORLEVEL%==0 exit /b 0

timeout /t 2 /nobreak >nul
set /a ELAPSED+=2
if %ELAPSED% GEQ %TIMEOUT% exit /b 1

goto WaitPortLoop
