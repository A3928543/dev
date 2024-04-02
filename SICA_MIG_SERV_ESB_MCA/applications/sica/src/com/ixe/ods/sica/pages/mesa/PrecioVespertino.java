/*
 * $Id: PrecioVespertino.java,v 1.26.38.1.18.3.12.1.10.1.8.2 2020/12/03 21:59:08 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.mesa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.PosicionDelegate;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.dao.DescuadreDao;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Corte;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.model.TcMinMaxTeller;
import com.ixe.ods.sica.model.TipoPizarron;
import com.ixe.ods.sica.pages.ValidacionDealsSicaPage;
import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.ixe.ods.sica.services.CancelacionMailSender;
import com.ixe.ods.sica.services.H2HService;
import com.ixe.ods.sica.services.MailVespertinoService;
import com.ixe.ods.sica.sicamurex.service.SicaMurexService;
import com.ixe.ods.sica.sicamurex.service.SicaMurexServiceImpl;
import com.ixe.ods.sica.sicamurex.utils.ConstantesSICAMUREX;
import com.ixe.ods.sica.utils.DateUtils;

/**
 * Permite al usuario definir el Precio Referencia y el Spread Vespertino con el que se
 * operar&aacute; en el Horario Vespertino (Precio Vespertino), as&iacute; como la consulta de
 * Deals Pendientes antes del cierre de la Mesa.
 *
 * @author Edgar I. Leija
 * @version $Revision: 1.26.38.1.18.3.12.1.10.1.8.2 $ $Date: 2020/12/03 21:59:08 $
 */
public abstract class PrecioVespertino extends ValidacionDealsSicaPage {

    /**
     * Coordina el despliegue de los deals que estan pendientes por liquidar y permite definir el
     * Precio Vespertino as&iacute; como el Spread Vespertino.
     *
     * @param cycle El objeto controlador de Tapestry que administra el ciclo
     *              de cada petici&oacute;n (request) en el servidor web
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        setPrecioReferencia(getSicaServiceData().findPrecioReferenciaActual());
        setParametro(getSicaServiceData().findParametro(ParametroSica.SPREAD_VESPERTINO));
        setModoRefresh(1);
        refrescarDeals(cycle);
    }

    /**
     * Validaci&oacute;n de Deals en precio vespertino.
     *
     */
    private void validarDealsPrecioVesp() {
    	//DAO para detectar el descuadre de la posicion.
    	DescuadreDao descDao = (DescuadreDao) getApplicationContext().getBean("descuadreDao");
    	//Se obtienen los deals a checar
    	List deals = new ArrayList();
        List dealsSwaps = new ArrayList();
        List reversos = new ArrayList();
        try {
            deals = getSicaServiceData().findDealsBlockerVespertino();
            dealsSwaps = getSicaServiceData().findSwapCierreDelDia();
            reversos = getSicaServiceData().findReversoByStatusPendiente();
        }
        catch (SicaException e1) {
            warn(e1.getMessage(), e1);
        }
        try {
            //Checa Deals Detenidos por Banxico
            setDealsPendientesDocumentacion(validarDealsBlocker(deals,
                    DEALS_PENDIENTES_POR_DOCUMENTACION, true, false));
          //Checa Deals Detenidos por Banxico
            setDealsPendientesPlantilla(validarDealsBlocker(deals,
                    DEALS_PENDIENTES_POR_PLANTILLA, true, false));
            //Checa Deals Detenidos por Horario
            setDealsPendientesHorario(validarDealsBlocker(deals,
                    DEALS_PENDIENTES_POR_HORARIO, false, false));
            //Checa Deals Detenidos por Banxico
            setDealsPendientesBanxico(new ArrayList());
            //setDealsPendientesBanxico(validarDealsBlocker(deals,
            //        DEALS_PENDIENTES_POR_BANXICO, true, false));
            //Checa Deals Detenidos por Toma
            setDealsPendientesLinTomaEnFirme(validarDealsBlocker(deals,
                    DEALS_PENDIENTES_POR_TOMA_EN_FIRME, true, false));
            //Checa Deals Detenidos por Monto
            setDealsPendientesMonto(validarDealsBlocker(deals,
                    DEALS_PENDIENTES_POR_MONTO, false, false));
            //Checa Deals Detenidos por No Balanceado
            setDealsPendientesNoBalanceado(validarDealsBlocker(deals,
                    DEALS_PENDIENTES_POR_BALANCE, true, false));
            //Checa Deals Detenidos por Modificacion
            setDealsPendientesModCan(validarDealsBlocker(deals,
                    DEALS_PENDIENTES_POR_MODIFICACION, false, false));
            //Checa Deals Detenidos por Detalles Pendientes
            setDealsDetallesPendientes(validarDealsBlocker(deals,
                    DEALS_PENDIENTES_POR_ALGUN_DETALLE, true, false));
            //Checa Deals Detenidos por Completar
            setDealsPendientesCompletar(validarDealsBlocker(deals,
                    DEALS_PENDIENTES_POR_COMPLETAR, true, false));
            //Checa Deals Detenidos por Swap
            setDealsSwap(validarDealsBlocker(dealsSwaps,
                    DEALS_PENDIENTES_POR_SWAP, true, false));
            //Checa Deals Detenidos por Contrato Sica
            setDealsPendientesContratoSica(validarDealsBlocker(deals,
                    DEALS_PENDIENTES_POR_CONTRATO, false, false));
            //Checa los Reversos Pendientes
            setReversosPendientes(reversos);
            //Checa Deals Detenidos por Solicitud de SobrePrecio
            setDealsPendientesSolicitudSobrePrecio(validarDealsBlocker(deals,
                    DEALS_PENDIENTES_POR_SOLICITUD_SOBREPRECIO, false, false));
            //Deals no cancelados que causan el descuadre
            setDealsNoCancelados(descDao.findDescuadreDetallesDealsNoCancelados());
            //Deals cancelados que causan el descuadre
            setDealsCancelados(descDao.findDescuadreDetallesDealsCancelados());
        }
        catch (Exception e) {
            warn(e.getMessage(), e);
        }
        if (getDealsPendientesDocumentacion().size() < 1 &&
        		getDealsPendientesPlantilla().size() < 1 &&
                getDealsPendientesHorario().size() < 1 &&
                getDealsPendientesLinTomaEnFirme().size() < 1 &&
                getDealsPendientesMonto().size() < 1 &&
                getDealsPendientesSolicitudSobrePrecio().size() < 1 &&
                getDealsPendientesNoBalanceado().size() < 1 &&
                getDealsPendientesModCan().size() < 1 &&
                getDealsDetallesPendientes().size() < 1 &&
                getDealsPendientesCompletar().size() < 1 &&
                getDealsSwap().size() < 1 && getDealsPendientesContratoSica().size() < 1 &&
                getReversosPendientes().size() < 1 && getDealsCancelados().size() < 1 &&
                getDealsNoCancelados().size() < 1) {
            setConfirmar(true);
        }
        else {
            setConfirmar(false);
        }
    }

    /**
     * Despliega los deals que est&aacute;n pendientes de ser v&aacute;lidados
     * para la generaci&oacute;n de movimientos Contables. En caso de no existir
     * dichos deals, cambia el Estado del Sistema a Operaci&oacute;n Vespertina.
     *
     * @param cycle El objeto controlador de Tapestry que administra el ciclo
     *              de cada petici&oacute;n (request) en el servidor web.
     */
    public void refrescarDeals(IRequestCycle cycle) {
        if (getModoRefresh() == 1) {
        	validarDealsPrecioVesp();
        }
    }

    /**
     * Obtiene el Id del Administrador del Sistema.
     *
     * @return el valor del Administrador del Sistema.
     */
    public Integer getIdAdministrador() {
        ParametroSica idUsuario = (ParametroSica) getSicaServiceData().
                find(ParametroSica.class, ParametroSica.ADMIN_ID_USUARIO);
        return new Integer(idUsuario.getValor());
    }

    /**
     * En caso de no existir deals pendientes de ser atendidos permite actualizar el precio
     * vespertino, llama a generar la utilidad autom&aacute;tica.
     *
     * @param cycle El objeto controlador de Tapestry que administra el ciclo
     *              de cada petici&oacute;n (request) en el servidor web.
     */
    public void actualizarPrecioVespertino(IRequestCycle cycle) {
        Visit visit = (Visit) getVisit();
        int idUsuario = visit.getUser().getIdUsuario();
        
        if (getModoRefresh() == 1) {
            refrescarDeals(cycle);
            return;
        }
        try {
        	//validaCreacionDealsReinicioPosicion(); ya no se valida el corte del día ahora valida que la posicion este en cero en divisas frecuentes
        	if (getSicaServiceData().getBanderaValidaPosicionCierre().equalsIgnoreCase("S")){
        	validaPosicion();
        	_logger.info("Entro a validar posicion antes de hacer el cierre: " + getSicaServiceData().getBanderaValidaPosicionCierre());
        	}
            if (getConfirmar() && Estado.ESTADO_OPERACION_RESTRINGIDA == getSicaServiceData().
                    findEstadoSistemaActual().getIdEstado()) {
                Map precios = new HashMap();
                try {
                    MailVespertinoService mvs = (MailVespertinoService) getApplicationContext().
                            getBean("mailVespertinoService");
                    mvs.enviarCorreoCierreVespertino(getClass().getResource("logo_ixe.jpg"));
                    getPrecioReferencia().getPreRef().
                            setMetodoActualizacion(PrecioReferenciaActual.METODO_VESPERTINO);
                    precios.put("preRef", getPrecioReferencia());
                    precios.put("param", getParametro());
                    ParametroSica tipCambVesp = getSicaServiceData().
                            findParametro(ParametroSica.TIPO_CAMBIO_VESPERTINO);
                    tipCambVesp.setValor(getPrecioReferencia().getPreRef().
                            getPrecioSpot().toString());
                    precios.put("tipCambVesp", tipCambVesp);
                    precios.put("idUsuario", new Integer(idUsuario));
                    getSicaServiceData().generarUtilidadEstadoVespertino(getTicket(), precios, getRequestCycle().getRequestContext().getRequest().getRemoteAddr(),
                    		((Visit) getVisit()).getUser());
                    setPrecioReferencia(getSicaServiceData().findPrecioReferenciaActual());
                    
                    _logger.error("-->actualizarPrecioVespertino(" + new Date() + "): Inicia validacion de deals para H2H en el cierre vespertino...");
                    
                    H2HService h2hService = (H2HService) getApplicationContext().getBean("h2hService");
                    h2hService.validarDealsH2HAlCierreVespertino();
                    
                    _logger.error("-->actualizarPrecioVespertino(" + new Date() + "): Finalizo la validacion de deals para H2H en el cierre vespertino.");
                    
                    ControlHorarios nextPage = (ControlHorarios) cycle.getPage("ControlHorarios");
                    nextPage.activate(cycle);
                }
                catch (SicaException e) {
                	if (_logger.isDebugEnabled()) {
                        _logger.error(e);
                    }
                    getDelegate().record("Surgi\u00f3 un error al querer escribir la Utilidad de " +
                            "Manera autom\u00e1tica.", null);
                }
            }
            else if (!getConfirmar()) {
                getDelegate().record("No se puede dar de Alta el Tipo de Cambio Vespertino si " +
                        "existen Deals o Reversos pendientes de atender.", null);
            }
            else
            if (getConfirmar() && Estado.ESTADO_OPERACION_RESTRINGIDA != getSicaServiceData().
                    findEstadoSistemaActual().getIdEstado()) {
                getDelegate().record("No se puede dar de Alta el Tipo de Cambio Vespertino. " +
                        "Estado del Sistema Inconsistente", null);
            }
            generarTcMinMaxTellerCierre();
        }
        catch (SicaException e) {
            getDelegate().record(e.getMessage(), null);
        }
    }

    private SicaMurexServiceImpl getSicaMurexService() {
		return (SicaMurexServiceImpl)getApplicationContext().
													   getBean(ConstantesSICAMUREX.SICA_MUREX_SERVICE);
	}
    
    private boolean validaCreacionDealsReinicioPosicion() {
    	SicaMurexService sicaMurexService = getSicaMurexService();
    	Corte corte                       = sicaMurexService.findCorteByFechaHoy();
    	    	
    	if(corte == null || !corte.getEstatusCorte().equals(ConstantesSICAMUREX.CORTE_PROCESADO)){
    		throw new SicaException("No se puede dar de Alta el Tipo de Cambio Vespertino, " +
                    "si no se han creado los DEALS Interbancarios que reinician la posici\u00f3n");
    	}
		return true;
	}

    private boolean validaPosicion() {
    	_logger.info("validando las posiciones de las divisas frecuentes ");
    	Visit visit     = (Visit) getVisit();
    	    	
    	PosicionDelegate posicionDelegate = getSicaMurexService().getPosicionDelegate();
    	List posicion = posicionDelegate.getPosicionDivisas(new Integer(visit.getIdMesaCambio()));

    	for (Iterator iterator = posicion.iterator(); iterator.hasNext();) {
			PosicionVO pvo = (PosicionVO) iterator.next();
			BigDecimal compras = new BigDecimal(0);
			BigDecimal ventas = new BigDecimal(0);
			compras = pvo.getCompraCash().add(pvo.getCompraTom().add(pvo.getCompraSpot().add(pvo.getCompraVFut().add(pvo.getCompra72Hr())))); 
			ventas = pvo.getVentaCash().add(pvo.getVentaTom().add(pvo.getVentaSpot().add(pvo.getVentaVFut().add(pvo.getVenta72Hr()))));
	    	_logger.info("validando la divisa: " + pvo.getIdDivisa() + " Compras: "+ compras +" Ventas: " + ventas);
			if (compras.compareTo(ventas)!= 0) {
				if(pvo.getIdDivisa().equals(Divisa.DOLAR)||pvo.getIdDivisa().equals(Divisa.DOLAR_CANADIENSE)||pvo.getIdDivisa().equals(Divisa.EURO)||pvo.getIdDivisa().equals(Divisa.LIBRA_ESTERLINA)||pvo.getIdDivisa().equals(Divisa.FRANCO_SUIZO)||pvo.getIdDivisa().equals(Divisa.YEN))
				throw new SicaException("No se puede dar de Alta el Tipo de Cambio Vespertino, " +
	                    "si no se encuentra la posici\u00f3n en cero");
				
			}
		}
    	
    	
		return true;
	}
	/**
     * Genera los tipos de cambio para el cierre de operaciones del Teller.
     *
     */
    public void generarTcMinMaxTellerCierre() {
    	try {
    		String producto = Constantes.EFECTIVO;
    		String idDivisa = Divisa.DOLAR;
    		List tiposPizarron = getSicaServiceData().findAll(TipoPizarron.class);
    		List canalesSucursales = getSicaServiceData().findCanalSucursales();
    		List tcSucursales = new ArrayList();
    		debug(":::CANALES " + canalesSucursales.size());
    		for (Iterator it = tiposPizarron.iterator(); it.hasNext(); ) {
    			TipoPizarron pizarron = (TipoPizarron) it.next();
    			for (Iterator itt = canalesSucursales.iterator(); itt.hasNext();) {
                    Canal canal = (Canal) itt.next();
                    if (canal.getTipoPizarron().getIdTipoPizarron().intValue() ==
                            pizarron.getIdTipoPizarron().intValue()) {
                        Map map = getPizarronServiceData().obtenerTipoDeCambioPorDivisa(canal,
                                idDivisa, producto);
                        map.put("idCanal", canal.getIdCanal());
                        tcSucursales.add(map);
                    }
                }
    		}
    		for (Iterator it = tcSucursales.iterator(); it.hasNext(); ) {
    			Map tc = (HashMap) it.next();
    			TcMinMaxTeller tcApertura = new TcMinMaxTeller(
    					((Double) tc.get("minimoCompra")).doubleValue(),
    					((Double) tc.get("maximoCompra")).doubleValue(),
    					((Double) tc.get("minimoVenta")).doubleValue(),
    					((Double) tc.get("maximoVenta")).doubleValue(),
    					(String) tc.get("idCanal"),
    					true);
    			getSicaServiceData().store(tcApertura);
    		}
    	}
    	catch (Exception e) {
    		warn(e);
            getDelegate().record("No fue posible guardar los Tipos de Cambio " +
            		"para las sucursales favor de notificar al area de Sistemas.", null);
		}
    }

    /**
     * Solicita la cancelaci&oacute;n del deal seleccionado. El primer elemento del arreglo de
     * 'serviceParameters' debe ser el n&uacute;mero de deal a cancelar.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cancelar(IRequestCycle cycle) {
        try {
            int idDeal = ((Integer) cycle.getServiceParameters()[0]).intValue();
            Deal deal = getSicaServiceData().findDeal(idDeal);
            refrescarEstadoActual();
            if (Estado.ESTADO_OPERACION_RESTRINGIDA != getEstadoActual().getIdEstado()) {
                throw new SicaException("S\u00f3lo se pueden cancelar deals durante el horario " +
                        "de operaci\u00f3n restringida.");
            }
            if (!deal.isCancelable()) {
                throw new SicaException("El deal  " + idDeal + " no es cancelable.");
            }
            if (deal.isDeSucursal()) {
                throw new SicaException("El deal   " + idDeal + " no es de sucursales.");
            }
            if (!Deal.STATUS_DEAL_PROCESO_CAPTURA.equals(deal.getStatusDeal())) {
                throw new SicaException("El deal " + idDeal + " no tiene status 'ALTA'.");
            }
            Date fechaHoy = DateUtils.inicioDia();
            if (!fechaHoy.equals(DateUtils.inicioDia(deal.getFechaCaptura()))) {
                throw new SicaException("s\u00f3lo se pueden cancelar deals con fecha de captura " +
                        "correspondiente a hoy");
            }
            if (deal.getSwap() != null) {
                throw new SicaException("est\u00e1 ligado a un Swap. Utilice la pantalla de " +
                        "consulta de swaps.");
            }
            if (deal.tieneAutPendientesHorario()) {
                throw new SicaException("tiene una solicitud de autorizaci\u00f3n por horario" +
                        " pendiente, llame a la mesa para que no lo autoricen.");

            }
            if (deal.tieneAutPendientesMonto()) {
                throw new SicaException("tiene una solicitud de autorizaci\u00f3n por monto " +
                        "pendiente, llame a la mesa para que no lo autoricen.");
            }
            getWorkFlowServiceData().wfSolicitarCancelacionDeal(getTicket(), deal.getIdDeal(),
                    ((Visit) getVisit()).getUser());
            for (Iterator it = getDeals().iterator(); it.hasNext();) {
                Deal d = (Deal) it.next();
                if (d.getIdDeal() == deal.getIdDeal()) {
                    d.setSeleccionado(true);
                }
            }
            // Pareceria que esto no es necesario, pero asi trabaja Tapestry:
            setDealsPendientesDocumentacion(getDealsPendientesDocumentacion());
            setDealsPendientesPlantilla(getDealsPendientesPlantilla());
            setDealsPendientesHorario(getDealsPendientesHorario());
            setDealsPendientesLinTomaEnFirme(getDealsPendientesLinTomaEnFirme());
            setDealsPendientesMonto(getDealsPendientesMonto());
            setDealsPendientesSolicitudSobrePrecio(getDealsPendientesSolicitudSobrePrecio());
            setDealsPendientesNoBalanceado(getDealsPendientesNoBalanceado());
            setDealsPendientesModCan(getDealsPendientesModCan());
            setDealsDetallesPendientes(getDealsDetallesPendientes());
            setDealsPendientesCompletar(getDealsPendientesCompletar());
            setDealsPendientesContratoSica(getDealsPendientesContratoSica());
            getDelegate().record("Se solicit\u00f3 la cancelaci\u00f3n del deal " + idDeal, null);
            CancelacionMailSender mailSender = (CancelacionMailSender) getApplicationContext().
                    getBean("cancelacionMailSender");
            mailSender.enviarMailPromotor(deal);
        }
        catch (SicaException e) {
            debug(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }
    }

    /**
     * Regresa la lista de los deals que aparecen en alguno de los portlets.
     *
     * @return List de Deal.
     */
    private List getDeals() {
        List todos = new ArrayList();
        todos.addAll(getDealsPendientesDocumentacion());
        todos.addAll(getDealsPendientesPlantilla());
        todos.addAll(getDealsPendientesHorario());
        todos.addAll(getDealsPendientesLinTomaEnFirme());
        todos.addAll(getDealsPendientesMonto());
        todos.addAll(getDealsPendientesSolicitudSobrePrecio());
        todos.addAll(getDealsPendientesNoBalanceado());
        todos.addAll(getDealsPendientesModCan());
        todos.addAll(getDealsDetallesPendientes());
        todos.addAll(getDealsPendientesCompletar());
        todos.addAll(getDealsPendientesContratoSica());
        return todos;
    }

    /**
     * Regresa el arreglo con los estados de operaci&oacute;n normal y operaci&oacute;n restringida.
     *
     * @return int[] Un arreglo de Estados v&aacute;lidos.
     */
    protected int[] getEstadosValidos() {
        return new int[]{Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA,
                Estado.ESTADO_OPERACION_VESPERTINA, Estado.ESTADO_FIN_LIQUIDACIONES,
                Estado.ESTADO_GENERACION_CONTABLE, Estado.ESTADO_OPERACION_NOCTURNA};
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
     * Regresa el valor de parametro.
     *
     * @return <code>ParametroSica</code> El parametro que se consulta.
     */
    public abstract ParametroSica getParametro();

    /**
     * Fija el valor de parametro.
     *
     * @param parametro El parametro que se consulta.
     */
    public abstract void setParametro(ParametroSica parametro);

    /**
     * Regresa el modo de ejecuci&oacute;n del bot&oacute;n
     *
     * @return int El modo de ejecuci&oacute;n
     */
    public abstract int getModoRefresh();

    /**
     * Fija el modo de ejecuci&oacute;n del bot&oacute;n
     *
     * @param modoRefresh El valor a asignar.
     */
    public abstract void setModoRefresh(int modoRefresh);

    /**
     * Regresa el valor de precioReferencia.
     *
     * @return <code>PrecioReferencia</code> El precio referencia que se consulta.
     */
    public abstract PrecioReferenciaActual getPrecioReferencia();

    /**
     * Fija el valor de precioReferencia.
     *
     * @param precioReferencia El precio de referencia a asignar.
     */
    public abstract void setPrecioReferencia(PrecioReferenciaActual precioReferencia);

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
     * Regresa el valor de reversosPendientes
     *
     * @return List.
     */
    public abstract List getReversosPendientes();

    /**
     * Fija el valor de reversosPendientes
     *
     * @param reversosPendientes El valor a asignar.
     */
    public abstract void setReversosPendientes(List reversosPendientes);


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
     * Almacena los Deals que al cierre de la Mesa carecen de <code>ContratoSica</code>.
     *
     * @param dealsPendientesContratoSica lista de deals que carecen de <code>ContratoSica</code>.
     */
    public abstract void setDealsPendientesContratoSica(List dealsPendientesContratoSica);

    /**
     * Regresa la lista con Deals que tiene solicitud por Sobreprecio pendiente.
     *
     * @return la lista de Deals pendientes por sobreprecio
     */
    public abstract List getDealsPendientesSolicitudSobrePrecio();

    /**
     * Almacena los Deals que al cierre de la Mesa tiene solicitud pendiente por sobreprecio.
     *
     * @param dealsPendientesSolSobrePrecio lista de deals pendientes por sobreprecio
     */
    public abstract void setDealsPendientesSolicitudSobrePrecio(List dealsPendientesSolSobrePrecio);

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
     * Obtiene la lista de deals no Cancelados que descuadran la Posici&opacute;n.
     *
     * @return List
     */
    public abstract List getDealsNoCancelados();

    /**
     * Asigna el valor para la lista de deals no Cancelados que descuadran la posici&oacute;n.
     *
     * @param dealsNoCancelados El valor para la lista.
     */
    public abstract void setDealsNoCancelados(List dealsNoCancelados);

    /**
     * Asigna el valor para la lista de deals Cancelados que descuadran la posici&oacute;n.
     *
     * @return List
     */
    public abstract List getDealsCancelados();

    /**
     * Obtiene la lista de deals Cancelados que descuadran la Posici&opacute;n.
     *
     * @param dealsCancelados El valor para la lista.
     */
    public abstract void setDealsCancelados(List dealsCancelados);
}
