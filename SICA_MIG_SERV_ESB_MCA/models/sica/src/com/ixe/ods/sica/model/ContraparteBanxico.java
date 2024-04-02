/*
 * $Id: ContraparteBanxico.java,v 1.12 2008/02/22 18:25:21 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.model;

import java.io.Serializable;

import com.ixe.ods.bup.model.Persona;

/**
 * Clase persistente para la tabla BUP_CONTRAPARTE_BANXICO.
 * Donde se consulta la informacion de los bancos y clientes mas frecuentes.
 *
 * @hibernate.class table="BUP_CONTRAPARTE_BANXICO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.ContraparteBanxico"
 * dynamic-update="true"
 *
 * @hibernate.query name="findCveBanxicoByIdPersona"
 * query="FROM ContraparteBanxico AS cb WHERE cb.personaSica.idPersona = ?"
 *
 * @author Javier Cordova
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:21 $
 */
public class ContraparteBanxico implements Serializable {

    /**
     * Constructor por default. No hace nada.
     */
    public ContraparteBanxico() {
        super();
    }

    /**
	 * Regresa el valor de idSaif.
	 *
	 * @hibernate.id column="ID_SAIF"
     * unsaved-value="null"
     * generator-class="assigned"
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
     * Regresa el valor de idPersonaTas.
     *
     * @hibernate.property column="ID_PERSONA_TAS"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getIdPersonaTas() {
        return idPersonaTas;
    }

    /**
     * Fija el valor de idPersonaTas.
     *
     * @param idPersonaTas El valor a asignar.
     */
    public void setIdPersonaTas(Integer idPersonaTas) {
        this.idPersonaTas = idPersonaTas;
    }
    
    /**
     * Regresa el valor de idPersonaIxec.
     *
     * @hibernate.property column="ID_PERSONA_IXEC"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getIdPersonaIxec() {
        return idPersonaIxec;
    }

    /**
     * Fija el valor de idPersonaIxec.
     *
     * @param idPersonaIxec El valor a asignar.
     */
    public void setIdPersonaIxec(Integer idPersonaIxec) {
        this.idPersonaIxec = idPersonaIxec;
    }
    
    /**
     * Regresa el valor de personaSica.
     * 
     * @hibernate.many-to-one column="ID_PERSONA_SICA"
     * cascade="none"
     * class="com.ixe.ods.bup.model.Persona"
     * outer-join="auto"
     * unique="false"
     * @return Persona.
     */
    public Persona getPersonaSica() {
        return personaSica;
    }

    /**
     * Fija el valor de personaSica.
     *
     * @param personaSica El valor a asignar.
     */
    public void setPersonaSica(Persona personaSica) {
        this.personaSica = personaSica;
    }
    
    /**
     * Regresa el valor de idPersonaSistebank.
     *
     * @hibernate.property column="ID_PERSONA_SISTEBANK"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getIdPersonaSistebank() {
        return idPersonaSistebank;
    }

    /**
     * Fija el valor de idPersonaSistebank.
     *
     * @param idPersonaSistebank El valor a asignar.
     */
    public void setIdPersonaSistebank(Integer idPersonaSistebank) {
        this.idPersonaSistebank = idPersonaSistebank;
    }
    
    /**
     * Regresa el valor de nombre.
     *
     * @hibernate.property column="NOMBRE"
     * not-null="false"
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
     * Regresa el valor de rfc.
     *
     * @hibernate.property column="RFC"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getRfc() {
        return rfc;
    }

    /**
     * Fija el valor de rfc.
     *
     * @param rfc El valor a asignar.
     */
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }
    
    /**
     * Regresa el valor de tipoContraparte.
     *
     * @hibernate.property column="TIPO_CONTRAPARTE"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getTipoContraparte() {
        return tipoContraparte;
    }

    /**
     * Fija el valor de tipoContraparte.
     *
     * @param tipoContraparte El valor a asignar.
     */
    public void setTipoContraparte(String tipoContraparte) {
        this.tipoContraparte = tipoContraparte;
    }

    /**
     * Identificador del registro y Clave de la Contraparte. 
     */
    private String idSaif;
    
    /**
     * Id Persona del Cliente para TAS.
     */
    private Integer idPersonaTas;
    
    /**
     * Id Persona del Cliente para IXEC.
     */
    private Integer idPersonaIxec;
    
    /**
     * Persona que el Cliente tiene en la BUP. Lo utiliza el SICA.
     */
    private Persona personaSica;
    
    /**
     * Id Persona del Cliente para SISTEBANK.
     */
    private Integer idPersonaSistebank;
    
    /**
     * Nombre del Cliente.
     */
    private String nombre;
    
    /**
     * RFC del Cliente.
     */
    private String rfc;
    
    /**
     * Tipo de Contraparte que el Cliente es para Banxico.
     */
    private String tipoContraparte;
    
}
