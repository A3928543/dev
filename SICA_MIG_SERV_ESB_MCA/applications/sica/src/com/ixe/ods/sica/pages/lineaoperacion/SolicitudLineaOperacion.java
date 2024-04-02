/*
 * $Id: SolicitudLineaOperacion.java,v 1.10 2008/02/22 18:25:53 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.lineaoperacion;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.pages.SicaPage;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase para la Consulta de Brokers.
 *
 * @author Edgar Leija, Javier Cordova (jcordova)
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:53 $
 */
public abstract class SolicitudLineaOperacion extends SicaPage {

	/**
     * Se ejecuta cada que se activa la p&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
	public void activate(IRequestCycle cycle) {
    	super.activate(cycle);
   		setTitleActionPortletBorder("Mesa de Cambios");
    	setBrokers(new ArrayList());
	 }
	
	/**
     * Sirve para colocar el focus de la p&aacute;gina en el Campo de Texto Deseado.
     * 
     * @return Map
     */
    public Map getFirstTextFieldMap() {
        Map map = new HashMap();
        map.put("textField", "document.Form0.razonSocialTextField");
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
        List brokers;
        convertirAUpperCase();
        if (getRazonSocial().indexOf("%") >= 0) {
            delegate.record("No se permite utilizar el caracter '%' en los criterios de consulta.", null);
            return;
        }
        else if (StringUtils.isEmpty(getRazonSocial().trim())) {
            setBrokers(limpiarPantalla);
            delegate.record("Debe definir un criterio de b\u00fasqueda.", null);
            return;
        }
        else if (StringUtils.isNotEmpty(getRazonSocial().trim()) && getRazonSocial().length() < 3) {
            setBrokers(limpiarPantalla);
            delegate.record("El criterio de b\u00fasqueda debe tener m\u00e1s de 2 caracteres.", null);
            return;
        }
        brokers = getSicaServiceData().findBrokersByRazonSocial(getRazonSocial().trim());
        if (brokers.isEmpty()) {
            setBrokers(limpiarPantalla);
            delegate.record("No se encontr\u00f3 el Broker.", null);
            return;
        }
		setBrokers(brokers);
	}
    
    /**
	 * Convierte lo escrito en los Criterios de Busqueda a Uppercase para evitar fallas en las busquedas 
	 * de la Base de Datos.
	 */
	public void convertirAUpperCase() {
        if (StringUtils.isNotEmpty(getRazonSocial())) {
            setRazonSocial(getRazonSocial().toUpperCase());
        }
    }

	/**
	 * Pasa el flujo de operaci&oacute;n a la P&aacute;gina <code>CapturaLineaOperacion</code>
	 * para el Alta o Edici&oacute;n de la L&iacute;nea de Operaci&oacute;n del Broker seleccionado.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void editar(IRequestCycle cycle) {
		CapturaLineaOperacion nextPage = (CapturaLineaOperacion) cycle.getPage("CapturaLineaOperacion");
		nextPage.setIdBroker(Integer.parseInt((cycle.getServiceParameters()[0]).toString()));
		nextPage.setRazonSocialBroker((cycle.getServiceParameters()[1]).toString());
		nextPage.setPaginaAnterior(getPageName());
		nextPage.activate(cycle);
	}
	
	/**
	 * Activa la lista de Brokers encontrados de acuerdo a los criterios de
	 * B&uacute;squeda preestablecidos.
	 *
	 * @param brokers La lista de Brokers encontrados.
	 */
	public abstract void setBrokers(List brokers);
	
	/**
	 * Obtiene lo establecido como criterio de b&uacute;squeda Raz&oacute;n
	 * Social.
	 *
	 * @return String razonSocial.
	 */
	public abstract String getRazonSocial();
	
	/**
     * Fija el valor de razonSocial.
     *
     * @param razonSocial El valor a asignar.
     */
    public abstract void setRazonSocial(String razonSocial);
	
	 /**
	 * Obtiene el T&iacute;tulo a mostrar en el componente de B&uacute;squeda: 
	 * Mesa de Cambios.
	 *
	 * @return String El T&iacute;tulo.
	 */
	public abstract String getTitleActionPortletBorder();
	
	/**
	 * Fija el T&iacute;tulo a mostrar en el componente de B&uacute;squeda: 
	 * Mesa de Cambios.
	 * 
	 * @param titleActionPortletBorder El T&iacute;tulo.
	 */
	public abstract void setTitleActionPortletBorder(String titleActionPortletBorder);

}
