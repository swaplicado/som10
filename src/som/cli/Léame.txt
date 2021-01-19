================================================================================
Ejecución automática de funcionalides CLI
================================================================================

--------------------------------------------------------------------------------
- SCliReportMailer
--------------------------------------------------------------------------------

1.1) Generación y envío vía mail (días lunes) de reporte comparativo histórico mensual de recepción en báscula de materia prima (e.g., aguacate).
    En Windows Task Scheduler:
    Desencadenador:
        Cada semana, lunes, 10:00 hr, indefinidamente.
    Acción: iniciar un programa:
        "C:\Program Files\Java\jre1.8.0_202\bin\javaw.exe"
    Argumentos (para 7 días hacia atrás):
        -classpath dist/som10.jar som.cli.SCliReportMailer 6;23;100 2010 7 ggomezs@aeth.mx;gortiz@aeth.mx;ebarron@aeth.mx sflores@swaplicado.com.mx
    Iniciar en:
        C:\swap\som

1.2) Generación y envío vía mail (días viernes) de reporte comparativo histórico mensual de recepción en báscula de materia prima (e.g., aguacate).
    En Windows Task Scheduler:
    Desencadenador:
        Cada semana, viernes, 08:00 hr, indefinidamente.
    Acción: iniciar un programa:
        "C:\Program Files\Java\jre1.8.0_202\bin\javaw.exe"
    Argumentos (para 4 días hacia atrás):
        -classpath dist/som10.jar som.cli.SCliReportMailer 6;23;100 2010 4 ggomezs@aeth.mx;gortiz@aeth.mx;ebarron@aeth.mx sflores@swaplicado.com.mx
    Iniciar en:
        C:\swap\som

Ejemplos de creación de tareas en el Programador de Tareas de Windows:

SOM CLI Report Mailer 01
SOM CLI Report Mailer weekly each Monday.

SOM CLI Report Mailer 02
SOM CLI Report Mailer weekly each Tuesday.

SOM CLI Report Mailer 03
SOM CLI Report Mailer weekly each Wednesday.

SOM CLI Report Mailer 04
SOM CLI Report Mailer weekly each Thursday.

SOM CLI Report Mailer 05
SOM CLI Report Mailer weekly each Friday.

"C:\Program Files\Java\jre1.8.0_202\bin\javaw.exe"
-classpath dist/som10.jar som.cli.SCliReportMailer 6;23;100 2010 7 floresgtz@hotmail.com sflores@swaplicado.com.mx
-classpath dist/som10.jar som.cli.SCliReportMailer 6;23;100 2010 7 ggomezs@aeth.mx;gortiz@aeth.mx;ebarron@aeth.mx sflores@swaplicado.com.mx
C:\swap\som


--------------------------------------------------------------------------------
- SCliReportMailerSummary
--------------------------------------------------------------------------------

1.1) Generación y envío vía mail (diariio) de reporte resumen de recepción en báscula de materia prima (e.g., aguacate).
    En Windows Task Scheduler:
    Desencadenador:
        Cada día, 06:00 hr, indefinidamente.
    Acción: iniciar un programa:
        "C:\Program Files\Java\jre1.8.0_202\bin\javaw.exe"
    Argumentos (para 1 día hacia atrás):
        -classpath dist/som10.jar som.cli.SCliReportMailerSummary 6;23;100 YESTERDAY ggomezs@aeth.mx;gortiz@aeth.mx;ebarron@aeth.mx sflores@swaplicado.com.mx
    Iniciar en:
        C:\swap\som

Ejemplos de creación de tareas en el Programador de Tareas de Windows:

SOM CLI Report Mailer Summary
SOM CLI Report Mailer Summary daily.

"C:\Program Files\Java\jre1.8.0_202\bin\javaw.exe"
-classpath dist/som10.jar som.cli.SCliReportMailerSummary 6;23;100 YESTERDAY floresgtz@hotmail.com sflores@swaplicado.com.mx
-classpath dist/som10.jar som.cli.SCliReportMailerSummary 6;23;100 YESTERDAY gortiz@aeth.mx sflores@swaplicado.com.mx
-classpath dist/som10.jar som.cli.SCliReportMailerSummary 6;23;100 YESTERDAY ggomezs@aeth.mx;gortiz@aeth.mx;ebarron@aeth.mx sflores@swaplicado.com.mx
C:\swap\som


--------------------------------------------------------------------------------
- SImportTicketsRevuelta
--------------------------------------------------------------------------------

2) Importación automática de boletos Revuelta a SOM.
    En Windows Task Scheduler:
    Desencadenador:
        Cada hora, indefinidamente.
    Acción: iniciar un programa:
        "C:\Program Files\Java\jre1.8.0_202\bin\javaw.exe"
    Argumentos:
        -classpath dist/som10.jar som.cli.SImportTicketsRevuelta
    Iniciar en:
        C:\swap\som

#EOF