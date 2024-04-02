/*
 * $Id: SC_SPREAD_ACTUAL.sql,v 1.11 2008/02/22 18:25:12 ccovian Exp $
 * Autor: Ricardo Legorreta H.
 * Fecha: 10/02/2005
 *
 */

DROP TABLE SICA_ADMIN.SC_SPREAD_ACTUAL;

CREATE TABLE SICA_ADMIN.SC_SPREAD_ACTUAL (
  ID_MESA_CAMBIO NUMBER NOT NULL
, ID_CANAL CHAR(3) NOT NULL
, CLAVE_FORMA_LIQUIDACION CHAR(6) NOT NULL
, ID_DIVISA CHAR(3) NOT NULL
, ID_SPREAD NUMBER NOT NULL
, ULTIMA_MODIFICACION DATE NOT NULL
, PRIMARY KEY (ID_MESA_CAMBIO, ID_CANAL, CLAVE_FORMA_LIQUIDACION, ID_DIVISA) 
);

CREATE PUBLIC SYNONYM SC_SPREAD_ACTUAL FOR SICA_ADMIN.SC_SPREAD_ACTUAL;
/