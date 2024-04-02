/*
 * $Id: RepDeal.java,v 1.10 2008/02/22 18:25:38 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.reportes;

import com.ixe.ods.sica.SicaService;
import com.ixe.ods.sica.pages.SicaPage;
import org.apache.tapestry.IRequestCycle;

import java.io.IOException;
import java.io.InputStream;

/**
 * P&aacute;gina que permite al usuario mandar por mail
 * el resumen del reporte del deal
 * 
 * @author Gerardo Corzo Herrera
 */
public abstract class RepDeal extends SicaPage{

    /**
     * M&eacute;todo que manda por mail el resumen del reporte del Deal
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
	public void mail(IRequestCycle cycle) {
		Object parameters[] = cycle.getServiceParameters();	
		String id = (String) parameters[2];
		SicaService sicaService = (SicaService) getApplicationContext().getBean("sicaService");
        sicaService.enviarDealPorMail(Long.valueOf(id));
	}

    /**
     * Regresa null.
     *
     * @return InputStream.
     * @throws IOException Si ocurre alg&uacute;n error.
     */
    public InputStream getInputStream() throws IOException {
		return null;
	}

	/**
     * Regresa el valor de idDeal.
     *
     * @return String.
     */  
    public abstract String getIdDeal();

    /**
     * Fija el valor de idDeal.
     *
     * @param idDeal El valor a asignar.
     */
    public abstract void setIdDeal(String idDeal);
}
