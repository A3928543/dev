/*
 * $Id: SicaPage.java,v 1.29.20.1.6.1.22.1.24.3.6.1 2020/12/01 04:53:01 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.valid.IValidationDelegate;
import org.springframework.context.ApplicationContext;

import com.ixe.ods.bup.model.ClaveReferenciaPersona;
import com.ixe.ods.bup.sdo.BupServiceData;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.seguridad.sdo.SeguridadServiceData;
import com.ixe.ods.seguridad.services.TicketService;
import com.ixe.ods.sica.Global;
import com.ixe.ods.sica.Keys;
import com.ixe.ods.sica.PlantillaPantallaCache;
import com.ixe.ods.sica.SecurityService;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaSiteCache;
import com.ixe.ods.sica.SicaWorkFlowService;
import com.ixe.ods.sica.SiteService;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.dao.JerarquiaDao;
import com.ixe.ods.sica.lineacredito.service.LineaCreditoService;
import com.ixe.ods.sica.lineacredito.service.dto.MensajeMailLineaCreditoDto;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.PlantillaPantalla;
import com.ixe.ods.sica.sdo.LineaCambioServiceData;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.sdo.ReportesServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.sdo.WorkFlowServiceData;
import com.ixe.ods.sica.services.DealService;
import com.ixe.ods.sica.services.EsbService;
import com.ixe.ods.sica.services.FormasPagoLiqService;
import com.ixe.ods.sica.services.GeneralMailSender;
import com.ixe.ods.sica.services.SicaMail;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.ixe.treasury.dom.common.Pais;
import com.legosoft.tapestry.hibernate.SupportPage;

/**
 * Superclase de todas las p&aacute;ginas que componen el SICA. Implementa la l&oacute;gica de
 * validaci&oacute;n de estados del sistema y define m&eacute;todos de conveniencia para el acceso a
 * los ServiceData y los servicios del SITE (Sistema de Tesorer&iacute;a).
 *
 * @author Jean C. Favila, Javier Cordova (jcordova)
 * @version  $Revision: 1.29.20.1.6.1.22.1.24.3.6.1 $ $Date: 2020/12/01 04:53:01 $
 */
public class SicaPage extends SupportPage {

    /**
     * Constructor por default.
     */
    public SicaPage() {
        super();
    }

    /**
     * Valida la caducidad del ticket y registra una nueva actividad para el ticket de la
     * sesi&oacute;n, utilizando el bean de ticketService. Llama a
     * <code>revisarEstadoActual()</code> para validar el estado actual del sistema.
     *
     * @see #revisarEstadoActual().
     * @see TicketService#isTicketValido(String).
     * @param cycle El IRequestCycle.
     */
    public void activate(IRequestCycle cycle) {
        try {
            super.activate(cycle);
            if (!((TicketService) getApplicationContext().getBean("ticketService")).
                    isTicketValido(getTicket())) {
                throw new ApplicationRuntimeException("El ticket expir\u00f3.");
            }
            SecurityService secService = (SecurityService) getApplicationContext().
                    getBean("securityService");
            if (cycle.getServiceParameters() != null && cycle.getServiceParameters().length > 0 &&
                    cycle.getServiceParameters()[0].toString().startsWith("SICA_")) {
                secService.autorizaFacultad(getTicket(),
                        getRequestCycle().getRequestContext().getRequest().getRemoteAddr(),
                        cycle.getServiceParameters()[0].toString());
            }
            try {
                refrescarEstadoActual();
                revisarEstadoActual();
            }
            catch (SicaException e) {
                throw new RuntimeException(e);
            }
        }
        catch (SeguridadException e) {
            debug(e.getMessage(), e);
            throw new ApplicationRuntimeException("Error al acceder al servicio de tickets.", e);
        }
    }

    /**
     * Genera un registro de auditor&iacute;a con los datos especificados.
     *
     * @param idDeal El n&uacute;mero de deal de la operaci&oacute;n (opcional).
     * @param tipoOperacion El tipo de Log, seg&uacute;n las constantes de LogAuditoria.
     * @param datosAdicionales Una cadena con descripci&oacute;n adicional de la operaci&oacute;n.
     * @see com.ixe.ods.sica.sdo.SicaServiceData#auditar(String, String, Integer,
     *          com.ixe.ods.seguridad.model.IUsuario, String, String).
     * @see com.ixe.ods.sica.model.LogAuditoria
     */
    protected void auditar(Integer idDeal, String tipoOperacion, String datosAdicionales,
    		String tipoEvento, String ind) {
    	
        String ip = getRequestCycle().getRequestContext().getRequest().getRemoteAddr();
        IUsuario usuario = ((Visit) getVisit()).getUser();
        try {
            getSicaServiceData().auditar(getTicket(), ip, idDeal, usuario, tipoOperacion,
                    datosAdicionales, tipoEvento, ind);
        }
        catch (Exception e) {
            warn("Error al guardar la auditor\u00eda t: " + getTicket() + " ip: " + ip +
                    " idDeal: " + idDeal + " tipoOp: " + tipoOperacion + " idUsuario: " +
                    usuario.getIdUsuario() + " datos: " + datosAdicionales, e);
        }
    }

    /**
     * Recorre el arreglo de p&aacute;ginas visitadas por el usuario durante la sesi&oacute;n actual
     * y utiliza forgetPage() del engine de tapestry para limpiar sus variables persistentes.
     */
    protected void limpiarTodo() {
        try {
            List pags = new ArrayList(((SupportEngine) getEngine()).getActivePageNames());
            for (Iterator it = pags.iterator(); it.hasNext();) {
                String pagina = (String) it.next();
                if (!getPageName().equals(pagina)) {
                    if (_logger.isDebugEnabled()) {
                        _logger.debug("Limpiando " + pagina);
                    }
                    getEngine().forgetPage(pagina);
                }
            }
        }
        catch (Exception e) {
            debug(e.getMessage(), e);
        }
    }

    /**
     * Obtiene los Tipos de Liquidaci&oacute;n.
     *
     * @param formasPagoLiq La lista de formas de liquidaci&oacute;n.
     * @return String[].
     */
    protected String[] getTiposLiquidacion(List formasPagoLiq) {
        List clavesTiposLiqTmp = new ArrayList();
        for (Iterator it = formasPagoLiq.iterator(); it.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            if (!clavesTiposLiqTmp.contains(fpl.getNombreTipoLiquidacion().trim())) {
                clavesTiposLiqTmp.add(fpl.getNombreTipoLiquidacion().trim());
            }
        }
        String[] clavesTiposLiq = new String[clavesTiposLiqTmp.size() + 1];
        clavesTiposLiq[0] = "";
        int j = 1;
        for (Iterator i = clavesTiposLiqTmp.iterator(); i.hasNext(); j++) {
            clavesTiposLiq[j] = (String) i.next();
        }
        return clavesTiposLiq;
    }

    /**
     * Realiza la validaci&oacute;n de los estados del sistema. Si el estado actual no se encuentra
     * entre los que regresa <code>getEstadosValidos()</code>, se muestra un mensaje al usuario
     * indicando que no se puede utilizar la funcionalidad de esta p&aacute;gina en ese estado del
     * sistema.
     *
     * Los estados de operaci&oacute;n normal y restringida tampoco son v&aacute;lidos para el
     * promotor de guardia.
     *
     * @see #getEstadosValidos().
     */
    protected void revisarEstadoActual() {
        int[] estadosValidos = getEstadosValidos();
        boolean valido = false;
        for (int i = 0; i < estadosValidos.length && !valido; i++) {
            if (_estadoActual.getIdEstado() == estadosValidos[i]) {
                valido = true;
            }
        }
        if (((Visit) getVisit()).isGuardia() &&
                _estadoActual.getIdEstado() == Estado.ESTADO_OPERACION_NORMAL) {
            valido = false;
        }
        if (!valido) {
        	throw new PageRedirectException("ErrorEstado");
        }
    }

    /**
     * Regresa por default el estado de operacion normal, restringida y vespertina. Las subclases
     * deben sobre escribir para indicar estados adicionales en los que &eacute;stas son
     * v&aacute;lidas.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[]{Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA,
                Estado.ESTADO_OPERACION_VESPERTINA, Estado.ESTADO_FIN_LIQUIDACIONES,
                Estado.ESTADO_GENERACION_CONTABLE};
    }

    /**
     * Regresa el estado actual del sistema.
     *
     * @return Estado.
     */
    public Estado getEstadoActual() {
        if (_estadoActual == null) {
            try {
                refrescarEstadoActual();
            }
            catch (SicaException e) {
                if (_logger.isDebugEnabled()) {
                    _logger.debug(e);
                }
            }
        }
        return _estadoActual;
    }

    /**
     * Actualiza el estado del sistema con el estado actual.
     *
     * @throws SicaException Si no se encuentra el estado actual del sistema.
     */
    protected void refrescarEstadoActual() throws SicaException {
        _estadoActual = getSicaServiceData().findEstadoSistemaActual();
    }

    /**
     * Regresa la clave para pagos referenciados de la persona especificada.
     *
     * @param idPersona El n&uacute;mero de persona a consultar.
     * @return String.
     */
    protected String getClaveReferencia(Integer idPersona) {
        if (idPersona == null) {
            return "";
        }
        List crps = getBupServiceData().findClavesReferenciaPersona(idPersona.intValue());
        for (Iterator it = crps.iterator(); it.hasNext();) {
            ClaveReferenciaPersona crp = (ClaveReferenciaPersona) it.next();
            if ("ACTIVA".equals(crp.getStatus()) ||
                    crp.getIdPersona() == crp.getIdPersonaAnterior()) {
                return crp.getClaveReferencia();
            }
        }
        if (crps.isEmpty()) {
            return "";
        }
        else {
            return ((ClaveReferenciaPersona) crps.get(0)).getClaveReferencia();
        }
    }

    /**
     * Regresa el applicationContext del objeto Global de la aplicaci&oacute;n.
     *
     * @return ApplicationContext.
     */
    public ApplicationContext getApplicationContext() {
        return ((Global) getGlobal()).getApplicationContext();
    }

    /**
     * Regresa una referencia al bean dealService del applicationContext de spring.
     *
     * @return DealService.
     */
    public DealService getDealService() {
        return (DealService) getApplicationContext().getBean("dealService");
    }

    /**
     * Regresa el ticket de la sesi&oacute;n del usuario.
     *
     * @return String.
     */
    public String getTicket() {
        return ((Visit) getVisit()).getTicket();
    }

    /**
     * Regresa el bean de SicaServiceData que se encuentra declarado en el applicationContext.xml.
     *
     * @return SicaServiceData.
     */
    public SicaServiceData getSicaServiceData() {
        return ((Global) getGlobal()).getSicaServiceData();
    }

    /**
     * Regresa la referencia al bean lineasCambioServiceData configurado en el applicationContext de
     * spring.
     *
     * @return LineaCambioServiceData.
     */
    public LineaCambioServiceData getLineaCambioServiceData() {
        return (LineaCambioServiceData) getApplicationContext().
                        getBean("lineasCambioServiceData");
    }
    
    /**
     * Regresa la referencia al bean lineasCambio BaseTransactionProxy configurado en el applicationContext de
     * spring.
     *
     * @return lineasCambioOptimisticServiceData.
     */
    public LineaCambioServiceData getLineaCambioOptimistic() {
        return (LineaCambioServiceData) getApplicationContext().
                        getBean("lineasCambioOptimisticServiceData");
    }
    
    /**
     * Regresa la referencia al bean lineaCreditoService configurado en el applicationContext de
     * spring.
     *
     * @return LineaCambioService.
     */
    public LineaCreditoService getLineaCreditoService() {
        return (LineaCreditoService) getApplicationContext().
                        getBean(LineaCreditoService.LINEA_CREDITO_SERVICE);
    }
    
    /**
     * Regresa la referencia al bean SicaMail configurado en el applicationContext de
     * spring.
     *
     * @return SicaMail.
     */
    public SicaMail getSicaMailService() {
        return (SicaMail) getApplicationContext().
                        getBean(SicaMail.SICA_MAIL_SERVICE);
    }
    
    
    /**
     * Regresa el bean de SeguridadServiceData que se encuentra declarado en el
     * applicationContext.xml.
     *
     * @return SeguridadServiceData.
     */
    public SeguridadServiceData getSeguridadServiceData() {
        return ((Global) getGlobal()).getSeguridadServiceData();
    }

    /**
     * Regresa el bean de BupServiceData que se encuentra declarado en el applicationContext.xml.
     *
     * @return BupServiceData.
     */
    public BupServiceData getBupServiceData() {
        return (BupServiceData) getApplicationContext().getBean("bupServiceData");
    }

    public MensajeMailLineaCreditoDto getMensajeMailLineaCredito()
    {
    	return (MensajeMailLineaCreditoDto)getApplicationContext().getBean("mensajesLineaCredito");
    }
    
    public GeneralMailSender getGeneralMailSender()
    {
    	GeneralMailSender gms = (GeneralMailSender)getApplicationContext().getBean("generalMailSender");
    	return gms;
    }
    
    public int obtenerTrimestre(Date fechaSugerida)
	{
    	int mes, trimestre;
		Calendar fecha = Calendar.getInstance();
		
		if(fechaSugerida != null)
			fecha.setTime(fechaSugerida);
		
		mes = fecha.get(Calendar.MONTH);
		trimestre = (mes/3) + 1;
		
		return trimestre;
	}
    
    public Calendar obtenerFechaInicialTrimestre(int trimestre)
	{
		Calendar fechaTrimestreSolicitado = Calendar.getInstance();
		int mes = 0;
		switch(trimestre)
		{
			case 1:
				mes = Calendar.JANUARY;
			break;
			case 2:
				mes = Calendar.APRIL;
			break;
			case 3:
				mes = Calendar.JULY;
			break;
			case 4:
				mes = Calendar.OCTOBER;
			break;
		}
		
		fechaTrimestreSolicitado.set(Calendar.DAY_OF_MONTH, 1);
		fechaTrimestreSolicitado.set(Calendar.MONTH, mes);
		fechaTrimestreSolicitado.set(Calendar.HOUR_OF_DAY, 0);
		fechaTrimestreSolicitado.set(Calendar.MINUTE, 0);
		fechaTrimestreSolicitado.set(Calendar.SECOND, 0);
		fechaTrimestreSolicitado.set(Calendar.MILLISECOND, 0);
		
		return fechaTrimestreSolicitado;
	}
    
    /**
     * Regresa el formateador para tipos de cambio que se encuentra declarado en el
     * applicationContext.xml.
     *
     * @return DecimalFormat.
     */
    public DecimalFormat getCurrencyFormat() {
        return (DecimalFormat) getApplicationContext().getBean("currencyFormat");
    }

    /**
     * Regresa el formateador con 6 decimales para tipos de cambio que se encuentra declarado en el
     * applicationContext.xml.
     *
     * @return DecimalFormat.
     */
    public DecimalFormat getCurrencyFormatSix() {
        return (DecimalFormat) getApplicationContext().getBean("currencyFormatSix");
    }

    /**
     * Regresa el formateador para fechas (dd-MM-yyyy) que se encuentra declarado en el
     * applicationContext.xml.
     *
     * @return SimpleDateFormat.
     */
    public SimpleDateFormat getDateFormat() {
        return (SimpleDateFormat) getApplicationContext().getBean("dateFormat");
    }

    /**
     * Regresa el formateador para cantidades monetarias que se encuentra declarado en el
     * applicationContext.xml.
     *
     * @return DecimalFormat.
     */
    public DecimalFormat getMoneyFormat() {
        return (DecimalFormat) getApplicationContext().getBean("moneyFormat");
    }

    /**
     * Regresa el formateador para fechas (dd/MM/YYYY HH:mm) que se encuentra declarado en el
     * applicationContext.xml.
     *
     * @return SimpleDateFormat.
     */
    public SimpleDateFormat getFullDateFormat() {
        return (SimpleDateFormat) getApplicationContext().getBean("fullDateFormat");
    }
    
    /**
     * Regresa el formateador para horas, minutos y segundos (HH:mm:ss) que se encuentra 
     * declarado en el applicationContext.xml.
     *
     * @return SimpleDateFormat.
     */
    public SimpleDateFormat getHmsFormat() {
        return (SimpleDateFormat) getApplicationContext().getBean("hmsFormat");
    }

    /**
     * En el caso de que una cadena venga nula pinta un cadena vacia (defaultString "") y si no la misma cadena 
     * @param cadena
     * @return La cadena 
     */
    public String getDefaultString(String cadena){
    	return StringUtils.defaultString(cadena, "");
    }
    
    
    /**
     * En el caso de que el monto venga nulo se pinta cero y si no entonces pinta el calor que traiga
     * @param numero monto a validar
     * @return el numero
     */
    public BigDecimal getDefaultBigDecimal(BigDecimal numero){
    	if (numero == null){
    		return new BigDecimal(0);
    	}
    	return numero;
    }
    
    /**
     * Regresa el bean de PizarronServiceData que se encuentra declarado en el
     * applicationContext.xml.
     *
     * @return PizarronServiceData.
     */
    public PizarronServiceData getPizarronServiceData() {
        return (PizarronServiceData) getApplicationContext().getBean("pizarronServiceData");
    }

    /**
     * Regresa el bean de SiteService que se encuentra declarado en el applicationContext.xml.
     *
     * @return SiteService
     */
    public SiteService getSiteService() {
        return (SiteService) getApplicationContext().getBean("siteService");
    }

    /**
     * Regresa el bean de SicaWorkFlowService que se encuentra declarado en el
     * sicaApplicationContext.xml.
     *
     * @return SicaWorkFlowService.
     */
    public SicaWorkFlowService getSicaWorkFlowService() {
        return (SicaWorkFlowService) getApplicationContext().getBean("sicaWorkFlowService");
    }
    
    /**
     * Regresa el bean de EsbService que se encuentra declarado en el
     * sicaApplicationContext.xml.
     *
     * @return EsbService.
     */
    public EsbService getSicaEsbService() {
        return (EsbService) getApplicationContext().getBean("sicaEsbService");
    }

    /**
     * Regresa el bean de WorkFlowServiceData que se encuentra declarado en el
     * applicationContext.xml.
     *
     * @return WorkFlowServiceData.
     */
    public WorkFlowServiceData getWorkFlowServiceData() {
        return (WorkFlowServiceData) getApplicationContext().getBean("sicaWorkFlowServiceData");
    }

    /**
     * Regresa el nombre de la aplicaci&oacute;n que contiene el SupportEngine.
     *
     * @return String.
     */
    public String getApplicationName() {
        return ((SupportEngine) getEngine()).getApplicationName();
    }

    /**
     * Obtiene la fecha de operacion del sistema.
     *
     * @return Date La fecha de operacion del sistema.
     */
    public Date getFechaOperacion() {
        return ((PizarronServiceData) getApplicationContext().getBean("pizarronServiceData")).
                getFechaOperacion();
    }

    /**
     * Obtiene la fecha TOM del sistema.
     *
     * @return Date La fecha TOM del sistema.
     */
    public Date getFechaTOM() {
        return ((PizarronServiceData) getApplicationContext().getBean("pizarronServiceData")).
                getFechaTOM();
    }

    /**
     * Obtiene la fecha SPOT del sistema.
     *
     * @return Date La fecha SPOT del sistema.
     */
    public Date getFechaSPOT() {
        return ((PizarronServiceData) getApplicationContext().getBean("pizarronServiceData")).
                getFechaSPOT();
    }

    /**
     * Obtiene la fecha 72HR del sistema.
     *
     * @return Date La fecha 72HR del sistema.
     */
    public Date getFecha72HR() {
        return ((PizarronServiceData) getApplicationContext().getBean("pizarronServiceData")).
                getFecha72HR();
    }

    /**
     * Obtiene la fecha VFUT del sistema.
     *
     * @return Date La fecha VFUT del sistema.
     */
    public Date getFechaVFUT() {
        return ((PizarronServiceData) getApplicationContext().getBean("pizarronServiceData")).
                getFechaVFUT();
    }

    /**
     * Regresa una referencia al bean formasPagoLiqService para realizar operaciones con
     * FormasPagoLiq.
     *
     * @return FormasPagoLiqService.
     */
    public FormasPagoLiqService getFormasPagoLiqService() {
        return (FormasPagoLiqService) getApplicationContext().getBean("formasPagoLiqService");
    }

    /**
     * Regresa las formasTipoLiq disponibles.
     *
     * @return List.
     * @see com.ixe.ods.sica.services.FormasPagoLiqService#getFormasTiposLiq(String).
     */
    public List getFormasTiposLiq() {
        return getFormasPagoLiqService().getFormasTiposLiq(getTicket());
    }

    /**
     * Regresa las formaPagoLiq para el mnem&oacute;nico especificado.
     *
     * @param mnemonico El mnem&oacute;nico a encontrar.
     * @return FormaPagoLiq.
     * @see com.ixe.ods.sica.services.FormasPagoLiqService#getFormaPagoLiq(String, String).
     */
    public FormaPagoLiq getFormaPagoLiq(String mnemonico) {
        return getFormasPagoLiqService().getFormaPagoLiq(getTicket(), mnemonico);
    }

    /**
     * Regresa el pais que tiene la clave ISO especificada.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param claveISO La clave ISO a buscar.
     * @return Pais.
     * @see com.ixe.ods.sica.SicaSiteCache#getPaisXClaveISO(String, String).
     */
    public Pais getPaisXClaveISO(String ticket, String claveISO) {
        return ((SicaSiteCache) getApplicationContext().getBean("sicaSiteCache")).
                getPaisXClaveISO(ticket, claveISO);
    }

    /**
     * Regresa la PlantillaPantalla que corresponde al mnem&oacute;nico especificado.
     *
     * @param mnemonico El Mnem&oacute;nico a buscar.
     * @return PlantillaPantalla.
     */
    public PlantillaPantalla getPlantillaPantalla(String mnemonico) {
        return ((PlantillaPantallaCache) getApplicationContext().getBean("plantillaPantallaCache")).
                getPlantillaPantalla(mnemonico);
    }

    /**
     * Regresa el delegate de la p&aacute;gina.
     *
     * @return IValidationDelegate.
     */
    public IValidationDelegate getDelegate() {
        return (IValidationDelegate) getBeans().getBean(Keys.DELEGATE);
    }

    /**
     * Regresa la lista de EmpleadosIxe de la jerarqu&iacute;a en la que se encuentra el usuario
     * loggeado al sistema.
     *
     * @return List.
     */
    public List getPromotoresJerarquia() {
        JerarquiaDao jerarquiaDao = (JerarquiaDao) getApplicationContext().getBean("jerarquiaDao");
        return jerarquiaDao.findPromotoresJerarquia(((Visit) getVisit()).getUser().getIdPersona());
    }

    /**
     * Obtiene una referencia al Bean de Servicios de Reporteo.
     *
     * @return ReportesServiceData El Bean de Servicios de Reporteo.
     */
	public ReportesServiceData getReportesServiceData() {
        return (ReportesServiceData) getApplicationContext().getBean("reportesServiceData");
    }
	
	/**
	 * Obtiene el formato de fecha y hora
	 */
	 public SimpleDateFormat getDateTimeFormat(){
	   	return DATE_TIME_FORMAT;
	 }

    /**
     * Actualiza el valor de la propiedad 'descargando' del Visit Object.
     *
     * @param descargando El valor a asignar.
     */
    public void setDescargando(boolean descargando) {
        ((Visit) getVisit()).setDescargando(descargando);
    }
	
    /**
     * Si el logger tiene habilitado la opci&oacute;n de DEBUG, hace el debug del objeto.
     *
     * @param o El objeto a loggear.
     */
    public void debug(Object o) {
        if (_logger.isDebugEnabled()) {
            _logger.debug(o);
        }
    }

    /**
     * Si el logger tiene habilitado la opci&oacute;n de DEBUG, hace el debug del objeto.
     *
     * @param o El objeto a loggear.
     * @param t El objeto throwable a loggear.
     */
    public void debug(Object o, Throwable t) {
        if (_logger.isDebugEnabled()) {
            _logger.debug(o, t);
        }
    }
    
    /**
     * Si el logger tiene habilitado la opci&oacute;n de WARN, hace el warn del objeto.
     *
     * @param o El objeto a loggear.
     */
    public void warn(Object o) {
        if (_logger.isWarnEnabled()) {
            _logger.warn(o);
        }
    }

    /**
     * Si el logger tiene habilitado la opci&oacute;n de WARN, hace el warn del objeto.
     *
     * @param o El objeto a loggear.
     * @param t El objeto throwable a loggear.
     */
    public void warn(Object o, Throwable t) {
        if (_logger.isWarnEnabled()) {
            _logger.warn(o, t);
        }
    }
    
    /*
     * Agregu￩ m￩todo para registrar en log los errores
     * */
    public void error(Object o, Throwable t){
    	if(_logger.isErrorEnabled()){
    		_logger.error(o, t);
    	}
    }
    
    /**
     * El estado actual del sistema.
     */
    private Estado _estadoActual;

    /**
     * El objeto para logging.
     */
    protected final transient Log _logger = LogFactory.getLog(getClass());

    /**
     * Constante con el formato de horas y minutos.
     */
    public static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm");
    
    /**
     * Constante con el formato de hora y fecha
     */
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyy HH:mm");
}