import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.ixe.ods.sica.posicion.vo.TipoCambioVO;

import mx.utils.Delegate;

/**
 * Clase que define el comportamiento de la vista del Accordion de los Tipos de Cambio
 * para el Pizarron, Cliente y Mesa del monitor de la posici&oacutel;n.
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.views.TipoOperacion extends mx.containers.Accordion
{
	
	/**
	 * El DataProvider para el tipo de cambio del cliente.
	 */
    var tipoCambioCliente : TipoCambioVO;
    
   	/**
	 * El DataProvider para el tipo de cambio del pizarr&oacute;n.
	 */
    var tipoCambioPizarron : TipoCambioVO;
    
    /**
	 * El DataProvider para el tipo de cambio de la mesa. 
	 */
    var tipoCambioMesa : TipoCambioVO;

	/**
	 * Constructor de la clase.
	 */
    function TipoOperacion()
    {
    }

	/**
	 * M&eacute;todo que se ejecuta cuando se inicia el componente; registra los eventos para el componente.
	 */
	function init() : Void
	{
	   super.init();
       addEventListener ("childrenCreated", Delegate.create(this, childrenCreated));
	}

	/**
	 * Registra los eventos para el componente.
	 */
	private function childrenCreated() : Void
	{
	}

	/**
	 * Asigna el DataProvider del componente (TipoCambioVO para pesta&ntilde;a del Accordion).
	 * 
	 * @param dataProvider El DataProvider (PosicionVO) para el componente.
	 */
	function set dataProvider(dataProvider : PosicionVO) : Void
	{
       tipoCambioCliente = dataProvider.tipoCambioCliente;
       tipoCambioPizarron = dataProvider.tipoCambioPizarron;
       tipoCambioMesa = dataProvider.tipoCambioMesa;
	}
}