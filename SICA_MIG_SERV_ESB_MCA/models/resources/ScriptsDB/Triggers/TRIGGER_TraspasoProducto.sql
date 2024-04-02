/**
 *  $Id: TRIGGER_TraspasoProducto.sql,v 1.11 2008/02/22 18:25:46 ccovian Exp $
 *  Autor : Ricardo Legorreta H.
 *  Fecha: 25/08/2005
 *
 *  Trigger TraspasoProducto
 *  Realiza la logica de traspasos de productos dentro de la
 *  misma mesa de acuerdo a la narrativa de "Traspasos Producto"
 *  en Posicion.
 *
 **/

-- ----------------------------------------------------------
DROP TRIGGER TraspasoProducto;

CREATE OR REPLACE TRIGGER SICA_ADMIN.TraspasoProducto
  BEFORE INSERT ON sc_traspaso_producto
  FOR EACH ROW
DECLARE
  CURSOR c_sc_deal_posicion IS
                SELECT id_mesa_cambio,id_divisa,monto,id_usuario
                  FROM sc_deal_posicion
                WHERE
                  (id_deal_posicion = :NEW.id_deal_posicion);
  sc_deal_posicion         c_sc_deal_posicion%ROWTYPE;
  CURSOR c_sc_tipo_traspaso_producto IS
                SELECT neteo,de,de_oper,de_signo,a,a_oper,a_signo,val_monto
                  FROM sc_tipo_traspaso_producto
                WHERE
                 (mnemonico_traspaso = :NEW.mnemonico_traspaso);
  sc_tipo_traspaso_producto       c_sc_tipo_traspaso_producto%ROWTYPE;
  CURSOR c_sc_all_posicion_detalle IS
                SELECT id_canal,
				       posicion_inicial_cash, posicion_inicial_mn_cash,
				       compra_cash, compra_in_cash, compra_mn_cliente_cash,
					   compra_mn_pizarron_cash, compra_mn_mesa_cash,
					   venta_cash, venta_in_cash, venta_mn_cliente_cash,
					   venta_mn_pizarron_cash, venta_mn_mesa_cash
                  FROM sc_posicion_detalle
                WHERE
                 (id_mesa_cambio = sc_deal_posicion.id_mesa_cambio) AND
				 (id_divisa = sc_deal_posicion.id_divisa)           AND
				 (clave_forma_liquidacion = sc_tipo_traspaso_producto.de);
  sc_all_posicion_detalle  c_sc_all_posicion_detalle%ROWTYPE;
  CURSOR c_sc_posicion_detalle IS
                SELECT posicion_inicial_cash, posicion_inicial_mn_cash,
				       compra_cash, compra_in_cash, compra_mn_cliente_cash,
					   compra_mn_pizarron_cash, compra_mn_mesa_cash,
					   venta_cash, venta_in_cash, venta_mn_cliente_cash,
					   venta_mn_pizarron_cash, venta_mn_mesa_cash
                  FROM sc_posicion_detalle
                WHERE
                 (id_mesa_cambio = sc_deal_posicion.id_mesa_cambio) AND
				 (id_divisa = sc_deal_posicion.id_divisa)           AND
				 (id_canal = :NEW.id_canal)                         AND
				 (clave_forma_liquidacion = sc_tipo_traspaso_producto.de);
  sc_posicion_detalle  c_sc_posicion_detalle%ROWTYPE;
  tipo_cambio_cliente  NUMBER;
  tipo_cambio_pizarron NUMBER;
  tipo_cambio_mesa     NUMBER;
  monto                NUMBER;
  monto_mn             NUMBER;
  monto_in             NUMBER;
  monto_in_neteo       NUMBER;
  oper_in              CHAR(2);
BEGIN
  -- ---------------------------------------------------------------
  -- Se lee la super-clase del traspaso
  OPEN c_sc_deal_posicion;
  FETCH c_sc_deal_posicion INTO sc_deal_posicion;
  IF (c_sc_deal_posicion%NOTFOUND) THEN
    RAISE_APPLICATION_ERROR(-20002,'Error al leer sc_deal_posicion');
  END IF;
  CLOSE c_sc_deal_posicion;
  -- ---------------------------------------------------------------
  -- Se lee el MNEMONICO en la tabla de tipos de traspasos
  OPEN c_sc_tipo_traspaso_producto;
  FETCH c_sc_tipo_traspaso_producto INTO sc_tipo_traspaso_producto;
  IF (c_sc_tipo_traspaso_producto%NOTFOUND) THEN
    RAISE_APPLICATION_ERROR(-20003,'Error mnemonico no valido');
  END IF;
  CLOSE c_sc_tipo_traspaso_producto;
  -- ---------------------------------------------------------------
  -- Se realiza la lectura de sc_posicion_detalle
  IF (:NEW.id_canal IS NULL) THEN
    -- se refiere a que se quiere hacer un traspaso de TODO el monto posible
    OPEN c_sc_all_posicion_detalle;
	LOOP
      FETCH c_sc_all_posicion_detalle INTO sc_all_posicion_detalle;
	  IF c_sc_all_posicion_detalle%FOUND THEN
	    -- no se hace ninguna validacion ya que el monto no importa
		-- porque es calculado de acuerdo a lo que se tiene en la posicion
		-- -----------------------------------------------------------------------
		-- Se realiza la operacion de traspaso por lo que se calcula el monto
		-- de acuerdo a como se encuentra la posicion
		IF (sc_tipo_traspaso_producto.val_monto = NULL) THEN
          RAISE_APPLICATION_ERROR(-20003,'Error mnemonico ilegal para traspasar toda la posicion');
        END IF;
		IF (sc_tipo_traspaso_producto.val_monto = 'largo') THEN
		  monto := sc_all_posicion_detalle.posicion_inicial_cash + sc_all_posicion_detalle.compra_cash -
		           sc_all_posicion_detalle.venta_cash;
		  IF (monto < 0) THEN
		    monto := 0;
		  END IF;
		  IF (monto < sc_all_posicion_detalle.compra_in_cash) THEN
		    monto_in := monto;
		  ELSE
		    monto_in := sc_all_posicion_detalle.compra_in_cash;
		  END IF;
		  monto := monto - monto_in;
		  IF (monto >= 1.0) THEN
		    tipo_cambio_cliente := (sc_all_posicion_detalle.posicion_inicial_mn_cash +
		                            sc_all_posicion_detalle.compra_mn_cliente_cash) /
								   (sc_all_posicion_detalle.posicion_inicial_cash +
								    sc_all_posicion_detalle.compra_cash);
		    tipo_cambio_pizarron := (sc_all_posicion_detalle.posicion_inicial_mn_cash +
		                             sc_all_posicion_detalle.compra_mn_pizarron_cash) /
								    (sc_all_posicion_detalle.posicion_inicial_cash +
								     sc_all_posicion_detalle.compra_cash);
		  END IF;
		  IF (monto_in >= 1.0) THEN
		    tipo_cambio_mesa := sc_all_posicion_detalle.compra_mn_mesa_cash /
					            sc_all_posicion_detalle.compra_in_cash;
		  END IF;
		END IF;
		IF (sc_tipo_traspaso_producto.val_monto = 'corto') THEN
		  monto := sc_all_posicion_detalle.venta_cash -
		           sc_all_posicion_detalle.posicion_inicial_cash - sc_all_posicion_detalle.compra_cash;
		  IF (monto < 0) THEN
		    monto := 0;
		  END IF;
		  IF (monto < sc_all_posicion_detalle.venta_in_cash) THEN
		    monto_in := monto;
		  ELSE
		    monto_in := sc_all_posicion_detalle.venta_in_cash;
		  END IF;
		  monto := monto - monto_in;
		  IF (monto >= 1.0) THEN
		    tipo_cambio_cliente := sc_all_posicion_detalle.venta_mn_cliente_cash /
			                       sc_all_posicion_detalle.venta_cash;
		    tipo_cambio_pizarron := sc_all_posicion_detalle.venta_mn_pizarron_cash /
			                        sc_all_posicion_detalle.venta_cash;
		  END IF;
		  IF (monto_in >= 1.0) THEN
		    tipo_cambio_mesa := sc_all_posicion_detalle.venta_mn_mesa_cash /
					            sc_all_posicion_detalle.venta_in_cash;
		  END IF;
		END IF;
		IF (sc_tipo_traspaso_producto.val_monto = 'compra') THEN
		  monto := sc_all_posicion_detalle.compra_cash;
		  IF (monto < sc_all_posicion_detalle.compra_in_cash) THEN
		    monto_in := monto;
		  ELSE
		    monto_in := sc_all_posicion_detalle.compra_in_cash;
		  END IF;
          monto := monto - monto_in;
		  IF (monto >= 1.0) THEN
		    tipo_cambio_cliente :=  sc_all_posicion_detalle.compra_mn_cliente_cash /
			                        sc_all_posicion_detalle.compra_cash;
		    tipo_cambio_pizarron := sc_all_posicion_detalle.compra_mn_pizarron_cash /
			                        sc_all_posicion_detalle.compra_cash;
		  END IF;
          IF (monto_in >= 1.0) THEN
		    tipo_cambio_mesa := sc_all_posicion_detalle.compra_mn_mesa_cash /
			                    sc_all_posicion_detalle.compra_in_cash;
		  END IF;
		END IF;
		IF (sc_tipo_traspaso_producto.val_monto = 'venta') THEN
		  monto := sc_all_posicion_detalle.venta_cash;
		  IF (monto < sc_all_posicion_detalle.venta_in_cash) THEN
		    monto_in := monto;
		  ELSE
		    monto_in := sc_all_posicion_detalle.venta_in_cash;
		  END IF;
		  monto := monto - monto_in;
		  IF (monto >= 1.0) THEN
		    tipo_cambio_cliente := sc_all_posicion_detalle.venta_mn_cliente_cash /
								   sc_all_posicion_detalle.venta_cash;
		    tipo_cambio_pizarron := sc_all_posicion_detalle.venta_mn_pizarron_cash /
								    sc_all_posicion_detalle.venta_cash;
		  END IF;
          IF (monto_in >= 1.0) THEN
		    tipo_cambio_mesa := sc_all_posicion_detalle.venta_mn_mesa_cash /
			                    sc_all_posicion_detalle.venta_in_cash;
		  END IF;
		END IF;
		-- fin de calculo se insertan los registros del traspaso
		IF (monto >= 1.0) THEN
          INSERT INTO sc_posicion_log (
                      id_posicion_log,
                      id_mesa_cambio, id_divisa,
				      id_canal, clave_forma_liquidacion,
				      tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
				      tipo_operacion, monto, monto_mn,
				      tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
				      id_usuario)
          VALUES
            (sc_posicion_log_seq.nextval,
			 sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
			 sc_all_posicion_detalle.id_canal,sc_tipo_traspaso_producto.de,
			 'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
			 sc_tipo_traspaso_producto.de_oper,(monto * sc_tipo_traspaso_producto.de_signo),
			 (monto * tipo_cambio_cliente * sc_tipo_traspaso_producto.de_signo),
			 tipo_cambio_cliente, tipo_cambio_pizarron, 0,
		     sc_deal_posicion.id_usuario);
		  -- se insertar la contraparte
		  INSERT INTO sc_posicion_log (
					  id_posicion_log,
					  id_mesa_cambio, id_divisa,
					  id_canal, clave_forma_liquidacion,
					  tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
					  tipo_operacion, monto, monto_mn,
					  tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
					  id_usuario)
		  VALUES
		    (sc_posicion_log_seq.nextval,
			sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
			sc_all_posicion_detalle.id_canal,sc_tipo_traspaso_producto.a,
			'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
			sc_tipo_traspaso_producto.a_oper,(monto * sc_tipo_traspaso_producto.a_signo),
			(monto * tipo_cambio_cliente * sc_tipo_traspaso_producto.a_signo),
			tipo_cambio_cliente, tipo_cambio_pizarron, 0,
			sc_deal_posicion.id_usuario);
        END IF;  -- monto >= 1.0
		-- ahora se realiza el traspado de interbancario (si es el caso)
		IF (monto_in >=  1.0) THEN
		  -- se inserta la contraparte del interbancario
		  -- se cambia la clave de operacion para el caso de interbancario
		  IF (sc_tipo_traspaso_producto.a_oper = 'CO') THEN
		    oper_in := 'CI';
		  ELSE
		    oper_in := 'VI';
		  END IF;
          INSERT INTO sc_posicion_log (
                    id_posicion_log,
                    id_mesa_cambio, id_divisa,
				    id_canal, clave_forma_liquidacion,
				    tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
				    tipo_operacion, monto, monto_mn,
				    tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
				    id_usuario)
          VALUES
             (sc_posicion_log_seq.nextval,
			  sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
              sc_all_posicion_detalle.id_canal,sc_tipo_traspaso_producto.a,
              'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
			   oper_in,(monto_in * sc_tipo_traspaso_producto.a_signo),
			   (monto_in * tipo_cambio_mesa * sc_tipo_traspaso_producto.a_signo),
              tipo_cambio_mesa, 0, 0,
			  sc_deal_posicion.id_usuario);
		  -- se cambia la clave de operacion para el caso de interbancario
		  -- se inserto primero la contraparta para dejar la ultima
		  -- operacion en oper_in que se hizo en caso de realizar el neteo
		  IF sc_tipo_traspaso_producto.de_oper = 'CO' THEN
		    oper_in := 'CI';
		  ELSE
		    oper_in := 'VI';
		  END IF;
          INSERT INTO sc_posicion_log (
                      id_posicion_log,
                      id_mesa_cambio, id_divisa,
				      id_canal, clave_forma_liquidacion,
				      tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
				      tipo_operacion, monto, monto_mn,
				      tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
				      id_usuario)
          VALUES
            (sc_posicion_log_seq.nextval,
			 sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
			 sc_all_posicion_detalle.id_canal,sc_tipo_traspaso_producto.de,
			 'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
			 oper_in,(monto_in * sc_tipo_traspaso_producto.de_signo),
			 (monto_in * tipo_cambio_mesa * sc_tipo_traspaso_producto.de_signo),
			 tipo_cambio_mesa, 0, 0,
		     sc_deal_posicion.id_usuario);
        END IF; -- IF (monto_in >= 1.0)
		-- -----------------------------------------------------------------------
		-- Inicio de la operacion de neteo si la operacion lo requiere
		IF sc_tipo_traspaso_producto.neteo = 1 THEN
		  IF (sc_all_posicion_detalle.venta_cash >= 1.0) AND
		     ((sc_all_posicion_detalle.compra_cash - sc_all_posicion_detalle.venta_cash) >= -1.0) THEN
		    -- se esta en largo se netea con las ventas_cash
			tipo_cambio_cliente := sc_all_posicion_detalle.compra_mn_cliente_cash /
			                       sc_all_posicion_detalle.compra_cash;
			tipo_cambio_pizarron := sc_all_posicion_detalle.compra_mn_pizarron_cash /
			                        sc_all_posicion_detalle.compra_cash;
			IF (sc_all_posicion_detalle.compra_in_cash >= 1.0) THEN
			  tipo_cambio_mesa := sc_all_posicion_detalle.compra_mn_mesa_cash /
								  sc_all_posicion_detalle.compra_in_cash;
			END IF;
			IF (oper_in = 'CI') THEN
			  monto_in_neteo := sc_all_posicion_detalle.compra_in_cash - monto_in;
			  -- ^ para no aplicarlo dos veces, no puede ser menor a cero
			ELSE
			  monto_in_neteo := sc_all_posicion_detalle.compra_in_cash;
			END IF;
			monto := sc_all_posicion_detalle.venta_cash - monto_in_neteo;
			IF (monto >= 1.0) THEN
              INSERT INTO sc_posicion_log (
                          id_posicion_log,
                          id_mesa_cambio, id_divisa,
				          id_canal, clave_forma_liquidacion,
				          tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
				          tipo_operacion, monto, monto_mn,
				          tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
				          id_usuario)
              VALUES
			    (sc_posicion_log_seq.nextval,
			     sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
			     sc_all_posicion_detalle.id_canal,sc_tipo_traspaso_producto.de,
			     'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
			     'CO',-monto, -(monto * tipo_cambio_cliente),
			     tipo_cambio_cliente, tipo_cambio_pizarron, 0,
			     sc_deal_posicion.id_usuario);
			END IF;
			-- Para el caso de interbancario se borra por completo tanto en las
			-- compras como en las ventas ya que se 'neteo' toda la operacion
			-- y no tiene sentido el netear los interbancarios, por lo que se
			-- pierde la informacion de utilidad por interbancario.
			-- Este caso no puede existir posicion en interbancario ya que
			-- interbancario solo opera TRAEXT. Aun asi se deja el codigo para
			-- mas hacerlo general
            IF (monto_in_neteo >= 1.0) THEN
              INSERT INTO sc_posicion_log (
                          id_posicion_log,
                          id_mesa_cambio, id_divisa,
				          id_canal, clave_forma_liquidacion,
				          tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
				          tipo_operacion, monto, monto_mn,
				          tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
				          id_usuario)
              VALUES
			    (sc_posicion_log_seq.nextval,
			     sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
				 sc_all_posicion_detalle.id_canal,sc_tipo_traspaso_producto.de,
			     'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
                 'CI',-monto_in_neteo, -(monto_in_neteo * tipo_cambio_mesa),
                 tipo_cambio_mesa, 0, 0,
				 sc_deal_posicion.id_usuario);
            END IF;
			-- se inserta la contraparte del neteo dejando la utilidad en ventas
			IF (oper_in = 'VI') THEN
			  monto_in_neteo := sc_all_posicion_detalle.venta_in_cash - monto_in;
			  -- ^ para no aplicarlo dos veces, no puede ser menor a cero
			ELSE
			  monto_in_neteo := sc_all_posicion_detalle.venta_in_cash;
			END IF;
			monto := sc_all_posicion_detalle.venta_cash - monto_in_neteo;
			IF (monto >= 1.0) THEN
              INSERT INTO sc_posicion_log (
                          id_posicion_log,
                          id_mesa_cambio, id_divisa,
						  id_canal, clave_forma_liquidacion,
				          tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
				          tipo_operacion, monto, monto_mn,
				          tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
				          id_usuario)
              VALUES
			    (sc_posicion_log_seq.nextval,
			     sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
			     sc_all_posicion_detalle.id_canal,sc_tipo_traspaso_producto.de,
			     'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
			     'VE',-monto, -(monto * tipo_cambio_cliente),
                  tipo_cambio_cliente, tipo_cambio_pizarron, 0,
				  sc_deal_posicion.id_usuario);
			END IF;
            IF (monto_in_neteo >= 1.0) THEN
              INSERT INTO sc_posicion_log (
                          id_posicion_log,
                          id_mesa_cambio, id_divisa,
				          id_canal, clave_forma_liquidacion,
				          tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
				          tipo_operacion, monto, monto_mn,
				          tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
				          id_usuario)
              VALUES
			    (sc_posicion_log_seq.nextval,
			     sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
				 sc_all_posicion_detalle.id_canal,sc_tipo_traspaso_producto.de,
			     'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
                 'VI',-monto_in_neteo, -(monto_in_neteo * tipo_cambio_cliente),
				 tipo_cambio_cliente, 0, 0,
				 sc_deal_posicion.id_usuario);
            END IF;
          ELSE -- IF compras > ventas
			IF (sc_all_posicion_detalle.compra_cash >= 1.0) AND
			   ((sc_all_posicion_detalle.venta_cash - sc_all_posicion_detalle.compra_cash) >= 1.0) THEN
		      -- se esta en corto se netea con las compras_cash
			  tipo_cambio_cliente := sc_all_posicion_detalle.venta_mn_cliente_cash /
			                         sc_all_posicion_detalle.venta_cash;
			  tipo_cambio_pizarron := sc_all_posicion_detalle.venta_mn_pizarron_cash /
			                          sc_all_posicion_detalle.venta_cash;
			  IF (sc_all_posicion_detalle.venta_in_cash >= 1.0) THEN
			    tipo_cambio_mesa := sc_all_posicion_detalle.venta_mn_mesa_cash /
								    sc_all_posicion_detalle.venta_in_cash;
			  END IF;
			  -- se deja la utilidad en compras
			  IF (oper_in = 'CI') THEN
			    monto_in_neteo := sc_all_posicion_detalle.compra_in_cash - monto_in;
			    -- ^ para no aplicarlo dos veces, no puede ser menor a cero
			  ELSE
			    monto_in_neteo := sc_all_posicion_detalle.compra_in_cash;
			  END IF;
			  monto := sc_all_posicion_detalle.compra_cash - monto_in_neteo;
			  IF (monto >= 1.0) THEN
                INSERT INTO sc_posicion_log (
                            id_posicion_log,
                            id_mesa_cambio, id_divisa,
				            id_canal, clave_forma_liquidacion,
				            tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
				            tipo_operacion, monto, monto_mn,
				            tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
				            id_usuario)
                VALUES
				  (sc_posicion_log_seq.nextval,
				   sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
				   sc_all_posicion_detalle.id_canal,sc_tipo_traspaso_producto.de,
				   'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
				   'CO',-monto, -(monto * tipo_cambio_cliente),
				   tipo_cambio_cliente, tipo_cambio_pizarron, 0,
				   sc_deal_posicion.id_usuario);
			  END IF;
			  -- Para el caso de interbancario se borra por completo tanto en las
			  -- compras como en las ventas ya que se 'neteo' toda la operacion
			  -- y no tiene sentido el netear los interbancarios, por lo que se
			  -- pierde la informacion de utilidad por interbancario.
			  -- Este caso no puede existir posicion en interbancario ya que
			  -- interbancario solo opera TRAEXT. Aun asi se deja el codigo para
			  -- mas hacerlo general
              IF (monto_in_neteo >= 1.0) THEN
			    -- No puede existir  posicion en interbancario ya que interbancario
			    -- solo opera TRAEXT. Aun asi se deja el codigo para hacerlo general
                INSERT INTO sc_posicion_log (
                            id_posicion_log,
                            id_mesa_cambio, id_divisa,
				            id_canal, clave_forma_liquidacion,
				            tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
				            tipo_operacion, monto, monto_mn,
				            tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
				            id_usuario)
                VALUES
			      (sc_posicion_log_seq.nextval,
			       sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
				   sc_all_posicion_detalle.id_canal,sc_tipo_traspaso_producto.de,
			       'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
                   'CI',-monto_in_neteo,-(monto_in_neteo * tipo_cambio_cliente),
                   tipo_cambio_cliente, 0, 0,
				   sc_deal_posicion.id_usuario);
              END IF;
			  -- se inserta la contraparte del neteo
			  IF (oper_in = 'VI') THEN
			    monto_in_neteo := sc_all_posicion_detalle.venta_in_cash - monto_in;
			    -- ^ para no aplicarlo dos veces, no puede ser menor a cero
			  ELSE
			    monto_in_neteo := sc_all_posicion_detalle.venta_in_cash;
			  END IF;
			  monto := sc_all_posicion_detalle.compra_cash - monto_in_neteo;
			  IF (monto >= 1.0) THEN
                INSERT INTO sc_posicion_log (
                            id_posicion_log,
                            id_mesa_cambio, id_divisa,
				            id_canal, clave_forma_liquidacion,
				            tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
				            tipo_operacion, monto, monto_mn,
				            tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
				            id_usuario)
                VALUES
				  (sc_posicion_log_seq.nextval,
				   sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
				   sc_all_posicion_detalle.id_canal,sc_tipo_traspaso_producto.de,
				   'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
				   'VE',-monto, -(monto * tipo_cambio_cliente),
				    tipo_cambio_cliente, tipo_cambio_pizarron, 0,
                    sc_deal_posicion.id_usuario);
			  END IF;
              IF (monto_in_neteo >= 1.0) THEN
			    -- No puede existir  posicion en interbancario ya que interbancario
			    -- solo opera TRAEXT. Aun asi se deja el codigo para hacerlo general
                INSERT INTO sc_posicion_log (
                            id_posicion_log,
                            id_mesa_cambio, id_divisa,
				            id_canal, clave_forma_liquidacion,
				            tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
				            tipo_operacion, monto, monto_mn,
				            tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
				            id_usuario)
                VALUES
			      (sc_posicion_log_seq.nextval,
			       sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
				   sc_all_posicion_detalle.id_canal,sc_tipo_traspaso_producto.de,
			       'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
                   'VI',-monto_in_neteo, -(monto_in_neteo * tipo_cambio_mesa),
                   tipo_cambio_mesa, 0, 0,
				   sc_deal_posicion.id_usuario);
              END IF;
            END IF; -- IF corto
		  END IF; -- IF largo
        END IF; -- IF traspaso neteo = 1
      END IF; -- IF c_sc_all_posicion_detalle%FOUND
      EXIT WHEN c_sc_all_posicion_detalle%NOTFOUND;
    END LOOP;
	CLOSE c_sc_all_posicion_detalle;
  ELSE
    -- ---------------------------------------------------------------
    -- Se tratan de traspasos parciales donde el usuario definio el
	-- monto a traspasar (mismo que se debe de validar) y por consi-
	-- guiente definio el id_canal que se trata
	-- -----------------------------------------------------------------------
	-- Se valida el monto previo a cualquier actualizacion
	OPEN c_sc_posicion_detalle;
    FETCH c_sc_posicion_detalle INTO sc_posicion_detalle;
    IF c_sc_posicion_detalle%FOUND THEN
      -- se hace la validacion ya que el monto fue capturado
      IF (sc_tipo_traspaso_producto.val_monto = 'largo') THEN
		monto := sc_posicion_detalle.posicion_inicial_cash + sc_posicion_detalle.compra_cash -
				 sc_posicion_detalle.venta_cash;
		IF ((monto < 0) OR ((sc_deal_posicion.monto - monto) >= 1.0)) THEN
		  RAISE_APPLICATION_ERROR(-20003,'El largo de la posicion no es suficiente');
		END IF;
	  END IF;
	  IF (sc_tipo_traspaso_producto.val_monto = 'corto') THEN
		monto := sc_posicion_detalle.venta_cash -
				 sc_posicion_detalle.posicion_inicial_cash - sc_posicion_detalle.compra_cash;
		IF ((monto < 0) OR ((sc_deal_posicion.monto - monto) >= 1.0)) THEN
		  RAISE_APPLICATION_ERROR(-20003,'El corto de la posicion no es suficiente');
		END IF;
	  END IF;
	  IF (sc_tipo_traspaso_producto.val_monto = 'compra') THEN
		monto := sc_posicion_detalle.compra_cash;
		IF ((monto < 0) OR ((sc_deal_posicion.monto - monto) >= 1.0)) THEN
		  RAISE_APPLICATION_ERROR(-20003,'El total de las compras de la posicion no es suficiente');
		END IF;
	  END IF;
	  IF (sc_tipo_traspaso_producto.val_monto = 'venta') THEN
		monto := sc_posicion_detalle.venta_cash - monto_in;
		IF ((monto < 0) OR ((sc_deal_posicion.monto - monto) >= 1.0)) THEN
		  RAISE_APPLICATION_ERROR(-20003,'El total de las ventas de la posicion no es suficiente');
		END IF;
	  END IF;
	ELSE -- posicion_found
      RAISE_APPLICATION_ERROR(-20003,'No existe posicion para el traspaso');
	END IF; -- IF c_sc_posicion_detalle%FOUND
	CLOSE c_sc_posicion_detalle;
	-- -----------------------------------------------------------------------
	-- Se realiza la operacion de traspaso de acuerdo al monto capturado
	IF (sc_tipo_traspaso_producto.val_monto = 'largo') THEN
	  monto := sc_posicion_detalle.posicion_inicial_cash + sc_posicion_detalle.compra_cash -
			   sc_posicion_detalle.venta_cash;
	  IF (monto < 0) THEN
	    monto := 0;
	  END IF;
	  IF (monto < sc_posicion_detalle.compra_in_cash) THEN
		    monto_in := monto;
		  ELSE
		    monto_in := sc_posicion_detalle.compra_in_cash;
		  END IF;
	  monto := monto - monto_in;
	  IF (monto >= 1.0) THEN
        tipo_cambio_cliente := (sc_posicion_detalle.posicion_inicial_mn_cash +
								sc_posicion_detalle.compra_mn_cliente_cash) /
							   (sc_posicion_detalle.posicion_inicial_cash +
								sc_posicion_detalle.compra_cash);
		tipo_cambio_pizarron := (sc_posicion_detalle.posicion_inicial_mn_cash +
								 sc_posicion_detalle.compra_mn_pizarron_cash) /
								(sc_posicion_detalle.posicion_inicial_cash +
								 sc_posicion_detalle.compra_cash);
	  END IF;
	  IF (monto_in >= 1.0) THEN
		tipo_cambio_mesa := sc_posicion_detalle.compra_mn_mesa_cash /
							sc_posicion_detalle.compra_in_cash;
	  END IF;
	END IF;
	IF (sc_tipo_traspaso_producto.val_monto = 'corto') THEN
	  monto := sc_posicion_detalle.venta_cash -
			   sc_posicion_detalle.posicion_inicial_cash - sc_posicion_detalle.compra_cash;
	  IF (monto < 0) THEN
		monto := 0;
	  END IF;
	  IF (monto < sc_posicion_detalle.venta_in_cash) THEN
		monto_in := monto;
	  ELSE
		monto_in := sc_posicion_detalle.venta_in_cash;
	  END IF;
	  monto := monto - monto_in;
	  IF (monto >= 1.0) THEN
		tipo_cambio_cliente := sc_posicion_detalle.venta_mn_cliente_cash /
							   sc_posicion_detalle.venta_cash;
		tipo_cambio_pizarron := sc_posicion_detalle.venta_mn_pizarron_cash /
								sc_posicion_detalle.venta_cash;
	  END IF;
	  IF (monto_in >= 1.0) THEN
		tipo_cambio_mesa := sc_posicion_detalle.venta_mn_mesa_cash /
							sc_posicion_detalle.venta_in_cash;
	  END IF;
	END IF;
	IF (sc_tipo_traspaso_producto.val_monto = 'compra') THEN
	  monto := sc_posicion_detalle.compra_cash;
	  IF (monto < sc_posicion_detalle.compra_in_cash) THEN
		monto_in := monto;
	  ELSE
		monto_in := sc_posicion_detalle.compra_in_cash;
	  END IF;
	  monto := monto - monto_in;
	  IF (monto >= 1.0) THEN
		tipo_cambio_cliente :=  sc_posicion_detalle.compra_mn_cliente_cash /
								sc_posicion_detalle.compra_cash;
		tipo_cambio_pizarron := sc_posicion_detalle.compra_mn_pizarron_cash /
								sc_posicion_detalle.compra_cash;
	  END IF;
	  IF (monto_in >= 1.0) THEN
		tipo_cambio_mesa := sc_posicion_detalle.compra_mn_mesa_cash /
							sc_posicion_detalle.compra_in_cash;
	  END IF;
	END IF;
	IF (sc_tipo_traspaso_producto.val_monto = 'venta') THEN
	  monto := sc_posicion_detalle.venta_cash;
	  IF (monto < sc_posicion_detalle.venta_in_cash) THEN
		monto_in := monto;
	  ELSE
		monto_in := sc_posicion_detalle.venta_in_cash;
	  END IF;
	  monto := monto - monto_in;
	  IF (monto >= 1.0) THEN
		tipo_cambio_cliente := sc_posicion_detalle.venta_mn_cliente_cash /
							   sc_posicion_detalle.venta_cash;
		tipo_cambio_pizarron := sc_posicion_detalle.venta_mn_pizarron_cash /
								sc_posicion_detalle.venta_cash;
	  END IF;
	  IF (monto_in >= 1.0) THEN
		tipo_cambio_mesa := sc_posicion_detalle.venta_mn_mesa_cash /
							sc_posicion_detalle.venta_in_cash;
	  END IF;
	END IF;
	IF (sc_tipo_traspaso_producto.val_monto IS NULL) THEN
	  monto_in := 0;
	END IF;
	-- se calcula el monto parcial a traspazar considerando la posicion
	-- que se tiene en interbancario.
	IF (monto_in >= 1.0) THEN
	  monto := sc_deal_posicion.monto - monto_in;
	  IF (monto < 0) THEN
	    monto_in := sc_deal_posicion.monto;
		monto := 0;
	  END IF;
	ELSE
	  monto := sc_deal_posicion.monto;
	END IF; -- IF (monto_in > 0.1)
	-- fin de calculo se insertan los registros del traspaso
	IF (monto >= 1.0) THEN
	  INSERT INTO sc_posicion_log (
				  id_posicion_log,
				  id_mesa_cambio, id_divisa,
				  id_canal, clave_forma_liquidacion,
				  tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
				  tipo_operacion, monto, monto_mn,
				  tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
				  id_usuario)
	  VALUES
		(sc_posicion_log_seq.nextval,
		 sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
		 :NEW.id_canal,sc_tipo_traspaso_producto.de,
		 'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
		 sc_tipo_traspaso_producto.de_oper,(monto * sc_tipo_traspaso_producto.de_signo),
		 (monto * tipo_cambio_cliente * sc_tipo_traspaso_producto.de_signo),
		 tipo_cambio_cliente, tipo_cambio_pizarron, 0,
		 sc_deal_posicion.id_usuario);
	  -- se insertar la contraparte
	  INSERT INTO sc_posicion_log (
				  id_posicion_log,
				  id_mesa_cambio, id_divisa,
				  id_canal, clave_forma_liquidacion,
				  tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
				  tipo_operacion, monto, monto_mn,
				  tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
				  id_usuario)
	  VALUES
		(sc_posicion_log_seq.nextval,
		 sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
		 :NEW.id_canal,sc_tipo_traspaso_producto.a,
		 'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
		 sc_tipo_traspaso_producto.a_oper,(monto * sc_tipo_traspaso_producto.a_signo),
		 (monto * tipo_cambio_cliente * sc_tipo_traspaso_producto.a_signo),
		 tipo_cambio_cliente, tipo_cambio_pizarron, 0,
		 sc_deal_posicion.id_usuario);
	END IF;  -- monto >= 1.0
	-- ahora se realiza el traspado de interbancario (si es el caso)
	IF (monto_in >=  1.0) THEN
      -- se inserta la contraparte del interbancario
	  -- se cambia la clave de operacion para el caso de interbancario
	  IF sc_tipo_traspaso_producto.a_oper = 'CO' THEN
		oper_in := 'CI';
	  ELSE
		oper_in := 'VI';
	  END IF;
	  INSERT INTO sc_posicion_log (
				  id_posicion_log,
				  id_mesa_cambio, id_divisa,
			      id_canal, clave_forma_liquidacion,
				  tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
				  tipo_operacion, monto, monto_mn,
				  tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
				  id_usuario)
	  VALUES
		(sc_posicion_log_seq.nextval,
		 sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
		 :NEW.id_canal,sc_tipo_traspaso_producto.a,
		 'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
		 oper_in,(monto_in * sc_tipo_traspaso_producto.a_signo),
		 (monto_in * tipo_cambio_mesa * sc_tipo_traspaso_producto.a_signo),
		 tipo_cambio_mesa, 0, 0,
		 sc_deal_posicion.id_usuario);
	  -- se cambia la clave de operacion para el caso de interbancario
	  -- se inserto primero la contraparta para dejar la ultima
	  -- operacion en oper_in que se hizo en caso de realizar el neteo
	  IF sc_tipo_traspaso_producto.de_oper = 'CO' THEN
		oper_in := 'CI';
	  ELSE
		oper_in := 'VI';
	  END IF;
	  INSERT INTO sc_posicion_log (
				  id_posicion_log,
				  id_mesa_cambio, id_divisa,
				  id_canal, clave_forma_liquidacion,
				  tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
				  tipo_operacion, monto, monto_mn,
				  tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
				  id_usuario)
	  VALUES
		(sc_posicion_log_seq.nextval,
		 sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
		 :NEW.id_canal,sc_tipo_traspaso_producto.de,
		 'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
		  oper_in,(monto_in * sc_tipo_traspaso_producto.de_signo),
		 (monto_in * tipo_cambio_mesa * sc_tipo_traspaso_producto.de_signo),
		 tipo_cambio_mesa, 0, 0,
		 sc_deal_posicion.id_usuario);
    END IF; -- IF (monto_in >= 1.0)
	-- -----------------------------------------------------------------------
	IF sc_tipo_traspaso_producto.neteo = 1 THEN
	  IF (sc_posicion_detalle.venta_cash >= 1.0) AND
		 ((sc_posicion_detalle.compra_cash - sc_posicion_detalle.venta_cash) >= -1.0) THEN
		-- se esta en largo se netea con las ventas_cash
		tipo_cambio_cliente := sc_posicion_detalle.compra_mn_cliente_cash /
							   sc_posicion_detalle.compra_cash;
		tipo_cambio_pizarron := sc_posicion_detalle.compra_mn_pizarron_cash /
								sc_posicion_detalle.compra_cash;
		IF (sc_posicion_detalle.compra_in_cash >= 1.0) THEN
		  tipo_cambio_mesa := sc_posicion_detalle.compra_mn_mesa_cash /
							  sc_posicion_detalle.compra_in_cash;
		END IF;
		IF (oper_in = 'CI') THEN
		  monto_in_neteo := sc_posicion_detalle.compra_in_cash - monto_in;
		  -- ^ para no aplicarlo dos veces, no puede ser menor a cero
		ELSE
		  monto_in_neteo := sc_posicion_detalle.compra_in_cash;
		END IF;
		monto := sc_posicion_detalle.venta_cash - monto_in_neteo;
		IF (monto >=  1.0) THEN
		  INSERT INTO sc_posicion_log (
					  id_posicion_log,
					  id_mesa_cambio, id_divisa,
					  id_canal, clave_forma_liquidacion,
					  tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
					  tipo_operacion, monto, monto_mn,
					  tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
					  id_usuario)
		  VALUES
			(sc_posicion_log_seq.nextval,
			 sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
			 :NEW.id_canal,sc_tipo_traspaso_producto.de,
			 'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
			 'CO',-monto, -(monto * tipo_cambio_cliente),
			 tipo_cambio_cliente, tipo_cambio_pizarron, 0,
			 sc_deal_posicion.id_usuario);
		END IF;
		-- Para el caso de interbancario se borra por completo tanto en las
		-- compras como en las ventas ya que se 'neteo' toda la operacion
		-- y no tiene sentido el netear los interbancarios, por lo que se
		-- pierde la informacion de utilidad por interbancario.
		-- Este caso no puede existir posicion en interbancario ya que
		-- interbancario solo opera TRAEXT. Aun asi se deja el codigo para
		-- mas hacerlo general
		IF (monto_in_neteo >= 1.0) THEN
		  INSERT INTO sc_posicion_log (
					  id_posicion_log,
					  id_mesa_cambio, id_divisa,
					  id_canal, clave_forma_liquidacion,
					  tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
					  tipo_operacion, monto, monto_mn,
					  tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
					  id_usuario)
		  VALUES
			(sc_posicion_log_seq.nextval,
			 sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
			 :NEW.id_canal,sc_tipo_traspaso_producto.de,
			 'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
			 'CI',-monto_in_neteo,-(monto_in_neteo * tipo_cambio_mesa),
			 tipo_cambio_mesa, 0, 0,
			 sc_deal_posicion.id_usuario);
		END IF;
		-- se inserta la contraparte del neteo
		IF (oper_in = 'VI') THEN
		  monto_in_neteo := sc_posicion_detalle.venta_in_cash - monto_in;
		  -- ^ para no aplicarlo dos veces, no puede ser menor a cero
		ELSE
		  monto_in_neteo := sc_posicion_detalle.venta_in_cash;
		END IF;
		monto := sc_posicion_detalle.venta_cash - monto_in_neteo;
		IF (monto >= 1.0) THEN
		  INSERT INTO sc_posicion_log (
					  id_posicion_log,
					  id_mesa_cambio, id_divisa,
					  id_canal, clave_forma_liquidacion,
					  tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
					  tipo_operacion, monto, monto_mn,
					  tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
					  id_usuario)
		  VALUES
			(sc_posicion_log_seq.nextval,
			 sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
			 :NEW.id_canal,sc_tipo_traspaso_producto.de,
			 'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
			 'VE',-monto,-(monto * tipo_cambio_cliente),
			 tipo_cambio_cliente, tipo_cambio_pizarron, 0,
			 sc_deal_posicion.id_usuario);
		END IF;
		IF (monto_in_neteo >= 1.0) THEN
		  INSERT INTO sc_posicion_log (
					  id_posicion_log,
					  id_mesa_cambio, id_divisa,
					  id_canal, clave_forma_liquidacion,
					  tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
					  tipo_operacion, monto, monto_mn,
					  tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
					  id_usuario)
		  VALUES
			(sc_posicion_log_seq.nextval,
			sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
			:NEW.id_canal,sc_tipo_traspaso_producto.de,
			'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
			'VI',-monto_in_neteo, -(monto_in_neteo * tipo_cambio_cliente),
			tipo_cambio_cliente, 0, 0,
			sc_deal_posicion.id_usuario);
		END IF;
	  ELSE -- IF compras > ventas
		IF (sc_posicion_detalle.compra_cash >= 1.0) AND
			((sc_posicion_detalle.venta_cash - sc_posicion_detalle.compra_cash) >= 1.0) THEN
		  -- se esta en corto se netea con las compras_cash
		  tipo_cambio_cliente := sc_posicion_detalle.venta_mn_cliente_cash /
								 sc_posicion_detalle.venta_cash;
		  tipo_cambio_pizarron := sc_posicion_detalle.venta_mn_pizarron_cash /
								  sc_posicion_detalle.venta_cash;
		  IF (sc_posicion_detalle.venta_in_cash >= 1.0) THEN
			tipo_cambio_mesa := sc_posicion_detalle.venta_mn_mesa_cash /
								sc_posicion_detalle.venta_in_cash;
		  END IF;
		  -- se deja la utilidad en compras
		  IF (oper_in = 'CI') THEN
			monto_in_neteo := sc_posicion_detalle.compra_in_cash - monto_in;
			-- ^ para no aplicarlo dos veces, no puede ser menor a cero
		  ELSE
			monto_in_neteo := sc_posicion_detalle.compra_in_cash;
		  END IF;
		  monto := sc_posicion_detalle.compra_cash - monto_in_neteo;
		  IF (monto > 1.0) THEN
			INSERT INTO sc_posicion_log (
						id_posicion_log,
						id_mesa_cambio, id_divisa,
						id_canal, clave_forma_liquidacion,
						tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
						tipo_operacion, monto, monto_mn,
						tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
						id_usuario)
			VALUES
			  (sc_posicion_log_seq.nextval,
			   sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
			   :NEW.id_canal,sc_tipo_traspaso_producto.de,
			   'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
			   'CO',-monto,-(monto * tipo_cambio_cliente),
			   tipo_cambio_cliente, tipo_cambio_pizarron, 0,
			   sc_deal_posicion.id_usuario);
		  END IF;
		  -- Para el caso de interbancario se borra por completo tanto en las
		  -- compras como en las ventas ya que se 'neteo' toda la operacion
		  -- y no tiene sentido el netear los interbancarios, por lo que se
		  -- pierde la informacion de utilidad por interbancario.
		  -- Este caso no puede existir posicion en interbancario ya que
		  -- interbancario solo opera TRAEXT. Aun asi se deja el codigo para
		  -- mas hacerlo general
		  IF (monto_in_neteo >= 1.0) THEN
			INSERT INTO sc_posicion_log (
						id_posicion_log,
						id_mesa_cambio, id_divisa,
						id_canal, clave_forma_liquidacion,
						tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
						tipo_operacion, monto, monto_mn,
						tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
						id_usuario)
			VALUES
			  (sc_posicion_log_seq.nextval,
			  sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
			  :NEW.id_canal,sc_tipo_traspaso_producto.de,
			  'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
			  'CI',-monto_in_neteo,-(monto_in_neteo * tipo_cambio_mesa),
			  tipo_cambio_mesa, 0, 0,
			  sc_deal_posicion.id_usuario);
		  END IF;
		  -- se inserta la contraparte del neteo
		  IF (oper_in = 'VI') THEN
			monto_in_neteo := sc_posicion_detalle.venta_in_cash - monto_in;
			-- ^ para no aplicarlo dos veces, no puede ser menor a cero
		  ELSE
			monto_in_neteo := sc_posicion_detalle.venta_in_cash;
		  END IF;
		  monto := sc_posicion_detalle.compra_cash - monto_in_neteo;
		  IF (monto >= 1.0) THEN
			INSERT INTO sc_posicion_log (
						id_posicion_log,
						id_mesa_cambio, id_divisa,
						id_canal, clave_forma_liquidacion,
						tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
						tipo_operacion, monto, monto_mn,
						tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
						id_usuario)
			VALUES
			  (sc_posicion_log_seq.nextval,
			   sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
			   :NEW.id_canal,sc_tipo_traspaso_producto.de,
			   'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
			   'VE',-monto,-(monto * tipo_cambio_cliente),
			   tipo_cambio_cliente, tipo_cambio_pizarron, 0,
			   sc_deal_posicion.id_usuario);
		  END IF;
		  IF (monto_in_neteo >= 1.0) THEN
			-- No puede existir  posicion en interbancario ya que interbancario
			-- solo opera TRAEXT. Aun asi se deja el codigo para hacerlo general
			INSERT INTO sc_posicion_log (
						id_posicion_log,
						id_mesa_cambio, id_divisa,
						id_canal, clave_forma_liquidacion,
						tipo_valor, id_deal, id_deal_posicion, nombre_cliente,
						tipo_operacion, monto, monto_mn,
						tipo_cambio_cliente, tipo_cambio_mesa, precio_referencia,
						id_usuario)
			VALUES
			  (sc_posicion_log_seq.nextval,
			   sc_deal_posicion.id_mesa_cambio,sc_deal_posicion.id_divisa,
			   :NEW.id_canal,sc_tipo_traspaso_producto.de,
			   'CASH', NULL,:NEW.id_deal_posicion,'Traspaso',
			   'VI',-monto_in_neteo, -(monto_in_neteo * tipo_cambio_mesa),
			   tipo_cambio_mesa, 0, 0,
			   sc_deal_posicion.id_usuario);
		  END IF;
		END IF; -- IF corto
	  END IF; -- IF largo
    END IF; -- IF traspaso neteo = 1
  END IF; -- IF (:NEW.ID_CANAL IS NULL) THEN
END TraspasoProducto;
/