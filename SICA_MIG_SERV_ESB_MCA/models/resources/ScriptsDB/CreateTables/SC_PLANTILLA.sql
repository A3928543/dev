/*
 * $Id: SC_PLANTILLA.sql,v 1.11 2008/02/22 18:25:12 ccovian Exp $
 * Autor: Ricardo Legorreta H, Javier Cordova (jcordova).
 * Fecha: 10/02/2005
 *
 */

DROP TABLE SICA_ADMIN.SC_PLANTILLA;

CREATE TABLE SICA_ADMIN.SC_PLANTILLA (
  ID_PLANTILLA NUMBER NOT NULL PRIMARY KEY,
  CLAVE_PLANTILLA CHAR(4) NULL,
  MNEMONICO VARCHAR2(15) NOT NULL,
  NO_CUENTA VARCHAR2(33) NOT NULL,
  INTERNACIONAL CHAR(1) NOT NULL,
  ULTIMA_MODIFICACION DATE NOT NULL,
  STATUS_PLANTILLA CHAR(2) NOT NULL, 
  ID_BENEFICIARIO NUMBER NULL
);

CREATE PUBLIC SYNONYM SC_PLANTILLA FOR SICA_ADMIN.SC_PLANTILLA;
/
