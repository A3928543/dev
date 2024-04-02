/*
 * $Id: ConsultaPlantillasNacionales.java,v 1.11 2008/02/22 18:25:01 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.deals.plantillas;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.sica.model.IPlantillaNacional;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.PlantillaNacional;
import com.ixe.ods.sica.pages.catalogos.AbstractCatConsulta;
import java.util.Date;
import org.apache.tapestry.IRequestCycle;

/**
 * Clase Base para la Consulta de Plantillas.
 *
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.11 $ $Date: 2008/02/22 18:25:01 $
 */
public abstract class ConsultaPlantillasNacionales extends AbstractCatConsulta {

	/**
	 * Llama a la pagina de Alta de Plantillas y le carga los parametros iniciales.
	 * 
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
    public void insertar(IRequestCycle cycle) {
		super.insertar(cycle);
		EdicionPlantillaNacional nextPage = (EdicionPlantillaNacional) getPaginaEdicion();
		((Plantilla) nextPage.getRegistro()).setContratoSica(getContratoSica());
        nextPage.setIdPersona(getIdPersona());
		nextPage.setNombreCliente(getNombreCliente());
	}
    
    /**
     * Cambia el Status de una Plantilla a Inactiva.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void inactivar(IRequestCycle cycle) {
    	IPlantillaNacional plantilla = (IPlantillaNacional) getSicaServiceData().find(PlantillaNacional.class,
                (Integer) cycle.getServiceParameters()[0]);
        plantilla.setStatusPlantilla(Plantilla.STATUS_PLANTILLA_SUSPENDIDA);
        plantilla.setUltimaModificacion(new Date());
        getSicaServiceData().update(plantilla);
    }

	/**
     * Regresa a la pantalla anterior.
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cancelar(IRequestCycle cycle) {
    	cycle.activate(getPaginaAnterior());
    }

	/**
	 * Establece el tipo de Plantilla.
	 * 
	 * @param tipoPlantilla El valor a asignar.
	 */
    public abstract void setTipoPlantilla(String tipoPlantilla);

	/**
	 * Establece el Contrato SICA de la Plantila.
	 * 
	 * @param contratoSica El valor a asignar.
	 */
    public abstract void setContratoSica(ContratoSica contratoSica);

	/**
	 * Obtiene el Contrato SICA de la Plantilla.
	 * 
	 * @return El Contrato SICA.
	 */
    public abstract ContratoSica getContratoSica();
	
    /**
     * Establece la Pagina Anterior desde donde se tuvo acceso a la actual.
     * 
     * @param paginaAnterior El valor a asignar.
     */
	public abstract void setPaginaAnterior(String paginaAnterior);
	
	/**
	 * Obtiene la Pagina Anterior desde donde se tuvo acceso a la actual.
	 * 
	 * @return La Pagina Anterior
	 */
	public abstract String getPaginaAnterior();

    /**
     * Regresa el valor de idPersona.
     *
     * @return Integer.
     */
    public abstract Integer getIdPersona();

    /**
     * Establece el valor de idPersona.
     *
     * @param idPersona El valor a asignar.
     */
    public abstract void setIdPersona(Integer idPersona);
    
    /**
	 * Establece el Nombre del Cliente del cual se quiere hacer una Plantilla.
	 * 
	 * @param mombreCliente El Nombre del Cliente.
	 */
	public abstract void setNombreCliente(String mombreCliente);
	
	/**
	 * Obtiene el Nombre del Cliente del cual se quiere hacer una Plantilla.
	 * 
	 * @return El Nombre del Cliente.
	 */
	public abstract String getNombreCliente();
}