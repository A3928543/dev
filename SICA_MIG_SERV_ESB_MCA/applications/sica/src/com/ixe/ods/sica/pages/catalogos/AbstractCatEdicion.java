/*
 * $Id: AbstractCatEdicion.java,v 1.10.42.1 2011/04/26 02:43:28 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.catalogos;

import com.ixe.ods.sica.pages.SicaPage;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;

import java.io.Serializable;

/**
 * P&aacute;gina de edici&oacute;n de registros para los cat&aacute;logos del sistema. Funciona en dos modos:
 * inserci&oacute;n y actualizaci&oacute;n. Al terminar de editar los valores del registro, regresa a la p&aacute;gina
 * de consulta, que es una subclase de AbstractCatConsulta.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.10.42.1 $ $Date: 2011/04/26 02:43:28 $
 */
public abstract class AbstractCatEdicion extends SicaPage {

    /**
     * Hace update o store sobre el registro, utilizando el sicaServiceData.
     *
     * @param cycle El IRequestCycle.
     */
    public void actualizar(IRequestCycle cycle) {
        try {
            IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
            if (delegate != null && delegate.getHasErrors()) {
                return;
            }
        }
        catch (Exception e) {
        }
      
        if (isModoUpdate()) {
            getSicaServiceData().update(getRegistro());
        }
        else {
            getSicaServiceData().store(getRegistro());
        }
        
        cycle.activate(getNombrePaginaConsulta());
    }
    
    /**
     * Regresa el valor de modoUpdate.
     *
     * @return boolean.
     */
    public abstract boolean isModoUpdate();

    /**
     * Establece el valor de modoUpdate.
     *
     * @param m El valor a asignar.
     */
    public abstract void setModoUpdate(boolean m);

    /**
     * Regresa el valor de registro.
     *
     * @return Serializable.
     */
    public abstract Serializable getRegistro();

    /**
     * Establece el valor de registro.
     *
     * @param r El valor a asignar.
     */
    public abstract void setRegistro(Serializable r);

    /**
     * Regresa el nombre de la p&aacute;gina de consulta.
     *
     * @return String.
     */
    public abstract String getNombrePaginaConsulta();

    /**
     * Establece el valor de nombrePaginaConsulta.
     *
     * @param nombrePaginaConsulta El valor a asignar.
     */
    public abstract void setNombrePaginaConsulta(String nombrePaginaConsulta);
  
}
