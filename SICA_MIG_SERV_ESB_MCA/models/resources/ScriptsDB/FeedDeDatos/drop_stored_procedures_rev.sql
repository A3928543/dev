-- ***************************************************************************************************************************
-- * Script de reverso que elimina los stored procedures creados
-- ***************************************************************************************************************************

DROP PROCEDURE SICA_ADMIN.SC_SP_ACTUALIZA_PREC_REF;

DROP PROCEDURE SICA_ADMIN.SC_SP_GENERAR_PIZARRONES_SICA;

--Revoke Permisos para las tablas externas al esquema SICA_ADMIN (en sp_genera_pizarrones.sql)
REVOKE SELECT ON site_admin.tes_forma_liquidacion FROM sica_admin;
REVOKE SELECT ON bupixe.bup_fecha_inhabil FROM sica_admin;