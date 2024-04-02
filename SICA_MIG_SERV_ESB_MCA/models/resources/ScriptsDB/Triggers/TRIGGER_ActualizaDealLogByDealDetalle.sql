/**
  *  Autor : Edgar Leija
  *  Fecha: 23/01/2006
  *
  *  Trigger ActualizaDealLogByDealDetalle
  *
**/
-- ----------------------------------------------------------

DROP TRIGGER ActualizaDealLogByDealDetalle;

CREATE OR REPLACE TRIGGER SICA_ADMIN.ActualizaDealLogByDealDetalle
  AFTER UPDATE ON sc_deal_detalle
  FOR EACH ROW
  BEGIN
	IF (:NEW.comision_cobrada_mxn != :OLD.comision_cobrada_mxn) THEN
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
			 :NEW.id_deal,'comision_cobrada_mxn',
			 :OLD.comision_cobrada_mxn,:NEW.comision_cobrada_mxn,
			 SYSDATE,null,:NEW.folio_detalle);
    END IF;
    IF (:NEW.comision_cobrada_usd != :OLD.comision_cobrada_usd) THEN
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
			 :NEW.id_deal,'comision_cobrada_usd',
			 :OLD.comision_cobrada_usd,:NEW.comision_cobrada_usd,
			 SYSDATE,null,:NEW.folio_detalle);
    END IF;
    IF (:NEW.comision_oficial_usd != :OLD.comision_oficial_usd) THEN
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
			 :NEW.id_deal,'comision_oficial_usd',
			 :OLD.comision_oficial_usd,:NEW.comision_oficial_usd,
			 SYSDATE,null,:NEW.folio_detalle);
    END IF;
    IF (:NEW.denominacion != :OLD.denominacion) THEN
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
			 :NEW.id_deal,'denominacion',
			 :OLD.denominacion,:NEW.denominacion,
			 SYSDATE,null,:NEW.folio_detalle);
    END IF;
    IF (:NEW.eventos_detalle_deal != :OLD.eventos_detalle_deal) THEN
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
			 :NEW.id_deal,'eventos_detalle_deal',
			 :OLD.eventos_detalle_deal,:NEW.eventos_detalle_deal,
			 SYSDATE,null,:NEW.folio_detalle);
    END IF;
    IF (:NEW.id_liquidacion_detalle != :OLD.id_liquidacion_detalle) THEN
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
			 :NEW.id_deal,'id_liquidacion_detalle',
			 :OLD.id_liquidacion_detalle,:NEW.id_liquidacion_detalle,
			 SYSDATE,null,:NEW.folio_detalle);
    END IF;
    IF (:NEW.id_plantilla != :OLD.id_plantilla) THEN
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
			 :NEW.id_deal,'id_plantilla',
			 :OLD.id_plantilla,:NEW.id_plantilla,
			 SYSDATE,null,:NEW.folio_detalle);
    END IF;
    IF (:NEW.instrucciones_beneficiario != :OLD.instrucciones_beneficiario) THEN
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
			 :NEW.id_deal,'instrucciones_beneficiario',
			 :OLD.instrucciones_beneficiario,:NEW.instrucciones_beneficiario,
			 SYSDATE,null,:NEW.folio_detalle);
    END IF;
    IF (:NEW.instrucciones_intermediario != :OLD.instrucciones_intermediario) THEN
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
			 :NEW.id_deal,'instrucciones_intermediario',
			 :OLD.instrucciones_intermediario,:NEW.instrucciones_intermediario,
			 SYSDATE,null,:NEW.folio_detalle);
    END IF;
    IF (:NEW.instrucciones_pagador != :OLD.instrucciones_pagador) THEN
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
			 :NEW.id_deal,'instrucciones_pagador',
			 :OLD.instrucciones_pagador,:NEW.instrucciones_pagador,
			 SYSDATE,null,:NEW.folio_detalle);
    END IF;
    IF (:NEW.mnemonico != :OLD.mnemonico) THEN
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
			 :NEW.id_deal,'mnemonico',
			 :OLD.mnemonico,:NEW.mnemonico,
			 SYSDATE,null,:NEW.folio_detalle);
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
			 SYSDATE,null,:NEW.folio_detalle);
    END IF;
    IF (:NEW.tipo_cambio_mesa != :OLD.tipo_cambio_mesa) THEN
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
			 :NEW.id_deal,'tipo_cambio_mesa',
			 :OLD.tipo_cambio_mesa,:NEW.tipo_cambio_mesa,
			 SYSDATE,null,:NEW.folio_detalle);
    END IF;
   END ActualizaDealLogByDealDetalle;
/

/**
  *  Autor : Edgar Leija
  *  Fecha: 23/01/2006
  *
  *  Trigger InsertaFechaCreacionDetalle
  *
**/
-- ----------------------------------------------------------

DROP TRIGGER InsertaFechaCreacionDetalle;

CREATE OR REPLACE TRIGGER SICA_ADMIN.InsertaFechaCreacionDetalle
  AFTER INSERT ON sc_deal_detalle
  FOR EACH ROW
  BEGIN
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
                 :NEW.id_deal,'fecha_creacion',
                 '-','-',
                 SYSDATE,:NEW.id_deal_posicion,:NEW.folio_detalle);
  END InsertaFechaCreacionDetalle;
/