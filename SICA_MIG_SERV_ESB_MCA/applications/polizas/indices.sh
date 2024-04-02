DETALLEMOVSCFDI_HOME=/AD/herramientas/apps/intranet/ClientesFusionados/DetalleMovsCFDI
cd $DETALLEMOVSCFDI_HOME
CLASSPATH=$DETALLEMOVSCFDI_HOME/lib/*:$DETALLEMOVSCFDI_HOME/bin
export CLASSPATH
echo $CLASSPATH
JAVA_HOME=/java/jdk1.6.0_16
export JAVA_HOME
echo $JAVA_HOME
$JAVA_HOME/bin/java com.ixe.ods.sica.batch.cfdi.ProcesamientoArchivoIndices