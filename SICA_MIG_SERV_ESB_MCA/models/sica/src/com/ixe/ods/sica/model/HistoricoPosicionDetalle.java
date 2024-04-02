/*
 * $Id: HistoricoPosicionDetalle.java,v 1.12 2008/02/22 18:25:21 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Clase persistente para la tabla SC_H_POSICION_DETALLE, donde se almacena el
 * hist&oacute;rico de la Posici&oacute;n Detalle. </p>
 *
 * @hibernate.class table="SC_H_POSICION_DETALLE"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.HistoricoPosicionDetalle"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:21 $
 */

public class HistoricoPosicionDetalle implements Serializable {

    /**
    * Constructor por default. No hace nada.
    */
    public HistoricoPosicionDetalle() {
        super();
    }

    /**
    * Inicializa el objeto recibiendo como par&aacute;metro posici&oacute;n detalle.
    *
    * @param pd La posici&oacute;n detalle que inicializa el hist&oacute;rico.
    */
    public HistoricoPosicionDetalle(PosicionDetalle pd) {
        this();
        setCanal(pd.getCanal());
        setClaveFormaLiquidacion(pd.getClaveFormaLiquidacion());
        setCpaVta(pd.getCpaVta());
        setCpaVtaIn(pd.getCpaVtaIn());
        setCpaVtaMn(pd.getCpaVtaMn());
        setDivisa(pd.getDivisa());
        setIdPosicionDetalle(pd.getIdPosicionDetalle());
        setMesaCambio(pd.getMesaCambio());
        // todo: hay que revisar esto:
        setPosicion(new Integer(pd.getPosicion().getIdPosicion()));
        setPosIni(pd.getPosIni());
        setUltimaModificacion(new Date());
    }

    /**
     * Regresa el valor de idPosicionDetalle.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_POSICION_DETALLE"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_POSICION_DETALLE_SEQ"
     * @return int.
     */
    public int getIdPosicionDetalle() {
        return idPosicionDetalle;
    }

        /**
     * Fija el valor de idPosicionDetalle.
     *
     * @param idPosicionDetalle El valor a asignar.
     */
    public void setIdPosicionDetalle(int idPosicionDetalle) {
        this.idPosicionDetalle = idPosicionDetalle;
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
     * Regresa el valor de cpaVta.
     *
     * @hibernate.component class="com.ixe.ods.sica.model.CpaVta"
     * @return CpaVta.
     */
    public CpaVta getCpaVta() {
        return _cpaVta;
    }

    /**
     * Fija el valor de cpaVta.
     *
     * @param cpaVta El valor a asignar.
     */
    public void setCpaVta(CpaVta cpaVta) {
        _cpaVta = cpaVta;
    }

    /**
     * Regresa el valor de cpaVtaIn.
     *
     * @hibernate.component class="com.ixe.ods.sica.model.CpaVtaIn"
     * @return CpaVta.
     */
    public CpaVtaIn getCpaVtaIn() {
        return _cpaVtaIn;
    }

    /**
     * Fija el valor de cpaVtaIn.
     *
     * @param cpaVtaIn El valor a asignar.
     */
    public void setCpaVtaIn(CpaVtaIn cpaVtaIn) {
        _cpaVtaIn = cpaVtaIn;
    }

    /**
     * Regresa el valor de cpaVtaMn.
     *
     * @hibernate.component class="com.ixe.ods.sica.model.CpaVtaMn"
     * @return CpaVtaMn.
     */
    public CpaVtaMn getCpaVtaMn() {
        return _cpaVtaMn;
    }

    /**
     * Fija el valor de cpaVtaMn.
     *
     * @param cpaVtaMn El valor a asignar.
     */
    public void setCpaVtaMn(CpaVtaMn cpaVtaMn) {
        _cpaVtaMn = cpaVtaMn;
    }

    /**
     * Regresa el valor de posIni.
     *
     * @hibernate.component class="com.ixe.ods.sica.model.PosIni"
     * @return PosIni.
     */
    public PosIni getPosIni() {
        return _posIni;
    }

    /**
     * Fija el valor de posIni.
     *
     * @param posIni El valor a asignar.
     */
    public void setPosIni(PosIni posIni) {
        _posIni = posIni;
    }

    /**
     * Regresa el valor de ultimaModificacion.
     *
     * @hibernate.property column="ULTIMA_MODIFICACION"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getUltimaModificacion() {
        return ultimaModificacion;
    }

    /**
     * Establece el valor de ultimaModificacion.
     *
     * @param ultimaModificacion El valor a asignar.
     */
    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    /**
     * Regresa el valor de canal.
     *
     * @hibernate.many-to-one column="ID_CANAL"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Canal"
     * outer-join="auto"
     * unique="false"
     * @return Canal.
     */
    public Canal getCanal() {
        return _canal;
    }

    /**
     * Fija el valor de canal.
     *
     * @param canal El valor a asignar.
     */
    public void setCanal(Canal canal) {
        _canal = canal;
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

    public void setMesaCambio(MesaCambio mesaCambio) {
        _mesaCambio = mesaCambio;
    }

    /**
     * Regresa el valor de idPosici&oacute;n
     * 
     * @hibernate.property column="ID_POSICION"
     * not-null="true"
     * unique="false" 
     * @return Integer.
     */
    public Integer getPosicion() {
        return _posicion;
    }

    /**
     * Fija el valor de posicion.
     *
     * @param posicion El valor a asignar.
     */
    public void setPosicion(Integer posicion) {
        _posicion = posicion;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof HistoricoPosicionDetalle)) {
            return false;
        }
        HistoricoPosicionDetalle castOther = (HistoricoPosicionDetalle) other;
        return new EqualsBuilder().append(this.getIdPosicionDetalle(), castOther.getIdPosicionDetalle()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdPosicionDetalle()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    protected int idPosicionDetalle;

    /**
     * La clave del producto.
     */
    private String claveFormaLiquidacion;

    /**
     * La fecha en que se escribi&oacute; este registro hist&oacute;rico.
     */
    private Date ultimaModificacion;

    /**
     * Objeto que almacena las propiedades de compra venta.
     */
    private CpaVta _cpaVta;

    /**
     * Objeto que almacena las propiedades de compra venta para interbancario.
     */
    private CpaVtaIn _cpaVtaIn;

    /**
     * El componente cpaVtaMn, con los totales de la posici&oacute;n de un
     * Producto en un canal.
     */
    private CpaVtaMn _cpaVtaMn;

    /**
     * El componente PosIni, con los valores de la posici&oacute;n inicial.
     */
    private PosIni _posIni = new PosIni();

    /**
     * Relaci&oacute;n muchos-a-uno con Canal.
     */
    private Canal _canal;

    /**
     * Relaci&oacute;n muchos-a-uno con Divisa.
     */
    private Divisa _divisa;

    /**
     * Relaci&oacute;n muchos-a-uno con MesaCambio.
     */
    private MesaCambio _mesaCambio;

    /**
     * Relaci&oacute;n muchos-a-uno con HistoricoPosicion.
     */
    private Integer _posicion;
}
