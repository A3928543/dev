import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.EventBroadcaster;

import mx.utils.Delegate;

/**
 * Clase que define el comportamiento de la vista que muestra los montos
 * de la posici&oacute;n por productos. 
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.views.PosicionProducto extends mx.containers.VBox
{
	
	/**
	 * El separardor del texto.
	 */
    private static var SEPARATOR : String = ", ";

	/**
	 * El DataProvider de la vista.
	 */
    var dataProvider : PosicionVO;

	/**
	 * El constructor de la clase.
	 */
    function PosicionProducto()
    {
    }

	/**
	 * M&eacute;todo de se ejecuta al iniciar el componente; registra los eventos para el componente.
	 */
	function init() : Void
	{
	   super.init();
       addEventListener("childrenCreated", Delegate.create(this, childrenCreated));
	}


	/**
	 * M&eacute;todo que registra los eventos para el componente; define cuando la vista de
	 * la posici&oacute;n por producto ha sido seleccionada.
	 */
	private function childrenCreated() : Void
	{
       ViewLocator.getInstance().registerView(Posicion.POSICION_PRODUCTO_VIEW, this);
	}

	/**
	 * Actualiza la etiqueta del producto seleccionado.
	 * 
	 * @param producto El producto seleccionado. 
	 */
	function updateLabel4Producto(producto : String) : Void
	{
       this['productoLabel'].text = "Producto:"
	   this['productos'].text = producto;
	   this['canalesBox'].visible = false;
	}

	/**
	 * Obtiene los productos contenidos en el arreglo.
	 * 
	 * @param productos El arreglo de productos.
	 */
	function updateLabel4Productos(productos : Array) : Void
	{
	   if (productos.length == 1)
	      updateLabel4Producto(productos[0]);
	   else {
          this['productoLabel'].text = "Productos:"
	      this['productos'].text = productos.join(SEPARATOR);
	      this['canalesBox'].visible = false;
	   }
	}

	/**
	 * Actializa y asigna los distintos productos que operan los canales.
	 * 
	 * @param canales El arreglo con los canales.
	 * @param productos El arreglo con los distintos productos para los canales.
	 */
	function updateLabel4CanalesProductos(canales: Array, productos : Array) : Void
	{
	   updateLabel4Productos(productos);
       this['canalLabel'].text = canales.length == 1 ? "Canal:" : "Canales:";
       this['canales'].text = canales.join(SEPARATOR);
       this['canalesBox'].visible = true;
	}
}
