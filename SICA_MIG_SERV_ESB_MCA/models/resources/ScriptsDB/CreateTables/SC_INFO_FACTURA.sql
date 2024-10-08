/*
 * $Id: SC_INFO_FACTURA.sql,v 1.1.2.1 2013/12/21 03:11:45 mejiar Exp $
 * Autor: Jean C. Favila
 * Fecha: 06/11/2013
 */

DROP TABLE SICA_ADMIN.SC_INFO_FACTURA;

CREATE TABLE SICA_ADMIN.SC_INFO_FACTURA (
  ID_DEAL_DETALLE NUMBER NOT NULL PRIMARY KEY
, FACTURA_FOLIO VARCHAR2(36) NULL
, FACTURA_URL_PDF VARCHAR2(126) NULL
, FACTURA_URL_XML VARCHAR2(126) NULL
, FACTURA_ERROR VARCHAR2(255) NULL
);

CREATE PUBLIC SYNONYM SC_INFO_FACTURA FOR SICA_ADMIN.SC_INFO_FACTURA;
/