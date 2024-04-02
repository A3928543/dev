/*
 * $Id: LogAuditoria.java,v 1.3.18.2.28.2 2014/01/07 19:38:48 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 - 2013 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.ixe.ods.seguridad.model.IUsuario;

/**
 * Clase persistente para la tabla SC_LOG_AUDITORIA.
 *
 * @hibernate.class table="SC_LOG_AUDITORIA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.LogAuditoria"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.3.18.2.28.2 $ $Date: 2014/01/07 19:38:48 $
 */
public class LogAuditoria implements Serializable {

    /**
     * Constructor vac&iacute;o.
     */
    public LogAuditoria() {
        super();
    }

    /**
     * Constructor que inicializa todas las variables de instancia.
     *
     * @param direccionIp Direcci&oacute;n IP del usuario que realiz&oacute; la operaci&oacute;n.
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idDeal El n&uacute;mero de deal involucrado (opcional).
     * @param usuario El usuario que realiz&oacute; la operaci&oacute;n.
     * @param tipoOperacion El tipo de operaci&oacute;n realizada.
     * @param datosAdicionales Cadena con datos adicionales del log de auditor&iacute;a (opcional).
     */
    public LogAuditoria(String ticket, String direccionIp, Integer idDeal, IUsuario usuario,
                        String tipoOperacion, String datosAdicionales) {
        this();
        this.ticket = ticket;
        this.direccionIp = direccionIp;
        this.idDeal = idDeal;
        this.idPersona = usuario.getPersona().getIdPersona().intValue();
        this.idUsuario = usuario.getIdUsuario();
        this.tipoOperacion = tipoOperacion;
        this.datosAdicionales = datosAdicionales;
    }

    /**
     * Regresa el valor de idLogAuditoria.
     *
     * @return String.
     * @hibernate.id generator-class="uuid.hex"
     * column="ID_LOG_AUDITORIA"
     * unsaved-value="null"
     */
    public String getIdLogAuditoria() {
        return idLogAuditoria;
    }

    /**
     * Establece el valor de idLogAuditoria.
     *
     * @param idLogAuditoria El valor a asignar.
     */
    public void setIdLogAuditoria(String idLogAuditoria) {
        this.idLogAuditoria = idLogAuditoria;
    }

    /**
     * Regresa el valor de datosAdicionales.
     *
     * @return String.
     * @hibernate.property column="DATOS_ADICIONALES"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public String getDatosAdicionales() {
        return datosAdicionales;
    }

    /**
     * Establece el valor de datosAdicionales.
     *
     * @param datosAdicionales El valor a asignar.
     */
    public void setDatosAdicionales(String datosAdicionales) {
        if (datosAdicionales != null && datosAdicionales.length() > NUM_512) {
            datosAdicionales = datosAdicionales.substring(0, NUM_511);
        }
        this.datosAdicionales = datosAdicionales;
    }

    /**
     * Regresa el valor de direccionIp.
     *
     * @return String.
     * @hibernate.property column="DIRECCION_IP"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public String getDireccionIp() {
        return direccionIp;
    }

    /**
     * Establece el valor de direccionIp.
     *
     * @param direccionIp El valor a asignar.
     */
    public void setDireccionIp(String direccionIp) {
        this.direccionIp = direccionIp;
    }

    /**
     * Regresa el valor de fecha.
     *
     * @return String.
     * @hibernate.property column="FECHA"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Establece el valor de fecha.
     *
     * @param fecha El valor a asignar.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Regresa el valor de ticket.
     *
     * @return String.
     * @hibernate.property column="TICKET"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public String getTicket() {
        return ticket;
    }

    /**
     * Establece el valor de ticket.
     *
     * @param ticket El valor a asignar.
     */
    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    /**
     * Regresa el valor de idDeal.
     *
     * @return Integer.
     * @hibernate.property column="ID_DEAL"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public Integer getIdDeal() {
        return idDeal;
    }

    /**
     * Establece el valor de idDeal.
     *
     * @param idDeal El valor a asignar.
     */
    public void setIdDeal(Integer idDeal) {
        this.idDeal = idDeal;
    }

    /**
     * Regresa el valor de idPersona.
     *
     * @return int.
     * @hibernate.property column="ID_PERSONA"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public int getIdPersona() {
        return idPersona;
    }

    /**
     * Establece el valor de idPersona.
     *
     * @param idPersona El valor a asignar.
     */
    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    /**
     * Regresa el valor de idUsuario.
     *
     * @return int.
     * @hibernate.property column="ID_USUARIO"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el valor de idUsuario.
     *
     * @param idUsuario El valor a asignar.
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Regresa el valor de tipoOperacion.
     *
     * @return String.
     * @hibernate.property column="TIPO_OPERACION"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public String getTipoOperacion() {
        return tipoOperacion;
    }

    /**
     * Establece el valor de tipoOperacion.
     *
     * @param tipoOperacion El valor a asignar.
     */
    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @return boolean.
     * @see java.lang.Object#equals(java.lang.Object).
     */
    public boolean equals(Object other) {
        if (!(other instanceof LogAuditoria)) {
            return false;
        }
        LogAuditoria castOther = (LogAuditoria) other;
        return new EqualsBuilder().append(this.getIdLogAuditoria(),
                castOther.getIdLogAuditoria()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @return <code>int</code>
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdLogAuditoria()).toHashCode();
    }

    /**
     * Regresa una cadena con la descripci&oacute;n de los campos de esta instancia.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this).append("ticket", ticket).
                append("direccionIp", direccionIp).append("fecha", fecha).
                append("idDeal", idDeal).append("idPersona", idPersona).
                append("idUsuario", idUsuario).append("tipoOperacion", tipoOperacion).
                append("datosAdicionales", datosAdicionales).toString();
    }

    /**
     * La llave primaria del registro.
     */
    private String idLogAuditoria;

    /**
     * Cadena con datos adicionales del log de auditor&iacute;a (opcional).
     */
    private String datosAdicionales;

    /**
     * Direcci&oacute;n IP del usuario que realiz&oacute; la operaci&oacute;n.
     */
    private String direccionIp;

    /**
     * La fecha de la operaci&oacute;n.
     */
    private Date fecha = new Date();

    /**
     * El ticket de la sesi&oacute;n del usuario.
     */
    private String ticket;

    /**
     * El n&uacute;mero de deal involucrado (opcional).
     */
    private Integer idDeal;

    /**
     * El n&uacute;mero de la persona que realiz&oacute; la operaci&oacute;n.
     */
    private int idPersona;

    /**
     * El n&uacute;mero de usuario que realiz&oacute; la operaci&oacute;n.
     */
    private int idUsuario;

    /**
     * El tipo de operaci&oacute;n realizada.
     */
    private String tipoOperacion;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -4525945020313445857L;

    /**
     * Constante para el tipo de operaci&oacute;n de la consulta general de deals.
     */
    public static final String CONSULTA_GRAL_DEALS = "Consulta General de Deals";

    /**
     * Constante para el tipo de operaci&oacute;n en la que el promotor visualiza un deal para
     * editarlo.
     */
    public static final String EDICION_DEAL = "Edici\u00f3n de deal";
    
    /**
     * Constante para el tipo de operaci&oacute;n en la que un detalle de deal sobrepasa el
     * l&iacute;mite normal de compra/venta de efectivo, pero queda por debajo del
     * l&iacute;mite ixe.
     */
    public static final String NOTIFICACION_LIMITE_OPERACION =
    	"Notificaci\u00f3n de l\u00edmite de operaci\u00f3n";
    
    /**
     * Constante para el tipo de operaci&oacute;n en la que un detalle de deal ya no puede operar
     * compra/venta de efectivo.
     */
    public static final String EXCEDIO_LIMITE_OPERACION =
    	"Excedi\u00f3 el l\u00edmite de operaci\u00f3n";
    
    /**
     * Constante para el tipo de operaci&oacute;n en la que un detalle de deal ya no puede operar
     * compra/venta de efectivo desde el Teller.
     */
    public static final String EXCEDIO_LIMITE_OPERACION_TELLER =
    	"Excedi\u00f3 el l\u00edmite de operaci\u00f3n.(Teller)";

    /**
     * Constante para el tipo de operaci&oacute;n de la que el usuario revisa los datos de un deal
     * en particular.
     */
    public static final String CONSULTA_DATOS_DEAL = "Consulta de datos del deal";

    /**
     * Constante para el tipo de operaci&oacute;n en la que el usuario ingresa manualmente registros
     * para la tabla SC_POLIZA.
     */
    public static final String GEN_MANUAL_POLIZAS = "Generaci\u00f3n Manual P\u00f3lizas";

    /**
     * Constante para el tipo de operaci&oacute;n en la que el usuario consulta el reporte de
     * utilidades.
     */
    public static final String REPORTE_UTILIDADES = "Reporte de Utilidades";

    /**
     * Constante para el tipo de operaci&oacute;n en la que el usuario modifica los datos de
     * facturaci&oacute;n de un deal.
     */
    public static final String CAMBIO_DATOS_FACT = "Cambio Datos Fact.";

    /**
     * Constante para el tipo de operaci&oacute;n en la que el usuario modifica los datos de
     * comprobante de un deal.
     */
    public static final String CAMBIO_DATOS_COMP = "Cambio Datos Comp.";

    /**
     * Constante para el valor entero 511.
     */
    public static final int NUM_511 = 511;

    /**
     * Constante para el valor entero 512.
     */
    public static final int NUM_512 = 512;
    
    /**
     * Constante para el tipo de operacion de Generaci￳n de Reporte de Compra Venta
     */
    public static final String REPORTE_OPS_COMPRA_VENTA = "Reporte de Operaciones de Compra Venta";
}
