/*
 * $Id: SpreadVO.java,v 1.11 2008/02/22 18:25:26 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.posicion.vo;

import java.io.Serializable;

/**
 * Value Object para Spread para la comunicaci&oacute;n del SICA con las applicaciones en Flex (Monitor de la Posici&oacute;n).
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:26 $
 */
public class SpreadVO implements Serializable {
	
	/**
	 * Constructor por Default.
	 *
	 */
    public SpreadVO() {
        super();
    }

    /**
     * Constructor que inicializa los valores para VO.
     * 
     * @param claveFormaLiquidacion La clave de la forma de liquidaci&oacute;n.
     * @param compraCash El valor del tipo de cambio compra Cash.
     * @param ventaCash El valor del tipo de cambio venta Cash.
     * @param compraTom El valor del tipo de cambio compra Tom.
     * @param ventaTom El valor del tipo de cambio venta Tom.
     * @param compraSpot El valor del tipo de cambio compra Spot.
     * @param ventaSpot El valor del tipo de cambio venta Spot.
     */
    public SpreadVO(String claveFormaLiquidacion, double compraCash, double ventaCash, double compraTom, double ventaTom, double compraSpot, double ventaSpot) {
        this();
        this.claveFormaLiquidacion = claveFormaLiquidacion;
        this.compraCash = compraCash;
        this.ventaCash = ventaCash;
        this.compraTom = compraTom;
        this.ventaTom = ventaTom;
        this.compraSpot = compraSpot;
        this.ventaSpot = ventaSpot;
    }
    
    /**
     * Regresa el valor de claveFormaLiquidacion.
     * 
     * @return String
     */
    public String getClaveFormaLiquidacion() {
        return claveFormaLiquidacion;
    }

    /**
     * Asigna el valor para claveFormaLiquidacion.
     * 
     * @param claveFormaLiquidacion El valor para claveFormaLiquiacion.
     */
    public void setClaveFormaLiquidacion(String claveFormaLiquidacion) {
        this.claveFormaLiquidacion = claveFormaLiquidacion;
    }

    /**
     * Obtiene el valor de compraCash. 
     * 
     * @return double
     */
    public double getCompraCash() {
        return compraCash;
    }
    
    /**
     * Asigna el valor para compraCash.
     * 
     * @param compraCash El valor para compraCash. 
     */
    public void setCompraCash(double compraCash) {
        this.compraCash = compraCash;
    }
    
    /**
     * Obtiene el valor de ventaCash.
     * 
     * @return double
     */
    public double getVentaCash() {
        return ventaCash;
    }
    
    /**
     * Asigna el valor para ventaCash.
     * 
     * @param ventaCash El valor para ventaCash.
     */
    public void setVentaCash(double ventaCash) {
        this.ventaCash = ventaCash;
    }

    /**
     * Obtiene el valor de compraTom.
     * 
     * @return double
     */
    public double getCompraTom() {
        return compraTom;
    }
    
    /**
     * Asigna el valor para compraTom
     * 
     * @param compraTom El valor para compraTom. 
     */
    public void setCompraTom(double compraTom) {
        this.compraTom = compraTom;
    }

    /**
     * Obtiene el valor de ventaTom. 
     * 
     * @return double
     */
    public double getVentaTom() {
        return ventaTom;
    }
    
    /**
     * Asigna el valor para ventaTom.
     * 
     * @param ventaTom El valor para ventaTom. 
     */
    public void setVentaTom(double ventaTom) {
        this.ventaTom = ventaTom;
    }

    /**
     * Obtiene el valor de compraSpot. 
     * 
     * @return double
     */
    public double getCompraSpot() {
        return compraSpot;
    }
    
    /**
     * Asigna el valor para compraSpot.
     * 
     * @param compraSpot El valor para compraSpot. 
     */
    public void setCompraSpot(double compraSpot) {
        this.compraSpot = compraSpot;
    }

    /**
     * Obtiene el valor de ventaSpot. 
     * 
     * @return double
     */
    public double getVentaSpot() {
        return ventaSpot;
    }
    
    /**
     * Asigna el valor para ventaSpot.
     * 
     * @param ventaSpot El valor para ventaSpot. 
     */
    public void setVentaSpot(double ventaSpot) {
        this.ventaSpot = ventaSpot;
    }

    /**
     * La clave de la forma de liquidaci&oacute;n.
     */
    private String claveFormaLiquidacion;
    
    /**
     * El tipo de cambio para compra Cash.
     */
    private double compraCash;
    
    /**
     * El tipo de cambio para venta Cash. 
     */
    private double ventaCash;
    
    /**
     * El tipo de cambio para compra Tom.
     */
    private double compraTom;
    
    /**
     * El tipo de cambio para venta Tom.
     */
    private double ventaTom;
    
    /**
     * El tipo de cambio para compra Spot.
     */
    private double compraSpot;
    
    /**
     * El tipo de cambio para venta Spot.
     */
    private double ventaSpot;
}
