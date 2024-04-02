/*
 * $Id: ReporteDealBean.java,v 1.11 2008/02/22 18:25:03 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;


/**
 * Bean para generar el reporte de Conciliacion Diaria.
 *
 * @author Gustavo Gonzalez
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:03 $
 */
public class ReporteDealBean implements Serializable {

    /**
     * Constructor de la clase ReporteDealBean, que se utiliza
     * para almacenar los datos para la prueba del reporte del Deal
     *
     * @param idDivisa           El id de la divisa
     * @param isRecibimos        Define si recibimos o entregamos.
     * @param idDeal             El  id del deal.
     * @param factura            Define si requiere factura.
     * @param fechaCaptura       La fecha de captura del deal.
     * @param fechaAplicacion    La fecha de aplicaci&oacute;n del deal.
     * @param tipoValor          El tipo valor del deal.
     * @param idUsuario          El id del usuario.
     * @param observaciones      Las observaciones del deal.
     * @param totalRecibimos     El monto total de recibimos.
     * @param folioDetalle       El folio del detalle.
     * @param descripcion        La descripcion de la divisa.
     * @param monto              El monto del deal.
     * @param tipoCambio         El tipo de cambio del deal.
     * @param importe            El importe del deal.
     * @param balance            El balance del deal.
     * @param totalEntregamos    El total de entregamos.
     * @param promotor           El nombre del promotor.
     * @param cliente            El nombre del cliente.
     * @param idPersona          El idPersona del cliente.
     * @param pagoAnticipado     Define si se requiere pago anticipado.
     * @param rfc                El rfc del cliente.
     * @param direccion          La direcci&oacute;n del cliente.
     * @param facturaAnticipada  Define si requiere factura anticipada.
     * @param tomaEnFirme        Define si requiere toma en firme.
     * @param image              Imagen de encabezado del reporte.
     * @param compra             Define si es compra o venta.
     * @param obsHr              Las observaciones por horario.
     * @param obsMonto           Las observaciones por monto.
     * @param obsDoc             Las obervaciones por documentaci&oacute;n.
     * @param obsMod             Las observaciones por modificaci&oacute;n.
     * @param obsCancel          Las observaciones por cancelaci&oacute;n.
     * @param obsFact            Las observaciones de facturaci7oacute;n.
     * @param obsRiesgo          Las observaciones por riesgo.
     * @param obsDocDet          Las observaciones por documentaci&oacute;n de detalle de deal.
     * @param obsLinCredDet      Las observaciones por l&iacute;nea de cr&eacute;dito.
     * @param leyendaDeal        La leyenda del deal.
     * @param descMnemonico      La descripci&oacute;n del mnemonico.
     * @param infoAdicional      La informaci&oacute;n adicional del deal.
     * @param subTotalRecibimos  El subtotal de recibimos.
     * @param subTotalEntregamos El subtotal de entregamos.
     * @param comisionMxn        El monto de la comisi&oacute;n.
     * @param mensajeria         Define si se requiere mensajer&iacute;a.
     * @param iva                El monto del IVA.
     * @param statusDetalleDeal  El estatus del detalle del deal.
     */
    public ReporteDealBean(String idDivisa, Boolean isRecibimos, Integer idDeal,
                           InputStream factura, Date fechaCaptura, Date fechaAplicacion,
                           String tipoValor, Integer idUsuario, String observaciones,
                           Double totalRecibimos, Integer folioDetalle,
                           String descripcion, Double monto, Double tipoCambio, Double importe,
                           Double balance, Double totalEntregamos, String promotor, String cliente,
                           Integer idPersona, InputStream pagoAnticipado, String rfc,
                           String direccion, InputStream facturaAnticipada,
                           InputStream tomaEnFirme, InputStream image, Boolean compra, String obsHr,
                           String obsMonto, String obsDoc, String obsMod, String obsCancel,
                           String obsFact, String obsRiesgo, String obsDocDet, String obsLinCredDet,
                           String leyendaDeal, String descMnemonico, String infoAdicional,
                           String subTotalRecibimos, String subTotalEntregamos,
                           Double comisionMxn, String mensajeria, Double iva,
                           String statusDetalleDeal, String tipCambio, String acudirCon,
                           String numeroReferencia) {
        super();
        this.tipCambio = tipCambio;
        this.idDivisa = idDivisa;
        this.isRecibimos = isRecibimos;
        this.idDeal = idDeal;
        this.factura = factura;
        this.fechaCaptura = fechaCaptura;
        this.fechaAplicacion = fechaAplicacion;
        this.tipoValor = tipoValor;
        this.idUsuario = idUsuario;
        this.observaciones = observaciones;
        this.totalRecibimos = totalRecibimos;
        this.folioDetalle = folioDetalle;
        this.descripcion = descripcion;
        this.monto = monto;
        this.tipoCambio = tipoCambio;
        this.importe = importe;
        this.totalEntregamos = totalEntregamos;
        this.promotor = promotor;
        this.cliente = cliente;
        this.idPersona = idPersona;
        this.pagoAnticipado = pagoAnticipado;
        this.rfc = rfc;
        this.direccion = direccion;
        this.facturaAnticipada = facturaAnticipada;
        this.tomaEnFirme = tomaEnFirme;
        this.image = image;
        this.compra = compra;
        this.obsHr = obsHr;
        this.obsMonto = obsMonto;
        this.obsDoc = obsDoc;
        this.obsMod = obsMod;
        this.obsCancel = obsCancel;
        this.obsFact = obsFact;
        this.obsRiesgo = obsRiesgo;
        this.obsDocDet = obsDocDet;
        this.obsLinCredDet = obsLinCredDet;
        this.leyendaDeal = leyendaDeal;
        this.descMnemonico = descMnemonico;
        this.infoAdicional = infoAdicional;
        this.subTotalRecibimos = subTotalRecibimos;
        this.subTotalEntregamos = subTotalEntregamos;
        this.comisionMxn = comisionMxn;
        this.mensajeria = mensajeria;
        this.iva = iva;
        this.statusDetalleDeal = statusDetalleDeal;
        this.acudirCon = acudirCon;
        this.numeroReferencia = numeroReferencia;
    }

    /**
     * Constructor de la clase ReporteDealBean, que se utiliza
     * para almacenar los datos para la prueba del reporte del Deal
     *
     * @param idDivisa           El id de la divisa
     * @param isRecibimos        Define si recibimos o entregamos.
     * @param idDeal             El  id del deal.
     * @param factura            Define si requiere factura.
     * @param fechaCaptura       La fecha de captura del deal.
     * @param fechaAplicacion    La fecha de aplicaci&oacute;n del deal.
     * @param tipoValor          El tipo valor del deal.
     * @param idUsuario          El id del usuario.
     * @param observaciones      Las observaciones del deal.
     * @param totalRecibimos     El monto total de recibimos.
     * @param folioDetalle       El folio del detalle.
     * @param descripcion        La descripcion de la divisa.
     * @param monto              El monto del deal.
     * @param tipoCambio         El tipo de cambio del deal.
     * @param importe            El importe del deal.
     * @param balance            El balance del deal.
     * @param totalEntregamos    El total de entregamos.
     * @param promotor           El nombre del promotor.
     * @param cliente            El nombre del cliente.
     * @param idPersona          El idPersona del cliente.
     * @param pagoAnticipado     Define si se requiere pago anticipado.
     * @param rfc                El rfc del cliente.
     * @param direccion          La direcci&oacute;n del cliente.
     * @param facturaAnticipada  Define si requiere factura anticipada.
     * @param tomaEnFirme        Define si requiere toma en firme.
     * @param image              Imagen de encabezado del reporte.
     * @param compra             Define si es compra o venta.
     * @param obsHr              Las observaciones por horario.
     * @param obsMonto           Las observaciones por monto.
     * @param obsDoc             Las obervaciones por documentaci&oacute;n.
     * @param obsMod             Las observaciones por modificaci&oacute;n.
     * @param obsCancel          Las observaciones por cancelaci&oacute;n.
     * @param obsFact            Las observaciones de facturaci7oacute;n.
     * @param obsRiesgo          Las observaciones por riesgo.
     * @param obsDocDet          Las observaciones por documentaci&oacute;n de detalle de deal.
     * @param obsLinCredDet      Las observaciones por l&iacute;nea de cr&eacute;dito.
     * @param leyendaDeal        La leyenda del deal.
     * @param descMnemonico      La descripci&oacute;n del mnemonico.
     * @param infoAdicional      La informaci&oacute;n adicional del deal.
     * @param subTotalRecibimos  El subtotal de recibimos.
     * @param subTotalEntregamos El subtotal de entregamos.
     * @param comisionMxn        El monto de la comisi&oacute;n.
     * @param mensajeria         Define si se requiere mensajer&iacute;a.
     * @param iva                El monto del IVA.
     * @param statusDetalleDeal  El estatus del detalle del deal.
     */
    public ReporteDealBean(String idDivisa, Boolean isRecibimos, Integer idDeal,
                           InputStream factura, Date fechaCaptura, Date fechaAplicacion,
                           String tipoValor, Integer idUsuario, String observaciones,
                           Double totalRecibimos, Integer folioDetalle, String descripcion,
                           Double monto, Double tipoCambio, Double importe, Double balance,
                           Double totalEntregamos, String promotor, String cliente,
                           Integer idPersona, InputStream pagoAnticipado, String rfc,
                           String direccion, InputStream facturaAnticipada, InputStream tomaEnFirme,
                           InputStream image, Boolean compra, String obsHr, String obsMonto,
                           String obsDoc, String obsMod, String obsCancel, String obsFact,
                           String obsRiesgo, String obsDocDet, String obsLinCredDet,
                           String leyendaDeal, String descMnemonico, String infoAdicional,
                           String infoAdicionalValor, String subTotalRecibimos,
                           String subTotalEntregamos, Double comisionMxn, String mensajeria,
                           Double iva, String statusDetalleDeal, String acudirCon,
                           String numeroReferencia) {
        super();
        this.idDivisa = idDivisa;
        this.isRecibimos = isRecibimos;
        this.idDeal = idDeal;
        this.factura = factura;
        this.fechaCaptura = fechaCaptura;
        this.fechaAplicacion = fechaAplicacion;
        this.tipoValor = tipoValor;
        this.idUsuario = idUsuario;
        this.observaciones = observaciones;
        this.totalRecibimos = totalRecibimos;
        this.folioDetalle = folioDetalle;
        this.descripcion = descripcion;
        this.monto = monto;
        this.tipoCambio = tipoCambio;
        this.importe = importe;
        this.totalEntregamos = totalEntregamos;
        this.promotor = promotor;
        this.cliente = cliente;
        this.idPersona = idPersona;
        this.pagoAnticipado = pagoAnticipado;
        this.rfc = rfc;
        this.direccion = direccion;
        this.facturaAnticipada = facturaAnticipada;
        this.tomaEnFirme = tomaEnFirme;
        this.image = image;
        this.compra = compra;
        this.obsHr = obsHr;
        this.obsMonto = obsMonto;
        this.obsDoc = obsDoc;
        this.obsMod = obsMod;
        this.obsCancel = obsCancel;
        this.obsFact = obsFact;
        this.obsRiesgo = obsRiesgo;
        this.obsDocDet = obsDocDet;
        this.obsLinCredDet = obsLinCredDet;
        this.leyendaDeal = leyendaDeal;
        this.descMnemonico = descMnemonico;
        this.infoAdicional = infoAdicional;
        this.infoAdicionalValor = infoAdicionalValor;
        this.subTotalRecibimos = subTotalRecibimos;
        this.subTotalEntregamos = subTotalEntregamos;
        this.comisionMxn = comisionMxn;
        this.mensajeria = mensajeria;
        this.iva = iva;
        this.statusDetalleDeal = statusDetalleDeal;
        this.acudirCon = acudirCon;
        this.numeroReferencia = numeroReferencia;
    }

    /**
     * Constructor de la clase ReporteDealBean, que se utiliza
     * para almacenar los datos para la prueba del reporte del Deal
     *
     * @param idDivisa
     * @param isRecibimos
     * @param idDeal
     * @param factura
     * @param fechaCaptura
     * @param fechaAplicacion
     * @param tipoValor
     * @param idUsuario
     * @param observaciones
     * @param totalRecibimos
     * @param folioDetalle
     * @param descripcion
     * @param monto
     * @param tipoCambio
     * @param importe
     * @param balance
     * @param totalEntregamos
     * @param promotor
     * @param cliente
     * @param idPersona
     * @param pagoAnticipado
     * @param rfc
     * @param direccion
     * @param facturaAnticipada
     * @param tomaEnFirme
     * @param image
     * @param compra
     * @param obsHr
     * @param obsMonto
     * @param obsDoc
     * @param obsMod
     * @param obsCancel
     * @param obsFact
     * @param obsRiesgo
     * @param obsDocDet
     * @param obsLinCredDet
     * @param leyendaDeal
     * @param descMnemonico
     * @param infoAdicional
     * @param subTotalRecibimos
     * @param subTotalEntregamos
     * @param comisionMxn
     * @param mensajeria
     * @param iva
     * @param statusDetalleDeal
     */
    public ReporteDealBean(String idDivisa, Boolean isRecibimos, Integer idDeal, String factura,
                           Date fechaCaptura, Date fechaAplicacion, String tipoValor, Integer idUsuario,
                           String observaciones, Double totalRecibimos, Integer folioDetalle, String descripcion,
                           Double monto, Double tipoCambio, Double importe, Double balance, Double totalEntregamos,
                           String promotor, String cliente, Integer idPersona, String pagoAnticipado, String rfc,
                           String direccion, String facturaAnticipada, String tomaEnFirme, InputStream image,
                           Boolean compra, String obsHr, String obsMonto, String obsDoc, String obsMod,
                           String obsCancel, String obsFact, String obsRiesgo, String obsDocDet, String obsLinCredDet,
                           String leyendaDeal, String descMnemonico, String infoAdicional, String subTotalRecibimos,
                           String subTotalEntregamos, Double comisionMxn, String mensajeria, Double iva,
                           String statusDetalleDeal, String tipCambio, String acudirCon) {
        super();
        this.idDivisa = idDivisa;
        this.isRecibimos = isRecibimos;
        this.idDeal = idDeal;
        this.sFactura = factura;
        this.fechaCaptura = fechaCaptura;
        this.fechaAplicacion = fechaAplicacion;
        this.tipoValor = tipoValor;
        this.idUsuario = idUsuario;
        this.observaciones = observaciones;
        this.totalRecibimos = totalRecibimos;
        this.folioDetalle = folioDetalle;
        this.descripcion = descripcion;
        this.monto = monto;
        this.tipoCambio = tipoCambio;
        this.importe = importe;
        this.totalEntregamos = totalEntregamos;
        this.promotor = promotor;
        this.cliente = cliente;
        this.idPersona = idPersona;
        this.sPagoAnticipado = pagoAnticipado;
        this.rfc = rfc;
        this.direccion = direccion;
        this.sFacturaAnticipada = facturaAnticipada;
        this.sTomaEnFirme = tomaEnFirme;
        this.image = image;
        this.compra = compra;
        this.obsHr = obsHr;
        this.obsMonto = obsMonto;
        this.obsDoc = obsDoc;
        this.obsMod = obsMod;
        this.obsCancel = obsCancel;
        this.obsFact = obsFact;
        this.obsRiesgo = obsRiesgo;
        this.obsDocDet = obsDocDet;
        this.obsLinCredDet = obsLinCredDet;
        this.leyendaDeal = leyendaDeal;
        this.descMnemonico = descMnemonico;
        this.infoAdicional = infoAdicional;
        this.infoAdicionalValor = infoAdicionalValor;
        this.subTotalRecibimos = subTotalRecibimos;
        this.subTotalEntregamos = subTotalEntregamos;
        this.comisionMxn = comisionMxn;
        this.mensajeria = mensajeria;
        this.iva = iva;
        this.statusDetalleDeal = statusDetalleDeal;
        this.acudirCon = acudirCon;
    }

    /**
     * Constructor de la clase ReporteDealBean, que se utiliza
     * para almacenar los datos para la prueba del reporte del Deal
     *
     * @param idDivisa
     * @param isRecibimos
     * @param idDeal
     * @param idDivisa           El id de la divisa
     * @param isRecibimos        Define si recibimos o entregamos.
     * @param idDeal             El  id del deal.
     * @param factura            Define si requiere factura.
     * @param fechaCaptura       La fecha de captura del deal.
     * @param fechaAplicacion    La fecha de aplicaci&oacute;n del deal.
     * @param tipoValor          El tipo valor del deal.
     * @param idUsuario          El id del usuario.
     * @param observaciones      Las observaciones del deal.
     * @param totalRecibimos     El monto total de recibimos.
     * @param folioDetalle       El folio del detalle.
     * @param descripcion        La descripcion de la divisa.
     * @param monto              El monto del deal.
     * @param tipoCambio         El tipo de cambio del deal.
     * @param importe            El importe del deal.
     * @param balance            El balance del deal.
     * @param totalEntregamos    El total de entregamos.
     * @param promotor           El nombre del promotor.
     * @param cliente            El nombre del cliente.
     * @param idPersona          El idPersona del cliente.
     * @param pagoAnticipado     Define si se requiere pago anticipado.
     * @param rfc                El rfc del cliente.
     * @param direccion          La direcci&oacute;n del cliente.
     * @param facturaAnticipada  Define si requiere factura anticipada.
     * @param tomaEnFirme        Define si requiere toma en firme.
     * @param image              Imagen de encabezado del reporte.
     * @param compra             Define si es compra o venta.
     * @param obsHr              Las observaciones por horario.
     * @param obsMonto           Las observaciones por monto.
     * @param obsDoc             Las obervaciones por documentaci&oacute;n.
     * @param obsMod             Las observaciones por modificaci&oacute;n.
     * @param obsCancel          Las observaciones por cancelaci&oacute;n.
     * @param obsFact            Las observaciones de facturaci7oacute;n.
     * @param obsRiesgo          Las observaciones por riesgo.
     * @param obsDocDet          Las observaciones por documentaci&oacute;n de detalle de deal.
     * @param obsLinCredDet      Las observaciones por l&iacute;nea de cr&eacute;dito.
     * @param leyendaDeal        La leyenda del deal.
     * @param descMnemonico      La descripci&oacute;n del mnemonico.
     * @param infoAdicional      La informaci&oacute;n adicional del deal.
     * @param subTotalRecibimos  El subtotal de recibimos.
     * @param subTotalEntregamos El subtotal de entregamos.
     * @param comisionMxn        El monto de la comisi&oacute;n.
     * @param mensajeria         Define si se requiere mensajer&iacute;a.
     * @param iva                El monto del IVA.
     * @param statusDetalleDeal  El estatus del detalle del deal.
     */
    public ReporteDealBean(String idDivisa, Boolean isRecibimos, Integer idDeal, String factura,
                           Date fechaCaptura, Date fechaAplicacion, String tipoValor, Integer idUsuario,
                           String observaciones, Double totalRecibimos, Integer folioDetalle, String descripcion,
                           Double monto, Double tipoCambio, Double importe, Double balance, Double totalEntregamos,
                           String promotor, String cliente, Integer idPersona, String pagoAnticipado, String rfc,
                           String direccion, String facturaAnticipada, String tomaEnFirme, InputStream image,
                           Boolean compra, String obsHr, String obsMonto, String obsDoc, String obsMod,
                           String obsCancel, String obsFact, String obsRiesgo, String obsDocDet, String obsLinCredDet,
                           String leyendaDeal, String descMnemonico, String infoAdicional, String subTotalRecibimos,
                           String subTotalEntregamos, Double comisionMxn, String mensajeria, Double iva,
                           String statusDetalleDeal, String tipCambio, String subtotalDivisa, String acudirCon,
                           String numeroReferencia) {
        super();
        this.idDivisa = idDivisa;
        this.isRecibimos = isRecibimos;
        this.idDeal = idDeal;
        this.sFactura = factura;
        this.fechaCaptura = fechaCaptura;
        this.fechaAplicacion = fechaAplicacion;
        this.tipoValor = tipoValor;
        this.idUsuario = idUsuario;
        this.observaciones = observaciones;
        this.totalRecibimos = totalRecibimos;
        this.folioDetalle = folioDetalle;
        this.descripcion = descripcion;
        this.monto = monto;
        this.tipoCambio = tipoCambio;
        this.importe = importe;
        this.totalEntregamos = totalEntregamos;
        this.promotor = promotor;
        this.cliente = cliente;
        this.idPersona = idPersona;
        this.sPagoAnticipado = pagoAnticipado;
        this.rfc = rfc;
        this.direccion = direccion;
        this.sFacturaAnticipada = facturaAnticipada;
        this.sTomaEnFirme = tomaEnFirme;
        this.image = image;
        this.compra = compra;
        this.obsHr = obsHr;
        this.obsMonto = obsMonto;
        this.obsDoc = obsDoc;
        this.obsMod = obsMod;
        this.obsCancel = obsCancel;
        this.obsFact = obsFact;
        this.obsRiesgo = obsRiesgo;
        this.obsDocDet = obsDocDet;
        this.obsLinCredDet = obsLinCredDet;
        this.leyendaDeal = leyendaDeal;
        this.descMnemonico = descMnemonico;
        this.infoAdicional = infoAdicional;
        this.infoAdicionalValor = infoAdicionalValor;
        this.subTotalRecibimos = subTotalRecibimos;
        this.subTotalEntregamos = subTotalEntregamos;
        this.comisionMxn = comisionMxn;
        this.mensajeria = mensajeria;
        this.iva = iva;
        this.statusDetalleDeal = statusDetalleDeal;
        this.subtotalDivisa = subtotalDivisa;
        this.acudirCon = acudirCon;
        this.numeroReferencia = numeroReferencia;
    }

    /**
     * Regresa el valor de acudirCon.
     *
     * @return String.
     */
    public String getAcudirCon() {
        return acudirCon;
    }

    /**
     * Asigna el valor para acudirCon.
     *
     * @param acudirCon El valor para acudirCon.
     */
    public void setAcudirCon(String acudirCon) {
        this.acudirCon = acudirCon;
    }

    /**
     * Regresa el valor de cliente.
     *
     * @return String.
     */
    public String getCliente() {
        return cliente;
    }

    /**
     * Asigna el valor para cliente.
     *
     * @param cliente El valor para cliente.
     */
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    /**
     * Regresa el valor de comisionMxn.
     *
     * @return Double.
     */
    public Double getComisionMxn() {
        return comisionMxn;
    }

    /**
     * Asigna el valor para comisionMxn.
     *
     * @param comisionMxn El valor para comisionMxn.
     */
    public void setComisionMxn(Double comisionMxn) {
        this.comisionMxn = comisionMxn;
    }

    /**
     * Regresa el valor de compra.
     *
     * @return Boolean.
     */
    public Boolean getCompra() {
        return compra;
    }

    /**
     * Asigna el valor para compra.
     *
     * @param compra El valor para compra.
     */
    public void setCompra(Boolean compra) {
        this.compra = compra;
    }

    /**
     * Regresa el valor de descMnemonico.
     *
     * @return String.
     */
    public String getDescMnemonico() {
        return descMnemonico;
    }

    /**
     * Asigna el valor para descMnemonico.
     *
     * @param descMnemonico El valor para descMnemonico.
     */
    public void setDescMnemonico(String descMnemonico) {
        this.descMnemonico = descMnemonico;
    }

    /**
     * Regresa el valor de descripcion.
     *
     * @return String.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Asigna el valor para descripcion.
     *
     * @param descripcion El valor para descripcion.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Regresa el valor de direccion.
     *
     * @return String.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Asigna el valor para direccion.
     *
     * @param direccion El valor para direccion.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Regresa el valor de factura.
     *
     * @return InputStream.
     */
    public InputStream getFactura() {
        return factura;
    }

    /**
     * Asigna el valor para factura.
     *
     * @param factura El valor para factura.
     */
    public void setFactura(InputStream factura) {
        this.factura = factura;
    }

    /**
     * Regresa el valor de facturaAnticipada.
     *
     * @return InputStream.
     */
    public InputStream getFacturaAnticipada() {
        return facturaAnticipada;
    }

    /**
     * Asigna el valor para facturaAnticipada.
     *
     * @param facturaAnticipada El valor para facturaAnticipada.
     */
    public void setFacturaAnticipada(InputStream facturaAnticipada) {
        this.facturaAnticipada = facturaAnticipada;
    }

    /**
     * Regresa el valor de fechaAplicacion.
     *
     * @return Date.
     */
    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    /**
     * Asigna el valor para fechaAplicacion.
     *
     * @param fechaAplicacion El valor para fechaAplicacion.
     */
    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    /**
     * Regresa el valor de fechaCaptura.
     *
     * @return Date.
     */
    public Date getFechaCaptura() {
        return fechaCaptura;
    }

    /**
     * Asigna el valor para fechaCaptura.
     *
     * @param fechaCaptura El valor para fechaCaptura.
     */
    public void setFechaCaptura(Date fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    /**
     * Regresa el valor de folioDetalle.
     *
     * @return Integer.
     */
    public Integer getFolioDetalle() {
        return folioDetalle;
    }

    /**
     * Asigna el valor para folioDetalle.
     *
     * @param folioDetalle El valor para folioDetalle.
     */
    public void setFolioDetalle(Integer folioDetalle) {
        this.folioDetalle = folioDetalle;
    }

    /**
     * Regresa el valor de idDeal.
     *
     * @return Integer.
     */
    public Integer getIdDeal() {
        return idDeal;
    }

    /**
     * Asigna el valor para idDeal.
     *
     * @param idDeal El valor para idDeal.
     */
    public void setIdDeal(Integer idDeal) {
        this.idDeal = idDeal;
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
     * Asigna el valor para idDivisa.
     *
     * @param idDivisa El valor para idDivisa.
     */
    public void setIdDivisa(String idDivisa) {
        this.idDivisa = idDivisa;
    }

    /**
     * Regresa el valor de idPersona.
     *
     * @return Integer.
     */
    public Integer getIdPersona() {
        return idPersona;
    }

    /**
     * Asigna el valor para idPersona.
     *
     * @param idPersona El valor para idPersona.
     */
    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }

    /**
     * Regresa el valor de idUsuario.
     *
     * @return Integer.
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }

    /**
     * Asigna el valor para idUsuario.
     *
     * @param idUsuario El valor para idUsuario.
     */
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Regresa el valor de image.
     *
     * @return InputStream.
     */
    public InputStream getImage() {
        return image;
    }

    /**
     * Asigna el valor para image.
     *
     * @param image El valor para image.
     */
    public void setImage(InputStream image) {
        this.image = image;
    }

    /**
     * Regresa el valor de importe.
     *
     * @return Double.
     */
    public Double getImporte() {
        return importe;
    }

    /**
     * Asigna el valor para importe.
     *
     * @param importe El valor para importe.
     */
    public void setImporte(Double importe) {
        this.importe = importe;
    }

    /**
     * Regresa el valor de infoAdicional.
     *
     * @return String.
     */
    public String getInfoAdicional() {
        return infoAdicional;
    }

    /**
     * Asigna el valor para infoAdicional.
     *
     * @param infoAdicional El valor para infoAdicional.
     */
    public void setInfoAdicional(String infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

    /**
     * Regresa el valor de infoAdicionalValor.
     *
     * @return String.
     */
    public String getInfoAdicionalValor() {
        return infoAdicionalValor;
    }

    /**
     * Asigna el valor para infoAdicionalValor.
     *
     * @param infoAdicionalValor El valor para infoAdicionalValor.
     */
    public void setInfoAdicionalValor(String infoAdicionalValor) {
        this.infoAdicionalValor = infoAdicionalValor;
    }

    /**
     * Regresa el valor de isRecibimos.
     *
     * @return Boolean.
     */
    public Boolean getIsRecibimos() {
        return isRecibimos;
    }

    /**
     * Asigna el valor para isRecibimos.
     *
     * @param isRecibimos El valor para isRecibimos.
     */
    public void setIsRecibimos(Boolean isRecibimos) {
        this.isRecibimos = isRecibimos;
    }

    /**
     * Regresa el valor de iva.
     *
     * @return Double.
     */
    public Double getIva() {
        return iva;
    }

    /**
     * Asigna el valor para iva.
     *
     * @param iva El valor para iva.
     */
    public void setIva(Double iva) {
        this.iva = iva;
    }

    /**
     * Regresa el valor de leyendaDeal.
     *
     * @return String.
     */
    public String getLeyendaDeal() {
        return leyendaDeal;
    }

    /**
     * Asigna el valor para leyendaDeal.
     *
     * @param leyendaDeal El valor para leyendaDeal.
     */
    public void setLeyendaDeal(String leyendaDeal) {
        this.leyendaDeal = leyendaDeal;
    }

    /**
     * Regresa el valor de mensajeria.
     *
     * @return String.
     */
    public String getMensajeria() {
        return mensajeria;
    }

    /**
     * Asigna el valor para mensajeria.
     *
     * @param mensajeria El valor para mensajeria.
     */
    public void setMensajeria(String mensajeria) {
        this.mensajeria = mensajeria;
    }

    /**
     * Regresa el valor de monto.
     *
     * @return Double.
     */
    public Double getMonto() {
        return monto;
    }

    /**
     * Asigna el valor para monto.
     *
     * @param monto El valor para monto.
     */
    public void setMonto(Double monto) {
        this.monto = monto;
    }

    /**
     * Regresa el valor de obsCancel.
     *
     * @return String.
     */
    public String getObsCancel() {
        return obsCancel;
    }

    /**
     * Asigna el valor para obsCancel.
     *
     * @param obsCancel El valor para obsCancel.
     */
    public void setObsCancel(String obsCancel) {
        this.obsCancel = obsCancel;
    }

    /**
     * Regresa el valor de obsDoc.
     *
     * @return String.
     */
    public String getObsDoc() {
        return obsDoc;
    }

    /**
     * Asigna el valor para obsDoc.
     *
     * @param obsDoc El valor para obsDoc.
     */
    public void setObsDoc(String obsDoc) {
        this.obsDoc = obsDoc;
    }

    /**
     * Regresa el valor de obsDocDet.
     *
     * @return String.
     */
    public String getObsDocDet() {
        return obsDocDet;
    }

    /**
     * Asigna el valor para obsDocDet.
     *
     * @param obsDocDet El valor para obsDocDet.
     */
    public void setObsDocDet(String obsDocDet) {
        this.obsDocDet = obsDocDet;
    }

    /**
     * Regresa el valor de observaciones.
     *
     * @return String.
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Asigna el valor para observaciones.
     *
     * @param observaciones El valor para observaciones.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Regresa el valor de obsFact.
     *
     * @return String.
     */
    public String getObsFact() {
        return obsFact;
    }

    /**
     * Asigna el valor para obsFact.
     *
     * @param obsFact El valor para obsFact.
     */
    public void setObsFact(String obsFact) {
        this.obsFact = obsFact;
    }

    /**
     * Regresa el valor de obsHr.
     *
     * @return String.
     */
    public String getObsHr() {
        return obsHr;
    }

    /**
     * Asigna el valor para obsHr.
     *
     * @param obsHr El valor para obsHr.
     */
    public void setObsHr(String obsHr) {
        this.obsHr = obsHr;
    }

    /**
     * Regresa el valor de obsLinCredDet.
     *
     * @return String.
     */
    public String getObsLinCredDet() {
        return obsLinCredDet;
    }

    /**
     * Asigna el valor para obsLinCredDet.
     *
     * @param obsLinCredDet El valor para obsLinCredDet.
     */
    public void setObsLinCredDet(String obsLinCredDet) {
        this.obsLinCredDet = obsLinCredDet;
    }

    /**
     * Regresa el valor de obsMod.
     *
     * @return String.
     */
    public String getObsMod() {
        return obsMod;
    }

    /**
     * Asigna el valor para obsMod.
     *
     * @param obsMod El valor para obsMod.
     */
    public void setObsMod(String obsMod) {
        this.obsMod = obsMod;
    }

    /**
     * Regresa el valor de obsMonto.
     *
     * @return String.
     */
    public String getObsMonto() {
        return obsMonto;
    }

    /**
     * Asigna el valor para obsMonto.
     *
     * @param obsMonto El valor para obsMonto.
     */
    public void setObsMonto(String obsMonto) {
        this.obsMonto = obsMonto;
    }

    /**
     * Regresa el valor de obsRiesgo.
     *
     * @return String.
     */
    public String getObsRiesgo() {
        return obsRiesgo;
    }

    /**
     * Asigna el valor para obsRiesgo.
     *
     * @param obsRiesgo El valor para obsRiesgo.
     */
    public void setObsRiesgo(String obsRiesgo) {
        this.obsRiesgo = obsRiesgo;
    }

    /**
     * Regresa el valor de pagoAnticipado.
     *
     * @return InputStream.
     */
    public InputStream getPagoAnticipado() {
        return pagoAnticipado;
    }

    /**
     * Asigna el valor para pagoAnticipado.
     *
     * @param pagoAnticipado El valor para pagoAnticipado.
     */
    public void setPagoAnticipado(InputStream pagoAnticipado) {
        this.pagoAnticipado = pagoAnticipado;
    }

    /**
     * Regresa el valor de promotor.
     *
     * @return String.
     */
    public String getPromotor() {
        return promotor;
    }

    /**
     * Asigna el valor para promotor.
     *
     * @param promotor El valor para promotor.
     */
    public void setPromotor(String promotor) {
        this.promotor = promotor;
    }

    /**
     * Regresa el valor de rfc.
     *
     * @return String.
     */
    public String getRfc() {
        return rfc;
    }

    /**
     * Asigna el valor para rfc.
     *
     * @param rfc El valor para rfc.
     */
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    /**
     * Regresa el valor de sFactura.
     *
     * @return String.
     */
    public String getSFactura() {
        return sFactura;
    }

    /**
     * Asigna el valor para sFactura.
     *
     * @param factura El valor para sFactura.
     */
    public void setSFactura(String factura) {
        sFactura = factura;
    }

    /**
     * Regresa el valor de sFacturaAnticipada.
     *
     * @return String.
     */
    public String getSFacturaAnticipada() {
        return sFacturaAnticipada;
    }

    /**
     * Asigna el valor para sFacturaAnticipada.
     *
     * @param facturaAnticipada El valor para sFacturaAnticipada.
     */
    public void setSFacturaAnticipada(String facturaAnticipada) {
        sFacturaAnticipada = facturaAnticipada;
    }

    /**
     * Regresa el valor de sPagoAnticipado.
     *
     * @return String.
     */
    public String getSPagoAnticipado() {
        return sPagoAnticipado;
    }

    /**
     * Asigna el valor para sPagoAnticipado.
     *
     * @param pagoAnticipado El valor para sPagoAnticipado.
     */
    public void setSPagoAnticipado(String pagoAnticipado) {
        sPagoAnticipado = pagoAnticipado;
    }

    /**
     * Regresa el valor de statusDeal.
     *
     * @return String.
     */
    public String getStatusDeal() {
        return statusDeal;
    }

    /**
     * Asigna el valor para statusDeal.
     *
     * @param statusDeal El valor para statusDeal.
     */
    public void setStatusDeal(String statusDeal) {
        this.statusDeal = statusDeal;
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
     * Asigna el valor para statusDetalleDeal.
     *
     * @param statusDetalleDeal El valor para statusDetalleDeal.
     */
    public void setStatusDetalleDeal(String statusDetalleDeal) {
        this.statusDetalleDeal = statusDetalleDeal;
    }

    /**
     * Regresa el valor de sTomaEnFirme.
     *
     * @return String.
     */
    public String getSTomaEnFirme() {
        return sTomaEnFirme;
    }

    /**
     * Asigna el valor para sTomaEnFirme.
     *
     * @param tomaEnFirme El valor para sTomaEnFirme.
     */
    public void setSTomaEnFirme(String tomaEnFirme) {
        sTomaEnFirme = tomaEnFirme;
    }

    /**
     * Regresa el valor de subtotalDivisa.
     *
     * @return String.
     */
    public String getSubtotalDivisa() {
        return subtotalDivisa;
    }

    /**
     * Asigna el valor para subtotalDivisa.
     *
     * @param subtotalDivisa El valor para subtotalDivisa.
     */
    public void setSubtotalDivisa(String subtotalDivisa) {
        this.subtotalDivisa = subtotalDivisa;
    }

    /**
     * Regresa el valor de subTotalEntregamos.
     *
     * @return String.
     */
    public String getSubTotalEntregamos() {
        return subTotalEntregamos;
    }

    /**
     * Asigna el valor para subTotalEntregamos.
     *
     * @param subTotalEntregamos El valor para subTotalEntregamos.
     */
    public void setSubTotalEntregamos(String subTotalEntregamos) {
        this.subTotalEntregamos = subTotalEntregamos;
    }

    /**
     * Regresa el valor de subTotalRecibimos.
     *
     * @return String.
     */
    public String getSubTotalRecibimos() {
        return subTotalRecibimos;
    }

    /**
     * Asigna el valor para subTotalRecibimos.
     *
     * @param subTotalRecibimos El valor para subTotalRecibimos.
     */
    public void setSubTotalRecibimos(String subTotalRecibimos) {
        this.subTotalRecibimos = subTotalRecibimos;
    }

    /**
     * Regresa el valor de tipCambio.
     *
     * @return String.
     */
    public String getTipCambio() {
        return tipCambio;
    }

    /**
     * Asigna el valor para tipCambio.
     *
     * @param tipCambio El valor para tipCambio.
     */
    public void setTipCambio(String tipCambio) {
        this.tipCambio = tipCambio;
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
     * Asigna el valor para tipoCambio.
     *
     * @param tipoCambio El valor para tipoCambio.
     */
    public void setTipoCambio(Double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    /**
     * Regresa el valor de tipoValor.
     *
     * @return String.
     */
    public String getTipoValor() {
        return tipoValor;
    }

    /**
     * Asigna el valor para tipoValor.
     *
     * @param tipoValor El valor para tipoValor.
     */
    public void setTipoValor(String tipoValor) {
        this.tipoValor = tipoValor;
    }

    /**
     * Regresa el valor de tomaEnFirme.
     *
     * @return InputStream.
     */
    public InputStream getTomaEnFirme() {
        return tomaEnFirme;
    }

    /**
     * Asigna el valor para tomaEnFirme.
     *
     * @param tomaEnFirme El valor para tomaEnFirme.
     */
    public void setTomaEnFirme(InputStream tomaEnFirme) {
        this.tomaEnFirme = tomaEnFirme;
    }

    /**
     * Regresa el valor de totalEntregamos.
     *
     * @return Double.
     */
    public Double getTotalEntregamos() {
        return totalEntregamos;
    }

    /**
     * Asigna el valor para totalEntregamos.
     *
     * @param totalEntregamos El valor para totalEntregamos.
     */
    public void setTotalEntregamos(Double totalEntregamos) {
        this.totalEntregamos = totalEntregamos;
    }

    /**
     * Regresa el valor de totalRecibimos.
     *
     * @return Double.
     */
    public Double getTotalRecibimos() {
        return totalRecibimos;
    }

    /**
     * Asigna el valor para totalRecibimos.
     *
     * @param totalRecibimos El valor para totalRecibimos.
     */
    public void setTotalRecibimos(Double totalRecibimos) {
        this.totalRecibimos = totalRecibimos;
    }

	public String getNumeroReferencia() {
		return numeroReferencia;
	}

	public void setNumeroReferencia(String numeroReferencia) {
		this.numeroReferencia = numeroReferencia;
	}

    /**
     * El id del Deal
     */
    private Integer idDeal;

    /**
     * La fecha de captura del Deal
     */
    private Date fechaCaptura = new Date();

    /**
     * La fecha de aplicaci&oacute;n del Deal
     */
    private Date fechaAplicacion = new Date();

    /**
     * La fecha valor del Deal
     */
    private String tipoValor;

    /**
     * El id del promotor
     */
    private Integer idUsuario;

    /**
     * Observaciones generales del Deal
     */
    private String observaciones;

    /**
     * Estatus del Deal
     */
    private String statusDeal;

    /**
     * Total recibimos del Deal
     */
    private Double totalRecibimos;

    /**
     * Folio del Detalle del deal
     */
    private Integer folioDetalle;

    /**
     * Descripci&oacute;n del producto del Detalle
     * del Deal
     */
    private String descripcion;

    /**
     * Monto del detalle del deal
     */
    private Double monto;

    /**
     * Tipo de cambio del Detalle del Deal
     */
    private Double tipoCambio;

    /**
     * Importe del detalle del Deal
     */
    private Double importe;

    /**
     * Total entregamos del Deal
     */
    private Double totalEntregamos;

    /**
     * Nombre del cliente
     */
    private String cliente;

    /**
     * Nombre del promotor
     */
    private String promotor;

    /**
     * El id persona del cliente
     */
    private Integer idPersona;

    /**
     * Imagen OK si existe pago anticipado
     */
    private InputStream pagoAnticipado;

    /**
     * Imagen OK si existe factura
     */
    private InputStream factura;

    /**
     * RFC del cliente
     */
    private String rfc;

    /**
     * Direcci&oacute;n del cliente
     */
    private String direccion;

    /**
     * Imagen OK si existe factura anticipada
     */
    private InputStream facturaAnticipada;

    /**
     * Imagen OK si existe toma en firme
     */
    private InputStream tomaEnFirme;

    /**
     * Logo de Ixe
     */
    private InputStream image;

    /**
     * Booleano para saber si el Deal es
     * de compra o venta
     */
    private Boolean compra;

    /**
     * Observaciones del horario del deal
     */
    private String obsHr;

    /**
     * Observaciones del monto del deal
     */
    private String obsMonto;

    /**
     * Observaciones de la docuemntaci&oacute;n del deal
     */
    private String obsDoc;

    /**
     * Observaciones de modificaciones del deal
     */
    private String obsMod;

    /**
     * Observaciones de la cancelaci&oacute;n del deal
     */
    private String obsCancel;

    /**
     * Observaciones del Factor del deal
     */
    private String obsFact;

    /**
     * El id deal
     */
    private String obsRiesgo;

    /**
     * El statusDetalleDeal
     */
    private String statusDetalleDeal;

    /**
     * Observaciones de la documentaci&oacute;n
     * del Detalle del Deal
     */
    private String obsDocDet;

    /**
     * Observaciones de la L&iacute;nea de Cr&eacute;dito
     * del Detalle del Deal
     */
    private String obsLinCredDet;

    /**
     * Define el tipo de cambio
     */
    private String tipCambio;

    /**
     * Leyenda del deal
     */
    private String leyendaDeal;

    /**
     * Mnem&oacute;nico del Detalle del Deal
     */
    private String descMnemonico;

    /**
     * informaci&oacute;n Adicional del deal
     */
    private String infoAdicional;

    /**
     * subTotalEntregamos e los Detalles del Deal
     */
    private String subTotalRecibimos;

    /**
     * subTotalEntregamos de los Detalles del Deal
     */
    private String subTotalEntregamos;

    /**
     * idDivisa del Detalle del Deal
     */
    private String idDivisa;

    /**
     * Booleano isRecibimos del Detalle del Deal
     */
    private Boolean isRecibimos;

    /**
     * Double comision del Detalle del Deal
     */
    private Double comisionMxn;

    /**
     * String mensajeria del Detalle del Deal
     */
    private String mensajeria;

    /**
     * Double iva del Detalle del Deal
     */
    private Double iva;

    /**
     * Contenido de la informaci&oacute;n Adicional del deal
     */
    private String infoAdicionalValor;

    /**
     * Define si el Deal requiere de factura
     */
    private String sFactura;

    /**
     * Define si el Deal requiere de toma en firme
     */
    private String sTomaEnFirme;

    /**
     * Define si el Deal requiere de pago anticipado
     */
    private String sPagoAnticipado;

    /**
     * Define si el Deal requiere de factura anticipada
     */
    private String sFacturaAnticipada;

    /**
     * Define el subtotal de la divisa
     */
    private String subtotalDivisa;

    /**
     * Define el contenido del campo acudir con
     * para mensajeria
     */
    private String acudirCon;

    /**
     * Define el numero de referencia de la operacion.
     */
    private String numeroReferencia;

    /**
     * El UID para serializaci&ocute;n.
     */
    private static final long serialVersionUID = -5166782580883382093L;

}
