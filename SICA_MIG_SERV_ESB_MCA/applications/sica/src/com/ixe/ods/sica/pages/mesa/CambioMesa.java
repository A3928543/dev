/*
 * $Id: CambioMesa.java,v 1.10 2008/02/22 18:25:32 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.mesa;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.pages.SicaPage;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * @author Jean C. Favila
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:32 $
 */
public abstract class CambioMesa extends SicaPage {

	/**
	 * Activa el submit de la p&aacute;gina.
	 * 
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
    public void submit(IRequestCycle cycle) {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
    	setOperacionExitosa(false);
    	try {
	        ((Visit)getPage().getVisit()).setIdCanal(getSelectedCanal().getIdCanal());
	        ((Visit)getPage().getVisit()).setIdMesaCambio(getSelectedCanal().getMesaCambio().getIdMesaCambio());
	        setOperacionExitosa(true);
			delegate.record("Se Cambi\u00f3 con \u00e9xito el Canal-Mesa.", null);
	    }
		catch (Exception e) {
	        if (_logger.isDebugEnabled()) {
		        _logger.debug(e);
	        }
	        throw new ApplicationRuntimeException("No se pudo realizar la operaci\u00f3n. Favor de intentar de nuevo.");
	    }
    }
    
    /**
     * Llena el combo de Canales.
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getCanalesModel() {
        return new RecordSelectionModel(((Visit) getVisit()).getCanales(), "idCanal", "nombre");
    }

    /**
     * Obtiene el Canal seleccionado por el Usuario del combo de Canales que puede ver.
     *
     * @return Canal El Canal seleccionado.
     */
    public abstract Canal getSelectedCanal();
    
    /**
	 * Fija la bandera que le indica al usuario a trav&eacute;s de un mensaje en pantalla 
	 * si su operaci&oacute;n fue exitosa o no.
	 * 
	 * @param operacionExitosa
	 */
	public abstract void setOperacionExitosa(boolean operacionExitosa);
    
}
