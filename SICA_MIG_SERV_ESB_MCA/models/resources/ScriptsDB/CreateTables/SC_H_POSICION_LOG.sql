/*
 * $Id: SC_H_POSICION_LOG.sql,v 1.11 2008/02/22 18:25:13 ccovian Exp $
 * Autor: Ricardo Legorreta H.
 * Fecha: 03/10/2005
 *
 */

DROP TABLE SICA_ADMIN.SC_H_POSICION_LOG;

CREATE TABLE SICA_ADMIN.SC_H_POSICION_LOG (
        ID_POSICION_LOG NUMBER NOT NULL PRIMARY KEY,
        TIPO_VALOR CHAR(4) NOT NULL,
        ID_CANAL CHAR(3) NOT NULL,
        ID_DEAL NUMBER NULL,
        ID_DEAL_POSICION NUMBER NULL,
        ID_DIVISA CHAR(3) NOT NULL,
        ID_MESA_CAMBIO NUMBER NOT NULL,
        CLAVE_FORMA_LIQUIDACION CHAR(6) NOT NULL,
        ID_USUARIO NUMBER NOT NULL,
        MONTO NUMBER(16,2) NOT NULL,
        MONTO_MN NUMBER(16,2) NOT NULL,
        NOMBRE_CLIENTE VARCHAR2(120) NULL, 
        PRECIO_REFERENCIA NUMBER(16,6) NOT NULL,
        TIPO_CAMBIO_CLIENTE NUMBER(12,6) NOT NULL,
        TIPO_CAMBIO_MESA NUMBER(12,6) NOT NULL,
        TIPO_OPERACION CHAR(2) NOT NULL,
        ULTIMA_MODIFICACION DATE NOT NULL
);

CREATE PUBLIC SYNONYM SC_H_POSICION_LOG FOR SICA_ADMIN.SC_H_POSICION_LOG;
/