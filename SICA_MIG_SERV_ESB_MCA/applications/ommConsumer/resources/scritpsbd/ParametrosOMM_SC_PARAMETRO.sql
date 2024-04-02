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
values('OMM_MARKETFEED_ENDTIME', 'c', '20:45');

SELECT * FROM sc_parametro WHERE id_parametro like 'OMM_%';
