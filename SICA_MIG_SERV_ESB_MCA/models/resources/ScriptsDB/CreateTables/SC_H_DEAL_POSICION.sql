/*
 * $Id: SC_H_DEAL_POSICION.sql,v 1.11 2008/02/22 18:25:12 ccovian Exp $
 * Autor: Ricardo Legorreta H.
 * Fecha: 03/10/2005
 *
 */

DROP TABLE SICA_ADMIN.SC_H_DEAL_POSICION;

CREATE TABLE SICA_ADMIN.SC_H_DEAL_POSICION (
  ID_DEAL_POSICION NUMBER NOT NULL PRIMARY KEY
, ID_DIVISA CHAR(3) NOT NULL
, ID_MESA_CAMBIO NUMBER NOT NULL
, ID_USUARIO NUMBER NOT NULL
, MONTO NUMBER(16, 2) NOT NULL
, OBSERVACIONES VARCHAR2(60) NULL
, RECIBIMOS CHAR(1) NOT NULL
, TIPO_CAMBIO NUMBER(16, 6) NOT NULL
, TIPO_DEAL CHAR(1) NOT NULL
);

CREATE PUBLIC SYNONYM SC_H_DEAL_POSICION FOR SICA_ADMIN.SC_H_DEAL_POSICION;
/