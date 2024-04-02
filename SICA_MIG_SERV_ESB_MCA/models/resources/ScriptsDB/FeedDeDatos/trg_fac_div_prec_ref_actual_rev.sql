-- ***********************************************************************************************************************
-- * Reverso para trigger's INSERT_FACTOR_DIVISA, INSERT_PRECIO_REFERENCIA
-- ***********************************************************************************************************************

ALTER TABLE SICA_ADMIN.SC_FACTOR_DIVISA_ACTUAL DISABLE ALL TRIGGERS;
DROP TRIGGER SICA_ADMIN.INSERT_FACTOR_DIVISA;
ALTER TABLE SICA_ADMIN.SC_PRECIO_REFERENCIA_ACTUAL DISABLE ALL TRIGGERS;
DROP TRIGGER SICA_ADMIN.INSERT_PRECIO_REFERENCIA;

show errors
