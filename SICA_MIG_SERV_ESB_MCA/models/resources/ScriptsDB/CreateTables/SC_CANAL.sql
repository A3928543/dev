/*
 * $Id: SC_CANAL.sql,v 1.11 2008/02/22 18:25:12 ccovian Exp $
 * Autor: Ricardo Legorreta H, Javier Cordova
 * Fecha: 14/02/2005
 *
 */

DROP TABLE SICA_ADMIN.SC_CANAL;

CREATE TABLE SICA_ADMIN.SC_CANAL (
  ID_CANAL CHAR(3) NOT NULL PRIMARY KEY, 
  FACULTAD VARCHAR2(20) NOT NULL, 
  ID_MESA_CAMBIO NUMBER NOT NULL, 
  ID_SUCURSAL NUMBER NULL,
  MONTO_MAXIMO_OPERACION NUMBER(16, 2) NULL,
  MONTO_OPERACIONES NUMBER(16, 2) NULL,
  NOMBRE VARCHAR2(20) NOT NULL,
  CLAVE_TIPO_IVA CHAR(3) NOT NULL DEFAULT 'NOR',
  EMAIL_JEFE VARCHAR2(255) NULL
);

CREATE PUBLIC SYNONYM SC_CANAL FOR SICA_ADMIN.SC_CANAL;
/
