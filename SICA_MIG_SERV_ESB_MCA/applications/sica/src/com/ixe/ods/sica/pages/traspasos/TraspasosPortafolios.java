/*
 * $Id: TraspasosPortafolios.java,v 1.10 2008/02/22 18:25:52 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
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
public abstract class TraspasosPortafolios extends SicaPage {

	/**
     * Se ejecuta cada que se activa la p&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
	public void activate(IRequestCycle cycle) {
    	super.activate(cycle);
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
    	List limpiarPantalla = new ArrayList();
    	setConsultaRealizada(false);
    	setTitulo("Traspasos Realizados el d\u00eda de hoy.");
    	setTitleActionPortletBorder("Consulta de Traspasos");
    	limpiarCriteriosBusqueda();
        setTraspasosPortafolios(getSicaServiceData().findTraspasosPortafoliosByFechaOperacion(
                DateUtils.inicioDia(), DateUtils.finDia()));             
       	if (getTraspasosPortafolios().size() <= 0 || getTraspasosPortafolios() == null) {
             setTraspasosPortafolios(limpiarPantalla);
             delegate.record("No se ha realizado ning\u00fan Traspaso el d\u00eda de hoy.", null);
         }
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
        setConsultaRealizada(true);
        setTitulo("Resultados de la b\u00fasqueda");
        if (getFechaOperIni() == null || getFechaOperFin() == null) {
            delegate.record("Debe seleccionar una Fecha Oper. Ini. y una Fecha Oper Fin.", null);
            return;
        }
        setTraspasosPortafolios(getSicaServiceData().findTraspasosPortafoliosByFechaOperacion(
                DateUtils.inicioDia(getFechaOperIni()),  DateUtils.finDia(getFechaOperFin())));           
        if (getTraspasosPortafolios().size() <= 0 || getTraspasosPortafolios() == null) {
        	setTraspasosPortafolios(limpiarPantalla);
        	delegate.record("No existen Traspasos con los criterios de b\u00fasqueda especificados.", null);
        }
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
	 * Obtiene la lista de los traspasos encontrados despues de haber
	 * realizado una busqueda.
     *
     * @return List.
	 */
	public abstract List getTraspasos();
	
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

	
	/**
	 * Obtiene los Traspasos de tipo TraspasoMesa
	 * 
	 *@return List lista de traspasos 
	 * */
	public abstract List getTraspasosPortafolios();
	
	/**
	 * Asigna los Traspasos de tipo TraspasoMesa 
	 * 
	 * @param traspasosPortafolios lista de traspasos
	 * */
	public abstract void setTraspasosPortafolios(List traspasosPortafolios);
	
	/**
	 * Obtiene el valor para una busqueda realizada.
	 * 
	 * @return boolean 
	 * */
	public abstract boolean getConsultaRealizada();
	
	/**
	 * Asigna si se ha realizado una busqueda.
	 * 
	 * @param consultaRealizada El valor a asignar.
	 */
	public abstract void setConsultaRealizada(boolean consultaRealizada);

    /**
     * Regresa el valor de titulo.
     *
     * @return String.
     */
    public abstract String getTitulo();

    /**
     * Establece el valor de titulo.
     *
     * @param titulo El valor a asignar.
     */
    public abstract void setTitulo(String titulo);
}
