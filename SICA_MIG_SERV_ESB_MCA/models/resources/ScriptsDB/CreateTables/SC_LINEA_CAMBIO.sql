/*
 * $Id: SC_LINEA_CAMBIO.sql,v 1.3 2008/12/26 23:17:36 ccovian Exp $
 * Autor: Ricardo Legorreta H., Javier Cordova (jcordova)
 * Fecha: 10/09/2008
 */

DROP TABLE SICA_ADMIN.SC_LINEA_CAMBIO;

CREATE TABLE SICA_ADMIN.SC_LINEA_CAMBIO (
        ID_LINEA_CAMBIO NUMBER NOT NULL PRIMARY KEY,
        ID_CORPORATIVO NUMBER NOT NULL,
        IMPORTE NUMBER(16,2) NOT NULL,
        IMPORTE_REMESA NUMBER(16, 2) NOT NULL,
        USO_CASH NUMBER(16,2) NOT NULL,
        USO_TOM NUMBER(16,2) NOT NULL,
        USO_SPOT NUMBER(16,2) NOT NULL,
        USO_72HR NUMBER(16,2) NOT NULL,
        USO_96HR NUMBER(16,2) NOT NULL,
        USO_REM_CASH NUMBER(16,2) NOT NULL,
        USO_REM_TOM NUMBER(16,2) NOT NULL,
        USO_REM_SPOT NUMBER(16,2) NOT NULL,
        USO_REM_72HR NUMBER(16,2) NOT NULL,
        USO_REM_96HR NUMBER(16,2) NOT NULL,
        PROMEDIO_LINEA NUMBER(16,2) NOT NULL,
        ULTIMA_MODIFICACION DATE NOT NULL,
        NUMERO_EXCEPCIONES NUMBER(3) NOT NULL,
        NUMERO_EXCEPCIONES_MES NUMBER(3) NOT NULL,
        VENCIMIENTO DATE NOT NULL,
        STATUS_LINEA CHAR(2) NOT NULL
);

CREATE PUBLIC SYNONYM SC_LINEA_CAMBIO FOR SICA_ADMIN.SC_LINEA_CAMBIO;
/
