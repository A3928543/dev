/*
 * $Id: CapturaTraspasoPortafolios.java,v 1.12.22.2.4.1 2011/04/26 02:51:07 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.traspasos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.model.TraspasoMesa;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.model.FactorDivisaActual;
import com.legosoft.tapestry.model.RecordSelectionModel;


/**
 * Clase para la Captura de Traspasos entre Portafolios.
 *
 * @author Javier Cordova (jcordova)
 * @version  $Revision: 1.12.22.2.4.1 $ $Date: 2011/04/26 02:51:07 $
 */
public abstract class CapturaTraspasoPortafolios extends SicaPage {
	
	

	/**
     * Se ejecuta cada que se activa la p&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
    	super.activate(cycle);
	    restablecerValoresIniciales();
	    setModoSubmit(0);
	    setModoMensaje(false);
	    setOperacionExitosa(false);
    }

    /**
	 * Restablece los valores iniciales de los campos.
	 */
    private void restablecerValoresIniciales() {
        setTraspasoPortafolios(new TraspasoMesa());
        setSelectedDelPortafolio(null);
        setSelectedAlPortafolio(null);
    	setSelectedDivisa(null);
    	setSelectedValor(null);
        setMonto(0.0);
        setTipoCambio(0.0);
        setTipoCambioA(0.0);
    }
    
    /**
     * Regresa true si el porcentaje de desviaci&oacute;n entre tcCapturado y tcRef es mayor al n%,
     * si es mayor al n% levanta una SicaException.
     *
     * @param tcCapturado El tipo de cambio capturado por el usuario.
     * @param tcRef El tipo de cambio de referencia de la divisa.
     * @return boolean.
     */
    private boolean validarPorcentajeDesv(double tcCapturado, double tcRef) {
        SicaServiceData sd = getSicaServiceData();
        ParametroSica paramLimDesv = sd.findParametro(ParametroSica.PORC_DESV_TRAS_PORT_LIM);
        double limPorcDesv = Redondeador.redondear2Dec(Double.valueOf(paramLimDesv.getValor()).
                doubleValue());
        // Revisamos el limite de bloqueo
        double tcInferior = Redondeador.redondear6Dec(tcRef * (1 - limPorcDesv / 100.00));
        double tcSuperior = Redondeador.redondear6Dec(tcRef * (1 + limPorcDesv / 100.00));
        
        if (tcCapturado < tcInferior || tcCapturado > tcSuperior) {
            throw new SicaException("El tipo de cambio 'Al Portafolio' debe estar entre " + tcInferior + " y " +
                    tcSuperior + ".");
        }
        // Ahora revisamos la desviacion con nivel de advertencia:
        paramLimDesv = sd.findParametro(ParametroSica.PORC_DESV_TRAS_PORT_WARN);
        limPorcDesv = Redondeador.redondear2Dec(Double.valueOf(paramLimDesv.getValor()).
                doubleValue());
        tcInferior = Redondeador.redondear6Dec(tcRef * (1 - limPorcDesv / 100.00));
        tcSuperior = Redondeador.redondear6Dec(tcRef * (1 + limPorcDesv / 100.00));
        
        return tcCapturado < tcInferior || tcCapturado > tcSuperior;
    }
    
    /**
     * Regresa el tipo de cambio rec&iacute;proco, si es que la divisa seleccionada debe dividir.\
     *
     * @param tcRef El tipo de cambio a invertir, si es necesario.
     * @param pr El precio de referencia actual.
     * @return double.
     */
    private double invertirTipoCambio(double tcRef, PrecioReferenciaActual pr) {
        double preRef = Redondeador.redondear6Dec(pr.getPreRef().getMidSpot().doubleValue());
        tcRef /= preRef;
        if (getSelectedDivisa().isDivide()) {
            tcRef = 1 / tcRef;
        }
        return tcRef;
    }
    
    
    /**
     * Levanta una excepci&oacute;n si el tipo de cambio se encuentra desviado m&aacute;s de un n%
     * (bloqueo configurable en BD), con respecto al precio de referencia de la divisa. 
     * Regresa true si el porcentaje de desviaci&oacute;n es de n% (warning configurable en BD).
     *
     * @return boolean.
     */
    private boolean advertirSobreDesviacion() {
        boolean recibimos = false;
        SicaServiceData sd = getSicaServiceData();
        PrecioReferenciaActual pr = sd.findPrecioReferenciaActual();
        debug("Referencia cv: " + pr.getPreRef().getPrecioCompra() + " " +
                pr.getPreRef().getPrecioVenta());
        Divisa div = getSelectedDivisa();
        FactorDivisaActual fda;
        double tcRef;
        if ( !getSelectedDelPortafolio().getDivisaReferencia().getIdDivisa().equals(Divisa.PESO)) {
            	fda = sd.findFactorDivisaActualFromTo(getSelectedDelPortafolio().getDivisaReferencia().getIdDivisa(), div.getIdDivisa());
            if(fda == null ){
            	debug("No se encontro  un factor divisa para "+ getSelectedDelPortafolio().getDivisaReferencia().getIdDivisa()
            			+" VS " + div.getIdDivisa() +"se intenta buscar a reves");
            	fda = sd.findFactorDivisaActualFromTo(div.getIdDivisa(), getSelectedDelPortafolio().getDivisaReferencia().getIdDivisa());
            }
            if( fda == null){
            	debug("No se encontro un factor divisa para la validacion del tipo de cambio en el traspaso entre" +
            			"portafolios" );
            	return false;
            }
            double factorVenta = fda.getFacDiv().getFactor();
            double factor = recibimos ?
                    factorVenta - fda.getFacDiv().getSpreadCompra() : factorVenta;
            tcRef = Redondeador.redondear6Dec(factor);
            debug("tcRef-> " + tcRef + " factorDivisa: " + factor);
        }
        else {
            tcRef = getPizarronServiceData().findPrecioPizarronPesos(
            		getSicaServiceData().findCanal("PMY").getTipoPizarron(),
                    getSelectedDivisa().getIdDivisa(), Constantes.EFECTIVO, recibimos,
                    getSelectedValor());
            debug("tcRef: " + tcRef);
        }
        return validarPorcentajeDesv(getTipoCambio(), tcRef);
    }

    /**
     * Crea un Traspaso con los datos proporcionados.
     *
     * @param cycle El IRequestCycle.
     */
    public void crearTraspaso(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        if (getModoSubmit() == 1 || getModoSubmit() == 2) {
    	    
    	    setOperacionExitosa(false);
    	    if (delegate != null && delegate.getHasErrors()) {
        		return;
            }
            if (getSelectedDelPortafolio() == null ||
                    getSelectedDelPortafolio().getIdMesaCambio() == 0) {
                delegate.record("Debe seleccionar una opci\u00f3n del combo 'Del Portafolio'.",
                        null);
                return;
            }
            if (getSelectedAlPortafolio() == null ||
                    getSelectedAlPortafolio().getIdMesaCambio() == 0) {
                delegate.record("Debe seleccionar una opci\u00f3n del combo 'Al Portafolio'.",
                        null);
                return;
            }
            if (getSelectedDivisa() == null ||
                    "".equals(getSelectedDivisa().getIdDivisa())) {
                delegate.record("Debe seleccionar una opci\u00f3n del combo 'Divisa'.",
                        null);
                return;
            }
            if (getMonto() <= 0.0) {
                delegate.record("El 'Monto' debe ser Mayor a 0.0.", null);
                return;
            }
            if (Divisa.DOLAR.equals(getSelectedDivisa().getIdDivisa())) {
            	setTipoCambio(1.0);
            }
            if (getTipoCambio() <= 0.0) {
                delegate.record("El 'Tipo Cambio Al Portafolio' debe ser Mayor a 0.0.", null);
                return;
            }
            if (getTipoCambioA() <= 0.0) {
                delegate.record("El 'Tipo Cambio Del Portafolio' debe ser Mayor a 0.0.", null);
                return;
            }
            //Validacion de la desviacion del tipo de cambio
            try{
            		//Solo se valida si el usuario no ha confirmado ya la operacion
            		//y si la divisa es diferente de Dolar Americano.
            	if( ! Divisa.DOLAR.equals(getSelectedDivisa().getIdDivisa()) ){
		             if(getModoSubmit() != 2 ){
		            	//Se valida el TC 'Al portafolio'
			            if(advertirSobreDesviacion()){
			            	delegate.record("El Tipo Cambio 'Al Portafolio' tiene un procentaje desviaci\u00f3n mayor al permitido, " +
			            			"favor de confirmar la operaci\u00f3n.", null);
			            	setModoMensaje(true);
			            	setModoSubmit(0);
			            	return;
			            }
		             }
            	}
            }catch(SicaException se ){
            	if (_logger.isDebugEnabled()) {
                    _logger.debug(se);
                }
                delegate.record( se.getMessage(), null);
                setModoSubmit(0);
                return;
            }
            
            Visit visit = (Visit) getVisit();
            try {
                getTraspasoPortafolios().setDeMesaCambio(getSelectedDelPortafolio());
                getTraspasoPortafolios().setAMesaCambio(getSelectedAlPortafolio());
                getTraspasoPortafolios().setDivisa(getSelectedDivisa());
                //Tipo Cambio Divisa Referencia "Al" Portafolio
                if (Divisa.PESO.equals(getSelectedAlPortafolio().getDivisaReferencia().
                        getIdDivisa())) {
                    getTraspasoPortafolios().setTipoCambio(getTipoCambio() * getTipoCambioA());
                }
                else {
                    getTraspasoPortafolios().setTipoCambio(getTipoCambio());
                }
                //Tipo Cambio Divisa Referencia "Del" Portafolio
                if (Divisa.PESO.equals(getSelectedDelPortafolio().getDivisaReferencia().
                        getIdDivisa())) {
                    getTraspasoPortafolios().setTipoCambioDivisaReferencia(getTipoCambio() *
                            getTipoCambioA());
                }
                else {
                    getTraspasoPortafolios().setTipoCambioDivisaReferencia(getTipoCambioA());
                }
                //IdCanal "Al Portafolio"
                ParametroSica idCanalAl = (ParametroSica) getSicaServiceData().
                        find(ParametroSica.class, "CANAL_MESA_" +
                                getSelectedAlPortafolio().getNombre().trim());
                //IdCanal "Del Portafolio"
                ParametroSica idCanalDel = (ParametroSica) getSicaServiceData().
                        find(ParametroSica.class, "CANAL_MESA_" +
                                getSelectedDelPortafolio().getNombre().trim());
                getSicaServiceData().storeTraspasoPortafolios(getTraspasoPortafolios(),
                        visit.getUser().getIdUsuario(), getMonto(), getSelectedValor(),
                        idCanalAl.getValor(), idCanalDel.getValor());
                restablecerValoresIniciales();
                setOperacionExitosa(true);
                delegate.record("Su Traspaso se realiz\u00f3 con \u00e9xito.", null);
                setModoSubmit(0);
                setModoMensaje(false);
            }
            catch (Exception e) {
                if (_logger.isDebugEnabled()) {
                    _logger.debug(e);
                }
                delegate.record("Hubo un error al intentar crear el Traspaso.", null);
            }
        }else if(getModoSubmit() == 3){
        	restablecerValoresIniciales();
        	setModoMensaje(false);
        	setOperacionExitosa(false);
        	setModoSubmit(0);
        }
    }

    /**
     * Llena el combo "Del Portafolio".
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getDelPortafolioModel() {
        List mesasCambio = new ArrayList();
        MesaCambio mc = new MesaCambio();
        mc.setIdMesaCambio(0);
        mc.setNombre("");
        mesasCambio.add(mc);
        mesasCambio.addAll(getSicaServiceData().findAll(MesaCambio.class));
        return new RecordSelectionModel(mesasCambio, "idMesaCambio", "nombre");
    }

    /**
     * Llena el combo "Al Portafolio".
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getAlPortafolioModel() {
        List mesasCambio = new ArrayList();
        MesaCambio mc = new MesaCambio();
        mc.setIdMesaCambio(0);
        mc.setNombre("");
        mesasCambio.add(mc);
        if (getSelectedDelPortafolio() != null) {
            List mesasCambioTmp = getSicaServiceData().findAll(MesaCambio.class);
            for (Iterator it = mesasCambioTmp.iterator(); it.hasNext(); ) {
                MesaCambio mesaCambio = (MesaCambio) it.next();
    	        if (!mesaCambio.equals(getSelectedDelPortafolio())) {
    	            mesasCambio.add(mesaCambio);
    	        }
    	    }
        }
        return new RecordSelectionModel(mesasCambio, "idMesaCambio", "nombre");
    }

    /**
     * Llena el combo de Divisas.
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getDivisasModel() {
        List divisasDelPortafolio = new ArrayList();
        Visit visit = (Visit) getVisit();
        Canal canal = getSicaServiceData().findCanal(visit.getIdCanal());
        if (getSelectedDelPortafolio() != null) {
        	divisasDelPortafolio.addAll(getSicaServiceData().
                    findDivisasByTipoPizarron(
                    		canal.getTipoPizarron().getIdTipoPizarron().intValue()));
        }
        List divisasAlPortafolio = new ArrayList();
        if (getSelectedAlPortafolio() != null) {
        	divisasAlPortafolio.addAll(getSicaServiceData().
        			findDivisasByTipoPizarron(
                    canal.getTipoPizarron().getIdTipoPizarron().intValue()));
        }
        List divisas = new ArrayList();
        Divisa d = new Divisa();
        d.setIdDivisa("");
        d.setDescripcion("");
        divisas.add(d);
        for (Iterator it = divisasDelPortafolio.iterator(); it.hasNext(); ) {
        	Divisa divisaDel = (Divisa) it.next();
        	for (Iterator it2 = divisasAlPortafolio.iterator(); it2.hasNext(); ) {
        		Divisa divisaAl = (Divisa) it2.next();
        		if (divisaDel.equals(divisaAl)) {
            		divisas.add(divisaAl);
            		break;
            	}
        	}
        }
        return new RecordSelectionModel(divisas, "idDivisa", "descripcion");
    }

    /**
     * Llena el combo de Valores.
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getValorModel() {
    	if (getPizarronServiceData().isValorFuturoHabilitado()) {
            return new StringPropertySelectionModel(new String[]{Constantes.CASH, Constantes.TOM,
                    Constantes.SPOT, Constantes.HR72, Constantes.VFUT});
        }
        else {
            return new StringPropertySelectionModel(new String[]{Constantes.CASH, Constantes.TOM,
                    Constantes.SPOT, Constantes.HR72});
        }
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
     * Obtiene el registro <code>TraspasoMesa</code> a capturar.
     *
     * @return TraspasoMesa.
     */
    public abstract TraspasoMesa getTraspasoPortafolios();

    /**
     * Fija el registro <code>TraspasoMesa</code> a capturar.
     *
     * @param traspasoMesa El valor a asignar.
     */
    public abstract void setTraspasoPortafolios(TraspasoMesa traspasoMesa);

    /**
     * Obtiene la <code>MesaCambio</code> seleccionada del Combo "Del Portafolio".
     *
     * @return MesaCambio.
     */
    public abstract MesaCambio getSelectedDelPortafolio();

    /**
     * Fija la <code>MesaCambio</code> seleccionada del Combo "Del Portafolio".
     *
     * @param selectedDelPortafolio El valor a asignar.
     */
    public abstract void setSelectedDelPortafolio(MesaCambio selectedDelPortafolio);

    /**
     * Obtiene la <code>MesaCambio</code> seleccionada del Combo "Al Portafolio".
     * @return MesaCambio.
     */
    public abstract MesaCambio getSelectedAlPortafolio();

    /**
     * Fija la <code>MesaCambio</code> seleccionada del Combo "Al Portafolio".
     *
     * @param selectedAlPortafolio El valor a asignar.
     */
    public abstract void setSelectedAlPortafolio(MesaCambio selectedAlPortafolio);

    /**
     * Obtiene la <code>Divisa</code> seleccionada del Combo de Divisa.
     *
     * @return Divisa.
     */
    public abstract Divisa getSelectedDivisa();

    /**
     * Fija la <code>Divisa</code> seleccionada del Combo de Divisa.
     *
     * @param divisa El valor a asignar.
     */
    public abstract void setSelectedDivisa(Divisa divisa);

    /**
     * Obtiene la Fecha Valor seleccionada del Combo Valor.
     * @return String.
     */
    public abstract String getSelectedValor();

    /**
     * Fija la Fecha Valor del Combo Valor.
     *
     * @param valor El valor a asignar.
     */
    public abstract void setSelectedValor(String valor);

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
     * Obtiene el <code>tipoCambio</code> tecleado del Traspaso.
     *
     * @return double El tipoCambio.
     */
    public abstract double getTipoCambio();

    /**
     * Fija el <code>tipoCambio</code> tecleado del Traspaso.
     *
     * @param tipoCambio El valor a asignar.
     */
    public abstract void setTipoCambio(double tipoCambio);

    /**
     * Obtiene el <code>tipoCambioA</code> tecleado del Traspaso.
     *
     * @return double El tipoCambioA.
     */
    public abstract double getTipoCambioA();

    /**
     * Fija el <code>tipoCambioA</code> tecleado del Traspaso.
     *
     * @param tipoCambioA El valor a asignar.
     */
    public abstract void setTipoCambioA(double tipoCambioA);

    /**
     * Bandera que indica si el Traspaso fue exitoso.
     *
     * @param operacionExitosa True o False.
     */
    public abstract void setOperacionExitosa(boolean operacionExitosa);
    
    /**
     * Regresa el valor de modoMensaje.
     *
     * @return boolean.
     */
    public abstract boolean isModoMensaje();

    /**
     * Fija el valor de modoMensaje.
     *
     * @param modoMensaje El valor a asignar.
     */
    public abstract void setModoMensaje(boolean modoMensaje);

}
