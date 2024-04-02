/*
 * $Id: PosicionDivisaDetalle.as,v 1.9.30.1 2010/06/16 15:59:44 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.ixe.ods.sica.posicion.vo.TipoCambioVO;
import com.ixe.ods.sica.posicion.Posicion;

import mx.utils.Delegate;

/**
 * Clase que define el comportamiento para la vista del
 * Detalle de la Posicion por Divisa.
 * 
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.views.PosicionDivisaDetalle extends mx.containers.VBox
{
	/**
	 * El DataProvider para la vista del detalle.
	 */
    var dataProvider : Array;
    
   	/**
	 * Las columnas para el DataGrid.
	 */
    var columns : Array;
   
	/**
	 * El id del tipo de cambio.
	 */
    var tipoCambioId : String;

	/**
    * El id de la Mesa de Cambio. 
    */
    var idMesaCambio : Number = Posicion.OPERACION;

	/**
	 * Constructor para la clase.
	 */
    function PosicionDivisaDetalle()
    {
    }

	/**
	 * M&acute;todo que se ejecuta al iniciar el componoente; registra los eventos del componente. 
	 */
	function init() : Void
	{
	   super.init();
       addEventListener("childrenCreated", Delegate.create(this, childrenCreated));
	}

	
	/**
	 * M&eacute;todo que registra los eventos para el componente.
	 */
	private function childrenCreated() : Void
	{
       this['dataGrid'].addEventListener("change", Delegate.create(this, changeDataGridSelection));
	}

	/**
	 * Actualiza los datos del DataGrid con base en la selecci&oacute;n de la Posicion.
	 */
    private function changeDataGridSelection() : Void
    {
       var dataGrid = this['dataGrid'];
       var viewStack = this['viewStack'];
       if (dataGrid.selectedIndex == undefined) {
          viewStack.selectedIndex = 0;
       }
       else {
          var vo : PosicionVO = PosicionVO(dataGrid.dataProvider.getItemAt(dataGrid.selectedIndex));
          var tipoCambio : TipoCambioVO = vo[tipoCambioId];
          this['posicionDivisaComprasVentas'].dataProvider = tipoCambio;
          viewStack.selectedIndex = 1;
       }
    }
}
