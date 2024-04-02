#!/bin/bash

REPORTEMENSUALUIF=/java/ReporteMensualUIF
export REPORTEMENSUALUIF

cd $REPORTEMENSUALUIF

PATH=/java/jdk1.6.0_16/bin/:$PATH
export PATH

java -jar reporteMensualUIF.jar