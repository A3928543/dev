/*
 * $Id: SC_H_TIPO_CAMBIO.sql,v 1.11 2008/02/22 18:25:13 ccovian Exp $
 * Autor: Ricardo Legorreta H.
 * Fecha: 26/01/2005
 *
 */

DROP TABLE SICA_ADMIN.SC_H_TIPO_CAMBIO;

CREATE TABLE SICA_ADMIN.SC_H_TIPO_CAMBIO (
        ID_HISTORICO NUMBER NOT NULL PRIMARY KEY,
        ID_DIVISA CHAR(3) NOT NULL,
        FECHA DATE NOT NULL,
        TIPO_CAMBIO NUMBER(12,6) NOT NULL 
);

CREATE PUBLIC SYNONYM SC_H_TIPO_CAMBIO FOR SICA_ADMIN.SC_H_TIPO_CAMBIO;
/