/*
 * $Id: CapturaDealInterbancario.java,v 1.35.2.1.60.1 2016/07/13 21:45:26 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals.interbancario;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.ValidationConstraint;
import org.springframework.remoting.RemoteAccessException;

import com.ixe.bean.bup.Expediente;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaConsultaProductosPersonaService;
import com.ixe.ods.sica.SicaConsultaRelacionesCuentaService;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaExceptionHelper;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.components.EncabezadoDeal;
import com.ixe.ods.sica.model.Broker;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.IPlantilla;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.Swap;
import com.ixe.ods.sica.pages.Mensaje;
import com.ixe.ods.sica.pages.deals.CapturaDatosEnvio;
import com.ixe.ods.sica.sdo.SicaServiceData;

/**
 * <p>P&aacute;gina que muestra un deal interbancario. El usuario puede definir el contrato sica si
 * es que el deal no tiene asignado uno.</p>
 *
 * <p>Una vez completo el deal el usuario puede instanciar el workflow presionando el bot&oacute;n
 * 'Procesar'. </p>
 *
 * <p>Tambi&eacute;n se provee la funcionalidad de cancelaci&oacute;n del deal interbancario,
 * siempre y cuando ninguno de los detalles haya sido liquidado y tesorer&iacute;a d&eacute; su
 * autorizaci&oacute;n.</p>
 *
 * @author Jean C. Favila, Javier Cordova (jcordova)
 * @version $Revision: 1.35.2.1.60.1 $ $Date: 2016/07/13 21:45:26 $
 */
public abstract class CapturaDealInterbancario extends AbstractCapturaDealInterbancario
        implements IExternalPage {

	/**
     * M&eacut;etodo que coordina todas las posibles opciones de operaci&oacute;n, como crear un
     * deal, insertar detalles, dividir en dos un detalle y editar el deal.
     *
     * @param params Los par&aacute;metros del Request Cycle.
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activateExternalPage(Object[] params, IRequestCycle cycle) {    	    	
        super.activate(cycle);
        int opc = ((Integer) params[0]).intValue();
        setMensaje(null);
        switch (opc) {
            case OPCION_MODIFICAR_DETALLE:
                limpiarFormaLiquidacion(((Integer) cycle.getServiceParameters()[1]).intValue(),
                        cycle.getServiceParameters()[2].toString().trim());
                break;
            case OPCION_SPLIT_DETALLE:
                int idDealPosicion = ((Integer) cycle.getServiceParameters()[1]).intValue();
                DealDetalle det = findDetalle(idDealPosicion);
                if (isHorarioVespertino() && !det.isPesos()) {
                    throw new SicaException("No es posible hacer Split para un detalle de " +
                            "deal en Horario Vespertino.");
                }
                else {
                    double monto = ((Double) cycle.getServiceParameters()[2]).doubleValue();
                    String mnemonico = cycle.getServiceParameters()[PARAM_SPLIT_MNEMONICO].
                            toString().trim();
                    hacerSplit(idDealPosicion, monto, mnemonico);
                }                
                break;
            default:
                break;
        }
    }
        
    /**
     * Regresa el arreglo con los estados de operaci&oacute;n normal y operaci&oacute;n restringida.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[] { Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA,
        		Estado.ESTADO_OPERACION_VESPERTINA};
    }

    /**
     * Listener de la forma. No hace nada. Cada bot&oacute;n de submit de la forma tiene asignado un
     * listener propio.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void submit(IRequestCycle cycle) {
        try {
            if (MODO_PAGO_ANTICIPADO.equals(getModoSubmit())) {
                getWorkFlowServiceData().marcarPagAntTomaEnFirme(getTicket(), getDeal(),
                        getDeal().getUsuario(), true);
            }
            else if (EncabezadoDeal.SUBMIT_MODE_MENSAJERIA.equals(getModoSubmit())) {
                if (getDeal().isMensajeria()) {
                    capturarDatosEnvio(cycle, true);
                    return;
                }
            }
            getWorkFlowServiceData().actualizarDatosDeal(getTicket(), getDeal());
        }
        catch (SicaException e) {
            debug(e);
            getDelegate().record(e.getMessage(), null);
        }
    }

    /**
     * Invoca el servicio de balanceo de pesos.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void balancearPesos(IRequestCycle cycle) {
        getSicaServiceData().balancearDealInterbancario(getDeal());
    }

    /**
     * <p>Activa la p&aacute;gina <code>SeleccionBroker</code> para que el usuario pueda escoger un
     * broker que es una
     * instituci&oacute;n financiera y asignarlo como cliente definitivo del deal interbancario.</p>
     * <p>Selecci&oacute;nBroker llamar&aacute; al mismo sobrecargado para asignar el contrato al
     * deal</p>
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see SeleccionBroker#inicializar(boolean).
     * @see #definirContraparte(com.ixe.ods.sica.model.Broker).
     */
    public void definirContraparte(IRequestCycle cycle) {
        SeleccionBroker nextPage = (SeleccionBroker) cycle.getPage("SeleccionBroker");
        nextPage.inicializar(true);
        cycle.activate(nextPage);
    }

    /**
     * Asigna el broker definitivo al deal interbancario, junto con su contrato sica. Encuentra las
     * plantillas del contrato y las asigna a los detalles.
     *
     * @param broker El broker a asignar.
     */
    public void definirContraparte(Broker broker) {
        SicaServiceData ssd = getSicaServiceData();
        Deal deal = ssd.findDeal(getDeal().getIdDeal());
        ContratoSica cs = ssd.findContratoSicaByIdPersona(broker.getId().getPersonaMoral().
                getIdPersona());
        if (deal.isCompra()) {
            IPlantilla p = getPlantillaConTipoParaContrato(isDivisaReferenciaPeso() ?
                    "TranNacionales" : "Intls",
                    cs.getNoCuenta(), getDivisaReferencia().getIdDivisa());
            ssd.insertarEntregamosDealIntConPlantilla(deal, deal.getTotalRecibimos(),
                    p, false, 1.0);
        }
        else {
            // Encuentro el detalle de entregamos y asigno el mnemonico de su plantilla:
            DealDetalle det = (DealDetalle) deal.getEntregamos().get(0);
            IPlantilla p = getPlantillaConTipoParaContrato("Intls", cs.getNoCuenta(),
                    det.getDivisa().getIdDivisa());
            if (p == null) {
                getDelegate().record("El broker no tiene plantilla de transferencia extranjera " +
                        "registrada o activa.", null);
                return;
            }
            det.setMnemonico(p.getMnemonico());
            det.setPlantilla(p);
            if (! Plantilla.STATUS_PLANTILLA_ACTIVA.equals(p.getStatusPlantilla())) {
                deal.setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_INT_DOCUMENTACION);
                det.setEvento(Deal.EV_SOLICITUD, DealDetalle.EV_IND_INT_DOCUMENTACION);
            }
        }
        deal.setBroker(broker);
        com.ixe.ods.sica.services.ContratoDireccionesService cds =
                (com.ixe.ods.sica.services.ContratoDireccionesService) getApplicationContext().
                getBean("contratoDireccionesService");
        try {
            deal.setContratoSica(cs);
            deal.setDirFactura(cds.getDireccionFiscalParaPersona(deal.getCliente().getIdPersona().
                    intValue()));
            deal.setDireccion(deal.getDirFactura());
        }
        catch (Exception e) {
            debug(e);
        }
        EmpleadoIxe promotor = ssd.findPromotorByContratoSica(cs.getNoCuenta());
        if (promotor == null) {
            getDelegate().record("El contrato n\u00fam " + cs.getNoCuenta() +
                    " no tiene un promotor asignado. ", ValidationConstraint.REQUIRED);
            return;
        }
        deal.setPromotor(promotor);
        // Verifica, si el deal es sujeto a pago anticipado por default de acuerdo a las propiedades
        // del broker.
        if (Broker.INSTITUCION_FINANCIERA.equals(broker.getTipoBroker())) {
			deal.setPagoAnticipado(broker.isPagoAnticipado());
            if (deal.getIdDeal() > 0) {
                getSicaServiceData().update(deal);
            }
        }
        // Verificar Documentacion:
        try {
            List relacionesCuenta = ((SicaConsultaRelacionesCuentaService) getApplicationContext().
                    getBean("sicaConsultaRelacionesCuentaService")).
                    obtenRelacionesCtaDocumentacion(
                            ((SicaConsultaProductosPersonaService) getApplicationContext().
                            getBean("sicaConsultaProductosPersonaService")).
                                    obtenCuentaContrato(cs.getNoCuenta()));
            if (relacionesCuenta != null && relacionesCuenta.size() > 0) {
                for (Iterator i = relacionesCuenta.iterator(); i.hasNext(); ) {
                    Map map = (HashMap) i.next();
                    Expediente expediente = (Expediente) map.get("expediente");
                    if (Constantes.EXP_DOC_STATUS.equals(expediente.getStatus())) {
                        deal.setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_INT_DOCUMENTACION);
                        break;
                    }
                }
            }
//            else {
//                deal.setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_INT_DOCUMENTACION);
//            }
            ssd.revisarLineaOperacionYActualizarDeal(deal, ((Visit) getVisit()).getUser());
        }
        catch (Exception e) {
            debug(e);
            Mensaje nextPage = (Mensaje) getRequestCycle().getPage("Mensaje");
            nextPage.setMensaje("Hubo un error al intentar verificar La Documentaci&oacute;n del " +
                    "Cliente.");
            nextPage.setTipo(Mensaje.TIPO_ERROR);
            getRequestCycle().activate(nextPage);
        }
        setDeal(deal);
    }

    /**
     * Actualiza el deal en la base de datos.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void guardar(IRequestCycle cycle) {
        getSicaServiceData().update(getDeal());
        Mensaje nextPage = (Mensaje) getRequestCycle().getPage("Mensaje");
        nextPage.setMensaje("El deal con n&uacute;mero: " + getDeal().getIdDeal() + " fue " +
                "actualizado con &eacute;xito.");
        nextPage.setTipo(Mensaje.TIPO_AVISO);
        getRequestCycle().activate(nextPage);
    }

    /**
     * Inicia una instancia del workflow de deal interbancario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void procesar(IRequestCycle cycle) {
        try {
            boolean esDeSwap = getDeal().getSwap() != null;
            int idDeal = getDeal().getIdDeal();
            
            if (getDeal().isPagoAnticipado() && getLinea() == null) {
                throw new SicaException("No se ha solicitado una l\u00ednea de cr\u00e9dito.");
            }
            asignarClaveDeReferencia(getDeal());
            getSicaWorkFlowService().wfIniciarProcesoDeal(getTicket(), idDeal);
            if (esDeSwap) {
                setDeal(getSicaServiceData().findDeal(idDeal));
                setLevel(1);
                getDelegate().record("El deal fue procesado con \u00e9xito", null);

            }
            else {
                Mensaje nextPage = (Mensaje) getRequestCycle().getPage("Mensaje");
                nextPage.setMensaje("El deal con n&uacute;mero " + getDeal().getIdDeal() +
                        " fue actualizado con &eacute;xito.");
                nextPage.setTipo(Mensaje.TIPO_AVISO);
                getRequestCycle().activate(nextPage);
            }
        }
        catch (RemoteAccessException e) {
            debug(e.getMessage(), e);
            getDelegate().record(SicaExceptionHelper.transformar(e).getMessage(), null);
        }        
        catch (Exception e) {
            debug(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }
    }

    /**
     * Regresa true si el deal es cancelable, false en otro caso.
     * 
     * @return boolean.
     */
    public boolean isCancelable() {
        Deal deal = getDeal();
        return deal.isCancelable() && !isHorarioVespertino() && (!deal.isInicioSwap() ||
                (deal.isInicioSwap() && Swap.STATUS_INICIO.equals(deal.getSwap().getStatus())));
    }

    /**
     * Define si el horario del sistema es Horario Vespertino.
     * 
     * @return boolean
     */
    protected boolean isHorarioVespertino() {
    	return getEstadoActual().getIdEstado() == Estado.ESTADO_OPERACION_VESPERTINA;
    }

    /**
     * Muestra la p&aacute;gina <code>CapturaDatosEnvio</code> y le pasa el deal que est&aacute;
     * siendo capturado.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @param primeraVez True si viene del checkbox de mensajer&iacute;a.
     */
    private void capturarDatosEnvio(IRequestCycle cycle, boolean primeraVez) {
        CapturaDatosEnvio nextPage = (CapturaDatosEnvio) cycle.getPage("CapturaDatosEnvio");
        nextPage.setDeal(getDeal());
        nextPage.setModoPrimeraVez(primeraVez);
        nextPage.setPaginaAnterior(getPageName());
        nextPage.activate(cycle);
    }

    /**
     * Muestra la p&aacute;gina <code>CapturaDatosEnvio</code> y le pasa el deal que est&aacute;
     * siendo capturado.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void capturarDatosEnvio(IRequestCycle cycle) {
        capturarDatosEnvio(cycle, false);
    }

    /**
     * Fija el valor de mensaje.
     *
     * @param mensaje El valor a asignar.
     */
    public abstract void setMensaje(String mensaje);
    
    /**
     * Regresa el valor de modoSubmit.
     *
     * @return String.
     */
    public abstract String getModoSubmit();
    
    /**
     * Fija el valor de modoSubmit.
     *
     * @param modoSubmit El valor a asignar.
     */
    public abstract void setModoSubmit(String modoSubmit);

    /**
     * Regresa el valor de paginaRetorno.
     *
     * @return boolean.
     */
    public abstract boolean getPaginaRetorno();
    
    /**
     * Fija el valor de paginaRetorno.
     *
     * @param paginaRetorno El valor a asignar.
     */
    public abstract void setPaginaRetorno(boolean paginaRetorno);
    
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
}
