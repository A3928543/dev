/*
 * $Id: Deal.java,v 1.52.2.3.8.1.10.1.4.3.14.1.14.1.10.1 2016/07/13 21:42:36 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.Direccion;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.utils.BDUtils;
import com.ixe.ods.sica.utils.DateUtils;

/**
 * Clase persistente para la tabla SC_DEAL. Almacena los encabezados de los deals.
 *
 * @hibernate.class table="SC_DEAL"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Deal"
 * dynamic-update="true"
 *
 * @hibernate.query name="findPersonaContratoSica"
 * query="SELECT distinct cs, p FROM ContratoSica AS cs LEFT JOIN cs.roles AS pcr,
 * com.ixe.ods.bup.model.Persona AS p WHERE pcr.id.persona.idPersona = p.idPersona AND
 * cs.noCuenta = ? AND pcr.id.rol = 'TIT' AND pcr.status.estatus = 'VIG'"
 *
 * @hibernate.query name="findAllDealsBySectorAndDate"
 * query="FROM Deal AS d WHERE d.fechaCaptura BETWEEN ? AND ? ORDER BY d.fechaLiquidacion"
 *
 * @hibernate.query name="findDealByStatus"
 * query="FROM Deal AS d WHERE d.statusDeal = 'CO' OR d.statusDeal = 'PL' OR d.statusDeal = 'TP'
 * ORDER BY d.idDeal"
 *
 * @hibernate.query name="findDealByStatusByFecha"
 * query="FROM Deal AS d WHERE (d.statusDeal = 'CO' OR d.statusDeal = 'PL' OR d.statusDeal = 'TP')
 * AND d.fechaLiquidacion BETWEEN ? AND ? ORDER BY d.idDeal"
 *
 * @hibernate.query name="findDealsPendientesReconocimiento"
 * query="FROM Deal AS d WHERE d.statusDeal != 'CA' AND
 * d.canalMesa.mesaCambio.divisaReferencia.idDivisa != 'MXN' ORDER BY d.idDeal"
 *
 * @hibernate.query name="findDealsPactadosHoy"
 * query="FROM Deal AS d INNER JOIN FETCH d.detalles det INNER JOIN FETCH det.divisa LEFT JOIN
 * FETCH det.plantilla WHERE (d.fechaCaptura BETWEEN ? AND ?) AND d.statusDeal != 'CA' ORDER BY
 * d.idDeal"
 *
 * @hibernate.query name="findAllDealsBySectorAndDateOderByDivisaAndSector"
 * query="FROM Deal AS d WHERE d.fechaCaptura BETWEEN ? AND ? ORDER BY d.fechaLiquidacion"
 *
 * @hibernate.query name="findDealsLiquidacionAnticipada"
 * query="FROM Deal AS d INNER JOIN FETCH d.detalles det INNER JOIN FETCH det.divisa LEFT JOIN
 * FETCH det.plantilla WHERE d.liquidacionAnticipada = '1'"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.52.2.3.8.1.10.1.4.3.14.1.14.1.10.1 $ $Date: 2016/07/13 21:42:36 $
 */
public class Deal implements Serializable {

    /**
     * Constructor por default. No hace nada.
     */
    public Deal() {
        super();
        setEvento(Deal.EV_NO_DETERMINADO, Deal.EV_IND_GRAL_PAG_ANT);
    }

    /**
     * Aplica el evento del ind&iacute;ce especificado al deal.
     *
     * @param status El status a asignar.
     * @param indice El &iacute;ndice del evento.
     */
    public void setEvento(String status, int indice) {
        DealHelper.setEvento(this, status, indice);
    }

    /**
     * Regresa el status del evento en el &iacute;ndice especificado.
     *
     * @param indice El &iacute;ndice del evento a decodificar.
     * @return String.
     */
    public String decodificarEvento(int indice) {
        return String.valueOf(getEventosDeal().charAt(indice));
    }

    /**
     * Regresa el valor del evento de timeout como un n&uacute;mero entero.
     *
     * @return int.
     */
    public int getTimeout() {
        return Integer.valueOf(getEventosDeal().substring(EV_IND_TIME_OUTS, EV_IND_TIME_OUTS + 2).
                trim()).intValue();
    }

    /**
     * Fija el indice del evento de timeout con el valor proporcionado.
     *
     * @param timeout El valor a asignar.
     */
    public void setTimeout(int timeout) {
        String to = (timeout < EV_IND_TIME_OUTS ? " " : "") + timeout;
        String prefix = getEventosDeal().substring(0, EV_IND_TIME_OUTS);
        setEventosDeal(prefix + to);
    }

    /**
     * Regresa la persona ligada a los rol 'TIT' del contratoSica.
     *
     * @return Persona.
     */
    public Persona getCliente() {
        return ContratoCliente.clienteParaContratoSica(getContratoSica());
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
        return DealHelper.isEvento(this, indice, status);
    }

    /**
     * Regresa true si el status del deal es 'PE' (Pendiente).
     *
     * @return boolean
     */
    public boolean isPendiente() {
        return !EV_NORMAL.equals(decodificarEvento(EV_IND_TIPO_CAPTURA));
    }


    /**
     * Regresa true si la sucursal del canal es distinta a null.
     *
     * @return boolean.
     */
    public boolean isDeSucursal() {
        return getCanalMesa().getCanal().getSucursal() != null;
    }

    /**
     * Regresa true si el deal fue capturado en horario nocturno.
     *
     * @return boolean.
     */
    public boolean isNocturno() {
        return EV_NOCTURNO.equals(decodificarEvento(EV_IND_TIPO_CAPTURA));
    }

    /**
     * Regresa true si hay una 'S' (SOLICITUD) en los eventos del deal.
     *
     * @return boolean.
     */
    public boolean haySolAut() {
        return getEventosDeal().indexOf('S') >= 0;
    }

    /**
     * Regresa true si hay una 'N' (NEGACION) en los eventos del deal.
     *
     * @return boolean.
     */
    public boolean hayNegAut() {
        return getEventosDeal().indexOf('N') >= 0;
    }

    /**
     * Regresa la suma de los importes de los detalles proporcionados.
     *
     * @param dets Los detalles del deal a sumar.
     * @return double.
     */
    private double calcularTotal(List dets) {
        double total = 0.0;
        for (Iterator it = dets.iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (! det.isCancelado()) {
                total += det.getImporte();
            }
        }
        return total;
    }

    /**
     * Regresa el monto en d&oacute;lares del deal.
     *
     * @return double.
     */
    public double getMontoUSD() {
        return DealHelper.getMontoUSD(this);
    }

    /**
     * Regresa el resultado de <code>calcularTotal(getRecibimos())</code>.
     *
     * @return double.
     */
    public double getTotalRecibimos() {
        return calcularTotal(getRecibimos());
    }

    /**
     * Regresa el resultado de <code>calcularTotal(getEntregamos())</code>.
     *
     * @return double.
     */
    public double getTotalEntregamos() {
        return calcularTotal(getEntregamos());
    }

    /**
     * Regresa la suma de comisiones cobradas en pesos de todos los detalles del deal.
     *
     * @return BigDecimal.
     */
    public double getTotalComisionMxn() {
        return DealHelper.getTotalComisionMxn(this);
    }

    /**
     * Regresa el conjunto de detalles de compra o de venta.
     *
     * @param recibimos true para compra, false para venta.
     * @param idDivisa El idDivisa a buscar.
     * @return List.
     */
    private List getDets(boolean recibimos, String idDivisa) {
        List dets = new ArrayList();
        for (Iterator it = getDetalles().iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
        if (det.isRecibimos() == recibimos) {
                if (idDivisa == null || det.getDivisa().getIdDivisa().equals(idDivisa)) {
                    dets.add(det);
                }
            }
        }
        return dets;
    }

    /**
     * Regresa el resultado de <code>getDets(true)</code>.
     *
     * @return Set.
     */
    public List getRecibimos() {
        return getDets(true, null);
    }

    /**
     * Regresa el resultado de <code>getDets(false)</code>.
     *
     * @return Set.
     */
    public List getEntregamos() {
        return getDets(false, null);
    }

    /**
     * Regresa el resultado de <code>getDets(true, Divisa.PESO)</code>.
     *
     * @return List.
     */
    public List getRecibimosEnPesos() {
        return getDets(true, Divisa.PESO);
    }

    /**
     * Regresa el resultado de <code>getDets(false, Divisa.PESO)</code>.
     *
     * @return List.
     */
    public List getEntregamosEnPesos() {
        return getDets(false, Divisa.PESO);
    }

    /**
     * Regresa la diferencia Mxn entre totalRecibimos y totalEntregamos + Total comisiones (Mxn).
     *
     * @return double.
     */
    public double getBalance() {
        double balance = Redondeador.redondear2Dec(getTotalRecibimos() -
                (getTotalEntregamos() + getTotalComisionMxn()));
        if (Math.abs(balance) < 0.01) {
            return 0.0;
        }
        return balance;
    }

    /**
     * Regresa una descripci&oacute;n significativa para el status del deal.
     *
     * @param statusDeal El status del deal.
     * @param reversado El valor del campo reversado.
     * @return String.
     */
    public static String getDescripcionStatus(String statusDeal, int reversado) {
        return DealHelper.getDescripcionStatus(statusDeal, reversado);
    }

    /**
     * Regresa una descripci&oacute;n significativa para el status del deal.
     *
     * @return String.
     */
    public String getDescripcionStatus() {
        return Deal.getDescripcionStatus(getStatusDeal(), getReversado());
    }

    /**
     * Si el tipoValor es 'CASH', regresa 'HOY'; en caso contrario regresa el mismo tipoValor.
     *
     * @return String.
     */
    public String getDescripcionTipoValor() {
        return DealHelper.getDescripcionTipoValor(this);
    }

    /**
     * Permite saber si el Deal tiene o no Factura.
     *
     * @return boolean
     */
    public boolean isConFactura() {
        if (factura == null) {
            return false;
        }
        return CON_FACTURA.equals(factura);
    }

    /**
     * Establece si el Deal tiene o no Factura.
     *
     * @param conFactura El valor a asignar.
     */
    public void setConFactura(boolean conFactura) {
        factura = conFactura ? CON_FACTURA : SIN_FACTURA;
    }

    /**
     * Permite saber si el deal es procesable o no.
     *
     * @param formasPagoLiq La Lista de Formas Pago Liq.
     * @return boolean
     */
    public boolean isProcesable(List formasPagoLiq) {
        return DealHelper.isProcesable(this, formasPagoLiq);
    }

    /**
     * Permite saber si el detalle del deal es procesable o no.
     *
     * @param det El Detalle del Deal
     * @param formasPagoLiq La Lista de Formas Pago Liq
     * @return boolean
     */
    public boolean isProcesableDetalle(DealDetalle det, List formasPagoLiq) {
        return DealHelper.isProcesableDetalle(det, formasPagoLiq);
    }

    /**
     * Regresa true si el detalle se encuentra en status Reversado o en proceso de reverso.
     *
     * @return boolean.
     */
    public boolean isReversadoProcesoReverso() {
        return PROC_REVERSO == getReversado()  || REVERSADO == getReversado();
    }

    /**
     * <p>Regresa true si el deal es cancelable.</p>
     * Para poder cancelarse:
     * <li>El deal debe haber sido capturado ese mismo d&iacute;a.</li>
     * <li>El status del deal debe ser Alta o Proces&aacute;ndose.</li>
     * <li>El status del deal no tiene solicitud de cancelaci&oacute;n pendiente.</li>
     * <li>El status del deal no tiene detalle de modificaci&oacute;n pendiente.</li>
     * <li>El deal debe existir en la base de datos idDeal > 0.</li>
     * <li>El deal debe contener por lo menos un detalle en alta o proces&aacute;ndose.</li>
     * <li>El deal no debe tener detalles liquidados.</li>
     *
     * @return boolean.
     */
    public boolean isCancelable() {
        return DealHelper.isCancelable(this);
    }

    /**
     * Regresa el detalle de deal que contiene el folio especificado.
     *
     * @param folioDetalle El folio a buscar.
     * @return DealDetalle.
     */
    public DealDetalle getDetalleConFolio(int folioDetalle) {
        return DealHelper.getDetalleConFolio(this, folioDetalle);
    }

    /**
     * Regresa true si hay relaci&oacute;n con un swap y el folioSwapInicio del swap es igual al
     * idDeal.
     *
     * @return boolean.
     */
    public boolean isInicioSwap() {
        return _swap != null && _swap.getFolioSwapInicio() != null &&
                idDeal == _swap.getFolioSwapInicio().intValue();
    }

    /**
     * Regresa true si hay relaci&oacute;n con un swap y el folioSwapInicio del swap es diferente al
     * idDeal.
     *
     * @return boolean.
     */
    public boolean isContraparteSwap() {
        int folioSwapInicio = _swap.getFolioSwapInicio() != null ?
                _swap.getFolioSwapInicio().intValue() : -1;
        return _swap != null && idDeal != folioSwapInicio;
    }

    /**
     * Regresa true si alguno de los detalles del deal tiene una autorizaci&oacute;n por horario
     * pendiente, y no es interbancario.
     *
     * @return boolean.
     * @see com.ixe.ods.sica.model.DealDetalle#tieneAutPendientesHorario().
     */
    public boolean tieneAutPendientesHorario() {
        if (isInterbancario()) {
            return false;
        }
        for (Iterator it = getDetalles().iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (det.tieneAutPendientesHorario()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Regresa true si alguno de los detalles del deal tiene una autorizaci&oacute;n por monto
     * pendiente, y no es interbancario..
     *
     * @return boolean.
     * @see com.ixe.ods.sica.model.DealDetalle#tieneAutPendientesMonto().
     */
    public boolean tieneAutPendientesMonto() {
        if (isInterbancario()) {
            return false;
        }
        for (Iterator it = getDetalles().iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (det.tieneAutPendientesMonto()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Regresa true si alguno de los detalles del deal tiene una autorizaci&oacute;n por
     * cancelaci&oacute;n pendiente, y no es interbancario.
     *
     * @return boolean.
     * @see com.ixe.ods.sica.model.DealDetalle#tieneSolCancPendiente().
     */
    public boolean tieneSolCancPendientesDetalles() {
        return DealHelper.tieneSolCancPendientesDetalles(this);
    }

    /**
     * Regresa true si alguno de los detalles del deal tiene una autorizaci&oacute;n por
     * modificaci&oacute;n pendiente.
     *
     * @return boolean.
     * @see com.ixe.ods.sica.model.DealDetalle#tieneSolModifPendiente().
     */
    public boolean tieneSolModifPendientesDetalles() {
        return DealHelper.tieneSolModifPendientesDetalles(this);
    }
    
    /**
     * Regresa el valor de seleccionado.
     *
     * @return boolean.
     */
    public boolean isSeleccionado() {
        return seleccionado;
    }

    /**
     * Establece el valor de seleccionado.
     *
     * @param seleccionado El valor a asignar.
     */
    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    /**
     * Regresa el valor de idDeal.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_DEAL"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_DEAL_SEQ"
     * @return int.
     */
    public int getIdDeal() {
        return idDeal;
    }

    /**
     * Fija el valor de idDeal.
     *
     * @param idDeal El valor a asignar.
     */
    public void setIdDeal(int idDeal) {
        this.idDeal = idDeal;
    }

    /**
     * Regresa el valor de acudirCon.
     *
     * @hibernate.property column="ACUDIR_CON"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     * @return String.
     */
    public String getAcudirCon() {
        return acudirCon;
    }

    /**
     * Establece el valor de acudirCon.
     *
     * @param acudirCon El valor a asignar.
     */
    public void setAcudirCon(String acudirCon) {
        this.acudirCon = acudirCon;
    }

    /**
     * Regresa el valor de cambioRfc.
     *
     * @hibernate.property column="CAMBIO_RFC"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     * @return String.
     */
    public String getCambioRfc() {
        return cambioRfc;
    }

    /**
     * Establece el valor de la propiedad cambioRfc.
     *
     * @param cambioRfc El valor a asignar.
     */
    public void setCambioRfc(String cambioRfc) {
        this.cambioRfc = cambioRfc;
    }

    /**
     * Regresa el valor de cambioEmail.
     *
     * @hibernate.property column="CAMBIO_EMAIL"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     * @return String.
     */
    public String getCambioEmail() {
        return cambioEmail;
    }

    /**
     * Establece el valor de la propiedad cambioEmail.
     *
     * @param cambioEmail El valor a asignar.
     */
    public void setCambioEmail(String cambioEmail) {
        this.cambioEmail = cambioEmail;
    }

    /**
     * Regresa el valor de compra.
     *
     * @hibernate.property column="COMPRA"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return boolean.
     */
    public boolean isCompra() {
        return compra;
    }

    /**
     * Fija el valor de compra.
     *
     * @param compra El valor a asignar.
     */
    public void setCompra(boolean compra) {
        this.compra = compra;
    }

    /**
     * Regresa el valor de emailFactura.
     *
     * @hibernate.property column="EMAIL_FACTURA"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     * @return String.
     */
    public String getEmailFactura() {
        return emailFactura;
    }

    /**
     * Establece el valor de emailFactura.
     *
     * @param emailFactura El valor a asignar.
     */
    public void setEmailFactura(String emailFactura) {
        this.emailFactura = emailFactura;
    }

    /**
     * Regresa el valor de emailFacturaOtro.
     *
     * @hibernate.property column="EMAIL_FACTURA_OTRO"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     * @return String.
     */
    public String getEmailFacturaOtro() {
        return emailFacturaOtro;
    }

    /**
     * Establece el valor de emailFacturaOtro.
     *
     * @param emailFacturaOtro El valor a asignar.
     */
    public void setEmailFacturaOtro(String emailFacturaOtro) {
        this.emailFacturaOtro = emailFacturaOtro;
    }

    /**
     * Regresa el valor de comprobante.
     *
     * @hibernate.property column="ENVIAR_AL_CLIENTE"
     * not-null="true"
     * @return String.
     */
    public String getComprobante() {
        return comprobante;
    }

    /**
     * Fija el valor de comprobante.
     *
     * @param comprobante El valor a asignar.
     */
    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    /**
     * Regresa el valor de emailsComprobantes.
     *
     * @hibernate.property column="EMAILS_COMPROBANTES"
     * not-null="false"
     * @return String.
     */
    public String getEmailsComprobantes() {
        return emailsComprobantes;
    }

    /**
     * Fija el valor de emailsComprobantes.
     *
     * @param emailsComprobantes El valor a asignar.
     */
    public void setEmailsComprobantes(String emailsComprobantes) {
        this.emailsComprobantes = emailsComprobantes;
    }

    /**
     * Regresa el valor de comprobante, como un boolean.
     */
    public boolean isEnviarAlCliente() {
        return "S".equals(comprobante);
    }

    /**
     * Fija el valor de enviarAlCliente.
     *
     * @param enviarAlCliente El valor a asignar.
     */
    public void setEnviarAlCliente(boolean enviarAlCliente) {
        setComprobante(enviarAlCliente ? "S" : "N");
    }

    /**
     * Regresa el valor de eventos.
     *
     * @hibernate.property column="EVENTOS_DEAL"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getEventosDeal() {
        return eventosDeal;
    }

    /**
     * Fija el valor de eventosDeal.
     *
     * @param eventosDeal El valor a asignar.
     */
    public void setEventosDeal(String eventosDeal) {
        this.eventosDeal = eventosDeal;
    }

    /**
     * Regresa el valor de factura.
     *
     * @hibernate.property column="FACTURA"
     * not-null="true"
     * @return String.
     */
    public String getFactura() {
        return factura;
    }

    /**
     * Fija el valor de factura.
     *
     * @param factura El valor a asignar.
     */
    public void setFactura(String factura) {
        this.factura = factura;
    }

    /**
     * Regresa el valor de facturaCaptura.
     *
     * @hibernate.property column="FECHA_CAPTURA"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getFechaCaptura() {
        return fechaCaptura;
    }

    /**
     * Fija el valor de fechaCaptura.
     *
     * @param fechaCaptura El valor a asignar.
     */
    public void setFechaCaptura(Date fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    /**
     * Regresa el valor de fechaLiquidacion.
     *
     * @hibernate.property column="FECHA_LIQUIDACION"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    /**
     * Fija el valor de fechaLiquidacion.
     *
     * @param fechaLiquidacion El valor a asignar.
     */
    public void setFechaLiquidacion(Date fechaLiquidacion) {
        this.fechaLiquidacion = fechaLiquidacion;
    }

    /**
     * Regresa el valor de factorRiesgo.
     *
     * @hibernate.property column="FACTOR_RIESGO"
     * not-null="true"
     * unique="false"
     * @return BigDecimal.
     */
    public BigDecimal getFactorRiesgo() {
        return factorRiesgo;
    }

    /**
     * Establece el valor de FactorRiesgo.
     *
     * @param factorRiesgo El valor a asignar.
     */
    public void setFactorRiesgo(BigDecimal factorRiesgo) {
        this.factorRiesgo = factorRiesgo;
    }

    /**
     * Regresa el valor de idLiquidacion.
     *
     * @hibernate.property column="ID_LIQUIDACION"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getIdLiquidacion() {
        return idLiquidacion;
    }

    /**
     * Fija el valor de idLiquidacion.
     *
     * @param idLiquidacion El valor a asignar.
     */
    public void setIdLiquidacion(Integer idLiquidacion) {
        this.idLiquidacion = idLiquidacion;
    }

    /**
     * Regresa el valor de mensajeria.
     *
     * @hibernate.property column="MENSAJERIA"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return boolean.
     */
    public boolean isMensajeria() {
        return mensajeria;
    }

    /**
     * Regresa el valor de montoEquivLc.
     *
     * @hibernate.property column="MONTO_EQUIV_LC"
     * not-null="false"
     * unique="false"
     * @return BigDecimal.
     */
    public BigDecimal getMontoEquivLc() {
        double mayor = getTotalRecibimos() > getTotalEntregamos() ?
                getTotalRecibimos() : getTotalEntregamos();
        if (montoEquivLc == null || montoEquivLc.doubleValue() < mayor) {
            montoEquivLc = BDUtils.generar2(mayor);
        }
        return montoEquivLc;
    }

    /**
     * Establece el valor de montoEquivLc.
     *
     * @param montoEquivLc El valor a asignar.
     */
    public void setMontoEquivLc(BigDecimal montoEquivLc) {
        this.montoEquivLc = montoEquivLc;
    }

    /**
     * Regresa true si el deal tiene algun detalle no cancelado que no sea pesos en compra y venta.
     *
     * @return boolean.
     */
    public boolean isNeteo() {
        boolean neteo = false;
        boolean neteoRecibimos = false;
        for (Iterator it = getDetalles().iterator(); it.hasNext();) {
            DealDetalle dt = (DealDetalle) it.next();
            if (!Divisa.PESO.equals(dt.getDivisa().getIdDivisa()) && dt.isRecibimos() &&
                    !dt.isCancelado()) {
                if (neteo) {
                    return true;
                }
                else {
                    neteoRecibimos = true;
                }
            }
            if (!Divisa.PESO.equals(dt.getDivisa().getIdDivisa()) && !dt.isRecibimos() &&
                    !dt.isCancelado()) {
                if (neteoRecibimos) {
                    return true;
                }
                else {
                    neteo = true;
                }
            }
        }
        return false;
    }

    /**
     * Fija el valor de mensajeria.
     *
     * @param mensajeria El valor a asignar.
     */
    public void setMensajeria(boolean mensajeria) {
        this.mensajeria = mensajeria;
    }

    /**
     * Regresa el valor de fechaValor.
     *
     * @hibernate.property column="TIPO_VALOR"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getTipoValor() {
        return tipoValor;
    }

    /**
     * Fija el valor de fechaValor.
     *
     * @param tipoValor El valor a asignar.
     */
    public void setTipoValor(String tipoValor) {
        this.tipoValor = tipoValor;
    }

    /**
     * Regresa el valor de tipoCambioCobUsdDiv.
     *
     * @hibernate.property column="TC_COB_USD_DIV"
     * not-null="false"
     * unique="false"
     * @return BigDecimal.
     */
    public BigDecimal getTipoCambioCobUsdDiv() {
        return tipoCambioCobUsdDiv;
    }

    /**
     * Establece el valor de tipoCambioCobUsdDiv.
     *
     * @param tipoCambioCobUsdDiv El valor a asignar.
     */
    public void setTipoCambioCobUsdDiv(BigDecimal tipoCambioCobUsdDiv) {
        this.tipoCambioCobUsdDiv = tipoCambioCobUsdDiv;
    }

    /**
     * Regresa el valor de tipoCambioCobMxnUsd.
     *
     * @hibernate.property column="TC_COB_MXN_USD"
     * not-null="false"
     * unique="false"
     * @return BigDecimal.
     */
    public BigDecimal getTipoCambioCobMxnUsd() {
        return tipoCambioCobMxnUsd;
    }

    /**
     * Establece el valor de tipoCambioCobMxnUsd.
     *
     * @param tipoCambioCobMxnUsd El valor a asignar.
     */
    public void setTipoCambioCobMxnUsd(BigDecimal tipoCambioCobMxnUsd) {
        this.tipoCambioCobMxnUsd = tipoCambioCobMxnUsd;
    }

    /**
     * Regresa el valor de nombreFactura.
     *
     * @hibernate.property column="NOMBRE_FACTURA"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getNombreFactura() {
        return nombreFactura;
    }

    /**
     * Fija el valor de nombreFactura.
     *
     * @param nombreFactura El valor a asignar.
     */
    public void setNombreFactura(String nombreFactura) {
        this.nombreFactura = nombreFactura;
    }
    
    /**
     * Regresa el valor de originalDe.
     *
     * @hibernate.property column="ORIGINAL_DE"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     * @return Integer.
     */
    public Integer getOriginalDe() {
        return originalDe;
    }

    /**
     * Establece el valor de la variable originalDe.
     *
     * @param originalDe El valor a asignar.
     */
    public void setOriginalDe(Integer originalDe) {
        this.originalDe = originalDe;
    }
    
    /**
     * Regresa el valor de contrarioDe.
    *
    * @hibernate.property column="CONTRARIO_DE"
    * not-null="false"
    * unique="false"
    * update="true"
    * insert="true"
    * @return Integer.
    */
   public Integer getContrarioDe() {
       return contrarioDe;
   }

   /**
    * Establece el valor de la variable contrarioDe.
    *
    * @param contrarioDe El valor a asignar.
    */
   public void setContrarioDe(Integer contrarioDe) {
       this.contrarioDe = contrarioDe;
   }

    /**
     * Regresa el valor de correccion.
     *
     * @return Integer.
     * @hibernate.property column="CORRECCION"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public Integer getCorreccion() {
        return correccion;
   }

    /**
     * Establece el valor de la variable correccion.
     *
     * @param correccion El valor a asignar.
     */
    public void setCorreccion(Integer correccion) {
        this.correccion = correccion;
    }

    /**
     * Regresa el valor de rfcFactura.
     *
     * @hibernate.property column="RFC_FACTURA"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getRfcFactura() {
        return rfcFactura;
    }

    /**
     * Fija el valor de rfcFactura.
     *
     * @param rfcFactura El valor a asignar.
     */
    public void setRfcFactura(String rfcFactura) {
        if (rfcFactura != null) {
            rfcFactura = rfcFactura.toUpperCase();
        }
        this.rfcFactura = rfcFactura;
    }

    /**
     * Regresa el valor de dirFactura.
     *
     * @hibernate.many-to-one column="ID_DIR_FACTURA"
     * cascade="none"
     * class="com.ixe.ods.bup.model.Direccion"
     * outer-join="auto"
     * unique="false"
     * @return Direccion.
     */
    public Direccion getDirFactura() {
        return dirFactura;
    }

    /**
     * Fija el valor de dirFactura.
     *
     * @param dirFactura El valor a asignar.
     */
    public void setDirFactura(Direccion dirFactura) {
        this.dirFactura = dirFactura;
    }

    /**
     * Regresa el valor de dirFactura.
     *
     * @hibernate.many-to-one column="ID_DIR_FACT_MENS"
     * cascade="none"
     * class="com.ixe.ods.bup.model.Direccion"
     * outer-join="auto"
     * unique="false"
     * @return Direccion.
     */
    public Direccion getDirFacturaMensajeria() {
        return dirFacturaMensajeria;
    }

    /**
     * Establece el valor de dirFacturaMensajeria.
     *
     * @param dirFacturaMensajeria El valor a asignar.
     */
    public void setDirFacturaMensajeria(Direccion dirFacturaMensajeria) {
        this.dirFacturaMensajeria = dirFacturaMensajeria;
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
     * Regresa el valor de pagoAnticipado.
     *
     * @hibernate.property column="PAGO_ANTICIPADO"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return boolean.
     */
    public boolean isPagoAnticipado() {
        return pagoAnticipado;
    }

    /**
     * Fija el valor de pagoAnticipado. Limpia la solicitud de
     * autorizaci&oacute;n de excedente en l&iacute;nea de cr&eacute;dito.
     *
     * @param pagoAnticipado El valor a asignar.
     */
    public void setPagoAnticipado(boolean pagoAnticipado) {
        this.pagoAnticipado = pagoAnticipado;
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
     * Regresa true si el deal es simple.
     *
     * @return boolean.
     */
    public boolean isSimple() {
        return TIPO_SIMPLE.equals(getTipoDeal());
    }

    /**
     * Regresa true si el deal es interbancario.
     *
     * @return boolean.
     */
    public boolean isInterbancario() {
        return TIPO_INTERBANCARIO.equals(getTipoDeal());
    }

    /**
     * Regresa el valor de tipoDeal.
     *
     * @hibernate.property column="SIMPLE"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getTipoDeal() {
        return tipoDeal;
    }

    /**
     * Fija el valor de tipoDeal.
     *
     * @param tipoDeal El valor a asignar.
     */
    public void setTipoDeal(String tipoDeal) {
        this.tipoDeal = tipoDeal;
    }

    /**
     * Regresa el valor de statusDeal.
     *
     * @hibernate.property column="STATUS_DEAL"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getStatusDeal() {
        return statusDeal;
    }

    /**
     * Fija el valor de statusDeal.
     *
     * @param statusDeal El valor a asignar.
     */
    public void setStatusDeal(String statusDeal) {
        this.statusDeal = statusDeal;
    }

    /**
     * Regresa el valor de tomaEnFirme.
     *
     * @hibernate.property column="TOMA_EN_FIRME"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return boolean.
     */
    public boolean isTomaEnFirme() {
        return tomaEnFirme;
    }

    /**
     * Fija el valor de tomaEnFirme.
     *
     * @param tomaEnFirme El valor a asignar.
     */
    public void setTomaEnFirme(boolean tomaEnFirme) {
        this.tomaEnFirme = tomaEnFirme;
    }

    /**
     * Regresa el valor de canalMesa.
     *
     * @hibernate.component class="com.ixe.ods.sica.model.CanalMesa"
     * @return CanalMesa.
     */
    public CanalMesa getCanalMesa() {
        return _canalMesa;
    }

    /**
     * Fija el valor de canalMesa.
     *
     * @param canalMesa El valor a asignar.
     */
    public void setCanalMesa(CanalMesa canalMesa) {
        _canalMesa = canalMesa;
    }

    /**
     * Regresa el valor de noCuentaIxe.
     *
     * @hibernate.property column="NO_CUENTA_IXE"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getNoCuentaIxe() {
        return noCuentaIxe;
    }

    /**
     * Establece el valor de noCuentaIxe.
     *
     * @param noCuentaIxe El valor a asignar.
     */
    public void setNoCuentaIxe(String noCuentaIxe) {
        this.noCuentaIxe = noCuentaIxe;
    }
    
    /**
     * Regresa el valor de la clave de referencia del Deal.
     * 
     * @hibernate.property column="CLAVE_REFERENCIA"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getClaveReferencia(){
    	return claveReferencia;
    }
    
    /**
     * Regresa el valor de contieneNeteo.
     *
     * @hibernate.property column="NETEO"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="false"
     * unique="false"
     * @return boolean.
     */
    public Boolean isContieneNeteo() {
        return contieneNeteo;
    }
    
    /**
     * Establece el valor de claveReferencia
     * 
     * @param claveReferencia El valor a asignar.
     */
    public void setClaveReferencia(String claveReferencia){
    	this.claveReferencia = claveReferencia;
    }
    
    /**
     * Fija el valor de contieneNeteo.
     *
     * @param contieneNeteo El valor a asignar.
     */
    public void setContieneNeteo(Boolean contieneNeteo) {
        this.contieneNeteo = contieneNeteo;
    }

    /**
     * Regresa el valor de broker.
     *
     * @hibernate.many-to-one column="ID_BROKER"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Broker"
     * outer-join="auto"
     * unique="false"
     * @return Broker.
     */
    public Broker getBroker() {
        return _broker;
    }

    /**
     * Fija el valor de Broker.
     *
     * @param broker El valor a asignar
     */
    public void setBroker(Broker broker) {
        _broker = broker;
    }

    /**
     * Regresa el valor de contratoSica.
     *
     * @hibernate.many-to-one column="NO_CUENTA"
     * cascade="none"
     * class="com.ixe.ods.bup.model.ContratoSica"
     * outer-join="auto"
     * unique="false"
     * @return ContratoSica.
     */
    public ContratoSica getContratoSica() {
        return _contratoSica;
    }

    /**
     * Fija el valor de contratoSica.
     *
     * @param contratoSica El valor a asignar.
     */
    public void setContratoSica(ContratoSica contratoSica) {
        _contratoSica = contratoSica;
    }

    /**
     * Regresa el valor de direccion.
     *
     * @hibernate.many-to-one column="ID_DIRECCION_MENSAJERIA"
     * cascade="none"
     * class="com.ixe.ods.bup.model.Direccion"
     * outer-join="auto"
     * unique="false"
     * @return Direccion.
     */
    public Direccion getDireccion() {
        return _direccion;
    }

    /**
     * Fija el valor de direccion.
     *
     * @param direccion El valor a asignar.
     */
    public void setDireccion(Direccion direccion) {
        _direccion = direccion;
    }

    /**
     * Regresa el valor de promotor.
     *
     * @hibernate.many-to-one column="ID_PROMOTOR"
     * cascade="none"
     * class="com.ixe.ods.bup.model.EmpleadoIxe"
     * outer-join="auto"
     * unique="false"
     * @return EmpleadoIxe.
     */
    public EmpleadoIxe getPromotor() {
        return _promotor;
    }

    /**
     * Fija el valor de promotor.
     *
     * @param promotor El valor a asignar.
     */
    public void setPromotor(EmpleadoIxe promotor) {
        _promotor = promotor;
    }

    /**
     * Regresa el valor de usuario.
     *
     * @hibernate.many-to-one column="ID_USUARIO"
     * cascade="none"
     * class="com.ixe.ods.seguridad.model.Usuario"
     * outer-join="auto"
     * unique="false"
     * @return IUsuario.
     */
    public IUsuario getUsuario() {
        return _usuario;
    }

    /**
     * Fija el valor de usuario.
     *
     * @param usuario El valor a asignar.
     */
    public void setUsuario(IUsuario usuario) {
        _usuario = usuario;
    }

    /**
     * Regresa el valor de detalles.
     *
     * @hibernate.bag inverse="true"
     * lazy="true"
     * cascade="none"
     * order-by="FOLIO_DETALLE"
     * @hibernate.collection-key column="ID_DEAL"
     * @hibernate.collection-one-to-many class="com.ixe.ods.sica.model.DealDetalle"
     * @return List.
     */
    public List getDetalles() {
        return _detalles;
    }

    /**
     * Fija el valor de detalles.
     *
     * @param detalles El valor a asignar.
     */
    public void setDetalles(List detalles) {
        _detalles = detalles;
    }

    /**
     * Regresa el valor de swap.
     *
     * @hibernate.many-to-one column="ID_FOLIO_SWAP"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Swap"
     * outer-join="auto"
     * unique="false"
     * @return Swap.
     */
    public Swap getSwap() {
        return _swap;
    }

    /**
     * Fija el valor de swap.
     *
     * @param swap El valor a asignar.
     */
    public void setSwap(Swap swap) {
        _swap = swap;
    }

    /**
     * Regresa el valor de fechaLimiteCapturaContrato.
     *
     * @hibernate.property column="FECHA_LIM_CAPT_CONT"
     * not-null="false"
     * unique="false"
     * @return Date.
     */
    public Date getFechaLimiteCapturaContrato() {
        return fechaLimiteCapturaContrato;
    }

    /**
     * Fija el valor de fechaLimiteCapturaContrato.
     *
     * @param fechaLimiteCapturaContrato El valor a asignar.
     */
    public void setFechaLimiteCapturaContrato(Date fechaLimiteCapturaContrato) {
        this.fechaLimiteCapturaContrato = fechaLimiteCapturaContrato;
    }

    /**
     * Regresa el Tipo de Contraparte que es un Cliente de un Deal.
     *
     * @return String	El Tipo de Contraparte.
     */
    public String getTipoContraparte() {
        return getCliente().getSectorEconomico().getSectorBanxico();
    }

    /**
     * Regresa el valor de version.
     *
     * @hibernate.version column="VERSION" name="version" access="property"
     * @return Integer.
     */
    public Integer getVersion() {
        if (version == null) {
            version = new Integer(0);
        }
        return version;
    }

    /**
     * Establece el valor de version.
     *
     * @param version El valor a asignar.
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Regresa el valor de liquidacionEspecial
     *
     * @return String.
     * @hibernate.property column="LIQUIDACION_ESPECIAL"
     * not-null="false"
     * unique="false"
     */
    public String getLiquidacionEspecial() {
        return liquidacionEspecial;
	}

	/**
	 * Fija el valor de liquidacionEspecial
	 *
	 * @param liquidacionEspecial El valor a Fijar
	 */
	public void setLiquidacionEspecial(String liquidacionEspecial) {
		this.liquidacionEspecial = liquidacionEspecial;
	}

	/**
     * Permite saber si el Deal se Liquid&oacute; Parcialmente de manera Anticipada.
     *
     * @hibernate.property column="LIQUIDACION_ANTICIPADA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getLiquidacionAnticipada() {
        return liquidacionAnticipada;
    }

    /**
     * Fija el valor de compra.
     *
     * @param liquidacionAnticipada El valor a asignar.
     */
    public void setLiquidacionAnticipada(String liquidacionAnticipada) {
    	this.liquidacionAnticipada = liquidacionAnticipada;
    }

    /**
     * Regresa true si la fecha de liquidaci&oacute;n del deal es posterior a la fecha actual del
     * sistema.
     *
     * @return boolean.
     */
    public boolean isLiquidableEnElFuturo() {
        return DateUtils.inicioDia().getTime()
                < DateUtils.inicioDia(getFechaLiquidacion()).getTime();
    }

    /**
     * Regresa el n&uacute;mero de detalles de divisa que no est&aacute;n cancelados.
     *
     * @return int.
     */
    public int getNumeroDetallesDivisaNoCancelados() {
        int noCancelados = 0;
        for (Iterator it = getDetalles().iterator(); it.hasNext();) {
            DealDetalle detalle = (DealDetalle) it.next();
            if (!detalle.isPesos()
                    && !DealDetalle.STATUS_DET_CANCELADO.equals(detalle.getStatusDetalleDeal())) {
                noCancelados++;
            }
        }
        return noCancelados;
    }

    /**
     * Regresa el valor de grupoTrabajo.
     * 
     * @hibernate.many-to-one column="id_grupo_trabajo"
     * cascade="none"
     * class="com.ixe.ods.sica.model.GrupoTrabajo"
     * outer-join="auto"
     * unique="false"
     * @return GrupoTrabajo.
	 */
	public GrupoTrabajo getGrupoTrabajo() {
		return grupoTrabajo;
	}

    /**
     * Establece el valor de grupoTrabajo.
     *
     * @param grupoTrabajo El valor a asignar.
     */
	public void setGrupoTrabajo(GrupoTrabajo grupoTrabajo) {
		this.grupoTrabajo = grupoTrabajo;
	}
    
    /**
     * Regresa el valor de tipoExcepcion.
     *
     * @hibernate.property column="TIPO_EXCEPCION"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getTipoExcepcion() {
        return tipoExcepcion;
    }

    /**
     * Fija el valor de tipoExcepcion.
     *
     * @param tipoExcepcion El valor a asignar.
     */
    public void setTipoExcepcion(String tipoExcepcion) {
        this.tipoExcepcion = tipoExcepcion;
    }
    
    /**
     * Regresa el valor de tipoZona.
     *
     * @hibernate.property column="TIPO_ZONA"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getTipoZona() {
        return tipoZona;
    }

    /**
     * Fija el valor de tipoZona.
     *
     * @param tipoZona El valor a asignar.
     */
    public void setTipoZona(String tipoZona) {
        this.tipoZona = tipoZona;
    }
    
    /**
     * Regresa el valor de esCliente.
     *
     * @hibernate.property column="ES_CLIENTE"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getEsCliente() {
    	return esCliente;
    }
    
    /**
     * Fija el valor de esCliente.
     *
     * @param esCliente El valor a asignar.
     */
    public void setEsCliente(String esCliente) {
    	this.esCliente = esCliente;
    }

	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof Deal)) {
            return false;
        }
        Deal castOther = (Deal) other;
        return new EqualsBuilder().append(this.getIdDeal(), castOther.getIdDeal()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdDeal()).toHashCode();
    }

    /**
     * Regresa true si el deal tiene una solicitud de autorizaci&oacute;n de modificaci&oacute;n
     * que est&aacute; pendiente.
     *
     * @return boolean.
     */
    public boolean tieneSolModifPendiente() {
        return isEvento(Deal.EV_IND_GRAL_MODIFICACION, new String[]{Deal.EV_SOLICITUD});
    }

    /**
     * Regresa true si el deal tiene una solicitud de sobreprecio en status pendiente.
     *
     * @return boolean.
     */
    public boolean tieneSolSobreprecioPendiente() {
        return !isInterbancario() &&
                isEvento(Deal.EV_IND_SOBREPRECIO, new String[]{Deal.EV_SOLICITUD});
    }

    /**
     * Regresa true si el deal tiene una solicitud de autorizaci&oacute;n de cancelaci&oacute;n
     * que est&aacute; pendiente. Si alguno de los detalles no est&aacute; cancelado y tiene
     * solicitud de cancelaci&oacute;n parcial pendiente, tambi&eacute;n regresa true.
     *
     * @return boolean.
     */
    public boolean tieneSolCancPendiente() {
        if (isEvento(Deal.EV_IND_GRAL_CANCELACION,
                new String[]{Deal.EV_SOLICITUD, Deal.EV_APROBACION_TESORERIA})) {
            return true;
        }
        else if (tieneSolCancPendientesDetalles()) {
            return true;
        }
        return false;
    }

    /**
     * Regresa true si este deal participa en un reverso como deal original.
     *
     * @return boolean.
     */
    public boolean isDealOriginal() {
        return getOriginalDe() != null && getOriginalDe().intValue() > 0;
    }

    /**
     * Regresa true si este deal participa en un reverso como deal de balanceo.
     *
     * @return boolean.
     */
    public boolean isDealBalanceo() {
        return getContrarioDe() != null && getContrarioDe().intValue() > 0;
    }

    /**
     * Regresa true si este deal participa en un reverso como deal de correcci&oacute;n.
     *
     * @return boolean.
     */
    public boolean isDealCorreccion() {
        return getCorreccion() != null && getCorreccion().intValue() > 0;
    }
    
    /**
     * Obtiene el sistema que capturo el deal con Tipo de Cambio Especial
     * 
     * @hibernate.many-to-one column="id_sistema"
     * cascade="none"
     * class="com.ixe.ods.sica.model.SistemaTce"
     * outer-join="auto"
     * unique="false"
     * 
     * @return
     */
    public SistemaTce getSistemaTce() {
		return sistemaTce;
	}

    /**
     * Establece el sistema que capturo el deal con TCE
     * 
     * @param sistemaTce
     */
	public void setSistemaTce(SistemaTce sistemaTce) {
		this.sistemaTce = sistemaTce;
	}

	/**
	 * Obtiene el campo {@link #especial}
	 * 
	 * @hibernate.property column="ESPECIAL"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
	 * 
	 * @return
	 */
	public String getEspecial() {
		return especial;
	}

	/**
	 * Establece el campo {@link #especial}
	 * @param especial
	 */
	public void setEspecial(String especial) {
		this.especial = especial;
	}

	/**
	 * Obtiene el campo {@link #estatusEspecial}
	 * 
	 * @hibernate.property column="ESTATUS_ESPECIAL"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
	 * 
	 * @return
	 */
	public String getEstatusEspecial() {
		return estatusEspecial;
	}

	/**
	 * Establece el campo {@link #estatusEspecial}
	 * 
	 * @param estatusEspecial
	 */
	public void setEstatusEspecial(String estatusEspecial) {
		this.estatusEspecial = estatusEspecial;
	}
	
	/**
	 * Regresa el valor de metodoPago.
	 * 
	 * @hibernate.property column="METODO_PAGO" not-null="false" unique="false"
	 * @return String.
	 */
	public String getMetodoPago() {
		return metodoPago;
	}

	/**
	 * Fija el valor de metodoPago.
	 * 
	 */
	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}

	/**
	 * Regresa el valor de cuentaPago.
	 * 
	 * @hibernate.property column="CUENTA_PAGO" not-null="false" unique="false"
	 * @return String.
	 */
	public String getCuentaPago() {
		return this.cuentaPago;
	}

	/**
	* Fija el valor de cuentaPago.
	* 
    */
	public void setCuentaPago(String cuentaPago) {
		this.cuentaPago = cuentaPago;
	}

	/**
	 * Indica si este deal tiene {@link #estatusEspecial} igual a {@link #STATUS_ESPECIAL_TC_AUTORIZADO}, es decir,
	 * ha sido capturado por la mesa de cambios con tipo de cambio especial
	 * 
	 * @return
	 */
	public boolean isAutorizadoTce() {
		return STATUS_ESPECIAL_TC_AUTORIZADO.equals(estatusEspecial);
	}
	
	/**
	 * Regresa el valor del CR o Sucursal de origen asignado al deal cuando este es procesado.
	 * 
	 * @hibernate.property column="CR"
	 * @return Integer.
	 */
	public Integer getCr() {
		return cr;
	}
	
	/**
	 * Fija el CR o sucursal de origen
	 * @param cr
	 */
	public void setCr(Integer cr) {
		this.cr = cr;
	}

	/**
     * El identificador del registro.
     */
    private int idDeal;

    /**
     * El nombre de la persona con la que se tiene que acudir para entregar la factura o el
     * comprobante.
     */
    private String acudirCon = "";

    /**
     * 'Sem&aacute;foro' para indicar el status de autorizaci&oacute;n por cambio en R. F. C.
     */
    private String cambioRfc = " ";

    /**
     * 'Sem&aacute;foro' para indicar el status de autorizaci&oacute;n por cambio en el correo
     * elect&oacute;nico. 
     */

    private String cambioEmail = " ";

    /**
     * Si el pivote del deal es compra o venta.
     */
    private boolean compra;

    /**
     * El conjunto de correos a los que se enviar&aacute; la factura electr&oacute;nica.
     */
    private String emailFactura;

    /**
     * Un correo electr&oacute;nico adicional para la factura electr&oacute;nica que no hab&iacute;a
     * sido validado al momento de la captura del deal.
     */
    private String emailFacturaOtro;

    /**
     * Si se env&iacute;a o no al cliente el deal por email.
     */
    private String comprobante = "-";

    /**
     * Una lista separada por punto y coma con los correos electr&oacute;nicos a los que se les
     * enviar&aacute; los comprobantes de operaci&oacute;n.
     */
    private String emailsComprobantes;

    /**
     * La m&aacute;scara de eventos del deal.
     */
    private String eventosDeal = "           0  ";

    /**
     * Si se debe facturar o no.
     */
    private String factura = "-";

    /**
     * La fecha en que se captura el deal.
     */
    private Date fechaCaptura = new Date();

    /**
     * La fecha en que se liquid&oacute; el deal.
     */
    private Date fechaLiquidacion;

    /**
     * El factor riesgo utilizado al hacer uso de la l&iacute;nea de cambios.
     */
    private BigDecimal factorRiesgo = new BigDecimal("0.00000000");

    /**
     * El n&uacute;mero de liquidaci&oacute;n de Tesorer&iacute;a.
     */
    private Integer idLiquidacion;

    /**
     * Si el cliente requiere o no servicio de mensajeria.
     */
    private boolean mensajeria;

    /**
     * El monto equivalente que us&oacute; la l&iacute;nea de cr&eacute;dito.
     */
    private BigDecimal montoEquivLc;

    /**
     * Si el cliente requiere o no pago anticipado en este detalle.
     */
    private boolean pagoAnticipado;

    /**
     * Propiedad no persistente, permite marcar el registro como seleccionado utilizando un
     * checkbox.
     */
    private boolean seleccionado;

    /**
     * C)ash | T)om | S)pot | + 48 hrs.
     */
    private String tipoValor;

    /**
     * Para deals interbancarios con cobertura en otra divisa, el tipo de cambio D&oacute;lar
     * Americano contra la divisa seleccionada.
     */
    private BigDecimal tipoCambioCobUsdDiv;

    /**
     * Para deals interbancarios con cobertura en otra divisa, el tipo de cambio Peso contra
     * D&oacute;lar Americano.
     */
    private BigDecimal tipoCambioCobMxnUsd;

    /**
     * Las observaciones.
     */
    private String observaciones = "";

    /**
     * Si se trata de un deal simple 'S' o complejo 'C' o interbancario 'i'.
     */
    private String tipoDeal;

    /**
     * El status del deal.
     */
    private String statusDeal = STATUS_DEAL_PROCESO_CAPTURA;

    /**
     * Si se toma en firme o no.
     */
    private boolean tomaEnFirme;

    /**
     * La propiedad de versi&oacute;n para optimistic locking.
     */
    private Integer version = new Integer(0);

    /**
     * El componente canalMesa.
     */
    private CanalMesa _canalMesa;

    /**
     * Relaci&oacute;n muchos-a-uno con Broker.
     */
    private Broker _broker;

    /**
     * Relaci&oacute;n muchos-a-uno con ContratoSica.
     */
    private ContratoSica _contratoSica;

    /**
     * Relaci&oacute;n muchos-a-uno con Direccion.
     */
    private Direccion _direccion;

    /**
     * Relaci&oacute; muchos-a-uno con EmpleadoIxe.
     */
    private EmpleadoIxe _promotor;

    /**
     * Relaci&oacute;n muchos-a-uno con Usuario.
     */
    private IUsuario _usuario;

    /**
     * Relaci&oacute;n uno-a-muchos con DealDetalle.
     */
    private List _detalles = new ArrayList();

    /**
     * El Nombre del Cliente para Facturaci&oacute;n.
     */
    private String nombreFactura;

    /**
     * El RFC del Cliente para Facturaci&oacute;n.
     */
    private String rfcFactura;

    /**
     *  El n&uacute;mero de deal que es el balanceo del reverso de &eacute;ste.
     */
    private Integer originalDe;

    /**
     * 0 No reversado, 1 en Proceso de Reverso, 2 Reversado. 
     */
    private int reversado;
    
    /**
     *  El n&uacute;mero de deal que es el balanceo del reverso de &eacute;ste.
     */
    private Integer contrarioDe;
    
    /**
     *  El n&uacute;mero de deal que es el balanceo del reverso de &eacute;ste.
     */
    private Integer correccion;

    /**
     * El n&uacute;mero de cuenta de Cheques del cliente en Ixe.
     */
    private String noCuentaIxe;
    
    /**
     * El n&uacute;mero de clave de referencia del Deal
     */
    private String claveReferencia;
    
    /**
     * Indica si el deal contiene detalles capturados mediante reglas de neteo.
     */
    private Boolean contieneNeteo;
    
    /**
     * Relaci&oacute;n muchos-a-uno con la Direcci&oacute;n Fiscal del Cliente para
     * Facturaci&oacute;n.
     */
    private Direccion dirFactura;

    /**
     * Relaci&oacute;n muchos-a-uno con la Direcci&oacute;n que se env&iacute;a la factura
     * electr&oacute;nica.
     */
    private Direccion dirFacturaMensajeria;

    /**
     * Relaci&oacute;n muchos-a-uno con la tabla Swap.
     */
    private Swap _swap;

    /**
	 * Relaci&oacute;n mucho-a-uno con la tabla GrupoTrabajo. 
	 */
	private GrupoTrabajo grupoTrabajo;
	
    /**
     * La fecha l&iacute;mite de captura del contrato SICA del deal.
     */
    private Date fechaLimiteCapturaContrato;

    /**
     * Para indicar si el Deal, cuando es Interbancario, lleva Liquidaci&oacute;n
     * Especial VISA o TESORERIA.
     */
    private String liquidacionEspecial;

    /**
     * Para indicar si un Deal se Liquida Parcialmente de manera Anticipada.
     */
    private String liquidacionAnticipada = "0";
    
    /**
     * Para indicar el tipo de excepci&oacute;n utilizado.
     */
    private String tipoExcepcion;
    /**
     * Para indicar el tipo de zona utilizado.
     */
    private String tipoZona;
    
    /**
     * Para indicar si lo opero un cliente o un usuario.
     */
    private String esCliente;
    
    /**
     * Relacion muchos-a-uno con la tabla {@link SistemaTce}
     */
    private SistemaTce sistemaTce;
    
    /**
     * Indica si se trata de una deal con Tipo de Cambio Especial
     */
    private String especial;
    
    /**
     * Estatus de un deal de tipo de cambio especial, toma el valor {@link #STATUS_ESPECIAL_TC_AUTORIZADO} si es capturado
     * por la mesa de cambios
     */
    private String estatusEspecial;
    
	/**
	 * M￩todo de pago utilizado al realizar la captura del Deal.
	 */
	private String metodoPago;

	/**
	 * Cuenta de pago asignada al momento de realizar la captura del Deal
	 */
	private String cuentaPago;
	
	private Integer cr;

	/**
	 * Status de deal con tipo de cambio especial 'TC Autorizado', capturado por
	 * la mesa, este valor se asigna solo al campo {@link #estatusEspecial}
	 */


 	/**
    * Constante para indicar que el deal no se ha iniciado.
    */
    public static final int DEAL_SIN_INICIALIZAR = 0;

	
    /**
     * Status de deal con tipo de cambio especial 'TC Autorizado', capturado por la mesa, este valor se asigna solo al campo {@link #estatusEspecial}
     */
    public static final String STATUS_ESPECIAL_TC_AUTORIZADO = "TA";
    
    /**
     * Status de deal con tipo de cambio especial, cuando un promotor acepta el deal, este valor se asigna solo al campo {@link #estatusEspecial}
     */
    public static final String STATUS_ESPECIAL_TC_TOMADO = "TT";

    /**
     * Constante para el status 'En proceso de captura'.
     */
    public static final String STATUS_DEAL_PROCESO_CAPTURA = "AL";

    /**
     * Constante para el status de deal completo (se ha iniciado el BPM).
     */
    public static final String STATUS_DEAL_CAPTURADO = "CO";

    /**
     * Constante para el status de 'Parcialmente Cobrado o Pagado'.
     */
    public static final String STATUS_DEAL_PARCIAL_PAGADO_LIQ = "PL";

    /**
     * Constante para el status de 'Totalmente recibido'.
     */
    public static final String STATUS_DEAL_TOTALMENTE_PAGADO = "TP";

    /**
     * Constante para el status de 'Totalmente entregado'.
     */
    public static final String STATUS_DEAL_TOTALMENTE_LIQUIDADO = "TL";

    /**
     * Constante para el status de 'Cancelado'.
     */
    public static final String STATUS_DEAL_CANCELADO = "CA";

    /**
     * Constante para el evento con estado Normal.
     */
    public static final String EV_NORMAL = " ";

    /**
     * Constante para el evento con estado Indeterminado.
     */
    public static final String EV_NO_DETERMINADO = "?";

    /**
     * Constante para el evento con estado Solicitud de autorizaci&oacute;n.
     */
    public static final String EV_SOLICITUD = "S";

    /**
     * Constante para el evento con estado Solicitud de autorizaci&oacute;n aprobada.
     */
    public static final String EV_APROBACION = "A";

    /**
     * Constante para el evento con estado Solicitud de autorizaci&oacute;n no aprobada.
     */
    public static final String EV_NEGACION = "N";

    /**
     * Constante para el evento con estado Cancelado.
     */
    public static final String EV_CANCELACION = "C";

    /**
     * Constante para el evento con estado Enviado (Banxico).
     */
    public static final String EV_ENVIAR = "E";

    /**
     * Constante para el evento con estado Posici&oacute;n afectada.
     */
    public static final String EV_POSICION = "P";

    /**
     * Constante para el evento con estado Error en env&iacute;o a Banxico.
     */
    public static final String EV_PROB_COM_BANXICO = "X";

    /**
     * Constante para el evento con estado Solicitud de aprobaci&oacute;n otorgada por
     * Tesorer&iacute;a.
     */
    public static final String EV_APROBACION_TESORERIA = "L";

    /**
     * Constante para el evento con estado Solicitud de aprobaci&oacute;n denegada por
     * Tesorer&iacute;a.
     */
    public static final String EV_NEGACION_TESORERIA = "M";

    /**
     * Constante para el evento con estado Uso de la l&iacute;nea de cr&eacute;dito.
     */
    public static final String EV_USO = "U";

    /**
     * Constante para el evento con tipo de captura vespertina.
     */
    public static final String EV_VESPERTINO = "V";

    /**
     * Constante para el evento con tipo de captura nocturna.
     */
    public static final String EV_NOCTURNO = "T";

    /**
     * Constante para el &iacute;ndice de documentaci&oacute;n (deals normales).
     */
    public static final int EV_IND_DOCUMENTACION = 0;

    /**
     * Constante para el &iacute;ndice de plantillas
     */
    public static final int EV_IND_PLANTILLA = 3;

    /**
     * Constante para el &iacute;ndice de tipo de captura, de acuerdo al horario.
     */
    public static final int EV_IND_TIPO_CAPTURA = 8;

    /**
     * Constante para el &iacute;ndice de solicitud de sobreprecio.
     */
    public static final int EV_IND_SOBREPRECIO = 9;

    /**
     * Constante para el &iacute;ndice de n&uacute;mero de timeouts ocurridos.
     */
    public static final int EV_IND_TIME_OUTS = 10;

    /**
     * Constante para el &iacute;ndice de documentaci&oacute;n (deals interbancarios).
     */
    public static final int EV_IND_INT_DOCUMENTACION = 0;

    /**
     * Constante para el &iacute;ndice de plantillas
     */
    public static final int EV_IND_INT_PLANTILLA = 3;
    /**
     * Constante para el &iacute;ndice de l&iacute;neas de operaci&oacute;n.
     */
    public static final int EV_IND_INT_LINEA_OPERACION = 7;

    /**
     * Constante para el &iacute;ndice de Asignaci&oacute;n de SWAP.
     */
    public static final int EV_IND_INT_ASIGN_SWAP = 8;

    /**
     * Constante para el &iacute;ndice de solicitud de modificaci&oacute;n.
     */
    public static final int EV_IND_GRAL_MODIFICACION = 1;

    /**
     * Constante para el &iacute;ndice de solicitud de cancelaci&oacute;n.
     */
    public static final int EV_IND_GRAL_CANCELACION = 2;

    /**
     * Constante para el &iacute;ndice de env&iacute;o de compras a banxico.
     */
    public static final int EV_IND_GRAL_BANXICO_C = 4;

    /**
     * Constante para el &iacute;ndice de env&iacute;o de ventas a banxico.
     */
    public static final int EV_IND_GRAL_BANXICO_V = 5;

    /**
     * Constante para el &iacute;ndice de pago anticipado.
     */
    public static final int EV_IND_GRAL_PAG_ANT = 6;

    /**
     * Constante Deal Sin Factura.
     */
    public static final String SIN_FACTURA = "N";

    /**
     * Constante Deal Con Factura.
     */
    public static final String CON_FACTURA = "C";

    /**
     * Constante Deal sin Comprobante.
     */
    public static final String SIN_COMPROBANTE = "N";

    /**
     * Constante Deal con Comprobante.
     */
    public static final String CON_COMPROBANTE = "S";

    /**
     * Constante Tipo Deal Simple.
     */
    public static final String TIPO_SIMPLE = "S";

    /**
     * Constante Tipo Deal Complejo.
     */
    public static final String TIPO_COMPLEJO = "C";

    /**
     * Constante Tipo Deal Interbancario.
     */
    public static final String TIPO_INTERBANCARIO = "I";

    /**
     * Constante para No Reversado.
     */
    public static final int NO_REVERSADO = 0;

    /**
     * Constante para En Proceso de Reverso.
     */
    public static final int PROC_REVERSO = 1;

    /**
     * Constante para Reversado.
     */
    public static final int REVERSADO = 2;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -1925885741834519248L;
}
