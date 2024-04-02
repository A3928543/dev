import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.vo.UtilidadGlobalVO;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.EventBroadcaster;

import mx.utils.Delegate;

/**
 * Clase que muestra los montos para la Utilidad Global de la posici&oacute;n.
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.views.UtilidadGlobal extends mx.containers.VBox
{
	/**
	 * El DataProvider de la vista.
	 */
    var dataProvider : UtilidadGlobalVO;

	/**
	 * Constructor de la clase.
	 */
    function UtilidadGlobal()
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
	 * Registra los eventos para el componente; sincroniza la informaci&oacute;n del
	 * monitor con la generada actualmente.
	 */
	private function childrenCreated() : Void
	{
       ViewLocator.getInstance().registerView(Posicion.UTILIDAD_GLOBAL_VIEW, this);
       this['synchLink'].addEventListener("click", Delegate.create(this, sync));
    }

	/**
	 * Envia un mensaje de BroadCast para notificar el evento de utilidad global.
	 */
    private function sync() : Void
    {
       EventBroadcaster.getInstance().broadcastEvent(Posicion.UTILIDAD_GLOBAL_COMMAND);
    }

	/**
	 * Actualiza los datos del DataPovider.
	 * 
	 * @param value El valor actualizado  para la utilidad (UtilidadGlobalVO).
	 */
    function updateDataProvider(value : UtilidadGlobalVO) : Void
    {
       if (dataProvider != undefined)
          value.computeTick(dataProvider);
       dataProvider = value;
    }
}