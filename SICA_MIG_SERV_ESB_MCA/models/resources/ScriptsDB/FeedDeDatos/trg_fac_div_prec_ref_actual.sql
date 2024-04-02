-- ***********************************************************************************************************************
-- * Trigger que inserta en SC_FACTOR_DIVISA al insertar en SC_FACTOR_DIVISA_ACTUAL, con el objeto de mantener 
-- * la historia de las variaciones del factor divisa
-- ***********************************************************************************************************************

CREATE OR REPLACE TRIGGER SICA_ADMIN.INSERT_FACTOR_DIVISA
    AFTER INSERT ON SICA_ADMIN.SC_FACTOR_DIVISA_ACTUAL FOR EACH ROW
BEGIN
	INSERT INTO SICA_ADMIN.SC_FACTOR_DIVISA (
		ID_FACTOR_DIVISA, 
		FROM_ID_DIVISA, 
		TO_ID_DIVISA, 
		FACTOR, 
		METODO_ACTUALIZACION, 
		SPREAD_REFERENCIA, 
		SPREAD_COMPRA, 
		CARRY, 
		ULTIMA_MODIFICACION, 
		FACTOR_COMPRA, 
		SLACK
	) VALUES ( 
		:NEW.ID_FACTOR_DIVISA, 
		:NEW.FROM_ID_DIVISA, 
		:NEW.TO_ID_DIVISA, 
		:NEW.FACTOR, 
		:NEW.METODO_ACTUALIZACION, 
		:NEW.SPREAD_REFERENCIA, 
		:NEW.SPREAD_COMPRA, 
		:NEW.CARRY, 
		:NEW.ULTIMA_MODIFICACION, 
		:NEW.FACTOR_COMPRA, 
		:NEW.SLACK
	);
END;
/

-- ***********************************************************************************************************************
-- * Trigger que inserta en SC_PRECIO_REFERENCIA al insertar en SC_PRECIO_REFERENCIA_ACTUAL, con el objeto de mantener 
-- * la historia de las variaciones del precio referencia
-- ***********************************************************************************************************************

CREATE OR REPLACE TRIGGER SICA_ADMIN.INSERT_PRECIO_REFERENCIA
    AFTER INSERT ON SICA_ADMIN.SC_PRECIO_REFERENCIA_ACTUAL FOR EACH ROW
BEGIN
	INSERT INTO SICA_ADMIN.SC_PRECIO_REFERENCIA (
		ID_PRECIO_REFERENCIA, 
		METODO_ACTUALIZACION, 
		PRECIO_SPOT, 
		PRECIO_COMPRA, 
		PRECIO_VENTA, 
		ULTIMA_MODIFICACION, 
		MID_SPOT
	) VALUES ( 
		:NEW.ID_PRECIO_REFERENCIA, 
		:NEW.METODO_ACTUALIZACION, 
		:NEW.PRECIO_SPOT, 
		:NEW.PRECIO_COMPRA, 
		:NEW.PRECIO_VENTA, 
		:NEW.ULTIMA_MODIFICACION, 
		:NEW.MID_SPOT 
	);
END;
/

show errors;