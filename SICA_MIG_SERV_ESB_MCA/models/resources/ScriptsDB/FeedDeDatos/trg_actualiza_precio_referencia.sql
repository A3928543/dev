-- ========================================================================================================================================
-- =
-- = SICA_ADMIN.ACTUALIZAPRECIOREFERENCIA: El trigger se modifica para que solo llame al procedimiento SC_SP_GENERAR_PIZARRONES_SICA
-- =
-- ========================================================================================================================================

DROP TRIGGER SICA_ADMIN.ACTUALIZAPRECIOREFERENCIA;

CREATE OR REPLACE TRIGGER SICA_ADMIN.ACTUALIZAPRECIOREFERENCIA
    AFTER INSERT ON SICA_ADMIN.SICA_VARIACION  FOR EACH ROW
DECLARE
	l_caller INTEGER;
BEGIN
	-- Identifica quien esta insertando
	IF (:NEW.origen IS NULL) THEN 
		l_caller := 1; -- Excel de REUTERS
	ELSE
		l_caller := 3; -- RMDS
	END IF;
	SICA_ADMIN.SC_SP_GENERAR_PIZARRONES_SICA(
		l_caller, 
		:NEW.fecha,
		:NEW.variacion_mid,
		:NEW.variacion_vta_spot,
		:NEW.variacion_vta_spot_ask,
		:NEW.cad_variacion_vta_spot,
		:NEW.cad_variacion_vta_spot_ask,
		:NEW.chf_variacion_vta_spot,
		:NEW.chf_variacion_vta_spot_ask,
		:NEW.eur_variacion_vta_spot,
		:NEW.eur_variacion_vta_spot_ask,
		:NEW.gbp_variacion_vta_spot,
		:NEW.gbp_variacion_vta_spot_ask,
		:NEW.jpy_variacion_vta_spot,
		:NEW.jpy_variacion_vta_spot_ask
	);
	
END ActualizaPrecioReferencia;
/
show errors;