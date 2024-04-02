#!/bin/bash

REPORTEMENSUALRENTABILIDAD=/java/ReporteRentabilidad
export REPORTEMENSUALRENTABILIDAD

cd $REPORTEMENSUALRENTABILIDAD

PATH=/java/jdk1.6.0_16/bin/:$PATH
export PATH

java -jar reporteRentabilidad.jar