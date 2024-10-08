--######################################################################################################################################## 
--# 
--# 	Procedimiento para respaldar swaps cancelados con todas sus dependencias f�sicas y l�gicas 
--# 
--########################################################################################################################################

set serveroutput on size 1000000

DECLARE
	
	-------------------------------------------------------------------
	--	CONSTANTES
	-------------------------------------------------------------------

	-- Status SWAP cancelado y totalmente liquidado
	C_STATUS_SWAP_CANCELADO 	CONSTANT VARCHAR2(5) 	:= 'CA';
	C_STATUS_SWAP_TOTAL_LIQU 	CONSTANT VARCHAR2(5) 	:= 'CO';
	
	-- Status que maneja el procedimiento para los Deals
	C_STATUS_DEAL_CANCELADO 	CONSTANT VARCHAR2(5) 	:= 'CA';
	C_STATUS_DEAL_TOTAL_LIQU 	CONSTANT VARCHAR2(5) 	:= 'TL';
	
	-- Status que maneja el procedimiento para los detalles de Deals
	C_STATUS_DET_CANCELADO		CONSTANT VARCHAR2(5)	:= C_STATUS_DEAL_CANCELADO;
	C_STATUS_DET_TOTAL_LIQU		CONSTANT VARCHAR2(5)	:= 'TT';
	
	-- Tipos de tablas a respaldar
	C_TIPO_TBL_REL_FISICA_DEAL CONSTANT NUMBER := 1;	-- Relaci�n f�sica con deal
	C_TIPO_TBL_REL_LOGICA_DEAL CONSTANT NUMBER := 2;	-- Relaci�n l�gica con deal
	C_TIPO_TBL_REL_NUL_DEAL CONSTANT NUMBER := 3;		-- Sin relaci�n con deal (respaldo por fecha)
	
	-- Valor del campo REVERSADO de un deal que determina si este se encuentra reversado o no
	C_NO_REVERSADO	CONSTANT INTEGER	:= 0;
	C_REVERSADO		CONSTANT INTEGER	:= 2;
	
	-- Identifica un tipo de respaldo de swaps
	C_TIPO_RESPALDO_SWAP CONSTANT INTEGER := 2;
	
	-- Formato de fecha
	C_DATE_FORMAT 		CONSTANT VARCHAR2(21)	:= 'DD/MM/YYYY HH24:MI:SS';
	
	-- Valores inicial y final para la secci�n del tiempo en el filtro por fecha
	C_TIME_PART_INI		CONSTANT VARCHAR2(10)	:= '00:00:00';
	C_TIME_PART_END		CONSTANT VARCHAR2(10)	:= '23:59:59';
	
	-- Define cuantas lineas de log se imprimen por respaldo de tabla, para mostrar progreso
	C_LOGS_X_TABLA		CONSTANT INTEGER := 20;
	
	-- Esquema
	C_SCHEMA				CONSTANT VARCHAR2(20) := 'SICA_ADMIN';
	
	-- Nombre de la columna de referencia hacia los datos de control de respaldo
	C_REF_COL_RESPLD_TBL 	CONSTANT VARCHAR2(20) := 'ID_RESPALDO_TABLA';
	C_SC_RESPALDO_TABLA 	CONSTANT VARCHAR2(20) := 'SC_RESPALDO_TABLA';
	
	-- Tipos de mensajes para la tabla SC_RESPALDO_TABLA_LOG
	-- 1 = Informacion
	-- 2 = Error
	C_TIPO_MSG_INFO CONSTANT NUMBER := 1;
	C_TIPO_MSG_ERROR CONSTANT NUMBER := 2;
	
	-- Constantes que identifican a las tablas utilizadas por este procedimiento
	C_SC_DEAL CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 1;
	C_SC_DEAL_DETALLE CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 2;
	C_SC_DEAL_LOG CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 3;
	C_SC_DEAL_STATUS_LOG CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 4;
	C_SC_DEAL_POSICION CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 5;
	C_SC_DEAL_DETALLE_STATUS_LOG CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 6;
	C_SC_POSICION_LOG CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 7;
	C_SC_LINEA_OPERACION_LOG CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 8;
	C_SC_LINEA_CAMBIO_LOG CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 9;
	C_SC_RECO_UTILIDAD CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 10;
	C_SC_UTILIDAD_FORWARD CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 11;
	C_SC_FACTOR_DIVISA CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 12;
	C_SC_PRECIO_REFERENCIA CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 13;
	
	-- Identifica a la tabla de swaps
	C_SC_SWAP CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 14;

	--	Conteo de deals por swap
	C_SQL_SEL_DEALS_BY_SWAP CONSTANT VARCHAR(300) :=
		'DEALS_BY_SWAP AS (
			SELECT S.ID_FOLIO_SWAP, COUNT(D.ID_DEAL) AS DEALS
			FROM SC_SWAP S
				INNER JOIN SC_DEAL D
					ON S.ID_FOLIO_SWAP = D.ID_FOLIO_SWAP
			GROUP BY S.ID_FOLIO_SWAP
		)';
		
	--	Conteo de deals con status = :sttdeal por swap
	C_SQL_SEL_DEALS_CA_BY_SWAP CONSTANT VARCHAR(300) :=	
		'DEALS_CA_BY_SWAP AS (
			SELECT S.ID_FOLIO_SWAP, COUNT(D.ID_DEAL) AS DEALS
			FROM SC_SWAP S
				INNER JOIN SC_DEAL D
					ON S.ID_FOLIO_SWAP = D.ID_FOLIO_SWAP
			WHERE D.STATUS_DEAL = ''CA''        
			GROUP BY S.ID_FOLIO_SWAP
		)';
		
	--	Conteo de deals con status = TL, rev = 0 por swap
	C_SQL_SEL_DEALS_TL_BY_SWAP CONSTANT VARCHAR(300) :=	
		'DEALS_TL_BY_SWAP AS (
			SELECT S.ID_FOLIO_SWAP, COUNT(D.ID_DEAL) AS DEALS
			FROM SC_SWAP S
				INNER JOIN SC_DEAL D
					ON S.ID_FOLIO_SWAP = D.ID_FOLIO_SWAP
			WHERE D.STATUS_DEAL = ''TL''        
				AND D.REVERSADO = 0
			GROUP BY S.ID_FOLIO_SWAP
		)';
	
	--	Consulta de conteo de detalles por deal
	C_SQL_SEL_DETS_BY_DEAL CONSTANT VARCHAR2(300) := 
		' DETS_BY_DEAL AS (
			SELECT D.ID_DEAL, COUNT(DD.ID_DEAL_POSICION) AS DETALLES
			FROM SC_DEAL D
				INNER JOIN SC_DEAL_DETALLE DD
					ON D.ID_DEAL = DD.ID_DEAL
			GROUP BY D.ID_DEAL
		) ';
	
	--	Consulta de conteo de detalles con status = CANCELADO por deal
	C_SQL_SEL_DETS_CA_BY_DEAL CONSTANT VARCHAR2(300) :=
		' DETS_CA_BY_DEAL AS (
			SELECT D.ID_DEAL, COUNT(DD.ID_DEAL_POSICION) AS DETALLES
			FROM SC_DEAL D
				INNER JOIN SC_DEAL_DETALLE DD
					ON D.ID_DEAL = DD.ID_DEAL
			WHERE DD.STATUS_DETALLE_DEAL = ''CA''
			GROUP BY D.ID_DEAL
		) ';
		
	--	Consulta de conteo de detalles con status = TT por deal
	C_SQL_SEL_DETS_TT_BY_DEAL CONSTANT VARCHAR2(300) :=
		' DETS_TT_BY_DEAL AS (
			SELECT D.ID_DEAL, COUNT(DD.ID_DEAL_POSICION) AS DETALLES
			FROM SC_DEAL D
				INNER JOIN SC_DEAL_DETALLE DD
					ON D.ID_DEAL = DD.ID_DEAL
			WHERE DD.STATUS_DETALLE_DEAL = ''TT''
			GROUP BY D.ID_DEAL
		) ';
		
	--	Consulta de conteo de detalles por factor divisa
	C_SQL_SEL_DETS_BY_FD CONSTANT VARCHAR(300) :=
		' DETS_BY_FACTOR AS (
			SELECT FD.ID_FACTOR_DIVISA, COUNT(DD.ID_DEAL_POSICION) AS DETALLES
			FROM SC_FACTOR_DIVISA FD
				INNER JOIN SC_DEAL_DETALLE DD
				ON FD.ID_FACTOR_DIVISA = DD.ID_FACTOR_DIVISA
			GROUP BY FD.ID_FACTOR_DIVISA
		) ';
	
	--	Consulta de conteo de detalles con status = :sttfacdet por factor divisa
    C_SQL_SEL_DETS_CA_BY_FD CONSTANT VARCHAR(300) :=
		'DETS_CA_BY_FACTOR AS (
			SELECT FD.ID_FACTOR_DIVISA, COUNT(DD.ID_DEAL_POSICION) AS DETALLES
			FROM SC_FACTOR_DIVISA FD
				INNER JOIN SC_DEAL_DETALLE DD
				ON FD.ID_FACTOR_DIVISA = DD.ID_FACTOR_DIVISA
			WHERE DD.STATUS_DETALLE_DEAL = ''CA''
			GROUP BY FD.ID_FACTOR_DIVISA
		)';
		
	--	Consulta de conteo de detalles con status = :sttfacdet por factor divisa
    C_SQL_SEL_DETS_TT_BY_FD CONSTANT VARCHAR(300) :=
		'DETS_TT_BY_FACTOR AS (
			SELECT FD.ID_FACTOR_DIVISA, COUNT(DD.ID_DEAL_POSICION) AS DETALLES
			FROM SC_FACTOR_DIVISA FD
				INNER JOIN SC_DEAL_DETALLE DD
				ON FD.ID_FACTOR_DIVISA = DD.ID_FACTOR_DIVISA
			WHERE DD.STATUS_DETALLE_DEAL = ''TT''
			GROUP BY FD.ID_FACTOR_DIVISA
		)';
		
	--	Consulta de conteo de detalles por precio de referencia
	C_SQL_SEL_DETS_BY_PR CONSTANT VARCHAR(300) :=
		' DETS_BY_PREC_REF AS (
			SELECT PR.ID_PRECIO_REFERENCIA, COUNT(DD.ID_DEAL_POSICION) AS DETALLES
			FROM SC_PRECIO_REFERENCIA PR
				INNER JOIN SC_DEAL_DETALLE DD
				ON PR.ID_PRECIO_REFERENCIA = DD.ID_PRECIO_REFERENCIA
			GROUP BY PR.ID_PRECIO_REFERENCIA
		) ';
	
	--	Consulta de conteo de detalles con estatus = sttprdet por precio de referencia
	C_SQL_SEL_DETS_CA_BY_PR CONSTANT VARCHAR(400) := 
		' DETS_CA_BY_PREC_REF AS (
			SELECT PR.ID_PRECIO_REFERENCIA, COUNT(DD.ID_DEAL_POSICION) AS DETALLES
			FROM SC_PRECIO_REFERENCIA PR
				INNER JOIN SC_DEAL_DETALLE DD
				ON PR.ID_PRECIO_REFERENCIA = DD.ID_PRECIO_REFERENCIA
			WHERE DD.STATUS_DETALLE_DEAL = ''CA''
			GROUP BY PR.ID_PRECIO_REFERENCIA
		) ';
		
	C_SQL_SEL_DETS_TT_BY_PR CONSTANT VARCHAR(400) := 
		' DETS_TT_BY_PREC_REF AS (
			SELECT PR.ID_PRECIO_REFERENCIA, COUNT(DD.ID_DEAL_POSICION) AS DETALLES
			FROM SC_PRECIO_REFERENCIA PR
				INNER JOIN SC_DEAL_DETALLE DD
				ON PR.ID_PRECIO_REFERENCIA = DD.ID_PRECIO_REFERENCIA
			WHERE DD.STATUS_DETALLE_DEAL = ''TT''
			GROUP BY PR.ID_PRECIO_REFERENCIA
		) ';
		
	--	Secci�n general del FROM que aplica para todas las consultas de respaldo
	C_SQL_SEL_FROM_PART CONSTANT VARCHAR2(1000) :=
		' FROM 
			SC_SWAP S
				INNER JOIN SC_DEAL D 
					ON S.ID_FOLIO_SWAP = D.ID_FOLIO_SWAP
                INNER JOIN SC_DEAL_DETALLE DD 
				    ON DD.ID_DEAL = D.ID_DEAL 
				INNER JOIN SC_DEAL_POSICION DP
					ON DP.ID_DEAL_POSICION = DD.ID_DEAL_POSICION
				INNER JOIN DETS_BY_DEAL DBD 
				    ON D.ID_DEAL = DBD.ID_DEAL 
				INNER JOIN DEALS_BY_SWAP DBS
					ON DBS.ID_FOLIO_SWAP = S.ID_FOLIO_SWAP ';
		
	--	Secci�n del WHERE que aplica para todas las consultas de respaldo
	C_SQL_SEL_WHERE_PART CONSTANT VARCHAR2(300) :=
		' WHERE 
			S.STATUS = :sttswap
			AND D.FECHA_CAPTURA >= TO_DATE(:inidate || ' || ''' ' || C_TIME_PART_INI || ''',''' || C_DATE_FORMAT || ''')
			AND D.FECHA_CAPTURA <= TO_DATE(:enddate || ' || ''' ' || C_TIME_PART_END || ''',''' || C_DATE_FORMAT || ''') ';
	
	---------
	--	Filtro para especificar deals reversados o no reversados:
	--		- :rev = 0 = No reversado
	--		- :rev = 2 = Reversado
	---------
	C_SQL_SEL_WHERE_PART_REVERSADO CONSTANT VARCHAR2(30) :=
		' AND D.REVERSADO = :rev ';
	
	-------------------------------------------------------------------
	--	TIPOS
	-------------------------------------------------------------------
	
	-- Cursor generico
	TYPE cur_type_ref IS REF CURSOR;
	
	-------------------------------------------------------------------
	--	EXCEPCIONES
	-------------------------------------------------------------------
	
	chk_total_after_backup_ex EXCEPTION;
	
    -------------------------------------------------------------------
	--	FUNCIONES Y PROCEDIMIENTOS DE USO GENERAL
	-------------------------------------------------------------------
    
    --------------
    --  Genera el siguiente valor de la llave primaria de la tabla SC_RESPALDO 
	--	
	--	return NUMBER
	--		Siguiente valor de la llave primaria de la tabla SC_RESPALDO
    --------------
	FUNCTION get_respaldo_next_pk_val RETURN NUMBER IS
		respaldo_pk_max_val NUMBER := 0;
	BEGIN
		SELECT MAX(id_respaldo) INTO respaldo_pk_max_val 
		FROM sica_admin.sc_respaldo;
        IF respaldo_pk_max_val IS NULL THEN
            RETURN 1;
        END IF;
        RETURN (respaldo_pk_max_val + 1);
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			RETURN 1;
	END;
	
	-----------
    --  Genera el siguiente valor de la llave primaria de la tabla SC_RESPALDO_TABLA 
	--	
	--	return NUMBER
	--		Siguiente valor de la llave primaria de la tabla SC_RESPALDO_TABLA
    -----------
	FUNCTION get_respaldo_tbl_next_pk_val RETURN NUMBER IS
		respaldo_tbl_pk_max_val NUMBER := 0;
	BEGIN
		SELECT MAX(id_respaldo_tabla) INTO respaldo_tbl_pk_max_val 
		FROM sica_admin.sc_respaldo_tabla;
        IF respaldo_tbl_pk_max_val IS NULL THEN
            RETURN 1;
        END IF;
        RETURN (respaldo_tbl_pk_max_val + 1);
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			RETURN 1;
	END;
    
	------------
	--	Valida las cifras de control obtenidas al principio y al final de un proceso de respaldo
	--	
	--	params:
	--		rec_sc_respaldo_tabla: Registro que contiene las cifras de control
	--	return:
	--		BOOLEAN: TRUE si las cifras son v�lidad, de lo contrario FALSE
	------------
	FUNCTION valida_cifras_control (
		rec_sc_respaldo_tabla IN sica_admin.sc_respaldo_tabla%ROWTYPE
	) RETURN BOOLEAN IS
		valid BOOLEAN := TRUE;
		selected_rows NUMBER := rec_sc_respaldo_tabla.regs_seleccionados; 
		inserted_rows NUMBER := rec_sc_respaldo_tabla.regs_insertados; 
		deleted_rows NUMBER := rec_sc_respaldo_tabla.regs_eliminados;
		rows_before_backup NUMBER := rec_sc_respaldo_tabla.regs_total_antes;
		rows_after_backup NUMBER := rec_sc_respaldo_tabla.regs_total_despues;
	BEGIN
		IF (selected_rows <> deleted_rows AND deleted_rows <> inserted_rows) THEN
			valid := FALSE;
		END IF;
		IF ((rows_after_backup + inserted_rows) <> rows_before_backup) THEN
			valid := FALSE;
		END IF;
		RETURN valid;
	END;
	
	-----------
	--	Inserta un nuevo registro en SC_RESPALDO_TABLA para la tabla y el id_respaldo especificados
	--	
	--	params
	--		p_id_tabla NUMBER: Identificador de la tabla a respaldar
	--		p_id_respaldo NUMBER: Identificador del respaldo
	--	return
	--		NUMBER: Identificador generado para el nuevo registro de SC_RESPALDO_TABLA
	-----------
	FUNCTION inserta_sc_respaldo_tabla (p_id_tabla IN NUMBER, p_id_respaldo IN NUMBER) RETURN NUMBER IS
		r_id_respaldo_tabla NUMBER := 0;
	BEGIN
		r_id_respaldo_tabla := get_respaldo_tbl_next_pk_val();
		INSERT INTO sica_admin.sc_respaldo_tabla (
			id_respaldo_tabla, id_respaldo, id_tabla, creation_time
		) VALUES (
			r_id_respaldo_tabla, p_id_respaldo, p_id_tabla, SYSTIMESTAMP
		);
		RETURN r_id_respaldo_tabla;
	END;
	
	------------------
	--	Almacena un mensaje en la tabla SC_RESPALDO_TABLA_LOG
	--        
	--		params:
	--      	p_id_respaldo NUMBER: Identificador del respaldo
	--			p_id_tabla NUMBER: Identificador de la tabla
	--			p_tipo_mensaje NUMBER: Tipo de mensaje 1 = Info, 2 = Error
	--			p_mensaje VARCHAR2: Mensaje a almacenar
	------------------
	PROCEDURE log_respld (
		p_id_respaldo NUMBER,
		p_id_tabla NUMBER,
		p_tipo_mensaje NUMBER,
		p_mensaje VARCHAR2
	) IS
		PRAGMA AUTONOMOUS_TRANSACTION;
		max_id_respld_tbl_log NUMBER := 0;
        next_id_respld_tbl_log NUMBER := 0;
	BEGIN
		SELECT MAX(id_respaldo_tabla_log) INTO max_id_respld_tbl_log
		FROM sica_admin.sc_respaldo_tabla_log;
		
		IF max_id_respld_tbl_log IS NULL THEN
			max_id_respld_tbl_log := 0;
		END IF;
        
        next_id_respld_tbl_log := max_id_respld_tbl_log + 1;
	
		INSERT INTO sica_admin.sc_respaldo_tabla_log (
			id_respaldo_tabla_log,
			log_time,
			id_respaldo,
			id_tabla,
			tipo_mensaje,
			mensaje
		) VALUES (
			next_id_respld_tbl_log,
			SYSTIMESTAMP,
			p_id_respaldo,
			p_id_tabla,
			p_tipo_mensaje,
			p_mensaje
		);
		COMMIT;
	EXCEPTION
		WHEN OTHERS THEN -- Garantizar qie este procedimiento siempre escriba el mensaje
			DBMS_OUTPUT.PUT_LINE('LOG - log_time=' || SYSTIMESTAMP || ', id_respaldo=' || p_id_respaldo || ', id_tabla=' || p_id_tabla || ', tipo_mensaje=' || p_tipo_mensaje || ', mensaje=' || p_mensaje || ':' || SQLCODE || ' - ' || SQLERRM);
	END;
	
	------------    
	--   Copia los registros de la tabla origen a la tabla hist�rica
	--        
	--   params
	--       table_to_backup : Registro que representa al tabla a respaldar
	--       ini_date : Fecha de inicio
	--       end_date : Fecha final
	--  	 status_swap: Estatus de los deals
	--		 p_id_respaldo: Identificador del respaldo
	------------    
	PROCEDURE insert_to_hist(
		table_to_backup IN sica_admin.sc_cat_tabla_respaldo%ROWTYPE, 
		ini_date IN VARCHAR2, 
		end_date IN VARCHAR2,
		status_swap IN VARCHAR2, 
		p_id_respaldo IN NUMBER
	) IS
		sql_count_sentence VARCHAR2(4000) := NULL;
		sql_rows_sentence VARCHAR2(4000) := NULL;
		sql_with_part VARCHAR(4000) := NULL;
		sql_from_part VARCHAR(4000) := NULL;
		sql_where_part VARCHAR(4000) := NULL;
		sql_from_where_part VARCHAR(4000) := NULL;
		inserted_rows NUMBER := 0;
		l_id_respaldo_tabla NUMBER := 0;
		r_row_count INTEGER := 0;
		sql_insert_sentence VARCHAR2(4000) := 0;
	BEGIN
		sql_with_part := 'WITH '
			|| C_SQL_SEL_DEALS_BY_SWAP
			|| ', ' 
			|| C_SQL_SEL_DETS_BY_DEAL;
			
		sql_from_part := C_SQL_SEL_FROM_PART;
		sql_where_part := C_SQL_SEL_WHERE_PART;
		
		-- Contruye la senetencia SQL de selecci�n para extraer los datos a enviar a hist�rico
		-- SWAP cancelado
		IF status_swap = C_STATUS_SWAP_CANCELADO THEN
			sql_with_part := sql_with_part 
				|| ', ' || C_SQL_SEL_DEALS_CA_BY_SWAP
				|| ', ' || C_SQL_SEL_DETS_CA_BY_DEAL;
			sql_from_part := sql_from_part
				|| ' INNER JOIN DEALS_CA_BY_SWAP DCBS
						ON DCBS.ID_FOLIO_SWAP = S.ID_FOLIO_SWAP
					 INNER JOIN DETS_CA_BY_DEAL DCBD
						ON DCBD.ID_DEAL = D.ID_DEAL ';
			sql_where_part := sql_where_part
				|| ' AND DBS.DEALS = DCBS.DEALS 
					 AND DBD.DETALLES = DCBD.DETALLES ';
			IF table_to_backup.id_tabla = C_SC_FACTOR_DIVISA THEN
				sql_with_part := sql_with_part
					|| ', ' || C_SQL_SEL_DETS_BY_FD
					|| ', ' || C_SQL_SEL_DETS_CA_BY_FD;
				sql_from_part := sql_from_part
					|| ' INNER JOIN DETS_BY_FACTOR DBF
							ON DBF.ID_FACTOR_DIVISA = DD.ID_FACTOR_DIVISA
						 INNER JOIN DETS_CA_BY_FACTOR DCBF
							ON DCBF.ID_FACTOR_DIVISA = DD.ID_FACTOR_DIVISA ';
				sql_where_part := sql_where_part
						|| ' AND DBF.DETALLES = DCBF.DETALLES ';
			END IF;
			IF table_to_backup.id_tabla = C_SC_PRECIO_REFERENCIA THEN
				sql_with_part := sql_with_part
					|| ', ' || C_SQL_SEL_DETS_BY_PR
					|| ', ' || C_SQL_SEL_DETS_CA_BY_PR;
				sql_from_part := sql_from_part
					|| ' INNER JOIN DETS_BY_PREC_REF DBPR
							ON DBPR.ID_PRECIO_REFERENCIA = DD.ID_PRECIO_REFERENCIA
						 INNER JOIN DETS_CA_BY_PREC_REF DCBPR
							ON DCBPR.ID_PRECIO_REFERENCIA = DD.ID_PRECIO_REFERENCIA ';
				sql_where_part := sql_where_part
						|| ' AND DBPR.DETALLES = DCBPR.DETALLES ';
			END IF;
		END IF;
		
		-- SWAP totalemnte liquidado
		IF status_swap = C_STATUS_SWAP_TOTAL_LIQU THEN
			sql_with_part := sql_with_part 
				|| ', ' || C_SQL_SEL_DEALS_TL_BY_SWAP
				|| ', ' || C_SQL_SEL_DETS_TT_BY_DEAL
				|| ', ' || C_SQL_SEL_DETS_CA_BY_DEAL;
			sql_from_part := sql_from_part
				|| ' INNER JOIN DEALS_TL_BY_SWAP DTBS
						ON DTBS.ID_FOLIO_SWAP = S.ID_FOLIO_SWAP
					 INNER JOIN DETS_TT_BY_DEAL DTBD
						ON DTBD.ID_DEAL = D.ID_DEAL 
					 INNER JOIN DETS_CA_BY_DEAL DCBD 
						ON DCBD.ID_DEAL = D.ID_DEAL ';
			sql_where_part := sql_where_part
				|| ' AND DBS.DEALS = DTBS.DEALS 
					 AND DBD.DETALLES = (DTBD.DETALLES + DCBD.DETALLES) ';
			IF table_to_backup.id_tabla = C_SC_FACTOR_DIVISA THEN
				sql_with_part := sql_with_part
					|| ', ' || C_SQL_SEL_DETS_BY_FD
					|| ', ' || C_SQL_SEL_DETS_TT_BY_FD
					|| ', ' || C_SQL_SEL_DETS_CA_BY_FD;
				sql_from_part := sql_from_part
					|| ' INNER JOIN DETS_BY_FACTOR DBF
							ON DBF.ID_FACTOR_DIVISA = DD.ID_FACTOR_DIVISA
						 INNER JOIN DETS_TT_BY_FACTOR DTBF
							ON DTBF.ID_FACTOR_DIVISA = DD.ID_FACTOR_DIVISA 
						 INNER JOIN DETS_CA_BY_FACTOR DCBF 
							ON DCBF.ID_FACTOR_DIVISA = DD.ID_FACTOR_DIVISA ';
				sql_where_part := sql_where_part
						|| ' AND DBF.DETALLES = (DTBF.DETALLES + DCBF.DETALLES) ';
			END IF;
			IF table_to_backup.id_tabla = C_SC_PRECIO_REFERENCIA THEN
				sql_with_part := sql_with_part
					|| ', ' || C_SQL_SEL_DETS_BY_PR
					|| ', ' || C_SQL_SEL_DETS_TT_BY_PR
					|| ', ' || C_SQL_SEL_DETS_CA_BY_PR;
				sql_from_part := sql_from_part
					|| ' INNER JOIN DETS_BY_PREC_REF DBPR
							ON DBPR.ID_PRECIO_REFERENCIA = DD.ID_PRECIO_REFERENCIA
						 INNER JOIN DETS_TT_BY_PREC_REF DTBPR
							ON DTBPR.ID_PRECIO_REFERENCIA = DD.ID_PRECIO_REFERENCIA 
						 INNER JOIN DETS_CA_BY_PREC_REF DCBPR 
							ON DCBPR.ID_PRECIO_REFERENCIA = DD.ID_PRECIO_REFERENCIA ';
				sql_where_part := sql_where_part
						|| ' AND DBPR.DETALLES = (DTBPR.DETALLES + DCBPR.DETALLES) ';
			END IF;
		END IF;
		
		IF table_to_backup.join_on_tbl_orig IS NOT NULL THEN
			sql_from_part := sql_from_part || 
				' INNER JOIN ' || table_to_backup.nom_tbl_orig || ' ' || table_to_backup.alias_tbl_orig
					|| ' ON ' || table_to_backup.join_on_tbl_orig || ' ';
		END IF;
		
		sql_from_where_part := sql_from_part || sql_where_part;
		
		sql_count_sentence := sql_with_part
			|| 'SELECT COUNT (DISTINCT ' || table_to_backup.alias_tbl_orig || '.' || table_to_backup.count_fields || ') '
			|| sql_from_where_part;
		
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Comienza proceso de inserci�n para la tabla' || table_to_backup.nom_tbl_hist);
				
		-- Inserta un nuevo registro en SC_RESPALDO_TABLA para controlar el respaldo de la tabla
		l_id_respaldo_tabla := inserta_sc_respaldo_tabla (table_to_backup.id_tabla, p_id_respaldo);
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Registro creado en SC_RESPALDO_TABLA, id_respaldo_tabla=' || l_id_respaldo_tabla);
				
		sql_rows_sentence := sql_with_part
			|| 'SELECT DISTINCT ' || table_to_backup.alias_tbl_orig || '.*, ' || l_id_respaldo_tabla
			|| sql_from_where_part;
		
		sql_insert_sentence := 'INSERT INTO ' || table_to_backup.nom_tbl_hist || ' ' || sql_rows_sentence;
		
		--DBMS_OUTPUT.PUT_LINE('======> select rows ...' || sql_rows_sentence);
		--DBMS_OUTPUT.PUT_LINE('======> select count ...' || sql_count_sentence);
		
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Ejecutando inserts en tabla hist�rica actual');
		
		EXECUTE IMMEDIATE sql_count_sentence INTO r_row_count 
			USING status_swap, ini_date, end_date;
		EXECUTE IMMEDIATE sql_insert_sentence
			USING status_swap, ini_date, end_date;
		
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Inserts finalizados');
		
		EXECUTE IMMEDIATE 'SELECT COUNT(' || table_to_backup.count_fields || ') ' 
			||' FROM ' || table_to_backup.nom_tbl_hist 
			|| ' WHERE ' || C_REF_COL_RESPLD_TBL || ' = :respldtbl' INTO inserted_rows USING l_id_respaldo_tabla;
		
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Registros seleccionados: ' || r_row_count);
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Registros insertados: ' || inserted_rows);
			
		-- Actualiza cifras de control
		UPDATE sica_admin.sc_respaldo_tabla 
		SET regs_seleccionados = r_row_count, regs_insertados = inserted_rows, last_modif_time = SYSTIMESTAMP
		WHERE id_respaldo_tabla = l_id_respaldo_tabla;
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Cifras de control actualizadas (regs_sel, regs_insert)');
		log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Ejecutando commit');
		COMMIT;
		log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Commit finalizado');
	EXCEPTION
		WHEN OTHERS THEN
			log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_ERROR, 'Procedimiento INSERT_TO_HIST: Error al insertar registros en tabla historica: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	----------
	--   Elimina los registros insertados en hist�ricos de la tabla original
	--
	--	 params
	--		rec_tbls_to_delete: Registro que representa la tabla cuyos registros van a eliminarse
	--		p_id_respaldo: Identificador del respaldo
	----------
	PROCEDURE delete_from_orig(rec_tbls_to_delete IN sica_admin.sc_cat_tabla_respaldo%ROWTYPE, p_id_respaldo IN NUMBER) IS
		rows_before_delete NUMBER := 0;
		rows_after_delete NUMBER := 0;
		deleted_rows NUMBER := 0;
		rec_sc_respaldo_tabla sica_admin.sc_respaldo_tabla%ROWTYPE := NULL;
	BEGIN
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Comienza delete de la tabla: ' || rec_tbls_to_delete.nom_tbl_orig);
	
		-- Cuenta registros en la tabla original antes de ser eliminados
		EXECUTE IMMEDIATE 'SELECT COUNT(' || rec_tbls_to_delete.count_fields || ') ' ||' FROM ' || rec_tbls_to_delete.nom_tbl_orig
			INTO rows_before_delete;
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Registros antes de eliminar: ' || rows_before_delete);
	
		EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || rec_tbls_to_delete.nom_tbl_orig || ' DISABLE ALL TRIGGERS';
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Triggers inhabilitados');
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Eliminando registros de la tabla');
		EXECUTE IMMEDIATE 'DELETE FROM ' || rec_tbls_to_delete.nom_tbl_orig 
			|| ' WHERE ' || rec_tbls_to_delete.count_fields || ' IN (' 
				|| 'SELECT tblh.' || rec_tbls_to_delete.count_fields 
				|| ' FROM '|| rec_tbls_to_delete.nom_tbl_hist || ' tblh '
					|| ' INNER JOIN ' || C_SC_RESPALDO_TABLA || ' rt '
						|| ' ON ' || ' tblh. ' || C_REF_COL_RESPLD_TBL || ' = rt.' || C_REF_COL_RESPLD_TBL
				|| ' WHERE rt.id_respaldo = :idrespld AND rt.id_tabla = :idtabla' 
			|| ')' USING p_id_respaldo, rec_tbls_to_delete.id_tabla;
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Eliminaci�n completa');
		EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || rec_tbls_to_delete.nom_tbl_orig || ' ENABLE ALL TRIGGERS';
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Triggers habilitados');
		
		-- Cuenta registros en la tabla original despues de ser eliminados
		EXECUTE IMMEDIATE 'SELECT COUNT(' || rec_tbls_to_delete.count_fields || ') ' ||' FROM ' || rec_tbls_to_delete.nom_tbl_orig
			INTO rows_after_delete;
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Registros despu�s de eliminar: ' || rows_after_delete);
		
		deleted_rows := rows_before_delete - rows_after_delete;
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Registros eliminados: ' || deleted_rows);
		
		-- Actualiza cifras de control
		UPDATE sica_admin.sc_respaldo_tabla 
		SET regs_eliminados = deleted_rows, regs_total_antes = rows_before_delete, regs_total_despues = rows_after_delete, last_modif_time = SYSTIMESTAMP
		WHERE id_respaldo = p_id_respaldo AND id_tabla = rec_tbls_to_delete.id_tabla;
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Cifras de control actualizadas');
		
		SELECT * INTO rec_sc_respaldo_tabla 
		FROM sc_respaldo_tabla 
		WHERE id_respaldo = p_id_respaldo AND id_tabla = rec_tbls_to_delete.id_tabla;
		
		IF valida_cifras_control(rec_sc_respaldo_tabla) = FALSE THEN
			log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_ERROR, 'Error al eliminar registros de tabla: Cifras de control inconsistentes');
			RAISE chk_total_after_backup_ex;
		END IF;
		
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Cifras de control validadas correctamente');
		log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Ejecutando commit');
		COMMIT;
		log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Commit finalizado');
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Finaliza delete de la tabla: ' || rec_tbls_to_delete.nom_tbl_orig);
	EXCEPTION
		WHEN OTHERS THEN
			log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_ERROR, 'Procedimiento DELETE_FROM_ORIG: Error al eliminar registros de tabla original: ' || SQLCODE || ' - ' || SQLERRM);
			EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || rec_tbls_to_delete.nom_tbl_orig || ' ENABLE ALL TRIGGERS';
			log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Triggers habilitados');
			RAISE;
	END;
	
	-------------------------------------------------------------------
	--	PROCEDIMIENTO PRINCIPAL DE RESPALDO
	-------------------------------------------------------------------
	 
	-------------
	--	Realiza el respaldo de todas las tablas con relacion hacia SC_DEAL
	--	
	--	params
	--		ini_date VARCHAR2: Fecha inicial en formato 'DD/MM/YYYY'
	--		end_date VARCHAR2: Fecha final en formato 'DD/MM/YYYY'
	--		status_swap VARCHAR2: Estatus de los deals a respaldar, puede tener solo 1 valor
	--				- 'CA' - Deals cancelados
	-------------
	 PROCEDURE respalda_todo (ini_date IN VARCHAR2, end_date IN VARCHAR2, status_swap IN VARCHAR2) IS
		g_id_respaldo NUMBER := 0;
		TYPE cat_tbl_respld_type IS TABLE OF sica_admin.sc_cat_tabla_respaldo%ROWTYPE INDEX BY BINARY_INTEGER;
		cat_tbl_respld_array cat_tbl_respld_type;
		tablas_procesadas INTEGER := 0;
	 BEGIN
		DBMS_OUTPUT.PUT_LINE('Comienza ejecuci�n de respaldo de Swaps');
		SELECT *
			BULK COLLECT INTO cat_tbl_respld_array
		FROM sica_admin.sc_cat_tabla_respaldo
		WHERE tipo_tabla IN (C_TIPO_TBL_REL_FISICA_DEAL, C_TIPO_TBL_REL_LOGICA_DEAL)
		ORDER BY orden_supr ASC NULLS LAST;
		
		-- Genera Id de respaldo e inserta un registro en la tabla de control SC_RESPALDO
		g_id_respaldo := get_respaldo_next_pk_val();
		INSERT INTO SICA_ADMIN.SC_RESPALDO (
			ID_RESPALDO, FECHA_CREACION, TIPO_RESPALDO, FILTRO_FECHA_INI, FILTRO_FECHA_FIN, FILTRO_ESTATUS, FECHA_RESTAURACION
		) VALUES (
			g_id_respaldo, SYSTIMESTAMP, C_TIPO_RESPALDO_SWAP, ini_date, end_date, status_swap, NULL
		);
		COMMIT;
		
		log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Comienza respaldo de Swaps: Fecha inicial=' || ini_date || ', Fecha final=' || end_date || ', Estatus=' || status_swap);
		log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Registro de respaldo creado, Id de respaldo: ' || g_id_respaldo);
		
		-- Realiza el respaldo para todas las tablas
		-- Realiza todos los inserts
		log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Comienza proceso de inserci�n en tablas hist�ricas');
		FOR indx IN cat_tbl_respld_array.FIRST .. cat_tbl_respld_array.LAST LOOP
			insert_to_hist(cat_tbl_respld_array(indx), ini_date, end_date, status_swap, g_id_respaldo) ;
			tablas_procesadas := tablas_procesadas + 1;
		END LOOP;
		log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Finaliza proceso de inserci�n en tablas hist�ricas, tablas procesadas: ' || tablas_procesadas);
		
		tablas_procesadas := 0;
		-- Realiza todos los deletes
		log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Comienza proceso de eliminaci�n de registros de tablas origen');
		FOR indx IN cat_tbl_respld_array.FIRST .. cat_tbl_respld_array.LAST LOOP
			delete_from_orig(cat_tbl_respld_array(indx), g_id_respaldo);
			tablas_procesadas := tablas_procesadas + 1;
		END LOOP;
		log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Finaliza proceso de eliminaci�n de registros de tablas origen: tablas procesadas: ' || tablas_procesadas);
		log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Proceso de respaldo de Swaps finalizado');
		DBMS_OUTPUT.PUT_LINE('Termina ejecuci�n de respaldo de Swaps');
	 EXCEPTION
		WHEN OTHERS THEN
			log_respld(g_id_respaldo, NULL, C_TIPO_MSG_ERROR, 'Procedimiento RESPALDA_TODO de Swaps: Error al respaldar las tablas: ' || SQLCODE || ' - ' || SQLERRM);
			log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Ejecutando rollback');
			ROLLBACK;
			log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Rollback finalizado');
			DBMS_OUTPUT.PUT_LINE('Termina ejecuci�n de respaldo de Swaps');
			--RAISE;
	 END;
	 	
BEGIN
	-- Llamadas para producci�n, descomentar
	-- 2007
	respalda_todo('01/01/2007','31/12/2007','CA');
	respalda_todo('01/01/2007','31/12/2007','CO');
	
	-- 2008
	respalda_todo('01/01/2008','31/12/2008','CA');
	respalda_todo('01/01/2008','31/12/2008','CO');
	
	-- 2009
	respalda_todo('01/01/2009','31/12/2009','CA');
	respalda_todo('01/01/2009','31/12/2009','CO');
	
	-- Llamada al procedimiento para prueba en ambiente local: Comentar l�nea para ejecuci�n en producci�n
	-- respalda_todo('01/01/2007','31/12/2008','CA');  
	
	--NULL;
END;

/
