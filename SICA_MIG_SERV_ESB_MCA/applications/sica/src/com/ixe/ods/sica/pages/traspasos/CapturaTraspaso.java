/*
 * $Id: CapturaTraspaso.java,v 1.12 2009/08/03 22:08:10 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.traspasos;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.model.TipoTraspasoProducto;
import com.ixe.ods.sica.model.TraspasoProducto;
import com.ixe.ods.sica.pages.SicaPage;
import com.legosoft.tapestry.model.RecordSelectionModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;

/**
 * Clase para la Captura de Traspasos.
 *
 * @author Javier Cordova (jcordova)
 * @version  $Revision: 1.12 $ $Date: 2009/08/03 22:08:10 $
 */
public abstract class CapturaTraspaso extends SicaPage {

	/**
     * Se ejecuta cada que se activa la p&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
    	super.activate(cycle);
	    restablecerValoresIniciales();
	    setModoSubmit(0);
	    setOperacionExitosa(false);
    }
    
    /**
	 * Restablece los valores iniciales de los campos.
	 */
    private void restablecerValoresIniciales() {
        setTraspasoProducto(new TraspasoProducto());
        List mesasCambio = getSicaServiceData().findAll(MesaCambio.class);
	    for (Iterator it = mesasCambio.iterator(); it.hasNext(); ) {
	        MesaCambio mesaCambio = (MesaCambio) it.next();
	        if (mesaCambio.getIdMesaCambio() == MESA_OPERACION) {
	            setSelectedMesaCambio(mesaCambio);
	            break;
	        }
	    }
    	List tiposTraspasosProducto = getSicaServiceData().findAllTiposTraspasoProducto();
	    setSelectedTipoTraspasoProducto((TipoTraspasoProducto) tiposTraspasosProducto.get(0));    
	    Canal canal = new Canal();
        canal.setIdCanal(TODOS_LOS_CANALES);
        canal.setNombre(TODOS_LOS_CANALES);
        setSelectedCanal(canal);
        setSelectedDivisa((Divisa) getSicaServiceData().findDivisasFrecuentes().get(0));
	    setMonto(0);
	    setObservaciones("");
    }
    
    /**
     * Permite saber si la divisa a mostrar en pantalla deber ser fija como USD 
     * o se muestra un combo de divisas frecuentes + no frecuentes.
     * 
     * @return boolean Si la divisa debe ser fija como USD.
     */
    public boolean isDivisaUSD() {
        return TRADEMEXDOL.equals(getSelectedTipoTraspasoProducto().
                    getMnemonicoTraspaso().trim()) 
                || TRAA_MEXDOL.equals(getSelectedTipoTraspasoProducto().
                    getMnemonicoTraspaso().trim())
                || FONDEMEXDOL.equals(getSelectedTipoTraspasoProducto().
                    getMnemonicoTraspaso().trim());
    }
    
    /**
     * Permite saber si se seleccionaron TODOS o un Canal en particular.
     * 
     * @return boolean Si son todos o un s&oacute;lo Canal.
     */
    public boolean isTodosLosCanales() {
        return TODOS_LOS_CANALES.equals(getSelectedCanal().getNombre());
    }

    /**
     * Crea un Traspaso con los datos proporcionados.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void crearTraspaso(IRequestCycle cycle) {
        if (isDivisaUSD()) {
            setSelectedDivisa((Divisa) getSicaServiceData().find(Divisa.class, "USD"));
        }
    	if (getModoSubmit() == 1) {
    	    setModoSubmit(0);
    	    setOperacionExitosa(false);
            IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                    getBean("delegate");
    	    if (!TODOS_LOS_CANALES.equals(getSelectedCanal().getNombre())) {
		    	if (!(getMonto() > 0)) {
		            delegate.record("El Monto debe ser mayor a 0 (cero).", null);
		            return;
		        }
	    	}
	    	else {
	    	    setMonto(0);
	    	}
	    	if (StringUtils.isEmpty(getObservaciones())) {
	            delegate.record("Debe especificar Observaciones.", null);
	            return;
	        }
	    	if (!delegate.getHasErrors()) {
	    	    Visit visit = (Visit) getVisit();
	    		try {
					getTraspasoProducto().setMesaCambio(getSelectedMesaCambio());
                    getTraspasoProducto().
                            setTipoTraspasoProducto(getSelectedTipoTraspasoProducto());
					if (!TODOS_LOS_CANALES.equals(getSelectedCanal().getNombre())) {
					    getTraspasoProducto().setCanal(getSelectedCanal());
					}
		    	    else {
		    	        getTraspasoProducto().setCanal(null);
		    	    }
					getTraspasoProducto().setDivisa(getSelectedDivisa());
					getTraspasoProducto().setMonto(getMonto());
					getTraspasoProducto().setObservaciones(getObservaciones());
					getTraspasoProducto().setRecibimos(true);
					getTraspasoProducto().setTipoCambio(0);
					getTraspasoProducto().setIdUsuario(visit.getUser().getIdUsuario());
					getTraspasoProducto().setFechaOper(getFechaActual());
					getSicaServiceData().store(getTraspasoProducto());
					
					restablecerValoresIniciales();
					setOperacionExitosa(true);
					delegate.record("Su Traspaso se realiz\u00f3 con \u00e9xito.", null);
	    		}
	    		catch (UncategorizedSQLException e) {
	    		    imprimeMensajeTrigger(e.getLocalizedMessage(), delegate);
	            }
	    		catch (DataIntegrityViolationException e) {
	    		    imprimeMensajeTrigger(e.getLocalizedMessage(), delegate);
	            }
			    catch (DataAccessException e) {
			        if (_logger.isDebugEnabled()) {
	    		        _logger.debug(e);
		            }
                    throw new ApplicationRuntimeException("Hubo un error al intentar crear el "
                            + "Traspaso.");
			    }
	    	}
    	}
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
     * Formatea los mensajes de error regresados por el Trigger de Posici&oacute;n Log.
     * 
     * @param localizedMessage El Mensaje de Error atrapado.
     * @param delegate El objeto que permite desplegar el Mensaje de Error Formateado en Pantalla. 
     */
    private void imprimeMensajeTrigger(String localizedMessage, IxenetValidationDelegate delegate) {
        int indiceIni = localizedMessage.indexOf("[Oracle]");
        int indiceIni2 = localizedMessage.indexOf("ORA-20003");
	    int indiceFin = localizedMessage.indexOf("ORA-06512");
	    int indiceAux;
	    String mensajeTrigger;
	    if (indiceIni > 0 && indiceFin > indiceIni) {
		    String mensajeTmp = localizedMessage.substring(indiceIni, indiceFin);
		    indiceAux = mensajeTmp.indexOf(":");
		    if (indiceAux > 0) {
			    int indiceIniMensajeTrigger = mensajeTmp.indexOf(":") + 1;
			    if (mensajeTmp.length() - 1 > indiceIniMensajeTrigger) {
			        mensajeTrigger = mensajeTmp.substring(indiceIniMensajeTrigger);
			    }
			    else {
			        mensajeTrigger = mensajeTmp;
			    }
		    }
		    else {
		        mensajeTrigger = mensajeTmp;
		    }
	    }
	    else if (indiceIni2 > 0 && indiceFin > indiceIni2) {
		    String mensajeTmp = localizedMessage.substring(indiceIni2, indiceFin);
		    indiceAux = mensajeTmp.indexOf(":");
		    if (indiceAux > 0) {
			    int indiceIniMensajeTrigger = mensajeTmp.indexOf(":") + 1;
			    if (mensajeTmp.length() - 1 > indiceIniMensajeTrigger) {
			        mensajeTrigger = mensajeTmp.substring(indiceIniMensajeTrigger);
			    }
			    else {
			        mensajeTrigger = mensajeTmp;
			    }
		    }
		    else {
		        mensajeTrigger = mensajeTmp;
		    }
	    }
	    else {
	        mensajeTrigger = localizedMessage;
	    }
	    delegate.record(mensajeTrigger, null);
    }

    /**
     * Regresa a la pantalla de Traspasos.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cancelarOperacion(IRequestCycle cycle) {
        Traspasos nextPage = (Traspasos) cycle.getPage("Traspasos");
		nextPage.activate(cycle);
    }
    
    /**
     * Llena el combo de Mesas de Cambio.
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getMesasCambioModel() {
        List mesasCambio = getSicaServiceData().findAll(MesaCambio.class);
        return new RecordSelectionModel(mesasCambio, "idMesaCambio", "nombre");
    }
    
    /**
     * Llena el combo de Divisas.
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getDivisasModel() {
        List divisas = getSicaServiceData().findDivisasFrecuentes();
        divisas.addAll(getSicaServiceData().findDivisasNoFrecuentes());
        return new RecordSelectionModel(divisas, "idDivisa", "descripcion");
    }
    
    /**
     * Llena el combo de Tipos de Traspaso Producto.
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getTiposTraspasoProductoModel() {
        List tiposTraspasosProducto = getSicaServiceData().findAllTiposTraspasoProducto();
        return new RecordSelectionModel(tiposTraspasosProducto, "mnemonicoTraspaso",
                "mnemonicoTraspaso");
    }
    
    /**
     * Llena el combo de Canales.
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getCanalesModel() {
        Canal canal = new Canal();
        canal.setIdCanal(TODOS_LOS_CANALES);
        canal.setNombre(TODOS_LOS_CANALES);
        List canales = new ArrayList();
        canales.add(canal);
        canales.addAll(getSicaServiceData().findAll(Canal.class));
        return new RecordSelectionModel(canales, "idCanal", "nombre");
    }
	
    /**
     * Permite saber si ya se oprimi&oacute; el bot&oacute;n Aceptar.
     *
     * @return int Aceptar: 1.
     */
    public abstract int getModoSubmit();

    /**
     * Fija el Modo Submit de la P&aacute;gina en Aceptar: 1.
     *
     * @param modoSubmit Aceptar: 1.
     */
    public abstract void setModoSubmit(int modoSubmit);
    
    /**
     * Obtiene el registro <code>TraspasoProducto</code> a capturar.
     * 
     * @return TraspasoProducto.
     */
    public abstract TraspasoProducto getTraspasoProducto();
    
    /**
     * Fija el registro <code>TraspasoProducto</code> a capturar.
     *
     * @param traspasoProducto El valor a asignar.
     */
    public abstract void setTraspasoProducto(TraspasoProducto traspasoProducto);
    
    /**
     * Obtiene la <code>MesaCambio</code> seleccionada del Combo de Mesas de Cambio.
     *
     * @return MesaCambio.
     */
    public abstract MesaCambio getSelectedMesaCambio();
    
    /**
     * Fija la <code>MesaCambio</code> seleccionada del Combo de Mesas de Cambio.
     *
     * @param selectedMesaCambio El valor a asignar.
     */
    public abstract void setSelectedMesaCambio(MesaCambio selectedMesaCambio);
    
    /**
     * Obtiene el <code>TipoTraspasoProducto</code> seleccionado del Combo de Tipos Traspaso
     * Producto.
     *
     * @return TipoTraspasoProducto.
     */
    public abstract TipoTraspasoProducto getSelectedTipoTraspasoProducto();
    
    /**
     * Fija el <code>TipoTraspasoProducto</code> seleccionado del Combo de Tipos Traspaso Producto.
     *
     * @param selectedTipoTraspasoProducto El valor a asignar.
     */
    public abstract void setSelectedTipoTraspasoProducto(
            TipoTraspasoProducto selectedTipoTraspasoProducto);
    
    /**
     * Obtiene el <code>Canal</code> seleccionado del Combo de Canales.
     *
     * @return Canal.
     */
    public abstract Canal getSelectedCanal();
    
    /**
     * Fija el <code>Canal</code> seleccionado del Combo de Canales.
     *
     * @param canal El valor a asignar.
     */
    public abstract void setSelectedCanal(Canal canal);
    
    /**
     * Obtiene la <code>Divisa</code> seleccionada del Combo de Divisas.
     *
     * @return Divisa.
     */
    public abstract Divisa getSelectedDivisa();
    
    /**
     * Fija la <code>Divisa</code> seleccionada del Combo de Divisas.
     *
     * @param divisa El valor a asignar.
     */
    public abstract void setSelectedDivisa(Divisa divisa);
    
    /**
     * Obtiene el monto tecleado del Traspaso.
     *
     * @return double El Monto.
     */
    public abstract double getMonto();
    
    /**
     * Fija el monto tecleado del Traspaso.
     *
     * @param monto El valor a asignar.
     */
    public abstract void setMonto(double monto);
    
    /**
     * Obtiene las Observaciones del Traspaso.
     *
     * @return String Las Observaciones.
     */
    public abstract String getObservaciones();
    
    /**
     * Fija las Observaciones del Traspaso.
     *
     * @param observaciones El valor a asignar.
     */
    public abstract void setObservaciones(String observaciones);
    
    /**
     * Bandera que indica si el Traspaso fue exitoso.
     *
     * @param operacionExitosa True o False.
     */
    public abstract void setOperacionExitosa(boolean operacionExitosa);
    
    /**
     * Constante Mesa de Operacion.
     */
    public static final int MESA_OPERACION = 1;
    
    /**
     * Constante Mnemonico Tipo Traspaso Producto TRADEMEXDOL.
     */
    public static final String TRADEMEXDOL = "TRADEMEXDOL";
    
    /**
     * Constante Mnemonico Tipo Traspaso Producto TRAA_MEXDOL.
     */
    public static final String TRAA_MEXDOL = "TRAA_MEXDOL";
    
    /**
     * Constante Mnemonico Tipo Traspaso Producto FONDEMEXDOL.
     */
    public static final String FONDEMEXDOL = "FONDEMEXDOL";
    
    /**
     * Constante Mnemonico Tipo Traspaso Producto TODOS.
     */
    public static final String TODOS_LOS_CANALES = "TODOS";
}
