/*
 * $Id: DealDetWorkitemVO.java,v 1.12.36.1 2010/10/08 01:32:41 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.vo;

import java.io.Serializable;

/**
 * Value Object para la comunicaci&oacute;n del SICA con la aplicaci&oacute;n en Flex 
 * de Autorizaciones de deals por parte de la mesa.
 * 
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.12.36.1 $ $Date: 2010/10/08 01:32:41 $
 */
public class DealDetWorkitemVO implements Serializable {

    /**
	 * Constructor por Default.
	 *
	 */
    public DealDetWorkitemVO() {
        super();
    }
    
    /**
     * Constructor para inicializar todas las variables de instancia.
     * 
     * @param idDealPosicion El el id del deal en la posici&ocaute;n.
     * @param tipoOp El tipo de operaci&oacute;n del deal.
     * @param claveFormaLiquidacion La clave de liquidaci&oacute;n.
     * @param idDivisa El id de la divisa.
     * @param monto El monto del deal.
     * @param tcc El tipo de cambio para el cliente.
     * @param tcm El tipo de cambio para la mesa.
     */
    public DealDetWorkitemVO(int idDealPosicion, String tipoOp, String claveFormaLiquidacion,
                             String idDivisa, double monto, double tcc, double tcm) {
        this();
        this.idDealPosicion = idDealPosicion;
        this.tipoOp = tipoOp;
        this.claveFormaLiquidacion = claveFormaLiquidacion;
        this.idDivisa = idDivisa;
        this.monto = monto;
        this.tcc = tcc;
        this.tcm = tcm;
    }
    
    /**
     * Constructor para inicializar todas las variables de instancia.
     * 
     * @param idDealPosicion El el id del deal en la posici&ocaute;n.
     * @param tipoOp El tipo de operaci&oacute;n del deal.
     * @param claveFormaLiquidacion La clave de liquidaci&oacute;n.
     * @param idDivisa El id de la divisa.
     * @param monto El monto del deal.
     * @param tcc El tipo de cambio para el cliente.
     * @param tcm El tipo de cambio para la mesa.
     */
    public DealDetWorkitemVO(int idDealPosicion, String tipoOp, String claveFormaLiquidacion,
                             String idDivisa, double monto, double tcc, double tcm,
                             double limiteDiario, double acumDiario, double limiteDiarioIxe,
                             double limiteMensual, double acumMensual, double limiteMensualIxe) {
        this();
        this.idDealPosicion = idDealPosicion;
        this.tipoOp = tipoOp;
        this.claveFormaLiquidacion = claveFormaLiquidacion;
        this.idDivisa = idDivisa;
        this.monto = monto;
        this.tcc = tcc;
        this.tcm = tcm;
        this.limiteDiario = limiteDiario;
        this.acumDiario = acumDiario;
        this.limiteDiarioIxe = limiteDiarioIxe;
        this.limiteMensual = limiteMensual;
        this.acumMensual = acumMensual;
        this.limiteMensualIxe = limiteMensualIxe;
    }
    
    /**
     * Obtiene el valor del idDealPosicion.
     * 
     * @return int
     */
    public int getIdDealPosicion() {
        return idDealPosicion;
    }
    
    /**
     * Asigna el valor para idDealPosicion
     * 
     * @param idDealPosicion El valor para idDealPosicion.
     */
    public void setIdDealPosicion(int idDealPosicion) {
        this.idDealPosicion = idDealPosicion;
    }

    /**
     * Otiene el valor de tipoOp.
     * 
     * @return String
     */
    public String getTipoOp() {
        return tipoOp;
    }
    
    /**
     * Asigna el valor para tipoOp.
     * 
     * @param tipoOp El valor para tipoOp.
     */
    public void setTipoOp(String tipoOp) {
        this.tipoOp = tipoOp;
    }
    
    /**
     * Obtiene el valor de claveFormaLiquidacion.
     * 
     * @return String.
     */
    public String getClaveFormaLiquidacion() {
        return claveFormaLiquidacion;
    }
    
    /**
     * Asigna el valor para claveFormaLiquidacion.
     * 
     * @param claveFormaLiquidacion El valor para clave formaLiquidacion.
     */
    public void setClaveFormaLiquidacion(String claveFormaLiquidacion) {
        this.claveFormaLiquidacion = claveFormaLiquidacion;
    }

    /**
     * Obtiene el valor de idDivisa.
     * 
     * @return String
     */
    public String getIdDivisa() {
        return idDivisa;
    }
    
    /**
     * Asigna el vaolor para idDivisa.
     * 
     * @param idDivisa El valor para idDivisa.
     */
    public void setIdDivisa(String idDivisa) {
        this.idDivisa = idDivisa;
    }

    /**
     * Obtiene el valor de monto.
     * 
     * @return double
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Asigna el valor para monto.
     * 
     * @param monto El valor para monto.
     */
    public void setMonto(double monto) {
        this.monto = monto;
    }

    /**
     * Obtiene el valor de tcc.
     *
     * @return double
     */
    public double getTcc() {
        return tcc;
    } 

    /**
     * Asigna el valor para tcc.
     * 
     * @param tcc El valor para tcc.
     */
    public void setTcc(double tcc) {
        this.tcc = tcc;
    }

    /**
     * Obtiene el valor de tcm.
     * 
     * @return double
     */
    public double getTcm() {
        return tcm;
    }

    /**
     * Asigna el valor para tcm.
     * 
     * @param tcm El valor para tcm.
     */
    public void setTcm(double tcm) {
        this.tcm = tcm;
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
	 * Regresa el valor de acumDiario.
	 *
	 * @return double.
	 */
	public double getAcumDiario() {
		return acumDiario;
	}
	
	/**
	 * Establece el valor de acumDiario.
	 *
	 * @param acumDiario El valor a asignar.
	 */
	public void setAcumDiario(double acumDiario) {
		this.acumDiario = acumDiario;
	}
	
	/**
	 * Regresa el valor de limiteDiarioIxe.
	 *
	 * @return double.
	 */
	public double getLimiteDiarioIxe() {
		return limiteDiarioIxe;
	}
	
	/**
	 * Establece el valor de limiteDiarioIxe.
	 *
	 * @param limiteDiarioIxe El valor a asignar.
	 */
	public void setLimiteDiarioIxe(double limiteDiarioIxe) {
		this.limiteDiarioIxe = limiteDiarioIxe;
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
	 * Regresa el valor de acumMensual.
	 *
	 * @return double.
	 */
	public double getAcumMensual() {
		return acumMensual;
	}
	
	/**
	 * Establece el valor de acumMensual.
	 *
	 * @param acumMensual El valor a asignar.
	 */
	public void setAcumMensual(double acumMensual) {
		this.acumMensual = acumMensual;
	}
	
	/**
	 * Regresa el valor de limiteMensualIxe.
	 *
	 * @return double.
	 */
	public double getLimiteMensualIxe() {
		return limiteMensualIxe;
	}
	
	/**
	 * Establece el valor de limiteMensualIxe.
	 *
	 * @param limiteMensualIxe
	 */
	public void setLimiteMensualIxe(double limiteMensualIxe) {
		this.limiteMensualIxe = limiteMensualIxe;
	}

    /**
     * El id del deal en la posici&oacute;n
     */
    private int idDealPosicion;
    
    /**
     * El tipo de operaci&oacute;n.
     */
    private String tipoOp;
    
    /**
     * La clave de la forma de liquidaci&oacute;n.
     */
    private String claveFormaLiquidacion;
    
    /**
     * El id de la divisa.
     */
    private String idDivisa;
    
    /**
     * El monto del deal.
     */
    private double monto;
    
    /**
     * El tipo de cambio para el cliente.
     */
    private double tcc;
    
    /**
     * El tipo de cambio para la mesa.
     */
    private double tcm;
    
    /**
     * El l&iacute;mite diario normal.
     */
    private double limiteDiario;
    
    /**
     * El monto diario acumulado en ese producto, de esa divisa.
     */
    private double acumDiario;
    
    /**
     * El l&iacute;mite diario Ixe.
     */
    private double limiteDiarioIxe;
    
    /**
     * El l&iacute;mite mensual normal.
     */
    private double limiteMensual;
    
    /**
     * El monto mensual acumulado en ese producto, de esa divisa.
     */
    private double acumMensual;
    
    /**
     * El l&iacute;mite mensual Ixe.
     */
    private double limiteMensualIxe;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -4352358571438269303L;
}
