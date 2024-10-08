/*
 * $Id: SC_PLANTILLA_PANTALLA.sql,v 1.11 2008/02/22 18:25:13 ccovian Exp $
 * Autor: Jean C. Favila
 * Fecha: 06/01/2005
 *
 */

DROP TABLE SICA_ADMIN.SC_PLANTILLA_PANTALLA;

CREATE TABLE SICA_ADMIN.SC_PLANTILLA_PANTALLA (
  MNEMONICO VARCHAR2(15) NOT NULL PRIMARY KEY
, NOMBRE_PANTALLA VARCHAR2(100) NOT NULL
, METODO_REPORTE VARCHAR2(25) NULL
);

CREATE PUBLIC SYNONYM SC_PLANTILLA_PANTALLA FOR SICA_ADMIN.SC_PLANTILLA_PANTALLA;
/