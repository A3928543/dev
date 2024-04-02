/*
 * $Id: AsignacionCtasCliente.java,v 1.2 2008/02/22 18:25:32 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.mesa;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.CuentaEjecutivo;
import com.ixe.ods.bup.model.CuentaEjecutivoPK;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Status;
import com.ixe.ods.bup.model.TipoEjecutivo;
import com.ixe.ods.bup.model.UltimaModificacion;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.pages.ConsultaClientes;
import com.ixe.ods.sica.pages.SicaPage;
import com.legosoft.tapestry.model.RecordSelectionModel;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 * P&aacute;gina que permite al usuario Asignar cuentas por b&uacute;squeda de clientes.
 *
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.2 $ $Date: 2008/02/22 18:25:32 $
 */
public abstract class AsignacionCtasCliente extends SicaPage {

	/**
     * M&eacut;etodo que carga todos los promotores de un usuario
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
    	super.activate(cycle);
        List promotores = getSicaServiceData().findAllPromotoresSICA(
        		((SupportEngine) getEngine()).getApplicationName());
        Collections.sort(promotores, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((EmpleadoIxe) o1).getNombreCompleto().
                        compareTo(((EmpleadoIxe) o2).getNombreCompleto());
            }
        });
        EmpleadoIxe emp = new EmpleadoIxe();
        emp.setIdPersona(new Integer(-1));
        emp.setNombre("Seleccione un promotor");
        promotores.add(0, emp);
        setPromotor(emp);
        setPromotoresList(promotores);
    }
	
    /**
     * Asigna el promotor seleccionado a la cuenta, tomando en cuenta
     * los siguientes casos:
     * 
     * <li>Si la CuentaEjecutivo no existe, crea un registro nuevo en la tabla.</li>
     * <li>Si ya existe, asigna el estatus 'CANCE' a las cuentas que tengan estatus
     * 'VIG' y activa el registro indicado.</li>
     * 
     * @param cycle El ciclo de la pagina.
     */
	public void submit(IRequestCycle cycle) {
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
        getBean("delegate");
		if (getPromotor() != null && getPromotor().getIdPersona().intValue() > 0) {
			int idPersona = getPromotor().getIdPersona().intValue();
			boolean ctaInexistente = getSicaServiceData().
                    findCuentaEjecutivoByContratoSicaAndIdEjecutivo(((SupportEngine) getEngine()).
                            getIdTipoEjecutivo(), getNoCuenta(),  getPromotor().getIdPersona());			
			List ctasEjec = getSicaServiceData().findCuentaEjecutivoByContratoSicaExcStatus(
	    			((SupportEngine) getEngine()).getIdTipoEjecutivo(), getNoCuenta());
			
			if (ctaInexistente) {
				if (!ctasEjec.isEmpty()) {
					asignaStatusCancelado(ctasEjec);
				}
				creaCuentaEjecutivo();
			}
			else {
				if (!ctasEjec.isEmpty()) {
		    		for (Iterator it = ctasEjec.iterator(); it.hasNext();) {
		    			CuentaEjecutivo ce = (CuentaEjecutivo) it.next();
		    			if (ce.getId().getEjecutivo().getIdPersona().intValue() == idPersona
                                && STATUS_CTA_EJE_CANCE.equals(ce.getStatus().getEstatus())) {
		    				asignaStatusVigente(ce);
		    			}
		    			if (ce.getId().getEjecutivo().getIdPersona().intValue() == idPersona
                                && STATUS_CTA_EJE_VIG.equals(ce.getStatus().getEstatus())) {
		    			}
		    			if (ce.getId().getEjecutivo().getIdPersona().intValue() != idPersona
                                && STATUS_CTA_EJE_VIG.equals(ce.getStatus().getEstatus())) {
		    				asignaStatusCancelado(ce);
		    			}
		    		}
		    	}
			}
			ConsultaClientes prevPage = (ConsultaClientes) cycle.getPage("ConsultaClientes");
			prevPage.setModo(FacultySystem.MODO_MESA_CONTROL);
			prevPage.setLevel(1);
			cycle.activate(prevPage);
		}
		else {
			delegate.record("Debe seleccionar un promotor para asignar la cuenta.", null);
		}
	}

	/**
	 * Regresa a la consulta de clientes.
	 * 
	 * @param cycle El ciclo de la pagina.
	 */
	public void cancelar(IRequestCycle cycle) {
		ConsultaClientes prevPage = (ConsultaClientes) cycle.getPage("ConsultaClientes");
		prevPage.setModo(FacultySystem.MODO_MESA_CONTROL);
        cycle.activate(prevPage);
	}

	/**
	 * Crea y almacena un nuevo registro en CuentaEjecutivo. 
	 */
	public void creaCuentaEjecutivo() {
		TipoEjecutivo te = (TipoEjecutivo) getSicaServiceData().find(TipoEjecutivo.class,
                ((SupportEngine) getEngine()).getIdTipoEjecutivo());
		Status status = new Status();
        status.setEstatus("VIG");
        UltimaModificacion um = new UltimaModificacion();
        um.setUsuario(((Visit) getVisit()).getUser().getClave());
        um.setFecha(new Timestamp(new Date().getTime()));
		CuentaEjecutivo nvaCta = new CuentaEjecutivo();
        CuentaEjecutivoPK nvaCtaPK = new CuentaEjecutivoPK();
        nvaCtaPK.setEjecutivo(getPromotor());
        nvaCtaPK.setNoCuenta(getNoCuenta());
        nvaCtaPK.setTipo(te);
        nvaCta.setId(nvaCtaPK);
        nvaCta.setStatus(status);
        nvaCta.setUltimaModificacion(um);
        getSicaServiceData().store(nvaCta);
	}

	/**
	 * Asigna el estatus 'CANCE' a todas las CuentasEjecutivo de la lista.
	 * 
	 * @param cuentasEjecutivo La lista de CuentasEjecutivo.
	 */
	public void asignaStatusCancelado(List cuentasEjecutivo) {
		for (Iterator it = cuentasEjecutivo.iterator(); it.hasNext();) {
			CuentaEjecutivo ce = (CuentaEjecutivo) it.next();
			Status status = new Status();
			status.setEstatus("CANCE");
			ce.setStatus(status);
			UltimaModificacion um = new UltimaModificacion();
			um.setUsuario(((Visit) getVisit()).getUser().getClave());
			um.setFecha(new Timestamp(new Date().getTime()));
			ce.setStatus(status);
			ce.setUltimaModificacion(um);
			getSicaServiceData().update(ce);
		}
	}
	
	/**
	 * Asigna el estatus 'CANCE' a la CuentaEjecutivo que se 
	 * define como parametro.
	 * 
	 * @param ce La CuentaEjecutivo.
	 */
	public void asignaStatusCancelado(CuentaEjecutivo ce){
		Status status = new Status();
		status.setEstatus("CANCE");
		ce.setStatus(status);
		UltimaModificacion um = new UltimaModificacion();
		um.setUsuario(((Visit) getVisit()).getUser().getClave());
		um.setFecha(new Timestamp(new Date().getTime()));
		ce.setStatus(status);
		ce.setUltimaModificacion(um);
		getSicaServiceData().update(ce);

	}
	
	/**
	 * Asigna el estatus 'VIG' a la CuentaEjecutivo definida como
	 * parametro.
	 * 
	 * @param ce La CuentaEjecutivo.
	 */
	public void asignaStatusVigente(CuentaEjecutivo ce){
		Status status = new Status();
		status.setEstatus("VIG");
		ce.setStatus(status);
		UltimaModificacion um = new UltimaModificacion();
		um.setUsuario(((Visit) getVisit()).getUser().getClave());
		um.setFecha(new Timestamp(new Date().getTime()));
		ce.setStatus(status);
		ce.setUltimaModificacion(um);
		getSicaServiceData().update(ce);
		
	}
	
	/**
     * Regresa un RecordSelectionModel con todos los ejecutivos.
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getPromotoresModel() {        
        return new RecordSelectionModel(getPromotoresList(), "idPersona", "nombreCompleto");
    }
	
    /**
     * Obtiene el nombre del cliente.
     * 
     * @return String
     */
	public abstract String getNombreCliente();
	
	/**
	 * Asigna el valor para el nombre del cliente.
	 * 
	 * @param nombreCliente El nombre para el cliente.
	 */
	public abstract void setNombreCliente(String nombreCliente);
	
	/**
	 * Obtiene el numero del cuenta del cliente.
	 * 
	 * @return String
	 */
	public abstract String getNoCuenta();
	
	/**
	 * Asigna el valor para el numero de cuenta.
	 * 
	 * @param noCuenta El valor para el numero de cuenta.
	 */
	public abstract void setNoCuenta(String noCuenta);
	
	/**
	 * Obtiene la lista de promotores.
	 * 
	 * @return List
	 */
	public abstract List getPromotoresList();
	
	/**
	 * Asigna el valor para la lista de promotores.
	 * 
	 * @param promotoresList El valor para la lista de promotores.
	 */
	public abstract void setPromotoresList(List promotoresList);
	
	/**
     * Regresa el valor de promotor.
     *
     * @return EmpleadoIxe.
     */
    public abstract EmpleadoIxe getPromotor();

    /**
     * Fija el valor de promotor.
     *
     * @param promotor El valor a asignar.
     */
    public abstract void setPromotor(EmpleadoIxe promotor);
	
    /**
     *  Variable estatica que define el estatus cancelado para CuentaEjecutivo.
     */
    public static final String STATUS_CTA_EJE_CANCE = "CANCE";
	
    /**
     * Variable estatica que define el estatus cancelado para CuentaEjecutivo.
     */
	public static final String STATUS_CTA_EJE_VIG = "VIG";
}
