/*
 * $Id: SC_PLANTILLA_TRAN_NACIONAL.sql,v 1.11 2008/02/22 18:25:11 ccovian Exp $
 * Autor: Ricardo Legorreta H., Javier Cordova
 * Fecha: 11/02/2005
 *
 */

DROP TABLE SICA_ADMIN.SC_PLANTILLA_TRAN_NACIONAL;

CREATE TABLE SICA_ADMIN.SC_PLANTILLA_TRAN_NACIONAL (
        ID_PLANTILLA NUMBER NOT NULL PRIMARY KEY,
        CLABE VARCHAR2(33) NULL,
        ID_DIVISA CHAR(3) NULL,
        ID_BANCO NUMBER NULL,
        NOMBRE_BANCO VARCHAR2(80) NULL
);

CREATE PUBLIC SYNONYM SC_PLANTILLA_TRAN_NACIONAL FOR SICA_ADMIN.SC_PLANTILLA_TRAN_NACIONAL;
/