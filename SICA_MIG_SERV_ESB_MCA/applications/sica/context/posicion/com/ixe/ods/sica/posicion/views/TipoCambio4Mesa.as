import com.ixe.ods.sica.posicion.vo.TipoCambioVO;

import mx.utils.Delegate;

/**
 * Clase que define el comportamiento de la vista que muestra los detalles
 * de los montos para los tipos de cambio para el cliente, la mesa y operaciones
 * interbancarias; muestra la informaci&oacute;n de los montos de la posici&oacute;n
 * para compras y ventas en CASH, TOM y SPOT, totales por operaci&oacute;n y 
 * posici&oacute;n final en divisa y pesos mexicanos.
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.views.TipoCambio4Mesa extends mx.containers.VBox
{
	/**
	 * El DataProvider de la vista (Value Object TipoCambioVO).
	 */
    var dataProvider : TipoCambioVO;
    
    /**
     * Imagen para salir de la vista del detalle de tipo de cambio. 
     */
    [Embed(source="resources/zoom_out.png")]
    var zoomOut : String;
    
    /**
     * Imagen para ver la vista del detalle de tipo de cambio.
     */
    [Embed(source="resources/zoom_in.png")]
    var zoomIn : String;

    /**
     * Constructor para la clase.
     */
    function TipoCambio4Mesa()
    {
    }

    /**
     * M&eacute;todo que se ejecuta al iniciar el componente; registra los eventos para el componente.
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