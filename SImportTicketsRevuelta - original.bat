echo off
color 0C
:loop
java -classpath dist/som10.jar som.cli.SImportTicketsRevuelta >> SImportTicketsRevuelta.log

for /F "usebackq tokens=1,2 delims==" %%i in (`wmic os get LocalDateTime /VALUE 2^>NUL`) do if '.%%i.'=='.LocalDateTime.' set ldt=%%j
set ldt=%ldt:~0,4%-%ldt:~4,2%-%ldt:~6,2% %ldt:~8,2%:%ldt:~10,2%:%ldt:~12,6%
cls
echo IMPORTACION COMPLETADA: [%ldt%] >> SImportTicketsRevuelta.log
echo IMPORTACION COMPLETADA: [%ldt%]

timeout /T 3600
REM timeout /T 3600 /NOBREAK 
goto loop 