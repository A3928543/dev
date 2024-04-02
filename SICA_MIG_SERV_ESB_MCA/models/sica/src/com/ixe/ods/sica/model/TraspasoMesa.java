/*
 * $Id: TraspasoMesa.java,v 1.12 2008/02/22 18:25:22 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import java.io.Serializable;
import java.util.Date;


/**
 * Clase persistente para la tabla SC_TRASPASO_MESA.
 *
 * @hibernate.class table="SC_TRASPASO_MESA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.TraspasoMesa"
 * dynamic-update="true"
 * 
 * @hibernate.query name="findAllTraspasosPortafolios"
 * query="FROM TraspasoMesa AS tm"
 * 
 * @hibernate.query name="findTraspasosPortafoliosDiaActual"
 * query="FROM TraspasoMesa AS tm WHERE tm.fechaOperacion = ? ORDER BY tm.idTraspasoMesa"
 * 
 * @hibernate.query name="findTraspasosPortafoliosByFechaOperacion"
 * query="FROM TraspasoMesa AS tm WHERE tm.fechaOperacion BETWEEN ? AND ? ORDER BY tm.fechaOperacion"
 *
 * @author Javier Cordova (jcordova)
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:22 $
 */
public class TraspasoMesa implements Serializable {

	/**
	 * Constructor por Default.
	 */
	public TraspasoMesa() {
        super();
    }
	
	/**
     * Regresa el valor de idTraspasoMesa.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_TRASPASO_MESA"
     * unsaved-value="0"
     * @hibernate.generator-param name="sequence"
     * value="SC_TRASPASO_MESA_SEQ"
     * @return int.
     */
    public int getIdTraspasoMesa() {
        return idTraspasoMesa;
    }

    /**
     * Fija el valor de idTraspasoMesa.
     *
     * @param idTraspasoMesa El valor a asignar.
     */
    public void setIdTraspasoMesa(int idTraspasoMesa) {
        this.idTraspasoMesa = idTraspasoMesa;
    }
    
    /**
     * Regresa el valor de deMesaCambio.
     *
     * @hibernate.many-to-one column="DE_MESA_CAMBIO"
     * cascade="none"
     * class="com.ixe.ods.sica.model.MesaCambio"
     * outer-join="auto"
     * unique="false"
     * @return MesaCambio.
     */
    public MesaCambio getDeMesaCambio() {
        return deMesaCambio;
    }
    
    /**
     * Fija el valor de deMesaCambio.
     *
     * @param deMesaCambio El valor a asignar.
     */
    public void setDeMesaCambio(MesaCambio deMesaCambio) {
        this.deMesaCambio = deMesaCambio;
    }
    
    /**
     * Regresa el valor de aMesaCambio.
     *
     * @hibernate.many-to-one column="A_MESA_CAMBIO"
     * cascade="none"
     * class="com.ixe.ods.sica.model.MesaCambio"
     * outer-join="auto"
     * unique="false"
     * @return MesaCambio.
     */
    public MesaCambio getAMesaCambio() {
        return aMesaCambio;
    }
    
    /**
     * Fija el valor de aMesaCambio.
     *
     * @param aMesaCambio El valor a asignar.
     */
    public void setAMesaCambio(MesaCambio aMesaCambio) {
        this.aMesaCambio = aMesaCambio;
    }
    
    /**
     * Regresa el valor de divisa.
     *
     * @hibernate.many-to-one column="DIVISA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Divisa"
     * outer-join="auto"
     * unique="false"
     * @return Divisa.
     */
    public Divisa getDivisa() {
        return divisa;
    }
    
    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(Divisa divisa) {
        this.divisa = divisa;
    }
    
    /**
     * Regresa el valor de tipoCambio.
     *
     * @hibernate.property column="TIPO_CAMBIO"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getTipoCambio() {
        return tipoCambio;
    }
    
    /**
     * Fija el valor de tipoCambio.
     *
     * @param tipoCambio El valor a asignar.
     */
    public void setTipoCambio(double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }
    
    /**
     * Regresa el valor de tipoCambioDivisaReferencia.
     *
     * @hibernate.property column="TIPO_CAMBIO_DIVISA_REFERENCIA"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getTipoCambioDivisaReferencia() {
        return tipoCambioDivisaReferencia;
    }
    
    /**
     * Fija el valor de tipoCambioDivisaReferencia.
     *
     * @param tipoCambioDivisaReferencia El valor a asignar.
     */
    public void setTipoCambioDivisaReferencia(double tipoCambioDivisaReferencia) {
        this.tipoCambioDivisaReferencia = tipoCambioDivisaReferencia;
    }
    
    /**
     * Regresa el valor de deIdDealPosicion.
     *
     * @hibernate.many-to-one column="DE_ID_DEAL_POSICION"
     * cascade="none"
     * class="com.ixe.ods.sica.model.DealPosicion"
     * outer-join="auto"
     * unique="false"
     * @return DealPosicion.
     */
    public DealPosicion getDeIdDealPosicion() {
        return deIdDealPosicion;
    }
    
    /**
     * Fija el valor de deIdDealPosicion.
     *
     * @param deIdDealPosicion El valor a asignar.
     */
    public void setDeIdDealPosicion(DealPosicion deIdDealPosicion) {
        this.deIdDealPosicion = deIdDealPosicion;
    }
    
    /**
     * Regresa el valor de aIdDealPosicion.
     *
     * @hibernate.many-to-one column="A_ID_DEAL_POSICION"
     * cascade="none"
     * class="com.ixe.ods.sica.model.DealPosicion"
     * outer-join="auto"
     * unique="false"
     * @return DealPosicion.
     */
    public DealPosicion getAIdDealPosicion() {
        return aIdDealPosicion;
    }
    
    /**
     * Fija el valor de aIdDealPosicion.
     *
     * @param aIdDealPosicion El valor a asignar.
     */
    public void setAIdDealPosicion(DealPosicion aIdDealPosicion) {
        this.aIdDealPosicion = aIdDealPosicion;
    }
    
    /**
     * Regresa el valor de facturaOperacion.
     *
     * @hibernate.property column="FECHA_OPERACION"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getFechaOperacion() {
        return fechaOperacion;
    }

    /**
     * Fija el valor de fechaOperacion.
     *
     * @param fechaCaptura El valor a asignar.
     */
    public void setFechaOperacion(Date fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }
    
    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof TraspasoMesa)) {
            return false;
        }
        TraspasoMesa castOther = (TraspasoMesa) other;
        return new EqualsBuilder().append(this.getIdTraspasoMesa(), castOther.getIdTraspasoMesa()).isEquals();
    }
    
    //Variables de instancia
    /**
     * Identificador del traspaso.
     */
    private int idTraspasoMesa;
    
    /**
     * Mesa vendedora de la Posición. FK a SC_MESA_CAMBIO.
     */
    private MesaCambio deMesaCambio;
    
    /**
     * Mesa compradora de la posición. FK a SC_MESA_CAMBIO.
     */
    private MesaCambio aMesaCambio;
    
    /**
     * DIvisa en la que se realizó el traspaso.
     */
    private Divisa divisa;
    
    /**
     * Tipo de cambio del traspaso.
     */
    private double tipoCambio;
    
    /**
     * Tipo de cambio a la divisa de referencia del traspaso.
     */
    private double tipoCambioDivisaReferencia;
    
    /**
     * FK a SC_DEAL_POSICION de la venta de la Posición.
     */
    private DealPosicion deIdDealPosicion;
    
    /**
     * FK a SC_DEAL_POSICION de la compra de la Posición.
     */
    private DealPosicion aIdDealPosicion;
    
    /**
     * Fecha de Operaci&oacute;n del Traspaso.
     */
    private Date fechaOperacion;
    
}
