/*
 * $Id: AprobacionLineaOperacion.java,v 1.11 2008/02/22 18:25:53 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.lineaoperacion;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Broker;
import com.ixe.ods.sica.model.LineaOperacion;
import com.ixe.ods.sica.model.LineaOperacionLog;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pages.lineacredito.CapturaObservaciones;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.springframework.dao.DataAccessException;

/**
 * Clase para la Autorizaci&oacute;n, y Suspensi&oacute;n de L&iacute;neas de Operaci&oacute;n.
 *
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:53 $
 */
public abstract class AprobacionLineaOperacion extends SicaPage {

	/**
	 * Se ejecuta cada que se activa la p&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
        limpiarTodo();
		setModo((String) cycle.getServiceParameters()[0]);
		setLineasOperacion(new ArrayList());
		// Verificando si se trata de la Mesa de Cambios
		if (FacultySystem.SICA_APROB_LINOPER_M.equals(getModo())){
			setTitleActionPortletBorder("Mesa de Cambios");
		}
		// Verificando si se trata del Area de Credito
		else {
			setTitleActionPortletBorder("Departamento de Cr&eacute;dito");
		}
		setEstatus(LineaOperacion.STATUS_SOLICITUD);
	}
	
	/**
	 * Realiza las operaciones de b&uacute;squeda de L&iacute;neas de Operaci&oacute;n por
     * Raz&oacute;n Social y por Estatus.
     *
     * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void fetch(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        convertirAUpperCase();
        if (getRazonSocial().indexOf("%") >= 0) {
            delegate.record("No se permite utilizar el caracter '%' en los criterios de consulta.",
                    null);
            return;
        }
        else if (StringUtils.isNotEmpty(getRazonSocial()) && getRazonSocial().length() < 3) {
            delegate.record("El criterio de b\u00fasqueda Raz\u00f3n Social debe tener m\u00e1s "
                    + "de 2 caracteres.", null);
            return;
        }
        if (((getLimiteInferior() > 0) && !(getLimiteSuperior() > 0))
                || (!(getLimiteInferior() > 0) && (getLimiteSuperior() > 0))) {
            delegate.record("Debe llenar los dos L\u00edmites del Porcentaje de Uso, Inferior y "
                    + "Superior, no s\u00f3lo uno.", null);
            return;
        }
        if (!delegate.getHasErrors()) {
            if (StringUtils.isNotEmpty(getRazonSocial())) {
                //Buscando Brokers.
                List brokers = getSicaServiceData().findBrokersByRazonSocial(getRazonSocial().
                        trim());
                if (brokers.isEmpty()) {
                    setLineasOperacion(new ArrayList());
                    delegate.record("No se encontr\u00f3 el Broker.", null);
                    return;
                }
                setBrokers(brokers);
                //Buscando Lineas por Brokers y por Status.
				List lineasBrokerEstatus = getSicaServiceData().
                        findLineasOperacionByBrokersAndEstatus(getBrokers(), getEstatus());
				if (!(getLimiteInferior() > 0) && !(getLimiteSuperior() > 0)) {
					setLineasOperacion(lineasBrokerEstatus);
                }
                else {
                    //Buscando Lineas por Porcentaje de Uso.
                    List lineasOperacionPorcentajeUso = new ArrayList();
                    for (Iterator iterator = lineasBrokerEstatus.iterator(); iterator.hasNext();) {
                        LineaOperacion lineaOperacion = (LineaOperacion) iterator.next();
                        double limiteInferior = (lineaOperacion.getImporte()
                                * getLimiteInferior()) / 100;
                        double limiteSuperior = (lineaOperacion.getImporte()
                                * getLimiteSuperior()) / 100;
                        if ((limiteInferior <= lineaOperacion.getUsoLinea())
                                && (lineaOperacion.getUsoLinea() <= limiteSuperior)) {
                            lineasOperacionPorcentajeUso.add(lineaOperacion);
                        }
                    }
                    setLineasOperacion(lineasOperacionPorcentajeUso);
                }
            }
			else {
                //Buscando Lineas por Status.
                List lineasStatus = getSicaServiceData().findLineasOperacionByStatus(getEstatus());
                if (!(getLimiteInferior() > 0) && !(getLimiteSuperior() > 0)) {
                    setLineasOperacion(lineasStatus);
                }
                else {
                    //Buscando Lineas por Porcentaje de Uso.
                    List lineasOperacionPorcentajeUso = new ArrayList();
                    for (Iterator iterator = lineasStatus.iterator(); iterator.hasNext();) {
                        LineaOperacion lineaOperacion = (LineaOperacion) iterator.next();
                        double limiteInferior = (lineaOperacion.getImporte()
                                * getLimiteInferior()) / 100;
                        double limiteSuperior = (lineaOperacion.getImporte()
                                * getLimiteSuperior()) / 100;
                        if ((limiteInferior <= lineaOperacion.getUsoLinea())
                                && (lineaOperacion.getUsoLinea() <= limiteSuperior)) {
                            lineasOperacionPorcentajeUso.add(lineaOperacion);
                        }
                    }
                    setLineasOperacion(lineasOperacionPorcentajeUso);
                }
            }
            if (!(getLineasOperacion().size() > 0)) {
                delegate.record("No hay L\u00edneas de Operaci\u00f3n con los Criterios de "
                        + "B\u00fasqueda especificados.", null);
            }
        }
    }

    /**
     * Convierte lo escrito en los Criterios de Busqueda a Uppercase para evitar fallas en las
     * b&uacute;squedas de la Base de Datos.
     */
	public void convertirAUpperCase() {
        if (StringUtils.isNotEmpty(getRazonSocial())) {
            setRazonSocial(getRazonSocial().toUpperCase());
        }
    }

	/**
	 * Operaci&oacute;n de Autorizaci&oacute;n de L&iacute;neas de Operaci&oacute;n.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
     */
    public void operacionAutorizar(IRequestCycle cycle) {
        Integer idLineaOperacion = new Integer((cycle.getServiceParameters()[0]).toString());
        setObservaciones("");
        cambiarEstatusLineaOperacion(LineaOperacion.STATUS_AUTORIZADA, "C", idLineaOperacion);
    }

    /**
     * Operaci&oacute;n de Suspensi&oacute;n de L&iacute;neas de Operaci&oacute;n.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void operacionSuspender(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        Integer idLineaOperacion = new Integer((cycle.getServiceParameters()[0]).toString());
        LineaOperacion lineaOperacion = (LineaOperacion) getSicaServiceData().
                find(LineaOperacion.class, idLineaOperacion);
        if (lineaOperacion.getUsoLinea() > 0) {
            delegate.record("Aviso: L\u00ednea de Operaci\u00f3n con Importe en Uso. Se Suspende "
                    + "la L\u00ednea y se espera a liquidar los deals interbancarios pendientes.",
                    null);
        }
        CapturaObservaciones nextPage = (CapturaObservaciones) cycle.
                getPage("CapturaObservaciones");
        nextPage.setModo("");
        nextPage.setIdLinea(idLineaOperacion);
        nextPage.setPaginaAnterior(getPageName());
        nextPage.activate(cycle);
    }

    /**
	 * Operaci&oacute;n de Cancelaci&oacute;n de L&iacute;neas de Operaci&oacute;n.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
    public void operacionCancelar(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        Integer idLineaOperacion = new Integer((cycle.getServiceParameters()[0]).toString());
        LineaOperacion lineaOperacion = (LineaOperacion) getSicaServiceData().
                find(LineaOperacion.class, idLineaOperacion);
        if (lineaOperacion.getUsoLinea() > 0) {
            delegate.record("Aviso: L\u00ednea de Operaci\u00f3n con Importe en Uso. "
                    + "Se Cancela la L\u00ednea.", null);
        }
        CapturaObservaciones nextPage = (CapturaObservaciones) cycle.
                getPage("CapturaObservaciones");
        nextPage.setModo("");
        nextPage.setIdLinea(idLineaOperacion);
        nextPage.setPaginaAnterior(getPageName());
        nextPage.activate(cycle);
    }

    /**
     * Apoya a los m&eacute;todos <code>operacionActivar</code> y <code>operacionSuspender</code>
     * para cambiar el Status de las L&iacute;neas de Operaci&oacute;n y hacer un LOG de la
     * operaci&oacute;n efectuada en la tabla <code>LineaOperacionLog</code>.
     *
     * @param estatus El Status a modificar de la L&iacute;nea de Operaci&oacute;n.
     * @param operacion La Operaci&oacute;n efectuada para guardar en el LOG.
     * @param idLineaOperacion La L&iacute;nea de Operaci&oacute;n sobre la que se efectu&oacute;
     *      la Operaci&oacute;n.
     */
    public void cambiarEstatusLineaOperacion(String estatus, String operacion,
                                             Integer idLineaOperacion) {
        Visit visit = (Visit) getVisit();
        try {
            LineaOperacion lineaOperacion = (LineaOperacion) getSicaServiceData().
                    find(LineaOperacion.class, idLineaOperacion);
            lineaOperacion.setUltimaModificacion(getFechaActual());
            lineaOperacion.setStatusLinea(estatus);
            getSicaServiceData().update(lineaOperacion);
            LineaOperacionLog lineaOperacionLog = new LineaOperacionLog();
            lineaOperacionLog.setLineaOperacion(lineaOperacion);
            lineaOperacionLog.setTipoOper(operacion);
            lineaOperacionLog.setImporte(lineaOperacion.getImporte());
            lineaOperacionLog.setFechaOperacion(lineaOperacion.getUltimaModificacion());
            lineaOperacionLog.setUsuario(visit.getUser());
            lineaOperacionLog.setObservaciones(getObservaciones());
            getSicaServiceData().store(lineaOperacionLog);
            setLineasOperacion(new ArrayList());
        }
        catch (DataAccessException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
            throw new ApplicationRuntimeException("Hubo un error al intentar efectuar la operacion "
                    + "sobre la Linea.");
        }
    }

    /**
     * Manda a llamar a la P&aacute;gina de Consulta del Historial de las L&iacute;neas de
     * Operaci&oacute;n.
     *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
    public void consultarHistorialLineaOperacion(IRequestCycle cycle) {
        ConsultaHistorialLineaOperacion nextPage = (ConsultaHistorialLineaOperacion) cycle.
                getPage("ConsultaHistorialLineaOperacion");
        nextPage.setIdLineaOperacion(new Integer((cycle.getServiceParameters()[0]).toString()));
        List bkl = getSicaServiceData().
                findBrokerByIdPersona(new Integer(cycle.getServiceParameters()[1].toString()));
        nextPage.setCorporativo(((Broker) bkl.get(0)).
                getId().getPersonaMoral().getNombreCompleto());
        nextPage.setPaginaAnterior(getPageName());
        nextPage.activate(cycle);
    }

    /**
     * Obtiene la Fecha Actual del Sistema.
     *
     * @return Date La Fecha.
     */
    public Date getFechaActual() {
    	return new Date();
    }

    /**
	 * Permite saber si el usuario del Sistema es la Mesa de Cambios.
	 *
	 * @return boolean True si el usuario es la Mesa de Cambios.
	 */
	public boolean isModoMesaCambios() {
		return FacultySystem.SICA_APROB_LINOPER_M.equals(getModo());
	}

	/**
     * Obtiene las L&iacute;neas de Operaci&oacute;n.
     *
     * @return List Las l&iacute;neas.
     */
	public abstract List getLineasOperacion();

	/**
     * Fija las L&iacute;neas de Operaci&oacute;n.
     *
     * @param lineasOperacion Los datos a fijar.
     */
	public abstract void setLineasOperacion(List lineasOperacion);

    /**
     * Establece el Modo de Operaci&oacute;n de la P&aacute;gina:
     * Mesa de Cambios o Cr&eacute;dito.
     *
     * @param modo El Modo de Operaci&oacute;n.
     */
    public abstract void setModo(String modo);

    /**
     * Obtiene el Modo de Operaci&oacute;n de la P&aacute;gina.
     *
     * @return String el Modo de Operaci&oacute;n de la P&aacute;gina.
     */
    public abstract String getModo();

    /**
	 * Obtiene el Estatus seleccionado para la b&uacute;squeda de
	 * L&iacute;neas de Operaci&oacute;n.
	 *
	 * @return String El Estatus seleccionado.
	 */
	public abstract String getEstatus();

	/**
	 * Fija el Estatus seleccionado para la b&uacute;squeda de
	 * L&iacute;neas de Operaci&oacute;n.
	 *
	 * @param estatus El Estatus seleccionado.
	 */
	public abstract void setEstatus(String estatus);

	/**
     * Obtiene, en caso de que la Caseta de Cr&eacute;dito Suspenda una L&iacute;nea de
     * Operaci&oacute;n, la Raz&oacute;n de la Suspensi&oacute;.
     *
     * @return String La Raz&oacute;n de la Suspensi&oacute;.
     */
    public abstract String getObservaciones();

    /**
     * En caso de que la Caseta de Cr&eacute;dito Suspenda una L&iacute;nea de Operaci&oacute;n,
     * guarda la Raz&oacute;n de la Suspensi&oacute;.
     *
     * @param observaciones La Raz&oacute;n de la Suspensi&oacute;.
     */
    public abstract void setObservaciones(String observaciones);

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda Porcentaje de Uso
     * L&iacute;mite Inferior.
     *
     * @return double limiteInferior.
     */
	public abstract double getLimiteInferior();

	/**
     * Obtiene lo establecido como criterio de b&uacute;squeda Porcentaje de Uso
     * L&iacute;mite Superior.
     *
     * @return double limiteSuperior.
     */
	public abstract double getLimiteSuperior();

	/**
     * Obtiene el T&iacute;tulo a mostrar en el componente de B&uacute;squeda,
     * dependiendo si se entr&oacute; del men&uacute; de la Mesa de Cambios
     * o del Departamento de Cr&eacute;dito.
     *
     * @return String El T&iacute;tulo.
     */
    public abstract String getTitleActionPortletBorder();

    /**
     * Fija el T&iacute;tulo a mostrar en el componente de B&uacute;squeda,
     * dependiendo si se entr&oacute; del men&uacute; de la Mesa de Cambios 
     * o del Departamento de Cr&eacute;dito.
     *
     * @param titleActionPortletBorder El T&iacute;tulo.
     */
    public abstract void setTitleActionPortletBorder(String titleActionPortletBorder);
    
    /**
     * Regresa el valor de brokers.
     *  
     * @return List.
     */
    public abstract List getBrokers();
    
    /**
	 * Activa la lista de Brokers encontrados de acuerdo al criterio de
	 * B&uacute;squeda preestablecido.
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
}