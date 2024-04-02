@echo off
set DETALLEMOVSPOLIZA_HOME=C:\tmp\BAT\DetalleMovsPoliza
set JAVA_HOME=C:\Java\jdk1.6.0_45
set CLASSPATH=%DETALLEMOVSPOLIZA_HOME%\bin;%DETALLEMOVSPOLIZA_HOME%\lib\*;
set PATH=C:\Java\jdk1.6.0_45\bin;%PATH%
echo %CLASSPATH%
echo %PATH%
java com.ixe.ods.sica.batch.poliza.CifrasControlLauncher