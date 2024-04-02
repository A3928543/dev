-- ****************************************************************************************************************
-- * Reverso para el script pl_sc_factor_divisa_actual.sql
-- ****************************************************************************************************************

	-- Elimina las columnas, constraints e indices que fueron agregados a sc_factor_divisa_actual
	ALTER TABLE SICA_ADMIN.SC_FACTOR_DIVISA_ACTUAL 
		DROP COLUMN FACTOR;

	ALTER TABLE SICA_ADMIN.SC_FACTOR_DIVISA_ACTUAL 
		DROP COLUMN METODO_ACTUALIZACION;

	ALTER TABLE SICA_ADMIN.SC_FACTOR_DIVISA_ACTUAL 
		DROP COLUMN SPREAD_REFERENCIA;

	ALTER TABLE SICA_ADMIN.SC_FACTOR_DIVISA_ACTUAL 
		DROP COLUMN SPREAD_COMPRA;

	ALTER TABLE SICA_ADMIN.SC_FACTOR_DIVISA_ACTUAL 
		DROP COLUMN CARRY;

	ALTER TABLE SICA_ADMIN.SC_FACTOR_DIVISA_ACTUAL 
		DROP COLUMN FACTOR_COMPRA;

	ALTER TABLE SICA_ADMIN.SC_FACTOR_DIVISA_ACTUAL 
		DROP COLUMN SLACK;

	ALTER TABLE SICA_ADMIN.SC_FACTOR_DIVISA_ACTUAL 
		DROP CONSTRAINT SC_FACTOR_DIVISA_ACTUAL_PK;

	DROP INDEX INDX_SC_FACTOR_DIVISA_ACTUAL;

-- Se crea nuevamente el trigger original de SC_FACTOR_DIVISA
CREATE OR REPLACE TRIGGER SICA_ADMIN.ACTFACTORDIVISAACTUAL
    AFTER INSERT ON SC_FACTOR_DIVISA
    FOR EACH ROW
BEGIN
    DELETE sc_factor_divisa_actual
     WHERE from_id_divisa = :NEW.from_id_divisa
       AND to_id_divisa = :NEW.to_id_divisa;
    
    INSERT INTO sc_factor_divisa_actual
        (from_id_divisa, to_id_divisa, id_factor_divisa, ultima_modificacion)
    VALUES
      (:NEW.from_id_divisa,
       :NEW.to_id_divisa,
       :NEW.id_factor_divisa, SYSDATE);
END ActFactorDivisaActual;
/

ALTER TRIGGER SICA_ADMIN.ACTFACTORDIVISAACTUAL ENABLE; 

SHOW ERRORS