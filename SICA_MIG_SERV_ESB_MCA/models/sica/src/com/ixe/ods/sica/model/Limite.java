/*
 * $Id: Limite.java,v 1.14 2008/05/20 21:54:12 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la tabla SC_LIMITE, donde se almacenan los
 * l&iacute;mites de riesgo por Mesa y/o Divisa.
 *
 * @hibernate.class table="SC_LIMITE"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Limite"
 * dynamic-update="true"
 * 
 * @hibernate.query name="findLimiteDeRiesgoByMesa"
 * query="FROM Limite as l WHERE l.mesaCambio.idMesaCambio = ? AND l.divisa.idDivisa is null
 * ORDER BY l.tipoLimite.idTipoLimite"
 * 
 * @hibernate.query name="findLimiteByMesaAndDivisa"
 * query="FROM Limite as l WHERE l.mesaCambio.idMesaCambio = ? AND l.divisa.idDivisa = ? ORDER
 * BY l.tipoLimite.idTipoLimite"
 * 
 * @author Jean C. Favila
 * @version  $Revision: 1.14 $ $Date: 2008/05/20 21:54:12 $
 */
public class Limite implements Serializable {

    /**
     * Constructor vac&iacute;o, no hace nada.
     */
    public Limite() {
        super();
    }

    /**
     * Regresa el valor de idLimite.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_LIMITE"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_LIMITE_SEQ"
     * @return int.
     */
    public int getIdLimite() {
        return idLimite;
    }

    /**
     * Fija el valor de idLimite.
     *
     * @param idLimite El valor a asignar.
     */
    public void setIdLimite(int idLimite) {
        this.idLimite = idLimite;
    }

    /**
     * Regresa el valor de limite.
     *
     * @hibernate.property column="LIMITE"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getLimite() {
        return limite;
    }

    /**
     * Fija el valor de limite.
     *
     * @param limite El valor a asignar.
     */
    public void setLimite(double limite) {
        this.limite = limite;
    }

    /**
     * Regresa el valor de porcentajeAviso.
     *
     * @hibernate.property column="PORCENTAJE_AVISO"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getPorcentajeAviso() {
        return porcentajeAviso;
    }

    /**
     * Fija el valor de porcentajeAviso.
     *
     * @param porcentajeAviso El valor a asignar.
     */
    public void setPorcentajeAviso(double porcentajeAviso) {
        this.porcentajeAviso = porcentajeAviso;
    }

    /**
     * Regresa el valor de porcentajeAlarma.
     *
     * @hibernate.property column="PORCENTAJE_ALARMA"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getPorcentajeAlarma() {
        return porcentajeAlarma;
    }

    /**
     * Fija el valor de porcentajeAlarma.
     *
     * @param porcentajeAlarma El valor a asignar.
     */
    public void setPorcentajeAlarma(double porcentajeAlarma) {
        this.porcentajeAlarma = porcentajeAlarma;
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
        return divisa;
    }

    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(Divisa divisa) {
        this.divisa = divisa;
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
     * Fija el valor de mesaCambio.
     *
     * @param mesaCambio El valor a asignar.
     */
    public void setMesaCambio(MesaCambio mesaCambio) {
        this.mesaCambio = mesaCambio;
    }

    /**
     * Regresa el valor de tipoLimite.
     *
     * @hibernate.many-to-one column="ID_TIPO_LIMITE"
     * cascade="none"
     * class="com.ixe.ods.sica.model.TipoLimite"
     * outer-join="auto"
     * unique="false"
     * @return TipoLimite.
     */
    public TipoLimite getTipoLimite() {
        return tipoLimite;
    }

    /**
     * Fija el valor de tipoLimite.
     *
     * @param tipoLimite El valor a asignar.
     */
    public void setTipoLimite(TipoLimite tipoLimite) {
        this.tipoLimite = tipoLimite;
    }

    /**
     * Regresa el valor de nivel.
     *
     * @return double.
     */
    public double getNivel() {
        return Math.abs(nivel);
    }

    /**
     * Establece el valor de nivel.
     *
     * @param nivel El valor a asignar.
     */
    public void setNivel(double nivel) {
        this.nivel = Math.abs(nivel);
    }

    /**
     * Regresa el porcentaje de consumo del nivel con respecto al l&iacute;mite.
     *
     * @return double.
     */
    public double getPorcentajeConsumo() {
        return limite != 0 ? nivel * CIEN / limite : 0;
    }

    /**
     * Regresa un n&uacute;mero del 0 al 4 para indicar el estado del nivel con respecto al
     * l&iacute;mite.
     *
     * @return int.
     */
    public int getEstado() {
        int res = 0;
        if (nivel > limite) {
            res = 4;
        }
        else if ((nivel * CIEN / limite) > porcentajeAviso &&
                (nivel * CIEN / limite) < porcentajeAlarma) {
            res = 3;
        }
        else if ((nivel * CIEN / limite) < porcentajeAlarma && nivel < limite) {
            res = 2;
        }
        else if (nivel < limite && (nivel * CIEN / limite) < porcentajeAviso &&
                (nivel * CIEN / limite) < porcentajeAlarma) {
            res = 1;
        }
        return res;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof Limite)) {
            return false;
        }
        Limite castOther = (Limite) other;
        return new EqualsBuilder().append(this.getLimite(), castOther.getIdLimite()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdLimite()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    private int idLimite;

    /**
     * Monto (en la divisa MXN o USD) que define el l&iacute;mite. Si el campo
     * se encuentra definido en cero, significa que dicho l&iacute;mite NO
     * deber&aacute; ser validado por el proceso de validaci&oacute;n de
     * l&iacute;mites de riesgo, ni enviar avisos o alarmas.
     */
    private double limite;

    /**
     * Porcentaje para decidir si se env&iacute;a el aviso al ser comparado el
     * l&iacute;mite con su nivel de posici&oacute;n. En caso de que el valor
     * cero, significa que no se debe validar el l&iacute;mite.
     */
    private double porcentajeAviso;

    /**
     * Porcentaje para decidir si se env&iacute;a la alarma y aviso al ser
     * comparado el l&iacute;mite con su nivel de posici&oacute;n. En caso de
     * que el valor sea cero, significa que no se debe de validar el
     * l&iacute;mite.
     */
    private double porcentajeAlarma;

    /**
     * Relaci&oacute;n opcional muchos-a-uno con Divisa. Es la divisa a la que
     * se le define el l&iacute;mite de riesgo. Si el valor es null se refiere
     * a todas las divisas, es decir, a los l&iacute;mites de riesgo de la mesa
     * en cuesti&oacute;n.
     */
    private Divisa divisa;

    /**
     * Relaci&oacute;n muchos-a-uno con MesaCambio.
     */
    private MesaCambio mesaCambio;

    /**
     * Relaci&oacute;n muchos-a-uno con TipoLimite.
     */
    private TipoLimite tipoLimite;

    /**
     * Propiedad no persistente. Debe llenarse por el LimiteDao.
     */
    private double nivel;

    /**
     * Constante para el valor 100.0.
     */
    private static final double CIEN = 100.0;
    
    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -4188748995016622668L;
}
