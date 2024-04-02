/*
 * $Id: AsignacionCtas.java,v 1.13 2008/02/22 18:25:32 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.mesa;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.CuentaEjecutivo;
import com.ixe.ods.bup.model.CuentaEjecutivoPK;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Status;
import com.ixe.ods.bup.model.TipoEjecutivo;
import com.ixe.ods.bup.model.UltimaModificacion;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.ContratoCliente;
import com.ixe.ods.sica.pages.SicaPage;
import com.legosoft.tapestry.model.RecordSelectionModel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 * P&aacute;gina que permite al usuario Asigar cuentas.
 *
 * @author Gerardo Corzo
 * @version  $Revision: 1.13 $ $Date: 2008/02/22 18:25:32 $
 */
public abstract class AsignacionCtas extends SicaPage {

    /**
     * M&eacut;etodo que carga todos los promotores de un usuario
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        Visit v = (Visit) getVisit();
        List promotores;
        setModo((String) cycle.getServiceParameters()[0]);
        if ((FacultySystem.MODO_MESA_CONTROL).equals(getModo())) {
            promotores = getSicaServiceData().findPromotoresSica(((SupportEngine) getEngine()).
                    getIdTipoEjecutivo());
        }
        else {
            // Obtiene subordinados incluyendo nodo raiz
            promotores = getSeguridadServiceData().
                    findSubordinadosEjecutivoYEjecutivosASustituir(((SupportEngine) getEngine()).
                            getApplicationName(), v.getUser().getIdPersona());
        }
        Collections.sort(promotores, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((EmpleadoIxe) o1).getNombreCompleto().
                        compareTo(((EmpleadoIxe) o2).getNombreCompleto());
            }
        });
        EmpleadoIxe emp = new EmpleadoIxe();
        emp.setIdPersona(new Integer(-1));
        emp.setNombre("-- Seleccione un promotor--");
        promotores.add(0, emp);
        setPromotor(emp);
        setPromotoresList(promotores);
    }

    /**
     * Regresa el modo de la mesa de cambio.
     *
     * @return boolean.
     */
    public boolean isModoMesaCambio() {
        return FacultySystem.MODO_MESA_CAMBIOS.equals(getModo());
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
     * Regresa un CuentasSelectionModel con todas las cuentas.
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getCuentasModel() {
        Visit v = (Visit) getVisit();
        List cuentas = getSicaServiceData().findCuentasEjecutivo(((SupportEngine) getEngine()).
                getIdTipoEjecutivo(), v.getUser().getIdPersona());
        for (Iterator it = cuentas.iterator(); it.hasNext();) {
            CuentaEjecutivo ce = (CuentaEjecutivo) it.next();
            ContratoSica cs = getSicaServiceData().findContrato(ce.getId().getNoCuenta());
            if (cs == null || ContratoCliente.clienteParaContratoSica(cs) == null) {
            	it.remove();
            }
        }
        return new CuentasSelectionModel(cuentas, getSicaServiceData());
    }

    /**
     * Regresa un CuentasSelectionModel con todas las cuentas asignadas a un ejecutivo.
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getCuentasAsignadasModel() {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
        getBean("delegate");
        List cuentas = new ArrayList();
        if (getPromotor() != null && getPromotor().getIdPersona().intValue() > 0) {
            cuentas = getSicaServiceData().findCuentasEjecutivo(((SupportEngine) getEngine()).
                    getIdTipoEjecutivo(), getPromotor().getIdPersona());
            if (cuentas.size() > 0) {
                for (Iterator it = cuentas.iterator(); it.hasNext();) {
                    CuentaEjecutivo ce = (CuentaEjecutivo) it.next();
                    ContratoSica cs = getSicaServiceData().findContrato(ce.getId().getNoCuenta());
                    if (ce.getId() == null) { 
                        it.remove();
                    }
                    else if (ContratoCliente.clienteParaContratoSica(cs) == null) { 
                        it.remove();
                    }
                }
            }
            else {
            	delegate.record("No se encontraron cuentas para el promotor seleccionado.", null);
                cuentas = new ArrayList();
            }
        }
        return new CuentasSelectionModel(cuentas, getSicaServiceData());
    }
    
    /**
     * M&eacut;etodo que define en que modo se va a trabajar, asignar o des-asignar una cuenta
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void submit(IRequestCycle cycle) {
        switch (getModoSubmit()) {
            case SUBMIT_ASIGNAR:
                asignar();
                break;
            case SUBMIT_DESASIGNAR:
                desAsignar();
                break;
            default:
                break;
        }
    }
    
    /**
     * M&eacut;etodo que des-asigna una cuenta
     * 
     */
    private void desAsignar() {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
    	try {
    		Status s = new Status();
    		s.setEstatus(STATUS_CTA_EJE_CANCE);
    		UltimaModificacion um = new UltimaModificacion();
            um.setUsuario(((Visit) getVisit()).getUser().getClave());
            um.setFecha(new Timestamp(new Date().getTime()));
            getCuentaAsignada().setUltimaModificacion(um);
            getCuentaAsignada().setStatus(s);
    		getSicaServiceData().update(getCuentaAsignada());
    		setLevel(1);
    		delegate.record("La cuenta ha sido desasignada.", null);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		setLevel(-1);
    		delegate.record("Hubo un error al intentar desasignar la cuenta.", null);
	        return;
		}
    }

    /**
     * Reasigna una cuenta.
     */
    private void asignar() {
        if (getPromotor() != null && getPromotor().getIdPersona().intValue() > 0
                && getCuentaSeleccionada() != null) {
            CuentaEjecutivo nvaCta = new CuentaEjecutivo();
            CuentaEjecutivoPK nvaCtaPK = new CuentaEjecutivoPK();
            nvaCtaPK.setEjecutivo(getPromotor());
            nvaCtaPK.setNoCuenta(getCuentaSeleccionada().getId().getNoCuenta());
            nvaCtaPK.setTipo(getCuentaSeleccionada().getId().getTipo());
            nvaCta.setId(nvaCtaPK);
            nvaCta.setStatus(getCuentaSeleccionada().getStatus());
            UltimaModificacion um = new UltimaModificacion();
            um.setUsuario(((Visit) getVisit()).getUser().getClave());
            um.setFecha(new Timestamp(new Date().getTime()));
            nvaCta.setUltimaModificacion(um);
            getSicaServiceData().reasignarCuentaEjecutivo(getCuentaSeleccionada(), nvaCta);
        }
    }

    /**
     * M&eacut;etodo que asigna una cuenta a un contrato sica.
     *
     * @param contratoSica El contrato sica al que se asigna la cuenta.
     */
    public void asignarCuenta(ContratoSica contratoSica) {
    	List l = getSicaServiceData().findCuentaEjecutivoByContratoSicaAndStatus(((SupportEngine) getEngine()).getIdTipoEjecutivo(),
    			STATUS_CTA_EJE_CANCE, contratoSica.getNoCuenta(), getPromotor().getIdPersona());
    	if (l.isEmpty()) {
    		TipoEjecutivo te = (TipoEjecutivo) getSicaServiceData().find(TipoEjecutivo.class,
                    ((SupportEngine) getEngine()).getIdTipoEjecutivo());
            CuentaEjecutivo nvaCta = new CuentaEjecutivo();
            CuentaEjecutivoPK nvaCtaPK = new CuentaEjecutivoPK();
            Status status = new Status();
            status.setEstatus("VIG");
            nvaCtaPK.setEjecutivo(getPromotor());
            nvaCtaPK.setNoCuenta(contratoSica.getNoCuenta());
            nvaCtaPK.setTipo(te);
            nvaCta.setId(nvaCtaPK);
            nvaCta.setStatus(status);
            UltimaModificacion um = new UltimaModificacion();
            um.setUsuario(((Visit) getVisit()).getUser().getClave());
            um.setFecha(new Timestamp(new Date().getTime()));
            nvaCta.setUltimaModificacion(um);
            getSicaServiceData().store(nvaCta);
    	}
    	else {
    		Status status = new Status();
            status.setEstatus("VIG");
            UltimaModificacion um = new UltimaModificacion();
            um.setUsuario(((Visit) getVisit()).getUser().getClave());
            um.setFecha(new Timestamp(new Date().getTime()));
            CuentaEjecutivo cta = (CuentaEjecutivo)l.get(0);
            cta.setStatus(status);
            cta.setUltimaModificacion(um);
            getSicaServiceData().update(cta);
    	}
    }

    /**
     * M&eacut;etodo que pasa a la p&aacute;gina de Consulta de cuentas
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void abrirConsultaCuentas(IRequestCycle cycle) {
        ConsultaCuentas nextPage = (ConsultaCuentas) cycle.getPage("ConsultaCuentas");
        nextPage.activate(cycle);
    }

    /**
     * Regresa el valor de cuentaSeleccionada.
     *
     * @return CuentaEjecutivo.
     */
    public abstract CuentaEjecutivo getCuentaSeleccionada();

    /**
     * Fija el valor de cuentaSeleccionada.
     *
     * @param cuentaSeleccionada El valor a asignar.
     */
    public abstract void setCuentaSeleccionada(CuentaEjecutivo cuentaSeleccionada);

    /**
     * Regresa el valor de cuentaAsignada.
     *
     * @return CuentaEjecutivo.
     */
    public abstract CuentaEjecutivo getCuentaAsignada();

    /**
     * Fija el valor de cuentaAsignada.
     *
     * @param cuentaAsignada El valor a asignar.
     */
    public abstract void setCuentaAsignada(CuentaEjecutivo cuentaAsignada);

    /**
     * Regresa el valor de promotoresList.
     *
     * @return List.
     */
    public abstract List getPromotoresList();

    /**
     * Fija el valor de promotoresList.
     *
     * @param promotoresList El valor a asignar.
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
     * Regresa el valor de modo.
     *
     * @return String.
     */
    public abstract String getModo();

    /**
     * Fija el valor de modo.
     *
     * @param modo El valor a asignar.
     */
    public abstract void setModo(String modo);

    /**
     * Regresa el valor de modoSubmit.
     *
     * @return int.
     */
    public abstract int getModoSubmit();

    /**
     * Nivel de los mensajes al usuario: -1 Error, 0 Warning, 1 Exito.
     * 
     * @param level El valor a asignar.
     */
	public abstract void setLevel(int level);
    
    /**
     * Constante para indicar la opci&oacute; de asignar una cuenta.
     */
    public static final int SUBMIT_ASIGNAR = 1;

    /**
     * Constante para indicar la opci&oacute; de des-asignar una cuenta.
     */
    public static final int SUBMIT_DESASIGNAR = 2;
    
    public static final String STATUS_CTA_EJE_CANCE = "CANCE";
	
	public static final String STATUS_CTA_EJE_VIG = "VIG";
}
