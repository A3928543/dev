/*
 * $Id: ConsultaDealsInterbancarios.java,v 1.19.72.1.6.1 2020/12/01 04:53:01 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals.interbancario;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.springframework.context.ApplicationContext;

import net.sf.jasperreports.engine.JRDataSource;

import com.ixe.ods.bup.model.Direccion;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.sdo.BupServiceData;
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaSiteCache;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.IPlantillaIntl;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.LogAuditoria;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PlantillaPantalla;
import com.ixe.ods.sica.pages.deals.AbstractConsultaDeals;
import com.ixe.ods.tapestry.BaseGlobal;
import com.ixe.ods.tapestry.services.jasper.DataSourceProvider;
import com.ixe.treasury.dom.common.Banco;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * <p>Subclase de AbstractConsultaDeals para consultar deals interbancarios.</p>
 * <p/>
 * <p>Los criterios de consulta son los siguientes:
 * <ul>
 * <li>No. deal.</li>
 * <li>Status.</li>
 * <li>No. cuenta.</li>
 * <li>Fecha inicio de captura.</li>
 * <li>Fecha fin de captura.</li>
 * <li>Fecha inicio de liquidaci&oacute;n.</li>
 * <li>Fecha fin de liquidaci&oacute;n.</li>
 * <li>Raz&oacute;n social.</li>
 * <li>Nombre, apellido paterno y/o apellido materno.</li>
 * <li>Eventos del deal.</li>
 * </ul>
 * </p>
 * <p/>
 * <p>Una vez listados los resultados de la consulta, el usuario puede seleccionar uno de los deals
 * y ver su informaci&oacute;n completa. Dependiendo del status de &eacute;ste se podr&aacute; o no
 * realizar modificaciones al deal.
 * </p>
 *
 * @author Jean C. Favila
 * @version $Revision: 1.19.72.1.6.1 $ $Date: 2020/12/01 04:53:01 $
 */
public abstract class ConsultaDealsInterbancarios extends AbstractConsultaDeals
        implements DataSourceProvider {

	/**
	 * Asigna lo valores necesarios al activarse la p&aacute;gina.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        limpiarTodo();
        setModo((String) cycle.getServiceParameters()[0]);
        setDeals(new ArrayList());
        Visit visit = (Visit) getVisit();
        List promotoresTmp;
        List promotores = new ArrayList();
        Integer idPromotor = visit.getUser().getIdPersona();
        int posicionPromotor = 0;
        // Verificando si se trata del Area de Promocion
        if (FacultySystem.SICA_CONS_DEAL.equals(getModo())) {
            // Obtiene subordinados incluyendo nodo raiz
            promotoresTmp = getSeguridadServiceData().
                    findSubordinadosEjecutivoYEjecutivosASustituir(((SupportEngine) getEngine()).
                            getApplicationName(), visit.getUser().getIdPersona());
            Collections.sort(promotoresTmp, new Comparator() {
                public int compare(Object o1, Object o2) {
                    EmpleadoIxe e1 = (EmpleadoIxe) o1;
                    EmpleadoIxe e2 = (EmpleadoIxe) o2;
                    return e1.getNombreCompleto().compareTo(e2.getNombreCompleto());
                }
            });
        }
        // Si se trata de otra Area
        else {
            promotoresTmp = getSicaServiceData().
                    findAllPromotoresSICA(((SupportEngine) getEngine()).getApplicationName());
        }
        //Llenando el Combo de Promotores
        if (promotoresTmp.size() > 0) {
            HashMap hm = new HashMap();
            hm.put("idPersona", new Integer(0));
            hm.put("nombreCompleto", "TODOS");
            promotores.add(hm);
            int aux = 0;
            for (Iterator it = promotoresTmp.iterator(); it.hasNext();) {
            	aux = aux + 1;
                hm = new HashMap();
                EmpleadoIxe promotor = (EmpleadoIxe) it.next();
                hm.put("idPersona", promotor.getIdPersona());
                hm.put("nombreCompleto", promotor.getNombreCompleto());
                if (promotor.getIdPersona().equals(idPromotor)) {
                	posicionPromotor = aux;
                }
                promotores.add(hm);
            }
            setPromotor((HashMap) promotores.get(posicionPromotor));
        }
        setPromotoresList(promotores);
    }

    /**
     * Activa la p&aacute;gina CapturaDealInterbancario y le pasa como par&aacute;metro el deal
     * seleccionado por el usuario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void verDeal(IRequestCycle cycle) {
        try {
            CapturaDealInterbancario nextPage = (CapturaDealInterbancario) cycle.
                    getPage("CapturaDealInterbancario");
            nextPage.setEstadoLineaCredito("");
            nextPage.setModoInsercion(false);
            Integer idDeal = (Integer) cycle.getServiceParameters()[0];
            auditar(idDeal, LogAuditoria.CONSULTA_DATOS_DEAL, null, "INFO", "E");
            Deal deal = getSicaServiceData().findDeal(idDeal.intValue());
            nextPage.setDeal(deal);
            LineaCambio linea = getLineaCreditoService().findLineaCreditoByIdPersona(deal.getCliente().getIdPersona());
            if (linea!= null){
            	nextPage.setEstadoLineaCredito(linea.getStatusLinea());
            }
            nextPage.activate(cycle);
        }
        catch (SicaException e) {
            debug(e);
            getDelegate().record(e.getMessage(), null);
        }
    }

    /**
     * M&eacut;etodo que obtiene el DataSource para reporte del Deal
     *
     * @param id El identificador del deal.
     * @return JRDataSource.
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
     */
    public JRDataSource getDataSource(String id) {
        try {
            Deal d = getSicaServiceData().findDeal(Integer.valueOf(id).intValue());
            List dealMap = new ArrayList();
            List subTotalRecibimos = new ArrayList();
            List subTotalEntregamos = new ArrayList();
            List printBefore = d.isCompra() ? d.getRecibimos() : d.getEntregamos();
            List printAfter = d.isCompra() ? d.getEntregamos() : d.getRecibimos();
            BaseGlobal global = (BaseGlobal) getGlobal();
            ApplicationContext applicationContext = global.getApplicationContext();
            for (Iterator it = printBefore.iterator(); it.hasNext();) {
                DealDetalle dealDetalle = (DealDetalle) it.next();
                if (!DealDetalle.STATUS_DET_CANCELADO.equals(dealDetalle.getStatusDetalleDeal())) {
                    HashMap map = llenarReporte(d, dealDetalle, applicationContext,
                            subTotalEntregamos, subTotalRecibimos);
                    if (map != null) {
                        dealMap.add(map);
                    }
                }
            }
            for (Iterator it = printAfter.iterator(); it.hasNext();) {
                DealDetalle dealDetalle = (DealDetalle) it.next();
                if (!DealDetalle.STATUS_DET_CANCELADO.equals(dealDetalle.getStatusDetalleDeal())) {
                    HashMap map = llenarReporte(d, dealDetalle, applicationContext,
                            subTotalEntregamos, subTotalRecibimos);
                    if (map != null) {
                        dealMap.add(map);
                    }
                }
            }
            return new ListDataSource(dealMap);
        }
        catch (SicaException e) {
            throw new ApplicationRuntimeException(e);
        }
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

    /**
     * Llena los datos del DataSource para reporte del Deal
     *
     * @param d El deal.
     * @param dealDetalle El detalle de deal.
     * @param applicationContext El applicationContext, utilizado para obtener las imagenes del
     *  reporte.
     * @param subTotalEntregamos El subtotal de la parte de entregamos.
     * @param subTotalRecibimos El subtotal de la parte de recibimos.
     * @return HashMap.
     */
    public HashMap llenarReporte(Deal d, DealDetalle dealDetalle,
                                 ApplicationContext applicationContext, List subTotalEntregamos,
                                 List subTotalRecibimos) {
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
                    getResource("images/reportes/logo_ixe.gif").getInputStream();
            map1.put("image", inputStream);
        }
        catch (IOException e) {
            debug(e);
        }
        if (dealDetalle.getDeal().isPagoAnticipado()) {
            try {
                InputStream inputStreamOkPagoAnt = applicationContext.
                        getResource("images/checked.gif").getInputStream();
                map1.put("pagoAnticipado", inputStreamOkPagoAnt);
            }
            catch (IOException e) {
//                if (_logger.isDebugEnabled()) {
//                    _logger.debug(e);
//                }
            }
        }
        else {
            map1.put("factura", null);
        }
        if (dealDetalle.getDeal().isConFactura()) {
            try {
                InputStream inputStreamOkFactura = applicationContext.
                        getResource("images/checked.gif").getInputStream();
                map1.put("factura", inputStreamOkFactura);
            }
            catch (IOException e) {
//                if (_logger.isDebugEnabled()) {
//                    _logger.debug(e);
//                }
            }
        }
        else {
            map1.put("factura", null);
        }
            map1.put("facturaAnticipada", null);
        if (dealDetalle.getDeal().isTomaEnFirme()) {
            try {
                InputStream inputStreamOkTomaEnFirme = applicationContext.
                        getResource("images/checked.gif").getInputStream();
                map1.put("tomaEnFirme", inputStreamOkTomaEnFirme);
            }
            catch (IOException e) {
//                if (_logger.isDebugEnabled()) {
//                    _logger.debug(e);
//                }
            }
        }
        else {
            map1.put("tomaEnFirme", null);
        }
        map1.put("idDeal", new Integer(d.getIdDeal()));
        String direccion = "";
        if (d.getCliente() != null) {
            map1.put("idPersona", d.getCliente().getIdPersona());
            map1.put("cliente", d.getCliente().getNombreCompleto());
            map1.put("rfc", d.getCliente().getRfc());
            BupServiceData bsd = (BupServiceData) applicationContext.getBean("bupServiceData");
            List l = bsd.findFiscalAddressByIdPerson(d.getCliente().getIdPersona());
            for (Iterator iter = l.iterator(); iter.hasNext();) {
                Direccion element = (Direccion) iter.next();
                direccion += (element.getCalleYNumero() + " COL. " + element.getColonia() + " " +
                        element.getCiudad() + " " + element.getDelegacionMunicipio() + " C.P. " +
                        element.getCodigoPostal());
            }
        }
        else {
            map1.put("idPersona", new Integer(0));
            map1.put("cliente", "IXE");
            map1.put("rfc", "0");
        }
        map1.put("direccion", direccion);
        if (d.getTipoValor().equals("CASH")) {
            map1.put("tipoValor", "HOY");
        }
        else {
            map1.put("tipoValor", d.getTipoValor());
        }
        map1.put("fechaCaptura", d.getFechaCaptura());
        map1.put("fechaAplicacion", d.getFechaLiquidacion());
        if (d.getPromotor() != null) {
            String pat="";
            String mat="";
            String nom="";
            if (d.getPromotor().getPaterno()!=null) {
            	pat = d.getPromotor().getPaterno();
            }
            if (d.getPromotor().getMaterno()!=null) {
            	mat = d.getPromotor().getMaterno();
            }
            if (d.getPromotor().getNombre()!=null) {
            	nom = d.getPromotor().getNombre();
            }
            map1.put("idUsuario", d.getPromotor().getIdPersona());
            //map1.put("promotor", d.getPromotor().getPaterno() + " "
            // + d.getPromotor().getMaterno() + " " + d.getPromotor().getNombre());
            map1.put("promotor",pat+" "+mat+" "+nom);
        }
        else {
            map1.put("idUsuario", new Integer(0));
            map1.put("promotor", "ixe");
        }
        map1.put("compra", Boolean.valueOf(d.isCompra()));
        map1.put("statusDetalleDeal", dealDetalle.getStatusDetalleDeal());
        map1.put("comisionMxn", new Double(d.getTotalComisionMxn()));
        map1.put("iva", new Double(d.getTotalComisionMxn() * 15 / 115));
        if (d.getDireccion() != null && d.isMensajeria()) {
            map1.put("mensajeria", d.getDireccion().getCalleYNumero() + " " + d.getDireccion().
                    getCiudad() + " " + d.getDireccion().getColonia() + " " + d.getDireccion().
                    getDelegacionMunicipio() + " C.P : " + d.getDireccion().getCodigoPostal());
        }
        map1.put("observaciones", dealDetalle.getDeal().getObservaciones());
        map1.put("leyendaDeal", getSicaServiceData().
                findParametro(ParametroSica.LEYENDA_IMPRESION_DEAL).getValor());
        map1.put("descMnemonico", fpl.getDescripcion());
        if ("S".equals(dealDetalle.getDeal().decodificarEvento(Deal.EV_IND_INT_DOCUMENTACION))) {
            map1.put("obsDoc", "Se requiere de documentación adicional");
        }
        else if ("A".equals(dealDetalle.getDeal().
                decodificarEvento(Deal.EV_IND_INT_DOCUMENTACION))) {
            map1.put("obsDoc", "Se aprobó el detalle del deal “sin” la documentación requerida");
        }
        else if ("N".equals(dealDetalle.getDeal().
                decodificarEvento(Deal.EV_IND_INT_DOCUMENTACION))) {
            map1.put("obsDoc", "Se encuentra en espera de la documentación por lo que el deal " +
                    "esta parado en el proceso del negocio");
        }
        if ("S".equals(dealDetalle.getDeal().decodificarEvento(Deal.EV_IND_GRAL_MODIFICACION))) {
            map1.put("obsMod", "Solicitud de autorización para modificar un deal");
        }
        else if ("A".equals(dealDetalle.getDeal().
                decodificarEvento(Deal.EV_IND_GRAL_MODIFICACION))) {
            map1.put("obsMod", "Aprobación de la modificación del deal");
        }
        else if ("N".equals(dealDetalle.getDeal().
                decodificarEvento(Deal.EV_IND_GRAL_MODIFICACION))) {
            map1.put("obsMod", "Negación a que se puede modificar el deal");
        }
        if ("S".equals(dealDetalle.getDeal().decodificarEvento(Deal.EV_IND_GRAL_CANCELACION))) {
            map1.put("obsCancel", "Solicitud de autorización para cancelar el deal");
        }
        else if ("L".equals(dealDetalle.getDeal().
                decodificarEvento(Deal.EV_IND_GRAL_CANCELACION))) {
            map1.put("obsCancel", "Aprobación del área de liquidación de cancelar el deal");
        }
        else if ("A".equals(dealDetalle.getDeal().
                decodificarEvento(Deal.EV_IND_GRAL_CANCELACION))) {
            map1.put("obsCancel", "Aprobación de la Mesa de Cambios de la cancelaci\u00f3n del " +
                    "deal");
        }
        else if ("N".equals(dealDetalle.getDeal().
                decodificarEvento(Deal.EV_IND_GRAL_CANCELACION))) {
            map1.put("obsCancel", "Negación del área de liquidación para la cancelaci\u00f3n del " +
                    "deal");
        }
        else if ("M".equals(dealDetalle.getDeal().
                decodificarEvento(Deal.EV_IND_GRAL_CANCELACION))) {
            map1.put("obsCancel", "Negación de la Mesa de Cambios para la cancelaci\u00f3n del " +
                    "deal");
        }
        else if ("C".equals(dealDetalle.getDeal().
                decodificarEvento(Deal.EV_IND_GRAL_CANCELACION))) {
            map1.put("obsCancel", "Deal cancelado manualmente");
        }
        if ("S".equals(dealDetalle.decodificarEvento(DealDetalle.EV_IND_INT_DOCUMENTACION))) {
            map1.put("obsDocDet", "Se requiere de documentación adicional del detalle");
        }
        else if ("A".equals(dealDetalle.decodificarEvento(DealDetalle.EV_IND_INT_DOCUMENTACION))) {
            map1.put("obsDocDet", "Se aprobó el detalle del deal “sin” la documentación " +
                    "requerida del detalle");
        }
        else if ("N".equals(dealDetalle.decodificarEvento(DealDetalle.EV_IND_INT_DOCUMENTACION))) {
            map1.put("obsDocDet", "Se encuentra en espera de la documentación por lo que el " +
                    "deal esta parado en el proceso del negocio del detalle");
        }
        map1.put("idDivisa", dealDetalle.getDivisa().getDescripcion());
        map1.put("isRecibimos", Boolean.valueOf(dealDetalle.isRecibimos()));
        if (String.valueOf(dealDetalle.getTipoCambio()) != null) {
        	String tipoCambio = getCurrencyFormatSix().format(dealDetalle.getTipoCambio());
        	map1.put("tipCambio", tipoCambio);
        }
        else {
        	map1.put("tipCambio", "");
        }
        map1.put("tipoCambio", new Double(dealDetalle.getTipoCambio()));
        map1.put("monto", new Double(dealDetalle.getMonto()));
        map1.put("importe", new Double(dealDetalle.getMonto() * dealDetalle.getTipoCambio()));
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
            		nombre="BANCO BENEFICIARIO";
                }
            	if (nombre.indexOf("BANCO PAGADOR")>0) {
            		int index = nombre.indexOf("BANCO PAGADOR");
            		String tmp = nombre.substring(0,index);
            		nombre = tmp+" "+"BANCO BENEFICIARIO";
            	}
            	int pipeIndex = valor.indexOf("|");
            	if (pipeIndex>0) {
            		StringTokenizer tokenValor = new StringTokenizer(valor,"|");
            		String newValor = "";
            		while (tokenValor.hasMoreTokens()) {
            			newValor+=tokenValor.nextToken()+" ";
            		}
            		valor=newValor;
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
            	infoAdicional += nombre+"\n";
            	infoAdicionalValor += valor+"\n";
                if (valor.length() > 70) {
            		infoAdicional += "\n";
            }
        }
        }
        map1.put("infoAdicional", infoAdicional);
        map1.put("infoAdicionalValor", infoAdicionalValor);
        return map1;
    }

    /**
     * Modelo del combo de Promotores.
     *
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getPromotoresModel() {
        return new RecordSelectionModel(getPromotoresList(), "idPersona", "nombreCompleto");
    }

    /**
     * Regresa false si el modo no es 'SICA_CONS_DEAL_INTER'. En caso contrario regresa lo que
     * conteste la superclase.
     *
     * @return boolean.
     */
    public boolean isVerHabilitado() {
        return FacultySystem.SICA_CONS_DEAL_INTER.equals(getModo()) && super.isVerHabilitado();
    }

    /**
     * Regresa siempre true para indicar que se trata de deals interbancarios.
     *
     * @return boolean.
     */
    protected boolean isInterbancario() {
        return true;
    }

    /**
     * Obtiene el Promotor seleccionado del combo de Promotores.
     *
     * @return HasMap El Promotor seleccionado.
     */
    public abstract HashMap getPromotor();

    /**
     * No hace nada.
     *
     * @param promotor El Promotor.
     */
    public abstract void setPromotor(HashMap promotor);

    /**
     * Regresa el string para calificar los eventos de acuerdo a la selecci&oacute;n en los combo
     * box de sem&aacute;foros.
     *
     * @return String.
     */
    protected String getStringEventos() {
        return new StringBuffer(getEvDoc()).append("_").append(getEvCan()).append("___").
                append(getEvLinCred()).append(getEvLinOp()).append("%").toString();
    }

    /**
     * Regresa el valor de evDoc.
     *
     * @return String.
     */
    public abstract String getEvDoc();

    /**
     * Regresa el valor de evCan.
     *
     * @return String.
     */
    public abstract String getEvCan();

    /**
     * Regresa el valor de evLinCred.
     *
     * @return String.
     */
    public abstract String getEvLinCred();

    /**
     * Regresa el valor de evLinOp.
     *
     * @return String.
     */
    public abstract String getEvLinOp();

    /**
     * Regresa el valor de modo.
     *
     * @return String.
     */
    public abstract String getModo();

    /**
     * Establece el valor de modo.
     *
     * @param modo El valor a asignar.
     */
    public abstract void setModo(String modo);

    /**
     * Obtiene la Lista de Promotores a popular en el combo de los mismos,
     * de acuerdo a las Jerarqu&iacute;as de Seguridad.
     *
     * @return List Los Promotores.
     */
    public abstract List getPromotoresList();

    /**
     * Fija la Lista de Promotores a popular en el combo de los mismos,
     * de acuerdo a las Jerarqu&iacute;as de Seguridad.
     *
     * @param listaPromotores Los Promotores.
     */
    public abstract void setPromotoresList(List listaPromotores);
}
