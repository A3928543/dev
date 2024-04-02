/*
 * $Id: Traspasos.java,v 1.10 2008/02/22 18:25:52 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.traspasos;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.utils.DateUtils;
import org.apache.tapestry.IRequestCycle;

import java.util.*;

/**
 * Clase para la Consulta de Traspasos.
 *
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:52 $
 */
public abstract class Traspasos extends SicaPage {

	/**
     * Se ejecuta cada que se activa la p&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
	public void activate(IRequestCycle cycle) {
    	super.activate(cycle);
   		setTitleActionPortletBorder("Consulta de Traspasos");
    	setTraspasos(new ArrayList());
    	limpiarCriteriosBusqueda();
	 }
	
	/**
     * Sirve para colocar el focus de la p&aacute;gina cuando se carga 
     * en el Campo de Texto Deseado.
     * 
     * @return Map
     */
    public Map getFirstTextFieldMap() {
        Map map = new HashMap();
        map.put("textField", "document.Form0.FechaOperIni");
        return map;
    }

    /**
     * Realiza las operaciones de b&uacute;squeda.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void fetch(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
        List limpiarPantalla = new ArrayList();
        if (getFechaOperIni() == null || getFechaOperFin() == null) {
            delegate.record("Debe seleccionar una Fecha Oper. Ini. y una Fecha Oper Fin.", null);
            return;
        }
        List traspasos = getSicaServiceData().findTraspasosByFechaOper(getFechaOperIni(), DateUtils.finDia(getFechaOperFin()));
        if (traspasos.size() <= 0) {
            setTraspasos(limpiarPantalla);
            delegate.record("No existen Traspasos con los criterios de b\u00fasqueda especificados.", null);
            return;
        }
        setTraspasos(traspasos);
	}

	/**
	 * Pasa el flujo de operaci&oacute;n a la P&aacute;gina <code>CapturaTraspaso</code>
	 * para realizar Traspasos entre Productos de una misma Mesa.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void insertar(IRequestCycle cycle) {
	    CapturaTraspaso nextPage = (CapturaTraspaso) cycle.getPage("CapturaTraspaso");
		nextPage.activate(cycle);
	}
	
	/**
	 * Limpia los campos de los Criterios de B&uacute;squeda.
	 */
	private void limpiarCriteriosBusqueda() {
	    setFechaOperIni(null);
	    setFechaOperFin(null);
	}
	
	/**
	 * Activa la lista de Traspasos encontrados de acuerdo a los criterios de
	 * B&uacute;squeda preestablecidos.
	 *
	 * @param traspasos La lista de Traspasos encontrados.
	 */
	public abstract void setTraspasos(List traspasos);
	
	/**
	 * Obtiene lo establecido como criterio de b&uacute;squeda Fecha Oper. Ini.
	 *
	 * @return Date fechaOperIni.
	 */
	public abstract Date getFechaOperIni();
	
	/**
	 * Fija el criterio de b&uacute;squeda Fecha Oper. Ini.
	 * 
	 * @param fechaOperIni El valor a asignar.
	 */
	public abstract void setFechaOperIni(Date fechaOperIni);
	
	/**
	 * Obtiene lo establecido como criterio de b&uacute;squeda Fecha Oper. Fin.
	 *
	 * @return Date fechaOperFin.
	 */
	public abstract Date getFechaOperFin();
	
	/**
	 * Fija el criterio de b&uacute;squeda Fecha Oper. Fin.
	 * 
	 * @param fechaOperFin El valor a asignar.
	 */
	public abstract void setFechaOperFin(Date fechaOperFin);
	
	 /**
	 * Obtiene el T&iacute;tulo a mostrar en el componente de B&uacute;squeda: 
	 * Consulta de Traspasos.
	 *
	 * @return String El T&iacute;tulo.
	 */
	public abstract String getTitleActionPortletBorder();
	
	/**
	 * Fija el T&iacute;tulo a mostrar en el componente de B&uacute;squeda: 
	 * Consulta de Traspasos.
	 * 
	 * @param titleActionPortletBorder El T&iacute;tulo.
	 */
	public abstract void setTitleActionPortletBorder(String titleActionPortletBorder);
}
