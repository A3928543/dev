/*
 * $Id: HistoricoDeal.java,v 1.12 2008/02/22 18:25:22 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Clase persistente para la tabla SC_H_DEAL, donde se almacenan el
 * hist&oacute;rico de Deals </p>
 *
 * @hibernate.class table="SC_H_DEAL"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.HistoricoDeal"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:22 $
 */
public class HistoricoDeal implements Serializable {

    
	/**
     * Constructor por default.
     */
    public HistoricoDeal() {
    	super();
    }
    
	/**
     * Constructor que recibe un Deal e inicializa todas sus variables.
     * 
     * @param deal El deal que inicializa el hist&oacute;rico.
     */
    public HistoricoDeal(Deal deal) {
        super();
        setBroker(deal.getBroker() != null ? deal.getBroker().getId().getPersonaMoral().getIdPersona() : null);
    	setCompra(deal.isCompra());
    	setContratoSica(deal.getContratoSica() != null ? deal.getContratoSica().getIdGrupoEmpresarial(): null);
    	setDireccion(deal.getDireccion() != null ? deal.getDireccion().getIdDireccion() : null);
    	setDirFactura(deal.getDirFactura() != null ? deal.getDirFactura().getIdDireccion(): null);
    	setEnviarAlCliente(deal.isEnviarAlCliente());
    	setEventosDeal(deal.getEventosDeal());
    	setIdCanal(deal.getCanalMesa().getCanal().getIdCanal());
    	setIdMesaCambio(deal.getCanalMesa().getMesaCambio().getIdMesaCambio());
    	setFactura(deal.getFactura());
    	setFechaCaptura(deal.getFechaCaptura());
    	setFechaLiquidacion(deal.getFechaLiquidacion());
    	setIdDeal(deal.getIdDeal());
    	setIdLiquidacion(deal.getIdLiquidacion() != null ? deal.getIdLiquidacion():null);
    	setMensajeria(deal.isMensajeria());
    	setNombreFactura(deal.getNombreFactura() != null ? deal.getNombreFactura() : null);
    	setObservaciones(deal.getObservaciones() != null ? deal.getObservaciones(): null);
    	setPagoAnticipado(deal.isPagoAnticipado());
    	setPromotor(deal.getPromotor()!= null ? deal.getPromotor().getIdPersona():null);
    	setRfcFactura(deal.getRfcFactura()!= null ? deal.getRfcFactura(): null);
    	setSwap(deal.getSwap()!= null ? new Integer(deal.getSwap().getIdFolioSwap()): null);
    	setTipoDeal(deal.getTipoDeal());
    	setTipoValor(deal.getTipoValor());
    	setTomaEnFirme(deal.isTomaEnFirme());
    	setUsuario(deal.getUsuario().getIdUsuario());
    }

    /**
     * Regresa el valor de idDeal.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_DEAL"
     * not-null="true"
     * @return int.
     */
    public int getIdDeal() {
        return idDeal;
    }

    /**
     * Fija el valor de idDeal.
     *
     * @param idDeal El valor a asignar.
     */
    public void setIdDeal(int idDeal) {
        this.idDeal = idDeal;
    }
    
    /**
     * Regresa el valor de idMesaCambio
     *
     * @hibernate.property column ="ID_MESA_CAMBIO"
     * not-null="true"
     * @return int.
     */
    public int getIdMesaCambio() {
        return idMesaCambio;
    }

    /**
     * Fija el valor de idMesaCambio.
     *
     * @param idMesaCambio El valor a asignar.
     */
    public void setIdMesaCambio(int idMesaCambio) {
        this.idMesaCambio = idMesaCambio;
    }

    /**
     * Regresa el valor de compra.
     *
     * @hibernate.property column="COMPRA"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return boolean.
     */
    public boolean isCompra() {
        return compra;
    }

    /**
     * Fija el valor de compra.
     *
     * @param compra El valor a asignar.
     */
    public void setCompra(boolean compra) {
        this.compra = compra;
    }

    /**
     * Regresa el valor de enviarAlCliente.
     *
     * @hibernate.property column="ENVIAR_AL_CLIENTE"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return boolean.
     */
    public boolean isEnviarAlCliente() {
        return enviarAlCliente;
    }

    /**
     * Fija el valor de enviarAlCliente.
     *
     * @param enviarAlCliente El valor a asignar.
     */
    public void setEnviarAlCliente(boolean enviarAlCliente) {
        this.enviarAlCliente = enviarAlCliente;
    }

    /**
     * Regresa el valor de eventos.
     *
     * @hibernate.property column="EVENTOS_DEAL"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getEventosDeal() {
        return eventosDeal;
    }

    /**
     * Fija el valor de eventosDeal.
     *
     * @param eventosDeal El valor a asignar.
     */
    public void setEventosDeal(String eventosDeal) {
        this.eventosDeal = eventosDeal;
    }

    /**
     * Regresa el valor de factura.
     *
     * @hibernate.property column="FACTURA"
     * not-null="true"
     * @return String.
     */
    public String getFactura() {
        return factura;
    }

    /**
     * Fija el valor de factura.
     *
     * @param factura El valor a asignar.
     */
    public void setFactura(String factura) {
        this.factura = factura;
    }

    /**
     * Regresa el valor de facturaCaptura.
     *
     * @hibernate.property column="FECHA_CAPTURA"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getFechaCaptura() {
        return fechaCaptura;
    }

    /**
     * Fija el valor de fechaCaptura.
     *
     * @param fechaCaptura El valor a asignar.
     */
    public void setFechaCaptura(Date fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    /**
     * Regresa el valor de fechaLiquidacion.
     *
     * @hibernate.property column="FECHA_LIQUIDACION"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    /**
     * Fija el valor de fechaLiquidacion.
     *
     * @param fechaLiquidacion El valor a asignar.
     */
    public void setFechaLiquidacion(Date fechaLiquidacion) {
        this.fechaLiquidacion = fechaLiquidacion;
    }

    /**
     * Regresa el valor de idLiquidacion.
     *
     * @hibernate.property column="ID_LIQUIDACION"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getIdLiquidacion() {
        return idLiquidacion;
    }

    public void setIdLiquidacion(Integer idLiquidacion) {
        this.idLiquidacion = idLiquidacion;
    }

    /**
     * Regresa el valor de mensajeria.
     *
     * @hibernate.property column="MENSAJERIA"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return boolean.
     */

    public boolean isMensajeria() {
        return mensajeria;
    }

    /**
     * Fija el valor de mensajeria.
     *
     * @param mensajeria El valor a asignar.
     */
    public void setMensajeria(boolean mensajeria) {
        this.mensajeria = mensajeria;
    }

    /**
     * Regresa el valor de fechaValor.
     *
     * @hibernate.property column="TIPO_VALOR"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getTipoValor() {
        return tipoValor;
    }

    /**
     * Fija el valor de fechaValor.
     *
     * @param tipoValor El valor a asignar.
     */
    public void setTipoValor(String tipoValor) {
        this.tipoValor = tipoValor;
    }

    /**
     * Regresa el valor de nombreFactura.
     *
     * @hibernate.property column="NOMBRE_FACTURA"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getNombreFactura() {
        return nombreFactura;
    }

    /**
     * Fija el valor de nombreFactura.
     *
     * @param nombreFactura El valor a asignar.
     */
    public void setNombreFactura(String nombreFactura) {
        this.nombreFactura = nombreFactura;
    }

    /**
     * Regresa el valor de rfcFactura.
     *
     * @hibernate.property column="RFC_FACTURA"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getRfcFactura() {
        return rfcFactura;
    }

    /**
     * Fija el valor de rfcFactura.
     *
     * @param rfcFactura El valor a asignar.
     */
    public void setRfcFactura(String rfcFactura) {
        if (rfcFactura != null) {
            rfcFactura = rfcFactura.toUpperCase();
        }
        this.rfcFactura = rfcFactura;
    }

    /**
     * Regresa el valor de idDeal.
     *
     * @hibernate.property column ="ID_DIR_FACTURA"
     * not-null="false"
     * unique="false"
     * @return Integer
     */
    public Integer getDirFactura() {
        return dirFactura;
    }

    /**
     * Fija el valor de dirFactura.
     *
     * @param dirFactura El valor a asignar.
     */
    public void setDirFactura(Integer dirFactura) {
        this.dirFactura = dirFactura;
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
     * Regresa el valor de pagoAnticipado.
     *
     * @hibernate.property column="PAGO_ANTICIPADO"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return boolean.
     */
    public boolean isPagoAnticipado() {
        return pagoAnticipado;
    }
    
    /**
     * Fija el valor de pagoAnticipado. Limpia la solicitud de
     * autorizaci&oacute;n de excedente en l&iacute;nea de cr&eacute;dito.
     *
     * @param pagoAnticipado El valor a asignar.
     */
    public void setPagoAnticipado(boolean pagoAnticipado) {
        this.pagoAnticipado = pagoAnticipado;
    }
    
    /**
     * Regresa el valor de tipoDeal.
     *
     * @hibernate.property column="SIMPLE"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getTipoDeal() {
        return tipoDeal;
    }

    /**
     * Fija el valor de tipoDeal.
     *
     * @param tipoDeal El valor a asignar.
     */
    public void setTipoDeal(String tipoDeal) {
        this.tipoDeal = tipoDeal;
    }

    /**
     * Regresa el valor de statusDeal.
     *
     * @hibernate.property column="STATUS_DEAL"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getStatusDeal() {
        return statusDeal;
    }

    /**
     * Fija el valor de statusDeal.
     *
     * @param statusDeal El valor a asignar.
     */
    public void setStatusDeal(String statusDeal) {
        this.statusDeal = statusDeal;
    }

    /**
     * Regresa el valor de tomaEnFirme.
     *
     * @hibernate.property column="TOMA_EN_FIRME"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return boolean.
     */
    public boolean isTomaEnFirme() {
        return tomaEnFirme;
    }

    /**
     * Fija el valor de tomaEnFirme.
     *
     * @param tomaEnFirme El valor a asignar.
     */
    public void setTomaEnFirme(boolean tomaEnFirme) {
        this.tomaEnFirme = tomaEnFirme;
    }

    /**
     * Regresa el valor de idBroker
     * 
     * @hibernate.property column ="ID_BROKER"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    
    public Integer getBroker() {
        return _broker;
    }

    /**
     * Fija el valor de Broker.
     *
     * @param broker El valor a asignar
     */
    public void setBroker(Integer broker) {
        _broker = broker;
    }

    /**
     * Regresa el valor de No Cuenta.
     *
     * @hibernate.property column ="NO_CUENTA"
     * not-null="false"
     * unique="false"
     * @return Integer
     */
    public Integer getContratoSica() {
        return _contratoSica;
    }

    /**
     * Fija el valor de contratoSica.
     *
     * @param contratoSica El valor a asignar.
     */
    public void setContratoSica(Integer contratoSica) {
        _contratoSica = contratoSica;
    }

    /**
     * Regresa el valor de idDireccionMensajeria
     * @hibernate.property column ="ID_DIRECCION_MENSAJERIA"
     * not-null="false"
     * unique="false"
     * @return int.
     */
    public Integer getDireccion() {
        return _direccion;
    }

    /**
     * Fija el valor de direccion.
     *
     * @param direccion El valor a asignar.
     */
    public void setDireccion(Integer direccion) {
        _direccion = direccion;
    }

    /**
     * Regresa el valor de idDeal.
     *
     * @hibernate.property column ="ID_PROMOTOR"
     * not-null="false"
     * unique="false"
     * @return int.
     */
    public Integer getPromotor() {
        return _promotor;
    }

    /**
     * Fija el valor de promotor.
     *
     * @param promotor El valor a asignar.
     */
    public void setPromotor(Integer promotor) {
        _promotor = promotor;
    }

    /**
     *
     * @hibernate.property column ="ID_USUARIO"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getUsuario() {
        return _usuario;
    }

    /**
     * Fija el valor de usuario.
     *
     * @param usuario El valor a asignar.
     */
    public void setUsuario(int usuario) {
        _usuario = usuario;
    }

    /**
     * Regresa el valor de idSwap.
     * @hibernate.property column ="ID_FOLIO_SWAP"
     * not-null="false"
     * unique="false"
     * @return Integer
     */
    
    public Integer getSwap() {
        return _swap;
    }

    /**
     * Fija el valor de swap.
     *
     * @param swap El valor a asignar.
     */
    public void setSwap(Integer swap) {
        _swap = swap;
    }

    /**
     * Regresa el valor de idCanal.
     *
     * @hibernate.property column ="ID_CANAL"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    
    public String getIdCanal() {
        return _idCanal;
    }

    /**
     * Fija el valor de idCanal.
     *
     * @param idCanal El valor a asignar.
     */
    public void setIdCanal(String idCanal) {
        _idCanal = idCanal;
    }

    
    /**
     * El identificador del registro.
     */
    protected int idDeal;
    
    /**
     * El identificador del registro.
     */
    protected int idMesaCambio;
    
    /**
     * El identificador del registro.
     */
    protected String _idCanal;

    /**
     * Si el pivote del deal es compra o venta.
     */
    private boolean compra;

    /**
     * Si se env&iacute;a o no al cliente el deal por email.
     */
    private boolean enviarAlCliente;

    /**
     * La m&aacute;scara de eventos del deal.
     */
    private String eventosDeal = "           0";

    /**
     * Si se debe facturar o no.
     */
    private String factura = Deal.SIN_FACTURA;

    /**
     * La fecha en que se captura el deal.
     */
    private Date fechaCaptura = new Date();

    /**
     * La fecha en que se liquid&oacute; el deal.
     */
    private Date fechaLiquidacion;

    /**
     * El n&uacute;mero de liquidaci&oacute;n de Tesorer&iacute;a.
     */
    private Integer idLiquidacion;

    /**
     * Si el cliente requiere o no servicio de mensajeria.
     */
    private boolean mensajeria;

    /**
     * Si el cliente requiere o no pago anticipado en este detalle.
     */
    private boolean pagoAnticipado;

    /**
     * C)ash | T)om | S)pot | + 48 hrs.
     */
    private String tipoValor;

    /**
     * Las observaciones.
     */
    private String observaciones = "";

    /**
     * Si se trata de un deal simple 'S' o complejo 'C' o interbancario 'i'.
     */
    private String tipoDeal;

    /**
     * El status del deal.
     */
    private String statusDeal = Deal.STATUS_DEAL_PROCESO_CAPTURA;

    /**
     * Si se toma en firme o no.
     */
    private boolean tomaEnFirme;

    /**
     * Relaci&oacute;n muchos-a-uno con Broker.
     */
    private Integer _broker;

    /**
     * Relaci&oacute;n muchos-a-uno con ContratoSica.
     */
    private Integer _contratoSica;

    /**
     * Relaci&oacute;n muchos-a-uno con Direccion.
     */
    private Integer _direccion;

    /**
     * Relaci&oacute; muchos-a-uno con EmpleadoIxe.
     */
    private Integer _promotor;

    /**
     * Relaci&oacute;n muchos-a-uno con Usuario.
     */
    private int _usuario;

    /**
     * El Nombre del Cliente para Facturaci&oacute;n.
     */
    private String nombreFactura;

    /**
     * El RFC del Cliente para Facturaci&oacute;n.
     */
    private String rfcFactura;

    /**
     * Relaci&oacute;n muchos-a-uno con la Direcci&oacute;n Fiscal del Cliente para Facturaci&oacute;n.
     */
    private Integer dirFactura;

    /**
     * Relaci&oacute;n muchos-a-uno con la tabla Swap.
     */
    private Integer _swap;
}
