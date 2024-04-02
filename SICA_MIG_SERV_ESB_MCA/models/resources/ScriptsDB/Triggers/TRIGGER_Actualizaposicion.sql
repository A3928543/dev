   /**
     *  $Id: TRIGGER_Actualizaposicion.sql,v 1.11 2008/02/22 18:25:45 ccovian Exp $
     *  Autor : Ricardo Legorreta H.
     *  Fecha: 27/02/2007
     *
     *  Trigger ActualizaPosicion
     *
   **/

   -- ----------------------------------------------------------

   CREATE OR REPLACE TRIGGER SICA_ADMIN.ActualizaPosicion
     AFTER INSERT ON sc_posicion_log
       FOR EACH ROW
   DECLARE
     not_found_flag       BOOLEAN;
     CURSOR c_sc_posicion IS
                    SELECT id_posicion
                      FROM sc_posicion
                    WHERE
                     (id_mesa_cambio = :NEW.id_mesa_cambio) AND (id_divisa = :NEW.id_divisa);
     sc_posicion_rec      c_sc_posicion%ROWTYPE;
     id_found_posicion    NUMBER;
   BEGIN
     -- ------------------------------------------------------
     -- Operacion: Compras de divisas de traspasos entre mesas
     -- ------------------------------------------------------
       IF (:NEW.tipo_operacion = 'CM') THEN
         -- traspasos entre mesas de compras CASH
         IF (:NEW.tipo_valor = 'CASH') THEN
           UPDATE sc_posicion_detalle
             SET compra_cash = compra_cash + :NEW.monto,
                 compra_in_cash = compra_in_cash + :NEW.monto,
                 compra_mn_cliente_cash = compra_mn_cliente_cash +
                                          :NEW.monto_mn,
                 compra_mn_pizarron_cash = compra_mn_pizarron_cash +
                                          :NEW.monto_mn,
                 compra_mn_mesa_cash = compra_mn_mesa_cash +
                                          :NEW.monto_mn
              WHERE
                (id_mesa_cambio = :NEW.id_mesa_cambio)        AND
                (id_divisa = :NEW.id_divisa)                  AND
                (id_canal = :NEW.id_canal)                    AND
                (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
         ELSE
         -- traspaso entre mesas de compras TOM
           IF (:NEW.tipo_valor = 'TOM') THEN
              UPDATE sc_posicion_detalle
                SET compra_tom = compra_tom + :NEW.monto,
                    compra_in_tom = compra_in_tom + :NEW.monto,
                    compra_mn_cliente_tom = compra_mn_cliente_tom +
                                            :NEW.monto_mn,
                    compra_mn_pizarron_tom = compra_mn_pizarron_tom +
                                             :NEW.monto_mn,
                    compra_mn_mesa_tom = compra_mn_mesa_tom +
                                         :NEW.monto_mn
              WHERE
                 (id_mesa_cambio = :NEW.id_mesa_cambio)       AND
                 (id_divisa = :NEW.id_divisa)                 AND
                 (id_canal = :NEW.id_canal)                   AND
                 (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
           ELSE
             -- operacion de compras SPOT
             IF (:NEW.tipo_valor = 'SPOT') THEN
               UPDATE sc_posicion_detalle
                SET compra_spot = compra_spot + :NEW.monto,
                    compra_in_spot = compra_in_spot +:NEW.monto,
                    compra_mn_cliente_spot = compra_mn_cliente_spot +
                                              :NEW.monto_mn,
                    compra_mn_pizarron_spot = compra_mn_pizarron_spot +
                                              :NEW.monto_mn,
                    compra_mn_mesa_spot = compra_mn_mesa_spot +
                                          :NEW.monto_mn
               WHERE
                   (id_mesa_cambio = :NEW.id_mesa_cambio)     AND
                   (id_divisa = :NEW.id_divisa)               AND
                   (id_canal = :NEW.id_canal)                 AND
                   (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
             ELSE
               -- operacion de compras 72Hr
               IF (:NEW.tipo_valor = '72HR') THEN
                 UPDATE sc_posicion_detalle
                  SET compra_72hr = compra_72hr + :NEW.monto,
                      compra_in_72hr = compra_in_72hr +:NEW.monto,
                      compra_mn_cliente_72hr = compra_mn_cliente_72hr +
                                                :NEW.monto_mn,
                      compra_mn_pizarron_72hr = compra_mn_pizarron_72hr +
                                              :NEW.monto_mn,
                      compra_mn_mesa_72hr = compra_mn_mesa_72hr +
                                            :NEW.monto_mn
                 WHERE
                     (id_mesa_cambio = :NEW.id_mesa_cambio)     AND
                     (id_divisa = :NEW.id_divisa)               AND
                     (id_canal = :NEW.id_canal)                 AND
                     (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
               ELSE
                 -- operacion de compras VFut
                 UPDATE sc_posicion_detalle
                  SET compra_vfut = compra_vfut + :NEW.monto,
                      compra_in_vfut = compra_in_vfut +:NEW.monto,
                      compra_mn_cliente_vfut = compra_mn_cliente_vfut +
                                                :NEW.monto_mn,
                      compra_mn_pizarron_vfut = compra_mn_pizarron_vfut +
                                              :NEW.monto_mn,
                      compra_mn_mesa_vfut = compra_mn_mesa_vfut +
                                            :NEW.monto_mn
                 WHERE
                     (id_mesa_cambio = :NEW.id_mesa_cambio)     AND
                     (id_divisa = :NEW.id_divisa)               AND
                     (id_canal = :NEW.id_canal)                 AND
                     (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
               END IF; -- traspaso compras 72HR
             END IF; -- traspaso compras SPOT
           END IF; -- traspaso compras TOM
         END IF;  -- traspasos compras CASH
       END IF; -- operacion == 'CM'

     -- ------------------------------------------------
     -- Operacion: Reconocimiento de utilidades en pesos.
     -- ------------------------------------------------
       IF (:NEW.tipo_operacion = 'PU') THEN
         -- operacion de reconocimientos de utilidades en pesos CASH
           IF (:NEW.tipo_valor = 'CASH') THEN
          UPDATE sc_posicion_detalle
             SET posicion_inicial_mn_spot = posicion_inicial_mn_spot + :NEW.monto_mn
          WHERE
                (id_mesa_cambio = :NEW.id_mesa_cambio)        AND
                (id_divisa = :NEW.id_divisa)                  AND
                (id_canal = :NEW.id_canal)                    AND
                (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
           ELSE
          -- operacion de compras TOM o SPOT. NO puede existir esta condicion
            RAISE_APPLICATION_ERROR(-20001,
                     'Error NO se pudo leer ni hacer UDPDATE en SC_POSICION ya que el tipo valor es TOM o SPOT.');
           END IF;  -- Reconocimiento de utilidades CASH
       END IF; -- operacion == 'PU'

     -- ------------------------------------------------------------------------
     -- Operacion: Reconocimiento de utilidades para Dolares. Las compra la Mesa
     --            de Operacion por eso no se modifica a posicion_inicial. Es
     --            decir es como un traspaso entre Mesas.
     -- ------------------------------------------------------------------------
       IF (:NEW.tipo_operacion = 'DU') THEN
         -- operacion de reconocimientos en pesos CASH
           IF (:NEW.tipo_valor = 'CASH') THEN
          UPDATE sc_posicion_detalle
			SET posicion_inicial_mn_spot = posicion_inicial_mn_spot + :NEW.monto_mn
              WHERE
                (id_mesa_cambio = :NEW.id_mesa_cambio)        AND
                (id_divisa = :NEW.id_divisa)                  AND
                (id_canal = :NEW.id_canal)                    AND
                (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
           ELSE
             -- Reconocimiento de perdida en Pesos TOM o SPOT. NO puede existir esta operacion
                RAISE_APPLICATION_ERROR(-20001,
                  'Error NO se pudo leer ni hacer UDPDATE en SC_POSICION ya que el tipo valor es TOM SPOT.');
           END IF; -- cash
       END IF; -- operacion : 'DU' Reconocimiento de utilidades en dolares

     -- ---------------------------------------------------------------
     -- Operacion: Compras y cancelaciones de divisas de deals normales
     -- ---------------------------------------------------------------
       IF (:NEW.tipo_operacion = 'CO' OR :NEW.tipo_operacion = 'CC') THEN
         -- Compras de divisas o cancelacion de compras CASH
           IF (:NEW.tipo_valor = 'CASH') THEN
             UPDATE sc_posicion_detalle
               SET compra_cash = compra_cash + :NEW.monto,
                   compra_mn_cliente_cash = compra_mn_cliente_cash +
                                            :NEW.monto_mn,
                   compra_mn_pizarron_cash = compra_mn_pizarron_cash +
                                             (:NEW.monto * :NEW.tipo_cambio_mesa)
             WHERE
               (id_mesa_cambio = :NEW.id_mesa_cambio)        AND
               (id_divisa = :NEW.id_divisa)                  AND
               (id_canal = :NEW.id_canal)                    AND
               (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
           ELSE
             -- Compras de divisas o cancelacion de compras TOM
               IF (:NEW.tipo_valor = 'TOM') THEN
                 UPDATE sc_posicion_detalle
                   SET compra_tom = compra_tom + :NEW.monto,
                       compra_mn_cliente_tom = compra_mn_cliente_tom +
                                               :NEW.monto_mn,
                       compra_mn_pizarron_tom = compra_mn_pizarron_tom +
                                               (:NEW.monto * :NEW.tipo_cambio_mesa)
                 WHERE
                   (id_mesa_cambio = :NEW.id_mesa_cambio)       AND
                   (id_divisa = :NEW.id_divisa)                 AND
                   (id_canal = :NEW.id_canal)                   AND
                   (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
               ELSE
                 -- Compras de divisas o cancelacion de compras SPOT
                 IF (:NEW.tipo_valor = 'SPOT') THEN
                     UPDATE sc_posicion_detalle
                       SET compra_spot = compra_spot + :NEW.monto,
                           compra_mn_cliente_spot = compra_mn_cliente_spot +
                                                    :NEW.monto_mn,
                           compra_mn_pizarron_spot = compra_mn_pizarron_spot +
                                                     (:NEW.monto * :NEW.tipo_cambio_mesa)
                     WHERE
                         (id_mesa_cambio = :NEW.id_mesa_cambio)     AND
                         (id_divisa = :NEW.id_divisa)               AND
                         (id_canal = :NEW.id_canal)                 AND
                         (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
                 ELSE
                    -- Compras de divisas o cancelacion de compras 72HR
                    IF (:NEW.tipo_valor = '72HR') THEN
                         UPDATE sc_posicion_detalle
                           SET compra_72hr = compra_72hr + :NEW.monto,
                               compra_mn_cliente_72hr = compra_mn_cliente_72hr +
                                                        :NEW.monto_mn,
                               compra_mn_pizarron_72hr = compra_mn_pizarron_72hr +
                                                         (:NEW.monto * :NEW.tipo_cambio_mesa)
                         WHERE
                             (id_mesa_cambio = :NEW.id_mesa_cambio)     AND
                             (id_divisa = :NEW.id_divisa)               AND
                             (id_canal = :NEW.id_canal)                 AND
                             (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
                    ELSE
                        -- Compras de divisas o cancelacion de compras VFUT
                         UPDATE sc_posicion_detalle
                           SET compra_vfut = compra_vfut + :NEW.monto,
                               compra_mn_cliente_vfut = compra_mn_cliente_vfut +
                                                        :NEW.monto_mn,
                               compra_mn_pizarron_vfut = compra_mn_pizarron_vfut +
                                                         (:NEW.monto * :NEW.tipo_cambio_mesa)
                         WHERE
                             (id_mesa_cambio = :NEW.id_mesa_cambio)     AND
                             (id_divisa = :NEW.id_divisa)               AND
                             (id_canal = :NEW.id_canal)                 AND
                             (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
                    END IF; -- Compras de divisas o cancelacion de compras 72HR
                 END IF; -- Compras de divisas o cancelacion de compras SPOT
               END IF; -- Compras de divisas o cancelacion de compras TOM
           END IF;  -- Compras de divisas o cancelacion de compras CASH
       END IF; -- operacion == 'CO' or operacion == CC

     -- ---------------------------------------------------------------------
     -- Operacion: Compras y cancelaciones de divisas de deals interbancarios
     -- ---------------------------------------------------------------------
       IF (:NEW.tipo_operacion = 'CI' OR :NEW.tipo_operacion = 'CN')  THEN
         -- Compras y cancelaciones de divisas de deals interbancarios CASH
           IF (:NEW.tipo_valor = 'CASH') THEN
             UPDATE sc_posicion_detalle
               SET compra_cash = compra_cash + :NEW.monto,
                   compra_in_cash = compra_in_cash + :NEW.monto,
                   compra_mn_cliente_cash = compra_mn_cliente_cash +
                                            :NEW.monto_mn,
                   compra_mn_pizarron_cash = compra_mn_pizarron_cash +
                                         :NEW.monto_mn,
                   compra_mn_mesa_cash = compra_mn_mesa_cash +
                                         :NEW.monto_mn
            WHERE
              (id_mesa_cambio = :NEW.id_mesa_cambio)        AND
              (id_divisa = :NEW.id_divisa)                  AND
              (id_canal = :NEW.id_canal)                    AND
              (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
           ELSE
             -- Compras y cancelaciones de divisas de deals interbancarios TOM
               IF (:NEW.tipo_valor = 'TOM') THEN
                 UPDATE sc_posicion_detalle
                   SET compra_tom = compra_tom + :NEW.monto,
                       compra_in_tom = compra_in_tom + :NEW.monto,
                       compra_mn_cliente_tom = compra_mn_cliente_tom +
                                               :NEW.monto_mn,
                    compra_mn_pizarron_tom = compra_mn_pizarron_tom +
                                                :NEW.monto_mn,
                       compra_mn_mesa_tom = compra_mn_mesa_tom +
                                            :NEW.monto_mn
              WHERE
                   (id_mesa_cambio = :NEW.id_mesa_cambio)       AND
                   (id_divisa = :NEW.id_divisa)                 AND
                (id_canal = :NEW.id_canal)                   AND
                   (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
               ELSE
                    -- Compras y cancelaciones de divisas de deals interbancarios SPOT
                    IF (:NEW.tipo_valor = 'SPOT') THEN
                       UPDATE sc_posicion_detalle
                         SET compra_spot = compra_spot + :NEW.monto,
                             compra_in_spot = compra_in_spot + :NEW.monto,
                             compra_mn_cliente_spot = compra_mn_cliente_spot +
                                                      :NEW.monto_mn,
                             compra_mn_pizarron_spot = compra_mn_pizarron_spot +
                                                       :NEW.monto_mn,
                             compra_mn_mesa_spot = compra_mn_mesa_spot +
                                                   :NEW.monto_mn
                       WHERE
                         (id_mesa_cambio = :NEW.id_mesa_cambio)     AND
                         (id_divisa = :NEW.id_divisa)               AND
                         (id_canal = :NEW.id_canal)                 AND
                         (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
                    ELSE
                        -- Compras y cancelaciones de divisas de deals interbancarios 72HR
                        IF (:NEW.tipo_valor = '72HR') THEN
                            UPDATE sc_posicion_detalle
                             SET compra_72hr = compra_72hr + :NEW.monto,
                                 compra_in_72hr = compra_in_72hr + :NEW.monto,
                                 compra_mn_cliente_72hr = compra_mn_cliente_72hr +
                                                          :NEW.monto_mn,
                                 compra_mn_pizarron_72hr = compra_mn_pizarron_72hr +
                                                           :NEW.monto_mn,
                                 compra_mn_mesa_72hr = compra_mn_mesa_72hr +
                                                       :NEW.monto_mn
                            WHERE
                             (id_mesa_cambio = :NEW.id_mesa_cambio)     AND
                             (id_divisa = :NEW.id_divisa)               AND
                             (id_canal = :NEW.id_canal)                 AND
                             (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
                        ELSE
                            -- Compras y cancelaciones de divisas de deals interbancarios VFUT
                            UPDATE sc_posicion_detalle
                             SET compra_vfut = compra_vfut + :NEW.monto,
                                 compra_in_vfut = compra_in_vfut + :NEW.monto,
                                 compra_mn_cliente_vfut = compra_mn_cliente_vfut +
                                                          :NEW.monto_mn,
                                 compra_mn_pizarron_vfut = compra_mn_pizarron_vfut +
                                                           :NEW.monto_mn,
                                 compra_mn_mesa_vfut = compra_mn_mesa_vfut +
                                                       :NEW.monto_mn
                            WHERE
                             (id_mesa_cambio = :NEW.id_mesa_cambio)     AND
                             (id_divisa = :NEW.id_divisa)               AND
                             (id_canal = :NEW.id_canal)                 AND
                             (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
                        END IF; -- Compras y cancelaciones de divisas de deals interbancarios 72HR
                    END IF; -- Compras y cancelaciones de divisas de deals interbancarios SPOT
               END IF; -- Compras y cancelaciones de divisas de deals interbancarios TOM
           END IF;  -- Compras y cancelaciones de divisas de deals interbancarios CASH
       END IF; -- operacion == 'CI' or operacion == 'CN'

     -- -----------------------------------------------------
     -- Operacion: Ventas de divisas de traspasos entre mesas
     -- -----------------------------------------------------
       IF (:NEW.tipo_operacion = 'VM')  THEN
         -- Ventas de divisas de traspasos entre mesas CASH
           IF (:NEW.tipo_valor = 'CASH') THEN
             UPDATE sc_posicion_detalle
            SET venta_cash = venta_cash + :NEW.monto,
                venta_in_cash = venta_in_cash + :NEW.monto,
                   venta_mn_cliente_cash = venta_mn_cliente_cash +
                                            :NEW.monto_mn,
                   venta_mn_pizarron_cash = venta_mn_pizarron_cash +
                                            :NEW.monto_mn,
                   venta_mn_mesa_cash = venta_mn_mesa_cash +
                                     :NEW.monto_mn
             WHERE
                (id_mesa_cambio = :NEW.id_mesa_cambio)          AND
                (id_divisa = :NEW.id_divisa)                    AND
                (id_canal = :NEW.id_canal)                      AND
                (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
           ELSE
             -- Ventas de divisas de traspasos entre mesas TOM
               IF (:NEW.tipo_valor = 'TOM') THEN
                 UPDATE sc_posicion_detalle
                   SET venta_tom = venta_tom + :NEW.monto,
                    venta_in_tom = venta_in_tom + :NEW.monto,
                       venta_mn_cliente_tom = venta_mn_cliente_tom +
                                               :NEW.monto_mn,
                       venta_mn_pizarron_tom = venta_mn_pizarron_tom +
                                               :NEW.monto_mn,
                       venta_mn_mesa_tom = venta_mn_mesa_tom +
                                           :NEW.monto_mn
                 WHERE
                   (id_mesa_cambio = :NEW.id_mesa_cambio)         AND
                (id_divisa = :NEW.id_divisa)                   AND
                   (id_canal = :NEW.id_canal)                     AND
                (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
               ELSE
                    -- Ventas de divisas de traspasos entre mesas SPOT
                    IF (:NEW.tipo_valor = 'SPOT') THEN
                        UPDATE sc_posicion_detalle
                         SET venta_spot = venta_spot + :NEW.monto,
                          venta_in_spot = venta_in_spot + :NEW.monto,
                             venta_mn_cliente_spot = venta_mn_cliente_spot +
                                                      :NEW.monto_mn,
                             venta_mn_pizarron_spot = venta_mn_pizarron_spot +
                                                      :NEW.monto_mn,
                             venta_mn_mesa_spot = venta_mn_mesa_spot +
                                                  :NEW.monto_mn
                        WHERE
                        (id_mesa_cambio = :NEW.id_mesa_cambio)         AND
                        (id_divisa = :NEW.id_divisa)                   AND
                        (id_canal = :NEW.id_canal)                     AND
                        (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
                    ELSE
                        -- Ventas de divisas de traspasos entre mesas 72HR
                        IF (:NEW.tipo_valor = '72HR') THEN
                            UPDATE sc_posicion_detalle
                             SET venta_72hr = venta_72hr + :NEW.monto,
                              venta_in_72hr = venta_in_72hr + :NEW.monto,
                                 venta_mn_cliente_72hr = venta_mn_cliente_72hr +
                                                          :NEW.monto_mn,
                                 venta_mn_pizarron_72hr = venta_mn_pizarron_72hr +
                                                          :NEW.monto_mn,
                                 venta_mn_mesa_72hr = venta_mn_mesa_72hr +
                                                      :NEW.monto_mn
                            WHERE
                            (id_mesa_cambio = :NEW.id_mesa_cambio)         AND
                            (id_divisa = :NEW.id_divisa)                   AND
                            (id_canal = :NEW.id_canal)                     AND
                            (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
                        ELSE
                            -- Ventas de divisas de traspasos entre mesas VFUT
                            UPDATE sc_posicion_detalle
                             SET venta_vfut = venta_vfut + :NEW.monto,
                              venta_in_vfut = venta_in_vfut + :NEW.monto,
                                 venta_mn_cliente_vfut = venta_mn_cliente_vfut +
                                                          :NEW.monto_mn,
                                 venta_mn_pizarron_vfut = venta_mn_pizarron_vfut +
                                                          :NEW.monto_mn,
                                 venta_mn_mesa_vfut = venta_mn_mesa_vfut +
                                                      :NEW.monto_mn
                            WHERE
                            (id_mesa_cambio = :NEW.id_mesa_cambio)         AND
                            (id_divisa = :NEW.id_divisa)                   AND
                            (id_canal = :NEW.id_canal)                     AND
                            (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
                        END IF; -- Ventas de divisas de traspasos entre mesas 72HR;
                    END IF; -- Ventas de divisas de traspasos entre mesas SPOT;
                END IF; -- Ventas de divisas de traspasos entre mesas TOM
           END IF; -- Ventas de divisas de traspasos entre mesas CASH
       END IF; -- operacion == 'VM'

     -- ----------------------------------------------
     -- Operacion: Reconocimiento de perdida en Pesos.
     -- ----------------------------------------------
       IF (:NEW.tipo_operacion = 'PP') THEN
          -- Reconocimiento de perdida en Pesos CASH
           IF (:NEW.tipo_valor = 'CASH') THEN
          UPDATE sc_posicion_detalle
                SET posicion_inicial_mn_spot = posicion_inicial_mn_spot - :NEW.monto_mn
          WHERE
               (id_mesa_cambio = :NEW.id_mesa_cambio)        AND
               (id_divisa = :NEW.id_divisa)                  AND
               (id_canal = :NEW.id_canal)                    AND
               (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
           ELSE
             -- Reconocimiento de perdida en Pesos TOM o SPOT. NO puede existir esta operacion
                RAISE_APPLICATION_ERROR(-20001,
                  'Error NO se pudo leer ni hacer UDPDATE en SC_POSICION ya que el tipo valor es TOM SPOT.');
           END IF;  -- Reconocimiento de perdida en Pesos CASH
       END IF; -- operacion == 'PP'

     -- ---------------------------------------------------------------------
     -- Operacion: Reconocimiento de perdidas para Dolares. Las vende la Mesa
     --            de Operacion por eso no se modifica a posicion_inicial. Es
     --            decir es como un traspaso entre Mesas.
     -- ---------------------------------------------------------------------
       IF (:NEW.tipo_operacion = 'DP') THEN
         -- Reconocimiento de perdidas para Dolares CASH
           IF (:NEW.tipo_valor = 'CASH') THEN
             UPDATE sc_posicion_detalle
               SET posicion_inicial_mn_spot = posicion_inicial_mn_spot - :NEW.monto_mn
          WHERE
               (id_mesa_cambio = :NEW.id_mesa_cambio)        AND
               (id_divisa = :NEW.id_divisa)                  AND
               (id_canal = :NEW.id_canal)                    AND
               (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
           ELSE
             -- Reconocimiento de perdida en Pesos TOM o SPOT. NO puede existir esta operacion
                RAISE_APPLICATION_ERROR(-20001,
                  'Error NO se pudo leer ni hacer UDPDATE en SC_POSICION ya que el tipo valor es TOM SPOT.');
           END IF; -- CASH
       END IF; -- operacion == 'DP'

     -- ---------------------------------------------------------------------
     -- Operacion: Ventas de divisas de deals normales y sus cancelaciones
     -- ---------------------------------------------------------------------
       IF (:NEW.tipo_operacion = 'VE' OR :NEW.tipo_operacion = 'VC')  THEN
         -- Ventas de divisas de deals normales y sus cancelaciones CASH
           IF (:NEW.tipo_valor = 'CASH') THEN
             UPDATE sc_posicion_detalle
               SET venta_cash = venta_cash + :NEW.monto,
                   venta_mn_cliente_cash = venta_mn_cliente_cash +
                                           :NEW.monto_mn,
                    venta_mn_pizarron_cash = venta_mn_pizarron_cash +
                                            (:NEW.monto * :NEW.tipo_cambio_mesa)
             WHERE
               (id_mesa_cambio = :NEW.id_mesa_cambio)          AND
               (id_divisa = :NEW.id_divisa)                    AND
               (id_canal = :NEW.id_canal)                      AND
               (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
           ELSE
             -- Ventas de divisas de deals normales y sus cancelaciones TOM
               IF (:NEW.tipo_valor = 'TOM') THEN
                 UPDATE sc_posicion_detalle
                   SET venta_tom = venta_tom + :NEW.monto,
                    venta_mn_cliente_tom = venta_mn_cliente_tom +
                                              :NEW.monto_mn,
                       venta_mn_pizarron_tom = venta_mn_pizarron_tom +
                                              (:NEW.monto * :NEW.tipo_cambio_mesa)
                WHERE
                   (id_mesa_cambio = :NEW.id_mesa_cambio)         AND
                   (id_divisa = :NEW.id_divisa)                   AND
                   (id_canal = :NEW.id_canal)                     AND
                   (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
               ELSE
                    -- Ventas de divisas de deals normales y sus cancelaciones SPOT
                    IF (:NEW.tipo_valor = 'SPOT') THEN
                        UPDATE sc_posicion_detalle
                        SET venta_spot = venta_spot + :NEW.monto,
                           venta_mn_cliente_spot = venta_mn_cliente_spot +
                                                   :NEW.monto_mn,
                           venta_mn_pizarron_spot = venta_mn_pizarron_spot +
                                                    (:NEW.monto * :NEW.tipo_cambio_mesa)
                        WHERE
                            (id_mesa_cambio = :NEW.id_mesa_cambio)         AND
                            (id_divisa = :NEW.id_divisa)                   AND
                            (id_canal = :NEW.id_canal)                     AND
                            (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
                    ELSE
                        -- Ventas de divisas de deals normales y sus cancelaciones 72HR
                        IF (:NEW.tipo_valor = '72HR') THEN
                            UPDATE sc_posicion_detalle
                            SET venta_72hr = venta_72hr + :NEW.monto,
                               venta_mn_cliente_72hr = venta_mn_cliente_72hr +
                                                       :NEW.monto_mn,
                               venta_mn_pizarron_72hr = venta_mn_pizarron_72hr +
                                                        (:NEW.monto * :NEW.tipo_cambio_mesa)
                            WHERE
                                (id_mesa_cambio = :NEW.id_mesa_cambio)         AND
                                (id_divisa = :NEW.id_divisa)                   AND
                                (id_canal = :NEW.id_canal)                     AND
                                (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
                        ELSE
                            -- Ventas de divisas de deals normales y sus cancelaciones VFUT
                            UPDATE sc_posicion_detalle
                            SET venta_vfut = venta_vfut + :NEW.monto,
                               venta_mn_cliente_vfut = venta_mn_cliente_vfut +
                                                       :NEW.monto_mn,
                               venta_mn_pizarron_vfut = venta_mn_pizarron_vfut +
                                                        (:NEW.monto * :NEW.tipo_cambio_mesa)
                            WHERE
                                (id_mesa_cambio = :NEW.id_mesa_cambio)         AND
                                (id_divisa = :NEW.id_divisa)                   AND
                                (id_canal = :NEW.id_canal)                     AND
                                (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
                        END IF; -- Ventas de divisas de deals normales y sus cancelaciones 72HR
                    END IF; -- Ventas de divisas de deals normales y sus cancelaciones SPOT
               END IF; -- Ventas de divisas de deals normales y sus cancelaciones TOM
           END IF; -- Ventas de divisas de deals normales y sus cancelaciones CASH
       END IF; -- operacion == 'VE'

     -- ------------------------------------------------------------------------
     -- Operacion: Ventas de divisas de deals interbancarios y sus cancelaciones
     -- ------------------------------------------------------------------------
       IF (:NEW.tipo_operacion = 'VI' OR :NEW.tipo_operacion = 'VN')  THEN
         -- Ventas de divisas de deals interbancarios y sus cancelaciones CASH
           IF (:NEW.tipo_valor = 'CASH') THEN
             UPDATE sc_posicion_detalle
               SET venta_cash = venta_cash + :NEW.monto,
                   venta_in_cash = venta_in_cash + :NEW.monto,
                venta_mn_cliente_cash = venta_mn_cliente_cash +
                                           :NEW.monto_mn,
                   venta_mn_pizarron_cash = venta_mn_pizarron_cash +
                                            :NEW.monto_mn,
                    venta_mn_mesa_cash = venta_mn_mesa_cash +
                                         :NEW.monto_mn
             WHERE
               (id_mesa_cambio = :NEW.id_mesa_cambio)          AND
               (id_divisa = :NEW.id_divisa)                    AND
               (id_canal = :NEW.id_canal)                      AND
               (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
           ELSE
             -- Ventas de divisas de deals interbancarios y sus cancelaciones TOM
               IF (:NEW.tipo_valor = 'TOM') THEN
                 UPDATE sc_posicion_detalle
                   SET venta_tom = venta_tom + :NEW.monto,
                       venta_in_tom = venta_in_tom + :NEW.monto,
                    venta_mn_cliente_tom = venta_mn_cliente_tom +
                                              :NEW.monto_mn,
                       venta_mn_pizarron_tom = venta_mn_pizarron_tom +
                                               :NEW.monto_mn,
                       venta_mn_mesa_tom = venta_mn_mesa_tom +
                                           :NEW.monto_mn
                 WHERE
                   (id_mesa_cambio = :NEW.id_mesa_cambio)         AND
                   (id_divisa = :NEW.id_divisa)                   AND
                   (id_canal = :NEW.id_canal)                     AND
                   (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
               ELSE
                    -- Ventas de divisas de deals interbancarios y sus cancelaciones SPOT
                    IF (:NEW.tipo_valor = 'SPOT') THEN
                        UPDATE sc_posicion_detalle
                        SET venta_spot = venta_spot + :NEW.monto,
                            venta_in_spot = venta_in_spot + :NEW.monto,
                            venta_mn_cliente_spot = venta_mn_cliente_spot +
                                                    :NEW.monto_mn,
                            venta_mn_pizarron_spot = venta_mn_pizarron_spot +
                                                     :NEW.monto_mn,
                            venta_mn_mesa_spot = venta_mn_mesa_spot +
                                                 :NEW.monto_mn
                        WHERE
                            (id_mesa_cambio = :NEW.id_mesa_cambio)         AND
                            (id_divisa = :NEW.id_divisa)                   AND
                            (id_canal = :NEW.id_canal)                     AND
                            (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
                    ELSE
                        -- Ventas de divisas de deals interbancarios y sus cancelaciones 72HR
                        IF (:NEW.tipo_valor = '72HR') THEN
                            UPDATE sc_posicion_detalle
                            SET venta_72hr = venta_72hr + :NEW.monto,
                                venta_in_72hr = venta_in_72hr + :NEW.monto,
                                venta_mn_cliente_72hr = venta_mn_cliente_72hr +
                                                        :NEW.monto_mn,
                                venta_mn_pizarron_72hr = venta_mn_pizarron_72hr +
                                                         :NEW.monto_mn,
                                venta_mn_mesa_72hr = venta_mn_mesa_72hr +
                                                     :NEW.monto_mn
                            WHERE
                                (id_mesa_cambio = :NEW.id_mesa_cambio)         AND
                                (id_divisa = :NEW.id_divisa)                   AND
                                (id_canal = :NEW.id_canal)                     AND
                                (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
                        ELSE
                            -- Ventas de divisas de deals interbancarios y sus cancelaciones VFUT
                            UPDATE sc_posicion_detalle
                            SET venta_vfut = venta_vfut + :NEW.monto,
                                venta_in_vfut = venta_in_vfut + :NEW.monto,
                                venta_mn_cliente_vfut = venta_mn_cliente_vfut +
                                                        :NEW.monto_mn,
                                venta_mn_pizarron_vfut = venta_mn_pizarron_vfut +
                                                         :NEW.monto_mn,
                                venta_mn_mesa_vfut = venta_mn_mesa_vfut +
                                                     :NEW.monto_mn
                            WHERE
                                (id_mesa_cambio = :NEW.id_mesa_cambio)         AND
                                (id_divisa = :NEW.id_divisa)                   AND
                                (id_canal = :NEW.id_canal)                     AND
                                (clave_forma_liquidacion = :NEW.clave_forma_liquidacion);
                        END IF; -- Ventas de divisas de deals interbancarios y sus cancelaciones 72HR
                    END IF; -- Ventas de divisas de deals interbancarios y sus cancelaciones SPOT
               END IF; -- Ventas de divisas de deals interbancarios y sus cancelaciones TOM
           END IF; -- Ventas de divisas de deals interbancarios y sus cancelaciones CASH
       END IF; -- operacion == 'VI' or operacion == 'VN'

     -- ------------------------------------------------------------------------
     -- Para que se inserte el registro se se valida si el registro no existia
     -- previamente en cuyo caso se inserta un nuevo registro del detalle de la
     -- posicion
     not_found_flag := SQL%NOTFOUND;
     -- Previo a insertar sc_posicion_detalle se checa si existe papa en sc_posicion.
     -- Sin no existe se crea un id_posicion y en caso contario se trae el id_posicion
     -- nota: solo se consideran operaciones de insercion a los deals normales e
     --       interbancario. Para los casos se reconocimiento de utildades y cancelaciones
     --       se pre-supone que siempres ya existan los registros. Que de hecho es
     --       como funciona la operacion.
     -- ------------------------------------------------------------------------
     IF not_found_flag THEN
       id_found_posicion := -1;
       OPEN c_sc_posicion;
       LOOP
         FETCH c_sc_posicion INTO sc_posicion_rec;
         EXIT WHEN c_sc_posicion%NOTFOUND;
         id_found_posicion := sc_posicion_rec.id_posicion;
       END LOOP;
       CLOSE c_sc_posicion;
       IF (id_found_posicion = -1) THEN
         -- se requiere insertar el registro de sc_posicion
         INSERT INTO sc_posicion (
             id_posicion,
             id_mesa_cambio, id_divisa,
             posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
             posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
             compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
             compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
             compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
             compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
             compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
             venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
             venta_in_cash, venta_in_tom , venta_in_spot, venta_in_72hr, venta_in_vfut,
             venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
             venta_mn_pizarron_cash, venta_mn_pizarron_tom,venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
             venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut,
             utilidad_global, utilidad_mesa)
           VALUES
                (sc_posicion_seq.nextval,
                 :NEW.id_mesa_cambio,:NEW.id_divisa,
                 0,0,0,0,0,
				 0,0,0,0,0,
                 0,0,0,0,0,
                 0,0,0,0,0,
                 0,0,0,0,0,
                 0,0,0,0,0,
                 0,0,0,0,0,
                 0,0,0,0,0,
                 0,0,0,0,0,
                 0,0,0,0,0,
                 0,0,0,0,0,
                 0,0,0,0,0,
                 0,0);
          -- se pregunta por el id_posicion que fue recien generado
         OPEN c_sc_posicion;
         LOOP
           FETCH c_sc_posicion INTO sc_posicion_rec;
           id_found_posicion := sc_posicion_rec.id_posicion;
           EXIT WHEN c_sc_posicion%NOTFOUND;
         END LOOP;
         CLOSE c_sc_posicion;
       END IF; -- found posicion = -1
     END IF; -- not_found_flag
     -- ya teniendo el id_posicion se pasa a insertar sc_posicion_detalle
     IF not_found_flag THEN
       -- ---------------------------------------------------------------------------
       -- Operacion: Insercion con el tipo de operacion de compras de deals sencillos
       -- ---------------------------------------------------------------------------
       IF (:NEW.tipo_operacion = 'CO') THEN
         -- Operacion de compras CASH
         IF (:NEW.tipo_valor = 'CASH') THEN
           INSERT INTO sc_posicion_detalle (
               id_posicion_detalle, id_posicion,
               id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
               posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
               posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
               compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
               compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
               compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
               compra_mn_pizarron_cash,compra_mn_pizarron_tom,compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
               compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
               venta_cash, venta_tom, venta_spot, venta_72hr, venta_vfut,
               venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
               venta_mn_cliente_cash, venta_mn_cliente_tom,venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
               venta_mn_pizarron_cash, venta_mn_pizarron_tom,venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
               venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
           VALUES
             (sc_posicion_detalle_seq.nextval, id_found_posicion,
              :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
              :NEW.clave_forma_liquidacion,
              0,0,0,0,0,
			  0,0,0,0,0,
              :NEW.monto,0,0,0,0,
              0,0,0,0,0,
              :NEW.monto_mn, 0, 0,0,0,
              (:NEW.monto * :NEW.tipo_cambio_mesa),0,0,0,0,
              0,0,0,0,0,
              0,0,0,0,0,
              0,0,0,0,0,
              0,0,0,0,0,
              0,0,0,0,0,
              0,0,0,0,0);
         ELSE
           -- Operacion de compras deals normales TOM
           IF (:NEW.tipo_valor = 'TOM') THEN
             INSERT INTO sc_posicion_detalle (
                 id_posicion_detalle, id_posicion,
                 id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                 posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                 posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                 compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                 compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                 compra_mn_cliente_cash, compra_mn_cliente_tom,compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                 compra_mn_pizarron_cash, compra_mn_pizarron_tom,compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                 compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                 venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                 venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                 venta_mn_cliente_cash, venta_mn_cliente_tom,venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                 venta_mn_pizarron_cash, venta_mn_pizarron_tom,venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                 venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
             VALUES
               (sc_posicion_detalle_seq.nextval, id_found_posicion,
                :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
                :NEW.clave_forma_liquidacion,
                0,0,0,0,0,
				0,0,0,0,0,
                0,:NEW.monto,0,0,0,
                0,0,0,0,0,
                0,:NEW.monto_mn,0,0,0,
                0,(:NEW.monto * :NEW.tipo_cambio_mesa),0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0);
           ELSE
             -- Operacion de compras de deals normales SPOT
                IF (:NEW.tipo_valor = 'SPOT') THEN
                    INSERT INTO sc_posicion_detalle (
                        id_posicion_detalle, id_posicion,
                        id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                        posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                        posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                        compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                        compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                        compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                        compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                        compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                        venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                        venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                        venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                        venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                        venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
                    VALUES
                        (sc_posicion_detalle_seq.nextval, id_found_posicion,
                        :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
                        :NEW.clave_forma_liquidacion,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,:NEW.monto,0,0,
                        0,0,0,0,0,
                        0,0,:NEW.monto_mn,0,0,
                        0,0,(:NEW.monto * :NEW.tipo_cambio_mesa),0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0);
                ELSE
                    -- Operacion de compras de deals normales 72HR
                    IF (:NEW.tipo_valor = '72HR') THEN
                        INSERT INTO sc_posicion_detalle (
                            id_posicion_detalle, id_posicion,
                            id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                            posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                            posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                            compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                            compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                            compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                            compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                            compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                            venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                            venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                            venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                            venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                            venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
                        VALUES
                            (sc_posicion_detalle_seq.nextval, id_found_posicion,
                            :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
                            :NEW.clave_forma_liquidacion,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,:NEW.monto,0,
                            0,0,0,0,0,
                            0,0,0,:NEW.monto_mn,0,
                            0,0,0,(:NEW.monto * :NEW.tipo_cambio_mesa),0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0);
                    ELSE
                        -- Operacion de compras de deals normales VFUT
                        INSERT INTO sc_posicion_detalle (
                            id_posicion_detalle, id_posicion,
                            id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                            posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                            posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                            compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                            compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                            compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                            compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                            compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                            venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                            venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                            venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                            venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                            venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
                        VALUES
                            (sc_posicion_detalle_seq.nextval, id_found_posicion,
                            :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
                            :NEW.clave_forma_liquidacion,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,:NEW.monto,
                            0,0,0,0,0,
                            0,0,0,0,:NEW.monto_mn,
                            0,0,0,0,(:NEW.monto * :NEW.tipo_cambio_mesa),
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0);
                    END IF; -- 72HR
                END IF; -- SPOT
           END IF; -- TOM
         END IF; -- CASH
       END IF; -- operacion = 'CO'

       -- ---------------------------------------------------------------------------
       -- Se valida que el tipo de operacion de reconocimiento de utilidades nunca
       -- se necesita insertar un registro. Lo mismo para cancelaciones
       IF (:NEW.tipo_operacion = 'PU' OR :NEW.tipo_operacion = 'PP' OR
        :NEW.tipo_operacion = 'DU' OR :NEW.tipo_operacion = 'DP' OR
        :NEW.tipo_operacion = 'CC' OR :NEW.tipo_operacion = 'CN' OR
        :NEW.tipo_operacion = 'VC' OR :NEW.tipo_operacion = 'VN') THEN
        RAISE_APPLICATION_ERROR(-20001, 'Error NO se puede leer ni hacer INSERT en SC_POSICION: Canal=' || :NEW.id_canal || ', Mesa=' || 
                                        :NEW.id_mesa_cambio || ', Divisa=' || :NEW.id_divisa || ', Producto=' || :NEW.clave_forma_liquidacion);
       END IF; -- operacion = 'PU'

       -- --------------------------------------------------------------------------------
       -- Operacion: Insercion con el tipo de operacion de compras de deals interbancarios
       --            o traspaso entre Mesas.
       -- --------------------------------------------------------------------------------
       IF (:NEW.tipo_operacion = 'CI' OR :NEW.tipo_operacion = 'CM') THEN
         -- Operacion de insercion compras CASH deals interbancarios o traspasos CASH
         IF (:NEW.tipo_valor = 'CASH') THEN
           INSERT INTO sc_posicion_detalle (
                id_posicion_detalle, id_posicion,
                id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
           VALUES
             (sc_posicion_detalle_seq.nextval, id_found_posicion,
              :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
              :NEW.clave_forma_liquidacion,
              0,0,0,0,0,
			  0,0,0,0,0,
              :NEW.monto,0,0,0,0,
              :NEW.monto,0,0,0,0,
              :NEW.monto_mn,0,0,0,0,
              :NEW.monto_mn,0,0,0,0,
              :NEW.monto_mn,0,0,0,0,
              0,0,0,0,0,
              0,0,0,0,0,
              0,0,0,0,0,
              0,0,0,0,0,
              0,0,0,0,0);
         ELSE
           -- Operacion de insercion compras CASH deals interbancarios o traspasos TOM
           IF (:NEW.tipo_valor = 'TOM') THEN
             INSERT INTO sc_posicion_detalle (
                id_posicion_detalle, id_posicion,
                id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
             VALUES
               (sc_posicion_detalle_seq.nextval, id_found_posicion,
                :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
                :NEW.clave_forma_liquidacion,
                0,0,0,0,0,
				0,0,0,0,0,
                0,:NEW.monto,0,0,0,
                0,:NEW.monto,0,0,0,
                0,:NEW.monto_mn,0,0,0,
                0,:NEW.monto_mn,0,0,0,
                0,:NEW.monto_mn,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0);
           ELSE
                -- Operacion de insercion compras CASH deals interbancarios o traspasos SPOT
                IF (:NEW.tipo_valor = 'SPOT') THEN
                    INSERT INTO sc_posicion_detalle (
                        id_posicion_detalle, id_posicion,
                        id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                        posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                        posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                        compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                        compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                        compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                        compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                        compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                        venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                        venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                        venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                        venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                        venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
                    VALUES
                        (sc_posicion_detalle_seq.nextval, id_found_posicion,
                        :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
                        :NEW.clave_forma_liquidacion,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,:NEW.monto,0,0,
                        0,0,:NEW.monto,0,0,
                        0,0,:NEW.monto_mn,0,0,
                        0,0,:NEW.monto_mn,0,0,
                        0,0,:NEW.monto_mn,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0);
                ELSE
                    -- Operacion de insercion compras 72HR deals interbancarios o traspasos 72HR
                    IF (:NEW.tipo_valor = '72HR') THEN
                        INSERT INTO sc_posicion_detalle (
                            id_posicion_detalle, id_posicion,
                            id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                            posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                            posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                            compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                            compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                            compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                            compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                            compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                            venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                            venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                            venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                            venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                            venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
                        VALUES
                            (sc_posicion_detalle_seq.nextval, id_found_posicion,
                            :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
                            :NEW.clave_forma_liquidacion,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,:NEW.monto,0,
                            0,0,0,:NEW.monto,0,
                            0,0,0,:NEW.monto_mn,0,
                            0,0,0,:NEW.monto_mn,0,
                            0,0,0,:NEW.monto_mn,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0);
                    ELSE
                        INSERT INTO sc_posicion_detalle (
                            id_posicion_detalle, id_posicion,
                            id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                            posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                            posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                            compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                            compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                            compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                            compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                            compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                            venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                            venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                            venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                            venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                            venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
                        VALUES
                            (sc_posicion_detalle_seq.nextval, id_found_posicion,
                            :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
                            :NEW.clave_forma_liquidacion,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,:NEW.monto,
                            0,0,0,0,:NEW.monto,
                            0,0,0,0,:NEW.monto_mn,
                            0,0,0,0,:NEW.monto_mn,
                            0,0,0,0,:NEW.monto_mn,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0);
                    END IF; -- 72HR
                END IF; -- SPOT
           END IF; -- TOM
         END IF; -- CASH
       END IF; -- operacion = 'CI' or operacion ==  'CM'

       -- --------------------------------------------------------------------------------
       -- Operacion: Insercion con el tipo de operacion de ventas de deals normales
       -- --------------------------------------------------------------------------------
       IF (:NEW.tipo_operacion = 'VE') THEN
         -- Operacion de insercion ventas CASH deals normales
         IF (:NEW.tipo_valor = 'CASH') THEN
           INSERT INTO sc_posicion_detalle (
                id_posicion_detalle, id_posicion,
                id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
           VALUES
             (sc_posicion_detalle_seq.nextval, id_found_posicion,
              :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
              :NEW.clave_forma_liquidacion,
              0,0,0,0,0,
			  0,0,0,0,0,
              0,0,0,0,0,
              0,0,0,0,0,
              0,0,0,0,0,
              0,0,0,0,0,
              0,0,0,0,0,
              :NEW.monto,0,0,0,0,
              0,0,0,0,0,
              :NEW.monto_mn,0,0,0,0,
              (:NEW.monto * :NEW.tipo_cambio_mesa),0,0,0,0,
              0,0,0,0,0);
         ELSE
           -- Operacion de insercion ventas TOM deals normales
           IF (:NEW.tipo_valor = 'TOM') THEN
             INSERT INTO sc_posicion_detalle (
                id_posicion_detalle, id_posicion,
                id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
             VALUES
               (sc_posicion_detalle_seq.nextval, id_found_posicion,
                :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
                :NEW.clave_forma_liquidacion,
                0,0,0,0,0,
				0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,:NEW.monto,0,0,0,
                0,0,0,0,0,
                0,:NEW.monto_mn,0,0,0,
                0,(:NEW.monto * :NEW.tipo_cambio_mesa),0,0,0,
                0,0,0,0,0);
           ELSE
             -- Operacion de insercion ventas SPOT deals normales
                IF (:NEW.tipo_valor = 'SPOT') THEN
                    INSERT INTO sc_posicion_detalle (
                        id_posicion_detalle, id_posicion,
                        id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                        posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                        posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                        compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                        compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                        compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                        compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                        compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                        venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                        venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                        venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                        venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                        venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
                    VALUES
                        (sc_posicion_detalle_seq.nextval, id_found_posicion,
                        :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
                        :NEW.clave_forma_liquidacion,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,:NEW.monto,0,0,
                        0,0,0,0,0,
                        0,0,:NEW.monto_mn,0,0,
                        0,0,(:NEW.monto * :NEW.tipo_cambio_mesa),0,0,
                        0,0,0,0,0);
                ELSE
                    -- Operacion de insercion ventas 72HR deals normales
                    IF (:NEW.tipo_valor = '72HR') THEN
                        INSERT INTO sc_posicion_detalle (
                            id_posicion_detalle, id_posicion,
                            id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                            posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                            posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                            compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                            compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                            compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                            compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                            compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                            venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                            venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                            venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                            venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                            venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
                        VALUES
                            (sc_posicion_detalle_seq.nextval, id_found_posicion,
                            :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
                            :NEW.clave_forma_liquidacion,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,:NEW.monto,0,
                            0,0,0,0,0,
                            0,0,0,:NEW.monto_mn,0,
                            0,0,0,(:NEW.monto * :NEW.tipo_cambio_mesa),0,
                            0,0,0,0,0);
                    ELSE
                        -- Operacion de insercion ventas VFUT deals normales
                        INSERT INTO sc_posicion_detalle (
                            id_posicion_detalle, id_posicion,
                            id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                            posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                            posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                            compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                            compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                            compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                            compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                            compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                            venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                            venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                            venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                            venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                            venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
                        VALUES
                            (sc_posicion_detalle_seq.nextval, id_found_posicion,
                            :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
                            :NEW.clave_forma_liquidacion,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,:NEW.monto,
                            0,0,0,0,0,
                            0,0,0,0,:NEW.monto_mn,
                            0,0,0,0,(:NEW.monto * :NEW.tipo_cambio_mesa),
                            0,0,0,0,0);
                    END IF; -- 72HR
                END IF; -- SPOT
           END IF; -- TOM
         END IF; -- CASH
       END IF; -- tipo_operacion = 'VE'

       -- --------------------------------------------------------------------------------
       -- Operacion: Insercion con el tipo de operacion de ventas de deals interbancarios
       --            o traspaso entre Mesas.
       -- --------------------------------------------------------------------------------
       IF (:NEW.tipo_operacion = 'VI' OR :NEW.tipo_operacion = 'VM') THEN
         -- Operacion de insercion de ventas CASH deals interbancarios o traspasos CASH
         IF (:NEW.tipo_valor = 'CASH') THEN
           INSERT INTO sc_posicion_detalle (
                id_posicion_detalle, id_posicion,
                id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
           VALUES
             (sc_posicion_detalle_seq.nextval, id_found_posicion,
              :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
              :NEW.clave_forma_liquidacion,
              0,0,0,0,0,
			  0,0,0,0,0,
              0,0,0,0,0,
              0,0,0,0,0,
              0,0,0,0,0,
              0,0,0,0,0,
              0,0,0,0,0,
              :NEW.monto,0,0,0,0,
              :NEW.monto,0,0,0,0,
              :NEW.monto_mn,0,0,0,0,
              :NEW.monto_mn,0,0,0,0,
              :NEW.monto_mn,0,0,0,0);
         ELSE
            -- Operacion de insercion de ventas CASH deals interbancarios o traspasos TOM
           IF (:NEW.tipo_valor = 'TOM') THEN
             INSERT INTO sc_posicion_detalle (
                id_posicion_detalle, id_posicion,
                id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
             VALUES
               (sc_posicion_detalle_seq.nextval, id_found_posicion,
                :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
                :NEW.clave_forma_liquidacion,
                0,0,0,0,0,
				0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,:NEW.monto,0,0,0,
                0,:NEW.monto,0,0,0,
                0,:NEW.monto_mn,0,0,0,
                0,:NEW.monto_mn,0,0,0,
                0,:NEW.monto_mn,0,0,0);
           ELSE
          -- Operacion de insercion de ventas CASH deals interbancarios o traspasos SPOT
                IF (:NEW.tipo_valor = 'SPOT') THEN
                    INSERT INTO sc_posicion_detalle (
                        id_posicion_detalle, id_posicion,
                        id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                        posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                        posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                        compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                        compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                        compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                        compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                        compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                        venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                        venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                        venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                        venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                        venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
                    VALUES
                        (sc_posicion_detalle_seq.nextval, id_found_posicion,
                        :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
                        :NEW.clave_forma_liquidacion,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,:NEW.monto,0,0,
                        0,0,:NEW.monto,0,0,
                        0,0,:NEW.monto_mn,0,0,
                        0,0,:NEW.monto_mn,0,0,
                        0,0,:NEW.monto_mn,0,0);
                ELSE
                    -- Operacion de insercion de ventas 72HR deals interbancarios o traspasos 72HR
                    IF (:NEW.tipo_valor = '72HR') THEN
                        INSERT INTO sc_posicion_detalle (
                            id_posicion_detalle, id_posicion,
                            id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                            posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                            posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                            compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                            compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                            compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                            compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                            compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                            venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                            venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                            venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                            venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                            venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
                        VALUES
                            (sc_posicion_detalle_seq.nextval, id_found_posicion,
                            :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
                            :NEW.clave_forma_liquidacion,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,:NEW.monto,0,
                            0,0,0,:NEW.monto,0,
                            0,0,0,:NEW.monto_mn,0,
                            0,0,0,:NEW.monto_mn,0,
                            0,0,0,:NEW.monto_mn,0);
                    ELSE
                        -- Operacion de insercion de ventas VFUT deals interbancarios o traspasos VFUT
                        INSERT INTO sc_posicion_detalle (
                            id_posicion_detalle, id_posicion,
                            id_mesa_cambio, id_divisa, id_canal, clave_forma_liquidacion,
                            posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
                            posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                            compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                            compra_in_cash, compra_in_tom, compra_in_spot, compra_in_72hr, compra_in_vfut,
                            compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                            compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                            compra_mn_mesa_cash, compra_mn_mesa_tom, compra_mn_mesa_spot, compra_mn_mesa_72hr, compra_mn_mesa_vfut,
                            venta_cash, venta_tom ,venta_spot, venta_72hr, venta_vfut,
                            venta_in_cash, venta_in_tom, venta_in_spot, venta_in_72hr, venta_in_vfut,
                            venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut,
                            venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                            venta_mn_mesa_cash, venta_mn_mesa_tom, venta_mn_mesa_spot, venta_mn_mesa_72hr, venta_mn_mesa_vfut)
                        VALUES
                            (sc_posicion_detalle_seq.nextval, id_found_posicion,
                            :NEW.id_mesa_cambio,:NEW.id_divisa,:NEW.id_canal,
                            :NEW.clave_forma_liquidacion,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,0,
                            0,0,0,0,:NEW.monto,
                            0,0,0,0,:NEW.monto,
                            0,0,0,0,:NEW.monto_mn,
                            0,0,0,0,:NEW.monto_mn,
                            0,0,0,0,:NEW.monto_mn);
                    END IF; -- 72HR
                END IF; -- SPOT
           END IF; -- TOM
         END IF; -- CASH
       END IF; -- tipo_operacion = 'VI' or tipo_operacion == 'VM'
     END IF; -- if not_found
   END ActualizaPosicion;
/

   -- ----------------------------------------------------------
   /**
     *  Autor : Ricardo Legorreta H.
     *  Fecha: 05/07/2006
     *
     *  Trigger ActualizaTotalPosicion
     *
   **/
   -- ----------------------------------------------------------

   CREATE OR REPLACE TRIGGER SICA_ADMIN.ActualizaTotalPosicion
     AFTER UPDATE ON sc_posicion_detalle
     FOR EACH ROW
   DECLARE
     newUtilidadGlobal   NUMBER;
     newUtilidadMesa     NUMBER;
     compra              NUMBER;
     venta               NUMBER;
     compraMN            NUMBER;
     ventaMN             NUMBER;
     difTasa             NUMBER;
	 posicionInicial      NUMBER;
	 posicionInicialMN   NUMBER;
     CURSOR c_sc_posicion IS
                    SELECT  posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
					        posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                            compra_cash, compra_tom, compra_spot, compra_72hr, compra_vfut,
                            venta_cash, venta_tom, venta_spot, venta_72hr, venta_vfut,
                            compra_mn_pizarron_cash, compra_mn_pizarron_tom, compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                            venta_mn_pizarron_cash, venta_mn_pizarron_tom, venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
							compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                            venta_mn_cliente_cash, venta_mn_cliente_tom, venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut
                      FROM sc_posicion
                      WHERE
                       (id_mesa_cambio = :OLD.id_mesa_cambio) AND
                       (id_divisa = :OLD.id_divisa);
     sc_posicion_rec   c_sc_posicion%ROWTYPE;
   BEGIN
     -- --------------------------------------------------------------
     -- Se mofifica el total de la posicion de acuerdo al detalle
     -- SC_POSICION_DETALLE que fue afectado en el trigger de
     -- ActualizaPosicion
     -- previamente se recalcula la utilidad realizada en los atributos
     -- de utilidad
     -- se pregunta por sc_posicion que se va actualizar
     OPEN c_sc_posicion;
       FETCH c_sc_posicion INTO sc_posicion_rec;
       IF c_sc_posicion%NOTFOUND THEN
         CLOSE c_sc_posicion;
         RAISE_APPLICATION_ERROR(-20001,
              'Error NO se pudo leer ni hacer UDPDATE en SC_POSICION');
       END IF;
     CLOSE c_sc_posicion;
     -- --------------------------------------------------------------
     -- Se calcula la utilidad realizada de la Mesa de Cambios
     -- --------------------------------------------------------------
	 posicionInicial := sc_posicion_rec.posicion_inicial_cash + :NEW.posicion_inicial_cash - :OLD.posicion_inicial_cash  +
	                    sc_posicion_rec.posicion_inicial_tom  + :NEW.posicion_inicial_tom - :OLD.posicion_inicial_tom +
                        sc_posicion_rec.posicion_inicial_spot + :NEW.posicion_inicial_spot - :OLD.posicion_inicial_spot +
                        sc_posicion_rec.posicion_inicial_72hr + :NEW.posicion_inicial_72hr - :OLD.posicion_inicial_72hr +
                        sc_posicion_rec.posicion_inicial_vfut + :NEW.posicion_inicial_vfut - :OLD.posicion_inicial_vfut;
     compra := sc_posicion_rec.compra_cash + :NEW.compra_cash - :OLD.compra_cash +
			   sc_posicion_rec.compra_tom + :NEW.compra_tom - :OLD.compra_tom    +
			   sc_posicion_rec.compra_spot + :NEW.compra_spot - :OLD.compra_spot +
			   sc_posicion_rec.compra_72hr + :NEW.compra_72hr - :OLD.compra_72hr +
			   sc_posicion_rec.compra_vfut + :NEW.compra_vfut - :OLD.compra_vfut;
	 venta  := sc_posicion_rec.venta_cash + :NEW.venta_cash - :OLD.venta_cash    +
               sc_posicion_rec.venta_tom + :NEW.venta_tom - :OLD.venta_tom       +
               sc_posicion_rec.venta_spot + :NEW.venta_spot - :OLD.venta_spot    +
               sc_posicion_rec.venta_72hr + :NEW.venta_72hr - :OLD.venta_72hr    +
               sc_posicion_rec.venta_vfut + :NEW.venta_vfut - :OLD.venta_vfut;
	 posicionInicialMN := sc_posicion_rec.posicion_inicial_mn_cash + :NEW.posicion_inicial_mn_cash - :OLD.posicion_inicial_mn_cash  +
	                      sc_posicion_rec.posicion_inicial_mn_tom  + :NEW.posicion_inicial_mn_tom - :OLD.posicion_inicial_mn_tom +
                          sc_posicion_rec.posicion_inicial_mn_spot + :NEW.posicion_inicial_mn_spot - :OLD.posicion_inicial_mn_spot +
                          sc_posicion_rec.posicion_inicial_mn_72hr + :NEW.posicion_inicial_mn_72hr - :OLD.posicion_inicial_mn_72hr +
                          sc_posicion_rec.posicion_inicial_mn_vfut + :NEW.posicion_inicial_mn_vfut - :OLD.posicion_inicial_mn_vfut;
     compraMN := sc_posicion_rec.compra_mn_pizarron_cash + :NEW.compra_mn_pizarron_cash
                                                         - :OLD.compra_mn_pizarron_cash    +
                 sc_posicion_rec.compra_mn_pizarron_tom + :NEW.compra_mn_pizarron_tom
                                                        - :OLD.compra_mn_pizarron_tom      +
                 sc_posicion_rec.compra_mn_pizarron_spot + :NEW.compra_mn_pizarron_spot
                                                         - :OLD.compra_mn_pizarron_spot    +
                 sc_posicion_rec.compra_mn_pizarron_72hr + :NEW.compra_mn_pizarron_72hr
                                                         - :OLD.compra_mn_pizarron_72hr    +
                 sc_posicion_rec.compra_mn_pizarron_vfut + :NEW.compra_mn_pizarron_vfut
                                                         - :OLD.compra_mn_pizarron_vfut;
     ventaMN := sc_posicion_rec.venta_mn_pizarron_cash + :NEW.venta_mn_pizarron_cash
                                                       - :OLD.venta_mn_pizarron_cash    +
                sc_posicion_rec.venta_mn_pizarron_tom + :NEW.venta_mn_pizarron_tom
                                                      - :OLD.venta_mn_pizarron_tom      +
                sc_posicion_rec.venta_mn_pizarron_spot + :NEW.venta_mn_pizarron_spot
                                                       - :OLD.venta_mn_pizarron_spot    +
                sc_posicion_rec.venta_mn_pizarron_72hr + :NEW.venta_mn_pizarron_72hr
                                                       - :OLD.venta_mn_pizarron_72hr    +
                sc_posicion_rec.venta_mn_pizarron_vfut + :NEW.venta_mn_pizarron_vfut
                                                       - :OLD.venta_mn_pizarron_vfut;
	 -- se incluye la Posicion Inicial
	 IF (posicionInicial > 0) THEN
       compra := compra + posicionInicial;
	   compraMN := compraMN + posicionInicialMN;
	 ELSE
       venta := venta - posicionInicial;
	   ventaMN := ventaMN - posicionInicialMN;
	 END IF;
     -- avoid divide by cero
     IF (compra > 1.0) AND (venta > 1.0) THEN
       difTasa := ((ventaMN / venta) - (compraMN / compra));
     ELSE
       difTasa := 0.0;
     END IF;
     -- se valida si esta en corto o en largo
     IF (compra > venta) THEN
       newUtilidadMesa := venta * difTasa;
     ELSE
       newUtilidadMesa := compra * difTasa;
     END IF;
     -- --------------------------------------------------------------
     -- Se calcula la utilidad Global
     -- --------------------------------------------------------------
     compraMN := sc_posicion_rec.compra_mn_cliente_cash + :NEW.compra_mn_cliente_cash
                                                        - :OLD.compra_mn_cliente_cash   +
                 sc_posicion_rec.compra_mn_cliente_tom + :NEW.compra_mn_cliente_tom
                                                       - :OLD.compra_mn_cliente_tom     +
                 sc_posicion_rec.compra_mn_cliente_spot + :NEW.compra_mn_cliente_spot
                                                        - :OLD.compra_mn_cliente_spot   +
                 sc_posicion_rec.compra_mn_cliente_72hr + :NEW.compra_mn_cliente_72hr
                                                         - :OLD.compra_mn_cliente_72hr  +
                 sc_posicion_rec.compra_mn_cliente_vfut + :NEW.compra_mn_cliente_vfut
                                                         - :OLD.compra_mn_cliente_vfut;
     ventaMN := sc_posicion_rec.venta_mn_cliente_cash + :NEW.venta_mn_cliente_cash
                                                      - :OLD.venta_mn_cliente_cash     +
                sc_posicion_rec.venta_mn_cliente_tom + :NEW.venta_mn_cliente_tom
                                                     - :OLD.venta_mn_cliente_tom       +
                sc_posicion_rec.venta_mn_cliente_spot + :NEW.venta_mn_cliente_spot
                                                      - :OLD.venta_mn_cliente_spot     +
                sc_posicion_rec.venta_mn_cliente_72hr + :NEW.venta_mn_cliente_72hr
                                                       - :OLD.venta_mn_cliente_72hr    +
                sc_posicion_rec.venta_mn_cliente_vfut + :NEW.venta_mn_cliente_vfut
                                                       - :OLD.venta_mn_cliente_vfut;
	 -- se incluye la Posicion Inicial
	 IF (posicionInicial > 0) THEN
	   compraMN := compraMN + posicionInicialMN;
	 ELSE
	   ventaMN := ventaMN - posicionInicialMN;
	 END IF;
     -- avoid divide by cero
     IF (compra > 1.0) AND (venta > 1.0) THEN
       difTasa := ((ventaMN / venta) - (compraMN / compra));
     ELSE
       difTasa := 0.0;
     END IF;
     -- se valida si esta en corto o en largo
     IF (compra > venta) THEN
       newUtilidadGlobal := venta * difTasa;
     ELSE
       newUtilidadGlobal := compra * difTasa;
     END IF;
     -- --------------------------------------------------------------
     -- se realiza la operacion de UPDATE
     UPDATE sc_posicion
       SET
           -- se utiliza posicion_inicial para el caso de los corrimientos
           -- en el cierre de dia.
            posicion_inicial_cash = posicion_inicial_cash +
                                    :NEW.posicion_inicial_cash -
                                    :OLD.posicion_inicial_cash,
            posicion_inicial_tom = posicion_inicial_tom +
                                    :NEW.posicion_inicial_tom -
                                    :OLD.posicion_inicial_tom,
            posicion_inicial_spot = posicion_inicial_spot +
                                    :NEW.posicion_inicial_spot -
                                    :OLD.posicion_inicial_spot,
            posicion_inicial_72hr = posicion_inicial_72hr +
                                    :NEW.posicion_inicial_72hr -
                                    :OLD.posicion_inicial_72hr,
            posicion_inicial_vfut = posicion_inicial_vfut +
                                    :NEW.posicion_inicial_vfut -
                                    :OLD.posicion_inicial_vfut,
            posicion_inicial_mn_cash = posicion_inicial_mn_cash +
                                    :NEW.posicion_inicial_mn_cash -
                                    :OLD.posicion_inicial_mn_cash,
            posicion_inicial_mn_tom = posicion_inicial_mn_tom +
                                    :NEW.posicion_inicial_mn_tom -
                                    :OLD.posicion_inicial_mn_tom,
            posicion_inicial_mn_spot = posicion_inicial_mn_spot +
                                    :NEW.posicion_inicial_mn_spot -
                                    :OLD.posicion_inicial_mn_spot,
            posicion_inicial_mn_72hr = posicion_inicial_mn_72hr +
                                    :NEW.posicion_inicial_mn_72hr -
                                    :OLD.posicion_inicial_mn_72hr,
            posicion_inicial_mn_vfut = posicion_inicial_mn_vfut +
                                    :NEW.posicion_inicial_mn_vfut -
                                    :OLD.posicion_inicial_mn_vfut,
            compra_cash = compra_cash + :NEW.compra_cash - :OLD.compra_cash,
            compra_tom = compra_tom + :NEW.compra_tom - :OLD.compra_tom,
            compra_spot = compra_spot + :NEW.compra_spot - :OLD.compra_spot,
            compra_72hr = compra_72hr + :NEW.compra_72hr - :OLD.compra_72hr,
            compra_vfut = compra_vfut + :NEW.compra_vfut - :OLD.compra_vfut,
            compra_in_cash = compra_in_cash + :NEW.compra_in_cash - :OLD.compra_in_cash,
            compra_in_tom = compra_in_tom + :NEW.compra_in_tom - :OLD.compra_in_tom,
            compra_in_spot = compra_in_spot + :NEW.compra_in_spot - :OLD.compra_in_spot,
            compra_in_72hr = compra_in_72hr + :NEW.compra_in_72hr - :OLD.compra_in_72hr,
            compra_in_vfut = compra_in_vfut + :NEW.compra_in_vfut - :OLD.compra_in_vfut,
            compra_mn_cliente_cash = compra_mn_cliente_cash +
                                     :NEW.compra_mn_cliente_cash -
                                     :OLD.compra_mn_cliente_cash,
            compra_mn_cliente_tom = compra_mn_cliente_tom +
                                     :NEW.compra_mn_cliente_tom -
                                     :OLD.compra_mn_cliente_tom,
            compra_mn_cliente_spot = compra_mn_cliente_spot +
                                     :NEW.compra_mn_cliente_spot -
                                     :OLD.compra_mn_cliente_spot,
            compra_mn_cliente_72hr = compra_mn_cliente_72hr +
                                     :NEW.compra_mn_cliente_72hr -
                                     :OLD.compra_mn_cliente_72hr,
            compra_mn_cliente_vfut = compra_mn_cliente_vfut +
                                     :NEW.compra_mn_cliente_vfut -
                                     :OLD.compra_mn_cliente_vfut,
            compra_mn_pizarron_cash = compra_mn_pizarron_cash +
                                     :NEW.compra_mn_pizarron_cash -
                                     :OLD.compra_mn_pizarron_cash,
            compra_mn_pizarron_tom = compra_mn_pizarron_tom +
                                     :NEW.compra_mn_pizarron_tom -
                                     :OLD.compra_mn_pizarron_tom,
            compra_mn_pizarron_spot = compra_mn_pizarron_spot +
                                     :NEW.compra_mn_pizarron_spot -
                                     :OLD.compra_mn_pizarron_spot,
            compra_mn_pizarron_72hr = compra_mn_pizarron_72hr +
                                     :NEW.compra_mn_pizarron_72hr -
                                     :OLD.compra_mn_pizarron_72hr,
            compra_mn_pizarron_vfut = compra_mn_pizarron_vfut +
                                     :NEW.compra_mn_pizarron_vfut -
                                     :OLD.compra_mn_pizarron_vfut,
            compra_mn_mesa_cash = compra_mn_mesa_cash +
                                     :NEW.compra_mn_mesa_cash -
                                     :OLD.compra_mn_mesa_cash,
            compra_mn_mesa_tom = compra_mn_mesa_tom +
                                    :NEW.compra_mn_mesa_tom -
                                     :OLD.compra_mn_mesa_tom,
            compra_mn_mesa_spot = compra_mn_mesa_spot +
                                     :NEW.compra_mn_mesa_spot -
                                     :OLD.compra_mn_mesa_spot,
            compra_mn_mesa_72hr = compra_mn_mesa_72hr +
                                     :NEW.compra_mn_mesa_72hr -
                                     :OLD.compra_mn_mesa_72hr,
            compra_mn_mesa_vfut = compra_mn_mesa_vfut +
                                     :NEW.compra_mn_mesa_vfut -
                                     :OLD.compra_mn_mesa_vfut,
            venta_cash = venta_cash + :NEW.venta_cash - :OLD.venta_cash,
            venta_tom = venta_tom + :NEW.venta_tom - :OLD.venta_tom,
            venta_spot = venta_spot + :NEW.venta_spot - :OLD.venta_spot,
            venta_72hr = venta_72hr + :NEW.venta_72hr - :OLD.venta_72hr,
            venta_vfut = venta_vfut + :NEW.venta_vfut - :OLD.venta_vfut,
            venta_in_cash = venta_in_cash + :NEW.venta_in_cash - :OLD.venta_in_cash,
            venta_in_tom = venta_in_tom + :NEW.venta_in_tom - :OLD.venta_in_tom,
            venta_in_spot = venta_in_spot + :NEW.venta_in_spot - :OLD.venta_in_spot,
            venta_in_72hr = venta_in_72hr + :NEW.venta_in_72hr - :OLD.venta_in_72hr,
            venta_in_vfut = venta_in_vfut + :NEW.venta_in_vfut - :OLD.venta_in_vfut,
            venta_mn_cliente_cash = venta_mn_cliente_cash +
                                     :NEW.venta_mn_cliente_cash -
                                     :OLD.venta_mn_cliente_cash,
            venta_mn_cliente_tom = venta_mn_cliente_tom +
                                    :NEW.venta_mn_cliente_tom -
                                     :OLD.venta_mn_cliente_tom,
            venta_mn_cliente_spot = venta_mn_cliente_spot +
                                     :NEW.venta_mn_cliente_spot -
                                     :OLD.venta_mn_cliente_spot,
            venta_mn_cliente_72hr = venta_mn_cliente_72hr +
                                     :NEW.venta_mn_cliente_72hr -
                                     :OLD.venta_mn_cliente_72hr,
            venta_mn_cliente_vfut = venta_mn_cliente_vfut +
                                     :NEW.venta_mn_cliente_vfut -
                                     :OLD.venta_mn_cliente_Vfut,
            venta_mn_pizarron_cash = venta_mn_pizarron_cash +
                                     :NEW.venta_mn_pizarron_cash -
                                     :OLD.venta_mn_pizarron_cash,
            venta_mn_pizarron_tom = venta_mn_pizarron_tom +
                                     :NEW.venta_mn_pizarron_tom -
                                     :OLD.venta_mn_pizarron_tom,
            venta_mn_pizarron_spot = venta_mn_pizarron_spot +
                                     :NEW.venta_mn_pizarron_spot -
                                     :OLD.venta_mn_pizarron_spot,
            venta_mn_pizarron_72hr = venta_mn_pizarron_72hr +
                                     :NEW.venta_mn_pizarron_72hr -
                                     :OLD.venta_mn_pizarron_72hr,
            venta_mn_pizarron_vfut = venta_mn_pizarron_vfut +
                                     :NEW.venta_mn_pizarron_vfut -
                                     :OLD.venta_mn_pizarron_vfut,
            venta_mn_mesa_cash = venta_mn_mesa_cash +
                                     :NEW.venta_mn_mesa_cash -
                                     :OLD.venta_mn_mesa_cash,
            venta_mn_mesa_tom = venta_mn_mesa_tom +
                                    :NEW.venta_mn_mesa_tom -
                                     :OLD.venta_mn_mesa_tom,
            venta_mn_mesa_spot = venta_mn_mesa_spot +
                                     :NEW.venta_mn_mesa_spot -
                                     :OLD.venta_mn_mesa_spot,
            venta_mn_mesa_72hr = venta_mn_mesa_72hr +
                                     :NEW.venta_mn_mesa_72hr -
                                     :OLD.venta_mn_mesa_72hr,
            venta_mn_mesa_vfut = venta_mn_mesa_vfut +
                                     :NEW.venta_mn_mesa_vfut -
                                     :OLD.venta_mn_mesa_vfut,
           utilidad_global = newUtilidadGlobal,
           utilidad_mesa = newUtilidadMesa
       WHERE
         (id_mesa_cambio = :NEW.id_mesa_cambio) AND (id_divisa = :NEW.id_divisa);
   END ActualizaTotalPosicion;
/

   -- ----------------------------------------------------------
   /**
     *  Autor : Ricardo Legorreta H.
     *  Fecha: 05/07/2006
     *
     *  Trigger InsertaTotalPosicion
     *
   **/
   -- ----------------------------------------------------------


   CREATE OR REPLACE TRIGGER SICA_ADMIN.InsertaTotalPosicion
     AFTER INSERT ON sc_posicion_detalle
     FOR EACH ROW
   DECLARE
     newUtilidadGlobal  NUMBER;
     newUtilidadMesa    NUMBER;
     compra             NUMBER;
     venta              NUMBER;
     compraMN           NUMBER;
     ventaMN            NUMBER;
     difTasa            NUMBER;
	 posicionInicial    NUMBER;
	 posicionInicialMN  NUMBER;
     CURSOR c_sc_posicion IS
                    SELECT posicion_inicial_cash, posicion_inicial_tom, posicion_inicial_spot, posicion_inicial_72hr, posicion_inicial_vfut,
					        posicion_inicial_mn_cash, posicion_inicial_mn_tom, posicion_inicial_mn_spot, posicion_inicial_mn_72hr, posicion_inicial_mn_vfut,
                            compra_cash,compra_tom,compra_spot, compra_72hr, compra_vfut,
                            venta_cash,venta_tom,venta_spot, venta_72hr, venta_vfut,
                            compra_mn_pizarron_cash, compra_mn_pizarron_tom,compra_mn_pizarron_spot, compra_mn_pizarron_72hr, compra_mn_pizarron_vfut,
                            venta_mn_pizarron_cash, venta_mn_pizarron_tom,venta_mn_pizarron_spot, venta_mn_pizarron_72hr, venta_mn_pizarron_vfut,
                            compra_mn_cliente_cash, compra_mn_cliente_tom, compra_mn_cliente_spot, compra_mn_cliente_72hr, compra_mn_cliente_vfut,
                            venta_mn_cliente_cash, venta_mn_cliente_tom,venta_mn_cliente_spot, venta_mn_cliente_72hr, venta_mn_cliente_vfut
                      FROM sc_posicion
                      WHERE
                       (id_mesa_cambio = :NEW.id_mesa_cambio) AND
                       (id_divisa = :NEW.id_divisa);
     sc_posicion_rec   c_sc_posicion%ROWTYPE;
   BEGIN
     -- --------------------------------------------------------------
     -- Se mofifica el total de la posicion de acuerdo al detalle
     -- SC_POSICION_DETALLE que fue afectado en el trigger de
     -- ActualizaPosicion.
     -- NO puede darse el caso que NO exista registro de sc_posicion
     -- ya que se dio de alta en el trigger de ActualizaPosicion.
     -- siempre se refiere a una operacion de update
     -- previamente se recalcula la utilidad realizada en los atributos
     -- de utilidad ya que puede ser el caso de otro canal y "si" puede
     -- tener utilidad
     -- se pregunta por sc_posicion que se va actualizar
     OPEN c_sc_posicion;
       FETCH c_sc_posicion INTO sc_posicion_rec;
       IF c_sc_posicion%NOTFOUND THEN
         CLOSE c_sc_posicion;
         RAISE_APPLICATION_ERROR(-20001,
               'Error NO se pudo leer ni hacer UDPDATE en SC_POSICION');
       END IF;
     CLOSE c_sc_posicion;
     -- --------------------------------------------------------------
     -- Se calcula la utilidad realizada de la Mesa de Cambios
     -- --------------------------------------------------------------
	 posicionInicial := sc_posicion_rec.posicion_inicial_cash + :NEW.posicion_inicial_cash +
	                    sc_posicion_rec.posicion_inicial_tom + :NEW.posicion_inicial_tom +
	                    sc_posicion_rec.posicion_inicial_spot + :NEW.posicion_inicial_spot +
	                    sc_posicion_rec.posicion_inicial_72hr + :NEW.posicion_inicial_72hr +
	                    sc_posicion_rec.posicion_inicial_vfut + :NEW.posicion_inicial_vfut;
     compra := sc_posicion_rec.compra_cash + :NEW.compra_cash    +
               sc_posicion_rec.compra_tom + :NEW.compra_tom      +
               sc_posicion_rec.compra_spot + :NEW.compra_spot    +
               sc_posicion_rec.compra_72hr + :NEW.compra_72hr    +
               sc_posicion_rec.compra_vfut + :NEW.compra_vfut;
     venta := sc_posicion_rec.venta_cash + :NEW.venta_cash      +
              sc_posicion_rec.venta_tom + :NEW.venta_tom        +
              sc_posicion_rec.venta_spot + :NEW.venta_spot      +
              sc_posicion_rec.venta_72hr + :NEW.venta_72hr      +
              sc_posicion_rec.venta_vfut + :NEW.venta_vfut;
	 posicionInicialMN := sc_posicion_rec.posicion_inicial_mn_cash + :NEW.posicion_inicial_mn_cash +
	                      sc_posicion_rec.posicion_inicial_mn_tom + :NEW.posicion_inicial_mn_tom +
	                      sc_posicion_rec.posicion_inicial_mn_spot + :NEW.posicion_inicial_mn_spot +
	                      sc_posicion_rec.posicion_inicial_mn_72hr + :NEW.posicion_inicial_mn_72hr +
	                      sc_posicion_rec.posicion_inicial_mn_vfut + :NEW.posicion_inicial_mn_vfut;
     compraMN := sc_posicion_rec.compra_mn_pizarron_cash + :NEW.compra_mn_pizarron_cash    +
                 sc_posicion_rec.compra_mn_pizarron_tom + :NEW.compra_mn_pizarron_tom      +
                 sc_posicion_rec.compra_mn_pizarron_spot + :NEW.compra_mn_pizarron_spot    +
                 sc_posicion_rec.compra_mn_pizarron_72hr + :NEW.compra_mn_pizarron_72hr    +
                 sc_posicion_rec.compra_mn_pizarron_vfut + :NEW.compra_mn_pizarron_vfut;
     ventaMN := sc_posicion_rec.venta_mn_pizarron_cash + :NEW.venta_mn_pizarron_cash     +
                sc_posicion_rec.venta_mn_pizarron_tom + :NEW.venta_mn_pizarron_tom       +
                sc_posicion_rec.venta_mn_pizarron_spot + :NEW.venta_mn_pizarron_spot     +
                sc_posicion_rec.venta_mn_pizarron_72hr + :NEW.venta_mn_pizarron_72hr     +
                sc_posicion_rec.venta_mn_pizarron_vfut + :NEW.venta_mn_pizarron_vfut;
	 -- se incrementa la Posicion Inicial
	 IF (posicionInicial > 0) THEN
	   compra := compra + posicionInicial;
	   compraMN := compraMN + posicionInicialMN;
	 ELSE
	   venta := venta - posicionInicial;
	   ventaMN := ventaMN - posicionInicialMN;
	 END IF;
     -- avoid divide by cero
     IF (compra > 1.0) AND (venta > 1.0) THEN
       difTasa := ((ventaMN / venta) - (compraMN / compra));
     ELSE
       difTasa := 0.0;
     END IF;
     -- se valida si esta en corto o en largo
     IF (compra > venta) THEN
       newUtilidadMesa := venta * difTasa;
     ELSE
       newUtilidadMesa := compra * difTasa;
     END IF;
     -- --------------------------------------------------------------
     -- Se calcula la utilidad realizada Global
     -- --------------------------------------------------------------
     compraMN := sc_posicion_rec.compra_mn_cliente_cash + :NEW.compra_mn_cliente_cash    +
                 sc_posicion_rec.compra_mn_cliente_tom + :NEW.compra_mn_cliente_tom      +
                 sc_posicion_rec.compra_mn_cliente_spot + :NEW.compra_mn_cliente_spot    +
                 sc_posicion_rec.compra_mn_cliente_72hr + :NEW.compra_mn_cliente_72hr    +
                 sc_posicion_rec.compra_mn_cliente_vfut + :NEW.compra_mn_cliente_vfut;
     ventaMN := sc_posicion_rec.venta_mn_cliente_cash + :NEW.venta_mn_cliente_cash       +
                sc_posicion_rec.venta_mn_cliente_tom + :NEW.venta_mn_cliente_tom         +
                sc_posicion_rec.venta_mn_cliente_spot + :NEW.venta_mn_cliente_spot       +
                sc_posicion_rec.venta_mn_cliente_72hr + :NEW.venta_mn_cliente_72hr       +
                sc_posicion_rec.venta_mn_cliente_vfut + :NEW.venta_mn_cliente_vfut;
	 -- se incrementa la Posicion Inicial
	 IF (posicionInicial > 0) THEN
	   compraMN := compraMN + posicionInicialMN;
	 ELSE
	   ventaMN := ventaMN - posicionInicialMN;
	 END IF;
     -- avoid divide by cero
     IF (compra > 1.0) AND (venta > 1.0) THEN
       difTasa := ((ventaMN / venta) - (compraMN / compra));
     ELSE
       difTasa := 0.0;
     END IF;
     IF (compra > venta) THEN
       newUtilidadGlobal := venta * difTasa;
     ELSE
       newUtilidadGlobal := compra * difTasa;
     END IF;
     -- --------------------------------------------------------------
     -- se realiza la operacion de UPDATE
     UPDATE sc_posicion
       SET compra_cash = compra_cash + :NEW.compra_cash,
           compra_tom = compra_tom + :NEW.compra_tom,
           compra_spot = compra_spot + :NEW.compra_spot,
           compra_72hr = compra_72hr + :NEW.compra_72hr,
           compra_vfut = compra_vfut + :NEW.venta_vfut,
           compra_in_cash = compra_in_cash + :NEW.compra_in_cash,
           compra_in_tom = compra_in_tom + :NEW.compra_in_tom,
           compra_in_spot = compra_in_spot + :NEW.compra_in_spot,
           compra_in_72hr = compra_in_72hr + :NEW.compra_in_72hr,
           compra_in_vfut = compra_in_vfut + :NEW.compra_in_vfut,
           compra_mn_cliente_cash = compra_mn_cliente_cash +
                                       :NEW.compra_mn_cliente_cash,
           compra_mn_cliente_tom = compra_mn_cliente_tom +
                                       :NEW.compra_mn_cliente_tom,
           compra_mn_cliente_spot = compra_mn_cliente_spot +
                                       :NEW.compra_mn_cliente_spot,
           compra_mn_cliente_72hr = compra_mn_cliente_72hr +
                                       :NEW.compra_mn_cliente_72hr,
           compra_mn_cliente_vfut = compra_mn_cliente_vfut +
                                       :NEW.compra_mn_cliente_vfut,
           compra_mn_pizarron_cash = compra_mn_pizarron_cash +
                                       :NEW.compra_mn_pizarron_cash,
           compra_mn_pizarron_tom = compra_mn_pizarron_tom +
                                       :NEW.compra_mn_pizarron_tom,
           compra_mn_pizarron_spot = compra_mn_pizarron_spot +
                                       :NEW.compra_mn_pizarron_spot,
           compra_mn_pizarron_72hr = compra_mn_pizarron_72hr +
                                       :NEW.compra_mn_pizarron_72hr,
           compra_mn_pizarron_vfut = compra_mn_pizarron_vfut +
                                       :NEW.compra_mn_pizarron_vfut,
           compra_mn_mesa_cash = compra_mn_mesa_cash +
                                       :NEW.compra_mn_mesa_cash,
           compra_mn_mesa_tom = compra_mn_mesa_tom +
                                       :NEW.compra_mn_mesa_tom,
           compra_mn_mesa_spot = compra_mn_mesa_spot +
                                       :NEW.compra_mn_mesa_spot,
           compra_mn_mesa_72hr = compra_mn_mesa_72hr +
                                       :NEW.compra_mn_mesa_72hr,
           compra_mn_mesa_vfut = compra_mn_mesa_vfut +
                                       :NEW.compra_mn_mesa_vfut,
           venta_cash = venta_cash + :NEW.venta_cash,
           venta_tom = venta_tom + :NEW.venta_tom,
           venta_spot = venta_spot + :NEW.venta_spot,
           venta_72hr = venta_72hr + :NEW.venta_72hr,
           venta_vfut = venta_vfut + :NEW.venta_vfut,
           venta_in_cash = venta_in_cash + :NEW.venta_in_cash,
           venta_in_tom = venta_in_tom + :NEW.venta_in_tom,
           venta_in_spot = venta_in_spot + :NEW.venta_in_spot,
           venta_in_72hr = venta_in_72hr + :NEW.venta_in_72hr,
           venta_in_vfut = venta_in_vfut + :NEW.venta_in_vfut,
           venta_mn_cliente_cash = venta_mn_cliente_cash +
                                       :NEW.venta_mn_cliente_cash,
           venta_mn_cliente_tom = venta_mn_cliente_tom +
                                       :NEW.venta_mn_cliente_tom,
           venta_mn_cliente_spot = venta_mn_cliente_spot +
                                       :NEW.venta_mn_cliente_spot,
           venta_mn_cliente_72hr = venta_mn_cliente_72hr +
                                       :NEW.venta_mn_cliente_72hr,
           venta_mn_cliente_vfut = venta_mn_cliente_vfut +
                                       :NEW.venta_mn_cliente_vfut,
           venta_mn_pizarron_cash = venta_mn_pizarron_cash +
                                       :NEW.venta_mn_pizarron_cash,
           venta_mn_pizarron_tom = venta_mn_pizarron_tom +
                                       :NEW.venta_mn_pizarron_tom,
           venta_mn_pizarron_spot = venta_mn_pizarron_spot +
                                       :NEW.venta_mn_pizarron_spot,
           venta_mn_pizarron_72hr = venta_mn_pizarron_72hr +
                                       :NEW.venta_mn_pizarron_72hr,
           venta_mn_pizarron_vfut = venta_mn_pizarron_vfut +
                                       :NEW.venta_mn_pizarron_vfut,
           venta_mn_mesa_cash = venta_mn_mesa_cash +
                                       :NEW.venta_mn_mesa_cash,
           venta_mn_mesa_tom = venta_mn_mesa_tom +
                                       :NEW.venta_mn_mesa_tom,
           venta_mn_mesa_spot = venta_mn_mesa_spot +
                                       :NEW.venta_mn_mesa_spot,
           venta_mn_mesa_72hr = venta_mn_mesa_72hr +
                                       :NEW.venta_mn_mesa_72hr,
           venta_mn_mesa_vfut = venta_mn_mesa_vfut +
                                       :NEW.venta_mn_mesa_vfut,
           utilidad_global = newUtilidadGlobal,
           utilidad_Mesa = newUtilidadMesa
       WHERE
         (id_mesa_cambio = :NEW.id_mesa_cambio) AND (id_divisa = :NEW.id_divisa);
     IF (SQL%NOTFOUND) THEN
       RAISE_APPLICATION_ERROR(-20001,
              'Error NO se pudo hacer UDPDATE en SC_POSICION');
     END IF;
    END InsertaTotalPosicion;
 /
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
/
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


