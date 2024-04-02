/*
 * $Id: SC_DEAL_STATUS_LOG.sql,v 1.11 2008/02/22 18:25:12 ccovian Exp $
 * Autor: Ricardo Legorreta H.
 * Fecha: 03/10/2005
 *
 */

/* Cuando se modifique la estructura de esta tabla se debera
   de considerar la modificacion de su tabla de su historico
   correspondiente que se llama SC_H_DEAL_STATUS_LOG
 */

DROP TABLE SICA_ADMIN.SC_DEAL_STATUS_LOG;

CREATE TABLE SICA_ADMIN.SC_DEAL_STATUS_LOG (
  ID_DEAL_STATUS_LOG NUMBER NOT NULL PRIMARY KEY
, ID_DEAL NUMBER NOT NULL
, FECHA_CAMBIO DATE NOT NULL
, STATUS_ANTERIOR CHAR(2) NOT NULL
, STATUS_NUEVO CHAR(2) NOT NULL
);

CREATE PUBLIC SYNONYM SC_DEAL_STATUS_LOG FOR SICA_ADMIN.SC_DEAL_STATUS_LOG;
/
