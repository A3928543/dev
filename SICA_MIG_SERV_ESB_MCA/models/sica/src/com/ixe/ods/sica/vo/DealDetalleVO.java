/*
 * $Id: DealDetalleVO.java,v 1.4.40.1 2011/04/26 01:07:22 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ixe.ods.sica.model.DealDetalle;

/**
 * Value Object para transmitir la informaci&oacute;n de un detalle de deal entre Flex y Java.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.4.40.1 $ $Date: 2011/04/26 01:07:22 $
 */
public class DealDetalleVO implements Serializable {

    /**
     * Constructor por default.
     */
    public DealDetalleVO() {
		super();
	}

    /**
     * Constructor para inicializar las variables de instancia a partir del modelo de Hibernate para
     * el detalle de deal.
     *
     * @param dealDetalle El modelo de Hibernate de donde se tomar&aacute;n los valores.
     */
    public DealDetalleVO(DealDetalle dealDetalle) {
		this();
		idDealPosicion = dealDetalle.getIdDealPosicion();
        folioDetalle = dealDetalle.getFolioDetalle();
        claveFormaLiquidacion = dealDetalle.getClaveFormaLiquidacion();
        mnemonico = dealDetalle.getMnemonico();
        idDivisa = dealDetalle.getDivisa().getIdDivisa();
        comisionCobradaMxn = dealDetalle.getComisionCobradaMxn();
        comisionCobradaUsd = dealDetalle.getComisionCobradaUsd();
        comisionOficialUsd = dealDetalle.getComisionOficialUsd();
        tipoDivisa = dealDetalle.getDivisa().getTipo();
        monto = dealDetalle.getMonto();
		recibimos = dealDetalle.isRecibimos();
		tipoCambio = dealDetalle.getTipoCambio();
        precioReferenciaMidSpot = dealDetalle.getPrecioReferenciaMidSpot();
        precioReferenciaSpot = dealDetalle.getPrecioReferenciaSpot();
        factorDivisa = dealDetalle.getFactorDivisa();
        idPrecioReferencia = dealDetalle.getIdPrecioReferencia();
        idFactorDivisa = dealDetalle.getIdFactorDivisa();
        idSpread = dealDetalle.getIdSpread();
        idPlantilla = dealDetalle.getPlantilla() != null ?
                new Integer(dealDetalle.getPlantilla().getIdPlantilla()) : null;
        tipoCambioMesa = dealDetalle.getTipoCambioMesa();
		statusDetalleDeal = dealDetalle.getStatusDetalleDeal();
        descripcionStatus = dealDetalle.getDescripcionStatus();
		importe = dealDetalle.getImporte();
	}

    /**
     * Regresa el valor de folioDetalle.
     *
     * @return int.
     */
    public int getFolioDetalle() {
		return folioDetalle;
	}

    /**
     * Establece el valor de folioDetalle.
     *
     * @param folioDetalle El valor a asignar.
     */
    public void setFolioDetalle(int folioDetalle) {
		this.folioDetalle = folioDetalle;
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
     * Regresa el valor de comisionOficialUsd.
     *
     * @return double.
     */
    public double getComisionOficialUsd() {
        return comisionOficialUsd;
    }

    /**
     * Establece el valor de comisionOficialUsd.
     *
     * @param comisionOficialUsd El valor a asignar.
     */
    public void setComisionOficialUsd(double comisionOficialUsd) {
        this.comisionOficialUsd = comisionOficialUsd;
    }

    /**
     * Regresa el valor de comisionCobradaUsd.
     *
     * @return double.
     */
    public double getComisionCobradaUsd() {
        return comisionCobradaUsd;
    }

    /**
     * Establece el valor de comisionCobradaUsd.
     *
     * @param comisionCobradaUsd El valor a asignar.
     */
    public void setComisionCobradaUsd(double comisionCobradaUsd) {
        this.comisionCobradaUsd = comisionCobradaUsd;
    }

    /**
     * Regresa el valor de comisionCobradaMxn.
     *
     * @return double.
     */
    public double getComisionCobradaMxn() {
        return comisionCobradaMxn;
    }

    /**
     * Establece el valor de comisionCobradaMxn.
     *
     * @param comisionCobradaMxn El valor a asignar.
     */
    public void setComisionCobradaMxn(double comisionCobradaMxn) {
        this.comisionCobradaMxn = comisionCobradaMxn;
    }

    /**
     * Regresa el valor de recibimos.
     *
     * @return recibimos.
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
     * Regresa el valor de idDealPosicion.
     *
     * @return int.
     */
    public int getIdDealPosicion() {
		return idDealPosicion;
	}

    /**
     * Establece el valor de idDealPosicion.
     *
     * @param idDealPosicion El valor a asignar.
     */
    public void setIdDealPosicion(int idDealPosicion) {
		this.idDealPosicion = idDealPosicion;
	}

    /**
     * Regresa el valor de mnenmonico.
     *
     * @return String.
     */
    public String getMnemonico() {
		return mnemonico;
	}

    /**
     * Establece el valor de mnemonico.
     *
     * @param mnemonico El valor a asignar.
     */
    public void setMnemonico(String mnemonico) {
		this.mnemonico = mnemonico;
	}

    /**
     * Regresa el valor de monto.
     *
     * @return double.
     */
    public double getMonto() {
		return monto;
	}

    /**
     * Establece el valor de monto.
     *
     * @param monto El valor a asignar.
     */
    public void setMonto(double monto) {
		this.monto = monto;
	}

    /**
     * Regresa el valor de statusDetalleDeal.
     *
     * @return String.
     */
    public String getStatusDetalleDeal() {
		return statusDetalleDeal;
	}

    /**
     * Establece el valor de statusDetalleDeal.
     *
     * @param statusDetalleDeal El valor a asignar.
     */
    public void setStatusDetalleDeal(String statusDetalleDeal) {
		this.statusDetalleDeal = statusDetalleDeal;
	}

    /**
     * Regresa el valor de descripcionStatus.
     *
     * @return String.
     */
    public String getDescripcionStatus() {
        return descripcionStatus;
    }

    /**
     * Establece el valor de descripcionStatus.
     *
     * @param descripcionStatus El valor a asignar.
     */
    public void setDescripcionStatus(String descripcionStatus) {
        this.descripcionStatus = descripcionStatus;
    }

    /**
     * Regresa el valor de tipoCambio.
     *
     * @return double.
     */
    public double getTipoCambio() {
		return tipoCambio;
	}

    /**
     * Establece el valor de tipoCambio.
     *
     * @param tipoCambio El valor a asignar.
     */
    public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

    /**
     * Regresa el valor de tipoCambioMesa.
     *
     * @return double.
     */
    public double getTipoCambioMesa() {
		return tipoCambioMesa;
	}

    /**
     * Establece el valor de tipoCambioMesa.
     *
     * @param tipoCambioMesa El valor a asignar.
     */
    public void setTipoCambioMesa(double tipoCambioMesa) {
		this.tipoCambioMesa = tipoCambioMesa;
	}

    /**
     * Regresa el valor de importe.
     *
     * @return double.
     */
    public double getImporte() {
		return importe;
	}

    /**
     * Establece el valor de importe.
     *
     * @param importe El valor a asignar.
     */
    public void setImporte(double importe) {
		this.importe = importe;
	}

    /**
     * Regresa el valor de idPrecioReferencia.
     * 
     * @deprecated Se debe utlizar el valor directo del Precio Referencia.
     * @return Integer.
     */
    public Integer getIdPrecioReferencia() {
        return idPrecioReferencia;
    }

    /**
     * Establece el valor de idPrecioReferencia.
     *
     * @deprecated Se debe utlizar el valor directo del Precio Referencia.
     * @param idPrecioReferencia El valor a asignar.
     */
    public void setIdPrecioReferencia(Integer idPrecioReferencia) {
        this.idPrecioReferencia = idPrecioReferencia;
    }
    
    /**
     * Regresa el valor de precioReferenciaMidSpot.
     * 
     * @return double
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
     * @return double
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
     * Regresa el valor de factorDivisa.
     *
     * @return Integer.
     */
    public Double getFactorDivisa() {
        return factorDivisa;
    }

    /**
     * Establece el valor de factorDivisa.
     *
     * @param factorDivisa El valor a asignar.
     */
    public void setFactorDivisa(Double factorDivisa) {
        this.factorDivisa = factorDivisa;
    }
    
    /**
     * Regresa el valor de idFactorDivisa.
     *
     * @deprecated Se debe utilizar el valor directo del Factor Divisa.
     * @return Integer.
     */
    public Integer getIdFactorDivisa() {
        return idFactorDivisa;
    }

    /**
     * Establece el valor de idFactorDivisa.
     * 
     * @deprecated Se debe utilizar el valor directo del Factor Divisa.
     * @param idFactorDivisa El valor a asignar.
     */
    public void setIdFactorDivisa(Integer idFactorDivisa) {
        this.idFactorDivisa = idFactorDivisa;
    }
    /**
     * Regresa el valor de idSpread.
     *
     * @return int.
     */
    public int getIdSpread() {
        return idSpread;
    }

    /**
     * Establece el valor de idSpread.
     *
     * @param idSpread El valor a asignar.
     */
    public void setIdSpread(int idSpread) {
        this.idSpread = idSpread;
    }

    /**
     * Regresa el valor de montoModificado.
     *
     * @return boolean.
     */
    public boolean isMontoModificado() {
        return montoModificado;
    }

    /**
     * Establece el valor de montoModificado.
     *
     * @param montoModificado El valor a asignar.
     */
    public void setMontoModificado(boolean montoModificado) {
        this.montoModificado = montoModificado;
    }

    /**
     * Regresa el valor de tipoCambioModificado.
     *
     * @return boolean.
     */
    public boolean isTipoCambioModificado() {
        return tipoCambioModificado;
    }

    /**
     * Establece el valor de tipoCambioModificado.
     *
     * @param tipoCambioModificado El valor a asignar.
     */
    public void setTipoCambioModificado(boolean tipoCambioModificado) {
        this.tipoCambioModificado = tipoCambioModificado;
    }

    /**
     * Regresa el valor de idPlantilla.
     *
     * @return Integer.
     */
    public Integer getIdPlantilla() {
        return idPlantilla;
    }

    /**
     * Establece el valor de idPlantilla.
     *
     * @param idPlantilla El valor a asignar.
     */
    public void setIdPlantilla(Integer idPlantilla) {
        this.idPlantilla = idPlantilla;
    }

    /**
     * Regresa el valor de datosAdicionales.
     *
     * @return List.
     */
    public List getDatosAdicionales() {
        return datosAdicionales;
    }

    /**
     * Establece el valor de datosAdicionales.
     *
     * @param datosAdicionales El valor a asignar.
     */
    public void setDatosAdicionales(List datosAdicionales) {
        this.datosAdicionales = datosAdicionales;
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
     * Regresa el valor de reversado.
     *
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
     * Regresa el valor de tipoDivisa.
     *
     * @return String.
     */
    public String getTipoDivisa() {
        return tipoDivisa;
    }

    /**
     * Establece el valor de tipoDivisa.
     *
     * @param tipoDivisa El valor a asignar.
     */
    public void setTipoDivisa(String tipoDivisa) {
        this.tipoDivisa = tipoDivisa;
    }

    /**
     * El n&uacute;mero de detalle de deal (llave primaria).
     */
    private int idDealPosicion;

    /**
     * El folio del detalle de deal.
     */
    private int folioDetalle;

    /**
     * La clave del producto (opcional).
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
     * True para compra, false para venta.
     */
    private boolean recibimos;

    /**
     * El mnem&oacute;nico de la forma de cobro o pago.
     */
    private String mnemonico;

    /**
     * El monto de la operaci&oacute;n en divisa.
     */
    private double monto;

    /**
     * El tipo de cambio del pizarr&oacute;n.
     */
    private double tipoCambioMesa;

    /**
     * El tipo de cambio pactado con el cliente.
     */
    private double tipoCambio;

    /**
     * El importe de la operaci&oacute;n.
     */
    private double importe;

    /**
     * El n&uacute;mero de precio de referencia para este detalle.
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
     * El valor del factor divisa para este detalle.
     */
    private Double factorDivisa;

    /**
     * El identificador del factor divisa para este detalle.
     * 
     * @deprecated Se debe utlizar le valor directo del Factor Divisa.
     */
    private Integer idFactorDivisa;
    
    /**
     * El n&uacute;mero de spread para este detalle.
     */
    private int idSpread;

    /**
     * El status del detalle de deal.
     */
    private String statusDetalleDeal;

    /**
     * La descripci&oacute;n legible del status del deal.
     */
    private String descripcionStatus;

    /**
     * La clave de divisa relacionada con este detalle de deal.
     */
    private String idDivisa;

    /**
     * 0 No reverso, 1 En proceso de reverso, 2 Reversado.
     */
    private int reversado;

    /**
     * F)recuente, N)o frecuente, M)etal amonedado.
     */    
    private String tipoDivisa;

    /**
     * Para uso del m&oacute;dulo de reversos, indica si fue modificado el monto original del
     * detalle de deal.
     */
    private boolean montoModificado;

    /**
     * Para uso del m&oacute;dulo de reversos, indica si fue modificado el tipo de cambio del 
     * detalle de deal.
     */
    private boolean tipoCambioModificado;

    /**
     * El identificador de la plantilla (opcional).
     */
    private Integer idPlantilla;

    /**
     * Una lista de HashMaps con la informaci&oacute;n de la plantilla asignada a este deal.
     */
    private List datosAdicionales = new ArrayList();

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -4552507630970252699L;
}
