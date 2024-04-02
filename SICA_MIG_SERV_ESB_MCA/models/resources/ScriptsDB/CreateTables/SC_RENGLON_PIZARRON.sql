/*
 * $Id: SC_RENGLON_PIZARRON.sql,v 1.3 2008/02/22 18:25:13 ccovian Exp $
 * Autor: Jean C. Favila
 * Fecha: 28/06/2007
 *
 */

CREATE TABLE SICA_ADMIN.SC_RENGLON_PIZARRON (
        ID_RENGLON_PIZARRON NUMBER NOT NULL PRIMARY KEY,
        FECHA DATE NOT NULL,
        ID_CANAL CHAR(3) NOT NULL,
        ID_DIVISA CHAR(3) NOT NULL,
        ID_PRECIO_REFERENCIA NUMBER NOT NULL,
        ID_SPREAD NUMBER NOT NULL,
        ID_FACTOR_DIVISA NUMBER NOT NULL,
        FACTOR_DIVISA NUMBER NULL,
        CLAVE_FORMA_LIQUIDACION CHAR(6) NOT NULL,
        NOMBRE_FORMA_LIQUIDACION VARCHAR2(255) NOT NULL,
        COMPRA_CASH NUMBER(14,6) NOT NULL,
        VENTA_CASH NUMBER(14,6) NOT NULL,
        COMPRA_TOM NUMBER(14,6) NULL,
        VENTA_TOM NUMBER(14,6) NULL,
        COMPRA_SPOT NUMBER(14,6) NULL,
        VENTA_SPOT NUMBER(14,6) NULL,
        COMPRA_72HR NUMBER(14,6) NULL,
        VENTA_72HR NUMBER(14,6) NULL,
        COMPRA_VFUT NUMBER(14,6) NULL,
        VENTA_VFUT NUMBER(14,6) NULL 
);

CREATE PUBLIC SYNONYM SC_RENGLON_PIZARRON FOR SICA_ADMIN.SC_RENGLON_PIZARRON;
/