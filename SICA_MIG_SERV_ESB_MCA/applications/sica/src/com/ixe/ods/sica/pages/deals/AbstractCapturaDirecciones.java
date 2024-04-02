/*
 * $Id: AbstractCapturaDirecciones.java,v 1.2 2009/08/03 21:53:42 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.deals;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.util.Comparator;

import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.SicaConsultaDireccionesPersonaService;
import com.ixe.ods.sica.SicaBusquedaPersonaService;
import com.ixe.ods.sica.SicaRegistroDireccionPersonaService;
import com.ixe.ods.sica.model.Deal;
import com.ixe.bean.bup.Persona;
import com.ixe.bean.bup.Direccion;
import com.ixe.bean.Constantes;

/**
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2009/08/03 21:53:42 $
 */
public abstract class AbstractCapturaDirecciones extends SicaPage {

    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        List direcciones = new ArrayList();
        Persona persona = getSicaBusquedaPersonaService().buscaPersonaPorId(getDeal().
                    getCliente().getIdPersona().intValue());
            List list = (ArrayList) getSicaConsultaDireccionesPersonaService().
                    obtenDirecciones(persona, Constantes.ID_RES_ARRAY_LIST);
            for (Iterator it = list.iterator(); it.hasNext();) {
                Direccion dir = (Direccion) it.next();
                if (getDeal().getDireccion() == null) {
                    if ("S".equals(dir.getEsFiscal())
                            || Constantes.ID_TIPO_DIR_SICA.equals(dir.getIdTipoDireccion())) {
                        //setIdDireccion(dir.getIdDireccion());
                    }
	            }
	            direcciones.add(dir);
	        }
        if (!direcciones.isEmpty()) {
            Collections.sort(direcciones, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Direccion d1 = (Direccion) o1;
                    Direccion d2 = (Direccion) o2;
                    return d1.toString().compareTo(d2.toString());
                }
            });
        }
	        setDireccionesMensajeria(direcciones);
    }

    /**
     * Obtiene una referencia al EJB del servicio de Contratacion
     * <code>SicaConsultaDireccionesPersonaService</code>.
     *
     * @return SicaConsultaDireccionesPersonaService.
     */
    protected SicaConsultaDireccionesPersonaService getSicaConsultaDireccionesPersonaService() {
        return (SicaConsultaDireccionesPersonaService) getApplicationContext().
                getBean("sicaConsultaDireccionesPersonaService");
    }
    
    /**
     * Obtiene una referencia al EJB del servicio de Contratacion
     * <code>SicaBusquedaPersonaService</code>.
     *
     * @return SicaBusquedaPersonaService.
     */
    protected SicaBusquedaPersonaService getSicaBusquedaPersonaService() {
        return (SicaBusquedaPersonaService) getApplicationContext().
                getBean("sicaBusquedaPersonaService");
    }

    /**
     * Obtiene una referencia al EJB del servicio de Contratacion
     * <code>SicaRegistroDireccionPersonaService</code>.
     * @return SicaRegistroDireccionPersonaService.
     */
    protected SicaRegistroDireccionPersonaService getSicaRegistroDireccionPersonaService() {
        return (SicaRegistroDireccionPersonaService) getApplicationContext().
                getBean("sicaRegistroDireccionPersonaService");
    }

    /**
     * Regresa el valor de deal.
     *
     * @return Deal.
     */
    public abstract Deal getDeal();

    /**
     * Fija el valor de deal.
     *
     * @param deal El valor a asignar.
     */
    public abstract void setDeal(Deal deal);
    
    /**
     * Establece el valor de direcciones.
     *
     * @param direccionesMensajeria El valor a asignar.
     */
    public abstract void setDireccionesMensajeria(List direccionesMensajeria);
}
