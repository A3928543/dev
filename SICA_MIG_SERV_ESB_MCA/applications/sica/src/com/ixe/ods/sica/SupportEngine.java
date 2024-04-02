/*
 * $Id: SupportEngine.java,v 1.14.68.1 2020/12/01 04:53:01 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.request.RequestContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ixe.ods.seguridad.TicketHelper;
import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.seguridad.sdo.SeguridadServiceData;
import com.ixe.ods.seguridad.services.TicketService;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.sdo.impl.HibernateSicaServiceData;
import com.ixe.ods.tapestry.SimpleEngine;
import com.legosoft.tapestry.browsersniffer.BrowserInfo;

/**
 * Subclase de SimpleEngine para la aplicaci&oacute;n del SICA.
 *
 * @author Jean C. Favila, Javier Cordova (jcordova)
 * @version  $Revision: 1.14.68.1 $ $Date: 2020/12/01 04:53:01 $
 */
public class SupportEngine extends SimpleEngine {

    /**
     * Constructor por default.
     */
    public SupportEngine() {
        super();
    }

    /**
     * Realiza la verificaci&oacute;n del ticket.
     *
     * @param cycle    El cycle de la p&aacute;gina
     * @param nextPage El identificador de la p&aacute;gina donde se redirecciona
     */
    public void processTicket(IRequestCycle cycle, String nextPage) {
        RequestContext  request = cycle.getRequestContext();
        String          ticket = request.getParameter("ticket2");

        ticket = TicketHelper.desencriptarTicket(ticket,
                cycle.getRequestContext().getRequest().getRemoteAddr());
        if (ticket == null) {
            throw new PageRedirectException(BUP_MESSAGE);
        }
        try {
            ApplicationContext appContext = ((Global) getGlobal()).getApplicationContext();
            TicketService ticketService = (TicketService) appContext.getBean("ticketService");

            if (!ticketService.isTicketValido(ticket)) {
                throw new SeguridadException("El ticket para el SICA ha expirado");
            }
            setupVisit(cycle, ticket);
            throw new PageRedirectException(nextPage);
        }
        catch (SeguridadException e) {
            throw new PageRedirectException(BUP_MESSAGE);
        }
    }

    /**
     * Asigna el applicationContext al objeto Global cuando &eacute;ste a&uacute;n no ha sido
     * inicializado.
     *
     * @param context El RequestContext.
     */
    protected void setupForRequest(RequestContext context) {
        super.setupForRequest(context);
        Global global = (Global) getGlobal();
        ApplicationContext applicationContext = global.getApplicationContext();
        if (applicationContext == null) {
            applicationContext = WebApplicationContextUtils.getWebApplicationContext(
                    context.getServlet().getServletContext());
            global.setApplicationContext(applicationContext);
        }
    }

    /**
     * Regresa una nueva instancia de BrowserInfo.
     *
     * @param cycle El IRequestCycle.
     * @return BrowserInfo.
     */
    public BrowserInfo getBrowserInfo(IRequestCycle cycle) {
        return new BrowserInfo(cycle);
    }

    /**
     * Carga las facultades del usuario en el objeto Visit.
     *
     * @param cycle El IRequestCycle.
     */
    protected void onSetupVisit(IRequestCycle cycle) {
        Visit visit = (Visit) this.getVisit();
        visit.setBrowserInfo(getBrowserInfo(cycle));
        Integer idUsuario = new Integer(visit.getTicketInfo().getIdUsuario());
        Global global = (Global) getGlobal();
        SeguridadServiceData seguridadServiceData = global.getSeguridadServiceData();
        SicaServiceData sicaServiceData = global.getSicaServiceData();
        visit.setUser(seguridadServiceData.getUsuario(idUsuario));
        visit.setGrupoTrabajo(sicaServiceData.findIdGrupoTrabajoByIdUsuario(
                visit.getUser().getPersona().getIdPersona()));
        cycle.getRequestContext().getRequest().getSession().
                setAttribute(TICKET, visit.getTicket());
        List facultadesUsuario = seguridadServiceData.findFacultadesSimplesByUserAndSystem(
                visit.getUser().getIdUsuario(), getApplicationName());
        List canales = new ArrayList();
        ParametroSica psMO = (ParametroSica) sicaServiceData.find(ParametroSica.class,
                ParametroSica.MESA_OPERACION);
        for (Iterator iterator = facultadesUsuario.iterator(); iterator.hasNext();) {
            String nombreFacultad = (String) iterator.next();
            if (nombreFacultad.startsWith("SICA_CAN")) {
                Canal canal = sicaServiceData.findCanalByFacultad(nombreFacultad.trim());
                if (canal != null) {
                    canales.add(canal);
                    if (canales.size() == 1) {
                        visit.setIdCanal(canal.getIdCanal());
                        visit.setIdMesaCambio(canal.getMesaCambio().getIdMesaCambio());
                    }
                    else {
                        if (Integer.parseInt(psMO.getValor()) ==
                                canal.getMesaCambio().getIdMesaCambio()) {
                            visit.setIdCanal(canal.getIdCanal());
                            visit.setIdMesaCambio(canal.getMesaCambio().getIdMesaCambio());
                        }
                    }
                }
            }
        }
        
        HibernateSicaServiceData service =  new HibernateSicaServiceData();
        service.auditar(visit.getTicket(), cycle.getRequestContext().getRequest().getRemoteAddr(),
				null, visit.getUser(), "Login", "BITACORA", "INFO","E");
        visit.setCanales(canales);
    }

    /**
     * Obtiene el Nombre de la Aplicacion Web tal cual esta en la Base de Datos.
     *
     * @return 'SICA'.
     */
    public String getApplicationName() {
        return FacultySystem.SICA;
    }

    /**
     * Obtiene el nombre largo de la Aplicacion para mostrase en las pantallas Web.
     *
     * @return 'Sistema de Cambios'.
     */
    public String getApplicationLongName() {
        return FacultySystem.SISTEMA_DE_CAMBIOS;
    }

    /**
     * Regresa el ID del Tipo Ejecutivo del SICA.
     *
     * @return String.
     */
    public String getIdTipoEjecutivo() {
        return "EJEBAN";
    }

    /**
     * Constante para la cadena 'ticket'.
     */
    private static final String TICKET = "ticket";

    /**
     * Constante para la cadena 'bup:Message'.
     */
    private static final String BUP_MESSAGE = "bup:Message";

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 6305999402655690954L;
}
