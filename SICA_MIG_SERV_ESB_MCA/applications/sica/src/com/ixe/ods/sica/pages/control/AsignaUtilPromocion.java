/*
 * $Id: AsignaUtilPromocion.java,v 1.14.34.1 2013/01/28 18:31:17 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.control;

import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.utils.DateUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;

/**
 * P&aacute;gina que permite asignar utilidades a promoci&oacute;n.
 *
 * @author Gerardo Corzo Herrera
 */
public abstract class AsignaUtilPromocion extends SicaPage {

	/**
	 * Se ejecuta cada que se tiene acceso a la p&aacute;gina.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
		limpiarValores();
	}

	/**
	 * Limpia las variables de la p&aacute;gina.
	 */
	private void limpiarValores() {
		setNoDeal("0");
		setDetEntregamos(new ArrayList());
		setDetRecibimos(new ArrayList());
		setDeal(null);
	}

    /**
     * M&eacut;etodo que realiza la b&uacute;squeda de un determinado Deal.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void search(IRequestCycle cycle) {
        try {
            if (getDelegate().getHasErrors()) {
                return;
            }
            List listaRecibimos = new ArrayList();
            List listaEntregamos = new ArrayList();
            if (getNoDeal() == null) {
                throw new SicaException("Debe ingresar un n\u00famero de Deal para la " +
                        "b\u00fasqueda.", null);
            }
            Integer i = new Integer(getNoDeal());
            setDeal(getSicaServiceData().findDeal(i.intValue()));
            if (getDeal().isInterbancario()) {
                throw new SicaException("No se puede realizar esta operaci\u00f3n sobre deals " +
                        "interbancarios.");
            }
            if (!DateUtils.inicioDia(getDeal().getFechaCaptura()).equals(DateUtils.inicioDia())) {
                setDeal(null);
                throw new SicaException("El deal fue capturado en un d\u00eda anterior.");
            }
            if (getDeal() == null) {
                setDetEntregamos(new ArrayList());
                setDetRecibimos(new ArrayList());
                throw new SicaException("La b\u00fasqueda no encontr\u00f3 ning\u00fan Deal con " +
                        "ese N\u00famero.");
            }
            if (Deal.STATUS_DEAL_CANCELADO.equals(getDeal().getStatusDeal())) {
                setDetEntregamos(new ArrayList());
                setDetRecibimos(new ArrayList());
                throw new SicaException("El Deal tiene Status de Cancelado.", null);
            }
            for (Iterator iter = getDeal().getDetalles().iterator(); iter.hasNext();) {
                DealDetalle element = (DealDetalle) iter.next();
                element.setTmpTcc(element.getTipoCambioMesa());
            }
            for (Iterator iter = getDeal().getRecibimos().iterator(); iter.hasNext();) {
                DealDetalle element = (DealDetalle) iter.next();
                if (!element.getDivisa().getIdDivisa().equals(Divisa.PESO) &&
                        (DealDetalle.STATUS_DET_PROCESO_CAPTURA.equals(
                                element.getStatusDetalleDeal()) ||
                                DealDetalle.STATUS_DET_COMPLETO.equals(
                                        element.getStatusDetalleDeal()) ||
                                DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(
                                        element.getStatusDetalleDeal()))) {
                    listaRecibimos.add(element);
                }
            }
            for (Iterator iter = getDeal().getEntregamos().iterator(); iter.hasNext();) {
                DealDetalle element = (DealDetalle) iter.next();
                if (!element.getDivisa().getIdDivisa().equals(Divisa.PESO) &&
                        (DealDetalle.STATUS_DET_PROCESO_CAPTURA.equals(
                                element.getStatusDetalleDeal()) ||
                                DealDetalle.STATUS_DET_COMPLETO.equals(
                                        element.getStatusDetalleDeal()) ||
                                DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(
                                        element.getStatusDetalleDeal()))) {
                    listaEntregamos.add(element);
                }
            }
            setDetRecibimos(listaRecibimos);
            setDetEntregamos(listaEntregamos);
            String mensaje = "";
            if (!(getDetRecibimos().size() > 0)) {
                mensaje = "El Deal no tiene Detalles Recibimos susceptibles de Modificaci\u00f3n.";
            }
            if (!(getDetEntregamos().size() > 0)) {
                if (StringUtils.isNotEmpty(mensaje)) {
                    mensaje = "El Deal no tiene Detalles Recibimos ni Entregamos susceptibles " +
                            "de Modificaci\u00f3n.";
                }
                else {
                    mensaje = "El Deal no tiene Detalles Entregamos susceptibles de " +
                            "Modificaci\u00f3n.";
                }
            }
            if (StringUtils.isNotEmpty(mensaje)) {
            	getDelegate().record(mensaje, null);
            }
        }
        catch (SicaException e) {
        	getDelegate().record(e.getMessage(), null);
        	if (_logger.isDebugEnabled()) {
        		_logger.debug(e);
        	}
        }
	}

	/**
     * Obtiene el valor de noContrato
     *
     * @return String
     */
    public abstract String getNoDeal();

    /**
     * Asigna el valor para noContrato.
     *
     * @param noDeal N&uacute;mero de Contrato
     */
    public abstract void setNoDeal(String noDeal);


    /**
     * M&eacut;etodo que actualiza los cambios realizados al Tipo de Cambio Mesa del Deal.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
	public void update(IRequestCycle cycle){
        try {
        	
        	if(validarDetallesTcm(getDeal())) {
        		getSicaServiceData().registrarCambioTCM(getDeal());
        	}
        	
        	setDetalles(new ArrayList());
            if (getDetRecibimos().size() > 0) {
            	getDetalles().addAll(getDetRecibimos());
            }
            if (getDetEntregamos().size() > 0) {
            	getDetalles().addAll(getDetEntregamos());
            }
        }
        catch (SicaException e) {
        	getDelegate().record(e.getMessage(), null);
        	debug(e);
        }
	}
	
	//aqui recorrer los detalles y validar por cada detalle
	public boolean validarDetallesTcm(Deal deal) {
		
		boolean sonDetallesTcmValidos = false;
		double porcentVar = getPorcentajeModificaTcm();
		
		
		for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
			DealDetalle det = (DealDetalle) it.next();
			if (det.getTmpTcc() != det.getTipoCambioMesa()) {
				
				if (isTcmValido(det.getTmpTcc(), det.getTipoCambioMesa(), porcentVar, det.getFolioDetalle())) {
					sonDetallesTcmValidos = true;
				}
				else {
					sonDetallesTcmValidos = false;
					break;
				}
			}
		}
		
		return sonDetallesTcmValidos;
	} 

	
	/**
	 * Valida la desviacion del tipo de cambio especificado
	 * y de ser necesario envía mensajes de error correspondientes. o
	 * 
	 * @param tc
	 *            Tipo de cambio a validar
	 * @param tcBase
	 *            Tipo de cambio base contra el que se valida el porcentaje de
	 *            desviacion
	 * 
	 * @return true si tc exede el porcentaje maximo de desviacion contra el
	 *         tcBase, false de otra forma
	 */
	private boolean isTcmValido(double tc, double tcBase,
			double porcentVar, int folioDetalle) {
		
		debug("isTcmValido, tcm=" + tc + ", tcBase=" + tcBase
				+ ", porcentVar=" + porcentVar );
		
		double desviacionMaxima = Redondeador.redondear6Dec(tcBase * porcentVar
				/ 100.0);
		
		double desviacionActual = Redondeador.redondear6Dec(Math.abs(tcBase
				- tc));

		debug("isTcmValido - porcentVar=" + porcentVar
				+ ", desviacionMaxima=" + desviacionMaxima
				+ ", desviacionActual=" + desviacionActual);
		
		if (desviacionActual > desviacionMaxima) {
			
			double tccLimInferior = tcBase - desviacionMaxima;
			double tccLimSuperior = tcBase + desviacionMaxima;

			String mensaje = MessageFormat.format("El TCM del detalle con folio {0} debe estar entre {1} y {2}",
					new Object[] {
							String.valueOf(folioDetalle),
							getCurrencyFormatSix().format(tccLimInferior),
							getCurrencyFormatSix().format(tccLimSuperior) });

			getDelegate().record(mensaje, null);
			return false;
		}

		return true;
	}
	
	
	/**
	 * Obtiene el porcentaje permitido para
	 *  modificar el TCM
	 * @return <code>double</code> con el valor
	 * 		del TCM.
	 */
	private double getPorcentajeModificaTcm() {
		
		try {
			String parametro = null;
			
			parametro = getSicaServiceData().findParametro(
					ParametroSica.DESV_PORC_MAX).getValor();
			
			if(parametro != null) {
				//tratar de convertir
				double porcentajeModificaTcm = Double.parseDouble(parametro);
				
				return porcentajeModificaTcm;
			}
			else {
				throw new SicaException(
						"No se pudo recuperar DESV_PORC_MAX");
			}
	
		}
		catch(NumberFormatException nfe) {
			throw new SicaException("DESV_PORC_MAX no tiene un dato valido");
		}
				
	}

	
    /**
     * Regresa el valor de idDeal.
     *
     * @return int.
     */
	public abstract int getIdDeal();

    /**
     * Fija el valor de idDeal.
     *
     * @param idDeal El valor a asignar.
     */
	public abstract void setIdDeal(int idDeal);

    /**
     * Regresa el valor de deal.
     *
     * @return Deal.
     */
	public abstract Deal getDeal();

    /**
     * Fija el valor de deal.
     *
     * @param deal El valor a asignar.
     */
	public abstract void setDeal(Deal deal);
    /**
     * Regresa el valor de detRecibimos.
     *
     * @return List.
     */
	public abstract List getDetRecibimos();

    /**
     * Fija el valor de detRecibimos.
     *
     * @param detRecibimos El valor a asignar.
     */
	public abstract void setDetRecibimos(List detRecibimos);

    /**
     * Regresa el valor de detEntregamos.
     *
     * @return List.
     */
	public abstract List getDetEntregamos();

    /**
     * Fija el valor de detEntregamos.
     *
     * @param detEntregamos El valor a asignar.
     */
	public abstract void setDetEntregamos(List detEntregamos);

    /**
     * Regresa el valor de detalles.
     *
     * @return List.
     */
	public abstract List getDetalles();

    /**
     * Fija el valor de detalles.
     *
     * @param detalles El valor a asignar.
     */
	public abstract void setDetalles(List detalles);
}
