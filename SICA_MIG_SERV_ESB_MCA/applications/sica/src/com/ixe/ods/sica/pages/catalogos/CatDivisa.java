/*
 * $Id: CatDivisa.java,v 1.10 2008/02/22 18:25:39 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.catalogos;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;

import com.ixe.ods.sica.model.Divisa;

/**
 * Subclase de AbstractCatEdicion que permite al usuario editar o insertar una nueva Divisa.
 *
 * @author Gustavo Gonzalez
 */
public abstract class CatDivisa extends AbstractCatEdicion {

	/**
     * Valida y asigna los datos capturados en la edici&oacute;n de la Divisa.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
	public void actualizar(IRequestCycle cycle) {
		
		try {
			IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
			if (delegate != null && delegate.getHasErrors()) {
				return;
			}
		}
		catch (Exception e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
		}		
		((Divisa) getRegistro()).setIdDivisa(((Divisa) getRegistro()).getIdDivisa().toUpperCase());
		((Divisa) getRegistro()).setDescripcion(((Divisa)getRegistro()).getDescripcion());
		((Divisa) getRegistro()).setTipo(((Divisa)getRegistro()).getTipo());
		((Divisa) getRegistro()).setIcono(((Divisa)getRegistro()).getIcono());
		((Divisa) getRegistro()).setOrden(((Divisa)getRegistro()).getOrden());
		((Divisa) getRegistro()).setIdMoneda(((Divisa)getRegistro()).getIdMoneda());
		((Divisa) getRegistro()).setDivide(((Divisa)getRegistro()).isDivide());
		if (isModoUpdate()) {
			getSicaServiceData().update(getRegistro());
		}
		else {
			getSicaServiceData().store(getRegistro());
		}
		cycle.activate(getNombrePaginaConsulta());
	}
}
