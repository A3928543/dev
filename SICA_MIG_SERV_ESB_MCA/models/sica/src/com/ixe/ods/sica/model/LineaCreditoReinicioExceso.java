package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase persistente para la tabla SC_LC_REINICIO_EXCESO.
 *
 * @hibernate.class table="SC_LC_REINICIO_EXCESO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.LineaCreditoReinicioExceso"
 * dynamic-update="true"
 * 
 * @hibernate.query name="findReinicioTrimestre"
 * query= "FROM LineaCreditoReinicioExceso AS p WHERE p.procesoEjecutado = '0' ORDER BY p.fechaEjecucion desc"
 * 
 */
public class LineaCreditoReinicioExceso implements Serializable 
{
	public static final String REINICIO_NO_EJECUTADO = "0";
	private static final long serialVersionUID = 1L;
	private Integer idReinicioExceso;
	private Date fechaEjecucion;
	private String procesoEjecutado;
	
	/**
     * Regresa el valor del id.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_REINICIO_EXCESO"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_LC_REINICIO_EXCESO_SEQ"
     * @return Integer.
     */
    public Integer getIdReinicioExceso() {
        return idReinicioExceso;
    }

    /**
     * Fija el valor del id.
     *
     * @param idReinicioExceso El valor a asignar.
     */
    public void setIdReinicioExceso(Integer idReinicioExceso) {
        this.idReinicioExceso = idReinicioExceso;
    }
    
    /**
     * Regresa el valor de la fecha de ejecucion.
     *
     * @hibernate.property column="FECHA_EJECUCION"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return Date.
     */
    public Date getFechaEjecucion() {
        return fechaEjecucion;
    }

    /**
     * Fija el valor de fecha de ejecucion.
     *
     * @param fechaEjecucion El valor a asignar.
     */
    public void setFechaEjecucion(Date fechaEjecucion) {
        this.fechaEjecucion = fechaEjecucion;
    }
    
    /**
     * Regresa el valor de proceso ejecutado.
     *
     * @hibernate.property column="PROCESO_EJECUTADO"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return String.
     */
    public String getProcesoEjecutado() {
        return procesoEjecutado;
    }

    /**
     * Fija el valor de procesoEjecutado.
     *
     * @param procesoEjecutado El valor a asignar.
     */
    public void setProcesoEjecutado(String procesoEjecutado) {
        this.procesoEjecutado = procesoEjecutado;
    }
}
