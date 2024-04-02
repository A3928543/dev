/*
 * $Id: CatGrupoTrabajo.java,v 1.3 2010/05/14 21:37:02 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.catalogos;

import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;

import com.ixe.ods.sica.model.GrupoTrabajo;

/**
 *
 * @author Israel Rebollar
 * @version  $Revision: 1.3 $ $Date: 2010/05/14 21:37:02 $
 */
public abstract class CatGrupoTrabajo extends AbstractCatEdicion {

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
            warn(e);
        }
        if (isModoUpdate()) {
            getSicaServiceData().update(getRegistro());
        }
        else {
        	List registros = getSicaServiceData().findAll(GrupoTrabajo.class);
        	for (Iterator iter = registros.iterator(); iter.hasNext();) {
				GrupoTrabajo reg = (GrupoTrabajo) iter.next();
				if (reg.equals((GrupoTrabajo) getRegistro())) {
					getDelegate().record("El nuevo grupo de trabajo que intenta " +
							"registrar ya existe con la misma clave, favor de proporcionar " +
							"una clave diferente para el nuevo grupo.", null);
					return;
				}
			}
            getSicaServiceData().store(getRegistro());
        }
        
        cycle.activate(getNombrePaginaConsulta());
	}
	
	/**
     * Regresa la expresi&oacute;n regular para validar el nuevo correo electr&oacute;nico.
     *
     * @return String.
     */
    public String getPatronEmail() {
        return "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|" +
                "(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    }
}
