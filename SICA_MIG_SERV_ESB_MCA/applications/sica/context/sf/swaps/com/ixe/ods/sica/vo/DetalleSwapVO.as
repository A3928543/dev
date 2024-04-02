/*
 * $Id: DetalleSwapVO.as,v 1.9 2008/02/22 18:25:44 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */

/**
 * Value Object que contiene la informacion de los detalles de Swap que fue capturado (Deal).
 * 
 * @author: Jean C. Favila
 * @version $Revision: 1.9 $ $Date: 2008/02/22 18:25:44 $
 */
class com.ixe.ods.sica.vo.DetalleSwapVO
{
	
	/**
	 * La clase para el VO, 
	 */
    static var registered = Object.registerClass("com.ixe.ods.sica.vo.DetalleSwapVO", DetalleSwapVO);
    
	/**
	 * El id del Swap (idDeal).
	 */
    var idDeal : Number;
    
    /**
	 * El tipo de Swap. 
	 */
    var tipo : String;
    
    /**
	 * Define si fue compra o venta. 
	 */
    var compra : Boolean;
    
    /**
	 * La fecha valor del Swap. 
	 */
	var fechaValor : String;

	/**
	 * La fecha de captura del Swap.
	 */
	var fechaCaptura : Date;
	
	/**
	 * La feche de liquidaci&oacute;n del detalle. 
	 */
	var fechaLiquidacion : Date;
	
	/**
	 * La clave de la forma de liquidaci&oacute;n del detalle. 
	 */
	var claveFormaLiquidacion : String;
	
	/**
	 * El monto del detalle. 
	 */
	var monto : Number;
	
	/**
	 * El tipo de cambio del detalle. 
	 */
	var tipoCambio : Number;

	/**
	 * Constructor de la Clase. Inicializa los valores de las variables de instancia.
	 * 
	 * @param tipo El tipo de Swap.
	 * @param compra Define si la operaci&oacute;n fue compra o venta.
	 * @param fechaValor La fecha valor del Swap.
	 * @param fechaLiquidacion La fecha de liquidaci&oacute;n del detalle.
	 * @param claveFormaLiquidacion La forma de liquidaci&oacute;n del detalle.
	 * @param monto El monto del detalle.
	 * @param tipoCambio El valor del tipo de cambio para el detalle.
	 */
	public function DetalleSwapVO(tipo : String, compra : Boolean, fechaValor : String, fechaLiquidacion : Date,
	                            claveFormaLiquidacion : String, monto : Number, tipoCambio : Number)
    {
        this.idDeal = 0;
        this.fechaCaptura = new Date();
        this.tipo = tipo;
        this.compra = compra;
        this.fechaValor = fechaValor;
        this.fechaLiquidacion = fechaLiquidacion;
        this.claveFormaLiquidacion = claveFormaLiquidacion;
        this.monto = monto;
        this.tipoCambio = tipoCambio;
    }

	/**
	 * Obtiene los valores de los atributos del objeto en forma de String.
	 * 
	 * @return String.
	 */
    public function toString() : String
    {
        return 'DetalleSwapVO: ' + tipo + ' ' + compra + ' ' + claveFormaLiquidacion + ' ' + monto + ' ' + tipoCambio; 
    }
}