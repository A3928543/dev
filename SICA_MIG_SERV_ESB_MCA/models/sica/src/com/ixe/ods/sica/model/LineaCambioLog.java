/*
 * $Id: LineaCambioLog.java,v 1.4.68.1 2016/08/31 21:21:24 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.ixe.ods.seguridad.model.IUsuario;

/**
 * Clase persistente para la tabla SC_LINEA_CAMBIO_LOG.
 *
 * @hibernate.class table="SC_LINEA_CAMBIO_LOG"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.LineaCambioLog"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.4.68.1 $ $Date: 2016/08/31 21:21:24 $
 */
public class LineaCambioLog {
            
    /**
     * Constructor por default.
     */
	public LineaCambioLog() {
        super();
    }

    /**
     * Construye una instancia de LineaCambioLog con los datos proporcionados.
     *
     * @param lineaCambio La L&iacute;neas de Crambio.
     * @param tipoOper El Tipo de Operaci&oacute;n: Uso-U, Liberaci&oacute;n-L.
     * @param importe El Importe de la Operaci&oacute;n: Uso-U, Liberaci&oacute;n-L.
     * @param det El <code>DealDetalle</code.>
     * @param factorDivisa El valor del factor de divisa.
     * @param observaciones La Descripci&oacute;n de la Operaci&oacute;n.
     * @param usuario El Usuario del Sistema.
     */
    public LineaCambioLog(LineaCambio lineaCambio, String tipoOper, BigDecimal importe,
                           DealDetalle det, BigDecimal factorDivisa, String observaciones,
                           IUsuario usuario) {
        this();
		setLineaCambio(lineaCambio);
		setTipoOper(tipoOper);
		setImporte(importe);
		setFechaOperacion(new Date());
        setDealDetalle(det);
		setDivisa(det.getDivisa());
        setFactorDivisa(factorDivisa);
        setObservaciones(observaciones);
		setUsuario(usuario);
    }

    /**
     * Regresa el valor de idLineaCambioLog.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_LINEA_CAMBIO_LOG"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_LINEA_Cambio_LOG_SEQ"
     * @return Integer.
     */
    public Integer getIdLineaCambioLog() {
        return idLineaCambioLog;
    }

    /**
     * Fija el valor de idLineaCambioLog.
     *
     * @param idLineaCambioLog El valor a asignar.
     */
    public void setIdLineaCambioLog(Integer idLineaCambioLog) {
        this.idLineaCambioLog = idLineaCambioLog;
    }

    /**
     * Regresa el valor de LineaCambio.
     *
     * @hibernate.many-to-one column="ID_LINEA_CAMBIO"
     * cascade="none"
     * class="com.ixe.ods.sica.model.LineaCambio"
     * outer-join="auto"
     * unique="false"
     * @return LineaCambio.
     */
    public LineaCambio getLineaCambio() {
        return lineaCambio;
    }

    /**
     * Fija el valor de LineaCambio.
     *
     * @param lineaCambio El valor a asignar.
     */
    public void setLineaCambio(LineaCambio lineaCambio) {
        this.lineaCambio = lineaCambio;
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
   * Regresa el valor de importe.
   *
   * @hibernate.property column="IMPORTE"
   * not-null="true"
   * unique="false"
   * @return double.
   */
  public BigDecimal getImporte() {
      return importe;
  }

  /**
   * Fija el valor de importe.
   *
   * @param importe El valor a asignar.
   */
  public void setImporte(BigDecimal importe) {
      this.importe = importe;
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
     * Regresa el valor de idDealDetalle
     *
     * @return Integer.
     */
    public Integer getIdDealDetalle() {
		return getDealDetalle() != null ? new Integer(getDealDetalle().getIdDealPosicion()) : null;
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
        return divisa;
    }

    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(Divisa divisa) {
        this.divisa = divisa;
    }

    /**
     * Regresa el valor de factorDivisa.
     *
     * @hibernate.property column="FACTOR_DIVISA"
     * not-null="false"
     * unique="false"
     * @return double.
     */
    public BigDecimal getFactorDivisa() {
        return factorDivisa;
    }

    /**
     * Fija el valor de factorDivisa.
     *
     * @param factorDivisa El valor a asignar.
     */
    public void setFactorDivisa(BigDecimal factorDivisa) {
        this.factorDivisa = factorDivisa;
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
     * Regresa el valor de dealDetalle.
     *
     * @hibernate.many-to-one column="ID_DEAL_DETALLE"
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
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof LineaCambioLog)) {
            return false;
        }
        LineaCambioLog castOther = (LineaCambioLog) other;
        return new EqualsBuilder().append(this.getIdLineaCambioLog(),
                castOther.getIdLineaCambioLog()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdLineaCambioLog()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    private Integer idLineaCambioLog;

    /**
     * Relaci&oacute; muchos-a-uno con LineaCambio.
     * La L&iacute;nea de Crambio que fue modificada.
     */
    private LineaCambio lineaCambio;

    /**
     * A: Aumento de L&iacute;nea, D: Disminuci&oacute;n de L&iacute;nea, V: Vencimiento de
     *  L&iacute;nea.
     * U: Uso de L&iacute;nea, L: Liberaci&oacute;n de L&iacute;nea, S: Suspensi&oacute;n de
     *  L&iacute;nea,
     * T: Alta de L&iacute;nea.
     */
    private String tipoOper;

    /**
     * Monto en la Divisa de la L&iacute;nea de Crambio.
     */
    private BigDecimal importe;

    /**
     * La Fecha de la Modifici&oacute;n a la L&iacute;nea de Crambio.
     */
    private Date fechaOperacion;

    /**
     * En caso de un deal sobre qu&eacute; divisa se realiz&oacute;.
     */
    private Divisa divisa;

    /**
     * Factor de la divisa cuando se captur&oacute; el deal
     */
    private BigDecimal factorDivisa;

    /**
     * Observaciones del cambio de status de la L&iacute;nea de Crambio (e.g.,
     * suspensi&oacute;n)
     */
    private String observaciones;

    /**
     * Usuario que realiz&oacute; la modificaci&oacute;n de la L&iacute;nea de Cambio.
     */
    private IUsuario usuario;

    /**
     * El detalle de deal al que est&aacute; asignado un log de l&iacute;nea de Cambio.
     */
    private DealDetalle dealDetalle;

    /**
     * Constante para el tipo de operaci&oacute;n de Activaci&oacute;n de la L&iacute;nea.
     */
    public static final String OPER_ACTIVACION = "C";

    /**
     * Constante para el tipo de operaci&oacute;n de Liberaci&oacute;n.
     */
    public static final String OPER_LIBERACION = "L";

    /**
     * Constante para el tipo de operaci&oacute;n de Uso.
     */
    public static final String OPER_USO = "U";
    
    /**
     * Constante para el tipo de operaci&oacute;n Vencimiento de la L&iacute;nea
     */
    public static final String OPER_VENCIMIENTO = "V";
}
