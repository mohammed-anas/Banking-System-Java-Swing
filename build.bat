@echo off
setlocal
cd /d "%~dp0"
if not exist bin mkdir bin
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -d bin -sourcepath src @sources.txt
if errorlevel 1 (
  if exist sources.txt del sources.txt
  exit /b 1
)
if exist sources.txt del sources.txt
echo Build finished: output in bin\
endlocal
