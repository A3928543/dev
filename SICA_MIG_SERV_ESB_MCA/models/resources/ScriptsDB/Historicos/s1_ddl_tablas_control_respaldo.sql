set serveroutput on size 1000000

--  *=================================================================================================
--  *	SC_CAT_TABLA_RESPALDO: Cat�logo de tablas con hist�rico
--	*	
--	*	TIPO_TABLA: Indica el tipo de tabla, puede ser uno de tres valores
--	*		- 1 = Tablas con relaci�n f�sica (FK) directa o indirecta con SC_DEAL
--	*		- 2 = Tablas con relaci�n l�gica con SC_DEAL
--	*		- 3 = Tablas sin relaci�n alguna con SC_DEAL (respaldo por fecha)
--  *=================================================================================================
CREATE TABLE SICA_ADMIN.SC_CAT_TABLA_RESPALDO (
	ID_TABLA NUMBER NOT NULL,
	NOM_TBL_ORIG VARCHAR2(50),
	NOM_TBL_HIST VARCHAR2(50),
	TIPO_TABLA NUMBER, 
	ORDEN_SUPR NUMBER, 
	COUNT_FIELDS VARCHAR(100),
	ALIAS_TBL_ORIG VARCHAR(10),
	JOIN_ON_TBL_ORIG VARCHAR(500),
	DATE_FIELD VARCHAR(500)
) TABLESPACE SICA_DAT;

ALTER TABLE SICA_ADMIN.SC_CAT_TABLA_RESPALDO ADD CONSTRAINT PK_SC_CAT_TABLA_RESPALDO 
PRIMARY KEY (ID_TABLA) USING INDEX TABLESPACE SICA_INX;

DROP PUBLIC SYNONYM SC_CAT_TABLA_RESPALDO;
CREATE PUBLIC SYNONYM SC_CAT_TABLA_RESPALDO FOR SICA_ADMIN.SC_CAT_TABLA_RESPALDO;

--  *=================================================================================================
--  *	SC_RESPALDO: Informaci�n sobre los respaldos
--	*
--	*	TIPO_RESPALDO: Indica el tipo de respaldo almacenado
--	*		1 - Respaldo de Deals (con todas sus dependencias f�sicas y l�gicas)
--	*		2 - Respaldo de Swaps (con todas sus dependencias f�sicas y l�gicas)
--	*		3 - Respaldo de Tablas sin relaci�n con deals ni swaps (por fecha)
--	*	FECHA_RESTAURACION: Contiene la fecha en que fu� restaurado el respaldo hacia 
-- 	*		las tablas originales
--  *=================================================================================================
CREATE TABLE SICA_ADMIN.SC_RESPALDO (
	ID_RESPALDO NUMBER NOT NULL,
	FECHA_CREACION TIMESTAMP(6),
	TIPO_RESPALDO NUMBER,
	FILTRO_FECHA_INI VARCHAR2(10),
	FILTRO_FECHA_FIN VARCHAR2(10),
	FILTRO_ESTATUS VARCHAR2(2),
	FECHA_RESTAURACION TIMESTAMP(6)
) TABLESPACE SICA_DAT;

ALTER TABLE SICA_ADMIN.SC_RESPALDO ADD CONSTRAINT PK_SC_RESPALDO 
PRIMARY KEY (ID_RESPALDO) USING INDEX TABLESPACE SICA_INX;

DROP PUBLIC SYNONYM SC_RESPALDO;
CREATE PUBLIC SYNONYM SC_RESPALDO FOR SICA_ADMIN.SC_RESPALDO;

--  *=================================================================================================
--  * SC_RESPALDO_TABLA: Asociacion Respaldo - Tabla
--  *=================================================================================================
CREATE TABLE SICA_ADMIN.SC_RESPALDO_TABLA (
	ID_RESPALDO_TABLA NUMBER NOT NULL,
	ID_RESPALDO NUMBER,
	ID_TABLA NUMBER,
	REGS_SELECCIONADOS NUMBER,
	REGS_ELIMINADOS NUMBER,
	REGS_INSERTADOS NUMBER,
	REGS_TOTAL_ANTES NUMBER,
	REGS_TOTAL_DESPUES NUMBER,
	CREATION_TIME TIMESTAMP(6),
	LAST_MODIF_TIME TIMESTAMP(6)
) TABLESPACE SICA_DAT;

ALTER TABLE SICA_ADMIN.SC_RESPALDO_TABLA ADD CONSTRAINT PK_SC_RESPALDO_TABLA 
PRIMARY KEY (ID_RESPALDO_TABLA) USING INDEX TABLESPACE SICA_INX;

ALTER TABLE SICA_ADMIN.SC_RESPALDO_TABLA ADD CONSTRAINT FK_SC_RESPLD_TBL_SC_RESPLD
FOREIGN KEY (ID_RESPALDO) REFERENCES SICA_ADMIN.SC_RESPALDO(ID_RESPALDO);

ALTER TABLE SICA_ADMIN.SC_RESPALDO_TABLA ADD CONSTRAINT FK_SC_RES_TBL_SC_CAT_TBL_RES
FOREIGN KEY (ID_TABLA) REFERENCES SICA_ADMIN.SC_CAT_TABLA_RESPALDO(ID_TABLA);

DROP PUBLIC SYNONYM SC_RESPALDO_TABLA;
CREATE PUBLIC SYNONYM SC_RESPALDO_TABLA FOR SICA_ADMIN.SC_RESPALDO_TABLA;

--  *=================================================================================================
--  *	SC_RESPALDO_TABLA_LOG: Tabla para almacenar mensajes de log por respaldo y tabla
--  *
--  * 	TIPO_MENSAJE: Indica el tipo de mensaje almacenado, puede ser de dos tipos:
--  *		- 1 = Informacion
--  *		- 2 = Error
--  *=================================================================================================
CREATE TABLE SICA_ADMIN.SC_RESPALDO_TABLA_LOG (
	ID_RESPALDO_TABLA_LOG NUMBER NOT NULL,
	LOG_TIME TIMESTAMP(6),
	ID_RESPALDO NUMBER,
	ID_TABLA NUMBER,
	TIPO_MENSAJE NUMBER,
	MENSAJE VARCHAR2(3000)
) TABLESPACE SICA_DAT;

ALTER TABLE SICA_ADMIN.SC_RESPALDO_TABLA_LOG ADD CONSTRAINT PK_SC_RESPALDO_TABLA_LOG 
PRIMARY KEY (ID_RESPALDO_TABLA_LOG) USING INDEX TABLESPACE SICA_INX;

ALTER TABLE SICA_ADMIN.SC_RESPALDO_TABLA_LOG ADD CONSTRAINT FK_SC_RESPLD_TBL_LOG_RESPLD
FOREIGN KEY (ID_RESPALDO) REFERENCES SICA_ADMIN.SC_RESPALDO(ID_RESPALDO);

ALTER TABLE SICA_ADMIN.SC_RESPALDO_TABLA_LOG ADD CONSTRAINT FK_SC_RESPLD_TBL_LOG_TBL
FOREIGN KEY (ID_TABLA) REFERENCES SICA_ADMIN.SC_CAT_TABLA_RESPALDO(ID_TABLA);

DROP PUBLIC SYNONYM SC_RESPALDO_TABLA_LOG;
CREATE PUBLIC SYNONYM SC_RESPALDO_TABLA_LOG FOR SICA_ADMIN.SC_RESPALDO_TABLA_LOG;

