/*
 * $Id: SC_TIPO_LIMITE.sql,v 1.12 2008/02/22 18:25:29 ccovian Exp $
 * Autor: Ricardo Legorreta H.
 * Fecha: 26/01/2005
 *
 */

insert into SICA_ADMIN.SC_TIPO_LIMITE (ID_TIPO_LIMITE, ID_DIVISA, ES_GLOBAL, METODO_CALCULO, NOMBRE, ES_AL_CIERRE) values (1, 'MXN', 'S', 'n', 'Riesgo Regulatorio', 'S');
insert into SICA_ADMIN.SC_TIPO_LIMITE (ID_TIPO_LIMITE, ID_DIVISA, ES_GLOBAL, METODO_CALCULO, NOMBRE, ES_AL_CIERRE) values (2, null, 'N', 'n', 'VaR', 'S');
insert into SICA_ADMIN.SC_TIPO_LIMITE (ID_TIPO_LIMITE, ID_DIVISA, ES_GLOBAL, METODO_CALCULO, NOMBRE, ES_AL_CIERRE) values (3, null, 'N', 'n', 'Posición Larga', 'S');
insert into SICA_ADMIN.SC_TIPO_LIMITE (ID_TIPO_LIMITE, ID_DIVISA, ES_GLOBAL, METODO_CALCULO, NOMBRE, ES_AL_CIERRE) values (4, null, 'N', 'n', 'Posición Corta', 'S');
insert into SICA_ADMIN.SC_TIPO_LIMITE (ID_TIPO_LIMITE, ID_DIVISA, ES_GLOBAL, METODO_CALCULO, NOMBRE, ES_AL_CIERRE) values (5, null, 'N', 'n', 'Stop Loss', 'S');
insert into SICA_ADMIN.SC_TIPO_LIMITE (ID_TIPO_LIMITE, ID_DIVISA, ES_GLOBAL, METODO_CALCULO, NOMBRE, ES_AL_CIERRE) values (6, null, 'N', 'n', 'Posición Larga Intradía', 'S');
insert into SICA_ADMIN.SC_TIPO_LIMITE (ID_TIPO_LIMITE, ID_DIVISA, ES_GLOBAL, METODO_CALCULO, NOMBRE, ES_AL_CIERRE) values (7, null, 'N', 'n', 'Posición Corta Intradía', 'S');

COMMIT;
/