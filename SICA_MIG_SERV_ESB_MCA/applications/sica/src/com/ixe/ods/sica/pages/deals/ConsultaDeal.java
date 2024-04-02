/*
 * $Id: ConsultaDeal.java,v 1.31.2.2.2.1.16.1.14.1 2013/12/21 03:08:40 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.springframework.context.ApplicationContext;

import net.sf.jasperreports.engine.JRDataSource;

import com.ixe.ods.bup.model.Direccion;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.sdo.BupServiceData;
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.Keys;
import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaSiteCache;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.InfoFactura;
import com.ixe.ods.sica.model.IPlantillaIntl;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PlantillaPantalla;
import com.ixe.ods.sica.pages.Mensaje;
import com.ixe.ods.sica.pages.reportes.RepDeal;
import com.ixe.ods.tapestry.BaseGlobal;
import com.ixe.ods.tapestry.services.jasper.DataSourceProvider;
import com.ixe.treasury.dom.common.Banco;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * Clase para la Consulta de Deals.
 *
 * @author Jean C. Favila, Javier Cordova
 * @version $Revision: 1.31.2.2.2.1.16.1.14.1 $ $Date: 2013/12/21 03:08:40 $
 */
public abstract class ConsultaDeal extends AbstractConsultaDeals implements DataSourceProvider {

    /**
     * Precarga informaci&oacute;n cada vez que se accesa a la p&aacute;gina.
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
        List idsPromotores = new ArrayList();
        Integer idPromotor = visit.getUser().getIdPersona();
        int posicionPromotor = 0;
        // Verificando si se trata del Area de Promocion
        if (FacultySystem.SICA_CONS_DEAL.equals(getModo())) {
            // Obtiene subordinados incluyendo nodo raiz
            promotoresTmp = getPromotoresJerarquia();
            if (getIdGrupoTrabajo() != null) {
            	setIdGrupoTrabajo(null);
            }
        }
        else if (FacultySystem.SICA_CONS_GUARDIA.equals(getModo())) {
            if (((Visit) getVisit()).getGrupoTrabajo() != null) {
                promotoresTmp = getSicaServiceData().findPromotoresByGrupoTrabajo(
                    ((Visit) getVisit()).getGrupoTrabajo().getIdGrupoTrabajo());
            }
            else {
                Mensaje nextPage = (Mensaje) getRequestCycle().getPage("Mensaje");
                nextPage.setMensaje("No tiene asignado un grupo de trabajo");
                cycle.activate(nextPage);
                return;
            }
        }        
        else {
            // Si se trata de otra Area:
            promotoresTmp = getSicaServiceData().
                    findAllPromotoresSICA(((SupportEngine) getEngine()).getApplicationName());
        }
        // Obteniendo modo de guardias
        if (FacultySystem.SICA_CONS_GUARDIA.equals(getModo())) {
            if (((Visit) getVisit()).getGrupoTrabajo() != null) {
                setIdGrupoTrabajo(((Visit) getVisit()).getGrupoTrabajo().getIdGrupoTrabajo());
            }
            else {
                Mensaje nextPage = (Mensaje) getRequestCycle().getPage("Mensaje");
                nextPage.setMensaje("No tiene asignado un grupo de trabajo");
                cycle.activate(nextPage);
                return;
            }
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
                idsPromotores.add(promotor.getIdPersona());
            }
            setPromotor((HashMap) promotores.get(posicionPromotor));
        }
        setTipoFecha("Captura");
        setPromotoresList(promotores);
        setIdsPromotores(idsPromotores);
    }

    /**
     * Regresa true si la consulta de deals se realiza desde Promoci&oacute;n Mayoreo.
     *
     * @return boolean.
     */
    protected boolean isCanalRestringido() {
        return FacultySystem.SICA_CONS_DEAL.equals(getModo());
    }

    /**
     * Vac&iacute;a la lista de deals.
     */
    public void limpiar() {
        setDeals(new ArrayList());
    }

    /**
     * M&eacut;etodo que obtiene el DataSource para reporte del Deal
     *
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider
     * @param id La llave primaria del registro.
     * @return JRDataSource.
     */
    public JRDataSource getDataSource(String id) {
        try {
            return getComprobanteDealDataSource(Integer.valueOf(id).intValue(), null);
        }
        catch (SicaException e) {
            debug(e);
            throw new ApplicationRuntimeException(e);
        }
    }

    /**
     * Regresa false si el modo no es 'SICA_CONS_DEAL' &oacute; 'SICA_CONS_GUARDIA'. 
     * En caso contrario regresa lo que conteste la superclase.
     *
     * @return boolean.
     */
    public boolean isVerHabilitado() {
        return (FacultySystem.SICA_CONS_DEAL.equals(getModo()) ||
                FacultySystem.SICA_CONS_GUARDIA.equals(getModo())) && super.isVerHabilitado();
    }

    /**
     * Filtra la Consulta de Deals segun la Jerarquia del Promotor Logeado a la Aplicaci&oacute;n,
     * y ademas verifica que el Promotor no tenga en Lista Negra las Cuentas de la Consulta de
     * Deals.
     *
     * @param deals La lista de deals a Filtrar
     * @return List.
     */
    protected List aplicarFiltrosAdicionales(List deals) {
        List dealsTmp = new ArrayList();
        Visit visit = (Visit) getVisit();
        if (visit.isIxeDirecto()) {
            dealsTmp.addAll(deals);
            deals.clear();
            for (Iterator it1 = dealsTmp.iterator(); it1.hasNext();) {
                Map deal = (Map) it1.next();
                if (FacultySystem.SICA_ID_CANAL_IXE_DIRECTO.equals(deal.get(Keys.ID_CANAL))) {
                    deals.add(deal);
                }
            }
        }
        else if (!((Visit) getVisit()).isGuardia() &&
                FacultySystem.SICA_CONS_DEAL.equals(getModo())) {
        	dealsTmp.addAll(deals);
            deals.clear();
            //Verificando Jerarquias:
            visit.getUser().getIdUsuario();
            for (Iterator it1 = dealsTmp.iterator(); it1.hasNext();) {
                Map deal = (Map) it1.next();
                if ((deal.get(Keys.ID_PROMOTOR) != null &&
                        getIdsPromotores().contains(deal.get(Keys.ID_PROMOTOR))) ||
                        getIdsPromotores().contains(deal.get(Keys.ID_PERSONA_USUARIO))) {
                    deals.add(deal);
                }
            }
        }
        return deals;
    }

    /**
     * Crea el String de Eventos segun lo especificado en los Criterios de B&uacute;squeda
     * para poder traer los Deals correspondientes.
     *
     * @return El String de Eventos del Deal.
     */
    protected String getStringEventos() {
        StringBuffer eventos = new StringBuffer();
        //DC
        if ("CUALQUIERA".equals(getDcCombo())) {
            eventos.append("_");
        }
        else if ("NORMAL".equals(getDcCombo())) {
            eventos.append(" ");
        }
        else if ("REQUIERE DOC. ADIC.".equals(getDcCombo())) {
            eventos.append("S");
        }
        else if ("APROBADO SIN DOC. REQ.".equals(getDcCombo())) {
            eventos.append("A");
        }
        else if ("EN ESPERA DE DOC.".equals(getDcCombo())) {
            eventos.append("N");
        }
        else {
            eventos.append(getDcCombo().charAt(0));
        }
        //MD
        if ("CUALQUIERA".equals(getMdCombo())) {
            eventos.append("_");
        }
        else if ("NORMAL".equals(getMdCombo())) {
            eventos.append(" ");
        }
        else {
            eventos.append(getMdCombo().charAt(0));
        }
        //CN
        if ("CUALQUIERA".equals(getCnCombo())) {
            eventos.append("_");
        }
        else if ("NORMAL".equals(getCnCombo())) {
            eventos.append(" ");
        }
        else if ("APROBACION LIQ.".equals(getCnCombo())) {
            eventos.append("L");
        }
        else if ("APROBACION MESA CAMB.".equals(getCnCombo())) {
            eventos.append("A");
        }
        else if ("NEGACION LIQ.".equals(getCnCombo())) {
            eventos.append("N");
        }
        else if ("NEGACION MESA CAMB.".equals(getCnCombo())) {
            eventos.append("M");
        }
        else {
            eventos.append(getCnCombo().charAt(0));
        }
        //FC, BANXICO y LC
        if ("CUALQUIERA".equals(getLcCombo())) {
            eventos.append("____");
        }
        else if ("NORMAL".equals(getLcCombo())) {
            eventos.append("___ ");
        }
        else {
            eventos.append("___").append(getLcCombo().charAt(0));
        }
        //TF
        if ("CUALQUIERA".equals(getTfCombo())) {
            eventos.append("_");
        }
        else if ("NORMAL".equals(getTfCombo())) {
            eventos.append(" ");
        }
        else {
            eventos.append(getTfCombo().charAt(0));
        }
        //TO
        if ("CUALQUIERA".equals(getToCombo())) {
            eventos.append("__");
        }
        else if ("SIN TIME-OUTS".equals(getToCombo())) {
            eventos.append(" 0");
        }
        else {
            eventos.append(getToCombo().charAt(0));
        }
        //Completando el Campo Eventos_Deal
        eventos.append("%");
        return eventos.toString();
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
     * Modelo del combo de Semaforos MT.
     *
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getSemaforosLCModel() {
        return new StringPropertySelectionModel(new String[]{"CUALQUIERA", "NORMAL", "SOLICITUD",
                "APROBACION", "NEGACION"});
    }

    /**
     * Modelo del combo de Semaforos TF.
     *
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getSemaforosTFModel() {
        return new StringPropertySelectionModel(new String[]{"CUALQUIERA", "NORMAL", "SOLICITUD",
                "APROBACION", "NEGACION"});
    }

    /**
     * Modelo del combo de Semaforos DC.
     *
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getSemaforosDCModel() {
        return new StringPropertySelectionModel(new String[]{"CUALQUIERA", "NORMAL",
                "REQUIERE DOC. ADIC.", "APROBADO SIN DOC. REQ.", "EN ESPERA DE DOC."});
    }

    /**
     * Modelo del combo de Semaforos MD.
     *
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getSemaforosMDModel() {
        return new StringPropertySelectionModel(new String[]{"CUALQUIERA", "NORMAL", "SOLICITUD",
                "APROBACION", "NEGACION"});
    }

    /**
     * Modelo del combo de Semaforos CN.
     *
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getSemaforosCNModel() {
        return new StringPropertySelectionModel(new String[]{"CUALQUIERA", "NORMAL", "SOLICITUD",
                "APROBACION LIQ.", "APROBACION MESA CAMB.", "NEGACION LIQ.", "NEGACION MESA CAMB.",
                "CANCELACION"});
    }

    /**
     * Modelo del combo de Semaforos TO.
     *
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getSemaforosTOModel() {
        return new StringPropertySelectionModel(new String[]{"CUALQUIERA", "SIN TIME-OUTS"});
    }

    /**
     * Prueba para la consulta de deal.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void prueba(IRequestCycle cycle) {
        Integer idDeal = (Integer) cycle.getServiceParameters()[0];
        RepDeal nextPage = (RepDeal) cycle.getPage("RepDeal");
        nextPage.setIdDeal(idDeal.toString());
        cycle.activate("RepDeal");
    }

    /**
     * Regresa el registro de SC_INFO_FACTURA que corresponde al detalle de deal especificado.
     *
     * @param idDealDetalle El n&uacute;mero de detalle de deal.
     * @return InfoFactura.
     */
    public InfoFactura getInfoFactura(int idDealDetalle) {
        if (getCurrentInfoFactura() != null && getCurrentInfoFactura().getIdDealDetalle().intValue() == idDealDetalle) {
            return getCurrentInfoFactura();
            }
        setCurrentInfoFactura(getSicaServiceData().findInfoFactura(idDealDetalle));
        return getCurrentInfoFactura();
    }

    /**
     * Regresa el valor de lcCombo.
     *
     * @return String.
     */
    public abstract String getLcCombo();

    /**
     * Regresa el valor de tfCombo.
     *
     * @return String.
     */
    public abstract String getTfCombo();

    /**
     * Regresa el valor de dcCombo.
     *
     * @return String.
     */
    public abstract String getDcCombo();

    /**
     * Regresa el valor de mdCombo.
     *
     * @return String.
     */
    public abstract String getMdCombo();

    /**
     * Regresa el valor de cnCombo.
     *
     * @return String.
     */
    public abstract String getCnCombo();

    /**
     * Regresa el valor de toCombo.
     *
     * @return String.
     */
    public abstract String getToCombo();

    /**
     * Establece el Modo de Operaci&oacute;n de la P&aacute;gina:
     * Promoci&oacute;n u Otro.
     *
     * @param modo El Modo de Operaci&oacute;n.
     */
    public abstract void setModo(String modo);

    /**
     * Obtiene el Modo de Operaci&oacute;n de la P&aacute;gina.
     *
     * @return String el Modo de Operaci&oacute;n de la P&aacute;gina.
     */
    public abstract String getModo();

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

    /**
     * Obtiene la Lista de Ids Persona de los Promotores del combo de los mismos.
     *
     * @return List Los Ids Persona de los Promotores.
     */
    public abstract List getIdsPromotores();

    /**
     * Fija la Lista de Ids Persona de los Promotores del combo de los mismos.
     *
     * @param idsPromotores Los Ids de los Promotores.
     */
    public abstract void setIdsPromotores(List idsPromotores);

    /**
     * Fija el valor de currentInfoFactura.
     *
     * @param infoFactura El valor a asignar.
     */
    public abstract void setCurrentInfoFactura(InfoFactura infoFactura);

    /**
     * Regresa el valor de currentInfoFactura.
     *
     * @return InfoFactura.
     */
    public abstract InfoFactura getCurrentInfoFactura();

    /**
     * Constante para descPesos.
     */
    public static String descPesos = "Pesos Mexicanos";
}