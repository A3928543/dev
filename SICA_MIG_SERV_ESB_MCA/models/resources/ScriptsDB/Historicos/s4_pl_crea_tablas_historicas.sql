-- ###################################################################################################################################
-- # 
-- # Crea las tablas históricas necesarias, definidas en la tabla SC_CAT_TABLA_RESPALDO
-- #
-- ###################################################################################################################################

set serveroutput on size 1000000

DECLARE
--     
--     * Constantes auxiliares para la creación de las tablas históricas
--     
	C_TBL_RESPALDO_TABLA 	CONSTANT VARCHAR2(17) 	:= 'SC_RESPALDO_TABLA';
	C_COL_ID_RESP_TBL 		CONSTANT VARCHAR2(17) 	:= 'ID_RESPALDO_TABLA';
	C_SCHEMA				CONSTANT VARCHAR2(10)	:= 'SICA_ADMIN';
	C_ROL					CONSTANT VARCHAR2(8)	:= 'ROL_SICA';
	C_TABLE_SPACE			CONSTANT VARCHAR2(10)	:= 'SICA_DAT';
	C_TABLE_SPACE_INX		CONSTANT VARCHAR2(10)	:= 'SICA_INX';
    C_FK_NAME				CONSTANT VARCHAR2(19)	:= 'FK_T_HIST_T_RESPLD_';
	C_PK_NAME				CONSTANT VARCHAR2(19)	:= 'PK_T_HIST_';
	
	C_RENAMED_TBL_PREFIX	CONSTANT VARCHAR2(10)	:= 'B';
	
--    
--     * Variables auxiliares para la creación de las tablas históricas
--    
	count_tables            INTEGER                 := 0;
	fk_name					VARCHAR2(50)			:= NULL;
	pk_name					VARCHAR2(50)			:= NULL;
	table_name_fk_found		VARCHAR2(50)			:= NULL;
	table_name_pk_found		VARCHAR2(50)			:= NULL;
	
	created_tables			INTEGER		:= 0;
	created_synonyms		INTEGER		:= 0;
	renamed_tables			INTEGER		:= 0;
	
--   
--     * Cursor que contiene las siguientes columnas
--     *         - nom_tbl_orig: Nombre de la tabla a respaldar
--     *        - nom_tbl_hist: Nombre de la tabla histórica correspondiente
--     *        - table_name: Contiene el nombre de la tabla histórica si es que esta existe en la BD
--     *        - synonym_name: Contiene el sinónimo de la tabla histórica si es que este existe
--
	CURSOR cur_tbls_respaldo IS
		SELECT 
			ctr.nom_tbl_orig, 
			ctr.nom_tbl_hist,
			allt.table_name,
			allsyn.synonym_name
		FROM 
			sica_admin.sc_cat_tabla_respaldo ctr 
				LEFT OUTER JOIN all_tables allt
					ON ctr.nom_tbl_hist = allt.table_name
				LEFT OUTER JOIN all_synonyms allsyn
					ON ctr.nom_tbl_hist = allsyn.table_name 
						AND ctr.nom_tbl_hist = allsyn.synonym_name;
	
-- 
--     * Función para obtener el nombre de la tabla a la cual pertenece un constraint
--
    FUNCTION get_constraint_table_name(in_constraint_name IN VARCHAR2) RETURN VARCHAR2 IS 
        constraint_table_name VARCHAR(50);
        get_constrt_tbl_name_ex EXCEPTION;
    BEGIN
        SELECT table_name INTO constraint_table_name 
		FROM all_constraints 
		WHERE constraint_name = in_constraint_name;
        RETURN constraint_table_name;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RETURN NULL;
        WHEN OTHERS THEN
            RAISE get_constrt_tbl_name_ex;
    END;
	
--
--     * Función para obtener el los campos de un PK (separados por comas) para una tabla específica
--
	FUNCTION get_pk_fields_for_table(in_table_name IN VARCHAR2) RETURN VARCHAR2 IS
        out_pk_fields VARCHAR2(100);
		get_pk_flds_tbl_ex EXCEPTION;
    CURSOR cur_pk_column_names IS
        SELECT DISTINCT allcc.column_name
		FROM all_constraints allc INNER JOIN all_cons_columns allcc 
			ON allc.table_name = allcc.table_name 
				AND allc.constraint_name = allcc.constraint_name 
				AND allc.owner = allcc.owner 
		WHERE allc.table_name = in_table_name and allc.constraint_type = 'P';
	BEGIN
		FOR pk_column_name IN cur_pk_column_names LOOP
            out_pk_fields := out_pk_fields || pk_column_name.column_name || ', ';
        END LOOP;
        out_pk_fields := SUBSTR(out_pk_fields, 1, INSTR(out_pk_fields, ', ', LENGTH(out_pk_fields) - LENGTH(', ')) - 1);
		RETURN out_pk_fields;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
            RETURN NULL;
        WHEN OTHERS THEN
            RAISE get_pk_flds_tbl_ex;
	END;
			
BEGIN

	FOR rec_tbls_respaldo IN cur_tbls_respaldo LOOP
		DBMS_OUTPUT.PUT_LINE('### Tabla: ' || rec_tbls_respaldo.nom_tbl_hist);
		
		-- Se inactiva validación de SC_ACTIVIDAD, la tabla histórica correspondiente se crea como todas las demas:
		-- es decir, se renombra la tabla historica existente anteponiendo una B, y se crea una nueva con todos los campos de la original + 1
		-- que es ID_RESPALDO_TABLA
		
		-- IF rec_tbls_respaldo.nom_tbl_orig <> 'SC_ACTIVIDAD' THEN
			IF rec_tbls_respaldo.synonym_name IS NOT NULL THEN
				EXECUTE IMMEDIATE 'DROP PUBLIC SYNONYM ' || rec_tbls_respaldo.synonym_name;
				DBMS_OUTPUT.PUT_LINE('===> Sinónimo eliminado: ' || rec_tbls_respaldo.synonym_name);
			END IF;
		
			IF rec_tbls_respaldo.table_name IS NOT NULL THEN
				EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || rec_tbls_respaldo.table_name 
					|| ' RENAME TO ' || C_RENAMED_TBL_PREFIX || rec_tbls_respaldo.table_name;
				renamed_tables := renamed_tables + 1;
				-- EXECUTE IMMEDIATE 'DROP TABLE ' || C_SCHEMA || '.' || rec_tbls_respaldo.table_name;
				DBMS_OUTPUT.PUT_LINE('===> Tabla renombrada: ' || C_RENAMED_TBL_PREFIX || rec_tbls_respaldo.table_name);
			END IF;
		
			EXECUTE IMMEDIATE 'CREATE TABLE ' || C_SCHEMA || '.' || rec_tbls_respaldo.nom_tbl_hist 
            || ' AS (SELECT * FROM ' || C_SCHEMA || '.' || rec_tbls_respaldo.nom_tbl_orig || ' WHERE 1=2) '; 
			DBMS_OUTPUT.PUT_LINE('===> Tabla creada');
			created_tables := created_tables + 1;
			
			EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || rec_tbls_respaldo.nom_tbl_hist
				|| ' ADD ' || C_COL_ID_RESP_TBL || ' NUMBER';
			DBMS_OUTPUT.PUT_LINE('===> Columna de referencia a respaldo agregada');
			
			fk_name := C_FK_NAME || count_tables;
			table_name_fk_found := get_constraint_table_name(fk_name);
			IF table_name_fk_found IS NOT NULL THEN
				DBMS_OUTPUT.PUT_LINE('===> Proceso interrumpido - Constraint FK encontrado: ' || fk_name || ', de la tabla: ' 
					|| table_name_fk_found || '. Elimine las tablas históricas previamente creadas');
				RETURN;
			END IF;
			
			EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || rec_tbls_respaldo.nom_tbl_hist
				|| ' ADD CONSTRAINT ' || fk_name || ' FOREIGN KEY (' || C_COL_ID_RESP_TBL || ') '
				|| 'REFERENCES ' || C_SCHEMA || '.' || C_TBL_RESPALDO_TABLA || ' (' || C_COL_ID_RESP_TBL || ')';
			DBMS_OUTPUT.PUT_LINE('===> Constraint de referencia a respaldo agregado: ' || fk_name);
			
			pk_name := C_PK_NAME || count_tables;
			table_name_pk_found := get_constraint_table_name(pk_name);
			IF table_name_pk_found IS NOT NULL THEN
				DBMS_OUTPUT.PUT_LINE('===> Proceso interrumpido - Constraint PK encontrado: ' || pk_name || ', de la tabla: ' 
					|| table_name_pk_found || '. Elimine las tablas históricas previamente creadas');
			END IF;
			
			EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || rec_tbls_respaldo.nom_tbl_hist
				|| ' ADD CONSTRAINT ' || pk_name || ' PRIMARY KEY (' || get_pk_fields_for_table(rec_tbls_respaldo.nom_tbl_orig) || ') '
				|| ' USING INDEX TABLESPACE ' || C_TABLE_SPACE_INX;
			DBMS_OUTPUT.PUT_LINE('===> Llave primaria creada: ' || pk_name);
			
			EXECUTE IMMEDIATE 'CREATE PUBLIC SYNONYM ' || rec_tbls_respaldo.nom_tbl_hist || ' FOR ' || C_SCHEMA 
				|| '.' || rec_tbls_respaldo.nom_tbl_hist;
			DBMS_OUTPUT.PUT_LINE('===> Sinónimo de tabla creado');
			created_synonyms := created_synonyms + 1;
			
			DBMS_OUTPUT.PUT_LINE('===> Permisos otorgados');
            count_tables := count_tables + 1;
			
		-- Se inactiva validación de SC_ACTIVIDAD, la tabla histórica correspondiente se crea como todas las demas
		-- ELSE
			--EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || 'SC_H_ACTIVIDAD '
            --    || ' ADD ' || C_COL_ID_RESP_TBL || ' NUMBER';
            --DBMS_OUTPUT.PUT_LINE('===> Columna de referencia a respaldo agregada para la tabla SC_H_ACTIVIDAD');
			
			--EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || 'SC_H_ACTIVIDAD '
            --    || ' ADD CONSTRAINT FK_H_ACTIVIDAD_RESPLD_TABLA FOREIGN KEY (' || C_COL_ID_RESP_TBL || ') '
            --    || 'REFERENCES ' || C_SCHEMA || '.' || C_TBL_RESPALDO_TABLA || ' (' || C_COL_ID_RESP_TBL || ')';
            --DBMS_OUTPUT.PUT_LINE('===> Constraint de referencia a respaldo agregado para SC_H_ACTIVIDAD');
			
			--EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || 'SC_H_ACTIVIDAD '
            --    || ' MODIFY ( RESULTADO VARCHAR2(512) )';
            --DBMS_OUTPUT.PUT_LINE('===> Columna RESULTADO redimensionada en SC_H_ACTIVIDAD');
		--END IF;
	END LOOP;
    DBMS_OUTPUT.PUT_LINE('======> Tablas creadas: ' || created_tables);
    DBMS_OUTPUT.PUT_LINE('======> Sinónimos creados: ' || created_synonyms);	
	DBMS_OUTPUT.PUT_LINE('======> Tablas renombradas: ' || renamed_tables);	

EXCEPTION
	WHEN OTHERS THEN
        IF cur_tbls_respaldo%ISOPEN THEN
		    CLOSE cur_tbls_respaldo;
	    END IF;
		DBMS_OUTPUT.PUT_LINE('Error al crear tablas históricas: ' || SQLCODE || ' - ' || SQLERRM);
        DBMS_OUTPUT.PUT_LINE('======> Tablas creadas: ' || created_tables);
        DBMS_OUTPUT.PUT_LINE('======> Sinónimos creados: ' || created_synonyms);
		DBMS_OUTPUT.PUT_LINE('======> Tablas renombradas: ' || renamed_tables);	
END;

/