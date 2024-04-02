/*
 * $Id: PosicionDetalle.java,v 1.12 2008/02/22 18:25:23 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Clase persistente para la tabla SC_POSICION_DETALLE. Almacena el detalle del
 * resumen total de la posici&oacute;n, por canal y producto.
 *
 * @hibernate.class table="SC_POSICION_DETALLE"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.PosicionDetalle"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:23 $
 */
public class PosicionDetalle implements Serializable {

    /**
     * Constructor vac&iacute;o. No hace nada.
     */
    public PosicionDetalle() {
        super();
    }
        
    /**
     * Constructor que inicializa todas las variables
     * 
     * @param canal El canal a asignar.
     * @param div La divisa a asignar.
     * @param mesa La mesa de cambios a asignar.
     * @param claveFormaLiq La clave del producto a asignar.
     * @param posicion El registro de posici&oacute;n relacionado con este detalle. 
     */
    public PosicionDetalle(Canal canal, Divisa div, MesaCambio mesa, String claveFormaLiq, Posicion posicion) {
    	this();
        CpaVta cpaVta = new CpaVta(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        CpaVtaIn cpaVtaIn = new CpaVtaIn(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        CpaVtaMn cpaVtaMn = new CpaVtaMn(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0);
        PosIni posIni = new PosIni(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        setCpaVta(cpaVta);
        setCpaVtaIn(cpaVtaIn);
        setCpaVtaMn(cpaVtaMn);
        setPosIni(posIni);
        setCanal(canal);
        setClaveFormaLiquidacion(claveFormaLiq);
        setMesaCambio(mesa);
        setDivisa(div);
        setPosicion(posicion);
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
     * Regresa el valor de posicion.
     *
     * @hibernate.many-to-one column="ID_POSICION"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Posicion"
     * outer-join="auto"
     * unique="false"
     * @return Posicion.
     */
    public Posicion getPosicion() {
        return _posicion;
    }

    /**
     * Fija el valor de posicion.
     *
     * @param posicion El valor a asignar.
     */
    public void setPosicion(Posicion posicion) {
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
        if (!(other instanceof PosicionDetalle)) {
            return false;
        }
        PosicionDetalle castOther = (PosicionDetalle) other;
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
     * Relaci&oacute;n muchos-a-uno con Posicion.
     */
    private Posicion _posicion;
}
