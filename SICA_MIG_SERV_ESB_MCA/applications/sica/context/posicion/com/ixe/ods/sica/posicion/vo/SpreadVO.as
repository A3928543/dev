/**
 * Value Object que contiene la informaci&oacute;n de los valores de los Spreads para el pizarr&oacute;n
 * de precios.
 */
class com.ixe.ods.sica.posicion.vo.SpreadVO
{
	/**
	 * La clase para el VO de Spread.
	 */
    static var registered = Object.registerClass("com.ixe.ods.sica.posicion.vo.SpreadVO", SpreadVO);
    
    /**
	 * La clave de liquidaci&oacute;n (Producto). 
	 */
    var claveFormaLiquidacion : String;
    
    /**
	 * El valor para Compra Cash. 
	 */
    var compraCash : Number;
    
    /**
	 * El valor para Venta Cash. 
	 */
    var ventaCash : Number;
    
    /**
	 * El valor para Compra TOM. 
	 */
    var compraTom : Number;	
    
    /**
	 * El valor para Venta TOM.
	 */
    var ventaTom : Number;
    
    /**
	 * El valor para Compra SPOT. 
	 */
    var compraSpot : Number;
    
    /**
	 * El valor para Venta SPOT. 
	 */
    var ventaSpot : Number;
}