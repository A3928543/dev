package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Clase persistente para la tabla SC_LB_MUNICIPIO_001.
 *
 * @hibernate.class table="SC_LB_MUNICIPIO_001"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.MunicipioListaBlanca"
 * dynamic-update="true"
 *
 * @author lvillegas
 * @version $Revision: 1.1.2.1 $ $Date: 2010/10/08 01:30:18 $
 */
public class MunicipioListaBlanca implements Serializable {
    /**
     * Constructor por default.
     */
    public MunicipioListaBlanca() {
        super();
    }
    
    /**
     * Constructor utilizando par&aacute;metros.
     * @param id id.
     * @param zonaFronteriza si es zona fronteriza.
     * @param zonaTuristica si es zona turistica.
     * @param fechaCreacion fecha de creacion.
     * @param fechaUltimaModificacion fecha de ultima modificacion.
     * @param usuarioModificacion usuario de ultima modificacion.
     */
    public MunicipioListaBlanca(MunicipioListaBlancaPK id, Boolean zonaFronteriza,
            Boolean zonaTuristica, Date fechaCreacion, Date fechaUltimaModificacion,
            Integer usuarioModificacion) {
        super();
        this.id = id;
        this.zonaFronteriza = zonaFronteriza;
        this.zonaTuristica = zonaTuristica;
        this.fechaCreacion = fechaCreacion;
        this.fechaUltimaModificacion = fechaUltimaModificacion;
        this.usuarioModificacion = usuarioModificacion;
    }

    /**
     * @hibernate.property column="FECHA_CREACION"
     * not-null="true"
     * unique="false"
     * @return the fechaCreacion
     */
    public final Date getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * @param fechaCreacion the fechaCreacion to set
     */
    public final void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * @hibernate.property column="FECHA_ULTIMA_MOD"
     * not-null="true"
     * unique="false"
     * @return the fechaUltimaModificacion
     */
    public final Date getFechaUltimaModificacion() {
        return fechaUltimaModificacion;
    }

    /**
     * @param fechaUltimaModificacion the fechaUltimaModificacion to set
     */
    public final void setFechaUltimaModificacion(Date fechaUltimaModificacion) {
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }

    /**
     * @hibernate.id
     * @return the id
     */
    public final MunicipioListaBlancaPK getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public final void setId(MunicipioListaBlancaPK id) {
        this.id = id;
    }

    /**
     * @hibernate.property column="ID_USUARIO_MOD"
     * not-null="false"
     * unique="false"
     * @return the usuarioModificacion
     */
    public final Integer getUsuarioModificacion() {
        return usuarioModificacion;
    }

    /**
     * @param usuarioModificacion the usuarioModificacion to set
     */
    public final void setUsuarioModificacion(Integer usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    /**
     * @hibernate.property column="ES_RF"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     * @return Boolean.
     */
    public final Boolean getZonaFronteriza() {
        return zonaFronteriza;
    }

    /**
     * @param zonaFronteriza the zonaFronteriza to set
     */
    public final void setZonaFronteriza(Boolean zonaFronteriza) {
        this.zonaFronteriza = zonaFronteriza;
    }

    /**
     * @hibernate.property column="ES_ZT"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="false"
     * unique="false"
     * update="true"
     * insert="true"
     * @return Boolean.
     */
    public final Boolean getZonaTuristica() {
        return zonaTuristica;
    }

    /**
     * @param zonaTuristica the zonaTuristica to set
     */
    public final void setZonaTuristica(Boolean zonaTuristica) {
        this.zonaTuristica = zonaTuristica;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @return boolean.
     * @see java.lang.Object#equals(java.lang.Object).
     */
    public boolean equals(Object other) {
        if (!(other instanceof MunicipioListaBlanca)) {
            return false;
        }
        MunicipioListaBlanca castOther = (MunicipioListaBlanca) other;
        return new EqualsBuilder().append(this.getId(),
                castOther.getId()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @return <code>int</code>
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Regresa una cadena con la descripci&oacute;n de los campos de esta instancia.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("zonaFronteriza", zonaFronteriza)
                .append("zonaTuristica", zonaTuristica)
                .append("fechaCreacion", fechaCreacion)
                .append("fechaUltimaModificacion", fechaUltimaModificacion)
                .append("usuarioModificacion", usuarioModificacion)
                .toString();
    }
    
    /**
     * Id.
     */
    private MunicipioListaBlancaPK id;
    
    /**
     * Determina si el c&oacute;digo postal es una zona fronteriza.
     */
    private Boolean zonaFronteriza;
    
    /**
     * Determina si el c&oacute;digo postal es una zona tur&iacute;stica.
     */
    private Boolean zonaTuristica;
    
    /**
     * Fecha de creaci&oacute;n del registro.
     */
    private Date fechaCreacion;
    
    /**
     * Fecha de modificaci&oacute;n del registro.
     */
    private Date fechaUltimaModificacion;
    
    /**
     * Usuario que hizo la &uacute;ltima modificaci&oacute;n. 
     */
    private Integer usuarioModificacion;

    /**
     * Serializador.
     */
    private static final long serialVersionUID = -2385466131256771260L;
}
