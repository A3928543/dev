import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.ixe.ods.sica.posicion.vo.TipoCambioVO;

import mx.utils.Delegate;

/**
 * Clase que define los montos para la Utilidad Global de la Posici&oacute;n.
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.views.Utilidad extends mx.containers.VBox
{
	/**
	 * El DataProvider del componente (PosicionVO).
	 */
    var dataProvider : PosicionVO;
    
    /**
	 * El tipo de cambio.
	 */
    var tipoCambio : TipoCambioVO;
    
    /**
	 * El tipo de cambio para mercado. 
	 */
    var tipoCambioMercado : Number;
    
    /**
	 * Define si la liga para Reuters esta habilitada. 
	 */
    var visibleReutersLink : Boolean = true;

	/**
	 * El tipo de cambio para mercado.
	 */
    private var __tipoCambioMercado : Number;


	/**
	 * El constructor de la clase.
	 */
    function Utilidad()
    {
    }

	/**
	 * M&eacute;todo que se ejecuta al iniciar el componentel; registra los eventos para el componente.
	 */
	function init() : Void
	{
	   super.init();
       addEventListener("childrenCreated", Delegate.create(this, childrenCreated));
	}

	/**
	 * Registra los eventos para el componente.
	 */
	private function childrenCreated() : Void
	{
       this['reutersLink'].addEventListener("click", Delegate.create(this, showField));
       this['formulaLink'].addEventListener("click", Delegate.create(this, showFormula));
       this['tcInput'].addEventListener("enter", Delegate.create(this, enter));
    }

	/**
	 * Inhabilita el campo de texto.
	 */
    function reset() : Void
    {
       this['inputBox'].visible = false; 
    }

	/**
	 * Asigna el monto para Tipo Cambio Mercado.
	 * 
	 * @param event El evento del que se obtiene el valor de Tipo Cambio Mercado.
	 */
    private function enter(event) : Void
    {
       tipoCambioMercado = Number(event.target.text);
    }

	/**
	 * Muestra el campo para el valor de Tipo Cambio Mercado.
	 */
    private function showField() : Void
    {
       var inputBox = this['inputBox'];
       if (!inputBox.visible) {
          inputBox.visible = true;
          __tipoCambioMercado = tipoCambioMercado;
          this['tcInput'].text = this['exchangeFormatter'].format(tipoCambioMercado);
       }
       else {
          inputBox.visible = false;
          tipoCambioMercado = __tipoCambioMercado;
       }
    }

	/**
	 * Muestra el detalle de los montos para la Utilidad Global.
	 * 
	 * @param event El elemento seleccionado del DataGrid.
	 */
    private function showFormula(event) : Void
    {
       this['formulaDetail'].visible = event.target.selected;
    }
}