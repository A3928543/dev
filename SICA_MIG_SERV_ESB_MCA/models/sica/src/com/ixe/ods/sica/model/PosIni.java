/*
 * $Id: PosIni.java,v 1.13 2008/04/16 18:21:33 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

/**
 * Componente de hibernate que agrupa los campos de la posici&oacute;n inicial.
 * Es utilizado por las clases Posicion y PosicionDetalle.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.13 $ $Date: 2008/04/16 18:21:33 $
 */
public class PosIni implements Serializable {

    /**
     * Constructor vac&iacute;o.
     */
    public PosIni() {
        super();
    }

    /**
     * Constructor por default. Inicializa todas las variables de instancia.
     *
     * @param posicionInicialCash El monto de la posici&oacute;n para Cash.
     * @param posicionInicialTom El monto de la posici&oacute;n para Tom.
     * @param posicionInicialSpot El monto de la posici&oacute;n para Spot.
     * @param posicionInicial72Hr El monto de la posici&oacute;n para 72Hr.
     * @param posicionIncialVFut El monto de la posici&oacute;n para VFut.
     * @param posicionInicialMnCash El monto en moneda nacional de la posici&oacute;n para Cash.
     * @param posicionInicialMnTom El monto en moneda nacional de la posici&oacute;n para Tom.
     * @param posicionInicialMnSpot El monto en moneda nacional de la posici&oacute;n para Spot.
     * @param posocionInicialMn72Hr El monto en moneda nacional de la posici&oacute;n para 72Hr.
     * @param posicionInicialMnVFut El monto en moneda nacional de la posici&oacute;n para VFut.
     */
    public PosIni(double posicionInicialCash, double posicionInicialTom, double posicionInicialSpot,
                  double posicionInicial72Hr, double posicionIncialVFut,
                  double posicionInicialMnCash, double posicionInicialMnTom,
                  double posicionInicialMnSpot, double posocionInicialMn72Hr,
                  double posicionInicialMnVFut) {
        this();
        this.posicionInicialCash = posicionInicialCash;
        this.posicionInicialTom = posicionInicialTom;
        this.posicionInicialSpot = posicionInicialSpot;
        this.posicionInicial72Hr = new Double(posicionInicial72Hr);
        this.posicionInicialVFut = new Double(posicionIncialVFut);
        this.posicionInicialMnCash = posicionInicialMnCash;
        this.posicionInicialMnTom = posicionInicialMnTom;
        this.posicionInicialMnSpot = posicionInicialMnSpot;
        this.posicionInicialMn72Hr = new Double(posocionInicialMn72Hr);
        this.posicionInicialMnVFut = new Double(posicionInicialMnVFut);
    }

    /**
     * Obtiene el monto de la posici&oacute;n inicial.
     *
     * @return double.
     */
    public double getPosicionInicial() {
        return getPosicionInicialCash() + getPosicionInicialTom() + getPosicionInicialSpot() +
        getPosicionInicial72Hr().doubleValue() + getPosicionInicialVFut().doubleValue();
    }

    /**
     * Obtiene el monto en moneda nacional de la posici&oacte;n inicial.
     *
     * @return double.
     */
    public double getPosicionInicialMn() {
        return getPosicionInicialMnCash() + getPosicionInicialMnTom() +
            getPosicionInicialMnSpot() + getPosicionInicialMn72Hr().doubleValue() +
            getPosicionInicialMnVFut().doubleValue();
    }

    /**
     * Regresa el valor de posicionInicialCash.
     *
     * @hibernate.property column="POSICION_INICIAL_CASH"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getPosicionInicialCash() {
        return posicionInicialCash;
    }

    /**
     * Establece el valor de posicionInicialCash.
     *
     * @param posicionInicialCash El valor a asignar.
     */
    public void setPosicionInicialCash(double posicionInicialCash) {
        this.posicionInicialCash = posicionInicialCash;
    }

    /**
     * Regresa el valor de posicionInicialTom.
     *
     * @hibernate.property column="POSICION_INICIAL_TOM"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getPosicionInicialTom() {
        return posicionInicialTom;
    }

    /**
     * Establece el valor de posicionInicialTom.
     *
     * @param posicionInicialTom El valor a asignar.
     */
    public void setPosicionInicialTom(double posicionInicialTom) {
        this.posicionInicialTom = posicionInicialTom;
    }

    /**
     * Regresa el valor de posicionInicialSpot.
     *
     * @hibernate.property column="POSICION_INICIAL_SPOT"
     * not-null="true"
     * unique="false"
     * @return posicionInicialSpot.
     */
    public double getPosicionInicialSpot() {
        return posicionInicialSpot;
    }

    /**
     * Establece el valor de posicionInicialSpot.
     *
     * @param posicionInicialSpot El valor a asignar.
     */
    public void setPosicionInicialSpot(double posicionInicialSpot) {
        this.posicionInicialSpot = posicionInicialSpot;
    }

    /**
     * Regresa el valor de posicionIniclal72Hr
     * 
     * @hibernate.property column="POSICION_INICIAL_72HR"
     * not-null="true"
     * unique="false" 
     * @return double
     */
    public Double getPosicionInicial72Hr() {
		return posicionInicial72Hr;
	}
    
    /**
     * Establece el valor de posicionInicial72Hr.
     * 
     * @param posicionInicial72Hr El valor a asignar
     */
	public void setPosicionInicial72Hr(Double posicionInicial72Hr) {
		this.posicionInicial72Hr = posicionInicial72Hr;
	}

	/**
	 * Regresa el valor de posicionInicialVFut
	 * 
	 * @hibernate.property column="POSICION_INICIAL_VFUT"
     * not-null="true"
     * unique="false" 
	 * @return double
	 */
	public Double getPosicionInicialVFut() {
		return posicionInicialVFut;
	}
	
	/**
     * Establece el valor de posicionInicialVFut.
     * 
	 * @param posicionInicialVFut El valor a asignar
	 */
	public void setPosicionInicialVFut(Double posicionInicialVFut) {
		this.posicionInicialVFut = posicionInicialVFut;
	}
	
    
    /**
     * Regresa el valor de posicionInicialMnCash.
     *
     * @hibernate.property column="POSICION_INICIAL_MN_CASH"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getPosicionInicialMnCash() {
        return posicionInicialMnCash;
    }

    /**
     * Establece el valor de posicionInicialMnCash.
     *
     * @param posicionInicialMnCash El valor a asignar.
     */
    public void setPosicionInicialMnCash(double posicionInicialMnCash) {
        this.posicionInicialMnCash = posicionInicialMnCash;
    }

    /**
     * Regresa el valor de posicionInicialMnTom.
     *
     * @hibernate.property column="POSICION_INICIAL_MN_TOM"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getPosicionInicialMnTom() {
        return posicionInicialMnTom;
    }

    /**
     * Establece el valor de posicionInicialMnTom.
     *
     * @param posicionInicialMnTom El valor a asignar.
     */
    public void setPosicionInicialMnTom(double posicionInicialMnTom) {
        this.posicionInicialMnTom = posicionInicialMnTom;
    }

    /**
     * Regresa el valor de posicionInicialMnSpot.
     *
     * @hibernate.property column="POSICION_INICIAL_MN_SPOT"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getPosicionInicialMnSpot() {
        return posicionInicialMnSpot;
    }
    
    /**
     * Establece el valor de posicionInicialMnSpot.
     *
     * @param posicionInicialMnSpot El valor a asignar.
     */
    public void setPosicionInicialMnSpot(double posicionInicialMnSpot) {
        this.posicionInicialMnSpot = posicionInicialMnSpot;
    }
	
    /**
	 * Regresa el valor de posicionInicialMn72Hr
	 * 
	 * @hibernate.property column="POSICION_INICIAL_MN_72HR"
     * not-null="true"
     * unique="false" 
	 * @return double
	 */
	public Double getPosicionInicialMn72Hr() {
		return posicionInicialMn72Hr;
	}
	
	 /**
     * Establece el valor de posicionInicialMn72Hr.
     *
	 * @param posicionInicialMn72Hr El valor a asignar
	 */
	public void setPosicionInicialMn72Hr(Double posicionInicialMn72Hr) {
		this.posicionInicialMn72Hr = posicionInicialMn72Hr;
	}
	
	/**
	 * Regresa el valor de posicionInicialMnVFut
	 * 
	 * @hibernate.property column="POSICION_INICIAL_MN_VFUT"
     * not-null="true"
     * unique="false" 
	 * @return double
	 */
	public Double getPosicionInicialMnVFut() {
		return posicionInicialMnVFut;
	}
	
	/**
     * Establece el valor de posicionInicialMnVFut.
     * 
	 * @param posicionInicialMnVFut El valor a asignar
	 */
	public void setPosicionInicialMnVFut(Double posicionInicialMnVFut) {
		this.posicionInicialMnVFut = posicionInicialMnVFut;
	}
	
    /**
     * El valor de la posici&oacute;n inicial en una divisa determinada en cash.
     */
    private double posicionInicialCash;

    /**
     * El valor de la posici&oacute;n inicial en una divisa determinada en cash.
     */
    private double posicionInicialTom;

    /**
     * El valor de la posici&oacute;n inicial en una divisa determinada en cash.
     */
    private double posicionInicialSpot;

    /**
     * El valor de la posici&oacute;n inicial en 72 Horas.
     */
    private Double posicionInicial72Hr;
    
    /**
     * El valor de la posici&oacute;n inicial en Valor Futuro.
     */
    private Double posicionInicialVFut;
    
    /**
     * El valor de la posici&oacute;n inicial en moneda nacional en cash.
     */
    private double posicionInicialMnCash;

    /**
     * El valor de la posici&oacute;n inicial en moneda nacional en cash.
     */
    private double posicionInicialMnTom;

    /**
     * El valor de la posici&oacute;n inicial en moneda nacional en cash.
     */
    private double posicionInicialMnSpot;
    
    /**
     * El valor de la posici&oacute;n inicial en moneda nacional en 72 Horas.
     */
    private Double posicionInicialMn72Hr;
    
    /**
     * El valor de la posici&oacute;n inicial en moneda nacional en Valor Futuro.
     */
    private Double posicionInicialMnVFut;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 5134977367197438881L;
}
