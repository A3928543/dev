/*
 * $Id: Poliza.java,v 1.19 2009/12/22 18:04:53 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la tabla SC_POLIZA.
 *
 * @hibernate.class table="SC_POLIZA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Poliza"
 * dynamic-update="true"
 * 
 * @hibernate.query name="findPolizaPorFecha"
 * query="FROM Poliza AS p WHERE p.fechaValor BETWEEN ? AND ?"
 * 
 * @hibernate.query name="findPolizaReversoByDeal"
 * query="FROM Poliza AS p WHERE p.idDeal = ?"
 * 
 * @author Edgar I. Leija, Gustavo Gonzalez, Javier Cordova.
 * @version  $Revision: 1.19 $ $Date: 2009/12/22 18:04:53 $
 */
 public class Poliza implements Serializable {

    /**
	 * Constructor por default.
	 */
	 public Poliza() {
		super();
	}
	
	 /**
      * Regresa el valor de idPoliza.
      *
     * @return String.
     * @hibernate.id generator-class="sequence"
      * column="ID_POLIZA"
      * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_POLIZA_SEQ"
      */
    public int getIdPoliza() {
        return idPoliza;
    }

    /**
     * Fija el valor de idPoliza.
     *
     * @param idPoliza El valor a asignar.
     */
    public void setIdPoliza(int idPoliza) {
        this.idPoliza = idPoliza;
    }
    
    /**
     * Regresa el valor de fechaValor.
     *
     * @hibernate.property column="FECHA_VALOR"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getFechaValor() {
        return fechaValor;
    }

    /**
     * Fija el valor de fechaValor
     *
     * @param fechaValor El valor a asignar.
     */
    public void setFechaValor(Date fechaValor) {
        this.fechaValor = fechaValor;
    }
    
    /**
     * Regresa el valor de fechaCreacion.
     *
     * @hibernate.property column="FECHA_CREACION"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Fija el valor de fechaCreacion
     *
     * @param fechaCreacion El valor a asignar.
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    /**
	 * Regresa el valor de folioDetalle.
	 * 
	 * @hibernate.property column="FOLIO_DETALLE"
     * not-null="false"
     * unique="false"
     * @return int
	 */
	public int getFolioDetalle() {
		return folioDetalle;
	}
	
	/**
	 * Fija el valor de folioDetalle
	 * 
	 * @param folioDetalle El valor a folioDetalle
	 */
	public void setFolioDetalle(int folioDetalle) {
		this.folioDetalle = folioDetalle;
	}
    
    /**
     * Regresa el valor de fechaProcesamiento.
     *
     * @hibernate.property column="FECHA_PROCESAMIENTO"
     * not-null="false"
     * unique="false"
     * @return Date.
     */
    public Date getFechaProcesamiento() {
        return fechaProcesamiento;
    }

    /**
     * Fija el valor de fechaProcesamiento
     *
     * @param fechaProcesamiento El valor a asignar.
     */
    public void setFechaProcesamiento(Date fechaProcesamiento) {
        this.fechaProcesamiento = fechaProcesamiento;
    }
    
    
    /**
     * Regresa el valor de cuenta.
     *
     * @hibernate.property column="CUENTA_CONTABLE"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getCuentaContable() {
        return cuentaContable;
    }

    /**
     * Fija el valor de cuenta.
     *
     * @param cuentaContable El valor a asignar.
     */
    public void setCuentaContable(String cuentaContable) {
        this.cuentaContable = cuentaContable;
    }
    
    /**
     * Regresa el valor de cuenta.
     *
     * @hibernate.property column="ENTIDAD"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getEntidad() {
        return entidad;
    }

    /**
     * Fija el valor de cuenta.
     *
     * @param entidad El valor a asignar.
     */
    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }
    
    /**
     * Regresa el valor de divisa.
     *
     * @hibernate.many-to-one column="ID_DIVISA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Divisa"
     * outer-join="auto"
     * unique="false"
     * @return Divisa.
     */
    public Divisa getDivisa() {
        return _divisa;
    }

    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(Divisa divisa) {
        _divisa = divisa;
    }

    
    /**
     * Regresa el valor de cargo Abono.
     *
     * @hibernate.property column="CARGO_ABONO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getCargoAbono() {
        return cargoAbono;
    }

    /**
     * Fija el valor de cargoAbono.
     *
     * @param cargoAbono El valor a asignar.
     */
    public void setCargoAbono(String cargoAbono) {
        this.cargoAbono = cargoAbono;
    }
    
    /**
     * Regresa el valor de importe.
     *
     * @hibernate.property column="IMPORTE"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getImporte() {
        return importe;
    }

    /**
     * Fija el valor de importe.
     *
     * @param importe El valor a asignar.
     */
    public void setImporte(double importe) {
        this.importe = importe;
    }
    
        
    /**
     * Regresa el valor de referencia.
     *
     * @hibernate.property column="REFERENCIA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getReferencia() {
        return referencia;
    }

    /**
     * Fija el valor de referencia.
     *
     * @param referencia El valor a asignar.
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
    
    /**
     * Regresa el valor de idDeal
     *
     * @hibernate.property column="ID_DEAL"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getIdDeal() {
        return idDeal;
    }

    /**
     * Fija el valor de referencia.
     *
     * @param idDeal El valor a asignar.
     */
    public void setIdDeal(int idDeal) {
        this.idDeal = idDeal;
    }
    
    /**
     * Regresa el valor de referencia.
     *
     * @hibernate.property column="TIPO_DEAL"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getTipoDeal() {
        return tipoDeal;
    }

    /**
     * Fija el valor de referencia.
     *
     * @param tipoDeal El valor a asignar.
     */
    public void setTipoDeal(String tipoDeal) {
        this.tipoDeal = tipoDeal;
    }
    
    /**
     * Regresa el valor de sucursalOrigen.
     *
     * @hibernate.property column="SUCURSAL_ORIGEN"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getSucursalOrigen() {
        return sucursalOrigen;
    }

    /**
     * Fija el valor de sucursalOrigen.
     *
     * @param sucursalOrigen El valor a asignar.
     */
    public void setSucursalOrigen(String sucursalOrigen) {
        this.sucursalOrigen = sucursalOrigen;
    }
    
    /**
     * Regresa el valor de statusProceso.
     *
     * @hibernate.property column="STATUS_PROCESO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getStatusProceso() {
        return statusProceso;
    }

    /**
     * Fija el valor de statusProceso.
     *
     * @param statusProceso El valor a asignar.
     */
    public void setStatusProceso(String statusProceso) {
        this.statusProceso = statusProceso;
    }
    
    /**
     * Regresa el valor de cliente.
     *
     * @hibernate.property column="CLIENTE"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getCliente() {
        return cliente;
    }

    /**
     * Fija el valor de sucursalOrigen.
     *
     * @param cliente El valor a asignar.
     */
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
    
    /**
     * Regresa el valor de tipoCambio
     *
     * @hibernate.property column="TIPO_CAMBIO"
     * not-null="true"
     * unique="false"
     * @return double
     */
	public double getTipoCambio() {
		return tipoCambio;
	}
	
	/**
	 * Fijar el valor de tipoCambio
	 * 
	 * @param tipoCambio Valor a asignar
	 */
	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	
	/**
     * Fija el valor de referencia.
     *
     * @param idDealPosicion El valor a asignar.
     */
    public void setIdDealPosicion(int idDealPosicion) {
        this.idDealPosicion = idDealPosicion;
    }

    /**
     * Regresa el valor de idDealPosicion
     *
     * @return int.
     * @hibernate.property column="ID_DEAL_POSICION"
     * not-null="true"
     * unique="false"
     */
    public int getIdDealPosicion() {
        return idDealPosicion;
    }

	/**
     * Regresa el valor de noContratoSica.
     *
     * @hibernate.property column="NO_CONTRATO_SICA"
     * not-null="true"
     * unique="false"
     * @return String
     */
    public String getNoContratoSica() {
        return noContratoSica;
    }

    /**
     * Fija el valor de noContratoSica.
     *
     * @param noContratoSica El valor a asignar.
     */
    public void setNoContratoSica(String noContratoSica) {
        this.noContratoSica = noContratoSica;
    }

    /**
     * Regresa el valor de claveReferencia.
     *
     * @return String.
     * @hibernate.property column="CLAVE_REFERENCIA"
     * not-null="false"
     * unique="false"
     */
    public String getClaveReferencia() {
        return claveReferencia;
    }

    /**
     * Establece el valor de claveReferencia.
     *
     * @param claveReferencia El valor a asignar.
     */
    public void setClaveReferencia(String claveReferencia) {
        this.claveReferencia = claveReferencia;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof Poliza)) {
            return false;
        }
        Poliza castOther = (Poliza) other;
        return new EqualsBuilder().append(this.getIdPoliza(), castOther.getIdPoliza()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdPoliza()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    private int idPoliza;
    
    /**
     * La fecha Valor del resgistro.
     */
    private Date fechaValor;
    
    /**
     * La cuenta contable referencia a donde se hace el respectivo cargo o abono.
     */
    private String cuentaContable;
    
    /**
     * La Entidad de la Cuenta Contable.
     */
    private String entidad;
    
    /**
     * Si se trata de un cargo o un abono a la Cuenta Contable.
     */
    private String cargoAbono;
    
    /**
     * Indica en qu&eacute; divisa es el cargo o abono a efectuar.
     */
    private Divisa _divisa;
    
    /**
     * El importe correspondiente del cargo o abono a efectuar.
     */
    private double importe;
    
    /**
     * El id del Deal del Detalle o el idDealPosicion de la tabla SC_RECO_UTILIDAD que
     * gener&oacute; la p&oacute;liza contable.
     */
    private int idDeal;
    
    /**
     * La descripci&oacute;n de la p&oacute;liza contable.
     */
    private String referencia;
    
    /**
     * La fecha de creaci&oacute; del resgistro.
     */
    private Date fechaCreacion;
    
    /**
     * Descripci&oacute;n del tipo de Deal: Normal, Interbancario, Neteo, Utilidades, etc.
     */
    private String tipoDeal;
    
    /**
     * Descripci&oacute;n de la sucursal origen del usuario que captur&oacute; el deal.
     */
    private String sucursalOrigen;
    
    /**
     * El cliente a quien se pact&oacute; el deal que gener&oacute; la p&oacute;liza.
     */
    private String cliente;
    
    /**
     * Fecha de Proceso del Registro.
     */
    private Date fechaProcesamiento;
    
    /**
     * Status que indica si ya la p&oacute;liza ya se subi&oacute; a Solomon.
     */
    private String statusProceso;
    
    /**
     * El folio del detalle de deal.
     */
    private int folioDetalle;
    
    /**
     * El tipo de Cambio Pactado del Detalle de Deal.
     */
    private double tipoCambio;
    
    /**
     * El n&uacute;mero de contrato Sica del deal pactado.
     */
    private String noContratoSica;

    /**
     * El idDealPosicion del Detalle de Deal o de la tabla SC_RECO_UTILIDAD.
     */
    private int idDealPosicion;

    /**
     * La clave para dep&oacute;sitos referenciados.
     */
    private String claveReferencia;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 2574778534675222450L;
}
