/*
 * $Id: FactorDivisaVO.java,v 1.11 2008/02/22 18:25:26 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.posicion.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Value Object para FactorDivisa para la comunicaci&oacute;n del SICA con las applicaciones en Flex (Monitor de la Posici&oacute;n).
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:26 $
 */
public class FactorDivisaVO implements Serializable {

	/**
	 * Constructor por Default. Inicializa los valores para VO.
	 *
	 */
    public FactorDivisaVO() {
        super();
    }
    
    /**
     * Constructor que inicializa los valores para VO.
     * 
     * @param idFromDivisa El id "De la divisa".
     * @param idToDivisa El id "A la divisa"
     * @param factor El valor del factor de la divisa.
     * @param sobreprecioIxe El sobreprecio para IXE.
     * @param spread El valor del spread.
     * @param carry El valor del carry.
     * @param ultimaModificacion La fecha de la ultima modificaci&oacute;n de los valores.
     */
    public FactorDivisaVO(String idFromDivisa, String idToDivisa, double factor, double sobreprecioIxe, double spread, double carry, Date ultimaModificacion) {
        this();
        this.idFromDivisa = idFromDivisa;
        this.idToDivisa = idToDivisa;
        this.factor = factor;
        this.sobreprecioIxe = sobreprecioIxe;
        this.spread = spread;
        this.carry = carry;
        this.ultimaModificacion = ultimaModificacion;
    }	
    
    /**
     * Obtiene el valor de idFromDivisa.
     * 
     * @return String
     */
    public String getIdFromDivisa() {
        return idFromDivisa;
    }

    /**
     * Asigna el valor para idFromDivisa.
     * 
     * @param idFromDivisa El valor para idFromDivisa.
     */
    public void setIdFromDivisa(String idFromDivisa) {
        this.idFromDivisa = idFromDivisa;
    }
    
    /**
     * Obtiene el valor de idToDivisa.
     * 
     * @return String
     */
    public String getIdToDivisa() {
        return idToDivisa;
    }
    
    /**
     * Asigna el valor para idToDivisa.
     * 
     * @param idToDivisa El valor para idToDivisa.
     */
    public void setIdToDivisa(String idToDivisa) {
        this.idToDivisa = idToDivisa;
    }

    /**
     * Obtiene el valor de factor.
     * 
     * @return double
     */
    public double getFactor() {
        return factor;
    }

    /**
     * Asigna el valor para factor
     * 
     * @param factor El valor para factor.
     */
    public void setFactor(double factor) {
        this.factor = factor;
    }
    
    /**
     * Obtiene el valor de sobrePrecioIxe.
     * 
     * @return double
     */
    public double getSobreprecioIxe() {
        return sobreprecioIxe;
    }

    /**
     * Asigna el valor para sobrePrecioIxe
     * 
     * @param sobreprecioIxe El valor para sobrePrecioIxe
     */
    public void setSobreprecioIxe(double sobreprecioIxe) {
        this.sobreprecioIxe = sobreprecioIxe;
    }

    /**
     * Obtiene el valor de spread.
     * 
     * @return double
     */
    public double getSpread() {
        return spread;
    }

    /**
     * Asigna el valor para spread.
     * 
     * @param spread El valor para spread.
     */
    public void setSpread(double spread) {
        this.spread = spread;
    }
    
    /**
     * Obtiene el valor de carry.
     * 
     * @return double
     */
    public double getCarry() {
        return carry;
    }
    
    /**
     * Asigna el valor para carry.
     * 
     * @param carry El valor para carry.
     */
    public void setCarry(double carry) {
        this.carry = carry;
    }
    
    /**
     * Obtiene el valor de ultimaModificacion.
     * 
     * @return Date
     */
    public Date getUltimaModificacion() {
        return ultimaModificacion;
    }
    
    /**
     * Asigna el valor para ultimaModificacion.
     *  
     * @param ultimaModificacion El valor para ultimaModificacion.
     */
    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }
    
    /**
     * El id "De la divisa"
     */
    private String idFromDivisa;
    
    /**
     * El id "A la divisa"
     */
    private String idToDivisa;
    
    /**
     * El factor de la divisa.
     */
    private double factor;
    
    /**
     * El sobreprecio de Ixe.
     */
    private double sobreprecioIxe;
    
    /**
     * El spread de la divisa.
     */
    private double spread;
    
    /**
     * El carry de la divisa.
     */
    private double carry;
    
    /**
     * La fecha de la &uacute;ltima modificaci&oacute;n.
     */
    private Date ultimaModificacion;
}
