--######################################################################################################################################## 
--# 
--# 	Procedimiento para respaldar registros por fecha
--# 
--########################################################################################################################################

set serveroutput on size 1000000

DECLARE
	
	-----------------------------------------------------------------
	--	CONSTANTES
	-----------------------------------------------------------------

	-- Tipos de tablas a respaldar
	C_TIPO_TBL_REL_NUL_DEAL CONSTANT NUMBER := 3;		-- Sin relación con deal (respaldo por fecha)
	
	-- Identifica un tipo de respaldo por fecha
	C_TIPO_RESPALDO_FECHA CONSTANT INTEGER := 3;
	
	-- Formato de fecha
	C_DATE_FORMAT 		CONSTANT VARCHAR2(21)	:= 'DD/MM/YYYY HH24:MI:SS';
	
	-- Valores inicial y final para la sección del tiempo en el filtro por fecha
	C_TIME_PART_INI		CONSTANT VARCHAR2(10)	:= '00:00:00';
	C_TIME_PART_END		CONSTANT VARCHAR2(10)	:= '23:59:59';
	
	-- Define cuantas lineas de log se imprimen por respaldo de tabla, para mostrar progreso
	C_LOGS_X_TABLA		CONSTANT INTEGER := 20;
	
	-- Esquema
	C_SCHEMA				CONSTANT VARCHAR2(20) := 'SICA_ADMIN';
	
	-- Alias genérico para referirse a la tabla actual en proceso de respaldo
	C_GENERIC_ALIAS			CONSTANT VARCHAR2(10) := 'CURTBL';
	
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
	
	-----------------------------------------------------------------
	--	TIPOS
	-----------------------------------------------------------------
	
	-- Cursor generico
	TYPE cur_type_ref IS REF CURSOR;
	
	-----------------------------------------------------------------
	--	EXCEPCIONES
	-----------------------------------------------------------------
	
	-- Ocurre cuando la validación de cifras de control es incorrecta
	chk_total_after_backup_ex EXCEPTION;
	
    -----------------------------------------------------------------
	--	FUNCIONES Y PROCEDIMIENTOS DE USO GENERAL
	-----------------------------------------------------------------
    
    ------------
    --  Genera el siguiente valor de la llave primaria de la tabla SC_RESPALDO 
	--	
	--	return NUMBER
	--		Siguiente valor de la llave primaria de la tabla SC_RESPALDO
	------------
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
	
	-----------
	--	Inserta un nuevo registro en SC_RESPALDO_TABLA para la tabla y el id_respaldo especificados
	--	
	--	params
	--		p_id_tabla VARCHAR2: Id de la tabla a respaldar
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
	
	------------
	--	Valida las cifras de control obtenidas al principio y al final de un proceso de respaldo
	--	
	--	params:
	--		rec_sc_respaldo_tabla: Registro que contiene los las cifras de control a validar
	--	return:
	--		BOOLEAN: TRUE si las cifras son válidad, de lo contrario FALSE
	-----------
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

    
	---------
	--	Copia los registros de la tabla original a la tabla histórica
	--	
	--	params
	--		table_to_backup: Registro que representa la tabla a respaldar
	--		ini_date: Filtro de fecha de inicio del respaldo
	--		end_date: Filtro de fecha final del respaldo
	--		p_id_respaldo: Identificador del respaldo
	---------
	PROCEDURE insert_to_hist(
		table_to_backup IN sica_admin.sc_cat_tabla_respaldo%ROWTYPE, 
		ini_date IN VARCHAR2, 
		end_date IN VARCHAR2,
		p_id_respaldo IN NUMBER
	) IS
		sql_count_sentence VARCHAR2(3000) := NULL;
		sql_rows_sentence VARCHAR2(3000) := NULL;
		sql_with_part VARCHAR(2000) := NULL;
		sql_join_part VARCHAR(2000) := NULL;
		sql_where_part VARCHAR(2000) := NULL;
		sql_from_where_part VARCHAR(2000) := NULL;
		sql_insert_sentence VARCHAR(3000) := NULL;
		inserted_rows NUMBER := 0;
		l_id_respaldo_tabla NUMBER := 0;
		r_row_count INTEGER := 0;
		sql_from_part VARCHAR(1000) := NULL;
	BEGIN
		-- count_and_select(table_to_backup, ini_date, end_date, selected_rows, cur);
		sql_from_part := ' FROM ' || table_to_backup.nom_tbl_orig || ' ' || table_to_backup.alias_tbl_orig;
		
		IF table_to_backup.id_tabla = C_SC_MOVIMIENTO_CONT_DETALLE THEN
			sql_from_part := sql_from_part ||
				' INNER JOIN SC_MOVIMIENTO_CONTABLE MC
					ON ' || table_to_backup.join_on_tbl_orig;
            sql_where_part := ' WHERE MC.FECHA_CREACION '  
                || '>= TO_DATE(:inidate || ' || ''' ' || C_TIME_PART_INI || ''',''' || C_DATE_FORMAT || ''')'
                || ' AND MC.FECHA_CREACION '
                || '<= TO_DATE(:enddate || ' || ''' ' || C_TIME_PART_END || ''',''' || C_DATE_FORMAT || ''')';
        ELSE
            sql_where_part := ' WHERE ' || table_to_backup.alias_tbl_orig || '.' || table_to_backup.date_field
                || '>= TO_DATE(:inidate || ' || ''' ' || C_TIME_PART_INI || ''',''' || C_DATE_FORMAT || ''')'
                || ' AND ' || table_to_backup.alias_tbl_orig || '.' || table_to_backup.date_field
                || '<= TO_DATE(:enddate || ' || ''' ' || C_TIME_PART_END || ''',''' || C_DATE_FORMAT || ''')';
		END IF;
  	
		sql_from_where_part := sql_from_part || sql_where_part;
			
		sql_count_sentence := 
			'SELECT COUNT(DISTINCT '|| table_to_backup.alias_tbl_orig || '.' || table_to_backup.count_fields || ') '
			|| sql_from_where_part;
		
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Comienza proceso de inserción en la tabla: ' || table_to_backup.nom_tbl_hist);
		
		-- Inserta un nuevo registro en SC_RESPALDO_TABLA para controlar el respaldo de la tabla
		l_id_respaldo_tabla := inserta_sc_respaldo_tabla (table_to_backup.id_tabla, p_id_respaldo);
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Registro creado en SC_RESPALDO_TABLA, id_respaldo_tabla=' || l_id_respaldo_tabla);
		
		sql_rows_sentence := 
			'SELECT DISTINCT ' || table_to_backup.alias_tbl_orig || '.*, ' || l_id_respaldo_tabla
			|| sql_from_where_part;
		
		sql_insert_sentence := 'INSERT INTO ' || table_to_backup.nom_tbl_hist || ' ' || sql_rows_sentence;
        
        --DBMS_OUTPUT.PUT_LINE('======> select conteo: ' || sql_count_sentence );
        --DBMS_OUTPUT.PUT_LINE('======> select registros: ' || sql_rows_sentence);
		
		EXECUTE IMMEDIATE sql_count_sentence INTO r_row_count USING ini_date, end_date;
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Registros a respaldar=' || r_row_count);
        
		-- Ejecuta el insert en la tabla histórica
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Insertando en tabla histórica');
		EXECUTE IMMEDIATE sql_insert_sentence USING ini_date, end_date;
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Inserción finalizada');
		
		-- Cuenta registros insertados en histórico
		EXECUTE IMMEDIATE 'SELECT COUNT(' || table_to_backup.count_fields || ') FROM ' || table_to_backup.nom_tbl_hist 
		|| ' WHERE id_respaldo_tabla = :idresptbl'
		INTO inserted_rows USING l_id_respaldo_tabla;
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Registros insertados en tabla histórica: ' || inserted_rows);
		
		-- Actualiza cifras de control
		UPDATE sica_admin.sc_respaldo_tabla 
		SET regs_seleccionados = r_row_count, regs_insertados = inserted_rows, last_modif_time = SYSTIMESTAMP
		WHERE id_respaldo_tabla = l_id_respaldo_tabla;
		log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_INFO, 'Cifras de control actualizadas');		
		log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Ejecutando commit');
		COMMIT;
		log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Commit finalizado');
	EXCEPTION
		WHEN OTHERS THEN
			log_respld(p_id_respaldo, table_to_backup.id_tabla, C_TIPO_MSG_ERROR, 'Procedimiento INSERT_TO_HIST: Error al insertar registros en tabla historica: ' || SQLCODE || ' - ' || SQLERRM);
			RAISE;
	END;
	
	---------
	--	Elimina los registros insertados en históricos de la tabla original
	--
	--  rec_tbls_to_delete: Registro que representa la tabla a respaldar
	--  p_id_respaldo: Identificador del respaldo
	---------
	PROCEDURE delete_from_orig(rec_tbls_to_delete IN sica_admin.sc_cat_tabla_respaldo%ROWTYPE, p_id_respaldo IN NUMBER) IS
		rows_before_delete NUMBER := 0;
		rows_after_delete NUMBER := 0;
		deleted_rows NUMBER := 0;
		rec_sc_respaldo_tabla sica_admin.sc_respaldo_tabla%ROWTYPE := NULL;
	BEGIN
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Comienza eliminación de registros de la tabla: ' || rec_tbls_to_delete.nom_tbl_orig);
	
		-- Cuenta registros en la tabla original antes de ser eliminados
		EXECUTE IMMEDIATE 'SELECT COUNT(' || rec_tbls_to_delete.count_fields || ') ' ||' FROM ' || rec_tbls_to_delete.nom_tbl_orig
			INTO rows_before_delete;
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Registros en tabla original antes de eliminar: ' || rows_before_delete);
	
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
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Eliminación de registros completa');
		EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || rec_tbls_to_delete.nom_tbl_orig || ' ENABLE ALL TRIGGERS';
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Triggers habilitados');
		
		-- Cuenta registros en la tabla original despues de ser eliminados
		EXECUTE IMMEDIATE 'SELECT COUNT(' || rec_tbls_to_delete.count_fields || ') ' ||' FROM ' || rec_tbls_to_delete.nom_tbl_orig
			INTO rows_after_delete;
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Registros en tabla origen después de eliminar: ' || rows_after_delete);
		
		deleted_rows := rows_before_delete - rows_after_delete;
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Registros eliminados: ' || deleted_rows);
		
		-- Actualiza cifras de control
		UPDATE sica_admin.sc_respaldo_tabla 
		SET regs_eliminados = deleted_rows, regs_total_antes = rows_before_delete, regs_total_despues = rows_after_delete, last_modif_time = SYSTIMESTAMP
		WHERE id_respaldo = p_id_respaldo ANd id_tabla = rec_tbls_to_delete.id_tabla;
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Cifras de control actualizadas');
		
		log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Ejecutando commit');
		COMMIT;
		log_respld(p_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Commit finalizado');
		
		SELECT * INTO rec_sc_respaldo_tabla 
		FROM sc_respaldo_tabla 
		WHERE id_respaldo = p_id_respaldo AND id_tabla = rec_tbls_to_delete.id_tabla;
		IF valida_cifras_control(rec_sc_respaldo_tabla) = FALSE THEN
			log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_ERROR, 'Error al eliminar registros de tabla: Cifras de control inconsistentes');
			RAISE chk_total_after_backup_ex;
		END IF;
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Cifras de control validadas correctamente');
		log_respld(p_id_respaldo, rec_tbls_to_delete.id_tabla, C_TIPO_MSG_INFO, 'Finaliza eliminación de registros de la tabla original');
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
	 
	-------
	--	Realiza el respaldo de todas las tablas con relacion hacia SC_DEAL
	--	
	--	params
	--		ini_date VARCHAR2: Fecha inicial en formato 'DD/MM/YYYY'
	--		end_date VARCHAR2: Fecha final en formato 'DD/MM/YYYY'
	-------
	 PROCEDURE respalda_todo (ini_date IN VARCHAR2, end_date IN VARCHAR2) IS
		g_id_respaldo NUMBER := 0;
		TYPE cat_tbl_respld_type IS TABLE OF sica_admin.sc_cat_tabla_respaldo%ROWTYPE INDEX BY BINARY_INTEGER;
		cat_tbl_respld_array cat_tbl_respld_type;
		tablas_procesadas INTEGER := 0;
	 BEGIN
		DBMS_OUTPUT.PUT_LINE('Comienza ejecución de respaldo por fecha');
		SELECT *
		BULK COLLECT INTO cat_tbl_respld_array
		FROM sica_admin.sc_cat_tabla_respaldo
		WHERE tipo_tabla = C_TIPO_TBL_REL_NUL_DEAL
		ORDER BY orden_supr ASC NULLS LAST;
	
		-- Genera Id de respaldo e inserta un registro en la tabla de control SC_RESPALDO
		g_id_respaldo := get_respaldo_next_pk_val();
		INSERT INTO SICA_ADMIN.SC_RESPALDO (
			ID_RESPALDO, FECHA_CREACION, TIPO_RESPALDO, FILTRO_FECHA_INI, FILTRO_FECHA_FIN, FILTRO_ESTATUS, FECHA_RESTAURACION
		) VALUES (
			g_id_respaldo, SYSTIMESTAMP, C_TIPO_RESPALDO_FECHA, ini_date, end_date, NULL, NULL
		);
		COMMIT;
		log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Comienza respaldo por fecha Fecha inicial=' || ini_date || ', Fecha final=' || end_date);
		log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Registro de respaldo creado, id_respaldo=' || g_id_respaldo);
		
		-- Realiza todos los inserts
		log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Comienza proceso de inserción en tablas históricas');
		FOR indx IN cat_tbl_respld_array.FIRST .. cat_tbl_respld_array.LAST LOOP
			insert_to_hist(cat_tbl_respld_array(indx), ini_date, end_date, g_id_respaldo) ;
			tablas_procesadas := tablas_procesadas + 1;
		END LOOP;
		log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Finaliza proceso de inserción en tablas históricas, tablas respaldadas: ' || tablas_procesadas);
		
		tablas_procesadas := 0;
		-- Realiza todos los deletes
		log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Comienza proceso de eliminación de registros de tablas origen');
		FOR indx IN cat_tbl_respld_array.FIRST .. cat_tbl_respld_array.LAST LOOP
			delete_from_orig(cat_tbl_respld_array(indx), g_id_respaldo);
			tablas_procesadas := tablas_procesadas + 1;
		END LOOP;
		log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Finaliza proceso de eliminación de registros de tablas origen, tablas procesadas: ' || tablas_procesadas);
		log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Finaliza respaldo por fecha');
		DBMS_OUTPUT.PUT_LINE('Finaliza ejecución de respaldo por fecha');
	 EXCEPTION
		WHEN OTHERS THEN
			log_respld(g_id_respaldo, NULL, C_TIPO_MSG_ERROR, 'Procedimiento respalda_todo: Error al respaldar las tablas: ' || SQLCODE || ' - ' || SQLERRM);
			log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Ejecutando rollback');
			ROLLBACK;
			log_respld(g_id_respaldo, NULL, C_TIPO_MSG_INFO, 'Rollback finalizado');
			DBMS_OUTPUT.PUT_LINE('Finaliza ejecución de respaldo por fecha');
			--RAISE;
	 END;
	 	
BEGIN
	-- Llama al procedimiento principal de respaldo para que opere sobre los registros del 2007 al 2009
	-- Descomentar para producción
	-- respalda_todo('01/01/2007','31/12/2007');
	-- respalda_todo('01/01/2008','31/12/2008');
	respalda_todo('01/01/2009','31/12/2009');
    
	-- Llamada para pruebas locales, comentar para producción
	-- respalda_todo('01/01/2010','31/12/2010');
	-- NULL;
END;

/