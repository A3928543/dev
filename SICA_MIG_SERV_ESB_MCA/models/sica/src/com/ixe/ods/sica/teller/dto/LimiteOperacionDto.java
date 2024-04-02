/*
 * $Id: LimiteOperacionDto.java,v 1.1.2.1 2010/10/08 01:31:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.teller.dto;

import java.io.Serializable;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Data Transfer Object que contiene la informaci&oacute;n general del Contrato Sica y del cliente.
 *
 * @author Abraham L&oacute;pez A.(alopez)
 * @version $Revision: 1.1.2.1 $ $Date: 2010/10/08 01:31:04 $
 */
public class LimiteOperacionDto implements Serializable {
	
	/**
     * Constructor por default.
     */
	public LimiteOperacionDto() {
		super();
	}	
	
	/**
	 * Constructor que inicializa todas las variables de instancia.
	 *
	 * @param idDivisa	La divisa.
	 * @param claveFormaLiquidacion La clave forma de liquidaci&oacute;n de la divisa.
	 * @param recibimos Si es de recibimos o entregamos.
	 * @param limiteDiario El l&iacute;mite diario que a&uacute;n puede operar.
	 * @param limiteMensual	El l&iacute;mite mensual que a&uacute;n puede operar.
	 */
	public LimiteOperacionDto(String idDivisa, String claveFormaLiquidacion,
			boolean recibimos, double limiteDiario, double limiteMensual) {
		this();
		this.idDivisa = idDivisa;
		this.claveFormaLiquidacion = claveFormaLiquidacion;
		this.recibimos = recibimos;
		this.limiteDiario = limiteDiario;
		this.limiteMensual = limiteMensual;
	}
	
	/**
     * Regresa el valor de idDivisa.
     *
     * @return String.
     */
	public String getIdDivisa() {
		return idDivisa;
	}
	
	/**
     * Establece el valor de idDivisa.
     *
     * @param idDivisa El valor a asignar.
     */
	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}
	
	/**
     * Regresa el valor de claveFormaLiquidacion.
     *
     * @return String.
     */
	public String getClaveFormaLiquidacion() {
		return claveFormaLiquidacion;
	}

	/**
     * Establece el valor de claveFormaLiquidacion.
     *
     * @param claveFormaLiquidacion El valor a asignar.
     */
	public void setClaveFormaLiquidacion(String claveFormaLiquidacion) {
		this.claveFormaLiquidacion = claveFormaLiquidacion;
	}
	
	/**
     * Regresa el valor de recibimos.
     *
     * @return boolean.
     */
	public boolean isRecibimos() {
		return recibimos;
	}

	/**
     * Establece el valor de recibimos.
     *
     * @param recibimos El valor a asignar.
     */
	public void setRecibimos(boolean recibimos) {
		this.recibimos = recibimos;
	}
	
	/**
     * Regresa el valor de limiteDiario.
     *
     * @return double.
     */
	public double getLimiteDiario() {
		return limiteDiario;
	}

	/**
     * Establece el valor de limiteDiario.
     *
     * @param limiteDiario El valor a asignar.
     */
	public void setLimiteDiario(double limiteDiario) {
		this.limiteDiario = limiteDiario;
	}
	
	/**
     * Regresa el valor de limiteMensual.
     *
     * @return double.
     */
	public double getLimiteMensual() {
		return limiteMensual;
	}

	/**
     * Establece el valor de limiteMensual.
     *
     * @param limiteMensual El valor a asignar.
     */
	public void setLimiteMensual(double limiteMensual) {
		this.limiteMensual = limiteMensual;
	}
	
	/**
     * Regresa el hashCode.
     * 
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
    	return new HashCodeBuilder().append(idDivisa).append(claveFormaLiquidacion).
    	append(recibimos).append(limiteDiario).append(limiteMensual).toHashCode();
    }
    
    /**
     * Regresa una cadena con la descripci&oacute;n de los campos de esta instancia.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this).append("idDivisa", idDivisa).
                append("claveFormaLiquidacion", claveFormaLiquidacion).append("recibimos", 
                		recibimos).append("limiteDiario", 
                				limiteDiario).append("limiteMensual", limiteMensual).toString();
    }
	
	/**
	 * La divisa.
	 */
	private String idDivisa;
	
	/**
	 * La clave forma de liquidaci&oacute;n de la divisa.
	 */
	private String claveFormaLiquidacion;
	
	/**
	 * Si es de recibimos o entregamos.
	 */
	private boolean recibimos;
	
	/**
	 * El l&iacute;mite diario que a&uacute;n puede operar.
	 */
	private double limiteDiario;
	
	/**
	 * El l&iacute;mite mensual que a&uacute;n puede operar.
	 */
	private double limiteMensual;
	
	/**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -2102757606028895819L;

}
