package com.ixe.ods.sica.model;

import java.util.Date;

/**
 * @hibernate.class table="SC_SUMATORIA_CORTES"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.SumatoriaCorte"
 * dynamic-update="true"
 * 
 * @hibernate.query name="findAllSumatoriaCorte"
 * query="FROM SumatoriaCorte AS sc WHERE sc.idCorte is NULL and sc.idDivisa = ?"
 *  * @hibernate.query name="findAllSumatoriaCorteSinEnviar"
 * query="FROM SumatoriaCorte AS sc WHERE sc.idCorte is NULL"
 * @author JDCH
 * @version  $Revision: 1.1.4.2 $ $Date: 2014/10/29 16:42:57 $
 */

public class SumatoriaCorte {
	
    /**
     * El identificador del registro.
     */
	private   int idSumatoriaCorte;
	
    /**
     * Relaci&oacute;n muchos a uno con Divisa.
     */
	private String idDivisa;
	
    /**
     * C)ash | T)om | S)pot.
     */
	private String tipoValor;

    /**
     * El tipo de operaci&oacute;n (COMPRA | VENTA | COMPRA_INTERBANCARIA |
     * VENTA_INTERBANCARIA | CANCELACION).
     */
	private String TipoOperacion;
	
    /**
     * El monto en moneda nacional al precio de la mesa.
     */
	private double montoMxnMesa;
	
	/**
     * El monto en moneda nacional al precio del cliente
     */
	private double montoMxnCliente;
	
	/**
     * El monto en divisa
     */
	private double monto;
	
    /**
     * Corte del que se realiza el acumulado
     */
    private Integer idCorte;
    
    /**
     * La fecha que se registra
     */
    private Date fecha;
    
    /**
     * Regresa el valor de idPosicionLogCorte.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_SUMATORIA_CORTE"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_SUMATORIA_CORTES_SEQ" 		  
     * @return int.
     */
	public int getIdSumatoriaCorte() {
		return idSumatoriaCorte;
	}

    /**
     * Fija el valor de idSumatoriaCorte.
     *
     * @param idSumatoriaCorte El valor a asignar.
     */
	public void setIdSumatoriaCorte(int idSumatoriaCorte) {
		this.idSumatoriaCorte = idSumatoriaCorte;
	}

    /**
     * Regresa el valor de idDivisa.
     * @hibernate.property column="ID_DIVISA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
	public String getIdDivisa() {
		return idDivisa;
	}
	
    /**
     * Fija el valor de idDivisa.
     *
     * @param idDivisa El valor a asignar.
     */
	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}

    /**
     * Regresa el valor de tipoValor.
     * @hibernate.property column="TIPO_VALOR"
     * not-null="true"
     * unique="false"
     * @return String.
     */
	public String getTipoValor() {
		return tipoValor;
	}

    /**
     * Fija el valor de tipoValor.
     *
     * @param tipoValor El valor a asignar.
     */
	public void setTipoValor(String tipoValor) {
		this.tipoValor = tipoValor;
	}

    /**
     * Regresa el valor de tipoOperacion.
     * @hibernate.property column="TIPO_OPERACION"
     * not-null="true"
     * unique="false"
     * @return String.
     */
	public String getTipoOperacion() {
		return TipoOperacion;
	}
	
    /**
     * Fija el valor de tipoOperacion.
     *
     * @param tipoOperacion El valor a asignar.
     */
	public void setTipoOperacion(String tipoOperacion) {
		TipoOperacion = tipoOperacion;
	}

    /**
     * Regresa el valor de montoMxnMesa.
     * @hibernate.property column="MONTO_MXN_MESA"
     * not-null="true"
     * unique="false"
     * @return double.
     */
	public double getMontoMxnMesa() {
		return montoMxnMesa;
	}

    /**
     * Fija el valor de montoMxnMesa.
     *
     * @param montoMxnMesa El valor a asignar.
     */
	public void setMontoMxnMesa(double montoMxnMesa) {
		this.montoMxnMesa = montoMxnMesa;
	}

    /**
     * Regresa el valor de montoMxnCliente.
     * @hibernate.property column="MONTO_MXN_CLIENTE"
     * not-null="true"
     * unique="false"
     * @return double.
     */
	public double getMontoMxnCliente() {
		return montoMxnCliente;
	}

    /**
     * Fija el valor de montoMxnCliente.
     *
     * @param montoMxnCliente El valor a asignar.
     */
	public void setMontoMxnCliente(double montoMxnCliente) {
		this.montoMxnCliente = montoMxnCliente;
	}

    /**
     * Regresa el valor de monto.
     * @hibernate.property column="MONTO"
     * not-null="true"
     * unique="false"
     * @return double.
     */
	public double getMonto() {
		return monto;
	}

    /**
     * Fija el valor de monto.
     *
     * @param monto El valor a asignar.
     */
	public void setMonto(double monto) {
		this.monto = monto;
	}

    /**
     * Regresa el valor de idCorte.
     * @hibernate.property column="ID_CORTE"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
	public Integer getIdCorte() {
		return idCorte;
	}
	
    /**
     * Fija el valor de idCorte.
     *
     * @param monto El valor a asignar.
     */
	public void setIdCorte(Integer idCorte) {
		this.idCorte = idCorte;
	}

    /**
     * Regresa el valor de fecha.
     *
     * @hibernate.property column="FECHA"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
	public Date getFecha() {
		return fecha;
	}

    /**
     * Fija el valor de fecha.
     *
     * @param fecha El valor a asignar.
     */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	  
	  

}
