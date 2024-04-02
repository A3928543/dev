/*
 * $Id: HistoricoDealDetalle.java,v 1.12.42.2 2011/04/29 00:29:16 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Clase persistente para la tabla SC_H_DEAL_DETALLE, donde se almacenan el
 * hist&oacute;rico de los Detalles de los Deals. </p>
 *
 * @hibernate.class table="SC_H_DEAL_DETALLE"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.HistoricoDealDetalle"
 * dynamic-update="true"
 * 
 * @author Jean C. Favila
 * @version  $Revision: 1.12.42.2 $ $Date: 2011/04/29 00:29:16 $
 */
public class HistoricoDealDetalle implements Serializable {

	/**
     * Constructor vacio.
     */
    public HistoricoDealDetalle() {
    	super();
    }
    
	/**
     * Constructor que recibe un Deal Detalle e inicializa todas sus variables.
     * 
     * @param dd El detalle de deal que inicializa el hist&oacute;rico.
     */
    public HistoricoDealDetalle(DealDetalle dd) {
        super();
		setIdDealPosicion(dd.getIdDealPosicion());
		setClaveFormaLiquidacion(dd.getClaveFormaLiquidacion() != null ? dd.getClaveFormaLiquidacion(): null);
		setComisionOficialUsd(dd.getComisionOficialUsd());
		setComisionCobradaUsd(dd.getComisionOficialUsd());
		setComisionCobradaMxn(dd.getComisionCobradaMxn());
		setCostoFormaLiq(dd.getCostoFormaLiq()!= null ? dd.getCostoFormaLiq(): null);
		setDenominacion(dd.getDenominacion() != null ? dd.getDenominacion(): null);
		setEventosDetalleDeal(dd.getEventosDetalleDeal());
		setFolioDetalle(dd.getFolioDetalle());
		setDeal(dd.getDeal().getIdDeal());
		setFactorDivisa(dd.getFactorDivisa());
		setIdFactorDivisa(dd.getIdFactorDivisa());
		setPlantilla(dd.getPlantilla() != null ? new Integer(dd.getPlantilla().getIdPlantilla()) : null);
		setPrecioReferenciaMidSpot(dd.getPrecioReferenciaMidSpot());
		setPrecioReferenciaSpot(dd.getPrecioReferenciaSpot());
		setIdPrecioReferencia(dd.getIdPrecioReferencia());
		setIdSpread(dd.getIdSpread());
		setIdLiquidacionDetalle(dd.getIdLiquidacionDetalle() != null ? dd.getIdLiquidacionDetalle(): null);
		setInstruccionesBeneficiario(dd.getInstruccionesBeneficiario() != null ? dd.getInstruccionesBeneficiario(): null);
		setInstruccionesIntermediario(dd.getInstruccionesIntermediario() != null ? dd.getInstruccionesIntermediario(): null);
		setInstruccionesPagador(dd.getInstruccionesPagador()!= null ? dd.getInstruccionesPagador(): null);
		setMnemonico(dd.getMnemonico() != null ? dd.getMnemonico(): null);
		setObservaciones(dd.getObservaciones()!= null ? dd.getObservaciones(): null);
		setStatusDetalleDeal(dd.getStatusDetalleDeal());
		setTipoCambioMesa(dd.getTipoCambioMesa());
    }

    /**
     * Regresa el valor de idDeal.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_DEAL_POSICION"
     * unsaved-value="null"
     * @return int.
     */
    public int getIdDealPosicion() {
        return idDealPosicion;
    }


    /**
     * Fija el valor de idDealPosicion.
     *
     * @param idDealPosicion El valor a asignar.
     */
    public void setIdDealPosicion(int idDealPosicion) {
        this.idDealPosicion = idDealPosicion;
    }
    
    /**
     * Regresa el valor de claveFormaLiquidacion.
     *
     * @hibernate.property column="CLAVE_FORMA_LIQUIDACION"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getClaveFormaLiquidacion() {
        return claveFormaLiquidacion;
    }

    /**
     * Fija el valor de claveFormaLiquidacion.
     *
     * @param claveFormaLiquidacion La claveFormaLiquidacion a asignar.
     */
    public void setClaveFormaLiquidacion(String claveFormaLiquidacion) {
        this.claveFormaLiquidacion = claveFormaLiquidacion;
    }

    /**
     * Regresa el valor de comisionOficialUsd.
     *
     * @hibernate.property column="COMISION_OFICIAL_USD"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getComisionOficialUsd() {
        return comisionOficialUsd;
    }

    /**
     * Fija el valor de comisionOficialUsd.
     *
     * @param comisionOficialUsd El valor a asignar.
     */
    public void setComisionOficialUsd(double comisionOficialUsd) {
        this.comisionOficialUsd = comisionOficialUsd;
    }

    /**
     * Regresa el valor de comisionCobradaUsd.
     *
     * @hibernate.property column="COMISION_COBRADA_USD"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getComisionCobradaUsd() {
        return comisionCobradaUsd;
    }

    /**
     * Fija el valor de comisionCobradaUsd.
     *
     * @param comisionCobradaUsd El valor a asignar.
     */
    public void setComisionCobradaUsd(double comisionCobradaUsd) {
        this.comisionCobradaUsd = comisionCobradaUsd;
    }

    /**
     * Regresa el valor de comisionOficialMxn.
     *
     * @hibernate.property column="COMISION_COBRADA_MXN"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getComisionCobradaMxn() {
        return comisionCobradaMxn;
    }

    /**
     * Fija el valor de comisionCobradaMxn.
     *
     * @param comisionCobradaMxn El valor a asignar.
     */
    public void setComisionCobradaMxn(double comisionCobradaMxn) {
        this.comisionCobradaMxn = comisionCobradaMxn;
    }

    /**
     * Regresa el valor de costoFormaLiq.
     *
     * @hibernate.property column="COSTO_FORMA_LIQ"
     * not-null="false"
     * unique="false"
     * @return BigDecimal.
     */
    public BigDecimal getCostoFormaLiq() {
        return costoFormaLiq;
    }

    /**
     * Fija el valor de costoFormaLiq.
     *
     * @param costoFormaLiq El valor a asignar.
     */
    public void setCostoFormaLiq(BigDecimal costoFormaLiq) {
        this.costoFormaLiq = costoFormaLiq;
    }

    /**
     * Regresa el valor de denominacion.
     *
     * @hibernate.property column="DENOMINACION"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getDenominacion() {
        return denominacion;
    }

    /**
     * Fija el valor de denominacion.
     *
     * @param denominacion El valor a asignar.
     */
    public void setDenominacion(Double denominacion) {
        this.denominacion = denominacion;
    }

    /**
     * Regresa el valor de eventosDetalleDeal.
     *
     * @hibernate.property column="EVENTOS_DETALLE_DEAL"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getEventosDetalleDeal() {
        return eventosDetalleDeal;
    }

    /**
     * Fija el valor de eventosDetalleDeal.
     *
     * @param eventosDetalleDeal El valor a asignar.
     */
    public void setEventosDetalleDeal(String eventosDetalleDeal) {
        this.eventosDetalleDeal = eventosDetalleDeal;
    }

    /**
     * Regresa el valor de folioDetalle.
     *
     * @hibernate.property column="FOLIO_DETALLE"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getFolioDetalle() {
        return folioDetalle;
    }

    /**
     * Fija el valor de folioDetalle.
     *
     * @param folioDetalle El valor a asignar.
     */
    public void setFolioDetalle(int folioDetalle) {
        this.folioDetalle = folioDetalle;
    }

    /**
     * Regresa el valor de idLiquidacionDetalle.
     *
     * @hibernate.property column="ID_LIQUIDACION_DETALLE"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getIdLiquidacionDetalle() {
        return idLiquidacionDetalle;
    }

    /**
     * Fija el valor de idLiquidacionDetalle.
     *
     * @param idLiquidacionDetalle El valor de idLiquidacionDetalle.
     */
    public void setIdLiquidacionDetalle(Integer idLiquidacionDetalle) {
        this.idLiquidacionDetalle = idLiquidacionDetalle;
    }

    /**
     * Regresa el valor de idPrecioReferencia.
     * 
     * @deprecated Se utilizar&aacute; el valor directo del Precio Referencia
     * @hibernate.property column="ID_PRECIO_REFERENCIA"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getIdPrecioReferencia() {
        return idPrecioReferencia;
    }

    /**
     * Fija el valor de idPrecioReferencia.
     *
     * @deprecated Se utilizar&aacute; el valor directo del Precio Referencia
     * @param idPrecioReferencia El valor a asignar.
     */
    public void setIdPrecioReferencia(Integer idPrecioReferencia) {
        this.idPrecioReferencia = idPrecioReferencia;
    }
    
    /**
     * Regresa el valor de precioReferenciaMidSpot.
     * 
     * @hibernate.property column="PRE_REF_MID_SPOT"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getPrecioReferenciaMidSpot() {
        return precioReferenciaMidSpot;
    }

    /**
     * Fija el valor de precioReferenciaMidSpot.
     *
     * @param precioReferenciaMidSpot El valor a asignar.
     */
    public void setPrecioReferenciaMidSpot(Double precioReferenciaMidSpot) {
        this.precioReferenciaMidSpot = precioReferenciaMidSpot;
    }
    
    /**
     * Regresa el valor de precioReferenciaSpot.
     * 
     * @hibernate.property column="PRE_REF_SPOT"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getPrecioReferenciaSpot() {
        return precioReferenciaSpot;
    }

    /**
     * Fija el valor de precioReferenciaSpot.
     *
     * @param precioReferenciaSpot El valor a asignar.
     */
    public void setPrecioReferenciaSpot(Double precioReferenciaSpot) {
        this.precioReferenciaSpot = precioReferenciaSpot;
    }

    /**
     * Regresa el valor de idSpread.
     *
     * @hibernate.property column="ID_SPREAD"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getIdSpread() {
        return idSpread;
    }

    /**
     * Fija el valor de idSpread.
     *
     * @param idSpread El valor a asignar.
     */
    public void setIdSpread(int idSpread) {
        this.idSpread = idSpread;
    }

    /**
     * Regresa el valor de instruccionesBeneficiario.
     *
     * @hibernate.property column="INSTRUCCIONES_BENEFICIARIO"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getInstruccionesBeneficiario() {
        return instruccionesBeneficiario;
    }

    /**
     * Fija el valor de instruccionesBeneficiario.
     *
     * @param instruccionesBeneficiario El valor a asignar.
     */
    public void setInstruccionesBeneficiario(String instruccionesBeneficiario) {
        this.instruccionesBeneficiario = instruccionesBeneficiario;
    }

    /**
     * Regresa el valor de instruccionesIntermediario.
     *
     * @hibernate.property column="INSTRUCCIONES_INTERMEDIARIO"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getInstruccionesIntermediario() {
        return instruccionesIntermediario;
    }

    /**
     * Fija el valor de instruccionesIntermediario.
     *
     * @param instruccionesIntermediario El valor a asignar.
     */
    public void setInstruccionesIntermediario(String instruccionesIntermediario) {
        this.instruccionesIntermediario = instruccionesIntermediario;
    }

    /**
     * Regresa el valor de instruccionesPagador.
     *
     * @hibernate.property column="INSTRUCCIONES_PAGADOR"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getInstruccionesPagador() {
        return instruccionesPagador;
    }

    /**
     * Fija el valor de instruccionesPagador.
     *
     * @param instruccionesPagador El valor a asignar.
     */
    public void setInstruccionesPagador(String instruccionesPagador) {
        this.instruccionesPagador = instruccionesPagador;
    }

    /**
     * Regresa el valor de mnemonico.
     *
     * @hibernate.property column="MNEMONICO"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getMnemonico() {
        return mnemonico;
    }

    /**
     * Fija el valor de mnemonico.
     *
     * @param mnemonico El mnemonico a asignar.
     */
    public void setMnemonico(String mnemonico) {
        this.mnemonico = mnemonico;
    }

    /**
     * Regresa el valor de observaciones.
     *
     * @hibernate.property column="OBSERVACIONES"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Fija el valor de observaciones.
     *
     * @param observaciones El valor a asignar.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Regresa el valor de statusDetalleDeal.
     *
     * @hibernate.property column="STATUS_DETALLE_DEAL"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getStatusDetalleDeal() {
        return statusDetalleDeal;
    }

    /**
     * Fija el valor de statusDetalleDeal.
     *
     * @param statusDetalleDeal El valor a asignar.
     */
    public void setStatusDetalleDeal(String statusDetalleDeal) {
        this.statusDetalleDeal = statusDetalleDeal;
    }

    /**
     * Regresa el valor de tipoCambioMesa.
     * @hibernate.property column="TIPO_CAMBIO_MESA"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getTipoCambioMesa() {
        return tipoCambioMesa;
    }

    /**
     * Fija el valor de tipoCambioMesa.
     *
     * @param tipoCambioMesa El valor a asignar.
     */
    public void setTipoCambioMesa(double tipoCambioMesa) {
        this.tipoCambioMesa = tipoCambioMesa;
    }

    
    /**
     * Regresa el valor de deal.
     * @hibernate.property column="ID_DEAL"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public int getDeal() {
        return _deal;
    }

    /**
     * Fija el valor de deal.
     *
     * @param deal El valor a asignar.
     */
    public void setDeal(int deal) {
        _deal = deal;
    }

    /**
     * Regresa el valor de idFactorDivisa.
     * 
     * @deprecated Se debe utilizar el valor directo del Factor Divisa.
     * @hibernate.property column="ID_FACTOR_DIVISA"
     * not-null="false"
     * unique="false"
     * @return idFactorDivisa.
     */
    public Integer getIdFactorDivisa() {
        return idFactorDivisa;
    }

    /**
     * Fija el valor de idFactorDivisa.
     *
     * @deprecated Se debe utilizar el valor directo del precio referencia.
     * @param idFactorDivisa El valor a asignar.
     */
    public void setIdFactorDivisa(Integer idFactorDivisa) {
        this.idFactorDivisa = idFactorDivisa;
    }
    
    /**
     * Regresa el valor de factorDivisa.
     * 
     * @hibernate.property column="FACTOR_DIVISA"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getFactorDivisa() {
        return factorDivisa;
    }
    
    /**
     * Fija el valor de factorDivisa.
     *
     * @param factorDivisa El valor a asignar.
     */
    public void setFactorDivisa(Double factorDivisa) {
        this.factorDivisa = factorDivisa;
    }

    /**
     * Regresa el valor de plantilla.
     * @hibernate.property column="ID_PLANTILLA"
     * not-null="false"
     * unique="false"
     * @return Integer
     */
    public Integer getPlantilla() {
        return _plantilla;
    }

    /**
     * Fija el valor de plantilla.
     *
     * @param plantilla El valor a asignar.
     */
    public void setPlantilla(Integer plantilla) {
        _plantilla = plantilla;
    }

    /**
     * La clave del producto.
     */
    private String claveFormaLiquidacion;

    /**
     * La comisi&oacute;n como debi&oacute; cobrarse.
     */
    private double comisionOficialUsd;

    /**
     * La comisi&oacute;n como se cobr&oacute; en divisa USD.
     */
    private double comisionCobradaUsd;

    /**
     * La comisi&oacute;n como se cobr&oacute; en divisa MXN.
     */
    private double comisionCobradaMxn;

    /**
     * El costo de la forma de liquidaci&oacute;n.
     */
    private BigDecimal costoFormaLiq;

    /**
     * Especificar qu&eacute; denominaciones se desean.
     */
    private Double denominacion;

    /**
     * Los eventos del detalle.
     */
    private String eventosDetalleDeal = "      ";
    
    /**
     * El identificador del registro.
     */
    protected int idDealPosicion;

    /**
     * El folio del detalle.
     */
    private int folioDetalle;

    /**
     * El identificador del detalle de liquidaci&oacute;n.
     */
    private Integer idLiquidacionDetalle;

    /**
     * Identificador del precio de referencia utilizado al momento de la
     * captura.
     * 
     * @deprecated Se debe utilizar el valor directo del Precio Referencia.
     */
    private Integer idPrecioReferencia;
    
    /**
     * El precio referencia Mid Spot utilizado al momento de la
     * captura.
     */
    private Double precioReferenciaMidSpot;
    
    /**
     * El precio referencia Spot utilizado al momento de la
     * captura.
     */
    private Double precioReferenciaSpot;

    /**
     * Identificador del spread utilizado al momento de la captura.
     */
    private int idSpread;

    /**
     * Las instrucciones para el beneficiario.
     */
    private String instruccionesBeneficiario;

    /**
     * Las instrucciones para el banco intermediario.
     */
    private String instruccionesIntermediario;

    /**
     * Las instrucciones para el banco pagador.
     */
    private String instruccionesPagador;

    /**
     * El mnemonico de la matriz de liquidaciones.
     */
    private String mnemonico;

    /**
     * Alguna instrucci&oacute;n especial para la liquidaci&oacute;n (opcional).
     */
    private String observaciones;

    /**
     * El status del detalle del deal.
     */
    private String statusDetalleDeal = Deal.STATUS_DEAL_PROCESO_CAPTURA;

    /**
     * El tipo de cambio de la pizarra.
     */
    private double tipoCambioMesa;

    /**
     * Relaci&oacute;n muchos-a-uno con Deal.
     */
    private int _deal;

    /**
     * El factor de divisa utilizado al momento de la captura.
     */
    private Double factorDivisa;
    
    /**
     * El identificador del Factor Divisa utilizado.
     * 
     * @deprecated Se debe utilizar el valor directo del Factor Divisa.
     */
    private Integer idFactorDivisa;

    /**
     * Relaci&oacute;n muchos-a-uno con Plantilla.
     */
    private Integer _plantilla;

    /**
     * Relaci&oacute;n uno-a-muchos con DealDetalleStatusLog.
     */
    private Set _detallesStatusLog = new HashSet();
}
