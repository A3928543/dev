#!/bin/bash

REPORTEMENSUALUIF=.
export REPORTEMENSUALUIF

cd $REPORTEMENSUALUIF

PATH=/java/jdk1.6.0_16/bin/:$PATH
export PATH

EJEMPLOJAVA=-Dfile.encoding=ISO-8859-1
export EJEMPLOJAVA


java $EJEMPLOJAVA -jar reporteMensualUIF.jar