-- Agrega una columna que identifica el origen de cualquier insert que se haga en esta tabla, se esperan dos origenes
-- ORIGEN = 'R' : Si viene desde el RMDS
-- ORIGEN = NULL : Si viene desde el Excel de Reuters

ALTER TABLE SICA_ADMIN.SICA_VARIACION ADD (
	ORIGEN VARCHAR2(1) NULL
);