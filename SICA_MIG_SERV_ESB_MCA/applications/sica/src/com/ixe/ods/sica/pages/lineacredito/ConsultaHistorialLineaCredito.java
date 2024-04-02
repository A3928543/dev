/*
 * $Id: ConsultaHistorialLineaCredito.java,v 1.13 2008/12/26 23:17:31 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.lineacredito;

import java.util.List;

import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.pages.SicaPage;

/**
 * Clase para la Consulta del Historial de las L&iacute;neas de Cr&eacute;dito.
 *
 * @author Gerardo Corzo, Javier Cordova (jcordova)
 * @version $Revision: 1.13 $ $Date: 2008/12/26 23:17:31 $
 */
public abstract class ConsultaHistorialLineaCredito extends SicaPage {

	/**
	 * Se ejecuta cada que se activa la p&aacute;gina.
     * Carga el Historial de una L&iacute;nea de Cr&eacute;dito de un Producto.
     *
     * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
		setLista(getLineaCambioServiceData().
                findHistoriaLineaCambioLogByIdLineaCredito(getIdLineaCredito()));
	}

	/**
     * Regresa a la pantalla de Aprobaci&oacute;n de L&iacute;neas de Cr&eacute;dito.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cancelarOperacion(IRequestCycle cycle) {
    	cycle.activate(getPaginaAnterior());
    }

	/**
	 * Activa el Historial de de una L&iacute;nea de Cr&eacute;dito.
	 *
	 * @param lista El Historial.
	 */
    public abstract void setLista(List lista);

    /**
     * Obtiene el Historial de una L&iacute;nea de Cr&eacute;dito.
     *
     * @return List El Historial.
     */
    public abstract List getLista();

    /**
     * Establece el ID de la L&iacute;nea de Cr&eacute;dito sobre la que se va a buscar el
     * Historial.
     *
     * @param idLineaCredito El ID de la L&iacute;nea de Cr&eacute;dito.
     */
    public abstract void setIdLineaCredito(Integer idLineaCredito);

    /**
     * Obtiene el ID de la L&iacute;nea de Cr&eacute;dito sobre la que se va a buscar el
     * Historial.
     *
     * @return Integer el ID de la L&iacute;nea de Cr&eacute;dito.
     */
    public abstract Integer getIdLineaCredito();

    /**
	 * Obtiene la P&aacute;gina a la que se debe de regresar en caso de Cancelaci&oacute;n.
	 *
	 * @return String El nombre de la P&aacute;gina.
	 */
	public abstract String getPaginaAnterior();

	/**
	 * Establece la P&aacute;gina a la que se debe de regresar en caso de Cancelaci&oacute;n.
	 *
	 * @param paginaAnterior La P&aacute;gina.
	 */
	public abstract void setPaginaAnterior(String paginaAnterior);
	
    /**
     * Regresa el valor de corporativo.
     *
     * @return String.
     */
	public abstract String getCorporativo();

    /**
     * Fija el valor de corporativo.
     *
     * @param corporativo El valor a asignar.
     */
	public abstract void setCorporativo(String corporativo);

}
