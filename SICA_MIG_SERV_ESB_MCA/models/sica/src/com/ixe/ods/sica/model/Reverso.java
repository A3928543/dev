/*
 * $Id: Reverso.java,v 1.17.28.1 2010/06/17 21:50:25 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.seguridad.model.IUsuario;

/**
 * Clase persistente para la entidad SC_REVERSO, que guarda la informaci&oacute;n sobre los reversos
 * realizados.
 *
 * @hibernate.class table="SC_REVERSO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Reverso"
 * dynamic-update="true"
 *
 * @hibernate.query name="findReversoByStatus"
 * query="FROM Reverso AS rev WHERE rev.statusReverso = 'PE' OR rev.statusReverso = 'AL' ORDER BY rev.idReverso ASC"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.17.28.1 $ $Date: 2010/06/17 21:50:25 $
 */
public class Reverso implements Serializable {

    /**
     * Constructor vac&icute;o.
     */
    public Reverso() {
        super();
    }

    /**
     * Regresa la persona ligada a los rol 'TIT' del contratoSica.
     *
     * @return Persona.
     */
    public Persona getCliente() {
        return ContratoCliente.clienteParaContratoSica(getPorContratoSica());
    }
    
    /**
     * Constructor para inicializar las variables de instancia.
     *
     * @param dealOriginal El deal que est&aacute; mal capturado.
     * @param dealBalanceo El deal que balancea al deal original.
     * @param observaciones Los motivos por los cuales se lleva a cabo el reverso.
     * @param usuario El usuario que registra el reverso.
     */
    public Reverso(Deal dealOriginal, Deal dealBalanceo, String observaciones, IUsuario usuario) {
        this();
        this.dealOriginal = dealOriginal;
        this.dealBalanceo = dealBalanceo;
        this.observaciones = observaciones;
        this.usuario = usuario;
    }

    /**
     * Constructor para inicializar las variables de instancia.
     *
     * @param dealOriginal El deal que est&aacute; mal capturado.
     * @param observaciones Los motivos por los cuales se lleva a cabo el reverso.
     * @param porCancelacion Si se trata de un reverso por cancelaci&oacute;n.
     * @param porContratoSica Si se trata de un reverso por cliente, el objeto ContratoSica
     *  (opcional).
     * @param porFechaValor Si se trata de un reverso por fecha valor, la fecha valor
     *  espec&iacute;fica (opcional).
     * @param usuario El usuario que registra el reverso.
     */
    public Reverso(Deal dealOriginal, String observaciones, boolean porCancelacion,
                   ContratoSica porContratoSica, String porFechaValor, IUsuario usuario) {
        // Todo: No debe mandar el deal de balanceo:
        this(dealOriginal, dealOriginal, observaciones, usuario);
        this.porCancelacion = porCancelacion;
        this.porContratoSica = porContratoSica;
        this.porFechaValor = porFechaValor;
    }

    /**
     * Regresa true si alguno de los detalles del reverso se trata de un cambio en monto.
     *
     * @return boolean.
     */
    public boolean isReversoPorMonto() {
        for (Iterator it = detalles.iterator(); it.hasNext();) {
            ReversoDetalle reversoDetalle = (ReversoDetalle) it.next();
            if (reversoDetalle.getMontoNuevo() != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Regresa true si alguno de los detalles del reverso se trata de un cambio en tipo de cambio.
     *
     * @return boolean.
     */
    public boolean isReversoPorTipoCambio() {
        for (Iterator it = detalles.iterator(); it.hasNext();) {
            ReversoDetalle reversoDetalle = (ReversoDetalle) it.next();
            if (reversoDetalle.getTipoCambioNuevo() != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Regresa una cadena descriptiva del status en el que se encuentra el reverso.
     *
     * @return String.
     */
    public String getDescripcionStatus() {
        String desc = "";
        if (STATUS_CANCELADO.equals(statusReverso)) {
            desc = "No Autorizado";
        }
        else if (STATUS_COMPLETO.equals(statusReverso)) {
            desc = "Autorizado";
        }
        else if (STATUS_PENDIENTE.equals(statusReverso)) {
            desc = "Pendiente";
        }
        else if (STATUS_ALTA.equals(statusReverso)) {
        	desc =  "Alta";
        }
        return desc;
    }

    /**
     * Regresa el valor de idReverso.
     *
     * @return int.
     * @hibernate.id generator-class="sequence"
     * column="ID_REVERSO"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_REVERSO_SEQ"
     */
    public int getIdReverso() {
        return idReverso;
    }

    /**
     * Establece el valor de idReverso.
     *
     * @param idReverso El valor a asignar.
     */
    public void setIdReverso(int idReverso) {
        this.idReverso = idReverso;
    }

    /**
     * Regresa el valor de actividad.
     *
     * @return String.
     */
    public String getActividad() {
        return actividad;
    }

    /**
     * Establece el valor de actividad.
     *
     * @param actividad El valor a asignar.
     */
    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    /**
     * Regresa el valor de fecha.
     *
     * @hibernate.property column="FECHA"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return Date.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Establece el valor de la variable fecha.
     *
     * @param fecha El valor a asignar.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Regresa el valor de fechaAutorizacion.
     *
     * @hibernate.property column="FECHA_AUTORIZACION"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     * @return Date.
     */
    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    /**
     * Establece el valor de fechaAutorizacion.
     *
     * @param fechaAutorizacion El valor a asignar.
     */
    public void setFechaAutorizacion(Date fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    /**
     * Regresa el valor de observaciones.
     *
     * @hibernate.property column="OBSERVACIONES"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     * @return String.
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Establece el valor de la variable observaciones.
     *
     * @param observaciones El valor a asignar.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Regresa el valor de porCancelacion.
     *
     * @hibernate.property column="POR_CANCELACION"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return boolean.
     */
    public boolean isPorCancelacion() {
        return porCancelacion;
    }

    /**
     * Establece el valor de porCancelacion.
     *
     * @param porCancelacion El valor a asignar.
     */
    public void setPorCancelacion(boolean porCancelacion) {
        this.porCancelacion = porCancelacion;
    }

    /**
     * Regreasa el valor de porContratoSica.
     *
     * @hibernate.many-to-one column="POR_NO_CUENTA"
     * cascade="none"
     * class="com.ixe.ods.bup.model.ContratoSica"
     * outer-join="auto"
     * unique="false"
     * @return ContratoSica.
     */
    public ContratoSica getPorContratoSica() {
        return porContratoSica;
    }

    /**
     * Establece el valor de porContratoSica.
     *
     * @param porContratoSica El valor a asignar.
     */
    public void setPorContratoSica(ContratoSica porContratoSica) {
        this.porContratoSica = porContratoSica;
    }

    /**
     * Regresa el valor de porFechaValor.
     *
     * @hibernate.property column="POR_FECHA_VALOR"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     * @return String.
     */
    public String getPorFechaValor() {
        return porFechaValor;
    }

    /**
     * Establece el valor de porFechaValor.
     *
     * @param porFechaValor El valor a asignar.
     */
    public void setPorFechaValor(String porFechaValor) {
        this.porFechaValor = porFechaValor;
    }

    /**
     * Regresa el valor de statusReverso.
     *
     * @return String.
     * @hibernate.property column="STATUS_REVERSO"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public String getStatusReverso() {
        return statusReverso;
    }

    /**
     * Establece el valor de la variable statusReverso.
     *
     * @param statusReverso El valor a asignar.
     */
    public void setStatusReverso(String statusReverso) {
        this.statusReverso = statusReverso;
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
        this.datosAdicionales = datosAdicionales;
    }

    /**
     * Regresa el valor de dealOriginal.
     *
     * @return Deal.
     * @hibernate.many-to-one column="ID_DEAL_ORIGINAL"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Deal"
     * outer-join="auto"
     * unique="false"
     */
    public Deal getDealOriginal() {
        return dealOriginal;
    }

    /**
     * Establece el valor de la variable dealOriginal.
     *
     * @param dealOriginal El valor a asignar.
     */
    public void setDealOriginal(Deal dealOriginal) {
        this.dealOriginal = dealOriginal;
    }

    /**
     * Regresa el valor de dealBalanceo.
     *
     * @return Deal.
     * @hibernate.many-to-one column="ID_DEAL_BALANCEO"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Deal"
     * outer-join="auto"
     * unique="false"
     */
    public Deal getDealBalanceo() {
        return dealBalanceo;
    }

    /**
     * Establece el valor de la variable dealBalanceo.
     *
     * @param dealBalanceo El valor a asignar.
     */
    public void setDealBalanceo(Deal dealBalanceo) {
        this.dealBalanceo = dealBalanceo;
    }

    /**
     * Regresa el valor de dealCorreccion.
     *
     * @return Deal.
     * @hibernate.many-to-one column="ID_DEAL_CORRECCION"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Deal"
     * outer-join="auto"
     * unique="false"
     */
    public Deal getDealCorreccion() {
        return dealCorreccion;
    }

    /**
     * Establece el valor de la variable dealCorreccion.
     *
     * @param dealCorreccion El valor a asignar.
     */
    public void setDealCorreccion(Deal dealCorreccion) {
        this.dealCorreccion = dealCorreccion;
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
     * Establece el valor de la variable usuario.
     *
     * @param usuario El valor a asignar.
     */
    public void setUsuario(IUsuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Regresa el valor de usuarioAutorizacion.
     *
     * @hibernate.many-to-one column="ID_USUARIO_AUTORIZACION"
     * cascade="none"
     * class="com.ixe.ods.seguridad.model.Usuario"
     * outer-join="auto"
     * unique="false"
     * @return IUsuario.
     */
    public IUsuario getUsuarioAutorizacion() {
        return usuarioAutorizacion;
    }

    /**
     * Establece el valor de usuarioAutorizacion.
     *
     * @param usuarioAutorizacion El valor a asignar.
     */
    public void setUsuarioAutorizacion(IUsuario usuarioAutorizacion) {
        this.usuarioAutorizacion = usuarioAutorizacion;
    }

    /**
     * @hibernate.bag inverse="true"
     * lazy="true"
     * cascade="none"
     * @hibernate.collection-key column="ID_REVERSO"
     * @hibernate.collection-one-to-many class="com.ixe.ods.sica.model.ReversoDetalle"
     * @return List.
     */
    public List getDetalles() {
        return detalles;
    }

    /**
     * Establece el valor de detalles.
     *
     * @param detalles El valor a asignar.
     */
    public void setDetalles(List detalles) {
        this.detalles = detalles;
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
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof Reverso)) {
            return false;
        }
        Reverso castOther = (Reverso) other;
        return new EqualsBuilder().append(this.getIdReverso(), castOther.getIdReverso()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdReverso()).toHashCode();
    }

    /**
     * El identificador del reverso.
     */
    private int idReverso;

    /**
     * La actividad para el workflow de autorizaciones.
     */
    private String actividad = "Reversos";

    /**
     * La fecha en la que se registr&oacute; el reverso.
     */
    private Date fecha = new Date();

    /**
     * La fecha en la que autoriz&oacute; el reverso.
     */
    private Date fechaAutorizacion;

    /**
     * Los motivos por los que se realiza el reverso.
     */
    private String observaciones;

    /**
     * True si el reverso fue por cancelaci&oacute;n del deal original.
     */
    private boolean porCancelacion;

    /**
     * El Contrato SICA al que corresponde en realidad el deal original (Puede ser null si el
     * reverso no es por cliente).
     */
    private ContratoSica porContratoSica;

    /**
     * La fecha valor que deb&iacute;a tener el deal original. (Puede ser null si el reverso no es
     * por fechaValor).
     */
    private String porFechaValor;

    /**
     * El status del reverso.
     */
    private String statusReverso = STATUS_ALTA;

    /**
     * Una cadena que almacena la informaci&oacute;n de los tipos de cambios de la mesa que puede
     * editar el operador de la mesa para el deal de balanceo y de correcci&oacute;n.
     */
    private String datosAdicionales;

    /**
     * El deal que fue mal capturado.
     */
    private Deal dealOriginal;

    /**
     * El deal que balancea la posici&oacute;n del deal original.
     */
    private Deal dealBalanceo;

    /**
     * El deal que reemplaza al deal original.
     */
    private Deal dealCorreccion;

    /**
     * El usuario que registr&oacute; el reverso.
     */
    private IUsuario usuario;

    /**
     * El usuario que autoriz&oacute; el reverso.
     */
    private IUsuario usuarioAutorizacion;

    /**
     * La propiedad de versi&oacute;n para optimistic locking.
     */
    private Integer version;

    /**
     * Relaci&oacute;n uno-a-muchos con ReversoDetalle.
     */
    private List detalles = new ArrayList();
    
    /**
     * Constante para identificar el status de reverso alta.
     */
    public static final String STATUS_ALTA = "AL";

    /**
     * Constante para identificar el status de reverso pendiente.
     */
    public static final String STATUS_PENDIENTE = "PE";

    /**
     * Constante para identificar el status de reverso autorizado por la mesa de cambios.
     */
    public static final String STATUS_COMPLETO = "CO";

    /**
     * Constante para identificar el status de reverso no autorizado por la mesa de cambios.
     */
    public static final String STATUS_CANCELADO = "CA";

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -137206224130767100L;    
}
