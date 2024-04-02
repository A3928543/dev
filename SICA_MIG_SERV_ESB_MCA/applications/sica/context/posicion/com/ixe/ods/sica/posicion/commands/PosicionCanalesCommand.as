/*
 * $Id: PosicionCanalesCommand.as,v 1.9 2008/02/22 18:25:36 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.vo.PosicionGrupoVO;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.Event;

import mx.core.UIObject;

/**
 * Este comando implementa una secuencia de comandos:
 * <ul>
 * <li>Obtiene la posici&oacute;n para los canales / divisa.</li>
 * <li>Obtiene el DataProvider para la vista de la posici&oacute;n de los canales / divisa. </li>
 * </ul>
 * 
 * @author David Solis, Jean C, Favila.
 */
class com.ixe.ods.sica.posicion.commands.PosicionCanalesCommand extends com.ixe.ods.sica.posicion.commands.BaseCommand
{	
	
	/**
	 * Obtiene la posici&oacute;n para los canales / divisa.
	 * 
	 * @param event El evento generado por el comando.
	 */
    function execute(event : Event) : Void
    {
       trace("PosicionCanalesCommand.execute");
       trace("PosicionCanalesCommand idMesaCambio->" + idMesaCambio + " idDivisa->" + idDivisa);
       delegate.getPosicionCanales(idMesaCambio, idDivisa);
    }

	/**
	 * Obtiene el DataProvier para la vista de la posici&oacute;n de los canales / divisa
	 * 
	 * @param event El evento generado por el comando.
	 */
    public function onResult(event : Object) : Void
    {
       var view : UIObject = ViewLocator.getInstance().getView(Posicion.POSICION_GRUPO_VIEW);
       var dataProvider : Array = event.result;
       if (dataProvider != null && dataProvider.length > 0) {
          initialiseDataProvider(dataProvider);
          view['dataProvider'] = dataProvider;
          view['isGrupoCanales'] = true;
          selectTopAndBottomViews(Posicion.MESA_VIEW_INDEX, Posicion.CANALES_VIEW_INDEX);
       }
       else {
          selectTopAndBottomViews(Posicion.MESA_VIEW_INDEX, Posicion.BOTTOM_NOT_AVAILABLE_INDEX);
       }
    }
	
	/**
	 * Asigna el valor para el DataProvider.
	 * 
	 * @param dataPovider El arreglo con los VO de la posicion del grupo.
	 */
    private function initialiseDataProvider(dataProvider : Array) : Void
    {
       for (var i : Number = 0; i < dataProvider.length; i++) {
          var vo : PosicionGrupoVO = PosicionGrupoVO(dataProvider[i]);
          vo.initialise();
       }
    }

}
