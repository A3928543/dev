import com.ixe.ods.sica.posicion.vo.TipoCambioVO;

import mx.utils.Delegate;

/**
 * Grid generico para mostrar una variante de la posicion utilizando un dataProvider definido como
 * un tipo de cambio TipoCambioVO.
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.views.TipoCambio extends mx.containers.Grid
{
	
	/**
	 * El DataProvider para la vista (Value Object TipoCambioVO).
	 */
    var dataProvider : TipoCambioVO;

	/**
	 * Constructor de la clase.
	 */
    function TipoCambio()
    {
    }

	/**
	 * M&eacute;todo que se ejecuta al iniciar la clase; registra los eventos para el componente.
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
    }
}
