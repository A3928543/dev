#!/bin/bash

DETALLESDEALSH2H_HOME=/AP/herramientas/apps/intranet/ClientesFusionados/DetallesDealsH2H
cd $ DETALLESDEALSH2H_HOME
CLASSPATH=$ DETALLESDEALSH2H_HOME/lib/*

JAVA_HOME=/java/jdk1.6.0_16
export JAVA_HOME
echo $JAVA_HOME


$JAVA_HOME/bin/java -jar detallesDealsH2H.jar