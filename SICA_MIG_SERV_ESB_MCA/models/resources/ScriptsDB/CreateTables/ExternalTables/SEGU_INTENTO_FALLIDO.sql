/*
 * $Id: SEGU_INTENTO_FALLIDO.sql,v 1.11 2008/02/22 18:25:43 ccovian Exp $
 * Autor: Javier Cordova (jcordova)
 * Fecha: 25/05/2005
 */

DROP TABLE BUPIXE.SEGU_INTENTO_FALLIDO;

CREATE TABLE BUPIXE.SEGU_INTENTO_FALLIDO (
        ID_INTENTO_FALLIDO NUMBER NOT NULL, 
        ID_PERFIL NUMBER NULL,
        CLAVE_USUARIO VARCHAR2(33) NULL,
        SISTEMA VARCHAR2(20) NULL,
        FECHA_INTENTO DATE NULL,
        DIRECCION_IP VARCHAR2(15) NULL,
        CODIGO_ERROR NUMBER DEFAULT 0 NOT NULL,
        CONSTRAINT PK_SEGU_INTENTO_FALLIDO PRIMARY KEY (ID_INTENTO_FALLIDO)
);

CREATE PUBLIC SYNONYM SEGU_INTENTO_FALLIDO FOR BUPIXE.SEGU_INTENTO_FALLIDO;

CREATE SEQUENCE BUPIXE.SEGU_INTENTO_FALLIDO_SEQ start with 1 increment by 1;

CREATE PUBLIC SYNONYM SEGU_INTENTO_FALLIDO_SEQ FOR BUPIXE.SEGU_INTENTO_FALLIDO_SEQ;
/