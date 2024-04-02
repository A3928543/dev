-- ****************************************************************************************************************
-- * Crea una nueva tabla sc_factor_divisa_actual
-- ****************************************************************************************************************

DECLARE
	TYPE TYP_ARR_FAC_DIV IS TABLE OF sica_admin.sc_factor_divisa%ROWTYPE INDEX BY BINARY_INTEGER;
	arr_fac_div TYP_ARR_FAC_DIV;
	idx_fac_div INTEGER := 0;
BEGIN
    -- se elimina el trigger de sc_factor_divisa
	EXECUTE IMMEDIATE 'DROP TRIGGER SICA_ADMIN.ACTFACTORDIVISAACTUAL';

	-- se guardan los factores divisa actuales
	SELECT fd.* BULK COLLECT INTO arr_fac_div
	FROM sica_admin.sc_factor_divisa fd
	INNER JOIN sica_admin.sc_factor_divisa_actual fda
		ON fda.id_factor_divisa = fd.id_factor_divisa;

	-- se eliminan los registros existentes en sc_factor_divisa_actual
	DELETE FROM sica_admin.sc_factor_divisa_actual;
	
	-- se altera la tabla sc_factor_divisa_actual para que quede igual que sc_factor_divisa
	EXECUTE IMMEDIATE 'ALTER TABLE sica_admin.sc_factor_divisa_actual 
		ADD (factor NUMBER(12, 6) NOT NULL)';

	EXECUTE IMMEDIATE 'ALTER TABLE sica_admin.sc_factor_divisa_actual 
		ADD (metodo_actualizacion CHAR(1 BYTE) NOT NULL)';

	EXECUTE IMMEDIATE 'ALTER TABLE sica_admin.sc_factor_divisa_actual 
		ADD (spread_referencia NUMBER(12, 6) NOT NULL)';

	EXECUTE IMMEDIATE 'ALTER TABLE sica_admin.sc_factor_divisa_actual 
		ADD (spread_compra NUMBER(12, 6) NOT NULL)';

	EXECUTE IMMEDIATE 'ALTER TABLE sica_admin.sc_factor_divisa_actual 
		ADD (carry NUMBER(12, 6) NOT NULL)';

	EXECUTE IMMEDIATE 'ALTER TABLE sica_admin.sc_factor_divisa_actual 
		ADD (factor_compra NUMBER(12, 6) )';

	EXECUTE IMMEDIATE 'ALTER TABLE sica_admin.sc_factor_divisa_actual 
		ADD (slack NUMBER(12, 6) NOT NULL)';

	EXECUTE IMMEDIATE 'CREATE INDEX indx_sc_factor_divisa_actual ON sica_admin.sc_factor_divisa_actual
		(from_id_divisa, to_id_divisa) tablespace sica_inx';
	 
	EXECUTE IMMEDIATE 'ALTER TABLE sica_admin.sc_factor_divisa_actual
		ADD CONSTRAINT sc_factor_divisa_actual_pk PRIMARY KEY 
		(id_factor_divisa) USING INDEX tablespace sica_inx';
	
	-- Se insertan los registros de arr_fac_div en la tabla sc_factor_divisa_actual
	FOR idx_fac_div IN arr_fac_div.FIRST .. arr_fac_div.LAST LOOP
		EXECUTE IMMEDIATE 'INSERT INTO sica_admin.sc_factor_divisa_actual (
			id_factor_divisa, 
			from_id_divisa, 
			to_id_divisa, 
			factor, 
			metodo_actualizacion, 
			spread_referencia, 
			spread_compra, 
			carry, 
			ultima_modificacion, 
			factor_compra, 
			slack
		) VALUES (
			:p_id_factor_divisa, 
			:p_from_id_divisa, 
			:p_to_id_divisa, 
			:p_factor, 
			:p_metodo_actualizacion, 
			:p_spread_referencia, 
			:p_spread_compra, 
			:p_carry, 
			:p_ultima_modificacion, 
			:p_factor_compra, 
			:p_slack
		)' USING 
			arr_fac_div(idx_fac_div).id_factor_divisa, 
			arr_fac_div(idx_fac_div).from_id_divisa, 
			arr_fac_div(idx_fac_div).to_id_divisa, 
			arr_fac_div(idx_fac_div).factor, 
			arr_fac_div(idx_fac_div).metodo_actualizacion, 
			arr_fac_div(idx_fac_div).spread_referencia, 
			arr_fac_div(idx_fac_div).spread_compra, 
			arr_fac_div(idx_fac_div).carry, 
			arr_fac_div(idx_fac_div).ultima_modificacion, 
			arr_fac_div(idx_fac_div).factor_compra, 
			arr_fac_div(idx_fac_div).slack;
	END LOOP;
	
	COMMIT;
EXCEPTION
	WHEN OTHERS THEN
		DBMS_OUTPUT.PUT_LINE('Error al alterar tabla sc_factor_divisa_actual: ' || SQLCODE || ' - ' || SQLERRM );
		ROLLBACK;
END;
/
show errors;
