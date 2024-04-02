/*
 * $Id: CapturaLineaCredito.java,v 1.16.72.2 2016/10/06 00:40:58 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.lineacredito;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.lineacredito.service.LineaCreditoService;
import com.ixe.ods.sica.model.Formalizacion;
import com.ixe.ods.sica.model.InstanciaFacultada;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.TipoAutorizacion;
import com.ixe.ods.sica.pages.SicaPage;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * Clase para la Captura de L&iacute;neas de Cr&eacute;dito.
 *
 * @author Javier Cordova (jcordova)
 * @version  $Revision: 1.16.72.2 $ $Date: 2016/10/06 00:40:58 $
 */
public abstract class CapturaLineaCredito extends SicaPage {

	public static final Logger LOGGER = Logger.getLogger(CapturaLineaCredito.class);
	
	/**
     * Se ejecuta cada que se activa la p&aacute;gina.
     * Carga la informacion de la linea de credito en el caso de que exista 
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
    	super.activate(cycle);
    	LineaCreditoService lcService=getLineaCreditoService();    	
    	LineaCambio lineaCambio = getLineaCambioServiceData().findLineaCambioParaCliente(getIdCorporativo());
    	setLineaCredito(lineaCambio);
    	setModoEdicion(false);
    	
    	if (lineaCambio != null) { 
    		setTipoAutorizacionSeleccionada(lineaCambio.getTipoAutorizacion());
    		setFormalizacionSeleccionada(lineaCambio.getFormalizacion());
    		setInstanciaFacultadaSeleccionada(lineaCambio.getInstanciaFacultada());
    	} else {
    		setModoEdicion(true);
    		lineaCambio = new LineaCambio();
    		lineaCambio.setImporteFV(new BigDecimal("0.0"));
    		lineaCambio.setImportePAyTF(new BigDecimal("0.0"));
    		lineaCambio.setVencimiento(lcService.getVencimientoDefault());
            setLineaCredito(lineaCambio);
    	}
    	setOperacionExitosa(false);
    }
	
    /**
     * Regresa un RecordSelectionModel con todos los Tipos de Autorizacion.
     *
     * @return IPropertySelectionModel
     */
    public IPropertySelectionModel getTipoAutorizacionModel() {
        List tipoAutorizacionList = getLineaCreditoService().getCatalogoTipoAutorizacion();
        TipoAutorizacion primerElemento = new TipoAutorizacion();
        primerElemento.setIdTipoAutorizacion(new Integer(0));
        primerElemento.setDescripcion(LineaCreditoService.SELECCIONAR);
       
        tipoAutorizacionList.add(0, primerElemento);
        return new RecordSelectionModel(tipoAutorizacionList, LineaCreditoService.ID_TIPO_AUTORIZACION, LineaCreditoService.DESCRIPCION);
    }
    
    
    /**
     * Regresa un RecordSelectionModel con todas las formalizaciones.
     *
     * @return IPropertySelectionModel
     */
    public IPropertySelectionModel getFormalizacionModel() {
        List formalizacionList = getLineaCreditoService().getCatalogoFormalizacion();
        Formalizacion primerElemento = new Formalizacion();
        primerElemento.setIdFormalizacion(new Integer(0));
        primerElemento.setDescripcion( LineaCreditoService.SELECCIONAR);
       
        formalizacionList.add(0, primerElemento);
        return new RecordSelectionModel(formalizacionList, LineaCreditoService.ID_FORMALIZACION, LineaCreditoService.DESCRIPCION);
    }
   
    /**
     * Regresa un RecordSelectionModel con todas las Instancias Facultadas.
     *
     * @return IPropertySelectionModel
     */
    public IPropertySelectionModel getinstanciaFacultadaModel() {
        List instanciaFacultadaList = getLineaCreditoService().getCatalogoInstanciaFacultada();
        InstanciaFacultada primerElemento = new InstanciaFacultada();
        primerElemento.setIdInstanciaFacultada(new Integer(0));
        primerElemento.setDescripcion(LineaCreditoService.SELECCIONAR);
       
        instanciaFacultadaList.add(0, primerElemento);
        return new RecordSelectionModel(instanciaFacultadaList, LineaCreditoService.ID_INSTANCIA_FACULTADA, LineaCreditoService.DESCRIPCION);
    }
    
	/**
     * Sirve para colocar el focus de la p&aacute;gina cuando se carga
     * en el Campo de Texto Deseado.
     *
     * @return Map
     */
    public Map getFirstTextFieldMap() {
        Map map = new HashMap();
        map.put("textField", "document.Form0.montoTextField");
        return map;
    }

    /**
     * 
     * Este metodo permite crear una linea de credito solo en el caso de que no exista.
     * Si el Cliente ya cuenta con una linea de credito entonces solo se mostrara la 
     * informacion de la linea de credito en modo consulta.
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void configurarRegistro(IRequestCycle cycle) {
    	LOGGER.info("Inicia insercion de Linea de Credito");
         IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                 getBean("delegate");
         LineaCreditoService service = getLineaCreditoService();
         LineaCambio lineaCredito= getLineaCredito();
         setOperacionExitosa(false);
         
         if (getModoEdicion()) {
             if (!delegate.getHasErrors()) {
                 lineaCredito.setInstanciaFacultada(getInstanciaFacultadaSeleccionada());
                 lineaCredito.setFormalizacion(getFormalizacionSeleccionada());
                 lineaCredito.setTipoAutorizacion(getTipoAutorizacionSeleccionada());
                 lineaCredito.setUltimaModificacion(getFechaActual());
                 lineaCredito.setStatusLinea(LineaCambio.STATUS_SOLICITUD);  
                 lineaCredito.setUltimaModificacion(getFechaActual());
                 lineaCredito.setUsoCash(new BigDecimal("0.00"));
                 lineaCredito.setUsoTom(new BigDecimal("0.00"));
                 lineaCredito.setUsoSpot(new BigDecimal("0.00"));
                 lineaCredito.setUso72Hr(new BigDecimal("0.00"));
                 lineaCredito.setUso96Hr(new BigDecimal("0.00"));
                 lineaCredito.setPromedioLinea(new BigDecimal("0.00"));
                 lineaCredito.setNumeroExcepciones(0);
                 lineaCredito.setNumeroExcepcionesMes(0);
                 lineaCredito.setExcesoFV(new Integer(0));
                 lineaCredito.setExcesoPAyTF(new Integer(0));
                 lineaCredito.setCorporativo((Persona) getSicaServiceData().
                         find(Persona.class, getIdCorporativo()));
                 if(validarCamposLineaCredito(delegate,lineaCredito))
                	 return;
                 service.crearLineaCredito(lineaCredito);        
                 delegate.record("La l\u00ednea de cr\u00e9dito se dio de alta con "
                         + "\u00e9xito. Oprima 'Regresar' para terminar.", null);
                 
                 setOperacionExitosa(true);
                 setModoEdicion(false);
                 LOGGER.info("Linea de credito creada" + ToStringBuilder.reflectionToString(lineaCredito) );
             }
         }
    	}
    
    
    /**
     * Valida los campos de montos y fecha de la linea de credito.
     * @param delegate
     */
	private boolean validarCamposLineaCredito(IxenetValidationDelegate delegate , LineaCambio lineaCambio) {
		boolean validacion= false;
		
		if (delegate.getHasErrors()) {
             validacion=true;
             return validacion;
         }
         
         if (!(lineaCambio.getImportePAyTF().doubleValue() >= 1)) {
             delegate.record("El importe Pago Anticipado/Toma en Firme de la l\u00ednea de cr\u00e9dito debe ser mayor o "
                     + "igual a 1.00.", ValidationConstraint.REQUIRED);
             validacion=true;
             return validacion;
         }
         
         if (!(lineaCambio.getImporteFV().doubleValue() >= 1)) {
             delegate.record("El importe Fecha Valor de la l\u00ednea de cr\u00e9dito debe ser mayor o "
                     + "igual a 1.00.", ValidationConstraint.REQUIRED);
             validacion=true;
             return validacion;
         }
         
         if(lineaCambio.getTipoAutorizacion().getIdTipoAutorizacion() != null && 
        		 lineaCambio.getTipoAutorizacion().getIdTipoAutorizacion().intValue() <= 0){
        	 delegate.record ("Favor de seleccionar el Tipo de Autorizacion",ValidationConstraint.REQUIRED);
        	 validacion=true;
        	 return validacion;
         }
         
         if(lineaCambio.getFormalizacion().getIdFormalizacion() != null &&
        		 lineaCambio.getFormalizacion().getIdFormalizacion().intValue() <= 0){
        	 delegate.record ("Favor de seleccionar una opcion en el campo Formalizacion",ValidationConstraint.REQUIRED);
        	 validacion=true;
        	 return validacion;
         }
         
         
         if(lineaCambio.getInstanciaFacultada().getIdInstanciaFacultada() != null &&
        	 lineaCambio.getInstanciaFacultada().getIdInstanciaFacultada().intValue() <= 0){
        		 delegate.record ("Favor de seleccionar la Instancia Facultada",ValidationConstraint.REQUIRED); 
        		 validacion=true;
        		 return validacion;
         }
         
         if (LineaCambio.STATUS_VENCIDA.equals(getLineaCredito().getStatusLinea())) {
             if (getLineaCredito().getVencimiento().compareTo(getFechaActual()) <= 0) {
                 delegate.record("La fecha de vencimiento de la l\u00ednea de cr\u00e9dito debe ser "
                         + "mayor a la fecha actual.", ValidationConstraint.REQUIRED);
                 validacion=true;
                 return validacion;
             }
         }
		
         return validacion;  
	}
    
    /**
     * Reactiva una L&iacute;nea de Cr&eacute;dito para el Corporativo seleccionado.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     
    public void reactivar(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        setOperacionExitosa(false);
        Visit visit = (Visit) getVisit();
        String mensaje = "";
        if (LineaCambio.STATUS_SUSPENDIDA.equals(getLineaCredito().getStatusLinea()) ||
                LineaCambio.STATUS_VENCIDA.equals(getLineaCredito().getStatusLinea())) {
            if (getLineaCredito().getUsoCash().doubleValue() > 0) {
                mensaje = "Aviso: La l\u00ednea de cr\u00e9dito tiene importe en uso, se " +
                        "deber\u00e1n liquidar los pagos anticipados pendientes. ";
            }
        }
        if (LineaCambio.STATUS_VENCIDA.equals(getLineaCredito().getStatusLinea())) {
            if (getLineaCredito().getVencimiento().compareTo(getFechaActual()) <= 0) {
                getLineaCredito().setVencimiento(getLineaCredito().getVencimientoDefault());
            }
        }
        getLineaCredito().setUltimaModificacion(getFechaActual());
        if (isContraparte()) {
            getLineaCredito().setStatusLinea(LineaCambio.STATUS_APROBADA);
        }
        else {
            getLineaCredito().setStatusLinea(LineaCambio.STATUS_SOLICITUD);
        }
        getSicaServiceData().update(getLineaCredito());
        LineaCambioLog lcl = new LineaCambioLog();
        lcl.setImporte(getLineaCredito().getImporte());
        lcl.setFechaOperacion(getLineaCredito().getUltimaModificacion());
        lcl.setUsuario(visit.getUser());
        lcl.setTipoOper("T");
        lcl.setFactorDivisa(new BigDecimal("0"));
        lcl.setLineaCambio(getLineaCredito());        
        getSicaServiceData().store(lcl);
        delegate.record(mensaje + "La l\u00ednea de cr\u00e9dito se reactiv\u00f3 con " +
                "\u00e9xito. Oprima 'Regresar' para terminar.", null);
        setOperacionExitosa(true);
    }
    
    
    */
    

 
    /**
     * Regresa a la pantalla de SolicitudLineaCredito.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void terminarOperacion(IRequestCycle cycle) {
    	cycle.activate(getPaginaAnterior());
    }

    /**
     * Obtiene la fecha actual del sistema.
     *
     * @return Date La fecha.
     */
    public Date getFechaActual() {
    	Calendar gc = new GregorianCalendar();
        return gc.getTime();
    }


    /**
	 * Obtiene la P&aacute;gina a la que se debe de regresar en caso de Cancelaci&oacute;n.
	 *
	 * @return String El nombre de la P&aacute;gina.
	 */
	public abstract String getPaginaAnterior();

	/**
	 * Establece la P&aacute;gina a la que se debe de regresar en caso de Cancelaci&oacute;n.
	 *
	 * @param paginaAnterior La P&aacute;gina.
	 */
	public abstract void setPaginaAnterior(String paginaAnterior);

	/**
	 * Obtiene el ID del Corporativo.
	 *
	 * @return Integer el ID Persona del Corporativo.
	 */
	public abstract Integer getIdCorporativo();

	/**
	 * Fija el ID del Corporativo.
	 *
	 * @param idCorporativo El ID a Fijar.
	 */
	public abstract void setIdCorporativo(Integer idCorporativo);

	/**
	 * Obtiene la Raz&oacute;n Social del Corporativo.
	 *
	 * @return String La Raz&oacute;n Social del Corporativo.
	 */
	public abstract String getRazonSocialCorporativo();

	/**
	 * Fija la Raz&oacute;n Social del Corporativo.
	 *
	 * @param razonSocialCorporativo La Raz&oacute;n Social a fijar.
	 */
	public abstract void setRazonSocialCorporativo(String razonSocialCorporativo);

    /**
     * Obtiene la L&iacute;nea de Cr&eacute;dito.
     *
     * @return LineaCambio La L&iacute;nea de Cr&eacute;dito.
     */
    public abstract LineaCambio getLineaCredito();

    /**
     * Fija la L&iacute;nea de Cr&eacute;dito.
     *
     * @param lineaCredito La L&iacute;nea de Cr&eacute;dito.
     */
    public abstract void setLineaCredito(LineaCambio lineaCredito);

    /**
     * Permite saber si la P&aacute;gina est&aacute; en modo de Actualizaci&oacute;n o de
     * Inserci&oacute;n.
     *
     * @return boolean El Modo de la P&aacute;gina.
     */
    public abstract boolean getModoEdicion();

    /**
     * Establece si la P&aacute;gina est&aacute; en modo de Actualizaci&oacute;n o de
     * Inserci&oacute;n.
     *
     * @param modoUpdate El Modo de la P&aacute;gina.
     */
    public abstract void setModoEdicion(boolean modoEdicion);

    /**
     * Indica si se va a capturar una Linea de Credito a una Contraparte.
     * @return boolean True o False.
     */
    public abstract boolean isContraparte();

    /**
     * Establece si se va a capturar una Linea de Credito a una Contraparte.
     * @param contraparte True o False.
     */
    public abstract void setContraparte(boolean contraparte);

    /**
     * Establece si la operaci&oacute;n de captura de la l&iacute;nea de cr&eacute;dito 
     * fue exitosa o no.
     *
     * @return boolean.
     */
    public abstract boolean getOperacionExitosa();

    /**
     * Fija si la operaci&oacute;n de captura de la l&iacute;nea de cr&eacute;dito 
     * fue exitosa o no.
     * 
     * @param operacionExitosa El valor a asignar.
     */
    public abstract void setOperacionExitosa(boolean operacionExitosa);

   
    /**
     * 
     * @param instanciaFacultada asignada a la linea de credito
     */
    public abstract void setInstanciaFacultadaSeleccionada(InstanciaFacultada instanciaFacultada); 

    /**
     * 
     * @param formalizacion asignada a la linea de credito
     */
    public abstract void setFormalizacionSeleccionada(Formalizacion formalizacion);

    /**
     * 
     * @param tipoAutorizacion asignado a la linea de credito
     */
    public abstract void setTipoAutorizacionSeleccionada(TipoAutorizacion tipoAutorizacion); 
	
    /**
     * Obtiene la instancia facultada asignada  a la linea de credito
     * @return InstanciaFacultada
     */
    public abstract InstanciaFacultada getInstanciaFacultadaSeleccionada();
    
    /**
     * Obtiene la entidad formalizacion asignada a la linea de credito
     * @return Formalizacion
     */
    public abstract Formalizacion getFormalizacionSeleccionada() ;

    /**
     * Obtiene el tipo de Autorizacion asignado a la linea de credito
     * @return TipoAutorizacion
     */
    public abstract TipoAutorizacion getTipoAutorizacionSeleccionada();
    
    
    
}