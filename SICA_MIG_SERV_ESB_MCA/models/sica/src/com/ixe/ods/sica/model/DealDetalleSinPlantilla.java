/*
 * $Id: DealDetalleSinPlantilla.java,v 1.2.40.1 2011/04/26 00:26:17 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Clase persistente para la tabla SC_DEAL_DETALLE que no incluye la relacion con la tabla
 * SC_PLANTILLA. Representa los detalles de compra y/o venta de un producto para una divisa
 * en particular.
 *
 * El detalle incluye:
 * <ul>
 *   <li>Lo que se recibe</li>
 *   <li>Lo que se entrega</li>
 *   <li>Un producto (opcionalmente)</li>
 * </ul>
 *
 * Se tiene una relaci&oacute;n 1:1 a la superclase SC_DEAL_DET_FL, que se
 * refiere a la forma de liquidaci&oacute;n del detalle.
 *
 * @hibernate.joined-subclass table="SC_DEAL_DETALLE"
 * proxy="com.ixe.ods.sica.model.DealDetalleSinPlantilla"
 * mutable="false"
 *
 * @hibernate.joined-subclass-key column="ID_DEAL_POSICION"
 *
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.2.40.1 $ $Date: 2011/04/26 00:26:17 $
 */
public class DealDetalleSinPlantilla extends DealPosicion {

    /**
     * Constructor vac&iacute;o. Inicializa tipoDeal en TIPO_DEAL_CLIENTE.
     */
    public DealDetalleSinPlantilla() {
        super();
        setTipoDeal(TIPO_DEAL_CLIENTE);
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
     * Regresa el valor de observaciones.
     *
     * @hibernate.property column="REVERSADO"
     * not-null="false"
     * unique="false"
     * @return int.
     */
    public int getReversado() {
        return reversado;
    }

    /**
     * Establece el valor de reversado.
     *
     * @param reversado El valor a asignar.
     */
    public void setReversado(int reversado) {
        this.reversado = reversado;
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
     *
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
     * Regresa el valor de tmpTcc.
     *
     * @return double.
     */
    public double getTmpTcc() {
        return tmpTcc;
    }

    /**
     * Fija el valor de tmpTcc.
     *
     * @param tmpTcc El valor a asignar.
     */
    public void setTmpTcc(double tmpTcc) {
        this.tmpTcc = tmpTcc;
    }

    /**
     * Regresa el valor de deal.
     *
     * @hibernate.many-to-one column="ID_DEAL"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Deal"
     * outer-join="auto"
     * unique="false"
     * @return Deal.
     */
    public Deal getDeal() {
        return _deal;
    }

    /**
     * Fija el valor de deal.
     *
     * @param deal El valor a asignar.
     */
    public void setDeal(Deal deal) {
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
     * Regresa el valor de detallesStatusLog.
     *
     * @hibernate.set inverse="true"
     * lazy="true"
     * cascade="none"
     * @hibernate.collection-key column="ID_DETALLE_DEAL"
     * @hibernate.collection-one-to-many class="com.ixe.ods.sica.model.DealDetalleStatusLog"
     * @return Set.
     */
    public Set getDetallesStatusLog() {
        return _detallesStatusLog;
    }

    /**
     * Fija el valor de detallesStatusLog.
     *
     * @param detallesStatusLog El valor a asignar.
     */
    public void setDetallesStatusLog(Set detallesStatusLog) {
        _detallesStatusLog = detallesStatusLog;
    }

    /**
     * Regresa el valor de reemplazadoPorA.
     *
     * @hibernate.property column="REEMPLAZADO_POR_A"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getReemplazadoPorA() {
        return reemplazadoPorA;
    }

    /**
     * Fija el valor de reemplazadoPorA.
     *
     * @param reemplazadoPorA El valor a asignar.
     */
    public void setReemplazadoPorA(Integer reemplazadoPorA) {
        this.reemplazadoPorA = reemplazadoPorA;
    }

    /**
     * Regresa el valor de sustituyeA.
     *
     * @hibernate.property column="SUSTITUYE_A"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getSustituyeA() {
        return sustituyeA;
    }

    /**
     * Fija el valor de sustituyeA.
     *
     * @param sustituyeA El valor a asignar.
     */
    public void setSustituyeA(Integer sustituyeA) {
        this.sustituyeA = sustituyeA;
    }

    /**
     * Regresa el valor de reemplazadoPorB.
     *
     * @hibernate.property column="REEMPLAZADO_POR_B"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getReemplazadoPorB() {
        return reemplazadoPorB;
    }

    /**
     * Fija el valor de reemplazadoPorB.
     *
     * @param reemplazadoPorB El valor a asignar.
     */
    public void setReemplazadoPorB(Integer reemplazadoPorB) {
        this.reemplazadoPorB = reemplazadoPorB;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof DealDetalleSinPlantilla)) {
            return false;
        }
        DealDetalleSinPlantilla castOther = (DealDetalleSinPlantilla) other;
        return new EqualsBuilder().append(this.getIdDealPosicion(), castOther.getIdDealPosicion()).
                append(this.getFolioDetalle(), castOther.getFolioDetalle()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdDealPosicion()).append(folioDetalle).toHashCode();
    }

    /**
     * Permite saber la Descripci&oacute;n del Status de un Deal Detalle.
     *
     * @return La descripcio&oacute;n.
     */
    public String getDescripcionStatus() {
        String desc = "";
        if (Deal.PROC_REVERSO == getReversado()) {
            desc = "En Proc. Reverso";
        }
        else if (Deal.REVERSADO == getReversado()) {
            desc = "Reversado";
        }
        else if (DealDetalle.STATUS_DET_PROCESO_CAPTURA.equals(statusDetalleDeal)) {
            desc = "Alta";
        }
        else if (DealDetalle.STATUS_DET_COMPLETO.equals(statusDetalleDeal)) {
            desc = "Proces\u00e1ndose";
        }
        else if (DealDetalle.STATUS_DET_PARCIALMENTE_PAG_LIQ.equals(statusDetalleDeal)) {
            desc = "Liq. Parcial";
        }
        else if (DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(statusDetalleDeal)) {
            desc = "Totalmente Liq.";
        }
        else if (DealDetalle.STATUS_DET_CANCELADO.equals(statusDetalleDeal)) {
            desc = "Cancelado";
        }
        return desc;
    }

    /**
     * Concatena en forma de cadenas las propiedades del Deal Detalle.
     *
     * @return El Objeto como cadena.
     */
    public String toString() {
        return new ToStringBuilder(this).append("claveFormaLiquidacion", claveFormaLiquidacion).
                append("comisionCobradaMxn", comisionCobradaMxn).
                append("comisionCobradaUsd", comisionCobradaUsd).
                append("comisionOficialUsd", comisionOficialUsd).
                append("costoFormaLiq", costoFormaLiq).append("denominacion", denominacion).
                append("eventosDetalleDeal", eventosDetalleDeal).
                append("factorDivisa", factorDivisa).append("folioDetalle", folioDetalle).
                append("idDealPosicion", idDealPosicion).
                append("idLiquidacionDetalle", idLiquidacionDetalle).
                append("idPrecioReferencia", idPrecioReferencia).append("idSpread", idSpread).
                append("idUsuario", getIdUsuario()).
                append("instruccionesBeneficiario", getInstruccionesBeneficiario()).
                append("instruccionesIntermediario", getInstruccionesIntermediario()).
                append("instruccionesPagador", instruccionesPagador).append("mnemonico", mnemonico).
                append("monto", getMonto()).append("observaciones", getObservaciones()).
                append("statusDetalleDeal", getStatusDetalleDeal()).
                append("tipoCambio", getTipoCambio()).append("tipoCambioMesa", getTipoCambioMesa()).
                append("tipoDeal", getTipoDeal()).toString();
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
    private String eventosDetalleDeal = "          ";

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
     * 0 No reversado, 1 En proceso de reverso, 2 Reversado.
     */
    private int reversado;

    /**
     * El status del detalle del deal.
     */
    private String statusDetalleDeal = Deal.STATUS_DEAL_PROCESO_CAPTURA;

    /**
     * Indica en un detalle de deal cancelado por split o por cambio de forma de liquidaci&oacute;n,
     * el idDealPosicion del detalle de un nuevo deal que lo reemplaza.
     */
    private Integer reemplazadoPorA;

    /**
     * Indica el idDealPosicion del detalle de deal cancelado al que sustituye.
     */
    private Integer sustituyeA;

    /**
     * Indica en un detalle de deal cancelado por split, el idDealPosicion del detalle de un nuevo
     * deal que lo reemplaza.
     */
    private Integer reemplazadoPorB;

    /**
     * El tipo de cambio de la pizarra.
     */
    private double tipoCambioMesa;

    /**
     * El tipo de cambio para correcci&oacute;n de desviaciones.
     */
    private double tmpTcc;

    /**
     * Relaci&oacute;n muchos-a-uno con Deal.
     */
    private Deal _deal;

    /**
     * El identificador del Factor Divisa utilizado.
     * 
     * @deprecated Se debe utilizar el valor directo del Factor Divisa.
     */
    private Integer idFactorDivisa;
    
    /**
     * El factor de divisa utilizado al momento de la captura.
     */
    private Double factorDivisa;

    /**
     * Relaci&oacute;n uno-a-muchos con DealDetalleStatusLog.
     */
    private Set _detallesStatusLog = new HashSet();

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -5352457011345753261L;
}