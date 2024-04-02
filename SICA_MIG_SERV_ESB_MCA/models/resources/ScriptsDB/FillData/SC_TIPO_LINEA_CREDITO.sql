/*
 * $Id: SC_TIPO_LINEA_CREDITO.sql,v 1.12 2008/02/22 18:25:28 ccovian Exp $
 * Autor: Javier Cordova (jcordova)
 * Fecha: 12/08/2005
 */

insert into SICA_ADMIN.SC_TIPO_LINEA_CREDITO (USA_LINEA_CREDITO, DESCRIPCION) values (1, 'Transferencia');
insert into SICA_ADMIN.SC_TIPO_LINEA_CREDITO (USA_LINEA_CREDITO, DESCRIPCION) values (2, 'Remesas en Camino');
insert into SICA_ADMIN.SC_TIPO_LINEA_CREDITO (USA_LINEA_CREDITO, DESCRIPCION) values (3, 'Cobro Inmediato');

COMMIT;
/