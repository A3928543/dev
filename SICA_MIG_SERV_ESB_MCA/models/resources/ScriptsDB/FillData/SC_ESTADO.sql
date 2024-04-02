/*
 * $Id: SC_ESTADO.sql,v 1.12 2008/02/22 18:25:29 ccovian Exp $
 * Autor: Ricardo Legorreta H.
 * Fecha: 08/04/2005
 *
 */

insert into SICA_ADMIN.SC_ESTADO (ID_ESTADO, ACTUAL, ESTADO, HORA_INICIO, HORA_FIN, NOMBRE_APLICACION, SIG_ESTADO, URL_APLICACION) values (3, 'N', 'Operación vespertina', '16:46', null, null, 4, 'HorarioVespertino');
insert into SICA_ADMIN.SC_ESTADO (ID_ESTADO, ACTUAL, ESTADO, HORA_INICIO, HORA_FIN, NOMBRE_APLICACION, SIG_ESTADO, URL_APLICACION) values (4, 'N', 'Fin de las Liquidaciones', '19:43', null, null, 5, 'FinLiquidaciones');
insert into SICA_ADMIN.SC_ESTADO (ID_ESTADO, ACTUAL, ESTADO, HORA_INICIO, HORA_FIN, NOMBRE_APLICACION, SIG_ESTADO, URL_APLICACION) values (5, 'N', 'Generación de los movimientos Contables', '19:53', null, null, 6, 'GeneracionContable');
insert into SICA_ADMIN.SC_ESTADO (ID_ESTADO, ACTUAL, ESTADO, HORA_INICIO, HORA_FIN, NOMBRE_APLICACION, SIG_ESTADO, URL_APLICACION) values (6, 'N', 'Cierre de día', '20:03', null, null, 5, null);
insert into SICA_ADMIN.SC_ESTADO (ID_ESTADO, ACTUAL, ESTADO, HORA_INICIO, HORA_FIN, NOMBRE_APLICACION, SIG_ESTADO, URL_APLICACION) values (7, 'N', 'Operación nocturna', '00:00', null, 'Fin de operación nocturna', 0, 'FinOperacionNocturna');
insert into SICA_ADMIN.SC_ESTADO (ID_ESTADO, ACTUAL, ESTADO, HORA_INICIO, HORA_FIN, NOMBRE_APLICACION, SIG_ESTADO, URL_APLICACION) values (0, 'N', 'Inicio del día', '06:59', null, 'Administración Pizarrón', 1, 'AdminPizarron');
insert into SICA_ADMIN.SC_ESTADO (ID_ESTADO, ACTUAL, ESTADO, HORA_INICIO, HORA_FIN, NOMBRE_APLICACION, SIG_ESTADO, URL_APLICACION) values (2, 'N', 'Operación restringida', '15:31', null, null, 3, null);
insert into SICA_ADMIN.SC_ESTADO (ID_ESTADO, ACTUAL, ESTADO, HORA_INICIO, HORA_FIN, NOMBRE_APLICACION, SIG_ESTADO, URL_APLICACION) values (8, 'N', 'Sistema Bloqueado', null, null, 'Desbloquear el sistema', 2, 'DesbloquearSistema');
insert into SICA_ADMIN.SC_ESTADO (ID_ESTADO, ACTUAL, ESTADO, HORA_INICIO, HORA_FIN, NOMBRE_APLICACION, SIG_ESTADO, URL_APLICACION) values (1, 'S', 'Operación normal', '06:59', '15:30', null, 2, null);

COMMIT;
/