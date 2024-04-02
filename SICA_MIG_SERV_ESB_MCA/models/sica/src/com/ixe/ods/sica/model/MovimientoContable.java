/*
 * $Id: MovimientoContable.java,v 1.16 2008/08/11 20:25:09 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Clase persistente para la tabla SC_MOVIMIENTO_CONTABLE.
 *
 * @author Edgar Leija, Javier Cordova
 * @version $Revision: 1.16 $ $Date: 2008/08/11 20:25:09 $
 * @hibernate.class table="SC_MOVIMIENTO_CONTABLE"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.MovimientoContable"
 * dynamic-update="true"
 * @hibernate.query name="findMovimientoPorFaseYFecha"
 * query="FROM MovimientoContable AS mc INNER JOIN FETCH mc.movimientoContableDetalles det
 * INNER JOIN FETCH mc.divisa INNER JOIN FETCH det.divisa WHERE mc.statusProceso = '0'
 * AND mc.faseContable = ? AND mc.fechaCreacion BETWEEN ? AND ?"
 * @hibernate.query name="findMovimientoPorFaseIdDeal"
 * query="FROM MovimientoContable AS mc WHERE mc.statusProceso = '0'
 * AND mc.faseContable = ? AND mc.idDeal = ? AND mc.fechaCreacion < ? "
 */
public class MovimientoContable implements Serializable {

    /**
     * Constructor por default.
     */
    public MovimientoContable() {
        super();
    }

    /**
     * Constructor que recibe un MovimientoContable y DealDetalle.reemplazadoPorA e inicializa todas
     * sus variables.
     *
     * @param mov             MovimientoContable
     * @param reemplazadoPorA DealDetalle.reemplazadoPorA
     */
    public MovimientoContable(MovimientoContable mov, int reemplazadoPorA) {
        super();
        setIdDeal(mov.getIdDeal());
        setCliente(mov.getCliente() != null ? mov.getCliente() : null);
        setFechaValor(mov.getFechaValor());
        setFaseContable(mov.getFaseContable());
        setTipoFechaValor(mov.getTipoFechaValor());
        setMnemonico(mov.getMnemonico() != null ? mov.getMnemonico() : null);
        setTipoDeal(mov.getTipoDeal());
        setTipoOperacion(mov.getTipoOperacion());
        setUsuario(mov.getUsuario());
        setSucursalOrigen(mov.getSucursalOrigen());
        setStatusActual(mov.getStatusActual());
        setStatusAnterior(mov.getStatusAnterior());
        setStatusProceso(mov.getStatusProceso());
        setFechaCreacion(mov.getFechaCreacion());
        setFechaProcesamiento(mov.getFechaProcesamiento() != null ?
                mov.getFechaProcesamiento() : null);
        setDivisa(mov.getDivisa());
        setFolioDetalle(mov.getFolioDetalle());
        setLiquidacionEspecial(mov.getLiquidacionEspecial() != null ?
                mov.getLiquidacionEspecial() : null);
        setIdDealPosicion(reemplazadoPorA);
        setTipoCambio(mov.getTipoCambio());
    }

    /**
     * Regresa el valor de idPoliza.
     *
     * @return String.
     * @hibernate.id generator-class="uuid.hex"
     * column="ID_MOVIMIENTO_CONTABLE"
     * unsaved-value="null"
     */
    public String getIdMovimientoContable() {
        return idMovimientoContable;
    }

    /**
     * Fija el valor de idMovimientoContable.
     *
     * @param idMovimientoContable El valor a asignar.
     */
    public void setIdMovimientoContable(String idMovimientoContable) {
        this.idMovimientoContable = idMovimientoContable;
    }

    /**
     * Regresa el valor de movimiento Contable detalles.
     *
     * @return List.
     * @hibernate.bag inverse="true"
     * lazy="true"
     * cascade="none"
     * @hibernate.collection-key column="ID_MOVIMIENTO_CONTABLE"
     * @hibernate.collection-one-to-many class="com.ixe.ods.sica.model.MovimientoContableDetalle"
     */
    public List getMovimientoContableDetalles() {
        return _movimientoContableDetalle;
    }

    /**
     * Fija el valor de Movimiento Contable Detalles.
     *
     * @param movimientoContableDetalle El valor a asignar.
     */
    public void setMovimientoContableDetalles(List movimientoContableDetalle) {
        _movimientoContableDetalle = movimientoContableDetalle;
    }

    /**
     * Regresa el valor de idDeal.
     *
     * @return int
     * @hibernate.property column="ID_DEAL"
     * not-null="true"
     * unique="false"
     */
    public int getIdDeal() {
        return idDeal;
    }

    /**
     * Fija el valor de idDeal
     *
     * @param idDeal El valor a idDeal
     */
    public void setIdDeal(int idDeal) {
        this.idDeal = idDeal;
    }

    /**
     * Fija el valor de referencia.
     *
     * @param idDealPosicion El valor a asignar.
     */
    public void setIdDealPosicion(int idDealPosicion) {
        this.idDealPosicion = idDealPosicion;
    }

    /**
     * Regresa el valor de idDealPosicion
     *
     * @return int.
     * @hibernate.property column="ID_DEAL_POSICION"
     * not-null="true"
     * unique="false"
     */
    public int getIdDealPosicion() {
        return idDealPosicion;
    }

    /**
     * Regresa el valor de folioDetalle.
     *
     * @return int
     * @hibernate.property column="FOLIO_DETALLE"
     * not-null="false"
     * unique="false"
     */
    public int getFolioDetalle() {
        return folioDetalle;
    }

    /**
     * Fija el valor de folioDetalle
     *
     * @param folioDetalle El valor a folioDetalle
     */
    public void setFolioDetalle(int folioDetalle) {
        this.folioDetalle = folioDetalle;
    }

    /**
     * Regresa el valor de cliente
     *
     * @return String.
     * @hibernate.property column="CLIENTE"
     * not-null="false"
     * unique="false"
     */
    public String getCliente() {
        return cliente;
    }

    /**
     * Fija el valor de cliente
     *
     * @param cliente El valor a cliente
     */
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    /**
     * Regresa el valor de fechaValor.
     *
     * @return Date.
     * @hibernate.property column="FECHA_VALOR"
     * not-null="true"
     * unique="false"
     */
    public Date getFechaValor() {
        return fechaValor;
    }

    /**
     * Fija el valor de fechaValor
     *
     * @param fechaValor El valor a fechaValor
     */
    public void setFechaValor(Date fechaValor) {
        this.fechaValor = fechaValor;
    }

    /**
     * Regresa el valor de divisa.
     *
     * @return Divisa.
     * @hibernate.many-to-one column="ID_DIVISA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Divisa"
     * outer-join="auto"
     * unique="false"
     */
    public Divisa getDivisa() {
        return _divisa;
    }

    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(Divisa divisa) {
        _divisa = divisa;
    }


    /**
     * Regresa el valor de fase
     *
     * @return String.
     * @hibernate.property column="FASE_CONTABLE"
     * not-null="true"
     * unique="false"
     */
    public String getFaseContable() {
        return faseContable;
    }

    /**
     * Fija el valor de faseContable
     *
     * @param faseContable El valor a asignar
     */
    public void setFaseContable(String faseContable) {
        this.faseContable = faseContable;
    }

    /**
     * Regresa el valor de tipoFechaValor
     *
     * @return String.
     * @hibernate.property column="TIPO_FECHA_VALOR"
     * not-null="true"
     * unique="false"
     */

    public String getTipoFechaValor() {
        return tipoFechaValor;
    }

    /**
     * Fija el valor de tipoFechaValor
     *
     * @param tipoFechaValor El valor a tipoFechaValor
     */
    public void setTipoFechaValor(String tipoFechaValor) {
        this.tipoFechaValor = tipoFechaValor;
    }

    /**
     * Regresa el valor de mnemonico.
     *
     * @return String.
     * @hibernate.property column="MNEMONICO"
     * not-null="false"
     * unique="false"
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
     * Regresa el valor de TipoDeal
     *
     * @return String.
     * @hibernate.property column="TIPO_DEAL"
     * not-null="true"
     * unique="false"
     */
    public String getTipoDeal() {
        return tipoDeal;
    }

    /**
     * Fija el valor de TipoDeal
     *
     * @param tipoDeal El valor a TipoDeal
     */
    public void setTipoDeal(String tipoDeal) {
        this.tipoDeal = tipoDeal;
    }

    /**
     * Regresa el valor de Tipo	Operacion
     *
     * @return String.
     * @hibernate.property column="TIPO_OPERACION"
     * not-null="true"
     * unique="false"
     */
    public String getTipoOperacion() {
        return tipoOperacion;
    }

    /**
     * Fija el valor de TipoOperacion
     *
     * @param tipoOperacion El valor a TipoOperacion
     */
    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    /**
     * Regresa el valor de usuario
     *
     * @return int
     * @hibernate.property column="ID_USUARIO"
     * not-null="true"
     * unique="false"
     */
    public int getUsuario() {
        return usuario;
    }

    /**
     * Fija el valor de usuario.
     *
     * @param usuario El valor a asignar.
     */
    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    /**
     * Regresa el valor de sucursalOrigen
     *
     * @return String.
     * @hibernate.property column="SUCURSAL_ORIGEN"
     * not-null="true"
     * unique="false"
     */
    public String getSucursalOrigen() {
        return sucursalOrigen;
    }

    /**
     * Fijar el valor de sucursalOrigen
     *
     * @param sucursalOrigen Valor a asignar
     */
    public void setSucursalOrigen(String sucursalOrigen) {
        this.sucursalOrigen = sucursalOrigen;
    }

    /**
     * Regresa el valor de statusAct
     *
     * @return String.
     * @hibernate.property column="STATUS_ACTUAL"
     * not-null="true"
     * unique="false"
     */
    public String getStatusActual() {
        return statusActual;
    }

    /**
     * Fija el valor de statusActual
     *
     * @param statusActual El valor a statusActual
     */
    public void setStatusActual(String statusActual) {
        this.statusActual = statusActual;
    }

    /**
     * Regresa el valor de statusAnterior
     *
     * @return String.
     * @hibernate.property column="STATUS_ANTERIOR"
     * not-null="true"
     * unique="false"
     */
    public String getStatusAnterior() {
        return statusAnterior;
    }

    /**
     * Fija el valor de statusAnt
     *
     * @param statusAnterior El valor a statusAnterior
     */
    public void setStatusAnterior(String statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    /**
     * Regresa el valor de tipoCambio
     *
     * @return double
     * @hibernate.property column="TIPO_CAMBIO"
     * not-null="true"
     * unique="false"
     */
    public double getTipoCambio() {
        return tipoCambio;
    }

    /**
     * Fijar el valor de tipoCambio
     *
     * @param tipoCambio Valor a asignar
     */
    public void setTipoCambio(double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    /**
     * Regresa el valor de statusProceso
     *
     * @return String.
     * @hibernate.property column="STATUS_PROCESO"
     * not-null="true"
     * unique="false"
     */
    public String getStatusProceso() {
        return statusProceso;
    }

    /**
     * Fija el valor de statusProceso
     *
     * @param statusProceso El valor a statusProceso
     */
    public void setStatusProceso(String statusProceso) {
        this.statusProceso = statusProceso;
    }

    /**
     * Regresa el valor de fechaCreacion
     *
     * @return Date.
     * @hibernate.property column="FECHA_CREACION"
     * not-null="true"
     * unique="false"
     */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Fija el valor de fechaCreacion
     *
     * @param fechaCreacion El valor a fechaCreacion
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Regresa el valor de fechaProcesamiento.
     *
     * @return Date.
     * @hibernate.property column="FECHA_PROCESAMIENTO"
     * not-null="false"
     * unique="false"
     */
    public Date getFechaProcesamiento() {
        return fechaProcesamiento;
    }

    /**
     * Fija el valor de fechaProcesamiento
     *
     * @param fechaProcesamiento El valor a fechaProcesamiento
     */
    public void setFechaProcesamiento(Date fechaProcesamiento) {
        this.fechaProcesamiento = fechaProcesamiento;
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
     * Regresa el valor de claveReferencia.
     *
     * @return String.
     * @hibernate.property column="CLAVE_REFERENCIA"
     * not-null="false"
     * unique="false"
     */
    public String getClaveReferencia() {
        return claveReferencia;
    }

    /**
     * Establece el valor de claveReferencia.
     *
     * @param claveReferencia El valor a asignar.
     */
    public void setClaveReferencia(String claveReferencia) {
        this.claveReferencia = claveReferencia;
    }
    
    /**
     * Concatena en forma de cadenas las propiedades del Deal Detalle.
     *
     * @return El Objeto como cadena.
     */
    public String toString() {
        return new ToStringBuilder(this).append("idMovimientoContable",
                idMovimientoContable).append("idDeal", idDeal).
                append("idDealPosicion", idDealPosicion).
                append("fechaValor", fechaValor).
                append("faseContable", faseContable).
                append("tipoFechaValor", tipoFechaValor).
                append("mnemonico", mnemonico).append("tipoDeal", tipoDeal).
                append("tipoOperacion", tipoOperacion).
                append("usuario", usuario).
                append("sucursalOrigen", sucursalOrigen).
                append("tipoCambio", tipoCambio).
                append("statusProceso", statusProceso).
                append("fechaCreacion", fechaCreacion).
                append("fechaProcesamiento", fechaProcesamiento).
                append("divisa", _divisa).
                append("folioDetalle", folioDetalle).toString();
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @return boolean.
     * @see java.lang.Object#equals (java.lang.Object) .
     */
    public boolean equals(Object other) {
        if (!(other instanceof MovimientoContable)) {
            return false;
        }
        MovimientoContable castOther = (MovimientoContable) other;
        return new EqualsBuilder().append(this.getIdMovimientoContable(), castOther.
                getIdMovimientoContable()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @return <code>int</code>
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdMovimientoContable()).toHashCode();
    }

    /**
     * Identificador del Movimiento Contable.
     */
    private String idMovimientoContable;

    /**
     * Identificador del idDeal
     */
    private int idDeal;

    /**
     * El idDealPosicion del Detalle de Deal o de la tabla SC_RECO_UTILIDAD.
     */
    private int idDealPosicion;

    /**
     * Nombre del Cliente
     */
    private String cliente;

    /**
     * Fecha Valor del Movimiento
     */
    private Date fechaValor;

    /**
     * Fase Contable a la que pertenece: P (Pactacion), U (Utilidades), L2A (Pago en Tiempo),
     * L2B (Deudores-Acreedores), L3 (Pago Anticipado), L4 (Pago Tardio)
     */
    private String faseContable;

    /**
     * Tipo Valor del Deal: CASH, TOM, SPOT o VFUT
     */
    private String tipoFechaValor;

    /**
     * Descripci&oacute;n del Mnemonico
     */
    private String mnemonico;

    /**
     * Descripci&oacute;n del tipo de Deal: Normal, Interbancario, Neteo, Utilidades, etc.
     */
    private String tipoDeal;

    /**
     * Descripci&oacute;n del tipo de Operaci&oacute;n: Compra o Venta
     */
    private String tipoOperacion;

    /**
     * Identificador del Usuario que capturo el deal.
     */
    private int usuario;

    /**
     * Descripci&oacute;n de la sucursal origen del usuario que captur&oacute; el deal
     */
    private String sucursalOrigen;

    /**
     * Descripci&oacute;n del Status actual del movimiento
     */
    private String statusActual;

    /**
     * Descripci&oacute;n del Status del que proviene el movimiento.
     */
    private String statusAnterior;

    /**
     * El tipo de Cambio Pactado del Detalle de Deal
     */
    private double tipoCambio;

    /**
     * Descripci&oacute;n del Status que indica si ya se contabiliz&oacute; el detalle.
     */
    private String statusProceso;

    /**
     * Fecha de Creaci&oacute;n del Registro
     */
    private Date fechaCreacion;

    /**
     * Fecha de Proceso del Registro
     */
    private Date fechaProcesamiento;

    /**
     * Relaci&oacute;n muchos-a-uno con Divisa.
     */
    private Divisa _divisa;

    /**
     * Descriptor del Folio al que pertenece el Detalle
     */
    private int folioDetalle;

    /**
     * Relaci&oacute;n a los detalles de los Movimientos.
     */
    private List _movimientoContableDetalle = new ArrayList();

    /**
     * Describe si hay una liquidaci&oacute;n especial: TESORERIA o VISA.
     */
    private String liquidacionEspecial;

    /**
     * La clave para dep&oacute;sitos referenciados.
     */
    private String claveReferencia;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 858246884864980948L;
}