@echo off
cd /d "%~dp0"
if not exist bin\Application.class (
  echo Run build.bat first ^(no compiled classes in bin\^).
  exit /b 1
)
rem Classpath: bin + src so /img/banking-logo.png loads if present
java -cp "bin;src" Application
exit /b %ERRORLEVEL%
