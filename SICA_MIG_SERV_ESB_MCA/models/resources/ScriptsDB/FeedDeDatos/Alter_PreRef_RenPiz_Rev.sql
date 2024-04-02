--REVERSO

--Se eliminan las columnas a SC_DEAL_DETALLE
--para los valores directos de precio referencia
ALTER TABLE sica_admin.sc_deal_detalle DROP column PRE_REF_MID_SPOT;
ALTER TABLE sica_admin.sc_deal_detalle DROP column PRE_REF_SPOT;

--Se eliminan las columnas a SC_H_DEAL_DETALLE
--para los valores directos de precio referencia
ALTER TABLE sica_admin.sc_h_deal_detalle DROP column PRE_REF_MID_SPOT;
ALTER TABLE sica_admin.sc_h_deal_detalle DROP column PRE_REF_SPOT;

--Se eliminan las columnas a SC_RENGLON_PIZARRON
--para los valores directos de precio referencia
ALTER TABLE sica_admin.sc_renglon_pizarron DROP column PRE_REF_MID_SPOT;
ALTER TABLE sica_admin.sc_renglon_pizarron DROP column PRE_REF_SPOT;

--Se elimina la columna agregada a SC_DEAL_DETALLE
--para el valor directo de Factor Divisa
ALTER TABLE sica_admin.sc_deal_detalle DROP column FACTOR_DIVISA;

--Se elimina la columna agregada a SC_H_DEAL_DETALLE
--para el valor directo de Factor Divisa
ALTER TABLE sica_admin.sc_h_deal_detalle DROP column FACTOR_DIVISA;

--Se vuelven  poner los constraints not null para los campos de id_precio_referencia
--y id_factor_divisa
alter table sica_admin.sc_renglon_pizarron modify id_precio_referencia number not null;
alter table sica_admin.sc_renglon_pizarron modify ID_FACTOR_DIVISA number not null;
alter table sica_admin.sc_deal_detalle modify id_precio_referencia number not null;
alter table sica_admin.sc_h_deal_detalle modify id_precio_referencia number not null;


ALTER TABLE SICA_ADMIN.SC_PRECIO_REFERENCIA_ACTUAL 
DROP COLUMN METODO_ACTUALIZACION;

ALTER TABLE SICA_ADMIN.SC_PRECIO_REFERENCIA_ACTUAL 
DROP COLUMN PRECIO_SPOT;

ALTER TABLE SICA_ADMIN.SC_PRECIO_REFERENCIA_ACTUAL 
DROP COLUMN PRECIO_COMPRA;

ALTER TABLE SICA_ADMIN.SC_PRECIO_REFERENCIA_ACTUAL 
DROP COLUMN PRECIO_VENTA;

ALTER TABLE SICA_ADMIN.SC_PRECIO_REFERENCIA_ACTUAL 
DROP COLUMN MID_SPOT;

CREATE OR REPLACE TRIGGER SICA_ADMIN.ACTPRECIOREFERENCIAACTUAL
    AFTER INSERT ON SC_PRECIO_REFERENCIA
    FOR EACH ROW
BEGIN
  -- Se borra el registro anterior
  DELETE sc_precio_referencia_actual;
  -- Se inserta el nuevo cambio
  INSERT INTO sc_precio_referencia_actual
              (id_precio_referencia,
               ultima_modificacion)
    VALUES
      (:NEW.id_precio_referencia,
       :NEW.ultima_modificacion);
END ActPrecioReferenciaActual;
/

ALTER TRIGGER SICA_ADMIN.ACTPRECIOREFERENCIAACTUAL ENABLE;

SHOW ERRORS