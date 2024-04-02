/**
  *  Autor : Edgar Leija
  *  Fecha: 23/01/2006
  *
  *  Trigger ActualizaDealLogByDealPosicion
  *
**/
-- ----------------------------------------------------------

DROP TRIGGER ActualizaDealLogByDealPosicion;

CREATE OR REPLACE TRIGGER SICA_ADMIN.ActualizaDealLogByDealPosicion
  AFTER UPDATE OR INSERT ON sc_deal_posicion
  FOR EACH ROW
DECLARE
id_deal_pos NUMBER;

  BEGIN
      IF (:NEW.tipo_deal = 'C' OR :NEW.tipo_deal = 'I') THEN
          IF (:NEW.observaciones != :OLD.observaciones) THEN
                INSERT INTO sc_deal_log (
                    id_deal_log,
                    id_deal,
                    nombre_campo,
                    valor_previo,
                    valor_nuevo,
                    fecha_cambio,
                    id_deal_posicion,
                    folio_detalle)
                VALUES
                    (sc_deal_log_seq.nextval,
                     749,'observaciones',
                     :OLD.observaciones,:NEW.observaciones,
                     SYSDATE,null,null);
            END IF;
            IF (:NEW.tipo_cambio != :OLD.tipo_cambio) THEN
                INSERT INTO sc_deal_log (
                    id_deal_log,
                    id_deal,
                    nombre_campo,
                    valor_previo,
                    valor_nuevo,
                    fecha_cambio,
                    id_deal_posicion,
                    folio_detalle)
                VALUES
                    (sc_deal_log_seq.nextval,
                     749,'tipo_cambio',
                     :OLD.tipo_cambio,:NEW.tipo_cambio,
                     SYSDATE,null,null);
            END IF;
      END IF;
  END ActualizaDealLogByDealPosicion;
/