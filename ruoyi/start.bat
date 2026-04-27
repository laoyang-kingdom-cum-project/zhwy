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

REM Check if Java is installed (use full path)
if not exist "%JAVA_HOME%\bin\java.exe" (
    echo [ERROR] Java is not found at %JAVA_HOME%!
    echo Please install Java 17 or update JAVA_HOME path.
    pause
    exit /b 1
)

REM Check if Maven is installed (use full path)
if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
    if not exist "%MAVEN_HOME%\bin\mvn.bat" (
        echo [ERROR] Maven is not found at %MAVEN_HOME%!
        echo Please install Maven 3.6 or update MAVEN_HOME path.
        pause
        exit /b 1
    )
)

echo [1/4] Checking environment... OK
echo.

REM Check if local repository exists and has dependencies
echo [2/4] Checking Maven dependencies...

set LOCAL_REPO=%USERPROFILE%\.m2\repository
set DEPS_MISSING=0

REM Check if local repository exists
if not exist "%LOCAL_REPO%" (
    echo [INFO] Local Maven repository not found at %LOCAL_REPO%
    set DEPS_MISSING=1
) else (
    REM Check if key dependencies exist (ruoyi-common)
    if not exist "%LOCAL_REPO%\com\ruoyi" (
        echo [INFO] RuoYi dependencies not found in local repository
        set DEPS_MISSING=1
    ) else (
        REM Check if Spring Boot dependencies exist
        if not exist "%LOCAL_REPO%\org\springframework\boot" (
            echo [INFO] Spring Boot dependencies not found in local repository
            set DEPS_MISSING=1
        )
    )
)

REM Check dependencies using Maven
if %DEPS_MISSING%==0 (
    call mvn dependency:resolve -q 2>nul
    if errorlevel 1 (
        set DEPS_MISSING=1
    )
)

if %DEPS_MISSING%==1 (
    echo [INFO] Dependencies missing, downloading...
    echo.
    call mvn dependency:resolve
    if errorlevel 1 (
        echo [ERROR] Failed to download dependencies!
        pause
        exit /b 1
    )
    echo [INFO] Dependencies downloaded successfully!
) else (
    echo [INFO] Dependencies OK
)

echo.
echo [3/4] Compiling project...

REM Check if target directories exist
set NEED_COMPILE=0
if not exist "ruoyi-admin\target" set NEED_COMPILE=1
if not exist "ruoyi-common\target" set NEED_COMPILE=1
if not exist "ruoyi-framework\target" set NEED_COMPILE=1
if not exist "ruoyi-system\target" set NEED_COMPILE=1

if %NEED_COMPILE%==1 (
    echo [INFO] Project not compiled, running mvn clean install...
    call mvn clean install -DskipTests
    if errorlevel 1 (
        echo [ERROR] Build failed!
        pause
        exit /b 1
    )
    echo [INFO] Project compiled successfully!
) else (
    echo [INFO] Project already compiled, skipping compilation
)

echo.
echo [4/4] Starting RuoYi Application...
echo ==========================================
echo    URL: http://localhost:8080
echo    Press Ctrl+C to stop
echo ==========================================
echo.

REM Start Spring Boot application
call mvn spring-boot:run -pl ruoyi-admin

if errorlevel 1 (
    echo.
    echo [ERROR] Startup failed!
    pause
    exit /b 1
)

pause
