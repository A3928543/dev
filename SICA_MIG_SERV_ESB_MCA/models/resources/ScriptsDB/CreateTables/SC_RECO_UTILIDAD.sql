/*
 * $Id: SC_RECO_UTILIDAD.sql,v 1.11 2008/02/22 18:25:11 ccovian Exp $
 * Autor: Ricardo Legorreta H.
 * Fecha: 30/05/2006
 *
 */

CREATE TABLE SICA_ADMIN.SC_RECO_UTILIDAD (
  ID_DEAL_POSICION NUMBER NOT NULL PRIMARY KEY
, FECHA_OPERACION DATE NOT NULL
, UTILIDAD_GLOBAL NUMBER(18, 2) NOT NULL
, UTILIDAD_MESA NUMBER(18, 2) NOT NULL
, TIPO_RECO CHAR(2) NOT NULL
, TIPO_CAMBIO_OTRA_DIV_REF NUMBER(16,6) DEFAULT 1 NOT NULL
);

CREATE PUBLIC SYNONYM SC_RECO_UTILIDAD FOR SICA_ADMIN.SC_RECO_UTILIDAD;
/