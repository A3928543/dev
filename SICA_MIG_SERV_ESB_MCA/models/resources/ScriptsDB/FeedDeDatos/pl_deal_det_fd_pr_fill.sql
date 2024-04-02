-- ****************************************************************************************************************
-- * Setea los campos: pre_ref_mid_spot, pre_ref_spot y factor_divisa para la tabla SC_DEAL_DETALLE, obteniendo
-- * los datos de SC_PRECIO_REFERENCIA (dos primeros campos) y SC_FACTOR_DIVISA (tercer campo), utilizando las
-- * referencias disponibles hacia estas dos últimas tablas en SC_DEAL_DETALLE (ID_PRECIO_REFERENCIA e 
-- * ID_FACTOR_DIVISA respectivamente)
-- ****************************************************************************************************************

DECLARE
	-- Fecha inicial y fecha final a enviar como parámetros al proceso
	ini_date_val VARCHAR2(20);
	end_date_val VARCHAR2(20);
	
	-- Constantes para las fechas
	C_INI_DATE_TIME CONSTANT VARCHAR(10) := '00:00:00';
	C_END_DATE_TIME CONSTANT VARCHAR(10) := '23:59:59';
	C_DATE_FORMAT CONSTANT VARCHAR(30) := 'DD/MM/YYYY HH24:MI:SS';
	
	--
	-- Procedimiento para completar los campos pre_ref_spot, pre_ref_mid_spot y factor_divisa de la tabla
	-- SC_DEAL_DETALLE
	--
	PROCEDURE fill_pr_fd_fields_deal_det (ini_date IN DATE, end_date IN DATE) IS
		procesed_rows_counter INTEGER := 0; 
	
		CURSOR cur_deal_det_to_complete IS
			SELECT DISTINCT dd.id_deal_posicion, pr.precio_spot, pr.mid_spot, fd.factor
			FROM sc_deal_detalle dd 
				INNER JOIN sc_deal d 
					ON dd.id_deal = d.id_deal
				LEFT OUTER JOIN sc_precio_referencia pr 
					ON pr.id_precio_referencia = dd.id_precio_referencia
				LEFT OUTER JOIN sc_factor_divisa fd 
					ON fd.id_factor_divisa = dd.id_factor_divisa
			WHERE d.fecha_captura >= ini_date AND d.fecha_captura <= end_date;
	BEGIN
		DBMS_OUTPUT.PUT_LINE(SYSTIMESTAMP || ' Feed de Datos: Comienza proceso de complementación de campos en sc_deal_detalle');
		FOR rec_current IN cur_deal_det_to_complete LOOP
			UPDATE sc_deal_detalle SET 
				pre_ref_spot = rec_current.precio_spot,
				pre_ref_mid_spot = rec_current.mid_spot,
				factor_divisa = rec_current.factor
			WHERE id_deal_posicion = rec_current.id_deal_posicion;
			procesed_rows_counter := procesed_rows_counter + 1;
		END LOOP;
		COMMIT;
		DBMS_OUTPUT.PUT_LINE(SYSTIMESTAMP || ' Feed de Datos: Finaliza proceso de complementación de campos en sc_deal_detalle');
		DBMS_OUTPUT.PUT_LINE('Feed de Datos: Registros procesados = ' || procesed_rows_counter);
	EXCEPTION
		WHEN OTHERS THEN
			DBMS_OUTPUT.PUT_LINE('Error al completar campos de factor divisa y precio referencia: ' || SQLCODE || ' - ' || SQLERRM );
			ROLLBACK;
	END;
	
BEGIN
	-- ### Establecer rango de fechas 
	ini_date_val := '01/01/2010' || ' ' || C_INI_DATE_TIME;
	end_date_val := '31/12/2010' || ' ' || C_END_DATE_TIME;
	-- ### Establecer rango de fechas 
	
	-- Llamada al procedimiento
	fill_pr_fd_fields_deal_det(
		TO_DATE(ini_date_val, C_DATE_FORMAT),
		TO_DATE(end_date_val, C_DATE_FORMAT)
	);
END;
/
show errors;
