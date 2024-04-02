/*
 * $Id: SC_BROKER.sql,v 1.11 2008/02/22 18:25:12 ccovian Exp $
 * Autor: Ricardo Legorreta H, Javier Cordova (jcordova).
 * Fecha: 10/02/2005
 *
 */
DROP TABLE SICA_ADMIN.SC_BROKER;

CREATE TABLE SICA_ADMIN.SC_BROKER (
  ID_PERSONA NUMBER NOT NULL PRIMARY KEY
, CLAVE_REUTERS VARCHAR2(10) NULL
, TIPO_BROKER CHAR(1) NOT NULL
, PAGO_ANTICIPADO CHAR(1) DEFAULT 'N' NOT NULL
);

CREATE PUBLIC SYNONYM SC_BROKER FOR SICA_ADMIN.SC_BROKER;
/