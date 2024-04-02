/*
 * $Id: TipoLineaCredito.java,v 1.13 2008/02/22 18:25:23 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la tabla SC_TIPO_LINEA_CREDITO.
 *
 * @hibernate.class table="SC_TIPO_LINEA_CREDITO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.TipoLineaCredito"
 * dynamic-update="true"
 *
 * @author Javier Cordova (jcordova)
 * @version  $Revision: 1.13 $ $Date: 2008/02/22 18:25:23 $
 */
public class TipoLineaCredito implements Serializable {

    /**
	 * Constructor por Default.
	 */
	public TipoLineaCredito() {
        super();
    }
	
	/**
     * Regresa el valor  de usaLineaCredito.
     *
     * @hibernate.id generator-class="assigned"
     * column="USA_LINEA_CREDITO"
     * unsaved-value="null"
     * @return int.
     */
    public int getUsaLineaCredito() {
        return usaLineaCredito;
    }

    /**
     * Fija el valor de usaLineaCredito.
     *
     * @param usaLineaCredito El valor a asignar.
     */
    public void setUsaLineaCredito(int usaLineaCredito) {
        this.usaLineaCredito = usaLineaCredito;
    }

    /**
     * Regresa el valor de descripcion.
     *
     * @hibernate.property column="DESCRIPCION"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Fija el valor de descripcion.
     *
     * @param descripcion La descripcion a asignar.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Regresa el valor de tipoLineaCredito.
     *
     * @hibernate.set inverse="false"
     * lazy="true"
     * cascade="delete"
     * @hibernate.collection-key column="ID_LINEA_CREDITO"
     * @hibernate.collection-one-to-many class="com.ixe.ods.sica.model.LineaCredito"
     * @return Set.
     */
    public Set getLineaCredito() {
        return lineaCredito;
    }

    /**
     * Fija el valor de lineaCredito.
     *
     * @param lineaCredito El valor a asignar.
     */
    public void setLineaCredito(Set lineaCredito) {
        this.lineaCredito = lineaCredito;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof TipoLineaCredito)) {
            return false;
        }
        TipoLineaCredito castOther = (TipoLineaCredito) other;
        return new EqualsBuilder().append(this.getUsaLineaCredito(),
                castOther.getUsaLineaCredito()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getUsaLineaCredito()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    private int usaLineaCredito;

    /**
     * La clave del producto.
     */
    private String descripcion;

    /**
     * * Relaci&oacute; uno-a-muchos con LineaCredito.
     */
    private Set lineaCredito;

    /**
     * Constante Tipo de Transferencia.
     */
    public static final int TIPO_TRANSFERENCIA = 1;
    
    /**
     * Constante Tipo de Remesas en Camino.
     */
    public static final int TIPO_REMESAS_EN_CAMINO = 2;
    
    /**
     * Constante Tipo Combro Inmediato.
     */
    public static final int TIPO_COBRO_INMEDIATO = 3;

    /**
     * Constante para los tipos de l&iacute;neas de cr&eacute;dito que corresponden a pago
     * anticipado.
     */
    public static final int[] TIPOS_PAGO_ANTICIPADO = new int[] {
            TipoLineaCredito.TIPO_TRANSFERENCIA, TipoLineaCredito.TIPO_REMESAS_EN_CAMINO,
            TipoLineaCredito.TIPO_COBRO_INMEDIATO
    };

    /**
     * Constante para los tipos de l&iacute;neas de cr&eacute;dito que corresponden a Toma en Firme.
     */
    public static final int[] TIPOS_TOMA_EN_FIRME = new int[] {
            TipoLineaCredito.TIPO_REMESAS_EN_CAMINO, TipoLineaCredito.TIPO_COBRO_INMEDIATO
    };

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -653622451745475423L;
}