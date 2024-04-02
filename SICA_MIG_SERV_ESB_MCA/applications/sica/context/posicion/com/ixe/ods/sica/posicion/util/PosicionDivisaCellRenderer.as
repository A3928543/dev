/*
 * $Id: PosicionDivisaCellRenderer.as,v 1.9 2008/02/22 18:25:54 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.ixe.ods.sica.posicion.vo.TipoCambioVO;

import mx.controls.Text;
import mx.formatters.NumberFormatter;

/**
 * Define el formato de texto que se muestra en el Monitor de la Posici&oacute;n
 * para los montos de la posici&oacute;n de divisas.
 * 
 * @author David Solis, Jean C, Favila.
 */
class com.ixe.ods.sica.posicion.util.PosicionDivisaCellRenderer extends Text
{

	/**
	 * Constante estatica que define el tama&ntilde;o de la letra para la etiqueta.
	 */
    private static var HEIGHT : Number = 30;
    
	/**
	 * 
	 */
	var listOwner : MovieClip;

	/**
	 * El componente para formatear los montos.
	 */
	var numberFormatter : NumberFormatter;
	
	/**
	 * El componente para formatear los montos.
	 */
	var exchangeFormatter : NumberFormatter;

	/**
	 * Inicializa la precisi&oacute;n para formatear los montos.
	 */
	function init() : Void
	{
		super.init();
		selectable = false;
		if (numberFormatter == undefined) {
		   numberFormatter = new NumberFormatter();
		   numberFormatter.precision = 2;
		}
		if (exchangeFormatter == undefined) {
		   exchangeFormatter = new NumberFormatter();
		   exchangeFormatter.precision = 6;
		}
	}

	/**
	 * Asigna el valor correspondiente al item seleccionado.
	 * 
	 * @param srt El valor para el item.
	 * @param item El item seleccionado.
	 * @param Define si el objeto esta o no seleccionado.
	 */
	function setValue(str : String, item : Object, sel : Boolean)
	{
		if (item != undefined) {
           var columnIndex = this["columnIndex"];
		   var columnName = listOwner.getColumnAt(columnIndex).columnName;
    	   var path : Array = columnName.split(".");
    	   var vo : PosicionVO = PosicionVO(item);
	       var tipoCambio : TipoCambioVO = vo[path[0]];
		   if (tipoCambio != undefined)
		      this.text = numberFormatter.format(tipoCambio[path[1]]) + newline + exchangeFormatter.format(tipoCambio[path[2]]);
		   else
		      this.text = "";
		}
		else {
			this.text = "";
		}
	}

	/**
	 * Define el tama&ntilde;o del texto.
	 */
	function size() : Void
	{
		this.setSize(layoutWidth, HEIGHT);
	}

}