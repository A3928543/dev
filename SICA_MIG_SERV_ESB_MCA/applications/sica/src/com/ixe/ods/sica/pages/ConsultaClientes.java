/*
 * $Id: ConsultaClientes.java,v 1.20.44.1.26.1 2017/07/29 03:17:55 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.CuentaEjecutivo;
import com.ixe.ods.bup.model.Status;
import com.ixe.ods.bup.model.UltimaModificacion;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.pages.deals.plantillas.ConsultaPlantillasCuentaIxe;
import com.ixe.ods.sica.pages.deals.plantillas.ConsultaPlantillasIntl;
import com.ixe.ods.sica.pages.deals.plantillas.ConsultaPlantillasNacionales;
import com.ixe.ods.sica.pages.deals.plantillas.ConsultaPlantillasTranNacionales;
import com.ixe.ods.sica.pages.mesa.AsignacionCtasCliente;
import com.ixe.ods.sica.pizarron.Consts;
import com.ixe.ods.sica.services.BusquedaClientesService;
import com.ixe.ods.sica.services.PersonaService;
import com.ixe.contratacion.ContratacionException;

/**
 * Clase para la Consulta de Clientes y sus Contratos SICA.
 *
 * @author Jean C. Favila, Javier Cordova (jcordova)
 * @version $Revision: 1.20.44.1.26.1 $ $Date: 2017/07/29 03:17:55 $
 */
public abstract class ConsultaClientes extends SicaPage {

    /**
     * Activa el Modo de Operaci&oacute; as&iacute; como el T&iacute;tulo del Portlet de
     * B&uacute;squedas de la P&aacute;gina cada que esta se activa.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        limpiarTodo();
        if (FacultySystem.MODO_DEAL.equals(cycle.getServiceParameters()[0])) {
        	if (isHorarioVespertino()) {
        		throw new PageRedirectException("ErrorEstado");
        	}
        	else {
            setModo(FacultySystem.MODO_DEAL);
            setTituloActionPB("Captura de Deal");
        }
        }
        else if (FacultySystem.MODO_PLANTILLAS.equals(cycle.getServiceParameters()[0])) {
            setModo(FacultySystem.MODO_PLANTILLAS);
            setTituloActionPB("Captura de Plantillas");
        }
        else if (FacultySystem.MODO_MESA_CONTROL.equals(cycle.getServiceParameters()[0])) {
            setModo(FacultySystem.MODO_MESA_CONTROL);
            setTituloActionPB("Asignaci\u00f3n de Cuentas por Cliente");
        }
        else if("SICA_CATALOGOS_PROMM".equals(cycle.getServiceParameters()[0])) {
            setModo("SICA_CATALOGOS_PROMM");
            setTituloActionPB("Alta de correos");
        }
        else {
            setModo(FacultySystem.MODO_BENEFICIARIOS);
            setTituloActionPB("Captura de Beneficiarios");
        }
        limpiar();
        setAltaRapidaDeClientesYContratos(false);
        if (getLevel() > 0) {
        	 getDelegate().record("La cuenta ha sido asignada.", null);
        }
    }

    /**
     * Define si el estado actual del sistema es Horario Vespertino.
     * 
     * @return boolean
     */
    protected boolean isHorarioVespertino() {
    	return getEstadoActual().getIdEstado() == Estado.ESTADO_OPERACION_VESPERTINA;
    }
    
    /**
     * Vac&iacute;a la lista de clientes.
     */
    public void limpiar() {
        setClientes(new ArrayList());
    }

    /**
     * Redirige a la p&aacute;gina <code>ConsultaPlantillasCuentaIxe</code> para permitir al usuario
     * insertar una nueva plantilla.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void plantillasCuentaIxe(IRequestCycle cycle) {
        ConsultaPlantillasCuentaIxe nextPage = (ConsultaPlantillasCuentaIxe) cycle.
                getPage("ConsultaPlantillasCuentaIxe");
        nextPage.setContratoSica(getSicaServiceData().findContrato(cycle.
                getServiceParameters()[Consts.NUM_1].toString()));
        nextPage.setTipoPlantilla(cycle.getServiceParameters()[Consts.NUM_0].toString());
        nextPage.setNombreCliente(cycle.getServiceParameters()[Consts.NUM_2].toString());
        nextPage.setPaginaAnterior(cycle.getServiceParameters()[Consts.NUM_3].toString());
        nextPage.setIdPersona((Integer) cycle.getServiceParameters()[Consts.NUM_4]);
        cycle.activate("ConsultaPlantillasCuentaIxe");
    }

    /**
     * Redirige a la p&aacute;gina <code>ConsultaPlantillasCuentaIxe</code> para permitir al usuario
     * insertar una nueva plantilla.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void plantillasNacionales(IRequestCycle cycle) {
        ConsultaPlantillasNacionales nextPage = (ConsultaPlantillasNacionales) cycle.
                getPage("ConsultaPlantillasNacionales");
        nextPage.setContratoSica(getSicaServiceData().findContrato(cycle.
                getServiceParameters()[Consts.NUM_1].toString()));
        nextPage.setTipoPlantilla(cycle.getServiceParameters()[Consts.NUM_0].toString());
        nextPage.setNombreCliente(cycle.getServiceParameters()[Consts.NUM_2].toString());
        nextPage.setPaginaAnterior(cycle.getServiceParameters()[Consts.NUM_3].toString());
        nextPage.setIdPersona((Integer) cycle.getServiceParameters()[Consts.NUM_4]);
        cycle.activate("ConsultaPlantillasNacionales");
    }

    /**
     * Redirige a la p&aacute;gina <code>ConsultaPlantillasIntl</code> para permitir al usuario
     * insertar una nueva plantilla.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void plantillasIntl(IRequestCycle cycle) {
        ConsultaPlantillasIntl nextPage = (ConsultaPlantillasIntl) cycle.
                getPage("ConsultaPlantillasIntl");
        nextPage.setContratoSica(getSicaServiceData().findContrato(cycle.
                getServiceParameters()[1].toString()));
        nextPage.setTipoPlantilla(cycle.getServiceParameters()[Consts.NUM_0].toString());
        nextPage.setNombreCliente(cycle.getServiceParameters()[Consts.NUM_2].toString());
        nextPage.setPaginaAnterior(cycle.getServiceParameters()[Consts.NUM_3].toString());
        nextPage.setIdPersona((Integer) cycle.getServiceParameters()[Consts.NUM_4]);
        cycle.activate("ConsultaPlantillasIntl");
    }

    /**
     * Redirige a la p&aacute;gina <code>ConsultaPlantillasTranNacionales</code> para permitir al
     * usuario insertar una nueva plantilla.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void plantillasTranNal(IRequestCycle cycle) {
        ConsultaPlantillasTranNacionales nextPage = (ConsultaPlantillasTranNacionales) cycle.
                getPage("ConsultaPlantillasTranNacionales");
        nextPage.setContratoSica(getSicaServiceData().findContrato(cycle.
                getServiceParameters()[Consts.NUM_1].toString()));
        nextPage.setTipoPlantilla(cycle.getServiceParameters()[Consts.NUM_0].toString());
        nextPage.setNombreCliente(cycle.getServiceParameters()[Consts.NUM_2].toString());
        nextPage.setPaginaAnterior(cycle.getServiceParameters()[Consts.NUM_3].toString());
        nextPage.setIdPersona((Integer) cycle.getServiceParameters()[Consts.NUM_4]);
        cycle.activate("ConsultaPlantillasTranNacionales");
    }

    /**
     * Regresa el mapa para asignar el focus al campo de texto empresasTextField.
     *
     * @return Map.
     */
    public Map getFirstTextFieldMap() {
        Map map = new HashMap();
        map.put("textField", "document.Form0.empresasTextField");
        return map;
    }

    /**
     * Realiza las operaciones de b&uacute;squeda por Clientes, o por Empresas, o por Contratos Sica
     * para los Canales de Sucursales y Mayoreo. Para este &uacute;ltimo Canal, hay dos casos,
     * cuando el ejecutivo que opera el SICA quiere consultar sus Cuentas Propias Asignadas y el
     * caso para cuando requiere consultar adem&aacute;s los Contratos Sica de sus Subordinados.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void fetch(IRequestCycle cycle) {
        List contratosSica;
        Visit visit = (Visit) getVisit();
        setAltaRapidaDeClientesYContratos(false);
        convertirAUpperCase();
        quitarEspaciosEnBlanco();
        try {
            BusquedaClientesService busquedaService = (BusquedaClientesService)
                    getApplicationContext().getBean("busquedaClientesService");
            contratosSica = busquedaService.findClientes(getRazonSocial(), getPaterno(),
                    getMaterno(), getNombre(), getNoCuenta(), getModo(), isCuentasSubordinados(),
                    visit.getUser().getIdPersona(), visit.getUser().getIdUsuario(),
                    visit.isIxeDirecto());
            if (contratosSica.isEmpty()) {
                setAltaRapidaDeClientesYContratos(true);
                throw new SicaException("No se encontr\u00f3 el Cliente, el Cliente no tiene " +
                        "Contrato SICA o el Contrato SICA est\u00e1 asignado a otro Ejecutivo");
            }
            setClientes(contratosSica);
        }
        catch (SicaException e) {
            debug(e);
            limpiar();
            getDelegate().record(e.getMessage(), null);
        }
    }

    /**
     * Convierte lo escrito en los Criterios de Busqueda a Uppercase para evitar fallas en las
     * busquedas de la Base de Datos.
     */
    public void convertirAUpperCase() {
        if (StringUtils.isNotEmpty(getPaterno())) {
            setPaterno(getPaterno().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getMaterno())) {
            setMaterno(getMaterno().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getNombre())) {
            setNombre(getNombre().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getRazonSocial())) {
            setRazonSocial(getRazonSocial().toUpperCase());
        }
    }

    /**
     * Quita los espacios en blanco en los Criterios de B&uacute;squeda para evitar fallas en las
     * busquedas en la Base de Datos.
     */
    public void quitarEspaciosEnBlanco() {
        if (StringUtils.isNotEmpty(getPaterno())) {
            setPaterno(getPaterno().trim());
        }
        if (StringUtils.isNotEmpty(getMaterno())) {
            setMaterno(getMaterno().trim());
        }
        if (StringUtils.isNotEmpty(getNombre())) {
            setNombre(getNombre().trim());
        }
        if (StringUtils.isNotEmpty(getRazonSocial())) {
            setRazonSocial(getRazonSocial().trim());
        }
        if (StringUtils.isNotEmpty(getNoCuenta())) {
            setNoCuenta(getNoCuenta().trim());
        }
    }

    /**
     * Agrega un beneficiario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void agregaBeneficiario(IRequestCycle cycle) {
        CapturaBeneficiarios captura = (CapturaBeneficiarios) cycle.
                getPage("CapturaBeneficiarios");
        captura.setContratoSica(getSicaServiceData().
                findContrato(cycle.getServiceParameters()[0].toString()));
        captura.setIdPersonaCliente(Integer.
                parseInt(cycle.getServiceParameters()[1].toString()));
        captura.setNombreCliente(cycle.getServiceParameters()[2].toString());
        captura.setPaginaAnterior(getPageName());
        cycle.activate("CapturaBeneficiarios");
    }

    /**
     * Asigna el estatus cancelado a la CuentaEjecutivo definida.
     *
     * @param cycle El ciclo de la pagina.
     */
    public void desasignarCuenta(IRequestCycle cycle) {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
    	List cta = getSicaServiceData().findCuentaEjecutivoByContratoSica(
    			((SupportEngine) getEngine()).getIdTipoEjecutivo(),
    			cycle.getServiceParameters()[0].toString());
    	if (!cta.isEmpty()) {
        	CuentaEjecutivo ce = (CuentaEjecutivo) cta.get(0);
    		try {
    			Status s = new Status();
    			s.setEstatus(STATUS_CTA_EJE_CANCE);
    			UltimaModificacion um = new UltimaModificacion();
    			um.setUsuario(((Visit) getVisit()).getUser().getClave());
    			um.setFecha(new Timestamp(new Date().getTime()));
    			ce.setUltimaModificacion(um);
    			ce.setStatus(s);
    			getSicaServiceData().update(ce);
    			setLevel(1);
    			delegate.record("La cuenta ha sido desasignada.", null);
    		}
    		catch (Exception e) {
    			setLevel(-1);
    			delegate.record("Hubo un error al intentar desasignar la cuenta.", null);
    		}
    	}
    	else {
    		setLevel(1);
    		delegate.record("La cuenta actualmente no tiene promotor asignado.", null);
    	}
    }

    /**
     * Envia al usuario a la pagina de Asignacion de Cuentas para un cliente seleccionado.
     *
     * @param cycle El ciclo de la pagina.
     */
    public void asignarCuenta(IRequestCycle cycle) {
    	try {
    		Integer idPersona = null;
    		if (cycle.getServiceParameters().length >= 3) {
    			idPersona = new Integer((cycle.getServiceParameters()[2]).toString()); 
    		}
	    	if (this.getPersonaService().isValidaInformacionGeneralPersona(idPersona, null)) {
		        AsignacionCtasCliente nextPage = (AsignacionCtasCliente) cycle.
		                getPage("AsignacionCtasCliente");
		        nextPage.setNombreCliente((String) cycle.getServiceParameters()[1]);
		        nextPage.setNoCuenta((String) cycle.getServiceParameters()[0]);
		        nextPage.activate(cycle);
	    	}
	    }
	    catch (SicaException e) {
	        debug(e);
	        String message = "Error al asignar la cuenta. " + e.getMessage();
	        getDelegate().record(message, null);
	        
	    }
    }

    /**
     * Obtiene el nombre del promotor asignado para la cuenta definida; en caso
     * de que la cuenta no tenga un promotor asignado, regresa una cadena vacia.
     *
     * @param contratoSica El numero de cuenta del contrato.
     * @return String
     */
    public String obtenerPromotorCuenta(String contratoSica) {
    	String promotor = "";
    	List ctasEjec = getSicaServiceData().findCuentaEjecutivoByContratoSica(
    			((SupportEngine) getEngine()).getIdTipoEjecutivo(), contratoSica);
    	if (!ctasEjec.isEmpty()) {
    		CuentaEjecutivo ce = (CuentaEjecutivo) ctasEjec.get(0);
    		promotor = ce.getId().getEjecutivo().getNombreCompleto();
    	}

    	return promotor;
    }
    /**
     * Obtiene la descripcion del motivo de bloqueo.
     *
     * @param idBloqueo El id del Bloqueo.
     * @return String
     */
    
    public String obtenerDescripcionBloqueo(Integer idBloqueo){
    	String descripcion = "";
    	descripcion = getSicaServiceData().obtenerDescripcionBloqueo(idBloqueo);
    	return descripcion;
    	
    }

    /**
     * Lleva el flujo a la P&aacute;gina de Alta R&aacute;pida de Clientes y Contratos SICA.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void altaRapidaDeClientes(IRequestCycle cycle) {
        AltaRapidaDeClientes alta = (AltaRapidaDeClientes) cycle.getPage("AltaRapidaDeClientes");
        alta.activate(cycle);
    }

    /**
     * Abre la p&aacute;gina AltaCorreosElectronicos para permitir al usuario registrar un nuevo
     * medio de contacto para el cliente seleccionado.
     *
     * @param cycle IRequestCycle.
     * @see com.ixe.ods.sica.pages.AltaCorreosElectronicos
     */
    public void altaCorreos(IRequestCycle cycle) {
        try {
            AltaCorreosElectronicos nextPage = (AltaCorreosElectronicos) cycle.
                    getPage("AltaCorreosElectronicos");
            nextPage.inicializar((String)cycle.getServiceParameters()[0]);
            cycle.activate(nextPage);
        }
        catch(ContratacionException e) {
            debug(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }
    }

    /**
     * Regresa el Nombre de la Facultad que tiene el Canal al que pertenece el Usuario que visita
     * la p&aacute;gina.
     *
     * @return String El Nombre de la Facultad.
     */
    public String getNombreFacultad() {
        try {
            String nombreFacultad = (getSicaServiceData().findCanal(
                    ((Visit) getVisit()).getIdCanal())).getFacultad().getNombre();
            return nombreFacultad != null ? nombreFacultad : "";
        }
        catch (SicaException e) {
            warn(e);
            return null;
        }
    }

    /**
     * Establece el Modo de operaci&oacute;n de la P&aacute;gina: MODO_DEAL
     * MODO_PLANTILLAS o MODO_MESA_CONTROL.
     *
     * @param modo El Modo de operaci&oacute;n de la P&aacute;gina.
     */
    public abstract void setModo(String modo);

    /**
     * Obtiene el modo de la pagina.
     *
     * @return String
     */
    public abstract String getModo();

    /**
     * Establece el T&iacute;tulo del Portlet de B&uacute;squedas de la P&aacute;gina de
     * acuerdo al modo de operaci&oacute;n de la misma.
     *
     * @param tituloActionPB El T&iacute;tulo del Portlet de B&uacute;squedas de la P&aacute;gina.
     */
    public abstract void setTituloActionPB(String tituloActionPB);

    /**
     * Activa la lista de Clientes encontrados de acuerdo a los criterios de B&uacute;squeda
     * preestablecidos.
     *
     * @param clientes La lista de Clientes encontrados.
     */
    public abstract void setClientes(List clientes);

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
     * Establece el valor de noCuenta.
     *
     * @param noCuenta El valor a asignar.
     */
    public abstract void setNoCuenta(String noCuenta);

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda No. de Cuenta (Contrato).
     *
     * @return String NoCuenta.
     */
    public abstract String getNoCuenta();

    /**
     * Regresa el valor de altaRapidaDeClientesYContratos.
     *
     * @return boolean Si aplica la opci&oacute;n de Alta R&aacute;pida de Clientes y Contratos
     * SICA.
     */
    public abstract boolean isAltaRapidaDeClientesYContratos();

    /**
     * Establece el valor de altaRapidaDeClientesYContratos.
     *
     * @param altaRapidaDeClientesYContratos Se establece si aplica o no la opci&oacute;n de Alta
     * R&aacute;pida de Clientes y Contratos SICA.
     */
    public abstract void setAltaRapidaDeClientesYContratos(boolean altaRapidaDeClientesYContratos);

    /**
     * Regresa el valor de cuentasSubordinados.
     *
     * @return boolean.
     */
    public abstract boolean isCuentasSubordinados();

    /**
     * Regresa el valor de nombrePromotor
     *
     * @return String
     */
    public abstract String getNombrePromotor();

    /**
     * Asigna el valor para el nombre del promotor.
     *
     * @param nombrePromotor El nombre del promotor.
     */
    public abstract void setNombrePromotor(String nombrePromotor);

    /**
     * Nivel de los mensajes al usuario: -1 Error, 0 Warning, 1 Exito.
     *
     * @param level El valor a asignar.
     */
	public abstract void setLevel(int level);

	/**
	 * Obtiene el nivel del delegate.
	 *
	 * @return int
	 */
	public abstract int getLevel();
	
	/**
     * Gets the persona service.
     *
     * @return the persona service
     */
    private PersonaService getPersonaService() {
    	return (PersonaService) getApplicationContext().getBean("personaService");
    }

	/**
	 *  Variable estatica que define el estatus cancelado para CuentaEjecutivo.
	 */
	public static final String STATUS_CTA_EJE_CANCE = "CANCE";

	/**
	 * Variable estatica que define el estatus cancelado para CuentaEjecutivo.
	 */
	public static final String STATUS_CTA_EJE_VIG = "VIG";
}
