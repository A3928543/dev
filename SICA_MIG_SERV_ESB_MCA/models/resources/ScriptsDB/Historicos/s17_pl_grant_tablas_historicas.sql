-- ###################################################################################################################################
-- # 
-- # Asigna grants para las tablas históricas
-- #
-- ###################################################################################################################################

set serveroutput on size 1000000

DECLARE
--     
--     * Constantes auxiliares para la creación de las tablas históricas
--     
	C_TBL_RESPALDO_TABLA 	CONSTANT VARCHAR2(17) 	:= 'SC_RESPALDO_TABLA';
	C_SCHEMA				CONSTANT VARCHAR2(10)	:= 'SICA_ADMIN';
	C_ROL					CONSTANT VARCHAR2(8)	:= 'ROL_SICA';
	C_TABLE_SPACE			CONSTANT VARCHAR2(10)	:= 'SICA_DAT';
	C_TABLE_SPACE_INX		CONSTANT VARCHAR2(10)	:= 'SICA_INX';
    	
--   
--     * Cursor que contiene las siguientes columnas
--     *        - nom_tbl_orig: Nombre de la tabla a respaldar
--     *        - nom_tbl_hist: Nombre de la tabla histórica correspondiente
--
	CURSOR cur_tbls_respaldo IS
		SELECT 
			ctr.nom_tbl_orig, 
			ctr.nom_tbl_hist
		FROM 
			sica_admin.sc_cat_tabla_respaldo ctr; 

BEGIN
	DBMS_OUTPUT.PUT_LINE('======> Comienza asignación de grants de tablas historicas...');	
	FOR rec_tbls_respaldo IN cur_tbls_respaldo LOOP
		EXECUTE IMMEDIATE 'GRANT SELECT, INSERT, UPDATE, DELETE ON '|| C_SCHEMA || '.' || rec_tbls_respaldo.nom_tbl_hist || ' TO ' || C_ROL;
		DBMS_OUTPUT.PUT_LINE('### Grant asignado a Tabla: ' || rec_tbls_respaldo.nom_tbl_hist);
	END LOOP;
	EXECUTE IMMEDIATE 'GRANT SELECT, INSERT, UPDATE, DELETE ON ' || C_SCHEMA || '.' || 'SC_CAT_TABLA_RESPALDO' || ' TO ' || C_ROL;
	EXECUTE IMMEDIATE 'GRANT SELECT, INSERT, UPDATE, DELETE ON ' || C_SCHEMA || '.' || 'SC_RESPALDO' || ' TO ' || C_ROL;
	EXECUTE IMMEDIATE 'GRANT SELECT, INSERT, UPDATE, DELETE ON ' || C_SCHEMA || '.' || 'SC_RESPALDO_TABLA' || ' TO ' || C_ROL;
	EXECUTE IMMEDIATE 'GRANT SELECT, INSERT, UPDATE, DELETE ON ' || C_SCHEMA || '.' || 'SC_RESPALDO_TABLA_LOG' || ' TO ' || C_ROL;
	DBMS_OUTPUT.PUT_LINE('======> Finaliza asignación de grants de tablas historicas...');	
EXCEPTION
	WHEN OTHERS THEN
		DBMS_OUTPUT.PUT_LINE('Error al asignar grants a tablas históricas: ' || SQLCODE || ' - ' || SQLERRM);
END;

/