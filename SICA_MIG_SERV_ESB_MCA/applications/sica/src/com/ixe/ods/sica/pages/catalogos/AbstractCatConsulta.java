/*
 * $Id: AbstractCatConsulta.java,v 1.10 2008/02/22 18:25:39 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.catalogos;

import com.ixe.ods.sica.pages.SicaPage;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;

import java.io.Serializable;

/**
 * Superclase abastracta para las p&aacute;ginas de listado de registro de los cat&aacute;logos de la aplicaci&oacute;n.
 * Proporciona la funcionalidad de listado y borrado de registros, tiene m&eacute;todos para insertar o editar registros
 * en una p&aacute;gina que sea subclase de AbstractCatEdicion.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:39 $
 */
public abstract class AbstractCatConsulta extends SicaPage {

    /**
     * Regresa el nombre de la p&aacute;gina de edici&oacute;n de registros.
     *
     * @return AbstractCatEdicion.
     */
    protected AbstractCatEdicion getPaginaEdicion() {    	
        return (AbstractCatEdicion) getRequestCycle().getPage(getNombrePaginaEdicion());
    }

    /**
     * Borra de la base de datos el registro seleccionado.
     *
     * @param cycle El IRequestCycle.
     */
    public void eliminar(IRequestCycle cycle) {
        Object reg = getSicaServiceData().find(getClaseRegistro(), (Serializable) cycle.getServiceParameters()[0]);
        getSicaServiceData().delete(reg);
    }

    /**
     * Regresa el objeto class de acuerdo a nombreClaseRegistro.
     *
     * @return Class.
     */
    protected Class getClaseRegistro() {
        try {
            return Class.forName(getNombreClaseRegistro());
        }
        catch (ClassNotFoundException e) {
            throw new ApplicationRuntimeException(e);
        }
    }

    /**
     * Redirige a la p&aacute;gina de edici&oacute;n, pas&aacute;ndole el registro seleccionado por el usuario.
     *
     * @param cycle El IRequestCycle.
     */
    public void editar(IRequestCycle cycle) {
        AbstractCatEdicion nextPage = getPaginaEdicion();
        Object r = getSicaServiceData().find(getClaseRegistro(), (Serializable) cycle.getServiceParameters()[0]);
        nextPage.setRegistro((Serializable) r);
        nextPage.setModoUpdate(true);
        cycle.activate(nextPage);
    }

    /**
     * Crea una nueva instancia del registro, de acuerdo a nombreClaseRegistro y activa la p&aacute;agina de
     * edici&oacute;n.
     *
     * @param cycle El IRequestCycle.
     */
    public void insertar(IRequestCycle cycle) {
        AbstractCatEdicion nextPage = getPaginaEdicion();
        try {
            nextPage.setRegistro((Serializable) getClaseRegistro().newInstance());
            nextPage.setModoUpdate(false);
        }
        catch (Exception e) {
            throw new ApplicationRuntimeException(e);
        }
        cycle.activate(nextPage);
    }

    /**
     * Regresa el valor de nombreClaseRegistro.
     *
     * @return String.
     */
    public abstract String getNombreClaseRegistro();

    /**
     * Regresa el valor de nombrePaginaEdicion.
     *
     * @return String.
     */
    public abstract String getNombrePaginaEdicion();
}
