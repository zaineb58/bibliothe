@echo off
REM Script pour compiler et exécuter l'application avec JavaFX

echo Compilation en cours...
cd /d "%~dp0"

REM Créer le dossier bin s'il n'existe pas
if not exist bin mkdir bin

REM Supprimer les anciennes classes compilées
del /q bin\*.class 2>nul
for /d %%p in (bin\*) do rmdir "%%p" /s /q 2>nul

REM Chemin vers JavaFX (adapter si nécessaire)
set JAVAFX_PATH=c:\Users\wassi\Downloads\openjfx-25.0.1_windows-x64_bin-sdk\javafx-sdk-25.0.1\lib
set JAVAFX_MODULES=javafx.controls,javafx.fxml

REM Compiler avec JavaFX dans le bon ordre
echo Compilation des fichiers sources...
javac -encoding UTF-8 --module-path "%JAVAFX_PATH%" --add-modules %JAVAFX_MODULES% -d bin -cp "lib/*" src\model\*.java
javac -encoding UTF-8 --module-path "%JAVAFX_PATH%" --add-modules %JAVAFX_MODULES% -d bin -cp "lib/*;bin" src\util\Connexion.java
javac -encoding UTF-8 --module-path "%JAVAFX_PATH%" --add-modules %JAVAFX_MODULES% -d bin -cp "lib/*;bin" src\DAO\*.java
javac -encoding UTF-8 --module-path "%JAVAFX_PATH%" --add-modules %JAVAFX_MODULES% -d bin -cp "lib/*;bin" src\services\*.java
javac -encoding UTF-8 --module-path "%JAVAFX_PATH%" --add-modules %JAVAFX_MODULES% -d bin -cp "lib/*;bin" src\util\*.java
javac -encoding UTF-8 --module-path "%JAVAFX_PATH%" --add-modules %JAVAFX_MODULES% -d bin -cp "lib/*;bin" src\Controller\*.java
javac -encoding UTF-8 --module-path "%JAVAFX_PATH%" --add-modules %JAVAFX_MODULES% -d bin -cp "lib/*;bin" src\view\*.java
javac -encoding UTF-8 --module-path "%JAVAFX_PATH%" --add-modules %JAVAFX_MODULES% -d bin -cp "lib/*;bin" src\Main.java

if errorlevel 1 (
    echo.
    echo Erreur lors de la compilation!
    pause
    exit /b 1
)

echo Compilation terminée avec succès.
echo.
echo Lancement de l'application...
echo.

REM Exécuter l'application avec JavaFX
java --module-path "%JAVAFX_PATH%" --add-modules %JAVAFX_MODULES% -cp "bin;lib/*" Main

pause
