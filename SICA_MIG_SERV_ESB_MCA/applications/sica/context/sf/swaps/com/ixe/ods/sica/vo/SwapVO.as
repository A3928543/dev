/*
 * $Id: SwapVO.as,v 1.9 2008/02/22 18:25:44 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

/**
 * Value Object que contiene los valores de un Swap que se crea desde la interfaz de Captura de Swaps.
 * 
 * @author: Jean C. Favila
 * @version $Revision: 1.9 $ $Date: 2008/02/22 18:25:44 $
 */
class com.ixe.ods.sica.vo.SwapVO
{
	
	/**
	 * La clase para el VO. 
	 */
    static var registered = Object.registerClass("com.ixe.ods.sica.vo.SwapVO", SwapVO);

	/**
	 * El folio para el Swap.
	 */
    var folioSwap    : Number;
    
    /**
	 * La contraparte del Swap.
	 */
    var contraparte  : com.ixe.ods.sica.vo.BrokerVO;

	/**
	 * El id del canal de operac&oacute;n.
	 */
    var idCanal      : String;
    
    /**
	 * El id de la divisa. 
	 */
    var idDivisa     : String;
    
    /**
	 * El id de la mesa de cambio. 
	 */
    var idMesaCambio : Number;
    
    /**
	 * El id del usuario que realiza la captura. 
	 */
    var idUsuario    : Number;
    
    /**
	 * Define si la operaci&oacute;n es compra o venta. 
	 */
    var compra       : Boolean;
    
    /**
	 * El arreglo de detalles de Swap. 
	 */
    var detalles     : Array;

	/**
	 * Constructor que inicializa los valores de las variables de instancia.
	 * 
	 * @param folioSwap El folio para el Swap.
	 * @param contraparte El Broker para el Swap.
	 * @param idCanal El id del Canal de Operaci&oacute;n.
	 * @param idDivisa El id de la Divisa.
	 * @param idMesaCambio El id de la mesa de cambio.
	 * @param compra Define si la operaci&oacute;n es compra o venta.
	 * @param idUsuario El id del usuario.
	 * @param detalles El arreglo de detalles de Swap.
	 */
    function SwapVO(folioSwap : Number, contraparte : com.ixe.ods.sica.vo.BrokerVO, idCanal : String, idDivisa : String,
                    idMesaCambio : Number, compra : Boolean, idUsuario : Number, detalles : Array)
    {
        this.folioSwap = folioSwap;
        this.contraparte = contraparte;
        this.idCanal = idCanal;
        this.idDivisa = idDivisa;
        this.idMesaCambio = idMesaCambio;
        this.compra = compra;
        this.idUsuario = idUsuario;
        this.detalles = detalles;
    }

	/**
	 * Obtiene el valor de los atributros del objeto en forma de String.
	 * 
	 * @return String.
	 */
    function toString() : String
    {
        return 'SwapVO: ' + folioSwap + ' ' + contraparte + ' ' + idDivisa + ' ' + compra + ' ' + detalles.length;
    } 
}
