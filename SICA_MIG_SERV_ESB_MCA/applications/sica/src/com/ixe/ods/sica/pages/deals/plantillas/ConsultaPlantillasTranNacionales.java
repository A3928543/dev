/*
 * $Id: ConsultaPlantillasTranNacionales.java,v 1.11 2008/02/22 18:25:01 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals.plantillas;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.sica.model.IPlantillaTranNacional;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.PlantillaTranNacional;
import com.ixe.ods.sica.pages.catalogos.AbstractCatConsulta;
import java.util.Date;
import org.apache.tapestry.IRequestCycle;

/**
 * Clase Base para la Consulta de Plantillas de Transferencias nacionales.

 * @author Jean C. Favila
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:01 $
 */
public abstract class ConsultaPlantillasTranNacionales extends AbstractCatConsulta {

    /**
	 * Llama a la pagina de Alta de Plantillas de Transferencias Nacionales y le carga los parametros iniciales.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
    public void insertar(IRequestCycle cycle) {
        EdicionPlantillaTranNacional nextPage = (EdicionPlantillaTranNacional) cycle.getPage("EdicionPlantillaTranNacional");
        IPlantillaTranNacional plantilla = new PlantillaTranNacional();
        plantilla.setContratoSica(getContratoSica());
        nextPage.setPlantilla(plantilla);
        nextPage.setIdPersona(getIdPersona());
        nextPage.setNombreCliente(getNombreCliente());
        nextPage.activate(cycle);
    }

    /**
     * Cambia el Status de una Plantilla a Inactiva.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void inactivar(IRequestCycle cycle) {
        IPlantillaTranNacional plantilla = (IPlantillaTranNacional) getSicaServiceData().find(PlantillaTranNacional.class,
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
     * Fija el valor de tipoPlantilla.
     *
     * @param tipoPlantilla El valor a asignar.
     */
    public abstract void setTipoPlantilla(String tipoPlantilla);

    /**
     * Regresa el valor de contratoSica.
     *
     * @return ContratoSica.
     */
    public abstract ContratoSica getContratoSica();

    /**
     * Fija el valor de contratoSica.
     *
     * @param contratoSica El valor a asignar.
     */
    public abstract void setContratoSica(ContratoSica contratoSica);

    /**
     * Regresa el valor de paginaAnterior.
     *
     * @return String.
     */
    public abstract String getPaginaAnterior();

    /**
     * Fija el valor de paginaAnterior.
     *
     * @param paginaAnterior El valor a asignar.
     */
    public abstract void setPaginaAnterior(String paginaAnterior);

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
     * Regresa el valor de nombreCliente.
     *
     * @return String.
     */
    public abstract String getNombreCliente();

    /**
     * Fija el valor de nombreCliente.
     *
     * @param mombreCliente El valor a asignar.
     */
    public abstract void setNombreCliente(String mombreCliente);
}
