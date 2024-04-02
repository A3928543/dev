/*
 * $Id: RenovarFechaLimCaptCont.java,v 1.12 2008/02/22 18:25:33 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2007 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.mesa;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.pages.SicaPage;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.springframework.dao.DataAccessException;

/**
 * Clase para la Renovaci&oacute;n de Fecha L&iacute;mite Captura Contrato en los Deals 
 * que se quedaron sin Contrato SICA por vencerse la fecha antes mencionada, y por consiguiente, 
 * que no se pudieron terminar y procesar. Tambi&eacute;n, permite cancelar un Deal.
 *
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:33 $
 */
public abstract class RenovarFechaLimCaptCont extends SicaPage {

	/**
	 * Se ejecuta cada que se activa la p&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
		listar(cycle);
	}
	
	/**
	 * Obtiene los deals por renovar.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findDealsNoCancelYSinContrato(). 
     */
	public void listar(IRequestCycle cycle) {
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
		setDealsARenovar(getSicaServiceData().findDealsNoCancelYSinContrato());
		if (!(getDealsARenovar().size() > 0)) {
			delegate.record("No hay Deals No Cancelados sin Contrato SICA.", null);
		}
	}
	
	/**
	 * Operaci&oacute;n de Renovaci&oacute;n de la Fecha L&iacute;mite Captura Contrato de
     * un Deal.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void operacionRenovar(IRequestCycle cycle) {
		Integer idDeal = new Integer((cycle.getServiceParameters()[0]).toString());
		cambiarFechaLimCaptCont(idDeal);
	}
	
	/**
	 * Operaci&oacute;n de Cancelaci&oacute;n de un Deal.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void operacionCancelar(IRequestCycle cycle) {
        try {
            Integer idDeal = new Integer((cycle.getServiceParameters()[0]).toString());
            getWorkFlowServiceData().marcarDealCancelado(idDeal.intValue());
            listar(cycle);
        }
        catch (SicaException e) {
            getDelegate().record(e.getMessage(), null);
            debug(e);
        }
    }

	/**
	 * Apoya al m&eacute;todos <code>operacionRenovar</code> para Renovar la Fecha
     * L&iacute;mite Captura Contrato de un Deal.
	 *
	 * @param idDeal El Deal a modificar.
	 */
	private void cambiarFechaLimCaptCont(Integer idDeal) {
		try {
			Deal deal = (Deal) getSicaServiceData().find(Deal.class, idDeal);
			deal.setFechaLimiteCapturaContrato(getFechaRenovada());
			getSicaServiceData().update(deal);
			listar(getRequestCycle());
		}
		catch (DataAccessException e) {
            debug(e);
		   	throw new ApplicationRuntimeException(
                       "Hubo un error al intentar efectuar la operaci\u00f3n sobre el Deal.");
	    }
	}
	
	/**
     * Obtiene el valor por "default" de la Fecha L&iacute;mite de Captura de Contratos
     * especificada en sc_parametro. Con el valor anterior, calcula la Fecha L&iacute;mite 
     * Captura Contrato Renovada de un Deal.
     *
     * @return Date La Fecha renovada.
     */
    private Date getFechaRenovada() {
    	int fechaLimCaptCont = getSicaServiceData().getFechaLimiteCapturaDeal();
    	Calendar gc = new GregorianCalendar();
    	gc.add(Calendar.MINUTE, fechaLimCaptCont);
    	return gc.getTime();
    }

    /**
     * Obtiene los Deals a Renovar (Fecha L&iacute;mite Captura Contrato) / Cancelar.
     *
     * @return List Los Deals.
     */
	public abstract List getDealsARenovar();

	/**
     * Fija los Deals a Renovar (Fecha L&iacute;mite Captura Contrato) / Cancelar.
     *
     * @param dealsARenovar Los datos a fijar.
     */
	public abstract void setDealsARenovar(List dealsARenovar);
	
}