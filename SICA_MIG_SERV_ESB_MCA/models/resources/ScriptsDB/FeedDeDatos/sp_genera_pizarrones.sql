GRANT SELECT ON site_admin.tes_forma_liquidacion TO sica_admin;
GRANT SELECT ON bupixe.bup_fecha_inhabil TO sica_admin;


-- ========================================================================================================================================
-- =
-- = SICA_ADMIN.SC_SP_GENERAR_PIZARRONES_SICA: Se encarga de actualizar el precio referencia y/o los pizarrones según el origen de la
-- = llamada
-- =
-- ========================================================================================================================================

CREATE OR REPLACE PROCEDURE SICA_ADMIN.SC_SP_GENERAR_PIZARRONES_SICA (
	p_caller IN INTEGER,
	p_fecha IN sica_variacion.fecha%TYPE,
	p_variacion_mid IN sica_variacion.variacion_mid%TYPE,
	p_variacion_vta_spot IN sica_variacion.variacion_vta_spot%TYPE,
	p_variacion_vta_spot_ask IN sica_variacion.variacion_vta_spot_ask%TYPE,
	p_cad_variacion_vta_spot IN sica_variacion.cad_variacion_vta_spot%TYPE,
	p_cad_variacion_vta_spot_ask IN sica_variacion.cad_variacion_vta_spot_ask%TYPE,
	p_chf_variacion_vta_spot IN sica_variacion.chf_variacion_vta_spot%TYPE,
	p_chf_variacion_vta_spot_ask IN sica_variacion.chf_variacion_vta_spot_ask%TYPE,
	p_eur_variacion_vta_spot IN sica_variacion.eur_variacion_vta_spot%TYPE,
	p_eur_variacion_vta_spot_ask IN sica_variacion.eur_variacion_vta_spot_ask%TYPE,
	p_gbp_variacion_vta_spot IN sica_variacion.gbp_variacion_vta_spot%TYPE,
	p_gbp_variacion_vta_spot_ask IN sica_variacion.gbp_variacion_vta_spot_ask%TYPE,
	p_jpy_variacion_vta_spot IN sica_variacion.jpy_variacion_vta_spot%TYPE,
	p_jpy_variacion_vta_spot_ask IN sica_variacion.jpy_variacion_vta_spot_ask%TYPE
) IS


	-- ######################################################################################################################
	-- #### CONSTANTES
	-- ######################################################################################################################
	
	
	-- #### Constantes para identificar la fuente de notificación para actualizar los pizarrones
	
	C_CALLER_REUTERS 		CONSTANT INTEGER := 1;
	C_CALLER_SICA 			CONSTANT INTEGER := 2;
	C_CALLER_RMDS 			CONSTANT INTEGER := 3;
	
	-- #### Constantes correspondientes al procedimiento log_msg(NUMBER, VARCHAR2)
	
	-- Tipo de mensaje de error
	ERRMSG CONSTANT NUMBER := -1;
	
	-- Tipo de mensaje de información
	INFMSG CONSTANT NUMBER := 0;
	
	-- Tipo de mensaje de timming (medición del tiempo de ejecución del proceso)
	TIMEMSG CONSTANT NUMBER := 1;
	
	-- Debug activado / desactivado
	DEBUG_ENABLED CONSTANT BOOLEAN := TRUE;
	
	-- #### Constantes para la colección de carries nombrados según las diferencias entre fechas valor consecutivas
	
	C_DIF_TOM_CASH 		CONSTANT VARCHAR2(30) 	:= 'DIF_TOM_CASH';
	C_DIF_SPOT_TOM 		CONSTANT VARCHAR2(30) 	:= 'DIF_SPOT_TOM';
	C_DIF_72HR_SPOT 	CONSTANT VARCHAR2(30) 	:= 'DIF_72HR_SPOT';
	C_DIF_VFUT_72HR 	CONSTANT VARCHAR2(30) 	:= 'DIF_VFUT_72HR';
	
	-- #### Constantes para las divisas
	
	C_DOLAR_US			CONSTANT VARCHAR2(5) := 'USD';
	C_DOLAR_CANADIENSE 	CONSTANT VARCHAR2(5) := 'CAD';
	C_EURO				CONSTANT VARCHAR2(5) := 'EUR';
	C_LIBRA_ESTERLINA 	CONSTANT VARCHAR2(5) := 'GBP';
	C_FRANCO_SUIZO 		CONSTANT VARCHAR2(5) := 'CHF';
	C_YEN 				CONSTANT VARCHAR2(5) := 'JPY';
	
	C_PESO_MEX	CONSTANT VARCHAR2(5) := 'MXN';
	
	-- #### Constantes para la colección resultante de la funcion get_factor_divisa_correcto
	
	C_SPREAD_COMPRA 		CONSTANT VARCHAR2(30) 	:= 'SPREAD_COMPRA';
	C_SPREAD_REFERENCIA 	CONSTANT VARCHAR2(30) 	:= 'SPREAD_REFERENCIA';
	C_FACTOR 				CONSTANT VARCHAR2(30) 	:= 'FACTOR';
	
	-- #### Constantes para campos booleanos cuyos valores pueden ser 'S' o 'N'
	
	C_SI	CONSTANT VARCHAR2(2) := 'S';
	C_NO	CONSTANT VARCHAR2(2) := 'N';
	
	-- #### Constantes para los valores de id_parametro en la tabla sc_parametro
	
	C_FACTOR_AUTOMATICO CONSTANT VARCHAR2(20) := 'FACTOR_AUTOMATICO'; 
	C_SPREAD_VESPERTINO CONSTANT VARCHAR2(20) := 'SPREAD_VESPERTINO';
	
	-- #### Constantes para la coleccion resultante de la funcion get_var_div_cv
	
	C_XD_VAR_VTA_SPOT CONSTANT VARCHAR2(30) := 'C_XD_VAR_VTA_SPOT';
	C_XD_VAR_VTA_SPOT_ASK CONSTANT VARCHAR(30) := 'C_XD_VAR_VTA_SPOT_ASK';
	
	-- ###
	
	C_CALC_DECIMALES CONSTANT INTEGER := 6; -- Especifica la precición en decimales de los cálculos
	
	-- ###
	
	C_COMPRA CONSTANT BOOLEAN := TRUE; -- compra
	C_VENTA CONSTANT BOOLEAN := FALSE; -- venta
	
	-- ### Metodos de actualizacion
	
	C_AUTOMATICO CONSTANT VARCHAR2(2) := 'A';
	C_MID_SPOT CONSTANT VARCHAR2(2) := 'P';
	C_MANUAL CONSTANT VARCHAR(2) := 'M';
	C_VESPERTINO CONSTANT VARCHAR(2) := 'V';
	
	-- ### Claves de formas de pago
	
	C_DOCEXT CONSTANT VARCHAR2(20) := 'DOCEXT';
	C_MEXDOL CONSTANT VARCHAR2(20) := 'MEXDOL';
	
	-- ### Descripciones de formas de pago para sucursales
	
	C_REMESA_GIRO CONSTANT VARCHAR2(20) := 'REMESA / GIRO';
	C_CHEQUE_CANAL_USD CONSTANT VARCHAR(20) := 'CHEQUE DE CAJA USD';
	
	-- ###
	
	C_TIPO_OPER_FDD CONSTANT VARCHAR2(20) := 'Feed de Datos'; -- Define el tipo de operacion de feed de datos para log_auditoria
	C_FDD_LOG_SWITCH CONSTANT VARCHAR2(20) := 'FDD_LOG_SWITCH'; -- Define el parámetro de encendido o apagado del log
	C_FDD_TIME_SWITCH CONSTANT VARCHAR2(20) := 'FDD_TIME_SWITCH'; -- Define el parametro de encendido o apagado del log de timming 
	C_FDD_ORIGEN_VARIACION CONSTANT VARCHAR2(25) := 'FDD_ORIGEN_VARIACION'; -- Define el parametro donde se configura el origen de la variacion (Excel o RMDS) a ser aceptado para generar los pizarrones
	C_ORIG_VAR_EXCEL CONSTANT VARCHAR2(20) := 'ORIG_VAR_EXCEL'; -- Identifica como origen de la variación al excel de reuters
	C_ORIG_VAR_RMDS CONSTANT VARCHAR2(20) := 'ORIG_VAR_RMDS'; -- Identifica como origen de la variación al RMDS
	C_MSG_LOG_AUDIT_MAX_LEN CONSTANT INTEGER := 512;

	
	-- ######################################################################################################################
	-- #### TIPOS
	-- ######################################################################################################################
	
	
	-- Arreglo de factores divisa
	TYPE TYP_ARR_FAC_DIV_ACT IS TABLE OF sc_factor_divisa_actual%ROWTYPE INDEX BY BINARY_INTEGER;
	
	-- Arreglo de spreads
	TYPE TYP_ARR_SPREAD IS TABLE OF sc_spread%ROWTYPE INDEX BY BINARY_INTEGER;
  
	-- Arreglo de enteros indexados por strings
	TYPE TYP_ARR_INT_IDX_STR IS TABLE OF INTEGER INDEX BY VARCHAR2(30);
	
	-- Arreglo de números enteros o fraccionarios indexados por strings
	TYPE TYP_ARR_NUM_IDX_STR IS TABLE OF NUMBER INDEX BY VARCHAR2(30);
	
	-- Arreglo de flotantes indexados por strings
	TYPE TYP_ARR_FLOAT_IDX_STR IS TABLE OF FLOAT INDEX BY VARCHAR2(30);
	
	-- Arreglo de fechas
	TYPE TYP_ARR_FECHA IS TABLE OF DATE INDEX BY BINARY_INTEGER;
	
	-- Arreglo de renglones de pizarrón
	TYPE TYP_ARR_RENGL_PIZRRN IS TABLE OF sc_renglon_pizarron%ROWTYPE INDEX BY BINARY_INTEGER;
	
	
	-- ######################################################################################################################
	-- #### EXCEPCIONES
	-- ######################################################################################################################

	
	-- variación actual no encontrada
	ex_var_act_not_found EXCEPTION;
	
	-- precio de referencia actual no encontrado
	ex_prec_ref_not_found EXCEPTION;
	
	-- factores divisa no encontrados
	ex_facs_div_not_found EXCEPTION;
	
	-- spreads no encontrados
	ex_spreads_not_found EXCEPTION;
	
	-- factor divisa no encontrado
	ex_fac_div_not_found EXCEPTION;
	
	-- divisa no encontrada
	ex_div_not_found EXCEPTION;
	
	-- Nombre de forma de liquidación no encontrado
	ex_nom_form_liqu_not_found EXCEPTION;
	
	-- Divisa no valida
	ex_divisa_not_valid EXCEPTION;
	
	-- No esta definido el precio de compra spot para el método 
	--ex_prec_compr_spot_undefined EXCEPTION;
	ex_metodo_act_not_valid EXCEPTION;

	
	-- ######################################################################################################################
	-- #### VARIABLES GLOBALES
	-- ######################################################################################################################

	
	g_log_counter INTEGER;
	log_enabled BOOLEAN := NULL;
	timming_enabled BOOLEAN := NULL;
	factores_automaticos BOOLEAN := NULL;
	spread_vespertino NUMBER := NULL;
	origen_variacion sc_parametro.valor%TYPE := NULL;
	
	
	-- ######################################################################################################################
	-- #### FUNCIONES Y PROCEDIMIENTOS DE USO GENERAL
	-- ######################################################################################################################
	
	
	-- *************************************************************************************************************
	-- * Inserta en sc_log_auditoria
	-- *************************************************************************************************************
	PROCEDURE log_audit(msg IN VARCHAR2) IS
		PRAGMA AUTONOMOUS_TRANSACTION;
		msg_to_log VARCHAR2(5000);
		l_id_log_auditoria sc_log_auditoria.id_log_auditoria%TYPE;
		l_curr_timestamp TIMESTAMP;
	BEGIN
		g_log_counter := g_log_counter + 1;
		l_curr_timestamp := SYSTIMESTAMP;
		l_id_log_auditoria := TO_CHAR(l_curr_timestamp,'DDMMYYYY-HH24MISSFF-') || g_log_counter;
		msg_to_log := TO_CHAR(l_curr_timestamp,'DD/MM/YYYY HH24:MI:SS:FF') || ' - SC_SP_GENERAR_PIZARRONES_SICA: ' || msg;		
		INSERT INTO sc_log_auditoria (
			id_log_auditoria,
			datos_adicionales,
			direccion_ip,
			fecha,
			id_persona,
			id_usuario,
			ticket,
			tipo_operacion
		) VALUES (
			l_id_log_auditoria,
			msg_to_log,
			'localhost',
			sysdate,
			101010,
			101010,
			'ticket',
			C_TIPO_OPER_FDD
		);
		COMMIT;
	EXCEPTION
		WHEN OTHERS THEN
			dbms_output.put_line('Error de log: ' || msg_to_log || ': ' || SQLCODE || ' - ' || SQLERRM);
	END;
	
	-- *********************************************************************************************
	-- * Imprime un mensaje en la consola
	-- * 
	-- * params: 
	-- *	msg_type: Tipo de mensaje 0 = info, -1 = error
	-- *	msg_text: Cuerpo del mensaje
	-- *********************************************************************************************
	PROCEDURE log_msg(msg_type NUMBER, msg_text VARCHAR2) IS
		err_type_str VARCHAR2(10) := '';
		msg_to_log VARCHAR2(5000) := '';
	BEGIN
		IF msg_type = ERRMSG THEN
			msg_to_log := REPLACE(msg_text, CHR(10), ',');
			msg_to_log := SUBSTR(msg_text, 1,  C_MSG_LOG_AUDIT_MAX_LEN - 1 );
			log_audit('ERROR: ' || msg_to_log);
		ELSIF msg_type = INFMSG AND log_enabled THEN
			DBMS_OUTPUT.PUT_LINE(SYSTIMESTAMP || ' - ' || 'INFO: ' || msg_text);
		ELSIF msg_type = TIMEMSG AND timming_enabled THEN
			log_audit(msg_text);
		END IF;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene la variación actual
	-- * Si la llamada es desde REUTERS se toma como variacion actual los parametros del SP
	-- * Si la llamada es desde SICA se obtiene la variacion actual con el query correspondiente 
	-- *
	-- * params: ninguno
	-- * return: sica_variacion%ROWTYPE
	-- *********************************************************************************************
	FUNCTION get_variacion_actual RETURN sica_variacion%ROWTYPE IS
		rec_sica_variacion_act sica_variacion%ROWTYPE := NULL; -- Almacena la variacion actual
	BEGIN
		IF p_caller = C_CALLER_REUTERS OR p_caller = C_CALLER_RMDS THEN
			rec_sica_variacion_act.fecha := p_fecha;
			rec_sica_variacion_act.variacion_mid := p_variacion_mid;
			rec_sica_variacion_act.variacion_vta_spot := p_variacion_vta_spot;
			rec_sica_variacion_act.variacion_vta_spot_ask := p_variacion_vta_spot_ask;
			rec_sica_variacion_act.cad_variacion_vta_spot := p_cad_variacion_vta_spot;
			rec_sica_variacion_act.cad_variacion_vta_spot_ask := p_cad_variacion_vta_spot_ask;
			rec_sica_variacion_act.chf_variacion_vta_spot := p_chf_variacion_vta_spot;
			rec_sica_variacion_act.chf_variacion_vta_spot_ask := p_chf_variacion_vta_spot_ask;
			rec_sica_variacion_act.eur_variacion_vta_spot := p_eur_variacion_vta_spot;
			rec_sica_variacion_act.eur_variacion_vta_spot_ask := p_eur_variacion_vta_spot_ask;
			rec_sica_variacion_act.gbp_variacion_vta_spot := p_gbp_variacion_vta_spot;
			rec_sica_variacion_act.gbp_variacion_vta_spot_ask := p_gbp_variacion_vta_spot_ask;
			rec_sica_variacion_act.jpy_variacion_vta_spot := p_jpy_variacion_vta_spot;
			rec_sica_variacion_act.jpy_variacion_vta_spot_ask := p_jpy_variacion_vta_spot_ask;
		ELSIF p_caller = C_CALLER_SICA THEN
			SELECT v.* INTO rec_sica_variacion_act
			FROM sica_variacion v
			WHERE v.id_variacion = (
				SELECT MAX(v2.id_variacion)
				FROM sica_variacion v2
			);
		END IF;
	
		RETURN rec_sica_variacion_act;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			log_msg(ERRMSG, 'FUNCTION get_variacion_actual: Variación actual no encontrada');
			RAISE ex_var_act_not_found;
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_variacion_actual: Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene precio referencia actual
	-- * 
	-- * params: ninguno
	-- * return: sc_precio_referencia_actual%ROWTYPE
	-- *********************************************************************************************
	FUNCTION get_prec_ref_actual RETURN sc_precio_referencia_actual%ROWTYPE IS
		rec_prec_ref_act sc_precio_referencia_actual%ROWTYPE;
	BEGIN
		SELECT pra.* INTO rec_prec_ref_act
		FROM sc_precio_referencia_actual pra;
		RETURN rec_prec_ref_act;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			log_msg(ERRMSG, 'FUNCTION get_prec_ref_actual: Precio de referencia actual no encontrado');
			RAISE ex_prec_ref_not_found;
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_prec_ref_actual: Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene factores divisa actuales
	-- * 
	-- * params: ninguno
	-- * return: TYP_ARR_FAC_DIV_ACT
	-- *********************************************************************************************
	FUNCTION get_fac_div_act RETURN TYP_ARR_FAC_DIV_ACT IS
		arr_facs_div_act TYP_ARR_FAC_DIV_ACT;
	BEGIN
		SELECT fda.* BULK COLLECT INTO arr_facs_div_act
		FROM sc_factor_divisa_actual fda;
		
		IF arr_facs_div_act.COUNT = 0 THEN
			RAISE NO_DATA_FOUND;
		END IF;
		
		RETURN arr_facs_div_act;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			log_msg(ERRMSG, 'FUNCTION get_fac_div_act: Factores divisa actuales no encontrados');
			RAISE ex_facs_div_not_found;
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_fac_div_act: Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene los spreads actuales por tipo_pizarron
	-- * 
	-- * params: 
	-- *	p_id_tipo_pizarron
	-- * return: TYP_ARR_SPREAD
	-- *********************************************************************************************
	FUNCTION get_spreads_act(p_id_tipo_pizarron IN NUMBER) RETURN TYP_ARR_SPREAD IS
		arr_spreads_act TYP_ARR_SPREAD;
	BEGIN
		SELECT s.* BULK COLLECT INTO arr_spreads_act
		FROM sc_spread s
		WHERE s.id_spread IN (
			SELECT sa.id_spread
			FROM sc_spread_actual sa
			WHERE sa.id_tipo_pizarron = p_id_tipo_pizarron
		);
		RETURN arr_spreads_act;
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_spreads_act(id_tipo_pizarron=' || p_id_tipo_pizarron || '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Evalua si un día es festivo
	-- * 
	-- * params: 
	-- *	fecha: Fecha a testear
	-- * return TRUE si la fecha es festiva, FALSE de lo contrario
	-- *********************************************************************************************
	FUNCTION is_dia_festivo(p_fecha IN DATE) RETURN BOOLEAN IS
		es_festivo BOOLEAN := TRUE;
		fecha_inhabil DATE := NULL;
	BEGIN
		SELECT fecha INTO fecha_inhabil
		FROM bupixe.bup_fecha_inhabil
		WHERE TO_CHAR(fecha, 'DD/MM/YYYY') = TO_CHAR(p_fecha, 'DD/MM/YYYY');
		
		IF fecha_inhabil IS NULL THEN
			es_festivo := FALSE;
		END IF;
		
		RETURN es_festivo;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			RETURN FALSE;
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION is_dia_festivo(fecha=' || p_fecha || '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Evalua si una fecha dada es habil
	-- * 
	-- * params: 
	-- *	fecha: Fecha a testear
	-- * return TRUE si la fecha es habil, FALSE de lo contrario
	-- *********************************************************************************************
	FUNCTION is_dia_habil(p_fecha IN DATE) RETURN BOOLEAN IS
		day_of_week NUMBER := NULL;
		DIA_SABADO CONSTANT INTEGER := 6;
		DIA_DOMINGO CONSTANT INTEGER := 7;
	BEGIN
		day_of_week := TO_CHAR(p_fecha, 'D');
		IF is_dia_festivo(p_fecha) OR day_of_week = DIA_SABADO OR day_of_week = DIA_DOMINGO THEN
			RETURN FALSE;
		ELSE
			RETURN TRUE;
		END IF;
	END;
	
	-- *********************************************************************************************
	-- * Retorna 5 fechas hábiles a partir de la fecha dada
	-- * 
	-- * params: 
	-- *	fecha: Fecha desde donde se calculan días hábiles
	-- * return arreglo de 5 fechas
	-- *********************************************************************************************
	FUNCTION get_5_dias_habiles (cash_date IN DATE) RETURN TYP_ARR_FECHA IS
		fechas_habiles TYP_ARR_FECHA;
		fec_hab_idx INTEGER := 1;
		l_current_date DATE := cash_date;
	BEGIN
		WHILE fec_hab_idx <= 5 LOOP
			IF is_dia_habil(l_current_date) THEN
				fechas_habiles(fec_hab_idx) := l_current_date;
				fec_hab_idx := fec_hab_idx + 1;
			END IF;
			l_current_date := l_current_date + 1;
		END LOOP;
		RETURN fechas_habiles;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene una lista de 5 "carries" (diferencia en dias entre dos fechas) para combinaciones 
	-- * sucesivas de fechas valor
	-- * DIF_TOM_CASH = fechaTom - fechaCash
	-- * DIF_SPOT_TOM = fechaSpot - fechaTom
	-- * DIF_72HR_SPOT = fecha72Hr - fechaSpot
	-- * DIF_VFUT_72HR = fechaVFut - fecha72Hr
	-- * 
	-- * params: 
	-- *	cash_date: fecha a partir de la cual se hace el cálculo
	-- * return: 
	-- *********************************************************************************************
	FUNCTION get_dif_carry_list(cash_date IN DATE) RETURN TYP_ARR_INT_IDX_STR IS
		arr_dif_carry TYP_ARR_INT_IDX_STR;
		fechas_habiles TYP_ARR_FECHA;
	BEGIN
		fechas_habiles := get_5_dias_habiles (cash_date);
		
		arr_dif_carry(C_DIF_TOM_CASH) := FLOOR ( fechas_habiles(2) - fechas_habiles(1) );
		arr_dif_carry(C_DIF_SPOT_TOM) := FLOOR ( fechas_habiles(3) - fechas_habiles(2) );
		arr_dif_carry(C_DIF_72HR_SPOT) := FLOOR ( fechas_habiles(4) - fechas_habiles(3) );
		arr_dif_carry(C_DIF_VFUT_72HR) := FLOOR ( fechas_habiles(5) - fechas_habiles(4) );
        
        RETURN arr_dif_carry;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene el factor divisa actual correspondiente a la divisa dada
	-- * 
	-- * params
	-- *	p_id_divisa: Divisa a buscar
	-- *********************************************************************************************
	FUNCTION get_factor_divisa(p_id_divisa IN VARCHAR2, p_arr_facs_div_act IN TYP_ARR_FAC_DIV_ACT) RETURN sc_factor_divisa_actual%ROWTYPE IS
		idx INTEGER := 0;
	BEGIN
		FOR idx IN p_arr_facs_div_act.FIRST .. p_arr_facs_div_act.LAST LOOP
			IF (p_arr_facs_div_act(idx).to_id_divisa = C_DOLAR_US) AND (p_arr_facs_div_act(idx).from_id_divisa = p_id_divisa) THEN
				RETURN p_arr_facs_div_act(idx);
			ELSIF (p_arr_facs_div_act(idx).from_id_divisa = C_DOLAR_US) AND (p_arr_facs_div_act(idx).to_id_divisa = p_id_divisa) THEN
				RETURN p_arr_facs_div_act(idx);
			END IF;
		END LOOP;
		log_msg(ERRMSG, 'FUNCTION get_factor_divisa: Factor divisa no encontrado para id_divisa=' || p_id_divisa);
		RAISE ex_fac_div_not_found;
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_factor_divisa(id_divisa=' || p_id_divisa || '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene la divisa especificada
	-- * 
	-- * params
	-- *	p_id_divisa: Divisa a buscar
	-- *********************************************************************************************
	FUNCTION get_divisa(p_id_divisa IN VARCHAR2) RETURN sc_divisa%ROWTYPE IS
		found_divisa sc_divisa%ROWTYPE;
	BEGIN
		SELECT * INTO found_divisa
		FROM sc_divisa
		WHERE id_divisa = p_id_divisa;
		
		RETURN found_divisa;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			log_msg(ERRMSG, 'FUNCTION get_divisa: Divisa no encontrada para id_divisa=' || p_id_divisa);
			RAISE ex_div_not_found;
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_divisa(id_divisa=' || p_id_divisa || '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene el nombre de la forma de liquidación dada la clave
	-- * 
	-- * params
	-- *	p_clave_forma_liqu: Clave de forma de liquidación a buscar
	-- *********************************************************************************************
	FUNCTION get_nombre_forma_liqu(p_clave_forma_liqu IN VARCHAR2, es_sucursal IN BOOLEAN) 
	RETURN site_admin.tes_forma_liquidacion.nombre_forma_liquidacion%TYPE IS
		nombre_forma_liqu site_admin.tes_forma_liquidacion.nombre_forma_liquidacion%TYPE;
	BEGIN
		IF es_sucursal THEN
			IF p_clave_forma_liqu = C_DOCEXT THEN
				RETURN C_REMESA_GIRO;
			ELSIF p_clave_forma_liqu = C_MEXDOL THEN
				RETURN C_CHEQUE_CANAL_USD;
			END IF;
		END IF;
	
		SELECT nombre_forma_liquidacion INTO nombre_forma_liqu
		FROM site_admin.tes_forma_liquidacion
		WHERE clave_forma_liquidacion = p_clave_forma_liqu;
		
		RETURN nombre_forma_liqu;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			log_msg(ERRMSG, 'FUNCTION get_nombre_forma_liqui: Nombre de forma liquidación no encontrada, clave_forma_liquidacion=' || p_clave_forma_liqu);
			RAISE ex_nom_form_liqu_not_found;
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_nombre_forma_liqu(clave_forma_liqu=' || p_clave_forma_liqu || '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene el valor del un parámetro dado de alta en la tabla SC_PARAMETRO
	-- * 
	-- * params
	-- *	p_id_parametro: Clave del parámetro
	-- *********************************************************************************************
	FUNCTION get_parametro_val(p_id_parametro IN VARCHAR2) RETURN sc_parametro.valor%TYPE IS
		l_valor sc_parametro.valor%TYPE;
	BEGIN
		SELECT valor INTO l_valor
		FROM sc_parametro
		WHERE id_parametro = p_id_parametro;
		
		RETURN l_valor;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			RETURN NULL;
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_parametro_val(id_parametro=' || p_id_parametro || '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Retorna los valores de los campos XXX_VARIACION_VTA_SPOT y XXX_VARIACION_VTA_SPOT_ASK
	-- * de la tabla SICA_VARIACION, segun la divisa dada
	-- *
	-- * params
	-- *	p_id_divisa: Identificador de la divisa
	-- *	va: Variación actual 
	-- *********************************************************************************************
	FUNCTION get_var_divisa(
		p_id_divisa IN sc_divisa.id_divisa%TYPE,
		va IN sica_variacion%ROWTYPE
	) RETURN TYP_ARR_FLOAT_IDX_STR IS
		var_div TYP_ARR_FLOAT_IDX_STR;
	BEGIN
		CASE p_id_divisa
			WHEN C_DOLAR_CANADIENSE THEN
				var_div(C_XD_VAR_VTA_SPOT) := va.cad_variacion_vta_spot;
				var_div(C_XD_VAR_VTA_SPOT_ASK) := va.cad_variacion_vta_spot_ask;
			WHEN C_EURO THEN
				var_div(C_XD_VAR_VTA_SPOT) := va.eur_variacion_vta_spot;
				var_div(C_XD_VAR_VTA_SPOT_ASK) := va.eur_variacion_vta_spot_ask;
			WHEN C_LIBRA_ESTERLINA THEN
				var_div(C_XD_VAR_VTA_SPOT) := va.gbp_variacion_vta_spot;
				var_div(C_XD_VAR_VTA_SPOT_ASK) := va.gbp_variacion_vta_spot_ask;
			WHEN C_FRANCO_SUIZO THEN
				var_div(C_XD_VAR_VTA_SPOT) := va.chf_variacion_vta_spot;
				var_div(C_XD_VAR_VTA_SPOT_ASK) := va.chf_variacion_vta_spot_ask;
			WHEN C_YEN THEN
				var_div(C_XD_VAR_VTA_SPOT) := va.jpy_variacion_vta_spot;
				var_div(C_XD_VAR_VTA_SPOT_ASK) := va.jpy_variacion_vta_spot_ask;
			ELSE
				RAISE ex_divisa_not_valid;
		END CASE;
		RETURN var_div;
	END;
	
	-- *********************************************************************************************
	-- * Calcula factor para una divisa dada
	-- * 
	-- * params
	-- *	p_id_divisa: Identificador de la divisa
	-- *	se_divide: bandera que indica si la divisa se divide
	-- *	es_compra: bandera que indica si el calculo es para compra
	-- *	va: Variación actual
	-- *	fda: Factor divisa actual
	-- *********************************************************************************************
	FUNCTION calc_factor_por_divisa(
		p_id_divisa IN sc_divisa.id_divisa%TYPE,
		se_divide IN BOOLEAN,
		es_compra IN BOOLEAN,
		va IN sica_variacion%ROWTYPE,
		fda IN sc_factor_divisa_actual%ROWTYPE
	) RETURN NUMBER IS
		var_div TYP_ARR_FLOAT_IDX_STR;
		l_factor NUMBER := 0;
	BEGIN
		var_div := get_var_divisa(p_id_divisa, va);
		
		IF se_divide THEN
			IF es_compra THEN
				l_factor := 1 / ( var_div(C_XD_VAR_VTA_SPOT) + fda.spread_compra / 2 );
			ELSE
				l_factor := 1 / ( var_div(C_XD_VAR_VTA_SPOT_ASK) - fda.spread_compra / 2 );
			END IF;
		ELSE
			IF es_compra THEN
				l_factor := var_div(C_XD_VAR_VTA_SPOT) - fda.spread_compra / 2;
			ELSE
				l_factor := var_div(C_XD_VAR_VTA_SPOT_ASK) + fda.spread_compra / 2;
			END IF;
		END IF;
		
		RETURN ROUND(l_factor, C_CALC_DECIMALES);
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION calc_factor_por_divisa(p_id_divisa=' || p_id_divisa 
			|| ',va.id_variacion=' || va.id_variacion
			|| ',fda.id_factor_divisa=' || fda.id_factor_divisa
			|| '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Calcula factor 
	-- * 
	-- * params
	-- *	se_divide: bandera que indica si la divisa se divide
	-- *	es_compra: bandera que indica si el calculo es para compra
	-- *	fda: Factor divisa actual
	-- *********************************************************************************************
	FUNCTION calc_factor_sin_divisa(
		se_divide IN BOOLEAN,
		es_compra IN BOOLEAN,
		fda IN sc_factor_divisa_actual%ROWTYPE
	) RETURN NUMBER IS
		l_factor NUMBER := 0;
	BEGIN
		IF se_divide THEN
			IF es_compra THEN
				l_factor := 1 / ( 1 / fda.factor + fda.spread_compra / 2 );
			ELSE
				l_factor := 1 / ( 1 / fda.factor - fda.spread_compra / 2 );
			END IF;
		ELSE
			IF es_compra THEN
				l_factor := fda.factor - fda.spread_compra / 2;
			ELSE
				l_factor := fda.factor + fda.spread_compra / 2;
			END IF;
		END IF;
		RETURN ROUND(l_factor, C_CALC_DECIMALES);
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION calc_factor_sin_divisa(fda.id_factor_divisa=' || fda.id_factor_divisa 
			|| '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene factor divisa correcto
	-- * 
	-- * params
	-- *	fda: Factor divisa de divisa actual
	-- *	va: Variacion actual
	-- *	es_compra: Especifica si el calculo el para compra o venta TRUE = Compra, FALSE = Venta
	-- *********************************************************************************************
	FUNCTION get_factor_divisa_correcto(
		fda IN sc_factor_divisa_actual%ROWTYPE,
		va IN sica_variacion%ROWTYPE,
		es_compra IN BOOLEAN
	) RETURN TYP_ARR_NUM_IDX_STR IS
		arr_result TYP_ARR_NUM_IDX_STR;
		l_factor NUMBER := 0;
		se_divide BOOLEAN := FALSE;
		l_divisa sc_divisa.id_divisa%TYPE;
	BEGIN
		arr_result(C_SPREAD_COMPRA) := 0;
		arr_result(C_SPREAD_REFERENCIA) := 0;
		arr_result(C_FACTOR) := 0;

		arr_result(C_SPREAD_REFERENCIA) := fda.spread_referencia;
    		
		IF fda.from_id_divisa = C_DOLAR_US AND fda.to_id_divisa = C_DOLAR_US THEN
			l_factor := fda.factor;
			arr_result(C_SPREAD_COMPRA) := fda.spread_compra;
		ELSE
			se_divide := get_divisa(fda.from_id_divisa).divide = C_SI OR get_divisa(fda.to_id_divisa).divide = C_SI;
			IF factores_automaticos THEN
				IF fda.from_id_divisa = C_DOLAR_US OR fda.from_id_divisa = C_PESO_MEX THEN
					l_divisa := fda.to_id_divisa;
				ELSE
					l_divisa := fda.from_id_divisa;
				END IF;
				l_factor := calc_factor_por_divisa(l_divisa, se_divide, es_compra, va, fda);
			ELSE
				l_factor := calc_factor_sin_divisa(se_divide, es_compra, fda);
			END IF;
		END IF;
		arr_result(C_FACTOR) := l_factor;
		RETURN arr_result;
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_factor_divisa_correcto(fda.id_factor_divisa=' || fda.id_factor_divisa 
			|| ', va.id_variacion=' || va.id_variacion 
			|| '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene precio de compra spot
	-- * 
	-- * params
	-- *	fda: Factor divisa de divisa actual
	-- *	pra: Precio de referencia actual
	-- *	va: Variacion actual 
	-- *********************************************************************************************
	FUNCTION get_precio_c_spot(
		fda IN sc_factor_divisa_actual%ROWTYPE, 
		pra IN sc_precio_referencia_actual%ROWTYPE,
		va IN sica_variacion%ROWTYPE
	) RETURN NUMBER IS
		precio_c_spot NUMBER := 0;
		arr_fac_div_correct TYP_ARR_NUM_IDX_STR;
		l_factor NUMBER := 0;
		l_spread_compra NUMBER := 0;
		l_spread_referencia NUMBER := 0;
	BEGIN
		-- Llama factor_divisa_correcto
		arr_fac_div_correct := get_factor_divisa_correcto(fda, va, C_COMPRA);
		
		l_factor := arr_fac_div_correct(C_FACTOR);
		l_spread_compra := arr_fac_div_correct(C_SPREAD_COMPRA);
		l_spread_referencia := arr_fac_div_correct(C_SPREAD_REFERENCIA);
		
		IF pra.metodo_actualizacion = C_AUTOMATICO OR pra.metodo_actualizacion = C_MID_SPOT THEN
			RETURN pra.precio_compra * l_factor + l_spread_referencia;
		ELSIF pra.metodo_actualizacion = C_MANUAL THEN
			RETURN (pra.precio_spot * l_factor - l_spread_compra) + l_spread_referencia;
		ELSIF pra.metodo_actualizacion = C_VESPERTINO THEN
			RETURN ( pra.precio_spot - spread_vespertino ) * l_factor + l_spread_referencia;
		ELSE
			log_msg(ERRMSG, 'FUNCTION get_precio_c_spot: No esta definido el precio de compra spot para el metodo de actualización: ' || pra.metodo_actualizacion);
			RAISE ex_metodo_act_not_valid;
		END IF;
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_precio_c_spot(fda.id_factor_divisa=' || fda.id_factor_divisa 
			|| ', pra.id_precio_referencia=' || pra.id_precio_referencia 
			|| ', va.id_variacion=' || va.id_variacion 
			|| ', SPREAD_VESPERTINO=' || spread_vespertino
			|| '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene precio de compra tomorrow
	-- * 
	-- * params
	-- *	fda: Factor divisa de divisa actual
	-- *	pra: Precio de referencia actual
	-- * 	dias_dif_spot_tom: dias de diferencia entre fecha SPOT y fecha TOM
	-- *********************************************************************************************
	FUNCTION get_precio_c_tom(
		fda IN sc_factor_divisa_actual%ROWTYPE, 
		pra IN sc_precio_referencia_actual%ROWTYPE,
		va IN sica_variacion%ROWTYPE,
		dias_dif_spot_tom IN INTEGER
	) RETURN NUMBER IS
		precio_c_spot NUMBER := 0;
	BEGIN
		precio_c_spot := get_precio_c_spot(fda, pra, va);
		IF precio_c_spot > 0 THEN
			RETURN precio_c_spot - fda.carry * dias_dif_spot_tom;
		ELSE
			RETURN 0;
		END IF;
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_precio_c_tom(fda.id_factor_divisa=' || fda.id_factor_divisa 
			|| ', pra.id_precio_referencia=' || pra.id_precio_referencia 
			|| ', va.id_variacion=' || va.id_variacion 
			|| ', dias_dif_spot_tom=' || dias_dif_spot_tom 
			|| '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene precio de compra cash
	-- * 
	-- * params
	-- *	fda: Factor divisa de divisa actual
	-- *	pra: Precio de referencia actual
	-- *	va: variación actual
	-- *	dias_dif_tom_cash: dias de diferencia entre fecha TOM y fecha CASH
	-- * 	dias_dif_spot_tom: dias de diferencia entre fecha SPOT y fecha TOM
	-- *********************************************************************************************
	FUNCTION get_precio_c_cash(
		fda IN sc_factor_divisa_actual%ROWTYPE, 
		pra IN sc_precio_referencia_actual%ROWTYPE,
		va IN sica_variacion%ROWTYPE,
		dias_dif_tom_cash IN INTEGER, 
		dias_dif_spot_tom IN INTEGER
	) RETURN NUMBER IS
		precio_c_tom NUMBER := 0;
	BEGIN
		precio_c_tom := get_precio_c_tom(fda, pra, va, dias_dif_spot_tom);
		IF precio_c_tom > 0 THEN
			RETURN precio_c_tom - fda.carry * dias_dif_tom_cash;
		ELSE
			RETURN 0;
		END IF;
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_precio_c_cash(fda.id_factor_divisa=' || fda.id_factor_divisa 
			|| ', pra.id_precio_referencia=' || pra.id_precio_referencia 
			|| ', va.id_variacion=' || va.id_variacion 
			|| ', dias_dif_tom_cash=' || dias_dif_tom_cash 
			|| ', dias_dif_spot_tom=' || dias_dif_spot_tom 
			|| '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene precio de compra 72Hr
	-- * 
	-- * params
	-- *	fda: Factor divisa de divisa actual
	-- *	pra: Precio de referencia actual
	-- *	va: Variacion actual
	-- *	dias_dif_72hr_spot: Dias de diferencia entre fecha 72hrs y fecha spot
	-- *********************************************************************************************
	FUNCTION get_precio_c_72hr(
		fda IN sc_factor_divisa_actual%ROWTYPE, 
		pra IN sc_precio_referencia_actual%ROWTYPE,
		va IN sica_variacion%ROWTYPE,
		dias_dif_72hr_spot IN INTEGER
	) RETURN NUMBER IS
		precio_c_spot NUMBER := 0;
	BEGIN
		precio_c_spot := get_precio_c_spot(fda, pra, va);
		IF precio_c_spot > 0 THEN
			RETURN precio_c_spot + fda.carry * dias_dif_72hr_spot;
		ELSE
			RETURN 0;
		END IF;
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_precio_c_72hr(fda.id_factor_divisa=' || fda.id_factor_divisa 
			|| ', pra.id_precio_referencia=' || pra.id_precio_referencia 
			|| ', va.id_variacion=' || va.id_variacion 
			|| ', dias_dif_72hr_spot=' || dias_dif_72hr_spot 
			|| '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene precio de venta spot
	-- * 
	-- * params
	-- *	fda: Factor divisa de divisa actual
	-- *	pra: Precio de referencia actual
	-- *	va: variacion actual
	-- *********************************************************************************************
	FUNCTION get_precio_v_spot(
		fda IN sc_factor_divisa_actual%ROWTYPE, 
		pra IN sc_precio_referencia_actual%ROWTYPE,
		va IN sica_variacion%ROWTYPE
	) RETURN NUMBER IS
		precio_c_spot NUMBER := 0;
		arr_fac_div_correct TYP_ARR_NUM_IDX_STR;
		l_factor NUMBER := 0;
		l_spread_compra NUMBER := 0;
		l_spread_referencia NUMBER := 0;
	BEGIN
		-- Llama factor_divisa_correcto
		arr_fac_div_correct := get_factor_divisa_correcto(fda, va, C_VENTA);
		
		l_factor := arr_fac_div_correct(C_FACTOR);
		l_spread_referencia := arr_fac_div_correct(C_SPREAD_REFERENCIA);
		
		IF pra.metodo_actualizacion = C_AUTOMATICO OR pra.metodo_actualizacion = C_MID_SPOT THEN
			RETURN pra.precio_venta * l_factor + l_spread_referencia;
		ELSIF pra.metodo_actualizacion = C_MANUAL THEN
			RETURN pra.precio_spot * l_factor + l_spread_referencia;
		ELSIF pra.metodo_actualizacion = C_VESPERTINO THEN
			RETURN pra.precio_spot * l_factor + l_spread_referencia;
		ELSE
			log_msg(ERRMSG, 'FUNCTION get_precio_v_spot: No esta definido el precio de venta spot para el metodo de actualización: ' || pra.metodo_actualizacion);
			RAISE ex_metodo_act_not_valid;
		END IF;
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_precio_v_spot(fda.id_factor_divisa=' || fda.id_factor_divisa 
			|| ', pra.id_precio_referencia=' || pra.id_precio_referencia 
			|| ', va.id_variacion=' || va.id_variacion 
			|| '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene precio de venta 72Hr
	-- * 
	-- * params
	-- *	fda: Factor divisa de divisa actual
	-- *	pra: Precio de referencia actual
	-- *	va: Variación actual
	-- *	dias_dif_72hr_spot: dias de diferencia entre fecha 72hr y fecha spot
	-- *********************************************************************************************
	FUNCTION get_precio_v_72hr(
		fda IN sc_factor_divisa_actual%ROWTYPE, 
		pra IN sc_precio_referencia_actual%ROWTYPE,
		va IN sica_variacion%ROWTYPE,
		dias_dif_72hr_spot IN INTEGER 
	) RETURN NUMBER IS
		precio_v_spot NUMBER := 0;
	BEGIN
		precio_v_spot := get_precio_v_spot(fda, pra, va);
		IF precio_v_spot > 0 THEN
			RETURN precio_v_spot + fda.carry * dias_dif_72hr_spot;
		ELSE
			RETURN 0;
		END IF;
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_precio_v_72hr(fda.id_factor_divisa=' || fda.id_factor_divisa 
			|| ', pra.id_precio_referencia=' || pra.id_precio_referencia 
			|| ', va.id_variacion=' || va.id_variacion 
			|| ', dias_dif_72hr_spot=' || dias_dif_72hr_spot 
			|| '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene precio de compra Valor futuro
	-- * 
	-- * params
	-- *	fda: Factor divisa de divisa actual
	-- *	pra: Precio de referencia actual
	-- * 	va: Variacion actual
	-- *	dias_dif_72hr_spot: Dias de diferencia entre la fecha 72hr y la fecha spot  
	-- * 	dias_dif_vfut_72hr: Dias de diferencia entre la fecha Vfut y la fecha 72hr
	-- *********************************************************************************************
	FUNCTION get_precio_c_vfut(
		fda IN sc_factor_divisa_actual%ROWTYPE, 
		pra IN sc_precio_referencia_actual%ROWTYPE,
		va IN sica_variacion%ROWTYPE,
		dias_dif_72hr_spot IN INTEGER, 
		dias_dif_vfut_72hr IN INTEGER
	) RETURN NUMBER IS
		precio_c_72hr NUMBER := 0;
	BEGIN
		precio_c_72hr := get_precio_c_72hr(fda, pra, va, dias_dif_72hr_spot);
		IF precio_c_72hr > 0 THEN
			RETURN precio_c_72hr + fda.carry * dias_dif_vfut_72hr;
		ELSE
			RETURN 0;
		END IF;
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_precio_c_vfut(fda.id_factor_divisa=' || fda.id_factor_divisa 
			|| ', pra.id_precio_referencia=' || pra.id_precio_referencia 
			|| ', va.id_variacion=' || va.id_variacion 
			|| ', dias_dif_72hr_spot=' || dias_dif_72hr_spot 
			|| ', dias_dif_vfut_72hr=' || dias_dif_vfut_72hr 
			|| '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene precio de venta Valor futuro
	-- * 
	-- * params
	-- *	fda: Factor divisa de divisa actual
	-- *	pra: Precio de referencia actual
	-- * 	va: Variacion actual
	-- *	dias_dif_72hr_spot: Dias de diferencia entre la fecha 72hr y la fecha spot
	-- * 	dias_dif_vfut_72hr: Días de diferencia entre la fecha Vfut y  la fecha 72hr
	-- *********************************************************************************************
	FUNCTION get_precio_v_vfut(
		fda IN sc_factor_divisa_actual%ROWTYPE, 
		pra IN sc_precio_referencia_actual%ROWTYPE,
		va IN sica_variacion%ROWTYPE,
		dias_dif_72hr_spot IN INTEGER, 
		dias_dif_vfut_72hr IN INTEGER
	) RETURN NUMBER IS
		precio_v_72hr NUMBER := 0;
	BEGIN
		precio_v_72hr := get_precio_v_72hr(fda, pra, va, dias_dif_72hr_spot);
		IF precio_v_72hr > 0 THEN
			RETURN precio_v_72hr + fda.carry * dias_dif_vfut_72hr;
		ELSE
			RETURN 0;
		END IF;
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_precio_v_vfut(fda.id_factor_divisa=' || fda.id_factor_divisa 
			|| ', pra.id_precio_referencia=' || pra.id_precio_referencia 
			|| ', va.id_variacion=' || va.id_variacion 
			|| ', dias_dif_72hr_spot=' || dias_dif_72hr_spot 
			|| ', dias_dif_vfut_72hr=' || dias_dif_vfut_72hr 
			|| '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene precio de venta tomorrow
	-- * 
	-- * params
	-- *	fda: Factor divisa de divisa actual
	-- *	pra: Precio de referencia actual
	-- *	va: Variación actuasl
	-- * 	dias_dif_spot_tom: dias de diferencia entre fecha SPOT y fecha TOM
	-- *********************************************************************************************
	FUNCTION get_precio_v_tom(
		fda IN sc_factor_divisa_actual%ROWTYPE, 
		pra IN sc_precio_referencia_actual%ROWTYPE,
		va IN sica_variacion%ROWTYPE,
		dias_dif_spot_tom IN INTEGER
	) RETURN NUMBER IS
		precio_v_spot NUMBER := 0;
	BEGIN
		precio_v_spot := get_precio_v_spot(fda, pra, va);
		IF precio_v_spot > 0 THEN
			RETURN precio_v_spot - fda.carry * dias_dif_spot_tom;
		ELSE
			RETURN 0;
		END IF;
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_precio_v_tom(fda.id_factor_divisa=' || fda.id_factor_divisa 
			|| ', pra.id_precio_referencia=' || pra.id_precio_referencia 
			|| ', va.id_variacion=' || va.id_variacion 
			|| ', dias_dif_spot_tom=' || dias_dif_spot_tom 
			|| '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene precio de venta cash
	-- * 
	-- * params
	-- *	fda: Factor divisa de divisa actual
	-- *	pra: Precio de referencia actual
	-- *	dias_dif_tom_cash: dias de diferencia entre fecha TOM y fecha CASH
	-- * 	dias_dif_spot_tom: dias de diferencia entre fecha SPOT y fecha TOM
	-- *********************************************************************************************
	FUNCTION get_precio_v_cash(
		fda IN sc_factor_divisa_actual%ROWTYPE, 
		pra IN sc_precio_referencia_actual%ROWTYPE,
		va IN sica_variacion%ROWTYPE,
		dias_dif_tom_cash IN INTEGER, 
		dias_dif_spot_tom IN INTEGER
	) RETURN NUMBER IS
		precio_v_tom NUMBER := 0;
	BEGIN
		precio_v_tom := get_precio_v_tom(fda, pra, va, dias_dif_spot_tom);
		IF precio_v_tom > 0 THEN
			RETURN precio_v_tom - fda.carry * dias_dif_tom_cash;
		ELSE
			RETURN 0;
		END IF;
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_precio_v_cash(fda.id_factor_divisa=' || fda.id_factor_divisa 
			|| ', pra.id_precio_referencia=' || pra.id_precio_referencia 
			|| ', va.id_variacion=' || va.id_variacion 
			|| ', dias_dif_tom_cash=' || dias_dif_tom_cash 
			|| ', dias_dif_spot_tom=' || dias_dif_spot_tom 
			|| '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Obtiene el siguiente valor de la secuencia SC_RENGLON_PIZARRON_SEQ para la PK de la
	-- * tabla SC_RENGLON_PIZARRON
	-- *********************************************************************************************
	FUNCTION get_ren_pizr_next_pk_val RETURN sc_renglon_pizarron.id_renglon_pizarron%TYPE IS
		ren_pizr_next_pk_val sc_renglon_pizarron.id_renglon_pizarron%TYPE;
	BEGIN
		SELECT sc_renglon_pizarron_seq.NEXTVAL INTO ren_pizr_next_pk_val
		FROM dual;
		RETURN ren_pizr_next_pk_val;
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION get_ren_pizr_next_pk_val(): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Crea los renglones del pizarrón y los inserta
	-- * 
	-- * params
	-- *	rec_precio_ref_act: Precio referencia actual
	-- *	arr_facs_div_act: Arreglo de factores divisa actuales, cada elemento es 
	-- *		sc_factor_divisa_actual%ROWTYPE
	-- *	arr_spreads_act: Arreglo de spreads actuales
	-- *	rec_sica_variacion_act: Variación actual
	-- *	arr_carries_act: Arreglo de carries
	-- *	sucursal: Si es o no para sucursales
	-- *********************************************************************************************
	FUNCTION crear_renglones (
		rec_precio_ref_act IN sc_precio_referencia_actual%ROWTYPE,
		arr_facs_div_act IN TYP_ARR_FAC_DIV_ACT,
		arr_spreads_act IN TYP_ARR_SPREAD,
		rec_sica_variacion_act IN sica_variacion%ROWTYPE,
		arr_carries_act IN TYP_ARR_INT_IDX_STR,
		sucursal IN BOOLEAN
	) RETURN TYP_ARR_RENGL_PIZRRN IS
		arr_renglon_pizarron TYP_ARR_RENGL_PIZRRN; -- arreglo de renglones de pizarrón resultante de esta función
		idx_arr_reng_pizrn INTEGER := 1; -- indice para el arreglo de renglones de pizarron
		spread_idx INTEGER := 0; -- indice para iterar los spreads del arreglo arr_spreads_act
		fd_for_curr_sprd sc_factor_divisa_actual%ROWTYPE; -- factor divisa para el spread actual
		nom_form_liqu_for_curr_sprd site_admin.tes_forma_liquidacion.nombre_forma_liquidacion%TYPE; -- nombre de forma liquidación para spread actual
		spread_sucursales NUMBER := 0;
		div_for_curr_sprd sc_divisa%ROWTYPE; -- divisa para el spread actual
		curr_precio NUMBER := 0;
	BEGIN
		log_msg(INFMSG, 'Comienza la creación de renglones de pizarron ...');
		IF arr_spreads_act.COUNT <> 0 THEN -- Valida que el arreglo de spreads no venga vacío
			FOR spread_idx IN arr_spreads_act.FIRST .. arr_spreads_act.LAST LOOP
				log_msg(INFMSG, 'Spread actual, id_spread=' || arr_spreads_act(spread_idx).id_spread);
				
				fd_for_curr_sprd := get_factor_divisa(arr_spreads_act(spread_idx).id_divisa, arr_facs_div_act);
				log_msg(INFMSG, 'Factor divisa para spread actual, id_factor_divisa=' || fd_for_curr_sprd.id_factor_divisa);
				log_msg(INFMSG, 'Factor divisa para spread actual, from_id_divisa=' || fd_for_curr_sprd.from_id_divisa || ', to_id_divisa=' || fd_for_curr_sprd.to_id_divisa);
				
				IF sucursal THEN
					div_for_curr_sprd := get_divisa(arr_spreads_act(spread_idx).id_divisa);
					spread_sucursales := div_for_curr_sprd.spread_sucursales;
				ELSE
					spread_sucursales := 0;
				END IF;
				log_msg(INFMSG, 'spread_sucursales=' || spread_sucursales);
				
				arr_renglon_pizarron(idx_arr_reng_pizrn).id_renglon_pizarron := get_ren_pizr_next_pk_val();
				arr_renglon_pizarron(idx_arr_reng_pizrn).fecha := SYSDATE;
				arr_renglon_pizarron(idx_arr_reng_pizrn).id_canal := NULL;
				log_msg(INFMSG, 'PK de renglón pizarron generada, id_renglon_pizarron=' || arr_renglon_pizarron(idx_arr_reng_pizrn).id_renglon_pizarron);
				
				arr_renglon_pizarron(idx_arr_reng_pizrn).id_precio_referencia := rec_precio_ref_act.id_precio_referencia;
				arr_renglon_pizarron(idx_arr_reng_pizrn).id_tipo_pizarron := arr_spreads_act(spread_idx).id_tipo_pizarron;
				arr_renglon_pizarron(idx_arr_reng_pizrn).id_spread := arr_spreads_act(spread_idx).id_spread;
				arr_renglon_pizarron(idx_arr_reng_pizrn).id_factor_divisa := fd_for_curr_sprd.id_factor_divisa;
				arr_renglon_pizarron(idx_arr_reng_pizrn).clave_forma_liquidacion := arr_spreads_act(spread_idx).clave_forma_liquidacion;
				nom_form_liqu_for_curr_sprd := get_nombre_forma_liqu(arr_spreads_act(spread_idx).clave_forma_liquidacion, sucursal);
				log_msg(INFMSG, 'Nombre forma liqu. obtenido=' || nom_form_liqu_for_curr_sprd);
				
				arr_renglon_pizarron(idx_arr_reng_pizrn).nombre_forma_liquidacion := nom_form_liqu_for_curr_sprd;
				arr_renglon_pizarron(idx_arr_reng_pizrn).id_divisa := arr_spreads_act(spread_idx).id_divisa;
				arr_renglon_pizarron(idx_arr_reng_pizrn).factor_divisa := fd_for_curr_sprd.factor;
				
				arr_renglon_pizarron(idx_arr_reng_pizrn).pre_ref_mid_spot := rec_precio_ref_act.mid_spot;
				arr_renglon_pizarron(idx_arr_reng_pizrn).pre_ref_spot := rec_precio_ref_act.precio_spot;
				
				log_msg(INFMSG, 'Datos generales del renglon pizarron establecidos');
				
				-- #### Cálculo del Precio de compra cash
				curr_precio := get_precio_c_cash( 
					fd_for_curr_sprd, 
					rec_precio_ref_act,
					rec_sica_variacion_act,
					arr_carries_act(C_DIF_TOM_CASH), 
					arr_carries_act(C_DIF_SPOT_TOM)
				);
				IF curr_precio > 0 THEN
					arr_renglon_pizarron(idx_arr_reng_pizrn).compra_cash := curr_precio + arr_spreads_act(spread_idx).compra_cash - spread_sucursales;
				ELSE
					arr_renglon_pizarron(idx_arr_reng_pizrn).compra_cash := 0;
				END IF;
				log_msg(INFMSG, 'Precio Compra CASH calculado = ' || arr_renglon_pizarron(idx_arr_reng_pizrn).compra_cash);
				
				-- #### Cálculo del Precio de venta cash
				curr_precio := get_precio_v_cash(
					fd_for_curr_sprd, 
					rec_precio_ref_act,
					rec_sica_variacion_act,
					arr_carries_act(C_DIF_TOM_CASH), 
					arr_carries_act(C_DIF_SPOT_TOM)
				);
				IF curr_precio > 0 THEN
					arr_renglon_pizarron(idx_arr_reng_pizrn).venta_cash := curr_precio + arr_spreads_act(spread_idx).venta_cash + spread_sucursales;
				ELSE
					arr_renglon_pizarron(idx_arr_reng_pizrn).venta_cash := 0;
				END IF;
				log_msg(INFMSG, 'Precio Venta CASH calculado = ' || arr_renglon_pizarron(idx_arr_reng_pizrn).venta_cash);
				
				-- #### Cálculo del Precio de compra TOM
				curr_precio := get_precio_c_tom(
					fd_for_curr_sprd, 
					rec_precio_ref_act,
					rec_sica_variacion_act,
					arr_carries_act(C_DIF_SPOT_TOM)
				);
				IF curr_precio > 0 THEN
					arr_renglon_pizarron(idx_arr_reng_pizrn).compra_tom := curr_precio + arr_spreads_act(spread_idx).compra_tom - spread_sucursales;
				ELSE
					arr_renglon_pizarron(idx_arr_reng_pizrn).compra_tom := 0;
				END IF;
				log_msg(INFMSG, 'Precio Compra TOM calculado = ' || arr_renglon_pizarron(idx_arr_reng_pizrn).compra_tom);
				
				-- #### Cálculo del Precio de venta TOM
				curr_precio := get_precio_v_tom(
					fd_for_curr_sprd, 
					rec_precio_ref_act,
					rec_sica_variacion_act,
					arr_carries_act(C_DIF_SPOT_TOM)
				);
				IF curr_precio > 0 THEN
					arr_renglon_pizarron(idx_arr_reng_pizrn).venta_tom := curr_precio + arr_spreads_act(spread_idx).venta_tom + spread_sucursales;
				ELSE
					arr_renglon_pizarron(idx_arr_reng_pizrn).venta_tom := 0;
				END IF;
				log_msg(INFMSG, 'Precio Venta TOM calculado = ' || arr_renglon_pizarron(idx_arr_reng_pizrn).venta_tom);
				
				-- #### Cálculo del Precio de compra SPOT
				curr_precio := get_precio_c_spot(
					fd_for_curr_sprd, 
					rec_precio_ref_act,
					rec_sica_variacion_act
				);
				IF curr_precio > 0 THEN
					arr_renglon_pizarron(idx_arr_reng_pizrn).compra_spot := curr_precio + arr_spreads_act(spread_idx).compra_spot - spread_sucursales;
				ELSE
					arr_renglon_pizarron(idx_arr_reng_pizrn).compra_spot := 0;
				END IF;
				log_msg(INFMSG, 'Precio Compra SPOT calculado = ' || arr_renglon_pizarron(idx_arr_reng_pizrn).compra_spot);
				
				-- #### Cálculo del Precio de venta SPOT
				curr_precio := get_precio_v_spot(
					fd_for_curr_sprd, 
					rec_precio_ref_act,
					rec_sica_variacion_act
				);
				IF curr_precio > 0 THEN
					arr_renglon_pizarron(idx_arr_reng_pizrn).venta_spot := curr_precio + arr_spreads_act(spread_idx).venta_spot + spread_sucursales;
				ELSE
					arr_renglon_pizarron(idx_arr_reng_pizrn).venta_spot := 0;
				END IF;
				log_msg(INFMSG, 'Precio Venta SPOT calculado = ' || arr_renglon_pizarron(idx_arr_reng_pizrn).venta_spot);
				
				-- #### Cálculo del Precio de compra 72 hr
				curr_precio := get_precio_c_72hr(
					fd_for_curr_sprd, 
					rec_precio_ref_act,
					rec_sica_variacion_act,
					arr_carries_act(C_DIF_72HR_SPOT)
				);
				IF curr_precio > 0 THEN
					arr_renglon_pizarron(idx_arr_reng_pizrn).compra_72hr := curr_precio + arr_spreads_act(spread_idx).compra_72hr - spread_sucursales;
				ELSE
					arr_renglon_pizarron(idx_arr_reng_pizrn).compra_72hr := 0;
				END IF;
				log_msg(INFMSG, 'Precio Compra 72hr calculado = ' || arr_renglon_pizarron(idx_arr_reng_pizrn).compra_72hr);
				
				-- #### Cálculo del Precio de venta 72 hr
				curr_precio := get_precio_v_72hr(
					fd_for_curr_sprd, 
					rec_precio_ref_act,
					rec_sica_variacion_act,
					arr_carries_act(C_DIF_72HR_SPOT)
				);
				IF curr_precio > 0 THEN
					arr_renglon_pizarron(idx_arr_reng_pizrn).venta_72hr := curr_precio + arr_spreads_act(spread_idx).venta_72hr + spread_sucursales;
				ELSE
					arr_renglon_pizarron(idx_arr_reng_pizrn).venta_72hr := 0;
				END IF;
				log_msg(INFMSG, 'Precio Venta 72hr calculado = ' || arr_renglon_pizarron(idx_arr_reng_pizrn).venta_72hr);
				
				-- #### Cálculo del Precio de compra VFUT
				curr_precio := get_precio_c_vfut(
					fd_for_curr_sprd, 
					rec_precio_ref_act,
					rec_sica_variacion_act,
					arr_carries_act(C_DIF_72HR_SPOT),
					arr_carries_act(C_DIF_VFUT_72HR)
				);
				IF curr_precio > 0 THEN
					arr_renglon_pizarron(idx_arr_reng_pizrn).compra_vfut := curr_precio + arr_spreads_act(spread_idx).compra_vfut - spread_sucursales;
				ELSE
					arr_renglon_pizarron(idx_arr_reng_pizrn).compra_vfut := 0;
				END IF;
				log_msg(INFMSG, 'Precio Compra VFUT calculado = ' || arr_renglon_pizarron(idx_arr_reng_pizrn).compra_vfut);
				
				-- #### Cálculo del Precio de venta VFUT
				curr_precio := get_precio_v_vfut(
					fd_for_curr_sprd, 
					rec_precio_ref_act,
					rec_sica_variacion_act,
					arr_carries_act(C_DIF_72HR_SPOT),
					arr_carries_act(C_DIF_VFUT_72HR)
				);
				IF curr_precio > 0 THEN
					arr_renglon_pizarron(idx_arr_reng_pizrn).venta_vfut := curr_precio + arr_spreads_act(spread_idx).venta_vfut + spread_sucursales;
				ELSE
					arr_renglon_pizarron(idx_arr_reng_pizrn).venta_vfut := 0;
				END IF;
				log_msg(INFMSG, 'Precio Venta VFUT calculado = ' || arr_renglon_pizarron(idx_arr_reng_pizrn).venta_vfut);
				
				idx_arr_reng_pizrn := idx_arr_reng_pizrn + 1;
			END LOOP;
		END IF;
		log_msg(INFMSG, 'Finaliza la creación de renglones de pizarron ...');
		RETURN arr_renglon_pizarron;
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION crear_renglones(arr_facs_div_act.COUNT=' || arr_facs_div_act.COUNT 
			|| ', rec_precio_ref_act.id_precio_referencia=' || rec_precio_ref_act.id_precio_referencia 
			|| ', rec_sica_variacion_act.id_variacion=' || rec_sica_variacion_act.id_variacion 
			|| ', arr_spreads_act.COUNT=' || arr_spreads_act.COUNT 
			|| ', arr_carries_act.COUNT=' || arr_carries_act.COUNT 
			|| '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Inserta los renglones generados en la tabla sc_renglon_pizarron
	-- * 
	-- * params
	-- * 	arr_renglon_pizarron: Arreglo de registros para la tabla sc_renglon_pizarron
	-- *********************************************************************************************
	PROCEDURE insert_renglones_pizarron (arr_renglon_pizarron IN TYP_ARR_RENGL_PIZRRN) IS
		idx_arr_rengl_pizrn INTEGER := 0;
	BEGIN
		IF arr_renglon_pizarron.COUNT <> 0 THEN
			FOR idx_arr_rengl_pizrn IN arr_renglon_pizarron.FIRST .. arr_renglon_pizarron.LAST LOOP
				INSERT INTO sc_renglon_pizarron VALUES arr_renglon_pizarron(idx_arr_rengl_pizrn);
			END LOOP;
		END IF;
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'PROCEDURE insert_renglones_pizarron(arr_renglon_pizarron.COUNT=' || arr_renglon_pizarron.COUNT 
			|| '): Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-- *************************************************************************************************************
	-- * Inicializa las variables globales
	-- *************************************************************************************************************
	PROCEDURE global_init IS
		param_value sc_parametro.valor%TYPE;
	BEGIN
		log_enabled := FALSE;
		timming_enabled := FALSE;
		factores_automaticos := FALSE;
		spread_vespertino := 0;
		origen_variacion := C_ORIG_VAR_EXCEL;
		
		param_value := get_parametro_val(C_FDD_LOG_SWITCH);
		IF param_value = '1' THEN
			log_enabled := TRUE;
		END IF;
		
		param_value := get_parametro_val(C_FDD_TIME_SWITCH);
		IF param_value = '1' THEN
			timming_enabled := TRUE;
		END IF;
		
		param_value := get_parametro_val(C_FACTOR_AUTOMATICO);
		IF param_value = C_SI THEN
			factores_automaticos := TRUE;
		END IF;
		
		param_value := get_parametro_val(C_SPREAD_VESPERTINO);
		IF param_value IS NOT NULL THEN
			spread_vespertino := TO_NUMBER(param_value, '999.999999');
		END IF;
		
		param_value := get_parametro_val(C_FDD_ORIGEN_VARIACION);
		IF param_value IS NOT NULL THEN
			origen_variacion := param_value;
		END IF;
		
		IF factores_automaticos THEN
			log_msg(INFMSG, 'FACTORES_AUTOMATICOS=TRUE');
		ELSE
			log_msg(INFMSG, 'FACTORES_AUTOMATICOS=FALSE');
		END IF;
		log_msg(INFMSG, 'SPREAD_VESPERTINO=' || spread_vespertino);
		log_msg(INFMSG, 'ORIGEN_VARIACION=' || origen_variacion);
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'FUNCTION global_init: Error inesperado: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	
	-- ######################################################################################################################
	-- #### PROCEDIMIENTOS PRINCIPALES
	-- ######################################################################################################################

	
	-- *********************************************************************************************
	-- * Procedimiento principal que se encarga de borrar el pizarron anterior e insertar el
	-- * nuevo
	-- *********************************************************************************************
	PROCEDURE refrescar_pizarrones IS
		PRAGMA AUTONOMOUS_TRANSACTION;
		CURSOR cur_tipo_pizarron IS -- Obtiene los tipos de pizarrones existentes
			SELECT *
			FROM sc_tipo_pizarron;
		rec_sica_variacion_act sica_variacion%ROWTYPE; -- Almacena la varicacion actual
		rec_precio_ref_act sc_precio_referencia_actual%ROWTYPE; -- Almacena el precio de referencia actual
		arr_facs_div_act TYP_ARR_FAC_DIV_ACT; -- Almacena los factores divisa actuales
		arr_spreads_act TYP_ARR_SPREAD; -- Almacena los spreads actuales
		arr_carries_act TYP_ARR_INT_IDX_STR; -- Almacena carries
		arr_renglon_pizarron TYP_ARR_RENGL_PIZRRN; -- Almzacena los renglones del pizarron
	BEGIN
		log_msg(INFMSG, 'Comienza actualización de pizarrones...');
		rec_sica_variacion_act := get_variacion_actual(); -- obtiene la variación actual
		log_msg(INFMSG, 'Variación actual obtenida, id_variacion=' || rec_sica_variacion_act.id_variacion);
		rec_precio_ref_act := get_prec_ref_actual(); -- Obtiene precio de referencia actual
		log_msg(INFMSG, 'Precio de referencia actual obtenido, id_precio_referencia=' || rec_precio_ref_act.id_precio_referencia);
		arr_facs_div_act := get_fac_div_act(); -- Obtiene factores divisa actuales
		log_msg(INFMSG, 'Factores divisa actuales obtenidos, tamaño arreglo=' || arr_facs_div_act.COUNT);
		arr_carries_act := get_dif_carry_list(sysdate); -- Obtiene carries (diferencias en dias entre pares de fechas valor sucesivos)
		log_msg(INFMSG, 'Arreglo de diferencias de fechas valor obtenido, tamaño arreglo=' || arr_carries_act.COUNT);
	
		log_msg(INFMSG, 'Itera tipos de pizarrones...');
		FOR rec_tipo_pizarron IN cur_tipo_pizarron LOOP
			log_msg(INFMSG, 'Tipo de pizarron actual: ' || rec_tipo_pizarron.descripcion);
			DELETE FROM sc_renglon_pizarron WHERE id_tipo_pizarron = rec_tipo_pizarron.id_tipo_pizarron; -- elimina los renglones con id_tipo_pizarron actual
			log_msg(INFMSG, 'Renglones eliminados de SC_RENGLON_PIZARRON con id_tipo_pizarron=' || rec_tipo_pizarron.id_tipo_pizarron);
			arr_spreads_act := get_spreads_act(rec_tipo_pizarron.id_tipo_pizarron); -- obtiene spreads actuales por tipo_pizarron actual
			log_msg(INFMSG, 'Spreads actuales obtenidos para tipo_pizarron actual, tamaño de lista=' || arr_spreads_act.COUNT);
			arr_renglon_pizarron := crear_renglones( -- Construye los renglones del pizarrón
				rec_precio_ref_act, 
				arr_facs_div_act, 
				arr_spreads_act, 
				rec_sica_variacion_act, 
				arr_carries_act,
				FALSE -- No es para sucursales
			);
			log_msg(INFMSG, 'Renglones construidos para tipo_pizarron actual, tamaño lista=' || arr_renglon_pizarron.COUNT);
			insert_renglones_pizarron(arr_renglon_pizarron);
			log_msg(INFMSG, 'Renglones insertados para tipo_pizarron actual');
		END LOOP;
		
		COMMIT;
		log_msg(INFMSG, 'Commit finalizado');
		log_msg(INFMSG, 'Finaliza actualización de pizarrones');
		
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'Procedimiento refrescar_pizarrones: Error al actualizar pizarrones: ' || SQLCODE || ' - ' || SQLERRM);
			ROLLBACK;
			log_msg(INFMSG, 'Rollback finalizado');
			RAISE;
	END;
	
	-- *********************************************************************************************
	-- * Procedimiento principal que es llamado por 3 diferentes origenes externos
	-- * 	REUTERS = 1
	-- *	SICA 	= 2
	-- *	RMDS	= 3
	-- * Se encarga de actualizar el precio referencia y/o los pizarrones según el origen de la
	-- * llamada
	-- *********************************************************************************************
	PROCEDURE main_process IS
		ini_timestamp TIMESTAMP(3);
		end_timestamp TIMESTAMP(3);
		intervalo VARCHAR(100);
		llamada_desde VARCHAR(30);
		l_actualizar_pr_fd BOOLEAN := TRUE;
		l_actualizar_pizarrones BOOLEAN := TRUE;
	BEGIN
		ini_timestamp := SYSTIMESTAMP;
		global_init();
		g_log_counter := 0;
		
		log_msg(INFMSG, 'Llamada desde: ' || p_caller);
		IF p_caller = C_CALLER_REUTERS AND origen_variacion = C_ORIG_VAR_EXCEL THEN
			llamada_desde := 'Llamada desde REUTERS';
		ELSIF p_caller = C_CALLER_SICA THEN
			llamada_desde := 'Llamada desde SICA';
			l_actualizar_pr_fd := FALSE;
		ELSIF p_caller = C_CALLER_RMDS AND origen_variacion = C_ORIG_VAR_RMDS THEN
			llamada_desde := 'Llamada desde RMDS';
		ELSE
			llamada_desde := 'Llamada no válida';
			l_actualizar_pr_fd := FALSE;
			l_actualizar_pizarrones := FALSE;
		END IF;
		log_msg(TIMEMSG, llamada_desde || ' - Comienza actualización de pizarrones');
		
		IF l_actualizar_pr_fd THEN
			sica_admin.sc_sp_actualiza_prec_ref(
				p_fecha,
				p_variacion_mid,
				p_variacion_vta_spot,
				p_variacion_vta_spot_ask,
				p_cad_variacion_vta_spot,
				p_cad_variacion_vta_spot_ask,
				p_chf_variacion_vta_spot,
				p_chf_variacion_vta_spot_ask,
				p_eur_variacion_vta_spot,
				p_eur_variacion_vta_spot_ask,
				p_gbp_variacion_vta_spot,
				p_gbp_variacion_vta_spot_ask,
				p_jpy_variacion_vta_spot,
				p_jpy_variacion_vta_spot_ask
			);
		END IF;
		IF l_actualizar_pizarrones THEN
			refrescar_pizarrones();
		END IF;
		
		end_timestamp := SYSTIMESTAMP;
		intervalo := 'INI=' || TO_CHAR(ini_timestamp,'HH24:MI:SS.FF') || ', FIN=' || TO_CHAR(end_timestamp,'HH24:MI:SS.FF') 
			|| ', DIF=' || TO_CHAR(end_timestamp - ini_timestamp, 'HH24:MI:SS.FF');
		log_msg(TIMEMSG, llamada_desde || ' - Finaliza actualización de pizarrones. ' || intervalo);
	EXCEPTION
		WHEN OTHERS THEN
			log_msg(ERRMSG, 'Procedimiento main_process: Error generar pizarrones: ' || SQLCODE || ' - ' || SQLERRM);
	END;

BEGIN

	main_process();
	
END;
/

GRANT EXECUTE ON sica_admin.sc_sp_generar_pizarrones_sica TO rol_sica;

show errors;