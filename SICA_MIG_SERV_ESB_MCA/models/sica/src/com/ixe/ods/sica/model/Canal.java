/*
 * $Id: Canal.java,v 1.22.22.1 2010/10/08 01:32:41 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.ixe.ods.bup.model.Sucursal;
import com.ixe.ods.bup.model.TipoIva;
import com.ixe.ods.seguridad.model.IFacultad;

/**
 * Clase persistente para la tabla SC_CANAL.
 *
 * <p>Se refiere a los canales de operaci&oacute;n del SICA.</p>
 * <p>Los canales son:</p>
 * <ul>
 *   <li>Promoci&oacute;n Mayoreo</li>
 *   <li>IxeNet</li>
 *   <li>IxeDirecto</li>
 *   <li>Cada sucursal por separado.</li>
 *   <li>Mesa de cambios (para operar con otra mesa</li>
 * </ul>
 *
 * @hibernate.class table="SC_CANAL"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Canal"
 * dynamic-update="true"
 *
 * @hibernate.query name="findCanalByFacultad"
 * query="FROM Canal AS c WHERE c.facultad.nombre = ?"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.22.22.1 $ $Date: 2010/10/08 01:32:41 $
 */
public class Canal implements Serializable {

    /**
     * Constructor por default. No hace nada.
     */
    public Canal() {
        super();
    }

    /**
     * Regresa el valor de idCanal.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_CANAL"
     * unsaved-value="null"
     * @return String.
     */
    public String getIdCanal() {
        return idCanal;
    }

    /**
     * Fija el valor de idCanal.
     *
     * @param idCanal El valor a asignar.
     */
    public void setIdCanal(String idCanal) {
        this.idCanal = idCanal;
    }

    /**
     * Regresa el valor de nombre.
     *
     * @hibernate.property column="NOMBRE"
     * not-null="true"
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
     * Regresa el valor de emailJefe.
     *
     * @hibernate.property column="EMAIL_JEFE"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getEmailJefe() {
        return emailJefe;
    }

    /**
     * Establece el valor de emailJefe.
     *
     * @param emailJefe El valor a asignar.
     */
    public void setEmailJefe(String emailJefe) {
        this.emailJefe = emailJefe;
    }

    /**
     * Regresa el valor de MesaCambio.
     *
     * @hibernate.many-to-one column="FACULTAD"
     * cascade="none"
     * class="com.ixe.ods.seguridad.model.Facultad"
     * outer-join="auto"
     * unique="false"
     * @return IFacultad.
     */
    public IFacultad getFacultad() {
        return _facultad;
    }

    /**
     * Fija el valor de facultad.
     *
     * @param facultad El valor a asignar.
     */
    public void setFacultad(IFacultad facultad) {
        _facultad = facultad;
    }

    /**
     * Regresa el valor de MesaCambio.
     *
     * @hibernate.many-to-one column="ID_MESA_CAMBIO"
     * cascade="none"
     * class="com.ixe.ods.sica.model.MesaCambio"
     * outer-join="auto"
     * unique="false"
     * @return MesaCambio.
     */
    public MesaCambio getMesaCambio() {
        return _mesaCambio;
    }

    /**
     * Fija el valor de mesaCambio.
     *
     * @param mesaCambio El valor a asignar.
     */
    public void setMesaCambio(MesaCambio mesaCambio) {
        _mesaCambio = mesaCambio;
    }

    /**
     * Regresa el valor de Sucursal.
     *
     * @hibernate.many-to-one column="ID_SUCURSAL"
     * cascade="none"
     * class="com.ixe.ods.bup.model.Sucursal"
     * outer-join="auto"
     * unique="false"
     * @return Sucursal.
     */
    public Sucursal getSucursal() {
        return _sucursal;
    }

    /**
     * Fija el valor de sucursal.
     *
     * @param sucursal El valor a asignar.
     */
    public void setSucursal(Sucursal sucursal) {
        _sucursal = sucursal;
    }
    
    /**
     * Regresa el valor de TipoIva.
     *
     * @hibernate.many-to-one column="CLAVE_TIPO_IVA"
     * cascade="none"
     * class="com.ixe.ods.bup.model.TipoIva"
     * outer-join="auto"
     * unique="false"
     * @return TipoIva.
     */
    public TipoIva getTipoIva() {
        return _tipoIva;
    }

    /**
     * Fija el valor del tipo de IVA
     *
     * @param tipoIva El valor a asignar.
     */
    public void setTipoIva(TipoIva tipoIva) {
        _tipoIva = tipoIva;
    }
    
    /**
     * Regresa el valor de codigoPostal.
     *
     * @hibernate.property column="CODIGO_POSTAL"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * Fija el valor de codigoPostal
     *
     * @param codigoPostal El valor a asignar.
     */
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof Canal)) {
            return false;
        }
        Canal castOther = (Canal) other;
        return new EqualsBuilder().append(this.getIdCanal(), castOther.getIdCanal()).isEquals();
    }

    /**
     * Hibernate lo utiliza para cuestiones de herencia.
     * Regresa el hashCode del Objeto.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code> El HashCode identificador del objeto.
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdCanal()).toHashCode();
    }
    
    /** 
     * Obtiene el tipo de Pizarron para el spread.
     * 
     * @hibernate.many-to-one column="ID_TIPO_PIZARRON"
     * cascade="none"
     * class="com.ixe.ods.sica.model.TipoPizarron"
     * outer-join="auto"
     * unique="false"
     * @return TipoPizarron
     */
    public TipoPizarron getTipoPizarron() {
		return tipoPizarron;
	}

    /**
     * Asigna el valor para IdTipoPizarron.
     * 
     * @param tipoPizarron El valor para IdTipoPizarron.
     */
	public void setTipoPizarron(TipoPizarron tipoPizarron) {
		this.tipoPizarron = tipoPizarron;
	}
	
    /**
	 * Obtiene el tipo de calculo que aplica para cada canal
	 * 
	 * @hibernate.property column="TIPO_CALCULO"
     * not-null="true"
     * unique="false"
     * @return String.
	 */
	public String getTipoCalculo() {
		return tipoCalculo;
	}

	/**
	 * @param tipoCalculo the tipoCalculo to set
	 */
	public void setTipoCalculo(String tipoCalculo) {
		this.tipoCalculo = tipoCalculo;
	}
	
    /**
     * El identificador del registro.
     */
    private String idCanal;

    /**
     * El nombre del canal.
     */
    private String nombre;

    /**
     * La direcci&oacute;n de correo del encargado del canal.
     */
    private String emailJefe;
    
    /**
     * Relaci&oacute;n muchos-a-uno con Tipo Iva
     */
    private TipoIva _tipoIva;

    /**
     * Relaci&oacute;n muchos-a-uno con Facultad.
     */
    private IFacultad _facultad;

    /**
     * Relaci&oacute;n muchos-a-uno con MesaCambio.
     */
    private MesaCambio _mesaCambio;

    /**
     * Relaci&oacute;n muchos-a-uno con Sucursal.
     */
    private Sucursal _sucursal;

    /**
     * El id del tipo de pizarron para el canal.
     */
    private TipoPizarron tipoPizarron;
    
    /**
     * El tipo de c&aacute;lculo de utilidades
     * CA - Usuario que captura
     * DU - Duenio del contrato
     */
    private String tipoCalculo = TIPO_CALCULO_DUENIO;
    
    /**
     * El c&oacute;digo postal asociado al canal.
     */
    private String codigoPostal;
    
    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -6974507896693122935L;
	
    /**
     * Constante para tipo de calculo al usuario que captura el Deal
     */
    public static String TIPO_CALCULO_CAPTURA = "CA";
    
    /**
     * Constante para tipo de calculo al usuario duenio del contrato
     */
    public static String TIPO_CALCULO_DUENIO = "DU";


	
}
