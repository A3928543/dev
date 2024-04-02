/*
 * $Id: AprobacionLineaCredito.java,v 1.22.14.1.54.2 2016/07/21 22:41:50 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.lineacredito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.seguridad.sdo.SeguridadServiceData;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.dao.ClienteDao;
import com.ixe.ods.sica.dto.ClienteContratoDto;
import com.ixe.ods.sica.lineacredito.service.LineaCreditoService;
import com.ixe.ods.sica.lineacredito.service.TipoOperacion;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.services.SicaMail;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * Clase para la Autorizaci&oacute;n, Activaci&oacute;n y Suspensi&oacute;n de L&iacute;neas de
 * Cr&eacute;dito.
 *
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.22.14.1.54.2 $ $Date: 2016/07/21 22:41:50 $
 */
public abstract class AprobacionLineaCredito extends SicaPage {
	
	public static final Logger LOGGER = Logger.getLogger(AprobacionLineaCredito.class);
	
	
    /**
     * Se ejecuta cada que se activa la p&aacute;gina.
     *
     * Carga la lista de Promotores de los cuales de desea hacer una operaci&oacute;n
     * sobre sus L&iacute;neas de Cr&eacute;dito asignadas, de acuerdo a las Jerarqu&iacute;as
     * de Seguridad y al usuario que opera la p&aacute;gina: Direcci&oacute;n de Promoci&oacute;n,
     * o Caseta de Cr&eacute;dito.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        setModo((String) cycle.getServiceParameters()[0]);
        limpiarTodo();
        Visit visit = (Visit) getVisit();
        List promotoresTmp;
        List promotores = new ArrayList();
        List idsPromotores = new ArrayList();
        List limpiarLineasDeCredito = new ArrayList();
        setLineasCredito(limpiarLineasDeCredito);
        setEstatus("");
        // Verificando si se trata de la Direccion de Promocion
        if (FacultySystem.SICA_APROB_LINCRED_P.equals(getModo())) {
            setTitleActionPortletBorder("Departamento de Promoci&oacute;n");
            setEstatus(LineaCambio.STATUS_SOLICITUD);
            // Obtiene subordinados incluyendo nodo raiz
            promotoresTmp = getSeguridadServiceData().
                    findSubordinadosEjecutivoYEjecutivosASustituir(((SupportEngine) getEngine()).
                            getApplicationName(), visit.getUser().getIdPersona());
        }
        // Verificando si se trata del Area de Credito
        else {
            setTitleActionPortletBorder("Busqueda por Promotor");
            promotoresTmp = getSicaServiceData().
                    findAllPromotoresSICA(((SupportEngine) getEngine()).getApplicationName());
            setEstatus(LineaCambio.STATUS_APROBADA);
        }
        if (promotoresTmp.size() > 0) {
            HashMap hm = new HashMap();
            hm.put("idPersona", new Integer(0));
            hm.put("nombreCompleto", "TODOS");
            promotores.add(hm);
            for (Iterator it = promotoresTmp.iterator(); it.hasNext();) {
                hm = new HashMap();
                EmpleadoIxe promotor = (EmpleadoIxe) it.next();
                hm.put("idPersona", promotor.getIdPersona());
                hm.put("nombreCompleto", promotor.getNombreCompleto());
                promotores.add(hm);
                idsPromotores.add(promotor.getIdPersona());
            }
            setPromotor((HashMap) promotores.get(0));
        }
		setPromotoresList(promotores);
		setIdsPromotores(idsPromotores);
	}

	/**
	 * Realiza las operaciones de b&uacute;squeda de L&iacute;neas de Cr&eacute;dito
	 * por Promotor, Status, Porcentaje de Uso, Lineas Por Renovar.
     *
     * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void fetch(IRequestCycle cycle) {
        if (getDelegate().getHasErrors()) {
            return;
        }
        try {
            setLineasCredito(new ArrayList());
            // Validaciones de Criterios de Busqueda:
            if (((getLimiteInferior() > 0) && !(getLimiteSuperior() > 0)) ||
                    (!(getLimiteInferior() > 0) && (getLimiteSuperior() > 0))) {
                throw new SicaException("Debe llenar los dos L\u00edmites del Porcentaje de Uso, " +
                        "Inferior y Superior, con valores 0 \u00f3 mayores.");
            }
            //Buscando Lineas por Promotores, Status, Porcentaje de Uso, Lineas Por Renovar.
            List lineasCreditoTmp;
            ParametroSica parametro = (ParametroSica) getSicaServiceData().find(
                    ParametroSica.class, "DIAS_VENCIMIENTO_LINEA");
            Integer diasVencimientoLineas = new Integer(parametro.getValor());
            if ("TODOS".equals(getPromotor().get("nombreCompleto"))) {
                lineasCreditoTmp = getLineaCambioServiceData().findLineasCambioByEjecutivos(
                        getIdsPromotores(), getEstatus(), getLimiteInferior(), getLimiteSuperior(),
                        isLineasPorRenovar(), getFechaActual(), diasVencimientoLineas);
            }
            else {
                List ejecutivos = new ArrayList();
                ejecutivos.add(getPromotor().get("idPersona"));
                lineasCreditoTmp = getLineaCambioServiceData().findLineasCambioByEjecutivos(
                        ejecutivos, getEstatus(), getLimiteInferior(), getLimiteSuperior(),
                        isLineasPorRenovar(), getFechaActual(), diasVencimientoLineas);
            }
            if (!(lineasCreditoTmp.size() > 0)) {
                throw new SicaException("No hay l\u00edneas de cr\u00e9dito con los criterios de " +
                        "b\u00fasqueda especificados.");
            }
            else {
                setLineasCredito(lineasCreditoTmp);
            }
        }
        catch (SicaException e) {
            debug(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }
    }

    /**
     * Realiza las operaciones de b&uacute;squeda por Clientes, o por Empresas, o por Contratos
     * Sica.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void fetchClientes(IRequestCycle cycle) {
        List contratosSica = new ArrayList();
        // Validaciones de Campos:
        if ((getRazonSocial() + getNombre() + getPaterno() + getMaterno()).indexOf("%") >= 0) {
        	limpiar();
            getDelegate().record("No se permite utilizar el caracter '%' en los criterios de " +
                    "consulta.", null);
            return;
        }
        else if (StringUtils.isEmpty(getNombre().trim()) &&
                StringUtils.isEmpty(getPaterno().trim()) &&
                StringUtils.isEmpty(getMaterno().trim()) &&
                StringUtils.isEmpty(getRazonSocial().trim()) &&
                StringUtils.isEmpty(getNoCuenta().trim())) {
        	limpiar();
            getDelegate().record("Debe definir al menos un criterio de b\u00fasqueda.", null);
            return;
        }
        else if ((StringUtils.isNotEmpty(getRazonSocial().trim()) &&
                getRazonSocial().trim().length() < Num.I_3) ||
                (StringUtils.isNotEmpty(getNombre().trim()) &&
                		getNombre().trim().length() < Num.I_3) ||
                (StringUtils.isNotEmpty(getPaterno().trim()) &&
                		getPaterno().trim().length() < Num.I_3) ||
                (StringUtils.isNotEmpty(getMaterno().trim()) &&
                		getMaterno().trim().length() < Num.I_3) ||
                (StringUtils.isNotEmpty(getNoCuenta().trim()) &&
                		getNoCuenta().trim().length() < Num.I_3)) {
        	limpiar();
            getDelegate().record("Los criterios de b\u00fasqueda deben tener m\u00e1s de 2 " +
                    "caracteres.", null);
            return;
        }
        else if (StringUtils.isNotEmpty(getRazonSocial().trim()) &&
                (StringUtils.isNotEmpty(getPaterno().trim()) ||
                        StringUtils.isNotEmpty(getMaterno().trim()) ||
                        StringUtils.isNotEmpty(getNombre().trim()) ||
                        StringUtils.isNotEmpty(getNoCuenta().trim()))) {
        	limpiar();
            getDelegate().record("La b\u00fasqueda es por Personas, por Empresas, o por Contrato.",
                    null);
            return;
        }
        else if ((StringUtils.isNotEmpty(getPaterno().trim()) ||
                StringUtils.isNotEmpty(getMaterno().trim()) ||
                StringUtils.isNotEmpty(getNombre().trim())) &&
                (StringUtils.isNotEmpty(getNoCuenta().trim()) ||
                        StringUtils.isNotEmpty(getRazonSocial().trim()))) {
        	limpiar();
            getDelegate().record("La b\u00fasqueda es por Personas, por Empresas, o por Contrato.",
                    null);
            return;
        }
        else if (StringUtils.isNotEmpty(getNoCuenta().trim()) &&
                (StringUtils.isNotEmpty(getPaterno().trim()) ||
                        StringUtils.isNotEmpty(getMaterno().trim()) ||
                        StringUtils.isNotEmpty(getNombre().trim()) ||
                        StringUtils.isNotEmpty(getRazonSocial().trim()))) {
        	limpiar();
            getDelegate().record("La b\u00fasqueda es por Personas, por Empresas, o por Contrato.",
                    null);
            return;
        }
        
        try{
	        ClienteDao clienteDao = (ClienteDao) getApplicationContext().getBean("clienteDao");
	        contratosSica = clienteDao.findClientes(getRazonSocial().trim(), getPaterno().trim(),
	        		getMaterno().trim(), getNombre().trim(), getNoCuenta().trim(), true);
	        if (contratosSica.isEmpty()) {
	        	limpiar();
	            getDelegate().record("No se encontr\u00f3 el Cliente, el Cliente no tiene Contrato " +
	                    "SICA o el Contrato SICA est\u00e1 asignado a otro Ejecutivo", null);
	            return;
	        }
	        List tmp = new ArrayList();
	        for (Iterator iterator = contratosSica.iterator(); iterator.hasNext();) {
	            ClienteContratoDto dto = (ClienteContratoDto) iterator.next();
	            String noCuenta = dto.getNoCuenta();
	            Persona corporativo = getSicaServiceData().findPersonaByContratoSica(noCuenta);
	            LineaCambio linea = getLineaCambioServiceData().findLineaCambioParaCliente(corporativo.
	                    getIdPersona());
	            if (linea != null && !tmp.contains(linea)) {
	                tmp.add(linea);
	            }
	        }
	       
	        
	        if (!(tmp.size() > 0)) {
	            throw new SicaException("No hay l\u00edneas de cr\u00e9dito con los criterios de " +
	                    "b\u00fasqueda especificados.");
	        }
	        else {
	            setLineasCredito(tmp);
	        }
	    }
	    catch (SicaException e) {
	        debug(e.getMessage(), e);
	        getDelegate().record(e.getMessage(), null);
	    }
        
    }

	/**
	 * Operaci&oacute;n de Aprobaci&oacute;n de L&iacute;neas de Cr&eacute;dito.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void operacionAprobar(IRequestCycle cycle) {
		
		Integer idLineaCredito = new Integer((cycle.getServiceParameters()[0]).toString());
		cambiarEstatusLineaCredito(LineaCambio.STATUS_APROBADA, TipoOperacion.APROBACION, idLineaCredito);
	}

    /**
     * Vac&iacute;a la lista de lineas de  cr&eacute;dito.
     */
    public void limpiar() {
    	setLineasCredito(new ArrayList());
    }

	/**
	 * Operaci&oacute;n de Activaci&oacute;n de L&iacute;neas de Cr&eacute;dito.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void operacionActivar(IRequestCycle cycle) {
		Integer idLineaCredito = new Integer((cycle.getServiceParameters()[0]).toString());
		String nombreCompleto = (String)cycle.getServiceParameters()[1];
		setObservaciones("");
		LineaCambio lineaCredito = cambiarEstatusLineaCredito(LineaCambio.STATUS_ACTIVADA, "C", idLineaCredito);
		if(lineaCredito != null)
		{
			lineaCredito.setExcesoFV(new Integer(0));
			lineaCredito.setExcesoPAyTF(new Integer(0));
			getSicaServiceData().update(lineaCredito);
		}
		notificarViaEmail(lineaCredito,nombreCompleto, LineaCreditoService.AUTORIZADA);
	}

	
	/**
	 * Proceso de envio de correo al Promotor y al email de los jefes de los canales a los que pertenece el Promotor.
	 * @param lineaCredito Linea de credito con la informacion a notificar
	 * @param nombreCompletoCliente nombre completo del Cliente
	 * @param operacion LineaCreditoService.AUTORIZADA o LineaCreditoService.SUSPENDIDA 
	 */
	
	public void notificarViaEmail(LineaCambio lineaCredito,
			String nombreCompletoCliente, String operacion) {
		
		SicaMail sicaMail     = getSicaMailService();
		ClienteDao clienteDao = (ClienteDao) getApplicationContext().getBean("clienteDao");
		Collection emailList  = new ArrayList();
		String[] emails;
		Map personaContrato       = clienteDao.findPromotorAsignadoCliente(lineaCredito.getCorporativo().getIdPersona());
		Integer idPersonaPromotor = new Integer(personaContrato.get("ID_PERSONA").toString());
		Map usuario               = clienteDao.findUsuarioByIdPersona(idPersonaPromotor);
		String emailPromotor      = (String)usuario.get("EMAIL");
		String contratoCliente    = personaContrato.get("NO_CUENTA").toString();
		Integer idUsuario         = new Integer(usuario.get("ID_USUARIO").toString());
		List canales              = getCanalesUsuario(idUsuario.intValue(),getApplicationName());
		
		emailList.add(emailPromotor);
		String[] parametrosEmail = getParametrosEmailLineaCredito(lineaCredito, nombreCompletoCliente,operacion,contratoCliente,getObservaciones());
        sicaMail.setParametrosEmail(parametrosEmail);
        LOGGER.info("Notificando a Promotor: " + emailPromotor);
        for (Iterator iterator = canales.iterator(); iterator.hasNext();) {
				Canal canal = (Canal) iterator.next();
				LOGGER.info("Notificando a jefes por canal: " + canal.getIdCanal() + " jefes: "+ canal.getEmailJefe());
				try{
					emailList.addAll(Arrays.asList(canal.getEmailJefe().split("\\,")));
				}catch(NullPointerException ne){
					ne.printStackTrace();
					LOGGER.error("email jefes Nulo para canal: "+ canal.getIdCanal() + " se  envia email solo a promotor");
				}
			}
        emails =(String[])emailList.toArray(new String[emailList.size()]);
        sicaMail.sendEmail(emails);
	}

	
	/**
	 * Obtiene los canales asociados a un usuario
	 * @param idUsuario id del Usuario
	 * @param applicationName Nombre de la aplicacion
	 * @return Lista de canales
	 */
	private List getCanalesUsuario(int idUsuario, String applicationName) {
		
		SeguridadServiceData seguridadServiceData = getSeguridadServiceData();
		List facultadesUsuario = seguridadServiceData.findFacultadesSimplesByUserAndSystem(
				idUsuario, getApplicationName());
        List canales = new ArrayList();
        for (Iterator iterator = facultadesUsuario.iterator(); iterator.hasNext();) {
            String nombreFacultad = (String) iterator.next();
            if (nombreFacultad.startsWith("SICA_CAN")) {
                Canal canal = getSicaServiceData().findCanalByFacultad(nombreFacultad.trim());
                if (canal != null) {
                    canales.add(canal);
                }
            }
        }
        return canales;
	}

	private String[] getParametrosEmailLineaCredito(LineaCambio lineaCredito, String nombreCompleto, 
			String operacion,String contratoCliente, String observaciones) {
		String[] parametrosEmail = new String[7];
		parametrosEmail[0] = lineaCredito.getIdLineaCambio().toString();
		parametrosEmail[1] = nombreCompleto;
		parametrosEmail[2] = operacion;
		parametrosEmail[3] = getMoneyFormat().format(lineaCredito.getImportePAyTF().doubleValue());
		parametrosEmail[4] = getMoneyFormat().format(lineaCredito.getImporteFV().doubleValue());
		parametrosEmail[5] = contratoCliente;
		parametrosEmail[6] = "";
		if(!StringUtils.isEmpty(observaciones) && operacion.equals(LineaCreditoService.SUSPENDIDA)){
			observaciones = "MOTIVO DE RECHAZO: " + observaciones;
			parametrosEmail[6] = observaciones;
		}
		return parametrosEmail;
		}

	/**
	 * Operaci&oacute;n de Reactivaci&oacute;n de L&iacute;neas de Cr&eacute;dito.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void operacionReactivar(IRequestCycle cycle) {
		Integer idLineaCredito = new Integer((cycle.getServiceParameters()[0]).toString());
		String nombreCompleto = (String)cycle.getServiceParameters()[1];
		setObservaciones("");
		cambiarEstatusLineaCredito(LineaCambio.STATUS_ACTIVADA, "C", idLineaCredito);
		LineaCambio linea = (LineaCambio) getSicaServiceData().find(LineaCambio.class,
                idLineaCredito);
		if(linea != null)
		{
			linea.setExcesoFV(new Integer(0));
			linea.setExcesoPAyTF(new Integer(0));
			getSicaServiceData().update(linea);
		}
		notificarViaEmail(linea,nombreCompleto, LineaCreditoService.AUTORIZADA);
		if (linea.getUsoTotal().doubleValue() > 0.0) {
            getDelegate().record("Aviso: L\u00ednea de cr\u00e9dito con importe en uso, se " +
                    "deber\u00e1n liquidar las operaciones pendientes.", null);
        }
	}

	/**
	 * Operaci&oacute;n de Suspensi&oacute;n de L&iacute;neas de Cr&eacute;dito.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void operacionSuspender(IRequestCycle cycle) {
		Integer idLineaCredito        = new Integer((cycle.getServiceParameters()[0]).toString());
		CapturaObservaciones nextPage = (CapturaObservaciones) cycle.
                getPage("CapturaObservaciones");
		String nombreCompleto    = (String)cycle.getServiceParameters()[1];
		nextPage.setModo(CapturaObservaciones.MODO_LINEA_CREDITO);
		nextPage.setIdLinea(idLineaCredito);
		nextPage.setPaginaAnterior(getPageName());
		nextPage.setNombreCompletoCliente(nombreCompleto);
		nextPage.activate(cycle);
	}
	/**
	 * Operaci&oacute;n de Agregar o Quitar Excepci&oacute;n en Adeudos.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
    public void operacionExcepcionEnAdeudos(IRequestCycle cycle) {
        try {
            List lineasCredito = getLineasCredito();
            Integer idLineaCredito = new Integer((cycle.getServiceParameters()[0]).toString());

            for (Iterator it = lineasCredito.iterator(); it.hasNext();) {
                LineaCambio lineaCredito = (LineaCambio) it.next();

                if (lineaCredito.getIdLineaCambio().equals(idLineaCredito)) {
                    lineaCredito.setCasoExcepcion(!lineaCredito.isCasoExcepcion());
                    getSicaServiceData().update(lineaCredito);
                    break;
                }
            }
        }
        catch (SicaException e) {
            warn(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }
    }

    /**
	 * Operaci&oacute;n de Cancelaci&oacute;n de L&iacute;neas de Cr&eacute;dito.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void operacionCancelar(IRequestCycle cycle) {
		
		Integer idLineaCredito = new Integer((cycle.getServiceParameters()[0]).toString());
		String nombreCompleto    = (String)cycle.getServiceParameters()[1];
		CapturaObservaciones nextPage = (CapturaObservaciones) cycle.
                getPage("CapturaObservaciones");
		nextPage.setModo(CapturaObservaciones.MODO_LINEA_CREDITO_CANCELAR);
		nextPage.setIdLinea(idLineaCredito);
        nextPage.setPaginaAnterior(getPageName());
		nextPage.setNombreCompletoCliente(nombreCompleto);
        nextPage.activate(cycle);
    }

    /**
     * Apoya a los m&eacute;todos <code>operacionAprobar</code>, <code>operacionActivar</code> y
     * <code>operacionSuspender</code> para cambiar el Status de las L&iacute;neas de Cr&eacute;dito
     * y hacer un LOG de la operaci&oacute;n efectuada en la tabla <code>LineaCreditoLog</code>.
     *
     * @param estatus El Status a modificar de la L&iacute;nea de Cr&eacute;dito.
     * @param operacion La Operaci&oacute;n efectuada para guardar en el LOG.
     * @param idLineaCredito La L&iacute;nea de Cr&eacute;dito sobre la que se efectu&oacute; la
     * Operaci&oacute;n.
     */
    public LineaCambio cambiarEstatusLineaCredito(String estatus, String operacion,
                                           Integer idLineaCredito) {
        Visit visit = (Visit) getVisit();
        
        String observaciones="";
        LineaCreditoService service = getLineaCreditoService();
       
            LineaCambio lineaCredito = (LineaCambio) service.findLineaCredito(idLineaCredito);
            LOGGER.info("Inicia cambio de estado de la linea de credito: Id->" + idLineaCredito );            
            if (LineaCambio.STATUS_SUSPENDIDA.equals(estatus) ||
                    LineaCambio.STATUS_VENCIDA.equals(estatus)) {
                if (lineaCredito.getUsoTotal().doubleValue() > 0) {
                    getDelegate().record("Aviso: L\u00ednea de cr\u00e9dito con importe en uso, " +
                            "se cancela la l\u00ednea y se espera a liquidar las operaciones " +
                            "pendientes.", null);
                }
            }
            if (LineaCambio.STATUS_VENCIDA.equals(lineaCredito.getStatusLinea())) {
                lineaCredito.setVencimiento(lineaCredito.getVencimientoDefault());
            }
            if(StringUtils.isNotEmpty(getObservaciones())){
                observaciones = getObservaciones().trim(); 
                LOGGER.info("Observaciones: " + observaciones );
            }
            
            service.cambiarEstatusLineaCredito(estatus, operacion, idLineaCredito, visit.getUser(), observaciones);
            LOGGER.info("Se actualizó el estado de la Linea de credito : " + idLineaCredito );
            getLineasCredito().remove(lineaCredito);
            return lineaCredito;
        }

   

	/**
	 * Manda a llamar a la P&aacute;gina de Consulta del Historial de las L&iacute;neas de
     * Cr&eacute;dito.
	 *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void consultarHistorialLineaCredito(IRequestCycle cycle) {
        ConsultaHistorialLineaCredito nextPage = (ConsultaHistorialLineaCredito) cycle.
                getPage("ConsultaHistorialLineaCredito");

        nextPage.setIdLineaCredito(new Integer((cycle.getServiceParameters()[0]).toString()));
        nextPage.setCorporativo(cycle.getServiceParameters()[1].toString());
        nextPage.setPaginaAnterior(getPageName());
        nextPage.activate(cycle);
    }

    /**
     * Permite enviar el control a la p&aacute;gina que permite modificar el monto.
     *
     * @param cycle El objeto controlador de Tapestry que administra el ciclo
     *				de cada petici&oacute;n (request) en el servidor web.
     */
	public void modificarMonto(IRequestCycle cycle) {
		ModificarMontoLineaCredito nextPage = (ModificarMontoLineaCredito) cycle.
                getPage("ModificarMontoLineaCredito");
        nextPage.setIdCorporativo(new Integer((cycle.getServiceParameters()[0]).toString()));
        nextPage.setRazonSocialCorporativo((cycle.getServiceParameters()[1]).toString());
        nextPage.setPaginaAnterior(getPageName());
        nextPage.setContraparte(false);
        nextPage.setIdLineaCredito((cycle.getServiceParameters()[2]).toString());
        nextPage.setModoPromocion(FacultySystem.SICA_APROB_LINCRED_P.equals(getModo()));
        nextPage.activate(cycle);
	}

    /**
     * Asigna el corporativo seleccionado.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void verLineaCambios(IRequestCycle cycle) {
        Persona corporativo = new Persona();
        corporativo.setIdPersona((Integer) cycle.getServiceParameters()[0]);
        corporativo.setNombreCorto((String) cycle.getServiceParameters()[1]);
        setCorporativoSeleccionado(corporativo);
    }

    /**
     * Activa la p&aacute;gina de ConsultaUsoFechaValor.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void ver(IRequestCycle cycle) {
        ConsultaUsoFechaValor nextPage = (ConsultaUsoFechaValor) cycle.
                getPage("ConsultaUsoFechaValor");
        nextPage.configurar((Integer) cycle.getServiceParameters()[0]);
        cycle.activate(nextPage);
    }

    /**
     * Obtiene la Fecha Actual del Sistema.
     *
     * @return Date La Fecha.
     */
    public Date getFechaActual() {
    	Calendar gc = new GregorianCalendar();
        return gc.getTime();
    }

    /**
	 * Permite saber si el usuario del Sistema es la Direcci&oacute;n de Promoci&oacute;n.
	 *
	 * @return boolean True si el usuario es la Direcci&oacute;n de Promoci&oacute;n
	 */
	public boolean isModoDireccionPromocion() {
		return FacultySystem.SICA_APROB_LINCRED_P.equals(getModo());
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
     * Obtiene las L&iacute;neas de Cr&eacute;dito.
     *
     * @return List Las l&iacute;neas de Cr&eacute;dito.
     */
	public abstract List getLineasCredito();

	/**
     * Fija las L&iacute;neas de Cr&eacute;dito.
     *
     * @param lineasCredito Los datosa fijar.
     */
	public abstract void setLineasCredito(List lineasCredito);

    /**
     * Establece el Modo de Operaci&oacute;n de la P&aacute;gina:
     * Promoci&oacute;n o Cr&eacute;dito.
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
     * Obtiene el Promotor seleccionado del combo de Promotores.
     *
     * @return HasMap El Promotor seleccionado.
     */
    public abstract HashMap getPromotor();

	/**
	 * Establece el Promotor seleccionado del combo de Promotores.
	 *
	 * @param promotor El Promotor seleccionado.
	 */
	public abstract void setPromotor(HashMap promotor);

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
     * Obtiene la Lista de IDs Persona de los Promotores mostrados en el combo de los mismos,
     * de acuerdo a las Jerarqu&iacute;as de Seguridad.
     *
     * @return List Los IDs Persona de los Promotores.
     */
    public abstract List getIdsPromotores();

	/**
	 * Fija la Lista de IDs de los Promotores a popular en el combo de los mismos,
     * de acuerdo a las Jerarqu&iacute;as de Seguridad.
	 *
	 * @param idsPromotores Los Promotores.
	 */
	public abstract void setIdsPromotores(List idsPromotores);

    /**
     * Obtiene el Estatus seleccionado para la b&uacute;squeda de
     * L&iacute;neas de Cr&eacute;dito.
     *
     * @return String El Estatus seleccionado.
     */
    public abstract String getEstatus();

    /**
     * Fija el Estatus seleccionado para la b&uacute;squeda de
     * L&iacute;neas de Cr&eacute;dito.
     *
     * @param estatus El Estatus seleccionado.
     */
    public abstract void setEstatus(String estatus);

    /**
     * Obtiene, en caso de que la Caseta de Cr&eacute;dito Suspenda una L&iacute;nea de
     * Cr&eacute;dito, la Raz&oacute;n de la Suspensi&oacute;.
     *
     * @return String La Raz&oacute;n de la Suspensi&oacute;.
     */
    public abstract String getObservaciones();

    /**
     * En caso de que la Caseta de Cr&eacute;dito Suspenda una L&iacute;nea de Cr&eacute;dito,
     * guarda la Raz&oacute;n de la Suspensi&oacute;.
     *
     * @param observaciones La Raz&oacute;n de la Suspensi&oacute;.
     */
    public abstract void setObservaciones(String observaciones);

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda Porcentaje de Uso
     * L&iacute;mite Inferior.
     *
     * @return double limiteInferior.
     */
	public abstract double getLimiteInferior();

	/**
     * Obtiene lo establecido como criterio de b&uacute;squeda Porcentaje de Uso
     * L&iacute;mite Superior.
     *
     * @return double limiteSuperior.
     */
	public abstract double getLimiteSuperior();

	/**
	 * Establece que los criterios de b&uacute;squeda de L&iacute;neas de Cr&eacute;dito
	 * incluyen las L&iacute;neas de Cr&eacute;dito por Renovar o no.
	 *
	 * @param lineasPorRenovar True:Si las incluye, False:No las incluye.
	 */
	public abstract void setLineasPorRenovar(boolean lineasPorRenovar);

	/**
	 * Permite saber si los criterios de b&uacute;squeda de L&iacute;neas de Cr&eacute;dito
	 * incluyen las L&iacute;neas de Cr&eacute;dito por Renovar.
	 *
	 * @return boolean Si incluye o no las L&iacute;neas de Cr&eacute;dito por Renovar.
	 */
	public abstract boolean isLineasPorRenovar();

	/**
     * Obtiene el T&iacute;tulo a mostrar en el componente de B&uacute;squeda,
     * dependiendo si se entr&oacute; del men&uacute; del Departamento de Promoci&oacute;n
     * o del Departamento de Cr&eacute;dito.
     *
     * @return String El T&iacute;tulo.
     */
    public abstract String getTitleActionPortletBorder();

    /**
     * Fija el T&iacute;tulo a mostrar en el componente de B&uacute;squeda,
     * dependiendo si se entr&oacute; del men&uacute; del Departamento de Promoci&oacute;n
     * o del Departamento de Cr&eacute;dito.
     *
     * @param titleActionPortletBorder El T&iacute;tulo.
     */
    public abstract void setTitleActionPortletBorder(String titleActionPortletBorder);

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda Nombre.
     *
     * @return String Nombre.
     */
    public abstract String getNombre();

    /**
     * Fija el valor de nombre.
     *
     * @param nombre El valor a asignar.
     */
    public abstract void setNombre(String nombre);

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda Apellido Paterno.
     *
     * @return String Paterno.
     */
    public abstract String getPaterno();

    /**
     * Fija el valor de paterno.
     *
     * @param paterno El valor a asignar.
     */
    public abstract void setPaterno(String paterno);

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda Apellido Materno.
     *
     * @return String Materno.
     */
    public abstract String getMaterno();

    /**
     * Fija el valor de materno.
     *
     * @param materno El valor a asignar.
     */
    public abstract void setMaterno(String materno);

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda Raz&oacute;n Social.
     *
     * @return String RazonSocial.
     */
    public abstract String getRazonSocial();

    /**
     * Fija el valor de razonSocial.
     *
     * @param razonSocial El valor a asignar.
     */
    public abstract void setRazonSocial(String razonSocial);

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda No. de Cuenta (Contrato).
     *
     * @return String NoCuenta.
     */
    public abstract String getNoCuenta();

    /**
     * Establece el valor de corporativoSeleccionado.
     *
     * @param corporativoSeleccionado El valor a asignar.
     */
    public abstract void setCorporativoSeleccionado(Persona corporativoSeleccionado);

	
}