echo off
color 0C
:loop
cls
echo "INICIO DE IMPORTACION"
java -classpath dist/som10.jar som.cli.SImportTicketsRevuelta >> log.log
echo "IMPORTACION COMPLETADA"
timeout /T 3600
REM timeout /T 3600 /NOBREAK 
goto loop 