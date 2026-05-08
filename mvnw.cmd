@echo off
@REM Maven Wrapper for SENSEI project (Windows CMD)
@REM Uses local JDK 21 for Lombok compatibility
setlocal

set "BASEDIR=%~dp0"
if "%BASEDIR:~-1%"=="\" set "BASEDIR=%BASEDIR:~0,-1%"

set "WRAPPER_JAR=%BASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set "WRAPPER_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

@REM Use local JDK 21 (Lombok requires JDK 21, not 25)
set "JAVA_HOME=%BASEDIR%\jdk-21.0.7+6"
set "JAVACMD=%JAVA_HOME%\bin\java.exe"

@REM Verify java works
"%JAVACMD%" -version >nul 2>&1
if ERRORLEVEL 1 (
    echo ERROR: JDK 21 not found at %JAVA_HOME%
    echo Please ensure jdk-21.0.7+6 folder exists in the project root.
    exit /b 1
)

@REM Create wrapper dir if needed
if not exist "%BASEDIR%\.mvn\wrapper" mkdir "%BASEDIR%\.mvn\wrapper"

@REM Download wrapper JAR if missing
if not exist "%WRAPPER_JAR%" (
    echo Downloading Maven Wrapper...
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri '%WRAPPER_URL%' -OutFile '%WRAPPER_JAR%'"
    if ERRORLEVEL 1 (
        echo ERROR: Download failed.
        exit /b 1
    )
    echo Done.
)

@REM Launch Maven with JDK 21
"%JAVACMD%" "-Dmaven.multiModuleProjectDirectory=%BASEDIR%" -classpath "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*

exit /b %ERRORLEVEL%
