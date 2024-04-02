/*
 * $Id: MotivosNoProcesar.java,v 1.11.14.1.36.1.24.4 2017/08/30 01:11:39 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tapestry.BaseComponent;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaValidationException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.services.PersonaService;

/**
 * Componente que muestra las razones por las cuales un deal determinado no se puede procesar.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.11.14.1.36.1.24.4 $ $Date: 2017/08/30 01:11:39 $
 */
public abstract class MotivosNoProcesar extends BaseComponent {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(MotivosNoProcesar.class);
    
    /**
     * Regresa una lista de explicaciones por las cuales no es posible procesar el deal:
     * <ul>
     * <li>El deal no tiene detalles asignados a&uacute;n</li>
     * <li>Hay detalles con autorizaciones por Horario pendientes.</li>
     * <li>Hay detalles con autorizaciones por Monto pendientes.</li>
     * <li>No est&aacute;n completos los datos de facturaci&oacute;n.</li>
     * <li>La direcci&oacute;n fiscal no ha sido verificada para facturaci&oacute;n
     * electr&oacute;nica.</li>
     * <li>No est&aacute;n completos los datos de mensajer&iacute;.</li>
     * <li>No est&aacute; balanceado la parte de recibimos con la parte de entregamos.</li>
     * <li>Alg&uacute;n detalle no tiene los datos completos.</li>
     * <li>No se puede procesar un deal de balanceo de reverso desde la captura de deal. Esto debe
     * realizarse completando el reverso.</li>
     * </ul>
     *
     * @return String.
     */
    public List getMotivosNoProcesar() {
        List motivos = new ArrayList();
        Deal deal = getDeal();
        if (deal.getDetalles().isEmpty()) {
            motivos.add("El deal no tiene detalles asignados a\u00fan.");
        }
        if (deal.tieneAutPendientesHorario()) {
            motivos.add("Hay detalles con autorizaciones por Horario pendientes.");
        }
        if (deal.tieneAutPendientesMonto()) {
            motivos.add("Hay detalles con autorizaciones por Monto pendientes.");
        }
        if (deal.isConFactura() && (deal.getDirFactura() == null ||
                StringUtils.isEmpty(deal.getRfcFactura()) ||
                StringUtils.isEmpty(deal.getNombreFactura()))) {
            motivos.add("No est\u00e1n completos los datos de facturaci\u00f3n.");
        }
        if (deal.isConFactura() && deal.getDirFactura() != null &&
                (deal.getDirFactura().getVerificadoFE() == null ||
                        deal.getDirFactura().getVerificadoFE().intValue() == 0)) {
            motivos.add("La direcci\u00f3n fiscal no ha sido verificada para facturaci\u00f3n " +
                    "electr\u00f3nica.");
        }
        if (deal.isMensajeria() && deal.getDireccion() == null) {
            motivos.add("No est\u00e1n completos los datos de mensajer\u00eda.");
        }
        if (Math.abs(deal.getBalance()) >= 0.01) {
            motivos.add("No est\u00e1 balanceado la parte de recibimos con " +
                    "la parte de entregamos.");
        }
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (!DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal())) {
                if (!deal.isProcesableDetalle(det, ((SicaPage) getPage()).getFormasTiposLiq())) {
                    motivos.add("El detalle " + det.getFolioDetalle() +
                            " no tiene los datos completos.");
                }
            }
        }
        if (deal.getFactura() == null || "-".equals(deal.getFactura())) {
            motivos.add("No est\u00e1 definida la opci\u00f3n de Facturaci\u00f3n.");
        }
        if (deal.getComprobante() == null || "-".equals(deal.getComprobante())) {
            motivos.add("No est\u00e1 definida la opci\u00f3n de Comprobante.");
        }
        
        //60057-CFDI 3.3
        if (deal.isConFactura() && (deal.getMetodoPago() == null || "".equals(deal.getMetodoPago().trim())) ) {
        	motivos.add("No est\u00e1 definida la Forma de Pago.");
        }
        
        return motivos;
    }

    /**
     * Regresa el valor del par&aacute;metro deal.
     *
     * @return Deal.
     */
    public abstract Deal getDeal();
    
    /**
     * Gets the error datos faltantes.
     *
     * @return the error datos faltantes
     */
    public String getErrorDatosFaltantes() {
    	String error = "";
    	if (tieneUnaTransferenciaAlExtranjero()) {
    		Integer idPersona = getDeal().getCliente().getIdPersona();
    		try {
	    		getService().isValidaInformacionGeneralPersona(idPersona, null);
    		}
    		catch (SicaValidationException ex) {
    			error = ex.getMessage();
    		}
    		catch (SicaException ex) {
    			log.error("SicaException en getErrorDatosFaltantes() ", ex);
    		}
    	}
    	
    	return error;
    }
    
    /**
     * Tiene una transferencia al extranjero.
     *
     * @return true, if successful
     */
    private boolean tieneUnaTransferenciaAlExtranjero() {
    	boolean tiene = false;
    	if (getDeal() != null && 
    			getDeal().getCliente() != null && 
    			getDeal().getEntregamos() != null && 
    			!getDeal().getEntregamos().isEmpty() &&
    			!getDeal().isInterbancario()) {
    		for (Iterator it = getDeal().getEntregamos().iterator(); it.hasNext();) {
                DealDetalle det = (DealDetalle) it.next();
                if (Constantes.TRANSFERENCIA.equals(det.getClaveFormaLiquidacion())) {
                	tiene = true;
                	break;
                }
            }
    	}
    	
    	return tiene;
    }
    
    /**
     * Gets the service.
     *
     * @return the service
     */
    public abstract PersonaService getService();
    
}
