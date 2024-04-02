/*
 * $Id: PosicionDivisasCommand.as,v 1.9.30.1 2010/06/16 15:45:47 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.Event;

import mx.core.UIObject;

/**
 * Este comando implementa una secuencia de comandos:
 * <ul>
 * <li>Obtiene la posici&oacute;n para las divisas.</li>
 * <li>Obtiene el DataProvider para la vista de la posici&oacute;n de las divisas. </li>
 * </ul>
 * 
 * @author David Solis, Jean C, Favila.
 */
class com.ixe.ods.sica.posicion.commands.PosicionDivisasCommand extends com.ixe.ods.sica.posicion.commands.BaseCommand
{
	/**
	 * Obtiene la posici&oacute;n para las divisas.
	 * 
	 * 
	 * @param event El evento generado por el comando.
	 */
    function execute(event : Event) : Void
    {
       trace("PosicionDivisasCommand.execute");
       var idMesaCambio:Number = event.data.idMesaCambio;
       delegate.getPosicionDivisas(idMesaCambio);
    }
	
	/**
	 * Obtiene el DataProvider para la vista de la posici&oacute;n de las divisas.
	 * 
	 * 
	 * @param event El evento generado por el comando.
	 */
    public function onResult(event : Object) : Void
    {
       var view : UIObject = ViewLocator.getInstance().getView(Posicion.POSICION_DIVISAS_VIEW);
       var dataProvider : Array = event.result;
       initialiseDataProvider(dataProvider);
       view['updateDataProvider'](dataProvider);
    }
	
	/**
	 * Asigna el valor para el DataProvider.
	 * 
	 * @param dataProvider El arreglo con los VO de Posicion.
	 */
    private function initialiseDataProvider(dataProvider : Array) : Void
    {
       for (var i : Number = 0; i < dataProvider.length; i++) {
          var vo : PosicionVO = PosicionVO(dataProvider[i]);
          vo.initialise();
       }
    }
	
	/**
	 * Toma el mensaje en caso de error,
	 * 
	 * @param event El evento generado por el comando.
	 */
	public function onFault(event : Object) : Void
	{
       trace("onFault->" + event.fault.faultstring);
    }
}
