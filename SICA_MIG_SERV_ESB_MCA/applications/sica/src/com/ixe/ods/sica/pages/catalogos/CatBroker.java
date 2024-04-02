/*
 * $Id: CatBroker.java,v 1.10.34.1 2010/09/09 00:30:37 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.catalogos;

import com.ixe.ods.bup.model.PersonaMoral;
import com.ixe.ods.sica.model.Broker;
import com.ixe.ods.sica.model.BrokerPK;
import com.ixe.ods.sica.pages.SicaPage;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.valid.IValidationDelegate;


/**
 * Clase que permite al usuario modificar la informaci&oacute;n del
 * broker.
 * 
 * @author Gerardo Corzo, Javier Cordova.
 * @version  $Revision: 1.10.34.1 $ $Date: 2010/09/09 00:30:37 $
 */
public abstract class CatBroker extends SicaPage {
	
	/**
     * M&eacute;todo que permite al usuario actualizar la informaci&oacute;n del
     * broker.
     *
     * @param cycle El IRequestCycle.
     */
	public void actualizar(IRequestCycle cycle) {
		IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
		
        if (getModoSubmit() == 1) {
        	convertirAUpperCase();
			BrokerPK bkpk = new BrokerPK();
	        bkpk.setPersonaMoral(getPersonaMoral());
	        Broker bk = new Broker();
	        bk.setId(bkpk);
	        bk.setClaveReuters(getClaveReuters());
	        
	        if(isVisibleKondor()){
	        	if( getIdSaif() == null || getIdSaif().trim().equals("") ){
	        		delegate.record("Para contrapartes visibles para Kondor debe proporcionar un ID SAIF.", null);
	        		return;
	        	} 
	        }
	        
	        bk.setVisibleKondor(isVisibleKondor());
	        bk.setIdSaif(getIdSaif());
	        
	        if (("Inst. No Financiera").equals(getTipoBrokerSeleccionado())) {
	            bk.setTipoBroker(Broker.NO_INSTITUCION_FINANCIERA);
	        }
	        else {
	            bk.setTipoBroker(Broker.INSTITUCION_FINANCIERA);
	            bk.setPagoAnticipado(isPagoAnticipado());
	        }
	        if (getTipoOperacion() == 1) {
	            getSicaServiceData().update(bk);
	        }
	        else {
	            getSicaServiceData().store(bk);
	            setTipoOperacion(1);
	        }
	        setLevel(1);
	        delegate.record("Operaci\u00f3n exitosa, oprima Regresar para terminar.", null);
        }
    }
	
	/**
	 * Convierte lo escrito en los campos del Registro a editar o insertar a Uppercase.
	 */
	private void convertirAUpperCase() {
        if (StringUtils.isNotEmpty(getClaveReuters())) {
        	setClaveReuters(getClaveReuters().toUpperCase());
        }
    }
	
	/**
	 * Permite saber si la Instituci&oacute;n Financiera a insertar o editar es sujeta a Pago Anticipado
	 * @return
	 */
	private boolean isPagoAnticipado() {
		return "S".equals(getPagoAnticipadoSeleccionado()) ? true : false;
	}

    /**
     * Regresa un StringPropertySelectionModel con los tipos de
     * Instituci&oacute;n.
     *
     */
    public StringPropertySelectionModel getTiposBroker() {
        return new StringPropertySelectionModel(new String[]{"Inst. Financiera", "Inst. No Financiera"});
    }
    
    /**
     * Obtiene la informaci&oacute;n a popular en el Combo Pago Anticipado.
     */
    public StringPropertySelectionModel getPagoAnticipadoModel() {
        return new StringPropertySelectionModel(new String[]{"N", "S"});
    }

    /**
	 * Regresa el modo de submit de la pagina. Si es por boton o por combo.
	 * @return int
	 */
	public abstract int getModoSubmit();
	
	/**
	 * Fija la Clave Reuters.
	 * 
	 * @param claveReuters La Clave Reuters.
	 */
    public abstract void setClaveReuters(String claveReuters);

    /**
     * Regresa el valor de claveReuters.
     *
     * @return String.
     */
    public abstract String getClaveReuters();

    /**
     * Fija el valor de personaMoral.
     *
     * @param personaMoral El valor a asignar.
     */
    public abstract void setPersonaMoral(PersonaMoral personaMoral);

    /**
     * Regresa el valor de personaMoral.
     *
     * @return PersonaMoral.
     */
    public abstract PersonaMoral getPersonaMoral();
    
    /**
     * Fija el valor de tipoBroker.
     *
     * @param tipoBroker El valor a asignar.
     */
    public abstract void setTipoBroker(String tipoBroker);

    /**
     * Regresa el valor de tipoBroker.
     *
     * @return String.
     */
    public abstract String getTipoBroker();
    
    /**
     * Fija el valor de paginaAnterior.
     *
     * @param paginaAnterior El valor a asignar.
     */
    public abstract void setPaginaAnterior(String paginaAnterior);

    /**
     * Regresa el valor de paginaAnterior.
     *
     * @return String.
     */
    public abstract String getPaginaAnterior();
    
    /**
     * Fija el valor de tipoOperacion.
     *
     * @param tipoOperacion El valor a asignar.
     */
    public abstract void setTipoOperacion(int tipoOperacion);

    /**
     * Regresa el valor de tipoOperacion.
     *
     * @return int.
     */
    public abstract int getTipoOperacion();
    
    /**
     * Fija el valor de tipoBrokerSeleccionado.
     *
     * @param tipoBrokerSeleccionado El valor a asignar.
     */
    public abstract void setTipoBrokerSeleccionado(String tipoBrokerSeleccionado);

    /**
     * Regresa el valor de tipoBrokerSeleccionado.
     *
     * @return String.
     */
    public abstract String getTipoBrokerSeleccionado();
    
    /**
     * Fija el valor de pagoAnticipadoSeleccionado.
     *
     * @param pagoAnticipadoSeleccionado El valor a asignar.
     */
    public abstract void setPagoAnticipadoSeleccionado(String pagoAnticipadoSeleccionado);

    /**
     * Regresa el valor de pagoAnticipadoSeleccionado.
     *
     * @return String.
     */
    public abstract String getPagoAnticipadoSeleccionado();
    
    /**
     * true si la contraparte es visible para Kondor, false de otra manera.
     * @return
     */
    public abstract boolean isVisibleKondor();
    
    /**
     * Fija el valor de visibleKondor.
     * @return
     */
    public abstract void setVisibleKondor( boolean visibleKondor );
    
    /**
     * El valor de idSaif
     * @return
     */
    public abstract String getIdSaif();
    
    /**
     * Fija el valor de idSaif. 
     * @param idSaif
     */
    public abstract void setIdSaif( String idSaif );
    
    /**
     * Regresa el valor de level.
     *
     * @return int.
     */
    public abstract int getLevel();
    
    /**
     * Fija el valor de level.
     *
     * @param level El valor a asignar.
     */
    public abstract void setLevel(int level);
    
}
