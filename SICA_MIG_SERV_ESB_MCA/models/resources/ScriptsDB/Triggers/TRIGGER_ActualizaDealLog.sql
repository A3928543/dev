/**
  *  Autor : Edgar Leija
  *  Fecha: 23/01/2006
  *
  *  Trigger ActualizaDealLog
  *
**/
-- ----------------------------------------------------------

DROP TRIGGER ActualizaDealLog;

CREATE OR REPLACE TRIGGER SICA_ADMIN.ActualizaDealLog
  AFTER UPDATE OR INSERT ON sc_deal
  FOR EACH ROW
BEGIN
	IF (:NEW.enviar_al_cliente != :OLD.enviar_al_cliente) THEN
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
			 :NEW.id_deal,'enviar_al_cliente',
			 :OLD.enviar_al_cliente,:NEW.enviar_al_cliente,
			 SYSDATE,null,null);
    END IF;
    IF (:NEW.eventos_deal != :OLD.eventos_deal) THEN
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
			 :NEW.id_deal,'eventos_deal',
			 :OLD.eventos_deal,:NEW.eventos_deal,
			 SYSDATE,null,null);
    END IF;
    IF (:NEW.factura != :OLD.factura) THEN
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
			 :NEW.id_deal,'factura',
			 :OLD.factura,:NEW.factura,
			 SYSDATE,null,null);
    END IF;
    IF (:NEW.id_broker != :OLD.id_broker) THEN
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
			 :NEW.id_deal,'id_broker',
			 :OLD.id_broker,:NEW.id_broker,
			 SYSDATE,null,null);
    END IF;
    IF (:NEW.id_direccion_mensajeria != :OLD.id_direccion_mensajeria) THEN
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
			 :NEW.id_deal,'id_direccion_mensajeria',
			 :OLD.id_direccion_mensajeria,:NEW.id_direccion_mensajeria,
			 SYSDATE,null,null);
    END IF;
    IF (:NEW.id_liquidacion != :OLD.id_liquidacion) THEN
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
			 :NEW.id_deal,'id_liquidacion',
			 :OLD.id_liquidacion,:NEW.id_liquidacion,
			 SYSDATE,null,null);
    END IF;
    IF (:NEW.id_promotor != :OLD.id_promotor) THEN
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
			 :NEW.id_deal,'id_promotor',
			 :OLD.id_promotor,:NEW.id_promotor,
			 SYSDATE,null,null);
    END IF;
    IF (:NEW.mensajeria != :OLD.mensajeria) THEN
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
			 :NEW.id_deal,'mensajeria',
			 :OLD.mensajeria,:NEW.mensajeria,
			 SYSDATE,null,null);
    END IF;
    IF (:NEW.no_cuenta != :OLD.no_cuenta) THEN
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
			 :NEW.id_deal,'no_cuenta',
			 :OLD.no_cuenta,:NEW.no_cuenta,
			 SYSDATE,null,null);
    END IF;
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
			 :NEW.id_deal,'observaciones',
			 :OLD.observaciones,:NEW.observaciones,
			 SYSDATE,null,null);
    END IF;
    IF (:NEW.pago_anticipado != :OLD.pago_anticipado) THEN
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
			 :NEW.id_deal,'pago_anticipado',
			 :OLD.pago_anticipado,:NEW.pago_anticipado,
			 SYSDATE,null,null);
    END IF;
    IF (:NEW.toma_en_firme != :OLD.toma_en_firme) THEN
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
			 :NEW.id_deal,'toma_en_firme',
			 :OLD.toma_en_firme,:NEW.toma_en_firme,
			 SYSDATE,null,null);
    END IF;
    IF (:NEW.nombre_factura != :OLD.nombre_factura) THEN
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
			 :NEW.id_deal,'nombre_factura',
			 :OLD.nombre_factura,:NEW.nombre_factura,
			 SYSDATE,null,null);
    END IF;
    IF (:NEW.rfc_factura != :OLD.rfc_factura) THEN
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
			 :NEW.id_deal,'rfc_factura',
			 :OLD.rfc_factura,:NEW.rfc_factura,
			 SYSDATE,null,null);
    END IF;
    IF (:NEW.id_dir_factura != :OLD.id_dir_factura) THEN
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
			 :NEW.id_deal,'id_dir_factura',
			 :OLD.id_dir_factura,:NEW.id_dir_factura,
			 SYSDATE,null,null);
    END IF;
    IF (:NEW.id_folio_swap != :OLD.id_folio_swap) THEN
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
			 :NEW.id_deal,'id_folio_swap',
			 :OLD.id_folio_swap,:NEW.id_folio_swap,
			 SYSDATE,null,null);
    END IF;
END ActualizaDealLog;
/