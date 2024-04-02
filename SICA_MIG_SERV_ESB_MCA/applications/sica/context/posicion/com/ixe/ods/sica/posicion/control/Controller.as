/*
 * $Id: Controller.as,v 1.9 2008/02/22 18:25:49 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.commands.*;

/**
 * Agrega los comandos definidos al controlador de la applicaci&oacute;n.
 *
 * @author: David Solis, Jean C. Favila
 * @version $Revision: 1.9 $ $Date: 2008/02/22 18:25:49 $
 */
class com.ixe.ods.sica.posicion.control.Controller extends org.nevis.cairngorm.control.FrontController
{
	/**
	 * Constructor de la clase; llama a initialiseCommands().
	 */
    public function Controller()
    {
        initialiseCommands();
    }

	/**
	 * Agrega al controlador de la aplicaci&oacute;n los comandos definidos.
	 */
    public function initialiseCommands() : Void
    {
       addCommand(Posicion.BLOTTER_COMMAND, new BlotterCommand());
       addCommand(Posicion.PARAMETROS_PIZARRON_COMMAND, new ParametrosPizarronCommand());
       addCommand(Posicion.DIVISAS_COMMAND, new DivisasCommand());
       addCommand(Posicion.POSICION_MESA_COMMAND, new PosicionMesaCommand());
       addCommand(Posicion.TIMER_COMMAND, new TimerCommand());
       addCommand(Posicion.POSICION_DIVISAS_COMMAND, new PosicionDivisasCommand());
       addCommand(Posicion.UTILIDAD_GLOBAL_COMMAND, new UtilidadGlobalCommand());
       addCommand(Posicion.POSICION_CANALES_COMMAND, new PosicionCanalesCommand());
       addCommand(Posicion.POSICION_PRODUCTOS_COMMAND, new PosicionProductosCommand());
       addCommand(Posicion.POSICION_SUCURSALES_COMMAND, new PosicionSucursalesCommand());
       addCommand(Posicion.POSICION_EFECTIVO_COMMAND, new PosicionEfectivoCommand());
       addCommand(Posicion.POSICION_PRODUCTO_COMMAND, new PosicionProductoCommand());
       addCommand(Posicion.POSICION_CANAL_COMMAND, new PosicionCanalCommand());
       addCommand(Posicion.QUERY_POSICION_CANALES_COMMAND, new QueryPosicionCanalesCommand());
       addCommand(Posicion.QUERY_POSICION_PRODUCTOS_COMMAND, new QueryPosicionProductosCommand());
       addCommand(Posicion.QUERY_POSICION_CANALES_PRODUCTOS_COMMAND, new QueryPosicionCanalesProductosCommand());
       addCommand(Posicion.SHOW_MESA_COMMAND, new ShowMesaCommand());
       addCommand(Posicion.ARBOL_COMMAND, new ArbolCommand());
       addCommand(Posicion.SHOW_APPLET_COMMAND, new ShowAppletCommand());
       addCommand(Posicion.UPDATE_TIPO_CAMBIO_COMMAND, new UpdateTipoCambioCommand());
       addCommand(Posicion.POSICION_INVENTARIO_EFECTIVO_COMMAND, new PosicionInventarioEfectivoCommand());
       addCommand(Posicion.QUERY_POSICION_INVENTARIO_EFECTIVO_COMMAND, new QueryPosicionInventarioEfectivoCommand());
       addCommand(Posicion.VALOR_FUTURO_COMMAND, new ValorFuturoCommand());
    }
}
