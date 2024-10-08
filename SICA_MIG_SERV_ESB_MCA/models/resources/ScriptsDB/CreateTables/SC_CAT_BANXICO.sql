/*
 * $Id: SC_CAT_BANXICO.sql,v 1.11 2008/02/22 18:25:13 ccovian Exp $
 * Autor: Javier Cordova
 * Fecha: 16/02/2007
 *
 */

DROP TABLE SICA_ADMIN.SC_CAT_BANXICO;

CREATE TABLE SICA_ADMIN.SC_CAT_BANXICO (
    DESCRIPCION VARCHAR2(30) NOT NULL
    ,RIM_NO NUMBER NOT NULL
    ,CVE_BANXICO CHAR(8) NOT NULL
    ,CREATE_DT DATE NULL
    ,EMPL_ID NUMBER NULL
    ,STATUS CHAR(12) NULL
);

CREATE PUBLIC SYNONYM SC_CAT_BANXICO FOR SICA_ADMIN.SC_CAT_BANXICO;
/
