#!/bin/sh

cd /bea/procesos/Sica

JAVA_HOME="/bea/jdk142_11"
export JAVA_HOME

JAVA_OPTIONS="t3://ixe04desa.ixe.com.mx:7023 $1 ccovian@ixe.com.mx,favilaj@ixe.com.mx,serviciosweb@ixe.com.mx,SiteSistemas@ixe.com.mx"
export JAVA_OPTIONS

CLASSPATH="./lib/sica-cierre.jar:./lib/sica-interfaces.jar:/bea/weblogic81/server/lib/:./lib/log4j-1.2.7.jar:./lib/"
export CLASSPATH
${JAVA_HOME}/bin/java com.ixe.ods.sica.Cierre ${JAVA_OPTIONS}
