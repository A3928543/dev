/*
 * $Id: ConsultaPlantillasIntl.java,v 1.11 2008/02/22 18:25:01 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.deals.plantillas;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.sica.model.IPlantillaIntl;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.PlantillaIntl;
import com.ixe.ods.sica.pages.catalogos.AbstractCatConsulta;
import java.util.Date;
import org.apache.tapestry.IRequestCycle;

/**
 * Clase Base para la Consulta de Plantillas de Transferencias Internacionales.
 *
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.11 $ $Date: 2008/02/22 18:25:01 $
 */
public abstract class ConsultaPlantillasIntl extends AbstractCatConsulta {

    /**
	 * Llama a la pagina de Alta de Plantillas de Transferencias Internacionales y le carga los
     * par&aacute;metros iniciales.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
    public void insertar(IRequestCycle cycle) {
		EdicionPlantillaIntl nextPage = (EdicionPlantillaIntl) cycle.
                getPage("EdicionPlantillaIntl");
        IPlantillaIntl plantilla = new PlantillaIntl();
        plantilla.setContratoSica(getContratoSica());
        nextPage.setPlantilla(plantilla);
        nextPage.setIdPersona(getIdPersona());
        nextPage.setNombreCliente(getNombreCliente());
        nextPage.setPaginaAnterior(getPageName());
        nextPage.activate(cycle);
    }

    /**
     * Cambia el Status de una Plantilla a Inactiva.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void inactivar(IRequestCycle cycle) {
        IPlantillaIntl plantilla = (IPlantillaIntl) getSicaServiceData().find(PlantillaIntl.class,
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
     * Establece el Tipo de Plantilla.
     *
     * @param tipoPlantilla El valor a asignar.
     */
    public abstract void setTipoPlantilla(String tipoPlantilla);
    
	/**
	 * Establece el Contrato SICA.
	 * @param contratoSica El valor a asignar.
	 */
    public abstract void setContratoSica(ContratoSica contratoSica);

	/**
	 * Obtiene el Contrato SICA.
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
