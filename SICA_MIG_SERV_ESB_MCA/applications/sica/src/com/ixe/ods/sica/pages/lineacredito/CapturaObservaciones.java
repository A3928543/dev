/*
 * $Id: CapturaObservaciones.java,v 1.17.84.1 2016/07/13 21:45:26 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.lineacredito;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.lineacredito.service.LineaCreditoService;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.LineaOperacion;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pages.lineaoperacion.AprobacionLineaOperacion;

/**
 * Clase que sirve para capturar las Observaciones (Razones de Suspensi&oacute;n)
 * de la L&iacute;nea de Cr&eacute;dito.
 *
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.17.84.1 $ $Date: 2016/07/13 21:45:26 $
 */
public abstract class CapturaObservaciones extends SicaPage {

	/**
	 * Se ejecuta cada que se activa la p&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
		setObservaciones("");
	}

	/**
     * Sirve para colocar el focus de la p&aacute;gina cuando se carga 
     * en el Campo de Texto Deseado.
     * 
     * @return Map
     */
    public Map getFirstTextFieldMap() {
        Map map = new HashMap();
        map.put("textField", "document.Form0.observacionesTextArea");
        return map;
    }

    /**
     * Activa las Observaciones (Razones de Suspensi&oacute;n) de la L&iacute;nea de
     * Cr&eacute;dito y regresa el flujo de operaci&oacute;n a la P&aacute;gina
     * <code>AprobacionLineaCredito</code>.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void activaObservaciones(IRequestCycle cycle) {
		LineaCambio lineaCredito  = getLineaCreditoService().findLineaCredito(getIdLinea());
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
		if ("".equals(getObservaciones())) {
			delegate.record("Debe teclear en observaciones la raz\u00f3n de la " +
                    "suspensi\u00f3n de la l\u00ednea.", null);
			return;
		}
		if (getObservaciones().length() > 60) {
			delegate.record("Las observaciones no deben superar 60 caracteres.", null);
			return;
        }
        if (!delegate.getHasErrors()) {
            if (MODO_LINEA_CREDITO.equals(getModo())) {
                AprobacionLineaCredito nextPage = (AprobacionLineaCredito) cycle.
                        getPage(getPaginaAnterior());
                nextPage.setObservaciones(getObservaciones());
                nextPage.cambiarEstatusLineaCredito(LineaCambio.STATUS_SUSPENDIDA,
                        "S", getIdLinea()); //Suspender
                nextPage.notificarViaEmail(lineaCredito, getNombreCompletoCliente(), 
                		LineaCreditoService.SUSPENDIDA);
                cycle.activate(nextPage);
            }
            else if (MODO_LINEA_CREDITO_CANCELAR.equals(getModo())) {
                AprobacionLineaCredito nextPage = (AprobacionLineaCredito) cycle.
                        getPage(getPaginaAnterior());
                nextPage.setObservaciones(getObservaciones());
                nextPage.cambiarEstatusLineaCredito(LineaCambio.STATUS_SUSPENDIDA, "S",
                        getIdLinea()); //Cancelar
                nextPage.notificarViaEmail(lineaCredito, getNombreCompletoCliente(), 
                		LineaCreditoService.SUSPENDIDA);
                cycle.activate(nextPage);
            }
            else {
                AprobacionLineaOperacion nextPage = (AprobacionLineaOperacion) cycle.
                        getPage(getPaginaAnterior());
                nextPage.setObservaciones(getObservaciones());
                nextPage.cambiarEstatusLineaOperacion(LineaOperacion.STATUS_SUSPENDIDA, "S",
                        getIdLinea()); //Suspender
                cycle.activate(nextPage);
            }
        }
    }

    /**
     * Regresa a la pantalla Anterior.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cancelarOperacion(IRequestCycle cycle) {
        cycle.activate(getPaginaAnterior());
    }

    /**
     * Obtiene la P&aacute;gina a la que se debe de regresar
     * despu&eacute;s de la captura de Observaciones.
     *
     * @return String El nombre de la P&aacute;gina.
     */
	public abstract String getPaginaAnterior();

	/**
     * Obtiene el N&uacute;mero de la L&iacute;nea de Cr&eacute;dito o de Operaci&oacute;n a
     * Suspender.
     *
     * @return Integer el N&uacute;mero de la L&iacute;nea.
     */
    public abstract Integer getIdLinea();

    /**
     * Obtiene, en caso de que la Caseta de Cr&eacute;dito Suspenda una L&iacute;nea de
     * Cr&eacute;dito, la Raz&oacute;n de la Suspensi&oacute;.
     *
     * @return String La Raz&oacute;n de la Suspensi&oacute;.
     */
    public abstract String getObservaciones();
    
    /**
     * Obtiene el Modo de Operaci&oacute;n de la P&aacute;gina.
     *
     * @return String el Modo de Operaci&oacute;n de la P&aacute;gina.
     */
    public abstract String getModo();

    /**
	 * Establece la P&aacute;gina a la que se debe de regresar
	 * despu&eacute;s de la captura de Observaciones.
	 *
	 * @param paginaAnterior La P&aacute;gina.
	 */
	public abstract void setPaginaAnterior(String paginaAnterior);
	
	/**
     * Fija el N&uacute;mero de la L&iacute;nea de Cr&eacute;dito o de Operaci&oacute;n a Suspender.
     *
     * @param idLinea el N&uacute;mero de la L&iacute;nea.
     */
    public abstract void setIdLinea(Integer idLinea);
	
	/**
     * En caso de que la Caseta de Cr&eacute;dito Suspenda una L&iacute;nea de Cr&eacute;dito,
     * guarda la Raz&oacute;n de la Suspensi&oacute;.
     *
     * @param observaciones La Raz&oacute;n de la Suspensi&oacute;.
     */
    public abstract void setObservaciones(String observaciones);
    
    /**
     * Establece el Modo de Operaci&oacute;n de la P&aacute;gina:
     * L&iacute;nea de Operaci&oacute;n o L&iacute;nea de Cr&eacute;dito.
     *
     * @param modo El Modo de Operaci&oacute;n.
     */
    public abstract void setModo(String modo);
    
    /**
     * Constante Modo Linea Credito. Para saber cuando se entra desde Lineas de Credito o 
     * desde Lineas de Operacion.
     */
    public static final String MODO_LINEA_CREDITO = "MODO_LINEA_CREDITO";

    /**
     * Constante Modo Cancelacion Linea Credito.
     */
    public static final String MODO_LINEA_CREDITO_CANCELAR = "MODO_LINEA_CREDITO_CANCELAR";

    /**
     * Nombre completo del Cliente
     * @param nombreCompleto
     */
	public abstract void setNombreCompletoCliente(String nombreCompleto);
	
	
	/**
	 * Obtiene el nombre completo del Cliente
	 * @return String Nombre completo del Cliente
	 */
	public abstract String getNombreCompletoCliente();

}
