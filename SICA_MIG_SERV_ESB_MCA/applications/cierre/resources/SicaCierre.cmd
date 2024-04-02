SET JAVA_HOME=%1
SET CLASSPATH=./lib/sica-cierre.jar;./lib/sica-interfaces.jar;./lib/wlclient.jar;./lib/log4j-1.2.7.jar;./lib/
%JAVA_HOME%/bin/java com.ixe.ods.sica.Cierre %2 %3 %4
