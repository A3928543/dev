/*
 * $Id: SC_H_DEAL.sql,v 1.11 2008/02/22 18:25:12 ccovian Exp $
 * Autor: Ricardo Legorreta H.
 * Fecha: 03/10/2005
 *
 * Historico de los encabezados de los deals.
 */

DROP TABLE SICA_ADMIN.SC_H_DEAL;

CREATE TABLE SICA_ADMIN.SC_H_DEAL (
        ID_DEAL NUMBER NOT NULL PRIMARY KEY,
        COMPRA CHAR(1) NOT NULL,
        ENVIAR_AL_CLIENTE CHAR(1) NOT NULL,
        EVENTOS_DEAL CHAR(16) NOT NULL,
        FACTURA CHAR(1) NOT NULL,
        FECHA_CAPTURA DATE NOT NULL,
        FECHA_LIQUIDACION DATE NOT NULL,
        ID_BROKER NUMBER NULL,
        ID_CANAL CHAR(3) NOT NULL,
        ID_DIRECCION_MENSAJERIA NUMBER NULL,
        ID_LIQUIDACION NUMBER NULL,
        ID_PROMOTOR NUMBER NULL,
        ID_USUARIO NUMBER NOT NULL,
        ID_MESA_CAMBIO NUMBER NOT NULL,
        MENSAJERIA CHAR(1) NOT NULL,
        NO_CUENTA VARCHAR2(33) NULL,
        OBSERVACIONES VARCHAR2(256) NULL,
        PAGO_ANTICIPADO CHAR(1) NOT NULL,
        TOMA_EN_FIRME CHAR(1) NOT NULL,
        SIMPLE CHAR(1) NOT NULL,
        STATUS_DEAL CHAR(2) NOT NULL,
        TIPO_VALOR CHAR(4) NOT NULL,
        NOMBRE_FACTURA VARCHAR2(120) NULL,
        RFC_FACTURA CHAR(13) NULL,
        ID_DIR_FACTURA NUMBER NULL,
        ID_FOLIO_SWAP NUMBER NULL 
);

CREATE PUBLIC SYNONYM SC_H_DEAL FOR SICA_ADMIN.SC_H_DEAL;
/
