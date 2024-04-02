--######################################################################################################################################## 
--# 
--# 	Procedimiento para reversar un respaldo de deals con todas sus dependencias físicas y lógicas 
--# 
--######################################################################################################################################## 

set serveroutput on size 1000000

DECLARE

	-------------------------------------------------------------------
	--	CONSTANTES
	-------------------------------------------------------------------
	
	-- Clausula from parcial utilizada para la selección de los registros de las tablas históricas a reversar
	C_SQL_SEL_FROM CONSTANT VARCHAR(200) := 
		'FROM
			SC_RESPALDO r
			INNER JOIN SC_RESPALDO_TABLA rt
				ON r.ID_RESPALDO = rt.ID_RESPALDO
			INNER JOIN SC_CAT_TABLA_RESPALDO ctr
				ON ctr.ID_TABLA = rt.ID_TABLA';
	
	-- Clausula WHERE utilizada para la selección de los registros de las tablas históricas a reversar
	C_SQL_SEL_WHERE CONSTANT VARCHAR(100) :=
		'WHERE
			r.ID_RESPALDO = :idrespld
			AND ctr.ID_TABLA = :idtbl';
			
	-- Alias genérico para referirse a la tabla actual en proceso de respaldo
	C_GENERIC_ALIAS			CONSTANT VARCHAR2(10) := 'CURTBL';
	
	-- Define cuantas lineas de log se imprimen por respaldo de tabla, para mostrar progreso
	C_LOGS_X_TABLA		CONSTANT INTEGER := 20;
	
	-- Identifica un tipo de respaldo por fecha
	C_TIPO_RESPALDO_FECHA CONSTANT INTEGER := 3;
	
	-- Tipos de tablas a respaldar
	C_TIPO_TBL_REL_NUL_DEAL CONSTANT NUMBER := 3;		-- Sin relación con deal (respaldo por fecha)
	
	-- Esquema
	C_SCHEMA				CONSTANT VARCHAR2(20) := 'SICA_ADMIN';
	
	-- Nombre de la columna de referencia hacia los datos de control de respaldo
	C_SC_RESPALDO_TABLA 	CONSTANT VARCHAR2(20) := 'SC_RESPALDO_TABLA';
	C_REF_COL_RESPLD_TBL 	CONSTANT VARCHAR2(20) := 'ID_RESPALDO_TABLA';
	
	-- Constantes que identifican a las tablas utilizadas por este procedimiento
	C_SAP_A_BITACORA_XS CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 15;
	C_SAP_A_GENPOL_XS CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 16;
	C_SC_LOG_AUDITORIA CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 17;
	C_SC_MOVIMIENTO_CONTABLE CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 18;
	C_SC_MOVIMIENTO_CONT_DETALLE CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 19;
	C_SC_ACTIVIDAD CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 20;
	C_SC_POLIZA CONSTANT sica_admin.sc_cat_tabla_respaldo.id_tabla%TYPE := 21;
	
	-- Tipos de mensajes para la tabla SC_RESPALDO_TABLA_LOG
	-- 1 = Informacion
	-- 2 = Error
	C_TIPO_MSG_INFO CONSTANT NUMBER := 1;
	C_TIPO_MSG_ERROR CONSTANT NUMBER := 2;
	
	-------------------------------------------------------------------
	--	TIPOS
	-------------------------------------------------------------------
	
	-- Cursor genérico
	TYPE cur_type_ref IS REF CURSOR;
	
	-------------------------------------------------------------------
	--	EXCEPCIONES
	-------------------------------------------------------------------
	
	-- Ocurre cuando la validación de cifras de control es incorrecta
	chk_total_after_backup_ex EXCEPTION;
	
	-------------------------------------------------------------------
	--	CURSORES
	-------------------------------------------------------------------
	 
	-- Obtiene todos los identificadores de respaldos generados para tipos de respaldo sin relación con deals (por fecha)
	 CURSOR cur_all_resplds IS
		SELECT id_respaldo
		FROM sica_admin.sc_respaldo
		WHERE tipo_respaldo = C_TIPO_RESPALDO_FECHA AND FECHA_RESTAURACION IS NULL;

	-------------------------------------------------------------------
	--	PROCEDIMIENTOS Y FUNCIONES DE USO GENERAL
	-------------------------------------------------------------------
	
	---------
	--	Devuelve las columnas de una tabla separadas por comas, anteponiendo en cada una el alias de
	--	tabla especificado
	--	
	--	params:
	--		p_id_tabla NUMBER: Identificador de la tabla cuyas columnas se desea encontrar
	--		p_tbl_alias VARCHAR2: Alias de tabla para anteponer en cada nombre de columna encontrado
	--	return VARCHAR2
	--		Concatenación de nombres de columnas con alias de tabla, separadas por comas 
	--------
	FUNCTION get_columns (p_id_tabla IN NUMBER, p_tbl_alias IN VARCHAR2) RETURN VARCHAR2 IS
		column_names VARCHAR2(2000) := NULL;
        CURSOR cur_column_names IS
            SELECT DISTINCT allt.column_id, allt.column_name
            FROM all_tab_columns allt 
				INNER JOIN sc_cat_tabla_respaldo ctr
					ON ctr.nom_tbl_orig = allt.table_name
            WHERE ctr.id_tabla = p_id_tabla
            ORDER BY allt.column_id ASC NULLS LAST;
    BEGIN
        FOR rec_column_names IN cur_column_names LOOP
            column_names := column_names || p_tbl_alias || '.' || rec_column_names.column_name || ',';
        END LOOP;
        column_names := SUBSTR(column_names, 1, INSTR(column_names, ',', LENGTH(column_names) - LENGTH(',')) - 1);
        RETURN column_names;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RETURN NULL;
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

    --------
	--	Procedimiento para hacer la restauración de la tabla histórica a la tabla original
	--	
	--	params
	--		p_id_respaldo: Identificador del respaldo a restaurar
	--		table_to_backup:: Registro que representa la tabla a restaurar
	--		tabla_reversada: Indica si se hizo un respaldo o no (en caso de encotrar o no registros)
	--------
	PROCEDURE rev_respld_tabla (
		p_id_respaldo IN NUMBER,
		table_to_backup IN sica_admin.sc_cat_tabla_respaldo%ROWTYPE,
		tabla_reversada OUT BOOLEAN
	) IS 
		cur cur_type_ref := NULL;
		inserted_rows INTEGER := 0;
		deleted_rows INTEGER := 0;
		sql_from_where VARCHAR2(2000) := NULL;
		sql_sel_rows VARCHAR2(2000) := NULL; 
		sql_count VARCHAR(2000) := NULL;
		count_rows_to_backup INTEGER := 0;
		hist_rows_before_backup INTEGER := 0;
		hist_rows_after_backup INTEGER := 0;
		orig_rows_before_backup INTEGER := 0;
		orig_rows_after_backup INTEGER := 0;
		procesed_rows INTEGER := 0;
		rows_by_log INTEGER := 0;
	BEGIN
		-- Construye el select 
		sql_from_where :=
			C_SQL_SEL_FROM 
				|| ' ' || ' INNER JOIN ' || table_to_backup.nom_tbl_hist || ' ' || C_GENERIC_ALIAS 
					|| ' ON ' || C_GENERIC_ALIAS || '.' || 'ID_RESPALDO_TABLA = rt.ID_RESPALDO_TABLA'
			|| ' ' || C_SQL_SEL_WHERE;
		sql_sel_rows := 
			'SELECT DISTINCT ' || get_columns(table_to_backup.id_tabla, C_GENERIC_ALIAS)
				|| ' ' ||  sql_from_where;
		sql_count := 
			'SELECT COUNT(DISTINCT ' || C_GENERIC_ALIAS || '.' || table_to_backup.count_fields || ') ' || sql_from_where;
			
		-- Ejecuta la consulta de conteo y la de selección de registros
		EXECUTE IMMEDIATE sql_count INTO count_rows_to_backup USING p_id_respaldo, table_to_backup.id_tabla;
		
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Inicia restauración de respaldo de: ' || table_to_backup.nom_tbl_hist);
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Registros a restaurar: ' || count_rows_to_backup);
        
        IF count_rows_to_backup = 0 THEN
			log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'No hay registros para restaurar');
			tabla_reversada := FALSE;
			RETURN;
		END IF;
		
		-- Cuenta registros totales en la tabla histórica y en la de origen antes del respaldo
		EXECUTE IMMEDIATE 'SELECT COUNT(' || table_to_backup.count_fields || ') FROM ' || table_to_backup.nom_tbl_hist 
		INTO hist_rows_before_backup;
		EXECUTE IMMEDIATE 'SELECT COUNT(' || table_to_backup.count_fields || ') FROM ' || table_to_backup.nom_tbl_orig 
		INTO orig_rows_before_backup;
		
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Registros totales en tabla histórica antes de la restauración: ' || hist_rows_before_backup);
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Registros totales en tabla origen antes de la restauración: ' || orig_rows_before_backup);
		
		-- Realiza la inserción a la tabla original
		EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || table_to_backup.nom_tbl_orig || ' DISABLE ALL TRIGGERS';
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Inhabilita triggers');
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Comienza inserción desde tabla histórica a tabla origen');
		EXECUTE IMMEDIATE 'INSERT INTO ' || table_to_backup.nom_tbl_orig || ' ' || sql_sel_rows
			USING p_id_respaldo, table_to_backup.id_tabla;
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Inserción finalizada');
		log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Ejecutando commit');
		COMMIT;
		log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Commit finalizado');
		EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || table_to_backup.nom_tbl_orig || ' ENABLE ALL TRIGGERS';
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Habilita triggers');
		
		-- Realiza la eliminación de registros de la tabla histórica
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Comienza eliminación de registros de tabla histórica');
		EXECUTE IMMEDIATE 'DELETE FROM ' || table_to_backup.nom_tbl_hist 
			|| ' WHERE ' || table_to_backup.count_fields || ' IN (' 
				|| 'SELECT tblh.' || table_to_backup.count_fields 
				|| ' FROM '|| table_to_backup.nom_tbl_hist || ' tblh '
					|| ' INNER JOIN ' || C_SC_RESPALDO_TABLA || ' rt '
						|| ' ON ' || ' tblh. ' || C_REF_COL_RESPLD_TBL || ' = rt.' || C_REF_COL_RESPLD_TBL
				|| ' WHERE rt.id_respaldo = :idrespld AND rt.id_tabla = :idtabla' 
			|| ')' USING p_id_respaldo, table_to_backup.id_tabla;
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Eliminación finalizada');
		log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Ejecutando commit');
		COMMIT;
		log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Commit finalizado');
		
		-- Cuenta registros totales en la tabla histórica y en la de origen despues del respaldo
		EXECUTE IMMEDIATE 'SELECT COUNT(' || table_to_backup.count_fields || ') FROM ' || table_to_backup.nom_tbl_hist 
		INTO hist_rows_after_backup;
		EXECUTE IMMEDIATE 'SELECT COUNT(' || table_to_backup.count_fields || ') FROM ' || table_to_backup.nom_tbl_orig 
		INTO orig_rows_after_backup;
		
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Registros totales en tabla histórica despues de la restauración: ' || hist_rows_after_backup);
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Registros totales en tabla origen despues de la restauración: ' || orig_rows_after_backup);
		
		inserted_rows := orig_rows_after_backup - orig_rows_before_backup;
		deleted_rows := hist_rows_before_backup - hist_rows_after_backup;
		
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Registros obtenidos: ' || count_rows_to_backup);
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Registros eliminados: ' || deleted_rows);
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Registros insertados: ' || inserted_rows);
		
		IF count_rows_to_backup <> deleted_rows AND deleted_rows <> inserted_rows THEN
			log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_ERROR, 'Error al restaurar registros de la tabla: Cifras de control inconsistentes');
			RAISE chk_total_after_backup_ex;
		END IF;
		tabla_reversada := TRUE;
	EXCEPTION
		WHEN OTHERS THEN
			EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || table_to_backup.nom_tbl_orig || ' ENABLE ALL TRIGGERS';
			log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Triggers habilitados');
			log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_ERROR, 'Procedimiento REV_RESPLD_TABLA: Error al restaurar respaldo la tabla: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	-------------------------------------------------------------------
	--	PROCEDIMIENTO PRINCIPAL
	-------------------------------------------------------------------
	
	--------
	--	Restaura todos los registros de todas las tablas del tipo de respaldo por fecha (TIPO_TABLA = 3), dado el identificador del respaldo
	--
	--	params
	--		p_id_respaldo: Identificador del respaldo a restaurar
	--------
	PROCEDURE reversa_todo(p_id_respaldo IN NUMBER) IS
		tablas_respaldadas INTEGER := 0;
		tabla_reversada BOOLEAN := FALSE;
		CURSOR tbls_to_backup IS
			SELECT *
			FROM sica_admin.sc_cat_tabla_respaldo
			WHERE tipo_tabla = C_TIPO_TBL_REL_NUL_DEAL
			ORDER BY orden_supr DESC NULLS LAST;
	BEGIN
		DBMS_OUTPUT.PUT_LINE('Comienza ejecución de restauración de respaldo por fecha');
		log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Comienza restauración de respaldo por Fecha, id_respaldo=' || p_id_respaldo);
		FOR rec_tbls_to_backup IN tbls_to_backup LOOP
			rev_respld_tabla(p_id_respaldo, rec_tbls_to_backup, tabla_reversada);
			IF tabla_reversada = TRUE THEN
				tablas_respaldadas := tablas_respaldadas + 1;
			END IF;
		END LOOP;
		log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Finaliza restauración de id_respaldo: ' || p_id_respaldo);
		log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Tablas históricas restauradas: ' || tablas_respaldadas);
		
		-- DELETE FROM sica_admin.sc_respaldo_tabla WHERE id_respaldo = p_id_respaldo;
		UPDATE sica_admin.sc_respaldo SET fecha_restauracion = SYSTIMESTAMP WHERE id_respaldo = p_id_respaldo;
		log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Establecida fecha de restauración');
		COMMIT;
		
		log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Finaliza restauración de respaldo por fecha');
		DBMS_OUTPUT.PUT_LINE('Finaliza ejecución de restauración de respaldo por fecha');
	EXCEPTION
		WHEN OTHERS THEN
			log_respld(p_id_respaldo, NULL, C_TIPO_MSG_ERROR, 'Error al reversar registros de tablas históricas: ' || SQLCODE || ' - ' || SQLERRM);
			log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Ejecutando rollback');
			ROLLBACK;
			log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Rollback finalizado');
			DBMS_OUTPUT.PUT_LINE('Finaliza ejecución de restauración de respaldo por fecha');
			-- RAISE;
	END;
		
BEGIN
	-- Itera todos los respaldos existentes y reversa cada uno
	--FOR rec_all_respld IN cur_all_resplds LOOP
	--	reversa_todo(rec_all_respld.id_respaldo);
	--END LOOP;
	
	-- Recuperación selectiva de respaldos de tablas independientes, llamar al procedimiento de recuperación por cada respaldo
	reversa_todo(1);
	reversa_todo(2);
	reversa_todo(3);
	-- reversa_todo(n);
END;

/
