/*
 * $Id: SC_H_PRECIO_REFERENCIA.sql,v 1.12 2008/02/22 18:25:11 ccovian Exp $
 * Autor: Jean C. Favila.
 * Fecha: 12/01/2005
 *
 */

DROP TABLE SICA_ADMIN.SC_H_PRECIO_REFERENCIA;

CREATE TABLE SICA_ADMIN.SC_H_PRECIO_REFERENCIA (
         ID_PRECIO_REFERENCIA NUMBER NOT NULL PRIMARY KEY,
        METODO_ACTUALIZACION CHAR(1) NOT NULL,
        PRECIO_SPOT NUMBER(12,6) NULL,
        PRECIO_COMPRA NUMBER(12,6) NULL,
        PRECIO_VENTA NUMBER(12,6) NULL,
        MID_SPOT NUMBER(12,6) NULL,
        ULTIMA_MODIFICACION DATE NOT NULL 
);

CREATE PUBLIC SYNONYM SC_H_PRECIO_REFERENCIA FOR SICA_ADMIN.SC_H_PRECIO_REFERENCIA;
/
