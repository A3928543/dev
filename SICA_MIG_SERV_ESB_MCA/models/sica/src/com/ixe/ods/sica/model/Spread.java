/*
 * $Id: Spread.java,v 1.13 2008/03/13 18:56:37 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la table SC_SPREAD. Contiene los spreads por claveFormaLiquidacion, canal,
 * mesa y divisa.
 *
 * @hibernate.class table="SC_SPREAD"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Spread"
 * dynamic-update="true"
 *
 * @hibernate.query name="findSpreadsActualesByMesaSucursalDivisaFormaLiquidacion"
 * query="FROM Spread AS s WHERE s.idSpread in (SELECT sa.idSpread FROM SpreadActual AS sa WHERE
 * sa.id.mesaCambio.idMesaCambio = ? AND sa.id.canal.sucursal is not null AND
 * sa.id.divisa.idDivisa = ? AND sa.id.claveFormaLiquidacion = ?)"
 *
 * @hibernate.query name="findSpreadsActualesByMesaCanalDivisa"
 * query="FROM Spread AS s WHERE s.idSpread in (SELECT sa.idSpread FROM SpreadActual AS sa WHERE
 * sa.id.mesaCambio.idMesaCambio = ? AND sa.id.canal.idCanal = ? AND sa.id.divisa.idDivisa = ?)"
 *
 * @hibernate.query name="findSpreadsActualesByCanal"
 * query="FROM Spread AS s WHERE s.idSpread in (SELECT sa.idSpread FROM SpreadActual AS sa WHERE
 * sa.id.canal.idCanal = ?)"
 *
 * @hibernate.query name="findSpreadsActualesByMesaTipoPizarron"
 * query="FROM Spread AS s INNER JOIN FETCH s.divisa WHERE s.idSpread in (SELECT sa.idSpread FROM
 * SpreadActual AS sa WHERE sa.id.tipoPizarron.idTipoPizarron = ?)"
 *
 * @hibernate.query name="findSpreadsActualesByMesaCanalFormaLiquidacionDivisa"
 * query="FROM Spread AS s WHERE s.idSpread in (SELECT sa.idSpread FROM SpreadActual AS sa WHERE
 * sa.id.mesaCambio.idMesaCambio = ? AND sa.id.canal.idCanal = ? AND
 * sa.id.claveFormaLiquidacion = ? AND sa.id.divisa.idDivisa = ?)"
 *
 * @hibernate.query name="findSpreadsActualesByTipoPizarronFormaLiquidacionDivisa"
 * query="FROM Spread AS s WHERE s.idSpread in (SELECT sa.idSpread FROM SpreadActual AS sa WHERE
 * sa.id.tipoPizarron.idTipoPizarron = ? AND sa.id.claveFormaLiquidacion = ? AND
 * sa.id.divisa.idDivisa = ?)"
 *
 * @hibernate.query name="findSpreadsActualesByTipoPizarronDivisa"
 * query="FROM Spread AS s WHERE s.idSpread in (SELECT sa.idSpread FROM SpreadActual AS sa WHERE
 * sa.id.tipoPizarron.idTipoPizarron = ? AND sa.id.divisa.idDivisa = ?)"
 *
 * @hibernate.query name="findSpreadsActualesByTipoPizarron"
 * query="FROM Spread AS s WHERE s.idSpread in (SELECT sa.idSpread FROM SpreadActual AS sa WHERE
 * sa.id.tipoPizarron.idTipoPizarron = ?)"
 *
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.13 $ $Date: 2008/03/13 18:56:37 $
 *
 */
public class Spread implements Serializable {

    /**
     * Constructor por default. no hace nada.
     */
    public Spread() {
        super();
    }

    /**
     * Crea una nueva instancia basada en el spread recibido como
     * par&aacute;metro.
     * @param spread El spread a copiar.
     */
    public Spread(Spread spread) {
        this();
        setTipoPizarron(spread.getTipoPizarron());
        setClaveFormaLiquidacion(spread.getClaveFormaLiquidacion());
        setCpaVta(spread.getCpaVta());
        setDivisa(spread.getDivisa());
    }

    /**
     * Regresa el valor de idSpread.
     * @hibernate.id generator-class="sequence"
     * column="ID_SPREAD"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_SPREAD_SEQ"
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
     * Fija el valor de ultimaModificacion.
     *
     * @param ultimaModificacion El valor a asignar.
     */
    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    /**
     * Regresa true si el objeto ha sido modificado.
     *
     * @return boolean.
     */
    public boolean isModificado() {
        return modificado;
    }

    /**
     * Establece el campo modificado
     * @param modificado Si se modifico el objeto.
     */
    public void setModificado(boolean modificado) {
        this.modificado = modificado;
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
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof Spread)) {
            return false;
        }
        Spread castOther = (Spread) other;
        return new EqualsBuilder().append(this.getIdSpread(), castOther.getIdSpread()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdSpread()).toHashCode();
    }

    /**
     * Obtiene el tipo de Pizarron para el spread.
     *
     * @hibernate.many-to-one column="ID_TIPO_PIZARRON"
     * cascade="none"
     * class="com.ixe.ods.sica.model.TipoPizarron"
     * outer-join="auto"
     * unique="false"
     * @return TipoPizarron
     */
    public TipoPizarron getTipoPizarron() {
    	return tipoPizarron;
    }

   /**
    * Asigna el valor para tipoPizarron.
    *
    * @param tipoPizarron El valor para tipoPizarron.
    */
	public void setTipoPizarron(TipoPizarron tipoPizarron) {
		this.tipoPizarron = tipoPizarron;
	}

    /**
     * El identificador del registro.
     */
    private int idSpread;

    /**
     * La clave del producto.
     */
    private String claveFormaLiquidacion;

    /**
     * El componente cpaVta.
     */
    private CpaVta _cpaVta = new CpaVta();

    /**
     * La fecha en que fue modificado el registro.
     */
    private Date ultimaModificacion = new Date();

    /**
     * Indica si el objeto ha sido modificado
     */
    private boolean modificado;

    /**
     * Relaci&oacute;n muchos a uno con Divisa.
     */
    private Divisa _divisa;

    /**
     * El id del tipo de pizarron para el spread.
     */
    private TipoPizarron tipoPizarron;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 7355148552937583478L;
}
