DETALLEMOVSPOLIZA_HOME=/$1/herramientas/apps/intranet/ClientesFusionados/DetalleMovsPoliza
echo $DETALLEMOVSPOLIZA_HOME
cd $DETALLEMOVSPOLIZA_HOME
CLASSPATH=$DETALLEMOVSPOLIZA_HOME/lib/*:$DETALLEMOVSPOLIZA_HOME/bin
export CLASSPATH
echo $CLASSPATH
JAVA_HOME=/java/jdk1.6.0_16
export JAVA_HOME
echo $JAVA_HOME
$JAVA_HOME/bin/java com.ixe.ods.sica.batch.poliza.CifrasControlLauncher