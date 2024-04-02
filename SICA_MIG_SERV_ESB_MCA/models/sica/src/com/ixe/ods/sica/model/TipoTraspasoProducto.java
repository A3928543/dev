/*
 * $Id: TipoTraspasoProducto.java,v 1.12 2008/02/22 18:25:21 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import java.io.Serializable;

/**
 * Clase persistente para la tabla SC_TIPO_TRASPASO_PRODUCTO.
 *
 * @hibernate.class table="SC_TIPO_TRASPASO_PRODUCTO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.TipoTraspasoProducto"
 * dynamic-update="true"
 *
 * @hibernate.query name="findAllTiposTraspasoProducto"
 * query="FROM TipoTraspasoProducto AS ttp"
 *
 * @author Javier Cordova (jcordova)
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:21 $
 */
public class TipoTraspasoProducto implements Serializable {

	/**
	 * Constructor por Default.
	 */
	public TipoTraspasoProducto() {
        super();
    }
	
	/**
     * Regresa el valor de mnemonicoTraspaso.
     *
     * @hibernate.id generator-class="assigned"
     * column="MNEMONICO_TRASPASO"
     * unsaved-value="null"
     * @return String.
     */
    public String getMnemonicoTraspaso() {
        return mnemonicoTraspaso;
    }

    /**
     * Fija el valor de mnemonicoTraspaso.
     *
     * @param mnemonicoTraspaso El valor a asignar.
     */
    public void setMnemonicoTraspaso(String mnemonicoTraspaso) {
        this.mnemonicoTraspaso = mnemonicoTraspaso;
    }
    
    /**
     * Regresa el valor de descripcion.
     *
     * @hibernate.property column="DESCRIPCION"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Fija el valor de descripcion.
     *
     * @param descripcion El valor a asignar.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    /**
     * Regresa el valor de neteo.
     *
     * @hibernate.property column="NETEO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getNeteo() {
        return neteo;
    }
    
    /**
     * Fija el valor de neteo.
     *
     * @param neteo El valor a asignar.
     */
    public void setNeteo(String neteo) {
        this.neteo = neteo;
    }
    
    /**
     * Regresa el valor de <code>de</code>.
     *
     * @hibernate.property column="DE"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getDe() {
        return de;
    }
    
    /**
     * Fija el valor de <code>de</code>.
     *
     * @param de El valor a asignar.
     */
    public void setDe(String de) {
        this.de = de;
    }
    
    /**
     * Regresa el valor de deOper.
     *
     * @hibernate.property column="DE_OPER"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getDeOper() {
        return deOper;
    }
    
    /**
     * Fija el valor de deOper.
     *
     * @param deOper El valor a asignar.
     */
    public void setDeOper(String deOper) {
        this.deOper = deOper;
    }
    
    /**
     * Regresa el valor de deSigno.
     *
     * @hibernate.property column="DE_SIGNO"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getDeSigno() {
        return deSigno;
    }
    
    /**
     * Fija el valor de deSigno.
     *
     * @param deSigno El valor a asignar.
     */
    public void setDeSigno(int deSigno) {
        this.deSigno = deSigno;
    }
    
    /**
     * Regresa el valor de <code>a</code>.
     *
     * @hibernate.property column="A"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getA() {
        return a;
    }
    
    /**
     * Fija el valor de <code>a</code>.
     *
     * @param a El valor a asignar.
     */
    public void setA(String a) {
        this.a = a;
    }
    
    /**
     * Regresa el valor de aOper.
     *
     * @hibernate.property column="A_OPER"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getAOper() {
        return aOper;
    }
    
    /**
     * Fija el valor de aOper.
     *
     * @param aOper El valor a asignar.
     */
    public void setAOper(String aOper) {
        this.aOper = aOper;
    }
    
    /**
     * Regresa el valor de aSigno.
     *
     * @hibernate.property column="A_SIGNO"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getASigno() {
        return aSigno;
    }
    
    /**
     * Fija el valor de aSigno.
     *
     * @param aSigno El valor a asignar.
     */
    public void setASigno(int aSigno) {
        this.aSigno = aSigno;
    }
    
    /**
     * Regresa el valor de valMonto.
     *
     * @hibernate.property column="VAL_MONTO"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getValMonto() {
        return valMonto;
    }
    
    /**
     * Fija el valor de valMonto.
     *
     * @param valMonto El valor a asignar.
     */
    public void setValMonto(String valMonto) {
        this.valMonto = valMonto;
    }
    
    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof TipoTraspasoProducto)) {
            return false;
        }
        TipoTraspasoProducto castOther = (TipoTraspasoProducto) other;
        return new EqualsBuilder().append(this.getMnemonicoTraspaso(), castOther.getMnemonicoTraspaso()).isEquals();
    }
    
    //Variables de instancia
    /**
     * Identificador Mnemonico del Registro <code>TipoTraspasoProducto</code>.
     */
    private String mnemonicoTraspaso;
    
    /**
     * Descripcion del Mnemonico del <code>TipoTraspasoProducto</code>.
     */
    private String descripcion;
    
    /**
     * Indica si se realiza un neteo en el traspaso o no.
     */
    private String neteo;
    
    /**
     * Liga logica a TES_FORMA_LIQUIDACION.
     */
    private String de;
    
    /**
     * Tipo de operacion para SC_POSICION_LOG.
     */
    private String deOper;
    
    /**
     * Posibles valores 1 o -1.
     */
    private int deSigno;
    
    /**
     * Liga logica a TES_FORMA_LIQUIDACION.
     */
    private String a;
    
    /**
     * Tipo de oepracion para SC_POSICION_LOG.
     */
    private String aOper;
    
    /**
     * Posibles valores 1 o -1.
     */
    private int aSigno;
    
    /**
     * Tipo de monto de la posicion a validar.
     */
    private String valMonto;
}
