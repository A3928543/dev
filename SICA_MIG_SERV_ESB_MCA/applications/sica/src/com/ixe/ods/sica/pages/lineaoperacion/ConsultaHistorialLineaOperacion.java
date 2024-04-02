/*
 * $Id: ConsultaHistorialLineaOperacion.java,v 1.10 2008/02/22 18:25:53 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.lineaoperacion;

import com.ixe.ods.sica.pages.SicaPage;
import org.apache.tapestry.IRequestCycle;
import java.util.List;

/**
 * Clase para la Consulta del Historial de las L&iacute;neas de Operaci&oacute;n.
 *
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:53 $
 */
public abstract class ConsultaHistorialLineaOperacion extends SicaPage {

	/**
	 * Se ejecuta cada que se activa la p&aacute;gina.
     * Carga el Historial de una L&iacute;nea de Operaci&oacute;n.
     *
     * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
		List l = getSicaServiceData().findHistorialLineaOperacionLogByIdLineaOperacion(getIdLineaOperacion());
		setLista(l);
	}

	/**
     * Regresa a la pantalla de Aprobaci&oacute;n de L&iacute;neas de Operaci&oacute;n.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cancelarOperacion(IRequestCycle cycle) {
    	cycle.activate(getPaginaAnterior());
    }

	/**
	 * Activa el Historial de de una L&iacute;nea de Operaci&oacute;n.
	 *
	 * @param lista El Historial.
	 */
    public abstract void setLista(List lista);

    /**
     * Obtiene el Historial de una L&iacute;nea de Operaci&oacute;n.
     *
     * @return List El Historial.
     */
    public abstract List getLista();

    /**
     * Establece el ID de la L&iacute;nea de Operaci&oacute;n sobre la que se va a buscar el Historial.
     *
     * @param idLineaOperacion El ID de la L&iacute;nea de Operaci&oacute;n.
     */
    public abstract void setIdLineaOperacion(Integer idLineaOperacion);

    /**
     * Obtiene el ID de la L&iacute;nea de Operaci&oacute;n sobre la que se va a buscar el Historial.
     *
     * @return Integer el ID de la L&iacute;nea de Operaci&oacute;n.
     */
    public abstract Integer getIdLineaOperacion();

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