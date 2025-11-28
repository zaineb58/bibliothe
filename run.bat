@echo off
REM Script pour compiler et exécuter l'application avec JavaFX

echo Compilation en cours...
cd /d "%~dp0"

REM Créer le dossier bin s'il n'existe pas
if not exist bin mkdir bin

REM Compiler tous les fichiers Java
for /r src %%f in (*.java) do (
    javac -encoding UTF-8 -d bin -cp "lib/*" "%%f" 2>nul
)

echo Compilation terminée.
echo.
echo Lancement de l'application...
echo.

REM Exécuter l'application
java -cp "bin;lib/*" Main

pause
