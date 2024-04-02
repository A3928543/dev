/*
 *  $Id: Broker.java,v 1.15.32.1 2010/09/09 00:34:29 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Entity class that represents a broker.
 * It is mapped to the <code>sc_broker</code> table.
 *
 * @hibernate.class table="sc_broker"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Broker"
 *
 * @hibernate.query name="findBrokerByIdPersona"
 * query="FROM Broker AS bk WHERE bk.id.personaMoral.idPersona = ?"
 *
 * @hibernate.query name="findBrokersByRazonSocial"
 * query="FROM com.ixe.ods.bup.model.PersonaMoral as pm WHERE rownum < 51 AND pm.razonSocial like ? AND pm.idPersona IN(SELECT bk.id.personaMoral.idPersona FROM Broker as bk) order by pm.razonSocial"
 * 
 * @author Gerardo Corzo, Javier Cordova
 * @version $Revision: 1.15.32.1 $ $Date: 2010/09/09 00:34:29 $
 */
public class Broker implements Serializable {

    /**
     * Constructor por default.
     */
    public Broker() {
        super();
        this.setStatus(Broker.STATUS_ACTIVO);
    }

    /**
     * @return Returns the id.
     * @hibernate.id
     */
    public BrokerPK getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(BrokerPK id) {
        this.id = id;
    }

    /**
     * Returns the claveReuters.
     *
     * @return String.
     * @hibernate.property column="clave_reuters"
     * not-null="false"
     * unique="false"
     */
    public String getClaveReuters() {
        return claveReuters;
    }

    /**
     * @param claveReuters The claveReuters to set.
     */
    public void setClaveReuters(String claveReuters) {
        this.claveReuters = claveReuters;
    }

    /**
     * Returns the status.
     *
     * @return String.
     * @hibernate.property column="STATUS"
     * not-null="true"
     * unique="false"
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * asigna el Estatus
     * @param status
     */
    public void setStatus(String status){
    	this.status=status;
    }

    /**
     * Regresa el valor de tipoBroker.
     *
     * @return String.
     * @hibernate.property column="tipo_broker"
     * not-null="true"
     * unique="false"
     */
    public String getTipoBroker() {
        return tipoBroker;
    }

    /**
     * Establece el valor de tipoBroker.
     *
     * @param tipoBroker El valor a asignar.
     */
    public void setTipoBroker(String tipoBroker) {
        this.tipoBroker = tipoBroker;
    }

    /**
     * Regresa el valor de pagoAnticipado.
     *
     * @return boolean.
     * @hibernate.property column="PAGO_ANTICIPADO"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public boolean isPagoAnticipado() {
        return pagoAnticipado;
    }

    /**
     * Fija el valor de pagoAnticipado.
     *
     * @param pagoAnticipado El valor a asignar.
     */
    public void setPagoAnticipado(boolean pagoAnticipado) {
        this.pagoAnticipado = pagoAnticipado;
    }
    
    /**
	 * Regresa el valor de idSaif.
	 *
	 * @hibernate.property column="ID_SAIF"
	 * not-null="false"
     * unique="false"
	 * @return String.
	 */
    public String getIdSaif() {
        return idSaif;
    }

    /**
     * Fija el valor de idSaif.
     *
     * @param idSaif El valor a asignar.
     */
    public void setIdSaif(String idSaif) {
        this.idSaif = idSaif;
    }
    
    /**
     * Regresa el valor de visibleKondor.
     *
     * @return boolean.
     * @hibernate.property column="VISIBLE_KONDOR"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public boolean isVisibleKondor() {
        return visibleKondor;
    }
    
    /**
     * Fija el valor de visibleKondor.
     *
     * @param visibleKondor El valor a asignar.
     */
    public void setVisibleKondor(boolean visibleKondor) {
        this.visibleKondor = visibleKondor;
    }

    /**
     * Implements a standard way to compare instances
     *
     * @param other Another object.
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     */
    public boolean equals(Object other) {
        if (!(other instanceof Broker)) {
            return false;
        }
        Broker object = (Broker) other;
        return new EqualsBuilder().append(this.getId(), object.getId()).isEquals();
    }

    /**
     * Hibernate lo utiliza para cuestiones de herencia.
     *
     * @return int    El HashCode identificador del objeto.
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    private BrokerPK id;

    /**
     * La clave de reuters del broker.
     */
    private String claveReuters;

    /**
     * Campo con el status
     */
    private String status;

    /**
     * El tipo de broker I)nstitucion Financiera | N)o institucion financiera.
     */
    private String tipoBroker;

    /**
     * Si el Broker (Contraparte) es sujeto o no a Pago Anticipado.
     */
    private boolean pagoAnticipado;
    
    /**
     * Si el Broker (Contraparte) es utilizado para operaciones de Kondor.
     */
    private boolean visibleKondor;
    
    /**
     * Clave de la Contraparte. 
     */
    private String idSaif;

    /**
     * Constante que representa a una Instituci&oacute;n Financiera
     */
    public final static String INSTITUCION_FINANCIERA = "I";

    /**
     * Constante que representa a una Instituci&oacute;n No Financiera
     */
    public final static String NO_INSTITUCION_FINANCIERA = "N";
    
    
    /**
     * Constante del status activo 
     */
    public final static String STATUS_ACTIVO="AC";
    
    
    /**
     * Constante del status inactivo
     */
    public final static String STATUS_INACTIVO="CA";
    
    
}
