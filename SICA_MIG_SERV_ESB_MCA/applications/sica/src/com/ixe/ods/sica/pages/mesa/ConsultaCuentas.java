/*
 * $Id: ConsultaCuentas.java,v 1.11.88.1 2017/07/29 03:17:56 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.mesa;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.PersonaCuentaRol;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.services.PersonaService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;

/**
 * P&aacute;gina que permite al usuario consultar las cuentas que no
 * estan asiganadas a ning&uacute;n promotor.
 *
 * @author Gerardo Corzo Herrera
 * @version $Revision: 1.11.88.1 $ $Date: 2017/07/29 03:17:56 $
 */
public abstract class ConsultaCuentas extends SicaPage {

    /**
     * M&eacut;etodo que carga todas las cuentas que no tengan un promotor
     * asiganado
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        setCuentasEncontradas(new ArrayList());
    }

    /**
     * M&eacut;etodo que busca una cuenta determinada que no tenga
     * un promotor asignado
     *  
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void search(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
        List limpiarPantalla = new ArrayList();
        if (StringUtils.isEmpty(getNombre().trim())
                && StringUtils.isEmpty(getPaterno().trim())
                && StringUtils.isEmpty(getMaterno().trim())
                && StringUtils.isEmpty(getRazonSocial().trim())
                && StringUtils.isEmpty(getNoCuenta().trim())) {
            setCuentasEncontradas(limpiarPantalla);
            delegate.record("Debe definir al menos un criterio de b\u00fasqueda.", null);
            return;
        }
        else if ((StringUtils.isNotEmpty(getRazonSocial()) && getRazonSocial().length() < 4)
                || (StringUtils.isNotEmpty(getNombre()) && getNombre().length() < 4)
                || (StringUtils.isNotEmpty(getPaterno()) && getPaterno().length() < 4)
                || (StringUtils.isNotEmpty(getMaterno()) && getMaterno().length() < 4)
                || (StringUtils.isNotEmpty(getNoCuenta()) && getNoCuenta().length() < 4)) {
            setCuentasEncontradas(limpiarPantalla);
            delegate.record("Los criterios de b\u00fasqueda deben tener m\u00e1s de 3 caracteres.", null);
            return;
        }

        List cuentasContrato1 = getSicaServiceData().findCuentasNoAsignadas(getRazonSocial(), getNombre(), getPaterno(), getMaterno(), getNoCuenta());
        List descCuentas = new ArrayList();
        for (Iterator iter = cuentasContrato1.iterator(); iter.hasNext();) {
        	HashMap map = new HashMap();
			ContratoSica element = (ContratoSica) iter.next();
			if (element != null) {
	            for (Iterator it = element.getRoles().iterator(); it.hasNext(); ) {
	                PersonaCuentaRol pcr = (PersonaCuentaRol) it.next();
	                if ("TIT".equals(pcr.getId().getRol().getIdRol())) {
	                   map.put("idPersona", pcr.getId().getPersona().getIdPersona());
	                   map.put("cliente", pcr.getId().getPersona().getNombreCompleto());
	                   map.put("noCuenta", pcr.getId().getCuenta().getNoCuenta());
	                   descCuentas.add(map);
	                }
	            }
			  }
		}        
        if (cuentasContrato1.isEmpty()) {
            setCuentasEncontradas(limpiarPantalla);
            delegate.record("La cuenta no existe o ya est\u00e1 asignada a un promotor", null);
        }
        else {
            setCuentasEncontradas(descCuentas);
        }
    }

    /**
     * M&eacut;etodo que selecciona una cuenta, la que se encontro en la b&uacute;squeda
     * que no tiene un promotor asignado, y la asigna a un promotor
     *  
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void seleccionarCuenta(IRequestCycle cycle) {
    	ContratoSica contratoSica = null;
    	IxenetValidationDelegate delegate = 
    			(IxenetValidationDelegate) getBeans().getBean("delegate");
    	try {
    		Integer idPersona = null;
    		if (cycle.getServiceParameters().length >= 2) {
    			idPersona = new Integer((cycle.getServiceParameters()[1]).toString()); 
    		}
	    	if (this.getPersonaService().isValidaInformacionGeneralPersona(idPersona, null)) {
	    		String noCuenta = (String) cycle.getServiceParameters()[0];
	    		contratoSica = (ContratoSica) getSicaServiceData().find(ContratoSica.class, noCuenta);
	    		AsignacionCtas asignacionCuentas = (AsignacionCtas) cycle.getPage("AsignacionCtas");
	    		asignacionCuentas.asignarCuenta(contratoSica);
	    		setCerrarVentana(true);
	    	}
    	}
    	catch (SicaException ex) {
    		this.debug(ex);
    		delegate.record("Error al asignar la cuenta. " + ex.getMessage(), null);
    	}
    	
    }
    
    /**
     * Gets the persona service.
     *
     * @return the persona service
     */
    private PersonaService getPersonaService() {
    	return (PersonaService) getApplicationContext().getBean("personaService");
    }
    
    /**
     * Fija el valor de cerrarVentana.
     *
     * @param cerrarVentana El valor a asignar.
     */
    public abstract void setCerrarVentana(boolean cerrarVentana);

    /**
     * Fija el valor de cuentasEncontradas.
     *
     * @param cuentasEncontradas El valor a asignar.
     */
    public abstract void setCuentasEncontradas(List cuentasEncontradas);

    /**
     * Regresa el valor de cuentasEncontradas.
     *
     * @return List.
     */
    public abstract List getCuentasEncontradas();

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda Nombre.
     *
     * @return String Nombre.
     */
    public abstract String getNombre();	

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda Apellido Paterno.
     *
     * @return String Paterno.
     */
    public abstract String getPaterno();

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda Apellido Materno.
     *
     * @return String Materno.
     */
    public abstract String getMaterno();

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda Raz&oacute;n
     * Social.
     *
     * @return String RazonSocial.
     */
    public abstract String getRazonSocial();

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda No. de Cuenta
     * (Contrato).
     *
     * @return String NoCuenta.
     */
    public abstract String getNoCuenta();

}
