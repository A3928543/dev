--  *=================================================================================================
--  * Inserta los registros de la antigua tabla histórica a la nueva 
--  *=================================================================================================

-- SC_H_ACTIVIDAD
INSERT INTO SICA_ADMIN.SC_H_ACTIVIDAD (
	ID_ACTIVIDAD, 
	NOMBRE_ACTIVIDAD, 
	FECHA_CREACION, 
	FECHA_TERMINACION, 
	ID_DEAL, 
	ID_DEAL_POSICION, 
	ID_USUARIO, 
	RESULTADO, 
	STATUS, 
	PROCESO, 
	VERSION
) SELECT * FROM SICA_ADMIN.BSC_H_ACTIVIDAD;