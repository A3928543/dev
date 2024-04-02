#! /bin/sh

TRANSFINT=/$1/herramientas/apps/intranet/ClientesFusionados/TRANSFINT/
export TRANSFINT

cd $TRANSFINT

LIB_TRANSINT=$TRANSFINT/lib
export LIB_TRANSINT

for i in $LIB_TRANSINT/*.jar
do
        CLASSPATH=$CLASSPATH:$i
done

export CLASSPATH

echo $CLASSPATH

JAVA_HOME=/java/jdk1.6.0_16
export JAVA_HOME

${JAVA_HOME}/bin/java com.banorte.ods.sica.batch.transfint.ReporteLauncher TRANS_DIARIAS