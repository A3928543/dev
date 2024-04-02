/*
 * $Id: SC_LIMITE.sql,v 1.12 2008/02/22 18:25:11 ccovian Exp $
 * Autor: Ricardo Legorreta H.
 * Fecha: 26/01/2005
 *
 */

DROP TABLE SICA_ADMIN.SC_LIMITE;

CREATE TABLE SICA_ADMIN.SC_LIMITE (
        ID_LIMITE NUMBER NOT NULL PRIMARY KEY
        ID_DIVISA CHAR(3) NULL,
        ID_MESA_CAMBIO NUMBER NOT NULL,
        ID_TIPO_LIMITE NUMBER NOT NULL,
        LIMITE NUMBER(18,2) NULL, 
        PORCENTAJE_AVISO NUMBER(12,6) NOT NULL,
        PORCENTAJE_ALARMA NUMBER(12,6) NOT NULL,
);

CREATE PUBLIC SYNONYM SC_LIMITE FOR SICA_ADMIN.SC_LIMITE;
/