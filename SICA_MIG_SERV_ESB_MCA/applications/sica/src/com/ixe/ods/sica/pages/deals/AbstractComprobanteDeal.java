/*
 * $Id: AbstractComprobanteDeal.java,v 1.1.2.2 2014/01/02 19:56:11 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2013 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import com.ixe.ods.bup.model.Direccion;
import com.ixe.ods.bup.sdo.BupServiceData;
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaSiteCache;
import com.ixe.ods.sica.model.*;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.tapestry.BaseGlobal;
import com.ixe.treasury.dom.common.Banco;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import net.sf.jasperreports.engine.JRDataSource;
import org.apache.tapestry.ApplicationRuntimeException;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * P&aacute;gina que permite obtener el DataSource para generar el PDF del comprobante de Deal.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.1.2.2 $ $Date: 2014/01/02 19:56:11 $
 */
public abstract class AbstractComprobanteDeal extends SicaPage {

    /**
     * M&eacut;etodo que obtiene el DataSource para reporte del Deal
     *
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider
     * @param idDeal La llave primaria del registro.
     * @param idDealDetalle El n&uacute;mero de detalle (opcional).
     * @return JRDataSource.
     */
    public JRDataSource getComprobanteDealDataSource(int idDeal, Integer idDealDetalle) {
        try {
            Deal d = getSicaServiceData().findDeal(idDeal);
            List dealMap = new ArrayList();
            List subTotalRecibimos = new ArrayList();
            List subTotalEntregamos = new ArrayList();
            List printBefore = d.isCompra() ? d.getRecibimos() : d.getEntregamos();
            List printAfter = d.isCompra() ? d.getEntregamos() : d.getRecibimos();
            BaseGlobal global = (BaseGlobal) getGlobal();
            ApplicationContext applicationContext = global.getApplicationContext();
            for (Iterator it = printBefore.iterator(); it.hasNext(); ) {
                DealDetalle dealDetalle = (DealDetalle) it.next();
                if (!DealDetalle.STATUS_DET_CANCELADO.equals(dealDetalle.getStatusDetalleDeal())) {
                    if (idDealDetalle == null || idDealDetalle.intValue() == dealDetalle.getIdDealPosicion()) {
                        HashMap map = llenarReporte(d, dealDetalle, applicationContext,
                                subTotalEntregamos, subTotalRecibimos, idDealDetalle == null);
                        if (map != null) {
                            dealMap.add(map);
                        }
                    }
                }
            }
            for (Iterator it = printAfter.iterator(); it.hasNext(); ) {
                DealDetalle dealDetalle = (DealDetalle) it.next();
                if (!DealDetalle.STATUS_DET_CANCELADO.equals(dealDetalle.getStatusDetalleDeal())) {
                    if (idDealDetalle == null || idDealDetalle.intValue() == dealDetalle.getIdDealPosicion()) {
                        HashMap map = llenarReporte(d, dealDetalle, applicationContext,
                                subTotalEntregamos, subTotalRecibimos, idDealDetalle == null);
                        if (map != null) {
                            dealMap.add(map);
                        }
                    }
                }
            }
            return new ListDataSource(dealMap);
        }
        catch (SicaException e) {
            debug(e);
            throw new ApplicationRuntimeException(e);
        }
    }

    /**
     * Llena los datos del DataSource para reporte del Deal .
     *
     * @param d                  El Deal seleccionado
     * @param dealDetalle        Detalle del deal seleccionado
     * @param applicationContext Contexto de la aplicacion
     * @param subTotalEntregamos Lista de las cantidades que se entregaron en el deal
     * @param subTotalRecibimos  Lista de las cantidades que se recibieron en el deal
     * @return HashMap Mapa con los datos de los detalles del reporte del deal seleccionado.
     */
    public HashMap llenarReporte(Deal d, DealDetalle dealDetalle,
                                 ApplicationContext applicationContext, List subTotalEntregamos,
                                 List subTotalRecibimos, boolean global) {
        FormaPagoLiq fpl = getFormaPagoLiq(dealDetalle.getMnemonico());
        PlantillaPantalla pp = getPlantillaPantalla(dealDetalle.getMnemonico());
        List datosAdicionales = dealDetalle.getInfoAdicional(fpl, pp);
        if (dealDetalle.getPlantilla() instanceof IPlantillaIntl) {
            HashMap map = new HashMap();
            map.put("nombre", "PLAZA");
            map.put("valor", plazaBanco((IPlantillaIntl) dealDetalle.getPlantilla()));
            datosAdicionales.add(map);
        }
        HashMap map1 = new HashMap();
        HashMap mapa;
        StringBuffer listTotalRecibimos = new StringBuffer();
        StringBuffer listTotalEntregamos = new StringBuffer();
        StringBuffer listMontoTotalRecibimos = new StringBuffer();
        StringBuffer listMontoTotalEntregamos = new StringBuffer();
        List sub = new ArrayList();
        List lista = new ArrayList();
        try {
            InputStream inputStream = applicationContext.
                    getResource("images/reportes/banorte.png").getInputStream();
            map1.put("image", inputStream);
        }
        catch (IOException e) {
            debug(e);
        }
        if (dealDetalle.getDeal().isPagoAnticipado()) {
            map1.put("SPagoAnticipado", "X");
        }
        else {
            map1.put("SPagoAnticipado", "");
        }
        if (dealDetalle.getDeal().isConFactura()) {
            map1.put("SFactura", "X");
        }
        else {
            map1.put("SFactura", "");
        }
        map1.put("SFfacturaAnticipada", "");
        if (dealDetalle.getDeal().isTomaEnFirme()) {
            map1.put("STomaEnFirme", "X");
        }
        else {
            map1.put("STomaEnFirme", "");
        }
        map1.put("idDeal", new Integer(d.getIdDeal()));
        map1.put("idPersona", d.getCliente().getIdPersona());
        //map1.put("cliente", d.getCliente().getNombreCompleto()+" "+d.getCliente().getRfc());
        //map1.put("rfc", "");
        BupServiceData bsd = (BupServiceData) applicationContext.getBean("bupServiceData");
        List l = bsd.findFiscalAddressByIdPerson(d.getCliente().getIdPersona());
        String direccion = "";
        for (Iterator iter = l.iterator(); iter.hasNext();) {
            StringBuffer dir = new StringBuffer();
            Direccion element = (Direccion) iter.next();
            if (element.getCalleYNumero() != null) {
                dir.append(element.getCalleYNumero());
            }
            if (element.getNumExterior() != null) {
                dir.append(" " + element.getNumExterior());
            }
            if (element.getNumInterior() != null) {
                dir.append(" " + element.getNumInterior());
            }
            if (element.getColonia() != null) {
                dir.append(" Col. ").append(element.getColonia());
            }
            if (element.getCiudad() != null) {
                dir.append(" ").append(element.getCiudad());
            }
            if (element.getDelegacionMunicipio() != null) {
                dir.append(" ").append(element.getDelegacionMunicipio());
            }
            if (element.getCodigoPostal() != null) {
                dir.append(" C.P. ").append(element.getCodigoPostal());
            }
            direccion = dir.toString();
            /*
        	direccion += (element.getCalleYNumero() + " COL. " + element.getColonia() + " "
        	+ element.getCiudad() + " " + element.getDelegacionMunicipio()
                    + " C.P. " + element.getCodigoPostal());
                    */
        }
        map1.put("cliente", d.getCliente().getNombreCompleto() + "\n" + d.getCliente().getRfc() +
                "\n" + direccion);
        map1.put("numeroReferencia", d.getCliente() != null ?
                getClaveReferencia(d.getCliente().getIdPersona()) : "S/N") ;
        //map1.put("rfc", "");
        //map1.put("direccion", "");
        if (d.getTipoValor().equals("CASH")) {
            map1.put("tipoValor", "HOY");
        }
        else {
            map1.put("tipoValor", d.getTipoValor());
        }
        map1.put("fechaCaptura", d.getFechaCaptura());
        map1.put("fechaAplicacion", d.getFechaLiquidacion());
        map1.put("idUsuario", d.getPromotor().getIdPersona());
        map1.put("promotor", d.getPromotor().getPaterno() + " " + d.getPromotor().getMaterno() +
                " " + d.getPromotor().getNombre());
        map1.put("compra", dealDetalle != null ?
                Boolean.valueOf(dealDetalle.isRecibimos()) :
                Boolean.valueOf(d.isCompra()));
        map1.put("statusDetalleDeal", dealDetalle.getStatusDetalleDeal());

        if (global) {
            double comisionSinIVA = d.getTotalComisionMxn() /  (1 + (d.getCanalMesa().getCanal().
                    getTipoIva().getPorcIva() / 100.0));

            Double comisionSinIVARedondeado = new Double(Redondeador.redondear2Dec(comisionSinIVA));
            map1.put("comisionMxn", comisionSinIVARedondeado);
            double iva = d.getTotalComisionMxn() - comisionSinIVARedondeado.doubleValue();
            Double ivaRedondeado = new Double(Redondeador.redondear2Dec(iva));
            map1.put("iva", ivaRedondeado);
        }
        else {
            double comisionSinIVA = dealDetalle.getComisionCobradaMxn() /  (1 + (d.getCanalMesa().getCanal().
                    getTipoIva().getPorcIva() / 100.0));

            Double comisionSinIVARedondeado = new Double(Redondeador.redondear2Dec(comisionSinIVA));
            map1.put("comisionMxn", comisionSinIVARedondeado);
            double iva = dealDetalle.getComisionCobradaMxn() - comisionSinIVARedondeado.doubleValue();
            Double ivaRedondeado = new Double(Redondeador.redondear2Dec(iva));
            map1.put("iva", ivaRedondeado);
        }
        if (d.getDireccion() != null && d.isMensajeria()) {
            StringBuffer dir = new StringBuffer();
            if (d.getDireccion().getCalleYNumero() != null) {
                dir.append(d.getDireccion().getCalleYNumero());
            }
            if (d.getDireccion().getColonia() != null) {
                dir.append(" Col. ").append(d.getDireccion().getColonia());
            }
            if (d.getDireccion().getCiudad() != null) {
                dir.append(" ").append(d.getDireccion().getCiudad());
            }
            if (d.getDireccion().getDelegacionMunicipio() != null) {
                dir.append(" ").append(d.getDireccion().getDelegacionMunicipio());
            }
            if (d.getDireccion().getCodigoPostal() != null) {
                dir.append(" C.P. ").append(d.getDireccion().getCodigoPostal());
            }
            map1.put("mensajeria", dir.toString());
            /*
    map1.put("mensajeria", d.getDireccion().getCalleYNumero() + " " + d.getDireccion().getCiudad()
        + " " + d.getDireccion().getColonia() + " " + d.getDireccion().getDelegacionMunicipio()
            + " C.P : " + d.getDireccion().getCodigoPostal());
            */
        }
        if (dealDetalle.getDeal().getObservaciones() != null) {
            String obs = dealDetalle.getDeal().getObservaciones().toUpperCase();
            map1.put("observaciones", obs);
        }
        else {
            map1.put("observaciones", dealDetalle.getDeal().getObservaciones());
        }
        map1.put("metodoPago", dealDetalle.getDeal().getMetodoPago());
        map1.put("cuentaPago", dealDetalle.getDeal().getCuentaPago());
        map1.put("leyendaDeal", getSicaServiceData().findParametro(ParametroSica.LEYENDA_IMPRESION_DEAL).getValor());
        map1.put("descMnemonico", fpl.getDescripcion().toUpperCase());
        if ("S".equals(dealDetalle.getDeal().decodificarEvento(Deal.EV_IND_DOCUMENTACION))) {
            map1.put("obsDoc", "Se requiere de documentaci\u00f3n adicional".toUpperCase());
        }
        else if ("A".equals(dealDetalle.getDeal().decodificarEvento(Deal.EV_IND_DOCUMENTACION))) {
            map1.put("obsDoc", ("Se aprob\u00f3 el detalle del deal sin la documentaci\u00f3n " +
                    "requerida").toUpperCase());
        }
        else if ("N".equals(dealDetalle.getDeal().decodificarEvento(Deal.EV_IND_DOCUMENTACION))) {
            map1.put("obsDoc", ("Se encuentra en espera de la documentaci\u00f3n por lo que el " +
                    "deal esta parado en el proceso del negocio").toUpperCase());
        }
        if ("S".equals(dealDetalle.getDeal().decodificarEvento(Deal.EV_IND_GRAL_MODIFICACION))) {
            map1.put("obsMod", "Solicitud de autorizaci\u00f3n para modificar un deal".
                    toUpperCase());
        }
        else
        if ("A".equals(dealDetalle.getDeal().decodificarEvento(Deal.EV_IND_GRAL_MODIFICACION))) {
            map1.put("obsMod", "Aprobaci\u00f3n de la modificaci\u00f3n del deal".toUpperCase());
        }
        else
        if ("N".equals(dealDetalle.getDeal().decodificarEvento(Deal.EV_IND_GRAL_MODIFICACION))) {
            map1.put("obsMod", "Negaci\u00f3n a que se puede modificar el deal".toUpperCase());
        }
        if ("S".equals(dealDetalle.getDeal().decodificarEvento(Deal.EV_IND_GRAL_CANCELACION))) {
            map1.put("obsCancel", "Solicitud de autorizaci\u00f3n para cancelar el deal".
                    toUpperCase());
        }
        else if ("L".equals(dealDetalle.getDeal().
                decodificarEvento(Deal.EV_IND_GRAL_CANCELACION))) {
            map1.put("obsCancel", ("Aprobaci\u00f3n del \u00e1rea de liquidaci\u00f3n de cancelar el " +
                    "deal").toUpperCase());
        }
        else if ("A".equals(dealDetalle.getDeal().
                decodificarEvento(Deal.EV_IND_GRAL_CANCELACION))) {
            map1.put("obsCancel", ("Aprobaci\u00f3n de la Mesa de Cambios de la cancelaci\u00f3n " +
                    "del deal").toUpperCase());
        }
        else if ("N".equals(dealDetalle.getDeal().
                decodificarEvento(Deal.EV_IND_GRAL_CANCELACION))) {
            map1.put("obsCancel", ("Negaci\u00f3n del \u00e1rea de liquidaci\u00f3n para la " +
                    "cancelaci\u00f3n del deal").toUpperCase());
        }
        else if ("M".equals(dealDetalle.getDeal().
                decodificarEvento(Deal.EV_IND_GRAL_CANCELACION))) {
            map1.put("obsCancel", ("Negaci\u00f3n de la Mesa de Cambios para la " +
                    "cancelaci\u00f3n del deal").toUpperCase());
        }
        else if ("C".equals(dealDetalle.getDeal().
                decodificarEvento(Deal.EV_IND_GRAL_CANCELACION))) {
            map1.put("obsCancel", "Deal cancelado manualmente".toUpperCase());
        }
        if ("S".equals(dealDetalle.getDeal().decodificarEvento(Deal.EV_IND_DOCUMENTACION))) {
            map1.put("obsDocDet", "Se requiere de documentaci\u00f3n adicional del detalle".
                    toUpperCase());
        }
        else if ("A".equals(dealDetalle.getDeal().decodificarEvento(Deal.EV_IND_DOCUMENTACION))) {
            map1.put("obsDocDet", ("Se aprob\u00f3 el detalle del deal sin la documentaci\u00f3n " +
                    "requerida del detalle").toUpperCase());
        }
        else if ("N".equals(dealDetalle.getDeal().decodificarEvento(Deal.EV_IND_DOCUMENTACION))) {
            map1.put("obsDocDet", ("Se encuentra en espera de la documentaci\u00f3n por lo que " +
                    "el deal esta parado en el proceso del negocio del detalle").toUpperCase());
        }
        if ("S".equals(dealDetalle.getDeal().decodificarEvento(Deal.EV_IND_GRAL_PAG_ANT))) {
            map1.put("obsLinCredDet", "Requiere autorizaci\u00f3n para Pago Anticipado".
                    toUpperCase());
        }
        else if ("S".equals(dealDetalle.getDeal().decodificarEvento(Deal.EV_IND_GRAL_PAG_ANT))) {
            map1.put("obsLinCredDet", "Solicitud de excedente de L\u00ednea de Cr\u00e9dito".
                    toUpperCase());
        }
        else if ("A".equals(dealDetalle.getDeal().decodificarEvento(Deal.EV_IND_GRAL_PAG_ANT))) {
            map1.put("obsLinCredDet", ("Autorizaci\u00f3n del excedente de la l\u00ednea de " +
                    "Cr\u00e9dito").toUpperCase());
        }
        else if ("N".equals(dealDetalle.getDeal().decodificarEvento(Deal.EV_IND_GRAL_PAG_ANT))) {
            map1.put("obsLinCredDet", ("No se autoriz\u00f3 el excedente de l\u00ednea de " +
                    "cr\u00e9dito por lo que se apag\u00f3 la bandera de pago anticipado").
                    toUpperCase());
        }
        map1.put("idDivisa", dealDetalle.getDivisa().getDescripcion());
        map1.put("isRecibimos", Boolean.valueOf(dealDetalle.isRecibimos()));
        if (String.valueOf(dealDetalle.getTipoCambio()) != null) {
            String tipoCambio = getCurrencyFormat().format(dealDetalle.getTipoCambio());
            map1.put("tipCambio", tipoCambio);
        }
        else {
            map1.put("tipCambio", "");
        }
        map1.put("tipoCambio", new Double(dealDetalle.getTipoCambio()));
        map1.put("monto", new Double(dealDetalle.getMonto()));
        map1.put("importe", new Double(dealDetalle.getImporte()));
        map1.put("totalRecibimos", new Double(d.getTotalRecibimos()));
        map1.put("totalEntregamos", new Double(d.getTotalEntregamos()));
        if (dealDetalle.isRecibimos()) {
            HashMap map2 = new HashMap();
            map2.put("descRecibimos", dealDetalle.getDivisa().getDescripcion());
            map2.put("montoRecibimos", new Double(dealDetalle.getMonto()));
            subTotalRecibimos.add(map2);
            for (Iterator iterator = subTotalRecibimos.iterator(); iterator.hasNext();) {
                HashMap elemento = (HashMap) iterator.next();
                String element = elemento.get("descRecibimos").toString();
                if (!sub.contains(element)) {
                    sub.add(element);
                }
            }
            for (Iterator iter = sub.iterator(); iter.hasNext();) {
                double subtot = 0;
                String element = (String) iter.next();
                for (Iterator iterator = subTotalRecibimos.iterator(); iterator.hasNext();) {
                    HashMap elemento = (HashMap) iterator.next();
                    if (elemento.get("descRecibimos").toString().equals(element)) {
                        subtot += ((Double) elemento.get("montoRecibimos")).doubleValue();
                    }
                }
                mapa = new HashMap();
                mapa.put("descRecibimos", element);
                mapa.put("montoRecibimos", new Double(subtot));
                lista.add(mapa);
            }
            for (Iterator iter = lista.iterator(); iter.hasNext();) {
                HashMap element = (HashMap) iter.next();
                listTotalRecibimos.append(element.get("descRecibimos")).append("\n");
                listMontoTotalRecibimos.append(getMoneyFormat().
                        format(element.get("montoRecibimos"))).append("\n");

            }
            map1.put("subTotalRecibimos", listTotalRecibimos.toString());
            map1.put("subtotalDivisa", listMontoTotalRecibimos.toString());
        }
        else {
            HashMap map3 = new HashMap();
            map3.put("descEntregamos", dealDetalle.getDivisa().getDescripcion());
            map3.put("montoEntregamos", new Double(dealDetalle.getMonto()));
            subTotalEntregamos.add(map3);
            for (Iterator iterator = subTotalEntregamos.iterator(); iterator.hasNext();) {
                HashMap elemento = (HashMap) iterator.next();
                String element = elemento.get("descEntregamos").toString();
                if (!sub.contains(element)) {
                    sub.add(element);
                }
            }
            for (Iterator iter = sub.iterator(); iter.hasNext();) {
                double subtot = 0;
                String element = (String) iter.next();
                for (Iterator iterator = subTotalEntregamos.iterator(); iterator.hasNext();) {
                    HashMap elemento = (HashMap) iterator.next();
                    if (elemento.get("descEntregamos").toString().equals(element)) {
                        subtot += ((Double) elemento.get("montoEntregamos")).doubleValue();
                    }
                }
                mapa = new HashMap();
                mapa.put("descEntregamos", element);
                mapa.put("montoEntregamos", new Double(subtot));
                lista.add(mapa);
            }
            for (Iterator iter = lista.iterator(); iter.hasNext();) {
                HashMap element = (HashMap) iter.next();
                listTotalEntregamos.append(element.get("descEntregamos")).append("\n");
                listMontoTotalEntregamos.append(getMoneyFormat().
                        format(element.get("montoEntregamos"))).append("\n");
            }
            map1.put("subTotalEntregamos", listTotalEntregamos.toString());
            map1.put("subtotalDivisa", listMontoTotalEntregamos.toString());
        }
        String infoAdicional = "";
        String infoAdicionalValor = "";
        String nombre;
        String valor;
        for (Iterator iter = datosAdicionales.iterator(); iter.hasNext();) {
            HashMap element = (HashMap) iter.next();
            if (element.get("nombre") != null && element.get("valor") != null) {
                nombre = element.get("nombre").toString();
                valor = element.get("valor").toString();
                if (nombre.equals("BANCO PAGADOR")) {
                    nombre = "BANCO BENEFICIARIO";
                }
                if (nombre.indexOf("BANCO PAGADOR") > 0) {
                    int index = nombre.indexOf("BANCO PAGADOR");
                    String tmp = nombre.substring(0, index);
                    nombre = tmp + " " + "BANCO BENEFICIARIO";
                }
                int pipeIndex = valor.indexOf("|");
                if (pipeIndex > 0) {
                    StringTokenizer tokenValor = new StringTokenizer(valor, "|");
                    String newValor = "";
                    while (tokenValor.hasMoreTokens()) {
                        newValor += tokenValor.nextToken() + " ";
                    }
                    valor = newValor;
                }
                String clave = "ALRA 422397";
                if (valor.startsWith(clave)) {
                    valor = valor.substring(12);
                }
                if (nombre.equals("INFORMACION PARA EL BENEFICIARIO")) {
                    nombre = "INF. PARA BENEFICIARIO";
                }
                if (nombre.equals("INSTRUCCIONES PARA EL  BANCO BENEFICIARIO")) {
                    nombre = "INST. A BANCO BENEFICIARIO";
                }
                if (nombre.equals("INSTRUCCIONES PARA EL BANCO INTERMEDIARIO")) {
                    nombre = "INST. A BANCO INTERMEDIARIO";
                }
                infoAdicional += nombre + "\n";
                infoAdicionalValor += valor + "\n";
                if (valor.length() > 70) {
                    infoAdicional += "\n";
                }
            }
        }
        map1.put("infoAdicional", infoAdicional);
        map1.put("infoAdicionalValor", infoAdicionalValor);
        map1.put("acudirCon", d.getAcudirCon() != null ? d.getAcudirCon() : "");
        return map1;
    }

    /**
     * Regresa la ciudad y el estado del banco de la plantilla.
     *
     * @param plantilla La plantilla a inspeccionar.
     * @return String.
     */
    public String plazaBanco(IPlantillaIntl plantilla) {
        try {
            if (plantilla != null) {
                SicaSiteCache ssc = (SicaSiteCache) getApplicationContext().
                        getBean("sicaSiteCache");
                Banco banco = ssc.encontrarBanco(getTicket(), plantilla, false);
                if (banco == null) {
                    return "";
                }
                return (banco.getPlaza() != null ? banco.getPlaza().toUpperCase() + " " : "") +
                        (banco.getEstado() != null ? banco.getEstado() : "");
            }
        }
        catch (Exception e) {
            debug(e);
        }
        return "";
    }
}
