 /**
  *  $Id: TRIGGER_ActualizaPrecioRef.sql,v 1.13 2008/02/22 18:25:46 ccovian Exp $
  *  Autor: Ricardo Legorreta H, Jean C. Favila
  *  Fecha: 15/09/2005
  *
  *  Trigger ActualizaPrecioReferencia
  *
  **/

-- ----------------------------------------------------------

CREATE OR REPLACE TRIGGER SICA_ADMIN.ActualizaPrecioReferencia
    AFTER INSERT ON SICA_VARIACION
    FOR EACH ROW
DECLARE
  PRAGMA AUTONOMOUS_TRANSACTION;
  CURSOR c_sc_precio_referencia IS
                    SELECT sc_precio_referencia.precio_spot,
                           sc_precio_referencia.metodo_actualizacion
                        FROM sc_precio_referencia, sc_precio_referencia_actual
                    WHERE
                        (sc_precio_referencia_actual.id_precio_referencia =
                         sc_precio_referencia.id_precio_referencia);
  CURSOR c_sc_parametro IS
                        SELECT valor
                            FROM sc_parametro
                        WHERE
                            (id_parametro = 'SLACK_PRECIO_REFERENCIA');
  CURSOR c_sc_spread IS
           SELECT t2.SPREAD_COMPRA spread_compra
           FROM sc_factor_divisa_actual t1, sc_factor_divisa t2
           WHERE t1.FROM_ID_DIVISA = 'USD' AND t1.TO_ID_DIVISA = 'USD'
           AND t1.ID_FACTOR_DIVISA = t2.ID_FACTOR_DIVISA;
  CURSOR c_sc_fd_cad IS
		SELECT t1.from_id_divisa, t1.to_id_divisa, t1.factor,
				t1.metodo_actualizacion, t1.spread_referencia,
				t1.spread_compra, t1.factor_compra, t1.carry, t1.slack
		FROM sc_factor_divisa t1, sc_factor_divisa_actual t2
		WHERE t1.id_factor_divisa = t2.id_factor_divisa
		AND t1.from_id_divisa = 'CAD';
  CURSOR c_sc_fd_euro IS
		SELECT t1.from_id_divisa, t1.to_id_divisa, t1.factor,
				t1.metodo_actualizacion, t1.spread_referencia,
				t1.spread_compra, t1.factor_compra, t1.carry, t1.slack
		FROM sc_factor_divisa t1, sc_factor_divisa_actual t2
		WHERE t1.id_factor_divisa = t2.id_factor_divisa
		AND t1.from_id_divisa = 'EUR';
  CURSOR c_sc_fd_chf IS
		SELECT t1.from_id_divisa, t1.to_id_divisa, t1.factor,
				t1.metodo_actualizacion, t1.spread_referencia,
				t1.spread_compra, t1.factor_compra, t1.carry, t1.slack
		FROM sc_factor_divisa t1, sc_factor_divisa_actual t2
		WHERE t1.id_factor_divisa = t2.id_factor_divisa
		AND t1.from_id_divisa = 'CHF';
  CURSOR c_sc_fd_gbp IS
		SELECT t1.from_id_divisa, t1.to_id_divisa, t1.factor,
				t1.metodo_actualizacion, t1.spread_referencia,
				t1.spread_compra, t1.factor_compra, t1.carry, t1.slack
		FROM sc_factor_divisa t1, sc_factor_divisa_actual t2
		WHERE t1.id_factor_divisa = t2.id_factor_divisa
		AND t1.from_id_divisa = 'GBP';
  CURSOR c_sc_fd_jpy IS
		SELECT t1.from_id_divisa, t1.to_id_divisa, t1.factor,
				t1.metodo_actualizacion, t1.spread_referencia,
				t1.spread_compra, t1.factor_compra, t1.carry, t1.slack
		FROM sc_factor_divisa t1, sc_factor_divisa_actual t2
		WHERE t1.id_factor_divisa = t2.id_factor_divisa
		AND t1.from_id_divisa = 'JPY';

  sc_precio_referencia_rec  c_sc_precio_referencia%ROWTYPE;
  sc_parametro_rec		    c_sc_parametro%ROWTYPE;
  sc_spread_rec             c_sc_spread%ROWTYPE;
  sc_fd_cad_rec             c_sc_fd_cad%ROWTYPE;
  sc_fd_euro_rec            c_sc_fd_euro%ROWTYPE;
  sc_fd_chf_rec             c_sc_fd_chf%ROWTYPE;
  sc_fd_gbp_rec             c_sc_fd_gbp%ROWTYPE;
  sc_fd_jpy_rec             c_sc_fd_jpy%ROWTYPE;
  slack_cambio			    float;
  spread                    float;
  spread_compra             float;
  spread_venta              float;
  metodo_actualizacion      char(1);
  metodo_actualizacion_fac  char(1);
  notificar                 char(1);
  id_precio_ref             number;
  wl_protocolo              varchar2(125);
  wl_ip                     varchar2(125);
  wl_puerto                 varchar2(125);
BEGIN
    notificar := 'N';
    -- -------------------------
    -- Se lee el precio de referencia anterior
    OPEN c_sc_precio_referencia;
    FETCH c_sc_precio_referencia INTO sc_precio_referencia_rec;
    IF (c_sc_precio_referencia%NOTFOUND) THEN
        RAISE_APPLICATION_ERROR(-20001,
           'Error NO se pudo leer el ultimp precio de referencia');
    END IF;
    CLOSE c_sc_precio_referencia;
    IF (:NEW.variacion_vta_spot = 0 OR :NEW.variacion_vta_spot_ask = 0) THEN
        metodo_actualizacion := 'A';
    ELSE
        metodo_actualizacion := sc_precio_referencia_rec.metodo_actualizacion;
    END IF;

    IF (metodo_actualizacion = 'A' OR metodo_actualizacion = 'P') THEN
        -- -------------------------
        -- Se lee el parametros de slack para saber si se actualiza
        -- o no el precio de referencia.
        OPEN c_sc_parametro;
        FETCH c_sc_parametro INTO sc_parametro_rec;
        IF (c_sc_parametro%NOTFOUND) THEN
            RAISE_APPLICATION_ERROR(-20001,
                'Error NO se pudo leer el SLACK del precio de referencia');
        END IF;
        CLOSE c_sc_parametro;
        slack_cambio := to_number(sc_parametro_rec.valor, '999.999999');

        IF (ABS(:NEW.variacion_mid -
            sc_precio_referencia_rec.precio_spot) > slack_cambio) THEN
            -- Se obtiene el spread:

            OPEN c_sc_spread;
            FETCH c_sc_spread INTO sc_spread_rec;
             IF (c_sc_spread%NOTFOUND) THEN
               RAISE_APPLICATION_ERROR(-20001,
                  'Error al leer el spread de compra');
             END IF;
             CLOSE c_sc_spread;

             IF (:NEW.variacion_vta_spot = 0) THEN
                spread_compra := 0;
             ELSE
                spread_compra := sc_spread_rec.spread_compra / 2;
             END IF;
             IF (:NEW.variacion_vta_spot_ask = 0) THEN
                spread_venta := 0;
             ELSE
                spread_venta := sc_spread_rec.spread_compra / 2;
             END IF;
           -- -------------------------
           -- Se realiza la actualizacion
           IF (metodo_actualizacion = 'A') THEN
               notificar := 'S';
               INSERT INTO sc_precio_referencia (
                        id_precio_referencia,
                        ultima_modificacion,
                        metodo_actualizacion,
                        precio_spot,
                        precio_compra,
                        precio_venta,
                        mid_spot)
               VALUES
                  (sc_precio_referencia_seq.nextval,
                   :NEW.fecha,
                   'A',
                   :NEW.variacion_mid,
                   :NEW.variacion_vta_spot - spread_compra,
                   :NEW.variacion_vta_spot_ask + spread_venta,
                   :NEW.variacion_mid);
           ELSE -- Es mid spot:
               notificar := 'S';
               INSERT INTO sc_precio_referencia (
                        id_precio_referencia,
                        ultima_modificacion,
                        metodo_actualizacion,
                        precio_spot,
                        precio_compra,
                        precio_venta,
                        mid_spot)
               VALUES
                  (sc_precio_referencia_seq.nextval,
                  :NEW.fecha,
                  'P',
                  :NEW.variacion_mid,
                  :NEW.variacion_mid - spread_compra,
                  :NEW.variacion_mid + spread_venta,
                  :NEW.variacion_mid);
           END IF;
        -- ELSE do nothing no se cambio el precio pues fue menor a slack
        END IF;
    -- ELSE do nothing el metodo de actualizacion no se encuentra en automatico
    END IF;

    -- Se revisa el metodo de actualizacion de los factores:
    SELECT VALOR INTO metodo_actualizacion_fac FROM SC_PARAMETRO
       WHERE ID_PARAMETRO = 'FACTOR_AUTOMATICO';

   IF (metodo_actualizacion_fac = 'S') THEN
        ---------------------------------------------------------------------------
        -- DOLAR CANADIENSE:
        ---------------------------------------------------------------------------
        OPEN c_sc_fd_cad;
        FETCH c_sc_fd_cad INTO sc_fd_cad_rec;
        IF (c_sc_fd_cad%NOTFOUND) THEN
            RAISE_APPLICATION_ERROR(-20001,
                'Error NO se pudo leer el ultimo factor divisa de cad.');
        END IF;
        CLOSE c_sc_fd_cad;
        IF (ABS(:NEW.cad_variacion_vta_spot_ask -
                sc_fd_cad_rec.factor) > sc_fd_cad_rec.slack OR
            ABS(:NEW.cad_variacion_vta_spot -
                sc_fd_cad_rec.factor_compra) > sc_fd_cad_rec.slack) THEN
            notificar := 'S';
            INSERT INTO sc_factor_divisa
                (id_factor_divisa, from_id_divisa, to_id_divisa,
                 factor, metodo_actualizacion,  spread_referencia,
                 spread_compra, carry, factor_compra, slack,
                 ultima_modificacion)
            VALUES
                (sc_factor_divisa_seq.nextval,
                 sc_fd_cad_rec.from_id_divisa,
                 sc_fd_cad_rec.to_id_divisa,
                 1 / :NEW.cad_variacion_vta_spot_ask,
                 sc_fd_cad_rec.metodo_actualizacion,
                 sc_fd_cad_rec.spread_referencia,
                 sc_fd_cad_rec.spread_compra,
                 sc_fd_cad_rec.carry,
                 1 / :NEW.cad_variacion_vta_spot,
                 sc_fd_cad_rec.slack,
                 sysdate);
        END IF; -- Slack CAD

        ---------------------------------------------------------------------------
        -- EURO:
        ---------------------------------------------------------------------------
        OPEN c_sc_fd_euro;
        FETCH c_sc_fd_euro INTO sc_fd_euro_rec;
        IF (c_sc_fd_euro%NOTFOUND) THEN
            RAISE_APPLICATION_ERROR(-20001,
                'Error NO se pudo leer el ultimo factor divisa de euro.');
        END IF;
        CLOSE c_sc_fd_euro;
        IF (ABS(:NEW.eur_variacion_vta_spot_ask -
                sc_fd_euro_rec.factor) > sc_fd_euro_rec.slack OR
            ABS(:NEW.eur_variacion_vta_spot -
                sc_fd_euro_rec.factor_compra) > sc_fd_euro_rec.slack) THEN
            notificar := 'S';
            INSERT INTO sc_factor_divisa
                (id_factor_divisa, from_id_divisa, to_id_divisa,
                 factor, metodo_actualizacion,  spread_referencia,
                 spread_compra, carry, factor_compra, slack,
                 ultima_modificacion)
            VALUES
                (sc_factor_divisa_seq.nextval,
                 sc_fd_euro_rec.from_id_divisa,
                 sc_fd_euro_rec.to_id_divisa,
                 :NEW.eur_variacion_vta_spot_ask,
                 sc_fd_euro_rec.metodo_actualizacion,
                 sc_fd_euro_rec.spread_referencia,
                 sc_fd_euro_rec.spread_compra,
                 sc_fd_euro_rec.carry,
                 :NEW.eur_variacion_vta_spot,
                 sc_fd_euro_rec.slack,
                 sysdate);
        END IF; -- Slack EUR

        ---------------------------------------------------------------------------
        -- FRANCO SUIZO:
        ---------------------------------------------------------------------------
        OPEN c_sc_fd_chf;
        FETCH c_sc_fd_chf INTO sc_fd_chf_rec;
        IF (c_sc_fd_chf%NOTFOUND) THEN
            RAISE_APPLICATION_ERROR(-20001,
                'Error NO se pudo leer el ultimo factor divisa de Franco Suizo.');
        END IF;
        CLOSE c_sc_fd_chf;
        IF (ABS(:NEW.chf_variacion_vta_spot_ask -
                sc_fd_chf_rec.factor) > sc_fd_chf_rec.slack OR
            ABS(:NEW.chf_variacion_vta_spot -
                sc_fd_chf_rec.factor_compra) > sc_fd_chf_rec.slack) THEN
            notificar := 'S';
            INSERT INTO sc_factor_divisa
                (id_factor_divisa, from_id_divisa, to_id_divisa,
                 factor, metodo_actualizacion,  spread_referencia,
                 spread_compra, carry, factor_compra, slack,
                 ultima_modificacion)
            VALUES
                (sc_factor_divisa_seq.nextval,
                 sc_fd_chf_rec.from_id_divisa,
                 sc_fd_chf_rec.to_id_divisa,
                 1 / :NEW.chf_variacion_vta_spot_ask,
                 sc_fd_chf_rec.metodo_actualizacion,
                 sc_fd_chf_rec.spread_referencia,
                 sc_fd_chf_rec.spread_compra,
                 sc_fd_chf_rec.carry,
                 1 / :NEW.chf_variacion_vta_spot,
                 sc_fd_chf_rec.slack,
                 sysdate);
        END IF; -- Slack CHF

        ---------------------------------------------------------------------------
        -- LIBRA ESTERLINA:
        ---------------------------------------------------------------------------
        OPEN c_sc_fd_gbp;
        FETCH c_sc_fd_gbp INTO sc_fd_gbp_rec;
        IF (c_sc_fd_gbp%NOTFOUND) THEN
            RAISE_APPLICATION_ERROR(-20001,
                'Error NO se pudo leer el ultimo factor divisa de Franco Suizo.');
        END IF;
        CLOSE c_sc_fd_gbp;
        IF (ABS(:NEW.gbp_variacion_vta_spot_ask -
                sc_fd_gbp_rec.factor) > sc_fd_gbp_rec.slack OR
            ABS(:NEW.gbp_variacion_vta_spot -
                sc_fd_gbp_rec.factor_compra) > sc_fd_gbp_rec.slack) THEN
            notificar := 'S';
            INSERT INTO sc_factor_divisa
                (id_factor_divisa, from_id_divisa, to_id_divisa,
                 factor, metodo_actualizacion,  spread_referencia,
                 spread_compra, carry, factor_compra, slack,
                 ultima_modificacion)
            VALUES
                (sc_factor_divisa_seq.nextval,
                 sc_fd_gbp_rec.from_id_divisa,
                 sc_fd_gbp_rec.to_id_divisa,
                 :NEW.gbp_variacion_vta_spot_ask,
                 sc_fd_gbp_rec.metodo_actualizacion,
                 sc_fd_gbp_rec.spread_referencia,
                 sc_fd_gbp_rec.spread_compra,
                 sc_fd_gbp_rec.carry,
                 :NEW.gbp_variacion_vta_spot,
                 sc_fd_gbp_rec.slack,
                 sysdate);
        END IF; -- Slack GBP

        ---------------------------------------------------------------------------
        -- YEN:
        ---------------------------------------------------------------------------
        OPEN c_sc_fd_jpy;
        FETCH c_sc_fd_jpy INTO sc_fd_jpy_rec;
        IF (c_sc_fd_jpy%NOTFOUND) THEN
            RAISE_APPLICATION_ERROR(-20001,
                'Error NO se pudo leer el ultimo factor divisa de Franco Suizo.');
        END IF;
        CLOSE c_sc_fd_jpy;
        IF (ABS(:NEW.jpy_variacion_vta_spot_ask -
                sc_fd_jpy_rec.factor) > sc_fd_jpy_rec.slack OR
            ABS(:NEW.jpy_variacion_vta_spot -
                sc_fd_jpy_rec.factor_compra) > sc_fd_jpy_rec.slack) THEN
            notificar := 'S';
            INSERT INTO sc_factor_divisa
                (id_factor_divisa, from_id_divisa, to_id_divisa,
                 factor, metodo_actualizacion,  spread_referencia,
                 spread_compra, carry, factor_compra, slack,
                 ultima_modificacion)
            VALUES
                (sc_factor_divisa_seq.nextval,
                 sc_fd_jpy_rec.from_id_divisa,
                 sc_fd_jpy_rec.to_id_divisa,
                 1 / :NEW.jpy_variacion_vta_spot_ask,
                 sc_fd_jpy_rec.metodo_actualizacion,
                 sc_fd_jpy_rec.spread_referencia,
                 sc_fd_jpy_rec.spread_compra,
                 sc_fd_jpy_rec.carry,
                 1 / :NEW.jpy_variacion_vta_spot,
                 sc_fd_jpy_rec.slack,
                 sysdate);
        END IF; -- Slack JPY
    END IF; -- metodo actualizacion de factores

    COMMIT;
    -- Se realiza la notificacion a WebLogic en caso de ser necesario:
    IF (notificar = 'S') THEN
       -- Llamar al stored procedure para notificar el cambio:
       SELECT VALOR INTO wl_protocolo FROM SC_PARAMETRO
           WHERE ID_PARAMETRO = 'WL_PROTOCOLO';
       SELECT VALOR INTO wl_ip FROM SC_PARAMETRO
           WHERE ID_PARAMETRO = 'WL_IP';
       SELECT VALOR INTO wl_puerto FROM SC_PARAMETRO
           WHERE ID_PARAMETRO = 'WL_PUERTO';
       SELECT t1.ID_PRECIO_REFERENCIA INTO id_precio_ref FROM SC_PRECIO_REFERENCIA t1, SC_PRECIO_REFERENCIA_ACTUAL t2
           WHERE t1.ID_PRECIO_REFERENCIA = t2.ID_PRECIO_REFERENCIA;
       notificarCambioReuters(wl_protocolo, wl_ip, wl_puerto,
            concat(concat('/sica/app?service=external/BroadcastPizarron&',
            'sp='), to_char(id_precio_ref)));

    END IF;
END ActualizaPrecioReferencia;
/
