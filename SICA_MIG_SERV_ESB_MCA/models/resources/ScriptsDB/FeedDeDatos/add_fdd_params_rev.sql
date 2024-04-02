-- Elimina el parámetro para controlar el log del proceso de generación de pizarrones
DELETE FROM SICA_ADMIN.SC_PARAMETRO WHERE  id_parametro = 'FDD_LOG_SWITCH';

-- Elimina el parametro para controlar el log de timming del  proceso de generación de pizarrones. 
DELETE FROM SICA_ADMIN.SC_PARAMETRO WHERE  id_parametro = 'FDD_TIME_SWITCH';

-- Elimina el parámetro que contiene el origen de la variación para los pizarrones
DELETE FROM SC_PARAMETRO WHERE ID_PARAMETRO = 'FDD_ORIGEN_VARIACION';

--Elimina lo parametros para el OMMConsumer
delete from sc_parametro
where id_parametro in ('OMM_CONNECTION_PROPERTIES_FILE_NAME',
'OMM_DACS_USERNAME',
'OMM_SESSION_NAME',
'OMM_LOGIN_TIMEOUT',
'OMM_MARKETFEED_TIMEOUT',
'OMM_SERVICE_NAME',
'OMM_RIC_LIST',
'OMM_INTERESTVIEW_LIST',
'OMM_MARKETFEED_ENDTIME',
'NETEO_TIMEOUT');