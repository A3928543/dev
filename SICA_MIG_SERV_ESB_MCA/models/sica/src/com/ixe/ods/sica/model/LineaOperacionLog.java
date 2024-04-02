/*
 * $Id: LineaOperacionLog.java,v 1.13.24.1 2011/04/26 00:47:25 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.model;

import com.ixe.ods.seguridad.model.IUsuario;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Movimiento que puede tener una l&iacute;nea de operaci&oacute;n:
 *  <ul>
 *   <li>Altas de l&iacute;neas de operaci&oacute;n</li>
 *   <li>Uso de la l&iacute;nea de operaci&oacute;n mediante la captura de un deal
 *       interbancario (para compras y ventas indistintamente).</li>
 *   <li>Liberaci&oacute;n de l&iacute;neas de operaci&oacute;n debido a
 *       liquidaciones de deals interbancarios.</li>
 *   <li>Suspensi&oacute;n de l&iacute;neas de operaci&oacute;n</li>
 *  </ul>
 * </p>
 *
 * @hibernate.class table="SC_LINEA_OPERACION_LOG"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.LineaOperacionLog"
 * dynamic-update="true"
 * 
 * @hibernate.query name="findHistorialLineaOperacionLogByIdLineaOperacion"
 * query="FROM LineaOperacionLog AS lol WHERE lol.lineaOperacion.idLineaOperacion = ? AND
 * lol.fechaOperacion >= ? ORDER BY lol.idLineaOperacionLog"
 *
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.13.24.1 $ $Date: 2011/04/26 00:47:25 $
 */
public class LineaOperacionLog implements Serializable {

    /**
     * Constructor por default.
     */
    public LineaOperacionLog() {
        super();
    }

    /**
     * Constructor que inicializa parametros.
     * 
     * @param lineaOperacion La Linea de Operacion sobre la que se hara el LOG.
     * @param tipoOper La operacion que genero el LOG: Solicitud, Autorizaci&oacute;n, Uso, Liberaci&oacute;n, Suspensi&oacute;n, Excedente.
     * @param dealDetalle El Detalle del Deal si se trata de una Operacion de USO.
     * @param usuario El usuario del Sistema.
     */
    public LineaOperacionLog(LineaOperacion lineaOperacion, String tipoOper, DealDetalle dealDetalle, IUsuario usuario) {
        this();
        setLineaOperacion(lineaOperacion);
        setTipoOper(tipoOper);
        setImporte(dealDetalle.getMonto());
        setIdDealDetalle(new Integer(dealDetalle.getIdDealPosicion()));
        setDivisa(dealDetalle.getDivisa());
        setFactorDivisa(dealDetalle.getFactorDivisa());
        setUsuario(usuario);
    }

    /**
     * Regresa el valor de idLineaOperacionLog.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_LINEA_OPERACION_LOG"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_LINEA_OPERACION_LOG_SEQ"
     * @return int.
     */
    public int getIdLineaOperacionLog() {
        return idLineaOperacionLog;
    }

    /**
     * Fija el valor de idLineaOperacionLog.
     *
     * @param idLineaOperacionLog El valor a asignar.
     */
    public void setIdLineaOperacionLog(int idLineaOperacionLog) {
        this.idLineaOperacionLog = idLineaOperacionLog;
    }

    /**
     * Regresa el valor de factorDivisa.
     *
     * @hibernate.property column="FACTOR_DIVISA"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getFactorDivisa() {
        return factorDivisa;
    }

    /**
     * Fija el valor de factorDivisa.
     *
     * @param factorDivisa El valor a asignar.
     */
    public void setFactorDivisa(Double factorDivisa) {
        this.factorDivisa = factorDivisa;
    }

    /**
     * Regresa el valor de fechaOperacion.
     *
     * @hibernate.property column="FECHA_OPERACION"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getFechaOperacion() {
        return fechaOperacion;
    }

    /**
     * Fija el valor de fechaOperacion.
     *
     * @param fechaOperacion El valor a asignar.
     */
    public void setFechaOperacion(Date fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
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
     * Fija el valor de usuario.
     *
     * @param usuario El valor a asignar.
     */
    public void setUsuario(IUsuario usuario) {
    		this.usuario = usuario;
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
     * Regresa el valor de tipoOper.
     *
     * @hibernate.property column="TIPO_OPER"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getTipoOper() {
        return tipoOper;
    }

    /**
     * Fija el valor de tipoOper.
     *
     * @param tipoOper El valor a asignar.
     */
    public void setTipoOper(String tipoOper) {
        this.tipoOper = tipoOper;
    }

    /**
     * Regresa el valor de idDealDetalle
     *
     * @hibernate.property column="ID_DEAL_POSICION"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getIdDealDetalle() {
		return idDealDetalle;
	}

    /**
     * Fija el valor de idDealDetalle
     * 
     * @param idDealDetalle El valor a asignar.
     */
	public void setIdDealDetalle(Integer idDealDetalle) {
		this.idDealDetalle = idDealDetalle;
	}

    /**
     * Regresa el valor de divisa.
     *
     * @hibernate.many-to-one column="ID_DIVISA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Divisa"
     * outer-join="auto"
     * unique="false"
     * @return Divisa.
     */
    public Divisa getDivisa() {
        return _divisa;
    }

    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(Divisa divisa) {
        _divisa = divisa;
    }

    /**
     * Regresa el valor de lineaOperacion.
     *
     * @hibernate.many-to-one column="ID_LINEA_OPERACION"
     * cascade="none"
     * class="com.ixe.ods.sica.model.LineaOperacion"
     * outer-join="auto"
     * unique="false"
     * @return LineaOperacion.
     */
    public LineaOperacion getLineaOperacion() {
        return _lineaOperacion;
    }

    /**
     * Fija el valor de lineaOperacion.
     *
     * @param lineaOperacion El valor a asignar.
     */
    public void setLineaOperacion(LineaOperacion lineaOperacion) {
        _lineaOperacion = lineaOperacion;
    }
    
    /**
     * Regresa el valor de observaciones.
     *
     * @hibernate.property column="OBSERVACIONES"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Fija el valor de observaciones.
     *
     * @param observaciones El valor a asignar.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof LineaOperacionLog)) {
            return false;
        }
        LineaOperacionLog castOther = (LineaOperacionLog) other;
        return new EqualsBuilder().append(this.getIdLineaOperacionLog(), castOther.getIdLineaOperacionLog()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdLineaOperacionLog()).toHashCode();
    }

    //Variables de Instancia.
    /**
     * El identificador del registro.
     */
    private int idLineaOperacionLog;

    /**
     * Factor de divisa cuando se captur&oacute; el deal.
     */
    private Double factorDivisa;

    /**
     * La fecha en que se realiz&oacute; la operaci&oacute;n.
     */
    private Date fechaOperacion = new Date();

    /**
     * Ususario que realiz&oacute; la modificaci&oacute;n de la L&iacute;nea de Operaci&oacute;n.
     */
    private IUsuario usuario;

    /**
     * Monto en d&oacute;lares de la operaci&oacute;n.
     */
    private double importe;

    /**
     * A)Solicitud, C)Autorizaci&oacute;n, DA)Uso, DL)Liberaci&oacute;n, S)uspensi&oacute;n, E)xcedente.
     */
    private String tipoOper;

    /**
     * Id del Detalle de Deal que se almacena en la posicion, para el caso donde la L&iacute;nea
     * de Cr&eacute;dito fue utilizada por un pago anticipado.
     */
    private Integer idDealDetalle;

    /**
     * Relaci&oacute;n muchos-a-uno opcional con Divisa.
     */
    private Divisa _divisa;

    /**
     * Relaci&oacute;n muchos-a-uno con LineaOperacion.
     * La L&iacute;nea de Operaci&oacute;n que fue modificada.
     */
    private LineaOperacion _lineaOperacion;
    
    /**
     * Observaciones por Suspensi&oacute;n de la L&iacute;nea de Operaci&oacute;n.
     */
    private String observaciones;
    
    /**
     * Constante Tipo de Operacion Solicitud.
     */
    public final static String TIPO_OPER_SOLICITUD = "A";
    
    /**
     * Constante Tipo de Operacion Autorizacion.
     */
    public final static String TIPO_OPER_AUTORIZACION = "C";
    
    /**
     * Constante Tipo de Operacion Uso.
     */
    public final static String TIPO_OPER_USO = "DA";
    
    /**
     * Constante Tipo de Operacion Solicitud.
     */
    public final static String TIPO_OPER_LIBERACION = "DL";
    
    /**
     * Constante Tipo de Operacion Suspension.
     */
    public final static String TIPO_OPER_SUSPENSION = "S";
    
    /**
     * Constante Tipo de Operacion Excedente.
     */
    public final static String TIPO_OPER_EXCEDENTE = "E";

    /**
     * Constante Tipo de Operacion Suspendida.
     */
    public final static String STATUS_SUSPENDIDA = "SU";
}
