/*
 * $Id: UpdateTipoCambioCommand.as,v 1.9 2008/02/22 18:25:36 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.ixe.ods.sica.posicion.vo.TipoCambioVO;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.Event;

import mx.core.UIObject;

/**
 * Este comando implementa una secuencia de comandos:
 * <ul>
 * <li>Actualiza los valores de los tipos de cambio.</li>
 * </ul>
 * 
 * @author David Solis, Jean C, Favila.
 */
class com.ixe.ods.sica.posicion.commands.UpdateTipoCambioCommand extends com.ixe.ods.sica.posicion.commands.AbstractCommand
{
	/**
	 * Actualiza los valores de los tipos de cambio.
	 * 
	 * @param event El evento generado por el comando.
	 */
    function execute(event : Event) : Void
    {
       var viewName : String = event.data.viewName;
       var view : UIObject = ViewLocator.getInstance().getView(viewName);
       var dataProvider : PosicionVO = view['dataProvider'];
       var tipoCambio : TipoCambioVO;
       var index : Number = event.data.index;
	   if (index == 0)
	      tipoCambio = dataProvider.tipoCambioCliente;
	   else if (index == 1)
	      tipoCambio = dataProvider.tipoCambioPizarron;
	   else
	      tipoCambio = dataProvider.tipoCambioMesa;
	   view['tipoCambio'] = tipoCambio;
    }
}