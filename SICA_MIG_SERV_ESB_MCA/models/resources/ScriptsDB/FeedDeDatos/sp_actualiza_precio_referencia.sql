-- ========================================================================================================================================
-- =
-- = SICA_ADMIN.SC_SP_ACTUALIZA_PREC_REF: Procedimiento que actualiza las tablas SC_PRECIO_REFERENCIA_ACTUAL y SC_FACTOR_DIVISA_ACTUAL
-- = en base a los nuevos valores insertados en SICA_VARIACION. Anteriormente esta lògica se encontraba en el trigger 
-- = ACTUALIZAPRECIOREFERENCIA
-- =
-- ========================================================================================================================================

CREATE OR REPLACE PROCEDURE SICA_ADMIN.SC_SP_ACTUALIZA_PREC_REF (
	p_fecha IN sica_variacion.fecha%TYPE,
	p_variacion_mid IN sica_variacion.variacion_mid%TYPE,
	p_variacion_vta_spot IN sica_variacion.variacion_vta_spot%TYPE,
	p_variacion_vta_spot_ask IN sica_variacion.variacion_vta_spot_ask%TYPE,
	p_cad_variacion_vta_spot IN sica_variacion.cad_variacion_vta_spot%TYPE,
	p_cad_variacion_vta_spot_ask IN sica_variacion.cad_variacion_vta_spot_ask%TYPE,
	p_chf_variacion_vta_spot IN sica_variacion.chf_variacion_vta_spot%TYPE,
	p_chf_variacion_vta_spot_ask IN sica_variacion.chf_variacion_vta_spot_ask%TYPE,
	p_eur_variacion_vta_spot IN sica_variacion.eur_variacion_vta_spot%TYPE,
	p_eur_variacion_vta_spot_ask IN sica_variacion.eur_variacion_vta_spot_ask%TYPE,
	p_gbp_variacion_vta_spot IN sica_variacion.gbp_variacion_vta_spot%TYPE,
	p_gbp_variacion_vta_spot_ask IN sica_variacion.gbp_variacion_vta_spot_ask%TYPE,
	p_jpy_variacion_vta_spot IN sica_variacion.jpy_variacion_vta_spot%TYPE,
	p_jpy_variacion_vta_spot_ask IN sica_variacion.jpy_variacion_vta_spot_ask%TYPE
) IS
    
  PRAGMA AUTONOMOUS_TRANSACTION;
  
  CURSOR c_sc_precio_referencia IS
	SELECT precio_spot, metodo_actualizacion
	FROM sc_precio_referencia_actual;

  CURSOR c_sc_parametro IS
	SELECT valor
		FROM sc_parametro
	WHERE
		(id_parametro = 'SLACK_PRECIO_REFERENCIA');

  CURSOR c_sc_spread IS
   SELECT SPREAD_COMPRA spread_compra
   FROM sc_factor_divisa_actual
   WHERE FROM_ID_DIVISA = 'USD' AND TO_ID_DIVISA = 'USD';

	CURSOR c_sc_fd_x (p_from_id_divisa sc_factor_divisa_actual.from_id_divisa%TYPE) IS 
		SELECT from_id_divisa, to_id_divisa, factor,
			metodo_actualizacion, spread_referencia,
			spread_compra, factor_compra, carry, slack
		FROM sc_factor_divisa_actual
		WHERE from_id_divisa = p_from_id_divisa;
  
  sc_precio_referencia_rec  c_sc_precio_referencia%ROWTYPE;
  sc_parametro_rec          c_sc_parametro%ROWTYPE;
  sc_spread_rec             c_sc_spread%ROWTYPE;
  sc_fd_cad_rec             c_sc_fd_x%ROWTYPE;
  sc_fd_euro_rec            c_sc_fd_x%ROWTYPE;
  sc_fd_chf_rec             c_sc_fd_x%ROWTYPE;
  sc_fd_gbp_rec             c_sc_fd_x%ROWTYPE;
  sc_fd_jpy_rec             c_sc_fd_x%ROWTYPE;
  slack_cambio              FLOAT;
  spread                    FLOAT;
  spread_compra             FLOAT;
  spread_venta              FLOAT;
  metodo_actualizacion      CHAR(1);
  metodo_actualizacion_fac  CHAR(1);
  notificar                 CHAR(1);
  id_precio_ref             NUMBER;
  wl_protocolo              VARCHAR2(125);
  wl_ip                     VARCHAR2(125);
  wl_puerto                 VARCHAR2(125);

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

    IF (p_variacion_vta_spot = 0 OR p_variacion_vta_spot_ask = 0) THEN
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
        slack_cambio := TO_NUMBER(sc_parametro_rec.valor, '999.999999');

        IF (ABS(p_variacion_mid -
            sc_precio_referencia_rec.precio_spot) > slack_cambio) THEN
            -- Se obtiene el spread:
            OPEN c_sc_spread;
            FETCH c_sc_spread INTO sc_spread_rec;
             IF (c_sc_spread%NOTFOUND) THEN
               RAISE_APPLICATION_ERROR(-20001,
                  'Error al leer el spread de compra');
             END IF;
             CLOSE c_sc_spread;

             IF (p_variacion_vta_spot = 0) THEN
                spread_compra := 0;
             ELSE
                spread_compra := sc_spread_rec.spread_compra / 2;
             END IF;

             IF (p_variacion_vta_spot_ask = 0) THEN
                spread_venta := 0;
             ELSE
                spread_venta := sc_spread_rec.spread_compra / 2;
             END IF;

           -- -------------------------
           -- Se realiza la actualizacion
		   
           IF (metodo_actualizacion = 'A') THEN
               notificar := 'S';
               DELETE FROM sc_precio_referencia_actual;
			   INSERT INTO sc_precio_referencia_actual (
                        id_precio_referencia,
                        ultima_modificacion,
                        metodo_actualizacion,
                        precio_spot,
                        precio_compra,
                        precio_venta,
                        mid_spot)
               VALUES
                  (sc_precio_referencia_seq.NEXTVAL,
                   p_fecha,
                   'A',
                   p_variacion_mid,
                   p_variacion_vta_spot - spread_compra,
                   p_variacion_vta_spot_ask + spread_venta,
                   p_variacion_mid);
           ELSE -- Es mid spot:
               notificar := 'S';
               DELETE FROM sc_precio_referencia_actual;
			   INSERT INTO sc_precio_referencia_actual (
                        id_precio_referencia,
                        ultima_modificacion,
                        metodo_actualizacion,
                        precio_spot,
                        precio_compra,
                        precio_venta,
                        mid_spot)
               VALUES
                  (sc_precio_referencia_seq.NEXTVAL,
                  p_fecha,
                  'P',
                  p_variacion_mid,
                  p_variacion_mid - spread_compra,
                  p_variacion_mid + spread_venta,
                  p_variacion_mid);
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
		
		OPEN c_sc_fd_x ('CAD');
        FETCH c_sc_fd_x INTO sc_fd_cad_rec;
        IF (c_sc_fd_x%NOTFOUND) THEN
            RAISE_APPLICATION_ERROR(-20001,
                'Error NO se pudo leer el ultimo factor divisa de cad.');
        END IF;
        CLOSE c_sc_fd_x;
		
        IF (ABS(p_cad_variacion_vta_spot_ask -
                sc_fd_cad_rec.factor) > sc_fd_cad_rec.slack OR
            ABS(p_cad_variacion_vta_spot -
                sc_fd_cad_rec.factor_compra) > sc_fd_cad_rec.slack) THEN
            notificar := 'S';

			DELETE sc_factor_divisa_actual
			WHERE from_id_divisa = sc_fd_cad_rec.from_id_divisa
				AND to_id_divisa = sc_fd_cad_rec.to_id_divisa;
			
            INSERT INTO sc_factor_divisa_actual
                (id_factor_divisa, from_id_divisa, to_id_divisa,
                 factor, metodo_actualizacion,  spread_referencia,
                 spread_compra, carry, factor_compra, slack,
                 ultima_modificacion)
            VALUES
                (sc_factor_divisa_seq.NEXTVAL,
                 sc_fd_cad_rec.from_id_divisa,
                 sc_fd_cad_rec.to_id_divisa,
                 1 / p_cad_variacion_vta_spot_ask,
                 sc_fd_cad_rec.metodo_actualizacion,
                 sc_fd_cad_rec.spread_referencia,
                 sc_fd_cad_rec.spread_compra,
                 sc_fd_cad_rec.carry,
                 1 / p_cad_variacion_vta_spot,
                 sc_fd_cad_rec.slack,
                 SYSDATE);
        END IF; -- Slack CAD

        ---------------------------------------------------------------------------
        -- EURO:
        ---------------------------------------------------------------------------
		
		OPEN c_sc_fd_x ('EUR');
        FETCH c_sc_fd_x INTO sc_fd_euro_rec;
        IF (c_sc_fd_x%NOTFOUND) THEN
            RAISE_APPLICATION_ERROR(-20001,
                'Error NO se pudo leer el ultimo factor divisa de euro.');
        END IF;
        CLOSE c_sc_fd_x;
        
        IF (ABS(p_eur_variacion_vta_spot_ask -
                sc_fd_euro_rec.factor) > sc_fd_euro_rec.slack OR
            ABS(p_eur_variacion_vta_spot -
                sc_fd_euro_rec.factor_compra) > sc_fd_euro_rec.slack) THEN
            notificar := 'S';
			
			DELETE sc_factor_divisa_actual
			WHERE from_id_divisa = sc_fd_euro_rec.from_id_divisa
				AND to_id_divisa = sc_fd_euro_rec.to_id_divisa;

            INSERT INTO sc_factor_divisa_actual
                (id_factor_divisa, from_id_divisa, to_id_divisa,
                 factor, metodo_actualizacion,  spread_referencia,
                 spread_compra, carry, factor_compra, slack,
                 ultima_modificacion)
            VALUES
                (sc_factor_divisa_seq.NEXTVAL,
                 sc_fd_euro_rec.from_id_divisa,
                 sc_fd_euro_rec.to_id_divisa,
                 p_eur_variacion_vta_spot_ask,
                 sc_fd_euro_rec.metodo_actualizacion,
                 sc_fd_euro_rec.spread_referencia,
                 sc_fd_euro_rec.spread_compra,
                 sc_fd_euro_rec.carry,
                 p_eur_variacion_vta_spot,
                 sc_fd_euro_rec.slack,
                 SYSDATE);
        END IF; -- Slack EUR

        ---------------------------------------------------------------------------
        -- FRANCO SUIZO:
        ---------------------------------------------------------------------------

		OPEN c_sc_fd_x ('CHF');
        FETCH c_sc_fd_x INTO sc_fd_chf_rec;
        IF (c_sc_fd_x%NOTFOUND) THEN
            RAISE_APPLICATION_ERROR(-20001,
                'Error NO se pudo leer el ultimo factor divisa de Franco Suizo.');
        END IF;
        CLOSE c_sc_fd_x;
		
        IF (ABS(p_chf_variacion_vta_spot_ask -
                sc_fd_chf_rec.factor) > sc_fd_chf_rec.slack OR
            ABS(p_chf_variacion_vta_spot -
                sc_fd_chf_rec.factor_compra) > sc_fd_chf_rec.slack) THEN
            notificar := 'S';
			
			DELETE sc_factor_divisa_actual
			WHERE from_id_divisa = sc_fd_chf_rec.from_id_divisa
				AND to_id_divisa = sc_fd_chf_rec.to_id_divisa;

            INSERT INTO sc_factor_divisa_actual
                (id_factor_divisa, from_id_divisa, to_id_divisa,
                 factor, metodo_actualizacion,  spread_referencia,
                 spread_compra, carry, factor_compra, slack,
                 ultima_modificacion)
            VALUES
                (sc_factor_divisa_seq.NEXTVAL,
                 sc_fd_chf_rec.from_id_divisa,
                 sc_fd_chf_rec.to_id_divisa,
                 1 / p_chf_variacion_vta_spot_ask,
                 sc_fd_chf_rec.metodo_actualizacion,
                 sc_fd_chf_rec.spread_referencia,
                 sc_fd_chf_rec.spread_compra,
                 sc_fd_chf_rec.carry,
                 1 / p_chf_variacion_vta_spot,
                 sc_fd_chf_rec.slack,
                 SYSDATE);
        END IF; -- Slack CHF

        ---------------------------------------------------------------------------
        -- LIBRA ESTERLINA:
        ---------------------------------------------------------------------------
		
		OPEN c_sc_fd_x ('GBP');
        FETCH c_sc_fd_x INTO sc_fd_gbp_rec;
        IF (c_sc_fd_x%NOTFOUND) THEN
            RAISE_APPLICATION_ERROR(-20001,
                'Error NO se pudo leer el ultimo factor divisa de Libra esterlina.');
        END IF;
        CLOSE c_sc_fd_x;
        
        IF (ABS(p_gbp_variacion_vta_spot_ask -
                sc_fd_gbp_rec.factor) > sc_fd_gbp_rec.slack OR
            ABS(p_gbp_variacion_vta_spot -
                sc_fd_gbp_rec.factor_compra) > sc_fd_gbp_rec.slack) THEN
            notificar := 'S';
			
			DELETE sc_factor_divisa_actual
			WHERE from_id_divisa = sc_fd_gbp_rec.from_id_divisa
				AND to_id_divisa = sc_fd_gbp_rec.to_id_divisa;
            
			INSERT INTO sc_factor_divisa_actual
                (id_factor_divisa, from_id_divisa, to_id_divisa,
                 factor, metodo_actualizacion,  spread_referencia,
                 spread_compra, carry, factor_compra, slack,
                 ultima_modificacion)
            VALUES
                (sc_factor_divisa_seq.NEXTVAL,
                 sc_fd_gbp_rec.from_id_divisa,
                 sc_fd_gbp_rec.to_id_divisa,
                 p_gbp_variacion_vta_spot_ask,
                 sc_fd_gbp_rec.metodo_actualizacion,
                 sc_fd_gbp_rec.spread_referencia,
                 sc_fd_gbp_rec.spread_compra,
                 sc_fd_gbp_rec.carry,
                 p_gbp_variacion_vta_spot,
                 sc_fd_gbp_rec.slack,
                 SYSDATE);
        END IF; -- Slack GBP

        ---------------------------------------------------------------------------
        -- YEN:
        ---------------------------------------------------------------------------
		
		OPEN c_sc_fd_x ('JPY');
        FETCH c_sc_fd_x INTO sc_fd_jpy_rec;
        IF (c_sc_fd_x%NOTFOUND) THEN
            RAISE_APPLICATION_ERROR(-20001,
                'Error NO se pudo leer el ultimo factor divisa de Yen.');
        END IF;
        CLOSE c_sc_fd_x;

        IF (ABS(p_jpy_variacion_vta_spot_ask -
                sc_fd_jpy_rec.factor) > sc_fd_jpy_rec.slack OR
            ABS(p_jpy_variacion_vta_spot -
                sc_fd_jpy_rec.factor_compra) > sc_fd_jpy_rec.slack) THEN
            notificar := 'S';
			
			DELETE sc_factor_divisa_actual
			WHERE from_id_divisa = sc_fd_jpy_rec.from_id_divisa
				AND to_id_divisa = sc_fd_jpy_rec.to_id_divisa;

            INSERT INTO sc_factor_divisa_actual
                (id_factor_divisa, from_id_divisa, to_id_divisa,
                 factor, metodo_actualizacion,  spread_referencia,
                 spread_compra, carry, factor_compra, slack,
                 ultima_modificacion)
            VALUES
                (sc_factor_divisa_seq.NEXTVAL,
                 sc_fd_jpy_rec.from_id_divisa,
                 sc_fd_jpy_rec.to_id_divisa,
                 1 / p_jpy_variacion_vta_spot_ask,
                 sc_fd_jpy_rec.metodo_actualizacion,
                 sc_fd_jpy_rec.spread_referencia,
                 sc_fd_jpy_rec.spread_compra,
                 sc_fd_jpy_rec.carry,
                 1 / p_jpy_variacion_vta_spot,
                 sc_fd_jpy_rec.slack,
                 SYSDATE);
        END IF; -- Slack JPY
    END IF; -- metodo actualizacion de factores

    COMMIT;

END;
/

show errors;
