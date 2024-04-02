/*
 * $Id: LineaOperacion.java,v 1.13 2008/02/22 18:25:21 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la tabla SC_LINEA_OPERACION. Representa la
 * l&iacute;nea de operaci&oacute;n por Instituci&oacute;n Financiera.
 *
 * @hibernate.class table="SC_LINEA_OPERACION"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.LineaOperacion"
 * dynamic-update="true"
 * 
 * @hibernate.query name="findLineaOperacionByBroker"
 * query="FROM LineaOperacion AS lo WHERE lo.idBroker = ?"
 * 
 * @hibernate.query name="findLineaOperacionByBrokerAndStatus"
 * query="FROM LineaOperacion AS lo WHERE lo.idBroker = ? AND lo.statusLinea = ?"
 * 
 * @hibernate.query name="findLineasOperacionByStatus"
 * query="FROM LineaOperacion AS lo WHERE lo.statusLinea = ?"
 *
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.13 $ $Date: 2008/02/22 18:25:21 $
 */
public class LineaOperacion implements Serializable {

    /**
     * Constructor vacio. No hace nada.
     */
    public LineaOperacion() {
        super();
    }

    /**
     * Regresa el valor de idLineaOperacion.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_LINEA_OPERACION"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_LINEA_OPERACION_SEQ"
     * @return int.
     */
    public int getIdLineaOperacion() {
        return idLineaOperacion;
    }

    /**
     * Fija el valor de idLineaOperacion.
     *
     * @param idLineaOperacion El valor a asignar.
     */
    public void setIdLineaOperacion(int idLineaOperacion) {
        this.idLineaOperacion = idLineaOperacion;
    }

    /**
     * Regresa el valor de idBroker.
     *
     * @hibernate.property column="ID_BROKER"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getIdBroker() {
        return idBroker;
    }

    /**
     * Fija el valor de idBroker.
     *
     * @param idBroker El valor a asignar.
     */
    public void setIdBroker(int idBroker) {
        this.idBroker = idBroker;
    }

    /**
     * Regresa el valor de importe.
     *
     * @hibernate.property column="IMPORTE"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getImporte() {
        return importe;
    }

    /**
     * Fija el valor de importe.
     *
     * @param importe El valor a asignar.
     */
    public void setImporte(double importe) {
        this.importe = importe;
    }

    /**
     * Regresa el valor de usoLinea.
     *
     * @hibernate.property column="USO_LINEA"
     * not-null="true"
     * unique="false"
     * @return double.
     */
    public double getUsoLinea() {
        return usoLinea;
    }

    /**
     * Fija el valor de usoLinea.
     *
     * @param usoLinea El valor a asignar.
     */
    public void setUsoLinea(double usoLinea) {
        this.usoLinea = usoLinea;
    }

    /**
     * Regresa el valor de numeroExcepciones.
     *
     * @hibernate.property column="NUMERO_EXCEPCIONES"
     * not-null="true"
     * unique="false"
     * @return int.
     */
    public int getNumeroExcepciones() {
        return numeroExcepciones;
    }

    /**
     * Fija el valor de numeroExcepciones.
     *
     * @param numeroExcepciones El valor a asignar.
     */
    public void setNumeroExcepciones(int numeroExcepciones) {
        this.numeroExcepciones = numeroExcepciones;
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
     * Regresa el valor de statusLinea.
     *
     * @hibernate.property column="STATUS_LINEA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getStatusLinea() {
        return statusLinea;
    }

    /**
     * Fija el valor de statusLinea.
     *
     * @param statusLinea El valor a asignar.
     */
    public void setStatusLinea(String statusLinea) {
        this.statusLinea = statusLinea;
    }

    /**
     * Regresa el valor de logs.
     *
     * @hibernate.set inverse="false"
     * lazy="true"
     * cascade="none"
     * @hibernate.collection-key column="ID_LINEA_OPERACION"
     * @hibernate.collection-one-to-many class="com.ixe.ods.sica.model.LineaOperacionLog"
     * @return Set.
     */
    public Set getLogs() {
        return _logs;
    }

    /**
     * Fija el valor de logs.
     *
     * @param logs El valor a asignar.
     */
    public void setLogs(Set logs) {
        _logs = logs;
    }
    
    /**
     * Obtiene la Descripcion del Status de la Linea.
     * 
     * @return String
     */
    public String getDescripcionStatus() {
     	if (STATUS_SOLICITUD.equals(statusLinea)) {
            return "Solicitada";
        }
     	else if (STATUS_AUTORIZADA.equals(statusLinea)) {
            return "Autorizada";
        }
        else if (STATUS_SUSPENDIDA.equals(statusLinea)) {
            return "Suspendida";
        }
        return null;
     }
    
    /**
     * Regresa el Saldo Disponible de la L&iacute;nea de Operacion.
     *
     * @return El Saldo Disponible.
     */
    public double getSaldo() {
    	return getImporte() - getUsoLinea();
    }
    
    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof LineaOperacion)) {
            return false;
        }
        LineaOperacion castOther = (LineaOperacion) other;
        return new EqualsBuilder().append(this.getIdLineaOperacion(), castOther.
                getIdLineaOperacion()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdLineaOperacion()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    private int idLineaOperacion;

    /**
     * TODO Verificar si es cierto esto con Alfonso Rivadeneyra y Ricardo Legorreta
     * Aplican las l&iacute;neas de operaci&oacute;n a las instituciones financieras y a las que
     * no lo son.
     */
    private int idBroker;

    /**
     * Monto m&aacute;ximo de la l&iacute;nea de operaci&oacute;n.
     */
    private double importe;

    /**
     * Monto en uso de la l&iacute;nea de operaci&oacute;n.
     */
    private double usoLinea;

    /**
     * El total de excepciones que se han tenido.
     */
    private int numeroExcepciones;

    /**
     * Ultima vez que se utiliz&oacute; la l&iacute;nea de Operaci&oacute;n.
     */
    private Date ultimaModificacion;

    /**
     * SO)licitud, OK: Autorizada, SU)spendida.
     * El valor por default es STATUS_SOLICITUD.
     */
    private String statusLinea = STATUS_SOLICITUD;

    /**
     * Relaci&oacute;n uno-a-muchos con LineaOperacionLog.
     */
    private Set _logs;
    
    /**
     * Constante Status Solicitud.
     */
    public static final String STATUS_SOLICITUD = "SO";

    /**
     * Constante Status Autorizada.
     */
    public static final String STATUS_AUTORIZADA = "OK";

    /**
     * Constante Status Suspendida.
     */
    public static final String STATUS_SUSPENDIDA = "SU";
    
}