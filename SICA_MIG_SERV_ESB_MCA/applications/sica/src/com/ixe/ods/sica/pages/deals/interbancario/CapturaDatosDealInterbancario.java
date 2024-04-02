/*
 * $Id: CapturaDatosDealInterbancario.java,v 1.23.22.1.24.1.22.3 2018/04/18 23:24:29 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals.interbancario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.valid.IValidationDelegate;

import com.ixe.bean.bup.Expediente;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.SicaConsultaProductosPersonaService;
import com.ixe.ods.sica.SicaConsultaRelacionesCuentaService;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Broker;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.CanalMesa;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.FactorDivisaActual;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.model.Spread;
import com.ixe.ods.sica.pages.Mensaje;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.ContratoDireccionesService;
import com.ixe.ods.sica.services.LineaCreditoConstantes;
import com.ixe.ods.sica.services.LineaCreditoMensajes;
import com.ixe.ods.sica.services.ValorFuturoService;
import com.legosoft.tapestry.IActivate;

/**
 * <p>P&aacute;gina para capturar los datos iniciales de un deal interbancario. Se pregunta al
 * usuario por el tipo de operaci&oacute;n a realizar (compra o venta), el tipo de cambio pactado,
 * utilizando el applet del pizarr&oacute;n.
 * </p>
 * <p>Una vez confirmada la operaci&oacute;n, se redirige a la p&aacute;gina
 * <code>CapturaDealInterbancario</code> para continuar con la captura del deal.</p>
 *
 * @author Jean C. Favila, Javier Cordova (jcordova)
 * @version $Revision: 1.23.22.1.24.1.22.3 $ $Date: 2018/04/18 23:24:29 $
 */
public abstract class CapturaDatosDealInterbancario extends AbstractCapturaDealInterbancario
        implements IExternalPage {

    /**
     * Recibe la confirmaci&oacute;n o cancelaci&oacute;n de la operaci&oacute;n desde el applet. Si
     * el usuario acept&oacute;, llama a <code>submit()</code>; en caso contrario, pone el
     * modoMensaje en false.
     *
     * @param params El arreglo de par&aacute;metros. Si el &iacute;ndice cero tiene un cero,
     *      significa que el usuario confirm&oacute; la operaci&oacute;n.
     * @param cycle  El ciclo de la p&aacute;gina.
     */
    public void activateExternalPage(Object[] params, IRequestCycle cycle) {
        int respuesta = ((Integer) params[0]).intValue();
        if (respuesta == 0) {
        	setModoSubmit(1);
            submit(cycle);
        }
        else {
            setModoMensaje(false);
        }
    }

    /**
     * Regresa el arreglo con los estados de operaci&oacute;n normal y operaci&oacute;n restringida.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[] { Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA };
    }

    /**
     * Llama a la superclase, fija el modoMensaje en false y llama a crearModeloProductos().
     *
     * @see #crearModeloProductos().
     * @param cycle El IRequestCycle.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        setModoMensaje(false);
        setDivisa((Divisa) getDivisas().get(0));
        try {
        	
            crearModeloProductos();
            validarLineaCreditoActiva(getDeal().getCliente().getIdPersona());
        }
        catch (SicaException e) {
            getDelegate().record(e.getMessage(), null);
        }
    }

    /**
     * Asigna los valores iniciales del deal interbancario. Si se trata de un deal con cobertura en
     * otra divisa, inicializa el tipoCambioCob en cero.
     *
     * @param deal El deal a inicializar.
     */
    private void inicializarDeal(Deal deal) {
        if (isModoCobertura()) {
            setTipoCambioCob(0.0);
        }
        deal.setTipoDeal(Deal.TIPO_INTERBANCARIO);
        deal.setUsuario(((Visit) getVisit()).getUser());
        setDeal(deal);
        
        setModoMensaje(false);
    }

    /**
     * Se encarga de validar si existe una Linea de Credito Activa para el Cliente
     * @param idPersona
     * @return
     */
	private void validarLineaCreditoActiva(Integer idPersona) {
    	
    	setEstadoLineaCredito("");
		LineaCambio lineaCambio   = getLineaCreditoService().findLineaCreditoByIdPersona(idPersona);
		if(lineaCambio == null || !LineaCreditoConstantes.STATUS_ACTIVADA.equals(lineaCambio.getStatusLinea())){
			throw new SicaException(LineaCreditoMensajes.LINEA_CREDITO_NO_ACTIVA);
		}
		if(lineaCambio != null){
			String estadoLineaCredito = lineaCambio.getStatusLinea();
			setEstadoLineaCredito(estadoLineaCredito);
		}
	}
    
    
    /**
     * Regresa la lista de divisas aplicables al canal de la sesi&oacute;n. Si se encuentra en modo
     * de cobertura, filtra la divisa d&oacute;lar americano.
     *
     * @return List.
     */
    public List getDivisas() {
    	List divisas = new ArrayList();
        try {
            divisas = getSicaServiceData().findDivisasByCanal(((Visit) getVisit()).getIdCanal());
            if (isModoCobertura()) {
                for (Iterator it = divisas.iterator(); it.hasNext();) {
                    Divisa divisa = (Divisa) it.next();
                    if (Divisa.DOLAR.equals(divisa.getIdDivisa())) {
                        it.remove();
                    }
                }
            }
            List factoresDivisaNF = getSicaServiceData().findDivisasNoFrecuentesFactor();
            for (Iterator it = factoresDivisaNF.iterator(); it.hasNext();) {
                FactorDivisaActual fd = (FactorDivisaActual) it.next();
                divisas.add(fd.getFacDiv().getToDivisa());
            }
            List factoresDivisaMT = getSicaServiceData().findDivisasMetalesFactor();
            for (Iterator it = factoresDivisaMT.iterator(); it.hasNext();) {
                FactorDivisaActual fd = (FactorDivisaActual) it.next();
                divisas.add(fd.getFacDiv().getToDivisa());
            }
        }
        catch (SicaException e) {
            getDelegate().record(e.getMessage(), null);
        }
        return divisas;
    }

    /**
     * Crea una nueva instancia de Deal y le asigna la contraparte proporcionada. Si &eacute;ste es
     * una instituci&oacute;n financiera, asigna el contratoSica al Deal y el promotor
     * correspondiente.
     *
     * @param broker La contraparte a asignar.
     */
    public void crearDeal(Broker broker) {
        SicaServiceData sd = getSicaServiceData();
        Deal deal = new Deal();
        if (broker != null) {
            deal.setBroker(broker);
            if (Broker.INSTITUCION_FINANCIERA.equals(broker.getTipoBroker())) {
                if (broker.isPagoAnticipado()) {
                    deal.setPagoAnticipado(true);
                }
                ContratoSica cs = sd.findContratoSicaByIdPersona(broker.getId().getPersonaMoral().
                        getIdPersona());
                if (cs == null) {
                    throw new SicaException("La contraparte no tiene un contrato registrado.");
                }
                ContratoDireccionesService cds =
                        (com.ixe.ods.sica.services.ContratoDireccionesService)
                                getApplicationContext().getBean("contratoDireccionesService");
                try {
                    deal.setContratoSica(cs);
                    deal.setDirFactura(cds.getDireccionFiscalParaPersona(deal.getCliente().
                            getIdPersona().intValue()));
                    deal.setDireccion(deal.getDirFactura());
                }
                catch (Exception e) {
                    if (_logger.isDebugEnabled()) {
                        _logger.debug(e);
                    }
                }
                EmpleadoIxe promotor = sd.findPromotorByContratoSica(cs.getNoCuenta());
                if (promotor == null) {
                    throw new SicaException("El contrato " + cs.getNoCuenta() +
                            " no tiene un promotor asignado.");
                }
                deal.setPromotor(promotor);
                //Verificar Documentacion:
                try {
                    verificarDocumentacion(deal, cs);
                }
                catch (Exception e) {
                    debug(e.getMessage(), e);
                    Mensaje nextPage = (Mensaje) getRequestCycle().getPage("Mensaje");
                    nextPage.setMensaje("Hubo un error al intentar verificar La " +
                            "Documentaci&oacute;n del Cliente.");
                    nextPage.setTipo(Mensaje.TIPO_ERROR);
                    getRequestCycle().activate(nextPage);
                }
            }
        }
        terminarInicializacion(sd, deal);
    }

    /**
     * Revisa si hay documentaci&oacute;n pendiente de recibir para el contrato. En este caso,
     * marca el deal para autorizaci&oacute;n por falta de documentaci&oacute;n.
     *
     * @param deal El deal a revisar.
     * @param cs El contrato Sica de la contraparte.
     */
    private void verificarDocumentacion(Deal deal, ContratoSica cs) {
        List relacionesCuenta = ((SicaConsultaRelacionesCuentaService)
                getApplicationContext().getBean("sicaConsultaRelacionesCuentaService")).
                obtenRelacionesCtaDocumentacion(((SicaConsultaProductosPersonaService)
                        getApplicationContext().
                                getBean("sicaConsultaProductosPersonaService")).
                        obtenCuentaContrato(cs.getNoCuenta()));
        if (relacionesCuenta != null && relacionesCuenta.size() > 0) {
            for (Iterator i = relacionesCuenta.iterator(); i.hasNext();) {
                Map map = (HashMap) i.next();
                Expediente expediente = (Expediente) map.get("expediente");
                if (Constantes.EXP_DOC_STATUS.equals(expediente.getStatus())) {
                    deal.setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_INT_DOCUMENTACION);
                    break;
                }
            }
        }
//        else {
//            deal.setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_INT_DOCUMENTACION);
//        }
    }

    /**
     * Asigna el canal al deal e inicializa los tipos de cambio y monto en cero.
     *
     * @param sd El SicaServiceData.
     * @param deal El deal a inicializar.
     */
    private void terminarInicializacion(SicaServiceData sd, Deal deal) {
        inicializarDeal(deal);
        Canal canal = sd.findCanal(((Visit) getVisit()).getIdCanal());
        deal.setCanalMesa(new CanalMesa(canal, canal.getMesaCambio()));
        deal.setTipoValor(Constantes.CASH);
        if (isDivisaReferenciaPeso()) {
            setDivisa(sd.findDivisa(Divisa.DOLAR));
        }
        setMonto(0);
        setMontoNeteo(0);
        setTipoCambio(0);
        setTipoCambioNeteo(0);
        setTipoOperacion("");
        setLiquidacionEspecial(SIN_LIQUIDACION);
    }

    /**
     * Regresa el valor del tipo de cambio para cobertura.
     *
     * @return double
     */
    private double getTipoCambioParaCobertura() {
        double montoMxn = getMontoUsdParaComplCob() * getTipoCambioCob();
        return montoMxn / getMonto();
    }

    /**
     * Regresa monto el monto en d&oacute;lares multiplicando el monto por el tipoCambio.
     *
     * @return double.
     */
    private double getMontoUsdParaComplCob() {
        return getMonto() * (getDivisa().isDivide() ? 1 / getTipoCambio() : getTipoCambio());
    }

    /**
     * Regresa true si el porcentaje de desviaci&oacute;n entre tcCapturado y tcRef es mayor al 1%,
     * si es mayor al 5% levanta una SicaException.
     *
     * @param tcCapturado El tipo de cambio capturado por el usuario.
     * @param tcRef El tipo de cambio de referencia de la divisa.
     * @return boolean.
     */
    private boolean validarPorcentajeDesv(double tcCapturado, double tcRef) {
        SicaServiceData sd = getSicaServiceData();
        ParametroSica paramLimDesv = sd.findParametro(ParametroSica.PORC_DESV_INTERB_LIM);
        double limPorcDesv = Redondeador.redondear2Dec(Double.valueOf(paramLimDesv.getValor()).
                doubleValue());
        // Revisamos el
        double tcInferior = Redondeador.redondear6Dec(tcRef * (1 - limPorcDesv / 100.00));
        double tcSuperior = Redondeador.redondear6Dec(tcRef * (1 + limPorcDesv / 100.00));
        debug("1. " + tcCapturado + " " + getTipoCambio() + " " +
                getTipoCambioParaCobertura() + " " + tcInferior + " " + tcSuperior);

        if (tcCapturado < tcInferior || tcCapturado > tcSuperior) {
            throw new SicaException("El tipo de cambio debe estar entre " + tcInferior + " y " +
                    tcSuperior + ".");
        }
        // Ahora revisamos la desviacion con nivel de advertencia:
        paramLimDesv = sd.findParametro(ParametroSica.PORC_DESV_INTERB_WARN);
        setPorcentajeDesviacion(paramLimDesv.getValor());
        limPorcDesv = Redondeador.redondear2Dec(Double.valueOf(paramLimDesv.getValor()).
                doubleValue());
        tcInferior = Redondeador.redondear6Dec(tcRef * (1 - limPorcDesv / 100.00));
        tcSuperior = Redondeador.redondear6Dec(tcRef * (1 + limPorcDesv / 100.00));
        debug("2. " + tcCapturado + " " + getTipoCambio() + " " + getTipoCambioParaCobertura() +
                " " + tcInferior + " " + tcSuperior);
        return tcCapturado < tcInferior || tcCapturado > tcSuperior;
    }

    /**
     * En deals interbancarios con cobertura en otra divisa, regresa true si el porcentaje de
     * desviaci&oacute;n entre tcCapturado y tcRef es mayor al 1%, si es mayor al 5% levanta una
     * SicaException.
     *
     * @return boolean.
     */
    private boolean advertirSobreDesviacionCobertura() {
        boolean recibimos = getTipoOperacion().equals(OPERACION_COMPRA);
        double tcDolarPeso = getTipoCambioCob(); // 11
        double tcRefDolar = getPizarronServiceData().findPrecioPizarronPesos(
        		getSicaServiceData().findCanal("PMY").getTipoPizarron(),
                Divisa.DOLAR, getClaveFormaLiquidacion(), recibimos,
                getDeal().getTipoValor());
        // Se valida el tipo de cambio de dolar:
        if (validarPorcentajeDesv(tcDolarPeso, tcRefDolar)) {
            return true;
        }
        double tcDivDolar = getTipoCambio();  // 1.33
        Divisa div = getDivisa();
        if (div.isMetalAmonedado() || div.isNoFrecuente()) {
            SicaServiceData sd = getSicaServiceData();
            FactorDivisaActual fda = sd.findFactorDivisaActualFromTo(Divisa.PESO,
                    div.getIdDivisa());
            double factorVenta = fda.getFacDiv().getFactor();
            double factor = recibimos ?
                    factorVenta - fda.getFacDiv().getSpreadCompra() : factorVenta;
            double tcRefDivisa = Redondeador.redondear6Dec(factor) / tcRefDolar;
            return validarPorcentajeDesv(tcDivDolar, tcRefDivisa);
        }
        else {
            double tcRefDivisa = getPizarronServiceData().findPrecioPizarronPesos(
            		getSicaServiceData().findCanal("PMY").getTipoPizarron(),
                    getDivisa().getIdDivisa(), getClaveFormaLiquidacion(), recibimos,
                    getDeal().getTipoValor()) / tcRefDolar;
            if (getDivisa().isDivide()) {
                tcRefDivisa = 1 / tcRefDivisa;
            }
            return validarPorcentajeDesv(tcDivDolar, tcRefDivisa);
        }
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
        if (getDivisa().isDivide()) {
            tcRef = 1 / tcRef;
        }
        return tcRef;
    }

    /**
     * Levanta una excepci&oacute;n si el tipo de cambio se encuentra desviado m&aacute;s de un 5%
     * con respecto al precio de referencia de la divisa. Regresa true si el porcentaje de
     * desviaci&oacute;n es de 1%.
     *
     * @return boolean.
     */
    private boolean advertirSobreDesviacion() {
        setPorcentajeDesviacion("");
        if (isModoCobertura()) {
            return advertirSobreDesviacionCobertura();
        }
        boolean recibimos = getTipoOperacion().equals(OPERACION_COMPRA);
        SicaServiceData sd = getSicaServiceData();
        PrecioReferenciaActual pr = sd.findPrecioReferenciaActual();
        debug("Referencia cv: " + pr.getPreRef().getPrecioCompra() + " " +
                pr.getPreRef().getPrecioVenta());
        Divisa div = getDivisa();
        FactorDivisaActual fda;
        double tcRef;
        if (div.isMetalAmonedado() || div.isNoFrecuente()) {
            fda = sd.findFactorDivisaActualFromTo(Divisa.PESO, div.getIdDivisa());
            double factorVenta = fda.getFacDiv().getFactor();
            double factor = recibimos ?
                    factorVenta - fda.getFacDiv().getSpreadCompra() : factorVenta;
            tcRef = Redondeador.redondear6Dec(factor);
            debug("tcRef-> " + tcRef + " factorDivisa: " + factor);
        }
        else {
            tcRef = getPizarronServiceData().findPrecioPizarronPesos(
            		getSicaServiceData().findCanal("PMY").getTipoPizarron(),
                    getDivisa().getIdDivisa(), getClaveFormaLiquidacion(), recibimos,
                    getDeal().getTipoValor());
            debug("tcRef: " + tcRef);
        }
        // Se divide entre el precio de referencia si la div de referencia no son Pesos:
        if (!Divisa.PESO.equals(getDivisaReferencia().getIdDivisa())) {
            tcRef = invertirTipoCambio(tcRef, pr);
        }
        boolean validar = validarPorcentajeDesv(getTipoCambio(), tcRef);
        if (!isModoNeteo()) {
            return validar;
        }
        else {
            // Si deal de neteo, debe revisar la desviaci&oacute;n del detalle contrario:
            tcRef = getPizarronServiceData().findPrecioPizarronPesos(
            		getSicaServiceData().findCanal("PMY").getTipoPizarron(),
                    getDivisa().getIdDivisa(), getClaveFormaLiquidacion(), !recibimos,
                    getDeal().getTipoValor());
            debug("tcRefNeteo: " + tcRef);
            // Se divide entre el precio de referencia si la div de referencia no son Pesos:
            if (!Divisa.PESO.equals(getDivisaReferencia().getIdDivisa())) {
                tcRef = invertirTipoCambio(tcRef, pr);
            }
            return validarPorcentajeDesv(getTipoCambioNeteo(), tcRef);
        }
    }

    /**
     * Valida que los datos est&eacute;n completos y la l&iacute;nea de operaci&oacute;n (si la
     * contraparte es una instituci&oacute;n financiera). Si el usuario ha confirmado la
     * operaci&oacute;n, se crea(n) el(los) detalle(s) del deal y en de ser posible, se asignan las
     * plantillas a estos detalles. Por &uacute;ltimo, el deal interbancario es insertado en la base
     * de datos.
     *
     * @param cycle El ciclo de la p&aacute;.
     */
    public void submit(IRequestCycle cycle) {
        IValidationDelegate delegate = getDelegate();
        Visit visit = (Visit) getVisit();
        
        try {
            if (getModoSubmit() != 1) {
                crearModeloProductos();
                return;
            }
            if (!isModoMensaje()) {
                if (delegate.getHasErrors()) {
                    return;
                }
                if (getMonto() <= 0.0) {
                    throw new SicaException("El monto debe ser mayor a cero.");
                }
                if (isModoNeteo() && getMontoNeteo() <= 0.0) {
                    throw new SicaException("El monto para el complemento del neteo debe ser " +
                            "mayor a cero.");
                }
                if (getTipoOperacion().trim().length() < 1) {
                    throw new SicaException("Por favor proporcione el tipo de operaci\u00f3n.");
                }
                if (!Divisa.DOLAR.equals(getDivisa().getIdDivisa()) &&
                        Constantes.MEXDOLAR.equals(getClaveFormaLiquidacion())) {
                    throw new SicaException("Para utilizar el producto MEXDOL, la divisa debe " +
                            "ser USD.");
                }
                if (Divisa.DOLAR.equals(getDivisa().getIdDivisa()) && 
                		Constantes.DOCUMENTO.equals(getClaveFormaLiquidacion()) && 
                		getTipoOperacion().equals(OPERACION_VENTA)) {
                	throw new SicaException("El producto DOCEXT USD no se encuentra disponible.");
                }
                if (getDivisa().isNoFrecuente()) {
                    if (getTipoOperacion().equals(OPERACION_COMPRA) &&
                            getClaveFormaLiquidacion().equals(Constantes.DOCUMENTO)) {
                        throw new SicaException("No se puede realizar Compra de Documentos " +
                                "Extranjeros para Divisas No Frecuentes.");
                    }
                }
                setAdvertirDesviacion(advertirSobreDesviacion());
                boolean hayValorFuturo = getPizarronServiceData().isValorFuturoHabilitado();
                Deal deal = getDeal();
                // Si es valor futuro, solo puede operarse transferencia en dolares para la ultima
                // fecha valor.
                if (!(Constantes.TRANSFERENCIA.equals(getClaveFormaLiquidacion()) &&
                        Divisa.DOLAR.equals(getDivisa().getIdDivisa())) &&
                        ((hayValorFuturo && Constantes.VFUT.equals(deal.getTipoValor())) ||
                                (! hayValorFuturo &&
                                        Constantes.HR72.equals(deal.getTipoValor())))) {
                    throw new SicaException("Cuando se opera la \u00faltima fecha valor, " +
                            "s\u00f3lo se pueden hacer transferencias para d\u00f3lares " +
                            "americanos.");
                }
                // Revisamos la opci&oacute;n de valor futuro:
                ValorFuturoService vfs = (ValorFuturoService) getApplicationContext().
                        getBean("valorFuturoService");
                vfs.validarSectorEconomicoPorFechaValor(getDeal().getTipoValor(), getDeal(),
                        hayValorFuturo);
                setModoMensaje(true);
            }
            else {
                continuarOperacion(getDeal(), visit.getTicket());
            }
        }
        catch (SicaException e) {
            debug(e.getMessage(), e);
            delegate.record(e.getMessage(), null);
        }
    }

    /**
     * Genera el deal interbancario, incluyendo sus detalles y lo guarda en la base de datos.
     *
     * @param deal El deal a crear.
     * @param string 
     */
    private void continuarOperacion(Deal deal, String ticket) {
        SicaServiceData sd = getSicaServiceData();
        setModoMensaje(false);
        deal = sd.crearDealInterbancario(deal, OPERACION_COMPRA.equals(getTipoOperacion()),
                deal.getTipoValor().trim().equals(Constantes.SPOT) ? getFechaSPOT() :
                        deal.getTipoValor().trim().equals(Constantes.TOM) ?
                                getFechaTOM() :
                                deal.getTipoValor().trim().equals(Constantes.CASH) ?
                                        getFechaOperacion() :
                                        deal.getTipoValor().trim().equals(Constantes.HR72) ?
                                                getFecha72HR() : getFechaVFUT(),
                isModoCobertura() ? getTipoCambioParaCobertura() : getTipoCambio(),
                getClaveFormaLiquidacion(), getMonto(),
                getDivisa(), getDeal().getCanalMesa().getMesaCambio(),
                ((Visit) getVisit()).getUser(), isContraparteSwap(), isModoCobertura(),
                getMontoUsdParaComplCob(), getTipoCambioCob(), isModoNeteo(),
                isModoNeteo() ? getMontoNeteo() : 0, isModoNeteo() ? getTipoCambioNeteo() : 0,
                getClaveFormaLiquidacionNeteo(),
                ticket);
        CapturaDealInterbancario nextPage = (CapturaDealInterbancario) getRequestCycle().
                getPage("CapturaDealInterbancario");
        nextPage.setModoInsercion(true);
        if (LIQUIDACION_VISA.equals(getLiquidacionEspecial())) {
            deal.setLiquidacionEspecial("V-" + getDivisa().getIdDivisa());
            deal.setObservaciones(LIQUIDACION_VISA + "-" + getDivisa().getIdDivisa() +
                    ". " + deal.getObservaciones());
        }
        else if (LIQUIDACION_TESORERIA.equals(getLiquidacionEspecial())) {
            deal.setLiquidacionEspecial("T-" + getDivisa().getIdDivisa());
            deal.setObservaciones(LIQUIDACION_TESORERIA + "-" + getDivisa().getIdDivisa() +
                    ". " + deal.getObservaciones());
        }
        if (isModoCobertura()) {
            deal.setTipoCambioCobUsdDiv(new BigDecimal("" + getTipoCambio()));
            deal.setTipoCambioCobMxnUsd(new BigDecimal("" + getTipoCambioCob()));
            sd.update(deal);
        }
        nextPage.setEstadoLineaCredito(getEstadoLineaCredito());
        nextPage.setDeal(deal);
        nextPage.activate(getRequestCycle());
    }

    /**
     * Si est&aacute; en modoMensaje, cambia el modoMensaje a false; en caso contrario, activa la
     * p&aacute;gina de 'SeleccionBroker'.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void regresar(IRequestCycle cycle) {
        if (isModoMensaje()) {
            setModoMensaje(false);
        }
        ((IActivate) cycle.getPage("SeleccionBroker")).activate(cycle);
    }

    /**
     * Crea el mapa para pedir la confirmaci&oacute;n de la operaci&oacute;n utilizando el applet,
     * v&iacute;a javascript. El mapa contiene las entradas: deal, tipoOperacion, montoLetra y
     * tipoCambio.
     *
     * @return Map.
     */
    public Map getMapaConfirmacion() {
        Map map = new HashMap();
        map.put("porcDesv", getPorcentajeDesviacion());
        map.put("adv", Boolean.valueOf(isAdvertirDesviacion()));
        map.put("rec", Boolean.valueOf(OPERACION_COMPRA.equals(this.getTipoOperacion())));
        map.put("tv", getDeal().getTipoValor());
        map.put("cant", new Double(getMonto()));
        if (isModoCobertura()) {
            Double tipoCambioParaCobertura = new Double(getTipoCambioParaCobertura());
            map.put("tcm", tipoCambioParaCobertura);
            map.put("tcc", tipoCambioParaCobertura);
        }
        else {
            map.put("tcm", new Double(getTipoCambio()));
            map.put("tcc", new Double(getTipoCambio()));
        }
        map.put("claveFormaLiquidacion", getClaveFormaLiquidacion());
        map.put("idDiv", getDivisa().getIdDivisa());
        map.put("sdec", Boolean.TRUE);
        return map;
    }

    /**
     * Regresa el mapa con el valor booleano de 'invertir'.
     *
     * @return Map
     */
    public Map getMapaCobertura() {
        Map mapa = new HashMap();
        mapa.put("invertir", Boolean.valueOf(getDivisa().isDivide()));
        return mapa;
    }

    /**
     * Regresa true so el deal es swap, false en otro caso.
     *
     * @return boolean
     */
    public boolean isContraparteSwap() {
        return getDeal().getSwap() != null;
    }

    /**
     * Modelo que contiene las fechas valor.
     *
     * @return IPropertySelectionModel
     */
    public IPropertySelectionModel getFechasValorModel() {
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
     * Crea el Modelo a mostrar en el Combo de Productos.
     */
    private void crearModeloProductos() {
    	Canal canal = getSicaServiceData().findCanal(((Visit) getVisit()).getIdCanal());
        Divisa divisa = getDivisa();
        if (divisa.isFrecuente()) {
            List spreadsActuales = getSicaServiceData().
            		findSpreadsActualesByTipoPizarronDivisa(canal.getTipoPizarron(),
            		getDivisa().getIdDivisa());
            String[] productos = new String[spreadsActuales.size()];
            int i = 0;
            for (Iterator it = spreadsActuales.iterator(); it.hasNext(); i++) {
                Spread spread = (Spread) it.next();
                productos[i] = spread.getClaveFormaLiquidacion();
            }
            setProductos(productos);
        }
        else if (divisa.isMetalAmonedado()) {
            setProductos(new String[] { "METALS" });
        }
        else {
            setProductos(new String[] { Constantes.TRANSFERENCIA, Constantes.DOCUMENTO, Constantes.EFECTIVO });
        }
    }

    /**
     * Regresa el Modelo del Combo de Productos.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getProductosModel() {
		return new StringPropertySelectionModel(getProductos());
    }

	/**
     * Regresa el Modelo del Combo de Liquidaciones Especiales.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getLiquidacionesEspecialesModel() {
		if (getDivisa().isFrecuente() && !isModoCobertura()) {
            return new StringPropertySelectionModel(new String[]{
                    SIN_LIQUIDACION, LIQUIDACION_VISA, LIQUIDACION_TESORERIA});
        }
        return new StringPropertySelectionModel(new String[]{SIN_LIQUIDACION});
    }

    /**
     * Regresa siempre false.
     *
     * @return boolean.
     */
    public boolean isModoInsercion() {
        return false;
    }

    /**
     * No hace nada.
     *
     * @param modoInsercion ignorado.
     */
    public void setModoInsercion(boolean modoInsercion) {
    }

    /**
     * Regresa el valor de claveFormaLiquidacion.
     *
     * @return String.
     */
    public abstract String getClaveFormaLiquidacion();

    /**
     * Fija el valor de claveFormaLiquidacion.
     *
     * @param claveFormaLiquidacion El valor a asignar.
     */
    public abstract void setClaveFormaLiquidacion(String claveFormaLiquidacion);

    /**
     * Regresa el valor de claveFormaLiquidacionNeteo.
     *
     * @return String.
     */
    public abstract String getClaveFormaLiquidacionNeteo();

    /**
     * Regresa el valor de divisa.
     *
     * @return Divisa.
     */
    public abstract Divisa getDivisa();

    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public abstract void setDivisa(Divisa divisa);

    /**
     * Regresa el valor de modoCobertura.
     *
     * @return boolean.
     */
    public abstract boolean isModoCobertura();

    /**
     * Establece el valor de modoCobertura.
     *
     * @param modoCobertura El valor a asignar.
     */
    public abstract void setModoCobertura(boolean modoCobertura);

    /**
     * Regresa el valor de modoNeteo.
     *
     * @return boolean.
     */
    public abstract boolean isModoNeteo();

    /**
     * Establece el valor de modoNeteo.
     *
     * @param modoNeteo El valor a asignar.
     */
    public abstract void setModoNeteo(boolean modoNeteo);

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

    /**
     * Regresa el valor de monto.
     *
     * @return double.
     */
    public abstract double getMonto();

    /**
     * Fija el valor de monto.
     *
     * @param monto El valor a asignar.
     */
    public abstract void setMonto(double monto);

    /**
     * Regresa el valor de montoNeteo.
     *
     * @return double.
     */
    public abstract double getMontoNeteo();

    /**
     * Establece el valor de montoNeteo.
     *
     * @param montoNeteo El valor a asignar.
     */
    public abstract void setMontoNeteo(double montoNeteo);

    /**
     * Regresa el valor tipoCambio.
     *
     * @return double.
     */
    public abstract double getTipoCambio();

    /**
     * Fija el valor de tipoCambio.
     *
     * @param tipoCambio El valor a asignar.
     */
    public abstract void setTipoCambio(double tipoCambio);

    /**
     * Regresa el valor de tipoCambioCob.
     *
     * @return double.
     */
    public abstract double getTipoCambioCob();

    /**
     * Establece el valor de tipoCambioCob.
     *
     * @param tipoCambioCob El valor a asignar.
     */
    public abstract void setTipoCambioCob(double tipoCambioCob);

    /**
     * Regresa el valor de tipoCambioNeteo.
     *
     * @return double.
     */
    public abstract double getTipoCambioNeteo();

    /**
     * Establece el valor de tipoCambioNeteo.
     *
     * @param tipoCambioNeteo El valor a asignar.
     */
    public abstract void setTipoCambioNeteo(double tipoCambioNeteo);

    /**
     * Regresa el valor de tipoOperacion.
     *
     * @return String.
     */
    public abstract String getTipoOperacion();

    /**
     * Fija el valor de tipoOperacion.
     *
     * @param tipoOperacion El valor a asignar.
     */
    public abstract void setTipoOperacion(String tipoOperacion);

    /**
	 * Regresa el modo de submit de la pagina. Si es por boton o por combo.
	 * @return int
	 */
	public abstract int getModoSubmit();

    /**
     * Fija el modo de Sumbit de la pagina. Ya sea por boton o por combo.
     * @param modoSubmit El valor a asignar.
     */
	public abstract void setModoSubmit(int modoSubmit);

	/**
     * Regresa el valor de productos.
     *
     * @return String[].
     */
    public abstract String[] getProductos();

    /**
     * Regresa el valor de liquidacionEspecial.
     *
     * @return String.
     */
    public abstract String getLiquidacionEspecial();

    /**
     * Fija el valor de liquidacionEspecial.
     *
     * @param liquidacionEspecial El valor a asignar.
     */
    public abstract void setLiquidacionEspecial(String liquidacionEspecial);

    /**
     * Fija el valor de productos.
     *
     * @param productos El valor a asignar.
     */
    public abstract void setProductos(String[] productos);

    /**
     * Regresa el valor de advertirDesviacion.
     *
     * @return boolean.
     */
    public abstract boolean isAdvertirDesviacion();

    /**
     * Establece el valor de advertirDesvia.
     *
     * @param advertirDesviacion El valor a asignar.
     */
    public abstract void setAdvertirDesviacion(boolean advertirDesviacion);

    /**
     * Regresa el valor de porcentajeDesviacion.
     *
     * @return String.
     */
    public abstract String getPorcentajeDesviacion();

    /**
     * Establece el valor de porcentajeDesviacion.
     *
     * @param porcDesv El valor a asignar.
     */
    public abstract void setPorcentajeDesviacion(String porcDesv);

    /**
     * Establece el valor del Estado de la Linea de Credito asociada al Cliente
     * @param estadoLineaCredito Estado de la Linea de credito 
     */
	public abstract  void setEstadoLineaCredito(String estadoLineaCredito);
	
	/**
	 * Obtiene el Estado de la Linea de credito asociada al Cliente
	 * @return String Estado de la Linea de Credito
	 */
	public abstract String getEstadoLineaCredito();
    
    
    /**
     * Constante para el tipo de operaci&oacute;n de compra.
     */
    public static final String OPERACION_COMPRA = "Compra";

    /**
     * Constante para el tipo de operaci&oacute;n de venta.
     */
    public static final String OPERACION_VENTA = "Venta";

    /**
     * Constante para Deal sin Liquidaci&oacute;n Especial.
     */
    public static final String SIN_LIQUIDACION = "NO APLICA";

    /**
     * Constante para la Liquidaci&oacute;n Especial VISA.
     */
    public static final String LIQUIDACION_VISA = "VISA";

    /**
     * Constante para la Liquidaci&oacute;n Especial TESORERIA.
     */
    public static final String LIQUIDACION_TESORERIA = "TESORERIA";
}
