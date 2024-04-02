/*
 * $Id: SC_TRASPASO_PRODUCTO.sql,v 1.11 2008/02/22 18:25:12 ccovian Exp $
 * Autor: Ricardo Legorreta H.
 * Fecha: 30/10/2005
 *
 */

/* Cuando se modifique la estructura de esta tabla se debera
   de considerar la modificacion de su tabla de su historico
   correspondiente que se llama SC_H_TRASPASO_PRODUCTO
 */

DROP TABLE SICA_ADMIN.SC_TRASPASO_PRODUCTO;

CREATE TABLE SICA_ADMIN.SC_TRASPASO_PRODUCTO (
  ID_DEAL_POSICION NUMBER NOT NULL PRIMARY KEY
, MNEMONICO_TRASPASO VARCHAR2(15) NOT NULL
, ID_CANAL CHAR(3) NULL
, FECHA_OPER DATE NOT NULL
);

CREATE PUBLIC SYNONYM SC_TRASPASO_PRODUCTO FOR SICA_ADMIN.SC_TRASPASO_PRODUCTO;
/
