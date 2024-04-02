/*
 * $Id: Actividad.java,v 1.28.10.1 2010/10/08 01:32:41 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.sica.SicaException;

/**
 * Clase persistente para la tabla SC_ACTIVIDAD.
 *
 * @hibernate.class table="SC_ACTIVIDAD"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Actividad"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.28.10.1 $ $Date: 2010/10/08 01:32:41 $
 */
public class Actividad implements Serializable {

    /**
     * Constructor por default.
     */
    public Actividad() {
        super();
    }

    /**
     * Constructor con par&aacute;metros.
     *
     * @param proceso La clave del proceso.
     * @param nombreActividad La clave de la actividad.
     * @param deal El deal ligado a esta actividad.
     * @param det El detalle de deal ligado a esta actividad (opcional).
     */
    public Actividad(String proceso, String nombreActividad, Deal deal, DealDetalle det) {
        this();
        this.proceso = proceso;
        this.nombreActividad = nombreActividad;
        this.deal = deal;
        this.dealDetalle = det;
    }

    /**
     * Cambia el status de la actividad, asigna la fecha de terminaci&oacute;n como la fecha actual
     * del sistema, y asigna el usuario y resultado proporcionados.
     *
     * @param usuario El usuario que complet&oacute; la actividad.
     * @param status El status de la actividad.
     * @param resultado El Tipo de resultado (Autorizado, No autorizado, etc.)
     */
    private void completar(IUsuario usuario, String status, String resultado) {
        setStatus(status);
        setFechaTerminacion(new Date());
        setUsuario(usuario);
        setResultado(resultado);
    }

    /**
     * Completa la actividad, asignando el status 'Terminado' y el resultado especificado.
     *
     * @see #completar(com.ixe.ods.seguridad.model.IUsuario, String, String).
     * @param usuario El usuario que complet&oacute; la actividad.
     * @param resultado El Tipo de resultado (Autorizado, No autorizado, etc.)
     * @throws SicaException Si la actividad no estaba en status pendiente.
     */
    public void terminar(IUsuario usuario, String resultado) throws SicaException {
        if (STATUS_PENDIENTE.equals(getStatus().trim())) {
            completar(usuario, STATUS_TERMINADO, resultado);
        }
        else {
            throw new SicaException("No se puede terminar la actividad porque no " +
                    "estaba en status Pendiente.");
        }
    }

    /**
     * Cancela la actividad, asignando el status 'Cancelado' y el resultado especificado. No se
     * cancela la actividad de sobreprecio.
     *
     * @param usuario El usuario que cancel&oacute; la actividad.
     * @param resultado El tipo de resultado (Autorizado, No autorizado, etc.).
     */
    public void cancelar(IUsuario usuario, String resultado) {
        if (!Actividad.ACT_DN_SOBREPRECIO.equals(getNombreActividad())) {
            completar(usuario, STATUS_CANCELADO, resultado);
        }
        else {
            System.out.println("No se mata la autorizaci\u00f3n por ser de sobreprecio.");
        }
    }

    /**
     * Regresa el valor de idActividad.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_ACTIVIDAD"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_ACTIVIDAD_SEQ"
     * @return int.
     */
    public int getIdActividad() {
        return idActividad;
    }

    /**
     * Establece el valor de idActividad.
     *
     * @param idActividad El valor a asignar.
     */
    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    /**
     * Regresa el valor de proceso.
     *
     * @hibernate.property column="PROCESO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getProceso() {
        return proceso;
    }

    /**
     * Establece el valor de proceso.
     *
     * @param proceso El valor a asignar.
     */

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    /**
     * Regresa el valor de fechaCreacion.
     *
     * @hibernate.property column="FECHA_CREACION"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece el valor de fechaCreacion.
     *
     * @param fechaCreacion El valor a asignar.
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Regresa el valor de fechaTerminacion.
     *
     * @hibernate.property column="FECHA_TERMINACION"
     * not-null="false"
     * unique="false"
     * @return Date.
     */
    public Date getFechaTerminacion() {
        return fechaTerminacion;
    }

    /**
     * Establece el valor de fechaTerminacion.
     *
     * @param fechaTerminacion El valor a asignar.
     */
    public void setFechaTerminacion(Date fechaTerminacion) {
        this.fechaTerminacion = fechaTerminacion;
    }

    /**
     * Regresa el valor de nombreActividad.
     *
     * @hibernate.property column="NOMBRE_ACTIVIDAD"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getNombreActividad() {
        return nombreActividad;
    }

    /**
     * Establece el valor de nombreActividad.
     *
     * @param nombreActividad El valor a asignar.
     */
    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    /**
     * Regresa el valor de RESULTADO.
     *
     * @hibernate.property column="RESULTADO"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getResultado() {
        return resultado;
    }

    /**
     * Establece el valor de resultado.
     *
     * @param resultado El valor a asignar.
     */
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    /**
     * Regresa el valor de status.
     *
     * @hibernate.property column="STATUS"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Establece el valor de status.
     *
     * @param status El valor a asignar.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Regresa el valor de deal.
     *
     * @hibernate.many-to-one column="ID_DEAL"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Deal"
     * outer-join="auto"
     * unique="false"
     * @return Deal.
     */
    public Deal getDeal() {
        return deal;
    }

    /**
     * Establece el valor de deal.
     *
     * @param deal El valor a asignar.
     */
    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    /**
     * Regresa el valor de dealDetalle.
     *
     * @hibernate.many-to-one column="ID_DEAL_POSICION"
     * cascade="none"
     * class="com.ixe.ods.sica.model.DealDetalle"
     * outer-join="auto"
     * unique="false"
     * @return com.ixe.ods.sica.model.DealDetalle.
     */
    public DealDetalle getDealDetalle() {
        return dealDetalle;
    }

    /**
     * Establece el valor de dealDetalle.
     *
     * @param dealDetalle El valor a asignar.
     */
    public void setDealDetalle(DealDetalle dealDetalle) {
        this.dealDetalle = dealDetalle;
    }

    /**
     * Regresa el valor de usuario.
     *
     * @hibernate.many-to-one column="ID_USUARIO"
     * cascade="none"
     * class="com.ixe.ods.seguridad.model.Usuario"
     * outer-join="auto"
     * unique="false"
     * @return IUsuario.
     */
    public IUsuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el valor de usuario.
     *
     * @param usuario El valor a asignar.
     */
    public void setUsuario(IUsuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Regresa el valor de version.
     *
     * @hibernate.version column="VERSION" name="version" access="property"
     * @return Integer.
     */
    public Integer getVersion() {
        if (version == null) {
            version = new Integer(0);
        }
        return version;
    }

    /**
     * Establece el valor de version.
     *
     * @param version El valor a asignar.
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Regresa una cadena con la descripci&oacute;n de todas las propiedades del objeto.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this).append("idActividad", idActividad).
                append("proceso", proceso).append("fechaCreacion", fechaCreacion).
                append("fechaTerminacion", fechaTerminacion).
                append("nombreActividad", nombreActividad).append("resultado", resultado).
                append("status", status).append("deal", deal).append("dealDetalle", dealDetalle).
                append("usuario", usuario).toString();
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof Actividad)) {
            return false;
        }
        Actividad castOther = (Actividad) other;
        return new EqualsBuilder().append(this.getIdActividad(),
                castOther.getIdActividad()).isEquals();
    }

    /**
     * Hibernate lo utiliza para cuestiones de herencia.
     * Regresa el hashCode del Objeto.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code> El HashCode identificador del objeto.
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdActividad()).toHashCode();
    }

    /**
     * El n&uacute;mero de la actividad. La llave primaria del registro.
     */
    private int idActividad;

    /**
     * El nombre del proceso.
     */
    private String proceso;

    /**
     * La fecha de creaci&oacute;n del registro.
     */
    private Date fechaCreacion = new Date();

    /**
     * La fecha en la que la actividad fue atendida.
     */
    private Date fechaTerminacion;

    /**
     * El tipo de actividad.
     */
    private String nombreActividad;

    /**
     * El resultado de actividad, por ejemplo: Autorizado, Aut. Negada, etc.
     */
    private String resultado;

    /**
     * El status de la actividad STATUS_PENDIENTE, STATUS_CANCELADO, STATUS_TERMINADO,
     * STATUS_SUSPENDIDO.
     */
    private String status = STATUS_PENDIENTE;

    /**
     * El deal con el que tiene relaci&oacute;n la actividad.
     */
    private Deal deal;

    /**
     * El detalle de deal con el que tiene relaci&oacute;n la actividad (opcional).
     */
    private DealDetalle dealDetalle;

    /**
     * El usuario que atendi&oacute; la actividad.
     */
    private IUsuario usuario;

    /**
     * La propiedad de versi&oacute;n para optimistic locking.
     */
    private Integer version = new Integer(0);

    /**
     * Constante para identificar el status de Pendiente.
     */
    public static final String STATUS_PENDIENTE = "Pendiente";

    /**
     * Constante para identificar el status de Cancelado.
     */
    public static final String STATUS_CANCELADO = "Cancelado";

    /**
     * Constante para identificar el status de Terminado.
     */
    public static final String STATUS_TERMINADO = "Terminado";

    /**
     * Constante para identificar el status de Suspendido.
     */
    public static final String STATUS_SUSPENDIDO = "Suspendido";

    /**
     * Constante para indentificar el proceso un deal normal.
     */
    public static final String PROC_DEAL_NORMAL = "DealNormal";

    /**
     * Constante para indentificar el proceso de autorizaciones de Horario y Monto.
     */
    public static final String PROC_HORARIO_MONTO = "HorMonto";

    /**
     * Constante para indentificar el proceso de deal interbancario.
     */
    public static final String PROC_DEAL_INTERBANCARIO = "DealInterb";

    /**
     * Constante para indentificar el proceso de cancelaci&oacute;n de un deal.
     */
    public static final String PROC_CANCELACION_DEAL = "CancDeal";

    /**
     * Constante para indentificar el proceso de cancelaci&oacute;n de detalle de un deal.
     */
    public static final String PROC_CANCELACION_DET = "CanDetalle";

    /**
     * Constante para identificar el proceso de Liquidaci&oacute;n de remesas.
     */
    public static final String PROC_LIQ_REMESAS = "Liq Remesa";

    /**
     * Constante para identificar el proceso de modificaci&oacute;n de un detalle de deal.
     */
    public static final String PROC_MODIF_DETALLE = "ModDeal";
    
    /**
     * Constante para identificar el proceso de notificaci&oacute;n en los l&iacute;mites de
     * operaci&oacute;n.
     */
    public static final String PROC_NOTIF_LIM_OPER = "LimOper";

    /**
     * Constante para indentificar el proceso de deal.
     */
    public static final String PROC_NOTIF_DEAL = "NotifDeal";

    /**
     * Constante para indentificar la actividad de autorizaci&oacute;n de deal normal por horario.
     */
    public static final String ACT_DN_HORARIO = "DN Horario";

    /**
     * Constante para indentificar la actividad de autorizaci&oacute;n de deal normal por monto.
     */
    public static final String ACT_DN_MONTO = "DN Monto";
    
    /**
     * Constante para indentificar la actividad de autorizaci&oacute;n de deal normal 
     * por modificaci&oacute;n de monto.
     */
    public static final String ACT_DN_MOD_MONTO = "DN Mod Monto";
    
    /**
     * Constante para indentificar la actividad de autorizaci&oacute;n de deal normal 
     * por modificaci&oacute;n de producto.
     */
    public static final String ACT_DN_MOD_PROD = "DN Mod Producto";
    
    /**
     * Constante para identificar la actividad de notificaci&oacute;n de que sobrepaso el
     * l&iacute;mite de operaci&oacute;n normal pero no el l&iacute;mite de operaci&oacute;n de Ixe.
     */
    public static final String ACT_DN_NOTIF_LIM = "DN Lim Oper.";

    /**
     * Constante para indentificar la actividad de autorizaci&oacute;n de deal normal
     * notificaci&oacute;n de desviaci&oacute;n para el tipo de cambio cliente.
     */
    public static final String ACT_DN_NOT_DESV_TCC = "DN Not. Desv. T.C.C.";

    /**
     * Constante para indentificar la actividad de autorizaci&oacute;n de deal normal por
     * sobreprecio.
     */
    public static final String ACT_DN_SOBREPRECIO = "DN Sobreprecio";

    /**
     * Constante para indentificar la actividad de autorizaci&oacute;n de deal normal por
     * documentaci&oacute;n.
     */
    public static final String ACT_DN_DOCUMENTACION = "DN Documento";

    /**
     * Constante para indentificar la actividad de autorizaci&oacute;n de deal normal por
     * documentaci&oacute;n.
     */
    public static final String ACT_DN_PLANTILLA = "DN Plantilla";

    /**
     * Constante para identificar la actividad de autorizaci&oacute;n de deal normal por R.F.C.
     */
    public static final String ACT_DN_RFC = "DN RFC";

    /**
     * Constante para identificar la actividad de autorizaci&oacute;n de deal normal por cambio de
     * correo electr&oacute;nico.
     */
    public static final String ACT_DN_EMAIL = "DN EMAIL";

    /**
     * Constante para indentificar la actividad de autorizaci&oacute;n de deal normal
     * de la mesa de control.
     */
    public static final String ACT_DN_REC_M_CTRL = "DN Rec. M. Ctrl.";

    /**
     * Constante para indentificar la actividad de autorizaci&oacute;n de deal normal con pago
     * anticipado.
     */
    public static final String ACT_DN_PAGO_ANTICIPADO = "DN Pag. Antic.";

    /**
     * Constante para indentificar la actividad de autorizaci&oacute;n de cancelaci&oacute;n de un
     * detalle de deal normal.
     */
    public static final String ACT_DN_CANC_DET = "DN Canc. Det.";

    /**
     * Constante para identificar la actividad de recuperaci&ocute;n de detalles en caso que
     * tesorer&iacute;a no autorice la cancelaci&oacute;n totalmente.
     */
    public static final String ACT_DNCA_RECUP_DET = "DNCA Recup. Dets.";

    /**
     * Constante para indentificar la actividad de cancelaci&oacute;n de deal normal a espera
     * de autorizaci&oacute;n por parte de la mesa.
     */
    public static final String ACT_DNCA_ESP_AUT_MESA = "DNCA Esp. Aut. Mesa.";

    /**
     * Constante para identificar la actividad de autorizaci&acute;n
     * de deal interbancario por documentaci&oacute;n
     *
     */
    public static final String ACT_DINT_DOCUMENTACION = "DInt Documento";

    /**
     * Constante para indentificar la actividad de autorizaci&oacute;n de deal interbancario
     * de la mesa de control.
     */
    public static final String ACT_DINT_REC_M_CTRL = "DInt Rec. M. Ctrl.";

    /**
     * Constante para identificar la actividad de autorizaci&oacute;n de deal interbancario
     * con l&iacute;a de operaci&oacute;n.
     */
    public static final String ACT_DINT_LINEA_OPERACION = "DInt Lin. Oper.";

    /**
     * Constante para identificar la actividad de autorizaci&oacute;n de deal interbancario
     * por l&iacute;nea de cr&eacute;dito.
     */
    public static final String ACT_DINT_LINEA_CREDITO = "DInt Lin. Cred.";

    /**
     * Constante para identificar la actividad de espera de respuesta de tesorar&iacute; sobre la
     * modificaci&oacute;n a un detalle de deal.
     */
    public static final String ACT_MOD_DET_ESP_TES = "Esp. Tes. Mod. Det.";

    /**
     * Constante para identificar la actividad de espera de respuesta de tesorar&iacute; sobre el
     * split de un detalle de deal.
     */
    public static final String ACT_SPLIT_ESP_TES = "Esp. Tes. Split Det.";

    /**
     * Constante para identificar la actividad de espera de confirmaci&oacute;n de remesas.
     */
    public static final String ACT_TES_REMESA = "TES Remesa";

    /**
     * Constante para identificar la actividad de respuesta a solicitud de autorizaci&oacute;n del
     * deal.
     */
    public static final String RES_AUTORIZADO = "Autorizado";

    /**
     * Constante para identificar la actividad de respuesta a deal no autorizado.
     */
    public static final String RES_NO_AUTORIZADO = "No Autorizado";

    /**
     * Constante para identificar la actividad de respuesta a solicitud de deal normal.
     */
    public static final String RES_NORMAL = "Normal";

    /**
     * Constante para identificar la actividad de respuesta a solicitud de cambio de instrucciones
     * de cr&eacute;dito.
     */
    public static final String RES_CAMB_CRED = "Por camb. cred.";

    /**
     * Constante para identificar la actividad de respuesta a solicitud de cancelaci&oacute;n.
     */
    public static final String RES_SOL_CANC = "Por sol. canc.";

    /**
     * Constante para identificar la actividad de respuesta a solicitud de terminaci&oacute;n por
     * Liquidaci&oacute;n Previa.
     */
    public static final String RES_SOL_TERM_LP = "Por liq. prev.";

    /**
     * Constante para identificar la actividad de respuesta a solicitud de modificaci&oacute;n del
     * deal.
     */
    public static final String RES_SOL_MODIF = "Por sol. modif.";

    /**
     * Constante para identificar la actividad de respuesta a solicitud de confirmaci&oacute;n del
     * deal.
     */
    public static final String RES_CONFIRMADA = "Confirmada";

    /**
     * Constante para identificar el resultado de pendiente de recuperar detalles por no
     * autorizaci&oacute;n del SITE de la solicitud de cancelaci&oacute;n total de un deal.
     */
    public static final String RES_RECUPERAR = "Recuperar";

    /**
     * El uid para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 5157972940396336869L;
}
