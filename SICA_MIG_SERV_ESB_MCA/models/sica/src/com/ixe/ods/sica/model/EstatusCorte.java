package com.ixe.ods.sica.model;

// default package

/**
 * Clase persistente para la tabla SC_ESTATUS_CORTE.
 *
 * @hibernate.class table="SC_ESTATUS_CORTE"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.EstatusCorte"
 * dynamic-update="true"
 *
 * @hibernate.query name="findEstatusCorteByEstatus" query="FROM EstatusCorte as e where e.estatusCorte = ?"
 *
 *
 * @author Banorte
 *          Diego Pazarán
 *
 * @version $Revision: 1.1.2.1 $
 */
public class EstatusCorte implements java.io.Serializable {
    // Fields    

    /** Identificador del registro */
    private String estatusCorte;

    /** Descripción del Estado del Corte */
    private String descripcion;

    // Constructors

    /** default constructor */
    public EstatusCorte() {
    }

    /** minimal constructor */
    public EstatusCorte(String estatusCorte) {
        this.estatusCorte = estatusCorte;
    }

    /** full constructor */
    public EstatusCorte(String estatusCorte, String descripcion) {
        this.estatusCorte = estatusCorte;
        this.descripcion = descripcion;
    }

    // Property accessors

    /**
     * Regresa el valor de estatusCorte.
     *
     * @return String.
     * 
     * @hibernate.id column="ESTATUS_CORTE"
     * unsaved-value="0"
     * generator-class="assigned"
     */
    public String getEstatusCorte() {
        return this.estatusCorte;
    }

    /**
     * Asigna estatusCorte
     *
     * @param estatusCorte con el valor a asignar.
     */
    public void setEstatusCorte(String estatusCorte) {
        this.estatusCorte = estatusCorte;
    }

    /**
     * Regresa el valor de estatusCorte.
     *
     * @return String.
     *
     * @hibernate.property column="DESCRIPCION"
     * not-null="true"
     * unique="false"
     */
    public String getDescripcion() {
        return this.descripcion;
    }

    /**
     * Asigna descripcion
     *
     * @param descripcion con el valor a asignar.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
