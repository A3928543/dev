/*
 * $Id: ComplementoDatos.java,v 1.1.2.1 2010/10/08 01:30:18 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.ixe.ods.seguridad.model.IUsuario;

/**
 * Clase persistente para la tabla SC_COMPLEMENTO_DATOS.
 *
 * @hibernate.class table="SC_COMPLEMENTO_DATOS"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.ComplementoDatos"
 * dynamic-update="true"
 *
 * @author Abraham L&oacute;pez A.
 * @version $Revision: 1.1.2.1 $ $Date: 2010/10/08 01:30:18 $
 */
public class ComplementoDatos implements Serializable{

	/**
     * Constructor vac&iacute;o.
     */
    public ComplementoDatos() {
        super();
    }
    
    /**
     * Constructor que inicializa todas las variables de instancia.
     *
     * @param noCuenta El n&uacute;mero de cuenta del contrato sica.
     * @param nacionalidad La nacionalidad de la persona que opera el contrato sica.
     * @param noPasaporte El n&uacute;mero de pasaporte de la persona que opera el contrato sica.
     * @param folioIfe El n&uacute;mero del folio de su credencial del ife.
     * @param anioRegistroIfe El anio de registro de su credencial del ife.
     * @param noEmisionIfe El n&uacute;mero de emisi&oacute;n de su credencial del ife.
     * @param ocrIfe El ocr de su credencial del ife.
     * @param cliente Si se trata de un cliente o un usuario.
     * @param fechaCreacion La fecha de creaci&oacute;n del registro.
     * @param fechaUltimaModificacion La fecha de la &uacute;ltima modificaci&oacute;n del registro.
     * @param usuario El usuario que realiz&oacute; la operaci&oacute;n.
     */
    public ComplementoDatos(String noCuenta, String nacionalidad, String noPasaporte,
    		String folioIfe, String anioRegistroIfe, String noEmisionIfe, String ocrIfe,
    		String cliente, Date fechaCreacion, Date fechaUltimaModificacion, IUsuario usuario) {
		super();
		this.noCuenta = noCuenta;
		this.nacionalidad = nacionalidad;
		this.noPasaporte = noPasaporte;
		this.folioIfe = folioIfe;
		this.anioRegistroIfe = anioRegistroIfe;
		this.noEmisionIfe = noEmisionIfe;
		this.ocrIfe = ocrIfe;
		this.cliente = cliente;
		this.fechaCreacion = fechaCreacion;
		this.fechaUltimaModificacion = fechaUltimaModificacion;
		this.idUsuario = usuario.getIdUsuario();
	}

    /**
     * Regresa el valor de noCuenta.
     *
     * @hibernate.id generator-class="assigned"
     * column="NO_CUENTA"
     * unsaved-value="null"
     * @return String.
     */
    public String getNoCuenta() {
		return noCuenta;
	}
    
    /**
     * Establece el valor de noCuenta.
     *
     * @param noCuenta El valor a asignar.
     */
	public void setNoCuenta(String noCuenta) {
		this.noCuenta = noCuenta;
	}
	
	/**
     * Regresa el valor de nacionalidad.
     *
     * @return String.
     * @hibernate.property column="NACIONALIDAD"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     */
	public String getNacionalidad() {
		return nacionalidad;
	}
	
	/**
     * Establece el valor de nacionalidad.
     *
     * @param nacionalidad El valor a asignar.
     */
	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}
	
	/**
     * Regresa el valor de noPasaporte.
     *
     * @return String.
     * @hibernate.property column="PASAPORTE_NUMERO"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     */
	public String getNoPasaporte() {
		return noPasaporte;
	}
	
	/**
     * Establece el valor de noPasaporte.
     *
     * @param noPasaporte El valor a asignar.
     */
	public void setNoPasaporte(String noPasaporte) {
		this.noPasaporte = noPasaporte;
	}
	
	/**
     * Regresa el valor de folioIfe.
     *
     * @return String.
     * @hibernate.property column="IFE_FOLIO"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     */
	public String getFolioIfe() {
		return folioIfe;
	}
	
	/**
     * Establece el valor de folioIfe.
     *
     * @param folioIfe El valor a asignar.
     */
	public void setFolioIfe(String folioIfe) {
		this.folioIfe = folioIfe;
	}
	
	/**
     * Regresa el valor de anioRegistroIfe.
     *
     * @return String.
     * @hibernate.property column="IFE_ANIO_REGISTRO"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     */
	public String getAnioRegistroIfe() {
		return anioRegistroIfe;
	}
	
	/**
     * Establece el valor de anioRegistroIfe.
     *
     * @param anioRegistroIfe El valor a asignar.
     */
	public void setAnioRegistroIfe(String anioRegistroIfe) {
		this.anioRegistroIfe = anioRegistroIfe;
	}
	
	/**
     * Regresa el valor de noEmisionIfe.
     *
     * @return String.
     * @hibernate.property column="IFE_NUMERO_EMISION"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     */
	public String getNoEmisionIfe() {
		return noEmisionIfe;
	}
	
	/**
     * Establece el valor de noEmisionIfe.
     *
     * @param noEmisionIfe El valor a asignar.
     */
	public void setNoEmisionIfe(String noEmisionIfe) {
		this.noEmisionIfe = noEmisionIfe;
	}
	
	/**
     * Regresa el valor de ocrIfe.
     *
     * @return String.
     * @hibernate.property column="IFE_OCR"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     */
	public String getOcrIfe() {
		return ocrIfe;
	}
	
	/**
     * Establece el valor de ocrIfe.
     *
     * @param ocrIfe El valor a asignar.
     */
	public void setOcrIfe(String ocrIfe) {
		this.ocrIfe = ocrIfe;
	}
	
	/**
     * Regresa el valor de cliente.
     *
     * @return String.
     * @hibernate.property column="CLIENTE"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     */
	public String getCliente() {
		return cliente;
	}
	
	/**
     * Establece el valor de cliente.
     *
     * @param cliente El valor a asignar.
     */
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	
	/**
     * Regresa el valor de fechaCreacion.
     *
     * @return Date.
     * @hibernate.property column="FECHA_CREACION"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
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
     * Regresa el valor de fechaUltimaModificacion.
     *
     * @return Date.
     * @hibernate.property column="FECHA_ULTIMA_MOD"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
	public Date getFechaUltimaModificacion() {
		return fechaUltimaModificacion;
	}
	
	/**
     * Establece el valor de fechaUltimaModificacion.
     *
     * @param fechaUltimaModificacion El valor a asignar.
     */
	public void setFechaUltimaModificacion(Date fechaUltimaModificacion) {
		this.fechaUltimaModificacion = fechaUltimaModificacion;
	}
	
	/**
     * Regresa el valor de idUsuario.
     *
     * @return int.
     * @hibernate.property column="ID_USUARIO_MOD"
     * not-null="false"
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
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @return boolean.
     * @see java.lang.Object#equals(java.lang.Object).
     */
    public boolean equals(Object other) {
        if (!(other instanceof ComplementoDatos)) {
            return false;
        }
        ComplementoDatos castOther = (ComplementoDatos) other;
        return new EqualsBuilder().append(this.getNoCuenta(),
                castOther.getNoCuenta()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @return <code>int</code>
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getNoCuenta()).toHashCode();
    }

    /**
     * Regresa una cadena con la descripci&oacute;n de los campos de esta instancia.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this).append("noCuenta", noCuenta).
                append("nacionalidad", nacionalidad).append("noPasaporte", noPasaporte).
                append("folioIfe", folioIfe).append("anioRegistroIfe", anioRegistroIfe).
                append("noEmisionIfe", noEmisionIfe).append("ocrIfe", ocrIfe).
                append("cliente", cliente).append("fechaCreacion", fechaCreacion).
                append("fechaUltimaModificacion", fechaUltimaModificacion).
                append("idUsuario", idUsuario).toString();
    }
    
    /**
     * El n&uacute;mero de cuenta del contrato sica.
     */
	private String noCuenta;
    
	/**
	 * La nacionalidad de la persona que opera el contrato sica.
	 */
    private String nacionalidad;
    
    /**
     * El n&uacute;mero de pasaporte de la persona que opera el contrato sica.
     */
    private String noPasaporte;
    
    /**
     * El n&uacute;mero del folio de su credencial del ife.
     */
    private String folioIfe;
    
    /**
     * El anio de registro de su credencial del ife.
     */
    private String anioRegistroIfe;
    
    /**
     * El n&uacute;mero de emisi&oacute;n de su credencial del ife.
     */
    private String noEmisionIfe;
    
    /**
     * El ocr de su credencial del ife.
     */
    private String ocrIfe;
    
    /**
     * Si se trata de un cliente o un usuario.
     */
    private String cliente;
    
    /**
     * La fecha de creaci&oacute;n del registro.
     */
    private Date fechaCreacion;
    
    /**
     * La fecha de la &uacute;ltima modificaci&oacute;n del registro.
     */
    private Date fechaUltimaModificacion;
    
    /**
     * El usuario que realiz&oacute; la operaci&oacute;n.
     */
    private int idUsuario;
    
    /**
	 * El UID para serializaci&oacute;n.
	 */
	private static final long serialVersionUID = -8839362755545009890L;

}
