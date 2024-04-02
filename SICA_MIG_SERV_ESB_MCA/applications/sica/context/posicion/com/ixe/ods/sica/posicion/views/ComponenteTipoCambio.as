/*
 * $Id: ComponenteTipoCambio.as,v 1.9 2008/02/22 18:25:27 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.vo.TipoCambioVO;

import mx.utils.Delegate;

/**
 * GridRow generico para mostrar una variante de la posicion utilizando un dataProvider definido como
 * un tipo de cambio TipoCambioVO.
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.views.ComponenteTipoCambio extends mx.containers.GridRow
{
	
	/**
	 * El DataProvider para el tipo de cambio.
	 */
    var dataProvider : TipoCambioVO;

	/**
	 * Constructor de la clase.
	 */
    function ComponenteTipoCambio()
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
    }
}
