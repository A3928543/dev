package com.ixe.ods.sica.pages;

/*
 * $Id: ConsultaAcumuladosCV.java,v 1.1.2.6.12.2 2014/12/03 01:29:51 mejiar Exp $
 * 
 */


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.PosicionDelegate;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaSiteCache;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Broker;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.CanalMesa;
import com.ixe.ods.sica.model.Corte;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.BitacoraCorte;
import com.ixe.ods.sica.sdo.CorteMurexServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.sicamurex.dto.PosicionDTO;
import com.ixe.ods.sica.sicamurex.service.FTPService;
import com.ixe.ods.sica.sicamurex.service.FTPServiceImpl;
import com.ixe.ods.sica.sicamurex.service.SicaMurexService;
import com.ixe.ods.sica.sicamurex.service.SicaMurexServiceImpl;
import com.ixe.ods.sica.sicamurex.utils.ConstantesSICAMUREX;
import com.ixe.treasury.dom.common.FormaPagoLiq;

/**
 * 
 * @author HMASG771
 * 
 **/
public abstract class ConsultaAcumuladosCV extends SicaPage {

	
	public static final Logger LOGGER = Logger.getLogger(ConsultaAcumuladosCV.class);
	
	/** El bean corteMurexServiceData* */
    private CorteMurexServiceData corteMurexServiceData;
    
    private PosicionDelegate      posicionDelegate;
    
    /**
     * Getter bean corteMurexServiceData
     *
     * @return la referencia al bean
     * 		corteMurexServiceData.
     */
    public CorteMurexServiceData getCorteMurexServiceData() {
        return corteMurexServiceData;
    }
    
	public PosicionDelegate getPosicionDelegate() {
		return posicionDelegate;
	}
    
    
    /**
     * Activa el Modo de Operaci&oacute; as&iacute; como el T&iacute;tulo del Portlet de
     * B&uacute;squedas de la P&aacute;gina cada que esta se activa.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
	    super.activate(cycle);
	    String estadoCorte = ConstantesSICAMUREX.INICIO;
	    limpiarTodo();
	    if (FacultySystem.SICA_MESACAMBIOS.equals(cycle.getServiceParameters()[0])){
	    	if (!isHorarioRestringidoOrNormal()) {
	    		estadoCorte = ConstantesSICAMUREX.ESTADO_SICA_INCORRECTO;
        		throw new PageRedirectException("ErrorEstado");
        	}else if ( isHorarioRestringidoOrNormal() && validarEstadoCorte(ConstantesSICAMUREX.ENVIADO_MUREX)){
        		estadoCorte = ConstantesSICAMUREX.VISTA_PREVIA_DEALS_INTERBANCARIOS;
		    	generarVistaPreviaDealsInterbancarios();
		    	setMostrarDealsInterbancarios(true);
		    	setMostrarPanelBotonesDI(true);
		    	setMostrarPanelProcesadosDI(false);
		    	setMostrarIdDealInterbancario(false);
		    }//else if (isHorarioRestringidoOrNormal() && validarEstadoCorte(ConstantesSICAMUREX.CORTE_PROCESADO)){
		    		//SicaMurexService service          = getSicaMurexService();
		    		//List corteHoyList                 = service.getCorteDetalleByFechaHoy();
			    	//List dealsInterbancariosHoyList   = service.getDealsInterbancariosByFechaHoy();
			    	//setDealsInterbancarios(dealsInterbancariosHoyList);
			    	//setMostrarDealsInterbancarios(true);
			    	//setMostrarIdDealInterbancario(true);
			    	//setMostrarLigaParaGenerarNuevaVP(true);
			    	//setMostrarPanelBotonesDI(false);
					//setMostrarPanelProcesadosDI(false);
			    	//setMostrarAcumuladoCV(true);
					//setMostrarPanelBotonesACV(false);
					//setAcumuladosCV(corteHoyList);		    		
		    else {
		    	
	            setTituloActionPB("Centralizar Posici\u00f3n");
		    }
	    }
	    setEstadoTransferencia(estadoCorte);
    }
   

	/**
     * Define si el estado actual del sistema es Horario Válido para realizar la generacion
     * de la vista previa.
     * 
     * @return boolean
     */
    protected boolean isHorarioRestringidoOrNormal() {
    	int estadoSICA = getEstadoActual().getIdEstado();
    	return estadoSICA == Estado.ESTADO_OPERACION_RESTRINGIDA || estadoSICA == Estado.ESTADO_OPERACION_NORMAL;
    }

   

	/**
     * Define si el estado actual del sistema es Horario Válido para realizar la generacion
     * de la vista previa.
     * 
     * @return boolean
     */
    protected boolean isHorarioRestringido() {
    	int estadoSICA = getEstadoActual().getIdEstado();
    	return estadoSICA == Estado.ESTADO_OPERACION_RESTRINGIDA;
    }
    
    /**
     * Vac&iacute;a la lista de clientes.
     */
    public void limpiar() {
        setAcumuladosCV(new ArrayList());
    }

    /**
     * Obtiene los acumulados de compra/venta de divisas para presentarlos 
     * y poder realizar la transferencia  hacia MUREX.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void fetch(IRequestCycle cycle) {
    	LOGGER.info("Iniciando Consulta de acumulados de compra/venta de divisas");
    	SicaMurexService service;
        List acumuladoCVList = new ArrayList();
        try {
        	//limpiarTodo();
        	LOGGER.debug("Opciones seleccionadas"+ "DF  = "+ getDivisasFrecuentes());
        	LOGGER.debug("Opciones seleccionadas"+ "DNF = "+ getDivisasNoFrecuentes());
        	LOGGER.debug("Opciones seleccionadas"+ "MA  = "+ getMetalesAmonedados());
        	Visit visit     = (Visit) getVisit();
        	service = getSicaMurexService();
        	acumuladoCVList = service.getPosicionDivisas(new Integer(visit.getIdMesaCambio()),
        																	visit.getUser().getClave(),
        																	getDivisasFrecuentes(),
        																	getDivisasNoFrecuentes(),
        																	getMetalesAmonedados());
        	LOGGER.debug("El numero de cortes generados hoy es de.   "+service.findNumCorteByFechaHoy());
        	setAcumuladosCV(acumuladoCVList);
        	if(acumuladoCVList != null && acumuladoCVList.size() > 0){
        		setMostrarAcumuladoCV(true);
        		setMostrarPanelBotonesACV(true);
        		setMostrarBotonesACV(true);
        	}
        	setEstadoTransferencia(ConstantesSICAMUREX.VISTA_PREVIA);
        }
        catch (SicaException e) {
            debug(e);
            limpiar();
            getDelegate().record(e.getMessage(), null);
        }
    }

	private SicaMurexServiceImpl getSicaMurexService() {
		
		return (SicaMurexServiceImpl)getApplicationContext().
													   getBean(ConstantesSICAMUREX.SICA_MUREX_SERVICE);
	}

	/***
	 * Se transfiere el archivo de acumulados de compra/venta via FTP a MUREX
	 * @param cycle
	 */
    public void transferirArchivo(IRequestCycle cycle){
    	SicaMurexService service;
    	try{
    		LOGGER.info("iniciando transferencia del archivo de hoy!!!"+ new Date().toString());
	    	Visit visit       = (Visit) getVisit();
	    	String usuario    = visit.getUser().getClave();
	    	service = getSicaMurexService();
	    	Corte corteHoy  = service.findCorteByFechaHoy(); //obteniendo el ultimo corte del día
	    	BitacoraCorte ultimaBitacora = service.findMaxBitacoraCorteByIdCorte(corteHoy); //obteniendo la ultima bitacora
	    	
	    	if (ultimaBitacora.getUsuario() != null && !usuario.equals(ultimaBitacora.getUsuario()))
	    	{
	    		LOGGER.info("Usuario que genero VP: " + ultimaBitacora.getUsuario());
	    		LOGGER.info("Usuario que intenta el envio a MX: " + usuario);
	    		LOGGER.info("El usuario que intenta hacer el envio no es el mismo que genero la ultima VP");
	    		//throw new PageRedirectException("ErrorEnvioMurex");
	    		setMostrarBotonesACV(false);
	    		throw new SicaException("El usuario que intenta hacer el envio no es el mismo que genero la ultima VP.");
	    		//getDelegate().record("El usuario que intenta hacer el envio no es el mismo que genero la ultima VP.", null);
	    	}
	    	
	    	if (ultimaBitacora.getUsuario() != null && usuario != null)
	    	{
	    		LOGGER.info("Usuario que genero VP: " + ultimaBitacora.getUsuario());
	    		LOGGER.info("Usuario que intenta el envio a MX: " + usuario);	    		
	    	}
	    	boolean respuesta = false;
			FTPService tCS    = getFTPService();
			respuesta         = tCS.transferir(usuario);
			LOGGER.info("Resultado de  la transferencia del archivo a MUREX !!!: "+ new Date().toString() +" = <"+ respuesta+ ">");
			setMostrarAcumuladoCV(true);
			setMostrarPanelBotonesACV(true);
			setMostrarBotonesACV(false);
			getDelegate().record("Corte enviado a MUREX.", null);
			setEstadoTransferencia(ConstantesSICAMUREX.ENVIADO_MUREX);
			//aqui setear el id_corte para la tabla de SC_POSICION_LOG_CORTES ya que el archivo ya fue enviado a MX
//			service = getSicaMurexService();
			service.setIdCortePorEnvioMX();
    	}catch (SicaException e) {
            debug(e);
            limpiar();
            getDelegate().record(e.getMessage(), null);
            return;
        }
    }

	private FTPServiceImpl getFTPService() {
		return (FTPServiceImpl) getApplicationContext().getBean(ConstantesSICAMUREX.FTP_SERVICE);
	}
	
	public SicaSiteCache getSicaSiteCache() {
	    return ((SicaSiteCache) getApplicationContext().getBean( ConstantesSICAMUREX.SICA_SITE_SERVICE));
	}
    
    
    /**
     * Generacion de la vista previa de los DEALS Interbancarios.
     */
    public void vistaPreviaDealsInterbancarios(IRequestCycle cycle){
    	generarVistaPreviaDealsInterbancarios();
    	setMostrarAcumuladoCV(false);
    	setMostrarDealsInterbancarios(true);
    	setMostrarPanelBotonesDI(true);
    }

    /**
     * Se genera la vista previa de los DEALS Interbancarios
     */
	private void generarVistaPreviaDealsInterbancarios() {
		LOGGER.info("Iniciando la vista previa de DEALS Interbancarios");
    	SicaMurexService service = getSicaMurexService();
    	setDealsInterbancarios(service.getVistaPreviaDealsReinicioPosicion());
    	setEstadoTransferencia(ConstantesSICAMUREX.VISTA_PREVIA_DEALS_INTERBANCARIOS);
    	LOGGER.info("Mostrando DEALS Interbancarios...");
	}
	
	
	/**
	 * Se aplican los DEALS Interbancarios a la posicion de SICA.
	 * @param cycle
	 */
	public void actualizarPosicionDI(IRequestCycle cycle){
		LOGGER.info("Actualizando posicion, DEALS Interbancarios...");
		Visit visit       	 = (Visit) getVisit();
    	Integer idMesa    	 = new Integer(visit.getIdMesaCambio());
    	String usuario    	 = visit.getUser().getClave();
    	String idCanal    	 = visit.getIdCanal();
    	boolean isCompra  	 = false;
    	SicaServiceData sdata= getSicaServiceData();
    	MesaCambio mesa   	 = sdata.findMesaCambio(idMesa.intValue());
    	Canal canal       	 = sdata.findCanal(idCanal);
    	CanalMesa canalM  	 = new CanalMesa(canal, canal.getMesaCambio());
		List dealIList    	 = getDealsInterbancarios();
		
		SicaMurexService service = getSicaMurexService();
    	Corte corteHoy  = service.findCorteByFechaHoy(); //obteniendo el ultimo corte del día
    	BitacoraCorte ultimaBitacora = service.findMaxBitacoraCorteByIdCorte(corteHoy); //obteniendo la ultima bitacora
    	
    	try{
    	if (ultimaBitacora.getUsuario() != null && !usuario.equals(ultimaBitacora.getUsuario()))
    	{
    		LOGGER.info("Usuario que hizo el envio a Murex: " + ultimaBitacora.getUsuario());
    		LOGGER.info("Usuario que intenta crear los Deals Interbancarios: " + usuario);
    		LOGGER.info("El usuario que intenta crear los deals intebancarios no es el mismo que realizo el envio a Murex");
    		throw new SicaException("El usuario que intenta crear los Deals Intebancarios no es el mismo que realizo el envio a Murex.");
    		
    	}
    	}
    	catch (SicaException e) {
            debug(e);
            limpiar();
            getDelegate().record(e.getMessage(), null);
            return;
        }
    	
    	if (ultimaBitacora.getUsuario() != null && usuario != null)
    	{
    		LOGGER.info("intenta crear los Deals Interbancarios: " + ultimaBitacora.getUsuario());
    		LOGGER.info("Usuario que intenta crear los deals intebancarios " + usuario);	    		
    	}
		
		LOGGER.info("Deals Interbancarios obtenidos: " + dealIList!= null ? new Integer( dealIList.size()): new Integer(0));
		//SicaMurexService service   = getSicaMurexService();
		SicaSiteCache cache        = getSicaSiteCache();	
		List formaLiquidacionLista = cache.obtenerFormasPagoLiq(visit.getTicket());
		Map formaLiquidacionMapa   = getFormaLiquidacionMapa(formaLiquidacionLista);
		for (Iterator iterator = dealIList.iterator(); iterator.hasNext();){
			try{
				Deal deal       = getDEAL(sdata, canalM, visit.getUser());
				PosicionDTO dto = (PosicionDTO) iterator.next();
				isCompra        = dto.getOperacion().equals(ConstantesSICAMUREX.COMPRA_STR)?true:false;
				Divisa divisa   = sdata.findDivisa(dto.getDivisa());
				Deal resultDeal = sdata.crearDealInterbancario(deal, 
															isCompra, 
															new Date(), 
															Math.abs(dto.getTcDI().doubleValue()), 
															ConstantesSICAMUREX.FORMA_LIQUIDACION_EFECTIVO, 
															Math.abs(dto.getMontoDivisaDI().doubleValue()), 
															divisa, 
															mesa, 
															visit.getUser(), 
															false);
				actualizaDealDetalle(resultDeal,sdata,isCompra,formaLiquidacionMapa);
				dto.setIdDealDI(resultDeal.getIdDeal());
				LOGGER.info("Deal creado" + ToStringBuilder.reflectionToString(resultDeal));
				service.actualizarReinicioPosicion(usuario, resultDeal.getIdDeal(), divisa, isCompra);
			}
			
			catch (Exception e) {
				LOGGER.error("No se pudo crear el DEAL Interbancario");
				e.printStackTrace();
			}	
		}
		service.actualizarEstadoCorteProcesado(usuario);
		setDealsInterbancarios(dealIList);
		setMostrarAcumuladoCV(false);
		setMostrarPanelBotonesDI(false);
		setMostrarPanelProcesadosDI(true);
		setMostrarIdDealInterbancario(true);
		getDelegate().record("DEALS  Interbancarios creados satisfactoriamente.",null);
    	LOGGER.info("DEALS Interbancarios creados y posicion actualizada...");		
	}

	
	private void actualizaDealDetalle(Deal resultDeal, SicaServiceData sdata, boolean isCompra, Map formaLiquidacionMapa) {
		List dealDetalleLista = resultDeal.getDetalles();
		String mnemonico = "";
		for (Iterator iterator = dealDetalleLista.iterator(); iterator
				.hasNext();) {
			DealDetalle dealDetalle = (DealDetalle) iterator.next();
			String divisa = dealDetalle.getDivisa().getIdDivisa();
			if(Divisa.PESO.equals(divisa)){
				if(isCompra){
					mnemonico = ((FormaPagoLiq)formaLiquidacionMapa.get(divisa+ConstantesSICAMUREX.ENTREGAMOS)).getMnemonico(); 		
				}else{
					mnemonico = ((FormaPagoLiq)formaLiquidacionMapa.get(divisa+ConstantesSICAMUREX.RECIBIMOS)).getMnemonico();
				}
				dealDetalle.setMnemonico(mnemonico);
			}else{
				if(isCompra){
					mnemonico = ((FormaPagoLiq)formaLiquidacionMapa.get(divisa+ConstantesSICAMUREX.RECIBIMOS)).getMnemonico(); 		
				}else{
					mnemonico = ((FormaPagoLiq)formaLiquidacionMapa.get(divisa+ConstantesSICAMUREX.ENTREGAMOS)).getMnemonico();
				}
			}
			dealDetalle.setMnemonico(mnemonico);
			sdata.update(dealDetalle);
		}
	}


	private Map getFormaLiquidacionMapa(List formaLiquidacionLista) {
		Map formaLiquidacionMapa = new  HashMap();
		for (Iterator iterator = formaLiquidacionLista.iterator(); iterator
				.hasNext();){
			String llaveMnemonico="";
			
			FormaPagoLiq formaPagoLiq  = (FormaPagoLiq) iterator.next();
			LOGGER.info("Mapeo de Formas de Liquidacion Divisa" +  formaPagoLiq.getIdDivisa());
			if(formaPagoLiq.getClaveFormaLiquidacion().equals("EFECTI") && 
					formaPagoLiq.getStatus().equals("ACTI")){
				
				if (formaPagoLiq.getClaveOperacion().intValue() == new Integer( ConstantesSICAMUREX.RECIBIMOS).intValue()) {
						if(formaLiquidacionMapa.get(formaPagoLiq.getIdDivisa()+ConstantesSICAMUREX.RECIBIMOS ) == null){
							llaveMnemonico = formaPagoLiq.getIdDivisa()+ConstantesSICAMUREX.RECIBIMOS;
							formaLiquidacionMapa.put(llaveMnemonico, formaPagoLiq );
							LOGGER.info("Se agrega al catalogo: " +  llaveMnemonico);
						}else {
							llaveMnemonico = formaPagoLiq.getIdDivisa()+ConstantesSICAMUREX.RECIBIMOS;
							formaLiquidacionMapa.remove(llaveMnemonico);
							LOGGER.info("Se elimina del catalogo: " +  llaveMnemonico);
						} 
					
				 }else if (formaPagoLiq.getClaveOperacion().intValue() == new Integer( ConstantesSICAMUREX.ENTREGAMOS).intValue()){ 
						
					 if(formaLiquidacionMapa.get(formaPagoLiq.getIdDivisa()+ConstantesSICAMUREX.ENTREGAMOS ) == null){
						llaveMnemonico = formaPagoLiq.getIdDivisa()+ConstantesSICAMUREX.ENTREGAMOS;
						formaLiquidacionMapa.put(llaveMnemonico, formaPagoLiq );
						LOGGER.info("Se agrega al catalogo: " +  llaveMnemonico);
					}else{
						llaveMnemonico = formaPagoLiq.getIdDivisa()+ConstantesSICAMUREX.ENTREGAMOS;
						formaLiquidacionMapa.remove(llaveMnemonico);
						LOGGER.info("Se elimina del catalogo: " +  llaveMnemonico);
					}
				 }
			}
		}
		return formaLiquidacionMapa;
	}


	public void mostrarResumenCorte(IRequestCycle cycle){
		
		List acumuladosCV = getAcumuladosCV();
			
		setMostrarAcumuladoCV(true);
		setMostrarPanelBotonesACV(false);
		setMostrarDealsInterbancarios(true);
		setMostrarPanelBotonesDI(false);
		setMostrarPanelProcesadosDI(false);
		
		if(acumuladosCV == null || acumuladosCV.size() == 0){
			SicaMurexService service          = getSicaMurexService();
    		List corteHoyList                 = service.getCorteDetalleByFechaHoy();
	    	setAcumuladosCV(corteHoyList);
		}
	}
	
	public void resetarBanderas(){
		
		setMostrarAcumuladoCV(false);
		setMostrarPanelBotonesACV(false);
		setMostrarDealsInterbancarios(false);
		setMostrarPanelBotonesDI(false);
		setMostrarPanelProcesadosDI(false);
		setMostrarLigaParaGenerarNuevaVP(true);
		
	} 

	private Deal getDEAL(SicaServiceData sdata, CanalMesa canalM,IUsuario usuario){ 
	    Deal deal = new Deal();
	    List brokerLista     = null;
		Broker broker        = null;
		ParametroSica parametroSica = sdata.findParametro(ParametroSica.BROKER_DEAL_REINICIO_POSICION);
		Persona persona             = sdata.findPersonaByContratoSica(parametroSica.getValor());
    	brokerLista                 = sdata.findBrokerByIdPersona(persona.getIdPersona());
    	for (Iterator iterator = brokerLista.iterator(); iterator.hasNext();) {
			Broker b = (Broker) iterator.next();
			 if (b.getId().getPersonaMoral().getIdPersona().intValue() == persona.getIdPersona().intValue()) {
                 broker = b;
             }
		}
	    ContratoSica contratoSica  = sdata.findContratoSicaByIdPersona(persona.getIdPersona());
	    EmpleadoIxe promotor       = sdata.findPromotorByContratoSica(contratoSica.getNoCuenta());
	    deal.setObservaciones("Deal Reinicio Posicion");
	    deal.setCanalMesa(canalM);
	    deal.setBroker(broker);
    	deal.setContratoSica(contratoSica);
	    deal.setTipoDeal(Deal.TIPO_INTERBANCARIO);
	    deal.setTipoValor(ConstantesSICAMUREX.CASH);
	    deal.setPromotor(promotor);
	    deal.setUsuario(usuario);
	    deal.setFechaCaptura(new Date());
	    deal.setStatusDeal(ConstantesSICAMUREX.DEAL_ESTADO_ALTA);
	    deal.setEnviarAlCliente(false);
	    deal.setMensajeria(false);
	    deal.setPagoAnticipado(false);
	    deal.setSeleccionado(false);
	    deal.setTomaEnFirme(false);
	    deal.setVersion(new Integer(0));
	    
    return deal;
	}

	/**
     * Este metodo indica si el corte ya se ha enviado a MUREX.
     * @return boolean resultado
     */
    private  boolean validarEstadoCorte(String estado){
    	boolean result= false;
    	SicaMurexService service = getSicaMurexService();
    	Corte corteHoy           =  service.findCorteByFechaHoy();
    	if(corteHoy!= null && corteHoy.getEstatusCorte() != null && corteHoy.getEstatusCorte().equals(estado)){
    		result=true;
    	}
    	return result;
    } 
    
    /**
     * Se reinicia la lista de acumulados de compra/venta.
     * @param cycle
     */
    public void regresar(IRequestCycle cycle){
		setAcumuladosCV(new ArrayList());
    }
    
    /**
     * Establece el T&iacute;tulo del Portlet de B&uacute;squedas de la P&aacute;gina de
     * acuerdo al modo de operaci&oacute;n de la misma.
     * @param tituloActionPB El T&iacute;tulo del Portlet de B&uacute;squedas de la P&aacute;gina.
     */
    public abstract void setTituloActionPB(String tituloActionPB);

    public abstract String getTituloActionPB();
    
    /**
     * @param clientes La lista de Clientes encontrados.
     */
    public abstract void setAcumuladosCV(List acumuladosCV);

    public abstract List getAcumuladosCV();
    
   	/**
	 *@param valor de Divisa Frecuente
	 */
	public abstract void setDivisasFrecuentes(boolean df);

	/**
	 *@param valor de Divisa Frecuente
	 */
	public abstract void setDivisasNoFrecuentes(boolean dnf);
	
	/**
	 *@param valor de Divisa Frecuente
	 */
	public abstract void setMetalesAmonedados( boolean ma);
	
	/**
	 *@param Obtener valor de Divisa Frecuente
	 */
	public abstract boolean getDivisasFrecuentes();

	/**
	 *@param Obtener valor de Divisa Frecuente
	 */
	public abstract boolean getDivisasNoFrecuentes();
	
	/**
	 *@param Obtener valor de Divisa Frecuente
	 */
	public abstract boolean getMetalesAmonedados();
	
	 /**
     * Establece el Estado en el cual se encuentra la transferencia del corte de SICA a MUREX.
     * @param modo El Modo de operaci&oacute;n de la P&aacute;gina.
     */
    public abstract void setEstadoTransferencia(String estadoCorte);
    public abstract String getEstadoTransferencia();
    
    /**
     * Indicador que permite ocultar o mostrar botones
     * @return boolean muestra botones.
     */
    public abstract boolean getMostrarBotonesACV();    
    public abstract void setMostrarBotonesACV(boolean mostrarBotones);
	    
    /**
     * Deals Interbancarios que reinician la posicion de las divisas.
     */
    public abstract void setDealsInterbancarios(List dealsInterbancariosVP);    
    public abstract List  getDealsInterbancarios();
       
    public abstract boolean  getMostrarPanelBotonesDI();
    public abstract void  setMostrarPanelBotonesDI(boolean mp);
    
    public abstract boolean  getMostrarAcumuladoCV();
    public abstract void  setMostrarAcumuladoCV(boolean mp);
    
    public abstract boolean  getMostrarDealsInterbancarios();
    public abstract void  setMostrarDealsInterbancarios(boolean mp);
    
    public abstract boolean  getMostrarPanelBotonesACV();
    public abstract void  setMostrarPanelBotonesACV(boolean mp);
    
    public abstract boolean  getMostrarPanelProcesadosDI();
    public abstract void  setMostrarPanelProcesadosDI(boolean mp);
    
    public abstract boolean  getMostrarIdDealInterbancario();
    public abstract void  setMostrarIdDealInterbancario(boolean mp);
    
    public abstract boolean  getMostrarLigaParaGenerarNuevaVP();
    public abstract void  setMostrarLigaParaGenerarNuevaVP(boolean mp);
    
	}