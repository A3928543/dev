@echo off

set ANT_CMD_LINE_ARGS=%*
set WL_HOME=\bea
set WL_SERVER=%WL_HOME%\weblogic81\server
set JAVA_HOME=\bea\jdk142_05

set CLASSPATH=%WL_SERVER%\lib\weblogic.jar;%JAVA_HOME%\lib\tools.jar
set CLASSPATH=%CLASSPATH%;%WL_SERVER%\lib\webserviceclient.jar

REM echo "PATH=" %PATH%
REM echo "CLASSPATH=" %CLASSPATH%

%JAVA_HOME%\bin\java.exe org.apache.tools.ant.Main %ANT_CMD_LINE_ARGS%
