#!/bin/sh

PROCESOS_HOME="/java"
export PROCESOS_HOME

cd ${PROCESOS_HOME}/SicaOMMConsumer

JAVA_HOME="/java/jdk1.6.0_16"
export JAVA_HOME

JAVA_OPTIONS="-Djava.util.logging.config.file=resources/logging.properties"
export JAVA_OPTIONS

${JAVA_HOME}/bin/java ${JAVA_OPTIONS} -jar SicaOMMConsumer.jar $1