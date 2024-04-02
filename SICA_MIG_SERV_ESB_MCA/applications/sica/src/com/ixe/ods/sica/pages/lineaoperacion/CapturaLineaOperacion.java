/*
 * $Id: CapturaLineaOperacion.java,v 1.10 2008/02/22 18:25:53 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.lineaoperacion;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.LineaOperacion;
import com.ixe.ods.sica.model.LineaOperacionLog;
import com.ixe.ods.sica.pages.SicaPage;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.ValidationConstraint;
import org.springframework.dao.DataAccessException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase para la Captura de L&iacute;neas de Operaci&oacute;n.
 *
 * @author Javier Cordova (jcordova)
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:53 $
 */
public abstract class CapturaLineaOperacion extends SicaPage {

	/**
     * Se ejecuta cada que se activa la p&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
    	super.activate(cycle);
    	configurarRegistro(cycle);
    	setBandera(false);
    }
    
    /**
     * Configura el Registro a capturar de la Linea de Operacion.
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void configurarRegistro(IRequestCycle cycle) {
    	LineaOperacion lineaOperacion = getSicaServiceData().findLineaOperacionByBroker(getIdBroker());
    	if (lineaOperacion != null) {
    		setLineaOperacion(lineaOperacion);
    		setModoUpdate(true);
    	}
    	else {
    		setLineaOperacion(new LineaOperacion());
    		setModoUpdate(false);
    	}
    }

    /**
     * Sirve para colocar el focus de la p&aacute;gina en el Campo de Texto Deseado.
     *
     * @return Map
     */
    public Map getFirstTextFieldMap() {
        Map map = new HashMap();
        map.put("textField", "document.Form0.importe");
        return map;
    }

    /**
     * Actualiza o crea una L&iacute;nea de Operaci&oacute;n para el Broker seleccionado.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void solicitarLineaOperacion(IRequestCycle cycle) {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
    	setLevel(0);
        if (getLineaOperacion().getImporte() < 1) {
            delegate.record("El importe de la l\u00ednea de cr\u00e9dito debe mayor o igual a 1.00.",
                    ValidationConstraint.REQUIRED);
            return;
        }
    	if (!delegate.getHasErrors()) {
    		try {
    			if (getModoUpdate()) {
    				if (LineaOperacion.STATUS_SUSPENDIDA.equals(getLineaOperacion().getStatusLinea())) {
    	                if (getLineaOperacion().getUsoLinea() > 0) {
    	                    delegate.record("Error: La L\u00ednea de Operaci\u00f3n no se puede reactivar debido a " +
    	                            		"que tiene importe en uso por liquidar. Se deber\u00e1 de esperar a " +
    										"que se liquiden los deals interbancarios pendientes.", null);
    	                    return;
    	                }
    	            }
    				getLineaOperacion().setUltimaModificacion(getFechaActual());
                    getLineaOperacion().setStatusLinea(LineaOperacion.STATUS_SOLICITUD);
                    getSicaServiceData().update(getLineaOperacion());
                    insertarLineaOperacionLog(cycle);
    			}
    			else if (getBandera()) {
    				setImporte(getLineaOperacion().getImporte());
    				setLineaOperacion(getSicaServiceData().findLineaOperacionByBroker(getIdBroker()));
    				getLineaOperacion().setImporte(getImporte());
    				getLineaOperacion().setUltimaModificacion(getFechaActual());
                    getLineaOperacion().setStatusLinea(LineaOperacion.STATUS_SOLICITUD);
                    getSicaServiceData().update(getLineaOperacion());
                    insertarLineaOperacionLog(cycle);
    			}
    			else {
    				getLineaOperacion().setIdBroker(getIdBroker());
    				getLineaOperacion().setUsoLinea(0);
    				getLineaOperacion().setNumeroExcepciones(0);
    				getLineaOperacion().setUltimaModificacion(getFechaActual());
    				getLineaOperacion().setStatusLinea(LineaOperacion.STATUS_SOLICITUD);
    				getSicaServiceData().store(getLineaOperacion());
    				insertarLineaOperacionLog(cycle);
    				setBandera(true);
    			}
    			setLevel(1);
    	        delegate.record("Operaci\u00f3n exitosa.", null);
    		}
    		catch (DataAccessException e) {
                if (_logger.isDebugEnabled()) {
                    _logger.debug(e);
                }
                throw new ApplicationRuntimeException("Hubo un error al intentar actualizar o insertar la Linea de Operaci\u00f3n.");
            }
    	}
    }

    /**
     * Inserta un LOG por movimiento hecho sobre una L&iacute;nea de Operaci&oacute;n.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void insertarLineaOperacionLog(IRequestCycle cycle) {
    	Visit visit = (Visit) getVisit();
    	setLineaOperacionLog(new LineaOperacionLog());
        getLineaOperacionLog().setLineaOperacion(getLineaOperacion());
        getLineaOperacionLog().setTipoOper("A");
        getLineaOperacionLog().setImporte(getLineaOperacion().getImporte());
        getLineaOperacionLog().setFechaOperacion(getFechaActual());
        getLineaOperacionLog().setUsuario(visit.getUser());
        getSicaServiceData().store(getLineaOperacionLog());
    }
    
    /**
     * Regresa a la pantalla de SolicitudLineaOperacion.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cancelarOperacion(IRequestCycle cycle) {
    	cycle.activate(getPaginaAnterior());
    }

    /**
     * Obtiene la fecha actual del sistema.
     *
     * @return Date La fecha.
     */
    public Date getFechaActual() {
    	
        return new Date();
    }

    /**
     * Obtiene el Importe Inicial de la L&iacute;nea de Operaci&oacute;n
     * antes de alguna modificaci&oacute;n.
     *
     * @return double El Importe Inicial de la L&iacute;nea de Operaci&oacute;n.
     */
    public double getImporteInicial() {
    	return importeInicial;
    }
    
    /**
	 * Obtiene la P&aacute;gina a la que se debe de regresar en caso de Cancelaci&oacute;n.
	 *
	 * @return String El nombre de la P&aacute;gina.
	 */
	public abstract String getPaginaAnterior();
	
	/**
	 * Obtiene el ID del Broker.
	 *
	 * @return int el ID Persona del Broker.
	 */
	public abstract int getIdBroker();
	
	/**
	 * Obtiene la Raz&oacute;n Social del Broker.
	 *
	 * @return String La Raz&oacute;n Social del Broker.
	 */
	public abstract String getRazonSocialBroker();
	
	/**
     * Obtiene la L&iacute;nea de Operaci&oacute;n.
     *
     * @return LineaOperacion La L&iacute;nea de Operacion.
     */
    public abstract LineaOperacion getLineaOperacion();
    
    /**
     * Obtiene la L&iacute;nea de Operaci&oacute;n Log.
     *
     * @return LineaOperacionLog La L&iacute;nea de Operaci&oacute;n Log.
     */
    public LineaOperacionLog getLineaOperacionLog() {
    	return lineaOperacionLog;
    }
    
    /**
     * Permite saber si la P&aacute;gina est&aacute; en modo de Actualizaci&oacute;n o de Inserci&oacute;n.
     *
     * @return boolean El Modo de la P&aacute;gina.
     */
    public abstract boolean getModoUpdate();
    
    /**
     * Bandera utilizada para la edicion-alta de valores de la Linea de Operacion.
     * @return True o False.
     */
    public abstract boolean getBandera();
    
    /**
     * El importe de la Linea de Operacion.
     * @return El importe.
     */
    public abstract double getImporte();
	
	/**
     * Fija el Importe Inicial de la L&iacute;nea de Operaci&oacute;n
     * antes de alguna modificaci&oacute;n.
     *
     * @param importeInicial El Importe Inicial de la L&iacute;nea de Operaci&oacute;n.
     */
    public void setImporteInicial(double importeInicial) {
    	this.importeInicial = importeInicial;
    }

    /**
	 * Establece la P&aacute;gina a la que se debe de regresar en caso de Cancelaci&oacute;n.
	 *
	 * @param paginaAnterior La P&aacute;gina.
	 */
	public abstract void setPaginaAnterior(String paginaAnterior);

	/**
	 * Fija el ID del Broker.
	 *
	 * @param idBroker El ID a Fijar.
	 */
	public abstract void setIdBroker(int idBroker);

	/**
	 * Fija la Raz&oacute;n Social del Broker.
	 *
	 * @param razonSocialBroker La Raz&oacute;n Social a fijar.
	 */
	public abstract void setRazonSocialBroker(String razonSocialBroker);

    /**
     * Fija la L&iacute;nea de Operaci&oacute;n.
     *
     * @param lineaOperacion La L&iacute;nea de Operaci&oacute;n.
     */
    public abstract void setLineaOperacion(LineaOperacion lineaOperacion);

    /**
     * Fija la L&iacute;nea de Operaci&oacute;n Log.
     *
     * @param lineaOperacionLog La L&iacute;nea de Operaci&oacute;n Log.
     */
    public void setLineaOperacionLog(LineaOperacionLog lineaOperacionLog) {
    	this.lineaOperacionLog = lineaOperacionLog;
    }

    /**
     * Establece si la P&aacute;gina est&aacute; en modo de Actualizaci&oacute;n o de Inserci&oacute;n.
     *
     * @param modoUpdate El Modo de la P&aacute;gina.
     */
    public abstract void setModoUpdate(boolean modoUpdate);
    
    /**
     * Fija la bandera utilizada para la edicion-alta de la Linea de Operacion.
     * @param bandera True o False.
     */
    public abstract void setBandera(boolean bandera);
    
    /**
     * Fija el Importe de la Linea de Operacion.
     * @param importe El valor a asignar.
     */
    public abstract void setImporte(double importe);
    
    /**
     * Fija el nivel de mensaje para el Delegate.
     * 
     * @param level El nivel de mensaje para el delegate.
     */
    public abstract void setLevel(int level);

    /**
     * El registro a Actualizar en LineaOperacionLog de acuerdo a un Aumento
     * o Disminuci&oacute;n de la L&iacute;nea de Operaci&oacute;n.
     */
    private LineaOperacionLog lineaOperacionLog;

    /**
     * El Importe Inicial de la L&iacute;nea de Operaci&oacute;n antes de
     * alguna Modificaci&oacute;n.
     */
    private double importeInicial;
    
}