/*
 * $Id: DealDetalle.java,v 1.39.12.1.4.2.32.1.12.2 2017/10/16 17:44:38 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.ixe.treasury.dom.common.FormaPagoLiq;

/**
 * Clase persistente para la tabla SC_DEAL_DETALLE. Representa los detalles de
 * compra y/o venta de un producto para una divisa en particular.
 *
 * El detalle incluye:
 * <ul>
 *   <li>Lo que se recibe</li>
 *   <li>Lo que se entrega</li>
 *   <li>Un producto (opcionalmente)</li>
 * </ul>
 *
 * Se tiene una relaci&oacute;n 1:1 a la superclase SC_DEAL_DET_FL, que se
 * refiere a la forma de liquidaci&oacute;n del detalle.
 *
 * @hibernate.joined-subclass table="SC_DEAL_DETALLE"
 * proxy="com.ixe.ods.sica.model.DealDetalle"
 *
 * @hibernate.joined-subclass-key column="ID_DEAL_POSICION"
 *
 * @hibernate.query name="findDealsDetallesByDateAndDivisaAndFormaLiquidacion"
 * query="FROM DealDetalle AS dd WHERE dd.deal.fechaCaptura BETWEEN ? AND ? AND
 * dd.divisa.idDivisa = ? AND dd.claveFormaLiquidacion = ? ORDER BY dd.recibimos DESC"
 *
 * @hibernate.query name="findDealsDetallesByDateAndDivisaAndFormaLiquidacionAndOperacion"
 * query="FROM DealDetalle AS dd WHERE dd.deal.fechaCaptura BETWEEN ? AND ? AND
 * dd.divisa.idDivisa = ? AND dd.claveFormaLiquidacion = ? AND dd.recibimos = ?"
 *
 * @hibernate.query name="findDealsDetallesByIdPersonaAndFechas"
 * query="FROM DealDetalle AS dd WHERE dd.deal.fechaCaptura BETWEEN ? AND ? AND
 * dd.deal.promotor.idPersona = ? AND dd.deal.statusDeal != 'CA'
 * ORDER BY dd.deal.promotor.idPersona, dd.divisa.idDivisa, dd.deal.idDeal"
 *
 * @hibernate.query name="findAllDealDetallesBySectorAndDate"
 * query="FROM DealDetalle AS dd WHERE dd.deal.fechaCaptura BETWEEN ? AND ? AND
 * dd.deal.statusDeal != 'CA' AND dd.divisa.idDivisa != 'MXN' AND dd.divisa.tipo != 'M'
 * ORDER BY dd.divisa.idDivisa"
 *
 * @hibernate.query name="findDetPactadosHoyFechaValorCash"
 * query="FROM DealDetalle AS dd WHERE dd.deal.fechaCaptura BETWEEN ? AND ? AND
 * dd.deal.tipoValor = 'CASH' AND dd.divisa.idDivisa != 'MXN' ORDER BY dd.deal.idDeal"
 *
 * @hibernate.query name="findDetPactadosHoyFechaValorTomSpot"
 * query="FROM DealDetalle AS dd WHERE dd.deal.fechaCaptura BETWEEN ? AND ? AND
 * dd.deal.tipoValor !='CASH' AND dd.divisa.idDivisa!= 'MXN' ORDER BY dd.deal.idDeal"
 *
 * @hibernate.query name="findDetallesUtilidadesMayoreo"
 * query="FROM DealDetalle AS dd WHERE dd.deal.fechaCaptura BETWEEN ? AND ? AND
 * dd.deal.statusDeal != 'CA' AND dd.statusDetalleDeal != 'CA'"
 *
 * @hibernate.query name="findAllDealDetallesBySectorAndDateOrderByDivisaAndRecibimos"
 * query="FROM DealDetalle AS dd WHERE dd.deal.fechaCaptura BETWEEN ? AND ? AND
 * dd.deal.statusDeal != 'CA' AND dd.divisa.idDivisa != 'MXN' AND dd.divisa.tipo != 'M'
 * ORDER BY dd.divisa.idDivisa,dd.recibimos ASC"
 *
 * @hibernate.query name="findAllDealDetallesBySectorAndDateOrderByDivisaAndRecibimosAndNoCuenta"
 * query="FROM DealDetalle AS dd WHERE dd.deal.fechaCaptura BETWEEN ? AND ? AND
 * dd.deal.statusDeal != 'CA' AND dd.divisa.idDivisa != 'MXN' AND dd.divisa.tipo != 'M'
 * ORDER BY dd.divisa.idDivisa,
 * dd.deal.fechaLiquidacion,dd.deal.contratoSica.noCuenta,dd.recibimos DESC"
 *
 * @hibernate.query name="findAllDealDetallesBySectorAndDateOrderByRecibimosAndDivisa"
 * query="FROM DealDetalle AS dd WHERE dd.deal.fechaCaptura BETWEEN ? AND ? AND
 * dd.deal.statusDeal != 'CA' AND dd.deal.statusDeal != 'AL' AND dd.divisa.idDivisa != 'MXN'
 * ORDER BY dd.recibimos,dd.divisa.idDivisa,dd.deal.fechaLiquidacion DESC"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.39.12.1.4.2.32.1.12.2 $ $Date: 2017/10/16 17:44:38 $
 */
public class DealDetalle extends DealPosicion {

    /**
     * Constructor vac&iacute;o. Inicializa tipoDeal en TIPO_DEAL_CLIENTE.
     */
    public DealDetalle() {
        super();
        setTipoDeal(TIPO_DEAL_CLIENTE);
    }

    /**
     * Constructor por default. Inicializa <i>claveFormaLiquidacion</i>,
     * <i>monto</i> y  <i>tipoCambioMesa</i>.
     *,
     * @param divisa La divisa del detalle.
     * @param claveFormaLiquidacion El producto del detalle.
     * @param monto El monto del detalle.
     * @param tipoCambioMesa El tipo de cambio en pizarra.
     */
    public DealDetalle(Divisa divisa, String claveFormaLiquidacion, double monto,
                       double tipoCambioMesa) {
        this();
        setDivisa(divisa);
        setClaveFormaLiquidacion(claveFormaLiquidacion);
        setMonto(monto);
        this.tipoCambioMesa = tipoCambioMesa;
        setTipoCambio(tipoCambioMesa);
    }

    /**
     * Constructor para detalles de forma liquidaci&oacute;n.
     *
     * @param deal El encabezado de deal a asignar.
     * @param recibimos true para compra, false para venta.
     * @param cfl La clave del producto.
     * @param divisa La divisa.
     * @param monto El monto en la divisa.
     * @param tipoCambioMesa El tipo de cambio de la mesa.
     * @param folioDetalle El n&uacute;mero de detalle a asignar.
     */
    public DealDetalle(Deal deal, boolean recibimos, String cfl, Divisa divisa, double monto,
                       double tipoCambioMesa, int folioDetalle) {
        this(divisa, cfl, monto, tipoCambioMesa);
        setIdUsuario(deal.getUsuario().getIdUsuario());
        setMesaCambio(deal.getCanalMesa().getMesaCambio());
        setRecibimos(recibimos);
        setFolioDetalle(folioDetalle);
        setIdPrecioReferencia(new Integer(0));
        deal.getDetalles().add(this);
        
        if (!deal.isInterbancario() && !deal.isDealCorreccion() && !deal.isDealBalanceo() && deal.getDetalles().size() == 1) {
            deal.setCompra(((DealDetalle) deal.getDetalles().get(0)).isRecibimos());
        }
    }

    /**
     * Constructor utilizado en la captura de deals interbancarios.
     *
     * @param claveFormaLiquidacion La clave de la forma de liquidaci&oacute;n.
     * @param deal El encabezado del deal.
     * @param divisa La divisa a asignar.
     * @param idUsuario El usuario que captura el deal.
     * @param mesaCambio La mesa de cambio.
     * @param monto El monto en la divisa.
     * @param recibimos true para recibimos, false para entregamos.
     * @param tipoCambio El tipo de cambio del cliente.
     * @param tipoCambioMesa El tipo de cambio de la mesa.
     * @param tipoDeal El tipo de deal.
     * @param precioReferenciaMidSpot El precio de referencia Mid Spot para la captura del Deal.
     * @param precioReferenciaSpot El precio de referencia Spot para la captura del Deal.
	 * @param idPrecioReferencia El identificador del precio de referencia a actual.
     */
    public DealDetalle(String claveFormaLiquidacion, Deal deal, Divisa divisa, int idUsuario,
                       MesaCambio mesaCambio, double monto, boolean recibimos, double tipoCambio,
                       double tipoCambioMesa, String tipoDeal, Double precioReferenciaMidSpot,
                       Double precioReferenciaSpot, Integer idPrecioReferencia) {
        this();
        setClaveFormaLiquidacion(claveFormaLiquidacion);
        setDeal(deal);
        setDivisa(divisa);
        setFactorDivisa(null);
        setIdUsuario(idUsuario);
        setMesaCambio(mesaCambio);
        setMonto(monto);
        setRecibimos(recibimos);
        setTipoCambio(tipoCambio);
        setTipoCambioMesa(tipoCambioMesa);
        setTipoDeal(tipoDeal);
        setPrecioReferenciaMidSpot(precioReferenciaMidSpot);
        setPrecioReferenciaSpot(precioReferenciaSpot);
        setIdPrecioReferencia(idPrecioReferencia);
    }

    /**
     * Regresa una copia de este detalle.
     *
     * @param detalle El detalle de deal a duplicar.
     * @return DealDetalle.
     */
    public static DealDetalle duplicar(DealDetalle detalle) {
        DealDetalle det = new DealDetalle();
        det.setClaveFormaLiquidacion(detalle.getClaveFormaLiquidacion());
        det.setCostoFormaLiq(detalle.getCostoFormaLiq());
        det.setDeal(detalle.getDeal());
        det.setDenominacion(detalle.getDenominacion());
        det.setDivisa(detalle.getDivisa());
        det.setFactorDivisa(detalle.getFactorDivisa());
        det.setPrecioReferenciaMidSpot(detalle.getPrecioReferenciaMidSpot());
        det.setPrecioReferenciaSpot(detalle.getPrecioReferenciaSpot());
        det.setIdPrecioReferencia(detalle.getIdPrecioReferencia());
        det.setIdFactorDivisa(detalle.getIdFactorDivisa());
        det.setIdSpread(detalle.getIdSpread());
        det.setIdUsuario(detalle.getIdUsuario());
        det.setInstruccionesBeneficiario(detalle.getInstruccionesBeneficiario());
        det.setInstruccionesIntermediario(detalle.getInstruccionesIntermediario());
        det.setInstruccionesPagador(detalle.getInstruccionesPagador());
        det.setMesaCambio(detalle.getMesaCambio());
        det.setMnemonico(detalle.getMnemonico());
        det.setMonto(detalle.getMonto());
        det.setObservaciones(detalle.getObservaciones());
        det.setPlantilla(detalle.getPlantilla());
        det.setRecibimos(detalle.isRecibimos());
        det.setTipoCambio(detalle.getTipoCambio());
        det.setTipoCambioMesa(detalle.getTipoCambioMesa());
        det.setTipoDeal(detalle.getTipoDeal());
        return det;
    }

    /**
     * Regresa true si el status del detalle no es Cancelado, el deal no es interbancario y si el
     * evento de horario est&aacute; en solicitud.
     *
     * @return boolean.
     */
    public boolean tieneAutPendientesHorario() {
        return !STATUS_DET_CANCELADO.equals(getStatusDetalleDeal()) &&
                !getDeal().isInterbancario() && isEvento(EV_IND_HORARIO,
                new String[]{Deal.EV_SOLICITUD});
    }

    /**
     * Regresa true si el status del detalle no es Cancelado, el deal no es interbancario y si el
     * evento de monto est&aacute; en solicitud.
     *
     * @return boolean.
     */
    public boolean tieneAutPendientesMonto() {
        return !STATUS_DET_CANCELADO.equals(getStatusDetalleDeal()) &&
                !getDeal().isInterbancario() && isEvento(EV_IND_MONTO,
                new String[]{Deal.EV_SOLICITUD});
    }

    //F65211 Complemento - Obligatorio propósito de transferencia Ley 115 LIC
    /**
     * Regresa true si es transferencia por SWIFT y tiene sin especificar el propósito de la transferencia
     *
     * @return boolean.
     */
    public boolean faltaPropositoDeTransferencia() {
    	
    	if(this.getPlantilla() != null && this.getPlantilla() instanceof IPlantillaIntl && 
    			(PlantillaIntl.TIPO_CUENTA_BANCO_SWIFT.equals(((IPlantillaIntl) this.getPlantilla()).getTipoCuentaBanco())
    				|| PlantillaIntl.TIPO_CUENTA_BANCO_SWIFT.equals(((IPlantillaIntl) this.getPlantilla()).getTipoCuentaBancoInterm()) )
    			){
    		if (this.getMnemonico() != null && this.getMnemonico().startsWith("E") &&
    				this.getInstruccionesBeneficiario() == null || "".equals(this.getInstruccionesBeneficiario().trim())) {
    			return true;
    		}
    	}            	
        return false;
    }
    
    /**
     * Fija en el indice especificado el status que se pasa como par&aacute;metro.
     *
     * @param status El status a asignar.
     * @param indice El &iacute;ndice a afectar.
     */
    public void setEvento(String status, int indice) {
        char[] evs = getEventosDetalleDeal().toCharArray();
        evs[indice] = status.charAt(0);
        setEventosDetalleDeal(new String(evs));
    }

    /**
     * Regresa el valor del evento en el &iacute;ndice especificado.
     *
     * @param indice El indice a buscar.
     * @return String.
     */
    public String decodificarEvento(int indice) {
        return String.valueOf(getEventosDetalleDeal().charAt(indice));
    }

    /**
     * Regresa true si el evento en el &iacute;ndice <i>indice</i> se encuentra en alguno de los
     * status especificados.
     *
     * @param indice El indice a localizar.
     * @param status EL arreglo de status a revisar.
     * @return boolean.
     */
    public boolean isEvento(int indice, String[] status) {
        String st = decodificarEvento(indice);
        for (int i = 0; i < status.length; i++) {
            if (st.equals(status[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Regresa true si el detalle de deal se encuentra en alguno de los status especificados.
     *
     * @param status EL arreglo de status a revisar.
     * @return boolean.
     */
    public boolean isStatusIn(String[] status) {
        for (int i = 0; i < status.length; i++) {
            if (getStatusDetalleDeal().equals(status[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Regresa true si el status del detalle es cancelado.
     *
     * @return boolean.
     */
    public boolean isCancelado() {
        return STATUS_DET_CANCELADO.equals(getStatusDetalleDeal());
    }

    /**
     * Si la divisa no es frecuente, regresa cero, si no regresa el valor del monto del detalle en
     * d&oacute;lares.
     *
     * @return double.
     */
    public double getMontoUSD() {
        return DealDetalleHelper.getMontoUSD(this);
    }

    /**
     * Permite saber si el detalle acepta Forma de Pago Liq.
     *
     * @param fpl La forma de pago liq. a verificar.
     * @return True o False.
     */
    public boolean aceptaFormaPagoLiq(FormaPagoLiq fpl) {
        return DealDetalleHelper.aceptaFormaPagoLiq(this, fpl);
    }

    /**
     * Permite saber la Descripci&oacute;n del Status de un Deal Detalle.
     *
     * @return La descripcio&oacute;n.
     */
    public String getDescripcionStatus() {
        return DealDetalleHelper.getDescripcionStatus(this);
    }

    /**
     * Permite saber informaci&oacute;n adicional de las Pantallas de las Plantillas y las Formas de
     * Pago Liq. que tiene un Detalle.
     *
     * @param fpl Las formas de pago liq.
     * @param pp Las Plantillas pantallas
     * @return La informacion adicional.
     */
    public List getInfoAdicional(FormaPagoLiq fpl, PlantillaPantalla pp) {
        return DealDetalleHelper.getInfoAdicional(this, fpl, pp);
    }

    /**
     * Permite saber si una Forma Liq. capturada es permitida o no.
     *
     * @return True o False.
     */
    public boolean isCapturaFormaLiqPermitida() {
        return DealDetalleHelper.isCapturaFormaLiqPermitida(this);
    }

    /**
     * Permite saber si se puede o no realizar una modificacion al detalle.
     *
     * @return boolean.
     */
    public boolean isModificacionPermitida() {
        return getMnemonico() != null && !isReversadoProcesoReverso() &&
                !getDeal().tieneSolSobreprecioPendiente() && !isStatusIn(new String[]{
                DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ, DealDetalle.STATUS_DET_CANCELADO});
    }

    /**
     * Permite saber si se puede o no realizar una cancelaci&oacute;n de una liquidaci&oacute;n de
     * un detalle.
     *
     * @return True o False.
     */
    public boolean isCancelacionLiqPermitida() {
        return DealDetalleHelper.isCancelacionLiqPermitida(this);
    }

    /**
     * Regresa true si el detalle a&uacute;n no ha sido liquidado, el deal no ha sido cancelado y no
     * hay solicitud de cancelaci&oacute;n o modificaci&oacute;n pendiente,
     *
     * @return boolean.
     */
    public boolean isSplitPermitido() {
        return DealDetalleHelper.isSplitPermitido(this);
    }

    /**
     * Regresa true si el detalle se encuentra en status Totalmente Pagado y Liquidado.
     *
     * @return boolean.
     */
    public boolean isLiquidado() {
        return STATUS_DET_TOTALMENTE_PAG_LIQ.equals(getStatusDetalleDeal());
    }

    /**
     * Regresa true si el detalle se encuentra en status Reversado o en proceso de reverso.
     *
     * @return boolean.
     */
    public boolean isReversadoProcesoReverso() {
        return Deal.PROC_REVERSO == getReversado() || Deal.REVERSADO == getReversado();
    }

    /**
     * Permite obtener el encabezado del deal.
     *
     * @return	El contexto.
     */
    public HashMap getContextoAlertas() {
        return DealDetalleHelper.getContextoAlertas(this);
    }

    /**
     * Complementa el mapa de contexto para reportes.
     *
     * @param detalle El mapa del detalle.
     */
    public void getContextoExcepciones(HashMap detalle) {
        if (haySolAut() || hayNegAut()) {
            detalle.put("SI_NO", "SI");
        }
        else {
            detalle.put("SI_NO", "NO");
        }
    }

    /**
     * Permite obtener el Status Posicion Log para Cancelaci&oacute;n.
     *
     * @return El Status.
     */
    public String getStatusPosLogParaCancelacion() {
        if (getDeal().isInterbancario()) {
            return isRecibimos() ?
                    PosicionLog.CANCELACION_COMPRA_INTERBANCARIA :
                    PosicionLog.CANCELACION_VENTA_INTERBANCARIA;
        }
        return isRecibimos() ? PosicionLog.CANCELACION_COMPRA : PosicionLog.CANCELACION_VENTA;
    }

    /**
     * Regresa el valor de claveFormaLiquidacion.
     *
     * @hibernate.property column="CLAVE_FORMA_LIQUIDACION"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getClaveFormaLiquidacion() {
        return claveFormaLiquidacion;
    }

    /**
     * Fija el valor de claveFormaLiquidacion.
     *
     * @param claveFormaLiquidacion La claveFormaLiquidacion a asignar.
     */
    public void setClaveFormaLiquidacion(String claveFormaLiquidacion) {
        this.claveFormaLiquidacion = claveFormaLiquidacion;
    }

    /**
     * Regresa el valor de comisionOficialUsd.
     *
     * @hibernate.property column="COMISION_OFICIAL_USD"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getComisionOficialUsd() {
        return comisionOficialUsd;
    }

    /**
     * Fija el valor de comisionOficialUsd.
     *
     * @param comisionOficialUsd El valor a asignar.
     */
    public void setComisionOficialUsd(double comisionOficialUsd) {
        this.comisionOficialUsd = comisionOficialUsd;
    }

    /**
     * Regresa el valor de comisionCobradaUsd.
     *
     * @hibernate.property column="COMISION_COBRADA_USD"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getComisionCobradaUsd() {
        return comisionCobradaUsd;
    }

    /**
     * Fija el valor de comisionCobradaUsd.
     *
     * @param comisionCobradaUsd El valor a asignar.
     */
    public void setComisionCobradaUsd(double comisionCobradaUsd) {
        this.comisionCobradaUsd = comisionCobradaUsd;
    }

    /**
     * Regresa el valor de comisionOficialMxn.
     *
     * @hibernate.property column="COMISION_COBRADA_MXN"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getComisionCobradaMxn() {
        return comisionCobradaMxn;
    }

    /**
     * Fija el valor de comisionCobradaMxn.
     *
     * @param comisionCobradaMxn El valor a asignar.
     */
    public void setComisionCobradaMxn(double comisionCobradaMxn) {
        this.comisionCobradaMxn = comisionCobradaMxn;
    }

    /**
     * Regresa el valor de costoFormaLiq.
     *
     * @hibernate.property column="COSTO_FORMA_LIQ"
     * not-null="false"
     * unique="false"
     * @return BigDecimal.
     */
    public BigDecimal getCostoFormaLiq() {
        return costoFormaLiq;
    }

    /**
     * Fija el valor de costoFormaLiq.
     *
     * @param costoFormaLiq El valor a asignar.
     */
    public void setCostoFormaLiq(BigDecimal costoFormaLiq) {
        this.costoFormaLiq = costoFormaLiq;
    }

    /**
     * Regresa el valor de datosConfirmacion.
     *
     * @hibernate.property column="DATOS_CONFIRMACION"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getDatosConfirmacion() {
        return datosConfirmacion;
    }

    /**
     * Establece el valor de datosConfirmacion.
     *
     * @param datosConfirmacion El valor a asignar.
     */
    public void setDatosConfirmacion(String datosConfirmacion) {
        this.datosConfirmacion = datosConfirmacion;
    }

    /**
     * Regresa el valor de denominacion.
     *
     * @hibernate.property column="DENOMINACION"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getDenominacion() {
        return denominacion;
    }

    /**
     * Fija el valor de denominacion.
     *
     * @param denominacion El valor a asignar.
     */
    public void setDenominacion(Double denominacion) {
        this.denominacion = denominacion;
    }

    /**
     * Regresa el valor de eventosDetalleDeal.
     *
     * @hibernate.property column="EVENTOS_DETALLE_DEAL"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getEventosDetalleDeal() {
        return eventosDetalleDeal;
    }

    /**
     * Fija el valor de eventosDetalleDeal.
     *
     * @param eventosDetalleDeal El valor a asignar.
     */
    public void setEventosDetalleDeal(String eventosDetalleDeal) {
        this.eventosDetalleDeal = eventosDetalleDeal;
    }

    /**
     * Regresa el valor de folioDetalle.
     *
     * @hibernate.property column="FOLIO_DETALLE"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getFolioDetalle() {
        return folioDetalle;
    }

    /**
     * Fija el valor de folioDetalle.
     *
     * @param folioDetalle El valor a asignar.
     */
    public void setFolioDetalle(int folioDetalle) {
        this.folioDetalle = folioDetalle;
    }

    /**
     * Regresa el valor de idLiquidacionDetalle.
     *
     * @hibernate.property column="ID_LIQUIDACION_DETALLE"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getIdLiquidacionDetalle() {
        return idLiquidacionDetalle;
    }

    /**
     * Fija el valor de idLiquidacionDetalle.
     *
     * @param idLiquidacionDetalle El valor de idLiquidacionDetalle.
     */
    public void setIdLiquidacionDetalle(Integer idLiquidacionDetalle) {
        this.idLiquidacionDetalle = idLiquidacionDetalle;
    }

    /**
     * Regresa el valor de idPrecioReferencia.
     * 
     * @deprecated Se utilizar&aacute; el valor directo del Precio Referencia
     * @hibernate.property column="ID_PRECIO_REFERENCIA"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getIdPrecioReferencia() {
        return idPrecioReferencia;
    }

    /**
     * Fija el valor de idPrecioReferencia.
     *
     * @deprecated Se utilizar&aacute; el valor directo del Precio Referencia
     * @param idPrecioReferencia El valor a asignar.
     */
    public void setIdPrecioReferencia(Integer idPrecioReferencia) {
        this.idPrecioReferencia = idPrecioReferencia;
    }
    
    /**
     * Regresa el valor de precioReferenciaMidSpot.
     * 
     * @hibernate.property column="PRE_REF_MID_SPOT"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getPrecioReferenciaMidSpot() {
        return precioReferenciaMidSpot;
    }

    /**
     * Fija el valor de precioReferenciaMidSpot.
     *
     * @param precioReferenciaMidSpot El valor a asignar.
     */
    public void setPrecioReferenciaMidSpot(Double precioReferenciaMidSpot) {
        this.precioReferenciaMidSpot = precioReferenciaMidSpot;
    }
    
    /**
     * Regresa el valor de precioReferenciaSpot.
     * 
     * @hibernate.property column="PRE_REF_SPOT"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getPrecioReferenciaSpot() {
        return precioReferenciaSpot;
    }

    /**
     * Fija el valor de precioReferenciaSpot.
     *
     * @param precioReferenciaSpot El valor a asignar.
     */
    public void setPrecioReferenciaSpot(Double precioReferenciaSpot) {
        this.precioReferenciaSpot = precioReferenciaSpot;
    }

    /**
     * Regresa el valor de idSpread.
     *
     * @hibernate.property column="ID_SPREAD"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getIdSpread() {
        return idSpread;
    }

    /**
     * Fija el valor de idSpread.
     *
     * @param idSpread El valor a asignar.
     */
    public void setIdSpread(int idSpread) {
        this.idSpread = idSpread;
    }

    /**
     * Regresa el valor de instruccionesBeneficiario.
     *
     * @hibernate.property column="INSTRUCCIONES_BENEFICIARIO"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getInstruccionesBeneficiario() {
        return instruccionesBeneficiario;
    }

    /**
     * Fija el valor de instruccionesBeneficiario.
     *
     * @param instruccionesBeneficiario El valor a asignar.
     */
    public void setInstruccionesBeneficiario(String instruccionesBeneficiario) {
        this.instruccionesBeneficiario = instruccionesBeneficiario;
    }

    /**
     * Regresa el valor de instruccionesIntermediario.
     *
     * @hibernate.property column="INSTRUCCIONES_INTERMEDIARIO"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getInstruccionesIntermediario() {
        return instruccionesIntermediario;
    }

    /**
     * Fija el valor de instruccionesIntermediario.
     *
     * @param instruccionesIntermediario El valor a asignar.
     */
    public void setInstruccionesIntermediario(String instruccionesIntermediario) {
        this.instruccionesIntermediario = instruccionesIntermediario;
    }

    /**
     * Regresa el valor de instruccionesPagador.
     *
     * @hibernate.property column="INSTRUCCIONES_PAGADOR"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getInstruccionesPagador() {
        return instruccionesPagador;
    }

    /**
     * Fija el valor de instruccionesPagador.
     *
     * @param instruccionesPagador El valor a asignar.
     */
    public void setInstruccionesPagador(String instruccionesPagador) {
        this.instruccionesPagador = instruccionesPagador;
    }

    /**
     * Regresa el valor de mnemonico.
     *
     * @hibernate.property column="MNEMONICO"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getMnemonico() {
        return mnemonico;
    }

    /**
     * Fija el valor de mnemonico.
     *
     * @param mnemonico El mnemonico a asignar.
     */
    public void setMnemonico(String mnemonico) {
        this.mnemonico = mnemonico;
    }

    /**
     * Regresa el valor de observaciones.
     *
     * @hibernate.property column="OBSERVACIONES"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Fija el valor de observaciones.
     *
     * @param observaciones El valor a asignar.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Regresa el valor de statusDetalleDeal.
     *
     * @hibernate.property column="STATUS_DETALLE_DEAL"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getStatusDetalleDeal() {
        return statusDetalleDeal;
    }

    /**
     * Fija el valor de statusDetalleDeal.
     *
     * @param statusDetalleDeal El valor a asignar.
     */
    public void setStatusDetalleDeal(String statusDetalleDeal) {
        this.statusDetalleDeal = statusDetalleDeal;
    }

    /**
     * Regresa el valor de tipoCambioMesa.
     *
     * @hibernate.property column="TIPO_CAMBIO_MESA"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getTipoCambioMesa() {
        return tipoCambioMesa;
    }

    /**
     * Fija el valor de tipoCambioMesa.
     *
     * @param tipoCambioMesa El valor a asignar.
     */
    public void setTipoCambioMesa(double tipoCambioMesa) {
        this.tipoCambioMesa = tipoCambioMesa;
    }

    /**
     * Regresa el valor de tmpTcc.
     *
     * @return double.
     */
    public double getTmpTcc() {
        return tmpTcc;
    }

    /**
     * Fija el valor de tmpTcc.
     *
     * @param tmpTcc El valor a asignar.
     */
    public void setTmpTcc(double tmpTcc) {
        this.tmpTcc = tmpTcc;
    }

    /**
     * Regresa el valor de deal.
     *
     * @hibernate.many-to-one column="ID_DEAL"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Deal"
     * outer-join="auto"
     * unique="false"
     * @return Deal.
     */
    public Deal getDeal() {
        return _deal;
    }

    /**
     * Fija el valor de deal.
     *
     * @param deal El valor a asignar.
     */
    public void setDeal(Deal deal) {
        _deal = deal;
    }

    /**
     * Regresa el valor de idFactorDivisa.
     * 
     * @deprecated Se debe utilizar el valor directo del Factor Divisa.
     * @hibernate.property column="ID_FACTOR_DIVISA"
     * not-null="false"
     * unique="false"
     * @return idFactorDivisa.
     */
    public Integer getIdFactorDivisa() {
        return idFactorDivisa;
    }

    /**
     * Fija el valor de idFactorDivisa.
     *
     * @deprecated Se debe utilizar el valor directo del precio referencia.
     * @param idFactorDivisa El valor a asignar.
     */
    public void setIdFactorDivisa(Integer idFactorDivisa) {
        this.idFactorDivisa = idFactorDivisa;
    }
    
    /**
     * Regresa el valor de fed.
     *
     * @hibernate.property column="FED"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getFed() {
        return fed;
    }

    /**
     * Fija el valor de fed.
     *
     * @param fed El valor a asignar.
     */
    public void setFed(String fed) {
        this.fed = fed;
    }

    /**
     * Regresa el valor de msgMt.
     *
     * @hibernate.property column="MSG_MT"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getMsgMt() {
        return msgMt;
    }

    /**
     * Fija el valor de msgMt.
     *
     * @param msgMt El valor a asignar.
     */
    public void setMsgMt(String msgMt) {
        this.msgMt = msgMt;
    }

    /**
     * Regresa el valor de factorDivisa.
     * 
     * @hibernate.property column="FACTOR_DIVISA"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getFactorDivisa() {
        return factorDivisa;
    }
    
    /**
     * Fija el valor de factorDivisa.
     *
     * @param factorDivisa El valor a asignar.
     */
    public void setFactorDivisa(Double factorDivisa) {
        this.factorDivisa = factorDivisa;
    }

    /**
     * Regresa el valor de plantilla.
     *
     * @hibernate.many-to-one column="ID_PLANTILLA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Plantilla"
     * outer-join="auto"
     * unique="false"
     * @return Plantilla.
     */
    public IPlantilla getPlantilla() {
        return _plantilla;
    }

    /**
     * Fija el valor de plantilla.
     *
     * @param plantilla El valor a asignar.
     */
    public void setPlantilla(IPlantilla plantilla) {
        _plantilla = plantilla;
    }

    /**
     * Regresa el valor de detallesStatusLog.
     *
     * @hibernate.set inverse="true"
     * lazy="true"
     * cascade="none"
     * @hibernate.collection-key column="ID_DETALLE_DEAL"
     * @hibernate.collection-one-to-many class="com.ixe.ods.sica.model.DealDetalleStatusLog"
     * @return Set.
     */
    public Set getDetallesStatusLog() {
        return _detallesStatusLog;
    }

    /**
     * Fija el valor de detallesStatusLog.
     *
     * @param detallesStatusLog El valor a asignar.
     */
    public void setDetallesStatusLog(Set detallesStatusLog) {
        _detallesStatusLog = detallesStatusLog;
    }

    /**
     * Regresa el valor de reemplazadoPorA.
     *
     * @hibernate.property column="REEMPLAZADO_POR_A"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getReemplazadoPorA() {
        return reemplazadoPorA;
    }

    /**
     * Fija el valor de reemplazadoPorA.
     *
     * @param reemplazadoPorA El valor a asignar.
     */
    public void setReemplazadoPorA(Integer reemplazadoPorA) {
        this.reemplazadoPorA = reemplazadoPorA;
    }

    /**
     * Regresa el valor de reversado.
     *
     * @hibernate.property column="REVERSADO"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getReversado() {
        return reversado;
    }

    /**
     * Establece el valor de reversado.
     *
     * @param reversado El valor a asignar.
     */
    public void setReversado(int reversado) {
        this.reversado = reversado;
    }
    
    /**
     * Regresa el valor de sustituyeA.
     *
     * @hibernate.property column="SUSTITUYE_A"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getSustituyeA() {
        return sustituyeA;
    }

    /**
     * Fija el valor de sustituyeA.
     *
     * @param sustituyeA El valor a asignar.
     */
    public void setSustituyeA(Integer sustituyeA) {
        this.sustituyeA = sustituyeA;
    }

    /**
     * Regresa el valor de reemplazadoPorB.
     *
     * @hibernate.property column="REEMPLAZADO_POR_B"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getReemplazadoPorB() {
        return reemplazadoPorB;
    }

    /**
     * Fija el valor de reemplazadoPorB.
     *
     * @param reemplazadoPorB El valor a asignar.
     */
    public void setReemplazadoPorB(Integer reemplazadoPorB) {
        this.reemplazadoPorB = reemplazadoPorB;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof DealDetalle)) {
            return false;
        }
        DealDetalle castOther = (DealDetalle) other;
        return new EqualsBuilder().append(this.getIdDealPosicion(), castOther.getIdDealPosicion()).
                append(this.getFolioDetalle(), castOther.getFolioDetalle()).isEquals();
    }

    /**
     * Permite saber si hay solicitud de autorizaci&oacute;n.
     *
     * @return True o False.
     */
    public boolean haySolAut() {
        return getEventosDetalleDeal().indexOf('S') >= 0;
    }

    /**
     * Permite saber si hay negaci&oacute;n de autorizaci&oacute;n.
     *
     * @return True o False.
     */
    public boolean hayNegAut() {
        return getEventosDetalleDeal().indexOf('N') >= 0;
    }

    /**
     * Regresa true si el deal tiene una solicitud de autorizaci&oacute;n de modificaci&oacute;n
     * que est&aacute; pendiente.
     *
     * @return boolean.
     */
    public boolean tieneSolModifPendiente() {
        return isEvento(isInterbancario() ?
                DealDetalle.EV_IND_INT_MODIFICACION : DealDetalle.EV_IND_MODIFICACION,
                    new String[]{Deal.EV_SOLICITUD});
    }

    /**
     * Regresa true si el encabezado del deal es interbancario.
     *
     * @return boolean.
     */
    public boolean isInterbancario() {
        return _deal.isInterbancario();
    }

    /**
     * Regresa true si el detalle se encuentra en status de Alta o de Proces&aacute;ndose.
     *
     * @return boolean.
     */
    public boolean isAltaOProcesandose() {
        return STATUS_DET_PROCESO_CAPTURA.equals(getStatusDetalleDeal()) ||
                STATUS_DET_COMPLETO.equals(getStatusDetalleDeal());
    }

    /**
     * Regresa true si el detalle se encuentra en status de Alta, Proces&aacute;ndose, o Totalmente
     * Liquidado.
     *
     * @return boolean.
     */
    public boolean isAltaOProcesandoseOLiquidado() {
        return isAltaOProcesandose() ||
                STATUS_DET_TOTALMENTE_PAG_LIQ.equals(getStatusDetalleDeal());
    }

    /**
     * Regresa true si la clave de la divisa del detalle es Pesos Mexicanos.
     *
     * @return boolean.
     */
    public boolean isPesos() {
        return Divisa.PESO.equals(getDivisa().getIdDivisa());
    }

    /**
     * Regresa true si la clave de la divisa del detalle es D&oacute;lar americano.
     *
     * @return boolean.
     */
    public boolean isDolar() {
        return Divisa.DOLAR.equals(getDivisa().getIdDivisa());
    }

    /**
     * Regresa true si tiene solicitud de cancelaci&oacute;n de detalle pendiente o .
     *
     * @return boolean.
     */
    public boolean tieneSolCancPendiente() {
        return !getDeal().isInterbancario() &&
                !isCancelado() &&
                isEvento(DealDetalle.EV_IND_GRAL_CANC_DET, new String[]{Deal.EV_SOLICITUD});
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdDealPosicion()).append(folioDetalle).toHashCode();
    }

    /**
     * Concatena en forma de cadenas las propiedades del Deal Detalle.
     *
     * @return El Objeto como cadena.
     */
    public String toString() {
        return new ToStringBuilder(this).append("claveFormaLiquidacion", claveFormaLiquidacion).
                append("comisionCobradaMxn", comisionCobradaMxn).
                append("comisionCobradaUsd", comisionCobradaUsd).
                append("comisionOficialUsd", comisionOficialUsd).
                append("costoFormaLiq", costoFormaLiq).append("denominacion", denominacion).
                append("eventosDetalleDeal", eventosDetalleDeal).
                append("factorDivisa", factorDivisa).append("folioDetalle", folioDetalle).
                append("idDealPosicion", idDealPosicion).
                append("idLiquidacionDetalle", idLiquidacionDetalle).
                append("idPrecioReferencia", idPrecioReferencia).append("idSpread", idSpread).
                append("idUsuario", getIdUsuario()).
                append("instruccionesBeneficiario", getInstruccionesBeneficiario()).
                append("instruccionesIntermediario", getInstruccionesIntermediario()).
                append("instruccionesPagador", instruccionesPagador).append("mnemonico", mnemonico).
                append("monto", getMonto()).append("observaciones", getObservaciones()).
                append("plantilla", getPlantilla()).append("recibimos", isRecibimos()).
                append("statusDetalleDeal", getStatusDetalleDeal()).
                append("reversado", getReversado()).
                append("tipoCambio", getTipoCambio()).append("tipoCambioMesa", getTipoCambioMesa()).
                append("tipoDeal", getTipoDeal()).toString();
    }
    
    /**
     * Retorna el tipo de cambio invertido solo en caso de que el deal sea interbancario, la divisa de referencia sea USD y la
     * divisa del deal tenga su atributo <divide> = true
     * 
     * @return
     */
    public double obtenTipoCambioInvertido() {
    	if (
    		getDeal().isInterbancario() 
    		&& Divisa.DOLAR.equals(getMesaCambio().getDivisaReferencia().getIdDivisa()) 
    		&& getDivisa().isDivide()
    	) {
    		return (1 / getTipoCambio());
    	} else {
    		return getTipoCambio();
    	}
    }

    /**
     * La clave del producto.
     */
    private String claveFormaLiquidacion;

    /**
     * La comisi&oacute;n como debi&oacute; cobrarse.
     */
    private double comisionOficialUsd;

    /**
     * La comisi&oacute;n como se cobr&oacute; en divisa USD.
     */
    private double comisionCobradaUsd;

    /**
     * La comisi&oacute;n como se cobr&oacute; en divisa MXN.
     */
    private double comisionCobradaMxn;

    /**
     * El costo de la forma de liquidaci&oacute;n.
     */
    private BigDecimal costoFormaLiq;

    /**
     * Cadena con tres valores separados por '|' para la confirmaci&oacute;n de deals interbancarios
     * a) Hora. b) Contacto. c) Qui&eacute;n confirm&oacute;.
     */
    private String datosConfirmacion;

    /**
     * Especificar qu&eacute; denominaciones se desean.
     */
    private Double denominacion;

    /**
     * Los eventos del detalle.
     */
    private String eventosDetalleDeal = "          ";

    /**
     * El folio del detalle.
     */
    private int folioDetalle;

    /**
     * El identificador del detalle de liquidaci&oacute;n.
     */
    private Integer idLiquidacionDetalle;

    /**
     * Identificador del precio de referencia utilizado al momento de la
     * captura.
     * 
     * @deprecated Se debe utilizar el valor directo del Precio Referencia.
     */
    private Integer idPrecioReferencia;
    
    /**
     * El precio referencia Mid Spot utilizado al momento de la
     * captura.
     */
    private Double precioReferenciaMidSpot;
    
    /**
     * El precio referencia Spot utilizado al momento de la
     * captura.
     */
    private Double precioReferenciaSpot;

    /**
     * Identificador del spread utilizado al momento de la captura.
     */
    private int idSpread;

    /**
     * Las instrucciones para el beneficiario.
     */
    private String instruccionesBeneficiario;

    /**
     * Las instrucciones para el banco intermediario.
     */
    private String instruccionesIntermediario;

    /**
     * Las instrucciones para el banco pagador.
     */
    private String instruccionesPagador;

    /**
     * El mnemonico de la matriz de liquidaciones.
     */
    private String mnemonico;

    /**
     * Alguna instrucci&oacute;n especial para la liquidaci&oacute;n (opcional).
     */
    private String observaciones;

    /**
     * El status del detalle del deal.
     */
    private String statusDetalleDeal = Deal.STATUS_DEAL_PROCESO_CAPTURA;

    /**
     * Indica en un detalle de deal cancelado por split o por cambio de forma de liquidaci&oacute;n,
     * el idDealPosicion del detalle de un nuevo deal que lo reemplaza.
     */
    private Integer reemplazadoPorA;

    /**
     * 0 No reversado, 1 en Proceso de Reverso, 2 Reversado.
     */
    private int reversado;

    /**
     * Indica el idDealPosicion del detalle de deal cancelado al que sustituye.
     */
    private Integer sustituyeA;

    /**
     * Indica en un detalle de deal cancelado por split, el idDealPosicion del detalle de un nuevo
     * deal que lo reemplaza.
     */
    private Integer reemplazadoPorB;

    /**
     * El tipo de cambio de la pizarra.
     */
    private double tipoCambioMesa;

    /**
     * El tipo de cambio para correcci&oacute;n de desviaciones.
     */
    private double tmpTcc;

    /**
     * Relaci&oacute;n muchos-a-uno con Deal.
     */
    private Deal _deal;

    /**
     * El factor de divisa utilizado al momento de la captura.
     */
    private Double factorDivisa;
    
    /**
     * El identificador del Factor Divisa utilizado.
     * 
     * @deprecated Se debe utilizar el valor directo del Factor Divisa.
     */
    private Integer idFactorDivisa;

    /**
     * El n&uacute;mero de Fed que provee el SITE.
     */
    private String fed = "";

    /**
     * El contenido del mensaje MT recibido del SITE al momento de la liquidaci&oacute;n.
     */
    private String msgMt = "";

    /**
     * Relaci&oacute;n muchos-a-uno con Plantilla.
     */
    private IPlantilla _plantilla;

    /**
     * Relaci&oacute;n uno-a-muchos con DealDetalleStatusLog.
     */
    private Set _detallesStatusLog = new HashSet();

    /**
     * Constante para el &iacute;ndice de documentaci&oacute;n (deals normales).
     */
    public static final int EV_IND_DOCUMENTACION = 0;

    /**
     * Constante para el &iacute;ndice de tipo de cambio cliente (deals normales).
     */
    public static final int EV_IND_TIPO_CAMBIO_CLIENTE = 1;

    /**
     * Constante para el &iacute;ndice de modificaci&oacute;n (deals normales).
     */
    public static final int EV_IND_MODIFICACION = 5;

    /**
     * Constante para el &iacute;ndice de horario (deals normales).
     */
    public static final int EV_IND_HORARIO = 6;

    /**
     * Constante para el &iacute;ndice de monto (deals normales).
     */
    public static final int EV_IND_MONTO = 7;

    /**
     * Constante para el &iacute;ndice de documentaci&oacute;n (deals interbancarios).
     */
    public static final int EV_IND_INT_DOCUMENTACION = 0;

    /**
     * Constante para el &iacute;ndice de l&iacute;nea de operaci&oacute;n (deals interbancarios).
     */
    public static final int EV_IND_INT_LINEA_OPERACION = 1;

    /**
     * Constante para el &iacute;ndice de modificaci&oacute;n (deals interbancarios).
     */
    public static final int EV_IND_INT_MODIFICACION = 4;

    /**
     * Constante para el &iacute;ndice de posici&oacute;n afectada.
     */
    public static final int EV_IND_GRAL_AFECTADA_POSICION = 2;

    /**
     * Constante para indicar que el detalle no debe liquidarse debido a que forma parte de un deal
     * de balanceo de un reverso.
     */
    public static final int EV_IND_GRAL_REVERSO_NO_LIQUIDAR = 8;

    /**
     * Constante para el &iacute;ndice de Cancelaci&oacute;n de Detalles.
     */
    public static final int EV_IND_GRAL_CANC_DET = 9;

    /**
     * Constante para el status del Detalle Proceso captura.
     */
    public static final String STATUS_DET_PROCESO_CAPTURA = "AL";

    /**
     * Constante para el status del Detalle Completo.
     */
    public static final String STATUS_DET_COMPLETO = "CO";

    /**
     * Constante para el status del Detalle Parcialmente Liquidado.
     */
    public static final String STATUS_DET_PARCIALMENTE_PAG_LIQ = "PT";

    /**
     * Constante para el status del Detalle Totalmente Liquidado.
     */
    public static final String STATUS_DET_TOTALMENTE_PAG_LIQ = "TT";

    /**
     * Constante para el status del Detalle Cancelado.
     */
    public static final String STATUS_DET_CANCELADO = "CA";

    /**
     * Permite obtener las descripciones de los status del detalle.
     */
    public static final String[] EVENTOS_DESCRIPCIONES = new String[] {
        "Plantilla", "Desviaci\u00f3n en Tipo de Cambio al Cliente", "Posici\u00f3n",
            "L\u00ednea de Cr\u00e9dito", "Toma en firme", "Modificaci\u00f3n", "Horario", "Monto",
            "Cancelaci\u00f3n Detalle"
    };

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 3854546518225787970L;
}
