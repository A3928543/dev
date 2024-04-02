/*
 * $Id: CatTipoPizarron.java,v 1.1 2008/02/22 18:25:39 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.catalogos;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;

import com.ixe.ods.sica.model.TipoPizarron;

/**
 * Catalogo de Tipos de Pizarron para cada canal de operacion.
 *
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.1 $ $Date: 2008/02/22 18:25:39 $
 */
public abstract class CatTipoPizarron extends AbstractCatEdicion {

	/**
	 * Asigna los valores necesarios para la pagina. Agrega un nuevo tipo de pizarron
 	 * para un canal de operacion.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void actualizar(IRequestCycle cycle) {
		IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
		if (delegate != null && delegate.getHasErrors()) {
			return;
		}
		if (StringUtils.isEmpty(((TipoPizarron) getRegistro()).getDescripcion())) {
			getDelegate().record("Defina un nombre para el Pizarr\u00f3n.", null);
			return;
		}
		getSicaServiceData().store(getRegistro());
		cycle.activate(getNombrePaginaConsulta());
	}
}
