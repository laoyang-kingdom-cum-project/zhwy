@echo off
chcp 65001 >nul
echo ==========================================
echo    RuoYi Management System Startup Script
echo ==========================================
echo.

REM Resolve Java (prefer JAVA_HOME, then PATH, then prompt)
set "JAVA_BIN="
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set "JAVA_BIN=%JAVA_HOME%\bin\java.exe"
if not defined JAVA_BIN (
    for /f "delims=" %%J in ('where java 2^>nul') do set "JAVA_BIN=%%J" & goto JAVA_BIN_FOUND
)
:JAVA_BIN_FOUND
if not defined JAVA_BIN (
    echo [WARN] Java not found in JAVA_HOME or PATH.
    set /p "JAVA_HOME=Input JAVA_HOME (JDK root, e.g. C:\Program Files\Java\jdk-17): "
    if exist "%JAVA_HOME%\bin\java.exe" set "JAVA_BIN=%JAVA_HOME%\bin\java.exe"
)
if not defined JAVA_BIN (
    echo [ERROR] Java is not found. Please install Java 17 or set JAVA_HOME.
    pause
    exit /b 1
)

REM Resolve Maven (prefer MAVEN_HOME, then PATH, then prompt)
set "MVN_BIN="
if defined MAVEN_HOME (
    if exist "%MAVEN_HOME%\bin\mvn.cmd" set "MVN_BIN=%MAVEN_HOME%\bin\mvn.cmd"
    if not defined MVN_BIN if exist "%MAVEN_HOME%\bin\mvn.bat" set "MVN_BIN=%MAVEN_HOME%\bin\mvn.bat"
)
if not defined MVN_BIN (
    for /f "delims=" %%M in ('where mvn 2^>nul') do set "MVN_BIN=%%M" & goto MVN_BIN_FOUND
)
:MVN_BIN_FOUND
if not defined MVN_BIN (
    echo [WARN] Maven not found in MAVEN_HOME or PATH.
    set /p "MAVEN_HOME=Input MAVEN_HOME (Maven root, e.g. C:\apache-maven-3.9.6): "
    if exist "%MAVEN_HOME%\bin\mvn.cmd" set "MVN_BIN=%MAVEN_HOME%\bin\mvn.cmd"
    if not defined MVN_BIN if exist "%MAVEN_HOME%\bin\mvn.bat" set "MVN_BIN=%MAVEN_HOME%\bin\mvn.bat"
)
if not defined MVN_BIN (
    echo [ERROR] Maven is not found. Please install Maven 3.6+ or set MAVEN_HOME.
    pause
    exit /b 1
)

if defined JAVA_HOME set "PATH=%JAVA_HOME%\bin;%PATH%"
if defined MAVEN_HOME set "PATH=%MAVEN_HOME%\bin;%PATH%"

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
    call "%MVN_BIN%" dependency:resolve -q 2>nul
    if errorlevel 1 (
        set DEPS_MISSING=1
    )
)

if %DEPS_MISSING%==1 (
    echo [INFO] Dependencies missing, downloading...
    echo.
    call "%MVN_BIN%" dependency:resolve
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

REM Always recompile to ensure latest code
echo [INFO] Compiling project with mvn clean install...
call "%MVN_BIN%" clean install -DskipTests
if errorlevel 1 (
    echo [ERROR] Build failed!
    pause
    exit /b 1
)
echo [INFO] Project compiled successfully!

echo.
echo [4/4] Starting RuoYi Application...
echo ==========================================
echo    URL: http://localhost:8080
echo    Press Ctrl+C to stop
echo ==========================================
echo.

REM Start Spring Boot application
call "%MVN_BIN%" spring-boot:run -pl ruoyi-admin

if errorlevel 1 (
    echo.
    echo [ERROR] Startup failed!
    pause
    exit /b 1
)

pause
