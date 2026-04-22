@echo off
chcp 65001 >nul
echo ========================================
echo   Smart Property Management System
echo   Auto Start Script
echo ========================================
echo.

REM Check if PowerShell is available
where powershell >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] PowerShell is not available
    exit /b 1
)

REM Run the PowerShell script with Bypass execution policy
powershell -NoProfile -ExecutionPolicy Bypass -Command "& '%~dp0start-project.ps1'" %*

pause
