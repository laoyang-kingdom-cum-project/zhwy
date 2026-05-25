@echo off
chcp 65001 >nul
echo.
echo ==========================================
echo    银铃助手 - 后端服务启动脚本
echo ==========================================
echo.

REM 切换到 ruoyi 目录
cd /d "%~dp0\ruoyi"

REM 检查 Java 环境
if not defined JAVA_HOME (
    echo [ERROR] 未设置 JAVA_HOME 环境变量
    echo [INFO] 请先安装 JDK 并配置 JAVA_HOME
    pause
    exit /b 1
)

echo [1/3] Java 环境: %JAVA_HOME%

REM 检查 Maven 环境
if not defined MAVEN_HOME (
    echo [WARN] 未设置 MAVEN_HOME，尝试使用系统 PATH 中的 mvn
    set "MVN_CMD=mvn"
) else (
    set "MVN_CMD=%MAVEN_HOME%\bin\mvn.cmd"
    echo [1/3] Maven 环境: %MAVEN_HOME%
)

echo.
echo [2/3] 开始编译项目...
call %MVN_CMD% clean install -DskipTests

if errorlevel 1 (
    echo.
    echo [ERROR] 编译失败！
    pause
    exit /b 1
)

echo.
echo [3/3] 启动后端服务...
echo ==========================================
echo    服务地址: http://localhost:8080
echo    API 文档: http://localhost:8080/swagger-ui/index.html
echo    Druid 监控: http://localhost:8080/druid
echo ==========================================
echo    按 Ctrl+C 停止服务
echo.

REM 启动 Spring Boot 应用
call %MVN_CMD% spring-boot:run -pl ruoyi-admin

pause
