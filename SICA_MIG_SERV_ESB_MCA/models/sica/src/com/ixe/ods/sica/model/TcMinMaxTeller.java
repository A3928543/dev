/*
 * $Id: TcMinMaxTeller.java,v 1.1 2008/04/16 18:21:33 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

import com.ixe.ods.sica.Redondeador;

/**
 * Clase persistente para la tabla SC_TC_MIN_MAX_TELLER. Define los tipos de
 * cambio minimos y maximos con los que opera el Teller definido por el SICA.
 * 
 * @hibernate.class table="SC_TC_MIN_MAX_TELLER"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.TcMinMaxTeller"
 * dynamic-update="true"
 * 
 * @author Gustavo Gonzalez
 * @version $Revision: 1.1 $ $Date: 2008/04/16 18:21:33 $
 *
 */
public class TcMinMaxTeller implements Serializable {

    /**
	 * Constructor por default. No hace nada.
	 */
	public TcMinMaxTeller() {
		super();
	}

	/**
	 * Constructor para Tipos de Cambio Minimo y Maximo provenientes del Teller.
	 *   
	 * @param tipoCambioMinimoCpa El valor del Tipo de Cambio Minimo para Compra.
	 * @param tipoCambioMaximoCpa El valor del Tipo de Cambio Maximo para Compra.
	 * @param tipoCambioMinimoVta El valor del Tipo de Cambio Minimo para Venta.
	 * @param tipoCambioMaximoVta El valor del Tipo de Cambio Maximo para Venta.
	 * @param idCanal El id del canal del operacion.
	 * @param cierre Compra o Venta.
	 */
	public TcMinMaxTeller(double tipoCambioMinimoCpa, double tipoCambioMaximoCpa,
			double tipoCambioMinimoVta, double tipoCambioMaximoVta,
			String idCanal, boolean cierre) {
		super();
		this.tipoCambioMinCpa = tipoCambioMinimoCpa;
		this.tipoCambioMaxCpa = tipoCambioMaximoCpa;
		this.tipoCambioMinVta = tipoCambioMinimoVta;
		this.tipoCambioMaxVta = tipoCambioMaximoVta;
		this.idCanal = idCanal;
		this.cierre = cierre;
	}

	/**
	 * Regresa el valor de idDeal.
	 *
	 * @hibernate.id generator-class="sequence"
	 * column="ID_TC_MIN_MAX"
	 * unsaved-value="null"
	 * @hibernate.generator-param name="sequence"
	 * value="SC_TC_MIN_MAX_TELLER_SEQ"
	 * @return int.
	 */
	public Integer getIdTcMinMax() {
		return idTcMinMax;
	}

	/**
	 * Asigna el valor para el id del Tipo de Pizarron.
     * 
	 * @param idTcMinMax El id del tipo de Pizarron
	 */
	public void setIdTcMinMax(Integer idTcMinMax) {
		this.idTcMinMax = idTcMinMax;
	}

	/**
	 * Regresa el valor de tipoCambioMinCpa.
	 *
	 * @hibernate.property column="TIPO_CAMBIO_MINIMO_CPA"
	 * not-null="true"
	 * unique="false"
	 * @return double.
	 */
	public double getTipoCambioMinCpa() {
		return Redondeador.redondear6Dec(tipoCambioMinCpa);
	}

	/**
	 * Fija el valor de tipoCambioMinCpa.
	 *
	 * @param tipoCambioMinCpa El valor a asignar.
	 */
	public void setTipoCambioMinCpa(double tipoCambioMinCpa) {
		this.tipoCambioMinCpa = tipoCambioMinCpa;
	}

	/**
	 * Regresa el valor de tipoCambioMaxCpa.
	 *
	 * @hibernate.property column="TIPO_CAMBIO_MAXIMO_CPA"
	 * not-null="true"
	 * unique="false"
	 * @return double.
	 */
	public double getTipoCambioMaxCpa() {
		return Redondeador.redondear6Dec(tipoCambioMaxCpa);
	}

	/**
	 * Fija el valor de tipoCambioMaxCpa.
	 *
	 * @param tipoCambioMaxCpa El valor a asignar.
	 */
	public void setTipoCambioMaxCpa(double tipoCambioMaxCpa) {
		this.tipoCambioMaxCpa = tipoCambioMaxCpa;
	}
	
	/**
	 * Regresa el valor de tipoCambioMinVta.
	 *
	 * @hibernate.property column="TIPO_CAMBIO_MINIMO_VTA"
	 * not-null="true"
	 * unique="false"
	 * @return double.
	 */
	public double getTipoCambioMinVta() {
		return Redondeador.redondear6Dec(tipoCambioMinVta);
	}

	/**
	 * Fija el valor de tipoCambioMinVta.
	 *
	 * @param tipoCambioMinVta El valor a asignar.
	 */
	public void setTipoCambioMinVta(double tipoCambioMinVta) {
		this.tipoCambioMinVta = tipoCambioMinVta;
	}

	/**
	 * Regresa el valor de tipoCambioMaxVta.
	 *
	 * @hibernate.property column="TIPO_CAMBIO_MAXIMO_VTA"
	 * not-null="true"
	 * unique="false"
	 * @return double.
	 */
	public double getTipoCambioMaxVta() {
		return Redondeador.redondear6Dec(tipoCambioMaxVta);
	}

	/**
	 * Fija el valor de tipoCambioMaxVta.
	 *
	 * @param tipoCambioMaxVta El valor a asignar.
	 */
	public void setTipoCambioMaxVta(double tipoCambioMaxVta) {
		this.tipoCambioMaxVta = tipoCambioMaxVta;
	}

	/**
	 * Regresa el valor de canal.
	 *
	 * @hibernate.property column="ID_CANAL"
	 * not-null="true"
	 * unique="false"
	 * @return Canal. 
	 */
	public String getIdCanal() {
		return idCanal;
	}

	/**
	 * Fija el valor de canal.
	 *
	 * @param idCanal El valor a asignar.
	 */
	public void setIdCanal(String idCanal) {
		this.idCanal = idCanal;
	}

	/**
	 * Regresa el valor de factura.
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
	 * Fija el valor de fechaCaptura.
	 *
	 * @param fecha El valor a asignar.
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * Regresa el valor de cierre.
	 *
	 * @hibernate.property column="CIERRE"
	 * type="com.legosoft.hibernate.type.SiNoType"
	 * not-null="true"
	 * unique="false"
	 * update="true"
	 * insert="true"
	 * @return boolean.
	 */
	public boolean isCierre() {
		return cierre;
	}

	/**
	 * Fija el valor de cierre.
	 *
	 * @param cierre El valor a asignar.
	 */
	public void setCierre(boolean cierre) {
		this.cierre = cierre;
	}
	
	/**
	 * Obtiene el mayor diferencial (Spread) entre compras y ventas.
	 * 
	 * @return double
	 */
	public double mayorDiferencialCpaVta(){
		return Math.abs(getTipoCambioMinCpa() - getTipoCambioMaxVta());
	}
	
	/**
	 * Obtiene el menor diferencial (Spread) entre compras y ventas.
	 * 
	 * @return double
     */
    public double menorDiferencialCpaVta() {
        return Math.abs(getTipoCambioMaxCpa() - getTipoCambioMinVta());
    }

    /**
	 * El id del Tipo del Tipo de Cambio.
	 */
	private Integer idTcMinMax;

	/**
	 * El tipo de cambio minimo para compra.
	 */
	private double tipoCambioMinCpa;

	/**
	 * El tipo de cambio maximo para compra.
	 */
	private double tipoCambioMaxCpa;
	
	/**
	 * El tipo de cambio minimo para venta.
	 */
	private double tipoCambioMinVta;

	/**
	 * El tipo de cambio maximo para venta.
	 */
	private double tipoCambioMaxVta;

	/**
	 * El id del canal de operacion.
	 */
	private String idCanal;

	/**
	 * La fecha del registro
	 */
	private Date fecha = new Date();

	/**
	 * True cierre operciones| False apertura operaciones.
	 */
	private boolean cierre;

    /**
     * UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -748459613763595013L;
}
