--Se agregan las columnas a SC_DEAL_DETALLE
--para los valores directos de precio referencia
ALTER TABLE sica_admin.sc_deal_detalle ADD ( PRE_REF_MID_SPOT NUMBER(12,6));
ALTER TABLE sica_admin.sc_deal_detalle ADD ( PRE_REF_SPOT    NUMBER(12,6));

--Se agregan las columnas a SC_H_DEAL_DETALLE
--para los valores directos de precio referencia
ALTER TABLE sica_admin.sc_h_deal_detalle ADD ( PRE_REF_MID_SPOT NUMBER(12,6));
ALTER TABLE sica_admin.sc_h_deal_detalle ADD ( PRE_REF_SPOT     NUMBER(12,6));

--Se agregan las columnas a SC_RENGLON_PIZARRON
--para los valores directos de precio referencia
ALTER TABLE sica_admin.sc_renglon_pizarron ADD ( PRE_REF_MID_SPOT NUMBER(12,6));
ALTER TABLE sica_admin.sc_renglon_pizarron ADD ( PRE_REF_SPOT     NUMBER(12,6));

--Se agregan las columnas a SC_DEAL_DETALLE
--para el valor directo de Factor Divisa
ALTER TABLE sica_admin.sc_deal_detalle ADD ( FACTOR_DIVISA NUMBER(12,6));

--Se agregan las columnas a SC_H_DEAL_DETALLE
--para el valor directo de Factor Divisa
ALTER TABLE sica_admin.sc_h_deal_detalle ADD ( FACTOR_DIVISA NUMBER(12,6));

--Se eliminan los constraints not null para los campos de id_precio_referencia
--y id_factor_divisa
alter table sica_admin.sc_renglon_pizarron modify id_precio_referencia number null;
alter table sica_admin.sc_renglon_pizarron modify ID_FACTOR_DIVISA number null;
alter table sica_admin.sc_deal_detalle modify id_precio_referencia number null;
alter table sica_admin.sc_h_deal_detalle modify id_precio_referencia number null;

----------------


--Se elimina el trigger en SC_PRECIO_REFERENCIA
ALTER TABLE SICA_ADMIN.SC_PRECIO_REFERENCIA disable all triggers;
DROP TRIGGER SICA_ADMIN.ACTPRECIOREFERENCIAACTUAL;

--Se eliminan los campos existentes para poder alterar la tabla
DELETE FROM sc_precio_referencia_actual;

--Se agregan los campos necesarios en SC_PRECIO_REFERENCIA_ACTUAL
ALTER TABLE SICA_ADMIN.SC_PRECIO_REFERENCIA_ACTUAL 
ADD (METODO_ACTUALIZACION CHAR(1 BYTE) NOT NULL);

ALTER TABLE SICA_ADMIN.SC_PRECIO_REFERENCIA_ACTUAL 
ADD (PRECIO_SPOT NUMBER(12, 6) );

ALTER TABLE SICA_ADMIN.SC_PRECIO_REFERENCIA_ACTUAL 
ADD (PRECIO_COMPRA NUMBER(12,6) );

ALTER TABLE SICA_ADMIN.SC_PRECIO_REFERENCIA_ACTUAL 
ADD (PRECIO_VENTA NUMBER(12,6) );

ALTER TABLE SICA_ADMIN.SC_PRECIO_REFERENCIA_ACTUAL 
ADD (MID_SPOT NUMBER(12,6) );

--Se inserta el registro del ultimo precio referencia
INSERT INTO SICA_ADMIN.SC_PRECIO_REFERENCIA_ACTUAL
  (
    ID_PRECIO_REFERENCIA,
    METODO_ACTUALIZACION,
    PRECIO_SPOT,
    PRECIO_COMPRA,
    PRECIO_VENTA,
    ULTIMA_MODIFICACION,
    MID_SPOT
  )
  (SELECT ID_PRECIO_REFERENCIA,
      METODO_ACTUALIZACION,
      PRECIO_SPOT,
      PRECIO_COMPRA,
      PRECIO_VENTA,
      ULTIMA_MODIFICACION,
      MID_SPOT
    FROM SICA_ADMIN.SC_PRECIO_REFERENCIA
    WHERE id_precio_referencia = (SELECT MAX(id_precio_referencia) FROM SICA_ADMIN.sc_precio_referencia)
  );