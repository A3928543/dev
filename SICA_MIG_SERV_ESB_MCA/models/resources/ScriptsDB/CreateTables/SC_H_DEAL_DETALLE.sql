/*
* $Id: SC_H_DEAL_DETALLE.sql,v 1.11 2008/02/22 18:25:12 ccovian Exp $
* Autor: Ricardo Legorreta H.
* Fecha: 03/10/2005
*
*/

DROP TABLE SICA_ADMIN.SC_H_DEAL_DETALLE;

CREATE TABLE SICA_ADMIN.SC_H_DEAL_DETALLE (
        ID_DEAL_POSICION NUMBER NOT NULL PRIMARY KEY,
        CLAVE_FORMA_LIQUIDACION CHAR(6) NULL,
        COMISION_OFICIAL_USD NUMBER(8,2) NOT NULL,
        COMISION_COBRADA_USD NUMBER(8,2) NOT NULL,
        COMISION_COBRADA_MXN NUMBER(8,2) NOT NULL,
        COSTO_FORMA_LIQ NUMBER(10,2) NULL,
        DENOMINACION NUMBER(10,2) NULL,
        EVENTOS_DETALLE_DEAL CHAR(16) NOT NULL,
        FOLIO_DETALLE NUMBER(2) NOT NULL,
        ID_DEAL NUMBER NOT NULL,
        ID_FACTOR_DIVISA NUMBER NULL,
        ID_PLANTILLA NUMBER NULL,
        ID_PRECIO_REFERENCIA NUMBER NOT NULL,
        ID_SPREAD NUMBER NOT NULL,
        ID_LIQUIDACION_DETALLE NUMBER NULL,
        INSTRUCCIONES_BENEFICIARIO VARCHAR2(255) NULL,
        INSTRUCCIONES_INTERMEDIARIO VARCHAR2(255) NULL,
        INSTRUCCIONES_PAGADOR VARCHAR2(255) NULL,
        MNEMONICO VARCHAR2(15) NULL,
        OBSERVACIONES VARCHAR2(128) NULL,
        STATUS_DETALLE_DEAL CHAR(2) NOT NULL,
        TIPO_CAMBIO_MESA NUMBER(12,6) NOT NULL
);

CREATE PUBLIC SYNONYM SC_H_DEAL_DETALLE FOR SICA_ADMIN.SC_H_DEAL_DETALLE;
/
