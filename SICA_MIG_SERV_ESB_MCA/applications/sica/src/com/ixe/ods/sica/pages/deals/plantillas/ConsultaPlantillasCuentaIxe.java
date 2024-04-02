/*
 * $Id: ConsultaPlantillasCuentaIxe.java,v 1.11 2008/02/22 18:25:01 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals.plantillas;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.sica.model.IPlantillaCuentaIxe;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.PlantillaCuentaIxe;
import com.ixe.ods.sica.pages.catalogos.AbstractCatConsulta;
import java.util.Date;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;

/**
 * Clase Base para la Consulta de Plantillas Cuenta Ixe.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:01 $
 */
public abstract class ConsultaPlantillasCuentaIxe extends AbstractCatConsulta
        implements IExternalPage {

    /**
     * Carga la configuracion inicial de la Pagina cada vez que se tiene acceso a ella desde un
     * link.
     *
     * @param params Los parametros de configuracion inicial enviados desde el link de acceso a la
     *  p&aacute;gina.
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activateExternalPage(Object[] params, IRequestCycle cycle) {
        setTipoPlantilla(cycle.getServiceParameters()[0].toString());
        setContratoSica(getSicaServiceData().
                findContrato(cycle.getServiceParameters()[1].toString()));
        setNombreCliente(cycle.getServiceParameters()[2].toString());
        setPaginaAnterior(cycle.getServiceParameters()[3].toString());
        setIdPersona((Integer) cycle.getServiceParameters()[4]);
    }

    /**
     * Llama a la pagina de Alta de Plantillas cuenta Ixe y le carga los parametros iniciales.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void insertarCuentaIxe(IRequestCycle cycle) {
        EdicionPlantillaCuentaIxe nextPage = (EdicionPlantillaCuentaIxe) cycle.
                getPage("EdicionPlantillaCuentaIxe");
        IPlantillaCuentaIxe plantilla = new PlantillaCuentaIxe();
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
    	IPlantillaCuentaIxe plantilla = (IPlantillaCuentaIxe) getSicaServiceData().
                find(PlantillaCuentaIxe.class, (Integer) cycle.getServiceParameters()[0]);
        plantilla.setStatusPlantilla(Plantilla.STATUS_PLANTILLA_SUSPENDIDA);
        plantilla.setUltimaModificacion(new Date());
        getSicaServiceData().update(plantilla);
    }

    /**
     * Regresa a la pantalla anterior.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cancelarCuentaIxe(IRequestCycle cycle) {
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
