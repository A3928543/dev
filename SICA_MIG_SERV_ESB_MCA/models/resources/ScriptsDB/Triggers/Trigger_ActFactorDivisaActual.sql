/**
  *  Autor: Jean C. Favila.
  *  Fecha: 15/09/2005
  *
  *  Trigger ActFactorDivisaActual
  *
  **/

-- ----------------------------------------------------------

CREATE OR REPLACE TRIGGER SICA_ADMIN.ActFactorDivisaActual
    AFTER INSERT ON sc_factor_divisa
    FOR EACH ROW
BEGIN
    -- Se borra el registro anterior
    DELETE sc_factor_divisa_actual
     WHERE from_id_divisa = :NEW.from_id_divisa
       AND to_id_divisa = :NEW.to_id_divisa;
    -- Se inserta el nuevo cambio
    INSERT INTO sc_factor_divisa_actual
        (from_id_divisa, to_id_divisa, id_factor_divisa, ultima_modificacion)
    VALUES
      (:NEW.from_id_divisa,
       :NEW.to_id_divisa,
       :NEW.id_factor_divisa, sysdate);
END ActFactorDivisaActual;
/
