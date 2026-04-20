@echo off
chcp 65001 >nul
title 若依管理系统一键启动脚本
echo.
echo ==========================================
echo    若依管理系统 - 一键启动脚本
echo ==========================================
echo.

:: 设置变量
set PROJECT_HOME=%~dp0
set JAVA_OPTS=-Xms512m -Xmx1024m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m
set JAR_NAME=ruoyi-admin.jar
set JAR_PATH=%PROJECT_HOME%ruoyi-admin\target\%JAR_NAME%

cd /d %PROJECT_HOME%

:: 检查 Java 环境
echo [1/5] 检查 Java 环境...
java -version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到 Java 环境，请安装 JDK 1.8 或更高版本！
    pause
    exit /b 1
)
echo [1/5] Java 环境检查通过
echo.

:: 检查 Maven 环境
echo [2/5] 检查 Maven 环境...
mvn -v >nul 2>&1
if errorlevel 1 (
    echo [警告] 未检测到 Maven 环境，将尝试使用 mvnw 或跳过编译
    set MVN_CMD=mvnw
) else (
    set MVN_CMD=mvn
)
echo [2/5] Maven 环境检查通过
echo.

:: 检查是否存在已编译的 jar 包
echo [3/5] 检查项目编译状态...
if exist "%JAR_PATH%" (
    echo [3/5] 检测到已编译的 jar 包
echo.
    choice /C YN /M "是否重新编译项目" /T 10 /D N
    if errorlevel 2 (
        echo [3/5] 跳过编译，使用现有 jar 包
        goto :run_jar
    ) else (
        echo [3/5] 开始重新编译项目...
        goto :compile
    )
) else (
    echo [3/5] 未检测到 jar 包，需要先编译项目
    goto :compile
)

:compile
echo.
echo ==========================================
echo [4/5] 开始编译打包项目...
echo ==========================================
echo.

:: 执行 Maven 打包
call %MVN_CMD% clean package -DskipTests
if errorlevel 1 (
    echo.
    echo [错误] 项目编译失败！请检查代码错误。
    pause
    exit /b 1
)
echo.
echo [4/5] 项目编译打包成功！
echo.

:run_jar
echo ==========================================
echo [5/5] 启动若依管理系统...
echo ==========================================
echo.

:: 检查 jar 包是否存在
if not exist "%JAR_PATH%" (
    echo [错误] 未找到 jar 包: %JAR_PATH%
    pause
    exit /b 1
)

echo 启动参数: %JAVA_OPTS%
echo 应用路径: %JAR_PATH%
echo.
echo 正在启动，请稍候...
echo 启动成功后，请访问: http://localhost:8080
echo 默认账号: admin / 密码: admin123
echo.
echo 按 Ctrl+C 可停止服务
echo ==========================================
echo.

:: 启动应用
cd /d %PROJECT_HOME%ruoyi-admin\target
java -jar %JAVA_OPTS% %JAR_NAME%

echo.
echo 服务已停止
echo.
pause
