/*
 * $Id: SC_TIPO_TRASPASO_PRODUCTO.sql,v 1.12 2008/02/22 18:25:29 ccovian Exp $
 * Autor: Ricardo Legorreta H.
 * Fecha: 14/02/2005
 *
 */

insert into SICA_ADMIN.SC_TIPO_TRASPASO_PRODUCTO (MNEMONICO_TRASPASO, DESCRIPCION, NETEO, DE, DE_OPER, DE_SIGNO, A, A_OPER, A_SIGNO, VAL_MONTO) values ('ACREDCHVIAJ ', 'Acreditaci�n de traveler-checks', '0', 'CHVIAJ', 'CO', -1, 'TRAEXT', 'CO', 1, 'compra');
insert into SICA_ADMIN.SC_TIPO_TRASPASO_PRODUCTO (MNEMONICO_TRASPASO, DESCRIPCION, NETEO, DE, DE_OPER, DE_SIGNO, A, A_OPER, A_SIGNO, VAL_MONTO) values ('PAGO_CHVIAJ ', 'Pago de traveler checks', '0', 'CHVIAJ', 'VE', -1, 'TRAEXT', 'VE', 1, 'venta');
insert into SICA_ADMIN.SC_TIPO_TRASPASO_PRODUCTO (MNEMONICO_TRASPASO, DESCRIPCION, NETEO, DE, DE_OPER, DE_SIGNO, A, A_OPER, A_SIGNO, VAL_MONTO) values ('ACREDDOCEXT ', 'Acreditaci�n de remesas', '0', 'DOCEXT', 'CO', -1, 'TRAEXT', 'CO', 1, 'compra');
insert into SICA_ADMIN.SC_TIPO_TRASPASO_PRODUCTO (MNEMONICO_TRASPASO, DESCRIPCION, NETEO, DE, DE_OPER, DE_SIGNO, A, A_OPER, A_SIGNO, VAL_MONTO) values ('COBRODOCEXT ', 'Cobro de Giros', '0', 'DOCEXT', 'VE', -1, 'TRAEXT', 'VE', 1, 'venta');
insert into SICA_ADMIN.SC_TIPO_TRASPASO_PRODUCTO (MNEMONICO_TRASPASO, DESCRIPCION, NETEO, DE, DE_OPER, DE_SIGNO, A, A_OPER, A_SIGNO, VAL_MONTO) values ('TRADEMEXDOL ', 'Traspaso de Mex-d�lares a Transferencias', '1', 'MEXDOL', 'CO', -1, 'TRAEXT', 'CO', 1, 'largo');
insert into SICA_ADMIN.SC_TIPO_TRASPASO_PRODUCTO (MNEMONICO_TRASPASO, DESCRIPCION, NETEO, DE, DE_OPER, DE_SIGNO, A, A_OPER, A_SIGNO, VAL_MONTO) values ('TRAA_MEXDOL ', 'Traspaso de Transferencias a Mex-d�lares', '1', 'MEXDOL', 'VE', -1, 'TRAEXT', 'VE', 1, 'corto');
insert into SICA_ADMIN.SC_TIPO_TRASPASO_PRODUCTO (MNEMONICO_TRASPASO, DESCRIPCION, NETEO, DE, DE_OPER, DE_SIGNO, A, A_OPER, A_SIGNO, VAL_MONTO) values ('FONDEMEXDOL ', 'Fondeo de Mex-d�lares', '0', 'TRAEXT', 'VE', 1, 'MEXDOL', 'CO', 1, null);
insert into SICA_ADMIN.SC_TIPO_TRASPASO_PRODUCTO (MNEMONICO_TRASPASO, DESCRIPCION, NETEO, DE, DE_OPER, DE_SIGNO, A, A_OPER, A_SIGNO, VAL_MONTO) values ('LARGOEFECTI ', 'Dep�sito cuenta por largos de efectivo', '1', 'EFECTI', 'CO', -1, 'TRAEXT', 'CO', 1, 'largo');
insert into SICA_ADMIN.SC_TIPO_TRASPASO_PRODUCTO (MNEMONICO_TRASPASO, DESCRIPCION, NETEO, DE, DE_OPER, DE_SIGNO, A, A_OPER, A_SIGNO, VAL_MONTO) values ('CORTOEFECTI ', 'Cubrir cortos de posici�n de efectivo', '1', 'EFECTI', 'VE', -1, 'TRAEXT', 'VE', 1, 'corto');

COMMIT;
/