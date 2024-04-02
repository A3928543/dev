/*
 * $Id: PosicionLog.java,v 1.16.72.1 2014/10/29 16:38:37 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>De acuerdo a la definici&oacute;n de la actualizaci&oacute;n de la
 * Posici&oacute;n, para efectos de optimizaci&oacute;n en el proceso continuo
 * de actualizaci&oacute;n de la Posici&oacute;n debido a las altas y
 * cancelaciones de los deals, en lugar de realizar operaciones de "update" en
 * la base de datos sobre los totales y poder provocar situaciones de bloqueo de
 * registros y posibles "deadlocks", se cre&oacute; esta tabla SC_POSICION_LOG
 * donde se registran todas las modificaciones que pudo tener la Posici&oacute;n
 * y as&iacute; únicamente realizar operaciones de inserción.</p>
 *
 * <p>Posteriormente y de acuerdo al esquema de actualizaci&oacute;n explicado
 * anteriormente, los totales de la Posici&oacute;n definidos en SC_POSICION y
 * SC_POSICION_DETALLE respectivamente ser&oacute;n actualizados para cuando se
 * desee consultar la Posici&oacute;n.</p>
 *
 * <p>Adicionalmente dicha tabla sirve de contingencia para casos donde se desee
 * re-crear la Posici&oacute;n desde el inicio del d&iacute;a, como funciona
 * como auditoría para cuando la Mesa de Cambios desee consultar el detalle de
 * sus operaciones.</p>
 *
 * @hibernate.class table="SC_POSICION_LOG"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.PosicionLog"
 * dynamic-update="true"
 *
 * @hibernate.query name="findPosicionesLogByIdMesaAndIdDivisaMayoresA"
 * query="FROM PosicionLog AS pl WHERE pl.canalMesa.mesaCambio.idMesaCambio = ? AND
 * pl.divisa.idDivisa = ? AND pl.idPosicionLog > ? ORDER BY pl.idPosicionLog"
 *
 * @hibernate.query name="findTipoValorByIdDealPosicion"
 * query="FROM PosicionLog AS pl WHERE pl.dealPosicion.idDealPosicion = ?"
 * 
 * @hibernate.query name="findPosicionesLogByFecha"
 * query="FROM PosicionLog AS pl WHERE to_char(pl.fecha, 'dd/MM/yyyy') = ? ORDER BY pl.idPosicionLog"
 * 
 * @hibernate.query name="findPosicionesLogMayoresA"
 * query="FROM PosicionLog AS pl WHERE pl.idPosicionLog > ? AND (NOMBRE_CLIENTE != 'IXE BANCO SUCURSALES' OR NOMBRE_CLIENTE IS NULL) ORDER BY pl.idPosicionLog"
 * 
 * @author Jean C. Favila
 * @version  $Revision: 1.16.72.1 $ $Date: 2014/10/29 16:38:37 $
 */
public class PosicionLog implements Serializable {

    /**
     * Constructor por default.
     */
    public PosicionLog() {
        super();
    }
    
    /**
     * Constructor que recibe los parametros necesarios para generar el log de la Posici&oacute;n.
     * 
     * @param dp El valor de DealPosicion.
     * @param tipoOperacion El tipo de operaci&oacute;n (compra o venta).
     * @param precioReferencia El precio de referencia de la divisa.
     */
    private PosicionLog(DealPosicion dp, String tipoOperacion, double precioReferencia) {
        this();
        boolean negarMontos = CANCELACION_COMPRA.equals(tipoOperacion) ||
                CANCELACION_COMPRA_INTERBANCARIA.equals(tipoOperacion) ||
                CANCELACION_VENTA.equals(tipoOperacion) ||
                CANCELACION_VENTA_INTERBANCARIA.equals(tipoOperacion);
        setDealPosicion(dp);
        setIdUsuario(dp.getIdUsuario());
        setMonto(dp.getMonto() * (negarMontos ? -1 : 1));
        setMontoMn(dp.getImporte() * (negarMontos ? -1 : 1));
        setDivisa(dp.getDivisa());
        setPrecioReferencia(precioReferencia);
        setTipoCambioCliente(dp.getTipoCambio());
        setTipoOperacion(tipoOperacion);
    }
    
    /**
     * Constructor que recibe los parametros necesarios para generar el log de la Posici&oacute;n.
     * 
     * @param det El valor de DealPosicion.
     * @param fechaValor La fecha valor a afectar.
     * @param precioReferencia El precio de referencia de la divisa.
     */
    public PosicionLog(DealDetalle det, double precioReferencia, String fechaValor) {
        this(det, det.isRecibimos() ?
                (det.getDeal().isInterbancario() ? COMPRA_INTERBANCARIA : COMPRA) :
                (det.getDeal().isInterbancario() ? VENTA_INTERBANCARIA : VENTA), precioReferencia,
                fechaValor);
    }

    /**
     * Inicializa todos los valores del registro de posicion con respecto a los
     * valores del detalle del deal proporcionado.
     *
     * @param detDeal El detalle del deal.
     * @param tipoOperacion El tipo de operaci&oacute;n.
     * @param fechaValor La fecha valor a asignar.
     * @param precioReferencia El valor del precio de referencia.
     */
    public PosicionLog(DealDetalle detDeal,  String tipoOperacion, double precioReferencia,
                       String fechaValor) {
        this(detDeal, tipoOperacion, precioReferencia);
        setCanalMesa(detDeal.getDeal().getCanalMesa());
        if (detDeal.getDeal().getCliente() != null) {
            setNombreCliente(detDeal.getDeal().getCliente().getNombreCompleto());
        }
        setDeal(detDeal.getDeal());
        setTipoValor(fechaValor);
        setClaveFormaLiquidacion(detDeal.getClaveFormaLiquidacion());
        setTipoCambioMesa(detDeal.getTipoCambioMesa());
    }

    
    /**
     * Define si la operaci&oacute;n es compra.
     * 
     * @return boolean
     */
    public boolean isCompra() {
        return COMPRA.equals(getTipoOperacion()) || COMPRA_INTERBANCARIA.equals(getTipoOperacion());
    }

    /**
     * Define si la operaci&oacute;n es venta.
     * 
     * @return boolean
     */
    public boolean isVenta() {
        return VENTA.equals(getTipoOperacion()) || VENTA_INTERBANCARIA.equals(getTipoOperacion());
    }

    /**
     * Regresa el valor de idPosicionLog.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_POSICION_LOG"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_POSICION_LOG_SEQ"
     * @return int.
     */
    public int getIdPosicionLog() {
        return idPosicionLog;
    }

    /**
     * Fija el valor de idPosicionLog.
     *
     * @param idPosicionLog El valor a asignar.
     */
    public void setIdPosicionLog(int idPosicionLog) {
        this.idPosicionLog = idPosicionLog;
    }

    /**
     * Regresa el valor de claveFormaLiquidacion.
     *
     * @hibernate.property column="CLAVE_FORMA_LIQUIDACION"
     * not-null="true"
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
     * Regresa el valor de idUsuario.
     *
     * @hibernate.property column="ID_USUARIO"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Fija el valor de idUsuario.
     *
     * @param idUsuario El valor a asignar.
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Regresa el valor de monto.
     *
     * @hibernate.property column="MONTO"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Fija el valor de monto.
     *
     * @param monto El valor a asignar.
     */
    public void setMonto(double monto) {
        this.monto = monto;
    }

    /**
     * Regresa el valor de montoMn.
     *
     * @hibernate.property column="MONTO_MN"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getMontoMn() {
        return montoMn;
    }

    /**
     * Fija el valor de montoMn.
     *
     * @param montoMn El valor a asignar.
     */
    public void setMontoMn(double montoMn) {
        this.montoMn = montoMn;
    }

    /**
     * Regresa el valor de nombreCliente.
     *
     * @hibernate.property column="NOMBRE_CLIENTE"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getNombreCliente() {
        return nombreCliente;
    }

    /**
     * Fija el valor de nombreCliente.
     *
     * @param nombreCliente El valor a asignar.
     */
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    /**
     * Regresa el valor de precioReferencia.
     *
     * @hibernate.property column="PRECIO_REFERENCIA"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getPrecioReferencia() {
        return precioReferencia;
    }

    /**
     * Fija el valor de precioReferencia.
     *
     * @param precioReferencia El valor a asignar.
     */
    public void setPrecioReferencia(double precioReferencia) {
        this.precioReferencia = precioReferencia;
    }

    /**
     * Regresa el valor de tipoOperacion.
     *
     * @hibernate.property column="TIPO_OPERACION"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getTipoOperacion() {
        return tipoOperacion;
    }

    /**
     * Fija el valor de tipoOperacion.
     *
     * @param tipoOperacion El valor a asignar.
     */
    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    /**
     * Regresa el valor de tipoCambioCliente.
     *
     * @hibernate.property column="TIPO_CAMBIO_CLIENTE"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getTipoCambioCliente() {
        return tipoCambioCliente;
    }

    /**
     * Fija el valor de tipoCambioCliente.
     *
     * @param tipoCambioCliente El valor a asignar.
     */
    public void setTipoCambioCliente(double tipoCambioCliente) {
        this.tipoCambioCliente = tipoCambioCliente;
    }

    /**
     * Regresa el valor de tipoCambioCliente.
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
     * Fija el valor det tipoCambioMesa.
     *
     * @param tipoCambioMesa El valor a asignar.
     */
    public void setTipoCambioMesa(double tipoCambioMesa) {
        this.tipoCambioMesa = tipoCambioMesa;
    }

    /**
     * Regresa el valor de tipoValor.
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
     * Fija el valor de tipoValor.
     *
     * @param tipoValor El valor a asignar.
     */
    public void setTipoValor(String tipoValor) {
        this.tipoValor = tipoValor;
    }

    /**
     * Regresa el valor de dealPosicion.
     *
     * @hibernate.many-to-one column="ID_DEAL_POSICION"
     * cascade="none"
     * class="com.ixe.ods.sica.model.DealPosicion"
     * outer-join="auto"
     * unique="false"
     * @return DealPosicion.
     */
    public DealPosicion getDealPosicion() {
        return _dealPosicion;
    }

    /**
     * Fija el valor de dealPosicion.
     *
     * @param dealPosicion El valor a asignar.
     */
    public void setDealPosicion(DealPosicion dealPosicion) {
        _dealPosicion = dealPosicion;
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
     * Regresa el valor de divisa.
     *
     * @hibernate.many-to-one column="ID_DIVISA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Divisa"
     * outer-join="auto"
     * unique="false"
     * @return Divisa.
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
     * Regresa el valor de fecha.
     *
     * @hibernate.property column="FECHA"
     * not-null="false"
     * unique="false"
     * @return Date.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Fija el valor de fecha.
     *
     * @param fecha El valor a asignar.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof PosicionLog)) {
            return false;
        }
        PosicionLog castOther = (PosicionLog) other;
        return new EqualsBuilder().append(this.getIdPosicionLog(),
                castOther.getIdPosicionLog()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdPosicionLog()).toHashCode();
    }

    /**
     * Regresa la descripci&oacute;n de los campos del objeto.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this).append("idPosicionLog", idPosicionLog).
                append("cfl", claveFormaLiquidacion).append("idUsuario", idUsuario).
                append("monto", monto).append("montoMn", montoMn).
                append("nombreCliente", nombreCliente).append("precioReferencia", precioReferencia).
                append("tcc", tipoCambioCliente).append("tipoCambioMesa", tipoCambioMesa).
                append("tipoOperacion", tipoOperacion).
                append("divisa", _divisa.getIdDivisa()).toString();
    }

    /**
     * El identificador del registro.
     */
    private int idPosicionLog;

    /**
     * La clave del producto.
     */
    private String claveFormaLiquidacion;

    /**
     * El componente CanalMesa.
     */
    private CanalMesa _canalMesa;

    /**
     * El usuario que hizo la operaci&oacute;n.
     */
    private int idUsuario;

    /**
     * El monto del detalle del deal.
     */
    private double monto;

    /**
     * El monto del detalle del deal en moneda nacional.
     */
    private double montoMn;

    /**
     * El nombre del cliente.
     */
    private String nombreCliente;

    /**
     * Precio de referencia SPOT en el momento de la captura.
     */
    private double precioReferencia;

    /**
     * El tipo de cambio pactado con el cliente.
     */
    private double tipoCambioCliente;

    /**
     * El tipo de cambio que exist&iacute;a en la Mesa de Cambios en el momento
     * de captura (este campo no tiene funcionalidad para los deals
     * interbancarios que son las operaciones de CI y VI). Su funci&oacute;n es
     * &uacute;nicamente de referencia y c&aacute;lculo de la utilidad por deal.
     *
     */
    private double tipoCambioMesa;

    /**
     * El tipo de operaci&oacute;n (COMPRA | VENTA | COMPRA_INTERBANCARIA |
     * VENTA_INTERBANCARIA | CANCELACION).
     */
    private String tipoOperacion;

    /**
     * C)ash | T)om | S)pot.
     */
    private String tipoValor;

    /**
     * Relaci&oacute;n muchos-a-uno con Deal.
     */
    private Deal _deal;

    /**
     * Relaci&oacute;n muchos-a-uno con DealPosicion.
     */
    private DealPosicion _dealPosicion;

    /**
     * Relaci&oacute;n muchos a uno con Divisa.
     */
    private Divisa _divisa;

    /**
     * La Fecha de registro del Log.
     */
    private Date fecha = new Date();
    
    /**
     * Constante que define el tipo de operaci&oacute;n como compra.
     */
    public static final String COMPRA = "CO";
    
    /**
     * Constante que define el tipo de operaci&oacute;n como venta.
     */
    public static final String VENTA = "VE";
    
    /**
     * Constante que define el tipo de operaci&oacute;n como compra interbancaria.
     */
    public static final String COMPRA_INTERBANCARIA = "CI";
    
    /**
     * Constante que define el tipo de operaci&oacute;n como venta interbancaria.
     */
    public static final String VENTA_INTERBANCARIA = "VI";
    
    /**
     * Constante que define el tipo de operaci&oacute;n como compra entre portafolios (traspaso).
     */
    public static final String COMPRA_TRASPASO_PORTAFOLIOS = "CM";
    
    /**
     * Constante que define el tipo de operaci&oacute;n como venta entre portafolios (traspaso).
     */
    public static final String VENTA_TRASPASO_PORTAFOLIOS = "VM";

    /**
     * Reconocimiento de Utilidades D&oacute;lares
     */
    public static final String DOLARES_UTILIDADES = "DU";

    /**
     * Reconocimiento de Utilidades Pesos
     */
    public static final String PESOS_UTILIDADES = "PU";

    /**
     * Reconocimiento de P&eacute;rdida D&oacute;lares
     */
    public static final String DOLARES_PERDIDA = "DP";

    /**
     * Reconocimiento de P&eacute;rdida Pesos
     */
    public static final String PESOS_PERDIDA = "PP";
    
    /**
     * Constante que define la operacion como cancelaci&oacute;n de una compra.
     */
    public static final String CANCELACION_COMPRA = "CC";
    
    /**
     * Constante que define la operacion como cancelaci&oacute;n de una venta.
     */
    public static final String CANCELACION_VENTA = "VC";
    
    /**
     * Constante que define la operacion como cancelaci&oacute;n de una compra interbancaria.
     */
    public static final String CANCELACION_COMPRA_INTERBANCARIA = "CN";
    
    /**
     * Constante que define la operacion como cancelaci&oacute;n de una venta interbancaria.
     */
    public static final String CANCELACION_VENTA_INTERBANCARIA = "VN";

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 2750424703776086586L;
}
