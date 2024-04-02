/*
 * $Id: VerRepDealsBanxico.java,v 1.13 2008/12/26 23:17:34 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.dao.BitacoraEnviadasDao;
import com.ixe.ods.sica.model.BitacoraEnviadas;
import com.ixe.ods.sica.pages.SicaPage;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;


/**
 * Clase para el Reenv&iacute;o de Reportes de Operaciones a Banxico con Error. 
 * 
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.13 $ $Date: 2008/12/26 23:17:34 $
 */
public abstract class VerRepDealsBanxico extends SicaPage {

	/**
     * Se ejecuta cada que se activa la p&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
	   	setOperacionExitosa(false);
	   	obtenerReportesConError();
	}
	
	/**
	 * Obtiene los registros de reportes de operaciones a banxico que tienen error.
	 */
	private void obtenerReportesConError() {
	    List reportes = getBitacoraEnviadasDao().findBitacorasEnviadasConError();
        if (reportes != null) {
            setReportes(reportes);
        }
        else {
            setReportes(new ArrayList());
        }
	}
	
	/**
	 * Reenv&iacute;a un Registro de Reporte de Operacion si es que el Error 
	 * respondido por Banxico es -100, -101, -103, -106 o -107.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void reenviar(IRequestCycle cycle) {
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
		setOperacionExitosa(false);
	    String idConf = (cycle.getServiceParameters()[0]).toString();
	    String divisa = (cycle.getServiceParameters()[1]).toString();
	    
	    try {
	        BitacoraEnviadas be = getBitacoraEnviadasDao().findBitacoraEnviadaByIdConfAndDivisa(idConf, divisa);
	        be.setStatus(BitacoraEnviadas.STATUS_ENVIAR);
    	    be.setError(new Integer(0));
    	    getSicaServiceData().update(be);
			setOperacionExitosa(true);
			obtenerReportesConError();
			delegate.record("Se cambi\u00f3 con \u00e9xito el Status = 'Enviar' del Registro para su Reenv\u00edo a Banxico.", null);
		}
		catch (DataAccessException e) {
	        if (_logger.isDebugEnabled()) {
		        _logger.debug(e);
            }
            throw new ApplicationRuntimeException("Hubo un error al intentar Reenviar el Registro.");
	    }
	}
	
	/**
	 * Redirige el flujo de operaci&oacute;n a la P&aacute;gina de Edici&oacute;n 
	 * de Registros de Operaciones para su posterior Reenv&iacute;o.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void editar(IRequestCycle cycle) {
	    String idConf = (cycle.getServiceParameters()[0]).toString();
	    String divisa = (cycle.getServiceParameters()[1]).toString();
	    int error = Integer.parseInt((cycle.getServiceParameters()[2]).toString());
	    ModRepDealsBanxico nextPage = (ModRepDealsBanxico) cycle.getPage("ModRepDealsBanxico");
		nextPage.setIdConf(idConf);
		nextPage.setDivisa(divisa);
		nextPage.setError(error);
		nextPage.setPaginaAnterior(getPageName());
		nextPage.activate(cycle);
	}

    /**
     * Regresa la referencia al bean bitacoraEnviadasDao.
     *
     * @return BitacoraEnviadasDao.
     */
    private BitacoraEnviadasDao getBitacoraEnviadasDao() {
        return (BitacoraEnviadasDao) getApplicationContext().getBean("bitacoraEnviadasDao");
    }

	/**
	 * Obtiene la lista de Reportes encontrados de acuerdo a los criterios de
	 * B&uacute;squeda preestablecidos.
	 *
	 * @return List La lista de Reportes encontrados.
	 */
	public abstract List getReportes();
	
	/**
	 * Activa la lista de Reportes encontrados de acuerdo a los criterios de
	 * B&uacute;squeda preestablecidos.
	 *
	 * @param reportes La lista de Reportes encontrados.
	 */
	public abstract void setReportes(List reportes);
	
	/**
	 * Fija la bandera que le indica al usuario a trav&eacute;s de un mensaje en pantalla 
	 * si su operaci&oacute;n fue exitosa o no.
	 * 
	 * @param operacionExitosa
	 */
	public abstract void setOperacionExitosa(boolean operacionExitosa);	
}