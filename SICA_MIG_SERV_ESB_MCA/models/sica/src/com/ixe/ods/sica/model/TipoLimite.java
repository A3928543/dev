/*
 * $Id: TipoLimite.java,v 1.12 2008/02/22 18:25:23 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * <p>Clase persistente para la tabla SC_TIPO_LIMITE.</p>
 * <p>Representa a los tipos de l&iacute;mites de riesgo que existen. No existe
 * propiamente un cat&aacute;logo para dar de alta/baja/cambios sobre esta tabla
 * ya que cada tipo de l&iaucte;mite depende de que exista la clase de java para
 * evaluar su c&aacute;lculo.</p>
 * <p>Los l&iacute;mites de riesgo definidos a la fecha son:</p>
 * <ul>
 *   <li>L&iacute;mite de riesgo regulatorio</li>
 *   <li>L&iacute;mite de riesgo de la posici&oacute;n larga.</li>
 *   <li>L&iacute;mite de riesgo de la posici&oacute;n larga intrad&iacute;a.</li>
 *   <li>L&iacute;mite de riesgo de la posici&oacute;n corta.</li>
 *   <li>L&iacute;mite de riesgo de la posici&oacute;n corta intrad&iacute;a.</li>
 *   <li>L&iacute;mite de riesgo VaR.</li>
 *   <li>L&iacute;mite de riesgo Stop Loss.</li>
 * </ul>
 *
 * @hibernate.class table="SC_TIPO_LIMITE"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.TipoLimite"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:23 $
 */
public class TipoLimite implements Serializable {

    /**
     * Constructor por default, no hace nada.
     */
    public TipoLimite() {
        super();
    }

    /**
     * Regresa el valor de idTipoLimite.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_TIPO_LIMITE"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_TIPO_LIMITE_SEQ"
     * @return int.
     */
    public int getIdTipoLimite() {
        return idTipoLimite;
    }

    /**
     * Fija el valor de idTipoLimite.
     *
     * @param idTipoLimite El valor a asignar.
     */
    public void setIdTipoLimite(int idTipoLimite) {
        this.idTipoLimite = idTipoLimite;
    }

    /**
     * Regresa el valor de nombre.
     *
     * @hibernate.property column="NOMBRE"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Fija el valor de nombre.
     *
     * @param nombre El valor a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Regresa el valor de metodoCalculo.
     *
     * @hibernate.property column="METODO_CALCULO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getMetodoCalculo() {
        return metodoCalculo;
    }

    /**
     * Fija el valor de metodoCalculo.
     *
     * @param metodoCalculo El valor a asignar.
     */
    public void setMetodoCalculo(String metodoCalculo) {
        this.metodoCalculo = metodoCalculo;
    }

    /**
     * Regresa el valor de alCierre.
     *
     * @hibernate.property column="ES_AL_CIERRE"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return boolean.
     */
    public boolean isAlCierre() {
        return alCierre;
    }

    /**
     * Fija el valor de alCierre.
     *
     * @param alCierre El valor a asignar.
     */
    public void setAlCierre(boolean alCierre) {
        this.alCierre = alCierre;
    }

    /**
     * Regresa el valor de global.
     *
     * @hibernate.property column="ES_GLOBAL"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return boolean.
     */
    public boolean isGlobal() {
        return global;
    }

    /**
     * Fija el valor de global.
     *
     * @param global El valor a asignar.
     */
    public void setGlobal(boolean global) {
        this.global = global;
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
        if (!(other instanceof TipoLimite)) {
            return false;
        }
        TipoLimite castOther = (TipoLimite) other;
        return new EqualsBuilder().append(this.getIdTipoLimite(), castOther.getIdTipoLimite()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdTipoLimite()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    private int idTipoLimite;

    /**
     * Concepto con el que se lista el tipo de l&iacute;mite de  riesgo. Este
     * concepto es el que se lista en las pantallas de la definici&oacute;n y
     * monitoreo de l&iacute;mites de riesgo.
     */
    private String nombre;

    /**
     * Variable que se refiere al servicio que debe llamarse para realizar el
     * c&acute;lculo del nivel del l&iacute;mite.
     */
    private String metodoCalculo;

    /**
     * Define si el tipo de l&iacute;mite de riesgo se eval&uacute;a durante la
     * operaci&oacute;n del d&iacute;a (O) o bien si s&oacute;lo se
     * eval&uacute;a en el proceso de cierre del d&iacute;a (C). La pantalla de
     * monitor de riesgo no utiliza dicho campo y eval&uacute;a todos los tipos
     * de l&iacute;mites de riesgo definido.
     */
    private boolean alCierre;

    /**
     * Indica si el tipo de l&iacute;mite de riesgo es global (regulatorio) o
     * bien, si se calcula por Mesa y/o Divisa.
     */
    private boolean global;

    /**
     * Relaci&oacute;n muchos-a-uno con Divisa. Indica en qu&eacute; divisa se
     * expresa el l&iacute;mite de riesgo. Si se tiene el valor, expresa la
     * divisa en que se define el l&iacute;mite (e. g. MXN para el tipo de
     * l&iacute;mite de riesgo VaR). Si el valor es null, se refiere a que el
     * l&iacute;mite se encuentra en la divisa en la que se est&aacute;
     * consultando la posici&oacute;n. Si la tabla no tiene divisa (i. e. son
     * los l&iacute;mites del total de la posici&oacute;n de una mesa), este
     * l&iacute;mite es expresado en USD.
     */
    private Divisa _divisa;

    /**
     * Constante para mostrar el Nombre del L&iacute;mite
     */
    public static final String RIESGO_REGULATORIO = "Riesgo Regulatorio";

    /**
     * Constante para mostrar el Nombre del L&iacute;mite
     */
    public static final String VAR = "VaR";

    /**
     * Constante para mostrar el Nombre del L&iacute;mite
     */
    public static final String POSICION_LARGA = "Posición Larga";

    /**
     * Constante para mostrar el Nombre del L&iacute;mite
     */
    public static final String POSICION_CORTA = "Posición Corta";

    /**
     * Constante para mostrar el Nombre del L&iacute;mite
     */
    public static final String STOP_LOSS = "Stop Loss";

    /**
     * Constante para mostrar el Nombre del L&iacute;mite
     */
    public static final String POSICION_LARGA_INTRADIA = "Posición Larga Intradía";

    /**
     * Constante para mostrar el Nombre del L&iacute;mite
     */
    public static final String POSICION_CORTA_INTRADIA = "Posición Corta Intradía";

    /**
     * Constante para mostrar el Id del L&iacute;mite Riesgo Regulatorio.
     */
    public static final int RR = 1;

    /**
     * Constante para mostrar el Id del L&iacute;mite Value at Risk.
     */
    public static final int V = 2;

    /**
     * Constante para mostrar el Id del L&iacute;mite Posición Larga.
     */
    public static final int PL = 3;

    /**
     * Constante para mostrar el Id del L&iacute;mite Posición Corta.
     */
    public static final int PC = 4;

    /**
     * Constante para mostrar el Id del L&iacute;mite Stop Loss.
     */
    public static final int SL = 5;

    /**
     * Constante para mostrar el Id del L&iacute;mite Posición Larga Intradía.
     */
    public static final int PLI = 6;

    /**
     * Constante para mostrar el Id del L&iacute;mite Posición Corta Intradía.
     */
    public static final int PCI = 7;

}
