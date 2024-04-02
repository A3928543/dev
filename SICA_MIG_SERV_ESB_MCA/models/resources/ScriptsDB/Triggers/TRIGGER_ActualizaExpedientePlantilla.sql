/**
  *  Autor : Carlos Covián S.
  *  Fecha: 05/07/2007
  *
  *  Trigger ActualizaExpedientePlantilla
  *
**/
-- ----------------------------------------------------------

CREATE OR REPLACE TRIGGER SICA_ADMIN.ActualizaExpedientePlantilla
  AFTER UPDATE OF status_plantilla ON sica_admin.sc_plantilla
  FOR EACH ROW
  BEGIN
    IF (:NEW.status_plantilla != :OLD.status_plantilla AND :NEW.STATUS_PLANTILLA = 'AC') THEN
            UPDATE BUPDES.BUP_EXPEDIENTE_PERSONA
            SET STATUS = 'ENTREGADO', FECHA_ULT_MOD = SYSDATE , USUARIO_ULT_MOD = 'trigger'
            WHERE ID_ROL = 'BENT' AND ID_DOCUMENTO = 'CAB' AND ID_TIPO_DOCUMENTO = 'CAASBENT'
            AND (ID_PERSONA = :NEW.ID_BENEFICIARIO) AND (NO_CUENTA = :NEW.NO_CUENTA);
    END IF;     
END ActualizaExpedientePlantilla;
/
