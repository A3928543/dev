--  ######################################################################################################################################## 
--  # 
--  #     Procedimiento de resverso para el script de creaci�n / modificaci�n de tablas hist�ricas 
--  # 
--  ######################################################################################################################################## 

set serveroutput on size 1000000

DECLARE
	
	C_SCHEMA				CONSTANT VARCHAR2(10)	:= 'SICA_ADMIN';

	C_RENAMED_TBL_PREFIX	CONSTANT VARCHAR2(10)	:= 'B';
	
--    
--     * Variables auxiliares para la creaci�n de las tablas hist�ricas
--    /
	deleted_tables          INTEGER         := 0;
	deleted_synonyms		INTEGER			:= 0;		
	renamed_tables			INTEGER			:= 0;
	
--    
--     * Cursor que contiene las siguientes columnas
--     *         - nom_tbl_orig: Nombre de la tabla a respaldar
--     *        - nom_tbl_hist: Nombre de la tabla hist�rica correspondiente
--     *        - table_name: Contiene el nombre de la tabla hist�rica si es que esta existe en la BD
--     *        - synonym_name: Contiene el sin�nimo de la tabla hist�rica si es que este existe
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
 --       Funci�n que determina si una tabla existe
 --
	FUNCTION  existe_tabla (nombre_tabla IN VARCHAR2) RETURN BOOLEAN IS
		exists_renamed_table BOOLEAN := FALSE;
		table_count INTEGER := 0;
	BEGIN
		SELECT COUNT(*) INTO table_count
		FROM all_tables 
		WHERE table_name = nombre_tabla;
		IF table_count = 1 THEN 
			exists_renamed_table := TRUE;
		END IF;
		RETURN exists_renamed_table;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			RETURN FALSE;
	END;
	
	
	
BEGIN
--
--        Si existen los sinonimos y las tablas historicas, estos se eliminan
--        Si existe una tabla hist�rica renombrada como BK_NOMBRE_TABLA_HIST, esta se vuelve a renombrar, suprimiendo el prefijo BK
-- 
	FOR rec_tbls_respaldo IN cur_tbls_respaldo LOOP
		DBMS_OUTPUT.PUT_LINE('### Tabla: ' || rec_tbls_respaldo.nom_tbl_hist);
		
		-- Se inactiva la validaci�n para SC_ACTIVIDAD, la tabla hist�rica correspondiente se restaura como todas las dem�s
		-- Es decir se elimina la tabla hist�rica creada y se recupera la tabla renombrada durante el proceso de creaci�n
		-- quitando el prefijo B
		--IF rec_tbls_respaldo.nom_tbl_orig <> 'SC_ACTIVIDAD' THEN
			IF rec_tbls_respaldo.synonym_name IS NOT NULL THEN
				EXECUTE IMMEDIATE 'DROP PUBLIC SYNONYM ' || rec_tbls_respaldo.synonym_name;
				DBMS_OUTPUT.PUT_LINE('===> Sin�nimo eliminado: ' || rec_tbls_respaldo.synonym_name);
				deleted_synonyms := deleted_synonyms + 1;
			END IF;
		
			IF rec_tbls_respaldo.table_name IS NOT NULL THEN
				EXECUTE IMMEDIATE 'DROP TABLE ' || C_SCHEMA || '.' || rec_tbls_respaldo.table_name;
				DBMS_OUTPUT.PUT_LINE('===> Tabla eliminada: ' || rec_tbls_respaldo.table_name);
				deleted_tables := deleted_tables + 1;
			END IF;
			
			IF existe_tabla(C_RENAMED_TBL_PREFIX || rec_tbls_respaldo.table_name) = TRUE THEN
				EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || C_RENAMED_TBL_PREFIX || rec_tbls_respaldo.table_name 
					|| ' RENAME TO ' || rec_tbls_respaldo.table_name;
				renamed_tables := renamed_tables + 1;
				DBMS_OUTPUT.PUT_LINE('===> Tabla recuperada: ' || rec_tbls_respaldo.table_name);
			END IF;
		
		-- Se inactiva la validaci�n para SC_ACTIVIDAD, la tabla hist�rica correspondiente se restaura como todas las dem�s
		--ELSE
			--EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || 'SC_H_ACTIVIDAD '
            --    || ' DROP CONSTRAINT FK_H_ACTIVIDAD_RESPLD_TABLA';
            --DBMS_OUTPUT.PUT_LINE('===> Constraint de referencia a respaldo eliminado para SC_H_ACTIVIDAD');
			
			--EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || 'SC_H_ACTIVIDAD '
            --    || ' DROP COLUMN ID_RESPALDO_TABLA';
            --DBMS_OUTPUT.PUT_LINE('===> Columna de referencia a respaldo eliminada de SC_H_ACTIVIDAD');
			
			--EXECUTE IMMEDIATE 'ALTER TABLE ' || C_SCHEMA || '.' || 'SC_H_ACTIVIDAD '
            --    || ' MODIFY ( RESULTADO VARCHAR2(30) )';
            --DBMS_OUTPUT.PUT_LINE('===> Columna RESULTADO redimensionada en SC_H_ACTIVIDAD');
		--END IF;
	END LOOP;
	
	DBMS_OUTPUT.PUT_LINE('===> Tablas eliminadas: ' || deleted_tables);
	DBMS_OUTPUT.PUT_LINE('===> Sin�nimos eliminados: ' || deleted_synonyms);
	DBMS_OUTPUT.PUT_LINE('===> Tablas renombradas: ' || renamed_tables);
	
EXCEPTION
	WHEN OTHERS THEN
        IF cur_tbls_respaldo%ISOPEN THEN
		    CLOSE cur_tbls_respaldo;
	    END IF;
		DBMS_OUTPUT.PUT_LINE('Error al eliminar tablas hist�ricas: ' || SQLCODE || ' - ' || SQLERRM);
END;

/
