/*
 * $Id: FactorDivisaActual.java,v 1.12.42.1 2011/04/26 00:34:25 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */
package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase persistente para la tabla SC_FACTOR_DIVISA_ACTUAL. Tabla donde se almacena
 * cu&aacute;l es el &uacute;ltimo factorDivisa que se utiliz&oacute; en el
 * pizarr&oacute;n para la consulta..
 *
 * @hibernate.class table="SC_FACTOR_DIVISA_ACTUAL"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.FactorDivisaActual"
 * dynamic-update="true"
 *
 * @hibernate.query name="findDivisasMetalesFactor"
 * query="SELECT fda FROM FactorDivisaActual AS fda INNER JOIN FETCH fda.facDiv.fromDivisa INNER JOIN FETCH fda.facDiv.toDivisa WHERE fda.facDiv.fromDivisa.tipo = 'N' AND fda.facDiv.toDivisa.tipo = 'M' ORDER BY fda.facDiv.toDivisa.idDivisa"
 *
 * @hibernate.query name="findDivisasNoFrecuentesFactor"
 * query="SELECT fda FROM FactorDivisaActual AS fda INNER JOIN FETCH fda.facDiv.fromDivisa INNER JOIN FETCH fda.facDiv.toDivisa WHERE fda.facDiv.fromDivisa.tipo = 'N' AND fda.facDiv.toDivisa.tipo = 'N' ORDER BY fda.facDiv.toDivisa.idDivisa"
 * 
 * @hibernate.query name="findFactorDivisaActual"
 * query = "FROM FactorDivisaActual AS fda WHERE fda.facDiv.fromDivisa.idDivisa = ? AND fda.facDiv.toDivisa.idDivisa = ?"
 *
 * @hibernate.query name="findFactorDivisaActualCualquierOrden"
 * query = "SELECT fda FROM FactorDivisaActual AS fda WHERE (fda.facDiv.fromDivisa.idDivisa = ? AND fda.facDiv.toDivisa.idDivisa = ?) OR (fda.facDiv.fromDivisa.idDivisa = ? AND fda.facDiv.toDivisa.idDivisa = ?)"
 *
 * @hibernate.query name="findFactorDivisaActualByID"
 * query = "FROM FactorDivisaActual AS fda WHERE fda.idFactorDivisa = ?"
 * 
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.12.42.1 $ $Date: 2011/04/26 00:34:25 $
 */
public class FactorDivisaActual implements Serializable {

    /**
     * Constructor por default. No hace nada.
     */
    public FactorDivisaActual() {
        super();
    }

    /**
     * Constructor para realizar una copia de otro FactorDivisa.
     *
     * @param fdOriginal El factor divisa a duplicar.
     */
    public FactorDivisaActual(FactorDivisaActual fdOriginal) {
        this();
        getFacDiv().setFromDivisa(fdOriginal.getFacDiv().getFromDivisa());
        getFacDiv().setToDivisa(fdOriginal.getFacDiv().getToDivisa());
        getFacDiv().setUltimaModificacion(new Date());
        getFacDiv().setMetodoActualizacion(fdOriginal.getFacDiv().getMetodoActualizacion());
        getFacDiv().setFactor(fdOriginal.getFacDiv().getFactor());
        getFacDiv().setFactorCompra(fdOriginal.getFacDiv().getFactorCompra());
        getFacDiv().setSpreadReferencia(fdOriginal.getFacDiv().getSpreadReferencia());
        getFacDiv().setSpreadCompra(fdOriginal.getFacDiv().getSpreadCompra());
        getFacDiv().setCarry(fdOriginal.getFacDiv().getCarry());
        getFacDiv().setSlack(fdOriginal.getFacDiv().getSlack());
    }

    /**
     * Regresa el valor de idFactorDivisa.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_FACTOR_DIVISA"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_FACTOR_DIVISA_SEQ"
     * @return int.
     */
    public int getIdFactorDivisa() {
        return idFactorDivisa;
    }

    /**
     * Fija el valor de idFactorDivisa.
     *
     * @param idFactorDivisa El valor a asginar.
     */
    public void setIdFactorDivisa(int idFactorDivisa) {
        this.idFactorDivisa = idFactorDivisa;
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
     * Regresa el valor de facDiv.
     *
     * @hibernate.component class="com.ixe.ods.sica.model.FacDiv"
     * @return FacDiv.
     */
    public FacDiv getFacDiv() {
        return this.facDiv;
    }

    /**
     * Fija el valor de facDiv.
     *
     * @param facDiv El valor a asignar.
     */
    public void setFacDiv(FacDiv facDiv) {
        this.facDiv = facDiv;
    }

    /**
     * Regresa la divisa que no es D&oacute;lar ni Pesos Mexicanos.
     *
     * @return Divisa.
     */
    public Divisa getOtraDivisaNoUsdNoMxn() {
        return Divisa.DOLAR.equals(getFacDiv().getFromDivisa().getIdDivisa()) || Divisa.PESO.equals(getFacDiv().getFromDivisa().getIdDivisa()) ?
                getFacDiv().getToDivisa() : getFacDiv().getFromDivisa();
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof FactorDivisaActual)) {
            return false;
        }
        FactorDivisaActual castOther = (FactorDivisaActual) other;
        return new EqualsBuilder().append(this.getIdFactorDivisa(), castOther.getIdFactorDivisa()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdFactorDivisa()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    private int idFactorDivisa;

    /**
     * Indica si el objeto ha sido modificado
     */
    private boolean modificado = false;

    /**
     * El componente FacDiv.
     */
    private FacDiv facDiv = new FacDiv();
}
