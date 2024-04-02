/**
  *  Autor: Ricardo Legorreta H.
  *  Fecha: 15/09/2005
  *
  *  Trigger ActPrecioReferenciaActual
  *
  **/

-- ----------------------------------------------------------

CREATE OR REPLACE TRIGGER SICA_ADMIN.ActPrecioReferenciaActual
    AFTER INSERT ON sc_precio_referencia
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
