/*
 * $Id: DealPosicion.java,v 1.12.88.1 2016/07/13 21:42:36 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import com.ixe.ods.sica.Redondeador;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * <p>Superclase abstracta que se utiliza para cualquier operaci&oacute;n que
 * modifique la Posici&oacute;n para realizar una relaci&oacute;n con
 * PosicionLog.</p>
 * <p>Las subclases a la fecha son:
 *  <ul>
 *   <li>DealDetalle</li>
 *   <li>DealInterbancario</li>
 *   <li>DealTraspaso</li>
 *  <ul>
 * </p>
 *
 * @hibernate.class table="SC_DEAL_POSICION"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.DealPosicion"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.12.88.1 $ $Date: 2016/07/13 21:42:36 $
 */
public class DealPosicion implements Serializable {

    /**
     * Constructor vac&iacute;o, no hace nada.
     */
    public DealPosicion() {
        super();
    }

    /**
     * Regresa el producto de tipoCambio por monto.
     * Mxn tipo Cambio = 1
     * @return double.
     */
    public double getImporte() {
        return Redondeador.redondear2Dec(getTipoCambio() * getMonto());
    }

    /**
     * Regresa el valor de idDealPosicion.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_DEAL_POSICION"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_DEAL_POSICION_SEQ"
     * @return int.
     */
    public int getIdDealPosicion() {
        return idDealPosicion;
    }

    /**
     * Fija el valor de idDealPosicion.
     *
     * @param idDealPosicion El valor a asignar.
     */
    public void setIdDealPosicion(int idDealPosicion) {
        this.idDealPosicion = idDealPosicion;
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
     * Regresa el valor de monto en la divisa que se capturo.
     *
     * @hibernate.property column="MONTO"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getMonto() {
        return Redondeador.redondear2Dec(monto);
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
     * Regresa el valor de recibimos.
     *
     * @hibernate.property column="RECIBIMOS"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return boolean.
     */
    public boolean isRecibimos() {
        return recibimos;
    }

    /**
     * Fija el valor de recibimos.
     *
     * @param recibimos El valor a asignar.
     */
    public void setRecibimos(boolean recibimos) {
        this.recibimos = recibimos;
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
     * Regresa el valor de tipoCambio.
     *
     * @hibernate.property column="TIPO_CAMBIO"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getTipoCambio() {
        return Redondeador.redondear6Dec(tipoCambio);
    }

    /**
     * Fija el valor de tipoCambio.
     *
     * @param tipoCambio El valor a asignar.
     */
    public void setTipoCambio(double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    /**
     * Regresa el valor de tipoDeal.
     *
     * @hibernate.property column="TIPO_DEAL"
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
        return _mesaCambio;
    }

    /**
     * Fija el valor de mesaCambio.
     *
     * @param mesaCambio El valor a asignar.
     */
    public void setMesaCambio(MesaCambio mesaCambio) {
        _mesaCambio = mesaCambio;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof DealPosicion)) {
            return false;
        }
        DealPosicion castOther = (DealPosicion) other;
        return new EqualsBuilder().append(this.getIdDealPosicion(), castOther.getIdDealPosicion()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdDealPosicion()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    protected int idDealPosicion;

    /**
     * El identificador del &uacute;ltimo usuario que modific&oacute; el
     * registro.
     */
    private int idUsuario;

    /**
     * Monto en la divisa que modific&oacute; PosicionLog.
     */
    private double monto;

    /**
     * True compra | False venta.
     */
    private boolean recibimos;

    /**
     * Notas en general opcionales.
     */
    private String observaciones;

    /**
     * Tipo del cambio del cliente.
     */
    private double tipoCambio;

    /**
     * C)liente, I)nterbancario, T)raspaso.
     */
    private String tipoDeal;

    /**
     * La propiedad de versi&oacute;n para optimistic locking.
     */
    private Integer version = new Integer(0);

    /**
     * Relaci&oacute;n muchos-a-uno con Divisa.
     */
    private Divisa _divisa;

    /**
     * Relaci&oacute;n muchos-a-uno con MesaCambio.
     */
    private MesaCambio _mesaCambio;

    /**
     * Constante para representar el Tipo de Deal Cliente.
     */
    public final static String TIPO_DEAL_CLIENTE = "C";

    /**
     * Constante  para representar el Tipo de Deal Interbancario
     */
    public final static String TIPO_DEAL_INTERBANCARIO = "I";

    /**
     * Constante para representar el Tipo de Deal Traspaso
     */
    public final static String TIPO_DEAL_TRASPASO = "T";

    /**
     * Constante para representar el Tipo de Deal Utilidades
     */
    public final static String TIPO_DEAL_UTILIDADES = "U";
}
