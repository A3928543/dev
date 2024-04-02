/*
 * $Id: UtilidadForward.java,v 1.1 2008/06/19 21:01:00 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la tabla SC_UTILIDAD_FORWARD, que almacena la informaci&oacute;n de los
 * swaps capturados en una fecha en particular. Esta informaci&oacute;n es consultada por el reporte
 * de Utilidades de Ixe Forwards del &aacute;rea de Contabilidad. Deben crearse estos registros
 * durante la Generaci&oacute;n de Movimientos Contables.
 *
 * @hibernate.class table="SC_UTILIDAD_FORWARD"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.UtilidadForward"
 * dynamic-update="true"
 *
 * @hibernate.query name="findUtilidadForwardByFecha"
 * query="FROM UtilidadForward AS uf LEFT JOIN FETCH uf.mesaCambio WHERE uf.fecha BETWEEN ? AND ?
 * ORDER BY uf.mesaCambio.idMesaCambio, uf.idDivisa"
 *
 * @hibernate.query name="findUtilidadForwardByFechaMesaDivisa"
 * query="FROM UtilidadForward AS uf WHERE uf.fecha BETWEEN ? AND ? AND
 * uf.mesaCambio.idMesaCambio = ? AND uf.idDivisa = ?"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.1 $ $Date: 2008/06/19 21:01:00 $
 */
public class UtilidadForward implements Serializable {

    /**
     * Constructor por default.
     */
    public UtilidadForward() {
        super();
    }

    /**
     * Constructor que inicializa las variables de instancia.
     *
     * @param fecha La fecha en la que se captur&oacute; el Forward.
     * @param idDeal El n&uacute;mero de deal de forward.
     * @param recibimos True para compra, False para venta.
     * @param fechaValor CASH | TOM | SPOT | 72HR | VFUT.
     * @param idDivisa La divisa operada.
     * @param monto El monto de la operaci&oacute;n del forward.
     * @param montoEquivalente El monto de la operaci&oacute;n en la divisa de referencia de la mesa
     * @param tipoCambioCliente El tipo de cambio operado.
     * @param utilidad La utilidad o p&eacute;rdida generada por el forward.
     * @param inicioSwap Si fue o no el inicio del Swap.
     * @param mesaCambio La mesa de cambio en la que se oper&oacute; el forward.
     */
    public UtilidadForward(Date fecha, int idDeal, boolean recibimos, String fechaValor,
                           String idDivisa, BigDecimal monto, BigDecimal montoEquivalente,
                           BigDecimal tipoCambioCliente, BigDecimal utilidad, boolean inicioSwap,
                           MesaCambio mesaCambio) {
        this();
        this.fecha = fecha;
        this.idDeal = idDeal;
        this.recibimos = recibimos;
        this.fechaValor = fechaValor;
        this.idDivisa = idDivisa;
        this.monto = monto;
        this.montoEquivalente = montoEquivalente;
        this.tipoCambioCliente = tipoCambioCliente;
        this.utilidad = utilidad;
        this.inicioSwap = inicioSwap;
        this.mesaCambio = mesaCambio;
    }

    /**
     * Regresa la cadena 'S&iacute;' si es el inicio de Swap y 'No' si no lo es.
     *
     * @return String.
     */
    public String getInicioSwapDesc() {
        return inicioSwap ?  "S\u00ed" : "No";
    }

    /**
     * Regresa la cadena 'Compra' si es recibimos y 'Venta' si no lo es.
     *
     * @return String.
     */
    public String getRecibimosDesc() {
        return recibimos ? "Compra" : "Venta";
    }

    /**
     * Regresa el valor de idUtilidadForward.
     *
     * @return String.
     * @hibernate.id generator-class="uuid.hex"
     * column="ID_UTILIDAD_FORWARD"
     * unsaved-value="null"
     */
    public String getIdUtilidadForward() {
        return idUtilidadForward;
    }

    /**
     * Establece el valor de idUtilidadForward.
     *
     * @param idUtilidadForward El valor a asignar.
     */
    public void setIdUtilidadForward(String idUtilidadForward) {
        this.idUtilidadForward = idUtilidadForward;
    }

    /**
     * Regresa el valor de fecha.
     *
     * @return Date.
     * @hibernate.property column="FECHA"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Establece el valor de fecha.
     *
     * @param fecha El valor a asignar.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Regresa el valor de fechaValor.
     *
     * @return String.
     * @hibernate.property column="FECHA_VALOR"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public String getFechaValor() {
        return fechaValor;
    }

    /**
     * Establece el valor de fechaValor.
     *
     * @param fechaValor El valor a asignar.
     */
    public void setFechaValor(String fechaValor) {
        this.fechaValor = fechaValor;
    }

    /**
     * Regresa el valor de idDeal.
     *
     * @return int.
     * @hibernate.property column="ID_DEAL"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public int getIdDeal() {
        return idDeal;
    }

    /**
     * Establece el valor de idDeal.
     *
     * @param idDeal El valor a asignar.
     */
    public void setIdDeal(int idDeal) {
        this.idDeal = idDeal;
    }

    /**
     * Regresa el valor de idDivisa.
     *
     * @return String.
     * @hibernate.property column="ID_DIVISA"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public String getIdDivisa() {
        return idDivisa;
    }

    /**
     * Establece el valor de idDivisa.
     *
     * @param idDivisa El valor a asignar.
     */
    public void setIdDivisa(String idDivisa) {
        this.idDivisa = idDivisa;
    }

    /**
     * Regresa el valor de inicioSwap.
     *
     * @return boolean.
     * @hibernate.property column="INICIO_SWAP"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public boolean isInicioSwap() {
        return inicioSwap;
    }

    /**
     * Establece el valor de inicioSwap.
     *
     * @param inicioSwap El valor a asignar.
     */
    public void setInicioSwap(boolean inicioSwap) {
        this.inicioSwap = inicioSwap;
    }

    /**
     * Regresa el valor de monto.
     *
     * @return BigDecimal.
     * @hibernate.property column="MONTO"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public BigDecimal getMonto() {
        return monto;
    }

    /**
     * Establece el valor de monto.
     *
     * @param monto El valor a asignar.
     */
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    /**
     * Regresa el valor de montoEquivalente.
     *
     * @return BigDecimal.
     * @hibernate.property column="MONTO_EQUIVALENTE"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public BigDecimal getMontoEquivalente() {
        return montoEquivalente;
    }

    /**
     * Establece el valor de montoEquivalente.
     *
     * @param montoEquivalente El valor a asignar.
     */
    public void setMontoEquivalente(BigDecimal montoEquivalente) {
        this.montoEquivalente = montoEquivalente;
    }

    /**
     * Regresa el valor de recibimos.
     *
     * @return boolean.
     * @hibernate.property column="RECIBIMOS"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public boolean isRecibimos() {
        return recibimos;
    }

    /**
     * Establece el valor de recibimos.
     *
     * @param recibimos El valor a asignar.
     */
    public void setRecibimos(boolean recibimos) {
        this.recibimos = recibimos;
    }

    /**
     * Regresa el valor de tipoCambioCliente.
     *
     * @return BigDecimal.
     * @hibernate.property column="TIPO_CAMBIO_CLIENTE"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public BigDecimal getTipoCambioCliente() {
        return tipoCambioCliente;
    }

    /**
     * Establece el valor de tipoCambioCliente.
     *
     * @param tipoCambioCliente El valor a asignar.
     */
    public void setTipoCambioCliente(BigDecimal tipoCambioCliente) {
        this.tipoCambioCliente = tipoCambioCliente;
    }

    /**
     * Regresa el valor de utilidad.
     *
     * @return BigDecimal.
     * @hibernate.property column="UTILIDAD"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public BigDecimal getUtilidad() {
        return utilidad;
    }

    /**
     * Establece el valor de utilidad.
     *
     * @param utilidad El valor a asignar.
     */
    public void setUtilidad(BigDecimal utilidad) {
        this.utilidad = utilidad;
    }

    /**
     * Regresa el valor de mesaCambio.
     *
     * @hibernate.many-to-one column="ID_MESA_CAMBIO"
     * cascade="none"
     * class="com.ixe.ods.sica.model.MesaCambio"
     * outer-join="auto"
     * unique="false"
     * @return MesaCambio.
     */
    public MesaCambio getMesaCambio() {
        return mesaCambio;
    }

    /**
     * Establece el valor de mesaCambio.
     *
     * @param mesaCambio El valor a asignar.
     */
    public void setMesaCambio(MesaCambio mesaCambio) {
        this.mesaCambio = mesaCambio;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @return boolean.
     * @see java.lang.Object#equals(java.lang.Object).
     */
    public boolean equals(Object other) {
        if (!(other instanceof UtilidadForward)) {
            return false;
        }
        UtilidadForward castOther = (UtilidadForward) other;
        return new EqualsBuilder().append(this.getIdUtilidadForward(),
                castOther.getIdUtilidadForward()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @return <code>int</code>
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdUtilidadForward()).toHashCode();
    }
    
    /**
     * La llave primaria del registro.
     */
    private String idUtilidadForward;

    /**
     * La fecha en la que se captur&oacute; el Forward.
     */
    private Date fecha;

    /**
     * La fecha valor de la operaci&oacute;n CASH, TOM, SPOT, 72HR o VFUT.
     */
    private String fechaValor;

    /**
     * El n&uacute;mero de deal de forward.
     */
    private int idDeal;

    /**
     * La divisa operada.
     */
    private String idDivisa;

    /**
     * Si el forward es o no el inicio de un swap.
     */
    private boolean inicioSwap;

    /**
     * El monto de la operaci&oacute;n de forwards.
     */
    private BigDecimal monto = new BigDecimal("" + 0);

    /**
     * El monto en la divisa de referencia de la mesa.
     */
    private BigDecimal montoEquivalente = new BigDecimal("" + 0);

    /**
     * True para compra, False para venta.
     */
    private boolean recibimos;

    /**
     * El tipo de cambio de la operaci&oacute;n.
     */
    private BigDecimal tipoCambioCliente = new BigDecimal("" + 0);

    /**
     * La utilidad o p&eacute;rdida generada por el forward.
     */
    private BigDecimal utilidad;

    /**
     * Relaci&oacute;n muchos-a-uno con MesaCambio.
     */
    private MesaCambio mesaCambio;

    /**
     * El UID para serializaci&oacute;n. 
     */
    private static final long serialVersionUID = 7125381385275065932L;
}
