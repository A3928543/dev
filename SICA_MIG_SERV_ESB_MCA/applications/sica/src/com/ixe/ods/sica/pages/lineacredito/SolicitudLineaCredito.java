/*
 * $Id: SolicitudLineaCredito.java,v 1.20 2009/08/03 22:06:42 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.lineacredito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.bup.model.PersonaMoral;
import com.ixe.ods.seguridad.model.IPerfil;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.dto.ClienteContratoDto;
import com.ixe.ods.sica.dao.ClienteDao;
import com.ixe.ods.sica.pages.SicaPage;

/**
 * Clase para la Consulta de Corporativos, Contrapartes y Personas F&iacute;sicas
 * para la Solicitud de L&iacute;neas de Cr&eacute;dito.
 *
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.20 $ $Date: 2009/08/03 22:06:42 $
 */
public abstract class SolicitudLineaCredito extends SicaPage {

	/**
     * Se ejecuta cada que se activa la p&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
    	super.activate(cycle);
    	setModo((String) cycle.getServiceParameters()[0]);
    	if (FacultySystem.MODO_DIRECCION_PROM.equals(getModo())) {
    		setTitleActionPortletBorder("Departamento de Promoci&oacute;n");
    	}
    	else if (FacultySystem.MODO_DIRECCION_MC.equals(getModo())) {
    		setTitleActionPortletBorder("Mesa de Cambios");
    	}
    	List limpiarPantalla = new ArrayList();
    	setCorporativos(limpiarPantalla);
    }

    /**
     * Sirve para colocar el focus de la p&aacute;gina cuando se carga
     * en el Campo de Texto Deseado.
     *
     * @return Map
     */
    public Map getFirstTextFieldMap() {
        Map map = new HashMap();
        map.put("textField", "document.Form0.razonSocialTextField");
        return map;
    }

    /**
	 * Realiza las operaciones de b&uacute;squeda.
	 *
	 * @param cycle El IRequestCycle.
	 */
	public void fetch(IRequestCycle cycle) {
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
		List limpiarPantalla = new ArrayList();
		List contratosSica;
		List corporativos = new ArrayList();
		Visit visit = (Visit) getVisit();
		convertirAUpperCase();
		if (!isModoMesaCambio()){
            //Validaciones de Campos:
            if ((getRazonSocial() + getNombre() + getPaterno() + getMaterno()).indexOf("%") >= 0) {
                setCorporativos(limpiarPantalla);
                delegate.record("No se permite utilizar el caracter '%' en los criterios de "
                        + "consulta.", null);
                return;
            }
            else if (StringUtils.isEmpty(getNombre()) && StringUtils.isEmpty(getPaterno()) &&
                    StringUtils.isEmpty(getMaterno()) && StringUtils.isEmpty(getRazonSocial()) &&
                    StringUtils.isEmpty(getNoCuenta())) {
                setCorporativos(limpiarPantalla);
                delegate.record("Debe definir al menos un criterio de b\u00fasqueda.", null);
                return;
            }
            else if ((StringUtils.isNotEmpty(getRazonSocial()) && getRazonSocial().length() < 3)
                    || (StringUtils.isNotEmpty(getNombre()) && getNombre().length() < 3)
                    || (StringUtils.isNotEmpty(getPaterno()) && getPaterno().length() < 3)
                    || (StringUtils.isNotEmpty(getMaterno()) && getMaterno().length() < 3)
                    || (StringUtils.isNotEmpty(getNoCuenta()) && getNoCuenta().length() < 3)) {
                setCorporativos(limpiarPantalla);
                delegate.record("Los criterios de b\u00fasqueda deben tener m\u00e1s de 2 "
                        + "caracteres.", null);
                return;
            }
            else if ((StringUtils.isNotEmpty(getPaterno()) || StringUtils.isNotEmpty(getMaterno())
                    || StringUtils.isNotEmpty(getNombre())) && (StringUtils.
                    isNotEmpty(getNoCuenta()) || StringUtils.isNotEmpty(getRazonSocial()))) {
                setCorporativos(limpiarPantalla);
                delegate.record("La b\u00fasqueda es por Personas, por Empresas, o por Contrato.",
                        null);
                return;
            }
            else if (StringUtils.isNotEmpty(getNoCuenta()) && (StringUtils.isNotEmpty(getPaterno())
                    || StringUtils.isNotEmpty(getMaterno()) || StringUtils.isNotEmpty(getNombre())
                    || StringUtils.isNotEmpty(getRazonSocial()))) {
                setCorporativos(limpiarPantalla);
                delegate.record("La b\u00fasqueda es por Personas, por Empresas, o por Contrato.",
                        null);
                return;
            }
        }
        else {
            if ((getRazonSocial()).indexOf("%") >= 0) {
                setCorporativos(limpiarPantalla);
                delegate.record("No se permite utilizar el caracter '%' en los criterios de "
                        + "consulta.", null);
	            return;
	        }
            else if (StringUtils.isEmpty(getRazonSocial())) {
                setCorporativos(limpiarPantalla);
                delegate.record("Debe definir al menos un criterio de b\u00fasqueda.", null);
                return;
            }
            else if (StringUtils.isNotEmpty(getRazonSocial()) && getRazonSocial().length() < 3) {
                setCorporativos(limpiarPantalla);
                delegate.record("Los criterios de b\u00fasqueda deben tener m\u00e1s de 2 "
                        + "caracteres.", null);
                return;
            }
        }
        // Logica de Negocio
        if (isModoMesaCambio()) {
            // Verifico que Existan corporativo Sica para los Criterios de Busqueda Especificados
            corporativos = getSicaServiceData().findContratosSicaBrokers(getRazonSocial());
        }
        else {
            ClienteDao clienteDao = (ClienteDao) getApplicationContext().getBean("clienteDao");
            if (!isCuentasSubordinados()) {
                contratosSica = clienteDao.findClientes(
                        ((Visit) getVisit()).getUser().getIdPersona(), getRazonSocial(),
                        getPaterno(), getMaterno(), getNombre(), getNoCuenta(), true);
                for (Iterator it = contratosSica.iterator(); it.hasNext();) {
                    ClienteContratoDto ccDto = (ClienteContratoDto) it.next();
                    verificarCorporativos(ccDto.getIdPersona(), corporativos);
                }
            }
            else {
                IPerfil perfil = getSeguridadServiceData().findPerfilByUserAndSystem(
                        visit.getUser().getIdUsuario(), ((SupportEngine) getEngine()).
                        getApplicationName());
                contratosSica = clienteDao.findClientes(getRazonSocial(), getPaterno(),
                        getMaterno(), getNombre(), getNoCuenta(), true);
                for (Iterator it2 = contratosSica.iterator(); it2.hasNext();) {
                    ClienteContratoDto ccDto = (ClienteContratoDto) it2.next();
                    String noCuenta = ccDto.getNoCuenta();
                    if (getSeguridadServiceData().isCuentaAsignadaEjecutivo(perfil,
                            noCuenta)) {
                        verificarCorporativos(ccDto.getIdPersona(), corporativos);
                    }
                }
            }
        }
        if (corporativos.isEmpty()) {
            setCorporativos(limpiarPantalla);
            delegate.record("No se encontr\u00f3 el Cliente, o el Cliente no tiene Contrato SICA.",
                    null);
            return;
        }
        // Se cargan los Corporativos en Pantalla
        setCorporativos(corporativos);
    }


    /**
	 * Verifica si la Persona de un Contrato SICA es Corporativo o no.
	 *
	 * @param idPersona La Persona del Contrato SICA
	 * @param corporativos La Lista de Corporativos a desplegar en Pantalla.
	 */
	private void verificarCorporativos(Integer idPersona, List corporativos) {
		PersonaMoral corporativo = getSicaServiceData().findCorporativo(idPersona);
		Object p;
		if (corporativo != null) {
		    p = corporativo;
		}
		else {
			p = getSicaServiceData().find(Persona.class, idPersona);
		}
		if (!corporativos.contains(p)) {
		    corporativos.add(p);
		}
	}

	/**
	 * Convierte lo escrito en los Criterios de B&uacute;squeda a Uppercase y quita espacios en
     * blanco para evitar fallas en las b&uacute;squedas en la Base de Datos.
	 */
	public void convertirAUpperCase() {
        if (StringUtils.isNotEmpty(getPaterno())) {
            setPaterno(getPaterno().trim().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getMaterno())) {
            setMaterno(getMaterno().trim().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getNombre())) {
            setNombre(getNombre().trim().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getRazonSocial())) {
            setRazonSocial(getRazonSocial().trim().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getNoCuenta())) {
            setNoCuenta(getNoCuenta().trim().toUpperCase());
        }
    }

	/**
	 * Pasa el flujo de operaci&oacute;n a la P&aacute;gina <code>CapturaLineaCredito</code>
	 * para el Alta o Edici&oacute;n de L&iacute;neas de Cr&eacute;dito del Corporativo
	 * seleccionado de acuerdo a un cierto Producto.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void editar(IRequestCycle cycle) {
        CapturaLineaCredito nextPage = (CapturaLineaCredito) cycle.getPage("CapturaLineaCredito");
        nextPage.setIdCorporativo(new Integer((cycle.getServiceParameters()[0]).toString()));
        nextPage.setRazonSocialCorporativo((cycle.getServiceParameters()[1]).toString());
        nextPage.setPaginaAnterior(getPageName());
        if (isModoMesaCambio()) {
            nextPage.setContraparte(true);
        }
        else {
            nextPage.setContraparte(false);
        }
		nextPage.activate(cycle);
	}

    /**
     * Activa la p&aacute;gina de ConsultaUsoFechaValor.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void ver(IRequestCycle cycle) {
        try {
            ConsultaUsoFechaValor nextPage = (ConsultaUsoFechaValor) cycle.
                    getPage("ConsultaUsoFechaValor");
            nextPage.configurar((Integer) cycle.getServiceParameters()[0]);
            cycle.activate(nextPage);
        }
        catch (SicaException e) {
            debug(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }    
    }    

    /**
	 * Activa la lista de Corporativos encontrados de acuerdo a los criterios de
	 * B&uacute;squeda preestablecidos.
	 *
	 * @param corporativos La lista de Corporativos encontrados.
	 */
	public abstract void setCorporativos(List corporativos);
	
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
	 * Obtiene lo establecido como criterio de b&uacute;squeda Raz&oacute;n
	 * Social.
	 *
	 * @return String razonSocial.
	 */
	public abstract String getRazonSocial();
	
	/**
     * Fija el valor de razonSocial.
     *
     * @param razonSocial El valor a asignar.
     */
    public abstract void setRazonSocial(String razonSocial);

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
     * Regresa el modo de la mesa de cambio.
     *
     * @return boolean.
     */
    public boolean isModoMesaCambio() {
        return FacultySystem.MODO_DIRECCION_MC.equals(getModo());
    }
    
    /**
     * Regresa el valor de cuentasSubordinados.
     *
     * @return boolean.
     */
    public abstract boolean isCuentasSubordinados();
    
    /**
     * Regresa el valor de noCuenta.
     * 
     * @return String.
     */
    public abstract String getNoCuenta();

    /**
     * Fija el valor de noCuenta.
     *
     * @param noCuenta El valor a asignar.
     */
    public abstract void setNoCuenta(String noCuenta);
}