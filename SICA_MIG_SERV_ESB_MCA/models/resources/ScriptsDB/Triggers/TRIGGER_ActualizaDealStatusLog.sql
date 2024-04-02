/**
  *  Autor : Edgar Leija
  *  Fecha: 30/08/2005
  *
  *  Trigger ActualizaDealStatusLog
  *
**/
-- ----------------------------------------------------------



CREATE OR REPLACE TRIGGER SICA_ADMIN.ActualizaDealStatusLog
  AFTER UPDATE ON sc_deal
  FOR EACH ROW
  BEGIN
	IF (:NEW.status_Deal != :OLD.status_Deal) THEN
		INSERT INTO sc_deal_status_log (
			id_deal_status_log,
			id_deal,
			fecha_cambio,
			status_anterior,
			status_nuevo)
		VALUES
			(sc_deal_status_log_seq.nextval,
			 :NEW.id_deal,SYSDATE,
			 :OLD.status_Deal,:NEW.status_Deal);
         END IF;
   END ActualizaDealStatusLog;

 /**
  *  Autor : Edgar Leija
  *  Fecha: 30/08/2005
  *
  *  Trigger ActualizaDetalleStatusLog
  *
**/
-- ----------------------------------------------------------



CREATE OR REPLACE TRIGGER SICA_ADMIN.ActualizaDetalleStatusLog
  AFTER UPDATE ON sc_deal_detalle
  FOR EACH ROW
  BEGIN
	IF (:NEW.status_Detalle_Deal != :OLD.status_Detalle_Deal) THEN
		INSERT INTO sc_deal_detalle_status_log (
			id_detalle_status_log,
			fecha_cambio,
			id_deal_posicion,
			status_anterior,
			status_nuevo)
		VALUES
			(sc_deal_detalle_status_log_seq.nextval,
			 SYSDATE,:NEW.id_deal_posicion,
			 :OLD.status_Detalle_Deal,:NEW.status_detalle_Deal);
         END IF;
   END ActualizaDetalleStatusLog;
 /