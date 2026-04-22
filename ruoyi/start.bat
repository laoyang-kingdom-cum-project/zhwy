@echo off
chcp 65001 >nul
echo ==========================================
echo    RuoYi Management System Startup Script
echo ==========================================
echo.

REM Set Java and Maven paths
set JAVA_HOME=C:\Users\lianfanshuang\.jdks\temurin-17
set MAVEN_HOME=C:\Users\lianfanshuang\.trae-cn\tools\maven\latest
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

echo [1/3] Checking Maven dependencies...

REM Check if dependencies are resolved
call mvn dependency:resolve -q 2>nul
if %errorlevel% neq 0 (
    echo [2/3] First run, compiling and installing project...
    call mvn clean install -DskipTests
    if %errorlevel% neq 0 (
        echo [ERROR] Build failed!
        pause
        exit /b 1
    )
) else (
    echo [2/3] Dependencies OK, skipping compilation
)

echo.
echo [3/3] Starting RuoYi Application...
echo ==========================================
echo    URL: http://localhost:8080
echo    Press Ctrl+C to stop
echo ==========================================
echo.

REM Start Spring Boot application
call mvn spring-boot:run -pl ruoyi-admin

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Startup failed!
    pause
    exit /b 1
)

pause
