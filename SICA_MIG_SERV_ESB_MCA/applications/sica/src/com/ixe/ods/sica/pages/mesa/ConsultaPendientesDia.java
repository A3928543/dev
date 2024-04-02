/*
 * $Id: ConsultaPendientesDia.java,v 1.3.14.3 2010/08/28 00:31:22 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.mesa;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.ixe.contratacion.ContratacionException;
import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.CuentaEjecutivo;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.pages.ValidacionDealsPendietesDiaSicaPage;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * Al inicio de operaciones y durante el d&iacute;a, el promotor no puede saber cu&aacute;ntos 
 * Deals capturados se encuentran pendientes, es decir, que a&uacute;n no se encuentren 
 * Totalmente Liquidados, Cancelados o que est&aacute;n en Alta, o tengan autorizaciones
 * pendientes por parte de la Mesa, salvo por medio de la consulta general de Deals; 
 * si estas operaciones no son procesadas, no es posible que la mesa de cambios 
 * realice el cierre del d&iacute;a.  
 * 
 * @author Eduardo Sanchez
 * @version $Revision: 1.3.14.3 $ $Date: 2010/08/28 00:31:22 $
 */
public abstract class ConsultaPendientesDia extends ValidacionDealsPendietesDiaSicaPage
        implements PageRenderListener {	

	

    /**
     * Coordina el despliegue de los deals  
     *
     * @param pageEvent El evento de Tapestry de BeginRender.
     */
    public void pageBeginRender(PageEvent pageEvent) {
    	/*
    	if ( getModoSubmit() == 0){
    		obtenerPendientesDia();
    	}
    	Se comento porque las consultas se
    	ejecutan 2 veces
    	*/
  

    }

    /**
     * Coordina el despliegue de los deals.
     *
     * @param cycle El objeto controlador de Tapestry que administra el ciclo
     *              de cada petici&oacute;n (request) en el servidor web
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        setGuardaFacultad((String) getRequestCycle().getServiceParameters()[0]);
        if (getRequestCycle().getServiceParameters() != null &&
                getRequestCycle().getServiceParameters().length > 0) {
            String facultad = (String) getRequestCycle().getServiceParameters()[0];
            if (FacultySystem.SICA_CONS_DEAL_PEND.equals(facultad)) {
                setOcultaSwap(false);
            }
            if (FacultySystem.SICA_CONS_DEAL_PENMC.equals(facultad)) {
                setOcultaSwap(FacultySystem.SICA_CONS_DEAL_PENMC.equals(facultad));
            }
        }
        obtenerPendientesDia();
        
    }

    /**
     * Despliega los deals  que est&aacute;n pendientes de ser v&aacute;lidados
     * y los clientes con documentacion incompleta
     *
     * @param cycle El objeto controlador de Tapestry que administra el ciclo
     *              de cada petici&oacute;n (request) en el servidor web.
     */
    public void submit(IRequestCycle cycle) {
    	
    	Visit visit = (Visit) getVisit();	    		
		
		String idTipoEjecutivo = ((SupportEngine) getEngine())
				.getIdTipoEjecutivo();
		
		Integer idEjecutivo = visit.getUser().getIdPersona();
    	
		int modoSubmit = this.getModoSubmit() ;
      	
    	switch (modoSubmit) {
    	
	    	case 0: {
	    		obtenerPendientesDia();
	    		break;
	    	}
			case 1:
			{
				validarDealsPendientesDia();
				if (getPromotor() != null && 
						getPromotor().getIdPersona().intValue() > 0) {
					idEjecutivo = getPromotor().getIdPersona();
				}
				try{
					obtenerClientesDocumentacionIncompleta(idEjecutivo, idTipoEjecutivo);		
				}
				catch (Exception e) {
					IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans()
							.getBean("delegate");
					delegate.record(e.getMessage(), null);
				}
				break;
			}
			case 2:
			{
				if (getPromotor() != null && 
						getPromotor().getIdPersona().intValue() > 0) {
					filtraClientesPendientesDocumentacion(getPromotor(), idTipoEjecutivo);
				}
				
				break;
			}
			default :
			{
				break;
			}
		}         
    }

    /**
     * Validaci&oacute;n de Deals pendientes
     */
    private void validarDealsPendientesDia() {
        Visit visit = (Visit) getVisit();
        int idUsuario = visit.getUser().getIdUsuario();
        
        List deals;
        List dealsSwaps;
        try {
        	 
        	deals = getSicaServiceData().findDealsPendientesDia(idUsuario);
            dealsSwaps = getSicaServiceData().findSwapCierreDelDia();            
            //Checa Deals Detenidos por Banxico
            setDealsPendientesDocumentacion(validarDealsPendietesDiaBlocker(deals,
                    DEALS_PENDIENTES_POR_DOCUMENTACION, false));
            //Cjeca deals detenidos por plantilla pendiente de autorizacion
            setDealsPendientesPlantilla(validarDealsPendietesDiaBlocker(deals,
                    DEALS_PENDIENTES_POR_PLANTILLA, false));
            //Checa Deals Detenidos por Horario
            setDealsPendientesHorario(validarDealsPendietesDiaBlocker(deals,
                    DEALS_PENDIENTES_POR_HORARIO, false));
            //Checa Deals Detenidos por Banxico
            setDealsPendientesBanxico(validarDealsPendietesDiaBlocker(deals,
                    DEALS_PENDIENTES_POR_BANXICO, false));
            //Checa Deals Detenidos por Toma
            setDealsPendientesLinTomaEnFirme(validarDealsPendietesDiaBlocker(deals,
                    DEALS_PENDIENTES_POR_TOMA_EN_FIRME, false));
            //Checa Deals Detenidos por Monto
            setDealsPendientesMonto(validarDealsPendietesDiaBlocker(deals,
                    DEALS_PENDIENTES_POR_MONTO, false));
            //Checa Deals Detenidos por No Balanceado
            setDealsPendientesNoBalanceado(validarDealsPendietesDiaBlocker(deals,
                    DEALS_PENDIENTES_POR_BALANCE, false));
            //Checa Deals Detenidos por Modificacion
            setDealsPendientesModCan(validarDealsPendietesDiaBlocker(deals,
                    DEALS_PENDIENTES_POR_MODIFICACION, false));
            //Checa Deals Detenidos por Detalles Pendientes
            setDealsDetallesPendientes(validarDealsPendietesDiaBlocker(deals,
                    DEALS_PENDIENTES_POR_ALGUN_DETALLE, false));
            //Checa Deals Detenidos por Completar
            setDealsPendientesCompletar(validarDealsPendietesDiaBlocker(deals,
                    DEALS_PENDIENTES_POR_COMPLETAR, false));
            //Checa Deals Detenidos por Completar ( estatus de alta solo para las paginas de
            // ConsultaPendientesDia )
            setDealsPendientesDiaAlta(validarDealsPendietesDiaBlocker(deals,
                    DEALS_PENDIENTES_POR_DEALS_PENDIENTES_ALTA, false));
            //Checa Deals Detenidos por Swap
            setDealsSwap(validarDealsPendietesDiaBlocker(dealsSwaps,
                    DEALS_PENDIENTES_POR_SWAP, false));
            //Checa Deals Detenidos por Contrato Sica
            setDealsPendientesContratoSica(validarDealsPendietesDiaBlocker(deals,
                    DEALS_PENDIENTES_POR_CONTRATO, false));
            // Checa Deals Detenidos por cambio en RFC para Facturaci&oacute;n Electr&oacute;nica.
            setDealsPendientesRfc(validarDealsPendietesDiaBlocker(deals, DEALS_PENDIENTES_POR_RFC,
                    false));
            // Checa Deals Detenidos por Email de Facturaci&oacute;n Electr&oacute;nica.
            setDealsPendientesEmail(validarDealsPendietesDiaBlocker(deals,
                    DEALS_PENDIENTES_POR_EMAIL, false));
            //Checa Deals Detenidos por Solicitud de SobrePrecio
            setDealsPendientesSolicitudSobrePrecio(validarDealsPendietesDiaBlocker(deals,
                    DEALS_PENDIENTES_POR_SOLICITUD_SOBREPRECIO, false));
            setFechaUltimaActualizacion(new Date());
        }
        catch (SicaException e) {
            debug(e);
        }
    }    

    /**
     *Consulta los deals pendientes y 
     *los clientes con documentacion
     *incompleta
     */
    public void obtenerPendientesDia(){
    	Visit visit = (Visit) getVisit();	    		
		
		String idTipoEjecutivo = ((SupportEngine) getEngine())
				.getIdTipoEjecutivo();
		
		Integer idEjecutivo = visit.getUser().getIdPersona();
         	validarDealsPendientesDia();            	
 		
 		try {
			obtenerClientesDocumentacionIncompleta(idEjecutivo, idTipoEjecutivo);
		} catch (Exception e) {
			IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans()
					.getBean("delegate");
			delegate.record(e.getMessage(), null);
		}
 		
    }

    /**
     * Obtiene los clientes con Documentacion
     * Incompleta.
     * @param idEjecutivo Id del promotor del cual se
     * filtraran las cuentas
     * @param idTipoEjecutivo - Tipo de ejecutivo
     */
    private void obtenerClientesDocumentacionIncompleta(Integer idEjecutivo, String idTipoEjecutivo) {
    	setClientesPendienteDocumentacion(null);
    	List cuentas = getSicaServiceData().findCuentasEjecutivo(
				idTipoEjecutivo, idEjecutivo);
		
    	Iterator iterator = cuentas.iterator();
		
		Map registrosMap = new HashMap();
		List ListPersonas = new ArrayList();
		
		while (iterator.hasNext()) {
			
			CuentaEjecutivo cuentaEjecutivo = (CuentaEjecutivo) iterator.next();
			String noCuenta = cuentaEjecutivo.getId().getNoCuenta();	

			Persona persona = getSicaServiceData().findPersonaByContratoSica(
					noCuenta);
			
			if ( persona == null) {
				continue;
			}
			
			Map registro = new HashMap();
			
			registro.put("noCuenta", noCuenta);			
			registro.put("nombreCorto", persona.getNombreCorto()); 
			registro.put("idPersona", persona.getIdPersona());
			registro.put("tipoPersona", persona.getTipoPersona());	
		
			boolean isPersonaFisica =( 
					Constantes.TP_ACTIVIDAD_EMPRESARIAL.equals(persona.getTipoPersona())
					|| Constantes.PERSONA_FISICA.equals(persona.getTipoPersona()) );
			
			
			try{
					String msgError = 
						getSicaServiceData().validaInformacionBUPcliente(persona.getIdPersona(),isPersonaFisica);
										
					if (msgError.length() > 0) {
						registro.put("mensajeError", msgError + " -");
					}
			}
			catch(SicaException se){
					_logger.error(se); 	
					continue;
			}

			ListPersonas.add(registro);
			registrosMap.put(persona.getIdPersona(), registro);
			
			}
				ListPersonas = 
					getSicaServiceData().validarDocumentacionClientes(ListPersonas);
		
		if( ListPersonas.size() >= MAX_CLIENTES_DOCUMENTACION){
			IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
			getBean("delegate");
			delegate.record("La consulta de clientes con documentacion faltante " +
			"ha rebasado la cantidad maxima de elementos, solo se muestran los primeros 10. ",null);
		}
		
		Iterator clientesIterator = ListPersonas.iterator();
		
	
		while (clientesIterator.hasNext()) {
			Map registro = (Map) clientesIterator.next();

			String noCuenta = (String) registro.get("noCuenta");
			Date fechaCompromiso = null;
			Integer numeroProrrogas = null;

			try {
				Map datosContratoSica = getSicaServiceData()
						.fechaEntregaDocumentacionCAM10(noCuenta);

				fechaCompromiso = (Date) datosContratoSica
						.get("fecha_comp_exp");
				numeroProrrogas = (Integer) datosContratoSica
						.get("prorroga_num");

			} catch (SicaException e) {
				IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
				getBean("delegate");
				delegate.record(e.getMessage(),null);
				
			} catch (ContratacionException e) {
				IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
				getBean("delegate");
				delegate.record(e.getMessage(),null);
				
			} catch (Exception e) {
				IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
				getBean("delegate");
				delegate.record(e.getMessage(),null);
				
			} 
			registro.put("fechaVencimiento", fechaCompromiso);			
			registro.put("numeroProrrogas", numeroProrrogas);
			

		}
		
 		setClientesPendienteDocumentacion(ListPersonas);
	
	}



    /**
     * Refresca la pantalla 
     * 
	 * @param cycle
	 *            El objeto controlador de Tapestry que administra el ciclo de
	 *            cada petici&oacute;n (request) en el servidor web.
     */
    public void actualizarDealsPendientes(IRequestCycle cycle) {

    }
    
    /**
     * Listener para el filtro de
     * clientes con documentacion incompleta
     * @param cycle
     */
    public void filtraClientesPendientesDocumentacion(EmpleadoIxe promotor, String idTipoEjecutivo ){
    	
    	if (promotor != null  && promotor.getIdPersona().intValue() > 0){
    		   	    		
    		Integer idEjecutivo = getPromotor().getIdPersona(); 
    		try{
    			obtenerClientesDocumentacionIncompleta(idEjecutivo, idTipoEjecutivo);
    		}
    		catch (Exception e) {
    			IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans()
    					.getBean("delegate");
    			delegate.record(e.getMessage(), null);
    		}
    }

    }
    /**
     * Regresa el arreglo con los estados de operaci&oacute;n normal y operaci&oacute;n restringida.
     *
     * @return int[] Un arreglo de Estados v&aacute;lidos.
     */
    protected int[] getEstadosValidos() {
        return new int[]{Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA};
    }

    /**
     * Regresa el valor de dealsPendientesPorLiquidar.
     *
     * @return List.
     */
    public abstract List getDealsPendientesPorLiquidar();

    /**
     * Fija el valor de dealsPendientesPorLiquidar.
     *
     * @param dealsPendientesPorLiquidar El valor a asignar.
     */
    public abstract void setDealsPendientesPorLiquidar(List dealsPendientesPorLiquidar);

    /**
     * Regresa el modo de ejecuci&oacute;n del bot&oacute;n
     *
     * @return int El modo de ejecuci&oacute;n
     */
    public abstract int getModoSubmit();

    /**
     * Fija el modo de ejecuci&oacute;n del bot&oacute;n
     *
     * @param modoRefresh El valor a asignar.
     */
    public abstract void setModoSubmit(int modoRefresh);

    /**
     * Regresa el valor de dealsPendientesNoBalanceado.
     *
     * @return <code>List</code> La lista que contiene
     */
    public abstract List getDealsPendientesNoBalanceado();

    /**
     * Fija el valor de dealsPendientesNoBalanceado
     *
     * @param dealsPendientesNoBalanceado El valor a asignar.
     */
    public abstract void setDealsPendientesNoBalanceado(List dealsPendientesNoBalanceado);

    /**
     * Regresa el valor de dealsPendientesDatosLiquidacion.
     *
     * @return List.
     */
    public abstract List getDealsPendientesDatosLiquidacion();

    /**
     * Fija el valor de dealsPendientesDatosLiquidacion.
     *
     * @param dealsPendientesDatosLiquidacion
     *         El valor a asignar.
     */
    public abstract void setDealsPendientesDatosLiquidacion(List dealsPendientesDatosLiquidacion);

    /**
     * Regresa el valor de dealsPendientesMonto.
     *
     * @return List.
     */
    public abstract List getDealsPendientesMonto();

    /**
     * Fija el valor de dealsPendientesMonto.
     *
     * @param dealsPendientesMonto El valor a asignar.
     */
    public abstract void setDealsPendientesMonto(List dealsPendientesMonto);

    /**
     * Regresa el valor de dealsPendientesPorLiquidar.
     *
     * @return List.
     */
    public abstract List getDealsPendientesHorario();

    /**
     * Fija el valor de dealsPendientesHorario.
     *
     * @param dealsPendientesHorario El valor a asignar.
     */
    public abstract void setDealsPendientesHorario(List dealsPendientesHorario);

    /**
     * Regresa el valor de dealsPendientesLinTomaEnFirme
     *
     * @return List.
     */
    public abstract List getDealsPendientesLinTomaEnFirme();

    /**
     * Fija el valor de dealsPendientesNoBalanceado
     *
     * @param dealsPendientesLinTomaEnFirme El valor a asignar.
     */
    public abstract void setDealsPendientesLinTomaEnFirme(List dealsPendientesLinTomaEnFirme);

    /**
     * Regresa el valor de dealsPendientesDesviacionTC
     *
     * @return List.
     */
    public abstract List getDealsPendientesDesviacionTC();

    /**
     * Fija el valor de dealsPendientesDesviacionTC
     *
     * @param dealsPendientesDesviacionTC El valor a asignar.
     */
    public abstract void setDealsPendientesDesviacionTC(List dealsPendientesDesviacionTC);

    /**
     * Regresa el valor de dealsSwap
     *
     * @return List.
     */
    public abstract List getDealsSwap();

    /**
     * Fija el valor de dealsSwap
     *
     * @param dSwap El valor a asignar.
     */
    public abstract void setDealsSwap(List dSwap);

    /**
     * Regresa el valor de dealsPendientesDocumentacion
     *
     * @return List.
     */
    public abstract List getDealsPendientesDocumentacion();

    /**
     * Fija el valor de dealsPendientesDocumentacion
     *
     * @param dealsPendientesDocumentacion El valor a asignar.
     */
    public abstract void setDealsPendientesDocumentacion(List dealsPendientesDocumentacion);

    /**
     * Regresa el valor de dealsPendientesPlantilla
     *
     * @return List.
     */
    public abstract List getDealsPendientesPlantilla();

    /**
     * Fija el valor de dealsPendientesPlantilla
     *
     * @param dealsPendientesPlantilla El valor a asignar.
     */
    public abstract void setDealsPendientesPlantilla(List dealsPendientesPlantilla);
    
    /**
     * Regresa el valor de dealsPendientesModCan
     *
     * @return la lista de Deals pendientes por modificaci&oacute;n o autorizaci&oacute;n.
     */
    public abstract List getDealsPendientesModCan();

    /**
     * Almacena los Deals que al cierre de la Mesa requieren modificaci&oacute;n o
     * cancelaci&oacute;n.
     *
     * @param dealsPendientesModCan lista de deals pendientes por requerir una autorizaci&oacute;n
     *                              por modificaci&oacute;n o cancelaci&oacute;n.
     */
    public abstract void setDealsPendientesModCan(List dealsPendientesModCan);

    /**
     * Regresa el valor de dealsDetallePendientes
     *
     * @return la lista de Deals pendientes.
     */
    public abstract List getDealsDetallesPendientes();

    /**
     * Almacena los Deals que al cierre de la Mesa tienen un detalle pendiente.
     *
     * @param dealsDetallesPendientes lista de deals que alguno de sus detalles
     *                                esta en status incompleto.
     */
    public abstract void setDealsDetallesPendientes(List dealsDetallesPendientes);

    /**
     * Regresa el valor de dealsPendientesCompletar
     *
     * @return la lista de Deals incompletos.
     */
    public abstract List getDealsPendientesCompletar();

    /**
     * Almacena los Deals que al cierre de la Mesa no han sido completados en su captura.
     *
     * @param dealsPendientesCompletar lista de deals en status de <code>AL</code>
     *                                 al cierre de la Mesa.
     */
    public abstract void setDealsPendientesCompletar(List dealsPendientesCompletar);

    /**
     * Regresa la lista con Deals que fueron capturados aun que el cliente no tiene
     * <code>ContratoSica</code>
     *
     * @return la lista de Deals sin <code>ContratoSica</code>.
     */
    public abstract List getDealsPendientesContratoSica();

	/**
	 * Almacena los Deals pendientes del Dia con status de Alta 
	 *
	 * @param dealsPendientesDiaAlta lista de deals en status de <code>AL</code>
	 */
	public abstract void setDealsPendientesDiaAlta(List dealsPendientesDiaAlta);

	/**
	 * Regresa la lista con los Deals pendientes del Dia con status de Alta 
	 *
	 * @return la lista de Deals Pendientes del Dia con status <code>AL</code>
	 */
	public abstract List getDealsPendientesDiaAlta();    
    
    /**
     * Almacena los Deals que al cierre de la Mesa carecen de <code>ContratoSica</code>.
     *
     * @param dealsPendientesContratoSica lista de deals que carecen de <code>ContratoSica</code>.
     */
    public abstract void setDealsPendientesContratoSica(List dealsPendientesContratoSica);
    
    /**
     * Regresa el valor de dealsPendientesRfc.
     *
     * @return List.
     */
    public abstract List getDealsPendientesRfc();

    /**
     * Establece el valor de dealsPendientesRfc.
     *
     * @param dealsPendientesRfc El valor a asignar.
     */
    public abstract void setDealsPendientesRfc(List dealsPendientesRfc);

    /**
     * Regresa el valor de dealsPendientesEmail.
     *
     * @return List.
     */
    public abstract List getDealsPendientesEmail();

    /**
     * Establece el valor de dealsPendientesEmail.
     *
     * @param dealsPendientesEmail El valor a asignar.
     */
    public abstract void setDealsPendientesEmail(List dealsPendientesEmail);

    /**
     * Regresa la lista con Deals que tiene solicitud por Sobreprecio pendiente.
     *
     * @return la lista de Deals pendientes por sobreprecio
     */
    public abstract List getDealsPendientesSolicitudSobrePrecio();

    /**
     * Almacena los Deals que al cierre de la Mesa tiene solicitud pendiente por sobreprecio.
     *
     * @param dealsPendSolSobrePrecio lista de deals pendientes por sobreprecio.
     */
    public abstract void setDealsPendientesSolicitudSobrePrecio(List dealsPendSolSobrePrecio);
    
    /**
     * Regresa el valor de dealsPendientesBanxico.
     *
     * @return List.
     */
    public abstract List getDealsPendientesBanxico();
    
    /**
     * Fija el valor de dealsPendientesBanxico.
     *
     * @param dealsPendientesBanxico El valor a asignar.
     */
    public abstract void setDealsPendientesBanxico(List dealsPendientesBanxico);

    /**
     * Regresa el valor de los clientes que tienen documentacion
     * sin entregar
     * @return List -
     */
    public abstract List getClientesPendienteDocumentacion();
    
    /**
     * Fija el valor de los clientes que tiene documentacion 
     * sin entregar
     * @param clientesPendientesDocumentacion Lista de clientes con 
     * documentacion pendiente
     */
    public abstract void setClientesPendienteDocumentacion(List clientesPendientesDocumentacion);

    /**
     * Regresa el valor de confirmar
     *
     * @return valor que permite saber si se aprueba el cambio de estado.
     */
    public abstract boolean getConfirmar();

    /**
     * Almacena el valor para saber el tipo de petici&oacute;n que se hace.
     *
     * @param confirmar el valor que permite saber si el request es de confirmaci&oacute;n.
     */
    public abstract void setConfirmar(boolean confirmar);	

    /**
     * Regresa el valor de ocultaSwap.
     *
     * @return boolean.
     */
    public abstract boolean isOcultaSwap();

    /**
     * Almacena el valor que permite ocultar el portlet de swap
     *
     * @param ocultaSwap el valor que permite ocultar el portlet de swap
     */
    public abstract void setOcultaSwap(boolean ocultaSwap);    
    
    /**
     * Regresa el valor de la Facultad.
     *
     * @return String. 
     */    
    public abstract String getGuardaFacultad();

    /**
     * Almacena el valor de la Facultad
     *
     * @param guardaFacultad el valor que permite guardar la Facultad
     */
    public abstract void setGuardaFacultad(String guardaFacultad);

    /**
     * Almacena la fecha de la &uacute;ltima lectura de datos en la pantalla.
     *
     * @param fechaUltimaActualizacion El valor a asignar.
     */
    public abstract void setFechaUltimaActualizacion(Date fechaUltimaActualizacion);
    
    /**
     * 
     * @return true clientesMinimizados false clientesMaximizados
     */
    public abstract boolean getClientesMinimizados();
    
    /**
     * 
     * @param value true clientesMinimizados false clientesMaximizados
     */
    public abstract void setClientesMinimizados(boolean value);
    
    /**
     * Devuelve el <code>EmpleadoIxe</code>
     * seleccionado en el combo de promotores
     * 
     * @return EmpleadoIxe
     */
    public abstract EmpleadoIxe getPromotor();
    
    /**
     * <code>EmpleadoIxe</code>
     * @param EmpleadoIxe - El promotor (EmpleadoIxe) 
     * to set
     */
    public abstract void setPromotor(EmpleadoIxe empleado);
    
    /**
     * Modelo que carga el combo de
     * promotores
     * @return IPropertySelectionModel Lista de promotores
     */
    public IPropertySelectionModel getPromotoresSicaModel() {
		List promotores = getSicaServiceData().findAllPromotoresSICA(
				((SupportEngine) getEngine()).getApplicationName());

		EmpleadoIxe empl = new EmpleadoIxe();
		empl.setIdPersona(new Integer(-1));
		
		empl.setNombre("--------" + "Selecciona un promotor" +
						 "--------------");
		
		promotores.add(0, empl);
		RecordSelectionModel model = 
			new RecordSelectionModel(promotores,
				"idPersona", "nombreCompleto");

		return model;
	}
    
    /**
	 * Determina si el usario que se firmo en el sistema es de mesa de control
	 * 
	 * @return True - si el usuario firmado es de mesa de control
	 */
	public  boolean isUsuarioMesaControl() {
		Visit visit = (Visit) getVisit();
		HashSet set = (HashSet) visit.getFaculties();
		
		return set.contains(FacultySystem.SICA_MESA_CONTROL);
	}
	
	public static int MAX_CLIENTES_DOCUMENTACION = 10;
   

   
}
