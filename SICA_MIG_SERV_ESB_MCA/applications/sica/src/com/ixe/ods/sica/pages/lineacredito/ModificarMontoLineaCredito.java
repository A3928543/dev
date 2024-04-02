/*
 * $Id: ModificarMontoLineaCredito.java,v 1.5.26.3.6.1.54.2 2016/07/13 23:58:47 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.lineacredito;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.seguridad.sdo.SeguridadServiceData;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.dao.ClienteDao;
import com.ixe.ods.sica.lineacredito.service.LineaCreditoService;
import com.ixe.ods.sica.lineacredito.service.TipoOperacion;
import com.ixe.ods.sica.lineacredito.service.dto.BitacoraLineaCreditoDTO;
import com.ixe.ods.sica.lineacredito.service.dto.MensajeMailLineaCreditoDto;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Formalizacion;
import com.ixe.ods.sica.model.InstanciaFacultada;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.TipoAutorizacion;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.services.LineaCreditoConstantes;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * Clase para consultar  la informaci&oacute;n de una L&iacute;nea de Cr&eacute;dito.
 *
 * @author Edgar I. Leija (eleija), Javier Cordova (jcordova)
 * @version  $Revision: 1.5.26.3.6.1.54.2 $ $Date: 2016/07/13 23:58:47 $
 */
public abstract class ModificarMontoLineaCredito extends SicaPage {

	public static final Logger LOGGER = Logger.getLogger(ModificarMontoLineaCredito.class);
	public final int EXCESO_LINEA_CREDITO = 6;
	
	/**
     * Se ejecuta cada que se activa la p&aacute;gina.
     * Carga una Linea de Credito para editar el Monto.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
    	super.activate(cycle);
    	boolean operacionExitosa = getOperacionExitosa();
    	StringBuffer mensaje = new StringBuffer();
    	String paytm = "", fv = "";
    	limpiarVariables();
    	LOGGER.info("Iniciando la modificacion de la linea de Credito  " + getIdLineaCredito() + "  operacionExistosa: "+ getOperacionExitosa());
    	if(!operacionExitosa){
    		LineaCambio lineaCredito = (LineaCambio) getLineaCreditoService().findLineaCredito(new Integer(getIdLineaCredito()));

	    	if (lineaCredito != null) {
	    		setLineaCredito(lineaCredito);
	    		
	    		setTipoAutorizacionSeleccionada(getLineaCredito().getTipoAutorizacion());
	    		setFormalizacionSeleccionada(getLineaCredito().getFormalizacion());
	    		setInstanciaFacultadaSeleccionada(getLineaCredito().getInstanciaFacultada());
	    		if(!getModoPromocion()){
		    		setNuevoMontoFV(lineaCredito.getImporteFV());
		    		setNuevoMontoPAyTF(lineaCredito.getImportePAyTF());
		    		setNuevaFechaVencimiento(lineaCredito.getVencimiento());
		    		
		    		mensaje.append("Informaci\u00f3n: Se le notifica que ");
		    		if(getLineaCredito().getExcesoPAyTF() == null)
		    			getLineaCredito().setExcesoPAyTF(new Integer(0));
		    		
		    		if(getLineaCredito().getExcesoFV() == null)
		    			getLineaCredito().setExcesoFV(new Integer(0));
		    			
		    		if(getLineaCredito().getExcesoPAyTF().intValue() == 5)
		    			paytm = "la l\u00ednea de cr\u00e9dito 'Pago Anticipado/Toma en Firme' tiene 5 excesos aplicados. ";
		    		
		    		if(getLineaCredito().getExcesoFV().intValue() == 5)
		    			fv = "La l\u00ednea de cr\u00e9dito 'Fecha Valor' tiene 5 excesos aplicados.";
		    		
		    		if(!"".equals(paytm) || !"".equals(fv))
		    		{
		    			mensaje.append(paytm).append(fv).append(" Solo son permitidas 6 autorizaciones de exceso en cada l\u00ednea de cr\u00e9dito.");
		    			getDelegate().record(mensaje.toString(), null);
		    		}
	    		}
	    	}
    	}
    }
    
    public void limpiarVariables()
    {
    	setExcesoMontoFV(false);
    	setExcesoMontoPAyTF(false);
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
     * Actualiza el Monto de una L&iacute;nea de Cr&eacute;dito.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void modificarMontoLineaCredito(IRequestCycle cycle) {
    	LOGGER.info("Actualizando informacion de Linea de Credito: " + ToStringBuilder.reflectionToString(getLineaCredito()));
    	LineaCreditoService service = getLineaCreditoService();
    	setOperacionExitosa(false);
    	setNuevoMontoPAyTF(getNuevoMontoPAyTF().setScale(2, BigDecimal.ROUND_HALF_UP));
    	setNuevoMontoFV(getNuevoMontoFV().setScale(2, BigDecimal.ROUND_HALF_UP));
    	List  listaModificaciones = new ArrayList(); 
    	String mensaje = "";
        String tipoOperacion = "";
        String observaciones = LineaCreditoConstantes.CAMBIO_INICIAL;
        BigDecimal importeBitacora = new BigDecimal("0"); 
        String formatoFecha= "dd-MM-yyyy";
        boolean huboExcesoFV = false, huboExcesoPAyTF = false;
    	LOGGER.info("Actualizando informacion de Linea de Credito" + getIdLineaCredito() );
    	try {
	    	
    		validarCamposLineaCredito();
	        Visit visit = (Visit) getVisit();
	        if (!getDelegate().getHasErrors()) {
                
                Date fechaVencimientoFormateada= modificarFormatoFecha(formatoFecha,getNuevaFechaVencimiento());
    			setNuevaFechaVencimiento(fechaVencimientoFormateada);
                
                
                if (getLineaCredito().getImportePAyTF().doubleValue() > getNuevoMontoPAyTF().doubleValue()) {
                    tipoOperacion = TipoOperacion.DISMINUCION;
                    importeBitacora = getLineaCredito().getImportePAyTF().subtract(getNuevoMontoPAyTF());
                    Assert.isTrue(getLineaCredito().getSaldoPAyTF().doubleValue() > importeBitacora.doubleValue()   , "El nuevo monto PAyTF debe ser mayor al Saldo"
                    		+ " PAyTF  de la l\u00ednea de cr\u00e9dito ");
                    getLineaCredito().setImportePAyTF(getNuevoMontoPAyTF());
                    listaModificaciones.add(new BitacoraLineaCreditoDTO(tipoOperacion, visit.getUser(),importeBitacora, LineaCreditoConstantes.CAMBIO_INICIAL + LineaCreditoConstantes.IMPORTE_PAYTF));
                }else if (getLineaCredito().getImportePAyTF().doubleValue() < getNuevoMontoPAyTF().doubleValue()) {
                	
                	if(getExcesoMontoPAyTF())
                	{
                		tipoOperacion = TipoOperacion.EXCESO;
                		getLineaCredito().setExcesoPAyTF(new Integer(getLineaCredito().getExcesoPAyTF().intValue() + 1));
                		observaciones = LineaCreditoConstantes.EXCESO_PAYTF;
                		huboExcesoPAyTF = true;
                	}
                	else
                	{
                	tipoOperacion = TipoOperacion.AUMENTO;
                		observaciones = LineaCreditoConstantes.CAMBIO_INICIAL + LineaCreditoConstantes.IMPORTE_PAYTF;
                	}
                	
                	importeBitacora = getNuevoMontoPAyTF().subtract(getLineaCredito().getImportePAyTF());
                	getLineaCredito().setImportePAyTF(getNuevoMontoPAyTF());
                	listaModificaciones.add(new BitacoraLineaCreditoDTO(tipoOperacion, visit.getUser(),importeBitacora, observaciones));
                }
                
                if (getLineaCredito().getImporteFV().doubleValue() > getNuevoMontoFV().doubleValue()) {
                    tipoOperacion = TipoOperacion.DISMINUCION;
                    importeBitacora = getLineaCredito().getImporteFV().subtract(getNuevoMontoFV());
                    Assert.isTrue(getLineaCredito().getSaldoFV().doubleValue() > importeBitacora.doubleValue()   , "El nuevo monto FV no puede rebasar el Saldo"
                    		+ " Fecha Valor  de la l\u00ednea de cr\u00e9dito ");
                    getLineaCredito().setImporteFV(getNuevoMontoFV());
                    listaModificaciones.add(new BitacoraLineaCreditoDTO(tipoOperacion, visit.getUser(),importeBitacora,LineaCreditoConstantes.CAMBIO_INICIAL + LineaCreditoConstantes.IMPORTE_FV));
                } else if (getLineaCredito().getImporteFV().doubleValue() < getNuevoMontoFV().doubleValue()) {
                	
                	if(getExcesoMontoFV())
                	{
                		tipoOperacion = TipoOperacion.EXCESO;
                		getLineaCredito().setExcesoFV(new Integer(getLineaCredito().getExcesoFV().intValue() + 1));
                		observaciones = LineaCreditoConstantes.EXCESO_FV;
                		huboExcesoFV = true; 
                	}
                	else
                	{
                	tipoOperacion = TipoOperacion.AUMENTO;
                		observaciones = LineaCreditoConstantes.CAMBIO_INICIAL + LineaCreditoConstantes.IMPORTE_FV;
                	}
                	
                	importeBitacora = getNuevoMontoFV().subtract(getLineaCredito().getImporteFV());
                	getLineaCredito().setImporteFV(getNuevoMontoFV());
                	listaModificaciones.add(new BitacoraLineaCreditoDTO(tipoOperacion, visit.getUser(), importeBitacora,observaciones));
                }
                
                if (getLineaCredito().getVencimiento().compareTo(getNuevaFechaVencimiento()) != 0) {
                	tipoOperacion = TipoOperacion.CAMBIO_FECHA_VENCIMIENTO;
                	importeBitacora = new BigDecimal("0");
                	getLineaCredito().setVencimiento(getNuevaFechaVencimiento());                	
                	listaModificaciones.add(new BitacoraLineaCreditoDTO(tipoOperacion, visit.getUser(), importeBitacora, LineaCreditoConstantes.CAMBIO_INICIAL +LineaCreditoConstantes.FECHA_VENCIMIENTO));
                }
                
                if(getLineaCredito().getInstanciaFacultada()==null ||
                		(getLineaCredito().getInstanciaFacultada().getIdInstanciaFacultada().intValue() != 
                		getInstanciaFacultadaSeleccionada().getIdInstanciaFacultada().intValue())){
                	tipoOperacion = TipoOperacion.CAMBIO_INSTANCIA_FACULTADA;
                	importeBitacora = new BigDecimal("0");
                	getLineaCredito().setInstanciaFacultada(getInstanciaFacultadaSeleccionada());
                	listaModificaciones.add(new BitacoraLineaCreditoDTO(tipoOperacion, visit.getUser(), importeBitacora,LineaCreditoConstantes.CAMBIO_INICIAL +LineaCreditoConstantes.INSTANCIA_FACULTADA));
                }
                
                if(getLineaCredito().getTipoAutorizacion()== null ||
                		(getLineaCredito().getTipoAutorizacion().getIdTipoAutorizacion().intValue() != 
                		getTipoAutorizacionSeleccionada().getIdTipoAutorizacion().intValue())){
                	tipoOperacion = TipoOperacion.CAMBIO_TIPO_AUTORIZACION;
                	importeBitacora = new BigDecimal("0");
                	getLineaCredito().setTipoAutorizacion(getTipoAutorizacionSeleccionada());
                	listaModificaciones.add(new BitacoraLineaCreditoDTO(tipoOperacion, visit.getUser(), importeBitacora, LineaCreditoConstantes.CAMBIO_INICIAL + LineaCreditoConstantes.TIPO_AUTORIZACION));
                }
                
                if(getLineaCredito().getFormalizacion()== null ||
                		getLineaCredito().getFormalizacion().getIdFormalizacion().intValue() != 
                		getFormalizacionSeleccionada().getIdFormalizacion().intValue()){
                	tipoOperacion = TipoOperacion.CAMBIO_FORMALIZACION;
                	importeBitacora = new BigDecimal("0");
                	getLineaCredito().setFormalizacion(getFormalizacionSeleccionada());
                	listaModificaciones.add(new BitacoraLineaCreditoDTO(tipoOperacion, visit.getUser(),  importeBitacora, LineaCreditoConstantes.CAMBIO_INICIAL + LineaCreditoConstantes.FORMALIZACION ));
                }
                    
                service.actualizarLineaCredito(getLineaCredito(),listaModificaciones);
   
                if(huboExcesoPAyTF)
                	sendMailExceso(getLineaCredito().getExcesoPAyTF(), "Pago Anticipado/Toma en Firme");
                
                if(huboExcesoFV)
                	sendMailExceso(getLineaCredito().getExcesoFV(), "Fecha Valor");
                
                getDelegate().record(mensaje + "La l\u00ednea de cr\u00e9dito se modific\u00f3 " 
                		+ "con \u00e9xito. Oprima 'Regresar' para terminar.", null);
                
                setOperacionExitosa(true);
                activate(cycle);
	        }
        } catch (DataAccessException e) {
            e.printStackTrace();
            mensaje = "Ocurrio un error al actualizar la l\u00ednea de cr\u00e9dito: ";
			getDelegate().record(mensaje +  e.getMessage(), null);
        }catch (Throwable t) {
			t.printStackTrace();
			mensaje = "Ocurrio un error al actualizar la l\u00ednea de cr\u00e9dito: ";
			getDelegate().record(mensaje +  t.getMessage(), null);
		}
        
    }


	private Date modificarFormatoFecha(String formato, Date fecha) {
		
		try {
			SimpleDateFormat format1 = new SimpleDateFormat(formato);
			return format1.parse(format1.format(fecha));
		}catch (ParseException e) {
		    throw new ApplicationRuntimeException("La fecha de vencimiento es incorrecta");
		}
	}

	private void validarCamposLineaCredito() {
		
		LineaCambio lineaCredito = getLineaCredito();
		
		LOGGER.info("Validando informacion de  Linea de Credito: " + lineaCredito.getIdLineaCambio() + 
				"montos" + lineaCredito.getImportePAyTF()+ "FV: " + lineaCredito.getImporteFV());
		
		Assert.isTrue((getNuevoMontoPAyTF().doubleValue() >= 1), "El importe Pago Anticipado/Toma en Firme de la l\u00ednea de cr\u00e9dito debe ser mayor o "
                + "igual a 1.00.");
		 
		Assert.isTrue(getNuevoMontoFV().doubleValue() >= 1,"El importe Fecha Valor de la l\u00ednea de cr\u00e9dito debe ser mayor o "
                + "igual a 1.00.");
		
		Assert.isTrue(getTipoAutorizacionSeleccionada().getIdTipoAutorizacion() != null && 
       		 getTipoAutorizacionSeleccionada().getIdTipoAutorizacion().intValue() > 0 , "Favor de seleccionar el Tipo de Autorizacion");
		
		Assert.isTrue(getFormalizacionSeleccionada().getIdFormalizacion() != null &&
        		 getFormalizacionSeleccionada().getIdFormalizacion().intValue() > 0, "Favor de seleccionar una opcion en el campo Formalizacion");
		
		Assert.isTrue(getInstanciaFacultadaSeleccionada().getIdInstanciaFacultada() != null &&
        	 getInstanciaFacultadaSeleccionada().getIdInstanciaFacultada().intValue() > 0,"Favor de seleccionar la Instancia Facultada");
		
		Assert.isTrue(getNuevaFechaVencimiento() != null ,"La Nueva Fecha de Vencimiento no debe estar vacia");
		
        Assert.isTrue( !(getNuevoMontoFV().doubleValue() == lineaCredito.getImporteFV().doubleValue() &&
					getNuevoMontoPAyTF().doubleValue() == lineaCredito.getImportePAyTF().doubleValue() &&
					getNuevaFechaVencimiento().equals(lineaCredito.getVencimiento()) &&
					getLineaCredito().getFormalizacion().getIdFormalizacion().intValue() == getFormalizacionSeleccionada().getIdFormalizacion().intValue() &&
					getLineaCredito().getInstanciaFacultada().getIdInstanciaFacultada().intValue() == getInstanciaFacultadaSeleccionada().getIdInstanciaFacultada().intValue() && 
					getLineaCredito().getTipoAutorizacion().getIdTipoAutorizacion().intValue() == getTipoAutorizacionSeleccionada().getIdTipoAutorizacion().intValue()),
					"No ha modificado alg\u00FAn par\u00E1metro de la l\u00EDnea de Cr\u00E9dito");
        
		Assert.isTrue(isValidDate(getNuevaFechaVencimiento()), "La fecha de vencimiento debe ser mayor a la fecha actual");
		
		//                                           marcaron el checkbox de exceso
		if(getExcesoMontoPAyTF())
		{
			// Limite de excesos alcanzado
			if(getLineaCredito().getExcesoPAyTF() != null)
			{
				Assert.isTrue(getLineaCredito().getExcesoPAyTF().intValue() < EXCESO_LINEA_CREDITO, 
			      "No es posible aplicar el exceso en la l\u00EDnea de cr\u00E9dito 'Pago Anticipado y Toma en Firme' porque ha alcando el m\u00E1ximo permitido.");
			}
			
			Assert.isTrue(!(getLineaCredito().getImportePAyTF().doubleValue() == getNuevoMontoPAyTF().doubleValue()), 
		      "No es posible aplicar el exceso en la l\u00EDnea de cr\u00E9dito 'Pago Anticipado y Toma en Firme' si los montos son iguales.");
			
			// Se pretende realizar una disminución de linea de credito y marcaron el checkbox
			Assert.isTrue(!(getLineaCredito().getImportePAyTF().doubleValue() > getNuevoMontoPAyTF().doubleValue()), 
				      "Si va a realizar una disminuci\u00F3n de la l\u00EDnea de cr\u00E9dito 'Pago Anticipado y Toma en Firme' no debe marcar el checkbox de exceso.");
		}
		
		if(getExcesoMontoFV())
		{
			// Limite de excesos alcanzado
			if(getLineaCredito().getExcesoFV() != null)
			{
				Assert.isTrue(getLineaCredito().getExcesoFV().intValue() < EXCESO_LINEA_CREDITO, 
			      "No es posible aplicar el exceso en la l\u00EDnea de cr\u00E9dito 'Fecha Valor' porque ha alcando el m\u00E1ximo permitido.");
			}
			
			Assert.isTrue(!(getLineaCredito().getImporteFV().doubleValue() == getNuevoMontoFV().doubleValue()), 
		      "No es posible aplicar el exceso en la l\u00EDnea de cr\u00E9dito 'Fecha Valor' si los montos son iguales.");
			
			// Se pretende realizar una disminución de linea de credito y marcaron el checkbox
			Assert.isTrue(!(getLineaCredito().getImporteFV().doubleValue() > getNuevoMontoFV().doubleValue()),
		      "Si va a realizar una disminuci\u00F3n de la l\u00EDnea de cr\u00E9dito 'Fecha Valor' no debe marcar el checkbox de exceso.");
		}
	}
	
	/**
	 * Obtiene los canales asociados a un usuario
	 * @param idUsuario id del Usuario
	 * @param applicationName Nombre de la aplicacion
	 * @return Lista de canales
	 */
	private List getCanalesUsuario(int idUsuario, String applicationName) {
		
		SeguridadServiceData seguridadServiceData = getSeguridadServiceData();
		List facultadesUsuario = seguridadServiceData.findFacultadesSimplesByUserAndSystem(
				idUsuario, getApplicationName());
        List canales = new ArrayList();
        for (Iterator iterator = facultadesUsuario.iterator(); iterator.hasNext();) {
            String nombreFacultad = (String) iterator.next();
            if (nombreFacultad.startsWith("SICA_CAN")) {
                Canal canal = getSicaServiceData().findCanalByFacultad(nombreFacultad.trim());
                if (canal != null) {
                    canales.add(canal);
                }
            }
        }
        return canales;
	}
	
	public void sendMailExceso(Integer numExceso, String lineaCredito)
	{
		Integer year = new Integer(Calendar.getInstance().get(Calendar.YEAR));
		ContratoSica contratoSica = getSicaServiceData().findContratoSicaByIdPersona(getLineaCredito().getCorporativo().getIdPersona());
		MensajeMailLineaCreditoDto dto = getMensajeMailLineaCredito();
		String datosExcesoAutorizacion[] = null;
		
		String exceso = null, numTrimestre = "";
		String datosExcesoPosterior[] = null;
		
		String subject = null, body = null;
		
		ClienteDao clienteDao = (ClienteDao) getApplicationContext().getBean("clienteDao");
    	Map personaContrato = clienteDao.findPromotorAsignadoCliente(getLineaCredito().getCorporativo().getIdPersona());
    	Integer idPersonaPromotor = new Integer(personaContrato.get("ID_PERSONA").toString());
		Map usuario               = clienteDao.findUsuarioByIdPersona(idPersonaPromotor);
    	Integer idUsuario = new Integer(usuario.get("ID_USUARIO").toString());
    	List canales              = getCanalesUsuario(idUsuario.intValue(),getApplicationName());
    	List emailList            = new ArrayList();
    	String emailPromotor      = (String)usuario.get("EMAIL");
    	String emails[] = null;
    	int trimestre;
		
		switch(numExceso.intValue())
		{
			case 4: case 6:
				exceso = numExceso.intValue() == 4 ? dto.getExcesoCuarto() : (numExceso.intValue() == 6 ? dto.getExcesoSexto() : numExceso.toString());
				datosExcesoPosterior = new String[]{contratoSica.getNoCuenta(), 
						                            getLineaCredito().getCorporativo().getNombreCorto(),
													exceso,
													lineaCredito};
				subject = MessageFormat.format(dto.getExcesoPosteriorSubject(), datosExcesoPosterior);
				body = MessageFormat.format(dto.getExcesoPosteriorBody(), datosExcesoPosterior);
			break;
			default:
				exceso = numExceso.toString();
				trimestre = obtenerTrimestre(new Date());
				switch(trimestre)
				{
					case 1: case 3:
						numTrimestre = trimestre + "er";
						break;
					case 2:
						numTrimestre = trimestre + "do";
						break;
					case 4:
						numTrimestre = trimestre + "to";
					break;
				}
				datosExcesoAutorizacion = new String[]{contratoSica.getNoCuenta(), 
									                   getLineaCredito().getCorporativo().getNombreCorto(),
									                   numExceso.toString(),
									                   lineaCredito,
									                   numTrimestre,
									                   year.toString()};
				subject = MessageFormat.format(dto.getExcesoAutorizacionSubject(), datosExcesoAutorizacion);
				body = MessageFormat.format(dto.getExcesoAutorizacionBody(), datosExcesoAutorizacion);
		}
		
    	emailList.add(emailPromotor);
    	for (Iterator iterator = canales.iterator(); iterator.hasNext();) 
    	{
			Canal canal = (Canal) iterator.next();

			try
			{
				emailList.addAll(Arrays.asList(canal.getEmailJefe().split("\\,")));
			}
			catch(NullPointerException ne)
			{
				ne.printStackTrace();
			}
		}
    	
    	try
    	{
    		emails =(String[])emailList.toArray(new String[emailList.size()]);
    		getGeneralMailSender().enviarMail(dto.getFrom(), emails, null, subject, body);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
	}
   
    /**
     * Regresa a la pantalla anterior.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void terminarOperacion(IRequestCycle cycle) {
    	AprobacionLineaCredito nextPage = (AprobacionLineaCredito) cycle.getPage(getPaginaAnterior());
    	cycle.setServiceParameters(new String[]{getModoPromocion() == true ? FacultySystem.SICA_APROB_LINCRED_P : FacultySystem.SICA_APROB_LINCRED_C});
    	nextPage.activate(cycle);
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
     * Verifica que la fecha sea correcta
     * @param fecha     Fecha de vencimiento ingresada por el usuario
     * @return boolean  True o False, dependiendo si la fecha es válida
     */
    public boolean isValidDate(Date fecha) {
        try {
            Calendar fechaNac = GregorianCalendar.getInstance();
            fechaNac.setTime(fecha);
            Calendar diaActual = GregorianCalendar.getInstance();
            diaActual.setTime(new Date());
            if (diaActual.after(fechaNac)) {
            	setNuevaFechaVencimiento(null);
            	return false;
            }
        }
        catch (Exception e) {
        	setNuevaFechaVencimiento(null);
            return false;
        }
        return true;
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
     * Permite saber si se viene del Men&uacute; de Promoci&oacute;n o de Cr&eacute;dito.
     *
     * @return boolean El Modo de la P&aacute;gina.
     */
    public abstract boolean getModoPromocion();

    /**
     * Establece si la P&aacute;gina viene del Men&uacute; de Promoci&oacute;n o de Cr&eacute;dito.
     *
     * @param modoPromocion El Modo de la P&aacute;gina.
     */
    public abstract void setModoPromocion(boolean modoPromocion);

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
     * Obtiene el valor del ID de la L&iacute;nea de Cr&eacute;dito.
     * 
     * @return String
     */
    public abstract String getIdLineaCredito();
    
    /**
     * Asigna el nuevo valor de idLineaCredito.
     * 
     * @param idLineaCredito El ID de la L&iacute;nea de Cr&eacute;dito
     */
    public abstract void setIdLineaCredito(String idLineaCredito);
    
            
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
    
    /**
     * Obtiene el nuevo monto para Pago Anticipado y Toma en Firme
     * @return BigDecimal el nuevo monto para Pago Anticipado y Toma en Firme
     */
    public abstract BigDecimal getNuevoMontoPAyTF();
    
    /**
     * Establece el nuevo monto para Pago Anticipado y Toma en Firme
     * @param nuevoMonto
     */
    public abstract void setNuevoMontoPAyTF(BigDecimal nuevoMonto);
        
    /**
     * Obtiene el nuevo monto para Fecha Valor
     * @return BigDecimal el nuevo monto Fecha Valor
     */
    public abstract BigDecimal getNuevoMontoFV();
    
    /**
     * Establece el nuevo monto para Fecha Valor
     * @param nuevoMonto
     */
    public abstract void setNuevoMontoFV(BigDecimal nuevoMonto);
    
    
    /**
     * Obtiene la nueva fecha de vencimiento de la linea de credito
     * @return java.util.Date nueva fecha de la linea de credito
     */
    public abstract Date getNuevaFechaVencimiento();
    
    /**
     * Establece la nueva fecha de vencimiento de la linea de credito
     * @param nuevaFecha
     */
    public abstract void setNuevaFechaVencimiento(Date nuevaFecha);
    

    /**
     * Obtiene el nuevo indicador de Exceso para el monto PAyTF
     * @return
     */
    public abstract boolean getExcesoMontoPAyTF();
    
    /**
     * Establece el nuevo indicador de Exceso para el monto PAyTF
     */
    public abstract void setExcesoMontoPAyTF(boolean excesoMontoPAyTF);
    
    /**
     * Obtiene el nuevo indicador de Exceso para el monto FV 
     * @return
     */
    public abstract boolean getExcesoMontoFV();
    
    
    /**
     * Establece el nuevo indicador de Exceso para el monto FV
     */
    public abstract void setExcesoMontoFV(boolean excesoMontoFV);
    
}
