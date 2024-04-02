@echo off
set DETALLEMOVSCFDI_HOME=C:\tmp\BAT\DetalleMovsCFDI
set JAVA_HOME=C:\Java\jdk1.6.0_45
set CLASSPATH=%DETALLEMOVSCFDI_HOME%\bin;%DETALLEMOVSCFDI_HOME%\lib\*;
set PATH=C:\Java\jdk1.6.0_45\bin;%PATH%
java com.ixe.ods.sica.batch.cfdi.ProcesamientoArchivoIndices