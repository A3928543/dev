/*
 * $Id: BitacoraEnviadasVO.java,v 1.12 2010/04/13 20:20:50 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.posicion.vo;

import java.io.Serializable;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.vo.BrokerVO;

/**
 * Value Object de BitacorasEnviadas para la comunicaci&oacute;n del SICA con las applicaciones en
 * Flex (Monitor de la Posici&oacute;n).
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2010/04/13 20:20:50 $
 */
public class BitacoraEnviadasVO implements Serializable {

	/**
	 * Constructor por Default.
	 *
	 */
    public BitacoraEnviadasVO() {
        super();
    }

    /**
     * Constructor que inicializa los valores para el VO.
     * 
     * @param folioTas El folio para TAS de la Bitacora.
     * @param compra Define si la operaci&oacute;n es compra.
     * @param idDivisa El id de la divisa.
     * @param monto El monto del deal.
     * @param contraparte La contraparte del Broker.
     */
    public BitacoraEnviadasVO(String idConf, int folioTas, boolean compra, String idDivisa,
                              double monto, Double montoDivisa, Double tipoCambio,
                              String comentarios, BrokerVO contraparte, Integer plazo,
                              String noCuenta, boolean estrategia, String clasifOperacion) {
        this();
        this.idConf = idConf;
        this.folioTas = folioTas;
        this.estrategia = estrategia;
        setCompra(compra);
        this.monto = monto;
        this.montoDivisa = montoDivisa;
        this.tipoCambio = tipoCambio;
        this.comentarios = comentarios;
        this.clasifOperacion = clasifOperacion;
        setContraparte(contraparte);
        this.idDivisa = idDivisa;
        this.noCuenta = noCuenta;
        if (plazo == null || plazo.intValue() < 0 || plazo.intValue() > 3) {
            throw new SicaException("No est\u00e1 bien definida la fecha valor.");
        }
        if (plazo.intValue() == 0) {
            fechaValor = Constantes.CASH;
        }
        else if (plazo.intValue() == 1) {
            fechaValor = Constantes.TOM;
        }
        else if (plazo.intValue() == 2) {
            fechaValor = Constantes.SPOT;
        }
        else if (plazo.intValue() == 3) {
            fechaValor = Constantes.HR72;
        }
    }

    /**
     * Regresa el valor de idConf.
     *
     * @return String.
     */
    public String getIdConf() {
        return idConf;
    }

    /**
     * Establece el valor de idConf.
     *
     * @param idConf El valor a asignar.
     */
    public void setIdConf(String idConf) {
        this.idConf = idConf;
    }
    
    /**
     * Regresa el valor de folioTas.
     * 
     * @return int
     */
    public int getFolioTas() {
        return folioTas;
    }

    /**
     * Asigna el valor para folioTas
     * 
     * @param folioTas El valor para folioTas
     */
    public void setFolioTas(int folioTas) {
        this.folioTas = folioTas;
    }
    
    /**
     * Regresa true si la operaci&oacute;n es una compra.
     * 
     * @return boolean
     */
    public boolean isCompra() {
        return compra;
    }

    /**
     * Asigna el valor para compra.
     * 
     * @param compra El valor para compra
     */
    public void setCompra(boolean compra) {
        this.compra = compra;
        this.tipoOperacion = compra ? "Compra" : "Venta";
    }

    /**
     * Regresa el valor de fechaValor.
     *
     * @return String.
     */
    public String getFechaValor() {
        return fechaValor;
    }

    /**
     * Establece el valor de fechaValor.
     *
     * @param fechaValor El valor a asignar.
     */
    public void setFechaValor(String fechaValor) {
        this.fechaValor = fechaValor;
    }
    
    /**
     * Regresa el valor de monto.
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
     * Regresa el valor de idDivisa.
     * 
     * @return String 
     */
    public String getIdDivisa() {
        return idDivisa;
    }
    
    /**
     * Asigna el valor para idDivisa.
     * 
     * @param idDivisa El valor para idDivisa.
     */
    public void setIdDivisa(String idDivisa) {
        this.idDivisa = idDivisa;
    }

    /**
     * Regresa el valor de estrategia.
     *
     * @return boolean.
     */
    public boolean isEstrategia() {
        return estrategia;
    }

    /**
     * Establece el valor de estrategia.
     *
     * @param estrategia El valor a asignar.
     */
    public void setEstrategia(boolean estrategia) {
        this.estrategia = estrategia;
    }

    /**
     * Regresa el valor de noCuenta.
     *
     * @return String.
     */
    public String getNoCuenta() {
        return noCuenta;
    }

    /**
     * Establece el valor de noCuenta.
     *
     * @param noCuenta El valor a asignar.
     */
    public void setNoCuenta(String noCuenta) {
        this.noCuenta = noCuenta;
    }

    /**
     * Regresa el valor de clasifOperacion.
     *
     * @return String.
     */
    public String getClasifOperacion() {
        return clasifOperacion;
    }

    /**
     * Establece el valor de clasifOperacion.
     *
     * @param clasifOperacion El valor a asignar.
     */
    public void setClasifOperacion(String clasifOperacion) {
        this.clasifOperacion = clasifOperacion;
    }

    /**
     * Regresa el valor de contraparte.
     * 
     * @return BrokerVO
     */
    public BrokerVO getContraparte() {
        return contraparte;
    }

    /**
     * Asigna el valor para contraparte.
     * 
     * @param contraparte El valor para contraparte.
     */
    public void setContraparte(BrokerVO contraparte) {
        this.contraparte = contraparte;
        setIdPersonaContraparte(contraparte != null ? contraparte.getIdPersona() : 0);
        setNombreContraparte(contraparte != null ? contraparte.getNombreCompleto() : "");
    }

    /**
     * Regresa el valor de idPersonaContraparte.
     *
     * @return int.
     */
    public int getIdPersonaContraparte() {
        return idPersonaContraparte;
    }

    /**
     * Establece el valor de idPersonaContraparte.
     *
     * @param idPersonaContraparte El valor a asignar.
     */
    public void setIdPersonaContraparte(int idPersonaContraparte) {
        this.idPersonaContraparte = idPersonaContraparte;
    }

    /**
     * Regresa el valor de nombreContraparte.
     *
     * @return String.
     */
    public String getNombreContraparte() {
        return nombreContraparte;
    }

    /**
     * Establece el valor de nombreContraparte.
     *
     * @param nombreContraparte El valor a asignar.
     */
    public void setNombreContraparte(String nombreContraparte) {
        this.nombreContraparte = nombreContraparte;
    }

    /**
     * Regresa el valor de montoDivisa.
     *
     * @return Double.
     */
    public Double getMontoDivisa() {
        return montoDivisa;
    }

    /**
     * Establece el valor de montoDivisa.
     *
     * @param montoDivisa El valor a asignar.
     */
    public void setMontoDivisa(Double montoDivisa) {
        this.montoDivisa = montoDivisa;
    }

    /**
     * Regresa el valor de tipoCambio.
     *
     * @return Double.
     */
    public Double getTipoCambio() {
        return tipoCambio;
    }

    /**
     * Establece el valor de tipoCambio.
     *
     * @param tipoCambio El valor a asignar.
     */
    public void setTipoCambio(Double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    /**
     * Regresa el valor de comentarios.
     *
     * @return String.
     */
    public String getComentarios() {
        return comentarios;
    }

    /**
     * Establece el valor de comentarios.
     *
     * @param comentarios El valor a asignar.
     */
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    /**
     * Regresa el valor de tipoOperacion.
     *
     * @return String.
     */
    public String getTipoOperacion() {
        return tipoOperacion;
    }

    /**
     * Establece el valor de tipoOperacion.
     *
     * @param tipoOperacion El valor a asignar.
     */
    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    private String idConf;

    /**
     * El id de la divisa.
     */
    private String idDivisa;
    
    /**
     * Si es estrategia o no.
     */
    private boolean estrategia;
    
    /**
     * El folio para tas.
     */
    private int folioTas;
    
    /**
     * Define si la operaci&oacute;n es compra.
     */
    private boolean compra;
    
    /**
     * La fecha valor de la operaci&oacute;n.
     */
    private String fechaValor;
    
    /**
     * El monto de la opeaci&oacute;n.
     */
    private double monto;
    
    private Double montoDivisa;

    private Double tipoCambio;

    private String comentarios;

    private String tipoOperacion;

    private String noCuenta;

    private String clasifOperacion;
    
    /**
     * El Broker.
     */
    private BrokerVO contraparte;

    /**
     * El n&uacute;mero de persona de la contraparte.
     */
    private int idPersonaContraparte;

    /**
     * El nombre de la contraparte.
     */
    private String nombreContraparte;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -5354174724694099779L;
}
