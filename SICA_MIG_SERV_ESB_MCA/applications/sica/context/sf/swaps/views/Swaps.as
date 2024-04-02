/*
 * $Id: Swaps.as,v 1.13 2010/04/13 15:29:11 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2010 LegoSoft S.C.
 */

import com.ixe.ods.sica.vo.*;

/**
 * Pantalla de captura de Swaps de Contado.
 *
 * Author: Jean C. Favila
 * Version $Revision: 1.13 $ $Date: 2010/04/13 15:29:11 $
 */
class views.Swaps extends mx.core.Application
{

	/**
     * Inicializa las variables de instancia y llama al m&#233;todo findContrapartes, findAllDivisas y
     * findIdsSectoresUltimaFechaValor de swapsService.
     */
	public function inicializar(idCanal : String, idMesaCambio : Number, idUsuario : Number) : Void
	{
	    this.idCanal = idCanal;
	    this.idMesaCambio = idMesaCambio;
	    this.idUsuario = idUsuario;
	    this['swapsService'].findContrapartes();
	    this['swapsService'].findAllDivisas();
	    this['swapsService'].findIdsSectoresUltimaFechaValor();
	    productos = new Array();
	}

	/**
     * Recibe la lista de contrapartes (BrokerVO).
     *
     * @param lista La lista de contrapartes encontradas.
 	 */
	private function respFindContrapartes(lista : Array) : Void
	{
		contrapartes = lista;
		contrapartesVisibles = lista;
		this['principal'].status = '';
	}

    /**
     * Recorre la lista de contrapartes. Las contrapartes cuyo nombreCompleto comience con la letra especificada, son
     * agregadas a la lista contrapartesVisibles. Si letra es '0', contrapartesVisibles tendr&#225; todos los objetos
     * de la lista contrapartes. (S&#243;lo en modo Contado).
     *
     * @param letra La letra para filtrar las contrapartes.
     */
	private function filtrarContrapartes(letra : String) : Void
	{
		if (letra == '0') {
			contrapartesVisibles = contrapartes;
		}
		else {
			contrapartesVisibles = new Array();
			var i : Number;
			for (i = 0; i < contrapartes.length; i++) {
				if (contrapartes[i].nombreCompleto.charAt(0) == letra) {
					contrapartesVisibles[contrapartesVisibles.length] = contrapartes[i];
				}
			}
		}
	}

	public function capturarOtro() : Void
	{
		this['capSwap'].capturarOtro();
	        this['acordion'].selectedIndex = 0;
	}

    /**
     * Obtiene del grid de contapartes, la contraparte seleccionada para capturar un Swap; ademas,
     * crea los items del combo box de fechas valor para el Broker, dependiendo si este puede operar
     * en Valor Futuro.
     */
	private function crearSwap(contraparte : BrokerVO) : Void
	{
	    if (contraparte != null) {
	        this['acordion'].selectedIndex = 2;
	        this.contraparte = contraparte;
	        var operaVFut = false;
	        var i : Number;
	        for (i = 0; i < idsSectores.length; i++) {
	        	if(contraparte.idSector == idsSectores[i]) {
	            	operaVFut = true;
	        	}
	        }
	        this['capSwap'].operaVFut = operaVFut;
	        this['capSwap'].fechasValor = this['capSwap'].crearFechasValor();
	    }
	}

	/**
	 * Obtiene el resultado a la llamada al servicio SwapsService; obtiene el
	 * arreglo de los productos para operar por mesa de cambio, canal y
	 * divisa.
	 */
	private function respFindAllDivisas(lista : Array) : Void
	{
	    divisas = lista;
		this['swapsService'].findProductos(idMesaCambio, idCanal, divisas[0].idDivisa);
	}

	/**
	 * Sincroniza la informacion de los datos de los swaps capturados.
	 */
	private function respFindProductos(lista : Array) : Void
	{
	    productos = lista;
	    this['capSwap'].sincronizar();
	}

	/**
	 * Obtiene el resultado a la llamada al servicio SwapsService; obtiene
	 * el arreglo de sectores que pueden operar Valor Futuro.
	 *
	 * @param lista El arreglo de sectores que operan Valor Futuro.
	 */
	private function respFindIdsSectoresUltimaFechaValor(lista : Array) : Void
	{
	    idsSectores = lista;
	}

	/**
	 * Obtiene el mensaje en caso de error y lo muestra en una ventana de Alert.
	 *
	 * @param falla El mensaje de error.
	 */
	public function falla(falla) : Void
	{
	    mx.controls.Alert.show(falla.description, 'Atenci\u00f3n:');
	}

	/**
	 * El id del canal de operaci&oacute;n.
	 */
	private var idCanal              : String;

	/**
	 * El id de la mesa de cambios.
	 */
    private var idMesaCambio         : Number;

    /**
     * El id del usuario.
     */
    private var idUsuario            : Number;

    /**
     * El arreglo de contrapartes.
     */
	private var contrapartes         : Array;

	/**
     * El arreglo de contrapartes que se muestran en el Accordion.
     */
	private var contrapartesVisibles : Array;

	/**
     * La contraparte seleccionada.
     */
	private var contraparte          : BrokerVO;

	/**
     * El arreglo de divisas.
     */
	private var divisas              : Array;

	/**
     * El arreglo de productos.
     */
	private var productos            : Array;

	/**
     * El arreglo de Id's de sectores que operan en Valor Fututo.
     */
	public var idsSectores 		   : Array;

}