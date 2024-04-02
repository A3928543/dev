-- Inserta el parámetro para controlar el log del proceso de generación de pizarrones
INSERT INTO SICA_ADMIN.SC_PARAMETRO (ID_PARAMETRO, CADUCIDAD, TIPO_VALOR, VALOR) 
VALUES ('FDD_LOG_SWITCH', NULL, 'b', '0');

-- Inserta el parámetro para controlar el log de timming del  proceso de generación de pizarrones. Parametro auxiliar para 
-- medicion de rendimiento
INSERT INTO SICA_ADMIN.SC_PARAMETRO (ID_PARAMETRO, CADUCIDAD, TIPO_VALOR, VALOR) 
VALUES ('FDD_TIME_SWITCH', NULL, 'b', '0');

-- Inserta el parametro que contiene el origen de la variación para los pizarrones
INSERT INTO SICA_ADMIN.SC_PARAMETRO (ID_PARAMETRO, CADUCIDAD, TIPO_VALOR, VALOR
) VALUES ('FDD_ORIGEN_VARIACION', NULL, 'c', 'ORIG_VAR_EXCEL');

-- Parametros para el OMMConsumer
insert into sc_parametro(ID_PARAMETRO, TIPO_VALOR,VALOR)
values('OMM_CONNECTION_PROPERTIES_FILE_NAME', 'c', 'resources/SICA_RMDSConnectionProperties.xml');

insert into sc_parametro(ID_PARAMETRO, TIPO_VALOR,VALOR)
values('OMM_DACS_USERNAME', 'c', 'sica');

insert into sc_parametro(ID_PARAMETRO, TIPO_VALOR,VALOR)
values('OMM_SESSION_NAME', 'c', 'sica::rmdsSession');

insert into sc_parametro(ID_PARAMETRO, TIPO_VALOR,VALOR)
values('OMM_LOGIN_TIMEOUT', 'c', '30');

insert into sc_parametro(ID_PARAMETRO, TIPO_VALOR,VALOR)
values('OMM_MARKETFEED_TIMEOUT', 'c', '0');

insert into sc_parametro(ID_PARAMETRO, TIPO_VALOR,VALOR)
values('OMM_SERVICE_NAME', 'c', 'IDN_INTERVAL');

insert into sc_parametro(ID_PARAMETRO, TIPO_VALOR,VALOR)
values('OMM_RIC_LIST', 'c', 'MXN=D2,EUR=,JPY=,GBP=,CAD=,CHF=');

insert into sc_parametro(ID_PARAMETRO, TIPO_VALOR,VALOR)
values('OMM_INTERESTVIEW_LIST', 'c', '22,25');

insert into sc_parametro(ID_PARAMETRO, TIPO_VALOR,VALOR)
values('OMM_MARKETFEED_ENDTIME', 'c', '16:00');

--Parametro para el pizarron de la celula java
insert into SC_PARAMETRO(ID_PARAMETRO,TIPO_VALOR,VALOR) 
values ('NETEO_TIMEOUT','n','180');

