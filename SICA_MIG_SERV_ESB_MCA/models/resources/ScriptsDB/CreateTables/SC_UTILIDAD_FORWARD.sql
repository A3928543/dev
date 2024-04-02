/*
 * $Id: SC_UTILIDAD_FORWARD.sql,v 1.1 2008/08/11 20:25:09 ccovian Exp $
 * Autor: Jean C. Favila
 * Fecha: 03/06/2008
 *
 * Tabla de utilidades por forward.
 */

DROP TABLE SICA_ADMIN.SC_UTILIDAD_FORWARD
;

CREATE TABLE SICA_ADMIN.SC_UTILIDAD_FORWARD (
    ID_UTILIDAD_FORWARD CHAR(32) NOT NULL PRIMARY KEY,
    RECIBIMOS CHAR(1) NOT NULL,
    FECHA_VALOR CHAR(4) NOT NULL,
    FECHA DATE NOT NULL,
    ID_MESA_CAMBIO NUMBER NOT NULL,
    ID_DIVISA CHAR(3) NOT NULL,
    ID_DEAL NUMBER NOT NULL,
    INICIO_SWAP CHAR(1) NOT NULL
    MONTO NUMBER(16, 2) NOT NULL,
    MONTO_EQUIVALENTE NUMBER(16, 2) NOT NULL,
    TIPO_CAMBIO_CLIENTE NUMBER(14, 8) NOT NULL,
    UTILIDAD NUMBER(16, 2) NOT NULL
)
;

CREATE PUBLIC SYNONYM SC_UTILIDAD_FORWARD FOR SICA_ADMIN.SC_UTILIDAD_FORWARD;
/

